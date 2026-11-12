package com.storyteam.storytelling.script.story.npc;

import com.mojang.authlib.GameProfile;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;

/** Uses a block through Forge's normal server-side player interaction path. */
public final class NpcWorldInteraction {
   private static final GameProfile PROFILE = new GameProfile(
      UUID.nameUUIDFromBytes("storytelling:script_npc".getBytes(StandardCharsets.UTF_8)), "[Story NPC]");
   private static final double REACH_SQUARED = 4.5 * 4.5;

   private NpcWorldInteraction() { }

   public static void useBlock(Entity npc, BlockPos block, String dimension) {
      if (npc == null || !(npc.level() instanceof ServerLevel level)) {
         throw new IllegalStateException("NPC must be spawned on the server before npc_use_block");
      }
      if (!level.dimension().location().toString().equals(dimension)) {
         throw new IllegalStateException("NPC and target block are in different dimensions: " + dimension);
      }
      if (!level.hasChunkAt(block) || level.getBlockState(block).isAir()) {
         throw new IllegalStateException("No loaded block to use at " + block.toShortString());
      }
      Vec3 center = Vec3.atCenterOf(block);
      if (npc.getEyePosition().distanceToSqr(center) > REACH_SQUARED) {
         throw new IllegalStateException("NPC is too far from block " + block.toShortString() + "; move it closer first");
      }

      double dx = npc.getX() - center.x;
      double dz = npc.getZ() - center.z;
      Direction face = Math.abs(dx) >= Math.abs(dz)
         ? (dx >= 0 ? Direction.EAST : Direction.WEST)
         : (dz >= 0 ? Direction.SOUTH : Direction.NORTH);
      Vec3 hitPoint = center.add(face.getStepX() * 0.5, 0, face.getStepZ() * 0.5);
      BlockHitResult hit = new BlockHitResult(hitPoint, face, block, false);
      FakePlayer player = FakePlayerFactory.get(level, PROFILE);
      player.setPos(npc.getX(), npc.getY(), npc.getZ());
      player.setYRot(npc.getYRot());
      player.setXRot(npc.getXRot());
      player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
      InteractionResult result = player.gameMode.useItemOn(player, level, ItemStack.EMPTY, InteractionHand.MAIN_HAND, hit);
      if (!result.consumesAction()) {
         throw new IllegalStateException("Block did not accept NPC interaction at " + block.toShortString());
      }
      if (npc instanceof LivingEntity living) living.swing(InteractionHand.MAIN_HAND);
   }
}
