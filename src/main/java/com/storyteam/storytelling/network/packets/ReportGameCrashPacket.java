package com.storyteam.storytelling.network.packets;

import com.storyteam.storytelling.network.BasePacket;
import com.storyteam.storytelling.network.utils.RegisterSTPacket;
import lombok.Generated;

@RegisterSTPacket
public class ReportGameCrashPacket extends BasePacket {
   private String report;
   private String log;

   public ReportGameCrashPacket() {
      super("reportGameCrash");
   }

   public ReportGameCrashPacket(String report, String log) {
      this();
      this.report = report;
      this.log = log;
   }

   @Generated
   public String getReport() {
      return this.report;
   }

   @Generated
   public String getLog() {
      return this.log;
   }
}
