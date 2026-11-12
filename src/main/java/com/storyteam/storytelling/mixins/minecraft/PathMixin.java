package com.storyteam.storytelling.mixins.minecraft;

import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({Path.class})
public class PathMixin {
   @Shadow
   @Final
   private List<Node> nodes;

   @Overwrite
   public Vec3 getEntityPosAtNode(Entity pEntity, int pIndex) {
      Node $$2 = this.nodes.get(pIndex);
      double $$3 = (double)$$2.x + 0.5;
      double $$4 = (double)$$2.y;
      double $$5 = (double)$$2.z + 0.5;
      return new Vec3($$3, $$4, $$5);
   }
}
