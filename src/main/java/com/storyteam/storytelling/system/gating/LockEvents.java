package com.storyteam.storytelling.system.gating;

import com.storyteam.storytelling.system.gating.event.GatherHiddenItemsEvent;
import com.storyteam.storytelling.system.gating.event.NotifyHiddenItemsEvent;
import com.storyteam.storytelling.system.gating.event.RequestLockedItemUpdateEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.event.TagsUpdatedEvent.UpdateCause;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LockEvents {
   private static boolean needRefresh;
   private static LockedContents currentlyHidden = LockedContents.NONE;

   public static void updateHiddenList(LockedContents hidden) {
      LockedContents old = currentlyHidden;
      currentlyHidden = hidden;
      MinecraftForge.EVENT_BUS.post(new NotifyHiddenItemsEvent(old, hidden));
   }

   @SubscribeEvent
   public static void processRequest(RequestLockedItemUpdateEvent e) {
      needRefresh = true;
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public static void clientTick(ClientTickEvent e) {
      if (e.phase == Phase.START) {
         if (needRefresh) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null) {
               updateHiddenList(GatherHiddenItemsEvent.gather(mc.player));
               needRefresh = false;
            }
         }
      }
   }

   @SubscribeEvent
   public static void receiveClientsideTags(TagsUpdatedEvent e) {
      if (e.getUpdateCause() == UpdateCause.CLIENT_PACKET_RECEIVED) {
         RequestLockedItemUpdateEvent.request();
      }
   }
}
