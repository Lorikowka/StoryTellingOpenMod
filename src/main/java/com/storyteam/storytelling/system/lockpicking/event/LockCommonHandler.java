package com.storyteam.storytelling.system.lockpicking.event;

import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.system.lockpicking.InitLockPicking;
import com.storyteam.storytelling.system.lockpicking.data.LockComplexity;
import com.storyteam.storytelling.system.lockpicking.data.LockRegistryEntry;
import com.storyteam.storytelling.system.lockpicking.data.LockState;
import com.storyteam.storytelling.system.lockpicking.data.LockStorage;
import com.storyteam.storytelling.system.lockpicking.items.ItemKey;
import com.storyteam.storytelling.system.lockpicking.net.PacketOpenLockpicking;
import com.storyteam.storytelling.system.sounds.SoundUtils;
import com.storyteam.storytelling.util.ChatUtilST;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.piston.PistonStructureResolver;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.level.BlockEvent.BreakEvent;
import net.minecraftforge.event.level.ExplosionEvent.Detonate;
import net.minecraftforge.event.level.PistonEvent.Pre;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import org.zeith.hammerlib.net.Network;
import org.zeith.hammerlib.util.java.Cast;

public class LockCommonHandler {
   public static void message(Component message) {
      ChatUtilST.sendMessageLololowka(message);
   }

   @SubscribeEvent
   public static void rightBlock(RightClickBlock event) {
      if (!event.isCanceled()) {
         Player player = event.getEntity();
         GlobalPos lockPos = GlobalPos.of(event.getLevel().dimension(), event.getHitVec().getBlockPos());
         if (!getLockState(lockPos).isUnlocked() && !player.isCreative()) {
            event.setCanceled(true);
            LockRegistryEntry data = LockStorage.getLock(lockPos);
            if (event.getHand() == InteractionHand.MAIN_HAND && event.getSide() == LogicalSide.SERVER && event.getLevel() instanceof ServerLevel sl) {
               LockEvent.Pre var10 = new LockEvent.Pre(data, event.getLevel(), player);
               if (!Storytelling.BUS.post(var10)) {
                  data = var10.getData();
                  if (data != null) {
                     ItemStack item = player.getMainHandItem();
                     ItemKey.KeyData kd = ItemKey.getKeyData(item);
                     if (kd != null) {
                        if (kd.matches(data)) {
                           if (performUnlock(sl, data, kd.getSound()) && kd.consume) {
                              item.shrink(1);
                           }
                        } else {
                           message(Component.literal("Этот ключ не подходит к замку."));
                        }
                     } else if (data.entry.getComplexity() == LockComplexity.IMPOSSIBLE) {
                        message(Component.literal("Мне нужен ключ."));
                     } else if (player.getMainHandItem().getItem() == InitLockPicking.LOCKPICK) {
                        LockEvent.Attempt evt = new LockEvent.Attempt(data);
                        if (!Storytelling.BUS.post(evt)) {
                           Network.sendTo(player, new PacketOpenLockpicking(data.root, data));
                        }
                     } else {
                        message(Component.literal("Мне нужен ключ или отмычка."));
                     }
                  }
               }
            }
         }
      }
   }

   public static boolean performUnlock(ServerLevel level, LockRegistryEntry entry, SoundEvent sound) {
      if (entry != null && !entry.isUnlocked()) {
         entry.unlock();
         SoundUtils.playVolumedSound(level, entry.root.pos(), sound);
         return true;
      } else {
         return false;
      }
   }

   @SubscribeEvent
   public static void breakListener(BreakEvent event) {
      GlobalPos globalPos = GlobalPos.of(event.getPlayer().level().dimension(), event.getPos());
      if (getLockState(globalPos).isLocked() && !event.getPlayer().isCreative()) {
         event.setCanceled(true);
      }
   }

   @SubscribeEvent
   public static void preventExplode(Detonate event) {
      ResourceKey<Level> dimension = event.getLevel().dimension();
      event.getAffectedBlocks().removeIf(pos -> getLockState(GlobalPos.of(dimension, pos)).isLocked());
   }

   @SubscribeEvent
   public static void breakSpeed(BreakSpeed event) {
      BlockPos pos = (BlockPos)event.getPosition().orElse(null);
      if (pos != null) {
         if (getLockState(GlobalPos.of(event.getEntity().level().dimension(), pos)).isLocked()) {
            event.setNewSpeed(0.0F);
         }
      }
   }

   @SubscribeEvent
   public static void preventPistonMovement(Pre event) {
      Level level = (Level)Cast.cast(event.getLevel(), Level.class);
      if (level != null) {
         PistonStructureResolver resolver = event.getStructureHelper();
         if (resolver != null) {
            ResourceKey<Level> dimension = level.dimension();
            if (Stream.<BlockPos>concat(resolver.getToPush().stream(), resolver.getToDestroy().stream())
               .anyMatch(pos -> getLockState(GlobalPos.of(dimension, pos)).isLocked())) {
               event.setCanceled(true);
            }
         }
      }
   }

   public static LockState getLockState(GlobalPos globalPos) {
      LockRegistryEntry data = LockStorage.getLock(globalPos);
      if (data == null) {
         return LockState.NOT_FOUND;
      } else {
         return data.isUnlocked() ? LockState.UNLOCKED : LockState.LOCKED;
      }
   }
}
