package com.storyteam.storytelling.script.performer.report;

import org.jetbrains.annotations.NotNull;

public class HelpReportBuilder {
   private final StringBuilder builder = new StringBuilder();
   private int messageIndex;

   public static HelpReportBuilder builder() {
      return new HelpReportBuilder();
   }

   public HelpReportBuilder append(@NotNull String text) {
      if (text.length() == 0) {
         return this;
      } else {
         if (this.messageIndex++ != 0) {
            this.builder.append(", ");
         }

         this.builder.append(text);
         return this;
      }
   }

   public HelpReportBuilder append(String format, Object... args) {
      if (this.messageIndex++ != 0) {
         this.builder.append(", ");
      }

      this.builder.append(String.format(format, args));
      return this;
   }

   public String build() {
      return this.builder.toString();
   }
}
