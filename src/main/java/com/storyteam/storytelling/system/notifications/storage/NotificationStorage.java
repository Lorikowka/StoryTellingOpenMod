package com.storyteam.storytelling.system.notifications.storage;

import com.storyteam.storytelling.system.notifications.data.NotificationData;
import java.util.ArrayList;
import java.util.List;

public class NotificationStorage {
   public static List<NotificationData> notifications = new ArrayList<>();
   public static List<NotificationData> queue = new ArrayList<>();

   public static void processQueue() {
      if (!queue.isEmpty()) {
         NotificationData entry = queue.get(queue.size() - 1);
         if (notifications.isEmpty() || notifications.get(0).getProgress() >= 12) {
            notifications.add(0, entry.clone());
            queue.remove(entry);
         }
      }
   }
}
