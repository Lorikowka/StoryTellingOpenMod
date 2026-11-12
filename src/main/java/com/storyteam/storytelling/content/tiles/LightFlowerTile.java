package com.storyteam.storytelling.content.tiles;

import com.storyteam.storytelling.init.TilesST;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.zeith.hammeranims.api.animsys.AnimationSystem;

public class LightFlowerTile extends BaseAnimatedTile<LightFlowerTile> {
   public final AnimationSystem animations = AnimationSystem.create(this);
   private static final Random random = new Random();
   private static final int LIGHTNING_INTERVAL_MIN = 120;
   private static final int LIGHTNING_INTERVAL_MAX = 200;
   private int lightningCooldown = 0;

   public LightFlowerTile(BlockPos pos, BlockState state) {
      super(TilesST.LIGHTFLOWER, pos, state);
      this.resetLightningCooldown();
   }

   @Override
   public void update() {
      super.update();
      if (this.level != null && !this.level.isClientSide) {
         this.tickLightning();
      }
   }

   private void tickLightning() {
      if (this.lightningCooldown > 0) {
         this.lightningCooldown--;
      } else if (this.level != null) {
         LightningBolt bolt = (LightningBolt)EntityType.LIGHTNING_BOLT.create(this.level);
         if (bolt != null) {
            bolt.moveTo((double)this.getBlockPos().getX() + 0.5, (double)this.getBlockPos().getY() + 0.7, (double)this.getBlockPos().getZ() + 0.5);
            bolt.setVisualOnly(true);
            this.level.addFreshEntity(bolt);
         }

         this.resetLightningCooldown();
      }
   }

   private void resetLightningCooldown() {
      int minTicks = 2400;
      int maxTicks = 4000;
      this.lightningCooldown = random.nextInt(minTicks, maxTicks);
   }

   public AABB getRenderBoundingBox() {
      return INFINITE_EXTENT_AABB;
   }
}
