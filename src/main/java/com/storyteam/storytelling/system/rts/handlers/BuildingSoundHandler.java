package com.storyteam.storytelling.system.rts.handlers;

import com.solegendary.reignofnether.event.BuildingBuiltEvent;
import com.solegendary.reignofnether.event.PlaceBuildingEvent;
import com.storyteam.storytelling.system.rts.init.RTSSoundsST;
import net.minecraft.client.Minecraft;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BuildingSoundHandler {
   @SubscribeEvent
   public static void onPlaceBuilding(PlaceBuildingEvent event) {
      if (event.isClientSide()) {
         Minecraft.getInstance().player.playSound(RTSSoundsST.PLACE_BUILDING, 1.0F, 1.0F);
      }
   }

   @SubscribeEvent
   public static void onBuildingBuilt(BuildingBuiltEvent event) {
      if (event.isClientSide()) {
         Minecraft.getInstance().player.playSound(RTSSoundsST.BUILDING_BUILT, 1.0F, 1.0F);
      }
   }
}
