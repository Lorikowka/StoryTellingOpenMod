package com.storyteam.storytelling.api.entity;

import it.unimi.dsi.fastutil.ints.IntIntPair;
import java.util.OptionalInt;
import java.util.Stack;

public interface IScriptOwnedEntity {
   boolean useByScript(int var1, int var2);

   void releaseScriptControl(int var1);

   OptionalInt getCurrentScriptInUse();

   public static class DefaultOwningContainer implements IScriptOwnedEntity {
      protected int currentlyActive;
      protected int currentPriority = Integer.MIN_VALUE;
      protected final Stack<IntIntPair> controls = new Stack<>();

      @Override
      public boolean useByScript(int scriptId, int priority) {
         if (!this.controls.isEmpty()) {
            if (priority <= this.currentPriority) {
               return false;
            }

            if (scriptId == this.currentlyActive) {
               this.controls.pop();
               this.currentPriority = priority;
               this.controls.push(IntIntPair.of(scriptId, priority));
               return true;
            }
         }

         this.currentlyActive = scriptId;
         this.currentPriority = priority;
         this.controls.push(IntIntPair.of(scriptId, priority));
         return true;
      }

      @Override
      public void releaseScriptControl(int scriptId) {
         if (this.currentlyActive == scriptId) {
            this.controls.pop();
            if (!this.controls.isEmpty()) {
               IntIntPair nextPair = this.controls.peek();
               this.currentlyActive = nextPair.leftInt();
               this.currentPriority = nextPair.rightInt();
            } else {
               this.currentPriority = Integer.MIN_VALUE;
               this.currentlyActive = 0;
            }
         } else {
            this.controls.removeIf(p -> p.leftInt() == scriptId);
         }
      }

      @Override
      public OptionalInt getCurrentScriptInUse() {
         return this.controls.isEmpty() ? OptionalInt.empty() : OptionalInt.of(this.currentlyActive);
      }

      @Override
      public String toString() {
         return "{currentlyActive=" + this.currentlyActive + ", currentPriority=" + this.currentPriority + ", controls=" + this.controls + "}";
      }
   }
}
