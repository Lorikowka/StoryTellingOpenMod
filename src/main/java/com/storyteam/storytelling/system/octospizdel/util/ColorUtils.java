package com.storyteam.storytelling.system.octospizdel.util;

import java.awt.Color;
import net.minecraft.util.Mth;

public class ColorUtils {
   public static Color blend(Color a, Color b, double t) {
      t = Mth.clamp(t, 0.0, 1.0);
      int aRed = a.getRed();
      int aGreen = a.getGreen();
      int aBlue = a.getBlue();
      int aAlpha = a.getAlpha();
      int bRed = b.getRed();
      int bGreen = b.getGreen();
      int bBlue = b.getBlue();
      int bAlpha = b.getAlpha();
      int blendedRed = Mth.lerpInt((float)t, aRed, bRed);
      int blendedGreen = Mth.lerpInt((float)t, aGreen, bGreen);
      int blendedBlue = Mth.lerpInt((float)t, aBlue, bBlue);
      int blendedAlpha = Mth.lerpInt((float)t, aAlpha, bAlpha);
      return new Color(blendedRed, blendedGreen, blendedBlue, blendedAlpha);
   }

   public static int lerpInt(int colorStart, int colorEnd, float t) {
      t = Mth.clamp(t, 0.0F, 1.0F);
      int aStart = colorStart >> 24 & 0xFF;
      int rStart = colorStart >> 16 & 0xFF;
      int gStart = colorStart >> 8 & 0xFF;
      int bStart = colorStart & 0xFF;
      int aEnd = colorEnd >> 24 & 0xFF;
      int rEnd = colorEnd >> 16 & 0xFF;
      int gEnd = colorEnd >> 8 & 0xFF;
      int bEnd = colorEnd & 0xFF;
      int a = (int)((float)aStart + t * (float)(aEnd - aStart));
      int r = (int)((float)rStart + t * (float)(rEnd - rStart));
      int g = (int)((float)gStart + t * (float)(gEnd - gStart));
      int b = (int)((float)bStart + t * (float)(bEnd - bStart));
      return a << 24 | r << 16 | g << 8 | b;
   }

   private static int clamp(int value) {
      return Mth.clamp(value, 0, 255);
   }
}
