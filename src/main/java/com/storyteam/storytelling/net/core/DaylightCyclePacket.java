package com.storyteam.storytelling.net.core;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameRules.BooleanValue;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class DaylightCyclePacket implements IPacket {
   private boolean doDaylightCycle;

   public DaylightCyclePacket(boolean doDaylightCycle) {
      this.doDaylightCycle = doDaylightCycle;
   }

   public DaylightCyclePacket() {
   }

   public void write(FriendlyByteBuf buf) {
      IPacket.super.write(buf);
      buf.writeBoolean(this.doDaylightCycle);
   }

   public void read(FriendlyByteBuf buf) {
      IPacket.super.read(buf);
      this.doDaylightCycle = buf.readBoolean();
   }

   public void serverExecute(PacketContext ctx) {
      ((BooleanValue)ctx.getSender().getServer().getGameRules().getRule(GameRules.RULE_DAYLIGHT)).set(this.doDaylightCycle, ctx.getSender().getServer());
   }
}
