package com.storyteam.storytelling.net.core;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.PacketContext;

public class ClientboundPlaySoundForPlayer implements IPacket {
   public SoundEvent soundEvent;
   public double x;
   public double y;
   public double z;
   public float volume;
   public float pitch;
   public boolean inEars;

   public ClientboundPlaySoundForPlayer(SoundEvent soundEvent, double x, double y, double z, float volume, float pitch, boolean inEars) {
      this.soundEvent = soundEvent;
      this.x = x;
      this.y = y;
      this.z = z;
      this.volume = volume;
      this.pitch = pitch;
      this.inEars = inEars;
   }

   public ClientboundPlaySoundForPlayer() {
   }

   public void write(FriendlyByteBuf buf) {
      IPacket.super.write(buf);
      ResourceLocation key = ForgeRegistries.SOUND_EVENTS.getKey(this.soundEvent);
      buf.writeResourceLocation(key);
      buf.writeDouble(this.x);
      buf.writeDouble(this.y);
      buf.writeDouble(this.z);
      buf.writeFloat(this.volume);
      buf.writeFloat(this.pitch);
      buf.writeBoolean(this.inEars);
   }

   public void read(FriendlyByteBuf buf) {
      IPacket.super.read(buf);
      ResourceLocation key = buf.readResourceLocation();
      SoundEvent event = (SoundEvent)ForgeRegistries.SOUND_EVENTS.getValue(key);
      this.soundEvent = event;
      this.x = buf.readDouble();
      this.y = buf.readDouble();
      this.z = buf.readDouble();
      this.volume = buf.readFloat();
      this.pitch = buf.readFloat();
      this.inEars = buf.readBoolean();
   }

   public void clientExecute(PacketContext ctx) {
      IPacket.super.clientExecute(ctx);
      Level level = Minecraft.getInstance().level;
      if (level != null) {
         if (!this.inEars) {
            level.playLocalSound(this.x, this.y, this.z, this.soundEvent, SoundSource.NEUTRAL, this.volume, this.pitch, false);
         } else {
            playSoundInEars(this.soundEvent, this.pitch, this.volume);
         }
      }
   }

   public static void playSoundInEars(SoundEvent soundEvent, float pitch, float volume) {
      Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(soundEvent, pitch, volume));
   }
}
