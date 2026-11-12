package com.storyteam.storytelling.content.items;

import com.google.common.base.Suppliers;
import com.storyteam.storytelling.api.itf.IHiddenFromJeiItem;
import com.storyteam.storytelling.client.render.item.EntityOnHandItemRenderer;
import com.storyteam.storytelling.init.ItemsST;
import com.storyteam.storytelling.system.clothes.Outfit;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import lombok.Generated;
import net.minecraft.client.model.HumanoidModel.ArmPose;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PackResources.ResourceOutput;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammeranims.api.animsys.AnimationSystem;
import org.zeith.hammeranims.api.geometry.IGeometryContainer;
import org.zeith.hammerlib.api.fml.IRegisterListener;
import org.zeith.hammerlib.api.proxy.IProxy;
import org.zeith.hammerlib.client.adapter.ResourcePackAdapter;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.mcf.Resources;

public class ItemEntityOnHand extends Item implements IHiddenFromJeiItem, IRegisterListener {
   public final IGeometryContainer geometry;
   public final ResourceLocation texture;
   public final String name;
   private Supplier<Outfit> outfit = Cast.constant(Outfit.builder().build());
   protected static final Set<String> ALL_ITEMS = new HashSet<>();
   private Supplier<EntityOnHandItemRenderer> renderer;
   public Consumer<AnimationSystem> startAnimations;

   public static void setup() {
      ResourcePackAdapter.registerResourcePack(
         new PackResources() {
            @Nullable
            public IoSupplier<InputStream> getRootResource(String... pElements) {
               return null;
            }

            public IoSupplier<InputStream> getResource(PackType pType, ResourceLocation pLocation) {
               String path = pType.getDirectory() + "/" + pLocation.getNamespace() + "/" + pLocation.getPath();
               return ItemEntityOnHand.ALL_ITEMS.contains(path)
                  ? () -> new ByteArrayInputStream(
                        "{\n   \"render_type\": \"cutout\",\n   \"parent\": \"storytelling:item/builtin/ister\"\n}".getBytes(StandardCharsets.UTF_8)
                     )
                  : null;
            }

            public void listResources(PackType pPackType, String pNamespace, String pPath, ResourceOutput pResourceOutput) {
               if (pPath.equalsIgnoreCase("models")) {
                  for (String res : ItemEntityOnHand.ALL_ITEMS) {
                     String[] p = res.substring(7).split("/", 2);
                     if (p[0].equals(pNamespace)) {
                        ResourceLocation rl = Resources.location(p[0], p[1]);
                        pResourceOutput.accept(rl, this.getResource(pPackType, rl));
                     }
                  }
               }
            }

            public Set<String> getNamespaces(PackType pType) {
               return Set.of("storytelling");
            }

            @Nullable
            public <T> T getMetadataSection(MetadataSectionSerializer<T> pDeserializer) throws IOException {
               return null;
            }

            public String packId() {
               return "";
            }

            public void close() {
            }
         }
      );
   }

   public ItemEntityOnHand(IGeometryContainer geometry, ResourceLocation texture, String name, Consumer<AnimationSystem> startAnimations) {
      super(ItemsST.props().stacksTo(1));
      this.geometry = geometry;
      this.texture = texture;
      this.startAnimations = startAnimations;
      this.name = name;
      IProxy.runOn(
         Dist.CLIENT,
         () -> () -> this.renderer = Suppliers.memoize(() -> new EntityOnHandItemRenderer(geometry, texture, startAnimations, () -> this.outfit.get()))
      );
   }

   public ItemEntityOnHand withOutfit(Supplier<Outfit> outfit) {
      this.outfit = outfit;
      return this;
   }

   public void inventoryTick(@NotNull ItemStack pStack, @NotNull Level pLevel, @NotNull Entity pEntity, int pSlotId, boolean pIsSelected) {
      if (pEntity instanceof Player p) {
         if (((ItemStack)p.getInventory().offhand.get(0)).equals(pStack, false)) {
            Inventory inv = p.getInventory();
            ItemStack item = inv.getItem(inv.selected).copy();
            inv.setItem(inv.selected, pStack.copy());
            p.getInventory().offhand.set(0, item);
         }

         if (!pIsSelected) {
            if (pSlotId > 9) {
               Inventory inv = p.getInventory();
               ItemStack item = inv.getItem(inv.selected).copy();
               inv.setItem(inv.selected, pStack.copy());
               inv.setItem(pSlotId, item);
            } else {
               p.getInventory().selected = pSlotId;
            }
         }
      }

      super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
   }

   public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
      entity.setNoPickUpDelay();
      return super.onEntityItemUpdate(stack, entity);
   }

   public boolean onDroppedByPlayer(ItemStack item, Player player) {
      player.getInventory().add(item);
      return false;
   }

   public void onPostRegistered(ResourceLocation id) {
      afterItemRegistration(id);
   }

   public static void afterItemRegistration(ResourceLocation id) {
      ALL_ITEMS.add("assets/%s/models/item/%s.json".formatted(id.getNamespace(), id.getPath()));
   }

   public Component getName(ItemStack pStack) {
      return Component.literal(this.name);
   }

   public void initializeClient(Consumer<IClientItemExtensions> consumer) {
      consumer.accept(new IClientItemExtensions() {
         public BlockEntityWithoutLevelRenderer getCustomRenderer() {
            return ItemEntityOnHand.this.renderer.get();
         }

         public ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
            return ItemEntityOnHand.ClientItemEntityOnHand.ARM_POSE;
         }
      });
   }

   @Generated
   public Supplier<Outfit> getOutfit() {
      return this.outfit;
   }

   @Generated
   public Supplier<EntityOnHandItemRenderer> getRenderer() {
      return this.renderer;
   }

   public static class ClientItemEntityOnHand {
      public static final ArmPose ARM_POSE = ArmPose.create("storytelling_hold_entity", true, (model, entity, arm) -> {
         ModelPart lArm = model.leftArm;
         ModelPart rArm = model.rightArm;
         lArm.xRot = -0.61086524F;
         rArm.xRot = -0.61086524F;
      });
   }
}
