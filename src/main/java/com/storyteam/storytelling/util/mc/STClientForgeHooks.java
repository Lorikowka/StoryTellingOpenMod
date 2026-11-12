package com.storyteam.storytelling.util.mc;

import com.google.common.collect.Lists;
import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.client.IClientCapturingBlockSnapshots;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.BlockSnapshot;
import org.jetbrains.annotations.NotNull;

public class STClientForgeHooks {
   public static InteractionResult onPlaceItemIntoWorld(@NotNull UseOnContext context, Function<UseOnContext, InteractionResult> callback) {
      Level level = context.getLevel();
      if (!level.isClientSide()) {
         return callback.apply(context);
      } else {
         ItemStack itemstack = context.getItemInHand();
         Player player = context.getPlayer();
         if (!(level instanceof IClientCapturingBlockSnapshots capt)) {
            return callback.apply(context);
         } else {
            Item item = itemstack.getItem();
            int size = itemstack.getCount();
            CompoundTag nbt = null;
            if (itemstack.getTag() != null) {
               nbt = itemstack.getTag().copy();
            }

            if (!(itemstack.getItem() instanceof BucketItem)) {
               capt.setCapturingClientBlockSnapshots(true);
            }

            InteractionResult ret = callback.apply(context);
            capt.setCapturingClientBlockSnapshots(false);
            ArrayList<BlockSnapshot> captured = capt.getCapturedClientSideBlockSnapshots();
            if (ret.consumesAction()) {
               int newSize = itemstack.getCount();
               CompoundTag newNBT = null;
               if (itemstack.getTag() != null) {
                  newNBT = itemstack.getTag().copy();
               }

               List<BlockSnapshot> blockSnapshots = (List<BlockSnapshot>)captured.clone();
               captured.clear();
               itemstack.setCount(size);
               itemstack.setTag(nbt);
               Direction side = context.getClickedFace();
               boolean eventResult = false;
               if (blockSnapshots.size() > 1) {
                  BlockSnapshot snap = blockSnapshots.get(0);
                  BlockState placedAgainst = snap.getLevel().getBlockState(snap.getPos().relative(side.getOpposite()));
                  EntityMultiPlaceEventClientside event = new EntityMultiPlaceEventClientside(blockSnapshots, placedAgainst, player);
                  eventResult = Storytelling.BUS.post(event);
               } else if (blockSnapshots.size() == 1) {
                  BlockSnapshot blockSnapshot = blockSnapshots.get(0);
                  BlockState placedAgainst = blockSnapshot.getLevel().getBlockState(blockSnapshot.getPos().relative(side.getOpposite()));
                  EntityPlaceEventClientside event = new EntityPlaceEventClientside(blockSnapshot, placedAgainst, player);
                  eventResult = Storytelling.BUS.post(event);
               }

               if (eventResult) {
                  ret = InteractionResult.FAIL;

                  for (BlockSnapshot blocksnapshot : Lists.reverse(blockSnapshots)) {
                     level.restoringBlockSnapshots = true;
                     blocksnapshot.restore(true, false);
                     level.restoringBlockSnapshots = false;
                  }
               } else {
                  itemstack.setCount(newSize);
                  itemstack.setTag(newNBT);
               }
            }

            captured.clear();
            return ret;
         }
      }
   }
}
