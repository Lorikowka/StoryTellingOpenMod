package com.storyteam.storytelling.content.entities;

import com.storyteam.storytelling.api.entity.IGazingEntity;
import com.storyteam.storytelling.api.entity.INoNameplate;
import com.storyteam.storytelling.client.Animations;
import com.storyteam.storytelling.content.caps.StoryData;
import com.storyteam.storytelling.content.entities.base.HeadEntityLookControlForAnimEntity;
import com.storyteam.storytelling.content.entities.configurations.AnimConfigurationWrapper;
import com.storyteam.storytelling.content.entities.configurations.IDefaultAnimConfiguration;
import com.storyteam.storytelling.mixins.minecraft.EntityAccessor;
import com.storyteam.storytelling.net.PacketAttackAnimAction;
import com.storyteam.storytelling.net.PacketEntityEyeBlinkAnimation;
import com.storyteam.storytelling.system.animation.EyeGazeSystem;
import com.storyteam.storytelling.system.fight.AttackAnimAction;
import com.storyteam.storytelling.system.navmesh.Navmesh;
import com.storyteam.storytelling.util.LayersList;
import com.storyteam.storytelling.util.ParticleHelper;
import com.storyteam.storytelling.util.ScriptUtils;
import com.storyteam.storytelling.util.world.LevelObtainer;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.Generated;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.Entity.MovementEmission;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.ScaffoldingBlock;
import net.minecraft.world.level.block.TallFlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4d;
import org.joml.Matrix4f;
import org.joml.Vector3d;
import org.zeith.hammeranims.api.animation.IAnimationSource;
import org.zeith.hammeranims.api.animation.interp.BlendMode;
import org.zeith.hammeranims.api.animsys.AnimationSystem;
import org.zeith.hammeranims.api.animsys.ConfiguredAnimation;
import org.zeith.hammeranims.api.animsys.layer.AnimationLayer;
import org.zeith.hammeranims.api.geometry.IGeometryContainer;
import org.zeith.hammeranims.api.geometry.model.IPositionalModel;
import org.zeith.hammerlib.net.Network;
import org.zeith.hammerlib.util.java.Cast;

public class AnimatedEntityLikeHumanoid extends AnimatedEntity implements INoNameplate, IGazingEntity {
   public static final String LAYER_EMOTION = "emotion";
   protected EyeGazeSystem eyeGaze;
   AttackAnimAction attackAnimAction = new AttackAnimAction();
   private static final Random BLINK_RANDOM = new Random();
   private int blinkTick = 0;
   public boolean playFallSound = true;
   public boolean disableEyeBlinkAnimation = false;
   public float yBody = 0.0F;
   public boolean lockYBody = false;
   protected float eyeHeight = Float.NaN;
   protected Vector3d prevEyePos;
   protected Vector3d eyePos;
   public Vector3d[] previousBoundingBoxVertices = null;
   public boolean blockBodyRotation;
   public boolean lockedHead;
   public float stepUpHeight = 1.0F;
   public int skipper = 0;
   protected final Matrix4f tfmat = new Matrix4f();
   protected final Matrix4d tfmatd = new Matrix4d();

   public AnimatedEntityLikeHumanoid(EntityType<? extends AnimatedEntityLikeHumanoid> type, Level world) {
      super(type, world);
      this.lookControl = new HeadEntityLookControlForAnimEntity<>(this);
      this.setMaxUpStep(1.0F);
   }

   public void setAttackAnimAction(AttackAnimAction attackAnimAction) {
      this.attackAnimAction = attackAnimAction;
      if (!this.level().isClientSide()) {
         Network.sendToAll(new PacketAttackAnimAction(this.getId(), attackAnimAction));
      }
   }

   protected MovementEmission getMovementEmission() {
      return this.isSilent() ? MovementEmission.NONE : MovementEmission.ALL;
   }

   public boolean canStealFrom() {
      return true;
   }

   public static AnimatedEntityLikeHumanoid createHumanoid(
      EntityType<? extends AnimatedEntityLikeHumanoid> type, Level world, IDefaultAnimConfiguration configuration
   ) {
      AnimatedEntityLikeHumanoid animated = new AnimatedEntityLikeHumanoid(type, world);
      animated.wrapper = new AnimConfigurationWrapper(configuration);
      return animated;
   }

   public Iterable<ItemStack> getArmorSlots() {
      return this.getInventory();
   }

   @Override
   public void addLayers(LayersList layers) {
      layers.addLast("EYES", BlendMode.ADD, 1.0F);
      layers.addLast("EMOTION_COMBINE", BlendMode.ADD, 1.0F);
      this.eyeGaze = new EyeGazeSystem(this, "EyeGaze");
      this.eyeGaze.setupLayers(layers);
      layers.addLast("emotion", BlendMode.ADD, 1.0F);
   }

