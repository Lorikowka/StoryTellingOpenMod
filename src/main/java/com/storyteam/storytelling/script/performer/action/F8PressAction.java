package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.network.utils.WebMessageHandler;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;
import net.minecraft.nbt.CompoundTag;

public class F8PressAction extends ScriptPerformerAction implements ActionCheckpoint {
   private boolean isDone = false;

   public F8PressAction(ScriptPerformer performer) {
      super(performer);
   }

   @Override
   public boolean isDone() {
      return this.isDone;
   }

   @Override
   public void reset() {
      this.isDone = false;
   }

   @Override
   public void fastForwardToStage() {
      this.isDone = true;
   }

   @Override
   public void rewindToStage() {
      this.isDone = false;
   }

   @Override
   public HelpReportBuilder gatherHelpReport(HelpReportBuilder builder) {
      return builder.append("Клавиша F8.");
   }

   @Override
   public HelpReportBuilder gatherInstruction(HelpReportBuilder builder) {
      return builder.append("Клавиша F8.");
   }

   @Override
   public void tick() {
      if (this.isDone) return;
      super.tick();
      WebMessageHandler.sendInstructionLine(
         this.performer.getName() + "_" + this.performer.getActionIndex(),
         this.performer.script.getId(),
         this.gatherHelpReport(new HelpReportBuilder()).build(),
         false
      );
   }

   @Override
   public <T> void handleEvent(EventType<T> eventType, T event) {
      if (eventType.equals(EventType.F8_PRESS)) {
         this.isDone = true;
      }
   }

   @Override
   public CompoundTag saveActionState() {
      CompoundTag state = new CompoundTag();
      state.putBoolean("done", this.isDone);
      return state;
   }

   @Override
   public void restoreActionState(CompoundTag state) {
      this.isDone = state.getBoolean("done");
   }
}
