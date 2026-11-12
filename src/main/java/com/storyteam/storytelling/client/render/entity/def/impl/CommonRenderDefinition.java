package com.storyteam.storytelling.client.render.entity.def.impl;

import com.storyteam.storytelling.St;
import com.storyteam.storytelling.client.render.entity.AnimatedEntityLikeHumanoidRenderer;
import com.storyteam.storytelling.client.render.entity.AnimatedNoGazeHumanoidRenderer;
import com.storyteam.storytelling.client.render.entity.AnimatedNoGazeHumanoidRendererWithWhiteLayerRenderer;
import com.storyteam.storytelling.client.render.entity.AnimatedNoGazeHumanoidWithEmissiveRenderer;
import com.storyteam.storytelling.client.render.entity.BaseMobWithWhiteLayerRenderer;
import com.storyteam.storytelling.client.render.entity.CommonRenderer;
import com.storyteam.storytelling.client.render.entity.CommonRendererWithWhiteLayer;
import com.storyteam.storytelling.client.render.entity.CommonWithEmissiveRenderer;
import com.storyteam.storytelling.client.render.entity.DynamicModelWithEmissiveRenderer;
import com.storyteam.storytelling.client.render.entity.ExpEntityRenderer;
import com.storyteam.storytelling.client.render.entity.FlytrapRenderer;
import com.storyteam.storytelling.client.render.entity.HumanoidAlwaysRenderer;
import com.storyteam.storytelling.client.render.entity.HumanoidLololowkaRedFaceRenderer;
import com.storyteam.storytelling.client.render.entity.HumanoidMeTabRenderer;
import com.storyteam.storytelling.client.render.entity.HumanoidRenderer;
import com.storyteam.storytelling.client.render.entity.HumanoidWithEmissiveAndWhiteLayerRenderer;
import com.storyteam.storytelling.client.render.entity.HumanoidWithEmissiveRenderer;
import com.storyteam.storytelling.client.render.entity.HumanoidWithWhiteLayerRenderer;
import com.storyteam.storytelling.client.render.entity.NullEntityRenderer;
import com.storyteam.storytelling.client.render.entity.RiddleEntityRenderer;
import com.storyteam.storytelling.client.render.entity.SkyEyeRenderer;
import com.storyteam.storytelling.client.render.entity.ToggleableEmissiveHumanoidRenderer;
import com.storyteam.storytelling.client.render.entity.def.ModelConfiguration;
import com.storyteam.storytelling.client.render.entity.def.RenderContext;
import com.storyteam.storytelling.client.render.entity.def.RenderDefinition;
import com.storyteam.storytelling.client.render.entity.def.RenderKey;
import com.storyteam.storytelling.system.currency.client.render.EidosEntityRenderer;
import com.storyteam.storytelling.system.currency.client.render.EidosGuiRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.hammeranims.api.geometry.IGeometryContainer;
import org.zeith.hammeranims.api.tile.IAnimatedEntity;
import org.zeith.hammerlib.abstractions.props.Key;
import org.zeith.hammerlib.util.java.Cast;

public class CommonRenderDefinition<T extends LivingEntity & IAnimatedEntity> extends RenderDefinition<T> {
   public static final Key<CommonRenderDefinition.Renderer> RENDERER = Key.of(St.id("renderer"), CommonRenderDefinition.Renderer.class);

   public CommonRenderDefinition(RenderContext<T> context) {
      super(context);
   }

