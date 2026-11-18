package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.util.ScriptUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

/** Waits until the story player's block position enters an inclusive cuboid. */
public final class WaitPlayerAreaAction extends ScriptPerformerAction {
   private final ResourceKey<Level> dimension;
   private final BlockArea area;

   public WaitPlayerAreaAction(ScriptPerformer performer, String dimension, BlockArea area) {
      super(performer);
      this.dimension = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(dimension));
      this.area = area;
   }

   @Override
   public boolean isDone() {
      ServerPlayer player = ScriptUtils.getPlayer();
      if (player == null || !player.level().dimension().equals(dimension)) return false;
      BlockPos position = player.blockPosition();
      return area.contains(position.getX(), position.getY(), position.getZ());
   }
}
