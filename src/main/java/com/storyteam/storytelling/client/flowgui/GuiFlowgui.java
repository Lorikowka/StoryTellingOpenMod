package com.storyteam.storytelling.client.flowgui;

import java.util.Objects;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.zeith.hammerlib.abstractions.props.KeyMap;
import org.zeith.hammerlib.client.flowgui.data.FlowQuery;
import org.zeith.hammerlib.client.flowgui.objects.GuiRootObject;
import org.zeith.hammerlib.client.flowgui.reader.FlowguiRegistry;

public class GuiFlowgui extends Screen {
   protected ResourceLocation prevFlowId;
   protected GuiRootObject root;

   protected GuiFlowgui(Component pTitle) {
      super(pTitle);
   }

   protected void populateQuery(FlowQuery q) {
   }

   protected void populateKeymap(KeyMap context) {
   }

   protected final void populateMandatoryKeymap(KeyMap keymap, ResourceLocation id) {
      FlowQuery q = this.createQuery();
      this.populateQuery(q);
      keymap.with(FlowguiRegistry.QUERY, q).with(FlowguiRegistry.ROOT_ID, id).with(FlowguiRegistry.PREVIOUS_ROOT, this.root);
   }

   protected FlowQuery createQuery() {
      return new FlowQuery(this);
   }

   protected ResourceLocation getFlowId() {
      return FlowguiRegistry.getId(this.getClass());
   }

   protected GuiRootObject createRoot(ResourceLocation id) {
      KeyMap keymap = KeyMap.createHash();
      this.populateMandatoryKeymap(keymap, id);
      this.populateKeymap(keymap);
      return FlowguiRegistry.readRoot(keymap);
   }

   protected void init() {
      this.prevFlowId = this.getFlowId();
      this.root = (GuiRootObject)this.addRenderableWidget(this.createRoot(this.prevFlowId));
      super.init();
   }

   public void tick() {
      this.root.sendUpdate();
      ResourceLocation newFlowId = this.getFlowId();
      if (!Objects.equals(newFlowId, this.prevFlowId)) {
         this.init(this.minecraft, this.width, this.height);
      }
   }

   public boolean isPauseScreen() {
      return false;
   }
}
