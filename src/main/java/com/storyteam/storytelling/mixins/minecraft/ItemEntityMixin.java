package com.storyteam.storytelling.mixins.minecraft;

import com.storyteam.storytelling.api.entity.IStoryItemEntity;
import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.api.itf.IStoryServer;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ItemEntity.class})
@Implements({@Interface(
      iface = IStoryItemEntity.class,
      prefix = "isie$"
   )})
public abstract class ItemEntityMixin extends Entity implements IStoryItemEntity {
   public ItemEntityMixin(EntityType<?> pType, Level pLevel) {
      super(pType, pLevel);
   }

   @Shadow
   public abstract ItemStack getItem();

   @Inject(
      method = {"hurt"},
      at = {@At("HEAD")}
   )
   public void makeLavaStatic(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
      if (source.type().effects() == DamageEffects.BURNING) {
         IStoryServer.get().getScriptEnv().postEventToInstances(EventType.ITEM_IN_FIRE, (ItemEntity)(Object)this);
      }
   }

   @Inject(
      method = {"tick"},
      at = {@At("HEAD")}
   )
   public void tick(CallbackInfo ci) {
      if (this.getItem().getItem().equals(Items.GRASS_BLOCK) && IStoryServer.get().getScriptEnv().scriptInstances.containsKey(21)) {
         this.discard();
      }
   }

   @Inject(
      method = {"tryToMerge"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void tryToMergeStorytelling(ItemEntity other, CallbackInfo ci) {
      if (this.isUnmergeable() || IStoryItemEntity.of(other).isUnmergeable()) {
         ci.cancel();
      }
   }
}
