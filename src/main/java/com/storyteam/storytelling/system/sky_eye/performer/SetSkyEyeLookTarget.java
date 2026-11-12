package com.storyteam.storytelling.system.sky_eye.performer;

import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.script.instances.backs.mechanics.S102SkyEye;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.action.ScriptPerformerAction;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;
import com.storyteam.storytelling.script.story.ScriptEnvironment;

public class SetSkyEyeLookTarget extends ScriptPerformerAction {
   private final String target;
   private boolean isDone = false;

   public SetSkyEyeLookTarget(ScriptPerformer performer, String target) {
      super(performer);
      this.target = target;
   }

   @Override
   public void tick() {
      if (!this.isDone) {
         this.setTarget();
         this.isDone = true;
      }
   }

   @Override
   public void fastForwardToStage() {
      this.setTarget();
   }

   @Override
   public void globalFastForward() {
      this.setTarget();
   }

   @Override
   public boolean isDone() {
      return this.isDone;
   }

   @Override
   public void reset() {
      this.isDone = false;
   }

   private void setTarget() {
      ScriptEnvironment scriptEnv = IStoryServer.get().getScriptEnv();
      if (scriptEnv.getScript(-102) instanceof S102SkyEye s102) {
         s102.entityLookAt(this.target);
      }
   }

   @Override
   public HelpReportBuilder gatherHelpReport(HelpReportBuilder builder) {
      return builder.append("Установка цели глазу.");
   }
}
