package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.data.EntityController;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.entity.EntityState;
import com.storyteam.storytelling.script.performer.entity.behaviour.BehaviourType;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.util.ToStringBuilder;
import com.storyteam.storytelling.util.world.Location;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;

public class EnitiyJumpAction<T extends Mob> extends EntityPerformerAction<T> {
   private final Supplier<Location> position;
   private List<Location> positionsLeft;
   private final Vec3 speed;
   private boolean started;
   private boolean completed;
   private double distance;
   private long ticks = 0L;

   public EnitiyJumpAction(ScriptPerformer performer, Character<T> character, Vec3 speed, Supplier<Location> pose) {
      super(performer, character);
      this.position = pose;
      this.speed = speed;
      this.started = false;
   }

   @Override
   public void tick() {
      EntityController<T> reference = this.script.env.server.getEntityRepository().get(this.character);
      EntityState<T> state = this.getEntity();
      if (reference != null && reference.getEntity() != null && state.isValid()) {
         this.ticks++;
         if (!this.started) {
            Location position = this.position.get();
            if (position != null) {
               this.started = true;
               this.ticks = 0L;
               state.setPosition(position);
               Vec3 pos = ((Mob)reference.getEntity()).position();
               Vec3 move = position.subtract(pos);
               this.distance = move.length();
               Vec3 middle = position.subtract(pos).add(this.speed.x, -0.5, 0.0);
               ((Mob)reference.getEntity()).setDeltaMovement(middle.x, middle.y, middle.z);
               state.stopBehaviour(BehaviourType.MOVEMENT);
            }
         }

         Vec3 left = this.position.get().subtract(((Mob)reference.getEntity()).position());
         double distanceLeft = left.length();
         if (distanceLeft < this.distance / 2.0) {
            ((Mob)reference.getEntity()).setDeltaMovement(left.normalize());
            if (distanceLeft < 0.5) {
               ((Mob)reference.getEntity()).setDeltaMovement(0.0, 0.0, 0.0);
               Location target = this.position.get();
               ((Mob)reference.getEntity()).teleportTo(target.x, target.y, target.z);
               this.completed = true;
            }
         } else {
            Vec3 movement = ((Mob)reference.getEntity()).getDeltaMovement();
            movement = movement.subtract(left.normalize().multiply(this.speed));
            ((Mob)reference.getEntity()).setDeltaMovement(movement);
         }
      }
   }

   @Override
   public void reset() {
      this.started = false;
      this.completed = false;
   }

   @Override
   public boolean isDone() {
      return this.completed;
   }

   @Override
   public void fastForwardToStage() {
      EntityState<T> entity = this.getEntity();
      Location pose = this.position.get();
      if (pose != null) {
         entity.setPosition(pose);
      }

      entity.markForReposition();
   }

   @Override
   public void rewindToStage() {
      this.getEntity().setPosition(null);
   }

   @Override
   public HelpReportBuilder gatherHelpReport(HelpReportBuilder builder) {
      return builder.append("Прыжок персонажа [%s] (%s)".formatted(this.character.getName(), this.character.getReferenceId()));
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this).append("character", this.character).append("speed", this.speed).append("position", this.position.get()).build();
   }
}
