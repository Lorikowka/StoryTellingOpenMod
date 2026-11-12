package com.storyteam.storytelling.system.tribute.data.medallion;

import com.storyteam.storytelling.system.tribute.TributeSystem;
import com.storyteam.storytelling.system.tribute.storage.registry.MedallionStageRegistry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Generated;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegisterEvent;
import org.zeith.hammerlib.api.fml.ICustomRegistrar;

public class MedallionStage implements ICustomRegistrar {
   ResourceLocation id;
   Map<MedallionStage.TextureSize, ResourceLocation> wheelTextures;
   Map<MedallionStage.TextureSize, ResourceLocation> frameTextures;
   List<MedallionEmotion> supportedEmotions;

   public String getIdPath() {
      return this.id.getPath();
   }

   public ResourceLocation getWheelTexture(MedallionStage.TextureSize size) {
      return this.wheelTextures.getOrDefault(size, TributeSystem.medallionTx(size == MedallionStage.TextureSize.SMALL ? "wheel_small" : "wheel_large"));
   }

   public ResourceLocation getFrameTexture(MedallionStage.TextureSize size) {
      return this.frameTextures
         .getOrDefault(size, TributeSystem.frameTx(this.getIdPath(), size == MedallionStage.TextureSize.SMALL ? "frame_small" : "frame_large"));
   }

   public ResourceLocation getFrameEmotionTexture(MedallionEmotion emotion, MedallionStage.TextureSize size) {
      if (emotion == null) {
         throw new IllegalArgumentException("Frame emotion cannot be null");
      } else {
         String suffix = "emotion/%s_frame".formatted(emotion.name().toLowerCase(Locale.ROOT));
         if (size == MedallionStage.TextureSize.SMALL) {
            suffix = "small_" + suffix;
         }

         return TributeSystem.frameTx(this.getIdPath(), suffix);
      }
   }

   private Map<MedallionEmotion, ResourceLocation> getFrameEmotionTextures(MedallionStage.TextureSize size) {
      return this.supportedEmotions.stream().collect(Collectors.toMap(Function.identity(), e -> this.getFrameEmotionTexture(e, size)));
   }

   public void performRegister(RegisterEvent registerEvent, ResourceLocation resourceLocation) {
      registerEvent.register(Registries.ITEM, registerHelper -> {
         this.id = resourceLocation;
         MedallionStageRegistry.register(this);
      });
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder("MedallionStage {\n");
      sb.append("  id: ").append(this.id).append("\n");

      for (MedallionStage.TextureSize size : MedallionStage.TextureSize.values()) {
         sb.append("  wheelTexture(").append(size).append("): ").append(this.getWheelTexture(size)).append("\n");
         sb.append("  frameTexture(").append(size).append("): ").append(this.getFrameTexture(size)).append("\n");
      }

      sb.append("  supportedEmotions: ");
      if (this.supportedEmotions != null && !this.supportedEmotions.isEmpty()) {
         sb.append(this.supportedEmotions.stream().map(Enum::name).collect(Collectors.joining(", ", "[", "]"))).append("\n");
      } else {
         sb.append("[]\n");
      }

      for (MedallionStage.TextureSize size : MedallionStage.TextureSize.values()) {
         sb.append("  frameEmotionTextures(").append(size).append("):\n");
         this.getFrameEmotionTextures(size).forEach((emotion, tex) -> sb.append("    ").append(emotion.name()).append(" -> ").append(tex).append("\n"));
      }

      sb.append("}");
      return sb.toString();
   }

   @Generated
   private static Map<MedallionStage.TextureSize, ResourceLocation> $default$wheelTextures() {
      return new EnumMap<>(MedallionStage.TextureSize.class);
   }

   @Generated
   private static Map<MedallionStage.TextureSize, ResourceLocation> $default$frameTextures() {
      return new EnumMap<>(MedallionStage.TextureSize.class);
   }

   @Generated
   MedallionStage(
      ResourceLocation id,
      Map<MedallionStage.TextureSize, ResourceLocation> wheelTextures,
      Map<MedallionStage.TextureSize, ResourceLocation> frameTextures,
      List<MedallionEmotion> supportedEmotions
   ) {
      this.id = id;
      this.wheelTextures = wheelTextures;
      this.frameTextures = frameTextures;
      this.supportedEmotions = supportedEmotions;
   }

