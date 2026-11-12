package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;
import com.storyteam.storytelling.util.ScriptUtils;
import net.minecraft.resources.ResourceLocation;

public class WaitPlayerEnterDimensionAction extends ScriptPerformerAction {
   private final ResourceLocation id;

   public WaitPlayerEnterDimensionAction(ScriptPerformer performer, ResourceLocation id) {
      super(performer);
      this.id = id;
   }

   @Override
   public boolean isDone() {
      return ScriptUtils.getPlayer().level().dimension().location().equals(this.id);
   }

   @Override
   public HelpReportBuilder gatherHelpReport(HelpReportBuilder builder) {
      return builder.append("ожидается заход в измерение %s", this.id);
   }
}
