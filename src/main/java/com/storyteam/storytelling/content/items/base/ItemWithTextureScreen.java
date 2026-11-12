package com.storyteam.storytelling.content.items.base;

import com.storyteam.storytelling.net.core.OpenImagePacket;
import lombok.Generated;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ItemWithTextureScreen extends ItemWithHoverText {
   private final ResourceLocation texture;
   private boolean screenLocked = false;

   public ItemWithTextureScreen(Properties pProperties, ResourceLocation texture) {
      super(pProperties, Component.empty());
      this.texture = texture;
   }

   public ItemWithTextureScreen(Properties pProperties, ResourceLocation texture, Component... description) {
      super(pProperties, description);
      this.texture = texture;
   }

   public ItemWithTextureScreen(Properties pProperties, ResourceLocation texture, String... description) {
      super(pProperties, description);
      this.texture = texture;
   }

   @NotNull
   public InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, Player pPlayer, @NotNull InteractionHand pUsedHand) {
      ItemStack itemstack = pPlayer.getItemInHand(pUsedHand);
      if (this.screenLocked) {
         return InteractionResultHolder.pass(itemstack);
      } else {
         if (this.texture != null) {
            OpenImagePacket.open(pPlayer, this.texture);
         }

         return InteractionResultHolder.success(itemstack);
      }
   }

   @Generated
   public void setScreenLocked(boolean screenLocked) {
      this.screenLocked = screenLocked;
   }

   @Generated
   public boolean isScreenLocked() {
      return this.screenLocked;
   }
}
