package com.storyteam.storytelling.script.story.npc;

import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import com.mojang.blaze3d.vertex.VertexSorting;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.math.Axis;
import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.script.story.StoryWorkspace;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import org.lytharalab.gfbs.gltf.api.client.GltfInstance;
import org.lytharalab.gfbs.gltf.api.client.GltfRenderer;
import org.lytharalab.gfbs.gltf.api.model.GltfAsset;
import org.lytharalab.gfbs.gltf.api.model.GltfMesh;
import org.lytharalab.gfbs.gltf.api.model.GltfNode;
import org.lytharalab.gfbs.gltf.api.model.GltfPrimitive;
import org.lytharalab.gfbs.gltf.api.model.GltfSkin;
import org.lytharalab.gfbs.gltf.api.model.PrimitiveMode;

/** Builds a still portrait from the head geometry in a story NPC's glTF model. Client render thread only. */
public final class NpcPortraits {
   private static final int SIZE = 128;
   private static final int MAX_CACHE = 32;
   private static final Map<String, Portrait> CACHE = new LinkedHashMap<>(16, 0.75f, true);

   private NpcPortraits() { }

   public static boolean draw(GuiGraphics graphics, String icon, int x, int y, int size, float alpha) {
      if (icon == null || !icon.startsWith("glb:")) return false;
      String model = icon.substring(4);
      if (!model.matches("models/[a-z0-9_/-]+\\.(?:glb|gltf)")) return false;
      Path root = StoryWorkspace.assets().toAbsolutePath().normalize();
      Path file = root.resolve(model).normalize();
      if (!file.startsWith(root) || !Files.isRegularFile(file)) return false;
      try {
         long stamp = Files.getLastModifiedTime(file).toMillis() ^ Files.size(file);
         Portrait portrait = CACHE.get(model);
         if (portrait == null || portrait.stamp != stamp) {
            if (portrait != null) portrait.close();
            portrait = create(model, stamp);
            CACHE.put(model, portrait);
            if (CACHE.size() > MAX_CACHE) {
               String oldest = CACHE.keySet().iterator().next();
               CACHE.remove(oldest).close();
            }
         }
         if (portrait.texture == null) return false;
         RenderSystem.enableBlend();
         RenderSystem.setShader(GameRenderer::getPositionTexShader);
         RenderSystem.setShaderTexture(0, portrait.texture);
         RenderSystem.setShaderColor(1f, 1f, 1f, alpha);
         Matrix4f matrix = graphics.pose().last().pose();
         BufferBuilder vertices = Tesselator.getInstance().getBuilder();
         vertices.begin(Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
         vertices.vertex(matrix, x, y + size, 0).uv(0, 1).endVertex();
         vertices.vertex(matrix, x + size, y + size, 0).uv(1, 1).endVertex();
         vertices.vertex(matrix, x + size, y, 0).uv(1, 0).endVertex();
         vertices.vertex(matrix, x, y, 0).uv(0, 0).endVertex();
         BufferUploader.drawWithShader(vertices.end());
         RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
         RenderSystem.disableBlend();
         return true;
      } catch (IOException | RuntimeException error) {
         CACHE.put(model, new Portrait(fileStamp(file), null));
         Storytelling.LOG.warn("Cannot create NPC portrait from ST/assets/{}", model, error);
         return false;
      }
   }

   private static long fileStamp(Path file) {
      try { return Files.getLastModifiedTime(file).toMillis() ^ Files.size(file); }
      catch (IOException ignored) { return 0; }
   }

   private static Portrait create(String model, long stamp) throws IOException {
      GltfAsset source = ScriptNpcRenderer.loadModel(model);
      Head head = headGeometry(source, model, stamp);
      if (head == null) {
         Storytelling.LOG.warn("No head geometry found in ST/assets/{}; using the default chat icon", model);
         return new Portrait(stamp, null);
      }
      NativeImage image = rasterize(head);
      if (image == null) return new Portrait(stamp, null);
      ResourceLocation texture = new ResourceLocation("storytelling", "portrait/" +
         Integer.toHexString(model.hashCode()) + "_" + Long.toHexString(stamp));
      Minecraft.getInstance().getTextureManager().register(texture, new DynamicTexture(image));
      return new Portrait(stamp, texture);
   }

   private static NativeImage rasterize(Head head) throws IOException {
      NativeImage output = new NativeImage(SIZE, SIZE, true);
      float[] depth = new float[SIZE * SIZE];
      Arrays.fill(depth, Float.NEGATIVE_INFINITY);
      Map<Integer, BufferedImage> images = new LinkedHashMap<>();
      float[] transforms;
      try (GltfInstance instance = new GltfInstance(head.asset)) {
         transforms = instance.nodes().computeWorldMatrices(instance.animations().pose());
      }
      float scale = 0.82f * SIZE / Math.max(0.01f, Math.max(head.maxX - head.minX, head.maxY - head.minY));
      float centerX = (head.minX + head.maxX) / 2f;
      float centerY = (head.minY + head.maxY) / 2f;
      int pixels = 0;
      for (int nodeIndex : head.visibleNodes) {
         GltfNode node = head.asset.nodes().get(nodeIndex);
         for (int meshIndex : node.meshes()) {
            GltfMesh mesh = head.asset.meshes().get(meshIndex);
            for (GltfPrimitive primitive : mesh.primitives()) {
               if (primitive.mode() != PrimitiveMode.TRIANGLES) continue;
               int materialIndex = primitive.material();
               if (materialIndex < 0 || materialIndex >= head.asset.materials().size()) continue;
               var material = head.asset.materials().get(materialIndex);
               int textureIndex = material.baseColorTexture();
               if (textureIndex < 0 || textureIndex >= head.asset.textures().size()) continue;
               BufferedImage texture = images.get(textureIndex);
               if (texture == null) {
                  ByteBuffer encoded = head.asset.textures().get(textureIndex).encodedImage().duplicate();
                  encoded.rewind();
                  byte[] bytes = new byte[encoded.remaining()];
                  encoded.get(bytes);
                  texture = ImageIO.read(new ByteArrayInputStream(bytes));
                  if (texture == null) throw new IOException("Cannot decode portrait texture " + textureIndex);
                  images.put(textureIndex, texture);
               }
               float[] pos = primitive.positions();
               float[] uv = primitive.texCoords0();
               if (pos == null || uv == null) continue;
               int[] indices = primitive.indices();
               if (indices == null || indices.length == 0) {
                  indices = new int[primitive.vertexCount()];
                  for (int i = 0; i < indices.length; i++) indices[i] = i;
               }
               for (int i = 0; i + 2 < indices.length; i += 3) {
                  Vertex a = vertex(pos, uv, indices[i], transforms, nodeIndex, centerX, centerY, scale);
                  Vertex b = vertex(pos, uv, indices[i + 1], transforms, nodeIndex, centerX, centerY, scale);
                  Vertex c = vertex(pos, uv, indices[i + 2], transforms, nodeIndex, centerX, centerY, scale);
                  if (a != null && b != null && c != null)
                     pixels += triangle(output, depth, texture, a, b, c, material.alphaCutoff());
               }
            }
         }
      }
      if (pixels == 0) { output.close(); return null; }
      return output;
   }

   private static Vertex vertex(float[] positions, float[] texCoords, int index, float[] matrices,
      int node, float centerX, float centerY, float scale) {
      int p = index * 3, t = index * 2, m = node * 16;
      if (p < 0 || p + 2 >= positions.length || t + 1 >= texCoords.length || m + 15 >= matrices.length) return null;
      float x = positions[p], y = positions[p + 1], z = positions[p + 2];
      float worldX = matrices[m] * x + matrices[m + 4] * y + matrices[m + 8] * z + matrices[m + 12];
      float worldY = matrices[m + 1] * x + matrices[m + 5] * y + matrices[m + 9] * z + matrices[m + 13];
      float worldZ = matrices[m + 2] * x + matrices[m + 6] * y + matrices[m + 10] * z + matrices[m + 14];
      return new Vertex(SIZE / 2f - (worldX - centerX) * scale,
         SIZE / 2f - (worldY - centerY) * scale, -worldZ, texCoords[t], texCoords[t + 1]);
   }

   private static int triangle(NativeImage target, float[] depth, BufferedImage texture,
      Vertex a, Vertex b, Vertex c, float cutoff) {
      float area = (b.y - c.y) * (a.x - c.x) + (c.x - b.x) * (a.y - c.y);
      if (Math.abs(area) < 0.00001f) return 0;
      int minX = Math.max(0, (int) Math.floor(Math.min(a.x, Math.min(b.x, c.x))));
      int maxX = Math.min(SIZE - 1, (int) Math.ceil(Math.max(a.x, Math.max(b.x, c.x))));
      int minY = Math.max(0, (int) Math.floor(Math.min(a.y, Math.min(b.y, c.y))));
      int maxY = Math.min(SIZE - 1, (int) Math.ceil(Math.max(a.y, Math.max(b.y, c.y))));
      int written = 0;
      for (int y = minY; y <= maxY; y++) for (int x = minX; x <= maxX; x++) {
         float px = x + 0.5f, py = y + 0.5f;
         float wa = ((b.y - c.y) * (px - c.x) + (c.x - b.x) * (py - c.y)) / area;
         float wb = ((c.y - a.y) * (px - c.x) + (a.x - c.x) * (py - c.y)) / area;
         float wc = 1f - wa - wb;
         if (wa < 0 || wb < 0 || wc < 0) continue;
         float z = wa * a.z + wb * b.z + wc * c.z;
         int at = y * SIZE + x;
         if (z <= depth[at]) continue;
         float u = wa * a.u + wb * b.u + wc * c.u;
         float v = wa * a.v + wb * b.v + wc * c.v;
         int tx = Math.min(texture.getWidth() - 1, Math.max(0, (int) (u * texture.getWidth())));
         int ty = Math.min(texture.getHeight() - 1, Math.max(0, (int) (v * texture.getHeight())));
         int argb = texture.getRGB(tx, ty);
         if (((argb >>> 24) & 255) < Math.max(1, (int) (cutoff * 255))) continue;
         target.setPixelRGBA(x, y, (argb & 0xff00ff00) | ((argb & 0xff) << 16) | ((argb >>> 16) & 0xff));
         depth[at] = z;
         written++;
      }
      return written;
   }

   private record Vertex(float x, float y, float z, float u, float v) { }

   static Head headGeometry(GltfAsset source, String model, long stamp) {
      List<GltfNode> nodes = source.nodes();
      int head = -1;
      Set<Integer> subtree = Set.of();
      int largestGroup = -1;
      boolean exactHead = false;
      for (int i = 0; i < nodes.size(); i++) {
         String name = nodes.get(i).name();
         if (name != null && name.toLowerCase(Locale.ROOT).contains("head")) {
            Set<Integer> candidate = subtree(nodes, i);
            int meshes = 0;
            for (int index : candidate) meshes += nodes.get(index).meshes().length;
            // A parent named Head may contain the face, hair and hat as separate meshes.
            boolean exact = name.equalsIgnoreCase("head");
            if (head < 0 || exact && !exactHead || exact == exactHead && meshes >= largestGroup) {
               head = i;
               subtree = candidate;
               largestGroup = meshes;
               exactHead = exact;
            }
         }
      }
      if (head < 0) return null;
      float[] transforms;
      try (GltfInstance instance = new GltfInstance(source)) {
         transforms = instance.nodes().computeWorldMatrices(instance.animations().pose());
      }
      float[] bounds = {Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY,
         Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY};
      List<GltfMesh> meshes = new ArrayList<>(source.meshes().size());
      for (int i = 0; i < source.meshes().size(); i++) meshes.add(source.meshes().get(i));
      boolean found = false;
      Set<Integer> visibleNodes = new HashSet<>();
      int selectedMeshes = 0;
      for (int i = 0; i < nodes.size(); i++) {
         GltfNode node = nodes.get(i);
         for (int meshIndex : node.meshes()) {
            if (meshIndex < 0 || meshIndex >= meshes.size()) continue;
            GltfMesh mesh = meshes.get(meshIndex);
            List<GltfPrimitive> selected = new ArrayList<>();
            for (GltfPrimitive primitive : mesh.primitives()) {
               GltfPrimitive filtered = null;
               if (node.skin() >= 0 && node.skin() < source.skins().size()) {
                  filtered = selectSkinHead(primitive, source.skins().get(node.skin()), subtree, bounds, transforms, i);
               } else if (subtree.contains(i)) {
                  filtered = primitive;
                  for (float[] point : points(primitive.positions(), primitive.indices())) include(bounds, point, transforms, i);
               }
               if (filtered != null) { selected.add(filtered); found = true; }
            }
            if (!selected.isEmpty()) {
               meshes.set(meshIndex, new GltfMesh(mesh.name(), selected, mesh.defaultMorphWeights()));
               selectedMeshes++;
               int parent = i;
               while (parent >= 0 && parent < nodes.size()) {
                  visibleNodes.add(parent);
                  parent = nodes.get(parent).parent();
               }
            }
         }
      }
      if (!found || !Float.isFinite(bounds[0])) return null;
      ResourceLocation id = new ResourceLocation("storytelling", "portraits/" +
         Integer.toHexString(model.hashCode()) + "_" + Long.toHexString(stamp) + ".glb");
      GltfAsset asset = new GltfAsset(id, source.scenes(), source.nodes(), meshes, source.materials(),
         source.textures(), source.skins(), source.animations(), source.extensionsUsed(),
         source.extensionsRequired(), source.defaultScene());
      return new Head(asset, bounds[0], bounds[1], bounds[2], bounds[3], bounds[4], bounds[5],
         Set.copyOf(visibleNodes), selectedMeshes);
   }

   private static Set<Integer> subtree(List<GltfNode> nodes, int root) {
      Set<Integer> result = new HashSet<>();
      for (int i = 0; i < nodes.size(); i++) {
         int parent = i;
         while (parent >= 0 && parent < nodes.size()) {
            if (parent == root) { result.add(i); break; }
            parent = nodes.get(parent).parent();
         }
      }
      return result;
   }

   private static GltfPrimitive selectSkinHead(GltfPrimitive p, GltfSkin skin, Set<Integer> subtree,
      float[] bounds, float[] transforms, int node) {
      if (p.mode() != PrimitiveMode.TRIANGLES) return null;
      int[] joints = p.joints();
      float[] weights = p.weights();
      float[] positions = p.positions();
      if (joints == null || weights == null || joints.length < p.vertexCount() * 4
         || weights.length < p.vertexCount() * 4) return null;
      int[] source = p.indices();
      if (source == null || source.length == 0) {
         source = new int[p.vertexCount()];
         for (int i = 0; i < source.length; i++) source[i] = i;
      }
      int[] selected = new int[source.length];
      int count = 0;
      for (int i = 0; i + 2 < source.length; i += 3) {
         int matches = 0;
         for (int corner = 0; corner < 3; corner++) {
            int vertex = source[i + corner];
            if (vertex < 0 || vertex >= p.vertexCount()) continue;
            float headWeight = 0;
            for (int j = 0; j < 4; j++) {
               int joint = joints[vertex * 4 + j];
               if (joint >= 0 && joint < skin.joints().length && subtree.contains(skin.joints()[joint]))
                  headWeight += weights[vertex * 4 + j];
            }
            if (headWeight >= 0.4f) matches++;
         }
         if (matches >= 2) {
            for (int corner = 0; corner < 3; corner++) {
               int vertex = source[i + corner];
               selected[count++] = vertex;
               int offset = vertex * 3;
               if (offset + 2 < positions.length)
                  include(bounds, new float[] {positions[offset], positions[offset + 1], positions[offset + 2]}, transforms, node);
            }
         }
      }
      if (count == 0) return null;
      return new GltfPrimitive(p.mode(), p.material(), p.vertexCount(), positions, p.normals(), p.tangents(),
         p.texCoords0(), p.texCoords1(), p.colors(), joints, weights, Arrays.copyOf(selected, count), p.morphTargets());
   }

   private static List<float[]> points(float[] positions, int[] indices) {
      List<float[]> result = new ArrayList<>();
      if (positions == null) return result;
      if (indices == null || indices.length == 0) {
         for (int i = 0; i + 2 < positions.length; i += 3)
            result.add(new float[] {positions[i], positions[i + 1], positions[i + 2]});
      } else {
         for (int index : indices) {
            int i = index * 3;
            if (i >= 0 && i + 2 < positions.length)
               result.add(new float[] {positions[i], positions[i + 1], positions[i + 2]});
         }
      }
      return result;
   }

   private static void include(float[] bounds, float[] point, float[] transforms, int node) {
      int m = node * 16;
      if (m + 15 >= transforms.length) return;
      float x = point[0], y = point[1], z = point[2];
      float[] world = {
         transforms[m] * x + transforms[m + 4] * y + transforms[m + 8] * z + transforms[m + 12],
         transforms[m + 1] * x + transforms[m + 5] * y + transforms[m + 9] * z + transforms[m + 13],
         transforms[m + 2] * x + transforms[m + 6] * y + transforms[m + 10] * z + transforms[m + 14]
      };
      for (int i = 0; i < 3; i++) {
         bounds[i] = Math.min(bounds[i], world[i]);
         bounds[i + 3] = Math.max(bounds[i + 3], world[i]);
      }
   }

   record Head(GltfAsset asset, float minX, float minY, float minZ,
      float maxX, float maxY, float maxZ, Set<Integer> visibleNodes, int selectedMeshes) { }

   private record Portrait(long stamp, ResourceLocation texture) {
      void close() { if (texture != null) Minecraft.getInstance().getTextureManager().release(texture); }
   }
}
