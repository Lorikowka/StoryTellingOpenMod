package com.storyteam.storytelling.script.story;

import com.storyteam.storytelling.script.performer.builder.ScriptPerformerBuilder;
import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.script.performer.action.WaitPlayerPositionAction;
import com.storyteam.storytelling.script.performer.action.ScriptPerformerAction;
import com.storyteam.storytelling.script.performer.action.behaviour.EntityPathfinderAction;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.script.story.npc.ScriptNpcEntity;
import com.storyteam.storytelling.script.story.npc.NpcWorldInteraction;
import com.storyteam.storytelling.script.story.camera.NpcCameraFlightAction;
import com.storyteam.storytelling.net.PacketLuaChoiceScreen;
import com.storyteam.storytelling.system._core.st_cutscene.STCutsceneData;
import com.storyteam.storytelling.system._core.st_cutscene.ServersideCutsceneHandler;
import com.storyteam.storytelling.script.story.EntityRepository;
import com.storyteam.storytelling.util.ScriptUtils;
import com.storyteam.storytelling.util.world.Location;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HexFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.LuaTable;
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
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.registries.ForgeRegistries;
import org.zeith.hammerlib.net.Network;

/** Loads a Lua story into the existing Java performer without exposing Minecraft internals to Lua. */
public final class LuaStory {
   private final int id;
   private final Map<String, List<Step>> parts;
   private final Map<String, NpcSpec> npcs;
   private final String fingerprint;

   private LuaStory(int id, Map<String, List<Step>> parts, Map<String, NpcSpec> npcs) {
      this.id = id;
      this.parts = parts;
      this.npcs = npcs;
      this.fingerprint = calculateFingerprint();
   }

   public int id() {
      return id;
   }

   /** Identifies the parsed action sequence, independent of comments and whitespace in Lua. */
   public String fingerprint() {
      return fingerprint;
   }

