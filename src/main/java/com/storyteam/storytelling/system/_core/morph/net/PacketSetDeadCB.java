package com.storyteam.storytelling.system._core.morph.net;

import com.storyteam.storytelling.api.animations.AnimationWithCondition;
import com.storyteam.storytelling.system._core.morph.IAnimatedPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class PacketSetDeadCB implements IPacket {
   boolean dead;

   public PacketSetDeadCB(boolean dead) {
      this.dead = dead;
   }

   public void read(FriendlyByteBuf buf) {
      this.dead = buf.readBoolean();
   }

   public void write(FriendlyByteBuf buf) {
      buf.writeBoolean(this.dead);
   }

   @OnlyIn(Dist.CLIENT)
   public void clientExecute(PacketContext ctx) {
      Player player = Minecraft.getInstance().player;
      if (player != null) {
         ((IAnimatedPlayer)player).getUnsavedProperties().put(AnimationWithCondition.DEAD, this.dead);
      }
   }
}
