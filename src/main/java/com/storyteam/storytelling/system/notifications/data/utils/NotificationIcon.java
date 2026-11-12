package com.storyteam.storytelling.system.notifications.data.utils;

import java.util.Locale;
import lombok.Generated;

public enum NotificationIcon {
   QUEST_COMPLETE,
   QUEST_FAIL,
   QUEST_UPDATE,
   SPELL_RESEARCH,
   GEN_NOTIFICATION,
   TABLET_NOTIFICATION,
   CRAFT_NOTIFICATION,
   ORG_MONEY_ADD,
   ORG_MONEY_TAKE,
   EXP_ADD,
   EXP_TAKE;

   public String getId() {
      return this.name().toLowerCase(Locale.ROOT);
   }
}
