package com.storyteam.storytelling.system.simulator.cognition_tab.network;

import com.storyteam.storytelling.init.CapsST;
import com.storyteam.storytelling.system.simulator.cognition_tab.Cognition;
import com.storyteam.storytelling.system.simulator.cognition_tab.CognitionHelper;
import com.storyteam.storytelling.system.simulator.cognition_tab.CognitionHolder;
import com.storyteam.storytelling.system.simulator.cognition_tab.capability.CognitionCapability;
import java.util.Optional;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.util.LazyOptional;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class C2SDowngradeCognition implements IPacket {
   private ResourceLocation id;
   private int tier;

   public C2SDowngradeCognition(Cognition cognition, int tier) {
      this.id = cognition.id();
      this.tier = tier;
   }

   public void write(FriendlyByteBuf buf) {
      buf.writeResourceLocation(this.id);
      buf.writeInt(this.tier);
   }

   public void read(FriendlyByteBuf buf) {
      this.id = buf.readResourceLocation();
      this.tier = buf.readInt();
   }

   public void serverExecute(PacketContext ctx) {
      ServerPlayer player = ctx.getSender();
      Cognition cognition = CognitionHolder.INSTANCE.getCognition(this.id);
      LazyOptional<CognitionCapability> capability = player.getCapability(CapsST.COG_CAP);
      Optional<CognitionCapability> resolve = capability.resolve();
      if (!resolve.isEmpty()) {
         CognitionCapability cap = resolve.get();
         System.out.println(cap.serializeNBT());
         if (this.tier <= 1) {
            CognitionHelper.remove(player, cognition);
         } else {
            int currTier = CognitionHelper.getTier(player, cognition);

            for (int i = currTier; i > this.tier - 1; i--) {
               CognitionHelper.downgrade(player, cognition);
            }
         }
      }
   }
}
