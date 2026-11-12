package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.entity.look.ILookTarget;
import com.storyteam.storytelling.script.performer.entity.look.targets.CharacterTarget;
import com.storyteam.storytelling.script.performer.entity.look.targets.PlayerTarget;
import com.storyteam.storytelling.script.performer.entity.look.targets.PositionTarget;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.util.PositionParser;
import com.storyteam.storytelling.util.ToStringBuilder;
import com.storyteam.storytelling.util.world.Location;
import net.minecraft.world.entity.Entity;

public class EntityLookAtPosAction<T extends Entity> extends EntityPerformerAction<T> {
   private final ILookTarget<T> target;
   private final Location loc;
   private final boolean isQuick;
   private final boolean waitForEnd;

   public EntityLookAtPosAction(ScriptPerformer performer, Character<T> character, String target, boolean isQuick, boolean waitForEnd) {
      super(performer, character);
      Location loc = PositionParser.parseLocationWithRotation(target);
      this.loc = loc;
      if (loc != null) {
         this.target = new PositionTarget<>(loc, false);
      } else if (target.equals("player")) {
         this.target = new PlayerTarget<>();
      } else {
         this.target = new CharacterTarget<>(target);
      }

      this.isQuick = isQuick;
      this.waitForEnd = waitForEnd;
   }

   @Override
   public void tick() {
      this.fastForwardToStage();
   }

   @Override
   public void fastForwardToStage() {
      this.getEntity().getData().setLookTarget(this.target);
   }

   @Override
   public boolean isDone() {
      return !this.waitForEnd || this.getEntity().getData().getLookController().hasEndedRotation();
   }

   @Override
   public void rewindToStage() {
      this.getEntity().getData().setLookTarget(null);
   }

   @Override
   public HelpReportBuilder gatherHelpReport(HelpReportBuilder builder) {
      if (this.loc != null) {
         builder.append("Поворот персонажа [%s] на позицию X:%d Y:%d Z:%d.", this.character.getName(), (int)this.loc.x, (int)this.loc.y, (int)this.loc.z);
      }

      return builder;
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this).append("character", this.character).append("target", this.target).append("isQuick", this.isQuick).build();
   }
}
