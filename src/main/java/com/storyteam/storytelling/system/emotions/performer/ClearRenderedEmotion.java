package com.storyteam.storytelling.system.emotions.performer;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.action.ScriptPerformerAction;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;
import com.storyteam.storytelling.system.emotions.network.S2CClearCharacterEmotions;
import org.zeith.hammerlib.net.Network;

public class ClearRenderedEmotion extends ScriptPerformerAction {
   private final String refId;
   private boolean isDone = false;

   public ClearRenderedEmotion(ScriptPerformer performer, String refId) {
      super(performer);
      this.refId = refId;
   }

   @Override
   public void tick() {
      if (!this.isDone) {
         Network.sendToAll(new S2CClearCharacterEmotions(this.refId));
         this.isDone = true;
      }
   }

   @Override
   public void reset() {
      super.reset();
      this.isDone = false;
   }

   @Override
   public boolean isDone() {
      return this.isDone;
   }

   @Override
   public void fastForwardToStage() {
      Network.sendToAll(new S2CClearCharacterEmotions(this.refId));
   }

   @Override
   public void globalFastForward() {
      Network.sendToAll(new S2CClearCharacterEmotions(this.refId));
   }

   @Override
   public HelpReportBuilder gatherHelpReport(HelpReportBuilder builder) {
      return builder.append("Удаление эмоций персонажа.");
   }
}
