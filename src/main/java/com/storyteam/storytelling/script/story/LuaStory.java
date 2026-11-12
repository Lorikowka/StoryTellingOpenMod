package com.storyteam.storytelling.script.story;

import com.storyteam.storytelling.script.performer.builder.ScriptPerformerBuilder;
import com.storyteam.storytelling.script.performer.action.WaitPlayerPositionAction;
import com.storyteam.storytelling.script.performer.action.behaviour.EntityPathfinderAction;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.script.story.npc.ScriptNpcEntity;
import com.storyteam.storytelling.script.story.npc.NpcWorldInteraction;
import com.storyteam.storytelling.script.story.EntityRepository;
import com.storyteam.storytelling.util.ScriptUtils;
import com.storyteam.storytelling.util.world.Location;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.LoadState;
import org.luaj.vm2.lib.BaseLib;
import org.luaj.vm2.lib.MathLib;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.PackageLib;
import org.luaj.vm2.lib.StringLib;
import org.luaj.vm2.lib.TableLib;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;
import org.luaj.vm2.lib.VarArgFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.registries.ForgeRegistries;

/** Loads a Lua story into the existing Java performer without exposing Minecraft internals to Lua. */
public final class LuaStory {
   private final int id;
   private final Map<String, List<Step>> parts;
   private final Map<String, NpcSpec> npcs;

   private LuaStory(int id, Map<String, List<Step>> parts, Map<String, NpcSpec> npcs) {
      this.id = id;
      this.parts = parts;
      this.npcs = npcs;
   }

   public int id() {
      return id;
   }

   public boolean hasPart(String name) {
      return parts.containsKey(name);
   }

   public Map<String, Character<?>> createNpcCharacters() {
      Map<String, Character<?>> characters = new LinkedHashMap<>();
      npcs.forEach((key, npc) -> {
         ResourceLocation typeId = ResourceLocation.tryParse(npc.type);
         EntityType<?> type = ForgeRegistries.ENTITY_TYPES.getValue(typeId);
         if (type == null) {
            throw new IllegalArgumentException("Unknown NPC entity type " + npc.type + " in Lua story " + id);
         }
         String referenceId = "lua_" + id + "_" + key;
         Character<?> character = Character.getById(referenceId);
         if (character == null) {
            if (npc.model != null) {
               @SuppressWarnings("unchecked") EntityType<ScriptNpcEntity> npcType = (EntityType<ScriptNpcEntity>) type;
               character = Character.builder(referenceId, npcType).name(npc.name)
                  .onCreatedFresh(entity -> {
                     entity.model(npc.model);
                     entity.hitbox(npc.width, npc.height);
                  }).build();
            } else {
               character = Character.builder(referenceId, type).name(npc.name).build();
            }
         } else if (character.getType() != type || !npc.name.equals(character.getName())) {
            throw new IllegalStateException("NPC " + referenceId + " changed type or name while a world is loaded");
         }
         if (npc.model != null) {
            character.onCreatedFresh.clear();
            character.onCreatedFresh.add(entity -> {
               if (entity instanceof ScriptNpcEntity scriptNpc) {
                  scriptNpc.model(npc.model);
                  scriptNpc.hitbox(npc.width, npc.height);
               }
            });
         }
         characters.put(key, character);
      });
      return Map.copyOf(characters);
   }

   public void refreshNpcEntities(EntityRepository repository) {
      Map<String, Character<?>> characters = createNpcCharacters();
      npcs.forEach((key, npc) -> {
         if (npc.model == null) return;
         Entity entity = repository.getEntity(characters.get(key));
         if (entity instanceof ScriptNpcEntity scriptNpc) {
            scriptNpc.model(npc.model);
            scriptNpc.hitbox(npc.width, npc.height);
         }
      });
   }

   public void appendTo(ScriptPerformerBuilder builder, String name) {
      appendTo(builder, name, createNpcCharacters());
   }

