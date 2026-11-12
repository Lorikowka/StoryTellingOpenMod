package com.storyteam.storytelling.system.rts.init;

import com.solegendary.reignofnether.unit.interfaces.Unit;
import com.storyteam.storytelling.client.render.entity.def.impl.CommonRenderDefinition;
import com.storyteam.storytelling.content.entities.configurations.Sex;
import com.storyteam.storytelling.init.EntitiesST;
import com.storyteam.storytelling.system.rts.units.AntUnit;
import com.storyteam.storytelling.system.rts.units.api.data.FormikProfession;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;

@SimplyRegister(
   prefix = "system/rts/unit_entity_type/"
)
public interface RTSUnitEntityTypesST {
   @RegistryName("ant")
   EntityType<AntUnit> ANT = EntitiesST.createAnimated(AntUnit::new, Sex.STEVE)
      .model(FormikProfession.WORKER.getGeo())
      .texture(FormikProfession.WORKER.getTx())
      .renderProp(CommonRenderDefinition.RENDERER, CommonRenderDefinition.Renderer.DynamicModelWithEmissiveRenderer)
      .attributes(
         () -> Mob.createMobAttributes()
               .add(Attributes.ATTACK_DAMAGE, 1.0)
               .add(Attributes.MOVEMENT_SPEED, 0.25)
               .add(Attributes.MAX_HEALTH, 25.0)
               .add(Attributes.FOLLOW_RANGE, (double)Unit.getFollowRange())
               .add(Attributes.ARMOR, 0.0)
               .add(RTSAttributesST.AGGRO_RANGE, 0.0)
               .add(RTSAttributesST.ATTACK_RANGE, 2.0)
               .add(RTSAttributesST.ATTACKS_PER_SECOND, 0.5)
               .add(RTSAttributesST.MAX_RESOURCES, 100.0)
               .add(RTSAttributesST.FOOD_CONSUMPTION, 4.0)
               .build()
      )
      .build();
}
