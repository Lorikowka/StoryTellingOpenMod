package com.storyteam.storytelling.system.animation.backend;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class STAnimsRenderBackend {
   public static final Logger LOG = LogManager.getLogger("ST-Anims");
   private static final ThreadFactory DAEMON = r -> {
      Thread t = new Thread(r);
      t.setDaemon(true);
      t.setName("AnimationRenderBackendThread");
      return t;
   };
   public static final ScheduledExecutorService BACKGROUND_CHECKER = Executors.newScheduledThreadPool(8, DAEMON);
   public static final ScheduledExecutorService IO_SAVE = Executors.newScheduledThreadPool(1, DAEMON);
}
