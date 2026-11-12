package com.storyteam.storytelling.system.lockpicking.event;

import com.storyteam.storytelling.system.lockpicking.data.LockRegistryEntry;
import lombok.Generated;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.LogicalSide;

public class LockEvent {
   @Cancelable
   public static class Attempt extends Event {
      private LockRegistryEntry data;

      @Generated
      public LockRegistryEntry getData() {
         return this.data;
      }

      @Generated
      public void setData(LockRegistryEntry data) {
         this.data = data;
      }

      @Generated
      public String toString() {
         return "LockEvent.Attempt(data=" + this.getData() + ")";
      }

      @Generated
      public Attempt(LockRegistryEntry data) {
         this.data = data;
      }

      @Generated
      public boolean equals(Object o) {
         if (o == this) {
            return true;
         } else if (!(o instanceof LockEvent.Attempt other)) {
            return false;
         } else if (!other.canEqual(this)) {
            return false;
         } else if (!super.equals(o)) {
            return false;
         } else {
            Object this$data = this.getData();
            Object other$data = other.getData();
            return this$data == null ? other$data == null : this$data.equals(other$data);
         }
      }

      @Generated
      protected boolean canEqual(Object other) {
         return other instanceof LockEvent.Attempt;
      }

      @Generated
      public int hashCode() {
         int PRIME = 59;
         int result = super.hashCode();
         Object $data = this.getData();
         return result * 59 + ($data == null ? 43 : $data.hashCode());
      }
   }

   @Cancelable
   public static class Pre extends Event {
      private LockRegistryEntry data;
      private Level level;
      private Player player;

      public LogicalSide getSide() {
         return this.getLevel().isClientSide ? LogicalSide.CLIENT : LogicalSide.SERVER;
      }

      @Generated
      public LockRegistryEntry getData() {
         return this.data;
      }

      @Generated
      public Level getLevel() {
         return this.level;
      }

      @Generated
      public Player getPlayer() {
         return this.player;
      }

      @Generated
      public Pre(LockRegistryEntry data, Level level, Player player) {
         this.data = data;
         this.level = level;
         this.player = player;
      }

      @Generated
      public boolean equals(Object o) {
         if (o == this) {
            return true;
         } else if (!(o instanceof LockEvent.Pre other)) {
            return false;
         } else if (!other.canEqual(this)) {
            return false;
         } else if (!super.equals(o)) {
            return false;
         } else {
            Object this$data = this.getData();
            Object other$data = other.getData();
            if (this$data == null ? other$data == null : this$data.equals(other$data)) {
               Object this$level = this.getLevel();
               Object other$level = other.getLevel();
               if (this$level == null ? other$level == null : this$level.equals(other$level)) {
                  Object this$player = this.getPlayer();
                  Object other$player = other.getPlayer();
                  return this$player == null ? other$player == null : this$player.equals(other$player);
               } else {
                  return false;
               }
            } else {
               return false;
            }
         }
      }

      @Generated
      protected boolean canEqual(Object other) {
         return other instanceof LockEvent.Pre;
      }

      @Generated
      public int hashCode() {
         int PRIME = 59;
         int result = super.hashCode();
         Object $data = this.getData();
         result = result * 59 + ($data == null ? 43 : $data.hashCode());
         Object $level = this.getLevel();
         result = result * 59 + ($level == null ? 43 : $level.hashCode());
         Object $player = this.getPlayer();
         return result * 59 + ($player == null ? 43 : $player.hashCode());
      }

      @Generated
      public void setData(LockRegistryEntry data) {
         this.data = data;
      }
   }

   public static class Unlock extends Event {
      private ServerPlayer player;
      private LockRegistryEntry data;

      @Generated
      public ServerPlayer getPlayer() {
         return this.player;
      }

      @Generated
      public LockRegistryEntry getData() {
         return this.data;
      }

      @Generated
      public Unlock(ServerPlayer player, LockRegistryEntry data) {
         this.player = player;
         this.data = data;
      }

      @Generated
      public boolean equals(Object o) {
         if (o == this) {
            return true;
         } else if (!(o instanceof LockEvent.Unlock other)) {
            return false;
         } else if (!other.canEqual(this)) {
            return false;
         } else if (!super.equals(o)) {
            return false;
         } else {
            Object this$player = this.getPlayer();
            Object other$player = other.getPlayer();
            if (this$player == null ? other$player == null : this$player.equals(other$player)) {
               Object this$data = this.getData();
               Object other$data = other.getData();
               return this$data == null ? other$data == null : this$data.equals(other$data);
            } else {
               return false;
            }
         }
      }

      @Generated
      protected boolean canEqual(Object other) {
         return other instanceof LockEvent.Unlock;
      }

      @Generated
      public int hashCode() {
         int PRIME = 59;
         int result = super.hashCode();
         Object $player = this.getPlayer();
         result = result * 59 + ($player == null ? 43 : $player.hashCode());
         Object $data = this.getData();
         return result * 59 + ($data == null ? 43 : $data.hashCode());
      }
   }
}
