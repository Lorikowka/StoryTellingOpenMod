package com.storyteam.storytelling.system.shaders;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;
import lombok.Generated;
import net.minecraft.client.Minecraft;

public class ConfiguredShaderPack {
   public String shaderPackName;
   ConfiguredShaderPack.ShaderPackConfig config;

   @Override
   public boolean equals(Object obj) {
      if (obj instanceof ConfiguredShaderPack pack && Objects.equals(pack.shaderPackName, this.shaderPackName) && Objects.equals(pack.config, this.config)) {
         return true;
      }

      return false;
   }

   @Generated
   public ConfiguredShaderPack(String shaderPackName, ConfiguredShaderPack.ShaderPackConfig config) {
      this.shaderPackName = shaderPackName;
      this.config = config;
   }

   public static class EmptyShaderPackConfig implements ConfiguredShaderPack.ShaderPackConfig {
      @Override
      public String getConfig() {
         return "";
      }

      @Override
      public boolean equals(Object obj) {
         return obj instanceof ConfiguredShaderPack.EmptyShaderPackConfig;
      }
   }

   interface ShaderPackConfig {
      ConfiguredShaderPack.EmptyShaderPackConfig EMPTY = new ConfiguredShaderPack.EmptyShaderPackConfig();

      String getConfig();
   }

   public static class ShaderPackTxtConfig implements ConfiguredShaderPack.ShaderPackConfig {
      String fileName;

      @Override
      public String getConfig() {
         try {
            return Files.readString(Minecraft.getInstance().gameDirectory.toPath().resolve("shaderpacks").resolve(this.fileName)).split("\n", 2)[1];
         } catch (IOException var2) {
            throw new RuntimeException(var2);
         }
      }

      @Override
      public boolean equals(Object obj) {
         if (obj instanceof ConfiguredShaderPack.ShaderPackTxtConfig cfg && Objects.equals(cfg.fileName, this.fileName)) {
            return true;
         }

         return false;
      }

      @Generated
      public ShaderPackTxtConfig(String fileName) {
         this.fileName = fileName;
      }
   }
}
