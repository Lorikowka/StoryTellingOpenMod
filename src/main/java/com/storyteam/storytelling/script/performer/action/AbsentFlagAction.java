package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;
import com.storyteam.storytelling.util.ToStringBuilder;
import org.zeith.hammerlib.util.shaded.json.JSONObject;

public class AbsentFlagAction extends ScriptPerformerAction {
   private final String flag;
   private boolean init = false;

   public AbsentFlagAction(ScriptPerformer performer, String flag) {
      super(performer);
      this.flag = flag;
   }

   @Override
   public void tick() {
      if (!this.init) {
         this.init = true;
      }
   }

   @Override
   public void reset() {
      this.init = false;
   }

   @Override
   public void fastForwardToStage() {
   }

   @Override
   public void rewindToStage() {
      this.init = false;
   }

   @Override
   public HelpReportBuilder gatherHelpReport(HelpReportBuilder builder) {
      return builder.append("Ожидание отсутствия флага " + JSONObject.quote(this.flag));
   }

   @Override
   public boolean isDone() {
      return !this.script.getFlags().has(this.flag);
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this).append("flag", this.flag).build();
   }
}
