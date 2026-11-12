package com.storyteam.storytelling.content.items.base;

import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.api.annotations.client.WithScreen;
import java.lang.reflect.Constructor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class ItemWithGui extends ItemWithHoverText implements WithScreen.IScreenTypeReceiver {
   private Class<?> screenType;

   public ItemWithGui(Properties pProperties, Component... description) {
      super(pProperties, description);
   }

   public ItemWithGui(Properties pProperties, String... description) {
      super(pProperties, description);
   }

   @NotNull
   public InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, @NotNull Player pPlayer, @NotNull InteractionHand pUsedHand) {
      if (pLevel.isClientSide) {
         this.openScreen();
      }

      return super.use(pLevel, pPlayer, pUsedHand);
   }

   @OnlyIn(Dist.CLIENT)
   public void openScreen() {
      try {
         Constructor<?> ctor = this.screenType.getDeclaredConstructor();
         if (ctor.trySetAccessible()) {
            Minecraft.getInstance().setScreen((Screen)ctor.newInstance());
         }
      } catch (Exception var2) {
         Storytelling.LOG.error("Failed to create screen {}!", this.screenType, var2);
      }
   }

   @Override
   public void acceptScreenAnnotation(Class<?> type) {
      this.screenType = type;
   }
}
