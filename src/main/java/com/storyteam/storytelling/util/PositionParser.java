package com.storyteam.storytelling.util;

import com.storyteam.storytelling.util.world.Location;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import net.minecraft.CrashReport;
import net.minecraft.ReportedException;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import org.zeith.hammerlib.util.shaded.json.JSONObject;

public class PositionParser {
   private static final Pattern WORLD_POSITION_LOOK_PATTERN = Pattern.compile(
      "(?:(\\w+:\\w+).+?)?([-+]?(?:\\d+\\.\\d*|\\.?\\d+)) ([-+]?(?:\\d+\\.\\d*|\\.?\\d+)) ([-+]?(?:\\d+\\.\\d*|\\.?\\d+))(?: ([-+]?(?:\\d+\\.\\d*|\\.?\\d+)) ([-+]?(?:\\d+\\.\\d*|\\.?\\d+)))?"
   );

   public static Location parseLocationWithRotation(String s) {
      return parseLocationWithRotation(s, Level.OVERWORLD);
   }

   public static Location parseLocationWithRotationOrException(String s, Throwable source) {
      return Optional.ofNullable(parseLocationWithRotation(s, Level.OVERWORLD))
         .orElseThrow(() -> new ReportedException(new CrashReport("Unable to parse position from string " + JSONObject.quote(s), source)));
   }

   @Nullable
   public static Location parseLocationWithRotation(String s, ResourceKey<Level> defaultDimension) {
      Matcher worldPositionLookPattern = WORLD_POSITION_LOOK_PATTERN.matcher(s);
      if (worldPositionLookPattern.find()) {
         String group1 = worldPositionLookPattern.group(1);
         String group2 = worldPositionLookPattern.group(2);
         String group3 = worldPositionLookPattern.group(3);
         String group4 = worldPositionLookPattern.group(4);
         String group5 = worldPositionLookPattern.group(5);
         String group6 = worldPositionLookPattern.group(6);
         ResourceKey<Level> dim = group1 == null ? defaultDimension : UtilST.getWorld(group1);
         double x = Double.parseDouble(group2);
         double y = Double.parseDouble(group3);
         double z = Double.parseDouble(group4);
         float yaw = group5 == null ? Float.NaN : Float.parseFloat(group5);
         float pitch = group6 == null ? Float.NaN : Float.parseFloat(group6);
         if (Float.isNaN(yaw)) {
            while (yaw > 360.0F) {
               yaw -= 360.0F;
            }

            while (yaw < -360.0F) {
               yaw += 360.0F;
            }
         }

         return new Location(dim, x, y, z, yaw, pitch);
      } else {
         return null;
      }
   }

   public static BlockPos parseBlockPos(String setblock) {
      Matcher worldPositionLookPattern = WORLD_POSITION_LOOK_PATTERN.matcher(setblock);
      if (worldPositionLookPattern.find()) {
         String group2 = worldPositionLookPattern.group(2);
         String group3 = worldPositionLookPattern.group(3);
         String group4 = worldPositionLookPattern.group(4);
         double x = Double.parseDouble(group2);
         double y = Double.parseDouble(group3);
         double z = Double.parseDouble(group4);
         return BlockPos.containing(x, y, z);
      } else {
         return null;
      }
   }
}
