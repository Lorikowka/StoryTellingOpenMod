package com.storyteam.storytelling.system.simulator.cognition_tab.network;

import com.storyteam.storytelling.system.simulator.cognition_tab.Cognition;
import com.storyteam.storytelling.system.simulator.cognition_tab.CognitionHolder;
import com.storyteam.storytelling.system.simulator.cognition_tab.utils.CognitionCustomRequement;
import com.storyteam.storytelling.system.simulator.cognition_tab.utils.CognitionTierInfo;
import com.storyteam.storytelling.system.simulator.cognition_tab.utils.CustomUpgradeHolder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class S2CSyncCognitionCustomReques implements IPacket {
   private Map<Integer, Boolean> customReques = new HashMap<>();

   public S2CSyncCognitionCustomReques(ServerPlayer player) {
      for (Cognition cognition : CognitionHolder.INSTANCE.getCognitions()) {
         for (CognitionTierInfo tierInfo : cognition.levels().values()) {
            for (CognitionCustomRequement reque : tierInfo.customReques()) {
               this.customReques.put(reque.getId(), reque.test(player));
            }
         }
      }
   }

   public void write(FriendlyByteBuf buf) {
      buf.writeInt(this.customReques.size());

      for (Entry<Integer, Boolean> entry : this.customReques.entrySet()) {
         buf.writeInt(entry.getKey());
         buf.writeBoolean(entry.getValue());
      }
   }

   public void read(FriendlyByteBuf buf) {
      this.customReques = new HashMap<>();
      int size = buf.readInt();

      for (int i = 0; i < size; i++) {
         this.customReques.put(buf.readInt(), buf.readBoolean());
      }
   }

   public void clientExecute(PacketContext ctx) {
      for (Entry<Integer, Boolean> entry : this.customReques.entrySet()) {
         CustomUpgradeHolder.setCustomUpgradeState(entry.getKey(), entry.getValue());
      }
   }
}
