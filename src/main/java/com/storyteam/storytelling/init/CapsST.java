package com.storyteam.storytelling.init;

import com.storyteam.storytelling.St;
import com.storyteam.storytelling.api.itf.IStoryEntity;
import com.storyteam.storytelling.compat.hl.src.StoryDataSourceType;
import com.storyteam.storytelling.content.caps.StoryData;
import com.storyteam.storytelling.system.simulator.cognition_tab.capability.CognitionCapability;
import com.storyteam.storytelling.system.simulator.cognition_tab.capability.CognitionCapabilityProvider;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.StartTracking;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;
import org.zeith.hammerlib.net.Network;

@SimplyRegister
@EventBusSubscriber
public class CapsST {
   public static final Capability<StoryData> STORY_DATA = CapabilityManager.get(new CapabilityToken<StoryData>() {
   });
   public static final Capability<CognitionCapability> COG_CAP = CapabilityManager.get(new CapabilityToken<CognitionCapability>() {
   });
   @RegistryName("story_data")
   public static final StoryDataSourceType STORY_DATA_SRC_TYPE = new StoryDataSourceType();

   @SubscribeEvent
   public static void attachEntityCaps(AttachCapabilitiesEvent<Entity> event) {
      if (event.getObject() instanceof LivingEntity) {
         event.addCapability(St.id("st_data"), new StoryData.Provider((Entity)event.getObject()));
      }

      if (event.getObject() instanceof Player player) {
         event.addCapability(St.id("cognition_cap"), new CognitionCapabilityProvider(player));
      }
   }

   @SubscribeEvent
   public static void startTracking(StartTracking event) {
      if (event.getEntity() instanceof ServerPlayer sp && IStoryEntity.of(event.getTarget()).isStorytellingEntity()) {
         StoryData data = StoryData.of(event.getTarget());
         if (data != null) {
            Network.sendTo(sp, data.getProperties().createGlobalUpdate());
         }
      }
   }
}
