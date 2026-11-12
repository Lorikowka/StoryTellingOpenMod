package com.storyteam.storytelling.events;

import com.mojang.datafixers.util.Either;
import com.storyteam.storytelling.api.itf.IDisableHealthStats;
import com.storyteam.storytelling.client.content.overlays.BlackoutBelowChatOverlay;
import com.storyteam.storytelling.client.content.overlays.BlackoutOverlay;
import com.storyteam.storytelling.client.content.overlays.BlinkOverlay;
import com.storyteam.storytelling.content.entities.HumanoidEntity;
import com.storyteam.storytelling.events.base.RenderCrosshairEvent;
import com.storyteam.storytelling.events.base.RenderHotBarEvent;
import com.storyteam.storytelling.events.base.RenderMiniMapEvent;
import com.storyteam.storytelling.events.base.RenderTOPEvent;
import com.storyteam.storytelling.events.base.RenderToroHealthEvent;
import com.storyteam.storytelling.net.RightClickEmptyPacket;
import com.storyteam.storytelling.system._core.st_cutscene.ClientsideCutsceneHandler;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderBlockScreenEffectEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderHighlightEvent.Block;
import net.minecraftforge.client.event.RenderPlayerEvent.Pre;
import net.minecraftforge.client.event.RenderTooltipEvent.GatherComponents;
import net.minecraftforge.client.event.ViewportEvent.ComputeCameraAngles;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickEmpty;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.joml.Matrix4d;
import org.joml.Vector3d;
import org.zeith.hammerlib.net.Network;

@EventBusSubscriber(
   modid = "storytelling",
   bus = Bus.FORGE,
   value = {Dist.CLIENT}
)
public class ClientEvents {
   private static double yawO;
   private static double pitchO;
   public static boolean HIDEGUI = false;

   @SubscribeEvent
   public static void setCameraAnglesWhileSpectatingHumanoid(ComputeCameraAngles event) {
      if (Minecraft.getInstance().cameraEntity instanceof HumanoidEntity entity) {
         Optional<Matrix4d> m = entity.getTransforms("Head", false);
         if (m.isPresent()) {
            Matrix4d mat = m.get();
            double hangle = computeHorizontalAngle(mat);
            hangle = Mth.lerp((double)Minecraft.getInstance().getDeltaFrameTime(), yawO, hangle);
            yawO = hangle;
            event.setYaw((float)hangle);
            double vangle = computeVerticalAngle(mat);
            double pitch = Mth.lerp((double)Minecraft.getInstance().getDeltaFrameTime(), pitchO, (double)(event.getPitch() + (float)vangle));
            pitchO = (double)(event.getPitch() + (float)vangle);
            event.setPitch((float)pitch);
         }
      }
   }

   public static double computeHorizontalAngle(Matrix4d transforms) {
      Vector3d zero = transforms.transformPosition(new Vector3d(0.0, 0.0, 0.0));
      Vector3d forward = transforms.transformPosition(new Vector3d(0.0, 0.0, -10.0)).sub(zero);
      Vector3d n = new Vector3d(0.0, 0.0, -1.0);
      double vect = forward.x * n.x + forward.z * n.z;
      double lc = Math.sqrt(forward.x * forward.x + forward.z * forward.z + 1.0E-4);
      return (180.0 - Math.toDegrees(Math.acos(vect / lc))) * (double)(forward.x > 0.0 ? -1 : 1);
   }

   public static double computeVerticalAngle(Matrix4d transforms) {
      Vector3d zero = transforms.transformPosition(new Vector3d(0.0, 0.0, 0.0));
      Vector3d above = transforms.transformPosition(new Vector3d(0.0, 0.0, -10.0)).sub(zero);
      Vector3d n = new Vector3d(above.x, 0.0, above.z);
      double vect = above.y * n.y + above.z * n.z + above.x * n.x;
      double lc = n.length() * above.length() + 1.0E-4;
      return Math.toDegrees(Math.acos(vect / lc)) * (double)(above.y <= 0.0 ? 1 : -1);
   }

   public static double computeRollAngle(Matrix4d transforms) {
      return Math.toDegrees(Math.atan2(transforms.get(1, 2), transforms.get(2, 2)));
   }

   @SubscribeEvent
   public static void gatherTooltips(GatherComponents event) {
      ItemStack stack = event.getItemStack();
      if (stack.getTag() != null && stack.getTag().contains("st_lore")) {
         event.getTooltipElements().add(Either.left(Component.literal(stack.getTag().getString("st_lore"))));
      }
   }

