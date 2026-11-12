package com.storyteam.storytelling.util;

import java.util.ArrayList;
import java.util.Optional;
import net.minecraft.util.Mth;
import org.zeith.hammeranims.api.animation.interp.BlendMode;
import org.zeith.hammeranims.api.animation.interp.Query;
import org.zeith.hammeranims.api.animsys.layer.ILayerMask;
import org.zeith.hammeranims.api.animsys.layer.AnimationLayer.Builder;

public class LayersList {
   ArrayList<LayersList.LayerDefinition> layers = new ArrayList<>();

   public void addFirst(LayersList.LayerDefinition LayerDefinition) {
      this.layers.add(0, LayerDefinition);
   }

   public void addFirst(String name, BlendMode blendType, float weight) {
      this.layers.add(0, new LayersList.LayerDefinition(name, blendType, weight));
   }

   public boolean addAfter(String after, LayersList.LayerDefinition LayerDefinition) {
      Optional<LayersList.LayerDefinition> ll = this.layers.stream().filter(l -> l.name().equals(after)).findFirst();
      if (ll.isPresent()) {
         this.layers.add(this.layers.indexOf(ll.get()) + 1, LayerDefinition);
         return true;
      } else {
         return false;
      }
   }

   public boolean addAfter(String after, String name, BlendMode blendType, float weight) {
      Optional<LayersList.LayerDefinition> ll = this.layers.stream().filter(l -> l.name().equals(after)).findFirst();
      if (ll.isPresent()) {
         this.layers.add(this.layers.indexOf(ll.get()) + 1, new LayersList.LayerDefinition(name, blendType, weight));
         return true;
      } else {
         return false;
      }
   }

   public boolean addBefore(String before, LayersList.LayerDefinition LayerDefinition) {
      Optional<LayersList.LayerDefinition> ll = this.layers.stream().filter(l -> l.name().equals(before)).findFirst();
      if (ll.isPresent()) {
         this.layers.add(this.layers.indexOf(ll.get()) + 1, LayerDefinition);
         return true;
      } else {
         return false;
      }
   }

   public boolean addBefore(String before, String name, BlendMode blendType, float weight) {
      Optional<LayersList.LayerDefinition> ll = this.layers.stream().filter(l -> l.name().equals(before)).findFirst();
      if (ll.isPresent()) {
         this.layers.add(this.layers.indexOf(ll.get()), new LayersList.LayerDefinition(name, blendType, weight));
         return true;
      } else {
         return false;
      }
   }

   public void addLast(LayersList.LayerDefinition LayerDefinition) {
      this.layers.add(LayerDefinition);
   }

   public void addLast(String name, BlendMode blendType, float weight) {
      this.layers.add(new LayersList.LayerDefinition(name, blendType, weight));
   }

   public ArrayList<LayersList.LayerDefinition> getLayers() {
      return this.layers;
   }

   public static final class LayerDefinition extends Builder {
      public LayerDefinition(String name, BlendMode blendType, float weight) {
         super(name);
         this.blendMode(blendType);
         this.weight = Mth.clamp(weight, 0.0F, 1.0F);
      }

      public String name() {
         return this.name;
      }

      public LayerDefinition(String name) {
         super(name);
      }

      public LayersList.LayerDefinition query(Query query) {
         return (LayersList.LayerDefinition)super.query(query);
      }

      public LayersList.LayerDefinition mask(ILayerMask mask) {
         return (LayersList.LayerDefinition)super.mask(mask);
      }

      public LayersList.LayerDefinition weight(float weight) {
         return (LayersList.LayerDefinition)super.weight(weight);
      }

      public LayersList.LayerDefinition blendMode(BlendMode blendMode) {
         return (LayersList.LayerDefinition)super.blendMode(blendMode);
      }

      public LayersList.LayerDefinition allowAutoSync(boolean allowAutoSync) {
         return (LayersList.LayerDefinition)super.allowAutoSync(allowAutoSync);
      }

      public LayersList.LayerDefinition preventAutoSync() {
         return (LayersList.LayerDefinition)super.preventAutoSync();
      }

      public LayersList.LayerDefinition persistent(boolean persistent) {
         return (LayersList.LayerDefinition)super.persistent(persistent);
      }

      public LayersList.LayerDefinition nonPersistent() {
         return (LayersList.LayerDefinition)super.nonPersistent();
      }
   }
}
