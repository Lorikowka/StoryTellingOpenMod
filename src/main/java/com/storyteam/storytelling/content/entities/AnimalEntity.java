package com.storyteam.storytelling.content.entities;

import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.events.FeedAnimalEvent;
import com.storyteam.storytelling.util.ParticleHelper;
import com.storyteam.storytelling.util.ScriptUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class AnimalEntity extends AnimatedEntity {
   HumanoidEntity humanoidEntity = null;
   protected Item food = Items.WHEAT;
   protected final int HUNGRY_DELAY = 6000;
   private int hungry_delay = 6000;
   int randomtick = this.random.nextInt(20);

   public AnimalEntity(EntityType<? extends AnimatedEntity> type, Level world) {
      super(type, world);
   }

   public void aiStep() {
      super.aiStep();
      if (!this.level().isClientSide && this.isAlive()) {
         ServerLevel level = ScriptUtils.getLevel();
         this.humanoidEntity = null;

         for (Entity entity : level.getEntities().getAll()) {
            if (entity instanceof HumanoidEntity && entity.distanceTo(this) < 10.0F) {
               if (this.humanoidEntity == null) {
                  this.humanoidEntity = (HumanoidEntity)entity;
               }

               if (this.humanoidEntity.distanceTo(this) > entity.distanceTo(this)) {
                  this.humanoidEntity = (HumanoidEntity)entity;
               }
            }
         }

         if (this.humanoidEntity != null && this.humanoidEntity.getItemRightHand().getItem() == this.food) {
            if ((double)this.humanoidEntity.distanceTo(this) > 1.5 && this.humanoidEntity.distanceTo(this) < 10.0F) {
               this.getNavigation().moveTo(this.humanoidEntity, 0.3F);
            } else {
               this.getNavigation().stop();
            }

            this.lookAt(this.humanoidEntity, 360.0F, 360.0F);
         } else if (this.tickCount % (90 + this.randomtick) == 0) {
            this.randomtick = this.random.nextInt(20);
            double posX = this.getX() + (this.getRandom().nextDouble() - 0.5) * 4.0;
            double posY = this.getY();
            double posZ = this.getZ() + (this.getRandom().nextDouble() - 0.5) * 4.0;
            this.getNavigation().moveTo(posX, posY, posZ, 0.3);
         }
      }
   }

   @NotNull
   protected InteractionResult mobInteract(@NotNull Player pPlayer, @NotNull InteractionHand pHand) {
      ItemStack item;
      if (pHand == InteractionHand.MAIN_HAND) {
         item = pPlayer.getMainHandItem();
      } else {
         item = pPlayer.getOffhandItem();
      }

      if (item.getItem().equals(this.food) && this.hungry_delay >= 6000) {
         ParticleHelper.spawnParticleEntity(ScriptUtils.getLevel(), ParticleTypes.HEART, this, 10, 0.2F);
         this.hungry_delay = 0;
         Storytelling.BUS.post(new FeedAnimalEvent(this, pPlayer, item));
         pPlayer.getMainHandItem().shrink(1);
      }

      return super.mobInteract(pPlayer, pHand);
   }

   @Override
   public void tick() {
      this.hungry_delay++;
      super.tick();
   }
}
