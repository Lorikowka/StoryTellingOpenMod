package com.storyteam.storytelling.script;

import com.google.common.base.Preconditions;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.api.Script;
import com.storyteam.storytelling.api.Trigger;
import com.storyteam.storytelling.api.events.EventHandler;
import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.client.content.overlays.Message;
import com.storyteam.storytelling.network.StoryServerHooks;
import com.storyteam.storytelling.network.utils.WebMessageHandler;
import com.storyteam.storytelling.script.data.ScriptFlagStorage;
import com.storyteam.storytelling.script.performer.Performer;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.builder.ScriptPerformerBuilder;
import com.storyteam.storytelling.script.performer.entity.EntityControlPriority;
import com.storyteam.storytelling.script.performer.entity.EntityStatesRepository;
import com.storyteam.storytelling.script.story.ScriptEnvironment;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.util.NBTUtils;
import com.storyteam.storytelling.util.ScriptUtils;
import com.storyteam.storytelling.util.StoryTicker;
import com.storyteam.storytelling.util.ToStringBuilder;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;
import lombok.Generated;
import net.minecraft.commands.CommandRuntimeException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.api.io.IAutoNBTSerializable;
import org.zeith.hammerlib.api.io.NBTSerializable;
import org.zeith.hammerlib.util.java.Cast;

public class ScriptInstance implements IAutoNBTSerializable {
   @NBTSerializable
   final EntityStatesRepository repository = new EntityStatesRepository(this, EntityControlPriority.HIGHEST);
   private final Map<String, StoryTicker> POST_TICKERS = new HashMap<>();
   private final Map<String, ScriptPerformer> PARALLEL_PERFORMERS = new HashMap<>();
   private final List<String> STOPPED_PARALLEL_PERFORMERS = new ArrayList<>();
   private static final String MAIN_PERFORMER_RESERVED_NAME = "main";
   private final HashMap<String, Integer> DIALOG_ANSWER = new HashMap<>();
   private final List<Message> CHAT_LOG = new ArrayList<>();
   final int id;
   @NBTSerializable
   public boolean valid = true;
   @NBTSerializable
   public long ticks;
   @NBTSerializable
   public long stageTicks;
   @NBTSerializable
   public final ScriptFlagStorage flags = new ScriptFlagStorage();
   @NBTSerializable
   public boolean isPaused = false;
   public final ScriptEnvironment env;
   protected final ScriptingContext ctx;
   private ScriptPerformer mainPerformer;
   public final HashMap<String, ScriptPerformer> performers = new HashMap<>();
   private final RandomSource randomSource;
   private final Random random;
   private final RandomSource seededRandomSource;
   private final Random seededRandom;
   private double lastReportedProgress = 0.0;
   boolean registered;
   private final Map<EventType<?>, EventHandler<?>> eventHandlers = new HashMap<>();

   public ScriptInstance(ScriptEnvironment env, int id) {
      this.env = env;
      this.id = id;
      this.ctx = new ScriptingContext(env, this);
      this.random = new Random();
      this.randomSource = RandomSource.create();
      this.seededRandom = new Random((long)id);
      this.seededRandomSource = RandomSource.create((long)id);
   }

   @Nullable
   public ScriptPerformer getMainPerformer() {
      return this.mainPerformer;
   }

   @Nullable
   public ScriptPerformer getPerformer(String name) {
      return this.performers.get(name);
   }

   public Collection<ScriptPerformer> getPerformers() {
      return this.performers.values();
   }

   public Collection<ScriptPerformer> getParallelPerformers() {
      return this.PARALLEL_PERFORMERS.values();
   }

   public Stream<ScriptPerformer> allPerformers() {
      return Stream.concat(this.getPerformers().stream(), this.getParallelPerformers().stream());
   }

   public ScriptPerformer registerMainPerformer(ScriptPerformer performer) {
      Preconditions.checkArgument(this.getMainPerformer() == null, "Main performer is already registered in script " + this.id + ".");
      performer.setName(this.getClass().getSimpleName().toLowerCase(Locale.ROOT) + "_main");
      this.mainPerformer = performer;
      return this.registerPerformer("main", performer);
   }

   public ScriptPerformer registerSecondaryPerformer(String name, ScriptPerformer performer) {
      return this.registerPerformer(name, performer);
   }

   public ScriptPerformer registerPerformer(String name, ScriptPerformer performer) {
      Preconditions.checkArgument(!this.performers.containsKey(name), "Performer with name \"" + name + "\" already registered in script " + this.id + ".");
      this.performers.put(name, performer);
      return performer;
   }

