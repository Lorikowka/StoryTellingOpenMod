package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.logging.LoggingController;
import com.storyteam.storytelling.network.utils.WebMessageHandler;
import com.storyteam.storytelling.script.performer.Performer;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;
import java.util.HashMap;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

public class SavePointAction<P extends Performer<P>> extends LabelAction<P> {
   public SavePointAction(P performer, @NotNull String name) {
      super(performer, name, true);
   }

   @Override
   public void tick() {
      if (this.performer instanceof ScriptPerformer sp) {
         HelpReportBuilder builder = HelpReportBuilder.builder();
         String latestMessage = "Не обнаружено";

         for (int i = sp.getActionIndex(); i > 0; i--) {
            PerformerAction<?> action = sp.getActions().get(i);
            if (action instanceof SpeechMessageAction messageAction) {
               latestMessage = messageAction.gatherHelpReport(HelpReportBuilder.builder()).build();
               break;
            }
         }

         builder.append(
            "Достигнута метка сохранения <button onclick=\"useNuxtApp().$utils.openSaveStateModal('%s')\" style=\"color: wheat\">%s</button>. Последнее событие перед меткой: %s ",
            this.name,
            this.name,
            latestMessage,
            this.getId()
         );
         WebMessageHandler.sendInstructionLine(this.performer.getName() + "_" + this.performer.getActionIndex(), sp.script.getId(), builder.build(), false);
      }
   }

   @Override
   public void fastForwardToStage() {
      if (this.performer instanceof ScriptPerformer sp) {
         HelpReportBuilder builder = HelpReportBuilder.builder();
         String latestMessage = "Не обнаружено";

         for (int i = sp.getActionIndex(); i > 0; i--) {
            PerformerAction<?> action = sp.getActions().get(i);
            if (action instanceof SpeechMessageAction messageAction) {
               latestMessage = messageAction.gatherHelpReport(HelpReportBuilder.builder()).build();
               break;
            }
         }

         builder.append(
            "Достигнута метка сохранения <button onclick=\"useNuxtApp().$utils.openSaveStateModal('%s')\" style=\"color: wheat\">%s</button>. Последнее событие перед меткой: %s",
            this.name,
            this.name,
            latestMessage,
            this.getId()
         );
         WebMessageHandler.sendInstructionLine(this.performer.getName() + "_" + this.performer.getActionIndex(), sp.script.getId(), builder.build(), false);
         Storytelling.LOG.debug("Fast forwarded through stage " + this.name + " within " + ((ScriptPerformer)this.performer).script);
      }
   }

   @Override
   public void rewindToStage() {
      if (this.performer instanceof ScriptPerformer) {
         Storytelling.LOG.debug("Rewound through stage " + this.name + " within " + ((ScriptPerformer)this.performer).script);
      }
   }

   @Override
   public HelpReportBuilder gatherHelpReport(HelpReportBuilder builder) {
      String latestMessage = "Не обнаружено";
      HashMap<Integer, String> entity = new HashMap<>();

      for (int i = 0; i < this.performer.getActionIndex(this); i++) {
         PerformerAction<?> action = this.performer.getActions().get(i);
         if (action instanceof SpeechSetEntityNameAction a) {
            entity.put(a.getEntity(), a.getName());
         }
      }

      for (int ix = this.performer.getActionIndex(this); ix > 0; ix--) {
         PerformerAction<?> action = this.performer.getActions().get(ix);
         if (action instanceof SpeechMessageAction messageAction) {
            String name = "null";
            if (action instanceof ThinkMessageAction) {
               name = "Lololowka (мысли)";
            } else if (action instanceof SpeechMessageAction) {
               name = Objects.toString(entity.get(messageAction.getEntity()));
            }

            latestMessage = messageAction.gatherHelpReport(HelpReportBuilder.builder()).build().replace("null", name);
            break;
         }
      }

      builder.append(
         "Достигнута метка сохранения <button onclick=\"useNuxtApp().$utils.openSaveStateModal('%s')\" style=\"color: wheat\">%s</button>. Последнее событие перед меткой: %s",
         this.name,
         this.name,
         latestMessage
      );
      return builder;
   }

   @Override
   public HelpReportBuilder gatherInstruction(HelpReportBuilder builder) {
      String latestMessage = "Не обнаружено";
      HashMap<Integer, String> entity = new HashMap<>();

      for (int i = 0; i < this.performer.getActionIndex(this); i++) {
         PerformerAction<?> action = this.performer.getActions().get(i);
         if (action instanceof SpeechSetEntityNameAction a) {
            entity.put(a.getEntity(), a.getName());
         }
      }

      for (int ix = this.performer.getActionIndex(this); ix > 0; ix--) {
         PerformerAction<?> action = this.performer.getActions().get(ix);
         if (action instanceof SpeechMessageAction messageAction) {
            latestMessage = messageAction.gatherHelpReport(HelpReportBuilder.builder()).build().replace("null", entity.get(messageAction.getEntity()));
            break;
         }
      }

      builder.append(
         "Достигнута метка сохранения <button onclick=\"useNuxtApp().$utils.openSaveStateModal('%s')\" style=\"color: wheat\">%s</button>. Последнее событие перед меткой: %s",
         this.name,
         this.name,
         latestMessage
      );
      return builder;
   }

   @Override
   public boolean isDone() {
      if (this.performer instanceof ScriptPerformer sp) {
         LoggingController.captureSaveState(sp.script, this.name, this.performer.getName());
      }

      this.performer.onStagePass(this.name);
      return true;
   }

   @Override
   public void reset() {
      super.reset();
      if (this.performer instanceof ScriptPerformer sp) {
         WebMessageHandler.removeInstructionLine(this.performer.getName() + "_" + this.performer.getActionIndex(), sp.script.getId());
      }
   }
}
