package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.util.ToStringBuilder;
import net.minecraft.world.entity.Entity;

public class EntitySetSpeedAction<T extends Entity> extends EntityPerformerAction<T> {
   private final double speed;

   public EntitySetSpeedAction(ScriptPerformer performer, Character<T> character, double speed) {
      super(performer, character);
      this.speed = speed;
   }

   @Override
   public void tick() {
      this.getEntity().getData().setMoveSpeed(this.speed);
   }

   @Override
   public void rewindToStage() {
      this.getEntity().getData().setMoveSpeed(this.character.getStandardMoveSpeed());
   }

   @Override
   public void fastForwardToStage() {
      this.getEntity().getData().setMoveSpeed(this.speed);
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this).append("character", this.character).append("speed", this.speed).build();
   }
}
