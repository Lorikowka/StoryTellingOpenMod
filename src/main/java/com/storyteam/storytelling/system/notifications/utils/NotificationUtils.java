package com.storyteam.storytelling.system.notifications.utils;

import com.storyteam.storytelling.St;
import com.storyteam.storytelling.init.SoundsST;
import com.storyteam.storytelling.system.notifications.data.NotificationData;
import com.storyteam.storytelling.system.notifications.data.utils.NotificationIcon;
import com.storyteam.storytelling.system.notifications.storage.NotificationStorage;
import com.storyteam.storytelling.system.sounds.SoundUtils;
import com.storyteam.storytelling.util.ScriptUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class NotificationUtils {
   public static void addNotification(NotificationIcon icon, String title) {
      addNotification(icon, Component.literal(title));
   }

   public static void addNotification(NotificationIcon icon, Component title) {
      ServerPlayer player = ScriptUtils.getPlayer();
      if (player != null) {
         SoundUtils.playVolumedSound(player, SoundsST.NOTIFICATION);
      }

      addNotification(icon.getId(), title);
   }

   public static void addTabletNotification(Component title) {
      ServerPlayer player = ScriptUtils.getPlayer();
      if (player != null) {
         SoundUtils.playVolumedSound(player, SoundsST.TABLET_NOTIFICATION);
      }

      addNotification(NotificationIcon.TABLET_NOTIFICATION.getId(), title);
   }

   public static void addNotification(String image, String title) {
      addNotification(image, Component.literal(title));
   }

   public static void addNotification(String image, Component title) {
      addNotification(St.tx("gui/notifications/" + image), title);
   }

   public static void addNotification(ResourceLocation icon, Component title) {
      NotificationStorage.queue.add(0, new NotificationData(icon, title, 0));
   }
}
