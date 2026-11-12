package com.storyteam.storytelling.system.simulator.cognition_tab.network;

import com.storyteam.storytelling.system.simulator.cognition_tab.CognitionHelper;
import com.storyteam.storytelling.system.simulator.cognition_tab.capability.CognitionCapability;
import com.storyteam.storytelling.system.simulator.cognition_tab.client.gui.CognitionScreen;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class S2CSyncCognitionCapability implements IPacket {
   private CompoundTag capNbt;

   public S2CSyncCognitionCapability(CompoundTag capNbt) {
      this.capNbt = capNbt;
   }

   public void write(FriendlyByteBuf buf) {
      buf.writeNbt(this.capNbt);
   }

   public void read(FriendlyByteBuf buf) {
      this.capNbt = buf.readNbt();
   }

   public void clientExecute(PacketContext ctx) {
      LocalPlayer player = Minecraft.getInstance().player;
      if (player != null) {
         Optional<CognitionCapability> optional = CognitionHelper.getOptional(player);
         optional.ifPresent(cap -> cap.deserializeNBT(this.capNbt));
         CognitionScreen.INSTANCE.reinit();
      }
   }
}
