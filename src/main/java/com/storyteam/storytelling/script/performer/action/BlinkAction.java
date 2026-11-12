package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.client.content.overlays.BlinkOverlay;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;
import com.storyteam.storytelling.util.ScriptUtils;

public class BlinkAction extends ScriptPerformerAction {
   private final BlinkOverlay.Blink blackout;

   public BlinkAction(ScriptPerformer performer, int color, int inTime, int time, int outTime) {
      super(performer);
      this.blackout = new BlinkOverlay.Blink(inTime, time, outTime, color);
   }

   @Override
   public void tick() {
      ScriptUtils.setBlink(this.blackout.color(), this.blackout.inTime(), this.blackout.time(), this.blackout.outTime());
   }

   @Override
   public void rewindToStage() {
      ScriptUtils.setBlink(0, 0, 0, 0);
   }

   @Override
   public HelpReportBuilder gatherHelpReport(HelpReportBuilder builder) {
      return builder.append("Эффект закрытия глаз");
   }
}
