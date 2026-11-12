package com.storyteam.storytelling.system.time.core;

import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.util.data_structure.NodeTreeMap;
import java.util.Comparator;
import java.util.Map.Entry;
import lombok.Generated;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.game.ClientboundSetTimePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameRules.BooleanValue;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.Nullable;

public class TimeController implements INBTSerializable<CompoundTag> {
   public static final String DEFAULT_RUNNER_NAME = "default_time_task";
   private static final TimeController INSTANCE = new TimeController();
   private final NodeTreeMap<String, TimeTask> tasks = new NodeTreeMap<>();
   @Nullable
   private TimeFreeze freeze;
   private Integer currentDay = 0;

   public static TimeController get() {
      return INSTANCE;
   }

   public void tick() {
      if (this.freeze != null) {
         this.freeze.tick();
      }

      if (this.freeze == null || !this.freeze.isLocked()) {
         this.handleTimeTask();
      }
   }

   private void handleTimeTask() {
      TimeTask highest = this.getHighestPriorityTask();
      if (highest != null) {
         this.setDayTimeRule(true);
         highest.tick();
         if (highest.isStopped() && this.tasks.isEmpty()) {
            this.setDayTimeRule(false);
         }
      }
   }

   public double getTaskSpeedFactor() {
      TimeTask highest = this.getHighestPriorityTask();
      return highest != null ? highest.getSpeedFactor() : 0.3;
   }

   public void setTaskSpeedFactor(double speedFactor) {
      TimeTask highest = this.getHighestPriorityTask();
      if (highest != null) {
         highest.setSpeedFactor(speedFactor);
      }
   }

   public void addTask(String id, TimeTask task) {
      this.addTask(id, task, this.getTime());
   }

   public void addTask(String id, TimeTask task, long initialTime) {
      task.setCreationTime(initialTime);
      this.tasks.put(id, task);
   }

   public void stopTimeTask() {
      String highestTaskId = this.getHighestPriorityTaskId();
      if (highestTaskId != null) {
         this.stopTimeTask(highestTaskId);
      }
   }

   public void resumeTimeTask() {
      String highestTaskId = this.getHighestPriorityTaskId();
      if (highestTaskId != null) {
         this.resumeTimeTask(highestTaskId);
      }
   }

   public void removeTimeTask(String key) {
      if ("default_time_task".equals(key)) {
         Storytelling.LOG.warn("Attempted to stop the default time task. Default time task can't be removed");
      } else {
         this.tasks.remove(key);
      }
   }

   public void stopTimeTask(String key) {
      if ("default_time_task".equals(key)) {
         Storytelling.LOG.warn("Attempted to stop the default time task. If this is intended, you can ignore this warning.");
      }

      TimeTask task = this.tasks.get(key);
      if (task != null) {
         task.setStopped(true);
      }
   }

   public void resumeTimeTask(String key) {
      if ("default_time_task".equals(key)) {
         Storytelling.LOG.warn("Attempted to resume the default time task. If this is intended, you can ignore this warning.");
      }

      TimeTask task = this.tasks.get(key);
      if (task != null) {
         task.setStopped(false);
      }
   }

   public void addTime(long timeDiff) {
      this.setTime(this.getTime() + timeDiff);
   }

   public long getTime() {
      return this.getMinecraftServer().overworld().dayTime();
   }

   public void setTime(long time) {
      this.setTime(null, time);
   }

   public void setTime(Integer newCurrentDay, long time) {
      for (ServerLevel level : this.getMinecraftServer().getAllLevels()) {
         level.setDayTime(time);
      }

      this.updateTimeOnClient();
      if (newCurrentDay != null) {
         get().setCurrentDay(newCurrentDay);
      }

      this.tasks.values().forEach(task -> task.timeOverride(time));
   }

   public void setDayTimeRule(boolean locked) {
      ((BooleanValue)this.getMinecraftServer().getGameRules().getRule(GameRules.RULE_DAYLIGHT)).set(!locked, this.getMinecraftServer());
   }

   public void lockTime() {
      this.freeze = new TimeFreeze(this);
   }

   public void lockTime(TimeFreeze freeze) {
      this.removeFreeze(false);
      this.freeze = freeze;
   }

   public void unlockTime(boolean restoreTime) {
      this.removeFreeze(restoreTime);
      this.setDayTimeRule(false);
   }

   private void removeFreeze(boolean restoreTime) {
      if (this.freeze != null) {
         this.freeze.onEnd(restoreTime);
         this.freeze = null;
      }
   }

   public void updateTimeOnClient() {
      MinecraftServer mcServer = this.getMinecraftServer();

      for (ServerPlayer player : mcServer.getPlayerList().getPlayers()) {
         ServerLevel level = player.serverLevel();
         player.connection
            .send(new ClientboundSetTimePacket(level.getGameTime(), level.dayTime(), mcServer.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT)));
      }
   }

   public boolean isTimeLocked() {
      return this.freeze != null && this.freeze.isLocked() || this.getHighestPriorityTask() != null && this.getHighestPriorityTask().isStopped();
   }

   public void incrementCurrentDay(int dayCount) {
      this.currentDay = this.currentDay + dayCount;
   }

   public CompoundTag serializeNBT() {
      CompoundTag tag = new CompoundTag();
      if (this.freeze != null) {
         tag.put("freeze", this.freeze.serializeNBT());
      }

      if (this.currentDay != null) {
         tag.putInt("current_day", this.currentDay);
      }

      ListTag taskList = new ListTag();
      this.tasks.forEach((key, task) -> {
         if (!"default_time_task".equals(key)) {
            CompoundTag inner = new CompoundTag();
            inner.put(key, task.serializeNBT());
            taskList.add(inner);
         }
      });
      tag.put("timeTasks", taskList);
      return tag;
   }

   public void deserializeNBT(CompoundTag nbt) {
      CompoundTag freezeTag = nbt.getCompound("freeze");
      if (!freezeTag.isEmpty()) {
         this.freeze = new TimeFreeze(this, freezeTag);
      }

      boolean hasCurrentDay = nbt.contains("current_day");
      if (hasCurrentDay) {
         this.currentDay = nbt.getInt("current_day");
      } else {
         this.currentDay = 0;
      }

      this.tasks.put("default_time_task", TimeTask.builder(this).stopAt(TimeCondition.infinity(this)).speed(0.1).speedFactor(0.3).updateEvent(false).build());

      for (Tag tagElement : nbt.getList("timeTasks", 10)) {
         if (tagElement instanceof CompoundTag compound) {
            compound.getAllKeys().stream().findFirst().ifPresent(key -> {
               CompoundTag inner = compound.getCompound(key);
               this.tasks.put(key, new TimeTask(this, inner));
            });
         }
      }
   }

   private MinecraftServer getMinecraftServer() {
      return ServerLifecycleHooks.getCurrentServer();
   }

   @Nullable
   public String getHighestPriorityTaskId() {
      return this.tasks.entrySet().stream().max(Comparator.comparingInt(e -> e.getValue().getPriority())).map(Entry::getKey).orElse(null);
   }

   @Nullable
   public TimeTask getHighestPriorityTask() {
      return this.tasks.values().stream().max(Comparator.comparingInt(TimeTask::getPriority)).orElse(null);
   }

   @Nullable
   @Generated
   public TimeFreeze getFreeze() {
      return this.freeze;
   }

   @Generated
   public Integer getCurrentDay() {
      return this.currentDay;
   }

   @Generated
   public void setCurrentDay(Integer currentDay) {
      this.currentDay = currentDay;
   }
}
