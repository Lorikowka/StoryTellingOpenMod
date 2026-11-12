package com.storyteam.storytelling.system.time.performer.task;

import com.storyteam.storytelling.script.performer.Performer;
import com.storyteam.storytelling.script.performer.action.PerformerAction;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;
import com.storyteam.storytelling.system.time.core.TimeController;
import com.storyteam.storytelling.system.time.data.TimeOfDay;

public class WaitTimeBetweenAction<P extends Performer<P>> extends PerformerAction<P> {
   final TimeOfDay lowerEdge;
   final TimeOfDay upperEdge;

   public WaitTimeBetweenAction(P performer, String lowerEdge, String upperEdge) {
      super(performer);
      this.lowerEdge = TimeOfDay.parse(lowerEdge);
      this.upperEdge = TimeOfDay.parse(upperEdge);
   }

   @Override
   public boolean isDone() {
      return TimeOfDay.between(TimeController.get().getTime(), this.lowerEdge, this.upperEdge, true);
   }

   @Override
   public HelpReportBuilder gatherHelpReport(HelpReportBuilder builder) {
      return builder.append("Ожидание времени между " + this.lowerEdge.getAsString() + " и " + this.upperEdge.getAsString());
   }
}
