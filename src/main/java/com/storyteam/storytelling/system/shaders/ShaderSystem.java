package com.storyteam.storytelling.system.shaders;

import com.storyteam.storytelling.compat.iris.IrisProxy;
import com.storyteam.storytelling.system._sysofsys.ISystem;
import com.storyteam.storytelling.system._sysofsys.SystemRoot;
import com.storyteam.storytelling.util.Necr0manthUtils;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.Generated;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@SystemRoot("shaders")
public class ShaderSystem implements ISystem {
   public static final String mainShaders = "Bliss-shader-patched.zip";
   public static HashMap<String, ShaderModifier> modifiers = new HashMap<>();
   private static boolean initialized = false;
   private static boolean enabled = false;
   private static String currentShaderpackString = null;
   private static boolean updateShaders = false;

   public static void setEnabled(boolean enabled) {
      ShaderSystem.enabled = enabled;
      Necr0manthUtils.saveStConfig("shaderSystemEnabled.txt", enabled + "");
      if (enabled) {
         updateShaderPack();
      }
   }

   public static List<ShaderModifier> sortModifiers() {
      return modifiers.values().stream().sorted(ShaderModifier::compare).toList();
   }

   public static void updateShaderPack() {
      List<ShaderModifier> sorted = sortModifiers();
      if (!sorted.isEmpty()) {
         String newShader = sorted.get(0).shaderPackName;
         if (!Objects.equals(getCurrentConfiguredShaderPack(), getConfiguredShaderPack(newShader))) {
            currentShaderpackString = newShader;
            updateShaders = true;
         }
      }
   }

   public static void addShader(ShaderModifier modifier) {
      modifiers.put(modifier.name, modifier);
      updateShaderPack();
   }

   public static void simplyChangeShaderPack(String shaderpack, float priority) {
      addShader(new ShaderModifier(shaderpack, shaderpack).priority(priority));
   }

   public static void simplyChangeShaderPack(String modifierName, String shaderpack, float priority) {
      addShader(new ShaderModifier(modifierName, shaderpack).priority(priority));
   }

   public static void simplyChangeShaderPack(String shaderpack) {
      simplyChangeShaderPack(shaderpack, Float.POSITIVE_INFINITY);
   }

   public static void simplyChangeShaderPack(String modifierName, String shaderpack) {
      addShader(new ShaderModifier(modifierName, shaderpack).priority(Float.POSITIVE_INFINITY));
   }

   public static void removeShader(String name) {
      modifiers.remove(name);
      updateShaderPack();
   }

   public static void removeShader(ShaderModifier mod) {
      if (modifiers.get(mod.name) == mod) {
         modifiers.remove(mod.name);
      }

      updateShaderPack();
   }

   @SubscribeEvent
   public static void tick(ClientTickEvent event) {
      if (!initialized) {
         String config = Necr0manthUtils.readStConfig("shaderSystemEnabled.txt");
         if (config.equals("true")) {
            enabled = true;
         } else if (config.equals("false")) {
            enabled = false;
         } else {
            setEnabled(true);
         }

         initialized = true;
         updateShaderPack();
      }

      if (enabled) {
         AtomicBoolean c = new AtomicBoolean(false);
         modifiers.entrySet().removeIf(stringShaderModifierEntry -> {
            if (!stringShaderModifierEntry.getValue().isValid()) {
               c.set(true);
               return true;
            } else {
               return false;
            }
         });
         if (c.get()) {
            updateShaderPack();
         }

         if (updateShaders
            && currentShaderpackString != null
            && !currentShaderpackString.equals(IrisProxy.getInstance().getCurrentShader())
            && !(currentShaderpackString + ".txt").equals(IrisProxy.getInstance().getCurrentShader())) {
            IrisProxy.getInstance().changeShader(currentShaderpackString);
            updateShaders = false;
         }
      }
   }

   public static ConfiguredShaderPack getCurrentConfiguredShaderPack() {
      return currentShaderpackString != null
         ? getConfiguredShaderPack(currentShaderpackString)
         : new ConfiguredShaderPack("", ConfiguredShaderPack.ShaderPackConfig.EMPTY);
   }

   public static ConfiguredShaderPack getConfiguredShaderPack(String rawShaderPackName) {
      if (rawShaderPackName.endsWith(".txt")) {
         try {
            String shaderPackName = Files.newBufferedReader(Minecraft.getInstance().gameDirectory.toPath().resolve("shaderpacks").resolve(rawShaderPackName)).readLine().split("shaderpack: ", 2)[1];
            return new ConfiguredShaderPack(shaderPackName, new ConfiguredShaderPack.ShaderPackTxtConfig(rawShaderPackName));
         } catch (Exception var3) {
            return new ConfiguredShaderPack(rawShaderPackName, ConfiguredShaderPack.ShaderPackConfig.EMPTY);
         }
      } else {
         Path configPath = Minecraft.getInstance().gameDirectory.toPath().resolve("shaderpacks").resolve(rawShaderPackName + ".txt");
         if (Files.exists(configPath)) {
            try {
               String shaderPackName = Files.newBufferedReader(configPath).readLine().split("shaderpack: ", 2)[1];
               return new ConfiguredShaderPack(shaderPackName, new ConfiguredShaderPack.ShaderPackTxtConfig(configPath.getFileName().toString()));
            } catch (Exception var4) {
               return new ConfiguredShaderPack(rawShaderPackName, new ConfiguredShaderPack.ShaderPackTxtConfig(configPath.getFileName().toString()));
            }
         } else {
            return new ConfiguredShaderPack(rawShaderPackName, ConfiguredShaderPack.ShaderPackConfig.EMPTY);
         }
      }
   }

   @Override
   public void subscribe(IEventBus gameBus, IEventBus modBus) {
      gameBus.register(ShaderCommand.class);
   }

   @Override
   public void subscribeClient(IEventBus gameBus, IEventBus modBus) {
      // Shader integration disabled in the offline build.
   }

   @Generated
   public static boolean isEnabled() {
      return enabled;
   }
}
