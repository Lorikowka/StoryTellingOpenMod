package com.storyteam.storytelling.mixins.minecraft;

import java.util.UUID;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({NbtUtils.class})
public class NBTUtilMixin {
   @Inject(
      method = {"loadUUID"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void loadUUIDFixNPE(Tag pTag, CallbackInfoReturnable<UUID> cir) {
      if (pTag == null) {
         cir.setReturnValue(null);
      }
   }
}
