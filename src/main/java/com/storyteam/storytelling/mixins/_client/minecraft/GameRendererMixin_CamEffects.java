package com.storyteam.storytelling.mixins._client.minecraft;

import com.mojang.blaze3d.vertex.PoseStack;
import com.storyteam.storytelling.init.EffectsST;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({GameRenderer.class})
public class GameRendererMixin_CamEffects {
   @Inject(
      method = {"renderLevel"},
      at = {@At("HEAD")}
   )
   public void renderLevel(float p_109090_, long p_109091_, PoseStack matrices, CallbackInfo ci) {
      Player player = Minecraft.getInstance().player;
      if (player != null && player.hasEffect(EffectsST.CAMERA_SHAKE)) {
         float spread = (float)player.getEffect(EffectsST.CAMERA_SHAKE).getAmplifier() * 0.003F;
         float rx = (float)(Math.sin((double)Minecraft.getInstance().level.getGameTime() * 0.1) * (double)spread);
         float ry = (float)(Math.cos((double)Minecraft.getInstance().level.getGameTime() * 0.1) * (double)spread);
         matrices.translate(rx, ry, 0.0F);
      }

      if (player != null && player.hasEffect(EffectsST.EARTHQUAKE)) {
         Random random = new Random(Minecraft.getInstance().level.getGameTime() * 1233L);
         float spread = (float)player.getEffect(EffectsST.EARTHQUAKE).getAmplifier() * 0.003F;
         float rx = random.nextFloat() * spread * 2.0F - spread;
         float ry = random.nextFloat() * spread * 2.0F - spread;
         matrices.translate(rx, ry, 0.0F);
      }
   }
}
