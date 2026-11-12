package com.storyteam.storytelling.system._core.st_cutscene;

import com.storyteam.storytelling.content.entities.STCameraEntity;
import com.storyteam.storytelling.init.EntitiesST;
import com.storyteam.storytelling.util.MathUtils;
import com.storyteam.storytelling.util.interpolation.Interpolation;
import com.storyteam.storytelling.util.splines.Spline;
import com.storyteam.storytelling.util.world.Location;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent.LoggingOut;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(
   modid = "storytelling",
   bus = Bus.FORGE,
   value = {Dist.CLIENT}
)
public class ClientsideCutsceneHandler {
   public static STCutsceneData currentData;
   public static Spline spline;
   public static Interpolation timeInterpolation;
   public static Interpolation lookInterpolation;
   public static int cutsceneTime;
   public static int fullCutsceneTime;
   public static STCameraEntity cameraEntity;
   public static CameraType lastCameraType;

   public static void initOrTpCamera(STCutsceneData data) {
      currentData = data;
      cutsceneTime = 0;
      fullCutsceneTime = data.time;
      lastCameraType = Minecraft.getInstance().options.getCameraType();
      Minecraft.getInstance().options.setCameraType(net.minecraft.client.CameraType.FIRST_PERSON);
      if (data.locations.size() > 1) {
         spline = data.splineType.splineFactory.apply(data.locations.toArray(new Location[0]));
         timeInterpolation = data.interpolationType.interpolationFactory.get();
         lookInterpolation = data.lookInterpolationType.interpolationFactory.get();
      } else {
         spline = null;
         timeInterpolation = null;
         lookInterpolation = null;
      }

      if (cameraEntity == null) {
         STCameraEntity entity = new STCameraEntity(EntitiesST.ST_CAMERA_ENTITY, Minecraft.getInstance().level);
         cameraEntity = entity;
      }

      Location l = data.locations.get(0);
      cameraEntity.setPos(l);
      cameraEntity.setPos(l);
      cameraEntity.setXRot(l.getPitch());
      cameraEntity.setYRot(l.getYaw());
      cameraEntity.yRotO = l.getYaw();
      cameraEntity.xRotO = l.getPitch();
      Minecraft.getInstance().setCameraEntity(cameraEntity);
   }

   public static void moveCamera(STCutsceneData data) {
      if (currentData == null) {
         throw new RuntimeException("Camera was not initialized");
      } else {
         Location location = getCurrentCameraLocation(
            currentData.locations.get(0), spline, timeInterpolation, lookInterpolation, cutsceneTime, fullCutsceneTime, 0.0F
         );
         data.locations.add(0, location);
         currentData = data;
         cutsceneTime = 0;
         fullCutsceneTime = data.time;
         spline = data.splineType.splineFactory.apply(data.locations.toArray(new Location[0]));
         timeInterpolation = data.interpolationType.interpolationFactory.get();
         lookInterpolation = data.lookInterpolationType.interpolationFactory.get();
      }
   }

   public static void stopCamera() {
      if (Minecraft.getInstance().player != null) {
         Minecraft.getInstance().setCameraEntity(Minecraft.getInstance().player);
      }

      currentData = null;
      cutsceneTime = 0;
      fullCutsceneTime = 0;
      cameraEntity = null;
      timeInterpolation = null;
      spline = null;
      lookInterpolation = null;
      if (lastCameraType != null) {
         Minecraft.getInstance().options.setCameraType(lastCameraType);
         lastCameraType = null;
      }

      Minecraft.getInstance().mouseHandler.grabMouse();
   }

   public static Location getCurrentCameraLocation(
      Location fallbackStartPos, Spline spline, Interpolation timeInterpolation, Interpolation lookInterpolation, int time, int maxTime, float pticks
   ) {
      if (spline != null) {
         float percent = Mth.clamp(timeInterpolation.interpolate(((float)time + pticks) / (float)maxTime), 0.0F, 1.0F);
         int pointCount = spline.points.length - 1;
         float globalPercent = percent * (float)pointCount;
         int id = (int)globalPercent;
         float localPercent = globalPercent - (float)id;
         Location splinePoint = spline.getLocalPoint(id, localPercent);
         float yaw;
         float pitch;
         if (id + 1 < spline.points.length) {
            Location currentPoint = spline.points[id];
            Location nextPoint = spline.points[id + 1];
            float cyaw = currentPoint.getYaw();
            float nyaw = nextPoint.getYaw();
            float cpitch = currentPoint.getPitch();
            float npitch = nextPoint.getPitch();
            float l = lookInterpolation.interpolate(localPercent);
            if (Math.abs(nyaw - cyaw) > 180.0F) {
               if (cyaw > nyaw) {
                  yaw = MathUtils.lerpThroughBoundaries(nyaw, cyaw, 1.0F - l, 0.0F, 360.0F);
               } else {
                  yaw = MathUtils.lerpThroughBoundaries(cyaw, nyaw, l, 0.0F, 360.0F);
               }
            } else {
               yaw = Mth.lerp(l, cyaw, nyaw);
            }

            pitch = Mth.lerp(l, cpitch, npitch);
         } else {
            Location finalPoint = spline.points[spline.points.length - 1];
            yaw = finalPoint.getYaw();
            pitch = finalPoint.getPitch();
         }

         return new Location(fallbackStartPos.getDimension(), splinePoint.x, splinePoint.y, splinePoint.z, yaw, pitch);
      } else {
         return fallbackStartPos;
      }
   }

   public static float transformYaw(float yaw) {
      if (yaw >= 0.0F) {
         yaw %= 360.0F;
      } else {
         yaw = yaw % 360.0F + 360.0F;
      }

      return yaw;
   }

   private static void tickCameraEntity() {
      Location point = getCurrentCameraLocation(
         currentData.locations.get(0), spline, timeInterpolation, lookInterpolation, cutsceneTime, fullCutsceneTime, 0.0F
      );
      Vec3 camPos = cameraEntity.position();
      cameraEntity.setPos(point);
      cameraEntity.tick();
      cameraEntity.setXRot(point.getPitch());
      cameraEntity.setYRot(point.getYaw());
   }

   @SubscribeEvent
   public static void tickClientPlayer(PlayerTickEvent event) {
      if (currentData != null && event.phase == Phase.START && event.player instanceof LocalPlayer player) {
         cutsceneTime = Mth.clamp(cutsceneTime + 1, 0, fullCutsceneTime);
         tickCameraEntity();
         if (!currentData.cameraOut) {
            Minecraft.getInstance().mouseHandler.releaseMouse();
         }
      }
   }

   @SubscribeEvent
   public static void onLogoff(LoggingOut event) {
      currentData = null;
      cutsceneTime = 0;
      fullCutsceneTime = 0;
      cameraEntity = null;
      timeInterpolation = null;
      spline = null;
      ServersideCutsceneHandler.currentData = null;
      ServersideCutsceneHandler.cutsceneTime = 0;
      ServersideCutsceneHandler.fullCutsceneTime = 0;
      ServersideCutsceneHandler.timeInterpolation = null;
      ServersideCutsceneHandler.spline = null;
      ServersideCutsceneHandler.cachedGameType = null;
   }
}
