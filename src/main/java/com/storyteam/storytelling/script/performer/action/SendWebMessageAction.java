package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.network.utils.WebMessageHandler;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;
import lombok.Generated;

public class SendWebMessageAction extends ScriptPerformerAction {
   private final String message;
   private final boolean isDebug;

   public SendWebMessageAction(ScriptPerformer performer, String message, boolean isDebug) {
      super(performer);
      this.message = message;
      this.isDebug = isDebug;
   }

   @Override
   public void tick() {
      WebMessageHandler.sendInstructionLine(
         this.performer.getName() + "_" + this.performer.getActionIndex(), this.performer.script.getId(), this.message, this.isDebug
      );
   }

   @Override
   public void fastForwardToStage() {
      WebMessageHandler.sendInstructionLine(
         this.performer.getName() + "_" + this.performer.getActionIndex(), this.performer.script.getId(), this.message, this.isDebug
      );
   }

   @Override
   public void rewindToStage() {
      WebMessageHandler.removeInstructionLine(this.performer.getName() + "_" + this.performer.getActionIndex(), this.performer.script.getId());
   }

   @Override
   public HelpReportBuilder gatherInstruction(HelpReportBuilder builder) {
      return builder.append(this.message);
   }

   @Generated
   public String getMessage() {
      return this.message;
   }

   @Generated
   public boolean isDebug() {
      return this.isDebug;
   }
}
