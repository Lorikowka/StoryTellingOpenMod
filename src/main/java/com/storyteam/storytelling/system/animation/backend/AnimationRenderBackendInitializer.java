package com.storyteam.storytelling.system.animation.backend;

import com.google.common.base.Suppliers;
import com.storyteam.storytelling.Storytelling;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;
import javax.swing.filechooser.FileSystemView;
import lombok.Generated;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@EventBusSubscriber({Dist.DEDICATED_SERVER})
public class AnimationRenderBackendInitializer {
   @Generated
   private static final Logger log = LoggerFactory.getLogger(AnimationRenderBackendInitializer.class);
   public static final boolean RENDER_BACKEND_ACTIVE;
   public static final Supplier<Path> STD_PATH = Suppliers.memoize(
      () -> FileSystemView.getFileSystemView().getDefaultDirectory().toPath().resolve("My Games").resolve("Storytelling")
   );

   @SubscribeEvent
   public static void clientTick(ServerStartedEvent e) {
   }

   static {
      boolean active = false;

      try {
         Path backendCfgFile = STD_PATH.get().resolve("backend.json");
         if (Files.exists(backendCfgFile)) {
            log.info("Render backend activated!");
            AnimationRenderBackend.initialize(backendCfgFile);
            Storytelling.BUS.register(AnimationRenderBackend.class);
            active = true;
         } else {
            log.warn("Render backend inactive. Configuration file '{}' not found.", backendCfgFile.toAbsolutePath());
         }
      } catch (UnsatisfiedLinkError | ExceptionInInitializerError var2) {
         log.error("Failed to start render backend dumper.");
      }

      RENDER_BACKEND_ACTIVE = active;
   }
}
