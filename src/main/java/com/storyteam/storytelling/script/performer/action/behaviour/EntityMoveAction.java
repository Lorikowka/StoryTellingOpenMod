package com.storyteam.storytelling.script.performer.action.behaviour;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.entity.EntityState;
import com.storyteam.storytelling.script.performer.entity.behaviour.movement.EntityMovementBehaviour;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.util.world.Location;
import java.util.function.Supplier;
import net.minecraft.world.entity.Entity;

public class EntityMoveAction<T extends Entity> extends BehaviourAction<T, EntityMovementBehaviour<T>> {
   final double speed;
   final Supplier<Location> position;
   boolean rotate;

   public EntityMoveAction(ScriptPerformer performer, Character<T> character, double speed, Supplier<Location> position, boolean rotate) {
      super(performer, character);
      this.speed = speed;
      this.position = position;
      this.rotate = rotate;
   }

   @Override
   public void fastForwardToStage() {
      if (this.behavior != null) {
         this.optionalState().ifPresent(s -> s.stopBehaviour(this.behavior.getType()));
      }

      EntityState<T> entity = this.getEntity();
      Location pos = this.position.get();
      if (pos != null) {
         entity.setPosition(pos, this.rotate);
      }

      entity.markForReposition();
   }

   public EntityMovementBehaviour<T> createBehaviour() {
      return new EntityMovementBehaviour<>(this.speed, this.position, this.rotate);
   }

   @Override
   public HelpReportBuilder gatherHelpReport(HelpReportBuilder builder) {
      return builder.append("Толкание персонажа \"%s\" (%s).", this.character.getName(), this.character.getReferenceId());
   }
}
