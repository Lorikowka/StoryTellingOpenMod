package com.storyteam.storytelling.system.clothes;

import com.storyteam.storytelling.system.clothes.entity.VanitySlot;
import com.storyteam.storytelling.system.clothes.options.VanityModelOptions;
import com.storyteam.storytelling.system.clothes.options.VanityOverride;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import lombok.Generated;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegisterEvent;
import org.zeith.hammeranims.api.HammerAnimationsApi;
import org.zeith.hammeranims.api.HammerAnimationsApi.Keys;
import org.zeith.hammeranims.api.geometry.IGeometryContainer;
import org.zeith.hammerlib.api.fml.ICustomRegistrar;
import org.zeith.hammerlib.util.mcf.Resources;

public class VanityModels implements ICustomRegistrar {
   public static final ResourceKey<Registry<IGeometryContainer>> GEOMETRY_KEY = Keys.GEOMETRY_CONTAINERS;
   protected final Map<VanitySlot, IGeometryContainer> rootModels = new HashMap<>();
   protected final Map<VanitySlot, Map<IGeometryContainer, IGeometryContainer>> modelOverrides = new HashMap<>();
   protected final Map<String, IGeometryContainer> registers = new HashMap<>();
   protected final Map<IGeometryContainer, String> overrideNames = new HashMap<>();
   private final Map<String, VanityOverride> rawOverrides;
   private final VanityModelOptions options;

   public VanityModels(VanityModelOptions options) {
      this.options = options;
      this.rawOverrides = options.getModelOverrides();

      for (VanitySlot slot : options.getParts().slots()) {
         this.addSlot(slot);
      }
   }

   public IGeometryContainer getRootModel(VanitySlot slot) {
      return this.rootModels.get(slot);
   }

   public Map<IGeometryContainer, IGeometryContainer> getOverrides(VanitySlot slot) {
      return this.modelOverrides.getOrDefault(slot, Map.of());
   }

   protected void addSlot(VanitySlot slot) {
      if (!this.rootModels.containsKey(slot)) {
         this.rootModels.put(slot, IGeometryContainer.create());

         for (VanityOverride override : this.options.getModelOverrides().values()) {
            String n = override.getOverrideName();
            IGeometryContainer m = override.getEntityModel();
            IGeometryContainer geom = IGeometryContainer.create();
            this.registers.put(n + "/" + slot.getSerializedName(), geom);
            this.modelOverrides.computeIfAbsent(slot, ff -> new HashMap<>()).put(m, geom);
            this.overrideNames.put(geom, n);
         }
      }
   }

   public void performRegister(RegisterEvent event, ResourceLocation id) {
      if (event.getRegistryKey().equals(GEOMETRY_KEY)) {
         IForgeRegistry<IGeometryContainer> reg = HammerAnimationsApi.geometries();
         this.rootModels.forEach((slot, model) -> this.registerGeom(reg, id, slot.getSerializedName(), model));

         for (Entry<String, IGeometryContainer> e : this.registers.entrySet()) {
            this.registerGeom(reg, id, e.getKey(), e.getValue());
         }
      }
   }

   private ResourceLocation subId(ResourceLocation baseId, String thing) {
      return Resources.location(baseId.getNamespace(), baseId.getPath() + "/" + thing);
   }

   private void registerGeom(IForgeRegistry<IGeometryContainer> reg, ResourceLocation id, String sub, IGeometryContainer ctr) {
      if (ctr != null) {
         reg.register(this.subId(id, sub), ctr);
      }
   }

   @Generated
   public Map<String, VanityOverride> getRawOverrides() {
      return this.rawOverrides;
   }
}
