package com.storyteam.storytelling.client.screen;

import com.storyteam.storytelling.client.screen.base.IAutoScaled;
import com.storyteam.storytelling.net.core.OpenImagePacket;
import com.storyteam.storytelling.util.java.PngSizeInfo;
import java.io.IOException;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.zeith.hammerlib.net.Network;

public class ImageScreen extends Screen implements IAutoScaled {
   public final ResourceLocation tx;
   protected final int imageWidth;
   protected final int imageHeight;

   public ImageScreen(ResourceLocation tx) {
      super(Component.empty());
      this.tx = tx;
      int w = 1024;
      int h = 1024;

      try {
         PngSizeInfo inf = PngSizeInfo.get(tx);
         w = inf.getPngWidth();
         h = inf.getPngHeight();
      } catch (IOException var5) {
         var5.printStackTrace();
      }

      this.imageWidth = w;
      this.imageHeight = h;
   }

   public void render(GuiGraphics pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
      int width = this.imageWidth;
      int height = this.imageHeight;
      if (width > this.width) {
         height = (int)((float)height * ((float)this.width / (float)width));
         width = this.width;
      }

      if (height > this.height) {
         width = (int)((float)width * ((float)this.height / (float)height));
         height = this.height;
      }

      int x = (this.width - width) / 2;
      int y = (this.height - height) / 2;
      pPoseStack.blit(this.tx, x, y, width, height, 0.0F, 0.0F, this.width, this.height, this.width, this.height);
   }

   public void onClose() {
      super.onClose();
      Network.sendToServer(new OpenImagePacket.ImageScreenClosedPacket(this.tx));
   }

   public boolean isPauseScreen() {
      return false;
   }

   public ResourceLocation getTx() {
      return this.tx;
   }
}
