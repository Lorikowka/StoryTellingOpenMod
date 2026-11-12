package com.storyteam.storytelling.content.entities.base;

import com.storyteam.storytelling.content.entities.HumanoidEntity;
import java.util.Optional;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.phys.Vec3;

public class HeadEntityLookControl extends LookControl {
   protected final HumanoidEntity human;

   public HeadEntityLookControl(HumanoidEntity pMob) {
      super(pMob);
      this.human = pMob;
   }

   protected Optional<Float> getXRotD() {
      Vec3 eyePos = this.human.getEyePosition();
      double d0 = this.wantedX - eyePos.x();
      double d1 = this.wantedY - eyePos.y();
      double d2 = this.wantedZ - eyePos.z();
      double d3 = Math.sqrt(d0 * d0 + d2 * d2);
      return !(Math.abs(d1) > 1.0E-5F) && !(Math.abs(d3) > 1.0E-5F) ? Optional.empty() : Optional.of((float)(-(Mth.atan2(d1, d3) * 180.0F / (float)Math.PI)));
   }

   protected Optional<Float> getYRotD() {
      Vec3 eyePos = this.human.getEyePosition();
      double d0 = this.wantedX - eyePos.x();
      double d1 = this.wantedZ - eyePos.z();
      return !(Math.abs(d1) > 1.0E-5F) && !(Math.abs(d0) > 1.0E-5F)
         ? Optional.empty()
         : Optional.of((float)(Mth.atan2(d1, d0) * 180.0F / (float)Math.PI) - 90.0F);
   }
}
