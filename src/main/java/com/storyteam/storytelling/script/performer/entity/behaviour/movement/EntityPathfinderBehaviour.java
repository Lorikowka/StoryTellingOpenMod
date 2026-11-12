package com.storyteam.storytelling.script.performer.entity.behaviour.movement;

import com.storyteam.storytelling.script.story.ScriptEnvironment;
import com.storyteam.storytelling.script.story.npc.ScriptNpcEntity;
import com.storyteam.storytelling.util.data.EntityContext;
import com.storyteam.storytelling.util.world.Location;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.world.entity.Mob;

public class EntityPathfinderBehaviour<T extends Mob> extends EntityPathBehaviour<T> {
   protected final Supplier<List<Location>> positions;

   public EntityPathfinderBehaviour(Supplier<List<Location>> positions, double speed) {
      super(speed);
      this.positions = positions;
   }

   @Override
   public void onInterrupt(ScriptEnvironment env, EntityContext<T> context) {
      super.onInterrupt(env, context);
      setWalking(context, false);
   }

   @Override
   public boolean canStillUse(ScriptEnvironment env, EntityContext<T> context) {
      return context.getRef().navigator.currentJob != null;
   }

   @Override
   public void tickEntity(ScriptEnvironment env, EntityContext<T> context) {
      updateWalking(context);
   }

   @Override
   public void onStart(ScriptEnvironment env, EntityContext<T> context) {
      List<Location> positions = this.positions.get();
      if (positions.isEmpty()) {
         throw new IndexOutOfBoundsException(context.getCharacter().getReferenceId() + " tried to navigate by an empty path.");
      } else {
         context.getRef().navigator.moveByPath((float)this.speed, positions.toArray(new Location[0]));
         context.getState().setPosition(positions.get(positions.size() - 1), true);
         updateWalking(context);
      }
   }

   @Override
   public void onStop(ScriptEnvironment env, EntityContext<T> context) {
      super.onStop(env, context);
      setWalking(context, false);
   }

   @Override
   public void onContinue(ScriptEnvironment env, EntityContext<T> context) {
      super.onContinue(env, context);
      updateWalking(context);
   }

   private void updateWalking(EntityContext<T> context) {
      setWalking(context, context.getRef().navigator.currentJob != null && !context.getRef().navigator.paused);
   }

   private void setWalking(EntityContext<T> context, boolean walking) {
      if (context.getEntity() instanceof ScriptNpcEntity scriptNpc) {
         scriptNpc.walking(walking);
      }
   }
}
