package com.storyteam.storytelling.system.navmesh.items;

import com.storyteam.storytelling.TabsST;
import com.storyteam.storytelling.init.ItemsST;
import com.storyteam.storytelling.system.navmesh.Navmesh;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;
import org.zeith.hammerlib.api.items.CreativeTab;
import org.zeith.hammerlib.core.adapter.CreativeTabAdapter;

@SimplyRegister(
   prefix = "system/navmesh/"
)
public class ItemNavmeshToggle extends Item {
   @RegistryName("simple_toggle")
   public static final ItemNavmeshToggle ITEM = new ItemNavmeshToggle(ItemsST.props());

   public ItemNavmeshToggle(Properties pProperties) {
      super(pProperties);
      CreativeTabAdapter.bindTab(this, new CreativeTab[]{TabsST.ITEM_GROUP});
   }

   public InteractionResult useOn(UseOnContext pContext) {
      Navmesh nm = Navmesh.get(pContext.getLevel(), pContext.getClickedPos());
      nm.setCanNavigate(pContext.getClickedPos(), !nm.canNavigate(pContext.getClickedPos()));
      return InteractionResult.SUCCESS;
   }

   public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
      if (pIsSelected && pEntity instanceof ServerPlayer sp && !pLevel.isClientSide() && pLevel instanceof ServerLevel sl) {
         BlockPos hitPos = sp.pick(sp.getBlockReach(), 1.0F, false) instanceof BlockHitResult b ? b.getBlockPos() : null;
         BlockPos bp = pEntity.blockPosition();
         if (sl.getGameTime() % 3L != 0L) {
            return;
         }

         BlockPos.betweenClosed(bp.offset(-6, -5, -6), bp.offset(6, 2, 6))
            .forEach(
               pos -> {
                  if (!sl.isEmptyBlock(pos)) {
                     BlockState bs = sl.getBlockState(pos);
                     if (!bs.getCollisionShape(sl, pos).isEmpty()) {
                        BlockPos above = pos.immutable().above();
                        bs = sl.getBlockState(above);
                        if (bs.getCollisionShape(sl, above).isEmpty()) {
                           Navmesh nm = Navmesh.get(sl, pos);
                           boolean cn = nm.canNavigate(pos);
                           Vector3f color = cn ? new Vector3f(0.0F, 1.0F, 0.0F) : new Vector3f(1.0F, 0.0F, 0.0F);
                           if (pos.equals(hitPos)) {
                              sp.displayClientMessage(Component.literal(cn ? "Allow" : "Block").withStyle(cn ? ChatFormatting.GREEN : ChatFormatting.RED), true);
                           }

                           if (!(sp.getLookAngle().dot(Vec3.upFromBottomCenterOf(pos, 1.0).subtract(sp.getEyePosition())) < 0.85F)) {
                              sl.sendParticles(
                                 sp,
                                 new DustParticleOptions(color, 1.0F),
                                 false,
                                 (double)((float)pos.getX() + 0.5F),
                                 (double)(pos.getY() + 1),
                                 (double)((float)pos.getZ() + 0.5F),
                                 1,
                                 0.1,
                                 0.0,
                                 0.1,
                                 0.0
                              );
                           }
                        }
                     }
                  }
               }
            );
         return;
      }
   }
}
