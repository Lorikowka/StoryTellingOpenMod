package com.storyteam.storytelling.system.simulator.main_page.tiles;

import com.storyteam.storytelling.client.animations.AnimationsST8;
import com.storyteam.storytelling.content.tiles.TileLinkable;
import com.storyteam.storytelling.system.simulator.main_page.MainTabStorage;
import com.storyteam.storytelling.system.simulator.main_page.MainTabType;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.zeith.hammeranims.api.animsys.AnimationSystem;
import org.zeith.hammeranims.api.animsys.AnimationSystem.Builder;
import org.zeith.hammeranims.api.tile.IAnimatedTile;

public class SimulatorTile extends TileLinkable implements IAnimatedTile {
   public final AnimationSystem animationSystem = AnimationSystem.create(this);

   public SimulatorTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
      super(type, pos, state);
   }

   @Nullable
   public Component getDisplayName() {
      return Component.empty();
   }

   public AABB getRenderBoundingBox() {
      return INFINITE_EXTENT_AABB;
   }

   public void update() {
      this.animationSystem.tick();
      if (this.level.isClientSide()) {
         boolean blocked = MainTabStorage.isBlocked(MainTabType.MAIN);
         if (blocked) {
            this.animationSystem.stopAnimation("action");
            this.animationSystem.stopAnimation("ambient");
         } else {
            if (!this.animationSystem.isActiveAnimation("ambient", AnimationsST8.SIMULATOR_IDLE)) {
               this.animationSystem.startAnimationAt("ambient", AnimationsST8.SIMULATOR_IDLE);
            }

            if (!this.animationSystem.isActiveAnimation("action", AnimationsST8.SIMULATOR_HOLO_IDLE)) {
               this.animationSystem.startAnimationAt("action", AnimationsST8.SIMULATOR_HOLO_IDLE);
            }
         }
      } else if (this.ticksExisted % 20 == 0) {
         for (Player player : this.level.getEntitiesOfClass(Player.class, new AABB(this.getBlockPos()).inflate(4.0))) {
            player.heal(1.0F);
         }
      }
   }

   public void setupSystem(Builder builder) {
      builder.addLayers(new String[]{"action"}).disableSync();
      builder.addLayers(new String[]{"ambient"}).disableSync();
   }

   public AnimationSystem getAnimationSystem() {
      return this.animationSystem;
   }
}
