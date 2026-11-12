package com.storyteam.storytelling.script.performer.entity.look.targets;

import com.storyteam.storytelling.script.performer.entity.look.ILookTarget;
import com.storyteam.storytelling.util.ScriptUtils;
import com.storyteam.storytelling.util.data.EntityContext;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class PlayerTarget<T extends Entity> implements ILookTarget<T> {
   @Override
   public Vec3 getTarget(EntityContext<T> context) {
      ServerPlayer player = ScriptUtils.getPlayer();
      return player != null ? player.getEyePosition() : null;
   }
}
