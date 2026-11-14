package com.storyteam.storytelling.script.story;

import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.client.content.overlays.Message;
import com.storyteam.storytelling.api.Script;
import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.events.listeners.ScriptIO;
import com.storyteam.storytelling.init.CommandsST;
import com.storyteam.storytelling.logging.LoggingController;
import com.storyteam.storytelling.script.ScriptInstance;
import com.storyteam.storytelling.script.data.ScriptFlagStorage;
import com.storyteam.storytelling.script.performer.Performer;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.action.PerformerAction;
import com.storyteam.storytelling.util.Title;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap.Entry;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import java.lang.reflect.Constructor;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.HashSet;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.ChatFormatting;
import net.minecraft.CrashReport;
import net.minecraft.ReportedException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.mcf.ScanDataHelper;

public class ScriptEnvironment {
   public final IStoryServer server;
   public final Int2ObjectArrayMap<ScriptPerformer> scriptSetups = new Int2ObjectArrayMap();
   public final Int2ObjectArrayMap<ScriptInstance> scriptInstances = new Int2ObjectArrayMap();
   public final Int2ObjectArrayMap<Constructor<? extends ScriptInstance>> constructors = new Int2ObjectArrayMap();
   public final Int2ObjectArrayMap<LuaStory> luaStories = new Int2ObjectArrayMap();
   private final List<Message> chatHistory = new ArrayList<>();
   private final ScriptFlagStorage worldVariables = new ScriptFlagStorage();
   private final Map<UUID, ScriptFlagStorage> playerVariables = new HashMap<>();
   private final Set<Integer> incompatibleLuaSaves = new HashSet<>();

   public boolean hasIncompatibleLuaSave(int id) {
      return incompatibleLuaSaves.contains(id);
   }

   public void setVariable(String scope, String name, String value, ServerPlayer player) {
      if (scope.equals("world")) worldVariables.set(name, value);
      else if (scope.equals("player") && player != null) {
         playerVariables.computeIfAbsent(player.getUUID(), ignored -> new ScriptFlagStorage()).set(name, value);
      } else throw new IllegalArgumentException("Invalid variable scope or missing player: " + scope);
   }

   public String getVariable(String scope, String name, ServerPlayer player) {
      if (scope.equals("world")) return worldVariables.get(name);
      if (scope.equals("player") && player != null) {
         ScriptFlagStorage values = playerVariables.get(player.getUUID());
         return values == null ? null : values.get(name);
      }
      return null;
   }
   public boolean hasInit;
   File loadDir;

   public ScriptEnvironment(IStoryServer server) {
      this.server = server;
   }

   public void addChatMessage(Message message) {
      this.chatHistory.add(message);
   }

   public List<Message> getChatHistory() {
      return List.copyOf(this.chatHistory);
   }

   public CompoundTag serializeChatHistory() {
      CompoundTag root = new CompoundTag();
      ListTag messages = new ListTag();
      for (Message message : this.chatHistory) {
         CompoundTag entry = new CompoundTag();
         entry.putString("speaker", message.speaker());
         entry.putString("speakerName", Component.Serializer.toJson(message.speakerName()));
         entry.putString("flavor", Component.Serializer.toJson(message.flavor()));
         entry.putInt("inTime", message.inTime());
         entry.putInt("time", message.time());
         entry.putInt("outTime", message.outTime());
         entry.putString("message", Component.Serializer.toJson(message.message()));
         messages.add(entry);
      }
      root.put("messages", messages);
      return root;
   }

   public void deserializeChatHistory(CompoundTag root) {
      this.chatHistory.clear();
      ListTag messages = root.getList("messages", Tag.TAG_COMPOUND);
      for (int i = 0; i < messages.size(); i++) {
         CompoundTag entry = messages.getCompound(i);
         try {
            Component speakerName = Component.Serializer.fromJson(entry.getString("speakerName"));
            Component flavor = Component.Serializer.fromJson(entry.getString("flavor"));
            Component text = Component.Serializer.fromJson(entry.getString("message"));
            if (speakerName != null && flavor != null && text != null) {
               this.chatHistory.add(new Message(entry.getString("speaker"), speakerName, flavor,
                  entry.getInt("inTime"), entry.getInt("time"), entry.getInt("outTime"), text));
            }
         } catch (RuntimeException e) {
            Storytelling.LOG.warn("Skipping invalid story chat history entry {}", i, e);
         }
      }
   }

