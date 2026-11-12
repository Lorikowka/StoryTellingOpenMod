package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.util.ScriptUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

/** Waits for the story player to reach a position in a specific dimension. */
public final class WaitPlayerPositionAction extends ScriptPerformerAction {
   private final ResourceKey<Level> dimension;
   private final double x, y, z, radiusSquared;
   private final boolean ignoreY;

   public WaitPlayerPositionAction(ScriptPerformer performer, String dimension, double x, double y, double z, double radius, boolean ignoreY) {
      super(performer);
      this.dimension = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(dimension));
      this.x = x;
      this.y = y;
      this.z = z;
      this.radiusSquared = radius * radius;
      this.ignoreY = ignoreY;
   }

   @Override
   public boolean isDone() {
      ServerPlayer player = ScriptUtils.getPlayer();
      if (player == null || !player.level().dimension().equals(dimension)) {
         return false;
      }
      double dx = player.getX() - x;
      double dy = ignoreY ? 0.0 : player.getY() - y;
      double dz = player.getZ() - z;
      return dx * dx + dy * dy + dz * dz <= radiusSquared;
   }
}
