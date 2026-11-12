package com.storyteam.storytelling.system.dialog;

import com.storyteam.storytelling.api.itf.IStoryEntity;
import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.events.base.F8PressEvent;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.util.ScriptUtils;
import java.util.HashMap;
import java.util.Objects;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.util.thread.EffectiveSide;
import org.zeith.hammerlib.net.Network;

@EventBusSubscriber({Dist.CLIENT})
public class DialogHandler {
   public static final HashMap<Character<? extends AnimatedEntity>, Dialog> currentDialogs = new HashMap<>();

   public static Dialog getDialog(Character<? extends AnimatedEntity> character) {
      return currentDialogs.get(character);
   }

   public static void setDialog(Dialog dialog) {
      currentDialogs.put(dialog.owner, dialog);
   }

   public static boolean isDialogPresent(Character<? extends AnimatedEntity> character) {
      return currentDialogs.containsKey(character);
   }

   public static boolean isDialogPresentHacky(Character<?> character) {
      return currentDialogs.containsKey(character);
   }

   public static void removeDialog(Character<? extends AnimatedEntity> character) {
      currentDialogs.remove(character);
   }

   public static void setAndOpenDialog(Dialog dialog) {
      setDialog(dialog);
      if (EffectiveSide.get().isClient()) {
         Minecraft.getInstance().setScreen(new DialogScreen(dialog.owner));
      } else {
         Network.sendTo(ScriptUtils.getPlayer(), new OpenDialogPacket(dialog.owner));
      }
   }

   @SubscribeEvent
   public static void handleDialogReopening(EntityInteract event) {
      Entity entity = event.getTarget();
      if (entity.level().isClientSide && event.getHand() == InteractionHand.MAIN_HAND) {
         IStoryEntity e = IStoryEntity.of(entity);
         Character<? extends AnimatedEntity> character = Character.getById(e.getStoryName());
         if (character != null && currentDialogs.containsKey(character)) {
            Minecraft.getInstance().setScreen(new DialogScreen(character));
         }
      }
   }

   @SubscribeEvent
   public static void handleDialogReopening3(F8PressEvent event) {
      if (Minecraft.getInstance().screen == null) {
         for (Entry<Character<? extends AnimatedEntity>, Dialog> entry : currentDialogs.entrySet()) {
            if (!Objects.equals(entry.getValue().talkingToId, entry.getKey().getReferenceId())) {
               Network.sendTo(ScriptUtils.getPlayer(), new OpenDialogPacket(entry.getKey()));
               return;
            }
         }
      }
   }
}
