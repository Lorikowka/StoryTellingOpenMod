package com.storyteam.storytelling.content.tiles;

import com.storyteam.storytelling.content.blocks.LevitationDustBlock;
import com.storyteam.storytelling.init.TilesST;
import com.storyteam.storytelling.util.Color;
import com.storyteam.storytelling.util.ParticleHelper;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.zeith.hammerlib.tiles.TileSyncableTickable;

public class LevitationDustTile extends TileSyncableTickable {
   private static int[] BASE_COLORS = new int[]{16777215, 13430527, 11198463};
   private static final int FALL_DAMAGE_SAFE_TIME_PER_BLOCK = 5;
   private final Map<UUID, Integer> recentPlayers = new HashMap<>();

   public LevitationDustTile(BlockPos pos, BlockState state) {
      super(TilesST.LEVITATION_DUST, pos, state);
   }

   public void update() {
      if (this.level != null) {
         float liftHeight = (float)((Integer)this.getBlockState().getValue(LevitationDustBlock.LIFT_HEIGHT)).intValue();
         Vec3 basePos = Vec3.atCenterOf(this.worldPosition).add(0.0, -0.5, 0.0);
         if (this.ticksExisted % 2 == 0) {
            this.spawnParticles(this.level, basePos, liftHeight);
         }

         this.applyLevitation(this.level, basePos, liftHeight);
         this.handleRecentPlayers();
      }
   }

   private void applyLevitation(Level level, Vec3 basePos, float liftHeight) {
      AABB liftArea = new AABB(basePos.x - 0.5, basePos.y, basePos.z - 0.5, basePos.x + 0.5, basePos.y + (double)liftHeight, basePos.z + 0.5);

      for (Player player : level.getEntitiesOfClass(Player.class, liftArea)) {
         double relativeY = player.getY() - basePos.y;
         double liftStrength = 0.3 - relativeY / (double)liftHeight * 0.2;
         if (liftStrength < 0.05) {
            liftStrength = 0.05;
         }

         Vec3 motion = player.getDeltaMovement();
         double targetY = Math.max(motion.y(), liftStrength);
         player.setDeltaMovement(motion.x() * 0.9, targetY, motion.z() * 0.9);
         player.fallDistance = 0.0F;
         this.recentPlayers.put(player.getUUID(), 5 * (int)liftHeight);
      }
   }

   private void spawnParticles(Level level, Vec3 basePos, float liftHeight) {
      RandomSource random = level.getRandom();

      for (int i = 0; i < 15; i++) {
         double x = basePos.x + (random.nextDouble() - 0.5);
         double y = basePos.y + random.nextDouble() * (double)liftHeight;
         double z = basePos.z + (random.nextDouble() - 0.5);
         double moveY = 0.01 + random.nextDouble() * 0.005;
         int base = BASE_COLORS[random.nextInt(BASE_COLORS.length)];
         Vector3f colorVec = this.getColorVector(base, random);
         float alpha = 0.4F + random.nextFloat() * 0.3F;
         float scale = 0.2F + random.nextFloat() * 0.2F;
         int lifetime = 30 + random.nextInt(20);
         ParticleHelper.spawnEnginedParticle(
            level,
            new DustParticleOptions(colorVec, 0.5F),
            new Vec3(x, y, z),
            0.0,
            moveY,
            0.0,
            scale,
            lifetime,
            new Color(colorVec.x(), colorVec.y(), colorVec.z()),
            alpha
         );
      }
   }

   private void handleRecentPlayers() {
      if (!this.recentPlayers.isEmpty()) {
         Iterator<Entry<UUID, Integer>> it = this.recentPlayers.entrySet().iterator();

         while (it.hasNext()) {
            Entry<UUID, Integer> entry = it.next();
            Player player = this.level.getPlayerByUUID(entry.getKey());
            if (player != null) {
               player.fallDistance = 0.0F;
            }

            int remaining = entry.getValue() - 1;
            if (remaining <= 0) {
               it.remove();
            } else {
               entry.setValue(remaining);
            }
         }
      }
   }

   private Vector3f getColorVector(int baseColor, RandomSource rand) {
      int r = baseColor >> 16 & 0xFF;
      int g = baseColor >> 8 & 0xFF;
      int b = baseColor & 0xFF;
      float variation = 0.9F + rand.nextFloat() * 0.2F;
      float rf = this.clamp((float)r / 255.0F * variation, 0.0F, 1.0F);
      float gf = this.clamp((float)g / 255.0F * variation, 0.0F, 1.0F);
      float bf = this.clamp((float)b / 255.0F * variation, 0.0F, 1.0F);
      return new Vector3f(rf, gf, bf);
   }

   private float clamp(float value, float min, float max) {
      return Math.max(min, Math.min(max, value));
   }

   public AABB getRenderBoundingBox() {
      return INFINITE_EXTENT_AABB;
   }
}
