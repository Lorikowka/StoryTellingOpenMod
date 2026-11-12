package com.storyteam.storytelling.script.story.characters;

import com.storyteam.storytelling.system.clothes.ArmorGeometryConfiguration;
import com.storyteam.storytelling.system.clothes.Outfit;
import com.storyteam.storytelling.system.clothes.client.VanityRenderOptions;
import com.storyteam.storytelling.system.clothes.item.ItemVanityArmor;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Generated;
import net.minecraft.resources.ResourceLocation;
import org.zeith.hammeranims.api.geometry.IGeometryContainer;
import org.zeith.hammerlib.util.mcf.Resources;

public class RenderCharacter {
   public final IGeometryContainer model;
   public final String name;
   public final ResourceLocation texture;
   public final ResourceLocation emissiveTexture;
   public final Outfit outfit;

   protected RenderCharacter(IGeometryContainer model, String name, ResourceLocation texture, ResourceLocation emissiveTexture, Outfit outfit) {
      this.model = model;
      this.name = name;
      this.texture = texture;
      this.emissiveTexture = emissiveTexture;
      this.outfit = outfit != null ? outfit : Outfit.builder().build();
   }

   public List<TexturedGeometryContainer> models() {
      List<TexturedGeometryContainer> lst = new ArrayList<>();
      lst.add(new TexturedGeometryContainer(this.model, this.texture, this.emissiveTexture));
      this.outfit
         .items()
         .values()
         .stream()
         .filter(s -> !s.isEmpty() && s.getItem() instanceof ItemVanityArmor)
         .map(
            s -> {
               ArmorGeometryConfiguration mod = ((ItemVanityArmor)s.getItem()).getModelConfiguration();
               IGeometryContainer geo = mod.getModelContainerFor(this.model);
               VanityRenderOptions opts = mod.getOptionsFor(this.model);
               return geo != null && opts != null
                  ? new TexturedGeometryContainer(geo, opts.emissive() ? null : opts.texture(), opts.emissive() ? opts.texture() : null)
                  : null;
            }
         )
         .filter(Objects::nonNull)
         .forEach(lst::add);
      return lst;
   }

   @Generated
   public static RenderCharacter.CharacterBuilder builder() {
      return new RenderCharacter.CharacterBuilder();
   }

   @Generated
   public RenderCharacter.CharacterBuilder toBuilder() {
      return new RenderCharacter.CharacterBuilder()
         .model(this.model)
         .name(this.name)
         .texture(this.texture)
         .emissiveTexture(this.emissiveTexture)
         .outfit(this.outfit);
   }

   @Generated
   public IGeometryContainer getModel() {
      return this.model;
   }

   @Generated
   public String getName() {
      return this.name;
   }

   @Generated
   public ResourceLocation getTexture() {
      return this.texture;
   }

   @Generated
   public ResourceLocation getEmissiveTexture() {
      return this.emissiveTexture;
   }

   @Generated
   public Outfit getOutfit() {
      return this.outfit;
   }

   public static class CharacterBuilder {
      @Generated
      private IGeometryContainer model;
      @Generated
      private String name;
      @Generated
      private ResourceLocation texture;
      @Generated
      private ResourceLocation emissiveTexture;
      @Generated
      private Outfit outfit;
      private boolean emissive;

      public RenderCharacter.CharacterBuilder withEmission() {
         this.emissive = true;
         if (this.texture != null) {
            this.updateEmissive();
         }

         return this;
      }

      public RenderCharacter.CharacterBuilder texture(ResourceLocation texture) {
         this.texture = texture;
         if (this.emissive) {
            this.updateEmissive();
         }

         return this;
      }

      private void updateEmissive() {
         this.emissiveTexture = Resources.location(this.texture.getNamespace(), this.texture.getPath().replace(".png", "_emissive.png"));
      }

      @Generated
      CharacterBuilder() {
      }

      @Generated
      public RenderCharacter.CharacterBuilder model(IGeometryContainer model) {
         this.model = model;
         return this;
      }

      @Generated
      public RenderCharacter.CharacterBuilder name(String name) {
         this.name = name;
         return this;
      }

      @Generated
      public RenderCharacter.CharacterBuilder emissiveTexture(ResourceLocation emissiveTexture) {
         this.emissiveTexture = emissiveTexture;
         return this;
      }

      @Generated
      public RenderCharacter.CharacterBuilder outfit(Outfit outfit) {
         this.outfit = outfit;
         return this;
      }

      @Generated
      public RenderCharacter build() {
         return new RenderCharacter(this.model, this.name, this.texture, this.emissiveTexture, this.outfit);
      }

      @Generated
      @Override
      public String toString() {
         return "RenderCharacter.CharacterBuilder(model="
            + this.model
            + ", name="
            + this.name
            + ", texture="
            + this.texture
            + ", emissiveTexture="
            + this.emissiveTexture
            + ", outfit="
            + this.outfit
            + ")";
      }
   }
}
