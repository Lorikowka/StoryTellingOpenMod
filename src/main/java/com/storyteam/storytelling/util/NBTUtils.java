package com.storyteam.storytelling.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.events.listeners.ScriptIO;
import com.storyteam.storytelling.script.ScriptInstance;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.INBTSerializable;
import org.apache.commons.lang3.tuple.Pair;
import org.zeith.hammerlib.api.io.NBTSerializationHelper;
import org.zeith.hammerlib.api.io.serializers.INBTSerializer;
import org.zeith.hammerlib.util.java.ReflectionUtil;
import org.zeith.hammerlib.util.mcf.Resources;

public class NBTUtils {
   private static final Gson LIST_SERIALIZER = new GsonBuilder().disableHtmlEscaping().create();

   public static void setBoolean(ItemStack stack, String tag, boolean value) {
      stack.getOrCreateTag().putBoolean(tag, value);
   }

   public static void setInt(ItemStack stack, String tag, int value) {
      stack.getOrCreateTag().putInt(tag, value);
   }

   public static void setLong(ItemStack stack, String tag, long value) {
      stack.getOrCreateTag().putLong(tag, value);
   }

   public static void setFloat(ItemStack stack, String tag, float value) {
      stack.getOrCreateTag().putFloat(tag, value);
   }

   public static void setDouble(ItemStack stack, String tag, double value) {
      stack.getOrCreateTag().putDouble(tag, value);
   }

   public static void setString(ItemStack stack, String tag, String value) {
      stack.getOrCreateTag().putString(tag, value);
   }

   public static void setCompound(ItemStack stack, String tag, CompoundTag value) {
      stack.getOrCreateTag().put(tag, value);
   }

   public static boolean getBoolean(ItemStack stack, String tag, boolean defaultValue) {
      return safeCheck(stack, tag) ? stack.getTag().getBoolean(tag) : defaultValue;
   }

   public static int getInt(ItemStack stack, String tag, int defaultValue) {
      return safeCheck(stack, tag) ? stack.getTag().getInt(tag) : defaultValue;
   }

   public static long getLong(ItemStack stack, String tag, long defaultValue) {
      return safeCheck(stack, tag) ? stack.getTag().getLong(tag) : defaultValue;
   }

   public static float getFloat(ItemStack stack, String tag, float defaultValue) {
      return safeCheck(stack, tag) ? stack.getTag().getFloat(tag) : defaultValue;
   }

   public static double getDouble(ItemStack stack, String tag, double defaultValue) {
      return safeCheck(stack, tag) ? stack.getTag().getDouble(tag) : defaultValue;
   }

   public static String getString(ItemStack stack, String tag, String defaultValue) {
      return safeCheck(stack, tag) ? stack.getTag().getString(tag) : defaultValue;
   }

   public static CompoundTag getCompound(ItemStack stack, String tag, CompoundTag defaultValue) {
      return safeCheck(stack, tag) ? stack.getTag().getCompound(tag) : defaultValue;
   }

   public static boolean safeCheck(ItemStack stack, String tag) {
      return !stack.isEmpty() && stack.getTag() != null && stack.getTag().contains(tag);
   }

   public static void clearTag(ItemStack stack, String tag) {
      stack.getOrCreateTag().remove(tag);
   }

   public static String writePosition(Vec3 vec) {
      return MathUtils.round(vec.x(), 1) + "," + MathUtils.round(vec.y(), 1) + "," + MathUtils.round(vec.z(), 1);
   }

   public static String writeLevel(Level level) {
      return level.dimension().location().toString();
   }

   @Nullable
   public static Vec3 parsePosition(String value) {
      if (value != null && !value.equals("")) {
         String[] pos = value.split(",");
         return new Vec3(Double.parseDouble(pos[0]), Double.parseDouble(pos[1]), Double.parseDouble(pos[2]));
      } else {
         return null;
      }
   }

   public static CompoundTag packBundledPosition(Vec3 pos, Level level) {
      CompoundTag tag = new CompoundTag();
      tag.putString("pos", writePosition(pos));
      tag.putString("level", writeLevel(level));
      return tag;
   }

