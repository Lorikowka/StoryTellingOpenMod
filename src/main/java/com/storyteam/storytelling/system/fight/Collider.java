package com.storyteam.storytelling.system.fight;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import com.storyteam.storytelling.content.entities.AnimatedEntityLikeHumanoid;
import com.storyteam.storytelling.content.entities.StoryEntityType;
import com.storyteam.storytelling.mixins._client.hammeranims.ModelCubeFAccessor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix4d;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector2i;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.zeith.hammeranims.api.geometry.IGeometryContainer;
import org.zeith.hammeranims.api.geometry.model.IGeometricModel;
import org.zeith.hammeranims.api.geometry.model.IRenderableBone;
import org.zeith.hammeranims.core.client.model.ModelBoneF;
import org.zeith.hammeranims.core.client.model.ModelCubeF;
import org.zeith.hammeranims.core.client.model.TexturedQuadF;
import org.zeith.hammeranims.core.client.model.VertexF;
import org.zeith.hammeranims.core.client.model.CubeUVs.BoxUVResolver;
import org.zeith.hammerlib.util.java.Cast;

public class Collider {
   public static Matrix4d getMatrix4dBone(Entity e, String bone) {
      if (e instanceof AnimatedEntityLikeHumanoid entity) {
         Optional<Matrix4d> matrix4d_opt = entity.getTransforms(bone, true);
         return matrix4d_opt.orElse(null);
      } else {
         return null;
      }
   }

   public static ModelBoneF getModelBone(Entity e, String bone) {
      if (e instanceof AnimatedEntityLikeHumanoid entity) {
         Matrix4d matrix4d = getMatrix4dBone(e, bone);
         if (matrix4d == null) {
            return null;
         } else {
            StoryEntityType<AnimatedEntityLikeHumanoid> stt = (StoryEntityType<AnimatedEntityLikeHumanoid>)Cast.cast(entity.getType());
            if (stt == null) {
               return null;
            } else {
               IGeometryContainer model = (IGeometryContainer)stt.getProperties().getModel().apply(entity);
               IGeometricModel iGeometricModel = model.createModel();
               IRenderableBone modelBoneFf = iGeometricModel.getBone(bone);
               return modelBoneFf instanceof ModelBoneF ? (ModelBoneF)modelBoneFf : null;
            }
         }
      } else {
         return null;
      }
   }

   public static Set<Vector3d> getGlobalCorners(ModelCubeF cube, Matrix4d transformMatrix) {
      Set<Vector3d> corners = new HashSet<>();

      for (TexturedQuadF quad : ((ModelCubeFAccessor)cube).getQuads()) {
         for (VertexF vertex : quad.vertices) {
            Vector3d pos = new Vector3d((double)(vertex.getPos().x() / 16.0F), (double)(vertex.getPos().y() / 16.0F), (double)(vertex.getPos().z() / 16.0F));
            pos = transformMatrix.transformPosition(pos);
            corners.add(pos);
         }
      }

      return corners;
   }

   public static Vec3 getMinCoord(Entity attacker, String bone) {
      if (!(attacker instanceof AnimatedEntityLikeHumanoid entityAttacker)) {
         return Vec3.ZERO;
      } else if (entityAttacker.getAttackAnimAction() != null && entityAttacker.getAttackAnimAction().is_active()) {
         ModelBoneF modelBoneF = getModelBone(entityAttacker, bone);
         Matrix4d matrix4d = new Matrix4d(getMatrix4dBone(entityAttacker, bone));
         if (matrix4d != null && modelBoneF != null) {
            List<ModelCubeF> cubes = modelBoneF.cubes;
            if (cubes.isEmpty()) {
               cubes = new ArrayList<>();

               for (ModelBoneF boneF : modelBoneF.getChildren().values()) {
                  cubes.addAll(boneF.cubes);
               }

               if (cubes.isEmpty()) {
                  return Vec3.ZERO;
               }
            }

            Set<Vector3d> vector3dSet = new HashSet<>();

            for (ModelCubeF cube : cubes) {
               vector3dSet.addAll(getCorners(cube));
            }

            double minX = Double.MAX_VALUE;
            double minY = Double.MAX_VALUE;
            double minZ = Double.MAX_VALUE;
            double maxX = Double.MIN_VALUE;
            double maxY = Double.MIN_VALUE;
            double maxZ = Double.MIN_VALUE;

            for (Vector3d vec : vector3dSet) {
               if (vec.x() < minX) {
                  minX = vec.x();
               }

               if (vec.y() < minY) {
                  minY = vec.y();
               }

               if (vec.z() < minZ) {
                  minZ = vec.z();
               }

               if (vec.x() > maxX) {
                  maxX = vec.x();
               }

               if (vec.y() > maxY) {
                  maxY = vec.y();
               }

               if (vec.z() > maxZ) {
                  maxZ = vec.z();
               }
            }

            Vector3d vec3d = matrix4d.transformPosition(new Vector3d(minX, minY, minZ));
            Optional<Vec3> vec3d_opt = Optional.of(new Vec3(vec3d.x(), vec3d.y(), vec3d.z()));
            return vec3d_opt.get();
         } else {
            return Vec3.ZERO;
         }
      } else {
         return Vec3.ZERO;
      }
   }