   public void tick() {
      Int2ObjectArrayMap<ScriptInstance> copy = new Int2ObjectArrayMap();
      copy.putAll(this.scriptInstances);
      ObjectIterator<ScriptInstance> iterator = copy.values().iterator();
      if (!this.hasInit) {
         MinecraftServer mcs = (MinecraftServer)Cast.cast(this.server);
         CommandsST.register(mcs, mcs.getCommands().getDispatcher(), CommandBuildContext.simple(mcs.registryAccess(), mcs.overworld().enabledFeatures()));
         this.hasInit = true;
      }

      this.processLoadHook();

      while (iterator.hasNext()) {
         ScriptInstance value = (ScriptInstance)iterator.next();
         boolean shouldRemove = false;
         if (!this.scriptSetups.containsKey(value.getId())) {
            if (!value.isInvalid()) {
               try {
                  value.update();
               } catch (Throwable var10) {
                  String message = String.format("An error has occurred while updating %s script.", value.getId());
                  var10.printStackTrace();
                  RuntimeException exception = new RuntimeException(message, var10);
                  var10.printStackTrace();
                  exception.printStackTrace();
                  LocalPlayer local = Minecraft.getInstance().player;
                  if (local != null) {
                     local.sendSystemMessage(Component.literal(message).withStyle(ChatFormatting.RED));
                  }

                  value.stop(false);
                  shouldRemove = true;
               }
            }

            if (value.isInvalid()) {
               try {
                  value.stop(true);
               } catch (Throwable var9) {
                  throw new ReportedException(new CrashReport("Stopping script " + value.getClass().getName(), var9));
               }

               shouldRemove = true;
            }

            if (shouldRemove) {
               iterator.remove();
            }
         }
      }
   }

   public <T> void postEventToInstances(EventType<T> type, T event) {
      ObjectIterator var3 = this.scriptInstances.values().iterator();

      while (var3.hasNext()) {
         ScriptInstance instance = (ScriptInstance)var3.next();
         instance.passEvent2Stage(type, event);
      }
   }

   public <T> boolean postCancellableEventToInstances(EventType<T> type, T event, Predicate<T> breakCondition) {
      ObjectIterator var4 = this.scriptInstances.values().iterator();

      while (var4.hasNext()) {
         ScriptInstance instance = (ScriptInstance)var4.next();
         instance.passEvent2Stage(type, event);
         if (breakCondition.test(event)) {
            return true;
         }
      }

      return false;
   }

   public final void tickPlayer(ServerPlayer player) {
      int toRemove = 0;
      ObjectIterator<Entry<ScriptPerformer>> setupIterator = this.scriptSetups.int2ObjectEntrySet().iterator();
      if (setupIterator.hasNext()) {
         Entry<ScriptPerformer> entry = setupIterator.next();
         int id = entry.getIntKey();
         ScriptPerformer performer = (ScriptPerformer)entry.getValue();
         if (performer.isCompleted()) {
            toRemove = id;
         } else {
            performer.tick();
         }

         try {
            float percentage = (float)performer.getActionIndex() / ((float)performer.getActionsAmount() / 100.0F);
            Title.builder("§6" + new DecimalFormat("#.#").format((double)percentage) + "%", "§aИнициализация скрипта §e#" + id + "§a...")
               .times(0, 5, 5)
               .send(player);
         } catch (Exception var10) {
            var10.printStackTrace();
         }
      }

      if (toRemove != 0) {
         this.scriptSetups.remove(toRemove);
      }

      ObjectIterator<ScriptInstance> iterator = this.scriptInstances.values().iterator();

      scripts: while (true) {
         ScriptInstance value;
         while (true) {
            if (!iterator.hasNext()) {
               return;
            }

            value = (ScriptInstance)iterator.next();
            if (!this.scriptSetups.containsKey(value.getId())) {
               if (value.isInvalid()) {
                  break;
               }

               try {
                  value.tick(player);
                  if (value instanceof LuaScriptInstance && value.getMainPerformer() != null && value.getMainPerformer().isCompleted()) {
                     value.stop(true);
                     iterator.remove();
                     continue scripts;
                  }
                  break;
               } catch (Throwable var11) {
                  String message = String.format("An error has occurred while updating %s script.", value.getId());
                  var11.printStackTrace();
                  RuntimeException exception = new RuntimeException(message, var11);
                  var11.printStackTrace();
                  exception.printStackTrace();
                  LocalPlayer local = Minecraft.getInstance().player;
                  if (local != null) {
                     local.sendSystemMessage(Component.literal(message).withStyle(ChatFormatting.RED));
                  }

                  value.stop(false);
                  iterator.remove();
               }
            }
         }

         if (value.isInvalid()) {
            try {
               value.stop(true);
            } catch (Throwable var9) {
               new ReportedException(new CrashReport("Stopping script " + value.getClass().getName(), var9)).printStackTrace();
            }

            iterator.remove();
         }
      }
   }

