package com.storyteam.storytelling.system.emotions.network;

import com.storyteam.storytelling.system.emotions.BubbleType;
import com.storyteam.storytelling.system.emotions.Emotion;
import com.storyteam.storytelling.system.emotions.RenderHolder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class S2CAddCharacterEmotion implements IPacket {
   private String refId;
   private Emotion emotion;
   private Vec3 offset;
   private BubbleType bubbleType;

   public S2CAddCharacterEmotion() {
   }

   public S2CAddCharacterEmotion(String refId, Emotion emotion, Vec3 offset, BubbleType bubbleType) {
      this.refId = refId;
      this.emotion = emotion;
      this.offset = offset;
      this.bubbleType = bubbleType;
   }

   public void write(FriendlyByteBuf buf) {
      buf.writeUtf(this.refId);
      buf.writeEnum(this.emotion);
      buf.writeEnum(this.bubbleType);
      buf.writeDouble(this.offset.x);
      buf.writeDouble(this.offset.y);
      buf.writeDouble(this.offset.z);
   }

   public void read(FriendlyByteBuf buf) {
      this.refId = buf.readUtf();
      this.emotion = (Emotion)buf.readEnum(Emotion.class);
      this.bubbleType = (BubbleType)buf.readEnum(BubbleType.class);
      this.offset = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
   }

   public void clientExecute(PacketContext ctx) {
      RenderHolder.addRenderedUUID(this.refId, this.emotion, this.offset, this.bubbleType);
   }
}
