package com.storyteam.storytelling.content.entities.mobs;

import com.storyteam.storytelling.ai.CustomWalkNodeEvalutor;
import com.storyteam.storytelling.content.entities.AnimatedEntityLikeHumanoid;
import com.storyteam.storytelling.content.entities.api.CustomMoveControl;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Generated;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class BaseMob extends AnimatedEntityLikeHumanoid {
   public int exp = 10;
   public List<BaseMob.EntityCategoryMob> entityCategoryMobs = new ArrayList<>();
   public float max_impact = 2.0F;
   public float impact = 2.0F;
   public Vec3 inflate_box_collide = new Vec3(0.0, 0.0, 0.0);

   public void onDeathByPlayer() {
   }

   public BaseMob(EntityType<? extends AnimatedEntityLikeHumanoid> type, Level world) {
      super(type, world);
      this.moveControl = new CustomMoveControl(this);
      this.entityCategoryMobs = new ArrayList<>();
      this.entityCategoryMobs.add(BaseMob.EntityCategoryMob.COMMON);
      this.setMaxUpStep(1.125F);
   }

   public BaseMob(EntityType<? extends AnimatedEntityLikeHumanoid> type, Level world, List<BaseMob.EntityCategoryMob> entityCategoryMobs) {
      super(type, world);
      this.moveControl = new CustomMoveControl(this);
      this.entityCategoryMobs = new ArrayList<>();
      this.entityCategoryMobs.addAll(entityCategoryMobs);
      this.setMaxUpStep(1.125F);
   }

   public BaseMob(EntityType<? extends AnimatedEntityLikeHumanoid> type, Level world, int health, int exp, float impact, Vec3 inflate_box_collide) {
      super(type, world);
      this.moveControl = new CustomMoveControl(this);
      this.exp = exp;
      this.impact = impact;
      this.max_impact = impact;
      this.inflate_box_collide = inflate_box_collide;
      this.getAttribute(Attributes.MAX_HEALTH)
         .addPermanentModifier(new AttributeModifier(UUID.fromString("62d7d832-684c-419f-a062-556ce372529a"), "add_hp", (double)health, Operation.ADDITION));
      this.entityCategoryMobs = new ArrayList<>();
      this.entityCategoryMobs.add(BaseMob.EntityCategoryMob.COMMON);
      this.setMaxUpStep(1.125F);
   }

   public BaseMob(
      EntityType<? extends AnimatedEntityLikeHumanoid> type,
      Level world,
      int health,
      int exp,
      float impact,
      Vec3 inflate_box_collide,
      List<BaseMob.EntityCategoryMob> entityCategoryMobs
   ) {
      super(type, world);
      this.moveControl = new CustomMoveControl(this);
      this.exp = exp;
      this.impact = impact;
      this.max_impact = impact;
      this.inflate_box_collide = inflate_box_collide;
      this.getAttribute(Attributes.MAX_HEALTH)
         .addPermanentModifier(new AttributeModifier(UUID.fromString("62d7d832-684c-419f-a062-556ce372529a"), "add_hp", (double)health, Operation.ADDITION));
      this.entityCategoryMobs = new ArrayList<>();
      this.entityCategoryMobs.addAll(entityCategoryMobs);
      this.setMaxUpStep(1.125F);
   }

   public static BaseMob createBaseMob(EntityType<? extends BaseMob> type, Level world, int health, int exp, float impact, Vec3 inflate_box_collide) {
      return new BaseMob(type, world, health, exp, impact, inflate_box_collide);
   }

   public static BaseMob createBaseMob(
      EntityType<? extends BaseMob> type,
      Level world,
      int health,
      int exp,
      float impact,
      Vec3 inflate_box_collide,
      List<BaseMob.EntityCategoryMob> entityCategoryMobs
   ) {
      return new BaseMob(type, world, health, exp, impact, inflate_box_collide, entityCategoryMobs);
   }

   public boolean hurt(DamageSource pSource, float pAmount) {
      return super.hurt(pSource, pAmount);
   }

   @NotNull
   @Override
   protected PathNavigation createNavigation(@NotNull Level pLevel) {
      return new BaseMob.HumanoidNavigation(this, pLevel);
   }

   public boolean isDeadOrDying() {
      return super.isDeadOrDying();
   }

   public boolean isPushable() {
      return super.isPushable();
   }

   public void push(double pX, double pY, double pZ) {
   }

   protected void pushEntities() {
      if (this.level().isClientSide()) {
         this.level()
            .getEntities(EntityTypeTest.forClass(Player.class), this.getBoundingBox(), EntitySelector.pushableBy(this))
            .forEach(x$0 -> this.doPush(x$0));
      } else {
         List<Entity> list = this.level().getEntities(this, this.getBoundingBox(), EntitySelector.pushableBy(this));
         if (!list.isEmpty()) {
            int i = this.level().getGameRules().getInt(GameRules.RULE_MAX_ENTITY_CRAMMING);
            if (i > 0 && list.size() > i - 1 && this.random.nextInt(4) == 0) {
               int j = 0;

               for (int k = 0; k < list.size(); k++) {
                  if (!list.get(k).isPassenger()) {
                     j++;
                  }
               }

               if (j > i - 1) {
                  this.hurt(this.damageSources().cramming(), 6.0F);
               }
            }

            for (int l = 0; l < list.size(); l++) {
               Entity entity = list.get(l);
               this.doPush(entity);
            }
         }
      }
   }

   @Override
   public boolean canCollideWith(@NotNull Entity entity) {
      return false;
   }

   public boolean canBeCollidedWith() {
      return false;
   }

   @Override
   public void tick() {
      super.tick();
      if (this.getNavigation().getPath() != null && !this.getNavigation().getPath().isDone()) {
         Path path = this.getNavigation().getPath();
         Node node = path.getNextNode();
         BlockPos pos = new BlockPos(node.asBlockPos());
         if (this.isEntityNearby(pos)) {
            this.getNavigation().recomputePath();
         }
      }
   }

   private boolean isEntityNearby(BlockPos position) {
      List<LivingEntity> livingEntityList = this.level().getEntitiesOfClass(LivingEntity.class, new AABB(position)).stream().filter(e -> e != this).toList();
      return !livingEntityList.isEmpty();
   }

   @Generated
   public int getExp() {
      return this.exp;
   }

   @Generated
   public List<BaseMob.EntityCategoryMob> getEntityCategoryMobs() {
      return this.entityCategoryMobs;
   }

   @Generated
   public float getMax_impact() {
      return this.max_impact;
   }

   @Generated
   public float getImpact() {
      return this.impact;
   }

   @Generated
   public Vec3 getInflate_box_collide() {
      return this.inflate_box_collide;
   }

   @Generated
   public void setExp(int exp) {
      this.exp = exp;
   }

   @Generated
   public void setEntityCategoryMobs(List<BaseMob.EntityCategoryMob> entityCategoryMobs) {
      this.entityCategoryMobs = entityCategoryMobs;
   }

   @Generated
   public void setMax_impact(float max_impact) {
      this.max_impact = max_impact;
   }

   @Generated
   public void setImpact(float impact) {
      this.impact = impact;
   }

   @Generated
   public void setInflate_box_collide(Vec3 inflate_box_collide) {
      this.inflate_box_collide = inflate_box_collide;
   }

   public static enum EntityCategoryMob {
      SMALL,
      BIG,
      ELITE,
      COMMON,
      BOSS;
   }

   static class HumanoidNavigation extends GroundPathNavigation {
      public HumanoidNavigation(Mob p_33379_, Level p_33380_) {
         super(p_33379_, p_33380_);
      }

      @NotNull
      protected PathFinder createPathFinder(int p_33382_) {
         this.nodeEvaluator = new CustomWalkNodeEvalutor();
         return new PathFinder(this.nodeEvaluator, p_33382_);
      }
   }
}
