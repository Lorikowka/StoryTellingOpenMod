package com.storyteam.storytelling.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.storyteam.storytelling.Storytelling;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import org.joml.Vector3f;
import org.zeith.hammeranims.api.geometry.IGeometryContainer;

public class BoneGetter {
   public static BoneGetter.CubeData extractCubeDataFromJar(IGeometryContainer model, String boneName) {
      String resourcePath = model.getRegistryKey().getPath();
      String jarPath = "assets/storytelling/bedrock/geometry/" + resourcePath + ".geo.json";

      try (InputStream stream = Storytelling.class.getClassLoader().getResourceAsStream(jarPath)) {
         if (stream == null) {
            throw new FileNotFoundException("Resource not found in JAR: " + jarPath);
         } else {
            InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray geometry = root.getAsJsonArray("minecraft:geometry");
            if (geometry != null && !geometry.isEmpty()) {
               JsonObject geoObj = geometry.get(0).getAsJsonObject();
               JsonArray bones = geoObj.getAsJsonArray("bones");
               if (bones == null) {
                  throw new IllegalArgumentException("Invalid geo format: no bones");
               } else {
                  for (JsonElement boneElement : bones) {
                     JsonObject bone = boneElement.getAsJsonObject();
                     String name = bone.get("name").getAsString();
                     if (boneName.equals(name)) {
                        Vector3f pivot = getVec3f(bone.get("pivot"));
                        JsonArray cubes = bone.getAsJsonArray("cubes");
                        if (cubes == null || cubes.size() == 0) {
                           throw new IllegalArgumentException("Bone has no cubes: " + boneName);
                        }

                        JsonObject bestCube = null;
                        float bestInflate = Float.NEGATIVE_INFINITY;
                        float bestSizeSum = Float.NEGATIVE_INFINITY;

                        for (JsonElement cubeElem : cubes) {
                           JsonObject cube = cubeElem.getAsJsonObject();
                           float inflate = cube.has("inflate") ? cube.get("inflate").getAsFloat() : 1.0F;
                           Vector3f size = getVec3f(cube.get("size"));
                           float sizeSum = size.x() + size.y() + size.z();
                           if (inflate > bestInflate || inflate == bestInflate && sizeSum > bestSizeSum) {
                              bestInflate = inflate;
                              bestSizeSum = sizeSum;
                              bestCube = cube;
                           }
                        }

                        if (bestCube == null) {
                           throw new IllegalStateException("No suitable cube found in bone: " + boneName);
                        }

                        Vector3f origin = getVec3f(bestCube.get("origin"));
                        Vector3f size = getVec3f(bestCube.get("size"));
                        float inflate = bestCube.has("inflate") ? bestCube.get("inflate").getAsFloat() : 1.0F;
                        return new BoneGetter.CubeData(origin, size, pivot, inflate);
                     }
                  }

                  throw new IllegalArgumentException("Bone not found: " + boneName);
               }
            } else {
               throw new IllegalArgumentException("Invalid geo format: no geometry");
            }
         }
      } catch (Exception var27) {
         throw new RuntimeException("Failed to extract cube data from JAR resource", var27);
      }
   }

   private static Vector3f getVec3f(JsonElement arrayElem) {
      if (arrayElem != null && arrayElem.isJsonArray()) {
         JsonArray array = arrayElem.getAsJsonArray();
         return new Vector3f(array.get(0).getAsFloat(), array.get(1).getAsFloat(), array.get(2).getAsFloat());
      } else {
         return new Vector3f();
      }
   }

   public static record CubeData(Vector3f origin, Vector3f size, Vector3f pivot, float inflate) {
   }
}