   @Generated
   public static MedallionStage.MedallionStageBuilder builder() {
      return new MedallionStage.MedallionStageBuilder();
   }

   @Generated
   public ResourceLocation getId() {
      return this.id;
   }

   @Generated
   public Map<MedallionStage.TextureSize, ResourceLocation> getWheelTextures() {
      return this.wheelTextures;
   }

   @Generated
   public Map<MedallionStage.TextureSize, ResourceLocation> getFrameTextures() {
      return this.frameTextures;
   }

   @Generated
   public List<MedallionEmotion> getSupportedEmotions() {
      return this.supportedEmotions;
   }

   @Generated
   public static class MedallionStageBuilder {
      @Generated
      private ResourceLocation id;
      @Generated
      private boolean wheelTextures$set;
      @Generated
      private Map<MedallionStage.TextureSize, ResourceLocation> wheelTextures$value;
      @Generated
      private boolean frameTextures$set;
      @Generated
      private Map<MedallionStage.TextureSize, ResourceLocation> frameTextures$value;
      @Generated
      private ArrayList<MedallionEmotion> supportedEmotions;

      @Generated
      MedallionStageBuilder() {
      }

      @Generated
      public MedallionStage.MedallionStageBuilder id(ResourceLocation id) {
         this.id = id;
         return this;
      }

      @Generated
      public MedallionStage.MedallionStageBuilder wheelTextures(Map<MedallionStage.TextureSize, ResourceLocation> wheelTextures) {
         this.wheelTextures$value = wheelTextures;
         this.wheelTextures$set = true;
         return this;
      }

      @Generated
      public MedallionStage.MedallionStageBuilder frameTextures(Map<MedallionStage.TextureSize, ResourceLocation> frameTextures) {
         this.frameTextures$value = frameTextures;
         this.frameTextures$set = true;
         return this;
      }

      @Generated
      public MedallionStage.MedallionStageBuilder supportedEmotions(MedallionEmotion supportedEmotions) {
         if (this.supportedEmotions == null) {
            this.supportedEmotions = new ArrayList<>();
         }

         this.supportedEmotions.add(supportedEmotions);
         return this;
      }

      @Generated
      public MedallionStage.MedallionStageBuilder supportedEmotions(Collection<? extends MedallionEmotion> supportedEmotions) {
         if (supportedEmotions == null) {
            throw new NullPointerException("supportedEmotions cannot be null");
         } else {
            if (this.supportedEmotions == null) {
               this.supportedEmotions = new ArrayList<>();
            }

            this.supportedEmotions.addAll(supportedEmotions);
            return this;
         }
      }

      @Generated
      public MedallionStage.MedallionStageBuilder clearSupportedEmotions() {
         if (this.supportedEmotions != null) {
            this.supportedEmotions.clear();
         }

         return this;
      }

      @Generated
      public MedallionStage build() {
         List supportedEmotions = switch (this.supportedEmotions == null ? 0 : this.supportedEmotions.size()) {
            case 0 -> Collections.emptyList();
            case 1 -> Collections.singletonList(this.supportedEmotions.get(0));
            default -> Collections.unmodifiableList(new ArrayList<>(this.supportedEmotions));
         };
         Map<MedallionStage.TextureSize, ResourceLocation> wheelTextures$value = this.wheelTextures$value;
         if (!this.wheelTextures$set) {
            wheelTextures$value = MedallionStage.$default$wheelTextures();
         }

         Map<MedallionStage.TextureSize, ResourceLocation> frameTextures$value = this.frameTextures$value;
         if (!this.frameTextures$set) {
            frameTextures$value = MedallionStage.$default$frameTextures();
         }

         return new MedallionStage(this.id, wheelTextures$value, frameTextures$value, supportedEmotions);
      }

      @Generated
      @Override
      public String toString() {
         return "MedallionStage.MedallionStageBuilder(id="
            + this.id
            + ", wheelTextures$value="
            + this.wheelTextures$value
            + ", frameTextures$value="
            + this.frameTextures$value
            + ", supportedEmotions="
            + this.supportedEmotions
            + ")";
      }
   }

   public static enum TextureSize {
      LARGE,
      SMALL;
   }
}
