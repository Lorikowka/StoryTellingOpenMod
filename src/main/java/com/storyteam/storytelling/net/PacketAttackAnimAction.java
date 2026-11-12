package com.storyteam.storytelling.net;

import com.storyteam.storytelling.content.entities.AnimatedEntityLikeHumanoid;
import com.storyteam.storytelling.system.fight.AttackAnimAction;
import com.storyteam.storytelling.system.fight.Collider;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector3f;
import org.zeith.hammeranims.core.client.model.ModelBoneF;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;
import org.zeith.hammerlib.util.java.tuples.Tuple3;
import org.zeith.hammerlib.util.java.tuples.Tuples;

@MainThreaded
public class PacketAttackAnimAction implements IPacket {
   private int id;
   private AttackAnimAction attackAnimAction;

   public PacketAttackAnimAction(int id, AttackAnimAction attackAnimAction) {
      this.id = id;
      this.attackAnimAction = attackAnimAction;
   }

   public PacketAttackAnimAction() {
   }

   public void write(FriendlyByteBuf buf) {
      buf.writeInt(this.id);
      buf.writeFloat(this.attackAnimAction.getBase_inflate());
      buf.writeVector3f(this.attackAnimAction.getInflate_v3().toVector3f());
      buf.writeVector3f(this.attackAnimAction.getOffsetV3().toVector3f());
      buf.writeBoolean(this.attackAnimAction.is_active());
      buf.writeFloat(this.attackAnimAction.getTime_anim_max());
      buf.writeFloat(this.attackAnimAction.getTime_anim());
      buf.writeInt(this.attackAnimAction.getBones().size());

      for (String bone : this.attackAnimAction.getBones()) {
         buf.writeUtf(bone);
      }

      buf.writeInt(this.attackAnimAction.getTime_inflate().size());

      for (Tuple3<Float, Float, Float> triple : this.attackAnimAction.getTime_inflate()) {
         writeTriple(buf, triple);
      }
   }

   public void read(FriendlyByteBuf buf) {
      this.id = buf.readInt();
      float base_inflate = buf.readFloat();
      Vector3f inflate_v3 = buf.readVector3f();
      Vector3f offsetV3 = buf.readVector3f();
      boolean is_active = buf.readBoolean();
      float time_max_anim = buf.readFloat();
      float time_anim = buf.readFloat();
      int bones_size = buf.readInt();
      List<String> bones = new ArrayList<>();

      for (int i = 0; i < bones_size; i++) {
         bones.add(buf.readUtf());
      }

      int triples_size = buf.readInt();
      List<Tuple3<Float, Float, Float>> triples = new ArrayList<>();

      for (int j = 0; j < triples_size; j++) {
         triples.add(readTriple(buf));
      }

      this.attackAnimAction = new AttackAnimAction(
         time_max_anim,
         time_anim,
         bones,
         is_active,
         base_inflate,
         triples,
         new Vec3((double)inflate_v3.x, (double)inflate_v3.y, (double)inflate_v3.z),
         new Vec3((double)offsetV3.x(), (double)offsetV3.y(), (double)offsetV3.z())
      );
   }

   @OnlyIn(Dist.CLIENT)
   public void clientExecute(PacketContext ctx) {
      ClientLevel level = Minecraft.getInstance().level;
      if (level != null) {
         Entity entity = level.getEntity(this.id);
         if (entity instanceof AnimatedEntityLikeHumanoid) {
            List<ModelBoneF> modelBoneFList = new ArrayList<>();

            for (String bone : this.attackAnimAction.getBones()) {
               ModelBoneF modelBoneF = Collider.getModelBone(entity, bone);
               if (modelBoneF != null) {
                  modelBoneFList.add(modelBoneF);
               }
            }

            this.attackAnimAction.setModelBoneFList(modelBoneFList);
            ((AnimatedEntityLikeHumanoid)entity).setAttackAnimAction(this.attackAnimAction);
         }
      }
   }

   public static void writeTriple(FriendlyByteBuf buf, Tuple3<Float, Float, Float> triple) {
      buf.writeFloat((Float)triple.a());
      buf.writeFloat((Float)triple.b());
      buf.writeFloat((Float)triple.c());
   }

   public static Tuple3<Float, Float, Float> readTriple(FriendlyByteBuf buf) {
      float a = buf.readFloat();
      float b = buf.readFloat();
      float c = buf.readFloat();
      return Tuples.immutable(a, b, c);
   }
}
