package com.storyteam.storytelling.system.particle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.storyteam.storytelling.util.Color;
import com.storyteam.storytelling.util.Necr0manthUtils;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.model.data.ModelData;

public class TranslucentParticle extends BaseParticle<TranslucentParticle> implements IColoredParticle {
   public float radius;
   public Color color;
   public float u0;
   public float v0;
   public float u1;
   public float v1;

   public TranslucentParticle(Vec3 pos, float radius, Color color, TextureAtlasSprite sprite) {
      this.pos = pos;
      this.radius = radius;
      this.color = color;
      this.u0 = sprite.getU0();
      this.u1 = sprite.getU1();
      this.v0 = sprite.getV0();
      this.v1 = sprite.getV1();
   }

   public TranslucentParticle(Vec3 pos, float radius, Color color, float u0, float v0, float u1, float v1) {
      this.pos = pos;
      this.radius = radius;
      this.color = color;
   }

   public TranslucentParticle(Vec3 pos, float radius, Color color, ItemStack pStack) {
      this.pos = pos;
      this.radius = radius;
      this.color = color;
      BakedModel model = Minecraft.getInstance().getItemRenderer().getModel(pStack, Minecraft.getInstance().level, null, 0);
      TextureAtlasSprite sprite = model.getOverrides().resolve(model, pStack, Minecraft.getInstance().level, null, 0).getParticleIcon(ModelData.EMPTY);
      float uo = Necr0manthUtils.random.nextFloat() * 3.0F;
      float vo = Necr0manthUtils.random.nextFloat() * 3.0F;
      this.u0 = sprite.getU((double)((uo + 1.0F) / 4.0F * 16.0F));
      this.u1 = sprite.getU((double)(uo / 4.0F * 16.0F));
      this.v0 = sprite.getV((double)(vo / 4.0F * 16.0F));
      this.v1 = sprite.getV((double)((vo + 1.0F) / 4.0F * 16.0F));
   }

   public final void render(PoseStack poseStack) {
      Vec3 d = this.pos.subtract(Minecraft.getInstance().gameRenderer.getMainCamera().position);
      Vec3 d1 = d.cross(new Vec3(0.0, 1.0, 0.0)).normalize();
      Vec3 d2 = d.cross(d1).normalize();
      Vec3 vv1 = d.add(d1.scale((double)(-this.radius))).add(d2.scale((double)(-this.radius)));
      Vec3 vv2 = d.add(d1.scale((double)(-this.radius))).add(d2.scale((double)this.radius));
      Vec3 vv3 = d.add(d1.scale((double)this.radius)).add(d2.scale((double)this.radius));
      Vec3 vv4 = d.add(d1.scale((double)this.radius)).add(d2.scale((double)(-this.radius)));
      Minecraft.getInstance()
         .renderBuffers()
         .bufferSource()
         .getBuffer(RenderType.translucent())
         .vertex(poseStack.last().pose(), (float)vv1.x, (float)vv1.y, (float)vv1.z)
         .color(this.color.getRed(), this.color.getGreen(), this.color.getBlue(), this.color.getAlpha())
         .uv(this.u0, this.v0)
         .overlayCoords(15)
         .uv2(15)
         .normal(0.0F, 0.0F, 1.0F)
         .endVertex();
      Minecraft.getInstance()
         .renderBuffers()
         .bufferSource()
         .getBuffer(RenderType.translucent())
         .vertex(poseStack.last().pose(), (float)vv2.x, (float)vv2.y, (float)vv2.z)
         .color(this.color.getRed(), this.color.getGreen(), this.color.getBlue(), this.color.getAlpha())
         .uv(this.u0, this.v1)
         .overlayCoords(15)
         .uv2(15)
         .normal(0.0F, 0.0F, 1.0F)
         .endVertex();
      Minecraft.getInstance()
         .renderBuffers()
         .bufferSource()
         .getBuffer(RenderType.translucent())
         .vertex(poseStack.last().pose(), (float)vv3.x, (float)vv3.y, (float)vv3.z)
         .color(this.color.getRed(), this.color.getGreen(), this.color.getBlue(), this.color.getAlpha())
         .uv(this.u1, this.v1)
         .overlayCoords(15)
         .uv2(15)
         .normal(0.0F, 0.0F, 1.0F)
         .endVertex();
      Minecraft.getInstance()
         .renderBuffers()
         .bufferSource()
         .getBuffer(RenderType.translucent())
         .vertex(poseStack.last().pose(), (float)vv4.x, (float)vv4.y, (float)vv4.z)
         .color(this.color.getRed(), this.color.getGreen(), this.color.getBlue(), this.color.getAlpha())
         .uv(this.u1, this.v0)
         .overlayCoords(15)
         .uv2(15)
         .normal(0.0F, 0.0F, 1.0F)
         .endVertex();
   }

   public TranslucentParticle setSpeed(Vec3 speed) {
      this.speed = speed;
      return this;
   }

   public TranslucentParticle setLifetime(float maxLifetime) {
      this.maxLifetime = maxLifetime;
      return this;
   }

   public static Predicate<TranslucentParticle> checkCollisions(float tickTime) {
      return particle -> {
         Vec3 nextPos = particle.pos.add(particle.speed.scale((double)tickTime));
         BlockPos blockPos = new BlockPos(Mth.floor(nextPos.x), Mth.floor(nextPos.y), Mth.floor(nextPos.z));

         for (AABB aabb : Minecraft.getInstance().level.getBlockState(blockPos).getShape(Minecraft.getInstance().level, blockPos).toAabbs()) {
            if (aabb.contains(nextPos.subtract((double)Mth.floor(nextPos.x), (double)Mth.floor(nextPos.y), (double)Mth.floor(nextPos.z)))) {
               particle.speed = Vec3.ZERO;
               return false;
            }
         }

         return false;
      };
   }

   public static BiConsumer<TranslucentParticle, Float> renderTickAcceleration(Vec3 acceleration) {
      return (particle, dt) -> particle.setSpeed(particle.speed.add(acceleration.scale((double)dt.floatValue())));
   }

   @Override
   public Color getColor() {
      return this.color;
   }

   @Override
   public void setColor(Color color) {
      this.color = color;
   }
}
