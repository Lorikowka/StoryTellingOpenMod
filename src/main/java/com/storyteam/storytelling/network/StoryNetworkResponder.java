package com.storyteam.storytelling.network;

import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.network.packets.CrashPacket;
import com.storyteam.storytelling.network.packets.script.GetInstructionPacket;
import com.storyteam.storytelling.network.packets.script.GetScriptPacket;
import com.storyteam.storytelling.network.packets.script.GetScriptsPacket;
import com.storyteam.storytelling.network.packets.script.GotoPacket;
import com.storyteam.storytelling.network.packets.script.SetScriptPaused;
import com.storyteam.storytelling.network.packets.script.SkipScriptPacket;
import com.storyteam.storytelling.network.packets.script.StartScriptPacket;
import com.storyteam.storytelling.network.packets.script.StopScriptPacket;
import com.storyteam.storytelling.network.types.Script;
import com.storyteam.storytelling.script.ScriptInstance;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.action.PerformerAction;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;
import com.storyteam.storytelling.script.story.ScriptEnvironment;
import com.storyteam.storytelling.util.ScriptUtils;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.CrashReport;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

public class StoryNetworkResponder {
   public void getScripts(@NotNull GetScriptsPacket.Request packet) {
      if (IStoryServer.get() == null) {
         StoryServer.getInstance().sendPacket(new GetScriptsPacket.Response(packet.getRequestId(), new ArrayList<>()), 2);
      } else {
         ScriptEnvironment env = IStoryServer.get().getScriptEnv();
         Int2ObjectArrayMap<ScriptInstance> active = env.scriptInstances;
         List<Script> scripts = new ArrayList<>();
         env.constructors.forEach((id, constructor) -> scripts.add(new Script(id, active.containsKey(id), false, 0.0, 0, 1)));
         StoryServer.getInstance().sendPacket(new GetScriptsPacket.Response(packet.getRequestId(), scripts), 2);
      }
   }

   public void getScript(GetScriptPacket.Request packet) {
      if (IStoryServer.get() == null) {
         StoryServer.getInstance().sendPacket(new GetScriptPacket.Response(packet.getRequestId()), 2);
      } else {
         ScriptInstance script = (ScriptInstance)IStoryServer.get().getScriptEnv().scriptInstances.get(packet.getScript());
         if (script == null) {
            Script s = new Script(packet.getScript(), false, false, 0.0, 0, 0);
            StoryServer.getInstance().sendPacket(new GetScriptPacket.Response(packet.getRequestId(), s), 2);
         } else {
            StoryServer.getInstance().sendPacket(new GetScriptPacket.Response(packet.getRequestId(), Script.fromScriptInstance(script)), 2);
         }
      }
   }

   public void setPaused(SetScriptPaused.Request packet) {
      if (IStoryServer.get() != null) {
         ScriptInstance script = (ScriptInstance)IStoryServer.get().getScriptEnv().scriptInstances.get(packet.getId());
         if (script != null) {
            script.setPaused(packet.isState());
            StoryServer.getInstance().sendPacket(new SetScriptPaused.Response(packet.getRequestId(), packet.getId(), packet.isState()), 2);
         }
      }
   }

   public void startScript(StartScriptPacket packet) {
      if (IStoryServer.get() != null) {
         IStoryServer.get().getScriptEnv().start(packet.getScript());
      }
   }

   public void startScript(CrashPacket packet) {
      Exception t = new Exception("");
      t.setStackTrace(new StackTraceElement[0]);
      Minecraft.getInstance().delayCrash(new CrashReport("", t));
   }

   public void stopScript(StopScriptPacket packet) {
      if (IStoryServer.get() != null) {
         IStoryServer.get().getScriptEnv().stop(packet.getScript());
      }
   }

   public void skipScript(SkipScriptPacket packet) {
      if (IStoryServer.get() != null) {
         ScriptInstance script = IStoryServer.get().getScriptEnv().getScript(packet.getScript());
         ScriptPerformer mainPerformer = script.getMainPerformer();
         if (mainPerformer != null) {
            mainPerformer.jumpTo(mainPerformer.getActionIndex() + 1, false);
         }
      }
   }

   public void getInstruction(GetInstructionPacket.Request packet) {
      if (IStoryServer.get() == null) {
         StoryServer.getInstance().sendPacket(new GetInstructionPacket.Response(packet.getRequestId(), packet.getScript(), new ArrayList<>()), 2);
      } else {
         ScriptEnvironment environment = IStoryServer.get().getScriptEnv();
         if (!environment.constructors.containsKey(packet.getScript())) {
            StoryServer.getInstance().sendPacket(new GetInstructionPacket.Response(packet.getRequestId(), packet.getScript(), new ArrayList<>()), 2);
         } else {
            ScriptInstance inst = environment.createInstance(packet.getScript());
            ScriptPerformer main = inst.getMainPerformer();
            List<GetInstructionPacket.InstructionRow> list = new ArrayList<>();

            for (PerformerAction<?> action : main.getActions()) {
               HelpReportBuilder builder = HelpReportBuilder.builder();
               String a;
               if (packet.isDebug()) {
                  a = action.gatherHelpReport(builder).build();
               } else {
                  a = action.gatherInstruction(builder).build();
               }

               if (!a.isEmpty()) {
                  list.add(new GetInstructionPacket.InstructionRow(action.getId(), a));
               }
            }

            StoryServer.getInstance().sendPacket(new GetInstructionPacket.Response(packet.getRequestId(), packet.getScript(), list), 2);
         }
      }
   }

   public void gotoPacket(GotoPacket packet) {
      if (IStoryServer.get() != null) {
         if (IStoryServer.get() != null) {
            ScriptUtils.server().execute(() -> {
               IStoryServer.get().getScriptEnv().start(packet.getScript());
               ScriptInstance script = IStoryServer.get().getScriptEnv().getScript(packet.getScript());
               ScriptPerformer mainPerformer = script.getMainPerformer();
               if (mainPerformer != null) {
                  try {
                     mainPerformer.gotoLabel(packet.getLabel());
                  } catch (IllegalArgumentException var4) {
                     mainPerformer.gotoSavepoint(packet.getLabel());
                  }
               }
            });
         }
      }
   }
}
