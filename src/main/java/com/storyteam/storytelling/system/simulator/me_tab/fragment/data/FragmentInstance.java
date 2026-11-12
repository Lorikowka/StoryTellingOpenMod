package com.storyteam.storytelling.system.simulator.me_tab.fragment.data;

import com.storyteam.storytelling.system.simulator.me_tab.fragment.call_context.ActionContext;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.call_context.CallContext;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.data.time.DayTimeData;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.data.time.DurationData;
import com.storyteam.storytelling.system.simulator.me_tab.net.FragmentAcceptRejectActionPacket;
import com.storyteam.storytelling.system.time.core.TimeController;
import com.storyteam.storytelling.system.time.data.TimeOfDay;
import com.storyteam.storytelling.util.ScriptUtils;
import lombok.Generated;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import org.zeith.hammerlib.api.io.IAutoNBTSerializable;
import org.zeith.hammerlib.api.io.NBTSerializable;
import org.zeith.hammerlib.net.Network;

public class FragmentInstance implements IAutoNBTSerializable {
   private FragmentData data;
   @NBTSerializable
   private FragmentState state;
   @NBTSerializable
   private FragmentResult result;
   DayTimeData start = null;
   DayTimeData end = null;
   private FragmentInstance.MoveAction pendingAction = FragmentInstance.MoveAction.NONE;
   private boolean conditionsPassed;

   public FragmentInstance(FragmentData fragmentData) {
      this.data = fragmentData;
      this.state = FragmentState.TRIGGERED;
      this.result = FragmentResult.NONE;
   }

   public void tick(ServerPlayer player) {
      this.recalculateState();
      this.state.tick(this, player);
   }

   public void initTimeStartEnd() {
      TimeController timeController = TimeController.get();
      long rawTime = timeController.getTime();
      this.start = new DayTimeData(timeController.getCurrentDay(), new TimeOfDay(rawTime));
      this.end = this.start.add(this.data.getDurationData());
   }

   public boolean setState(FragmentState newState) {
      if (newState == this.state) {
         return false;
      } else {
         if (this.state == FragmentState.FINISHED && newState.ordinal() < this.state.ordinal()) {
            this.rollbackActions(ScriptUtils.getPlayer());
            this.result = FragmentResult.NONE;
         }

         this.state.onExit(this);
         this.state = newState;
         this.state.onEnter(this);
         return true;
      }
   }

   public void recalculateState() {
      if (this.start != null && this.end != null) {
         DayTimeData now = this.getNow();
         boolean hasDecision = this.result == FragmentResult.ACCEPTED || this.result == FragmentResult.REJECTED;
         if (now.compareTo(this.start) >= 0) {
            FragmentState targetState;
            if (now.compareTo(this.end) < 0) {
               targetState = FragmentState.THINKING;
            } else if (!hasDecision) {
               targetState = FragmentState.READY;
            } else {
               targetState = FragmentState.FINISHED;
            }

            this.setState(targetState);
         }
      }
   }

   public void rollbackActions(ServerPlayer player) {
      if (player != null) {
         if (this.result == FragmentResult.ACCEPTED && this.data.getAcceptAction() != null) {
            CallContext.runIn(ActionContext.FRAGMENT_ROLLBACK_ACCEPT_ACTION, () -> this.data.getAcceptAction().rollbackAction(player));
            this.result = FragmentResult.NONE;
         } else if (this.result == FragmentResult.REJECTED && this.data.getRejectAction() != null) {
            CallContext.runIn(ActionContext.FRAGMENT_ROLLBACK_REJECT_ACTION, () -> this.data.getRejectAction().rollbackAction(player));
            this.result = FragmentResult.NONE;
         }
      }
   }

   public void accept() {
      Network.sendToServer(new FragmentAcceptRejectActionPacket(true, this.data.getId()));
   }

   public void reject() {
      Network.sendToServer(new FragmentAcceptRejectActionPacket(false, this.data.getId()));
   }

