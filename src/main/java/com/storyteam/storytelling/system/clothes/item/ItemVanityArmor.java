package com.storyteam.storytelling.system.clothes.item;

import com.google.common.base.Suppliers;
import com.storyteam.storytelling.TabsST;
import com.storyteam.storytelling.content.items.ItemEntityOnHand;
import com.storyteam.storytelling.init.items.VanityArmorST;
import com.storyteam.storytelling.system.clothes.ArmorGeometryConfiguration;
import com.storyteam.storytelling.system.clothes.VanitySet;
import com.storyteam.storytelling.system.clothes.client.HumanoidBedrockModel;
import com.storyteam.storytelling.system.clothes.client.ItemVanityArmorRenderer;
import com.storyteam.storytelling.system.clothes.entity.VanitySlot;
import java.util.function.Consumer;
import java.util.function.Supplier;
import lombok.Generated;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item.Properties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammeranims.api.geometry.model.RenderData;
import org.zeith.hammerlib.api.items.ITabItem;
import org.zeith.hammerlib.core.adapter.TagAdapter;

public class ItemVanityArmor extends ArmorItem implements ITabItem {
   protected final ArmorGeometryConfiguration model;
   protected final VanitySlot vanitySlot;
   protected final VanitySet set;
   protected final String texture;
   protected final String name;
   protected Supplier bedrockModel;

   public ItemVanityArmor(VanitySet set, ArmorMaterial pMaterial, VanitySlot pSlot, Properties pProperties, String texture, ArmorGeometryConfiguration model) {
      super(pMaterial, pSlot.getPlayerSlot(), pProperties.stacksTo(1).defaultDurability(100));
      this.vanitySlot = pSlot;
      this.set = set;
      this.model = model;
      this.texture = texture;
      this.name = set.getOptions().getNameOverrides().get(pSlot);
      TagAdapter.bind(VanityArmorST.VANITY_TAG, new Item[]{this});
   }

   public ArmorGeometryConfiguration getModelConfiguration() {
      return this.model;
   }

   public Component getName(ItemStack pStack) {
      return (Component)(this.name != null ? Component.literal(this.name) : super.getName(pStack));
   }

   @Nullable
   public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
      return this.texture;
   }

   public void markRegistered(ResourceLocation itemId) {
      if (this.set.getOptions().isUseBuiltinItemModels()) {
         ItemEntityOnHand.afterItemRegistration(itemId);
      }
   }

   @OnlyIn(Dist.CLIENT)
   public HumanoidBedrockModel<?> getModel() {
      return (HumanoidBedrockModel<?>)this.bedrockModel.get();
   }

   @OnlyIn(Dist.CLIENT)
   public void initializeClient(Consumer<IClientItemExtensions> consumer) {
      final Supplier<HumanoidBedrockModel<?>> bedrockModel = Suppliers.memoize(() -> new HumanoidBedrockModel(this.model));
      this.bedrockModel = bedrockModel;
      consumer.accept(new IClientItemExtensions() {
         final Supplier<ItemVanityArmorRenderer> renderer = Suppliers.memoize(() -> new ItemVanityArmorRenderer(ItemVanityArmor.this));

         public BlockEntityWithoutLevelRenderer getCustomRenderer() {
            return this.renderer.get();
         }

         @NotNull
         public HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
            HumanoidBedrockModel<?> m = bedrockModel.get();
            m.contextStack = itemStack;
            return m;
         }
      });
   }

   public void setupColors(ItemStack stack, RenderData renderData) {
      renderData.red = 1.0F;
      renderData.green = 1.0F;
      renderData.blue = 1.0F;
      renderData.alpha = 1.0F;
   }

   public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
      return slotChanged;
   }

   public CreativeModeTab getItemCategory() {
      return TabsST.VANITY_TAB.tab();
   }

   @Generated
   public VanitySlot getVanitySlot() {
      return this.vanitySlot;
   }

   @Generated
   public VanitySet getSet() {
      return this.set;
   }

   @Generated
   public String getTexture() {
      return this.texture;
   }

   @Generated
   public String getName() {
      return this.name;
   }

   @Generated
   public Supplier getBedrockModel() {
      return this.bedrockModel;
   }
}
