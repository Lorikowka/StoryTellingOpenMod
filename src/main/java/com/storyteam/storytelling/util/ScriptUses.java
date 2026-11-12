package com.storyteam.storytelling.util;

import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.client.Animations;
import com.storyteam.storytelling.client.particle.circleTint.CircleTintData;
import com.storyteam.storytelling.client.particle.controllers.GravityController;
import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.content.entities.HumanoidEntity;
import com.storyteam.storytelling.content.entities.mobs.BaseMob;
import com.storyteam.storytelling.events.listeners.ScriptIO;
import com.storyteam.storytelling.mixins.minecraft.ItemFrameAccessor;
import com.storyteam.storytelling.mixins.minecraft.MobEffectInstanceAccessor;
import com.storyteam.storytelling.net.PacketPlaySound;
import com.storyteam.storytelling.net.PacketSetPlayerDeltaMovement;
import com.storyteam.storytelling.net.PacketSetSelected;
import com.storyteam.storytelling.net.PacketTimeSet;
import com.storyteam.storytelling.script.ScriptInstance;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.action.HandleStateAction;
import com.storyteam.storytelling.script.performer.builder.ScriptPerformerBuilder;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.system._core.structio.StructIO;
import com.storyteam.storytelling.system._core.structio.repo.DownloadResult;
import com.storyteam.storytelling.system._core.structio.repo.DwnResult;
import com.storyteam.storytelling.system._core.structio.repo.StructureRepository;
import com.storyteam.storytelling.system.clothes.Outfit;
import com.storyteam.storytelling.system.clothes.client.VanityArmRenderingHelper;
import com.storyteam.storytelling.system.clothes.entity.VanitySlot;
import com.storyteam.storytelling.system.clothes.item.ItemVanityArmor;
import com.storyteam.storytelling.system.persona.Persona;
import com.storyteam.storytelling.system.reputation.ReputationManager;
import com.storyteam.storytelling.system.reputation.util.ChangeReason;
import com.storyteam.storytelling.util.data.EntityContext;
import com.storyteam.storytelling.util.world.Location;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.arguments.EntityAnchorArgument.Anchor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Component.Serializer;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.GameRules.BooleanValue;
import net.minecraft.world.level.GameRules.IntegerValue;
import net.minecraft.world.level.GameRules.Key;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import org.joml.Matrix4d;
import org.joml.Vector3d;
import org.zeith.hammeranims.api.animation.IAnimationSource;
import org.zeith.hammeranims.api.animsys.ConfiguredAnimation;
import org.zeith.hammeranims.api.animsys.SerializableMask;
import org.zeith.hammerlib.net.Network;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.shaded.json.JSONObject;

public class ScriptUses {
   public static Supplier<Outfit> playerOutfit() {
      return () -> {
         Outfit.OutfitBuilder b = Outfit.builder();
         ServerPlayer p = ScriptUtils.getPlayer();
         if (p != null) {
            b = b.nothing();

            for (ItemStack as : p.getArmorSlots()) {
               if (as.getItem() instanceof ItemVanityArmor iva) {
                  b.set(iva.getVanitySlot(), as.copy());
               }
            }
         }

         for (VanitySlot value : VanitySlot.values()) {
            ItemStack it = VanityArmRenderingHelper.get(value);
            if (!it.isEmpty()) {
               b.set(value, it.copy());
            }
         }

         return b.build();
      };
   }

   public static Supplier<Outfit> personaOutfit(Persona fursona) {
      return () -> {
         Outfit.OutfitBuilder b = Outfit.builder();
         ServerPlayer p = ScriptUtils.getPlayer();
         Inventory inv = new Inventory(p);
         inv.load(ScriptIO.getSubTag("persona").getCompound(fursona.name() + "_inventory").getList("Inventory", 10));
         if (p != null) {
            b = b.nothing();

            for (ItemStack as : inv.armor) {
               if (as.getItem() instanceof ItemVanityArmor iva) {
                  b.set(iva.getVanitySlot(), as.copy());
               }
            }
         }

         return b.build();
      };
   }

   public static SerializableMask maskNoArms() {
      return SerializableMask.builder()
         .exclude("LeftArm")
         .exclude("LeftHand")
         .exclude("Left_placeholder")
         .exclude("RightArm")
         .exclude("RightHand")
         .exclude("Right_placeholder")
         .build();
   }

   public static SerializableMask maskNoArmPlaceholders() {
      return SerializableMask.builder().exclude("Left_placeholder").exclude("Right_placeholder").build();
   }

   public static SerializableMask maskNoRightArm() {
      return SerializableMask.builder().exclude("RightArm").exclude("RightHand").exclude("Right_placeholder").build();
   }

   public static SerializableMask maskNoRightArmAndModel() {
      return SerializableMask.builder().exclude("model").exclude("RightArm").exclude("RightHand").exclude("Right_placeholder").build();
   }

   public static SerializableMask maskNoLeftArm() {
      return SerializableMask.builder().exclude("LeftArm").exclude("LeftHand").exclude("Left_placeholder").build();
   }

   public static SerializableMask maskNoRightArmAndHead() {
      return SerializableMask.builder().exclude("Head").exclude("RightArm").exclude("RightHand").exclude("Right_placeholder").build();
   }

   public static SerializableMask maskNoArmsAndFace() {
      return SerializableMask.builder()
         .exclude("LeftArm")
         .exclude("LeftHand")
         .exclude("Left_placeholder")
         .exclude("RightArm")
         .exclude("RightHand")
         .exclude("Right_placeholder")
         .exclude("eyebrow")
         .exclude("eyebrow_right")
         .exclude("eyebrow_left")
         .exclude("eye_right")
         .exclude("eye_left")
         .exclude("ocular_right")
         .exclude("ocular_left")
         .exclude("Mouth")
         .exclude("teath")
         .exclude("Eyes")
         .build();
   }

   public static SerializableMask maskNoRightPlaceholder() {
      return SerializableMask.builder().exclude("Right_placeholder").build();
   }

   public static SerializableMask maskNoHead() {
      return SerializableMask.builder().exclude("Head").build();
   }

   public static SerializableMask maskNoMouth() {
      return SerializableMask.builder().exclude("Mouth").build();
   }

   public static SerializableMask maskNoHeadAndArms() {
      return SerializableMask.builder()
         .exclude("Head")
         .exclude("LeftArm")
         .exclude("LeftHand")
         .exclude("Left_placeholder")
         .exclude("RightArm")
         .exclude("RightHand")
         .exclude("Right_placeholder")
         .build();
   }

   public static SerializableMask maskNoHeadNoBodyAndArms() {
      return SerializableMask.builder()
         .exclude("Head")
         .exclude("BodyUp")
         .exclude("Body")
         .exclude("LeftArm")
         .exclude("LeftHand")
         .exclude("Left_placeholder")
         .exclude("RightArm")
         .exclude("RightHand")
         .exclude("Right_placeholder")
         .build();
   }

   public static SerializableMask maskNoHeadNoBodyAndRightArm() {
      return SerializableMask.builder()
         .exclude("Head")
         .exclude("BodyUp")
         .exclude("Body")
         .exclude("RightArm")
         .exclude("RightHand")
         .exclude("Right_placeholder")
         .build();
   }

   public static SerializableMask maskNoHeadNoBodyAndLeftArm() {
      return SerializableMask.builder()
         .exclude("Head")
         .exclude("BodyUp")
         .exclude("Body")
         .exclude("LeftArm")
         .exclude("LeftHand")
         .exclude("Left_placeholder")
         .build();
   }

   public static SerializableMask maskNoEyes() {
      return SerializableMask.builder()
         .exclude("eyebrow")
         .exclude("eyebrow_right")
         .exclude("eyebrow_left")
         .exclude("eye_right")
         .exclude("eye_left")
         .exclude("ocular_right")
         .exclude("ocular_left")
         .exclude("Eyes")
         .build();
   }

   public static SerializableMask maskNoOculars() {
      return SerializableMask.builder().exclude("ocular_right").exclude("ocular_left").build();
   }

   public static SerializableMask maskNoRightEye() {
      return SerializableMask.builder().exclude("eyebrow").exclude("eyebrow_right").exclude("eye_right").exclude("ocular_right").build();
   }

   public static SerializableMask maskNoLeftEye() {
      return SerializableMask.builder().exclude("eyebrow").exclude("eyebrow_left").exclude("eye_left").exclude("ocular_left").build();
   }

   public static SerializableMask maskNoFace() {
      return SerializableMask.builder()
         .exclude("eyebrow")
         .exclude("eyebrow_right")
         .exclude("eyebrow_left")
         .exclude("eye_right")
         .exclude("eye_left")
         .exclude("ocular_right")
         .exclude("ocular_left")
         .exclude("Mouth")
         .exclude("teath")
         .exclude("Eyes")
         .build();
   }

   public static SerializableMask maskNoEyesNoHead() {
      return SerializableMask.builder()
         .exclude("Head")
         .exclude("eyebrow")
         .exclude("eyebrow_right")
         .exclude("eyebrow_left")
         .exclude("Eyes")
         .exclude("eye_right")
         .exclude("eye_left")
         .exclude("ocular_right")
         .exclude("ocular_left")
         .build();
   }

