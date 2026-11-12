package com.storyteam.storytelling.system.dialog;

import net.minecraft.network.chat.Component;

public class Answer {
   protected Component answerText = Component.empty();
   protected boolean locked = true;
   protected Dialog nextDialog = null;
   protected Runnable action;
   protected boolean centred = false;
   protected DialogEmotion emotion = null;

   public Answer setText(Component component) {
      this.answerText = component;
      return this;
   }

   public Answer setText(String component) {
      return this.setText(Component.literal(component));
   }

   public Answer setNextDialog(Dialog next) {
      this.nextDialog = next;
      return this;
   }

   public Answer setOnPressAction(Runnable action) {
      this.action = action;
      return this;
   }

   public Answer locked(boolean locked) {
      this.locked = locked;
      return this;
   }

   public Answer centred(boolean centred) {
      this.centred = centred;
      return this;
   }

   public Answer setEmotion(DialogEmotion emotion) {
      this.emotion = emotion;
      return this;
   }
}
