package com.storyteam.storytelling.system.dialog;

import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.script.story.characters.Character;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.chat.Component;
import org.zeith.hammeranims.api.animation.IAnimationSource;

public class Dialog {
   protected int scriptId;
   protected Character<? extends AnimatedEntity> owner;
   protected String talkingToId = null;
   protected List<Answer> answers = new ArrayList<>();
   protected List<TextInstance> texts = new ArrayList<>();
   protected DialogEmotion emotion = DialogEmotion.INDIFFERENCE;

   public Dialog(int script, Character<? extends AnimatedEntity> owner) {
      this.scriptId = script;
      this.owner = owner;
      this.talkingToId = owner.getReferenceId();
   }

   public Dialog() {
   }

   public Dialog addText(String text) {
      this.texts.add(new TextInstance(Component.literal(text), null));
      return this;
   }

   public Dialog addText(Component text) {
      this.texts.add(new TextInstance(text, null));
      return this;
   }

   public Dialog addText(String text, IAnimationSource animation) {
      this.texts.add(new TextInstance(Component.literal(text), animation));
      return this;
   }

   public Dialog addText(Component text, IAnimationSource animation) {
      this.texts.add(new TextInstance(text, animation));
      return this;
   }

   public Dialog setTalkingTo(String id) {
      this.talkingToId = id;
      return this;
   }

   public Dialog setTalkingTo(Character<? extends AnimatedEntity> entity) {
      this.talkingToId = entity.getReferenceId();
      return this;
   }

   public Dialog addAnswer(Answer answer) {
      this.answers.add(answer);
      return this;
   }

   public Dialog addAnswer(String answer, boolean locked) {
      return this.addAnswer(new Answer().setText(answer).locked(locked));
   }

   public Dialog addAnswer(String answer, boolean locked, DialogEmotion emotion) {
      return this.addAnswer(new Answer().setText(answer).locked(locked).setEmotion(emotion));
   }

   public Dialog addAnswer(String answer) {
      return this.addAnswer(new Answer().setText(answer).locked(true));
   }

   public Dialog addAnswer(String answer, DialogEmotion emotion) {
      return this.addAnswer(new Answer().setText(answer).locked(true).setEmotion(emotion));
   }

   public Dialog addAnswer(Component answer, boolean locked) {
      return this.addAnswer(new Answer().setText(answer).locked(locked));
   }

   public Dialog addAnswer(Component answer, boolean locked, DialogEmotion emotion) {
      return this.addAnswer(new Answer().setText(answer).locked(locked).setEmotion(emotion));
   }

   public Dialog addAnswer(Component answer) {
      return this.addAnswer(new Answer().setText(answer).locked(true));
   }

   public Dialog addAnswer(Component answer, DialogEmotion emotion) {
      return this.addAnswer(new Answer().setText(answer).locked(true).setEmotion(emotion));
   }

   public Dialog addCentredAnswer(Answer answer) {
      this.answers.add(answer.centred(true));
      return this;
   }

   public Dialog addCentredAnswer(String answer, boolean locked) {
      return this.addAnswer(new Answer().setText(answer).locked(locked).centred(true));
   }

   public Dialog addCentredAnswer(String answer, boolean locked, DialogEmotion emotion) {
      return this.addAnswer(new Answer().setText(answer).locked(locked).centred(true).setEmotion(emotion));
   }

   public Dialog addCentredAnswer(String answer) {
      return this.addAnswer(new Answer().setText(answer).locked(true).centred(true));
   }

   public Dialog addCentredAnswer(String answer, DialogEmotion emotion) {
      return this.addAnswer(new Answer().setText(answer).locked(true).centred(true).setEmotion(emotion));
   }

   public Dialog addCentredAnswer(Component answer, boolean locked) {
      return this.addAnswer(new Answer().setText(answer).locked(locked).centred(true));
   }

   public Dialog addCentredAnswer(Component answer, DialogEmotion emotion) {
      return this.addAnswer(new Answer().setText(answer).locked(true).centred(true).setEmotion(emotion));
   }

   public Dialog addCentredAnswer(Component answer) {
      return this.addAnswer(new Answer().setText(answer).locked(true).centred(true));
   }

   public Dialog setEmotion(DialogEmotion emotion) {
      this.emotion = emotion;
      return this;
   }

   public Character<? extends AnimatedEntity> getOwner() {
      return this.owner;
   }
}
