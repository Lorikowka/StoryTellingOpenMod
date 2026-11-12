package com.storyteam.storytelling.client.content.overlays;

import java.util.List;
import net.minecraft.client.gui.Font;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

public record Message(String speaker, Component speakerName, Component flavor, int inTime, int time, int outTime, Component message) {
   public int getBoxHeight(List<FormattedCharSequence> lines, Font font, int maxWidth, int iconHeight, int vertMargins) {
      return Math.max(9 * lines.size(), iconHeight) + vertMargins * 2;
   }

   public int getBoxWidth(List<FormattedCharSequence> lines, Font font, int maxWidth, int iconWidth, int horMargins) {
      int maxCurrentLineWidth = 0;

      for (FormattedCharSequence line : lines) {
         maxCurrentLineWidth = Math.max(maxCurrentLineWidth, font.width(line));
      }

      return maxCurrentLineWidth + iconWidth + horMargins * 3;
   }

   public void toNetwork(FriendlyByteBuf buf) {
      buf.writeUtf(this.speaker());
      buf.writeComponent(this.speakerName());
      buf.writeComponent(this.flavor());
      buf.writeInt(this.inTime());
      buf.writeInt(this.time());
      buf.writeInt(this.outTime());
      buf.writeComponent(this.message());
   }

   public static Message fromNetwork(FriendlyByteBuf buf) {
      String icon = buf.readUtf();
      Component speakerName = buf.readComponent();
      Component flavor = buf.readComponent();
      int inTime = buf.readInt();
      int time = buf.readInt();
      int outTime = buf.readInt();
      Component message = buf.readComponent();
      return new Message(icon, speakerName, flavor, inTime, time, outTime, message);
   }
}