   public static SerializableMask maskNoLegs() {
      return SerializableMask.builder().exclude("Legs").exclude("LeftLeg").exclude("LeftFoot").exclude("RightLeg").exclude("RightFoot").build();
   }

   public static SerializableMask maskNoFoot() {
      return SerializableMask.builder().exclude("LeftFoot").exclude("RightFoot").build();
   }

   public static SerializableMask maskNoLegsNoArms() {
      return SerializableMask.builder()
         .exclude("Legs")
         .exclude("LeftLeg")
         .exclude("LeftFoot")
         .exclude("RightLeg")
         .exclude("RightFoot")
         .exclude("LeftArm")
         .exclude("LeftHand")
         .exclude("Left_placeholder")
         .exclude("RightArm")
         .exclude("RightHand")
         .exclude("Right_placeholder")
         .build();
   }

   public static SerializableMask maskNoLegsNoArmsNoHead() {
      return SerializableMask.builder()
         .exclude("Legs")
         .exclude("LeftLeg")
         .exclude("LeftFoot")
         .exclude("RightLeg")
         .exclude("RightFoot")
         .exclude("LeftArm")
         .exclude("LeftHand")
         .exclude("Left_placeholder")
         .exclude("RightArm")
         .exclude("RightHand")
         .exclude("Right_placeholder")
         .exclude("Head")
         .build();
   }

   public static SerializableMask maskNoLegsNoBody() {
      return SerializableMask.builder()
         .exclude("Legs")
         .exclude("LeftLeg")
         .exclude("LeftFoot")
         .exclude("RightLeg")
         .exclude("RightFoot")
         .exclude("BodyUp")
         .exclude("Body")
         .build();
   }

   public static SerializableMask maskNoLegsNoModelNoArms() {
      return SerializableMask.builder()
         .exclude("model")
         .exclude("Legs")
         .exclude("LeftLeg")
         .exclude("LeftFoot")
         .exclude("RightLeg")
         .exclude("RightFoot")
         .exclude("BodyUp")
         .exclude("Body")
         .exclude("LeftArm")
         .exclude("LeftHand")
         .exclude("Left_placeholder")
         .exclude("RightArm")
         .exclude("RightHand")
         .exclude("Right_placeholder")
         .build();
   }

   public static SerializableMask maskNoLegsNoBodyNoModel() {
      return SerializableMask.builder()
         .exclude("model")
         .exclude("Legs")
         .exclude("LeftLeg")
         .exclude("LeftFoot")
         .exclude("RightLeg")
         .exclude("RightFoot")
         .exclude("BodyUp")
         .exclude("Body")
         .build();
   }

   public static SerializableMask maskNoLegsNoModel() {
      return SerializableMask.builder()
         .exclude("model")
         .exclude("Legs")
         .exclude("LeftLeg")
         .exclude("LeftFoot")
         .exclude("RightLeg")
         .exclude("RightFoot")
         .build();
   }

   public static SerializableMask maskNoModel() {
      return SerializableMask.builder().exclude("model").build();
   }

   public static SerializableMask maskNoLegsNoBodyAndLeftArm() {
      return SerializableMask.builder()
         .exclude("Legs")
         .exclude("LeftLeg")
         .exclude("LeftFoot")
         .exclude("RightLeg")
         .exclude("RightFoot")
         .exclude("BodyUp")
         .exclude("Body")
         .exclude("LeftArm")
         .exclude("LeftHand")
         .exclude("Left_placeholder")
         .build();
   }

   public static SerializableMask maskNoLegsNoBodyAndRightArm() {
      return SerializableMask.builder()
         .exclude("Legs")
         .exclude("LeftLeg")
         .exclude("LeftFoot")
         .exclude("RightLeg")
         .exclude("RightFoot")
         .exclude("BodyUp")
         .exclude("Body")
         .exclude("RightArm")
         .exclude("RightHand")
         .exclude("Right_placeholder")
         .build();
   }

   public static SerializableMask maskNoBodyAndRightHand() {
      return SerializableMask.builder().exclude("BodyUp").exclude("Body").exclude("RightHand").exclude("Right_placeholder").build();
   }

   public static SerializableMask maskNoBodyAndArms() {
      return SerializableMask.builder()
         .exclude("BodyUp")
         .exclude("Body")
         .exclude("LeftArm")
         .exclude("LeftHand")
         .exclude("Left_placeholder")
         .exclude("RightArm")
         .exclude("RightHand")
         .exclude("Right_placeholder")
         .build();
   }

   public static SerializableMask maskNoHeadAndLeftArm() {
      return SerializableMask.builder().exclude("Head").exclude("LeftArm").exclude("LeftHand").exclude("Left_placeholder").build();
   }

   public static SerializableMask maskNoHeadAndRightArm() {
      return SerializableMask.builder().exclude("Head").exclude("RightArm").exclude("RightHand").exclude("Right_placeholder").build();
   }

   public static Consumer<Entity> skintTpFx() {
      return e -> {
         for (int i = 0; i < 2; i++) {
            ParticleHelper.spawnParticleAABB(
               e.level(),
               new CircleTintData(new Color(255, 179, 62), 0.17F, 40, -1.0F, true, false, new GravityController()),
               e.getBoundingBox().inflate(0.1, 0.0, 0.1),
               80,
               0.086666666F
            );
            ParticleHelper.spawnParticleAABB(
               e.level(),
               new CircleTintData(new Color(255, 159, 62), 0.17F, 40, -1.0F, true, false, new GravityController()),
               e.getBoundingBox().inflate(0.1, 0.0, 0.1),
               80,
               0.086666666F
            );
         }
      };
   }

   public static Supplier<Boolean> playerCloseBy(String location, double distance) {
      Location l = new Location(location);
      return () -> {
         ServerPlayer p = ScriptUtils.getPlayer();
         return p != null && l.closerThan(p, distance);
      };
   }

   public static Consumer<ScriptPerformerBuilder> takeItemFromPocket(Character<? extends AnimatedEntity> c, ItemStack stack) {
      return p -> p.action(c, Animations.BAG_SEARCH_IN_BAG).thenDelay(25).thenSetHeldItem(c, stack).thenDelay(15).stopAction(c);
   }

   public static Consumer<ScriptPerformerBuilder> takeItemFromPocket(Character<? extends HumanoidEntity> c, ItemStack stack, IAnimationSource afterAnim) {
      return p -> p.action(c, Animations.BAG_SEARCH_IN_BAG).thenDelay(25).thenSetHeldItem(c, stack).thenDelay(15).action(c, afterAnim);
   }

   public static Consumer<ScriptPerformerBuilder> takeItemFromPocket(Character<? extends HumanoidEntity> c, ItemStack stack, ConfiguredAnimation afterAnim) {
      return p -> p.action(c, Animations.BAG_SEARCH_IN_BAG).thenDelay(25).thenSetHeldItem(c, stack).thenDelay(15).action(c, afterAnim);
   }

   @SafeVarargs
   public static <T> Consumer<ScriptPerformerBuilder> createDivided(AtomicInteger counter, T input, BiConsumer<T, ScriptPerformerBuilder>... pbs) {
      return builder -> {
         int i = counter.getAndIncrement();
         BiConsumer<T, ScriptPerformerBuilder> c = pbs[i % pbs.length];
         c.accept(input, builder);
      };
   }

   public static Consumer<ScriptPerformerBuilder> initializeReputation(Character<?> ch, int baseline) {
      return b -> b.thenHandleState(new HandleStateAction.StateHandler() {
            @Override
            public void onPassed() {
               ReputationManager.getOrEnableForCharacter(ch).setBaseline(baseline);
            }

            @Override
            public void onRewind() {
               ReputationManager.deleteForCharacter(ch);
            }

            @Override
            public void globalFastForward() {
               this.onPassed();
            }
         });
   }

   public static Consumer<ScriptPerformerBuilder> clearRepMod(Character<?> ch, String mod) {
      return b -> b.thenHandleState(new HandleStateAction.StateHandler() {
            @Override
            public void onPassed() {
               ReputationManager.revokeReputation(mod, ch);
            }

            @Override
            public void globalFastForward() {
               this.onPassed();
            }
         });
   }

   public static <T extends AnimatedEntity> Consumer<ScriptPerformerBuilder> animationEachCord(
      Character<T> character, int minDelay, int maxDelay, ConfiguredAnimation animation, String... paths
   ) {
      List<List<Location>> cords = Arrays.stream(paths).map(s -> Arrays.stream(s.split("\n")).map(Location::new).toList()).toList();
      return b -> cords.forEach(cord -> {
            b.thenEntityPath(character, 0.3, () -> cord);
            b.entityLookAtFromRot(character, () -> (Location)cord.get(cord.size() - 1), 100.0, true);
            b.thenDelay(50);
            b.action(character, animation);
            b.thenDelay(minDelay, maxDelay, b.getPerformer().script.getRandom());
            b.stopAction(character);
         });
   }

   public static <T extends AnimatedEntity> Consumer<ScriptPerformerBuilder> fumblingForEachCord(
      Character<T> character, int minDelay, int maxDelay, String... paths
   ) {
      return animationEachCord(character, minDelay, maxDelay, Animations.HANDS_FUMBLING_DOWN.configure(), paths);
   }

