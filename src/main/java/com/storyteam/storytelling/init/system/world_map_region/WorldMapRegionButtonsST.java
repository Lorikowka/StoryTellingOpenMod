package com.storyteam.storytelling.init.system.world_map_region;

import com.storyteam.storytelling.system.simulator.world_tab.data.WMRButton;
import com.storyteam.storytelling.system.simulator.world_tab.ui.WorldTabConstants;
import com.storyteam.storytelling.util.ScriptUtils;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cod;

public class WorldMapRegionButtonsST {
   public static final WMRButton TELEPORT = WMRButton.builder()
      .name("teleport")
      .action(region -> region.getWorldMapRegionInfo().getTeleportLocation().teleport(ScriptUtils.getPlayer()))
      .enabled(true)
      .texture(WorldTabConstants.MAP_REGION_ICON_4)
      .price(100)
      .build();
   public static final WMRButton FISH = WMRButton.builder().name("fish").action(region -> {
      Cod cod = new Cod(EntityType.COD, ScriptUtils.getLevel());
      cod.setPos(ScriptUtils.getPlayer().position());
      ScriptUtils.getLevel().addFreshEntity(cod);
   }).enabled(false).texture(WorldTabConstants.MAP_REGION_ICON_5).price(0).build();
}
