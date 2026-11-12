package com.storyteam.storytelling.mixins._client.minecraft;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.storyteam.storytelling.client.IClientCapturingBlockSnapshots;
import com.storyteam.storytelling.system.shaders.lighting.LightSystem;
import java.util.ArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.util.BlockSnapshot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.zeith.hammerlib.util.java.Cast;

@Implements({@Interface(
      iface = IClientCapturingBlockSnapshots.class,
      prefix = "st$"
   )})
@Mixin({Level.class})
public class LevelMixinClient {
   @Shadow
   @Final
   private ResourceKey<Level> dimension;
   @Unique
   private final ArrayList<BlockSnapshot> st$ClientSideSnapshots = new ArrayList<>();
   @Unique
   private boolean st$capturingClientSnapshots;

   @ModifyExpressionValue(
      method = {"setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;II)Z"},
      at = {@At("MIXINEXTRAS:EXPRESSION")}
   )
   @Definition(
      id = "captureBlockSnapshots",
      field = {"Lnet/minecraft/world/level/Level;captureBlockSnapshots:Z"}
   )
   @Expression({"this.captureBlockSnapshots"})
   private boolean ST_clientsideCaptureBlockSnapshots(
      boolean original, BlockPos pPos, BlockState pState, int pFlags, @Share("st$clientSnapshot") LocalRef<BlockSnapshot> st$clientSnapshot
   ) {
      if (this.st$capturingClientSnapshots) {
         BlockSnapshot blockSnapshot = BlockSnapshot.create(this.dimension, (LevelAccessor)Cast.cast(this), pPos, pFlags);
         this.st$ClientSideSnapshots.add(blockSnapshot);
      }

      return original;
   }

   @Inject(
      method = {"setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;II)Z"},
      at = {@At("RETURN")},
      slice = {@Slice(
         from = @At(
            value = "INVOKE",
            target = "Ljava/util/ArrayList;remove(Ljava/lang/Object;)Z"
         ),
         to = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;markAndNotifyBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/chunk/LevelChunk;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;II)V"
         )
      )}
   )
   private void ST_clientsideCaptureBlockSnapshots(CallbackInfoReturnable<Boolean> cir, @Share("st$clientSnapshot") LocalRef<BlockSnapshot> st$clientSnapshot) {
      BlockSnapshot snapshot = (BlockSnapshot)st$clientSnapshot.get();
      if (snapshot != null) {
         this.st$ClientSideSnapshots.remove(snapshot);
      }
   }

   @WrapOperation(
      method = {"setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;II)Z"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/level/Level;markAndNotifyBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/chunk/LevelChunk;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;II)V"
      )}
   )
   private void ST_clientsidePreventBlockUpdates(
      Level instance,
      BlockPos pos,
      LevelChunk pPos,
      BlockState levelchunk,
      BlockState blockstate,
      int pState,
      int pFlags,
      Operation<Void> original,
      @Share("st$clientSnapshot") LocalRef<BlockSnapshot> st$clientSnapshot
   ) {
      BlockSnapshot snapshot = (BlockSnapshot)st$clientSnapshot.get();
      if (snapshot == null) {
         original.call(new Object[]{instance, pos, pPos, levelchunk, blockstate, pState, pFlags});
      }
   }

   public void st$setCapturingClientBlockSnapshots(boolean flag) {
      this.st$capturingClientSnapshots = flag;
   }

   public ArrayList<BlockSnapshot> st$getCapturedClientSideBlockSnapshots() {
      return this.st$ClientSideSnapshots;
   }

   @Inject(
      method = {"onBlockStateChange"},
      at = {@At("HEAD")}
   )
   public void onBlockStateChange(BlockPos pPos, BlockState pBlockState, BlockState pNewState, CallbackInfo ci) {
      LightSystem.blockChanged(pPos, pNewState);
   }
}
