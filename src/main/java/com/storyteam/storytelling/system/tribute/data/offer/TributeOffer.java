package com.storyteam.storytelling.system.tribute.data.offer;

import com.storyteam.storytelling.system.tribute.storage.registry.TributeOfferRegistry;
import java.util.Optional;
import lombok.Generated;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegisterEvent;
import org.zeith.hammerlib.api.fml.ICustomRegistrar;

public class TributeOffer implements ICustomRegistrar {
   private ResourceLocation id;
   private int requiredCount;
   private int daysToSubmit;
   private String nextOfferId;

   public static TributeOffer empty() {
      return builder().id(null).requiredCount(0).daysToSubmit(0).nextOfferId(null).build();
   }

   public String getIdPath() {
      return this.id != null ? this.id.getPath() : "";
   }

   public Optional<TributeOffer> getNextOffer() {
      return this.nextOfferId == null ? Optional.empty() : TributeOfferRegistry.get(this.nextOfferId);
   }

   public boolean isEmpty() {
      return this.id == null && this.requiredCount == 0 && this.daysToSubmit == 0 && this.nextOfferId == null;
   }

   public void performRegister(RegisterEvent registerEvent, ResourceLocation resourceLocation) {
      registerEvent.register(Registries.ITEM, registerHelper -> {
         this.id = resourceLocation;
         TributeOfferRegistry.register(this);
      });
   }

   @Override
   public String toString() {
      return "TributeOffer {\n  id: "
         + this.id
         + ",\n  daysToSubmit: "
         + this.daysToSubmit
         + ",\n  nextOffer: "
         + (this.getNextOffer().isPresent() ? this.getNextOffer().get().getIdPath() : "null")
         + ",\n  nextOfferId: "
         + (this.nextOfferId != null ? this.nextOfferId : "null")
         + "\n}";
   }

   @Generated
   TributeOffer(ResourceLocation id, int requiredCount, int daysToSubmit, String nextOfferId) {
      this.id = id;
      this.requiredCount = requiredCount;
      this.daysToSubmit = daysToSubmit;
      this.nextOfferId = nextOfferId;
   }

   @Generated
   public static TributeOffer.TributeOfferBuilder builder() {
      return new TributeOffer.TributeOfferBuilder();
   }

   @Generated
   public ResourceLocation getId() {
      return this.id;
   }

   @Generated
   public int getRequiredCount() {
      return this.requiredCount;
   }

   @Generated
   public int getDaysToSubmit() {
      return this.daysToSubmit;
   }

   @Generated
   public String getNextOfferId() {
      return this.nextOfferId;
   }

   @Generated
   public static class TributeOfferBuilder {
      @Generated
      private ResourceLocation id;
      @Generated
      private int requiredCount;
      @Generated
      private int daysToSubmit;
      @Generated
      private String nextOfferId;

      @Generated
      TributeOfferBuilder() {
      }

      @Generated
      public TributeOffer.TributeOfferBuilder id(ResourceLocation id) {
         this.id = id;
         return this;
      }

      @Generated
      public TributeOffer.TributeOfferBuilder requiredCount(int requiredCount) {
         this.requiredCount = requiredCount;
         return this;
      }

      @Generated
      public TributeOffer.TributeOfferBuilder daysToSubmit(int daysToSubmit) {
         this.daysToSubmit = daysToSubmit;
         return this;
      }

      @Generated
      public TributeOffer.TributeOfferBuilder nextOfferId(String nextOfferId) {
         this.nextOfferId = nextOfferId;
         return this;
      }

      @Generated
      public TributeOffer build() {
         return new TributeOffer(this.id, this.requiredCount, this.daysToSubmit, this.nextOfferId);
      }

      @Generated
      @Override
      public String toString() {
         return "TributeOffer.TributeOfferBuilder(id="
            + this.id
            + ", requiredCount="
            + this.requiredCount
            + ", daysToSubmit="
            + this.daysToSubmit
            + ", nextOfferId="
            + this.nextOfferId
            + ")";
      }
   }
}
