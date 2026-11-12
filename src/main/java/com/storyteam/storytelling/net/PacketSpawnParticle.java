package com.storyteam.storytelling.net;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class PacketSpawnParticle implements IPacket {
   ParticleOptions options;
   double spawnX;
   double spawnY;
   double spawnZ;
   double moveX;
   double moveY;
   double moveZ;

   public PacketSpawnParticle() {
   }

   public PacketSpawnParticle(ParticleOptions options, double spawnX, double spawnY, double spawnZ, double moveX, double moveY, double moveZ) {
      this.options = options;
      this.spawnX = spawnX;
      this.spawnY = spawnY;
      this.spawnZ = spawnZ;
      this.moveX = moveX;
      this.moveY = moveY;
      this.moveZ = moveZ;
   }

   public void write(FriendlyByteBuf buf) {
      buf.writeRegistryId(ForgeRegistries.PARTICLE_TYPES, this.options.getType());
      this.options.writeToNetwork(buf);
      buf.writeDouble(this.spawnX);
      buf.writeDouble(this.spawnY);
      buf.writeDouble(this.spawnZ);
      buf.writeDouble(this.moveX);
      buf.writeDouble(this.moveY);
      buf.writeDouble(this.moveZ);
   }

   public void read(FriendlyByteBuf buf) {
      ParticleType<?> particletype = (ParticleType<?>)buf.readRegistryId();
      this.options = this.readParticle(buf, (ParticleType<ParticleOptions>)particletype);
      this.spawnX = buf.readDouble();
      this.spawnY = buf.readDouble();
      this.spawnZ = buf.readDouble();
      this.moveX = buf.readDouble();
      this.moveY = buf.readDouble();
      this.moveZ = buf.readDouble();
   }

   private <T extends ParticleOptions> T readParticle(FriendlyByteBuf pBuffer, ParticleType<T> pParticleType) {
      return (T)pParticleType.getDeserializer().fromNetwork(pParticleType, pBuffer);
   }

   @OnlyIn(Dist.CLIENT)
   public void clientExecute(PacketContext ctx) {
      ClientLevel level = Minecraft.getInstance().level;
      if (level != null) {
         level.addParticle(this.options, true, this.spawnX, this.spawnY, this.spawnZ, this.moveX, this.moveY, this.moveZ);
      }
   }
}