   public void applyServerSideAcceptReject(boolean accept, ServerPlayer player) {
      this.state = FragmentState.FINISHED;
      if (accept) {
         this.result = FragmentResult.ACCEPTED;
         CallContext.runIn(ActionContext.FRAGMENT_ACCEPT_ACTION, () -> this.data.getAcceptAction().applyAction(player));
      } else {
         this.result = FragmentResult.REJECTED;
         CallContext.runIn(ActionContext.FRAGMENT_REJECT_ACTION, () -> this.data.getRejectAction().applyAction(player));
      }

      this.setPendingAction(FragmentInstance.MoveAction.TO_HISTORY);
   }

   public void recalculateEndWithAcceleration(double accelerationFactor) {
      if (this.start != null && this.end != null) {
         long originalDuration = this.data.getDurationData().toTotalTicks();
         long acceleratedDuration = (long)((double)originalDuration / accelerationFactor);
         this.end = this.start.add(DurationData.fromTotalTicks(acceleratedDuration));
      }
   }

   public DayTimeData getNow() {
      TimeController timeController = TimeController.get();
      return new DayTimeData(timeController.getCurrentDay(), new TimeOfDay(timeController.getTime()));
   }

   public double progress() {
      if (this.start != null && this.end != null) {
         long s = this.start.toTotalTicks();
         long e = this.end.toTotalTicks();
         long n = this.getNow().toTotalTicks();
         if (n <= s) {
            return 0.0;
         } else {
            return n >= e ? 1.0 : (double)(n - s) / (double)(e - s);
         }
      } else {
         return 0.0;
      }
   }

   public CompoundTag serializeNBT() {
      CompoundTag tag = IAutoNBTSerializable.super.serializeNBT();
      if (this.start != null) {
         tag.put("start_time", this.start.serializeNBT());
      }

      if (this.end != null) {
         tag.put("end_time", this.end.serializeNBT());
      }

      return tag;
   }

   public void deserializeNBT(CompoundTag nbt) {
      IAutoNBTSerializable.super.deserializeNBT(nbt);
      if (nbt.contains("start_time")) {
         CompoundTag activeTag = nbt.getCompound("start_time");
         this.start = new DayTimeData(0, new TimeOfDay(0L));
         this.start.deserializeNBT(activeTag);
      }

      if (nbt.contains("end_time")) {
         CompoundTag activeTag = nbt.getCompound("end_time");
         this.end = new DayTimeData(0, new TimeOfDay(0L));
         this.end.deserializeNBT(activeTag);
      }
   }

   @Generated
   public FragmentData getData() {
      return this.data;
   }

   @Generated
   public FragmentState getState() {
      return this.state;
   }

   @Generated
   public FragmentResult getResult() {
      return this.result;
   }

   @Generated
   public DayTimeData getStart() {
      return this.start;
   }

   @Generated
   public DayTimeData getEnd() {
      return this.end;
   }

   @Generated
   public void setData(FragmentData data) {
      this.data = data;
   }

   @Generated
   public void setResult(FragmentResult result) {
      this.result = result;
   }

   @Generated
   public void setStart(DayTimeData start) {
      this.start = start;
   }

   @Generated
   public void setEnd(DayTimeData end) {
      this.end = end;
   }

   @Generated
   public FragmentInstance.MoveAction getPendingAction() {
      return this.pendingAction;
   }

   @Generated
   public void setPendingAction(FragmentInstance.MoveAction pendingAction) {
      this.pendingAction = pendingAction;
   }

   @Generated
   public boolean isConditionsPassed() {
      return this.conditionsPassed;
   }

   @Generated
   public void setConditionsPassed(boolean conditionsPassed) {
      this.conditionsPassed = conditionsPassed;
   }

   public static enum MoveAction {
      NONE,
      TO_ACTIVE,
      TO_HISTORY,
      REMOVE;
   }
}
