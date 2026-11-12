package com.storyteam.storytelling.system.simulator.me_tab.fragment.data;

import com.storyteam.storytelling.system.simulator.me_tab.fragment.net.FragmentAddQueueWordAnimationPacket;
import net.minecraft.server.level.ServerPlayer;
import org.zeith.hammerlib.net.Network;

public enum FragmentState {
   TRIGGERED {
      @Override
      public void onEnter(FragmentInstance inst) {
         Network.sendToAll(new FragmentAddQueueWordAnimationPacket(inst.getData().getId()));
      }
   },
   GIVEN {
      @Override
      public void tick(FragmentInstance inst, ServerPlayer player) {
         for (DescribedCondition condition : inst.getData().getDescribedConditions()) {
            if (!condition.test(player)) {
               return;
            }
         }

         inst.setState(THINKING);
      }
   },
   THINKING {
      @Override
      public void onEnter(FragmentInstance inst) {
         inst.initTimeStartEnd();
      }

      @Override
      public void tick(FragmentInstance inst, ServerPlayer player) {
         if (inst.getNow().compareTo(inst.getEnd()) >= 0) {
            inst.setState(READY);
         }
      }
   },
   READY {
      @Override
      public boolean canAccept() {
         return true;
      }

      @Override
      public boolean canReject() {
         return true;
      }
   },
   FINISHED;

   public void onEnter(FragmentInstance inst) {
   }

   public void onExit(FragmentInstance inst) {
   }

   public void tick(FragmentInstance inst, ServerPlayer player) {
   }

   public boolean canAccept() {
      return false;
   }

   public boolean canReject() {
      return false;
   }
}
