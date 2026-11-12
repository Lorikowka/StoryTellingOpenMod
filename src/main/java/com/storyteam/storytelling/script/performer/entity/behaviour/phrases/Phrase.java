package com.storyteam.storytelling.script.performer.entity.behaviour.phrases;

import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.util.data.EntityContext;
import java.util.HashMap;
import java.util.function.Consumer;
import lombok.Generated;
import net.minecraft.network.chat.Component;
import org.zeith.hammeranims.api.animation.IAnimationSource;
import org.zeith.hammeranims.api.animsys.ConfiguredAnimation;

public class Phrase<T extends AnimatedEntity> {
   private ConfiguredAnimation emotion;
   private boolean lookOnPlayer;
   private ConfiguredAnimation action;
   private final Component phrase;
   private final HashMap<String, ConfiguredAnimation> others = new HashMap<>();
   private Phrase<T> next;
   private int delay = -1;
   private Consumer<EntityContext<T>> runnable = context -> {
   };

   public Phrase(String phrase) {
      this.phrase = Component.literal(phrase);
   }

   public Phrase(Component phrase) {
      this.phrase = phrase;
   }

   public Phrase<T> then(Consumer<EntityContext<T>> runnable) {
      this.getCurrentPhrase().runnable = runnable;
      return this;
   }

   public Phrase<T> addEmotion(ConfiguredAnimation emotion) {
      this.getCurrentPhrase().emotion = emotion;
      return this;
   }

   public Phrase<T> addEmotion(IAnimationSource emotion) {
      this.getCurrentPhrase().emotion = emotion.configure();
      return this;
   }

   public Phrase<T> addAction(ConfiguredAnimation action) {
      this.getCurrentPhrase().action = action;
      return this;
   }

   public Phrase<T> addAction(IAnimationSource action) {
      this.getCurrentPhrase().action = action.configure();
      return this;
   }

   public Phrase<T> addAnotherAnimation(String layer, ConfiguredAnimation animation) {
      this.getCurrentPhrase().others.put(layer, animation);
      return this;
   }

   public Phrase<T> addAnotherAnimation(String layer, IAnimationSource action) {
      this.getCurrentPhrase().others.put(layer, action.configure());
      return this;
   }

   public Phrase<T> thenNextPhrase(String phrase) {
      this.getCurrentPhrase().next = new Phrase<>(phrase);
      return this;
   }

   public Phrase<T> thenNextPhrase(Component phrase) {
      this.getCurrentPhrase().next = new Phrase<>(phrase);
      return this;
   }

   public Phrase<T> lookOnPlayer() {
      this.getCurrentPhrase().lookOnPlayer = true;
      return this;
   }

   public Phrase<T> setDelay(int delay) {
      this.getCurrentPhrase().delay = delay;
      return this;
   }

   private Phrase<T> getCurrentPhrase() {
      Phrase<T> current = this;

      while (current.hasNext()) {
         current = current.next;
      }

      return current;
   }

   public boolean hasNext() {
      return this.next != null;
   }

   @Generated
   public ConfiguredAnimation getEmotion() {
      return this.emotion;
   }

   @Generated
   public boolean isLookOnPlayer() {
      return this.lookOnPlayer;
   }

   @Generated
   public ConfiguredAnimation getAction() {
      return this.action;
   }

   @Generated
   public Component getPhrase() {
      return this.phrase;
   }

   @Generated
   public HashMap<String, ConfiguredAnimation> getOthers() {
      return this.others;
   }

   @Generated
   public Phrase<T> getNext() {
      return this.next;
   }

   @Generated
   public int getDelay() {
      return this.delay;
   }

   @Generated
   public Consumer<EntityContext<T>> getRunnable() {
      return this.runnable;
   }
}
