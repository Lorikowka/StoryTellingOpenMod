package com.storyteam.storytelling.script.performer.entity.behaviour.movement;

import com.storyteam.storytelling.content.entities.ChairEntity;
import com.storyteam.storytelling.init.EntitiesST;
import com.storyteam.storytelling.script.performer.entity.behaviour.BehaviourType;
import com.storyteam.storytelling.script.performer.entity.behaviour.IStoryBehaviour;
import com.storyteam.storytelling.script.story.ScriptEnvironment;
import com.storyteam.storytelling.util.data.EntityContext;
import com.storyteam.storytelling.util.world.Location;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class EntitySitBehaviour<T extends Entity> implements IStoryBehaviour<T>, ISpeedyBehavior<T> {
   @Override
   public void tickEntity(ScriptEnvironment env, EntityContext<T> context) {
      if (!context.getEntity().isPassenger() && context.getEntity().isAddedToWorld() && context.getEntity().isAlive()) {
         ServerLevel world = (ServerLevel)context.getEntity().level();
         Location pos = new Location(context.getEntity());
         ChairEntity chair = (ChairEntity)EntitiesST.CHAIR.create(world);
         chair.setPos(pos.x, pos.y, pos.z);
         world.addFreshEntity(chair);
         context.getEntity().startRiding(chair, true);
      }
   }

   @Override
   public void onStart(ScriptEnvironment env, EntityContext<T> context) {
      if (!context.getEntity().isPassenger() && context.getEntity().isAddedToWorld() && context.getEntity().isAlive()) {
         ServerLevel world = (ServerLevel)context.getEntity().level();
         Location pos = new Location(context.getEntity());
         ChairEntity chair = (ChairEntity)EntitiesST.CHAIR.create(world);
         chair.setPos(pos.x, pos.y, pos.z);
         world.addFreshEntity(chair);
         context.getEntity().startRiding(chair, true);
      }
   }

   @Override
   public void onStop(ScriptEnvironment env, EntityContext<T> context) {
      context.getRef().getEntity().removeVehicle();
   }

   @Override
   public boolean canStillUse(ScriptEnvironment env, EntityContext<T> context) {
      return true;
   }

   @NotNull
   @Override
   public BehaviourType getType() {
      return BehaviourType.MOVEMENT;
   }

   @Override
   public double getSpeed() {
      return -1.0;
   }
}