   public final void update() {
      if (!this.registered) {
         this.registered = true;
         MinecraftForge.EVENT_BUS.register(this);
      }

      this.ticks++;
      this.stageTicks++;
      this.passEvent2Stage(EventType.UPDATE, this.env.server);
   }

   public final void tick(ServerPlayer player) {
      if (!this.isPaused) {
         ScriptPerformer performer = this.getMainPerformer();
         if (performer != null) {
            for (Entry<Integer, String> entry : performer.getHelpReports().entrySet()) {
               WebMessageHandler.sendInstructionLine(performer.getName() + "_" + entry.getKey(), this.id, entry.getValue(), true);
            }
         }

         this.passEvent2Stage(EventType.PLAYER_UPDATE, player);
         this.performers.values().forEach(Performer::tick);
         this.processParallelPerformers();
         this.POST_TICKERS.forEach((id, ticker) -> ticker.tick(player));
         this.updateAsPlayer(player);
         double percent = 0.0;
         if (this.mainPerformer != null) {
            percent = (double)this.mainPerformer.getActionIndex() / (double)this.mainPerformer.getActionsAmount() * 100.0;
         }

         if (Double.isNaN(percent)) {
            percent = 0.0;
         }

         if (percent != this.lastReportedProgress) {
            StoryServerHooks.updateScript(this, true);
            this.lastReportedProgress = percent;
         }
      }
   }

   public void updateAsPlayer(ServerPlayer player) {
   }

   private void processParallelPerformers() {
      List<String> toRemove = new ArrayList<>();

      for (Entry<String, ScriptPerformer> entry : this.PARALLEL_PERFORMERS.entrySet()) {
         ScriptPerformer p = entry.getValue();
         if (!this.STOPPED_PARALLEL_PERFORMERS.contains(entry.getKey())) {
            p.tick();
         }

         if (p.isCompleted()) {
            toRemove.add(entry.getKey());
            this.STOPPED_PARALLEL_PERFORMERS.remove(entry.getKey());
         }
      }

      toRemove.forEach(this.PARALLEL_PERFORMERS::remove);
   }

   public EntityStatesRepository getRepository() {
      return this.repository;
   }

   public ScriptFlagStorage getFlags() {
      return this.flags;
   }

   public ScriptingContext getContext() {
      return this.ctx;
   }

   public final void onInstanceCreated() {
      for (ScriptPerformer performer : this.performers.values()) {
         performer.onScriptInstanceCreated();
      }

      if (this.mainPerformer != null) {
         for (ScriptPerformer performer : this.performers.values()) {
            performer.setStateRepository(this.mainPerformer.getStateRepository());
         }

         NBTUtils.deserializeScriptGlobally(this.id, this);
      }
   }

   public final void onInstanceDestroyed() {
      MinecraftForge.EVENT_BUS.unregister(this);
      this.performers.values().forEach(Performer::onScriptInstanceDestroyed);
      this.PARALLEL_PERFORMERS.values().forEach(Performer::onScriptInstanceDestroyed);
      NBTUtils.serializeScriptGlobally(this.id, this);
   }

   public final void stop(boolean invalidate) {
      if (this.valid) {
         StoryServerHooks.stopScript(this);
      }

      this.valid = false;
      this.ticks = 0L;
      if (invalidate) {
         this.clearAllTickers();
         this.DIALOG_ANSWER.clear();
         this.env.server.postStoryEvent(EventType.STOP, this);
         this.env.server.postStoryEvent(EventType.STAGE_END, this.env.server);
         MinecraftForge.EVENT_BUS.unregister(this);
         this.PARALLEL_PERFORMERS.values().forEach(Performer::onStop);
         this.performers.values().forEach(Performer::onStop);
         this.PARALLEL_PERFORMERS.clear();
      }

      this.handleStop();
      this.repository.removeAll();
      NBTUtils.serializeScriptGlobally(this.id, this);
      StoryServerHooks.updateScript(this, false, 0.0);
      StoryServerHooks.clearInstruction(this);
   }

   public final void start() {
      this.env.server.postStoryEvent(EventType.START, this);
      this.env.server.postStoryEvent(EventType.STAGE_START, this);
      WebMessageHandler.clearNotices(this.id);
      this.performers.values().forEach(Performer::onStart);
      NBTUtils.deserializeScriptGlobally(this.id, this);
      this.handleStart();
      StoryServerHooks.updateScript(this, true);
      StoryServerHooks.startScript(this);
   }

