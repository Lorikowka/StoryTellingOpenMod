package com.storyteam.storytelling.system.rts.units.api;

import com.storyteam.storytelling.system.rts.storage.Identifiable;
import com.storyteam.storytelling.system.rts.storage.StorageType;
import com.storyteam.storytelling.system.rts.storage.Storages;
import lombok.Generated;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.RegisterEvent;
import org.zeith.hammerlib.api.fml.ICustomRegistrar;

public class RTSUnitConfig<T extends AnimatedUnit> implements ICustomRegistrar, Identifiable<ResourceLocation> {
   private ResourceLocation id;
   private final EntityType<T> entity;
   private final boolean defaultUnit;

   public void performRegister(RegisterEvent event, ResourceLocation id) {
      event.register(
         Registries.ENTITY_TYPE, registerHelper -> Storages.<RTSUnitConfig, ResourceLocation>get(RTSUnitConfig.class, StorageType.MEMORY).register(id, this)
      );
   }

   public static <T extends AnimatedUnit> RTSUnitConfig.Builder<T> builder() {
      return new RTSUnitConfig.Builder<>();
   }

   @Generated
   public ResourceLocation getId() {
      return this.id;
   }

   @Generated
   public EntityType<T> getEntity() {
      return this.entity;
   }

   @Generated
   public boolean isDefaultUnit() {
      return this.defaultUnit;
   }

   @Generated
   public RTSUnitConfig(EntityType<T> entity, boolean defaultUnit) {
      this.entity = entity;
      this.defaultUnit = defaultUnit;
   }

   @Generated
   public void setId(ResourceLocation id) {
      this.id = id;
   }

   public static class Builder<T extends AnimatedUnit> {
      private EntityType<T> entity;
      private boolean defaultUnit;

      public RTSUnitConfig.Builder<T> entity(EntityType<T> entity) {
         this.entity = entity;
         return this;
      }

      public RTSUnitConfig.Builder<T> defaultUnit(boolean defaultUnit) {
         this.defaultUnit = defaultUnit;
         return this;
      }

      public RTSUnitConfig<T> build() {
         return new RTSUnitConfig<>(this.entity, this.defaultUnit);
      }
   }
}
