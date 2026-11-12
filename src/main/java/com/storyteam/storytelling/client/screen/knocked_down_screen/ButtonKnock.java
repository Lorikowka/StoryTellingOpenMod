package com.storyteam.storytelling.client.screen.knocked_down_screen;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Button.Builder;
import net.minecraft.client.gui.components.Button.CreateNarration;
import net.minecraft.client.gui.components.Button.OnPress;
import net.minecraft.network.chat.Component;

public class ButtonKnock extends Button {
   protected ButtonKnock(int pX, int pY, int pWidth, int pHeight, Component pMessage, OnPress pOnPress, CreateNarration pCreateNarration) {
      super(pX, pY, pWidth, pHeight, pMessage, pOnPress, pCreateNarration);
   }

   protected ButtonKnock(Builder builder) {
      super(builder);
   }
}
