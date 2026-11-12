package com.storyteam.storytelling.system.exp;

import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.events.listeners.ScriptIO;
import com.storyteam.storytelling.system.exp.events.ExpChangeEvent;
import com.storyteam.storytelling.system.notifications.data.utils.NotificationIcon;
import com.storyteam.storytelling.system.notifications.utils.NotificationUtils;
import com.storyteam.storytelling.system.persona.Persona;
import com.storyteam.storytelling.system.persona.PersonaHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraftforge.common.MinecraftForge;

public class ExpStorage {
   private static int AMOUNT = 0;

   public static void setAmount(int value) {
      AMOUNT = Mth.clamp(value, 0, 9999999);
   }

   public static void addAmount(int value) {
      addAmount(value, false);
   }

   public static void addAmount(int value, boolean ignoreModifiers) {
      addAmount(value, ignoreModifiers, true);
   }

   public static void addAmount(int value, boolean ignoreModifiers, boolean notify) {
      ExpChangeEvent e = new ExpChangeEvent(value);
      MinecraftForge.EVENT_BUS.post(e);
      IStoryServer.get().postStoryEvent(EventType.EXP_CHANGE_EVENT, e);
      setAmount(AMOUNT + e.getExp());
      if (notify && value < 0) {
         NotificationUtils.addNotification(NotificationIcon.EXP_TAKE, Component.literal(String.valueOf(-value)));
      }

      if (notify && value > 0) {
         NotificationUtils.addNotification(NotificationIcon.EXP_ADD, Component.literal(String.valueOf(value)));
      }
   }

   public static int getAmount() {
      return AMOUNT;
   }

   public static void save() {
      try {
         Persona fursona = PersonaHandler.getCurrentPersona().getLevelingPersona();
         if (fursona == Persona.LOLOLOWKA) {
            ScriptIO.WORLD_EXTRA_NBT.putInt("expAmount", AMOUNT);
         } else {
            ScriptIO.WORLD_EXTRA_NBT.putInt(fursona.name() + "expAmount", AMOUNT);
         }
      } catch (Throwable var1) {
         if (var1 instanceof RuntimeException runtime) throw runtime;
         if (var1 instanceof Error error) throw error;
         throw new IllegalStateException("Operation failed", var1);
      }
   }

   public static void load() {
      try {
         Persona fursona = PersonaHandler.getCurrentPersona().getLevelingPersona();
         if (fursona == Persona.LOLOLOWKA) {
            AMOUNT = ScriptIO.WORLD_EXTRA_NBT.getInt("expAmount");
         } else {
            AMOUNT = ScriptIO.WORLD_EXTRA_NBT.getInt(fursona.name() + "expAmount");
         }
      } catch (Throwable var1) {
         if (var1 instanceof RuntimeException runtime) throw runtime;
         if (var1 instanceof Error error) throw error;
         throw new IllegalStateException("Operation failed", var1);
      }
   }
}
