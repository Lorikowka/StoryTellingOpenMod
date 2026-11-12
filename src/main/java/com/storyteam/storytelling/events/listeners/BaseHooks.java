package com.storyteam.storytelling.events.listeners;

import com.storyteam.storytelling.api.data.EntityInteractionData;
import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.api.itf.IStoryEntity;
import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.net.PacketKeyInputEvent;
import com.storyteam.storytelling.net.PacketMouseInputEvent;
import com.storyteam.storytelling.net.PacketSyncStoryEntity;
import com.storyteam.storytelling.util.ScriptUtils;
import java.util.concurrent.atomic.AtomicBoolean;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.InputEvent.Key;
import net.minecraftforge.client.event.InputEvent.MouseButton.Pre;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.living.LivingSwapItemsEvent.Hands;
import net.minecraftforge.event.entity.living.MobSpawnEvent.AllowDespawn;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.StartTracking;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteractSpecific;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.event.level.SleepFinishedTimeEvent;
import net.minecraftforge.event.level.BlockEvent.BreakEvent;
import net.minecraftforge.event.level.BlockEvent.EntityPlaceEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.zeith.hammerlib.net.Network;
import org.zeith.hammerlib.util.java.Cast;

@EventBusSubscriber
public class BaseHooks {
   public static boolean teleportUnlock = false;

   @SubscribeEvent
   public static void sleepFinish(SleepFinishedTimeEvent event) {
      IStoryServer.get().postStoryEvent(EventType.SLEEP_FINISH, ScriptUtils.getPlayer());
   }

   @SubscribeEvent
   public static void syncStoryEntityToClient(StartTracking event) {
      Entity target = event.getTarget();
      if (((IStoryEntity)target).isStorytellingEntity()) {
         Network.sendToTracking(
            target, new PacketSyncStoryEntity(target.getId(), ((IStoryEntity)target).getStoryName(), target.getUUID(), ((IStoryEntity)target).isSEVulnerable())
         );
      }
   }

   @SubscribeEvent
   public static void serverTick(ServerTickEvent e) {
      if (e.phase == Phase.END) {
         IStoryServer.optionally().ifPresent(IStoryServer::tickStory);
      }
   }

   @SubscribeEvent
   public static void serverTick(PlayerTickEvent e) {
      if (e.phase == Phase.END && e.side == LogicalSide.SERVER && e.player instanceof ServerPlayer) {
         IStoryServer.optionally().map(IStoryServer::getScriptEnv).ifPresent(env -> env.tickPlayer((ServerPlayer)e.player));
      }
   }

