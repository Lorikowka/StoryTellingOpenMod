package com.storyteam.storytelling.system.simulator.cognition_tab.client;

import com.storyteam.storytelling.system.simulator.cognition_tab.CognitionPoseConfig;
import com.storyteam.storytelling.system.simulator.cognition_tab.client.gui.CognitionScreen;
import com.storyteam.storytelling.system.simulator.cognition_tab.network.C2SSyncCognitionData;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent.Key;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.zeith.hammerlib.net.Network;

@EventBusSubscriber({Dist.CLIENT})
public class GuiOpenEvent {
   @SubscribeEvent
   public static void onKeyPressed(Key event) {
      Minecraft minecraft = Minecraft.getInstance();
      if (CognitionPoseConfig.editorMod) {
         if (CognitionKeybinding.OPEN_COGNITION_GUI.isDown()) {
            Network.sendToServer(new C2SSyncCognitionData());
            minecraft.setScreen(CognitionScreen.INSTANCE);
         }
      }
   }
}