   public void appendTo(ScriptPerformerBuilder builder, String name, Map<String, Character<?>> characters) {
      List<Step> steps = parts.get(name);
      if (steps == null) {
         throw new IllegalArgumentException("Lua story " + id + " has no part " + name);
      }
      for (Step step : steps) {
         switch (step.type) {
            case SPEAKER -> {
               String icon = step.arguments == null ? null : step.arguments[0];
               if (icon == null) {
                  icon = npcs.values().stream()
                     .filter(npc -> npc.model != null && npc.name.equals(step.text))
                     .map(npc -> "glb:" + npc.model).findFirst().orElse(null);
               }
               if (icon == null) builder.thenSetSpeechEntity(step.speaker, step.text);
               else builder.thenSetSpeechEntity(step.speaker, step.text, icon);
            }
            case SPEECH -> builder.thenSpeechMessage(step.speaker, step.text);
            case WARNING -> builder.sendWebWarning(step.text);
            case ANCHOR -> builder.saveState(step.text);
            case WAIT_F8 -> builder.thenWaitForF8();
            case WAIT_TICKS -> builder.thenDelay(step.speaker);
            case WAIT_CHAT -> builder.thenWaitChatMessage(step.text);
            case WAIT_FLAG -> builder.thenWaitForFlag(step.text);
            case THINK -> builder.thenThink(step.text);
            case BLACKOUT -> builder.blackout(step.speaker);
            case FLASH -> builder.flash(step.speaker);
            case STORY_ENGINE -> builder.thenRun(() -> StoryEngineAdapter.invoke(step.operation, step.arguments));
            case WAIT_POS -> builder.append(p -> new WaitPlayerPositionAction(p, step.position.dimension, step.position.x,
               step.position.y, step.position.z, step.position.radius, step.position.ignoreY));
            case TELEPORT_PLAYER -> builder.teleportPlayer(() -> teleportLocation(step.arguments));
            case NPC_SPAWN -> {
               builder.positionEntity(characters.get(step.text), () -> npcLocation(step.position));
               NpcSpec spec = npcs.get(step.text);
               if (spec.model != null) {
                  builder.thenRun(() -> {
                     Entity entity = characters.get(step.text).repositoryEntity();
                     if (entity instanceof ScriptNpcEntity scriptNpc) {
                        scriptNpc.model(spec.model);
                        scriptNpc.hitbox(spec.width, spec.height);
                     }
                  });
               }
            }
            case NPC_MOVE -> appendNpcPath(builder, characters.get(step.text), step.position);
            case NPC_USE_BLOCK -> builder.thenRun(() -> NpcWorldInteraction.useBlock(
               characters.get(step.text).repositoryEntity(),
               new BlockPos((int) step.position.x, (int) step.position.y, (int) step.position.z),
               step.position.dimension));
            case NPC_INTERACT -> builder.thenAwaitEntityClick(characters.get(step.text));
            case NPC_REMOVE -> builder.removeEntity(characters.get(step.text));
            case NPC_ANIMATION -> builder.thenRun(() -> {
               Entity entity = characters.get(step.text).repositoryEntity();
               if (entity instanceof ScriptNpcEntity scriptNpc) scriptNpc.animation(step.arguments[0], step.arguments[1]);
            });
            case NPC_LOOK -> builder.thenRun(() -> {
               Entity entity = characters.get(step.text).repositoryEntity();
               if (entity instanceof ScriptNpcEntity scriptNpc) {
                  if (step.arguments[0].equals("player")) scriptNpc.lookAt(ScriptUtils.getPlayer());
                  else if (step.arguments[0].equals("none")) scriptNpc.clearLook();
                  else scriptNpc.lookAt(Double.parseDouble(step.arguments[0]),
                     Double.parseDouble(step.arguments[1]), Double.parseDouble(step.arguments[2]));
               }
            });
         }
      }
   }