   public void loadScripts() {
      if (FMLEnvironment.dist != Dist.DEDICATED_SERVER) {
         Storytelling.LOG.info("Loading scripts...");
         this.constructors.clear();
         this.luaStories.clear();
         Int2ObjectMap<Class<? extends ScriptInstance>> mapped = new Int2ObjectOpenHashMap();

         for (Class<? extends ScriptInstance> type : ScanDataHelper.lookupAnnotatedTypes(Script.class, ScriptInstance.class)) {
            int id = type.getAnnotation(Script.class).value();
            if (mapped.containsKey(id)) {
               throw new RuntimeException("Duplicate script id: " + id + ". Conflicting types: " + mapped.get(id) + " and " + type);
            }

            mapped.put(id, type);
            Constructor<? extends ScriptInstance> constructor = null;

            try {
               constructor = type.getConstructor(ScriptEnvironment.class, int.class);
               constructor.setAccessible(true);
            } catch (Exception var7) {
               Storytelling.LOG.warn("Failed to load script with id " + id + "!");
            }

            if (this.constructors.containsKey(id)) {
               Storytelling.LOG.warn("Script ID " + id + " is already taken!");
            } else if (constructor != null) {
               this.constructors.put(id, constructor);
            }
         }

         this.registerLuaStories(LuaStory.storyDirectory());
         Storytelling.LOG.info("Scripts loaded. Registered " + this.constructors.size() + " scripts (" + this.luaStories.size() + " Lua).");
      }
   }

   /** Registers new story IDs from Lua files while preserving existing Java script IDs. */
   public void registerLuaStories(Path storyDirectory) {
      // Existing Java scripts keep their IDs; only new IDs are registered from Lua files.
      try {
         LuaStory.initializeStoryDirectory(storyDirectory);
      } catch (IOException e) {
         Storytelling.LOG.error("Cannot initialize Lua story directory {}", storyDirectory, e);
      }
      if (Files.isDirectory(storyDirectory)) {
         try (Stream<Path> files = Files.list(storyDirectory)) {
            for (Path file : files.filter(p -> p.getFileName().toString().matches("[0-9]+\\.lua")).sorted().toList()) {
               try {
                  LuaStory story = LuaStory.load(file);
                  String expectedName = story.id() + ".lua";
                  if (!file.getFileName().toString().equals(expectedName)) {
                     throw new IllegalArgumentException("Lua story filename must be " + expectedName);
                  }
                  if (this.constructors.containsKey(story.id())) {
                     Storytelling.LOG.warn("Lua story {} conflicts with an existing Java script ID; keeping Java script", file);
                     continue;
                  }
                  if (this.server != null) {
                     story.refreshNpcEntities(this.server.getEntityRepository());
                  }
                  this.luaStories.put(story.id(), story);
                  this.constructors.put(story.id(), LuaScriptInstance.class.getConstructor(ScriptEnvironment.class, int.class));
               } catch (Exception e) {
                  Storytelling.LOG.error("Cannot register Lua story {}", file, e);
               }
            }
         } catch (IOException e) {
            Storytelling.LOG.error("Cannot list Lua stories in {}", storyDirectory, e);
         }
      }
   }