   public static Vec3 getMaxCoord(Entity attacker, String bone) {
      if (!(attacker instanceof AnimatedEntityLikeHumanoid entityAttacker)) {
         return Vec3.ZERO;
      } else if (entityAttacker.getAttackAnimAction() != null && entityAttacker.getAttackAnimAction().is_active()) {
         ModelBoneF modelBoneF = getModelBone(entityAttacker, bone);
         Matrix4d matrix4d = new Matrix4d(getMatrix4dBone(entityAttacker, bone));
         if (matrix4d != null && modelBoneF != null) {
            List<ModelCubeF> cubes = modelBoneF.cubes;
            if (cubes.isEmpty()) {
               cubes = new ArrayList<>();

               for (ModelBoneF boneF : modelBoneF.getChildren().values()) {
                  cubes.addAll(boneF.cubes);
               }

               if (cubes.isEmpty()) {
                  return Vec3.ZERO;
               }
            }

            Set<Vector3d> vector3dSet = new HashSet<>();

            for (ModelCubeF cube : cubes) {
               vector3dSet.addAll(getCorners(cube));
            }

            double minX = Double.MAX_VALUE;
            double minY = Double.MAX_VALUE;
            double minZ = Double.MAX_VALUE;
            double maxX = Double.MIN_VALUE;
            double maxY = Double.MIN_VALUE;
            double maxZ = Double.MIN_VALUE;

            for (Vector3d vec : vector3dSet) {
               if (vec.x() < minX) {
                  minX = vec.x();
               }

               if (vec.y() < minY) {
                  minY = vec.y();
               }

               if (vec.z() < minZ) {
                  minZ = vec.z();
               }

               if (vec.x() > maxX) {
                  maxX = vec.x();
               }

               if (vec.y() > maxY) {
                  maxY = vec.y();
               }

               if (vec.z() > maxZ) {
                  maxZ = vec.z();
               }
            }

            Vector3d vec3d = matrix4d.transformPosition(new Vector3d(maxX, maxY, maxZ));
            Optional<Vec3> vec3d_opt = Optional.of(new Vec3(vec3d.x(), vec3d.y(), vec3d.z()));
            return vec3d_opt.get();
         } else {
            return Vec3.ZERO;
         }
      } else {
         return Vec3.ZERO;
      }
   }