   public static LuaStory loadBundled(String name) {
      if (name == null || !name.matches("[a-z0-9_]+")) {
         throw new IllegalArgumentException("Invalid Lua story name: " + name);
      }
      Path override = storyDirectory().resolve(name + ".lua");
      if (Files.isRegularFile(override)) {
         return load(override);
      }
      String resource = "/data/storytelling/story_scripts/" + name + ".lua";
      try (InputStream stream = LuaStory.class.getResourceAsStream(resource)) {
         if (stream == null) {
            throw new IOException("Missing bundled story " + resource);
         }
         return parse(new String(stream.readAllBytes(), StandardCharsets.UTF_8), resource);
      } catch (IOException e) {
         throw new IllegalStateException("Cannot load Lua story " + name, e);
      }
   }

   public static Path storyDirectory() {
      return StoryWorkspace.scripts();
   }

   /** Creates the authoring directory and installs editable examples once. */
   public static void initializeStoryDirectory(Path directory) throws IOException {
      Files.createDirectories(directory);
      installExample(directory, "s7.lua");
      installExample(directory, "900001.lua");
   }

   private static void installExample(Path directory, String name) throws IOException {
      Path target = directory.resolve(name);
      if (Files.exists(target)) {
         return;
      }
      try (InputStream source = LuaStory.class.getResourceAsStream("/data/storytelling/story_scripts/" + name)) {
         if (source == null) {
            throw new IOException("Missing bundled Lua example: " + name);
         }
         try {
            Files.copy(source, target);
         } catch (FileAlreadyExistsException ignored) {
            // Another loader created the file after the existence check.
         }
      }
   }

   public static LuaStory load(Path file) {
      try {
         return parse(Files.readString(file, StandardCharsets.UTF_8), file.toString());
      } catch (IOException e) {
         throw new IllegalStateException("Cannot read Lua story " + file, e);
      }
   }

