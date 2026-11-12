package com.storyteam.storytelling.mixins.worldedit;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.sk89q.worldedit.util.asset.AssetLoader;
import com.sk89q.worldedit.util.asset.AssetLoaders;
import com.sk89q.worldedit.util.asset.holder.ImageHeightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(
   value = {AssetLoaders.class},
   remap = false
)
public class AssetLoadersMixin {
   @WrapOperation(
      method = {"init"},
      at = {@At(
         value = "INVOKE",
         target = "Lcom/sk89q/worldedit/util/asset/AssetLoaders;registerAssetLoader(Lcom/sk89q/worldedit/util/asset/AssetLoader;Ljava/lang/Class;)V"
      )}
   )
   private <T> void ST_init(AssetLoaders instance, AssetLoader<T> tAssetLoader, Class<T> loader, Operation<Void> original) {
      if (loader != ImageHeightmap.class) {
         original.call(new Object[]{instance, tAssetLoader, loader});
      }
   }
}