   public boolean canBeLeashed(@NotNull Player pPlayer) {
      return false;
   }

   public void setDisableEyeBlinkAnimation(boolean disableEyeBlinkAnimation) {
      this.disableEyeBlinkAnimation = disableEyeBlinkAnimation;
      if (!this.level().isClientSide()) {
         Network.sendToAll(new PacketEntityEyeBlinkAnimation(this.getId(), disableEyeBlinkAnimation));
      }
   }

   @NotNull
   @Override
   protected BodyRotationControl createBodyControl() {
      return new RestrictiveBodyController(this);
   }

   public void recalcEyeHeight() {
      if (this.isAlive() && (this.level().isClientSide() || ((ServerLevel)this.level()).isPositionEntityTicking(this.blockPosition()))) {
         if (this.tickCount % 5 == 0 && this.getType() instanceof StoryEntityType<?> stt) {
            IGeometryContainer model = ((Function<Object, IGeometryContainer>)stt.getProperties().model).apply((Entity)Cast.cast(this));
            Matrix4d tfm;
            if (model != null && (tfm = this.getTransforms(model, "Head", false).orElse(null)) != null) {
               this.eyeHeight = (float)((this.eyePos = tfm.transformPosition(new Vector3d(0.0, 0.125, 0.0))).y - this.position().y);
               ((EntityAccessor)this).setEyeHeight(this.eyeHeight);
            }
         } else {
            this.eyeHeight = Float.NEGATIVE_INFINITY;
         }
      }
   }

   @Override
   public void tick() {
      if (this.lockYBody) {
         this.yBodyRot = this.yBody;
         this.yBodyRotO = this.yBody;
      }

      super.tick();
      this.setMaxUpStep(this.stepUpHeight);
      this.manageEyes();
      this.manageEyeGaze();
      if (this.lockYBody) {
         this.yBodyRot = this.yBody;
         this.yBodyRotO = this.yBody;
      }

      this.prevEyePos = this.eyePos;
      this.recalcEyeHeight();
      this.skipper++;
      if (this.attackAnimAction != null && this.attackAnimAction.is_active()) {
         this.attackAnimAction.setTime_anim(this.attackAnimAction.getTime_anim() + 1.0F);
         if (this.attackAnimAction.getTime_anim() >= this.attackAnimAction.getTime_anim_max()) {
            this.setAttackAnimAction(new AttackAnimAction());
            this.previousBoundingBoxVertices = null;
         }
      }

      Matrix4d tfm;
      if (this.getMainHandItem().getItem().equals(Items.TORCH)
         && this.skipper % 20 == 0
         && (
               tfm = this.getTransforms(
                     "Right_placeholder", true, layer -> !Objects.equals(layer.activeAnimationLocation(), this.getIdleAnimation().getLocation())
                  )
                  .orElse(null)
            )
            != null) {
         Vector3d basePos = new Vector3d(0.0, 0.0625, -0.25);
         Vector3d pos = tfm.transformPosition(basePos);
         ParticleHelper.spawnDirectedParticle(
            ScriptUtils.getLevel(), ParticleTypes.SMALL_FLAME, pos.x, pos.y, pos.z, -0.01 + Math.random() * 0.02, 0.01, -0.01 + Math.random() * 0.02
         );
         ParticleHelper.spawnDirectedParticle(
            ScriptUtils.getLevel(), ParticleTypes.FLAME, pos.x, pos.y, pos.z, -0.01 + Math.random() * 0.02, 0.01, -0.01 + Math.random() * 0.02
         );
         ParticleHelper.spawnDirectedParticle(
            ScriptUtils.getLevel(), ParticleTypes.SMOKE, pos.x, pos.y, pos.z, -0.01 + Math.random() * 0.02, 0.015, -0.01 + Math.random() * 0.02
         );
      }
   }

   public boolean canCollideWith(@NotNull Entity entity) {
      return entity instanceof Projectile;
   }

   private void manageEyes() {
      AnimationSystem anims = this.getAnimationSystem();
      if (!this.level().isClientSide) {
         if (this.disableEyeBlinkAnimation || anims.getLayer("EYES") == null) {
            anims.stopAnimation("EYES");
         } else if (this.blinkTick == 0) {
            anims.startAnimationAt("EYES", Animations.BLINK_ENDANIMATED.configure().transitionTime(0.0F).important());
            this.blinkTick = 50 + BLINK_RANDOM.nextInt(4) * 20;
         } else {
            this.blinkTick--;
         }
      }
   }

   protected float getStandingEyeHeight(@NotNull Pose pPose, @NotNull EntityDimensions pDimensions) {
      if (Float.isFinite(this.eyeHeight)) {
         return this.eyeHeight;
      } else {
         if (this.eyeHeight != Float.NEGATIVE_INFINITY) {
            this.recalcEyeHeight();
            if (Float.isFinite(this.eyeHeight)) {
               return this.eyeHeight;
            }
         }

         return pDimensions.height * 0.85F;
      }
   }