   public static LuaStory parse(String source, String label) {
      Builder builder = new Builder(label);
      Globals globals = new Globals();
      globals.load(new BaseLib());
      globals.load(new PackageLib());
      globals.load(new TableLib());
      globals.load(new StringLib());
      globals.load(new MathLib());
      LoadState.install(globals);
      LuaC.install(globals);
      globals.set("require", LuaValue.NIL);
      globals.set("package", LuaValue.NIL);
      globals.set("dofile", LuaValue.NIL);
      globals.set("loadfile", LuaValue.NIL);
      globals.set("story", new OneArgFunction() {
         @Override public LuaValue call(LuaValue id) { builder.story(id.checkint()); return NIL; }
      });
      globals.set("part", new OneArgFunction() {
         @Override public LuaValue call(LuaValue name) { builder.part(name.checkjstring()); return NIL; }
      });
      globals.set("anchor", new OneArgFunction() {
         @Override public LuaValue call(LuaValue name) { builder.anchor(nonempty(name)); return NIL; }
      });
      globals.set("npc", new ThreeArgFunction() {
         @Override public LuaValue call(LuaValue key, LuaValue type, LuaValue name) {
            builder.npc(nonempty(key), nonempty(type), nonempty(name));
            return NIL;
         }
      });
      globals.set("npc_model", new ThreeArgFunction() {
         @Override public LuaValue call(LuaValue key, LuaValue model, LuaValue name) {
            builder.npcModel(nonempty(key), nonempty(model), nonempty(name));
            return NIL;
         }
      });
      globals.set("npc_hitbox", new ThreeArgFunction() {
         @Override public LuaValue call(LuaValue key, LuaValue width, LuaValue height) {
            builder.npcHitbox(nonempty(key), (float) width.checkdouble(), (float) height.checkdouble());
            return NIL;
         }
      });
      globals.set("npc_animation", new VarArgFunction() {
         @Override public Varargs invoke(Varargs args) {
            if (args.narg() != 2 && args.narg() != 3) {
               throw new IllegalArgumentException("npc_animation(key, clip, [mode]) expects 2 or 3 arguments");
            }
            String mode = args.narg() == 3 ? nonempty(args.arg(3)) : "loop";
            builder.npcAnimation(nonempty(args.arg(1)), nonempty(args.arg(2)), mode);
            return NIL;
         }
      });
      globals.set("npc_look", new VarArgFunction() {
         @Override public Varargs invoke(Varargs args) {
            if (args.narg() == 2) {
               builder.npcLook(nonempty(args.arg(1)), nonempty(args.arg(2)));
            } else if (args.narg() == 4) {
               double x = args.arg(2).checkdouble();
               double y = args.arg(3).checkdouble();
               double z = args.arg(4).checkdouble();
               if (!Double.isFinite(x) || !Double.isFinite(y) || !Double.isFinite(z)
                  || Math.abs(x) > 30000000 || Math.abs(y) > 30000000 || Math.abs(z) > 30000000) {
                  throw new IllegalArgumentException("Invalid npc_look coordinates");
               }
               builder.npcLook(nonempty(args.arg(1)), Double.toString(x), Double.toString(y), Double.toString(z));
            } else {
               throw new IllegalArgumentException("npc_look(key, 'player'|'none') or npc_look(key, x, y, z) expected");
            }
            return NIL;
         }
      });
      globals.set("npc_wait_interact", new OneArgFunction() {
         @Override public LuaValue call(LuaValue key) { builder.npcAction(Type.NPC_INTERACT, nonempty(key), null); return NIL; }
      });
      globals.set("npc_remove", new OneArgFunction() {
         @Override public LuaValue call(LuaValue key) { builder.npcAction(Type.NPC_REMOVE, nonempty(key), null); return NIL; }
      });
      globals.set("npc_spawn", new VarArgFunction() {
         @Override public Varargs invoke(Varargs args) {
            if (args.narg() != 4 && args.narg() != 5) {
               throw new IllegalArgumentException("npc_spawn(key, x, y, z, [dimension]) expects 4 or 5 arguments");
            }
            String key = nonempty(args.arg(1));
            double x = args.arg(2).checkdouble();
            double y = args.arg(3).checkdouble();
            double z = args.arg(4).checkdouble();
            String dimension = args.narg() == 5 ? nonempty(args.arg(5)) : "minecraft:overworld";
            if (!Double.isFinite(x) || !Double.isFinite(y) || !Double.isFinite(z) || ResourceLocation.tryParse(dimension) == null) {
               throw new IllegalArgumentException("Invalid npc_spawn coordinates or dimension");
            }
            builder.npcAction(Type.NPC_SPAWN, key, new PositionSpec(dimension, x, y, z, 0, false));
            return NIL;
         }
      });
      globals.set("npc_move", new VarArgFunction() {
         @Override public Varargs invoke(Varargs args) {
            if (args.narg() != 5 && args.narg() != 6) {
               throw new IllegalArgumentException("npc_move(key, x, y, z, speed, [dimension]) expects 5 or 6 arguments");
            }
            String key = nonempty(args.arg(1));
            double x = args.arg(2).checkdouble();
            double y = args.arg(3).checkdouble();
            double z = args.arg(4).checkdouble();
            double speed = args.arg(5).checkdouble();
            String dimension = args.narg() == 6 ? nonempty(args.arg(6)) : "minecraft:overworld";
            if (!Double.isFinite(x) || !Double.isFinite(y) || !Double.isFinite(z) || !Double.isFinite(speed)
               || speed <= 0 || speed > 1 || ResourceLocation.tryParse(dimension) == null) {
               throw new IllegalArgumentException("Invalid npc_move coordinates, speed or dimension");
            }
            builder.npcAction(Type.NPC_MOVE, key, new PositionSpec(dimension, x, y, z, 0, false, speed));
            return NIL;
         }
      });
      globals.set("npc_use_block", new VarArgFunction() {
         @Override public Varargs invoke(Varargs args) {
            if (args.narg() != 4 && args.narg() != 5) {
               throw new IllegalArgumentException("npc_use_block(key, x, y, z, [dimension]) expects 4 or 5 arguments");
            }
            String key = nonempty(args.arg(1));
            int x = blockCoordinate(args.arg(2));
            int y = blockCoordinate(args.arg(3));
            int z = blockCoordinate(args.arg(4));
            String dimension = args.narg() == 5 ? nonempty(args.arg(5)) : "minecraft:overworld";
            if (ResourceLocation.tryParse(dimension) == null) {
               throw new IllegalArgumentException("Invalid npc_use_block dimension: " + dimension);
            }
            builder.npcAction(Type.NPC_USE_BLOCK, key, new PositionSpec(dimension, x, y, z, 0, false));
            return NIL;
         }
      });
      globals.set("speaker", new TwoArgFunction() {
         @Override public LuaValue call(LuaValue id, LuaValue name) { builder.add(new Step(Type.SPEAKER, id.checkint(), name.checkjstring())); return NIL; }
      });
      globals.set("speaker_npc", new TwoArgFunction() {
         @Override public LuaValue call(LuaValue id, LuaValue key) {
            builder.speakerNpc(id.checkint(), nonempty(key));
            return NIL;
         }
      });
      globals.set("speech", new TwoArgFunction() {
         @Override public LuaValue call(LuaValue id, LuaValue text) { builder.add(new Step(Type.SPEECH, id.checkint(), text.checkjstring())); return NIL; }
      });
      globals.set("warning", new OneArgFunction() {
         @Override public LuaValue call(LuaValue text) { builder.add(new Step(Type.WARNING, 0, text.checkjstring())); return NIL; }
      });
      globals.set("wait_f8", new ZeroArgFunction() {
         @Override public LuaValue call() { builder.add(new Step(Type.WAIT_F8, 0, "")); return NIL; }
      });
      globals.set("wait_ticks", new OneArgFunction() {
         @Override public LuaValue call(LuaValue ticks) { builder.add(new Step(Type.WAIT_TICKS, positiveTicks(ticks), "")); return NIL; }
      });
      globals.set("wait_chat", new OneArgFunction() {
         @Override public LuaValue call(LuaValue text) { builder.add(new Step(Type.WAIT_CHAT, 0, nonempty(text))); return NIL; }
      });
      globals.set("wait_flag", new OneArgFunction() {
         @Override public LuaValue call(LuaValue name) { builder.add(new Step(Type.WAIT_FLAG, 0, nonempty(name))); return NIL; }
      });
      globals.set("think", new OneArgFunction() {
         @Override public LuaValue call(LuaValue text) { builder.add(new Step(Type.THINK, 0, nonempty(text))); return NIL; }
      });
      globals.set("blackout", new OneArgFunction() {
         @Override public LuaValue call(LuaValue ticks) { builder.add(new Step(Type.BLACKOUT, positiveTicks(ticks), "")); return NIL; }
      });
      globals.set("flash", new OneArgFunction() {
         @Override public LuaValue call(LuaValue ticks) { builder.add(new Step(Type.FLASH, positiveTicks(ticks), "")); return NIL; }
      });
      globals.set("wait_pos", new VarArgFunction() {
         @Override public Varargs invoke(Varargs args) {
            if (args.narg() < 4 || args.narg() > 6) {
               throw new IllegalArgumentException("wait_pos(x, y, z, radius, [dimension], [ignore_y]) expects 4 to 6 arguments");
            }
            double x = args.arg(1).checkdouble();
            double y = args.arg(2).checkdouble();
            double z = args.arg(3).checkdouble();
            double radius = args.arg(4).checkdouble();
            String dimension = args.narg() >= 5 ? nonempty(args.arg(5)) : "minecraft:overworld";
            boolean ignoreY = args.narg() == 6 && args.arg(6).checkboolean();
            if (!Double.isFinite(x) || !Double.isFinite(y) || !Double.isFinite(z) || !Double.isFinite(radius)
               || radius <= 0.0 || ResourceLocation.tryParse(dimension) == null) {
               throw new IllegalArgumentException("Invalid wait_pos coordinates, radius or dimension");
            }
            builder.add(new Step(Type.WAIT_POS, 0, "", new PositionSpec(dimension, x, y, z, radius, ignoreY)));
            return NIL;
         }
      });
      globals.set("teleport_player", new VarArgFunction() {
         @Override public Varargs invoke(Varargs args) {
            if (args.narg() != 3 && args.narg() != 4 && args.narg() != 6) {
               throw new IllegalArgumentException("teleport_player(x, y, z, [dimension], [yaw, pitch]) expects 3, 4 or 6 arguments");
            }
            double x = args.arg(1).checkdouble();
            double y = args.arg(2).checkdouble();
            double z = args.arg(3).checkdouble();
            String dimension = args.narg() >= 4 ? nonempty(args.arg(4)) : "minecraft:overworld";
            float yaw = args.narg() == 6 ? (float) args.arg(5).checkdouble() : Float.NaN;
            float pitch = args.narg() == 6 ? (float) args.arg(6).checkdouble() : Float.NaN;
            if (!Double.isFinite(x) || !Double.isFinite(y) || !Double.isFinite(z)
               || ResourceLocation.tryParse(dimension) == null || (args.narg() == 6 && (!Float.isFinite(yaw) || !Float.isFinite(pitch)))) {
               throw new IllegalArgumentException("Invalid teleport_player coordinates, dimension or rotation");
            }
            builder.add(new Step(Type.TELEPORT_PLAYER, 0, "", null,
               new String[] {Double.toString(x), Double.toString(y), Double.toString(z), dimension,
                  Float.isNaN(yaw) ? null : Float.toString(yaw), Float.isNaN(pitch) ? null : Float.toString(pitch)}));
            return NIL;
         }
      });
      globals.set("quest_start", new OneArgFunction() {
         @Override public LuaValue call(LuaValue id) { external(builder, StoryEngineAdapter.Operation.START_QUEST, id); return NIL; }
      });
      globals.set("quest_reset", new OneArgFunction() {
         @Override public LuaValue call(LuaValue id) { external(builder, StoryEngineAdapter.Operation.RESET_QUEST, id); return NIL; }
      });
      globals.set("quest_complete", new OneArgFunction() {
         @Override public LuaValue call(LuaValue id) { external(builder, StoryEngineAdapter.Operation.COMPLETE_QUEST, id); return NIL; }
      });
      globals.set("quest_fail", new OneArgFunction() {
         @Override public LuaValue call(LuaValue id) { external(builder, StoryEngineAdapter.Operation.FAIL_QUEST, id); return NIL; }
      });
      globals.set("quest_edit", new TwoArgFunction() {
         @Override public LuaValue call(LuaValue id, LuaValue description) { external(builder, StoryEngineAdapter.Operation.EDIT_QUEST, id, description); return NIL; }
      });
      globals.set("quest_task_complete", new TwoArgFunction() {
         @Override public LuaValue call(LuaValue id, LuaValue task) { external(builder, StoryEngineAdapter.Operation.COMPLETE_TASK, id, task); return NIL; }
      });
      globals.set("quest_notify", new TwoArgFunction() {
         @Override public LuaValue call(LuaValue title, LuaValue message) { external(builder, StoryEngineAdapter.Operation.NOTIFY, title, message); return NIL; }
      });
      globals.set("quest_create", new ThreeArgFunction() {
         @Override public LuaValue call(LuaValue id, LuaValue title, LuaValue description) { external(builder, StoryEngineAdapter.Operation.ENSURE_QUEST, id, title, description); return NIL; }
      });
      globals.set("quest_task", new ThreeArgFunction() {
         @Override public LuaValue call(LuaValue id, LuaValue task, LuaValue title) { external(builder, StoryEngineAdapter.Operation.ADD_TASK, id, task, title); return NIL; }
      });
      try {
         globals.load(source, label).call();
         return builder.build();
      } catch (LuaError | IllegalArgumentException e) {
         throw new IllegalArgumentException("Invalid Lua story " + label + ": " + e.getMessage(), e);
      }
   }

