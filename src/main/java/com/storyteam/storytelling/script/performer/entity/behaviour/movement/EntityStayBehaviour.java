package com.storyteam.storytelling.script.performer.entity.behaviour.movement;

import com.storyteam.storytelling.script.performer.entity.behaviour.utils.ISightBehaviour;
import com.storyteam.storytelling.script.story.ScriptEnvironment;
import com.storyteam.storytelling.util.data.EntityContext;
import com.storyteam.storytelling.util.world.Location;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import org.jetbrains.annotations.NotNull;

public class EntityStayBehaviour<T extends Mob> extends EntityPathBehaviour<T> implements ISightBehaviour {
   @NotNull
   private final Location pos;

   public EntityStayBehaviour(double speed, @NotNull Location pos) {
      super(speed);
      this.pos = pos;
   }

   @Override
   public void tickEntity(ScriptEnvironment env, EntityContext<T> context) {
      if (!this.pos.closerThan(((Mob)context.getEntity()).position(), 1.0)) {
         PathNavigation navigator = ((Mob)context.getEntity()).getNavigation();
         navigator.moveTo(navigator.createPath(this.pos.x, this.pos.y, this.pos.z, 0), this.speed);
      }
   }
}
