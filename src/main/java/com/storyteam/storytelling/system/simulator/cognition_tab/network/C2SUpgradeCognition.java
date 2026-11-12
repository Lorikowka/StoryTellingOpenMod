package com.storyteam.storytelling.system.simulator.cognition_tab.network;

import com.storyteam.storytelling.system.simulator.cognition_tab.Cognition;
import com.storyteam.storytelling.system.simulator.cognition_tab.CognitionHelper;
import com.storyteam.storytelling.system.simulator.cognition_tab.CognitionHolder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class C2SUpgradeCognition implements IPacket {
   private ResourceLocation id;
   private int tier;
   private boolean force;

   public C2SUpgradeCognition(Cognition cognition, int tier, boolean force) {
      this.id = cognition.id();
      this.tier = tier;
      this.force = force;
   }

   public void write(FriendlyByteBuf buf) {
      buf.writeResourceLocation(this.id);
      buf.writeInt(this.tier);
      buf.writeBoolean(this.force);
   }

   public void read(FriendlyByteBuf buf) {
      this.id = buf.readResourceLocation();
      this.tier = buf.readInt();
      this.force = buf.readBoolean();
   }

   public void serverExecute(PacketContext ctx) {
      ServerPlayer player = ctx.getSender();
      CognitionHelper.upgrade(player, CognitionHolder.INSTANCE.getCognition(this.id), this.tier, this.force);
   }
}