   @SubscribeEvent
   public static void inventoryCheck(PlayerTickEvent e) {
      if (e.phase == Phase.END && e.side == LogicalSide.SERVER && e.player instanceof ServerPlayer player) {
         for (ItemStack item : player.getInventory().items) {
            if (item.isDamageableItem() && item.getDamageValue() >= item.getMaxDamage()) {
               player.getInventory().removeItem(item);
            }
         }
      }
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public static void entityAttacked(LivingAttackEvent event) {
      if (!event.getEntity().level().isClientSide()) {
         AtomicBoolean prevent = new AtomicBoolean(false);
         IStoryServer.get()
            .postCancellableStoryEvent(
               EventType.ENTITY_ATTACK, new EntityInteractionData(ScriptUtils.getPlayer(), event.getEntity(), () -> prevent.set(true)), e -> prevent.get()
            );
      }
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public static void interact(EntityInteractSpecific event) {
      if (!event.getLevel().isClientSide) {
         if (!event.getEntity().isCrouching()) {
            Entity target = event.getTarget();
            AtomicBoolean prevent = new AtomicBoolean(false);
            IStoryServer.get()
               .postCancellableStoryEvent(
                  EventType.ENTITY_CLICK, new EntityInteractionData(event.getEntity(), (Entity)Cast.cast(target), () -> prevent.set(true)), e -> prevent.get()
               );
            if (event.getHand() == InteractionHand.MAIN_HAND) {
               IStoryServer.get()
                  .postCancellableStoryEvent(
                     EventType.ENTITY_CLICK_ONE_HAND,
                     new EntityInteractionData(event.getEntity(), (Entity)Cast.cast(target), () -> prevent.set(true)),
                     e -> prevent.get()
                  );
            }
         }
      }
   }

   @SubscribeEvent
   public static void onEntityKill(LivingDeathEvent event) {
      LivingEntity entity = event.getEntity();
      if (!entity.level().isClientSide) {
         Entity killer = event.getSource().getEntity();
         if (killer instanceof Player) {
            AtomicBoolean prevent = new AtomicBoolean(false);
            IStoryServer.get()
               .postCancellableStoryEvent(EventType.ENTITY_KILL, new EntityInteractionData((Player)killer, entity, () -> prevent.set(true)), e -> prevent.get());
            if (prevent.get()) {
               event.setCanceled(true);
               return;
            }
         }
      }
   }

   @SubscribeEvent
   public static void onLivingHurt(LivingHurtEvent event) {
      LivingEntity entity = event.getEntity();
      Entity killer = event.getSource().getEntity();
      if (!entity.level().isClientSide) {
         if (entity instanceof Player p && p.isCreative()) {
            event.setCanceled(true);
         }

         IStoryServer.get().postStoryEvent(EventType.LIVING_HURT_DIRECT, event);
         if (!event.isCanceled()) {
            if (killer instanceof Player) {
               AtomicBoolean prevent = new AtomicBoolean(false);
               IStoryServer.get()
                  .postCancellableStoryEvent(
                     EventType.ENTITY_HIT, new EntityInteractionData((Player)killer, entity, () -> prevent.set(true)), e -> prevent.get()
                  );
               if (prevent.get()) {
                  event.setCanceled(true);
               }
            }
         }
      }
   }

   @SubscribeEvent
   public static void onLivingDamage(LivingKnockBackEvent e) {
      LivingEntity entity = e.getEntity();
      if (!entity.level().isClientSide) {
         IStoryServer.get().postStoryEvent(EventType.LIVING_KNOCK_BACK, e);
      }
   }

   @SubscribeEvent
   public static void onChatMessage(ServerChatEvent event) {
      IStoryServer.get().postStoryEvent(EventType.CHAT_MESSAGE_SENT, event.getMessage().getString());
   }

   @SubscribeEvent
   public static void wakeUp(PlayerWakeUpEvent e) {
      IStoryServer.get().postStoryEvent(EventType.PLAYER_WAKE_UP, new EntityInteractionData(e.getEntity()));
   }

   @SubscribeEvent
   public static void blockPlace(EntityPlaceEvent e) {
      if (!e.getLevel().isClientSide()) {
         IStoryServer.get().postStoryEvent(EventType.BLOCK_PLACE, e.getPlacedBlock().getBlock());
      }
   }

   @SubscribeEvent
   public static void blockClick(RightClickBlock e) {
      if (!e.getLevel().isClientSide()) {
         IStoryServer.get().postStoryEvent(EventType.BLOCK_CLICK, e);
      }
   }

   @SubscribeEvent
   public static void blockDestoy(BreakEvent e) {
      if (!e.getLevel().isClientSide()) {
         IStoryServer.get().postStoryEvent(EventType.BLOCK_DESTROY, e);
      }
   }

   @SubscribeEvent
   public static void itemClick(RightClickItem e) {
      if (!e.getLevel().isClientSide()) {
         IStoryServer.get().postStoryEvent(EventType.ITEM_CLICK, e);
      }
   }

   @SubscribeEvent
   public static void checkDespawn(AllowDespawn event) {
      if (event.getEntity() instanceof IStoryEntity e && e.isStorytellingEntity()) {
         event.setResult(Result.DENY);
      }
   }

   @SubscribeEvent
   public static void onServerStopped(ServerStoppingEvent event) {
      IStoryServer.get().getScriptEnv().unloadScripts();
   }

   @SubscribeEvent
   public static void onKeyPress(Key event) {
      if (event.getAction() == 1 && Minecraft.getInstance().getConnection() != null) {
         Network.sendToServer(new PacketKeyInputEvent(event.getKey()));
      }
   }

   @SubscribeEvent
   public static void onMousePress(Pre event) {
      if (event.getAction() == 1 && Minecraft.getInstance().getConnection() != null) {
         Network.sendToServer(new PacketMouseInputEvent(event.getButton()));
      }
   }

   @SubscribeEvent
   public static void onSwapHands(Hands event) {
      if (event.getEntity() instanceof Player player && !player.isCreative() && player.getInventory().selected == 8) {
         event.setCanceled(true);
      }
   }
}