   public static Consumer<ScriptPerformerBuilder> addRepMod(Character<?> ch, String mod, int repAmount) {
      return b -> b.thenRun(() -> ReputationManager.addReputation(mod, ch, repAmount, ChangeReason.EXTERNAL, true))
            .thenHandleState(new HandleStateAction.StateHandler() {
               @Override
               public void onPassed() {
                  ReputationManager.addReputation(mod, ch, repAmount, ChangeReason.EXTERNAL, false);
               }

               @Override
               public void globalFastForward() {
                  this.onPassed();
               }
            });
   }

   public static Consumer<? super HumanoidEntity> spitFX() {
      return spitFX(1.0F);
   }

   public static Consumer<? super HumanoidEntity> spitFX(float scale) {
      return ent -> {
         Matrix4d tfm = ent.getTransforms("Mouth", false).orElse(null);
         if (tfm != null) {
            Vector3d pos = tfm.transformPosition(new Vector3d(0.0, 0.0, 0.0));
            Vector3d mov = tfm.transformPosition(new Vector3d(0.0, -1.0, -3.0)).sub(pos).normalize().mul(0.5);
            ParticleHelper.spawnEnginedParticle(
               ScriptUtils.getLevel(), ParticleTypes.SPIT, new Vec3(pos.x, pos.y, pos.z), new Vec3(mov.x, mov.y, mov.z), scale, 40, Color.WHITE, 1.0F
            );
         }
      };
   }

   @Deprecated
   public static void cryFX(ScriptInstance instance, ServerPlayer player, Character<? extends HumanoidEntity> character, int rate) {
      HumanoidEntity e = instance.getEntity(character);
      if (e != null && instance.ticks % (long)rate == 0L) {
         RandomSource random = player.getRandom();
         Matrix4d tfm;
         if ((tfm = e.getTransforms(random.nextBoolean() ? "ocular_right" : "ocular_left", false).orElse(null)) != null) {
            Vector3d basePos = new Vector3d(0.0, -0.125, -0.0625);
            Vector3d pos = tfm.transformPosition(basePos);
            player.serverLevel().sendParticles(ParticleTypes.FALLING_WATER, pos.x, pos.y, pos.z, 1, 0.0, 0.0, 0.0, 0.0);
         }
      }
   }

   public static void cryIntenseFXB(ScriptInstance instance, ServerPlayer player, Character<? extends BaseMob> character) {
      cryIntenseFXB(instance, player, character, Vec3.ZERO);
   }

   public static void cryIntenseFXB(ScriptInstance instance, ServerPlayer player, Character<? extends BaseMob> character, Vec3 offset) {
      BaseMob e = instance.getEntity(character);
      if (e != null) {
         Matrix4d tfm;
         if ((tfm = e.getTransforms("ocular_right", false).orElse(null)) != null) {
            Vector3d basePos1 = new Vector3d(0.040625F, -0.0, -0.00625F);
            Vector3d pos1 = tfm.transformPosition(basePos1).add(offset.x(), offset.y(), offset.z());
            ParticleHelper.spawnParticles(player.serverLevel(), ParticleTypes.UNDERWATER, pos1.x, pos1.y + 0.195, pos1.z, 1, 0.015, 0.0, 0.015, 0.0);
         }

         if ((tfm = e.getTransforms("ocular_left", false).orElse(null)) != null) {
            Vector3d basePos2 = new Vector3d(-0.040625F, -0.0, -0.00625F);
            Vector3d pos2 = tfm.transformPosition(basePos2).add(offset.x(), offset.y(), offset.z());
            ParticleHelper.spawnParticles(player.serverLevel(), ParticleTypes.UNDERWATER, pos2.x, pos2.y + 0.195, pos2.z, 1, 0.015, 0.0, 0.015, 0.0);
         }
      }
   }

   public static void cryIntenseFX(ScriptInstance instance, ServerPlayer player, Character<? extends HumanoidEntity> character) {
      HumanoidEntity e = instance.getEntity(character);
      if (e != null) {
         Vec3 eyeRight = Vec3.ZERO;
         Optional<Matrix4d> eyeRightOpt = e.getTransforms("ocular_right", false);
         if (eyeRightOpt.isPresent()) {
            Vector3d eye3d = eyeRightOpt.get().transformPosition(new Vector3d(0.040625F, -0.0, -0.00625F));
            Optional<Vec3> eyeOpt = Optional.of(new Vec3(eye3d.x(), eye3d.y(), eye3d.z()));
            eyeRight = eyeOpt.get();
         }

         ParticleHelper.spawnEnginedParticle(
            player.serverLevel(),
            ParticleTypes.UNDERWATER,
            eyeRight.add(ParticleHelper.rng.nextDouble(-0.015, 0.015), 0.08, ParticleHelper.rng.nextDouble(-0.015, 0.015)),
            new Vec3(0.0, 0.0, 0.0),
            1.0F,
            10,
            new Color(0.4F, 0.4F, 0.7F),
            0.1F
         );
         Vec3 eyeLeft = Vec3.ZERO;
         Optional<Matrix4d> eyeLeftOpt = e.getTransforms("ocular_left", false);
         if (eyeLeftOpt.isPresent()) {
            Vector3d eye3d = eyeLeftOpt.get().transformPosition(new Vector3d(-0.040625F, -0.0, -0.00625F));
            Optional<Vec3> eyeOpt = Optional.of(new Vec3(eye3d.x(), eye3d.y(), eye3d.z()));
            eyeLeft = eyeOpt.get();
         }

         ParticleHelper.spawnEnginedParticle(
            player.serverLevel(),
            ParticleTypes.UNDERWATER,
            eyeLeft.add(ParticleHelper.rng.nextDouble(-0.015, 0.015), 0.08, ParticleHelper.rng.nextDouble(-0.015, 0.015)),
            new Vec3(0.0, 0.0, 0.0),
            1.0F,
            10,
            new Color(0.4F, 0.4F, 0.7F),
            0.1F
         );
      }
   }

   public static Consumer<ScriptPerformerBuilder> joinParallelPerformers(ScriptInstance inst, String... performers) {
      if (performers.length == 0) {
         return pb -> {
         };
      } else {
         BiConsumer<String, ScriptPerformerBuilder> waiter = (pn, pb) -> pb.thenWaitUntilWithInfo(() -> {
               ScriptPerformer pp = inst.getParallelPerformer(pn);
               return pp == null || pp.isCompleted();
            }, Cast.constant("Ожидание завершения подскрипта " + JSONObject.quote(pn))).removeParallelPerformer(pn);
         return performers.length == 1 ? pb -> waiter.accept(performers[0], pb) : pb -> pb.thenAllPerformers(Arrays.stream(performers).map(pn -> {
               ScriptPerformerBuilder pb2 = new ScriptPerformerBuilder(inst);
               waiter.accept(pn, pb2);
               return pb2.build();
            }).toArray(ScriptPerformer[]::new));
      }
   }

   public static Consumer<ScriptPerformerBuilder> predownloadStructures(String... structures) {
      return b -> {
         final Function<Boolean, CompletableFuture<?>> downloader = quickCheck -> StructureRepository.downloadStructures(
                  b.getPerformer().script.registryAccess(), List.of(structures), quickCheck, msg -> {
                     if (!quickCheck) {
                        for (String id : structures) {
                           msg = msg.replace(id, "§7\"" + id + "\"§r");
                        }

                        ScriptUtils.sendMessageToPlayers("§6§l[§e§l>§6§l] §r" + msg);
                     }
                  }
               )
               .handle((res, err) -> {
                  if (err != null) {
                     ScriptUtils.sendMessageToPlayers("Storytelling-Cloud", Component.literal("ОШИБКА ЗАГРУЗКИ " + structures.length + " СТРУКТУР..."));
                     return null;
                  } else if (quickCheck) {
                     return null;
                  } else {
                     long downloaded = res.values().stream().filter(DownloadResult::wasDownloaded).count();
                     ScriptUtils.sendMessageToPlayers("Storytelling-Cloud", Component.literal(downloaded + "/" + res.size() + " структур загружено!"));

                     for (Entry<String, DownloadResult> e : res.entrySet()) {
                        if (!e.getValue().wasDownloaded() && e.getValue().result() != DwnResult.EXISTS) {
                           ScriptUtils.sendMessageToPlayers("Storytelling-Cloud", Component.literal("- " + e.getKey() + ": " + e.getValue().result()));
                        }
                     }

                     return null;
                  }
               });
         b.thenHandleState(new HandleStateAction.StateHandler() {
            @Override
            public void globalFastForward() {
               downloader.apply(true);
            }
         });
         b.thenRun(() -> downloader.apply(false).join());
      };
   }