   private static int positiveTicks(LuaValue value) {
      int ticks = value.checkint();
      if (ticks < 1 || ticks > 72000) {
         throw new IllegalArgumentException("Ticks must be between 1 and 72000");
      }
      return ticks;
   }

   private static int blockCoordinate(LuaValue value) {
      double number = value.checkdouble();
      if (!Double.isFinite(number) || number != Math.rint(number)
         || number < Integer.MIN_VALUE || number > Integer.MAX_VALUE) {
         throw new IllegalArgumentException("npc_use_block coordinates must be whole numbers within the integer range");
      }
      return (int) number;
   }

   private static String nonempty(LuaValue value) {
      String text = value.checkjstring();
      if (text.isBlank()) {
         throw new IllegalArgumentException("Text must not be empty");
      }
      return text;
   }

   private static void external(Builder builder, StoryEngineAdapter.Operation operation, LuaValue... values) {
      String[] arguments = new String[values.length];
      for (int i = 0; i < values.length; i++) {
         arguments[i] = nonempty(values[i]);
      }
      builder.add(new Step(Type.STORY_ENGINE, 0, "", operation, arguments));
   }

   private static Location teleportLocation(String[] values) {
      ServerPlayer player = ScriptUtils.getPlayer();
      float yaw = values[4] == null ? player.getYRot() : Float.parseFloat(values[4]);
      float pitch = values[5] == null ? player.getXRot() : Float.parseFloat(values[5]);
      ResourceKey<Level> dimension = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(values[3]));
      return new Location(dimension, Double.parseDouble(values[0]), Double.parseDouble(values[1]), Double.parseDouble(values[2]), yaw, pitch);
   }

   private static Location npcLocation(PositionSpec position) {
      ResourceKey<Level> dimension = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(position.dimension));
      return new Location(dimension, position.x, position.y, position.z);
   }

   @SuppressWarnings("unchecked")
   private static void appendNpcPath(ScriptPerformerBuilder builder, Character<?> character, PositionSpec position) {
      // Story NPCs that can walk use Mob navigation, which follows terrain and handles steps.
      Character<Mob> walkingNpc = (Character<Mob>) character;
      builder.append(performer -> new EntityPathfinderAction<>(performer, walkingNpc, position.speed,
         () -> List.of(npcLocation(position))));
   }

   private enum Type { SPEAKER, SPEECH, WARNING, ANCHOR, WAIT_F8, WAIT_TICKS, WAIT_CHAT, WAIT_FLAG, THINK, BLACKOUT, FLASH, STORY_ENGINE, WAIT_POS, TELEPORT_PLAYER, NPC_SPAWN, NPC_MOVE, NPC_USE_BLOCK, NPC_INTERACT, NPC_REMOVE, NPC_ANIMATION, NPC_LOOK }
   private record NpcSpec(String type, String name, String model, float width, float height) { }
   private record PositionSpec(String dimension, double x, double y, double z, double radius, boolean ignoreY, double speed) {
      private PositionSpec(String dimension, double x, double y, double z, double radius, boolean ignoreY) {
         this(dimension, x, y, z, radius, ignoreY, 0);
      }
   }
   private record Step(Type type, int speaker, String text, StoryEngineAdapter.Operation operation, String[] arguments, PositionSpec position) {
      private Step(Type type, int speaker, String text) {
         this(type, speaker, text, null, null, null);
      }

      private Step(Type type, int speaker, String text, StoryEngineAdapter.Operation operation, String[] arguments) {
         this(type, speaker, text, operation, arguments, null);
      }

      private Step(Type type, int speaker, String text, PositionSpec position) {
         this(type, speaker, text, null, null, position);
      }
   }

   private static final class Builder {
      private final String label;
      private final Map<String, List<Step>> parts = new LinkedHashMap<>();
      private final Map<String, NpcSpec> npcs = new LinkedHashMap<>();
      private final Set<String> anchors = new HashSet<>();
      private int id;
      private String currentPart;

      private Builder(String label) { this.label = label; }

      private void story(int id) {
         if (this.id != 0 || id <= 0 || !parts.isEmpty()) {
            throw new IllegalArgumentException("story(id) must appear once, with a positive ID, before any part");
         }
         this.id = id;
      }

      private void part(String name) {
         if (id == 0 || !name.matches("[a-z][a-z0-9_]*") || parts.containsKey(name)) {
            throw new IllegalArgumentException("Invalid or duplicate part " + name + " in " + label);
         }
         currentPart = name;
         parts.put(name, new ArrayList<>());
      }

      private void npc(String key, String type, String name) {
         if (id == 0 || !parts.isEmpty() || !key.matches("[a-z][a-z0-9_]*") || ResourceLocation.tryParse(type) == null || npcs.containsKey(key)) {
            throw new IllegalArgumentException("Invalid or duplicate NPC definition " + key + " in " + label);
         }
         npcs.put(key, new NpcSpec(type, name, null, 0.6f, 1.8f));
      }

      private void npcModel(String key, String model, String name) {
         if (!model.matches("[a-z0-9_/-]+\\.(?:glb|gltf)") || model.contains("..")) {
            throw new IllegalArgumentException("NPC model must be a .glb or .gltf file in ST/assets/models: " + model);
         }
         npc(key, "storytelling:script_npc", name);
         npcs.put(key, new NpcSpec("storytelling:script_npc", name, "models/" + model, 0.6f, 1.8f));
      }

      private void npcHitbox(String key, float width, float height) {
         NpcSpec spec = npcs.get(key);
         if (spec == null || spec.model == null || !parts.isEmpty()
            || !Float.isFinite(width) || !Float.isFinite(height)
            || width < 0.1f || width > 8f || height < 0.1f || height > 16f) {
            throw new IllegalArgumentException("npc_hitbox requires a declared npc_model before part; width 0.1–8, height 0.1–16: " + key);
         }
         npcs.put(key, new NpcSpec(spec.type, spec.name, spec.model, width, height));
      }

      private void npcAnimation(String key, String animation, String mode) {
         NpcSpec spec = npcs.get(key);
         if (spec == null || spec.model == null || animation.length() > 128
            || !Set.of("once", "hold", "loop").contains(mode)) {
            throw new IllegalArgumentException("npc_animation requires a declared npc_model, a clip name up to 128 characters, and mode once, hold or loop: " + key);
         }
         add(new Step(Type.NPC_ANIMATION, 0, key, null, new String[] {animation, mode}));
      }

      private void npcLook(String key, String... target) {
         NpcSpec spec = npcs.get(key);
         if (spec == null || spec.model == null || (target.length == 1 && !Set.of("player", "none").contains(target[0]))) {
            throw new IllegalArgumentException("npc_look requires a declared npc_model and target player, none, or coordinates: " + key);
         }
         add(new Step(Type.NPC_LOOK, 0, key, null, target));
      }

      private void anchor(String name) {
         if (currentPart == null || !name.matches("[a-z][a-z0-9_]*") || name.equals("start") || name.equals("final") || !anchors.add(name)) {
            throw new IllegalArgumentException("Invalid or duplicate anchor " + name + " in " + label);
         }
         add(new Step(Type.ANCHOR, 0, name));
      }

      private void npcAction(Type type, String key, PositionSpec position) {
         if (!npcs.containsKey(key)) {
            throw new IllegalArgumentException("NPC " + key + " must be defined before its actions in " + label);
         }
         add(new Step(type, 0, key, position));
      }

      private void speakerNpc(int id, String key) {
         NpcSpec spec = npcs.get(key);
         if (spec == null) throw new IllegalArgumentException("Unknown NPC for speaker_npc: " + key);
         add(new Step(Type.SPEAKER, id, spec.name, null,
            new String[] {spec.model == null ? "unknown" : "glb:" + spec.model}));
      }

      private void add(Step step) {
         if (currentPart == null) {
            throw new IllegalArgumentException("Call part(name) before story actions in " + label);
         }
         List<Step> actions = parts.get(currentPart);
         if (actions.size() >= 10000) {
            throw new IllegalArgumentException("Too many actions in " + label);
         }
         actions.add(step);
      }

      private LuaStory build() {
         if (id == 0 || !parts.containsKey("main") || parts.get("main").isEmpty()) {
            throw new IllegalArgumentException("Lua story needs story(id) and a nonempty main part: " + label);
         }
         Map<String, List<Step>> frozen = new LinkedHashMap<>();
         parts.forEach((name, actions) -> frozen.put(name, List.copyOf(actions)));
         return new LuaStory(id, Map.copyOf(frozen), Map.copyOf(npcs));
      }
   }
}
