package com.storyteam.storytelling.system.simulator.me_tab.content.entity;

import com.storyteam.storytelling.content.entities.HumanoidEntity;
import com.storyteam.storytelling.system.animation.EyeGazeMeTabSystem;
import com.storyteam.storytelling.util.LayersList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.zeith.hammeranims.api.animation.interp.BlendMode;

public class HumanoidMeTabEntity extends HumanoidEntity {
   public HumanoidMeTabEntity(EntityType<? extends HumanoidEntity> type, Level world) {
      super(type, world);
   }

   @Override
   public void addLayers(LayersList layers) {
      layers.addLast("EYES", BlendMode.ADD, 1.0F);
      layers.addLast("EMOTION_COMBINE", BlendMode.ADD, 1.0F);
      this.eyeGaze = new EyeGazeMeTabSystem(this, "EyeGazeMeTab");
      this.eyeGaze.setupLayers(layers);
      layers.addLast("emotion", BlendMode.ADD, 1.0F);
   }
}
