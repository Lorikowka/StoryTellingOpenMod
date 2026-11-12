package com.storyteam.storytelling.system.simulator.cognition_tab.client.utils;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FontUtils {
   public static List<String> wrapText(String text, float width, float scale) {
      Font font = Minecraft.getInstance().font;
      List<String> lines = new ArrayList<>();

      for (String paragraph : text.split("\n")) {
         StringBuilder currentLine = new StringBuilder();
         String[] words = paragraph.split(" ");
         int line = 0;

         for (String word : words) {
            if (line >= 2) {
               currentLine.append(" ").append(word);
            } else if (!currentLine.isEmpty()) {
               String testLine = currentLine + " " + word;
               if ((float)font.width(testLine) <= width * scale) {
                  currentLine.append(" ").append(word);
               } else {
                  lines.add(currentLine.toString());
                  currentLine = new StringBuilder(word);
               }
            } else {
               currentLine.append(word);
            }

            line++;
         }

         if (!currentLine.isEmpty()) {
            lines.add(currentLine.toString());
         }

         if (!paragraph.equals(text.split("\n")[text.split("\n").length - 1])) {
            lines.add("");
         }
      }

      return lines;
   }
}