   private String calculateFingerprint() {
      StringBuilder definition = new StringBuilder().append(id);
      npcs.keySet().stream().sorted().forEach(key -> definition.append("\nNPC\n").append(key).append(npcs.get(key)));
      parts.keySet().stream().sorted().forEach(part -> {
         definition.append("\nPART\n").append(part);
         for (Step step : parts.get(part)) {
            definition.append("\n").append(step.type).append('\u0000').append(step.speaker)
               .append('\u0000').append(step.text).append('\u0000').append(step.operation)
               .append('\u0000').append(Arrays.toString(step.arguments)).append('\u0000').append(step.position);
         }
      });
      try {
         return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256")
            .digest(definition.toString().getBytes(StandardCharsets.UTF_8)));
      } catch (NoSuchAlgorithmException impossible) {
         throw new IllegalStateException("SHA-256 is unavailable", impossible);
      }
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
            case CAMERA_SHOT -> builder.append(performer -> new ScriptPerformerAction(performer) {
               private STCutsceneData camera;
               private int elapsed;
               private boolean done;

               private void release() {
                  if (camera != null && ServersideCutsceneHandler.currentData == camera
                     && ScriptUtils.getPlayer() != null) ServersideCutsceneHandler.stopCamera();
                  camera = null;
               }

               @Override public void tick() {
                  if (done) return;
                  ServerPlayer player = ScriptUtils.getPlayer();
                  if (player == null) return;
                  if (camera == null) {
                     if (ServersideCutsceneHandler.currentData != null) {
                        throw new IllegalStateException("Another camera scene is already active");
                     }
                     Location location = new Location(player.level().dimension(),
                        Double.parseDouble(step.arguments[0]), Double.parseDouble(step.arguments[1]),
                        Double.parseDouble(step.arguments[2]), Float.parseFloat(step.arguments[3]),
                        Float.parseFloat(step.arguments[4]));
                     camera = new STCutsceneData(location, false);
                     ServersideCutsceneHandler.initOrTpCamera(camera);
                  }
                  if (++elapsed >= Integer.parseInt(step.arguments[5])) {
                     release();
                     done = true;
                  }
               }

               @Override public boolean isDone() { return done; }
               @Override public void reset() { release(); elapsed = 0; done = false; }
               @Override public void onScriptStop() { release(); }
            });
            case CAMERA_FLY_NPC -> builder.append(performer -> new NpcCameraFlightAction(performer,
               characters.get(step.text), Integer.parseInt(step.arguments[0]),
               Integer.parseInt(step.arguments[1]), Double.parseDouble(step.arguments[2])));
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
            case NPC_TRY_USE_BLOCK, NPC_APPROACH_USE_BLOCK -> builder.append(performer -> new ScriptPerformerAction(performer) {
               private boolean applied;
               private int waiting;
               private boolean approaching;
               private final Set<BlockPos> attemptedStands = new HashSet<>();
               private String marker() { return "lua.block." + performer.getActionIndex(this); }
               private void finish(boolean success) {
                  Entity npcEntity = characters.get(step.text).repositoryEntity();
                  if (npcEntity instanceof ScriptNpcEntity npc) {
                     npc.walking(false);
                     if (approaching) {
                        var controller = script.env.server.getEntityRepository().get(characters.get(step.text));
                        if (controller != null) controller.navigator.stop();
                     }
                  }
                  script.getFlags().set("lua.var." + step.arguments[0], Boolean.toString(success));
                  script.getFlags().set(marker());
                  applied = true;
               }

               @Override public void tick() {
                  if (script.getFlags().has(marker())) { applied = true; return; }
                  Entity entity = characters.get(step.text).repositoryEntity();
                  if (entity == null) {
                     if (++waiting > 200) finish(false);
                     return;
                  }
                  BlockPos block = new BlockPos((int) step.position.x, (int) step.position.y, (int) step.position.z);
                  if (step.type == Type.NPC_APPROACH_USE_BLOCK && entity instanceof ScriptNpcEntity npc
                     && npc.getEyePosition().distanceToSqr(net.minecraft.world.phys.Vec3.atCenterOf(block)) > 1.5 * 1.5) {
                     if (++waiting > 400) { finish(false); return; }
                     if (!approaching) {
                        if (!(npc.level() instanceof net.minecraft.server.level.ServerLevel level)) return;
                        var controller = script.env.server.getEntityRepository().get(characters.get(step.text));
                        if (controller == null) { finish(false); return; }
                        List<BlockPos> stands = new ArrayList<>();
                        for (int dy = -2; dy <= 1; dy++) {
                           for (int dx = -4; dx <= 4; dx++) {
                              for (int dz = -4; dz <= 4; dz++) {
                                 if (Math.abs(dx) + Math.abs(dz) < 1 || Math.abs(dx) + Math.abs(dz) > 5) continue;
                                 BlockPos pos = block.offset(dx, dy, dz);
                                 if (new net.minecraft.world.phys.Vec3(pos.getX() + 0.5, pos.getY() + npc.getEyeHeight(), pos.getZ() + 0.5)
                                    .distanceToSqr(net.minecraft.world.phys.Vec3.atCenterOf(block)) > 1.5 * 1.5) continue;
                                 if (attemptedStands.contains(pos) || !level.hasChunkAt(pos)
                                    || !level.getBlockState(pos).getCollisionShape(level, pos).isEmpty()
                                    || !level.getBlockState(pos.above()).getCollisionShape(level, pos.above()).isEmpty()
                                    || level.getBlockState(pos.below()).getCollisionShape(level, pos.below()).isEmpty()) continue;
                                 stands.add(pos);
                              }
                           }
                        }
                        stands.sort(java.util.Comparator
                           .comparingDouble((BlockPos pos) -> net.minecraft.world.phys.Vec3.atCenterOf(pos)
                              .distanceToSqr(net.minecraft.world.phys.Vec3.atCenterOf(block)))
                           .thenComparingDouble(pos -> npc.distanceToSqr(net.minecraft.world.phys.Vec3.atCenterOf(pos))));
                        BlockPos selected = stands.isEmpty() ? null : stands.get(0);
                        if (selected == null) {
                           Storytelling.LOG.info("NPC could not find a reachable stand near block: story={}, npc={}, block={}, pos={}, candidates={}",
                              id, step.text, block, npc.position(), stands.size());
                           finish(false);
                           return;
                        }
                        attemptedStands.add(selected);
                        controller.navigator.moveByPath(0.8f,
                           new Location(level.dimension(), selected.getX() + 0.5, selected.getY(), selected.getZ() + 0.5));
                        Storytelling.LOG.info("NPC approaching block: story={}, npc={}, stand={}, block={}", id, step.text, selected, block);
                        npc.walking(true);
                        approaching = true;
                     } else {
                        var controller = script.env.server.getEntityRepository().get(characters.get(step.text));
                        if (controller == null || controller.navigator.currentJob == null
                           || controller.navigator.currentJob.isStopped()) {
                           if (controller != null) controller.navigator.stop();
                           approaching = false;
                        }
                     }
                     return;
                  }
                  if (approaching && entity instanceof ScriptNpcEntity npc) {
                     var controller = script.env.server.getEntityRepository().get(characters.get(step.text));
                     if (controller != null) controller.navigator.stop();
                  }
                  boolean success;
                  try {
                     NpcWorldInteraction.useBlock(entity,
                        block,
                        step.position.dimension);
                     success = true;
                  } catch (IllegalStateException failure) {
                     Storytelling.LOG.info("NPC block interaction failed: story={}, npc={}, reason={}", id, step.text, failure.getMessage());
                     success = false;
                  }
                  finish(success);
               }

               @Override public boolean isDone() { return applied; }
               @Override public void reset() { applied = false; waiting = 0; approaching = false; attemptedStands.clear(); }
            });
            case NPC_INTERACT -> builder.thenAwaitEntityClick(characters.get(step.text));
            case NPC_REMOVE -> builder.removeEntity(characters.get(step.text));
            case NPC_ANIMATION -> builder.thenRun(() -> {
               Entity entity = characters.get(step.text).repositoryEntity();
               if (entity instanceof ScriptNpcEntity scriptNpc) scriptNpc.animation(step.arguments[0], step.arguments[1]);
            });
            case NPC_LOOK -> builder.append(performer -> new ScriptPerformerAction(performer) {
               private boolean applied;

               @Override public void tick() {
                  if (applied) return;
                  Entity entity = characters.get(step.text).repositoryEntity();
                  if (entity == null) return;
                  if (!(entity instanceof ScriptNpcEntity scriptNpc)) {
                     throw new IllegalStateException("npc_look expected a model NPC: " + step.text);
                  }
                  scriptNpc.lookMode(step.arguments[step.arguments.length - 1]);
                  if (step.arguments[0].equals("player")) scriptNpc.lookAt(ScriptUtils.getPlayer());
                  else if (step.arguments[0].equals("none")) scriptNpc.clearLook();
                  else scriptNpc.lookAt(Double.parseDouble(step.arguments[0]),
                     Double.parseDouble(step.arguments[1]), Double.parseDouble(step.arguments[2]));
                  Storytelling.LOG.info("NPC look: story={}, npc={}, target={}", id, step.text,
                     String.join(",", step.arguments));
                  applied = true;
               }

               @Override public boolean isDone() { return applied; }
               @Override public void reset() { applied = false; }
            });
            case VAR_SET -> builder.append(performer -> new ScriptPerformerAction(performer) {
               private boolean applied;

               @Override public void tick() {
                  if (!applied) {
                     String scope = step.arguments[1];
                     if (scope.equals("player") && ScriptUtils.getPlayer() == null) return;
                     if (scope.equals("script")) script.getFlags().set("lua.var." + step.text, step.arguments[0]);
                     else script.env.setVariable(scope, step.text, step.arguments[0], ScriptUtils.getPlayer());
                     applied = true;
                  }
               }

               @Override public boolean isDone() { return applied; }
               @Override public void reset() { applied = false; }
            });
            case VAR_IF -> builder.append(performer -> new ScriptPerformerAction(performer) {
               @Override public void tick() {
                  String scope = step.arguments[3];
                  if (scope.equals("player") && ScriptUtils.getPlayer() == null) return;
                  String current = scope.equals("script") ? script.getFlags().get("lua.var." + step.text)
                     : script.env.getVariable(scope, step.text, ScriptUtils.getPlayer());
                  String destination = step.arguments[0].equals(current) ? step.arguments[1] : step.arguments[2];
                  performer.gotoSavepoint(destination, true);
               }

               @Override public boolean isDone() { return false; }
            });
            case GOTO_ANCHOR -> builder.append(performer -> new ScriptPerformerAction(performer) {
               @Override public void tick() { performer.gotoSavepoint(step.text, true); }
               @Override public boolean isDone() { return false; }
            });
            case CHOICE, CHOICE_IF -> builder.append(performer -> new ScriptPerformerAction(performer) {
               private boolean displayed;
               private boolean chosen;
               private String marker() { return "lua.choice." + performer.getActionIndex(this); }
               private void closeScreen() {
                  if (!displayed) return;
                  ServerPlayer player = ScriptUtils.getPlayer();
                  if (player != null) Network.sendTo(player, PacketLuaChoiceScreen.close(id, performer.getActionIndex(this)));
               }
               private int stride() { return step.type == Type.CHOICE_IF ? 4 : 1; }
               private boolean visible(int offset) {
                  if (step.type != Type.CHOICE_IF || step.arguments[offset + 1].isEmpty()) return true;
                  String scope = step.arguments[offset + 3];
                  ServerPlayer player = ScriptUtils.getPlayer();
                  if (scope.equals("player") && player == null) return false;
                  String actual = scope.equals("script") ? script.getFlags().get("lua.var." + step.arguments[offset + 1])
                     : script.env.getVariable(scope, step.arguments[offset + 1], player);
                  return step.arguments[offset + 2].equals(actual);
               }

               @Override public void tick() {
                  if (script.getFlags().has(marker())) { chosen = true; return; }
                  if (displayed) return;
                  ServerPlayer player = ScriptUtils.getPlayer();
                  if (player == null) return;
                  player.sendSystemMessage(Component.literal(step.arguments[0]));
                  String[] labels = new String[(step.arguments.length - 1) / stride()];
                  for (int i = 1; i < step.arguments.length; i += stride()) {
                     if (!visible(i)) continue;
                     int option = (i - 1) / stride() + 1;
                     labels[option - 1] = step.arguments[i];
                     player.sendSystemMessage(Component.literal(option + ". " + step.arguments[i])
                        .withStyle(style -> style.withColor(ChatFormatting.AQUA)
                           .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, Integer.toString(option)))));
                  }
                  Network.sendTo(player, new PacketLuaChoiceScreen(id, performer.getActionIndex(this), step.arguments[0], labels));
                  displayed = true;
               }

