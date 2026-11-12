package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.system._core.st_cutscene.ClientsideCutsceneHandler;
import com.storyteam.storytelling.system._core.st_cutscene.ServersideCutsceneHandler;
import com.storyteam.storytelling.system.dialog.Dialog;
import com.storyteam.storytelling.system.dialog.DialogHandler;

public class DialogAction extends ScriptPerformerAction {
   private Character<? extends AnimatedEntity> character;
   private Dialog dialog;
   private boolean openDialog;
   private boolean isDone;
   private boolean initialized = false;

   public DialogAction(ScriptPerformer performer, Dialog dialog, boolean openDialog) {
      super(performer);
      this.openDialog = openDialog;
      this.dialog = dialog;
      this.character = dialog.getOwner();
   }

   @Override
   public void tick() {
      super.tick();
      if (!this.initialized) {
         if (this.openDialog) {
            DialogHandler.setAndOpenDialog(this.dialog);
         } else {
            DialogHandler.setDialog(this.dialog);
         }

         this.initialized = true;
      }

      boolean cutsceneEnded = ServersideCutsceneHandler.currentData == null && ClientsideCutsceneHandler.currentData == null;
      this.isDone = !DialogHandler.isDialogPresent(this.character) && cutsceneEnded;
   }

   @Override
   public boolean isDone() {
      return this.isDone;
   }

   @Override
   public void reset() {
      super.reset();
      DialogHandler.removeDialog(this.character);
      this.initialized = false;
      this.isDone = false;
   }
}
