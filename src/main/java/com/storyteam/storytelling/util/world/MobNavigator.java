package com.storyteam.storytelling.util.world;

import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.api.itf.IStoryEntity;
import com.storyteam.storytelling.util.ScriptUtils;
import com.storyteam.storytelling.util.UtilST;
import java.util.Arrays;
import java.util.LinkedList;
import lombok.Generated;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraftforge.common.util.INBTSerializable;
import org.zeith.hammerlib.api.io.IAutoNBTSerializable;
import org.zeith.hammerlib.api.io.NBTSerializable;
import org.zeith.hammerlib.api.io.NBTSerializationHelper;
import org.zeith.hammerlib.util.java.Cast;

public class MobNavigator implements IAutoNBTSerializable {
   public MobNavigator.NavigationJob currentJob;
   @NBTSerializable
   public boolean paused = false;
   public boolean markStopped;

   public void stop() {
      this.currentJob = null;
      this.markStopped = true;
   }

   public void refresh() {
      this.paused = false;
      this.currentJob = null;
      this.markStopped = true;
   }

   public void update(Mob mob) {
      if (this.markStopped) {
         mob.getNavigation().stop();
         this.markStopped = false;
      }

      if (this.currentJob != null && !this.paused) {
         this.currentJob.update(mob);
         if (!this.currentJob.followPlayer && this.currentJob.positionsLeft.isEmpty()) {
            this.currentJob = null;
         }
      }
   }

   public void moveByPath(float speed, Location... pos) {
      this.currentJob = new MobNavigator.NavigationJob(false, speed, pos);
   }

   public static class NavigationJob implements INBTSerializable<CompoundTag> {
      public boolean followPlayer;
      public float followSpeed;
      public LinkedList<Location> positionsLeft;
      private boolean initialized;
      boolean stopped;

      public NavigationJob(boolean followPlayer, float followSpeed, Location... positions) {
         this.followPlayer = followPlayer;
         this.followSpeed = followSpeed;
         this.positionsLeft = new LinkedList<>(Arrays.asList(positions));
      }

      private void handleFollowPlayer(Mob mob) {
         ServerPlayer player = ScriptUtils.getPlayer();
         if (player != null) {
            if (mob.closerThan(player, 3.0)) {
               mob.getNavigation().stop();
            } else {
               mob.getNavigation().moveTo(player, (double)this.followSpeed);
            }
         }
      }

      public void init(Mob mob) {
         if (!this.initialized) {
            mob.getNavigation().stop();
            this.initialized = true;
         }
      }

      public void update(Mob mob) {
         this.init(mob);
         if (this.followPlayer) {
            this.handleFollowPlayer(mob);
         } else if (!this.stopped) {
            PathNavigation navigator = mob.getNavigation();
            if (navigator.isStuck()) {
               Storytelling.LOG
                  .error(
                     String.format(
                        "Персонаж с id %s простраивает путь до недостижимой точки (слишком большое расстояние). Нужно установить промежуточные координаты!",
                        IStoryEntity.of(mob).getStoryName()
                     )
                  );
               this.stopped = true;
            } else {
               Location wantedRawPos = this.positionsLeft.peek();
               Path navigatorPath = navigator.getPath();
               BlockPos blockPos = BlockPos.containing(wantedRawPos);
               BlockPos mobPos = mob.blockPosition();
               double distance = mob.position().distanceToSqr(wantedRawPos);
               boolean closeEnough = distance < (double)mob.getBbWidth() / 3.0 + (double)(this.followSpeed / 4.0F)
                  && (float)Math.abs(blockPos.getY() - mobPos.getY()) <= mob.getStepHeight() / 2.0F;
               boolean straightMove = distance < 1.3 && (float)Math.abs(blockPos.getY() - mobPos.getY()) <= mob.getStepHeight() + 1.0F;
               if (closeEnough) {
                  if (this.positionsLeft.isEmpty()) {
                     this.stopped = true;
                  }

                  this.positionsLeft.pop();
                  navigator.stop();
               } else if (straightMove) {
                  mob.getNavigation().stop();
                  mob.getMoveControl()
                     .setWantedPosition(wantedRawPos.x, UtilST.getGroundY(mob.level(), wantedRawPos), wantedRawPos.z, (double)this.followSpeed);
                  navigator.stop();
               } else {
                  if (navigatorPath == null || navigatorPath.isDone()) {
                     navigatorPath = navigator.createPath(wantedRawPos.x, wantedRawPos.y, wantedRawPos.z, 0);
                  }

                  navigator.moveTo(navigatorPath, (double)this.followSpeed);
               }
            }
         }
      }

      public CompoundTag serializeNBT() {
         CompoundTag nbt = new CompoundTag();
         nbt.putBoolean("FollowPlayer", this.followPlayer);
         nbt.putFloat("FollowSpeed", this.followSpeed);
         ListTag lst = new ListTag();

         for (Location p : this.positionsLeft) {
            CompoundTag n = new CompoundTag();
            NBTSerializationHelper.serializeField(Location.class, p, n, "p");
            lst.add(n.getCompound("p"));
         }

         nbt.put("Positions", lst);
         nbt.putBoolean("Init", this.initialized);
         return nbt;
      }

      public void deserializeNBT(CompoundTag nbt) {
         this.followPlayer = nbt.getBoolean("FollowPlayer");
         this.followSpeed = nbt.getFloat("FollowSpeed");
         this.positionsLeft = new LinkedList<>();
         ListTag lst = nbt.getList("Positions", 10);

         for (int i = 0; i < lst.size(); i++) {
            CompoundTag n = new CompoundTag();
            n.put("p", lst.getCompound(i));
            Location p = (Location)Cast.cast(NBTSerializationHelper.deserializeField(Location.class, n, "p"), Location.class);
            this.positionsLeft.add(p);
         }

         this.initialized = nbt.getBoolean("init");
      }

      @Generated
      public boolean isStopped() {
         return this.stopped;
      }
   }
}