   public static boolean isCollidingBoxWithBone(Entity attacker, LivingEntity target, String sBone) {
      if (attacker instanceof AnimatedEntityLikeHumanoid entityAttacker) {
         if (entityAttacker.getAttackAnimAction() != null && entityAttacker.getAttackAnimAction().is_active()) {
            List<Pair<ModelBoneF, Matrix4d>> model_matrix = new ArrayList<>();
            List<ModelBoneF> bones = entityAttacker.getAttackAnimAction().getModelBoneFList();
            if (bones.isEmpty()) {
               return false;
            } else {
               for (ModelBoneF bone : bones) {
                  Matrix4d matrix4d = new Matrix4d(getMatrix4dBone(entityAttacker, bone.boxName));
                  if (matrix4d != null && Objects.equals(bone.boxName, sBone)) {
                     model_matrix.add(new Pair(bone, matrix4d));
                  }
               }

               return isCollidingBox(attacker, target, model_matrix);
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static boolean isCollidingBox(Entity attacker, LivingEntity target) {
      if (attacker instanceof AnimatedEntityLikeHumanoid entityAttacker) {
         if (entityAttacker.getAttackAnimAction() != null && entityAttacker.getAttackAnimAction().is_active()) {
            List<Pair<ModelBoneF, Matrix4d>> model_matrix = new ArrayList<>();
            List<ModelBoneF> bones = entityAttacker.getAttackAnimAction().getModelBoneFList();
            if (bones.isEmpty()) {
               return false;
            } else {
               for (ModelBoneF bone : bones) {
                  Matrix4d matrix4d = new Matrix4d(getMatrix4dBone(entityAttacker, bone.boxName));
                  if (matrix4d != null) {
                     model_matrix.add(new Pair(bone, matrix4d));
                  }
               }

               return isCollidingBox(attacker, target, model_matrix);
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static boolean isCollidingBox(Entity attacker, LivingEntity target, List<Pair<ModelBoneF, Matrix4d>> model_matrix) {
      if (!(attacker instanceof AnimatedEntityLikeHumanoid entityAttacker)) {
         return false;
      } else if (entityAttacker.getAttackAnimAction() != null && entityAttacker.getAttackAnimAction().is_active()) {
         for (Pair<ModelBoneF, Matrix4d> modelMatrix : model_matrix) {
            List<ModelCubeF> cubes = ((ModelBoneF)modelMatrix.getFirst()).cubes;
            if (cubes.isEmpty()) {
               cubes = new ArrayList<>();

               for (ModelBoneF boneF : ((ModelBoneF)modelMatrix.getFirst()).getChildren().values()) {
                  cubes.addAll(boneF.cubes);
               }

               if (cubes.isEmpty()) {
                  ModelCubeF cubeF = ModelCubeF.make(
                     new Vector3f(0.0F, 0.0F, 0.0F),
                     new Vector3f(2.0F, 2.0F, 2.0F),
                     new BoxUVResolver(new Vector2i(1, 1), new Vector3f(1.0F, 1.0F, 1.0F)),
                     2.0F,
                     false,
                     1,
                     1,
                     org.zeith.hammeranims.core.client.render.vertex.VertexType.DEFAULT
                  );
                  cubes.add(cubeF);
               }
            }

            Set<Vector3d> vector3dSet = new HashSet<>();

            for (ModelCubeF cube : cubes) {
               vector3dSet.addAll(getCorners(cube));
            }

            double minX = Double.MAX_VALUE;
            double minY = Double.MAX_VALUE;
            double minZ = Double.MAX_VALUE;
            double maxX = Double.MIN_VALUE;
            double maxY = Double.MIN_VALUE;
            double maxZ = Double.MIN_VALUE;

            for (Vector3d vec : vector3dSet) {
               if (vec.x() < minX) {
                  minX = vec.x();
               }

               if (vec.y() < minY) {
                  minY = vec.y();
               }

               if (vec.z() < minZ) {
                  minZ = vec.z();
               }

               if (vec.x() > maxX) {
                  maxX = vec.x();
               }

               if (vec.y() > maxY) {
                  maxY = vec.y();
               }

               if (vec.z() > maxZ) {
                  maxZ = vec.z();
               }
            }

            Vec3 inflate_v3 = entityAttacker.getAttackAnimAction().getInflate_v3();
            float inflate = entityAttacker.getAttackAnimAction().base_inflate * entityAttacker.getAttackAnimAction().inflateBoxFromTime();
            minX *= (double)inflate * inflate_v3.x;
            minY *= (double)inflate * inflate_v3.y;
            minZ *= (double)inflate * inflate_v3.z;
            maxX *= (double)inflate * inflate_v3.x;
            maxY *= (double)inflate * inflate_v3.y;
            maxZ *= (double)inflate * inflate_v3.z;
            Vector3d[] boundingBoxVertices = new Vector3d[]{
               new Vector3d(minX, minY, minZ),
               new Vector3d(maxX, minY, minZ),
               new Vector3d(minX, maxY, minZ),
               new Vector3d(minX, minY, maxZ),
               new Vector3d(maxX, maxY, minZ),
               new Vector3d(maxX, minY, maxZ),
               new Vector3d(minX, maxY, maxZ),
               new Vector3d(maxX, maxY, maxZ)
            };
            if (entityAttacker.previousBoundingBoxVertices == null) {
               entityAttacker.previousBoundingBoxVertices = boundingBoxVertices;
            }

            for (Vector3d vertex : boundingBoxVertices) {
               AttackAnimAction attackAnimAction = entityAttacker.getAttackAnimAction();
               Vec3 offsetVec = attackAnimAction.offsetV3;
               vertex = vertex.add(offsetVec.x(), offsetVec.x(), offsetVec.z());
               Vector3d vector3d1 = ((Matrix4d)modelMatrix.getSecond()).transformPosition(vertex);
            }

            Vector3d[] interpolatedVertices = interpolateBoundingBox(entityAttacker.previousBoundingBoxVertices, boundingBoxVertices, 0.5F);
            entityAttacker.previousBoundingBoxVertices = boundingBoxVertices;
            int[][] edgeIndices = new int[][]{
               {0, 1}, {0, 2}, {0, 3}, {1, 4}, {1, 5}, {2, 4}, {2, 6}, {3, 5}, {3, 6}, {4, 7}, {5, 7}, {6, 7}, {6, 0}, {2, 5}, {3, 4}
            };

            for (int[] edgeIndex : edgeIndices) {
               Vector3d vector3d1 = boundingBoxVertices[edgeIndex[0]];
               Optional<Vec3> vec31 = Optional.of(new Vec3(vector3d1.x(), vector3d1.y(), vector3d1.z()));
               Vector3d vector3d2 = boundingBoxVertices[edgeIndex[1]];
               Optional<Vec3> vec32 = Optional.of(new Vec3(vector3d2.x(), vector3d2.y(), vector3d2.z()));
               AABB playerbb = target.getBoundingBox();
               Optional<Vec3> vec3_res = playerbb.clip(vec31.get(), vec32.get());
               if (vec3_res.isPresent()) {
                  return true;
               }

               Optional<Vec3> vec3_res2 = playerbb.clip(vec32.get(), vec31.get());
               if (vec3_res2.isPresent()) {
                  return true;
               }
            }

            for (int[] edgeIndex : edgeIndices) {
               Vector3d vector3d1x = interpolatedVertices[edgeIndex[0]];
               Optional<Vec3> vec31x = Optional.of(new Vec3(vector3d1x.x(), vector3d1x.y(), vector3d1x.z()));
               Vector3d vector3d2x = interpolatedVertices[edgeIndex[1]];
               Optional<Vec3> vec32x = Optional.of(new Vec3(vector3d2x.x(), vector3d2x.y(), vector3d2x.z()));
               AABB playerbbx = target.getBoundingBox();
               Optional<Vec3> vec3_resx = playerbbx.clip(vec31x.get(), vec32x.get());
               if (vec3_resx.isPresent()) {
                  return true;
               }

               Optional<Vec3> vec3_res2 = playerbbx.clip(vec32x.get(), vec31x.get());
               if (vec3_res2.isPresent()) {
                  return true;
               }
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public static Vector3d[] interpolateBoundingBox(Vector3d[] previousVertices, Vector3d[] currentVertices, float partialTick) {
      if (previousVertices.length != currentVertices.length) {
         throw new IllegalArgumentException("The arrays of vertices must have the same size.");
      } else {
         Vector3d[] interpolatedVertices = new Vector3d[previousVertices.length];

         for (int i = 0; i < previousVertices.length; i++) {
            double x = Mth.lerp((double)partialTick, previousVertices[i].x(), currentVertices[i].x());
            double y = Mth.lerp((double)partialTick, previousVertices[i].y(), currentVertices[i].y());
            double z = Mth.lerp((double)partialTick, previousVertices[i].z(), currentVertices[i].z());
            interpolatedVertices[i] = new Vector3d(x, y, z);
         }

         return interpolatedVertices;
      }
   }

   public static ModelBoneF isCollidingBoxReturnBone(Entity attacker, LivingEntity target) {
      if (!(attacker instanceof AnimatedEntityLikeHumanoid entityAttacker)) {
         return null;
      } else if (entityAttacker.getAttackAnimAction() != null && entityAttacker.getAttackAnimAction().is_active()) {
         List<Pair<ModelBoneF, Matrix4d>> model_matrix = new ArrayList<>();
         List<ModelBoneF> bones = entityAttacker.getAttackAnimAction().getModelBoneFList();
         if (bones.isEmpty()) {
            return null;
         } else {
            for (ModelBoneF bone : bones) {
               Matrix4d matrix4d = new Matrix4d(getMatrix4dBone(entityAttacker, bone.boxName));
               if (matrix4d != null) {
                  model_matrix.add(new Pair(bone, matrix4d));
               }
            }

            for (Pair<ModelBoneF, Matrix4d> modelMatrix : model_matrix) {
               List<ModelCubeF> cubes = ((ModelBoneF)modelMatrix.getFirst()).cubes;
               if (cubes.isEmpty()) {
                  cubes = new ArrayList<>();

                  for (ModelBoneF boneF : ((ModelBoneF)modelMatrix.getFirst()).getChildren().values()) {
                     cubes.addAll(boneF.cubes);
                  }

                  if (cubes.isEmpty()) {
                     ModelCubeF cubeF = ModelCubeF.make(
                        new Vector3f(0.0F, 0.0F, 0.0F),
                        new Vector3f(2.0F, 2.0F, 2.0F),
                        new BoxUVResolver(new Vector2i(1, 1), new Vector3f(1.0F, 1.0F, 1.0F)),
                        2.0F,
                        false,
                        1,
                        1,
                        org.zeith.hammeranims.core.client.render.vertex.VertexType.DEFAULT
                     );
                     cubes.add(cubeF);
                  }
               }

               Set<Vector3d> vector3dSet = new HashSet<>();

               for (ModelCubeF cube : cubes) {
                  vector3dSet.addAll(getCorners(cube));
               }

               double minX = Double.MAX_VALUE;
               double minY = Double.MAX_VALUE;
               double minZ = Double.MAX_VALUE;
               double maxX = Double.MIN_VALUE;
               double maxY = Double.MIN_VALUE;
               double maxZ = Double.MIN_VALUE;

               for (Vector3d vec : vector3dSet) {
                  if (vec.x() < minX) {
                     minX = vec.x();
                  }

                  if (vec.y() < minY) {
                     minY = vec.y();
                  }

                  if (vec.z() < minZ) {
                     minZ = vec.z();
                  }

                  if (vec.x() > maxX) {
                     maxX = vec.x();
                  }

                  if (vec.y() > maxY) {
                     maxY = vec.y();
                  }

                  if (vec.z() > maxZ) {
                     maxZ = vec.z();
                  }
               }

               Vec3 inflate_v3 = entityAttacker.getAttackAnimAction().getInflate_v3();
               float inflate = entityAttacker.getAttackAnimAction().base_inflate * entityAttacker.getAttackAnimAction().inflateBoxFromTime();
               minX *= (double)inflate * inflate_v3.x;
               minY *= (double)inflate * inflate_v3.y;
               minZ *= (double)inflate * inflate_v3.z;
               maxX *= (double)inflate * inflate_v3.x;
               maxY *= (double)inflate * inflate_v3.y;
               maxZ *= (double)inflate * inflate_v3.z;
               Vector3d[] boundingBoxVertices = new Vector3d[]{
                  new Vector3d(minX, minY, minZ),
                  new Vector3d(maxX, minY, minZ),
                  new Vector3d(minX, maxY, minZ),
                  new Vector3d(minX, minY, maxZ),
                  new Vector3d(maxX, maxY, minZ),
                  new Vector3d(maxX, minY, maxZ),
                  new Vector3d(minX, maxY, maxZ),
                  new Vector3d(maxX, maxY, maxZ)
               };
               if (entityAttacker.previousBoundingBoxVertices == null) {
                  entityAttacker.previousBoundingBoxVertices = boundingBoxVertices;
               }

               for (Vector3d vertex : boundingBoxVertices) {
                  AttackAnimAction attackAnimAction = entityAttacker.getAttackAnimAction();
                  Vec3 offsetVec = attackAnimAction.offsetV3;
                  vertex = vertex.add(offsetVec.x(), offsetVec.x(), offsetVec.z());
                  Vector3d vector3d1 = ((Matrix4d)modelMatrix.getSecond()).transformPosition(vertex);
               }

               Vector3d[] interpolatedVertices = interpolateBoundingBox(entityAttacker.previousBoundingBoxVertices, boundingBoxVertices, 0.5F);
               entityAttacker.previousBoundingBoxVertices = boundingBoxVertices;
               int[][] edgeIndices = new int[][]{
                  {0, 1}, {0, 2}, {0, 3}, {1, 4}, {1, 5}, {2, 4}, {2, 6}, {3, 5}, {3, 6}, {4, 7}, {5, 7}, {6, 7}, {6, 0}, {2, 5}, {3, 4}
               };

               for (int[] edgeIndex : edgeIndices) {
                  Vector3d vector3d1 = boundingBoxVertices[edgeIndex[0]];
                  Optional<Vec3> vec31 = Optional.of(new Vec3(vector3d1.x(), vector3d1.y(), vector3d1.z()));
                  Vector3d vector3d2 = boundingBoxVertices[edgeIndex[1]];
                  Optional<Vec3> vec32 = Optional.of(new Vec3(vector3d2.x(), vector3d2.y(), vector3d2.z()));
                  AABB playerbb = target.getBoundingBox();
                  Optional<Vec3> vec3_res = playerbb.clip(vec31.get(), vec32.get());
                  if (vec3_res.isPresent()) {
                     return (ModelBoneF)modelMatrix.getFirst();
                  }

                  Optional<Vec3> vec3_res2 = playerbb.clip(vec32.get(), vec31.get());
                  if (vec3_res2.isPresent()) {
                     return (ModelBoneF)modelMatrix.getFirst();
                  }
               }

               for (int[] edgeIndex : edgeIndices) {
                  Vector3d vector3d1x = interpolatedVertices[edgeIndex[0]];
                  Optional<Vec3> vec31x = Optional.of(new Vec3(vector3d1x.x(), vector3d1x.y(), vector3d1x.z()));
                  Vector3d vector3d2x = interpolatedVertices[edgeIndex[1]];
                  Optional<Vec3> vec32x = Optional.of(new Vec3(vector3d2x.x(), vector3d2x.y(), vector3d2x.z()));
                  AABB playerbbx = target.getBoundingBox();
                  Optional<Vec3> vec3_resx = playerbbx.clip(vec31x.get(), vec32x.get());
                  if (vec3_resx.isPresent()) {
                     return (ModelBoneF)modelMatrix.getFirst();
                  }

                  Optional<Vec3> vec3_res2 = playerbbx.clip(vec32x.get(), vec31x.get());
                  if (vec3_res2.isPresent()) {
                     return (ModelBoneF)modelMatrix.getFirst();
                  }
               }
            }

            return null;
         }
      } else {
         return null;
      }
   }

   public static Set<Vector3d> getCorners(ModelCubeF cube) {
      Set<Vector3d> corners = new HashSet<>();

      for (TexturedQuadF quad : ((ModelCubeFAccessor)cube).getQuads()) {
         for (VertexF vertex : quad.vertices) {
            Vector3d pos = new Vector3d((double)(vertex.getPos().x() / 16.0F), (double)(vertex.getPos().y() / 16.0F), (double)(vertex.getPos().z() / 16.0F));
            corners.add(new Vector3d(pos.x(), pos.y(), pos.z()));
         }
      }

      return corners;
   }

   @OnlyIn(Dist.CLIENT)
   public static void draw(PoseStack matrixStack, MultiBufferSource buffer, Entity e, List<ModelBoneF> bones, float partialTicks) {
      if (e instanceof AnimatedEntityLikeHumanoid entity) {
         ArrayList<Pair<ModelBoneF, Matrix4d>> model_matrix = new ArrayList<>();
         if (!bones.isEmpty()) {
            for (ModelBoneF bone : bones) {
               Matrix4d matrix4d = new Matrix4d(getMatrix4dBone(e, bone.boxName));
               if (matrix4d != null) {
                  model_matrix.add(new Pair(bone, matrix4d));
               }
            }

            for (Pair<ModelBoneF, Matrix4d> modelMatrix : model_matrix) {
               Set<Vector3d> vec3dSet = new HashSet<>();
               List<ModelCubeF> cubes = new ArrayList<>(((ModelBoneF)modelMatrix.getFirst()).cubes);
               if (cubes.isEmpty()) {
                  cubes = new ArrayList<>();

                  for (ModelBoneF boneF : ((ModelBoneF)modelMatrix.getFirst()).getChildren().values()) {
                     cubes.addAll(boneF.cubes);
                  }

                  if (cubes.isEmpty()) {
                     ModelCubeF cubeF = ModelCubeF.make(
                        new Vector3f(0.0F, 0.0F, 0.0F),
                        new Vector3f(2.0F, 2.0F, 2.0F),
                        new BoxUVResolver(new Vector2i(1, 1), new Vector3f(1.0F, 1.0F, 1.0F)),
                        2.0F,
                        false,
                        1,
                        1,
                        org.zeith.hammeranims.core.client.render.vertex.VertexType.DEFAULT
                     );
                     cubes.add(cubeF);
                  }
               }

               for (ModelCubeF cube : cubes) {
                  vec3dSet.addAll(getCorners(cube));
               }

               drawVertex(matrixStack, (Matrix4d)modelMatrix.getSecond(), entity, vec3dSet, buffer);
            }
         }
      }
   }

   public static void drawVertex(PoseStack matrixStack, Matrix4d matrix4d, AnimatedEntityLikeHumanoid entity, Set<Vector3d> vec3dSet, MultiBufferSource buffer) {
      matrixStack.pushPose();
      AttackAnimAction attackAnimAction = entity.getAttackAnimAction();
      Vec3 offsetVec = attackAnimAction.offsetV3;
      Vector3d vec3d = matrix4d.transformPosition(new Vector3d(offsetVec.x(), offsetVec.y(), offsetVec.z()));
      Optional<Vec3> vec3d_opt = Optional.of(new Vec3(vec3d.x(), vec3d.y(), vec3d.z()));
      Vec3 vec3 = vec3d_opt.get();
      Vec3 translate = vec3.subtract(entity.position());
      matrixStack.translate(translate.x(), translate.y(), translate.z());
      Quaternionf quaternionf = new Quaternionf();
      matrix4d.getNormalizedRotation(quaternionf);
      matrixStack.mulPose(quaternionf);
      double minX = Double.MAX_VALUE;
      double minY = Double.MAX_VALUE;
      double minZ = Double.MAX_VALUE;
      double maxX = Double.MIN_VALUE;
      double maxY = Double.MIN_VALUE;
      double maxZ = Double.MIN_VALUE;

      for (Vector3d vec : vec3dSet) {
         if (vec.x() < minX) {
            minX = vec.x();
         }

         if (vec.y() < minY) {
            minY = vec.y();
         }

         if (vec.z() < minZ) {
            minZ = vec.z();
         }

         if (vec.x() > maxX) {
            maxX = vec.x();
         }

         if (vec.y() > maxY) {
            maxY = vec.y();
         }

         if (vec.z() > maxZ) {
            maxZ = vec.z();
         }
      }

      Vec3 inflate_v3 = entity.getAttackAnimAction().getInflate_v3();
      float inflate = entity.getAttackAnimAction().base_inflate * entity.getAttackAnimAction().inflateBoxFromTime();
      minX *= (double)inflate * inflate_v3.x;
      minY *= (double)inflate * inflate_v3.y;
      minZ *= (double)inflate * inflate_v3.z;
      maxX *= (double)inflate * inflate_v3.x;
      maxY *= (double)inflate * inflate_v3.y;
      maxZ *= (double)inflate * inflate_v3.z;
      Vector3d[] boundingBoxVertices = new Vector3d[]{
         new Vector3d(minX, minY, minZ),
         new Vector3d(maxX, minY, minZ),
         new Vector3d(minX, maxY, minZ),
         new Vector3d(minX, minY, maxZ),
         new Vector3d(maxX, maxY, minZ),
         new Vector3d(maxX, minY, maxZ),
         new Vector3d(minX, maxY, maxZ),
         new Vector3d(maxX, maxY, maxZ)
      };
      VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.lines());
      Matrix4f matrix = matrixStack.last().pose();
      int[][] edgeIndices = new int[][]{{0, 1}, {0, 2}, {0, 3}, {1, 4}, {1, 5}, {2, 4}, {2, 6}, {3, 5}, {3, 6}, {4, 7}, {5, 7}, {6, 7}};

      for (int[] edgeIndex : edgeIndices) {
         Vector3d vector3d1 = boundingBoxVertices[edgeIndex[0]];
         Vector3d vector3d2 = boundingBoxVertices[edgeIndex[1]];
         vertexConsumer.vertex(matrix, (float)vector3d1.x(), (float)vector3d1.y(), (float)vector3d1.z())
            .color(2, 255, 2, 255)
            .normal(0.0F, 0.0F, 1.0F)
            .endVertex();
         vertexConsumer.vertex(matrix, (float)vector3d2.x(), (float)vector3d2.y(), (float)vector3d2.z())
            .color(2, 255, 2, 255)
            .normal(0.0F, 0.0F, 1.0F)
            .endVertex();
      }

      matrixStack.popPose();
   }
}
