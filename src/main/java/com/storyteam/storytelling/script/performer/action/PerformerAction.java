package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.script.performer.IPerformer;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;
import com.storyteam.storytelling.util.ToStringBuilder;
import java.util.Random;
import lombok.Generated;

public abstract class PerformerAction<P extends IPerformer<P>> {
   protected P performer;
   protected static final Random DEFALT_RANDOM = new Random();

   public PerformerAction(P performer) {
      this.performer = performer;
   }

   public void tick() {
   }

   public void stop() {
   }

   public void reset() {
   }

   public void reset0() {
      this.reset();
   }

   public void globalFastForward() {
   }

   public void fastForwardToStage() {
   }

   public void rewindToStage() {
   }

   public <T> void handleEvent(EventType<T> eventType, T event) {
   }

   public void onScriptInstanceCreated() {
   }

   public void onInit() {
      this.reset();
   }

   public void onScriptStop() {
   }

   public boolean shouldContinue() {
      return false;
   }

   public boolean isDone() {
      return true;
   }

   public HelpReportBuilder gatherHelpReport(HelpReportBuilder builder) {
      return builder.append(this.getClass().getSimpleName());
   }

   public HelpReportBuilder gatherInstruction(HelpReportBuilder builder) {
      return builder;
   }

   public String getId() {
      return this.performer.getName() + "_" + this.performer.getActionIndex(this);
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this).build();
   }

   @Generated
   public P getPerformer() {
      return this.performer;
   }
}
