package com.storyteam.storytelling.init;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.storyteam.storytelling.client.particle.ColoredDropParticle;
import com.storyteam.storytelling.client.particle.GlitchParticle;
import com.storyteam.storytelling.client.particle.circleTint.CircleGradientData;
import com.storyteam.storytelling.client.particle.circleTint.CircleGradientFactory;
import com.storyteam.storytelling.client.particle.circleTint.CircleTintData;
import com.storyteam.storytelling.client.particle.circleTint.CircleTintFactory;
import com.storyteam.storytelling.client.particle.circleTint.CircleTintParticleWithTrail;
import com.storyteam.storytelling.client.particle.circleTint.CircleTintWithTrailData;
import com.storyteam.storytelling.client.particle.controllers.AccelerationData;
import com.storyteam.storytelling.client.particle.controllers.AccelerationType;
import com.storyteam.storytelling.client.particle.controllers.ControllerData;
import com.storyteam.storytelling.client.particle.controllers.ControllerType;
import com.storyteam.storytelling.client.particle.controllers.CustomController;
import com.storyteam.storytelling.client.particle.controllers.GravityController;
import com.storyteam.storytelling.client.particle.controllers.GravityControllerNew;
import com.storyteam.storytelling.client.particle.controllers.LiquidBehaviorController;
import com.storyteam.storytelling.client.particle.controllers.LiquidBehaviorData;
import com.storyteam.storytelling.client.particle.controllers.MoveFromEntity;
import com.storyteam.storytelling.client.particle.controllers.RotatingData;
import com.storyteam.storytelling.client.particle.controllers.RotatingType;
import com.storyteam.storytelling.client.particle.controllers.ScatterController;
import com.storyteam.storytelling.client.particle.controllers.SpinData;
import com.storyteam.storytelling.client.particle.controllers.SpinType;
import com.storyteam.storytelling.client.particle.controllers.StandartController;
import com.storyteam.storytelling.client.particle.controllers.VelocityFollowEntityController;
import com.storyteam.storytelling.client.particle.controllers.ZeroMovementController;
import com.storyteam.storytelling.client.particle.st_particle.STParticleData;
import com.storyteam.storytelling.client.particle.st_particle.STParticleProvider;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammeranims.api.particles.IParticleContainer;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;

@SimplyRegister
public interface ParticleTypesST {
   @RegistryName("test")
   IParticleContainer TEST = IParticleContainer.create();
   @RegistryName("circle_tint")
   ParticleType<CircleTintData> CIRCLE_TINT_TYPE = new CircleTintFactory.CircleTintType();
   @RegistryName("circle_tint_gradient")
   ParticleType<CircleGradientData> CIRCLE_TINT_GRADIENT_TYPE = new CircleGradientFactory.CircleGradientType();
   @RegistryName("circle_tint_trail")
   ParticleType<CircleTintWithTrailData> CIRCLE_TINT_TRAIL_TYPE = new CircleTintParticleWithTrail.CircleTintFactory.CircleTintType();
   @RegistryName("quad_tint_u")
   ParticleType<STParticleData> QUAD_TINT_U = new STParticleProvider.STParticleType();
   @RegistryName("glitch")
   SimpleParticleType GLITCH = new SimpleParticleType(true);
   @RegistryName("colored_drop")
   SimpleParticleType COLORED_DROP = new SimpleParticleType(true);
   @RegistryName("white_smoke")
   ParticleType<STParticleData> WHITE_SMOKE = new STParticleProvider.STParticleType();

   @EventBusSubscriber(
      bus = Bus.MOD,
      value = {Dist.CLIENT}
   )
   public static class ClientSideRegistration {
      @SubscribeEvent
      static void onParticleRegistry(RegisterParticleProvidersEvent event) {
         event.registerSpriteSet(ParticleTypesST.CIRCLE_TINT_TYPE, CircleTintFactory::new);
         event.registerSpriteSet(ParticleTypesST.CIRCLE_TINT_GRADIENT_TYPE, CircleGradientFactory::new);
         event.registerSpriteSet(ParticleTypesST.CIRCLE_TINT_TRAIL_TYPE, CircleTintParticleWithTrail.CircleTintFactory::new);
         event.registerSpriteSet(ParticleTypesST.QUAD_TINT_U, STParticleProvider::new);
         event.registerSpriteSet(ParticleTypesST.GLITCH, GlitchParticle.Factory::new);
         event.registerSpriteSet(ParticleTypesST.WHITE_SMOKE, STParticleProvider::new);
         event.registerSpriteSet(ParticleTypesST.COLORED_DROP, spriteSet -> (type, level, x, y, z, vx, vy, vz) -> {
               ColoredDropParticle p = new ColoredDropParticle(level, x, y, z);
               p.pickSprite(spriteSet);
               return p;
            });
      }
   }

   public static class ParticleBehaviorRegistry {
      private static final BiMap<Integer, ControllerType<?>> MAP = HashBiMap.create();
      public static final ControllerType<StandartController> STANDARD_CONTROLLER = register(new StandartController());
      public static final ControllerType<ZeroMovementController> ZERO_MOVEMENT = register(new ZeroMovementController());
      public static final ControllerType<RotatingData> ROTATING_CONTROLLER = register(new RotatingType());
      public static final ControllerType<ScatterController> SCATTER_CONTROLLER = register(new ScatterController());
      public static final ControllerType<GravityController> GRAVITY_CONTROLLER = register(new GravityController());
      public static final ControllerType<AccelerationData> ACCELERATION_CONTROLLER = register(new AccelerationType());
      public static final ControllerType<GravityControllerNew> GRAVITY_CONTROLLER_NEW = register(new GravityControllerNew());
      public static final ControllerType<CustomController> CUSTOM_CONTROLLER = register(new CustomController());
      public static final ControllerType<MoveFromEntity> MOVE_FROM_ENTITY = register(new MoveFromEntity());
      public static final ControllerType<VelocityFollowEntityController> VELOCITY_FOLLOW_ENTITY_CONTROLLER = register(new VelocityFollowEntityController());
      public static final ControllerType<LiquidBehaviorData> LIQUID_BEHAVIOR_CONTROLLER = register(new LiquidBehaviorController());
      public static final ControllerType<SpinData> SPIN_CONTROLLER = register(new SpinType());

      public static int getId(@Nullable ControllerType<?> type) {
         return (Integer)MAP.inverse().get(type);
      }

      public static <T extends ControllerType<C>, C extends ControllerData> T register(T type) {
         MAP.put(MAP.size(), type);
         return type;
      }

      public static ControllerType<?> byId(int id) {
         return (ControllerType<?>)MAP.get(id);
      }
   }
}
