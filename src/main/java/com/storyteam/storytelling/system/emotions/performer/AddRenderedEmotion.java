package com.storyteam.storytelling.system.emotions.performer;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.action.ScriptPerformerAction;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;
import com.storyteam.storytelling.system.emotions.BubbleType;
import com.storyteam.storytelling.system.emotions.Emotion;
import com.storyteam.storytelling.system.emotions.network.S2CAddCharacterEmotion;
import net.minecraft.world.phys.Vec3;
import org.zeith.hammerlib.net.Network;

public class AddRenderedEmotion extends ScriptPerformerAction {
   private final String refId;
   private final Emotion emotion;
   private final BubbleType type;
   private Vec3 offset = Vec3.ZERO;
   private boolean isDone = false;

   public AddRenderedEmotion(ScriptPerformer performer, String refId, Emotion emotion, BubbleType type) {
      super(performer);
      this.refId = refId;
      this.emotion = emotion;
      this.type = type;
   }

   public AddRenderedEmotion(ScriptPerformer performer, String refId, Emotion emotion, BubbleType type, Vec3 offset) {
      this(performer, refId, emotion, type);
      this.offset = offset;
   }

   @Override
   public void tick() {
      if (!this.isDone) {
         Network.sendToAll(new S2CAddCharacterEmotion(this.refId, this.emotion, this.offset, this.type));
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
      Network.sendToAll(new S2CAddCharacterEmotion(this.refId, this.emotion, this.offset, this.type));
   }

   @Override
   public void globalFastForward() {
      Network.sendToAll(new S2CAddCharacterEmotion(this.refId, this.emotion, this.offset, this.type));
   }

   @Override
   public HelpReportBuilder gatherHelpReport(HelpReportBuilder builder) {
      return builder.append("Добавление эмоции в рендер персонажа.");
   }
}
