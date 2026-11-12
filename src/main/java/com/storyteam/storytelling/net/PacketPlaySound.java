package com.storyteam.storytelling.net;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.Validate;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class PacketPlaySound implements IPacket {
   private Vec3 pos;
   private SoundEvent sound;
   private SoundSource source;
   private float volume;
   private float pitch;

   public PacketPlaySound() {
   }

   public PacketPlaySound(Vec3 pos, SoundEvent pSound, SoundSource pSource, float pVolume, float pPitch) {
      Validate.notNull(pSound, "sound", new Object[0]);
      this.pos = pos;
      this.sound = pSound;
      this.source = pSource;
      this.volume = pVolume;
      this.pitch = pPitch;
   }

   public void write(FriendlyByteBuf buf) {
      buf.writeDouble(this.pos.x).writeDouble(this.pos.y).writeDouble(this.pos.z);
      buf.writeResourceLocation(this.sound.getLocation());
      buf.writeEnum(this.source);
      buf.writeFloat(this.volume);
      buf.writeFloat(this.pitch);
   }

   public void read(FriendlyByteBuf buf) {
      this.pos = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
      ResourceLocation snd = buf.readResourceLocation();
      this.sound = (SoundEvent)ForgeRegistries.SOUND_EVENTS.getValue(snd);
      if (this.sound == null) {
         this.sound = SoundEvent.createVariableRangeEvent(snd);
      }

      this.source = (SoundSource)buf.readEnum(SoundSource.class);
      this.volume = buf.readFloat();
      this.pitch = buf.readFloat();
   }

   @OnlyIn(Dist.CLIENT)
   public void clientExecute(PacketContext ctx) {
      ClientLevel w = Minecraft.getInstance().level;
      if (w != null) {
         w.playLocalSound(this.pos.x, this.pos.y, this.pos.z, this.sound, this.source, this.volume, this.pitch, false);
      }
   }
}
