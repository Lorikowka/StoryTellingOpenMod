package com.storyteam.storytelling.system.containertier.data;

import com.storyteam.storytelling.system.containertier.storage.ContainerTierStorage;
import java.util.Arrays;
import java.util.List;
import lombok.Generated;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegisterEvent;
import org.zeith.hammerlib.api.fml.ICustomRegistrar;

public class ContainerTier implements ICustomRegistrar {
   private final List<ContainerTierElement> tierElements;

   public ContainerTier(ContainerTierElement... elements) {
      this.tierElements = List.of(elements);
   }

   public void performRegister(RegisterEvent event, ResourceLocation id) {
      event.register(Registries.CUSTOM_STAT, registerHelper -> ContainerTierStorage.addTier(this, id));
   }

   @Generated
   public static ContainerTier.ContainerTierBuilder builder() {
      return new ContainerTier.ContainerTierBuilder();
   }

   @Generated
   public List<ContainerTierElement> getTierElements() {
      return this.tierElements;
   }

   @Generated
   public static class ContainerTierBuilder {
      @Generated
      private ContainerTierElement[] elements;

      @Generated
      ContainerTierBuilder() {
      }

      @Generated
      public ContainerTier.ContainerTierBuilder elements(ContainerTierElement[] elements) {
         this.elements = elements;
         return this;
      }

      @Generated
      public ContainerTier build() {
         return new ContainerTier(this.elements);
      }

      @Generated
      @Override
      public String toString() {
         return "ContainerTier.ContainerTierBuilder(elements=" + Arrays.deepToString(this.elements) + ")";
      }
   }
}
