package com.storyteam.storytelling.system.simulator.world_tab.ui;

import com.storyteam.storytelling.client.screen.base.IAutoScaled;
import com.storyteam.storytelling.system.octospizdel.client.animation.Tween;
import com.storyteam.storytelling.system.octospizdel.client.animation.easing.EaseType;
import com.storyteam.storytelling.system.octospizdel.client.animation.easing.TransitionType;
import com.storyteam.storytelling.system.simulator.world_tab.data.WorldMapRegion;
import com.storyteam.storytelling.system.simulator.world_tab.data.WorldMapRegionIcon;
import com.storyteam.storytelling.system.simulator.world_tab.data.WorldMapRegionsStorage;
import com.storyteam.storytelling.system.simulator.world_tab.ui.elements.GuiRegionIconObject;
import com.storyteam.storytelling.system.simulator.world_tab.ui.elements.GuiRegionInfoObject;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.zeith.hammerlib.client.flowgui.GuiObject;
import org.zeith.hammerlib.client.flowgui.objects.GuiImageObject;
import org.zeith.hammerlib.client.flowgui.objects.GuiRootObject;
import org.zeith.hammerlib.client.flowgui.objects.GuiButtonObject.OnPress;
import org.zeith.hammerlib.util.math.Point;

public class WorldMainTab extends Screen implements IAutoScaled {
   protected GuiRootObject root;
   protected GuiObject background;
   private GuiRegionInfoObject infoObject = null;
   private Tween tween;

   public WorldMainTab() {
      super(Component.empty());
   }

   public GuiRootObject createRoot() {
      GuiRootObject root = GuiObject.root();
      this.background = this.mapBackground();
      GuiObject regions = this.generateRegions();
      this.background.addChild(regions);
      root.add(this.background);
      return root;
   }

   private GuiObject mapBackground() {
      return GuiObject.create("root_bg")
         .image(WorldTabConstants.BW_MAP, 0.0F, 0.0F, 480.0F, 270.0F, 480.0F, 270.0F)
         .scale(1.3333334F)
         .pos(((float)this.width - 640.0F) / 2.0F, ((float)this.height - 360.0F) / 2.0F)
         .pivotAtCenter();
   }

   private GuiObject generateRegions() {
      GuiObject regions = GuiObject.create("regions").empty();
      GuiObject region_icons = GuiObject.create("region_icons").empty();

      for (WorldMapRegion region : WorldMapRegionsStorage.WORLD_MAP_REGIONS.values()) {
         if (region.getState() != 0) {
            GuiImageObject regionGui = GuiObject.create(region.getId().getPath())
               .image(region.getTexture(), 0.0F, 0.0F, (float)region.getWidth(), (float)region.getHeight(), (float)region.getWidth(), (float)region.getHeight());
            regionGui.alpha = region.getState() > 1 ? 1.0F : 0.0F;
            regions.addChild(regionGui.pos((float)region.getXPos(), (float)region.getYPos()));
            region_icons.addChild(this.getRegionIcon(region));
         }
      }

      region_icons.scale(0.5F);
      regions.addChild(region_icons);
      return regions;
   }

   private GuiObject getRegionIcon(WorldMapRegion region) {
      WorldMapRegionIcon WMRIcon = region.getWorldMapRegionIcon();
      return GuiRegionIconObject.regionIconBuilder()
         .name(region.getId().getPath() + "_icon")
         .message(Component.empty())
         .customTexture(WMRIcon.iconTexture())
         .callback(this.showRegionInfo(region))
         .defaultPos(new Point((float)WMRIcon.XPos(), (float)WMRIcon.YPos()))
         .alpha(1.0F)
         .enabled(true)
         .build()
         .zOffset(2.0F)
         .size((float)WMRIcon.width(), (float)WMRIcon.height())
         .pos((float)WMRIcon.XPos(), (float)WMRIcon.YPos());
   }

   private OnPress showRegionInfo(WorldMapRegion region) {
      return b -> {
         this.background.removeChild("info");
         this.infoObject = new GuiRegionInfoObject(region);
         this.background.addChild(this.infoObject);
      };
   }

   protected void init() {
      super.init();
      this.root = (GuiRootObject)this.addRenderableWidget(this.createRoot());
   }

   public boolean shouldCloseOnEsc() {
      if (this.infoObject != null && this.tween == null) {
         this.infoObject.tween.kill();
         this.tween = Tween.create().setTransitionType(TransitionType.CIRC).setEase(EaseType.EASE_IN_OUT).setLoops(1);
         this.tween
            .<Double>tweenMethod(x -> this.infoObject.setSize(x), this.infoObject.getScale(), 0.0, 0.4)
            .setTransitionType(TransitionType.CUBIC)
            .setEaseType(EaseType.EASE_OUT);
         this.tween
            .parallel()
            .tweenMethod(this.infoObject::setXPos, this.infoObject.getPos().x(), this.infoObject.startPoint.x(), 0.4)
            .setTransitionType(TransitionType.CUBIC)
            .setEaseType(EaseType.EASE_OUT);
         this.tween
            .parallel()
            .tweenMethod(this.infoObject::setYPos, this.infoObject.getPos().y(), this.infoObject.startPoint.y(), 0.4)
            .setTransitionType(TransitionType.CUBIC)
            .setEaseType(EaseType.EASE_OUT);
         this.tween.tweenRunnable(() -> {
            this.infoObject.remove();
            this.infoObject = null;
            this.rebuildWidgets();
            this.tween = null;
         });
         this.tween.start();
         return false;
      } else {
         return true;
      }
   }

   public boolean isPauseScreen() {
      return false;
   }
}