   @NotNull
   public Vec3 getEyePosition() {
      if (this.eyePos == null) {
         this.recalcEyeHeight();
      }

      return this.eyePos != null ? new Vec3(this.eyePos.x, this.eyePos.y, this.eyePos.z) : super.getEyePosition();
   }

   @NotNull
   public Vec3 getEyePosition(float pPartialTicks) {
      if (this.eyePos == null) {
         this.recalcEyeHeight();
      }

      if (this.prevEyePos == null && this.eyePos != null) {
         return new Vec3(this.eyePos.x, this.eyePos.y, this.eyePos.z);
      } else {
         return this.eyePos != null
            ? new Vec3(
               Mth.lerp((double)pPartialTicks, this.prevEyePos.x, this.eyePos.x),
               Mth.lerp((double)pPartialTicks, this.prevEyePos.y, this.eyePos.y),
               Mth.lerp((double)pPartialTicks, this.prevEyePos.z, this.eyePos.z)
            )
            : super.getEyePosition(pPartialTicks);
      }
   }

   public Optional<Matrix4d> getTransforms(String bone, boolean useBodyRotation) {
      StoryEntityType<AnimatedEntityLikeHumanoid> stt = (StoryEntityType<AnimatedEntityLikeHumanoid>)Cast.cast(this.getType());
      if (stt == null) {
         return Optional.empty();
      } else {
         IGeometryContainer model = stt.properties.model.apply(this);
         return this.getTransforms(model, bone, useBodyRotation);
      }
   }

   public Optional<Matrix4d> getTransforms(IGeometryContainer model, String bone, boolean useBodyRotation) {
      AnimationSystem asys = this.getAnimationSystem();
      if (model != null && asys != null) {
         IPositionalModel pmod = model.getPositionalModel();
         pmod.applySystem(1.0F, asys, layer -> !Objects.equals(layer.activeAnimationLocation(), this.getIdleAnimation().getLocation()));
         Vec3 pos = this.position();
         if (pmod.applyBoneTransforms(this.tfmat.identity(), bone)) {
            float scale = 1.0F;
            if (this.getType() instanceof StoryEntityType<?> stt) {
               scale = stt.getProperties().scale;
            }

            this.tfmatd
               .identity()
               .translate((double)((float)pos.x), (double)((float)pos.y), (double)((float)pos.z))
               .rotateY((double)((float) (Math.PI / 180.0) * (180.0F - (useBodyRotation ? this.yBodyRot : this.yHeadRot))))
               .mul(this.tfmat.scale(scale));
            return Optional.of(this.tfmatd);
         } else {
            return Optional.empty();
         }
      } else {
         return Optional.empty();
      }
   }

   public Optional<Matrix4d> getTransforms(String bone, boolean useBodyRotation, Predicate<AnimationLayer> layerMask) {
      StoryEntityType<AnimatedEntityLikeHumanoid> stt = (StoryEntityType<AnimatedEntityLikeHumanoid>)Cast.cast(this.getType());
      if (stt == null) {
         return Optional.empty();
      } else {
         IGeometryContainer model = stt.properties.model.apply(this);
         return this.getTransforms(model, bone, useBodyRotation, layerMask);
      }
   }

   public Optional<Matrix4d> getTransforms(IGeometryContainer model, String bone, boolean useBodyRotation, Predicate<AnimationLayer> layerMask) {
      AnimationSystem asys = this.getAnimationSystem();
      if (model != null && asys != null) {
         IPositionalModel pmod = model.getPositionalModel();
         pmod.applySystem(1.0F, asys, layerMask);
         Vec3 pos = this.position();
         if (pmod.applyBoneTransforms(this.tfmat.identity(), bone)) {
            float scale = 1.0F;
            if (this.getType() instanceof StoryEntityType<?> stt) {
               scale = stt.getProperties().scale;
            }

            this.tfmatd
               .identity()
               .translate((double)((float)pos.x), (double)((float)pos.y), (double)((float)pos.z))
               .rotateY((double)((float) (Math.PI / 180.0) * (180.0F - (useBodyRotation ? this.yBodyRot : this.yHeadRot))))
               .mul(this.tfmat.scale(scale));
            return Optional.of(this.tfmatd);
         } else {
            return Optional.empty();
         }
      } else {
         return Optional.empty();
      }
   }

