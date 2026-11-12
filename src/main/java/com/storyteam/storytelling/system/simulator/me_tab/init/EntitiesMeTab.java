package com.storyteam.storytelling.system.simulator.me_tab.init;

import com.storyteam.storytelling.St;
import com.storyteam.storytelling.client.render.entity.def.RenderKey;
import com.storyteam.storytelling.client.render.entity.def.impl.CommonRenderDefinition;
import com.storyteam.storytelling.content.entities.configurations.IDefaultAnimConfiguration;
import com.storyteam.storytelling.init.EntitiesST;
import com.storyteam.storytelling.init.geo.EntityModelsST;
import com.storyteam.storytelling.system.simulator.me_tab.content.entity.HumanoidMeTabEntity;
import net.minecraft.world.entity.EntityType;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;

@SimplyRegister(
   prefix = "system/simulator/me_tab/"
)
public interface EntitiesMeTab {
   @RegistryName("lo_me_tab_head")
   EntityType<HumanoidMeTabEntity> LO_ME_TAB_HEAD = EntitiesST.createAnimated(HumanoidMeTabEntity::new, IDefaultAnimConfiguration.noAnims())
      .model(EntityModelsST.LOLOLOSHKA)
      .texture(St.tx("entity/system/simulator/me_tab/lololoshka_metab_head"))
      .renderProp(CommonRenderDefinition.RENDERER, CommonRenderDefinition.Renderer.HumanoidMeTabRenderer)
      .renderer(CommonRenderDefinition::new)
      .renderProp(RenderKey.KEY, RenderKey.entityTranslucent)
      .build();
}