   public static Consumer<ScriptPerformerBuilder> createStructureImportSetup(String... structures) {
      return b -> {
         AtomicReference<Location> playerPos = new AtomicReference<>();
         AtomicReference<GameType> gameMode = new AtomicReference<>();
         b.appendBuilder(predownloadStructures(structures));
         b.thenSetSpeechEntity(0, "Storytelling", "setup").thenUsePlayer(p -> {
            playerPos.set(new Location(p));
            gameMode.set(p.gameMode.getGameModeForPlayer());
            p.setGameMode(GameType.SPECTATOR);
            ((BooleanValue)p.server.getGameRules().getRule(GameRules.RULE_DOBLOCKDROPS)).set(false, p.server);
            ((BooleanValue)p.server.getGameRules().getRule(GameRules.RULE_DOENTITYDROPS)).set(false, p.server);
         });

         for (int i = 0; i < structures.length; i++) {
            String id = structures[i];
            b.thenSpeechMessage(0, "[" + (i + 1) + "/" + structures.length + "] Импорт " + structures[i] + "...", false)
               .thenUsePlayer(
                  player -> StructIO.importBuilding(player.server, (aabb, level) -> new Location(level, aabb.getCenter()).teleport(player), id)
                        .thenAccept(bounds -> {
                           if (bounds != null && bounds.b() != null && bounds.a() != null) {
                              for (ItemEntity entitiesOfClass : ((ServerLevel)bounds.b())
                                 .getEntitiesOfClass(ItemEntity.class, ((AABB)bounds.a()).inflate(32.0))) {
                                 entitiesOfClass.kill();
                              }
                           }
                        })
               )
               .thenDelay(80);
         }

         b.thenSpeechMessage(0, ChatFormatting.GREEN + "Готово!", false).teleportPlayer(playerPos::get).thenUsePlayer(p -> {
            ((BooleanValue)p.server.getGameRules().getRule(GameRules.RULE_DOBLOCKDROPS)).set(true, p.server);
            ((BooleanValue)p.server.getGameRules().getRule(GameRules.RULE_DOENTITYDROPS)).set(true, p.server);
            p.setGameMode(gameMode.get());
         }).thenDelay(20).clearChat();
      };
   }

   public static ItemStack rename(ItemStack stack, String name) {
      return rename(stack, Component.literal(name));
   }

   public static ItemStack rename(ItemStack stack, Component name) {
      CompoundTag display = stack.getOrCreateTagElement("display");
      display.putString("Name", Serializer.toJson(name));
      return stack;
   }

   public static ItemStack withLore(ItemStack stack, String... lines) {
      CompoundTag display = stack.getOrCreateTagElement("display");
      ListTag lore = display.getList("Lore", 8);

      for (String ln : lines) {
         lore.add(StringTag.valueOf(Serializer.toJson(Component.literal(ln))));
      }

      display.put("Lore", lore);
      return stack;
   }

   public static ItemStack withLore(ItemStack stack, Component... lines) {
      CompoundTag display = stack.getOrCreateTagElement("display");
      ListTag lore = display.getList("Lore", 8);

      for (Component ln : lines) {
         lore.add(StringTag.valueOf(Serializer.toJson(ln)));
      }

      display.put("Lore", lore);
      return stack;
   }

   public static Consumer<ServerPlayer> timeSet(long time) {
      return p -> {
         for (ServerLevel serverlevel : p.getServer().getAllLevels()) {
            serverlevel.setDayTime(time);
         }

         Network.sendToAll(new PacketTimeSet(time));
      };
   }

   public static Consumer<ScriptPerformerBuilder> approachPlayer(Character<? extends Mob> ch, double speed) {
      return b -> b.thenEntityFollowPlayer(ch, speed, 0.0, 48.0)
            .thenWhenPlayerCloseTo(ch, 2.0)
            .thenEntityMove(ch, 0.08 * speed / 0.3, playerPos())
            .thenDelay(1)
            ._parallelAny()
            .thenClearEntityGoal(ch)
            .entityLookAt(ch, "player", true);
   }

   public static Consumer<ScriptPerformerBuilder> approachPlayerTight(Character<? extends Mob> ch, double speed) {
      return b -> b.thenEntityFollowPlayer(ch, speed, 0.0, 48.0)
            .thenWhenPlayerCloseTo(ch, 2.0)
            .thenEntityMove(ch, 0.08 * speed / 0.3, playerPos())
            .thenDelay(8)
            ._parallelAny()
            .thenClearEntityGoal(ch)
            .entityLookAt(ch, "player", true);
   }

   public static Supplier<Location> moveBackwards(Character<?> charr, float by) {
      AtomicReference<Location> lastPos = new AtomicReference<>();
      return () -> {
         Entity repe = charr.repositoryEntity();
         if (repe == null) {
            return null;
         } else {
            Location savedPos = lastPos.get();
            if (savedPos == null) {
               Vec3 moveBy = repe.position().subtract(ScriptUtils.getPlayer().position()).multiply(1.0, 0.0, 1.0).normalize().scale((double)by);
               savedPos = new Location(repe.level(), repe.position().add(moveBy));
               lastPos.set(savedPos);
            }

            return savedPos;
         }
      };
   }

   public static Supplier<Location> moveBackwards(Character<?> charr, Character<?> from, float by) {
      AtomicReference<Location> lastPos = new AtomicReference<>();
      return () -> {
         Entity repe = charr.repositoryEntity();
         Entity fromEntity = from.repositoryEntity();
         if (repe == null) {
            return null;
         } else {
            Location savedPos = lastPos.get();
            if (fromEntity == null) {
               return new Location(repe);
            } else {
               if (savedPos == null) {
                  Vec3 moveBy = repe.position().subtract(fromEntity.position()).normalize().scale((double)by);
                  savedPos = new Location(repe.level(), repe.position().add(moveBy));
                  lastPos.set(savedPos);
               }

               return savedPos;
            }
         }
      };
   }

   public static <T extends Mob> Consumer<T> stopNavigator() {
      return m -> m.getNavigation().stop();
   }

   public static Consumer<? super Entity> killItemsAround(double range) {
      return p -> p.level().getEntitiesOfClass(ItemEntity.class, new AABB(p.blockPosition()).inflate(range)).forEach(Entity::kill);
   }

   public static Consumer<? super Entity> killItemsAround(Vec3 vec3, double range) {
      return p -> p.level()
            .getEntitiesOfClass(ItemEntity.class, new AABB(new BlockPos((int)vec3.x(), (int)vec3.y(), (int)vec3.z())).inflate(range))
            .forEach(Entity::kill);
   }

   public static Supplier<Location> playerPos() {
      return () -> new Location(ScriptUtils.getPlayer());
   }

   public static Supplier<Location> charPos(Character<?> c) {
      return () -> {
         Entity e = c.repositoryEntity();
         return e != null ? new Location(e.getEyePosition(1.0F)) : null;
      };
   }

   public static Supplier<Location> charPosAtEyeLevel(Character<?> c) {
      return () -> {
         Entity e = c.repositoryEntity();
         return e != null ? new Location(e.position().add(0.0, (double)e.getEyeHeight(), 0.0)) : null;
      };
   }

   public static Supplier<Location> shiftBy(Supplier<Location> origin, Vec3 by) {
      return () -> {
         Location l = origin.get();
         if (l != null) {
            l = l.add(by);
         }

         return l;
      };
   }

   public static Consumer<ScriptPerformerBuilder> entitySitOnEntity(Character<?> ent, Character<?> vehicle) {
      return $ -> $.thenClearEntityGoal(vehicle).thenStaticEntityState(vehicle, Entity::stopRiding).thenHandleEntityState(ent, Entity::stopRiding, e -> {
            if (vehicle.repositoryEntity() != null) {
               e.startRiding(vehicle.repositoryEntity());
            }
         });
   }

   public static Consumer<AnimatedEntity> setEntityOffset(double x, double y, double z) {
      return e -> e.setPassengerOffset(x, y, z);
   }

   public static Consumer<ScriptPerformerBuilder> playerSitOnEntity(Character<?> vehicle) {
      return $ -> $.thenUseEntity(vehicle, e -> {
            e.stopRiding();
            ScriptUtils.getPlayer().startRiding(e);
         });
   }

   public static Consumer<ScriptPerformerBuilder> waitForPlayerToSitOnEntity(Character<?> vehicle) {
      return $ -> $.thenAwaitEvent(EventType.ENTITY_CLICK, e -> {
            if (e.isOf(vehicle)) {
               e.player.startRiding(e.target);
               Network.swingHand(e.player, InteractionHand.MAIN_HAND);
               return true;
            } else {
               return false;
            }
         });
   }

   public static Supplier<Location> bonePosition(Character<? extends HumanoidEntity> owner, String bone, Vector3d offset, boolean useBodyRotation) {
      return () -> {
         HumanoidEntity ent = (HumanoidEntity)owner.repositoryEntity();
         if (ent == null) {
            return null;
         } else {
            Matrix4d tfmo = ent.getTransforms(bone, useBodyRotation).orElse(null);
            if (tfmo == null) {
               return null;
            } else {
               Vector3d vec = tfmo.transformPosition(offset, new Vector3d());
               return new Location(ent.level().dimension(), vec.x, vec.y, vec.z, ent.getYRot(), ent.getXRot());
            }
         }
      };
   }

   public static Consumer<ScriptPerformerBuilder> dropToPlayerFromHandAndWaitPickup(
      Character<? extends HumanoidEntity> dropper, InteractionHand hand, ItemStack drop
   ) {
      return dropToPlayerFromHandAndWaitPickup(dropper, hand, drop::copy, null, null);
   }

