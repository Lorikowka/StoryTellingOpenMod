package com.storyteam.storytelling.init;

import com.storyteam.storytelling.content.entities.data.EntityDataSerializerHL;
import com.storyteam.storytelling.util.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;
import net.minecraft.world.phys.Vec3;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;

@SimplyRegister
public interface EntityDataSerializersST {
   @RegistryName("vec3")
   EntityDataSerializerHL<Vec3> VEC3 = EntityDataSerializerHL.simpleCopyable(
      (buf, v) -> buf.writeDouble(v.x).writeDouble(v.y).writeDouble(v.z),
      buf -> new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble()),
      UnaryOperator.identity()
   );
   @RegistryName("list_vec3")
   EntityDataSerializerHL<List<Vec3>> LIST_VEC3 = EntityDataSerializerHL.simpleCopyable((buf, list) -> {
      buf.writeVarInt(list.size());
      list.forEach(v -> buf.writeDouble(v.x).writeDouble(v.y).writeDouble(v.z));
   }, buf -> {
      List<Vec3> list = new ArrayList<>();

      for (int i = 0; i < buf.readVarInt(); i++) {
         list.add(new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble()));
      }

      return list;
   }, UnaryOperator.identity());
   @RegistryName("color")
   EntityDataSerializerHL<Color> COLOR = EntityDataSerializerHL.simpleCopyable((buf, color) -> {
      buf.writeByte(color.getRed());
      buf.writeByte(color.getGreen());
      buf.writeByte(color.getBlue());
      buf.writeByte(color.getAlpha());
   }, buf -> new Color(buf.readUnsignedByte(), buf.readUnsignedByte(), buf.readUnsignedByte(), buf.readUnsignedByte()), UnaryOperator.identity());
}
