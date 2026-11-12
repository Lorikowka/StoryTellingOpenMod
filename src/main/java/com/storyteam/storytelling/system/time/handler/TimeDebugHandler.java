package com.storyteam.storytelling.system.time.handler;

import com.storyteam.storytelling.system.time.core.TimeController;
import com.storyteam.storytelling.system.time.core.TimeTask;
import com.storyteam.storytelling.system.time.data.TimeOfDay;
import com.storyteam.storytelling.system.videotest.GatherVideotestReportEvent;
import net.minecraft.ChatFormatting;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TimeDebugHandler {
   @SubscribeEvent
   public static void handleVideoTest(GatherVideotestReportEvent e) {
      TimeController timeController = TimeController.get();
      long rawTime = timeController.getTime();
      String formattedTime = new TimeOfDay(rawTime).getAsStringWithSeconds();
      TimeTask currentTask = timeController.getHighestPriorityTask();
      String taskId = timeController.getHighestPriorityTaskId();
      int taskPriority = currentTask.getPriority();
      boolean isStopped = TimeController.get().isTimeLocked();
      String speedFactorText = String.format("%s[%.2fx]", ChatFormatting.GRAY, currentTask.getSpeedFactor());
      String pauseText = isStopped ? String.format("%s[ПАУЗА]", ChatFormatting.RED) : "";
      e.reportCategory(
         1.0,
         String.format("Время: %s%s %s %s", ChatFormatting.YELLOW, formattedTime, speedFactorText, pauseText),
         String.format("Текущий день: %s%s", ChatFormatting.YELLOW, TimeController.get().getCurrentDay()),
         String.format("Воркер: %s%s", ChatFormatting.YELLOW, taskId),
         String.format("Приоритет: %s%d", ChatFormatting.YELLOW, taskPriority)
      );
   }
}
