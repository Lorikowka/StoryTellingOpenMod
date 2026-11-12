package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.entity.EntityState;
import com.storyteam.storytelling.script.performer.entity.behaviour.movement.EntityLoopPathBehaviour;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.util.ToStringBuilder;
import com.storyteam.storytelling.util.world.Location;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.world.entity.Mob;

public class EntityLoopPathAction<T extends Mob> extends EntityPerformerAction<T> {
   private final double speed;
   private final Supplier<List<Location>> positions;
   private EntityLoopPathBehaviour<T> pathData;

   public EntityLoopPathAction(ScriptPerformer performer, Character<T> character, double speed, Supplier<List<Location>> positions) {
      super(performer, character);
      this.speed = speed;
      this.positions = positions;
   }

   @Override
   public void tick() {
      EntityState<T> entity = this.getEntity();
      if (this.pathData == null) {
         this.pathData = new EntityLoopPathBehaviour<>(this.speed, this.positions.get());
      }

      entity.addBehaviour(this.pathData);
   }

   @Override
   public void reset() {
      this.pathData = null;
   }

   @Override
   public void fastForwardToStage() {
      EntityState<T> entity = this.getEntity();
      entity.addBehaviour(this.pathData = new EntityLoopPathBehaviour<>(this.speed, this.positions.get()));
   }

   @Override
   public void rewindToStage() {
      this.pathData = null;
      this.getEntity().setPosition(null);
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this)
         .append("character", this.character)
         .append("speed", this.speed)
         .append("positions", Arrays.toString(this.positions.get().toArray(new Location[0])))
         .build();
   }
}