   public static Consumer<ScriptPerformerBuilder> dropToPlayerFromHandAndWaitPickup(
      Character<? extends HumanoidEntity> dropper, InteractionHand hand, Supplier<ItemStack> drop, Consumer<ItemEntity> setup, Consumer<ItemEntity> tick
   ) {
      return builder -> {
         AtomicReference<ItemEntity> var = new AtomicReference<>();
         builder.thenUseEntity(dropper, e -> {
            Vec3 offset = Vec3.ZERO;
            Optional<Matrix4d> tfmo = e.getTransforms(hand == InteractionHand.MAIN_HAND ? "Right_placeholder" : "Left_placeholder", true);
            if (tfmo.isPresent()) {
               Vector3d basePos = new Vector3d(0.0, 0.0625, -0.125);
               Vector3d pos = tfmo.orElse(null).transformPosition(basePos);
               Vec3 p = e.position();
               double d0 = e.getEyeY() - 0.3F;
               offset = new Vec3(pos.x, pos.y, pos.z).subtract(p.x, d0, p.z);
            }

            ItemEntity ie = dropItemTowards(e, ScriptUtils.getPlayer(), drop.get().copy(), false, false, Vec3.ZERO, offset);
            if (setup != null) {
               setup.accept(ie);
            }

            var.set(ie);
         }).thenWaitUntilWithInfo(() -> {
            ItemEntity ie = var.get();
            if (tick != null && ie != null && ie.isAlive() && ie.isAddedToWorld()) {
               tick.accept(ie);
            }

            return ie == null || !ie.isAlive();
         }, () -> "Ожидается подбор " + drop.get().getDisplayName().getString()).thenRun(() -> var.set(null));
      };
   }

   public static Consumer<ScriptPerformerBuilder> dropToPlayerAndWaitPickup(Character<?> dropper, ItemStack drop) {
      return builder -> {
         AtomicReference<ItemEntity> var = new AtomicReference<>();
         builder.thenUseEntity(dropper, e -> {
            ItemEntity ie = dropItemTowards(e, ScriptUtils.getPlayer(), drop.copy(), false, false);
            var.set(ie);
         }).thenWaitUntilWithInfo(() -> {
            ItemEntity ie = var.get();
            return ie == null || !ie.isAlive();
         }, () -> "Ожидается подбор " + drop.getDisplayName().getString()).thenRun(() -> var.set(null));
      };
   }

   public static Consumer<AnimatedEntity> setOpenDoors(boolean state) {
      return e -> e.canOpenDoors = state;
   }

   public static Consumer<AnimatedEntity> set(boolean state) {
      return e -> e.canOpenDoors = state;
   }

   public static Consumer<ScriptPerformerBuilder> dropToCharAndWaitPickup(Character<?> dropper, Character<? extends Mob> picker, ItemStack drop) {
      return builder -> {
         AtomicReference<ItemEntity> var = new AtomicReference<>();
         builder.thenUseEntity(picker, o -> {
         }).thenUseEntity(dropper, o -> {
         }).thenWaitUntil(() -> {
            Entity d = dropper.repositoryEntity();
            Entity e = picker.repositoryEntity();
            if (d != null && e != null) {
               ItemEntity ie = dropItemTowards(d, e, drop.copy(), false, false);
               ie.setNeverPickUp();
               ie.lifespan = 42;
               var.set(ie);
               return true;
            } else {
               return false;
            }
         }).thenWaitUntilWithInfo(() -> {
            Mob e = (Mob)picker.repositoryEntity();
            ItemEntity ie = var.get();
            if (ie != null && e != null && ie.tickCount > 40) {
               e.take(ie, ie.getItem().getCount());
               return true;
            } else {
               return ie == null || !ie.isAlive();
            }
         }, () -> "Ожидается подбор " + drop.getDisplayName().getString()).thenRun(() -> var.set(null));
      };
   }

   public static ItemEntity dropItem(Entity thi, ItemStack droppedItem_, boolean dropAround_, boolean traceItem_) {
      if (droppedItem_.isEmpty()) {
         return null;
      } else {
         double d0 = thi.getEyeY() - 0.3F;
         ItemEntity itementity = new ItemEntity(thi.level(), thi.getX(), d0, thi.getZ(), droppedItem_);
         itementity.setPickUpDelay(40);
         if (traceItem_) {
            itementity.setThrower(thi.getUUID());
         }

         if (dropAround_) {
            Random rng = new Random();
            float f = rng.nextFloat() * 0.5F;
            float f1 = rng.nextFloat() * (float) (Math.PI * 2);
            itementity.setDeltaMovement((double)(-Mth.sin(f1) * f / 7.0F), 0.2F, (double)(Mth.cos(f1) * f / 7.0F));
         } else {
            Player player = thi.level().getNearestPlayer(thi, 16.0);
            Vec3 motion;
            if (player != null) {
               motion = player.position().subtract(thi.position()).normalize().scale(0.36F);
            } else {
               motion = thi.getLookAngle().scale(0.6F);
            }

            itementity.setDeltaMovement(motion);
         }

         thi.level().addFreshEntity(itementity);
         return itementity;
      }
   }

   public static ItemEntity dropItem(Vec3 start, Vec3 end, Level world, ItemStack droppedItem_, double speed) {
      if (droppedItem_.isEmpty()) {
         return null;
      } else {
         ItemEntity itementity = new ItemEntity(world, start.x, start.y, start.z, droppedItem_);
         itementity.setPickUpDelay(40);
         itementity.setDeltaMovement(end.subtract(start).normalize().scale(speed));
         world.addFreshEntity(itementity);
         return itementity;
      }
   }

   public static ItemEntity dropItem(Vec3 pos, Level world, ItemStack droppedItem_, boolean dropAround_, boolean traceItem_) {
      if (droppedItem_.isEmpty()) {
         return null;
      } else {
         ItemEntity itementity = new ItemEntity(world, pos.x, pos.y, pos.z, droppedItem_);
         itementity.setPickUpDelay(40);
         if (dropAround_) {
            Random rng = new Random();
            float f = rng.nextFloat() * 0.5F;
            float f1 = rng.nextFloat() * (float) (Math.PI * 2);
            itementity.setDeltaMovement((double)(-Mth.sin(f1) * f), 0.2F, (double)(Mth.cos(f1) * f));
         } else {
            Player player = world.getNearestPlayer(pos.x, pos.y, pos.z, 16.0, true);
            Vec3 motion;
            if (player != null) {
               motion = player.position().subtract(pos).normalize().scale(0.36F);
            } else {
               motion = ScriptUtils.getPlayer().position().subtract(pos).normalize().scale(0.36F);
            }

            itementity.setDeltaMovement(motion);
         }

         world.addFreshEntity(itementity);
         return itementity;
      }
   }

   public static ItemEntity dropItemTowards(Entity thi, Entity towards, ItemStack droppedItem_, boolean dropAround_, boolean traceItem_) {
      return dropItemTowards(thi, towards, droppedItem_, dropAround_, traceItem_, Vec3.ZERO, Vec3.ZERO);
   }

   public static ItemEntity dropItemTowards(
      Entity thi, Entity towards, ItemStack droppedItem_, boolean dropAround_, boolean traceItem_, Vec3 modifer, Vec3 addPoint
   ) {
      if (droppedItem_.isEmpty()) {
         return null;
      } else {
         double d0 = thi.getEyeY() - 0.3F;
         ItemEntity drop = new ItemEntity(thi.level(), thi.getX() + addPoint.x, d0 + addPoint.y, thi.getZ() + addPoint.z, droppedItem_);
         drop.setPickUpDelay(40);
         if (traceItem_) {
            drop.setThrower(thi.getUUID());
         }

         if (dropAround_) {
            Random rng = new Random();
            float f = rng.nextFloat() * 0.5F;
            float f1 = rng.nextFloat() * (float) (Math.PI * 2);
            drop.setDeltaMovement((double)(-Mth.sin(f1) * f), 0.2F, (double)(Mth.cos(f1) * f));
         } else {
            Vec3 motion;
            if (thi == towards) {
               motion = Vec3.ZERO;
            } else if (towards != null) {
               motion = towards.position().subtract(drop.position()).normalize().scale(0.36F);
            } else {
               motion = thi.getLookAngle().scale(0.6F);
            }

            motion = motion.add(modifer);
            drop.setDeltaMovement(motion);
         }

         thi.level().addFreshEntity(drop);
         return drop;
      }
   }

   public static ItemEntity dropItemTowards(
      Entity thi, Vec3 towards, ItemStack droppedItem_, boolean dropAround_, boolean traceItem_, Vec3 modifer, Vec3 addPoint
   ) {
      if (droppedItem_.isEmpty()) {
         return null;
      } else {
         double d0 = thi.getEyeY() - 0.3F;
         ItemEntity drop = new ItemEntity(thi.level(), thi.getX() + addPoint.x, d0 + addPoint.y, thi.getZ() + addPoint.z, droppedItem_);
         drop.setPickUpDelay(40);
         if (traceItem_) {
            drop.setThrower(thi.getUUID());
         }

         if (dropAround_) {
            Random rng = new Random();
            float f = rng.nextFloat() * 0.5F;
            float f1 = rng.nextFloat() * (float) (Math.PI * 2);
            drop.setDeltaMovement((double)(-Mth.sin(f1) * f), 0.2F, (double)(Mth.cos(f1) * f));
         } else {
            Vec3 motion;
            if (towards != null) {
               motion = towards.subtract(drop.position()).normalize().scale(0.36F);
            } else {
               motion = thi.getLookAngle().scale(0.6F);
            }

            motion = motion.add(modifer);
            drop.setDeltaMovement(motion);
         }

         thi.level().addFreshEntity(drop);
         return drop;
      }
   }

