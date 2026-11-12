package com.storyteam.storytelling.system.particle;

import com.storyteam.storytelling.mixins._client.minecraft.ParticleEngineAccessor;
import com.storyteam.storytelling.util.Color;
import com.storyteam.storytelling.util.Necr0manthUtils;
import com.storyteam.storytelling.util.ParticleHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class OptimizedParticleHelper {
   public static OptimizedParticle addParticle(ParticleFactory<OptimizedParticle> factory, Vec3 pos, Vec3 speed) {
      return addParticle(factory.create(pos, speed));
   }

   public static <T extends OptimizedParticle> T addParticle(T particle) {
      OptimizedParticleEngine.instance.addParticle(particle);
      return particle;
   }

   public static void createParticleLine(ParticleFactory factory, Vec3 start, Vec3 end, float particlesPerBlock, boolean excludeFirst) {
      int particleCount = Mth.ceil(end.distanceTo(start) * (double)particlesPerBlock);

      for (int i = excludeFirst ? 1 : 0; i < particleCount; i++) {
         addParticle(factory, start.add(end.subtract(start).scale((double)i / (double)(particleCount - 1))), Vec3.ZERO);
      }
   }

   public static void createNarrowingParticleLine(
      ParticleFactory factory, Vec3 start, Vec3 end, float particlesPerBlock, boolean excludeFirst, float startSize, float endSize
   ) {
      float length = (float)start.distanceTo(end);
      float i = 0.0F;

      while (i < length) {
         float size = Mth.lerp(i / length, startSize, endSize);
         if (i != 0.0F || !excludeFirst) {
            addParticle(factory, start.add(end.subtract(start).scale((double)(i / length))), Vec3.ZERO).radius = size;
         }

         i += size / startSize / particlesPerBlock;
      }
   }

   public static void spawnAwesomeLightning(
      Vec3 begin, Vec3 end, double maxJagMultiplier, int[] particlesPerBlock, int sliceIterations, ParticleFactory... options
   ) {
      List<Vec3> locs = new ArrayList<>(List.of(begin));
      locs.addAll(ParticleHelper.getJagged(begin, end, sliceIterations, maxJagMultiplier));
      locs.add(end);

      for (int i = 1; i < locs.size(); i++) {
         double distance = locs.get(i).subtract(locs.get(i - 1)).length();

         for (int j = 0; j < options.length; j++) {
            createParticleLine(options[j], locs.get(i - 1), locs.get(i), (float)(distance * (double)particlesPerBlock[j]), true);
         }
      }
   }

   public static void spawnAwesomeRecursiveLightning(
      Vec3 begin,
      Vec3 end,
      double maxJagMultiplier,
      float fractions,
      int maxDepth,
      float scale,
      int[] particlesPerBlock,
      int sliceIterations,
      ParticleFactory<OptimizedParticle>... options
   ) {
      float[] startWidth = new float[options.length];

      for (int i = 0; i < options.length; i++) {
         startWidth[i] = options[i].create(Vec3.ZERO, Vec3.ZERO).radius;
      }

      spawnAwesomeRecursiveLightning(begin, end, maxJagMultiplier, fractions, maxDepth, 0, scale, startWidth, particlesPerBlock, sliceIterations, options);
   }

   public static void spawnAwesomeRecursiveLightning(
      Vec3 begin,
      Vec3 end,
      double maxJagMultiplier,
      float fractions,
      int maxDepth,
      int currentDepth,
      float scale,
      float[] startWidth,
      int[] particlesPerBlock,
      int sliceIterations,
      ParticleFactory... options
   ) {
      List<Vec3> locs = new ArrayList<>(List.of(begin));
      locs.addAll(ParticleHelper.getJagged(begin, end, sliceIterations, maxJagMultiplier));
      locs.add(end);
      double baseLength = begin.distanceTo(end);
      double maxFractions = (double)(1.5F * fractions) * (1.0 - (double)currentDepth / (double)maxDepth);
      int fr = 0;

      for (int i = 1; i < locs.size(); i++) {
         if (Math.random() < (double)(fractions / (float)locs.size()) * (1.0 - (double)currentDepth / (double)maxDepth)) {
            if ((double)(++fr) < maxFractions) {
               Vec3 dir = locs.get(i).subtract(locs.get(i - 1)).normalize();
               float dev = Necr0manthUtils.random.nextFloat(1.5F, 3.0F);
               dir = dir.add(
                     new Vec3(
                        dir.cross(new Vec3(0.0, 1.0, 0.0))
                           .toVector3f()
                           .rotateAxis((float)(Math.random() * Math.PI * 2.0), (float)dir.x, (float)dir.z, (float)dir.z)
                           .mul(dev)
                     )
                  )
                  .normalize();
               double s = baseLength * Math.pow((double)scale, (double)(currentDepth + 1)) * Necr0manthUtils.random.nextDouble(0.7, 1.4);
               if (s > 0.1F && (double)i < 0.9 * (double)locs.size()) {
                  float[] newWidth = new float[startWidth.length];
                  int[] newParticlesPerBlock = new int[particlesPerBlock.length];

                  for (int j = 0; j < newWidth.length; j++) {
                     newWidth[j] = startWidth[j] * (1.0F - (float)i / (float)locs.size());
                     newParticlesPerBlock[j] = (int)((float)particlesPerBlock[j] / (1.0F - (float)i / (float)locs.size()));
                  }

                  spawnAwesomeRecursiveLightning(
                     locs.get(i - 1),
                     locs.get(i - 1).add(dir.scale(s)),
                     maxJagMultiplier,
                     fractions * fractions / (float)fr,
                     maxDepth,
                     currentDepth + 1,
                     scale,
                     newWidth,
                     newParticlesPerBlock,
                     sliceIterations,
                     options
                  );
               }
            }
         }

         for (int j = 0; j < options.length; j++) {
            float a = 1.0F / (1.0F - (float)Math.min((double)(i - 1), (double)locs.size() * 0.9) / (float)locs.size());
            float b = Math.max(0.01F, 1.0F - (float)Math.min((double)(i - 1), (double)locs.size() * 0.9) / (float)locs.size());
            float c = Math.max(0.01F, 1.0F - (float)(Math.min((double)i, (double)locs.size() * 0.9) / (double)locs.size()));
            createNarrowingParticleLine(options[j], locs.get(i - 1), locs.get(i), (float)particlesPerBlock[j] * a, true, startWidth[j] * b, startWidth[j] * c);
         }
      }
   }

   public static Predicate<BaseParticle<?>> checkCollisions(float tickTime) {
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

   public static BiConsumer<BaseParticle<?>, Float> renderTickAcceleration(Vec3 acceleration) {
      return (particle, dt) -> particle.speed(particle.speed.add(acceleration.scale((double)dt.floatValue())));
   }

   public static Predicate<BaseParticle<?>> tickAcceleration(Vec3 acceleration) {
      return renderTickerToTicker(renderTickAcceleration(acceleration));
   }

   public static Predicate<BaseParticle<?>> renderTickerToTicker(BiConsumer<BaseParticle<?>, Float> renderTicker) {
      return p -> {
         renderTicker.accept(p, 0.05F);
         return false;
      };
   }

   public static BiConsumer<BaseParticle<?>, Float> tickerToRenderTick(Predicate<BaseParticle<?>> ticker) {
      return (p, dt) -> ticker.test(p);
   }

   public static <T extends BaseParticle<T> & IColoredParticle> BiConsumer<T, Float> fadeOut(float time) {
      AtomicReference<Integer> initialAlpha = new AtomicReference<>();
      return (p, dt) -> {
         if (p.maxLifetime - p.lifetime <= time) {
            if (initialAlpha.get() == null) {
               initialAlpha.set(p.getColor().getAlpha());
            }

            p.setColor(
               new Color(
                  p.getColor().getRed(),
                  p.getColor().getGreen(),
                  p.getColor().getBlue(),
                  Math.max(0, (int)((float)initialAlpha.get().intValue() * (p.maxLifetime - p.lifetime) / time))
               )
            );
         }
      };
   }

   public static <T extends BaseParticle<T> & IColoredParticle> BiConsumer<T, Float> fadeIn(float time) {
      AtomicReference<Integer> initialAlpha = new AtomicReference<>();
      return (p, dt) -> {
         if (p.lifetime <= time) {
            if (initialAlpha.get() == null) {
               initialAlpha.set(p.getColor().getAlpha());
            }

            p.setColor(
               new Color(
                  p.getColor().getRed(),
                  p.getColor().getGreen(),
                  p.getColor().getBlue(),
                  Math.max(0, (int)((float)initialAlpha.get().intValue() * p.lifetime / time))
               )
            );
         }
      };
   }

   public static <T extends OptimizedParticle> BiConsumer<T, Float> animate(List<OptimizedParticleType> types, float delay) {
      AtomicReference<Float> t = new AtomicReference<>(0.0F);
      AtomicReference<Integer> i = new AtomicReference<>(0);
      return (particle, dt) -> {
         t.set(t.get() - dt);
         if (t.get() <= 0.0F) {
            particle.type = types.get(i.get());
            i.set((i.get() + 1) % types.size());
            t.set(delay);
         }
      };
   }

   public static <T extends OptimizedParticle> BiConsumer<T, Float> animate(
      SpriteSet spriteSet, Function<TextureAtlasSprite, OptimizedParticleType> typeFactory
   ) {
      Function<TextureAtlasSprite, OptimizedParticleType> cachedTypeFactory = Util.memoize(typeFactory);
      return (particle, dt) -> {
         particle.type = cachedTypeFactory.apply(
            spriteSet.get(Mth.floor(20.0F * Math.min(particle.lifetime, particle.maxLifetime)), Mth.floor(20.0F * particle.maxLifetime))
         );
         if (particle.type == null) {
            System.out.println();
         }
      };
   }

   public static <T extends OptimizedParticle> BiConsumer<T, Float> animate(SpriteSet spriteSet) {
      return animate(spriteSet, OptimizedParticleHelper::typeFromSprite);
   }

   public static <T extends OptimizedParticle> BiConsumer<T, Float> animate(ParticleType<?> particleType) {
      return animate(getSpriteSet(particleType), OptimizedParticleHelper::typeFromSprite);
   }

   public static OptimizedParticleType typeFromSprite(TextureAtlasSprite sprite) {
      return OptimizedParticleType.builder()
         .texture(sprite.atlasLocation())
         .uv0(new Vec2(sprite.getU1(), sprite.getV1()))
         .uv1(new Vec2(sprite.getU1(), sprite.getV0()))
         .uv2(new Vec2(sprite.getU0(), sprite.getV1()))
         .uv3(new Vec2(sprite.getU0(), sprite.getV0()))
         .build();
   }

   public static SpriteSet getSpriteSet(ParticleType<?> type) {
      return ((ParticleEngineAccessor)Minecraft.getInstance().particleEngine).storytelling$getSpriteSets().get(BuiltInRegistries.PARTICLE_TYPE.getKey(type));
   }
}
