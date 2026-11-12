package com.storyteam.storytelling.system.simulator.main_page.performer;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.action.ScriptPerformerAction;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;
import com.storyteam.storytelling.system.simulator.main_page.MainTabStorage;
import com.storyteam.storytelling.system.simulator.main_page.MainTabType;

public class SetSimulatorBlocking extends ScriptPerformerAction {
   private final MainTabType type;
   private final boolean state;
   private boolean isDone = false;

   public SetSimulatorBlocking(ScriptPerformer performer, MainTabType type, boolean state) {
      super(performer);
      this.type = type;
      this.state = state;
   }

   @Override
   public void tick() {
      if (!this.isDone) {
         MainTabStorage.setBlocked(this.type, this.state);
         this.isDone = true;
      }
   }

   @Override
   public void fastForwardToStage() {
      MainTabStorage.setBlocked(this.type, this.state);
   }

   @Override
   public void globalFastForward() {
      MainTabStorage.setBlocked(this.type, this.state);
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
   public HelpReportBuilder gatherHelpReport(HelpReportBuilder builder) {
      return builder.append("Установка блокировки симулятора.");
   }
}