   public static Runnable doCommand(ScriptInstance context, String command) {
      return () -> context.runCommand(command);
   }

   public static Consumer<HumanoidEntity> addEffect(MobEffectInstance instance) {
      return e -> e.addEffect(instance);
   }

   public static Runnable addInvisibleEffect(MobEffect effect, int ticks) {
      return addInvisibleEffect(effect, ticks, 0);
   }

   public static Runnable addInvisibleEffect(MobEffect effect, int ticks, int amplifier) {
      return () -> {
         ServerPlayer p = ScriptUtils.getPlayer();
         MobEffectInstance ae = (MobEffectInstance)p.getActiveEffectsMap().get(effect);
         if (ae != null) {
            ((MobEffectInstanceAccessor)ae).setDuration(ticks);
            ((MobEffectInstanceAccessor)ae).setAmplifier(amplifier);
            p.connection.send(new ClientboundUpdateMobEffectPacket(p.getId(), ae));
         } else {
            ScriptUtils.getPlayer().addEffect(new MobEffectInstance(effect, ticks, amplifier, true, false));
         }
      };
   }

   public static Runnable removeEffect(MobEffect effect) {
      return () -> ScriptUtils.getPlayer().removeEffect(effect);
   }

   public static <T extends Entity> Consumer<T> setDoorState(BlockPos pos, boolean open) {
      return sk -> setDoorsState(sk, sk.level(), pos, open);
   }

   public static <T extends Entity> Consumer<T> setDoorState(String setBlock, boolean open) {
      return setDoorState(PositionParser.parseBlockPos(setBlock), open);
   }

   public static <T extends Entity> Consumer<T> playSoundAt(SoundEvent sound, SoundSource category, float volume, float pitch) {
      return sk -> Network.sendToAll(new PacketPlaySound(sk.position(), sound, category, volume, pitch));
   }

   public static Runnable playSoundAt(Vec3 pos, SoundEvent sound, SoundSource category, float volume, float pitch) {
      return () -> Network.sendToAll(new PacketPlaySound(pos, sound, category, volume, pitch));
   }

   public static Runnable playSoundAtPlayer(SoundEvent sound, SoundSource category, float volume, float pitch) {
      return () -> Network.sendToAll(new PacketPlaySound(ScriptUtils.getPlayer().position(), sound, category, volume, pitch));
   }

   public static Runnable playSoundAtPlayer(SoundEvent sound, float volume, float pitch) {
      return () -> Network.sendToAll(new PacketPlaySound(ScriptUtils.getPlayer().position(), sound, SoundSource.MASTER, volume, pitch));
   }

   public static Runnable playSoundAtPlayer(SoundEvent sound) {
      return () -> Network.sendToAll(new PacketPlaySound(ScriptUtils.getPlayer().position(), sound, SoundSource.MASTER, 1.0F, 1.0F));
   }

   public static <T extends Entity> Consumer<T> setNoGravity(boolean isNoGravity) {
      return sk -> sk.setNoGravity(isNoGravity);
   }

   public static <T extends LivingEntity> Consumer<T> setDeltaMovement(Vec3 vec) {
      return sk -> setDeltaMovement(sk, vec);
   }

   public static <T extends LivingEntity> void setDeltaMovement(T sk, Vec3 vec) {
      sk.setDeltaMovement(vec);
      if (sk instanceof ServerPlayer sp) {
         Network.sendTo(sp, new PacketSetPlayerDeltaMovement(vec));
      }
   }

   public static <T extends ItemFrame> Consumer<T> setFramedItem(ItemStack stack) {
      return f -> f.setItem(stack.copy());
   }

   public static <T extends ItemFrame> Consumer<T> setFrameAttachSide(Direction attachedTo) {
      return f -> ((ItemFrameAccessor)f).callSetDirection(attachedTo.getOpposite());
   }

   public static <T extends HumanoidEntity> Consumer<T> disableEyeBlink(boolean disable) {
      return e -> e.setDisableEyeBlinkAnimation(disable);
   }

   public static Consumer<ScriptPerformerBuilder> entityNear(Character character, String position) {
      return $ -> $.thenWaitUntil(() -> $.getPerformer().script.<Entity>getEntity(character).position().distanceTo(new Location(position)) < 1.0);
   }

   public static Consumer<ServerPlayer> setGameRule(Key<BooleanValue> rule, boolean value) {
      return player -> {
         MinecraftServer s = player.server;
         ((BooleanValue)s.getGameRules().getRule(rule)).set(value, s);
      };
   }

   public static Consumer<ServerPlayer> setGameRule(Key<IntegerValue> rule, int value) {
      return player -> {
         MinecraftServer s = player.server;
         ((IntegerValue)s.getGameRules().getRule(rule)).setFrom(new IntegerValue(null, value), s);
      };
   }

   public static Runnable setGameType(GameType mode) {
      return () -> ScriptUtils.getPlayer().setGameMode(mode);
   }

   public static Consumer<ServerPlayer> giveAndSelectItem(ItemStack stack) {
      return giveAndSelectItem(stack::copy);
   }

   public static Consumer<ServerPlayer> giveAndSelectItem(Supplier<ItemStack> stack) {
      return player -> {
         Inventory inventory = player.getInventory();
         NonNullList<ItemStack> inv = inventory.items;
         int sel = inventory.selected;
         int emptySelected = -1;

         for (int i = 0; i < Math.min(inv.size(), 9); i++) {
            if (((ItemStack)inv.get(i)).isEmpty()) {
               emptySelected = i;
               break;
            }
         }

         if (emptySelected < 0 || ((ItemStack)inv.get(sel)).isEmpty()) {
            emptySelected = sel;
         }

         ItemStack prev = (ItemStack)inv.set(emptySelected, stack.get());
         if (!prev.isEmpty()) {
            inventory.add(prev);
            inventory.selected = sel;
            Network.sendToAll(new PacketSetSelected(sel));
            if (!prev.isEmpty()) {
               dropItem(player.position(), player.position(), player.level(), prev, 0.0);
            }
         }
      };
   }

   public static Consumer<ServerPlayer> giveOrDropItem(ItemStack stack) {
      return giveOrDropItem(Cast.constant(stack));
   }

   public static Consumer<ServerPlayer> giveOrDropItem(Supplier<ItemStack> stack) {
      return player -> {
         Inventory inventory = player.getInventory();
         ItemStack prev = stack.get().copy();
         if (!prev.isEmpty()) {
            inventory.add(prev);
            if (!prev.isEmpty()) {
               dropItem(player.position(), player.position(), player.level(), prev, 0.0);
            }
         }
      };
   }

   @Deprecated(
      forRemoval = true
   )
   public static IntSupplier randomInt(int min, int max) {
      Random rng = new Random();
      return () -> min + rng.nextInt(max - min + 1);
   }

   public static Supplier<Boolean> charGone(Character<?> ch) {
      return () -> {
         Entity e = ch.repositoryEntity();
         return e == null || !e.isAddedToWorld();
      };
   }

   public static Supplier<Boolean> playerHasItem(Ingredient item) {
      return () -> {
         ServerPlayer pl = ScriptUtils.getPlayer();
         if (pl == null) {
            return false;
         } else {
            Inventory iv = pl.getInventory();

            for (int i = 0; i < iv.getContainerSize(); i++) {
               ItemStack s = iv.getItem(i);
               if (item.test(s)) {
                  return true;
               }
            }

            return false;
         }
      };
   }

   public static Runnable startSpectatingCharacter(ScriptInstance inst, Character character) {
      return () -> {
         Entity entity = inst.getEntity(character);
         ScriptUtils.getPlayer().setCamera(entity);
      };
   }

   public static Runnable summonEntity(Location pos, EntityType<?> type) {
      return () -> {
         Level level = ScriptUtils.server().getLevel(pos.getDimension());
         Entity entity = type.create(level);
         entity.setPos(pos);
         level.addFreshEntity(entity);
      };
   }

   public static <T extends Entity> Consumer<T> startRiding(ScriptInstance scr, Character<?> character) {
      return e -> {
         if (character == null) {
            e.stopRiding();
         } else {
            if (scr.getEntity(character) == null) {
               return;
            }

            e.startRiding(scr.getEntity((Character<Entity>)character), true);
         }
      };
   }

   public static Predicate<Player> playerHasItemPredicate(Ingredient item) {
      return pl -> {
         if (pl == null) {
            return false;
         } else {
            Inventory iv = pl.getInventory();

            for (int i = 0; i < iv.getContainerSize(); i++) {
               ItemStack s = iv.getItem(i);
               if (item.test(s)) {
                  return true;
               }
            }

            return false;
         }
      };
   }

   public static boolean playerHasItem(Player pl, Item item, int minCount) {
      if (pl == null) {
         return false;
      } else {
         Inventory iv = pl.getInventory();
         int invCount = 0;

         for (int i = 0; i < iv.getContainerSize(); i++) {
            ItemStack s = iv.getItem(i);
            if (s.getItem() == item) {
               invCount += s.getCount();
            }
         }

         return invCount >= minCount;
      }
   }

   public static boolean playerHasItemMax(Player pl, Item item, int maxCount) {
      if (pl == null) {
         return false;
      } else {
         Inventory iv = pl.getInventory();
         int invCount = 0;

         for (int i = 0; i < iv.getContainerSize(); i++) {
            ItemStack s = iv.getItem(i);
            if (s.getItem() == item) {
               invCount += s.getCount();
            }
         }

         return invCount <= maxCount;
      }
   }

