package com.storyteam.storytelling.system.emotions.network;

import com.storyteam.storytelling.system.emotions.BubbleType;
import com.storyteam.storytelling.system.emotions.Emotion;
import com.storyteam.storytelling.system.emotions.EmotionRenderData;
import com.storyteam.storytelling.system.emotions.RenderHolder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class S2CSetCharacterEmotion implements IPacket {
   private String refId;
   private Emotion[] emotion;
   private BubbleType type;
   private Vec3 offset;

   public S2CSetCharacterEmotion() {
   }

   public S2CSetCharacterEmotion(String refId, Emotion[] emotion, BubbleType type, Vec3 offset) {
      this.refId = refId;
      this.emotion = emotion;
      this.type = type;
      this.offset = offset;
   }

   public void write(FriendlyByteBuf buf) {
      buf.writeUtf(this.refId);
      buf.writeInt(this.emotion.length);

      for (Emotion emotion1 : this.emotion) {
         buf.writeEnum(emotion1);
      }

      buf.writeEnum(this.type);
      buf.writeDouble(this.offset.x);
      buf.writeDouble(this.offset.y);
      buf.writeDouble(this.offset.z);
   }

   public void read(FriendlyByteBuf buf) {
      this.refId = buf.readUtf();
      int length = buf.readInt();
      this.emotion = new Emotion[length];

      for (int i = 0; i < length; i++) {
         this.emotion[i] = (Emotion)buf.readEnum(Emotion.class);
      }

      this.type = (BubbleType)buf.readEnum(BubbleType.class);
      this.offset = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
   }

   public void clientExecute(PacketContext ctx) {
      if (RenderHolder.hasRenderedUUID(this.refId)) {
         EmotionRenderData data = RenderHolder.getOldestRenderedUUID(this.refId);
         if (!this.refId.equals("player")) {
            RenderHolder.removeRenderedUUID(this.refId);
         } else {
            RenderHolder.hideRenderedPlayer();
         }

         EmotionRenderData newData = EmotionRenderData.of(this.refId, this.emotion[0], this.offset, this.type);
         newData.setAppearTick(data.getAppearTick());
         newData.setTween(data.getTween());
         newData.setRenderScale(data.getRenderScale());
         RenderHolder.add(newData);
      } else {
         RenderHolder.addRenderedUUID(this.refId, this.emotion[0], this.offset, this.type);
      }

      for (int i = 1; i < this.emotion.length; i++) {
         RenderHolder.addRenderedUUID(this.refId, this.emotion[i], this.offset, this.type);
      }
   }
}
