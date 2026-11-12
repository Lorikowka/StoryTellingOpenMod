package com.storyteam.storytelling.client.particle;

import com.storyteam.storytelling.client.particle.controllers.ControllerData;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.world.phys.Vec3;

public abstract class StoryTexturedSheetParticle extends TextureSheetParticle {
   public ControllerData data;

   protected StoryTexturedSheetParticle(
      ControllerData data, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed
   ) {
      super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
      this.data = data;
   }

   public void tick() {
      this.xo = this.x;
      this.yo = this.y;
      this.zo = this.z;
      if (this.age++ >= this.lifetime) {
         this.remove();
      } else {
         Vec3 dmove = this.data
            .<ControllerData>getType()
            .handleMovement(
               new ParticleInfo(
                  this,
                  this.level,
                  new Vec3(this.xd, this.yd, this.zd),
                  new Vec3(this.x, this.y, this.z),
                  new Vec3(this.xo, this.yo, this.zo),
                  this.random,
                  this.onGround,
                  this.bbWidth,
                  this.bbHeight,
                  (float)this.lifetime,
                  (float)this.age,
                  this.gravity,
                  this.friction,
                  this.getBoundingBox()
               ),
               this.data
            );
         this.xd = dmove.x;
         this.yd = dmove.y;
         this.zd = dmove.z;
         this.move(this.xd, this.yd, this.zd);
      }
   }
}
