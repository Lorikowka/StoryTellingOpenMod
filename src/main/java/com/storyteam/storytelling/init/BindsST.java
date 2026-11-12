package com.storyteam.storytelling.init;

import com.mojang.blaze3d.platform.InputConstants.Type;
import com.storyteam.storytelling.client.content.overlays.MessageOverlay;
import com.storyteam.storytelling.client.screen.ChatLogScreen;
import com.storyteam.storytelling.net.PacketF10Press;
import com.storyteam.storytelling.net.PacketF8Press;
import com.storyteam.storytelling.net.PacketF9Press;
import com.storyteam.storytelling.net.StopTDCutscenePacket;
import com.storyteam.storytelling.system.sounds.SoundUtils;
import com.storyteam.storytelling.system.tribute.client.screen.TributeScreen;
import com.storyteam.storytelling.system.tribute.storage.TributeOfferStorage;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.InputEvent.Key;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.zeith.hammerlib.net.Network;

@EventBusSubscriber(
   modid = "storytelling",
   bus = Bus.FORGE,
   value = {Dist.CLIENT}
)
public class BindsST {
   public static final KeyMapping keySpeedupSpeech = new KeyMapping("storytelling.key_bindings.speedup_speech", 298, "storytelling.key_bindings.category.name");
   public static final KeyMapping keySpeedupDialog = new KeyMapping("storytelling.key_bindings.speedup_dialog", 299, "storytelling.key_bindings.category.name");
   public static final KeyMapping keyNextSpeech = new KeyMapping("storytelling.key_bindings.next_speech", 297, "storytelling.key_bindings.category.name");
   public static final KeyMapping STOP_TD_CUTSCENE = new KeyMapping("storytelling.key_bindings.stop_td_cutscene", 78, "storytelling.key_bindings.category.name");
   public static final KeyMapping OPEN_LEVELING = new KeyMapping("storytelling.key_bindings.open_leveling", 80, "storytelling.key_bindings.category.name");
   public static final KeyMapping OPEN_CHATLOG = new KeyMapping("storytelling.key_bindings.open_chatlog", 89, "storytelling.key_bindings.category.name");
   public static final KeyMapping OPEN_SPELL_SELECTION = new KeyMapping(
      "storytelling.key_bindings.open_spell_selection", 90, "storytelling.key_bindings.category.name"
   );
   public static final KeyMapping TOGGLE_VIDEOTEST_INFO = new KeyMapping(
      "storytelling.key_bindings.toggle_videotest_info", 268, "storytelling.key_bindings.category.name"
   );
   public static final KeyMapping OVERRIDE_SPEED = new KeyMapping("storytelling.key_bindings.override_speed", 296, "storytelling.key_bindings.category.name");
   public static final KeyMapping MESSAGE_OFFSET_SV_TOGGLE = new KeyMapping(
      "storytelling.key_bindings.message_offset_sv_toggle", 325, "storytelling.key_bindings.category.name"
   );
   public static final KeyMapping OPEN_TRIBUTE_SCREEN = new KeyMapping(
      "storytelling.key_bindings.open_tribute_screen", 73, "storytelling.key_bindings.category.name"
   );
   public static final float NPC_WALKING_SPEED_VALUE_APPROXIMATION_FOR_PLAYER_SPEED = 0.044F;
   public static Float overrideSpeed = null;
   public static boolean showVideotestInfo = false;

   @SubscribeEvent
   public static void onKeyPress(Key event) {
      Minecraft mc = Minecraft.getInstance();
      if (mc.screen == null) {
         if (STOP_TD_CUTSCENE.isDown()) {
            Network.sendToServer(new StopTDCutscenePacket());
         }

         if (OPEN_CHATLOG.isDown()) {
            mc.setScreen(new ChatLogScreen());
         }

         if (OPEN_LEVELING.isDown()) {
            SoundUtils.playVolumedSound(mc.player, SoundsST.STATS_MENU_OPEN);
         }

         if (OPEN_TRIBUTE_SCREEN.isDown() && !TributeOfferStorage.getOffer().isEmpty()) {
            mc.setScreen(new TributeScreen());
            SoundUtils.playVolumedSound(Minecraft.getInstance().player, SoundsST.TRIBUTE_OPEN);
         }
      }

      if (MESSAGE_OFFSET_SV_TOGGLE.consumeClick()) {
         MessageOverlay.SV = !MessageOverlay.SV;
      }

      if (keyNextSpeech.getKey().getType() == Type.KEYSYM && event.getKey() == keyNextSpeech.getKey().getValue() && event.getAction() == 1) {
         onF8Press();
      }

      if (keySpeedupSpeech.getKey().getType() == Type.KEYSYM && event.getKey() == keySpeedupSpeech.getKey().getValue() && event.getAction() == 1) {
         onF9Press();
      }

      if (keySpeedupDialog.getKey().getType() == Type.KEYSYM && event.getKey() == keySpeedupDialog.getKey().getValue() && event.getAction() == 1) {
         onF10Press();
      }

      if (TOGGLE_VIDEOTEST_INFO.getKey().getType() == Type.KEYSYM && event.getKey() == TOGGLE_VIDEOTEST_INFO.getKey().getValue() && event.getAction() == 1) {
         showVideotestInfo = !showVideotestInfo;
      }

      if (OVERRIDE_SPEED.getKey().getType() == Type.KEYSYM && event.getKey() == OVERRIDE_SPEED.getKey().getValue() && event.getAction() == 1) {
         overrideSpeed = overrideSpeed != null ? null : 0.044F;
      }
   }

   public static void onF8Press() {
      if (Minecraft.getInstance().getConnection() != null) {
         Network.sendToServer(new PacketF8Press());
      }
   }

   private static void onF9Press() {
      if (Minecraft.getInstance().getConnection() != null) {
         Network.sendToServer(new PacketF9Press());
      }
   }

   private static void onF10Press() {
      if (Minecraft.getInstance().getConnection() != null) {
         Network.sendToServer(new PacketF10Press());
      }
   }

   static {
      OPEN_SPELL_SELECTION.setKeyConflictContext(KeyConflictContext.UNIVERSAL);
   }

   @EventBusSubscriber(
      bus = Bus.MOD,
      value = {Dist.CLIENT}
   )
   public static class KeyBindsRegistrar {
      @SubscribeEvent
      public static void onKeybindingRegistry(RegisterKeyMappingsEvent event) {
         try {
            for (Field f : BindsST.class.getDeclaredFields()) {
               if (KeyMapping.class.isAssignableFrom(f.getType()) && Modifier.isStatic(f.getModifiers())) {
                  f.setAccessible(true);
                  event.register((KeyMapping)f.get(null));
               }
            }
         } catch (IllegalAccessException e) {
            throw new IllegalStateException("Unable to register key bindings", e);
         }
      }
   }
}