   /** Replace one definition only after parsing and validating the complete file. */
   public int reloadLuaStory(String fileName) throws IOException, NoSuchMethodException {
      if (fileName == null || !fileName.matches("[1-9][0-9]{0,8}\\.lua")) {
         throw new IllegalArgumentException("Укажите имя вида 900030.lua без папок");
      }
      Path directory = LuaStory.storyDirectory().toAbsolutePath().normalize();
      Path file = directory.resolve(fileName).normalize();
      if (!file.getParent().equals(directory) || !Files.isRegularFile(file)
         || !file.toRealPath().getParent().equals(directory.toRealPath())) {
         throw new IllegalArgumentException("Lua-файл не найден внутри ST/scripts: " + fileName);
      }
      int id = Integer.parseInt(fileName.substring(0, fileName.length() - 4));
      LuaStory story = LuaStory.load(file);
      if (story.id() != id) {
         throw new IllegalArgumentException("ID story(" + story.id() + ") не совпадает с именем " + fileName);
      }
      if (constructors.containsKey(id) && !luaStories.containsKey(id)) {
         throw new IllegalArgumentException("ID " + id + " уже занят встроенным Java-сценарием");
      }
      if (this.server != null) story.refreshNpcEntities(this.server.getEntityRepository());
      var constructor = LuaScriptInstance.class.getConstructor(ScriptEnvironment.class, int.class);
      luaStories.put(id, story);
      constructors.put(id, constructor);
      Storytelling.LOG.info("Reloaded Lua story {} from {}", id, file);
      return id;
   }

   public void unloadScripts() {
      Storytelling.LOG.info("Unloading scripts...");
      ObjectIterator var1 = this.scriptInstances.values().iterator();

      while (var1.hasNext()) {
         ScriptInstance scriptInstance = (ScriptInstance)var1.next();
         scriptInstance.onInstanceDestroyed();
      }

      Storytelling.LOG.info("Scripts unloaded.");
   }

   public void start(int script) {
      ScriptInstance instance = (ScriptInstance)this.scriptInstances.get(script);
      if (instance != null) {
         instance.stop(true);
         this.scriptInstances.remove(script);
      }

      if (script > 0 && script < 200) {
         for (int i = 1; i < script; i++) {
            try {
               if (!this.constructors.containsKey(i)) {
                  Storytelling.LOG.info("Skipping missing script #{}", i);
               } else {
                  ScriptInstance prev = this.createInstance(i);
                  List<ScriptPerformer> perfs = new ArrayList<>();
                  ScriptPerformer setup = prev.handleSetup();
                  if (setup != null) {
                     perfs.add(setup);
                  }

                  perfs.add(prev.getMainPerformer());

                  for (ScriptPerformer performer : prev.getPerformers()) {
                     if (!perfs.contains(performer)) {
                        perfs.add(performer);
                     }
                  }

                  perfs.stream().map(Performer::getActions).flatMap(Collection::stream).forEach(PerformerAction::globalFastForward);
               }
            } catch (RuntimeException var9) {
               Storytelling.LOG.error("Failed to perform globalFastForward on script {}. The globalFastForward cycle will terminate.", i, var9);
               break;
            }
         }
      }

      instance = this.createInstance(script);
      if (instance != null) {
         this.incompatibleLuaSaves.remove(script);
         instance.onInstanceCreated();
         instance.start();
         this.scriptInstances.put(script, instance);
      }
   }

   public void stop(int script) {
      ScriptInstance instance = (ScriptInstance)this.scriptInstances.get(script);
      if (instance != null) {
         instance.stop(false);
      }
   }

   public void save(File dir) {
      File[] existing = dir.listFiles();
      List<File> all = new ArrayList<>(existing == null ? List.of() : Arrays.asList(existing));
      incompatibleLuaSaves.forEach(id -> all.remove(new File(dir, Integer.toString(id))));
      File variablesFile = new File(dir, "lua-variables.dat");
      all.remove(variablesFile);
      all.remove(new File(dir, "lua-variables.dat.old"));
      CompoundTag variables = new CompoundTag();
      variables.put("world", worldVariables.serializeNBT());
      CompoundTag players = new CompoundTag();
      playerVariables.forEach((uuid, values) -> players.put(uuid.toString(), values.serializeNBT()));
      variables.put("players", players);
      ScriptIO.save(variables, variablesFile);
      IntIterator var3 = this.scriptInstances.keySet().iterator();

      while (var3.hasNext()) {
         int id = (Integer)var3.next();
         File fl = new File(dir, Integer.toString(id));
         all.remove(fl);
         if (fl.isDirectory() || fl.mkdirs()) {
            ScriptInstance instance = (ScriptInstance)this.scriptInstances.get(id);
            CompoundTag saved = instance.serializeNBT();
            if (instance instanceof LuaScriptInstance lua) saved.putString("luaDefinitionHash", lua.definitionHash);
            ScriptIO.save(saved, new File(fl, "+.dat"));
         }
      }

      all.forEach(ScriptEnvironment::deleteCompletely);
   }

