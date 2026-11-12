package com.storyteam.storytelling;

import com.storyteam.storytelling.client.Animations;
import com.storyteam.storytelling.compat.base.BaseCompatST;
import com.storyteam.storytelling.content.items.ItemEntityOnHand;
import com.storyteam.storytelling.init.ext.ExternalBlocks;
import com.storyteam.storytelling.logging.LoggingController;
import com.storyteam.storytelling.network.StoryServer;
import com.storyteam.storytelling.proxy.STClientProxy;
import com.storyteam.storytelling.proxy.STCommonProxy;
import com.storyteam.storytelling.script.story.LuaStory;
import com.storyteam.storytelling.script.story.StoryWorkspace;
import com.storyteam.storytelling.script.story.npc.ScriptNpcs;
import com.storyteam.storytelling.system._sysofsys.SystemOfSystems;
import com.storyteam.storytelling.system.octospizdel.client.animation.TweenSystem;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.CrashReport;
import net.minecraft.ReportedException;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.GameShuttingDownEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.util.thread.EffectiveSide;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zeith.hammeranims.api.HammerAnimationsApi;
import org.zeith.hammerlib.api.proxy.IProxy;
import org.zeith.hammerlib.compat.base.CompatList;
import org.zeith.hammerlib.core.adapter.LanguageAdapter;

@Mod("storytelling")
public class Storytelling {
   public static final String MODID = "storytelling";
   public static final Logger LOG = LogManager.getLogger("Storytelling");
   public static final STCommonProxy PROXY = (STCommonProxy)IProxy.create(() -> STClientProxy::new, () -> STCommonProxy::new);
   public static final CompatList<BaseCompatST> COMPATS = CompatList.gather(BaseCompatST.class);
   public static final Set<String> MISSING_FILES = new HashSet<>();
   public static final IEventBus BUS = MinecraftForge.EVENT_BUS;
   public static boolean renderDebugPaths = false;
   public static boolean teleportOnFreeCamera = true;

   public Storytelling() {
      HammerAnimationsApi.LOG_RELOADS = false;
      System.setProperty("java.awt.headless", "false");
      LanguageAdapter.registerMod("storytelling");
      try {
         StoryWorkspace.initialize();
         LuaStory.initializeStoryDirectory(LuaStory.storyDirectory());
      } catch (IOException e) {
         LOG.error("Cannot initialize Lua story directory", e);
      }
      PROXY.setup();
      IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
      ScriptNpcs.TYPES.register(bus);
      bus.addListener(this::loadComplete);
      ItemEntityOnHand.setup();
      SystemOfSystems.construct(BUS, bus);
      BUS.addListener(EventPriority.HIGHEST, true, Storytelling::onClose);

      for (BaseCompatST c : COMPATS.getActive()) {
         c.construct(bus, BUS);
      }

      TweenSystem.init();
      LOG.info("Storytelling constructed");
   }

   private void loadComplete(FMLLoadCompleteEvent e) {
      long bootupTime = ManagementFactory.getRuntimeMXBean().getUptime();
      ExternalBlocks.setup();
      ExternalBlocks.bind();
      LOG.info("Minecraft loaded in {} seconds.", new DecimalFormat("#.###").format((double)bootupTime / 1000.0));
      StoryServer.getInstance().connect();
      if (EffectiveSide.get().isClient()) {
         new LoggingController();
      }

      if (Boolean.parseBoolean(System.getProperty("runability", "false"))) {
         System.exit(0);
      }

      Animations.getAnimationMap();
      if (!MISSING_FILES.isEmpty()) {
         StackTraceElement[] ste = new StackTraceElement[]{
            new StackTraceElement(Storytelling.class.getSimpleName(), "loadComplete", "Storytelling.java", 100500)
         };
         FileNotFoundException er0 = new FileNotFoundException(String.join(", ", MISSING_FILES));
         er0.setStackTrace(ste);
         ReportedException er1 = new ReportedException(CrashReport.forThrowable(er0, "Unable to find required files:\n" + String.join("\n", MISSING_FILES)));
         er1.setStackTrace(ste);
         throw er1;
      }
   }

   @SubscribeEvent
   public static void onClose(GameShuttingDownEvent event) {
      StoryServer.getInstance().disconnect();
   }
}
