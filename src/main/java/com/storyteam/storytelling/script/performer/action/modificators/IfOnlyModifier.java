package com.storyteam.storytelling.script.performer.action.modificators;

import com.storyteam.storytelling.script.performer.Performer;
import com.storyteam.storytelling.script.performer.action.PerformerAction;
import java.util.List;
import java.util.function.BooleanSupplier;
import org.jetbrains.annotations.NotNull;

public class IfOnlyModifier<P extends Performer<P>> implements PerformerModifier<P> {
   BooleanSupplier supplier;
   boolean dynamicCondition;

   public IfOnlyModifier(BooleanSupplier supplier, boolean dynamicCondition) {
      this.supplier = supplier;
      this.dynamicCondition = dynamicCondition;
   }

   public void modify(@NotNull P performer, @NotNull List<PerformerAction<P>> actionTrace) {
      if (actionTrace.isEmpty()) {
         this.throwIllegalStateException(0, performer.getName());
      }

      PerformerAction<P> last = actionTrace.remove(actionTrace.size() - 1);
      if (this.dynamicCondition) {
         actionTrace.add(new PerformerActionDecorator<P>(performer, last) {
            @Override
            public void tick() {
               if (IfOnlyModifier.this.supplier.getAsBoolean()) {
                  super.tick();
               }
            }

            @Override
            public boolean isDone() {
               return super.isDone() || !IfOnlyModifier.this.supplier.getAsBoolean();
            }

            @Override
            public String toString() {
               return IfOnlyModifier.this.getName() + super.toString();
            }
         });
      } else {
         actionTrace.add(new PerformerActionDecorator<P>(performer, last) {
            boolean completed;
            boolean init;

            @Override
            public void tick() {
               if (!this.init) {
                  this.completed = IfOnlyModifier.this.supplier.getAsBoolean();
                  this.init = true;
               }

               if (this.completed) {
                  super.tick();
               }
            }

            @Override
            public boolean isDone() {
               return super.isDone() || !this.completed;
            }

            @Override
            public String toString() {
               return IfOnlyModifier.this.getName() + super.toString();
            }
         });
      }
   }

   @Override
   public String getName() {
      return "Условие";
   }
}
