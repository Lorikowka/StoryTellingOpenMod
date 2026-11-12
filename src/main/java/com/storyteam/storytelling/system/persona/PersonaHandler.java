package com.storyteam.storytelling.system.persona;

import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.events.listeners.ScriptIO;
import java.io.File;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Player;

public class PersonaHandler {
   public static String textureOverride = null;

   public static Persona getCurrentPersona() {
      String name = ScriptIO.getSubTag("persona").getString("current");
      if (name.isEmpty()) {
         ScriptIO.getSubTag("persona").putString("current", Persona.LOLOLOWKA.name());
         return Persona.LOLOLOWKA;
      } else {
         return Persona.valueOf(name);
      }
   }

   @Deprecated(
      since = "usePersona(Persona)"
   )
   public static void setPersona(Player player, Persona persona) {
      if (player != null && persona != getCurrentPersona()) {
         savePersona(player);
         loadPersona(player, persona);
      }
   }

   private static void savePersona(Player player) {
      IStoryServer story = IStoryServer.get(player.getServer());
      File dir = story.getStorytellingDir().toFile();
      if (!dir.isDirectory()) {
         dir.mkdirs();
      }

      CompoundTag data = new CompoundTag();
      player.addAdditionalSaveData(data);
      ScriptIO.getSubTag("persona").put(getCurrentPersona().name() + "_inventory", data);

      Storytelling.BUS.post(new PersonaChangeEvent.SavePersona(dir, player, getCurrentPersona()));
   }

   private static void loadPersona(Player player, Persona persona) {
      Persona prevPersona = getCurrentPersona();
      IStoryServer story = IStoryServer.get(player.getServer());
      File dir = story.getStorytellingDir().toFile();
      if (!dir.isDirectory()) {
         dir.mkdirs();
      }

      CompoundTag newData = ScriptIO.getSubTag("persona").getCompound(persona.name() + "_inventory");
      player.readAdditionalSaveData(newData);

      ScriptIO.getSubTag("persona").putString("current", persona.name());
      player.setHealth(player.getMaxHealth());
      Storytelling.BUS.post(new PersonaChangeEvent.ReadPersona(dir, player, prevPersona, persona));
   }
}
