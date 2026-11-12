package com.storyteam.storytelling.system.char_icon.performer;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.action.ScriptPerformerAction;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.system.char_icon.network.S2CRemoveItemRender;
import org.zeith.hammerlib.net.Network;

public class RemoveRenderedIcon extends ScriptPerformerAction {
   private final String refId;
   private boolean isDone = false;

   public RemoveRenderedIcon(ScriptPerformer performer, Character<?> character) {
      super(performer);
      this.refId = character.getReferenceId();
   }

   @Override
   public void tick() {
      if (!this.isDone) {
         Network.sendToAll(new S2CRemoveItemRender(this.refId));
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
      Network.sendToAll(new S2CRemoveItemRender(this.refId));
   }

   @Override
   public void globalFastForward() {
      Network.sendToAll(new S2CRemoveItemRender(this.refId));
   }

   @Override
   public HelpReportBuilder gatherHelpReport(HelpReportBuilder builder) {
      return builder.append("Удаление иконки из рендер персонажа.");
   }
}
