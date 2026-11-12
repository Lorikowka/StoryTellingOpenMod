package com.llamalad7.mixinextras.platform.forge;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.IExtensionPoint.DisplayTest;
import net.minecraftforge.fml.common.Mod;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod("mixinextras")
public class MixinExtrasMod {
   private static final Logger LOGGER = LoggerFactory.getLogger("MixinExtras|Forge");
   private static final String IGNORESERVERONLY = "OHNOES\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31";

   public MixinExtrasMod() {
      ArtifactVersion version = this.getForgeVersion();
      if (version != null) {
         if (version.compareTo(new DefaultArtifactVersion("41.1.0")) < 0) {
            this.markAsOneSided();
         }
      }
   }

   private ArtifactVersion getForgeVersion() {
      return ModList.get().getModContainerById("forge").map(it -> it.getModInfo().getVersion()).orElse(null);
   }

   private void markAsOneSided() {
      try {
         ModLoadingContext.get()
            .registerExtensionPoint(
               DisplayTest.class,
               () -> new DisplayTest(
                     () -> "OHNOES\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31",
                     (a, b) -> true
                  )
            );
      } catch (Exception var2) {
         LOGGER.warn("Failed to mark MixinExtras as a one-sided mod", var2);
      }
   }
}
