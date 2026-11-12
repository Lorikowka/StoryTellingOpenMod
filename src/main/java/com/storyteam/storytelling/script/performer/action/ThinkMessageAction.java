package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;
import com.storyteam.storytelling.script.performer.state.SpeechEntity;
import com.storyteam.storytelling.util.ToStringBuilder;
import java.util.function.Supplier;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class ThinkMessageAction extends SpeechMessageAction {
   private String name = "Lololowka";
   private String icon = "thinking_timeless";

   public ThinkMessageAction(ScriptPerformer performer, @Nullable Supplier<Component> flavor, Supplier<Component> message, boolean waitForF8) {
      super(performer, 0, flavor, message, waitForF8);
   }

   public ThinkMessageAction(
      ScriptPerformer performer, @Nullable Supplier<Component> flavor, Supplier<Component> message, String name, String icon, boolean waitForF8
   ) {
      super(performer, 0, flavor, message, waitForF8);
      this.name = name;
      this.icon = icon;
   }

   @Override
   protected SpeechEntity getSpeaker() {
      return new SpeechEntity(this.name, 16750070, this.icon);
   }

   @Override
   public HelpReportBuilder gatherHelpReport(HelpReportBuilder builder) {
      return builder.append("Мысли: %s %s", this.message.get().getString(), this.waitForF8 ? " [F8]" : "");
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this)
         .append("message", this.message.get() == null ? null : this.message.get().getString())
         .append("waitForF8", this.waitForF8)
         .build();
   }
}
