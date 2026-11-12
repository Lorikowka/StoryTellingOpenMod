package com.storyteam.storytelling.system._core.structio.structure.v3;

import java.io.BufferedReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import lombok.Generated;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeith.hammerlib.util.mcf.RunnableReloader;

public class AttachmentRegistry {
   @Generated
   private static final Logger log = LoggerFactory.getLogger(AttachmentRegistry.class);
   private static final Map<String, IAttachmentProcessor> REGISTRY = new HashMap<>();

   @SubscribeEvent
   public static void register(AddReloadListenerEvent e) {
      e.addListener(RunnableReloader.of(AttachmentRegistry::collect));
   }

   public static Map<String, IAttachmentProcessor> getRegistry() {
      return Collections.unmodifiableMap(REGISTRY);
   }

   public static void collect(ResourceManager resources) {
      REGISTRY.clear();
      Map<ResourceLocation, Resource> map = resources.listResources("structio/attachments", f -> f.getPath().endsWith(".js"));
      if (!map.isEmpty()) {
         NashornScriptEngineFactory fact = new NashornScriptEngineFactory();

         for (Entry<ResourceLocation, Resource> e : map.entrySet()) {
            try (BufferedReader reader = e.getValue().openAsReader()) {
               ScriptEngine engine = fact.getScriptEngine(new String[]{"-doe", "--language=es6"});
               Invocable invocable = (Invocable)engine;
               engine.eval(reader);
               AttachmentRegistry.JSFilter filter = invocable.getInterface(AttachmentRegistry.JSFilter.class);
               if (filter.test()) {
                  REGISTRY.put(
                     e.getKey().toString(),
                     new IAttachmentProcessor.SafeWrapper(e.getKey().toString(), Objects.requireNonNull(invocable.getInterface(IAttachmentProcessor.class)))
                  );
                  log.info("Registered IAttachmentProcessor using ES6 from {}", e.getKey());
               } else {
                  log.info("Script {} did not pass the test. Skipping!", e.getKey());
               }
            } catch (Exception var11) {
               log.error("Failed to load {}!", e.getKey(), var11);
            }
         }
      }
   }

   public interface JSFilter {
      boolean test();
   }
}