   @OnlyIn(Dist.CLIENT)
   @Override
   public EntityRenderer<T> create(Context pContext) {
      IGeometryContainer model = (IGeometryContainer)this.context.props.get(RenderContext.ENTITY_MODEL);
      ResourceLocation texture = (ResourceLocation)this.context.props.get(RenderContext.DEFAULT_TEXTURE);
      float shadow = (Float)this.context.props.getOrDefault(RenderContext.ENTITY_SHADOW_SCALE, 0.5F);
      float scale = (Float)this.context.props.getOrDefault(RenderContext.ENTITY_SCALE, 1.0F);
      boolean always_renderer = (Boolean)this.context.props.getOrDefault(RenderContext.ENTITY_RENDER_ALWAYS, false);
      RenderKey key = (RenderKey)this.context.props.getOrDefault(RenderKey.KEY, RenderKey.entityCutoutNoCull);
      ModelConfiguration modelConfiguration = new ModelConfiguration(model, key::getNMC);

      return (EntityRenderer<T>)Cast.cast(
         switch ((CommonRenderDefinition.Renderer)this.context.props.get(RENDERER)) {
            case CommonRenderer -> new CommonRenderer(pContext, modelConfiguration, shadow, texture, always_renderer);
            case HumanoidRenderer -> new HumanoidRenderer(pContext, modelConfiguration, shadow, texture, scale, always_renderer);
            case HumanoidWithEmissiveRenderer -> new HumanoidWithEmissiveRenderer(pContext, modelConfiguration, shadow, texture, scale);
            case ToggleableEmissiveHumanoidRenderer -> new ToggleableEmissiveHumanoidRenderer(pContext, modelConfiguration, shadow, texture, scale);
            case HumanoidAlwaysRenderer -> new HumanoidAlwaysRenderer(pContext, modelConfiguration, shadow, texture, scale);
            case AnimatedNoGazeHumanoidRenderer -> new AnimatedNoGazeHumanoidRenderer(pContext, modelConfiguration, shadow, texture, scale);
            case AnimatedNoGazeHumanoidRendererWithWhiteLayerRenderer -> new AnimatedNoGazeHumanoidRendererWithWhiteLayerRenderer(
            pContext, modelConfiguration, shadow, texture, scale
         );
            case AnimatedNoGazeHumanoidWithEmissiveRenderer -> new AnimatedNoGazeHumanoidWithEmissiveRenderer(
            pContext, modelConfiguration, shadow, texture, scale
         );
            case AnimatedEntityLikeHumanoidRenderer -> new AnimatedEntityLikeHumanoidRenderer(
            pContext, modelConfiguration, shadow, texture, scale, always_renderer
         );
            case ExpOrbRenderer -> new ExpEntityRenderer(pContext);
            case CommonWithEmissiveRenderer -> new CommonWithEmissiveRenderer(pContext, modelConfiguration, shadow, texture);
            case HumanoidWithWhiteLayerRenderer -> new HumanoidWithWhiteLayerRenderer(pContext, modelConfiguration, shadow, texture, scale);
            case HumanoidWithEmissiveAndWhiteLayerRenderer -> new HumanoidWithEmissiveAndWhiteLayerRenderer(
            pContext, modelConfiguration, shadow, texture, scale
         );
            case BaseMobWithWhiteLayerRenderer -> new BaseMobWithWhiteLayerRenderer(pContext, modelConfiguration, shadow, texture, scale);
            case HumanoidLololowkaRedFaceRenderer -> new HumanoidLololowkaRedFaceRenderer(pContext, modelConfiguration, shadow, texture, scale);
            case CommonRendererWithWhiteLayer -> new CommonRendererWithWhiteLayer(pContext, modelConfiguration, shadow, texture);
            case RiddleEntityRenderer -> new RiddleEntityRenderer(pContext);
            case DynamicModelWithEmissiveRenderer -> new DynamicModelWithEmissiveRenderer(pContext, modelConfiguration, shadow, texture, key);
            case SkyEyeRenderer -> new SkyEyeRenderer(pContext, modelConfiguration, shadow, texture);
            case HumanoidMeTabRenderer -> new HumanoidMeTabRenderer(pContext, modelConfiguration, shadow, texture, scale, always_renderer);
            case EidosRenderer -> new EidosEntityRenderer(pContext, modelConfiguration, shadow, texture, always_renderer);
            case EidosGuiRenderer -> new EidosGuiRenderer(pContext, modelConfiguration, shadow, texture, always_renderer);
            case FlytrapRenderer -> new FlytrapRenderer(pContext, modelConfiguration, shadow, texture, always_renderer);
            case NullEntityRenderer -> new NullEntityRenderer(pContext);
         }
      );
   }

   public static enum Renderer {
      CommonRenderer,
      HumanoidRenderer,
      HumanoidWithEmissiveRenderer,
      ToggleableEmissiveHumanoidRenderer,
      HumanoidAlwaysRenderer,
      AnimatedNoGazeHumanoidRenderer,
      AnimatedNoGazeHumanoidRendererWithWhiteLayerRenderer,
      AnimatedNoGazeHumanoidWithEmissiveRenderer,
      AnimatedEntityLikeHumanoidRenderer,
      ExpOrbRenderer,
      CommonWithEmissiveRenderer,
      HumanoidWithWhiteLayerRenderer,
      HumanoidWithEmissiveAndWhiteLayerRenderer,
      HumanoidLololowkaRedFaceRenderer,
      BaseMobWithWhiteLayerRenderer,
      CommonRendererWithWhiteLayer,
      RiddleEntityRenderer,
      DynamicModelWithEmissiveRenderer,
      SkyEyeRenderer,
      HumanoidMeTabRenderer,
      EidosRenderer,
      EidosGuiRenderer,
      FlytrapRenderer,
      NullEntityRenderer;
   }
}
