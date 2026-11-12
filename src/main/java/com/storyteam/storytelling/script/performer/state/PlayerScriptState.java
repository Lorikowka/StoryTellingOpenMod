package com.storyteam.storytelling.script.performer.state;

import com.storyteam.storytelling.content.entities.ChairEntity;
import com.storyteam.storytelling.init.EntitiesST;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.util.ScriptUtils;
import com.storyteam.storytelling.util.world.Location;
import net.minecraft.server.level.ServerPlayer;

public class PlayerScriptState extends State<ScriptPerformer> {
   public static final State.Type<ScriptPerformer, PlayerScriptState> TYPE = create(PlayerScriptState::new);
   public Location sitLocation;

   public PlayerScriptState(ScriptPerformer performer) {
      super(performer);
   }

   @Override
   public void tick() {
      ServerPlayer player = ScriptUtils.getPlayer();
      if (this.sitLocation != null && !player.isPassenger()) {
         ChairEntity chair = (ChairEntity)EntitiesST.CHAIR.create(player.level());
         chair.setPos(this.sitLocation.x, this.sitLocation.y, this.sitLocation.z);
         player.level().addFreshEntity(chair);
         player.startRiding(chair);
      }
   }
}
