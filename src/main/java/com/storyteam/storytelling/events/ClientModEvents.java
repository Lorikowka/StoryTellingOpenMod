package com.storyteam.storytelling.events;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.storyteam.storytelling.St;
import com.storyteam.storytelling.client.content.overlays.BlackoutBelowChatOverlay;
import com.storyteam.storytelling.client.content.overlays.BlackoutOverlay;
import com.storyteam.storytelling.client.content.overlays.BlinkOverlay;
import com.storyteam.storytelling.client.content.overlays.FlashbackOverlay;
import com.storyteam.storytelling.client.content.overlays.GameTipOverlay;
import com.storyteam.storytelling.client.content.overlays.ImageOverlay;
import com.storyteam.storytelling.client.content.overlays.MessageOverlay;
import com.storyteam.storytelling.client.content.overlays.TitleOverlay;
import com.storyteam.storytelling.system.currency.overlay.CurrencyOverlay;
import com.storyteam.storytelling.system.emotions.events.EmotionsOverlay;
import com.storyteam.storytelling.system.health.HealthOverlay;
import com.storyteam.storytelling.system.simulator.me_tab.word.client.WordOverlay;
import com.storyteam.storytelling.system.tribute.overlay.MedallionOverlay;
import java.io.IOException;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.zeith.hammerlib.client.flowgui.FlowguiShaderRegistry;
import org.zeith.hammerlib.util.mcf.Resources;

@EventBusSubscriber(
   bus = Bus.MOD,
   value = {Dist.CLIENT}
)
public class ClientModEvents {
   public static ShaderInstance POSITION_COLOR_TEX_NO_DISCARD = null;

   public static ShaderInstance noDiscardPositionColorTex() {
      return POSITION_COLOR_TEX_NO_DISCARD;
   }

   @SubscribeEvent
   public static void clientSetup(FMLClientSetupEvent e) {
      e.enqueueWork(() -> FlowguiShaderRegistry.register(St.id("translucent"), ClientModEvents::noDiscardPositionColorTex));
   }

   @SubscribeEvent
   public static void shaderRegistry(RegisterShadersEvent event) throws IOException {
      event.registerShader(
         new ShaderInstance(event.getResourceProvider(), St.id("position_color_tex"), DefaultVertexFormat.POSITION_COLOR_TEX),
         inst -> POSITION_COLOR_TEX_NO_DISCARD = inst
      );
   }

   @SubscribeEvent
   public static void registerOverlays(RegisterGuiOverlaysEvent event) {
      event.registerBelow(Resources.location("minecraft", "title_text"), "blink_overlay", new BlinkOverlay());
      event.registerAbove(Resources.location("minecraft", "title_text"), "blackout_overlay", new BlackoutOverlay());
      event.registerAbove(Resources.location("minecraft", "title_text"), "flashback_overlay", new FlashbackOverlay());
      event.registerBelow(Resources.location("minecraft", "item_name"), "game_tip_overlay", new GameTipOverlay());
      event.registerBelow(Resources.location("minecraft", "item_name"), "title_overlay", new TitleOverlay());
      event.registerBelow(Resources.location("minecraft", "item_name"), "message_overlay", new MessageOverlay());
      event.registerBelow(Resources.location("minecraft", "item_name"), "word_overlay", new WordOverlay());
      event.registerBelow(St.id("message_overlay"), "image_overlay", new ImageOverlay());
      event.registerBelow(St.id("message_overlay"), "blackout_overlay_below_chat", new BlackoutBelowChatOverlay());
      event.registerBelow(St.id("blackout_overlay_below_chat"), "currency_overlay", new CurrencyOverlay());
      event.registerBelow(St.id("blackout_overlay_below_chat"), "medallion_overlay", new MedallionOverlay());
      event.registerBelow(St.id("blackout_overlay_below_chat"), "emotions_overlay", new EmotionsOverlay());
      event.registerBelow(St.id("emotions_overlay"), "health_overlay", new HealthOverlay());
   }
}
