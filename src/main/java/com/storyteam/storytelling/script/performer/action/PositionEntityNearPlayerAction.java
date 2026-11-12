package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.entity.EntityState;
import com.storyteam.storytelling.script.performer.entity.look.targets.PlayerTarget;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.util.ScriptUtils;
import com.storyteam.storytelling.util.ToStringBuilder;
import com.storyteam.storytelling.util.world.Location;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import org.jetbrains.annotations.NotNull;

public class PositionEntityNearPlayerAction<T extends Entity> extends EntityPerformerAction<T> {
   private final int radius;
   private final int yRange;
   private final boolean behindPlayer;
   private boolean spawned;

   public PositionEntityNearPlayerAction(ScriptPerformer performer, @NotNull Character<T> character, int radius, int yRange, boolean behindPlayer) {
      super(performer, character);
      this.radius = radius;
      this.yRange = yRange;
      this.behindPlayer = behindPlayer;
   }

   private Vec3 findPos(ServerPlayer player, Zombie testEntity) {
      Vec3 pos;
      if (this.behindPlayer) {
         Vec3 back = Vec3.directionFromRotation(player.getXRot(), player.getYRot() + 180.0F);
         Vec3 posFinderCenter = player.position().add(back.scale((double)this.radius));
         pos = LandRandomPos.getPosTowards(testEntity, this.radius, this.yRange, posFinderCenter);
      } else {
         pos = LandRandomPos.getPos(testEntity, this.radius, this.yRange);
      }

      double incRadius = (double)this.radius + 0.5;
      if (pos != null && (player.distanceToSqr(pos) > incRadius * incRadius || Math.abs(player.getY() - pos.y) > (double)this.yRange + 0.0625)) {
         pos = null;
      }

      if (pos != null) {
         boolean isClear = player.level().clip(new ClipContext(player.getEyePosition(), pos.add(0.0, 0.5, 0.0), Block.COLLIDER, Fluid.ANY, player)).getType()
            == Type.MISS;
         if (!isClear) {
            return null;
         }
      }

      return pos;
   }

   private void tryPositionEntityNear(ServerPlayer player) {
      Level world = player.level();
      Zombie testEntity = new Zombie(world);
      testEntity.setPos(player.getX(), player.getY(), player.getZ());
      int tries = 10;

      Vec3 spawnPos;
      do {
         spawnPos = this.findPos(player, testEntity);
      } while (spawnPos == null && --tries > 0);

      if (spawnPos != null) {
         testEntity.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
         if (world.noCollision(testEntity) && !world.containsAnyLiquid(testEntity.getBoundingBox())) {
            this.spawnAt(new Location(testEntity));
            this.spawned = true;
         }
      }
   }

   @Override
   public void tick() {
      if (!this.spawned) {
         this.tryPositionEntityNear(ScriptUtils.getPlayer());
      }
   }

   private void spawnAt(Location pos) {
      EntityState<T> state = this.getOrCreateEntity(true);
      state.setPosition(pos);
      state.getData().setLookTarget(new PlayerTarget<>());
      state.markForReposition();
   }

   @Override
   public void reset() {
      this.spawned = false;
   }

   @Override
   public void fastForwardToStage() {
      ServerPlayer player = ScriptUtils.getPlayer();
      this.spawned = false;
      this.tryPositionEntityNear(player);
      if (!this.spawned) {
         this.spawnAt(new Location(player));
      }
   }

   @Override
   public void rewindToStage() {
      this.spawned = false;
      EntityState<T> state = this.getEntity();
      state.markForDelete();
   }

   @Override
   public boolean isDone() {
      return this.spawned;
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this)
         .append("character", this.character)
         .append("radius", this.radius)
         .append("yRange", this.yRange)
         .append("behindPlayer", this.behindPlayer)
         .build();
   }
}