               @Override public <T> void handleEvent(EventType<T> type, T event) {
                  if (chosen || !displayed || (type != EventType.CHAT_MESSAGE_SENT && type != EventType.LUA_CHOICE_SELECTED)) return;
                  String answer = String.valueOf(event).trim();
                  int index;
                  try { index = Integer.parseInt(answer); }
                  catch (NumberFormatException ignored) { return; }
                  int offset = 1 + (index - 1) * stride();
                  if (index < 1 || offset >= step.arguments.length || !visible(offset)) return;
                  script.getFlags().set("lua.var." + step.text, Integer.toString(index));
                  script.getFlags().set(marker());
                  chosen = true;
               }

               @Override public boolean isDone() { return chosen; }
               @Override public void stop() { closeScreen(); }
               @Override public void onScriptStop() { closeScreen(); }
               @Override public void reset() { closeScreen(); displayed = false; chosen = false; }
            });
            case PLAY_SOUND -> builder.append(performer -> new ScriptPerformerAction(performer) {
               private boolean applied;
               private String marker() { return "lua.action." + performer.getActionIndex(this); }
               @Override public void tick() {
                  if (script.getFlags().has(marker())) { applied = true; return; }
                  ServerPlayer player = ScriptUtils.getPlayer();
                  if (player == null) return;
                  SoundEvent sound = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(step.text));
                  if (sound == null) throw new IllegalArgumentException("Unknown sound: " + step.text);
                  player.serverLevel().playSound(null, player.getX(), player.getY(), player.getZ(), sound,
                     SoundSource.MASTER, Float.parseFloat(step.arguments[0]), Float.parseFloat(step.arguments[1]));
                  script.getFlags().set(marker());
                  applied = true;
               }
               @Override public boolean isDone() { return applied; }
               @Override public void reset() { applied = false; }
            });
            case GIVE_ITEM -> builder.append(performer -> new ScriptPerformerAction(performer) {
               private boolean applied;
               private String marker() { return "lua.action." + performer.getActionIndex(this); }
               @Override public void tick() {
                  if (script.getFlags().has(marker())) { applied = true; return; }
                  ServerPlayer player = ScriptUtils.getPlayer();
                  if (player == null) return;
                  Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(step.text));
                  if (item == null) throw new IllegalArgumentException("Unknown item: " + step.text);
                  ItemStack stack = new ItemStack(item, Integer.parseInt(step.arguments[0]));
                  player.getInventory().add(stack);
                  if (!stack.isEmpty()) player.drop(stack, false);
                  script.getFlags().set(marker());
                  applied = true;
               }
               @Override public boolean isDone() { return applied; }
               @Override public void reset() { applied = false; }
            });
            case WAIT_ITEM -> builder.append(performer -> new ScriptPerformerAction(performer) {
               private boolean found;
               @Override public void tick() {
                  ServerPlayer player = ScriptUtils.getPlayer();
                  if (player == null) return;
                  Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(step.text));
                  if (item == null) throw new IllegalArgumentException("Unknown item: " + step.text);
                  int count = 0;
                  for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
                     ItemStack stack = player.getInventory().getItem(slot);
                     if (stack.is(item)) count += stack.getCount();
                  }
                  found = count >= Integer.parseInt(step.arguments[0]);
               }
               @Override public boolean isDone() { return found; }
               @Override public void reset() { found = false; }
            });
            case PARTICLE, PARTICLE_HERE -> builder.append(performer -> new ScriptPerformerAction(performer) {
               private boolean applied;
               private boolean warned;
               private String marker() { return "lua.action." + performer.getActionIndex(this); }
               @Override public void tick() {
                  if (script.getFlags().has(marker())) { applied = true; return; }
                  ServerPlayer player = ScriptUtils.getPlayer();
                  if (player == null) return;
                  ParticleType<?> type = ForgeRegistries.PARTICLE_TYPES.getValue(new ResourceLocation(step.text));
                  if (!(type instanceof SimpleParticleType simple)) {
                     throw new IllegalArgumentException("Particle must be a registered simple particle: " + step.text);
                  }
                  net.minecraft.world.phys.Vec3 point = step.type == Type.PARTICLE_HERE
                     ? player.getEyePosition().add(player.getLookAngle().scale(2.0))
                     : new net.minecraft.world.phys.Vec3(Double.parseDouble(step.arguments[0]),
                        Double.parseDouble(step.arguments[1]), Double.parseDouble(step.arguments[2]));
                  int count = Integer.parseInt(step.arguments[3]);
                  if (!player.serverLevel().sendParticles(player, simple, true, point.x, point.y, point.z,
                     count, 0.3, 0.3, 0.3, 0.02)) {
                     if (!warned) {
                        Storytelling.LOG.warn("Lua particle waiting for player to come within 512 blocks: story={}, point={}, player={}",
                           id, point, player.position());
                        warned = true;
                     }
                     return;
                  }
                  script.getFlags().set(marker());
                  applied = true;
               }
               @Override public boolean isDone() { return applied; }
               @Override public void reset() { applied = false; warned = false; }
            });
         }
      }
   }

   public static Path storyDirectory() {
      return StoryWorkspace.scripts();
   }

   /** Creates an empty authoring directory. Existing user scenarios are left untouched. */
   public static void initializeStoryDirectory(Path directory) throws IOException {
      Files.createDirectories(directory);
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
      globals.set("var_set", new VarArgFunction() {
         @Override public Varargs invoke(Varargs args) {
            if (args.narg() != 2 && args.narg() != 3) throw new IllegalArgumentException("var_set(name, value, [scope]) expected");
            builder.variableSet(nonempty(args.arg(1)), args.arg(2), args.narg() == 3 ? nonempty(args.arg(3)) : "script");
            return NIL;
         }
      });
      globals.set("var_if", new VarArgFunction() {
         @Override public Varargs invoke(Varargs args) {
            if (args.narg() != 4 && args.narg() != 5) {
               throw new IllegalArgumentException("var_if(name, value, yes_anchor, no_anchor, [scope]) expects 4 or 5 arguments");
            }
            builder.variableIf(nonempty(args.arg(1)), args.arg(2), nonempty(args.arg(3)), nonempty(args.arg(4)),
               args.narg() == 5 ? nonempty(args.arg(5)) : "script");
            return NIL;
         }
      });
      globals.set("goto_anchor", new OneArgFunction() {
         @Override public LuaValue call(LuaValue name) { builder.gotoAnchor(nonempty(name)); return NIL; }
      });
      globals.set("choice", new VarArgFunction() {
         @Override public Varargs invoke(Varargs args) {
            if (args.narg() < 4 || args.narg() > 8) {
               throw new IllegalArgumentException("choice(name, prompt, option1, option2, ...) needs 2–6 options");
            }
            String[] values = new String[args.narg() - 1];
            for (int i = 2; i <= args.narg(); i++) values[i - 2] = nonempty(args.arg(i));
            builder.choice(nonempty(args.arg(1)), values);
            return NIL;
         }
      });
      globals.set("choice_if", new VarArgFunction() {
         @Override public Varargs invoke(Varargs args) {
            if (args.narg() < 4 || args.narg() > 8) {
               throw new IllegalArgumentException("choice_if(name, prompt, option1, option2, ...) needs 2–6 option tables");
            }
            String[] values = new String[1 + (args.narg() - 2) * 4];
            values[0] = nonempty(args.arg(2));
            boolean unconditional = false;
            for (int i = 3; i <= args.narg(); i++) {
               LuaValue option = args.arg(i).checktable();
               int offset = 1 + (i - 3) * 4;
               values[offset] = nonempty(option.get("text"));
               LuaValue condition = option.get("when");
               values[offset + 1] = condition.isnil() ? "" : nonempty(condition);
               values[offset + 2] = condition.isnil() ? "" : nonempty(option.get("equals"));
               values[offset + 3] = condition.isnil() ? "script" :
                  (option.get("scope").isnil() ? "script" : nonempty(option.get("scope")));
               if (condition.isnil()) unconditional = true;
            }
            if (!unconditional) throw new IllegalArgumentException("choice_if needs at least one unconditional option");
            builder.choiceIf(nonempty(args.arg(1)), values);
            return NIL;
         }
      });
      globals.set("play_sound", new VarArgFunction() {
         @Override public Varargs invoke(Varargs args) {
            if (args.narg() < 1 || args.narg() > 3) throw new IllegalArgumentException("play_sound(id, [volume], [pitch]) expected");
            String id = nonempty(args.arg(1));
            float volume = args.narg() >= 2 ? (float) args.arg(2).checkdouble() : 1f;
            float pitch = args.narg() == 3 ? (float) args.arg(3).checkdouble() : 1f;
            builder.playSound(id, volume, pitch);
            return NIL;
         }
      });
      globals.set("give_item", new VarArgFunction() {
         @Override public Varargs invoke(Varargs args) {
            if (args.narg() < 1 || args.narg() > 2) throw new IllegalArgumentException("give_item(id, [count]) expected");
            builder.giveItem(nonempty(args.arg(1)), args.narg() == 2 ? args.arg(2).checkint() : 1);
            return NIL;
         }
      });
      globals.set("wait_item", new VarArgFunction() {
         @Override public Varargs invoke(Varargs args) {
            if (args.narg() < 1 || args.narg() > 2) throw new IllegalArgumentException("wait_item(id, [count]) expected");
            builder.waitItem(nonempty(args.arg(1)), args.narg() == 2 ? args.arg(2).checkint() : 1);
            return NIL;
         }
      });
      globals.set("spawn_particle", new VarArgFunction() {
         @Override public Varargs invoke(Varargs args) {
            if (args.narg() != 5) throw new IllegalArgumentException("spawn_particle(id, x, y, z, count) expected");
            builder.particle(nonempty(args.arg(1)), args.arg(2).checkdouble(), args.arg(3).checkdouble(),
               args.arg(4).checkdouble(), args.arg(5).checkint());
            return NIL;
         }
      });
      globals.set("spawn_particle_here", new VarArgFunction() {
         @Override public Varargs invoke(Varargs args) {
            if (args.narg() != 2) throw new IllegalArgumentException("spawn_particle_here(id, count) expected");
            builder.particleHere(nonempty(args.arg(1)), args.arg(2).checkint());
            return NIL;
         }
      });
      globals.set("npc", new VarArgFunction() {
         @Override public Varargs invoke(Varargs args) {
            if (args.narg() == 3) {
               builder.npc(nonempty(args.arg(1)), nonempty(args.arg(2)), nonempty(args.arg(3)));
               return NIL;
            }
            if (args.narg() != 1 || !args.arg(1).istable()) {
               throw new IllegalArgumentException("npc{key=..., model=..., name=...} or npc(key, type, name) expected");
            }
            LuaTable spec = args.arg(1).checktable();
            String key = nonempty(spec.get("key"));
            String name = nonempty(spec.get("name"));
            LuaValue model = spec.get("model");
            LuaValue type = spec.get("type");
            if (model.isnil() == type.isnil()) {
               throw new IllegalArgumentException("NPC object requires exactly one of model or type: " + key);
            }
            if (!model.isnil()) {
               builder.npcModel(key, nonempty(model), name);
               LuaValue width = spec.get("width");
               LuaValue height = spec.get("height");
               if (!width.isnil() || !height.isnil()) {
                  builder.npcHitbox(key, width.isnil() ? 0.6f : (float) width.checkdouble(),
                     height.isnil() ? 1.8f : (float) height.checkdouble());
               }
            } else {
               if (!spec.get("width").isnil() || !spec.get("height").isnil()) {
                  throw new IllegalArgumentException("Custom hitbox requires a glTF model: " + key);
               }
               builder.npc(key, nonempty(type), name);
            }
            LuaTable object = new LuaTable();
            object.set("key", LuaValue.valueOf(key));
            object.set("name", LuaValue.valueOf(name));
            bindNpcMethod(globals, object, key, "spawn", "npc_spawn");
            bindNpcMethod(globals, object, key, "move", "npc_move");
            bindNpcMethod(globals, object, key, "look", "npc_look");
            bindNpcMethod(globals, object, key, "animate", "npc_animation");
            bindNpcMethod(globals, object, key, "use_block", "npc_approach_use_block");
            bindNpcMethod(globals, object, key, "try_use_block", "npc_try_use_block");
            bindNpcMethod(globals, object, key, "wait_interact", "npc_wait_interact");
            bindNpcMethod(globals, object, key, "remove", "npc_remove");
            bindNpcMethod(globals, object, key, "camera", "camera_fly_npc");
            object.set("speaker", new VarArgFunction() {
               @Override public Varargs invoke(Varargs call) {
                  if (call.narg() != 2 || call.arg(1) != object) {
                     throw new IllegalArgumentException("npc:speaker(id) expected");
                  }
                  return globals.get("speaker_npc").call(call.arg(2), LuaValue.valueOf(key));
               }
            });
            return object;
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
            if (args.narg() == 2 || args.narg() == 3) {
               String mode = args.narg() == 3 ? nonempty(args.arg(3)) : "auto";
               builder.npcLook(nonempty(args.arg(1)), nonempty(args.arg(2)), mode);
            } else if (args.narg() == 4 || args.narg() == 5) {
               double x = args.arg(2).checkdouble();
               double y = args.arg(3).checkdouble();
               double z = args.arg(4).checkdouble();
               if (!Double.isFinite(x) || !Double.isFinite(y) || !Double.isFinite(z)
                  || Math.abs(x) > 30000000 || Math.abs(y) > 30000000 || Math.abs(z) > 30000000) {
                  throw new IllegalArgumentException("Invalid npc_look coordinates");
               }
               String mode = args.narg() == 5 ? nonempty(args.arg(5)) : "auto";
               builder.npcLook(nonempty(args.arg(1)), Double.toString(x), Double.toString(y), Double.toString(z), mode);
            } else {
               throw new IllegalArgumentException("npc_look(key, 'player'|'none', [mode]) or npc_look(key, x, y, z, [mode]) expected");
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
      globals.set("npc_try_use_block", new VarArgFunction() {
         @Override public Varargs invoke(Varargs args) {
            if (args.narg() != 5 && args.narg() != 6) {
               throw new IllegalArgumentException("npc_try_use_block(key, x, y, z, result_name, [dimension]) expects 5 or 6 arguments");
            }
            String key = nonempty(args.arg(1));
            int x = blockCoordinate(args.arg(2));
            int y = blockCoordinate(args.arg(3));
            int z = blockCoordinate(args.arg(4));
            String result = nonempty(args.arg(5));
            String dimension = args.narg() == 6 ? nonempty(args.arg(6)) : "minecraft:overworld";
            if (!result.matches("[a-z][a-z0-9_]*") || ResourceLocation.tryParse(dimension) == null) {
               throw new IllegalArgumentException("Invalid npc_try_use_block result name or dimension");
            }
            builder.npcBlockResult(key, new PositionSpec(dimension, x, y, z, 0, false), result);
            return NIL;
         }
      });
      globals.set("npc_approach_use_block", new VarArgFunction() {
         @Override public Varargs invoke(Varargs args) {
            if (args.narg() != 5 && args.narg() != 6) {
               throw new IllegalArgumentException("npc_approach_use_block(key, x, y, z, result_name, [dimension]) expected");
            }
            String key = nonempty(args.arg(1));
            int x = blockCoordinate(args.arg(2));
            int y = blockCoordinate(args.arg(3));
            int z = blockCoordinate(args.arg(4));
            String result = nonempty(args.arg(5));
            String dimension = args.narg() == 6 ? nonempty(args.arg(6)) : "minecraft:overworld";
            if (!result.matches("[a-z][a-z0-9_]*") || ResourceLocation.tryParse(dimension) == null) {
               throw new IllegalArgumentException("Invalid npc_approach_use_block result name or dimension");
            }
            builder.npcBlockResult(Type.NPC_APPROACH_USE_BLOCK, key, new PositionSpec(dimension, x, y, z, 0, false), result);
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
      globals.set("camera_shot", new VarArgFunction() {
         @Override public Varargs invoke(Varargs args) {
            if (args.narg() != 6) throw new IllegalArgumentException("camera_shot(x, y, z, yaw, pitch, ticks) expected");
            builder.cameraShot(args.arg(1).checkdouble(), args.arg(2).checkdouble(), args.arg(3).checkdouble(),
               args.arg(4).checkdouble(), args.arg(5).checkdouble(), args.arg(6).checkint());
            return NIL;
         }
      });
      globals.set("camera_fly_npc", new VarArgFunction() {
         @Override public Varargs invoke(Varargs args) {
            if (args.narg() < 1 || args.narg() > 4) {
               throw new IllegalArgumentException("camera_fly_npc(key, [flight_ticks], [hold_ticks], [distance]) expects 1 to 4 arguments");
            }
            builder.cameraFlyNpc(nonempty(args.arg(1)), args.narg() >= 2 ? args.arg(2).checkint() : 40,
               args.narg() >= 3 ? args.arg(3).checkint() : 40,
               args.narg() >= 4 ? args.arg(4).checkdouble() : 1.6);
            return NIL;
         }
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

   private static void bindNpcMethod(Globals globals, LuaTable object, String key,
                                     String method, String legacyFunction) {
      object.set(method, new VarArgFunction() {
         @Override public Varargs invoke(Varargs args) {
            if (args.narg() < 1 || args.arg(1) != object) {
               throw new IllegalArgumentException("Use npc:" + method + "(...) with a colon");
            }
            LuaValue[] forwarded = new LuaValue[args.narg()];
            forwarded[0] = LuaValue.valueOf(key);
            for (int i = 2; i <= args.narg(); i++) forwarded[i - 1] = args.arg(i);
            return globals.get(legacyFunction).invoke(LuaValue.varargsOf(forwarded));
         }
      });
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

   private enum Type { SPEAKER, SPEECH, WARNING, ANCHOR, WAIT_F8, WAIT_TICKS, WAIT_CHAT, WAIT_FLAG, WAIT_ITEM, THINK, BLACKOUT, FLASH, CAMERA_SHOT, CAMERA_FLY_NPC, STORY_ENGINE, WAIT_POS, TELEPORT_PLAYER, NPC_SPAWN, NPC_MOVE, NPC_USE_BLOCK, NPC_TRY_USE_BLOCK, NPC_APPROACH_USE_BLOCK, NPC_INTERACT, NPC_REMOVE, NPC_ANIMATION, NPC_LOOK, VAR_SET, VAR_IF, GOTO_ANCHOR, CHOICE, CHOICE_IF, PLAY_SOUND, GIVE_ITEM, PARTICLE, PARTICLE_HERE }
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
         if (spec == null || spec.model == null || !Set.of("auto", "head", "body").contains(target[target.length - 1])
            || (target.length == 2 && !Set.of("player", "none").contains(target[0]))
            || (target.length != 2 && target.length != 4)) {
            throw new IllegalArgumentException("npc_look requires npc_model, target and mode auto, head or body: " + key);
         }
         add(new Step(Type.NPC_LOOK, 0, key, null, target));
      }

      private void anchor(String name) {
         if (currentPart == null || !name.matches("[a-z][a-z0-9_]*") || name.equals("start") || name.equals("final") || !anchors.add(name)) {
            throw new IllegalArgumentException("Invalid or duplicate anchor " + name + " in " + label);
         }
         add(new Step(Type.ANCHOR, 0, name));
      }

      private void variableSet(String name, LuaValue value, String scope) {
         if (!name.matches("[a-z][a-z0-9_]*") || !Set.of("script", "world", "player").contains(scope)
            || value.isnil() || (!value.isstring() && !value.isnumber() && !value.isboolean())) {
            throw new IllegalArgumentException("var_set requires a simple name and string, number or boolean value");
         }
         add(new Step(Type.VAR_SET, 0, name, null, new String[] {value.tojstring(), scope}));
      }

      private void variableIf(String name, LuaValue value, String yes, String no, String scope) {
         if (!name.matches("[a-z][a-z0-9_]*") || !Set.of("script", "world", "player").contains(scope)
            || value.isnil() || (!value.isstring() && !value.isnumber() && !value.isboolean())) {
            throw new IllegalArgumentException("var_if requires a simple name and string, number or boolean value");
         }
         add(new Step(Type.VAR_IF, 0, name, null, new String[] {value.tojstring(), yes, no, scope}));
      }

      private void gotoAnchor(String name) {
         add(new Step(Type.GOTO_ANCHOR, 0, name));
      }

      private void choice(String name, String[] values) {
         if (!name.matches("[a-z][a-z0-9_]*")) throw new IllegalArgumentException("Invalid choice variable: " + name);
         for (String value : values) if (value.length() > 256) throw new IllegalArgumentException("Choice text must be at most 256 characters");
         add(new Step(Type.CHOICE, 0, name, null, values));
      }

      private void choiceIf(String name, String[] values) {
         if (!name.matches("[a-z][a-z0-9_]*")) throw new IllegalArgumentException("Invalid choice variable: " + name);
         if (values[0].length() > 256) throw new IllegalArgumentException("Choice prompt must be at most 256 characters");
         for (int i = 1; i < values.length; i += 4) {
            if (values[i].length() > 256) throw new IllegalArgumentException("Choice text must be at most 256 characters");
            if (!values[i + 1].isEmpty() && (!values[i + 1].matches("[a-z][a-z0-9_]*")
               || !Set.of("script", "world", "player").contains(values[i + 3]))) {
               throw new IllegalArgumentException("Invalid choice_if condition or scope for option " + ((i - 1) / 4 + 1));
            }
         }
         add(new Step(Type.CHOICE_IF, 0, name, null, values));
      }

      private void playSound(String id, float volume, float pitch) {
         if (ResourceLocation.tryParse(id) == null || !Float.isFinite(volume) || volume < 0f || volume > 4f
            || !Float.isFinite(pitch) || pitch < 0.5f || pitch > 2f) {
            throw new IllegalArgumentException("play_sound requires a sound ID, volume 0–4 and pitch 0.5–2");
         }
         add(new Step(Type.PLAY_SOUND, 0, id, null, new String[] {Float.toString(volume), Float.toString(pitch)}));
      }

      private void giveItem(String id, int count) {
         if (ResourceLocation.tryParse(id) == null || count < 1 || count > 64) {
            throw new IllegalArgumentException("give_item requires an item ID and count 1–64");
         }
         add(new Step(Type.GIVE_ITEM, 0, id, null, new String[] {Integer.toString(count)}));
      }

      private void waitItem(String id, int count) {
         if (ResourceLocation.tryParse(id) == null || count < 1 || count > 2304)
            throw new IllegalArgumentException("wait_item requires an item ID and count 1–2304");
         add(new Step(Type.WAIT_ITEM, 0, id, null, new String[] {Integer.toString(count)}));
      }

      private void particle(String id, double x, double y, double z, int count) {
         if (ResourceLocation.tryParse(id) == null || !Double.isFinite(x) || !Double.isFinite(y) || !Double.isFinite(z)
            || Math.abs(x) > 30000000 || Math.abs(y) > 30000000 || Math.abs(z) > 30000000
            || count < 1 || count > 200) {
            throw new IllegalArgumentException("spawn_particle requires particle ID, finite coordinates and count 1–200");
         }
         add(new Step(Type.PARTICLE, 0, id, null,
            new String[] {Double.toString(x), Double.toString(y), Double.toString(z), Integer.toString(count)}));
      }

      private void particleHere(String id, int count) {
         if (ResourceLocation.tryParse(id) == null || count < 1 || count > 200) {
            throw new IllegalArgumentException("spawn_particle_here requires particle ID and count 1–200");
         }
         add(new Step(Type.PARTICLE_HERE, 0, id, null, new String[] {"0", "0", "0", Integer.toString(count)}));
      }

      private void cameraShot(double x, double y, double z, double yaw, double pitch, int ticks) {
         if (!Double.isFinite(x) || !Double.isFinite(y) || !Double.isFinite(z)
            || Math.abs(x) > 30000000 || Math.abs(y) > 30000000 || Math.abs(z) > 30000000
            || !Double.isFinite(yaw) || !Double.isFinite(pitch) || Math.abs(yaw) > 3600
            || pitch < -90 || pitch > 90 || ticks < 2 || ticks > 72000) {
            throw new IllegalArgumentException("camera_shot requires finite coordinates, yaw within ±3600, pitch -90..90 and 2..72000 ticks");
         }
         add(new Step(Type.CAMERA_SHOT, 0, "", null, new String[] {
            Double.toString(x), Double.toString(y), Double.toString(z),
            Double.toString(yaw), Double.toString(pitch), Integer.toString(ticks)}));
      }

      private void cameraFlyNpc(String key, int flightTicks, int holdTicks, double distance) {
         if (!npcs.containsKey(key) || flightTicks < 2 || flightTicks > 1200
            || holdTicks < 0 || holdTicks > 72000 || !Double.isFinite(distance)
            || distance < 0.5 || distance > 8) {
            throw new IllegalArgumentException("camera_fly_npc requires a declared NPC, flight 2–1200 ticks, hold 0–72000 ticks and distance 0.5–8 blocks: " + key);
         }
         add(new Step(Type.CAMERA_FLY_NPC, 0, key, null,
            new String[] {Integer.toString(flightTicks), Integer.toString(holdTicks), Double.toString(distance)}));
      }

      private void npcAction(Type type, String key, PositionSpec position) {
         if (!npcs.containsKey(key)) {
            throw new IllegalArgumentException("NPC " + key + " must be defined before its actions in " + label);
         }
         add(new Step(type, 0, key, position));
      }

      private void npcBlockResult(String key, PositionSpec position, String result) {
         npcBlockResult(Type.NPC_TRY_USE_BLOCK, key, position, result);
      }

      private void npcBlockResult(Type type, String key, PositionSpec position, String result) {
         if (!npcs.containsKey(key)) throw new IllegalArgumentException("Unknown NPC for npc_try_use_block: " + key);
         add(new Step(type, 0, key, null, new String[] {result}, position));
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
         parts.forEach((partName, actions) -> {
            for (int index = 0; index < actions.size(); index++) {
               Step step = actions.get(index);
               if (step.type != Type.VAR_IF && step.type != Type.GOTO_ANCHOR) continue;
               String[] targets = step.type == Type.GOTO_ANCHOR ? new String[] {step.text} :
                  new String[] {step.arguments[1], step.arguments[2]};
               for (String anchor : targets) {
                  if (anchor.equals("final")) continue;
                  int found = -1;
                  for (int i = index + 1; i < actions.size(); i++) {
                     if (actions.get(i).type == Type.ANCHOR && anchor.equals(actions.get(i).text)) {
                        found = i;
                        break;
                     }
                  }
                  if (found < 0) throw new IllegalArgumentException("Forward anchor in " + partName + " is missing: " + anchor);
               }
            }
         });
         Map<String, List<Step>> frozen = new LinkedHashMap<>();
         parts.forEach((name, actions) -> frozen.put(name, List.copyOf(actions)));
         return new LuaStory(id, Map.copyOf(frozen), Map.copyOf(npcs));
      }
   }
}
