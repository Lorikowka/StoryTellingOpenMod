package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.entity.EntityState;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.util.ScriptUtils;
import com.storyteam.storytelling.util.ToStringBuilder;
import com.storyteam.storytelling.util.world.Location;
import java.util.function.Supplier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
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

public class EntityPositionRelativeToPlayer<T extends Entity> extends EntityPerformerAction<T> {
   private final double distance;
   private final int radiusOffset;
   private int yRange;
   private boolean isFront;
   private final Supplier<Location> location;
   private boolean spawned;

   public EntityPositionRelativeToPlayer(
      ScriptPerformer performer, @NotNull Character<T> character, double distance, int radiusOffset, int yRange, boolean isFront, Supplier<Location> location
   ) {
      super(performer, character);
      this.distance = distance;
      this.radiusOffset = radiusOffset;
      this.yRange = yRange;
      this.isFront = isFront;
      this.location = location;
   }

   @Override
   public void tick() {
      if (!this.spawned) {
         ServerPlayer player = ScriptUtils.getPlayer();
         if (player == null) {
            return;
         }

         this.spawnEntityInFrontOfPlayer(player, this.distance, this.radiusOffset, this.yRange);
      }
   }

   public void spawnEntityInFrontOfPlayer(ServerPlayer player, double distance, int radiusOffset, int yRange) {
      Level level = player.level();
      float yaw = player.getYRot();
      Zombie zombie = new Zombie(level);
      double dx = (double)(-Mth.sin(yaw * (float) Math.PI / 180.0F));
      double dz = (double)Mth.cos(yaw * (float) Math.PI / 180.0F);
      Vec3 direction = new Vec3(dx, 0.0, dz);
      Vec3 targetPosition = player.position().add(direction.scale(this.isFront ? distance : -distance));
      zombie.setPos(targetPosition);
      Vec3 spawnPosition = LandRandomPos.getPosTowards(zombie, radiusOffset, yRange, targetPosition);
      if (spawnPosition != null) {
         boolean isClear = level.clip(new ClipContext(player.getEyePosition(), spawnPosition.add(0.0, 0.5, 0.0), Block.COLLIDER, Fluid.ANY, player)).getType()
            == Type.MISS;
         if (isClear) {
            zombie.setPos(spawnPosition.x, spawnPosition.y, spawnPosition.z);
            Location loc = this.location.get();
            if (loc != null) {
               float yaw1 = loc.getYaw();
               float pitch1 = loc.getPitch();
               zombie.setYRot(yaw1);
               zombie.setXRot(pitch1);
            }

            if (level.noCollision(zombie) && !level.containsAnyLiquid(zombie.getBoundingBox())) {
               this.spawnAt(new Location(zombie));
               this.spawned = true;
            }
         }
      }
   }

   private void spawnAt(Location pos) {
      EntityState<T> state = this.getOrCreateEntity(true);
      state.setPosition(pos, true);
      state.markForReposition();
   }

   @Override
   public void reset() {
      this.spawned = false;
   }

   @Override
   public void fastForwardToStage() {
      ServerPlayer player = ScriptUtils.getPlayer();
      if (player != null) {
         this.spawned = false;
         this.spawnEntityInFrontOfPlayer(ScriptUtils.getPlayer(), this.distance, this.radiusOffset, this.yRange);
         if (!this.spawned) {
            this.spawnAt(new Location(player));
         }
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
         .append("distance", this.distance)
         .append("radiusOffset", this.radiusOffset)
         .append("yRange", this.yRange)
         .build();
   }
}