   public void pause() {
      this.isPaused = true;
      StoryServerHooks.updateScript(this, true);
   }

   public void resume() {
      this.isPaused = false;
      StoryServerHooks.updateScript(this, true);
   }

   public boolean isPaused() {
      return this.isPaused;
   }

   public void setPaused(boolean paused) {
      this.isPaused = paused;
      StoryServerHooks.updateScript(this, true);
   }

   public int getDialogAnswer(String dialogueId) {
      return this.DIALOG_ANSWER.getOrDefault(dialogueId, -1);
   }

   public void addDialogAnswer(String dialogueId, int answerId) {
      this.DIALOG_ANSWER.put(dialogueId, answerId);
   }

   public void addMessageToChatlog(Message message) {
      this.CHAT_LOG.add(message);
      this.env.addChatMessage(message);
   }

   public List<Message> getChatlog() {
      return this.CHAT_LOG;
   }

   public ScriptPerformer handleSetup() {
      return ScriptPerformerBuilder.builder(this).build();
   }

   public void handleStart() {
   }

   public void handleStop() {
   }

   public final String getDescription() {
      return Optional.ofNullable(this.getClass().getAnnotation(Script.class)).map(Script::description).orElse("");
   }

   public boolean isInvalid() {
      return !this.valid;
   }

   public CompoundTag serializeNBT() {
      return IAutoNBTSerializable.super.serializeNBT();
   }

   public void deserializeNBT(CompoundTag nbt) {
      IAutoNBTSerializable.super.deserializeNBT(nbt);
   }

   public final <T> void handleEvent(EventType<T> type, Consumer<T> handler) {
      EventHandler<T> h = (EventHandler<T>)Cast.cast(this.eventHandlers.computeIfAbsent(type, EventHandler::new));
      h.add(handler);
   }

   public <T> void passEvent2Stage(EventType<T> type, T event) {
      if (this.eventHandlers.containsKey(type)) {
         EventHandler<T> handler = (EventHandler<T>)Cast.cast(this.eventHandlers.get(type));
         if (handler != null) {
            handler.accept(event);
         }
      }

      this.performers.values().forEach(performer -> performer.handleEvent(type, event));
      this.PARALLEL_PERFORMERS.forEach((id, perf) -> perf.handleEvent(type, event));
   }

   protected Map<String, Consumer<Player>> getTriggers() {
      return Map.of();
   }

   public static List<ScriptInstance.ScriptSubcommand> gatherSubcommands(ScriptInstance script, ScriptEnvironment env) {
      try {
         Class<? extends ScriptInstance> type = (Class<? extends ScriptInstance>)script.getClass();
         if (type == null) {
            return Collections.emptyList();
         } else {
            List<ScriptInstance.ScriptSubcommand> subs = new ArrayList<>();

            @FunctionalInterface
            interface PlayerAcceptor {
               void accept(Player var1);
            }

            for (Method method : type.getDeclaredMethods()) {
               Trigger a = method.getAnnotation(Trigger.class);
               if (a != null) {
                  subs.add(new ScriptInstance.ScriptSubcommand(a.value(), () -> env.getScript(script.id), method));
               }
            }

            for (Entry<String, Consumer<Player>> e : env.optionalScript(script.id).map(ScriptInstance::getTriggers).orElse(Map.of()).entrySet()) {
               Consumer<Player> value = e.getValue();
               PlayerAcceptor pa = value::accept;
               subs.add(new ScriptInstance.ScriptSubcommand(e.getKey(), Cast.constant(pa), PlayerAcceptor.class.getDeclaredMethod("accept", Player.class)));
            }

            return subs;
         }
      } catch (ReflectiveOperationException e) {
         throw new IllegalStateException("Unable to prepare script commands", e);
      }
   }

   public void runCommand(String cmd) {
      MinecraftServer mcs = (MinecraftServer)this.ctx.server;
      mcs.getCommands().performPrefixedCommand(mcs.createCommandSourceStack().withLevel(ScriptUtils.getLevel()).withSuppressedOutput(), cmd);
      Storytelling.LOG.info("[" + this.getClass().getSimpleName() + "] Executed /" + cmd);
   }

   @Nullable
   public <T extends Entity> T getEntity(Character<T> character) {
      return character == null ? null : this.env.server.getEntityRepository().getEntity(character);
   }

