package com.storyteam.storytelling.system.dialog;

import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.script.story.characters.Character;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class OpenDialogPacket implements IPacket {
   public String id;

   public OpenDialogPacket(Character<? extends AnimatedEntity> character) {
      this.id = character.getReferenceId();
   }

   public void write(FriendlyByteBuf buf) {
      buf.writeUtf(this.id);
      IPacket.super.write(buf);
   }

   public void read(FriendlyByteBuf buf) {
      IPacket.super.read(buf);
      this.id = buf.readUtf();
   }

   public void clientExecute(PacketContext ctx) {
      IPacket.super.clientExecute(ctx);
      Minecraft.getInstance().setScreen(new DialogScreen(Character.getById(this.id)));
   }
}
