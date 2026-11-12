package com.storyteam.storytelling.events.listeners;

import com.storyteam.storytelling.util.ScriptUtils;
import com.storyteam.storytelling.util.world.Location;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class PathRecorderHandler {
   private static boolean ENABLED = false;
   private static String CURRENT_NAME = null;
   private static List<Location> POSITIONS = null;

   public static void record(String name) {
      if (!ENABLED) {
         CURRENT_NAME = name;
         POSITIONS = new ArrayList<>();
         ENABLED = true;
      }
   }

   public static void finish() {
      if (ENABLED) {
         ENABLED = false;
         CompoundTag tag = new CompoundTag();
         ListTag positions = new ListTag();

         for (Location position : POSITIONS) {
            CompoundTag pos = new CompoundTag();
            pos.putDouble("x", position.x);
            pos.putDouble("y", position.y);
            pos.putDouble("z", position.z);
            pos.putString("world", position.getDimension().location().toString());
            pos.putFloat("yaw", position.getYaw());
            pos.putFloat("pitch", position.getPitch());
            positions.add(pos);
         }

         tag.put("positions", positions);
         save(tag, new File(ScriptIO.WORLD_PATH.toFile(), "paths/" + CURRENT_NAME + ".dat"));
         CURRENT_NAME = null;
         POSITIONS = null;
      }
   }

   @SubscribeEvent
   public static void onTick(ServerTickEvent event) {
      if (ENABLED) {
         if (event.phase == Phase.END) {
            POSITIONS.add(new Location(ScriptUtils.getPlayer()));
         }
      }
   }

   public static CompoundTag read(File in) {
      try {
         return in.isFile() ? NbtIo.readCompressed(in) : null;
      } catch (IOException var2) {
         var2.printStackTrace();
         return null;
      }
   }

   public static boolean save(CompoundTag nbt, File out) {
      try {
         out.toPath().getParent().toFile().mkdirs();
         File old = new File(out.getAbsolutePath() + ".old");
         if (old.isFile()) {
            old.delete();
         }

         if (out.isFile()) {
            out.renameTo(old);
         }

         NbtIo.writeCompressed(nbt, out);
         return true;
      } catch (IOException var3) {
         var3.printStackTrace();
         return false;
      }
   }
}
