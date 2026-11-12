package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.client.content.overlays.BlackoutBelowChatOverlay;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;
import com.storyteam.storytelling.util.ScriptUtils;

public class ScreenColorBelowChatAction extends ScriptPerformerAction {
   private BlackoutBelowChatOverlay.Blackout blackout;

   public ScreenColorBelowChatAction(ScriptPerformer performer, int color, int inTime, int time, int outTime) {
      super(performer);
      this.blackout = new BlackoutBelowChatOverlay.Blackout(inTime, time, outTime, color);
   }

   @Override
   public void tick() {
      ScriptUtils.setBlackout(this.blackout.color(), this.blackout.inTime(), this.blackout.time(), this.blackout.outTime(), true);
   }

   @Override
   public void rewindToStage() {
      ScriptUtils.setBlackout(0, 0, 0, 0);
   }

   @Override
   public HelpReportBuilder gatherHelpReport(HelpReportBuilder builder) {
      return builder.append("Применение цвета экрана");
   }
}
