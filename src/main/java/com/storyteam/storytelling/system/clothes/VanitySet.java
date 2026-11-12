package com.storyteam.storytelling.system.clothes;

import com.google.common.base.Suppliers;
import com.storyteam.storytelling.client.render.entity.def.RenderKey;
import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.system.clothes.client.VanityRenderOptions;
import com.storyteam.storytelling.system.clothes.entity.VanityInventory;
import com.storyteam.storytelling.system.clothes.entity.VanitySlot;
import com.storyteam.storytelling.system.clothes.item.IVanityArmorItemFactory;
import com.storyteam.storytelling.system.clothes.item.IVanityArmorMaterialFactory;
import com.storyteam.storytelling.system.clothes.item.ItemVanityArmor;
import com.storyteam.storytelling.system.clothes.options.IVanityTextureProvider;
import com.storyteam.storytelling.system.clothes.options.VanityOverride;
import com.storyteam.storytelling.system.clothes.options.VanityParts;
import com.storyteam.storytelling.system.clothes.options.VanitySetOptions;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Stream;
import lombok.Generated;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.ForgeRegistries.Keys;
import org.zeith.hammeranims.api.geometry.IGeometryContainer;
import org.zeith.hammerlib.api.fml.ICustomRegistrar;
import org.zeith.hammerlib.api.items.CreativeTab;
import org.zeith.hammerlib.core.adapter.CreativeTabAdapter;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.mcf.Resources;

public class VanitySet implements ICustomRegistrar {
   protected final VanitySetOptions options;
   protected final VanityModels models;
   protected final IVanityArmorItemFactory itemFactory;
   protected final IVanityArmorMaterialFactory materialFactory;
   protected final Map<VanitySlot, ItemVanityArmor> items = new HashMap<>();
   protected ArmorMaterial material;

   public VanitySet() {
      this(VanitySetOptions.builder().build());
   }

   public VanitySet(VanitySetOptions options) {
      this.options = options;
      this.models = options.getGeometry();
      this.itemFactory = options.getItemFactory();
      this.materialFactory = options.getMaterialFactory();
      VanityParts parts = options.getParts();

      for (VanitySlot slot : parts.slots()) {
         this.models.addSlot(slot);
      }

      FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
   }

   protected void clientSetup(FMLClientSetupEvent e) {
      this.items().map(Entry::getValue).forEach(v -> v.getModel());
   }

   public void performRegister(RegisterEvent event, ResourceLocation id) {
      if (event.getRegistryKey().equals(Keys.ITEMS)) {
         this.material = this.materialFactory.create(this, id);
         VanityParts parts = this.options.getParts();

         for (VanitySlot slot : parts.slots()) {
            this.items.put(slot, this.registerItem(this.items.get(slot), event, id, slot));
         }
      }
   }

   public void equip(LivingEntity entity) {
      if (!(entity instanceof AnimatedEntity he)) {
         for (VanitySlot slot : Set.of(VanitySlot.HEAD, VanitySlot.BODY, VanitySlot.LEGS, VanitySlot.FEET)) {
            ItemStack it = this.get(slot).getDefaultInstance();
            if (!it.isEmpty()) {
               entity.setItemSlot(slot.getPlayerSlot().getSlot(), it);
            }
         }
      } else {
         VanityInventory inv = he.getInventory();

         for (Entry<VanitySlot, ItemVanityArmor> e : this.items.entrySet()) {
            inv = inv.withItem(e.getKey(), e.getValue().getDefaultInstance());
         }

         he.setInventory(inv);
      }
   }

   public Stream<Entry<VanitySlot, ItemVanityArmor>> items() {
      return this.items.entrySet().stream();
   }

   public boolean has(VanitySlot slot) {
      return this.options.getParts().has(slot);
   }

   public Item get(VanitySlot slot) {
      return (Item)Cast.or(new Item[]{(Item)this.items.get(slot), Items.AIR});
   }

   private ResourceLocation subId(ResourceLocation baseId, String thing) {
      return Resources.location(baseId.getNamespace(), baseId.getPath() + "/" + thing);
   }

   private ItemVanityArmor registerItem(ItemVanityArmor prev, RegisterEvent event, ResourceLocation id, VanitySlot slot) {
      String sub = slot.getSerializedName();
      if (prev == null) {
         String baseTexture = this.options.getTextures().provideTexture(id, slot);
         IGeometryContainer ctr = this.models.getRootModel(slot);
         ResourceLocation baseTexRl = Resources.location(baseTexture + ".png");
         Map<IGeometryContainer, VanityRenderOptions> textureOverrides = new HashMap<>();
         Map<IGeometryContainer, IGeometryContainer> modelOverrides = this.models.getOverrides(slot);

         for (VanityOverride override : this.models.getRawOverrides().values()) {
            String name = override.getOverrideName();
            IVanityTextureProvider txo = override.getTextures();
            VanityRenderOptions.Builder opts = VanityRenderOptions.builder().renderer(RenderKey.armorCutoutNoCull);
            if (txo != null) {
               String tx = txo.provideTexture(Resources.location(id.getNamespace(), id.getPath() + "/" + name), slot);
               if (tx != null) {
                  opts.texture(Resources.location(tx + ".png"));
               } else {
                  opts.texture(baseTexRl);
               }
            } else {
               opts.texture(Resources.location(baseTexture + "_" + name + ".png"));
            }

            opts = this.options.getRenderOptions().apply(slot, opts);
            textureOverrides.put(override.getEntityModel(), override.getRenderOptions().apply(slot, opts).build());
         }

         VanityRenderOptions opts = this.options
            .getRenderOptions()
            .apply(slot, VanityRenderOptions.builder().texture(baseTexRl).renderer(RenderKey.armorCutoutNoCull))
            .build();
         prev = this.itemFactory
            .create(
               this,
               this.material,
               slot,
               this.options.getArmorProps().get().defaultDurability(-1),
               baseTexture + ".png",
               new ArmorGeometryConfiguration(ctr, opts, modelOverrides, textureOverrides)
            );
      }

      ResourceLocation fid = this.subId(id, sub);
      ItemVanityArmor finalPrev = prev;
      event.register(Keys.ITEMS, fid, Suppliers.memoize(() -> {
         finalPrev.markRegistered(fid);
         return finalPrev;
      }));
      CreativeTabAdapter.bindTab(prev, new CreativeTab[0]);
      return prev;
   }

   @Generated
   public VanitySetOptions getOptions() {
      return this.options;
   }
}
