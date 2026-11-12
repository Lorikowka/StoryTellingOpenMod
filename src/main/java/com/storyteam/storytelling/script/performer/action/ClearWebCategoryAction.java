package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.network.utils.WebMessageHandler;
import com.storyteam.storytelling.script.performer.ScriptPerformer;

public class ClearWebCategoryAction extends ScriptPerformerAction {
   public ClearWebCategoryAction(ScriptPerformer performer) {
      super(performer);
   }

   @Override
   public void tick() {
      WebMessageHandler.clearNotices(this.performer.script.getId());
   }

   @Override
   public void fastForwardToStage() {
      WebMessageHandler.clearNotices(this.performer.script.getId());
   }
}
