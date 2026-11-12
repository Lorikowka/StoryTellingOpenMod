package com.storyteam.storytelling.mixins._client.minecraft;

import com.storyteam.storytelling.api.itf.IStoryEntity;
import com.storyteam.storytelling.network.StoryServer;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({DebugScreenOverlay.class})
public abstract class DebugScreenOverlayMixin {
   @Shadow
   @Final
   private Minecraft minecraft;

   @Inject(
      method = {"getGameInformation"},
      at = {@At("RETURN")},
      cancellable = true
   )
   public void run(CallbackInfoReturnable<List<String>> cir) {
      List<String> list = (List<String>)cir.getReturnValue();
      list.add(1, "Packet Queue: " + StoryServer.getInstance().getPacketQueueSize());
      cir.setReturnValue(list);
   }

   @ModifyVariable(
      method = {"getSystemInformation"},
      slice = @Slice(
         from = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/Minecraft;showOnlyReducedInfo()Z"
         ),
         to = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/multiplayer/ClientLevel;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"
         )
      ),
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/phys/HitResult;getType()Lnet/minecraft/world/phys/HitResult$Type;",
         shift = Shift.AFTER
      ),
      name = {"list"}
   )
   public List<String> run(List<String> value) {
      Entity entity = this.minecraft.crosshairPickEntity;
      if (entity != null) {
         IStoryEntity se = IStoryEntity.of(entity);
         if (se.isStorytellingEntity()) {
            value.add("");
            value.add("" + ChatFormatting.GOLD + ChatFormatting.UNDERLINE + "Storytelling Character");
            value.add("ID: " + se.getStoryName());
            value.add("Vulnerable: " + colorizeBool(se.isSEVulnerable()));
            String tx = se.getCustomTexture();
            if (tx != null && !tx.isBlank()) {
               value.add("Texture: " + se.getCustomTexture());
            }
         }
      }

      return value;
   }

   @Unique
   private static String colorizeBool(boolean v) {
      return (v ? ChatFormatting.GREEN : ChatFormatting.RED).toString() + v;
   }
}