   private void manageEyeGaze() {
      if (this.level().isClientSide) {
         StoryData sd = StoryData.of(this);
         float yRot = this.yHeadRot;
         float xRot = this.getXRot();
         if (this.lockYBody) {
            yRot = this.yBodyRot;
         }

         if (sd != null) {
            if (sd.lockHead.getBoolean()) {
               yRot = this.yBodyRot;
            }

            if (sd.blockBody.getBoolean()) {
               if (sd.lockHead.getBoolean()) {
                  yRot = this.yBodyRot;
               } else {
                  yRot = this.yHeadRot;
               }
            }
         }

         this.eyeGaze.updateClient(this.getEyePosition(1.0F).add(0.0, 0.18, 0.0), xRot, yRot, this.animationSystem);
      }
   }

   public final void runEmotionAnimation(IAnimationSource animation) {
      this.startAnimation("emotion", animation);
   }

   public final void runEmotionAnimation(ConfiguredAnimation starter) {
      this.startAnimation("emotion", starter);
   }

   public final void stopEmotionAnimation() {
      this.stopAnimation("emotion");
   }

   @Override
   public void checkDespawn() {
      this.noActionTime = 0;
   }

   protected void playBlockFallSound() {
      if (this.playFallSound) {
         super.playBlockFallSound();
      }
   }

   @NotNull
   @Override
   protected PathNavigation createNavigation(@NotNull Level pLevel) {
      return new AnimatedEntityLikeHumanoid.HumanoidNavigation(this, pLevel);
   }

   @Override
   public EyeGazeSystem getGazeSystem() {
      return this.eyeGaze;
   }

   public ItemStack getItemLeftHand() {
      return this.getItemInHand(InteractionHand.OFF_HAND);
   }

   public ItemStack getItemRightHand() {
      return this.getItemInHand(InteractionHand.MAIN_HAND);
   }

   @NotNull
   @Override
   public String toString() {
      String s = this.level().toString();
      String name = this.getClass().getSimpleName();
      name = name + "[";
      StringBuilder sb = new StringBuilder();
      CompoundTag st = this.getPersistentData().getCompound("Storytelling");
      if (st != null && !st.isEmpty()) {
         sb.append("sid='").append(st.getString("Id")).append("', ");
      }

      if (!sb.isEmpty()) {
         name = name + sb;
      }

      return this.getRemovalReason() != null
         ? String.format(
            Locale.ROOT,
            "%s'%s'/%d, l='%s', x=%.2f, y=%.2f, z=%.2f, removed=%s]",
            name,
            this.getName().getString(),
            this.getId(),
            s,
            this.getX(),
            this.getY(),
            this.getZ(),
            this.getRemovalReason()
         )
         : String.format(
            Locale.ROOT, "%s'%s'/%d, l='%s', x=%.2f, y=%.2f, z=%.2f]", name, this.getName().getString(), this.getId(), s, this.getX(), this.getY(), this.getZ()
         );
   }

   @Generated
   public AttackAnimAction getAttackAnimAction() {
      return this.attackAnimAction;
   }

   @Generated
   public void setBlockBodyRotation(boolean blockBodyRotation) {
      this.blockBodyRotation = blockBodyRotation;
   }

   @Generated
   public boolean isBlockBodyRotation() {
      return this.blockBodyRotation;
   }

   @Generated
   public void setLockedHead(boolean lockedHead) {
      this.lockedHead = lockedHead;
   }

   @Generated
   public boolean isLockedHead() {
      return this.lockedHead;
   }

   static class HumanoidNavigation extends GroundPathNavigation {
      public HumanoidNavigation(Mob p_33379_, Level p_33380_) {
         super(p_33379_, p_33380_);
      }

      @NotNull
      protected PathFinder createPathFinder(int p_33382_) {
         this.nodeEvaluator = new AnimatedEntityLikeHumanoid.HumanoidNodeEvaluator();
         return new PathFinder(this.nodeEvaluator, p_33382_);
      }
   }

   static class HumanoidNodeEvaluator extends WalkNodeEvaluator {
      @NotNull
      public BlockPathTypes getBlockPathType(BlockGetter pLevel, int pX, int pY, int pZ) {
         BlockPos pos = new BlockPos(pX, pY, pZ);
         Level level = LevelObtainer.fromBlockGetter(pLevel);
         if (level != null && Navmesh.isPositionBlockedForNavigation(level, pos)) {
            return BlockPathTypes.BLOCKED;
         } else {
            BlockState state = pLevel.getBlockState(new BlockPos(pX, pY, pZ));
            return !(state.getBlock() instanceof FlowerBlock) && !(state.getBlock() instanceof TallFlowerBlock)
               ? (
                  state.getBlock() instanceof ScaffoldingBlock
                     ? BlockPathTypes.OPEN
                     : (state.getBlock() instanceof FenceGateBlock ? BlockPathTypes.WALKABLE_DOOR : super.getBlockPathType(pLevel, pX, pY, pZ))
               )
               : BlockPathTypes.BREACH;
         }
      }

      public boolean canPassDoors() {
         return true;
      }

      public boolean canOpenDoors() {
         return true;
      }
   }
}