   public static Consumer<ServerPlayer> removeItem(Item item, int maxCount) {
      return pl -> removeItem(pl, item, maxCount);
   }

   public static void removeItem(Player pl, Item item, int maxCount) {
      if (pl != null) {
         Inventory iv = pl.getInventory();
         int invCount = 0;

         for (int i = 0; i < iv.getContainerSize(); i++) {
            ItemStack s = iv.getItem(i);
            if (s.getItem() == item) {
               invCount += s.getCount();
               if (invCount >= maxCount) {
                  s.setCount(invCount - maxCount);
                  break;
               }

               iv.setItem(i, ItemStack.EMPTY);
            }
         }
      }
   }

   public static ItemStack getItem(Player player, ItemStack item, boolean needCopy) {
      return getItem(player, (Predicate<ItemStack>)(itemStack -> itemStack.is(item.getItem())), needCopy);
   }

   public static ItemStack getItem(Player player, Predicate<ItemStack> itemCondition, boolean needCopy) {
      if (player != null && itemCondition != null) {
         ItemStack foundItem = findAndExtractItem(player.getInventory().items, itemCondition, needCopy, 0);
         return !foundItem.isEmpty() ? foundItem : findAndExtractItem(player.getInventory().armor, itemCondition, needCopy, 0);
      } else {
         return ItemStack.EMPTY;
      }
   }

   public static ItemStack takeAndGetItem(Player player, ItemStack item) {
      if (player != null && item != null) {
         ItemStack foundItem = findAndExtractItem(player.getInventory().items, item, true, 1);
         return !foundItem.isEmpty() ? foundItem : findAndExtractItem(player.getInventory().armor, item, true, 1);
      } else {
         return ItemStack.EMPTY;
      }
   }

   private static ItemStack findAndExtractItem(List<ItemStack> itemStacks, ItemStack item, boolean needCopy, int extractCount) {
      return findAndExtractItem(itemStacks, (Predicate<ItemStack>)(itemStack -> itemStack.is(item.getItem())), needCopy, extractCount);
   }

   private static ItemStack findAndExtractItem(List<ItemStack> itemStacks, Predicate<ItemStack> itemCondition, boolean needCopy, int extractCount) {
      for (int i = 0; i < itemStacks.size(); i++) {
         ItemStack stack = itemStacks.get(i);
         if (itemCondition.test(stack)) {
            ItemStack result = needCopy ? stack.copyWithCount(1) : stack;
            stack.shrink(extractCount);
            if (stack.isEmpty()) {
               itemStacks.set(i, ItemStack.EMPTY);
            }

            return result;
         }
      }

      return ItemStack.EMPTY;
   }

   public static Predicate<Player> playerHasItemsPredicate(PickupList items) {
      return pl -> items.hasAll(pl.getInventory());
   }

   public static void setDoorsStateAt(Entity opener, Level world, BlockPos pos, int range, boolean open) {
      BlockPos.withinManhattanStream(pos, range, range, range).forEach(p -> {
         BlockState state = world.getBlockState(p);
         if (state.getBlock() instanceof DoorBlock d) {
            d.setOpen(opener, world, state, p, open);
         }

         if (state.getBlock() instanceof FenceGateBlock) {
            world.setBlock(p, (BlockState)state.setValue(FenceGateBlock.OPEN, open), 10);
         }

         if (state.getBlock() instanceof TrapDoorBlock) {
            world.setBlock(p, (BlockState)state.setValue(TrapDoorBlock.OPEN, open), 10);
         }
      });
   }

   public static void setDoorsStateAt(Entity opener, Level world, BlockPos pos, int range) {
      BlockPos.withinManhattanStream(pos, range, range, range).forEach(p -> {
         BlockState state = world.getBlockState(p);
         if (state.getBlock() instanceof DoorBlock d) {
            if (state.getValue(DoorBlock.HALF) != DoubleBlockHalf.LOWER) {
               return;
            }

            boolean open = (Boolean)state.getValue(DoorBlock.OPEN);
            d.setOpen(opener, world, state, p, !open);
            state = world.getBlockState(p);
         }

         if (state.getBlock() instanceof FenceGateBlock) {
            boolean open = (Boolean)state.getValue(FenceGateBlock.OPEN);
            world.setBlock(p, (BlockState)state.setValue(FenceGateBlock.OPEN, !open), 10);
         }

         if (state.getBlock() instanceof TrapDoorBlock) {
            boolean open = (Boolean)state.getValue(TrapDoorBlock.OPEN);
            world.setBlock(p, (BlockState)state.setValue(TrapDoorBlock.OPEN, !open), 10);
         }
      });
   }

   public static void openMenuProvider(Player player, Level world, BlockPos startPos, int range) {
      for (BlockPos p : (Iterable<BlockPos>) BlockPos.withinManhattanStream(startPos, range, range, range)::iterator) {
         BlockState state = world.getBlockState(p);
         if (state.getBlock() instanceof ChestBlock d) {
            MenuProvider menuprovider = d.getMenuProvider(state, world, p);
            if (menuprovider != null) {
               player.openMenu(menuprovider);
               player.awardStat(Stats.CUSTOM.get(Stats.OPEN_CHEST));
               PiglinAi.angerNearbyPiglins(player, true);
               break;
            }
         }
      }
   }

   public static void openMenuProvider(Player player, Level world, BlockPos pos) {
      BlockState state = world.getBlockState(pos);
      if (state.getBlock() instanceof ChestBlock d) {
         MenuProvider menuprovider = d.getMenuProvider(state, world, pos);
         if (menuprovider != null) {
            player.openMenu(menuprovider);
            player.awardStat(Stats.CUSTOM.get(Stats.OPEN_CHEST));
            PiglinAi.angerNearbyPiglins(player, true);
         }
      }
   }

   public static void setDoorsState(Entity opener, Level world, BlockPos bottomPos, boolean open) {
      BlockState state = world.getBlockState(bottomPos);
      if (state.getBlock() instanceof FenceGateBlock) {
         world.setBlock(bottomPos, (BlockState)state.setValue(FenceGateBlock.OPEN, open), 10);
      } else if (state.getBlock() instanceof TrapDoorBlock) {
         world.setBlock(bottomPos, (BlockState)state.setValue(TrapDoorBlock.OPEN, open), 10);
      } else {
         if (state.getBlock() instanceof DoorBlock d) {
            d.setOpen(opener, world, state, bottomPos, open);
         }

         BlockPos pos = bottomPos.above();
         state = world.getBlockState(pos);
         if (state.getBlock() instanceof DoorBlock d) {
            d.setOpen(opener, world, state, pos, open);
         }
      }
   }

   public static Consumer<Boolean> spectate(ServerPlayer player) {
      return b -> {
         if (b) {
            player.setGameMode(GameType.SPECTATOR);
         } else {
            player.setGameMode(GameType.SURVIVAL);
         }
      };
   }

   public static Consumer<Boolean> survival(ServerPlayer player) {
      return b -> {
         if (b) {
            player.setGameMode(GameType.SURVIVAL);
         } else {
            player.setGameMode(GameType.SPECTATOR);
         }
      };
   }

   public static Predicate<RightClickBlock> isAt(String pos) {
      return event -> event.getHitVec().getBlockPos().equals(new Location(pos).toBlockPos());
   }

   public static Supplier<Boolean> isEntityInBox(Supplier<Entity> entity, AABB box) {
      return () -> box.contains(entity.get().position());
   }

   public static Consumer<ScriptPerformerBuilder> appendTalkToSitCharacter(Character<? extends HumanoidEntity> character) {
      return p -> p.thenAwaitEvent(EventType.ENTITY_CLICK, ref -> ref.isOf(character))
            .stopAction(character)
            .blockBodyRotation(character, true)
            .entityLookAt(character, "player")
            .thenDelay(30);
   }

   public static Consumer<ScriptPerformerBuilder> appendTalkToCharacter(Character<? extends HumanoidEntity> character) {
      return p -> p.thenAwaitEvent(EventType.ENTITY_CLICK, ref -> ref.isOf(character)).stopAction(character).entityLookAt(character, "player").thenDelay(30);
   }

   public static <T> List<T> getRandomItems(List<T> list, int count) {
      List<T> temp_list = new ArrayList<>(list);
      List<T> res = new ArrayList<>();
      Random random = new Random();

      for (int i = 0; i < Math.min(count, list.size()); i++) {
         int ind = random.nextInt(temp_list.size());
         res.add(temp_list.get(ind));
         temp_list.set(ind, temp_list.get(temp_list.size() - 1));
         temp_list.remove(temp_list.size() - 1);
      }

      return res;
   }

