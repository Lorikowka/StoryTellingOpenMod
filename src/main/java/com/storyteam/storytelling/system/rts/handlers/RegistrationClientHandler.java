package com.storyteam.storytelling.system.rts.handlers;

import com.solegendary.reignofnether.hud.HudClientEvents;
import com.solegendary.reignofnether.hud.HudClientEvents.ResourceDisplay;
import com.storyteam.storytelling.system.currency.storage.CurrencyStorage;
import java.util.List;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import org.zeith.hammerlib.util.mcf.Resources;

public class RegistrationClientHandler {
   @SubscribeEvent(
      priority = EventPriority.LOWEST
   )
   public static void registerClient(RegisterEvent event) {
      if (event.getRegistryKey().equals(ForgeRegistries.ITEMS.getRegistryKey())) {
         HudClientEvents.resourceDisplays
            .add(
               new ResourceDisplay(
                  Resources.location("reignofnether", "textures/icons/items/eidos.png"),
                  () -> String.valueOf(CurrencyStorage.getEidos()),
                  () -> 0,
                  false,
                  List.of(FormattedCharSequence.forward("Эйдосы", Style.EMPTY)),
                  () -> List.of(FormattedCharSequence.forward("Рабочих на эйдосах", Style.EMPTY))
               )
            );
      }
   }
}
