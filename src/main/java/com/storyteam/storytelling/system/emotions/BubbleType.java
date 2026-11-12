package com.storyteam.storytelling.system.emotions;

import net.minecraft.resources.ResourceLocation;
import org.zeith.hammerlib.util.mcf.Resources;

public enum BubbleType {
   SPEECH("speech_bubble_left", "speech_bubble_middle", "speech_bubble_right", "speech_bubble", "speech_bubble_tail"),
   THINK("think_bubble_left", "think_bubble_middle", "think_bubble_right", "think_bubble", "think_bubble_tail");

   public final ResourceLocation LEFT;
   public final ResourceLocation MID;
   public final ResourceLocation RIGHT;
   public final ResourceLocation FULL;
   public final ResourceLocation TAIL;

   private BubbleType(String left, String mid, String right, String full, String tail) {
      this.LEFT = Resources.location("storytelling", "textures/system/emotions/" + left + ".png");
      this.MID = Resources.location("storytelling", "textures/system/emotions/" + mid + ".png");
      this.RIGHT = Resources.location("storytelling", "textures/system/emotions/" + right + ".png");
      this.FULL = Resources.location("storytelling", "textures/system/emotions/" + full + ".png");
      this.TAIL = Resources.location("storytelling", "textures/system/emotions/" + tail + ".png");
   }
}
