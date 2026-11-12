package com.storyteam.storytelling.net;

import com.storyteam.storytelling.mixins._client.minecraft.ParticleAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
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
public class PacketEnginedParticle implements IPacket {
   ParticleOptions options;
   double spawnX;
   double spawnY;
   double spawnZ;
   double moveX;
   double moveY;
   double moveZ;
   float scale;
   int lifetime;
   float r;
   float g;
   float b;
   float alpha;

   public PacketEnginedParticle() {
   }

   public PacketEnginedParticle(
      ParticleOptions options,
      double spawnX,
      double spawnY,
      double spawnZ,
      double moveX,
      double moveY,
      double moveZ,
      float scale,
      int lifetime,
      float r,
      float g,
      float b,
      float alpha
   ) {
      this.options = options;
      this.spawnX = spawnX;
      this.spawnY = spawnY;
      this.spawnZ = spawnZ;
      this.moveX = moveX;
      this.moveY = moveY;
      this.moveZ = moveZ;
      this.scale = scale;
      this.lifetime = lifetime;
      this.r = r;
      this.g = g;
      this.b = b;
      this.alpha = alpha;
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
      buf.writeFloat(this.scale);
      buf.writeInt(this.lifetime);
      buf.writeFloat(this.r);
      buf.writeFloat(this.g);
      buf.writeFloat(this.b);
      buf.writeFloat(this.alpha);
   }

   public void read(FriendlyByteBuf buf) {
      this.options = this.readParticle(buf, (ParticleType<ParticleOptions>)buf.readRegistryId());
      this.spawnX = buf.readDouble();
      this.spawnY = buf.readDouble();
      this.spawnZ = buf.readDouble();
      this.moveX = buf.readDouble();
      this.moveY = buf.readDouble();
      this.moveZ = buf.readDouble();
      this.scale = buf.readFloat();
      this.lifetime = buf.readInt();
      this.r = buf.readFloat();
      this.g = buf.readFloat();
      this.b = buf.readFloat();
      this.alpha = buf.readFloat();
   }

   private <T extends ParticleOptions> T readParticle(FriendlyByteBuf pBuffer, ParticleType<T> pParticleType) {
      return (T)pParticleType.getDeserializer().fromNetwork(pParticleType, pBuffer);
   }

   @OnlyIn(Dist.CLIENT)
   public void clientExecute(PacketContext ctx) {
      ClientLevel level = Minecraft.getInstance().level;
      if (level != null) {
         Particle particle = Minecraft.getInstance()
            .particleEngine
            .createParticle(this.options, this.spawnX, this.spawnY, this.spawnZ, this.moveX, this.moveY, this.moveZ);
         if (particle != null) {
            particle.scale(this.scale);
            particle.setLifetime(this.lifetime);
            particle.setColor(this.r, this.g, this.b);
            ((ParticleAccessor)particle).setAlpha(this.alpha);
         }
      }
   }
}
