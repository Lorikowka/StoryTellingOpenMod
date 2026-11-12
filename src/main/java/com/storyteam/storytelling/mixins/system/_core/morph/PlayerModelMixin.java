package com.storyteam.storytelling.mixins.system._core.morph;

import com.storyteam.storytelling.system._core.morph.client.IPlayerModelWithRoot;
import java.util.function.Function;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({PlayerModel.class})
public class PlayerModelMixin<T extends LivingEntity> extends HumanoidModel<T> implements IPlayerModelWithRoot {
   @Shadow
   @Final
   private boolean slim;
   @Unique
   protected ModelPart mt$pRoot;

   public PlayerModelMixin(ModelPart pRoot) {
      super(pRoot);
   }

   public PlayerModelMixin(ModelPart pRoot, Function<ResourceLocation, RenderType> pRenderType) {
      super(pRoot, pRenderType);
   }

   @Inject(
      method = {"<init>"},
      at = {@At("TAIL")}
   )
   private void mt$constructor(ModelPart pRoot, boolean pSlim, CallbackInfo ci) {
      this.mt$pRoot = pRoot;
   }

   @Override
   public ModelPart getPlayerModelRoot() {
      return this.mt$pRoot;
   }

   @Override
   public boolean st$IsSlim() {
      return this.slim;
   }
}
