package com.storyteam.storytelling.script.performer.action.behaviour;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.entity.EntityState;
import com.storyteam.storytelling.script.performer.entity.behaviour.movement.EntityPathfinderBehaviour;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.script.story.npc.ScriptNpcEntity;
import com.storyteam.storytelling.script.story.npc.ScriptNpcs;
import com.storyteam.storytelling.util.ToStringBuilder;
import com.storyteam.storytelling.util.world.Location;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.world.entity.Mob;

public class EntityPathfinderAction<T extends Mob> extends BehaviourAction<T, EntityPathfinderBehaviour<T>> {
   Supplier<List<Location>> positions;
   protected final double speed;
   boolean parallel;
   private int elapsedTicks;

   public EntityPathfinderAction(ScriptPerformer performer, Character<T> character, double speed, Supplier<List<Location>> poses) {
      super(performer, character);
      this.speed = speed;
      this.positions = poses;
   }

   public EntityPathfinderAction(ScriptPerformer performer, Character<T> character, double speed, Supplier<List<Location>> poses, boolean parallel) {
      super(performer, character);
      this.speed = speed;
      this.positions = poses;
      this.parallel = parallel;
   }

   @Override
   public boolean isDone() {
      if (this.parallel) return true;
      if (this.completed && this.character.getType() == ScriptNpcs.SCRIPT_NPC.get()) {
         if (!(this.character.repositoryEntity() instanceof ScriptNpcEntity npc)) {
            throw new IllegalStateException("NPC route ended while NPC is unloaded: " + this.character.getReferenceId());
         }
         List<Location> targets = this.positions.get();
         if (targets != null && !targets.isEmpty()) {
            Location target = targets.get(targets.size() - 1);
            double dx = npc.getX() - target.x;
            double dz = npc.getZ() - target.z;
            double dy = npc.getY() - target.y;
            if (dx * dx + dz * dz > 4.0 || Math.abs(dy) > 2.0) {
               throw new IllegalStateException("NPC route ended before reaching destination: " + this.character.getReferenceId());
            }
         }
      }
      return super.isDone();
   }

   @Override
   public void tick() {
      super.tick();
      if (!this.completed && ++elapsedTicks > 2400) {
         throw new IllegalStateException("NPC route timed out after 120 seconds: " + this.character.getReferenceId());
      }
   }

   @Override
   public void reset() {
      super.reset();
      elapsedTicks = 0;
   }

   @Override
   public void fastForwardToStage() {
      if (this.behavior != null) {
         this.optionalState().ifPresent(s -> s.stopBehaviour(this.behavior.getType()));
      }

      EntityState<T> entity = this.getEntity();
      List<Location> poses = this.positions.get();
      if (poses != null && !poses.isEmpty()) {
         entity.setPosition(poses.get(poses.size() - 1), true);
      }

      entity.markForReposition();
   }

   public EntityPathfinderBehaviour<T> createBehaviour() {
      return new EntityPathfinderBehaviour<>(this.positions, this.speed);
   }

   @Override
   public void rewindToStage() {
      super.rewindToStage();
      this.getEntity().setPosition(null);
   }

   @Override
   public HelpReportBuilder gatherHelpReport(HelpReportBuilder builder) {
      return builder.append("Путь персонажа [%s] (%s).".formatted(this.character.getName(), this.character.getReferenceId()));
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this).append("character", this.character).append("speed", this.speed).build();
   }
}
