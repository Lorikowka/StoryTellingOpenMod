package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.script.performer.Performer;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;
import com.storyteam.storytelling.script.performer.state.StateHolder;
import com.storyteam.storytelling.util.ToStringBuilder;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import org.zeith.hammerlib.util.java.Cast;

public class ParallelPerformersAction<P extends Performer<P>> extends PerformerAction<P> {
   private final Performer<P>[] performers;
   private final ParallelPerformersAction.CompletionCondition completion;
   private boolean started;

   public ParallelPerformersAction(P performer, Performer<P>[] performers, ParallelPerformersAction.CompletionCondition completionCondition) {
      super(performer);
      this.performers = (Performer<P>[])performers.clone();
      this.completion = completionCondition;
   }

   @Override
   public void globalFastForward() {
      Arrays.stream(this.performers).map(Performer::getActions).flatMap(Collection::stream).forEach(PerformerAction::globalFastForward);
   }

   @Override
   public void tick() {
      if (!this.started) {
         this.started = true;

         for (Performer<P> scriptPerformer : this.performers) {
            scriptPerformer.inheritFrom(this.performer);
            scriptPerformer.onStart();
         }
      }

      for (Performer<?> scriptPerformer : this.performers) {
         scriptPerformer.tick();
      }
   }

   @Override
   public void fastForwardToStage() {
      if (!this.started) {
         for (Performer<P> scriptPerformer : this.performers) {
            scriptPerformer.inheritFrom(this.performer);
            scriptPerformer.onStart();
         }
      }

      if (this.completion == ParallelPerformersAction.CompletionCondition.ALL_COMPLETED) {
         for (Performer<?> scriptPerformer : this.performers) {
            scriptPerformer.gotoSavepoint("final");
         }
      }

      this.started = true;
   }

   @Override
   public void rewindToStage() {
      this.started = false;

      for (Performer<?> scriptPerformer : this.performers) {
         scriptPerformer.gotoSavepoint("start");
      }
   }

   @Override
   public void reset() {
      this.started = false;
   }

   @Override
   public void onInit() {
      super.onInit();
      this.started = false;
   }

   @Override
   public void onScriptInstanceCreated() {
      int n = 1;

      for (Performer<?> innerPerformer : this.performers) {
         innerPerformer.setName(this.performer.getName() + "_" + this.performer.getActionIndex(this) + "_inner" + n++);
         ((Performer)innerPerformer).setStateRepository((StateHolder)this.performer.getStateRepository());
         innerPerformer.onScriptInstanceCreated();
      }
   }

   @Override
   public boolean isDone() {
      for (Performer<?> scriptPerformer : this.performers) {
         if (scriptPerformer.isCompleted()) {
            if (this.completion == ParallelPerformersAction.CompletionCondition.ANY_COMPLETED) {
               return true;
            }
         } else if (this.completion == ParallelPerformersAction.CompletionCondition.ALL_COMPLETED) {
            return false;
         }
      }

      if (this.completion == ParallelPerformersAction.CompletionCondition.ANY_COMPLETED) {
         return false;
      } else if (this.completion == ParallelPerformersAction.CompletionCondition.ALL_COMPLETED) {
         return true;
      } else {
         throw new IllegalStateException("Unhandled completion condition: " + this.completion);
      }
   }

   @Override
   public <T> void handleEvent(EventType<T> eventType, T event) {
      for (Performer<?> scriptPerformer : this.performers) {
         scriptPerformer.handleEvent(eventType, event);
      }
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this)
         .append(
            "performers",
            Arrays.toString(
               Arrays.stream(this.performers)
                  .map(p -> p.getActions().isEmpty() ? "[Пустой перформер]" : (p.isCompleted() ? "[Завершен]" : p.getActions().get(p.getActionIndex())))
                  .toArray()
            )
         )
         .append("completion", this.completion)
         .build();
   }

   @Override
   public HelpReportBuilder gatherHelpReport(HelpReportBuilder builder) {
      if (this.performers.length == 1 && this.performers[0] instanceof ScriptPerformer) {
         builder.append(this.performers[0].gatherHelpReport());
      } else if (this.performers.length > 1) {
         builder.append(
            String.format(
               "Выполнение %s из: %s",
               this.completion == ParallelPerformersAction.CompletionCondition.ALL_COMPLETED ? "всех" : "одного",
               Arrays.stream(this.performers)
                  .flatMap(p -> Cast.optionally(p, ScriptPerformer.class).stream())
                  .map(Performer::gatherHelpReport)
                  .collect(Collectors.toList())
            )
         );
      }

      return builder;
   }

   public static enum CompletionCondition {
      ALL_COMPLETED,
      ANY_COMPLETED;
   }
}