   @SubscribeEvent
   public static void tick(PlayerTickEvent event) {
      if (event.side == LogicalSide.CLIENT && event.phase == Phase.END) {
         BlackoutOverlay.ticker = Mth.clamp(BlackoutOverlay.ticker - 1, 0, Integer.MAX_VALUE);
         BlackoutBelowChatOverlay.ticker = Mth.clamp(BlackoutBelowChatOverlay.ticker - 1, 0, Integer.MAX_VALUE);
         BlinkOverlay.ticker = Mth.clamp(BlinkOverlay.ticker - 1, 0, Integer.MAX_VALUE);
         BlinkOverlay.ticker_close = Mth.clamp(BlinkOverlay.ticker_close - 1, 0, Integer.MAX_VALUE);
         if (BlinkOverlay.currentBlackout != null && BlinkOverlay.ticker <= BlinkOverlay.currentBlackout.outTime()) {
            BlinkOverlay.ticker_open = Mth.clamp(BlinkOverlay.ticker_open - 1, 0, Integer.MAX_VALUE);
         }
      }
   }

   @SubscribeEvent
   public static void rightClickEmpty(RightClickEmpty event) {
      if (event.getHand() == InteractionHand.MAIN_HAND) {
         Network.sendToServer(new RightClickEmptyPacket());
      }
   }

   @SubscribeEvent
   public static void cancelItemPickup(EntityItemPickupEvent event) {
      if (ClientsideCutsceneHandler.currentData != null) {
         event.setCanceled(true);
      }
   }

   @SubscribeEvent
   public static void onRenderMinimap(RenderMiniMapEvent event) {
      if (ClientsideCutsceneHandler.currentData != null || HIDEGUI) {
         event.setCanceled(true);
      }
   }

   @SubscribeEvent
   public static void onRenderTOP(RenderTOPEvent event) {
      if (ClientsideCutsceneHandler.currentData != null || HIDEGUI) {
         event.setCanceled(true);
      }
   }

   @SubscribeEvent
   public static void onRenderToroHealth(RenderToroHealthEvent event) {
      if (ClientsideCutsceneHandler.currentData != null || HIDEGUI) {
         event.setCanceled(true);
      }

      if (event.getEntity() instanceof IDisableHealthStats) {
         event.setCanceled(true);
      }
   }

   @SubscribeEvent
   public static void onPRenderLayer(Pre event) {
      if (ClientsideCutsceneHandler.currentData != null && ClientsideCutsceneHandler.currentData.cameraOut) {
         event.setCanceled(true);
      }
   }

   @SubscribeEvent
   public static void onHRenderLayer(RenderHotBarEvent event) {
      if (ClientsideCutsceneHandler.currentData != null || BlackoutOverlay.getAlpha() > 0.99F || HIDEGUI) {
         event.setCanceled(true);
      }
   }

   @SubscribeEvent
   public static void onRenderEffects(net.minecraftforge.client.event.RenderGuiOverlayEvent.Pre event) {
      if ((ClientsideCutsceneHandler.currentData != null || HIDEGUI) && event.getOverlay() == VanillaGuiOverlay.POTION_ICONS.type()) {
         event.setCanceled(true);
      }

      if ((ClientsideCutsceneHandler.currentData != null || HIDEGUI) && event.getOverlay() == VanillaGuiOverlay.HOTBAR.type()) {
         event.setCanceled(true);
      }
   }

   @SubscribeEvent
   public static void onCRenderLayer(RenderCrosshairEvent event) {
      if (ClientsideCutsceneHandler.currentData != null || HIDEGUI) {
         event.setCanceled(true);
      }
   }

   @SubscribeEvent
   public static void onBlockHighlight(Block event) {
      if (ClientsideCutsceneHandler.currentData != null || HIDEGUI) {
         event.setCanceled(true);
      }
   }

   @SubscribeEvent
   public static void onHandRenderer(RenderHandEvent event) {
      if (ClientsideCutsceneHandler.currentData != null || HIDEGUI) {
         event.setCanceled(true);
      }
   }

   @SubscribeEvent
   public static void onEffect(RenderBlockScreenEffectEvent event) {
      if (ClientsideCutsceneHandler.currentData != null || HIDEGUI) {
         event.setCanceled(true);
      }
   }

   @SubscribeEvent
   public static void onCutsceneStuckInBlocks(LivingAttackEvent event) {
      if (ClientsideCutsceneHandler.currentData != null && event.getEntity() instanceof Player) {
         event.setCanceled(true);
      }
   }
}