   public void load(File dir) {
      this.loadDir = dir;
   }

   public boolean hasPendingLoad() {
      return this.loadDir != null;
   }

   private static void deleteCompletely(File dir) {
      if (dir.isFile()) {
         dir.delete();
      }

      if (dir.isDirectory()) {
         for (File f : dir.listFiles()) {
            deleteCompletely(f);
         }

         dir.delete();
      }
   }

   @Nullable
   public ScriptInstance getScript(int id) {
      return (ScriptInstance)this.scriptInstances.get(id);
   }

   public Optional<ScriptInstance> optionalScript(int id) {
      return Optional.ofNullable((ScriptInstance)this.scriptInstances.get(id));
   }

   public <T extends ScriptInstance> Optional<T> optionalScript(int id, Class<T> clazz) {
      ScriptInstance script = this.getScript(id);
      return clazz.isInstance(script) ? Optional.ofNullable((T)Cast.cast(script)) : Optional.empty();
   }

   public static Function<ScriptEnvironment, Optional<ScriptInstance>> script(int id) {
      return e -> e.optionalScript(id);
   }

   public ScriptInstance createInstance(int id) {
      Constructor<? extends ScriptInstance> constructor = (Constructor<? extends ScriptInstance>)this.constructors.get(id);

      try {
         return constructor.newInstance(this, id);
      } catch (Exception var4) {
         var4.printStackTrace();
         throw new ReportedException(new CrashReport(String.format("Something goes wrong while %d script instance initialization.", id), var4));
      }
   }

   private void processLoadHook() {
      if (this.loadDir != null) {
         worldVariables.getFlags().clear();
         playerVariables.clear();
         CompoundTag variables = ScriptIO.read(new File(this.loadDir, "lua-variables.dat"));
         if (variables != null) {
            worldVariables.deserializeNBT(variables.getCompound("world"));
            CompoundTag players = variables.getCompound("players");
            for (String key : players.getAllKeys()) {
               try {
                  ScriptFlagStorage values = new ScriptFlagStorage();
                  values.deserializeNBT(players.getCompound(key));
                  playerVariables.put(UUID.fromString(key), values);
               } catch (IllegalArgumentException ignored) {
                  Storytelling.LOG.warn("Ignoring invalid player variable owner {}", key);
               }
            }
         }
         this.scriptInstances.clear();
         this.incompatibleLuaSaves.clear();
         IntIterator var1 = this.constructors.keySet().iterator();

         while (var1.hasNext()) {
            int id = (Integer)var1.next();
            File fl = new File(this.loadDir, Integer.toString(id));
            if (fl.isDirectory()) {
               CompoundTag read = ScriptIO.read(new File(fl, "+.dat"));
               if (read != null) {
                  if (this.luaStories.containsKey(id) && !read.getCompound("mainFlow").contains("actionIndex")) {
                     Storytelling.LOG.warn("Discarding legacy Lua script {} without saved action index to prevent replay", id);
                     continue;
                  }
                  LuaStory luaStory = this.luaStories.get(id);
                  if (luaStory != null && read.contains("luaDefinitionHash")
                     && !read.getString("luaDefinitionHash").equals(luaStory.fingerprint())) {
                     Storytelling.LOG.error("Lua script {} changed since this world was saved; refusing to resume at an incompatible action. Restore the previous Lua file or reset this script.", id);
                     this.incompatibleLuaSaves.add(id);
                     continue;
                  }
                  ScriptInstance i = this.createInstance(id);
                  i.onInstanceCreated();
                  i.deserializeNBT(read);
                  if (i instanceof LuaScriptInstance lua) lua.restoreNpcEntities();
                  this.scriptInstances.put(id, i);
               }
            }
         }

         LoggingController.captureLoad(this.scriptInstances);
         this.loadDir = null;
      }
   }
}
