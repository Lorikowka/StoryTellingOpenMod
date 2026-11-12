package com.storyteam.storytelling.system.sky_eye.performer;

import com.storyteam.storytelling.content.entities.SkyEyeEntity;
import com.storyteam.storytelling.script.instances.backs.mechanics.S102SkyEye;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.action.ScriptPerformerAction;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;
import com.storyteam.storytelling.script.story.characters.CharactersST8;
import com.storyteam.storytelling.util.world.Location;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

public class SetSkyEyePosition extends ScriptPerformerAction {
   private Supplier<Location> location = S102SkyEye::getDefLocation;
   private boolean isDone = false;

   public SetSkyEyePosition(ScriptPerformer performer) {
      super(performer);
   }

   public SetSkyEyePosition(ScriptPerformer performer, Supplier<Location> location) {
      super(performer);
      this.location = location;
   }

   @Override
   public void tick() {
      if (!this.isDone) {
         this.teleportSkyEye();
         this.isDone = true;
      }
   }

   @Override
   public void fastForwardToStage() {
      this.teleportSkyEye();
   }

   @Override
   public void globalFastForward() {
      this.teleportSkyEye();
   }

   private void teleportSkyEye() {
      SkyEyeEntity skyEye = this.getSkyEye();
      if (skyEye != null) {
         ResourceKey<Level> dimension = this.location.get().getDimension();
         if (dimension != null) {
            MinecraftServer server = skyEye.getServer();
            if (server != null) {
               ServerLevel level = server.getLevel(dimension);
               if (level != null) {
                  skyEye.changeDimension(level);
               }
            }
         }

         skyEye.setSpawnPoint(this.location.get());
      }
   }

   private SkyEyeEntity getSkyEye() {
      return (SkyEyeEntity)CharactersST8.SKY_EYE.repositoryEntity();
   }

   @Override
   public void reset() {
      super.reset();
      this.isDone = false;
   }

   @Override
   public boolean isDone() {
      return this.isDone;
   }

   @Override
   public HelpReportBuilder gatherHelpReport(HelpReportBuilder builder) {
      return builder.append("Перемещение глаза.");
   }
}
