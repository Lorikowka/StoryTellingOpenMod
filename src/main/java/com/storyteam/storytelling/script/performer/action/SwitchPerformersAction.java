package com.storyteam.storytelling.script.performer.action;

import com.google.common.base.Suppliers;
import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.script.performer.Performer;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;
import com.storyteam.storytelling.script.performer.state.StateHolder;
import com.storyteam.storytelling.util.ToStringBuilder;
import java.util.UUID;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import org.zeith.hammerlib.util.java.Cast;

public class SwitchPerformersAction<P extends Performer<P>> extends PerformerAction<P> {
   private final IntSupplier condition;
   private final Performer<?>[] branches;
   private Integer curBranch = null;
   private final boolean dynamic;
   private boolean started;
   private final Supplier<UUID> uniqueIdentifier = Suppliers.memoize(UUID::randomUUID);
   private boolean hasCreated;

   public SwitchPerformersAction(P performer, IntSupplier condition, boolean dynamicCondition, Performer<?>... branches) {
      super(performer);
      this.condition = condition;
      this.dynamic = dynamicCondition;
      this.branches = branches;
   }

   @Override
   public void globalFastForward() {
      this.started = true;
      this.curBranch = this.condition.getAsInt();
      if (this.curBranch >= 0 && this.curBranch < this.branches.length) {
         this.branches[this.curBranch].getActions().forEach(PerformerAction::globalFastForward);
      }
   }

   @Override
   public void tick() {
      if (!this.started) {
         this.started = true;
         this.curBranch = this.condition.getAsInt();
         if (this.curBranch >= 0 && this.curBranch < this.branches.length) {
            ((Performer)this.branches[this.curBranch]).inheritFrom((Performer)Cast.cast(this.performer));
            this.branches[this.curBranch].gotoSavepoint("start");
         }

         for (Performer<?> branch : this.branches) {
            branch.onStart();
         }
      }

      int flag = this.dynamic ? (this.curBranch = this.condition.getAsInt()) : this.curBranch;
      if (flag >= 0 && flag < this.branches.length) {
         this.branches[flag].tick();
      }
   }

   @Override
   public void fastForwardToStage() {
      this.started = true;
      this.curBranch = this.condition.getAsInt();
      if (this.curBranch >= 0 && this.curBranch < this.branches.length) {
         this.branches[this.curBranch].gotoSavepoint("final");
      }
   }

   @Override
   public void rewindToStage() {
      this.started = false;
      this.curBranch = null;

      for (Performer<?> branch : this.branches) {
         branch.gotoSavepoint("start");
      }
   }

   @Override
   public void reset() {
      this.started = false;
      this.curBranch = null;
   }

   @Override
   public void onScriptInstanceCreated() {
      int n = 1;

      for (Performer<?> innerPerformer : this.branches) {
         innerPerformer.setName(this.performer.getName() + "_inner" + n++);
         ((Performer)innerPerformer).setStateRepository((StateHolder)this.performer.getStateRepository());
         innerPerformer.onScriptInstanceCreated();
      }

      this.hasCreated = true;
   }

   @Override
   public boolean isDone() {
      if (this.dynamic) {
         int branch = this.condition.getAsInt();
         return branch < 0 || branch < this.branches.length && this.branches[branch].isCompleted();
      } else {
         return this.curBranch != null && (this.curBranch < 0 || this.curBranch < this.branches.length && this.branches[this.curBranch].isCompleted());
      }
   }

   @Override
   public <T> void handleEvent(EventType<T> eventType, T event) {
      if (this.hasCreated) {
         for (Performer<?> scriptPerformer : this.branches) {
            scriptPerformer.handleEvent(eventType, event);
         }
      }
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this).append("branches", this.branches).append("conditionState", this.curBranch).build();
   }

   @Override
   public HelpReportBuilder gatherHelpReport(HelpReportBuilder builder) {
      if (this.curBranch == null) {
         builder.append("Не выбрана ветка");
      } else if (this.curBranch > 0 && this.curBranch <= this.branches.length) {
         builder.append(this.branches[this.curBranch].gatherHelpReport());
      }

      return builder;
   }
}