   public <T extends Entity> Optional<T> optional(Character<T> character) {
      return this.env.server.getEntityRepository().optional(character);
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this).append("mainPerformer", this.mainPerformer).build();
   }

   public void setPriority(EntityControlPriority priority) {
      this.repository.setPriority(priority);
   }

   public void addTicker(String id, StoryTicker ticker) {
      this.POST_TICKERS.put(id, ticker);
   }

   public void addParallelPerformer(String id, ScriptPerformer performer) {
      ScriptPerformer main = this.getMainPerformer();
      Objects.requireNonNull(
         main, "Parallel performer can't be created in case of absence of the main performer. Specify acting performer in overloading method."
      );
      this.addParallelPerformer(id, performer, main);
   }

   public void addParallelPerformer(String id, ScriptPerformer performer, @NotNull ScriptPerformer currentPerformer) {
      performer.setName(id);
      performer.onScriptInstanceCreated();
      performer.inheritFrom(currentPerformer);
      performer.onStart();
      this.PARALLEL_PERFORMERS.put(id, performer);
   }

   public void stopParallelPerformer(String id) {
      this.STOPPED_PARALLEL_PERFORMERS.add(id);
   }

   public void resumeParallelPerformer(String id) {
      this.STOPPED_PARALLEL_PERFORMERS.remove(id);
   }

   public void removeTicker(String id) {
      this.POST_TICKERS.remove(id);
   }

   public void deleteParallelPerformer(String id) {
      this.PARALLEL_PERFORMERS.remove(id);
   }

   public ScriptPerformer getParallelPerformer(String id) {
      return this.PARALLEL_PERFORMERS.get(id);
   }

   public void clearAllTickers() {
      this.POST_TICKERS.clear();
   }

   public Consumer<Boolean> createFlag(String flag) {
      return after -> {
         if (after) {
            this.flags.set(flag);
         } else {
            this.flags.remove(flag);
         }
      };
   }

   public Consumer<Boolean> createFlag(String flag, String value) {
      return after -> {
         if (after) {
            this.flags.set(flag, value);
         } else {
            this.flags.remove(flag);
         }
      };
   }

   public Consumer<Boolean> clearFlag(String flag) {
      return after -> {
         if (after) {
            this.flags.remove(flag);
         } else {
            this.flags.set(flag);
         }
      };
   }

   public RegistryAccess registryAccess() {
      return ((MinecraftServer)this.env.server).registryAccess();
   }

   @Generated
   public int getId() {
      return this.id;
   }

   @Generated
   public RandomSource getRandomSource() {
      return this.randomSource;
   }

   @Generated
   public Random getRandom() {
      return this.random;
   }

   @Generated
   public RandomSource getSeededRandomSource() {
      return this.seededRandomSource;
   }

   @Generated
   public Random getSeededRandom() {
      return this.seededRandom;
   }

   @Retention(RetentionPolicy.RUNTIME)
   @Target({ElementType.FIELD})
   public @interface GlobalSerializable {
      String value() default "";
   }

   public static class ScriptSubcommand implements Command<CommandSourceStack> {
      final String name;
      final Method method;
      final Supplier<?> instance;

      public ScriptSubcommand(String name, Supplier<?> instance, Method method) {
         this.name = name;
         this.instance = instance;
         this.method = method;
      }

      public String getName() {
         return this.name;
      }

      private Object[] fitArguments(CommandContext<CommandSourceStack> ctx) {
         Class<?>[] types = this.method.getParameterTypes();
         if (this.method.getParameterCount() == 1) {
            if (Player.class.isAssignableFrom(types[0])) {
               return new Object[]{((CommandSourceStack)ctx.getSource()).getPlayer()};
            }

            if (CommandContext.class.isAssignableFrom(types[0])) {
               return new Object[]{ctx};
            }
         }

         return new Object[0];
      }

      public int run(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException, CommandRuntimeException {
         this.method.setAccessible(true);

         try {
            boolean statik = Modifier.isStatic(this.method.getModifiers());
            Object inst = statik ? null : this.instance.get();
            if (!statik && inst == null) {
               throw new RuntimeException("Скрипт не запущен.");
            } else {
               this.method.invoke(inst, this.fitArguments(ctx));
               return 1;
            }
         } catch (Throwable var4) {
            Throwable throwable = var4;
            var4.printStackTrace();
            if (var4.getCause() != null) {
               throwable = var4.getCause();
            }

            throw new CommandRuntimeException(Component.literal(throwable.toString()));
         }
      }
   }
}
