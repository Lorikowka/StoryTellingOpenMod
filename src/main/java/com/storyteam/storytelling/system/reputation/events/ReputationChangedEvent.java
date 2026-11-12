package com.storyteam.storytelling.system.reputation.events;

import com.storyteam.storytelling.system.reputation.util.ChangeReason;
import lombok.Generated;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class ReputationChangedEvent extends Event {
   private int amount;
   private final ChangeReason reason;
   private String modifierId;
   private boolean shouldSendNotification;

   @Generated
   public int getAmount() {
      return this.amount;
   }

   @Generated
   public ChangeReason getReason() {
      return this.reason;
   }

   @Generated
   public String getModifierId() {
      return this.modifierId;
   }

   @Generated
   public boolean shouldSendNotification() {
      return this.shouldSendNotification;
   }

   @Generated
   public void setAmount(int amount) {
      this.amount = amount;
   }

   @Generated
   public void setModifierId(String modifierId) {
      this.modifierId = modifierId;
   }

   @Generated
   public ReputationChangedEvent shouldSendNotification(boolean shouldSendNotification) {
      this.shouldSendNotification = shouldSendNotification;
      return this;
   }

   @Generated
   public String toString() {
      return "ReputationChangedEvent(amount="
         + this.getAmount()
         + ", reason="
         + this.getReason()
         + ", modifierId="
         + this.getModifierId()
         + ", shouldSendNotification="
         + this.shouldSendNotification()
         + ")";
   }

   @Generated
   public ReputationChangedEvent(int amount, ChangeReason reason, String modifierId, boolean shouldSendNotification) {
      this.amount = amount;
      this.reason = reason;
      this.modifierId = modifierId;
      this.shouldSendNotification = shouldSendNotification;
   }

   @Generated
   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof ReputationChangedEvent other)) {
         return false;
      } else if (!other.canEqual(this)) {
         return false;
      } else if (!super.equals(o)) {
         return false;
      } else if (this.getAmount() != other.getAmount()) {
         return false;
      } else if (this.shouldSendNotification() != other.shouldSendNotification()) {
         return false;
      } else {
         Object this$reason = this.getReason();
         Object other$reason = other.getReason();
         if (this$reason == null ? other$reason == null : this$reason.equals(other$reason)) {
            Object this$modifierId = this.getModifierId();
            Object other$modifierId = other.getModifierId();
            return this$modifierId == null ? other$modifierId == null : this$modifierId.equals(other$modifierId);
         } else {
            return false;
         }
      }
   }

   @Generated
   protected boolean canEqual(Object other) {
      return other instanceof ReputationChangedEvent;
   }

   @Generated
   public int hashCode() {
      int PRIME = 59;
      int result = super.hashCode();
      result = result * 59 + this.getAmount();
      result = result * 59 + (this.shouldSendNotification() ? 79 : 97);
      Object $reason = this.getReason();
      result = result * 59 + ($reason == null ? 43 : $reason.hashCode());
      Object $modifierId = this.getModifierId();
      return result * 59 + ($modifierId == null ? 43 : $modifierId.hashCode());
   }
}