   public static void serializeScriptGlobally(int scriptId, Object instance) {
      Class<?> type = instance.getClass();
      CompoundTag nbt = new CompoundTag();

      for (Field field : ReflectionUtil.getFieldsUpTo(type, null)) {
         field.setAccessible(true);
         ScriptInstance.GlobalSerializable nbts = field.getAnnotation(ScriptInstance.GlobalSerializable.class);
         if (nbts != null) {
            String name = nbts.value();
            if (name.trim().isEmpty()) {
               name = field.getName();
            }

            try {
               if (Modifier.isFinal(field.getModifiers())) {
                  if (field.get(instance) instanceof INBTSerializable<?> s) {
                     nbt.put(name, s.serializeNBT());
                  } else if (!INBTSerializer.class.isAssignableFrom(field.getType())) {
                     Storytelling.LOG.warn("Don't know how to serialize " + field + " in " + type);
                  }
               } else {
                  NBTSerializationHelper.serializeField(field.getType(), field.get(instance), nbt, name);
               }
            } catch (ReflectiveOperationException var10) {
               Storytelling.LOG.error("Failed to serialize field " + field + " in " + type, var10);
            }
         }
      }

      CompoundTag extraNBT = ScriptIO.WORLD_EXTRA_NBT.getCompound("Global Script Info");
      extraNBT.put(String.valueOf(scriptId), nbt);
      ScriptIO.WORLD_EXTRA_NBT.put("Global Script Info", extraNBT);
   }

   public static void deserializeScriptGlobally(int scriptId, Object instance) {
      CompoundTag nbt = ScriptIO.WORLD_EXTRA_NBT.getCompound("Global Script Info").getCompound(String.valueOf(scriptId));
      if (!nbt.getAllKeys().isEmpty()) {
         Class<?> type = instance.getClass();

         for (Field field : ReflectionUtil.getFieldsUpTo(type, null)) {
            field.setAccessible(true);
            ScriptInstance.GlobalSerializable nbts = field.getAnnotation(ScriptInstance.GlobalSerializable.class);
            if (nbts != null) {
               String name = nbts.value();
               if (name.trim().isEmpty()) {
                  name = field.getName();
               }

               try {
                  if (Modifier.isFinal(field.getModifiers())) {
                     if (field.get(instance) instanceof INBTSerializable s) {
                        Tag tag = nbt.get(name);
                        if (tag != null) {
                           s.deserializeNBT(tag);
                        }
                     } else if (INBTSerializer.class.isAssignableFrom(field.getType())) {
                        if (nbt.contains(name)) {
                           Storytelling.LOG
                              .warn(
                                 "Can't deserialize " + field + " in " + type + " since the final value is null. Trying to deserialize tag: " + nbt.get(name)
                              );
                        }
                     } else {
                        Storytelling.LOG.warn("Don't know how to deserialize " + field + " in " + type);
                     }
                  } else {
                     Object val = NBTSerializationHelper.deserializeField(field.getType(), nbt, name);
                     if (val != null || !field.getType().isPrimitive()) {
                        field.set(instance, val);
                     }
                  }
               } catch (Throwable var11) {
                  Storytelling.LOG.error("Failed to deserialize field " + field + " in " + type, var11);
               }
            }
         }
      }
   }

   @Nullable
   public static Pair<ServerLevel, Vec3> parseBundledPosition(Level world, CompoundTag tag) {
      ServerLevel level = parseLevel(world, tag.getString("level"));
      Vec3 vec = parsePosition(tag.getString("pos"));
      return level != null && vec != null ? Pair.of(level, vec) : null;
   }

   @Nullable
   public static ServerLevel parseLevel(Level world, String value) {
      return world.getServer().getLevel(ResourceKey.create(Registries.DIMENSION, Resources.location(value)));
   }

   public static void setList(ItemStack stack, String tag, List<?> list) {
      setString(stack, tag, LIST_SERIALIZER.toJson(list, List.class));
   }

   public static <T> List<T> getList(ItemStack stack, String tag, Class<T> type) {
      List<T> positions = (List<T>)LIST_SERIALIZER.fromJson(getString(stack, tag, ""), TypeToken.getParameterized(List.class, new Type[]{type}).getType());
      return (List<T>)(positions == null ? new ArrayList<>() : positions);
   }
}
