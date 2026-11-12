package com.storyteam.storytelling.events.listeners;

import com.storyteam.storytelling.api.itf.IStoryEntity;
import com.storyteam.storytelling.util.ScriptUtils;
import java.util.Collection;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent.FinalizeSpawn;
import net.minecraftforge.event.entity.living.MobSpawnEvent.SpawnPlacementCheck;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.event.level.BlockEvent.CropGrowEvent.Pre;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class MiscHooks {
   public static ResourceKey<Level> world = null;

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public static void onRealDimensionChange(PlayerChangedDimensionEvent event) {
      world = event.getTo();
      Collection<MobEffectInstance> list = event.getEntity().getActiveEffects().stream().toList();
      event.getEntity().removeAllEffects();

      for (MobEffectInstance effect : list) {
         event.getEntity().addEffect(effect);
      }
   }

   @SubscribeEvent
   public static void onDimensionChange(PlayerTickEvent event) {
      if (event.side == LogicalSide.SERVER) {
         if (world == null) {
            world = event.player.level().dimension();
         }

         if (!world.equals(event.player.level().dimension())) {
            MinecraftForge.EVENT_BUS.post(new PlayerChangedDimensionEvent(event.player, world, event.player.level().dimension()));
         }
      }
   }

   @SubscribeEvent
   public static void deleteLeftHandSlot(PlayerTickEvent event) {
      Player player = event.player;
      if (!player.level().isClientSide && ScriptUtils.isLeftHandDisabled() && !player.getOffhandItem().isEmpty()) {
         ItemEntity item = new ItemEntity(player.level(), player.getX(), player.getY() + 1.0, player.getZ(), player.getOffhandItem().copy());
         player.level().addFreshEntity(item);
         player.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
      }
   }

   @SubscribeEvent
   public static void checkSpawn(SpawnPlacementCheck event) {
      if (event.getSpawnType() != MobSpawnType.COMMAND && event.getSpawnType() != MobSpawnType.SPAWN_EGG
         || event.getEntityType().equals(EntityType.PHANTOM)
         || event.getEntityType().equals(EntityType.SILVERFISH)) {
         event.setResult(Result.DENY);
      }
   }

   @SubscribeEvent
   public static void onSpawn(FinalizeSpawn event) {
      if (event.getSpawnType() != MobSpawnType.COMMAND && event.getSpawnType() != MobSpawnType.SPAWN_EGG && event.getEntity() instanceof LivingEntity) {
         IStoryEntity storyEntity = IStoryEntity.of(event.getEntity());
         if (storyEntity != null && !storyEntity.isStorytellingEntity()) {
            event.setSpawnCancelled(true);
         }
      }
   }

   @SubscribeEvent
   public static void onCropGrowth(Pre event) {
      event.setResult(Result.DENY);
   }
}
