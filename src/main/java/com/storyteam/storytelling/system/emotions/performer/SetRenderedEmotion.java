package com.storyteam.storytelling.system.emotions.performer;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.action.ScriptPerformerAction;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;
import com.storyteam.storytelling.system.emotions.BubbleType;
import com.storyteam.storytelling.system.emotions.Emotion;
import com.storyteam.storytelling.system.emotions.network.S2CSetCharacterEmotion;
import net.minecraft.world.phys.Vec3;
import org.zeith.hammerlib.net.Network;

public class SetRenderedEmotion extends ScriptPerformerAction {
   private final String refId;
   private final Emotion[] emotion;
   private final BubbleType type;
   private Vec3 offset = Vec3.ZERO;
   private boolean isDone = false;

   public SetRenderedEmotion(ScriptPerformer performer, String refId, Emotion[] emotion, BubbleType type) {
      super(performer);
      this.refId = refId;
      this.emotion = emotion;
      this.type = type;
   }

   public SetRenderedEmotion(ScriptPerformer performer, String refId, Emotion[] emotion, BubbleType type, Vec3 offset) {
      this(performer, refId, emotion, type);
      this.offset = offset;
   }

   @Override
   public void tick() {
      if (!this.isDone) {
         Network.sendToAll(new S2CSetCharacterEmotion(this.refId, this.emotion, this.type, this.offset));
         this.isDone = true;
      }
   }

   @Override
   public void reset() {
      super.reset();
      this.isDone = false;
   }

   @Override
   public boolean isDone() {
      return this.isDone;
   }

   @Override
   public void fastForwardToStage() {
      Network.sendToAll(new S2CSetCharacterEmotion(this.refId, this.emotion, this.type, this.offset));
   }

   @Override
   public void globalFastForward() {
      Network.sendToAll(new S2CSetCharacterEmotion(this.refId, this.emotion, this.type, this.offset));
   }

   @Override
   public HelpReportBuilder gatherHelpReport(HelpReportBuilder builder) {
      return builder.append("Установка эмоции персонажа.");
   }
}
