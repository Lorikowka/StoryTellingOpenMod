package com.storyteam.storytelling.system.persona;

import java.io.File;
import lombok.Generated;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;

public class PersonaChangeEvent extends Event {
   private final File stDir;
   private final Player player;
   private final Persona persona;

   public File getSystemDirectory(String system) {
      return new File(this.stDir, system);
   }

   public File getSystemDirectoryForPersona(String system) {
      return this.getSystemDirectory(this.persona.name() + File.separator + system);
   }

   @Generated
   public File getStDir() {
      return this.stDir;
   }

   @Generated
   public Player getPlayer() {
      return this.player;
   }

   @Generated
   public Persona getPersona() {
      return this.persona;
   }

   @Generated
   public PersonaChangeEvent(File stDir, Player player, Persona persona) {
      this.stDir = stDir;
      this.player = player;
      this.persona = persona;
   }

   public static class ReadPersona extends PersonaChangeEvent {
      private final Persona previous;

      public ReadPersona(File stDir, Player player, Persona previous, Persona persona) {
         super(stDir, player, persona);
         this.previous = previous;
      }

      @Generated
      public Persona getPrevious() {
         return this.previous;
      }
   }

   public static class SavePersona extends PersonaChangeEvent {
      public SavePersona(File stDir, Player player, Persona persona) {
         super(stDir, player, persona);
      }

      @Override
      public File getSystemDirectory(String system) {
         File f = super.getSystemDirectory(system);
         if (f.isFile()) {
            f.delete();
         }

         if (!f.isDirectory()) {
            f.mkdirs();
         }

         return f;
      }
   }
}
