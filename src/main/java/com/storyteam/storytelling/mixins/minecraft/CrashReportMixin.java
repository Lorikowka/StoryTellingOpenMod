package com.storyteam.storytelling.mixins.minecraft;

import com.storyteam.storytelling.network.StoryServerHooks;
import java.io.File;
import java.nio.file.Files;
import net.minecraft.CrashReport;
import net.minecraftforge.fml.util.thread.EffectiveSide;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({CrashReport.class})
public abstract class CrashReportMixin {
   @Shadow
   @Final
   private String title;

   @Shadow
   public abstract String getFriendlyReport();

   @Redirect(
      method = {"saveToFile"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/CrashReport;getFriendlyReport()Ljava/lang/String;"
      )
   )
   public String catchCrashReportSummary(CrashReport instance) {
      String str = instance.getFriendlyReport();
      File file = new File("logs/latest.log");

      String log;
      try {
         log = Files.readString(file.toPath());
      } catch (Exception var6) {
         log = "Failed to read log file";
      }

      if (EffectiveSide.get().isClient()) {
         StoryServerHooks.sendCrashReport(this.getFriendlyReport(), log);
      }

      return str;
   }
}
