package com.storyteam.storytelling.mixins._client.minecraft;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.storyteam.storytelling.system.clothes.client.HumanoidBedrockModel;
import com.storyteam.storytelling.system.clothes.client.VanityArmRenderingHelper;
import com.storyteam.storytelling.system.clothes.item.ItemVanityArmor;
import java.util.Locale;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.zeith.hammerlib.util.mcf.Resources;

@Mixin({PlayerRenderer.class})
public abstract class PlayerRendererMixin extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
   @Unique
   private static final Map<String, ResourceLocation> ARMOR_LOCATION_CACHE = Maps.newHashMap();

   public PlayerRendererMixin(Context pContext, PlayerModel<AbstractClientPlayer> pModel, float pShadowRadius) {
      super(pContext, pModel, pShadowRadius);
   }

   @Inject(
      method = {"renderHand"},
      at = {@At("TAIL")}
   )
   private void ST_renderHand_Vanity(
      PoseStack pMatrixStack,
      MultiBufferSource pBuffer,
      int pCombinedLight,
      AbstractClientPlayer pPlayer,
      ModelPart pRendererArm,
      ModelPart pRendererArmwear,
      CallbackInfo ci
   ) {
      HumanoidArm arm = pRendererArm == ((PlayerModel)this.model).leftArm ? HumanoidArm.LEFT : HumanoidArm.RIGHT;
      this.ST_renderArmorPieceFirstPerson(arm, pMatrixStack, pBuffer, pPlayer, VanityArmRenderingHelper.HEAD_PIECE, pCombinedLight);
      this.ST_renderArmorPieceFirstPerson(arm, pMatrixStack, pBuffer, pPlayer, VanityArmRenderingHelper.CHEST_PIECE, pCombinedLight);
      this.ST_renderArmorPieceFirstPerson(arm, pMatrixStack, pBuffer, pPlayer, VanityArmRenderingHelper.LEGS_PIECE, pCombinedLight);
      this.ST_renderArmorPieceFirstPerson(arm, pMatrixStack, pBuffer, pPlayer, VanityArmRenderingHelper.FEET_PIECE, pCombinedLight);
   }

   @Unique
   private void ST_renderArmorPieceFirstPerson(
      HumanoidArm arm, PoseStack pPoseStack, MultiBufferSource pBuffer, AbstractClientPlayer pLivingEntity, ItemStack itemstack, int pPackedLight
   ) {
      if (itemstack.getItem() instanceof ItemVanityArmor vi) {
         HumanoidBedrockModel var10 = vi.getModel();
         ((PlayerModel)this.getModel()).copyPropertiesTo(var10);
         boolean foil = itemstack.hasFoil();
         var10.prepareFirstPerson(arm);
         this.ST_renderModel(
            pPoseStack, pBuffer, pPackedLight, foil, var10, 1.0F, 1.0F, 1.0F, this.ST_getArmorResource(pLivingEntity, itemstack, vi.getEquipmentSlot(), null)
         );
      }
   }

   @Unique
   private ResourceLocation ST_getArmorResource(Entity entity, ItemStack stack, EquipmentSlot slot, @Nullable String type) {
      ArmorItem item = (ArmorItem)stack.getItem();
      String texture = item.getMaterial().getName();
      String domain = "minecraft";
      int idx = texture.indexOf(58);
      if (idx != -1) {
         domain = texture.substring(0, idx);
         texture = texture.substring(idx + 1);
      }

      String s1 = String.format(
         Locale.ROOT,
         "%s:textures/models/armor/%s_layer_%d%s.png",
         domain,
         texture,
         this.ST_usesInnerModel(slot) ? 2 : 1,
         type == null ? "" : String.format(Locale.ROOT, "_%s", type)
      );
      s1 = ForgeHooksClient.getArmorTexture(entity, stack, s1, slot, type);
      ResourceLocation resourcelocation = ARMOR_LOCATION_CACHE.get(s1);
      if (resourcelocation == null) {
         resourcelocation = Resources.location(s1);
         ARMOR_LOCATION_CACHE.put(s1, resourcelocation);
      }

      return resourcelocation;
   }

   @Unique
   private boolean ST_usesInnerModel(EquipmentSlot pSlot) {
      return pSlot == EquipmentSlot.LEGS;
   }

   @Unique
   private void ST_renderModel(
      PoseStack pPoseStack,
      MultiBufferSource pBuffer,
      int pPackedLight,
      boolean foil,
      Model model,
      float p_117114_,
      float p_117115_,
      float p_117116_,
      ResourceLocation armorResource
   ) {
      VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(pBuffer, RenderType.armorCutoutNoCull(armorResource), false, foil);
      model.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, p_117114_, p_117115_, p_117116_, 1.0F);
   }
}