   public static Consumer<ScriptPerformerBuilder> smoke(Character<HumanoidEntity> character, boolean isLeft) {
      Random random = new Random((long)character.hashCode());
      return b -> {
         b.action(character, isLeft ? Animations.CIGARETTE_ENDANIMATED.left() : Animations.CIGARETTE_ENDANIMATED.right());
         b.thenDelay(Animations.CIGARETTE_ENDANIMATED.left().configure().speed(2.0F));
         b.thenRun(
            () -> {
               HumanoidEntity ent = (HumanoidEntity)character.repositoryEntity();
               if (ent != null) {
                  Matrix4d head = ent.getTransforms("Head", true).get();
                  Vector3d headPos = new Vector3d(0.0, 0.0, 0.0);
                  Vector3d h = head.transformPosition(headPos);
                  Vec3 look = ent.getLookAngle().scale(0.15);
                  h.add(look.x, look.y, look.z);
                  look = look.scale(1.5);

                  for (int i = 0; i < 20; i++) {
                     ParticleHelper.spawnDirectedParticle(
                        ScriptUtils.getLevel(),
                        ParticleTypes.SMOKE,
                        h.x,
                        h.y,
                        h.z,
                        look.x + (double)random.nextFloat(-0.1F, 0.1F),
                        look.y + (double)random.nextFloat(0.0F, 0.15F),
                        look.z + (double)random.nextFloat(-0.1F, 0.1F)
                     );
                  }
               }
            }
         );
         b.thenDelay(Animations.CIGARETTE_ENDANIMATED.left().configure().speed(2.0F));
         b.action(character, isLeft ? Animations.CIGARETTE_POSE.left() : Animations.CIGARETTE_POSE.right());
      };
   }

   public static void deleteWaypoint(String name) {
      // JourneyMap integration is disabled.
   }

   public static void createWaypoint(String name, BlockPos pos, Color color) {
      // JourneyMap integration is disabled.
   }

   public static void createWaypoint(String name, String pos, Color color) {
      createWaypoint(name, new Location(pos).toBlockPos(), color);
   }

   public static Consumer<ScriptPerformerBuilder> clapShoulder(
      Character<? extends HumanoidEntity> character, Character<? extends HumanoidEntity> character2, InteractionHand hand
   ) {
      return b -> {
         b.thenEntityFollowEntity(character, character2, 0.3, 0.0);
         b.thenWaitUntil(() -> {
            LivingEntity ent1 = (LivingEntity)character.repositoryEntity();
            LivingEntity ent2 = (LivingEntity)character2.repositoryEntity();
            return ent1 != null && ent2 != null ? ent1.position().distanceToSqr(ent2.position()) < 4.0 : false;
         });
         b.thenClearEntityGoal(character);
         b.createTicker(
            character.getReferenceId() + "_clap",
            player -> ScriptUtils.moveEntityToEntity((LivingEntity)character.repositoryEntity(), (LivingEntity)character2.repositoryEntity(), 0.1)
         );
         b.thenDelay(5);
         b.deleteTicker(character.getReferenceId() + "_clap");
         b.entityLookAt(character, character2);
         b.action(character, hand == InteractionHand.OFF_HAND ? Animations.SHOULDER_CLAP_LEFT : Animations.SHOULDER_CLAP);
         b.thenDelay(Animations.SHOULDER_CLAP);
         b.stopAction(character);
      };
   }

   public static Consumer<ScriptPerformerBuilder> allLookAt(Character<?> target, boolean wait, Character<?>... viewers) {
      return b -> {
         ScriptInstance inst = b.getPerformer().script;
         ScriptPerformer[] perfs = new ScriptPerformer[viewers.length];
         int i = 0;

         for (int viewersLength = viewers.length; i < viewersLength; i++) {
            Character<?> viewer = viewers[i];
            perfs[i] = new ScriptPerformerBuilder(inst).entityLookAt(viewer, target, wait).build();
         }

         b.thenAllPerformers(perfs);
      };
   }

   public static Consumer<ScriptPerformerBuilder> allLookAtPlayer(boolean wait, Character<?>... viewers) {
      return b -> {
         ScriptInstance inst = b.getPerformer().script;
         ScriptPerformer[] perfs = new ScriptPerformer[viewers.length];
         int i = 0;

         for (int viewersLength = viewers.length; i < viewersLength; i++) {
            Character<?> viewer = viewers[i];
            perfs[i] = new ScriptPerformerBuilder(inst).entityLookAt(viewer, "player", wait).build();
         }

         b.thenAllPerformers(perfs);
      };
   }

   public static Consumer<ScriptPerformerBuilder> clapShoulder(Character<? extends HumanoidEntity> character, Character<? extends HumanoidEntity> character2) {
      return clapShoulder(character, character2, InteractionHand.MAIN_HAND);
   }

   public static Consumer<ScriptPerformerBuilder> clapShoulder(Character<? extends HumanoidEntity> character, InteractionHand hand) {
      return b -> {
         b.thenEntityFollowPlayer(character, 0.3, 0.0, 100.0);
         b.thenWhenPlayerCloseTo(character, 2.0);
         b.thenClearEntityGoal(character);
         b.createTicker(character.getReferenceId() + "_clap", player -> ScriptUtils.moveEntityToPlayer((LivingEntity)character.repositoryEntity(), player, 0.1));
         b.thenDelay(5);
         b.deleteTicker(character.getReferenceId() + "_clap");
         b.entityLookAt(character, "player");
         b.action(character, hand == InteractionHand.OFF_HAND ? Animations.SHOULDER_CLAP_LEFT : Animations.SHOULDER_CLAP);
         b.thenDelay(Animations.SHOULDER_CLAP);
         b.stopAction(character);
      };
   }

   public static Consumer<ScriptPerformerBuilder> clapShoulder(Character<? extends HumanoidEntity> character) {
      return clapShoulder(character, InteractionHand.MAIN_HAND);
   }

   public static boolean prov(EntityContext<BaseMob> context, String pos, double distance_agr) {
      return ScriptUtils.getPlayer() != null && (double)((BaseMob)context.getEntity()).distanceTo(ScriptUtils.getPlayer()) > distance_agr;
   }

   public static boolean prov(BaseMob context, String pos, double distance_agr) {
      return context.position().distanceTo(new Location(pos)) > distance_agr
         || ScriptUtils.getPlayer() != null && (double)context.distanceTo(ScriptUtils.getPlayer()) > distance_agr;
   }

   public static boolean playerContainsAABB(AABB... aabb) {
      Player p = ScriptUtils.getPlayer();
      if (p == null) {
         return false;
      } else {
         List<AABB> aabbList = Arrays.stream(aabb).toList();
         boolean increats = true;

         for (AABB ab : aabbList) {
            if (ab.contains(p.position())) {
               increats = false;
            }
         }

         return increats;
      }
   }

   public static Consumer<ScriptPerformerBuilder> spitParticle(Character<? extends AnimatedEntity> character, double angle0) {
      return b -> b.thenUseEntity(
            character,
            e -> {
               if (e != null) {
                  Vec3 look = e.getLookAngle();
                  double angle = angle0 * -1.0;
                  Vec3 lookvec = new Vec3(look.x * Math.cos(angle) + look.z * Math.sin(angle), -0.3, -look.x * Math.sin(angle) + look.z * Math.cos(angle))
                     .normalize();
                  Vec3 spitPos = e.getEyePosition(1.0F).add(lookvec.scale(0.6)).add(0.0, -0.2, 0.0);
                  ParticleHelper.spawnEnginedParticle(
                     e.level(), ParticleTypes.CLOUD, spitPos.x, spitPos.y, spitPos.z, lookvec.x(), -1.0, lookvec.z(), 2.0F, 200, new Color(255, 255, 255), 1.0F
                  );
                  ParticleHelper.spawnEnginedParticle(
                     e.level(), ParticleTypes.CLOUD, spitPos.x, spitPos.y, spitPos.z, lookvec.x(), -1.0, lookvec.z(), 1.0F, 200, new Color(255, 255, 255), 1.0F
                  );
                  ParticleHelper.spawnEnginedParticle(
                     e.level(), ParticleTypes.CLOUD, spitPos.x, spitPos.y, spitPos.z, lookvec.x(), -1.0, lookvec.z(), 1.0F, 200, new Color(255, 255, 255), 1.0F
                  );
               }
            }
         );
   }

   public static Consumer<ScriptPerformerBuilder> spitParticle(Character<? extends AnimatedEntity> character) {
      return spitParticle(character, Math.PI / 4);
   }

   public static Supplier<Location> positionNearPlayer(boolean behindPlayer, int radius, int yRange) {
      return () -> {
         ServerPlayer player = ScriptUtils.getPlayer();
         Level world = player.level();
         Zombie testEntity = new Zombie(world);
         testEntity.setPos(player.getX(), player.getY(), player.getZ());
         int i = 0;
         int tries = 10;
         Vec3 dir = behindPlayer ? Vec3.directionFromRotation(0.0F, player.getYRot() + 180.0F) : Vec3.directionFromRotation(0.0F, player.getYRot());

         do {
            Vec3 pos;
            if (i == 0) {
               pos = player.position().add(dir.scale((double)radius));
            } else {
               int n = (tries - 1) / 2;
               pos = player.position().add(dir.scale((double)radius).yRot((float)(90 * ((i + 1) / 2) / n * (i % 2 == 0 ? 1 : -1))));
            }

            testEntity.setPos(pos.x, pos.y, pos.z);
            testEntity.lookAt(Anchor.EYES, player.position());
            if (world.noCollision(testEntity) && !world.containsAnyLiquid(testEntity.getBoundingBox())) {
               return new Location(testEntity);
            }
         } while (i++ < tries);

         return new Location(Vec3.ZERO);
      };
   }
}
