package com.storyteam.storytelling.client.model;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.entity.EquipmentSlot;
import org.zeith.hammerlib.util.java.Cast;

public class ArmorModelHolder {
   private final ImmutableMap<EquipmentSlot, Supplier<HumanoidModel<?>>> modelMap;
   private static final List<Runnable> RESET_TASKS = new ArrayList<>();
   public static final ResourceManagerReloadListener RELOADER = manager -> {
      for (Runnable task : RESET_TASKS) {
         task.run();
      }
   };

   public ArmorModelHolder(com.google.common.collect.ImmutableMap.Builder<EquipmentSlot, Supplier<HumanoidModel<?>>> modelMap) {
      this.modelMap = modelMap.build();
   }

   public static MeshDefinition createHumanoidMesh() {
      MeshDefinition md = new MeshDefinition();
      PartDefinition def = md.getRoot();
      def.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
      def.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);
      def.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.ZERO);
      def.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.ZERO);
      def.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.ZERO);
      def.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.ZERO);
      def.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.ZERO);
      return md;
   }

   public HumanoidModel<?> getModel(EquipmentSlot slot) {
      Supplier<HumanoidModel<?>> s = (Supplier<HumanoidModel<?>>)this.modelMap.get(slot);
      return s != null ? s.get() : null;
   }

   public static <T extends HumanoidModel<?>> Supplier<T> bakeModel(Supplier<Supplier<LayerDefinition>> definition, Supplier<Function<ModelPart, T>> model) {
      Supplier<Supplier<T>> factory = () -> Suppliers.memoize(() -> model.get().apply(((LayerDefinition)Cast.get2(definition)).bakeRoot()));
      AtomicReference<Supplier<T>> sup = new AtomicReference<>();
      RESET_TASKS.add(() -> sup.set(null));
      return () -> {
         Supplier<T> cur = sup.get();
         if (cur == null) {
            cur = factory.get();
            sup.set(cur);
         }

         return cur.get();
      };
   }

   public static ArmorModelHolder.Builder builder() {
      return new ArmorModelHolder.Builder();
   }

   public static class Builder {
      private final com.google.common.collect.ImmutableMap.Builder<EquipmentSlot, Supplier<HumanoidModel<?>>> modelMap = ImmutableMap.builder();

      public ArmorModelHolder.Builder head(Supplier<HumanoidModel<?>> model) {
         this.modelMap.put(EquipmentSlot.HEAD, model);
         return this;
      }

      public ArmorModelHolder.Builder body(Supplier<HumanoidModel<?>> model) {
         this.modelMap.put(EquipmentSlot.CHEST, model);
         return this;
      }

      public ArmorModelHolder.Builder legs(Supplier<HumanoidModel<?>> model) {
         this.modelMap.put(EquipmentSlot.LEGS, model);
         return this;
      }

      public ArmorModelHolder.Builder feet(Supplier<HumanoidModel<?>> model) {
         this.modelMap.put(EquipmentSlot.FEET, model);
         return this;
      }

      public ArmorModelHolder build() {
         return new ArmorModelHolder(this.modelMap);
      }
   }
}
