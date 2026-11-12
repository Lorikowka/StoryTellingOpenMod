package com.storyteam.storytelling.events.base;

import com.storyteam.storytelling.system._sysofsys.ISystem;
import com.storyteam.storytelling.system.persona.PersonaHandler;
import java.io.File;
import lombok.Generated;
import net.minecraftforge.eventbus.api.Event;

public abstract class ScriptIOEvent extends Event {
   private final File stDir;

   public File getSystemDirectory(String system) {
      return new File(this.stDir, system);
   }

   public File getSystemDirectoryForPersona(String system) {
      return this.getSystemDirectory(PersonaHandler.getCurrentPersona().name() + File.separator + system);
   }

   public File getSystemDirectory(ISystem system) {
      return this.getSystemDirectory(system.getSystemName());
   }

   public File getSystemDirectoryForPersona(ISystem system) {
      return this.getSystemDirectory(system.getSystemName());
   }

   @Generated
   public ScriptIOEvent(File stDir) {
      this.stDir = stDir;
   }

   @Generated
   public File getStDir() {
      return this.stDir;
   }

   public static class Load extends ScriptIOEvent {
      private final net.minecraftforge.event.level.LevelEvent.Load forgeEvent;

      public Load(File stDir, net.minecraftforge.event.level.LevelEvent.Load forgeEvent) {
         super(stDir);
         this.forgeEvent = forgeEvent;
      }

      @Generated
      public net.minecraftforge.event.level.LevelEvent.Load getForgeEvent() {
         return this.forgeEvent;
      }
   }

   public static class Save extends ScriptIOEvent {
      private final net.minecraftforge.event.level.LevelEvent.Save forgeEvent;

      public Save(File stDir, net.minecraftforge.event.level.LevelEvent.Save forgeEvent) {
         super(stDir);
         this.forgeEvent = forgeEvent;
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

      @Generated
      public net.minecraftforge.event.level.LevelEvent.Save getForgeEvent() {
         return this.forgeEvent;
      }
   }
}
