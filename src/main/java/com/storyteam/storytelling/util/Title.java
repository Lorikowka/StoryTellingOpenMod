package com.storyteam.storytelling.util;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import org.jetbrains.annotations.Nullable;

public class Title {
   private final Component title;
   private final Component subtitle;
   private final int fadeIn;
   private final int stay;
   private final int fadeOut;
   @Nullable
   private final MobEffect effect;

   public static Title.Builder builder(Component title) {
      return new Title.Builder(title);
   }

   public static Title.Builder builder(Component title, Component subtitle) {
      return new Title.Builder(title, subtitle);
   }

   public static Title.Builder builder(String title) {
      return new Title.Builder(Component.literal(title));
   }

   public static Title.Builder builder(String title, String subtitle) {
      return new Title.Builder(Component.literal(title), Component.literal(subtitle));
   }

   public Title(Component title, Component subtitle, @Nullable MobEffect effect, int fadeIn, int stay, int fadeOut) {
      this.title = title;
      this.subtitle = subtitle;
      this.fadeIn = fadeIn;
      this.stay = stay;
      this.effect = effect;
      this.fadeOut = fadeOut;
   }

   public int getCombinedDurationTicks() {
      return this.fadeIn + this.stay + this.fadeOut;
   }

   public void send() {
      this.send(ScriptUtils.getPlayer());
   }

   public void send(ServerPlayer player) {
      player.connection.send(new ClientboundSetTitlesAnimationPacket(this.fadeIn, this.stay, this.fadeOut));
      player.connection.send(new ClientboundSetTitleTextPacket(this.title));
      if (this.subtitle != null) {
         player.connection.send(new ClientboundSetSubtitleTextPacket(this.subtitle));
      }

      if (this.effect != null) {
         player.forceAddEffect(new MobEffectInstance(this.effect, this.fadeIn + this.stay + this.fadeOut, 0, false, false, false), null);
      }
   }

   public static class Builder {
      private final Component title;
      private Component subtitle;
      private MobEffect effect;
      private int fadeIn = 10;
      private int stay = 70;
      private int fadeOut = 20;

      public Builder(Component title) {
         this.title = title;
      }

      public Builder(String title) {
         this(Component.literal(title));
      }

      public Builder(Component title, Component subtitle) {
         this(title);
         this.subtitle = subtitle;
      }

      public Builder(String title, String subtitle) {
         this(title);
         this.subtitle = Component.literal(subtitle);
      }

      public Title.Builder subtitle(Component subtitle) {
         this.subtitle = subtitle;
         return this;
      }

      public Title.Builder subtitle(String subtitle) {
         this.subtitle = Component.literal(subtitle);
         return this;
      }

      public Title.Builder times(int fadeIn, int stay, int fadeOut) {
         this.fadeIn = fadeIn;
         this.stay = stay;
         this.fadeOut = fadeOut;
         return this;
      }

      public Title.Builder effect(MobEffect effect) {
         this.effect = effect;
         return this;
      }

      public Title build() {
         return new Title(this.title, this.subtitle, this.effect, this.fadeIn, this.stay, this.fadeOut);
      }

      public void send() {
         this.build().send();
      }

      public void send(ServerPlayer player) {
         this.build().send(player);
      }
   }
}
