package com.storyteam.storytelling.client.render.entity.def;

import com.storyteam.storytelling.St;
import com.storyteam.storytelling.content.entities.base.EntityRegistrar;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.zeith.hammeranims.api.geometry.IGeometryContainer;
import org.zeith.hammerlib.abstractions.props.Key;
import org.zeith.hammerlib.abstractions.props.KeyMap;

public class RenderContext<T extends Entity> {
   public static final Key<IGeometryContainer> ENTITY_MODEL = Key.of(St.id("entity_model"), IGeometryContainer.class);
   public static final Key<ResourceLocation> DEFAULT_TEXTURE = Key.of(St.id("default_texture"), ResourceLocation.class);
   public static final Key<Float> ENTITY_SCALE = Key.of(St.id("entity_scale"), Float.class);
   public static final Key<Float> ENTITY_SHADOW_SCALE = Key.of(St.id("entity_shadow_scale"), Float.class);
   public static final Key<Boolean> ENTITY_RENDER_ALWAYS = Key.of(St.id("entity_render_always"), Boolean.class);
   public final EntityRegistrar<T> registrar;
   public final KeyMap props = KeyMap.createHash();

   public RenderContext(EntityRegistrar<T> registrar) {
      this.registrar = registrar;
      this.props.put(ENTITY_SCALE, 1.0F);
      this.props.put(ENTITY_SHADOW_SCALE, 0.5F);
      this.props.put(DEFAULT_TEXTURE, St.tx("entity/no_texture"));
      this.props.put(ENTITY_RENDER_ALWAYS, true);
   }

   public <P> RenderContext<T> setProp(Key<P> prop, P value) {
      if (value == null) {
         return this;
      } else {
         this.props.put(prop, value);
         return this;
      }
   }
}
