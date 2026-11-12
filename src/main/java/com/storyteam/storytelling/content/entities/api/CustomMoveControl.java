package com.storyteam.storytelling.content.entities.api;

import com.storyteam.storytelling.content.entities.AnimatedEntity;
import java.util.Iterator;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraft.world.phys.Vec3;

public class CustomMoveControl extends MoveControl {
   AnimatedEntity animEntity;
   private static final List<Class<? extends Block>> exceptions = List.of(TrapDoorBlock.class);

   public CustomMoveControl(AnimatedEntity pMob) {
      super(pMob);
      this.animEntity = pMob;
   }

   public void tick() {
      if (this.operation == Operation.STRAFE) {
         float f = (float)this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED);
         float f1 = (float)this.speedModifier * f;
         float f2 = this.strafeForwards;
         float f3 = this.strafeRight;
         float f4 = Mth.sqrt(f2 * f2 + f3 * f3);
         if (f4 < 1.0F) {
            f4 = 1.0F;
         }

         f4 = f1 / f4;
         f2 *= f4;
         f3 *= f4;
         float f5 = Mth.sin(this.mob.getYRot() * (float) (Math.PI / 180.0));
         float f6 = Mth.cos(this.mob.getYRot() * (float) (Math.PI / 180.0));
         float f7 = f2 * f6 - f3 * f5;
         float f8 = f3 * f6 + f2 * f5;
         if (!this.isWalkable(f7, f8)) {
            this.strafeForwards = 1.0F;
            this.strafeRight = 0.0F;
         }

         this.mob.setSpeed(f1);
         this.mob.setZza(this.strafeForwards);
         this.mob.setXxa(this.strafeRight);
         this.operation = Operation.WAIT;
      } else if (this.operation == Operation.MOVE_TO) {
         this.operation = Operation.WAIT;
         double d0 = this.wantedX - this.mob.getX();
         double d1 = this.wantedZ - this.mob.getZ();
         double d2 = this.wantedY - this.mob.getY();
         double d3 = d0 * d0 + d2 * d2 + d1 * d1;
         if (d3 < 2.5000003E-7F) {
            this.mob.setZza(0.0F);
            return;
         }

         float speed = (float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
         float f9 = (float)(Mth.atan2(d1, d0) * 180.0F / (float)Math.PI) - 90.0F;
         this.mob.setYRot(this.rotlerp(this.mob.getYRot(), f9, 80.0F));
         if (Math.abs(Mth.wrapDegrees(this.mob.getYRot() - f9)) > 50.0F) {
            speed /= 1.5F;
         }

         this.mob.setSpeed(speed);
         Vec3 wantedVec = new Vec3(this.wantedX, this.wantedY, this.wantedZ);
         BlockPos wantedPos = BlockPos.containing(this.mob.position().add(wantedVec.subtract(this.mob.position()).normalize().scale(0.3F)));
         BlockState wantedState = this.mob.level().getBlockState(wantedPos);
         if (this.animEntity.canOpenDoors()) {
            Class<? extends Block> wantedBlockClass = wantedState.getBlock().getClass();
            if (wantedState.hasProperty(DoorBlock.OPEN) && exceptions.stream().noneMatch(exp -> exp.isAssignableFrom(wantedBlockClass))) {
               this.animEntity.openedDoors.add(GlobalPos.of(this.mob.level().dimension(), wantedPos));
               if (wantedState.getBlock() instanceof DoorBlock door) {
                  door.setOpen(this.mob, this.mob.level(), wantedState, wantedPos, true);
               } else {
                  wantedState = (BlockState)wantedState.setValue(DoorBlock.OPEN, true);
                  this.mob.level().setBlock(wantedPos, wantedState, 10);
               }
            }

            Iterator<GlobalPos> openedDoorsItr = this.animEntity.openedDoors.iterator();

            while (openedDoorsItr.hasNext()) {
               GlobalPos pos = openedDoorsItr.next();
               if (this.mob.position().distanceToSqr(Vec3.atCenterOf(pos.pos())) > 1.75) {
                  openedDoorsItr.remove();
                  BlockState doorState = this.mob.level().getBlockState(pos.pos());
                  if (doorState.hasProperty(DoorBlock.OPEN)) {
                     if (doorState.getBlock() instanceof DoorBlock door) {
                        door.setOpen(this.mob, this.mob.level(), doorState, pos.pos(), false);
                     } else {
                        doorState = (BlockState)doorState.setValue(DoorBlock.OPEN, false);
                        this.mob.level().setBlock(pos.pos(), doorState, 10);
                     }
                  }
               }
            }
         }
      } else if (this.operation == Operation.JUMPING) {
         this.mob.setSpeed((float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
         if (this.mob.onGround()) {
            this.operation = Operation.WAIT;
         }
      } else {
         this.mob.setZza(0.0F);
      }
   }

   private boolean isWalkable(float pRelativeX, float pRelativeZ) {
      PathNavigation nav = this.mob.getNavigation();
      NodeEvaluator eval = nav.getNodeEvaluator();
      return eval.getBlockPathType(
            this.mob.level(), Mth.floor(this.mob.getX() + (double)pRelativeX), this.mob.getBlockY(), Mth.floor(this.mob.getZ() + (double)pRelativeZ)
         )
         == BlockPathTypes.WALKABLE;
   }
}

