package com.storyteam.storytelling.mixins._client.minecraft;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.mojang.blaze3d.systems.RenderSystem;
import com.storyteam.storytelling.events.base.RenderCrosshairEvent;
import com.storyteam.storytelling.events.base.RenderHotBarEvent;
import com.storyteam.storytelling.system.octospizdel.particle.ParticleSystem;
import com.storyteam.storytelling.util.UtilST;
import java.util.Collection;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.MobEffectTextureManager;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.client.extensions.common.IClientMobEffectExtensions;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Gui.class})
public class GuiMixin {
   @Shadow
   @Final
   protected Minecraft minecraft;
   @Shadow
   protected int screenWidth;

   @Inject(
      method = {"render"},
      at = {@At("RETURN")}
   )
   private void renderGuiParticles(GuiGraphics pGuiGraphics, float pPartialTick, CallbackInfo ci) {
      ParticleSystem.renderGuiParticles(pGuiGraphics, pPartialTick);
   }

   @Inject(
      method = {"renderHotbar"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void renderHotbar(float pPartialTick, GuiGraphics pGuiGraphics, CallbackInfo ci) {
      if (MinecraftForge.EVENT_BUS.post(new RenderHotBarEvent())) {
         ci.cancel();
      }
   }

   @Inject(
      method = {"renderPlayerHealth"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void renderHotbar(GuiGraphics pGuiGraphics, CallbackInfo ci) {
      if (MinecraftForge.EVENT_BUS.post(new RenderHotBarEvent())) {
         ci.cancel();
      }
   }

   @Inject(
      method = {"renderExperienceBar"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void renderHotbar(GuiGraphics pGuiGraphics, int pX, CallbackInfo ci) {
      if (MinecraftForge.EVENT_BUS.post(new RenderHotBarEvent())) {
         ci.cancel();
      }
   }

   @Inject(
      method = {"renderCrosshair"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void renderCrosshair(GuiGraphics pGuiGraphics, CallbackInfo ci) {
      if (MinecraftForge.EVENT_BUS.post(new RenderCrosshairEvent())) {
         ci.cancel();
      }
   }

   @Inject(
      method = {"renderEffects"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void renderEffects(GuiGraphics pGuiGraphics, CallbackInfo ci) {
      Collection<MobEffectInstance> collection = this.minecraft.player.getActiveEffects();
      if (!collection.isEmpty()) {
         if (this.minecraft.screen instanceof EffectRenderingInventoryScreen effectrenderinginventoryscreen && effectrenderinginventoryscreen.canSeeEffects()) {
            return;
         }

         RenderSystem.enableBlend();
         int j1 = 0;
         int k1 = 0;
         MobEffectTextureManager mobeffecttexturemanager = this.minecraft.getMobEffectTextures();
         List<Runnable> list = Lists.newArrayListWithExpectedSize(collection.size());

         for (MobEffectInstance mobeffectinstance : Ordering.natural().reverse().sortedCopy(collection)) {
            MobEffect mobeffect = mobeffectinstance.getEffect();
            IClientMobEffectExtensions renderer = IClientMobEffectExtensions.of(mobeffectinstance);
            if (renderer.isVisibleInGui(mobeffectinstance) && mobeffectinstance.showIcon()) {
               int i = this.screenWidth - UtilST.getMobEffectsOffset();
               int j = 1;
               if (this.minecraft.isDemo()) {
                  j += 15;
               }

               if (mobeffect.isBeneficial()) {
                  j1++;
                  i -= 25 * j1;
               } else {
                  k1++;
                  i -= 25 * k1;
                  j += 26;
               }

               float f = 1.0F;
               if (mobeffectinstance.isAmbient()) {
                  pGuiGraphics.blit(AbstractContainerScreen.INVENTORY_LOCATION, i, j, 165, 166, 24, 24);
               } else {
                  pGuiGraphics.blit(AbstractContainerScreen.INVENTORY_LOCATION, i, j, 141, 166, 24, 24);
                  if (mobeffectinstance.endsWithin(200)) {
                     int k = mobeffectinstance.getDuration();
                     int l = 10 - k / 20;
                     f = Mth.clamp((float)k / 10.0F / 5.0F * 0.5F, 0.0F, 0.5F)
                        + Mth.cos((float)k * (float) Math.PI / 5.0F) * Mth.clamp((float)l / 10.0F * 0.25F, 0.0F, 0.25F);
                  }
               }

               if (!renderer.renderGuiIcon(mobeffectinstance, (Gui)(Object)this, pGuiGraphics, i, j, 0.0F, f)) {
                  TextureAtlasSprite textureatlassprite = mobeffecttexturemanager.get(mobeffect);
                  int i1 = j;
                  float f1 = f;
                  int i_f = i;
                  list.add(() -> {
                     pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, f1);
                     pGuiGraphics.blit(i_f + 3, i1 + 3, 0, 18, 18, textureatlassprite);
                     pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
                  });
               }
            }
         }

         list.forEach(Runnable::run);
      }

      ci.cancel();
   }
}
