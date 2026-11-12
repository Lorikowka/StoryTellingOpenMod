package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;
import com.storyteam.storytelling.util.ToStringBuilder;
import org.zeith.hammerlib.util.shaded.json.JSONObject;

public class ConsumeFlagAction extends ScriptPerformerAction {
   private final String flag;
   private boolean init = false;
   private final boolean doConsume;

   public ConsumeFlagAction(ScriptPerformer performer, String flag, boolean doConsume) {
      super(performer);
      this.flag = flag;
      this.doConsume = doConsume;
   }

   @Override
   public void tick() {
      if (!this.init) {
         this.init = true;
         if (this.doConsume) {
            this.script.getFlags().remove(this.flag);
         }
      }
   }

   @Override
   public void reset() {
      this.init = false;
   }

   @Override
   public void fastForwardToStage() {
      if (this.doConsume) {
         this.script.getFlags().remove(this.flag);
      }
   }

   @Override
   public void rewindToStage() {
      if (this.doConsume) {
         this.script.getFlags().remove(this.flag);
      }

      this.init = false;
   }

   @Override
   public HelpReportBuilder gatherHelpReport(HelpReportBuilder builder) {
      return builder.append("Ожидание появление флага " + JSONObject.quote(this.flag));
   }

   @Override
   public boolean isDone() {
      return this.doConsume ? this.script.getFlags().tryRemove(this.flag) : this.script.getFlags().has(this.flag);
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this).append("flag", this.flag).append("doConsume", this.doConsume).build();
   }
}
