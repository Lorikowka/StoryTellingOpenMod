package com.storyteam.storytelling.script.performer.entity.behaviour.movement;

import com.storyteam.storytelling.script.story.ScriptEnvironment;
import com.storyteam.storytelling.util.data.EntityContext;
import com.storyteam.storytelling.util.world.Location;
import java.util.List;
import net.minecraft.world.entity.Mob;
import org.zeith.hammerlib.util.java.RoundRobinList;

public class EntityLoopPathBehaviour<T extends Mob> extends EntityPathBehaviour<T> {
   private final RoundRobinList<Location> positions;

   public EntityLoopPathBehaviour(double speed, List<Location> positions) {
      super(speed);
      this.positions = new RoundRobinList(positions);
   }

   @Override
   public void tickEntity(ScriptEnvironment env, EntityContext<T> context) {
      if (context.getRef().navigator.currentJob == null) {
         context.getRef().navigator.moveByPath((float)this.speed, (Location)this.positions.next());
      }
   }
}
