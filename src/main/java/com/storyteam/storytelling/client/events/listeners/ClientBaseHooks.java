package com.storyteam.storytelling.client.events.listeners;

import com.storyteam.storytelling.api.entity.INoNameplate;
import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.api.itf.IStoryEntity;
import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.init.BindsST;
import com.storyteam.storytelling.system._core.st_cutscene.ClientsideCutsceneHandler;
import com.storyteam.storytelling.system._core.structio.AsyncStructIO;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.client.event.RenderNameTagEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent.Post;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber({Dist.CLIENT})
public class ClientBaseHooks {
   @SubscribeEvent
   public static void inputUpdateMove(MovementInputUpdateEvent e) {
      IStoryServer.get().postStoryEvent(EventType.MOVEMENT_INPUT_UPDATE, e);
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public static void preventNameplateRender(RenderNameTagEvent event) {
      if (event.getEntity() instanceof IStoryEntity && ((IStoryEntity)event.getEntity()).isStorytellingEntity()) {
         event.setResult(Result.DENY);
      }

      if (event.getEntity() instanceof INoNameplate) {
         event.setResult(Result.DENY);
      }

      if (ClientsideCutsceneHandler.currentData != null) {
         event.setResult(Result.DENY);
      }
   }

   @SubscribeEvent
   public static void onClientTick(ClientTickEvent event) {
      if (BindsST.keySpeedupSpeech.isDown() && Minecraft.getInstance().player != null && Minecraft.getInstance().player.tickCount % 2 == 0) {
         BindsST.onF8Press();
      }
   }

   @SubscribeEvent
   public static void onRenderTick(Post event) {
      AtomicInteger pad = new AtomicInteger(10);
      AsyncStructIO.runningTasks.iterator().forEachRemaining(task -> {
         event.getGuiGraphics().drawString(Minecraft.getInstance().font, task.name() + " - " + task.percentage() + "%", 5, 10 + pad.get(), 16777215, false);
         pad.addAndGet(25);
      });
   }
}
