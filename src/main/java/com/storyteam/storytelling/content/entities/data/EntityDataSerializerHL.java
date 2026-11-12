package com.storyteam.storytelling.content.entities.data;

import java.util.Optional;
import java.util.function.UnaryOperator;
import net.minecraft.core.IdMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.FriendlyByteBuf.Reader;
import net.minecraft.network.FriendlyByteBuf.Writer;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.ForgeRegistries.Keys;
import org.zeith.hammerlib.api.fml.ICustomRegistrar;
import org.zeith.hammerlib.util.java.Cast;

public interface EntityDataSerializerHL<T> extends EntityDataSerializer<T>, ICustomRegistrar {
   default void performRegister(RegisterEvent event, ResourceLocation id) {
      event.register(Keys.ENTITY_DATA_SERIALIZERS, id, Cast.constant(this));
   }

   static <T> EntityDataSerializerHL<T> simple(final Writer<T> writer, final Reader<T> reader) {
      return new EntityDataSerializerHL.ForValueTypeHL<T>() {
         public void write(FriendlyByteBuf buf, T obj) {
            writer.accept(buf, obj);
         }

         public T read(FriendlyByteBuf obj) {
            return (T)reader.apply(obj);
         }
      };
   }

   static <T> EntityDataSerializerHL<T> simpleCopyable(final Writer<T> writer, final Reader<T> reader, final UnaryOperator<T> copy) {
      return new EntityDataSerializerHL.ForValueTypeHL<T>() {
         public void write(FriendlyByteBuf buf, T obj) {
            writer.accept(buf, obj);
         }

         public T read(FriendlyByteBuf obj) {
            return (T)reader.apply(obj);
         }

         @Override
         public T copy(T original) {
            return copy.apply(original);
         }
      };
   }

   static <T> EntityDataSerializer<Optional<T>> optional(Writer<T> writer, Reader<T> reader) {
      return simple(writer.asOptional(), reader.asOptional());
   }

   static <T extends Enum<T>> EntityDataSerializer<T> simpleEnum(Class<T> eClass) {
      return simple(FriendlyByteBuf::writeEnum, buf -> buf.readEnum(eClass));
   }

   static <T> EntityDataSerializer<T> simpleId(IdMap<T> idmap) {
      return simple((buf, t) -> buf.writeId(idmap, t), buf -> buf.readById(idmap));
   }

   public interface ForValueTypeHL<T> extends EntityDataSerializerHL<T> {
      default T copy(T original) {
         return original;
      }
   }
}
