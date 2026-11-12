package com.storyteam.storytelling.mixins.minecraft;

import com.storyteam.storytelling.Storytelling;
import javax.annotation.Nullable;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({DebugPackets.class})
public class DebugPacketSenderMixin {
   @Inject(
      method = {"sendPathFindingPacket"},
      at = {@At("HEAD")}
   )
   private static void sendDebugPath(Level level_, Mob mob_, @Nullable Path path_, float maxDistanceToWaypoint_, CallbackInfo ci) {
      Storytelling.PROXY.renderDebugPath(mob_, path_, maxDistanceToWaypoint_);
   }
}
