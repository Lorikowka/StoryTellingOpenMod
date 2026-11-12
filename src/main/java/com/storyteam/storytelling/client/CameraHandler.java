package com.storyteam.storytelling.client;

import com.storyteam.storytelling.net.StopCameraPathPacket;
import com.storyteam.storytelling.util.world.Location;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent.ComputeCameraAngles;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.zeith.hammerlib.net.Network;

@EventBusSubscriber({Dist.CLIENT})
public class CameraHandler {
   public static float progress0 = 0.0F;
   public static Location startPos = null;
   public static Location targetPos = null;
   public static boolean slowCameraStart = false;
   public static boolean slowCameraEnd = false;
   public static float speed = 1.0F;

   @SubscribeEvent
   public static void tick(ClientTickEvent event) {
      if (event.phase == Phase.START && startPos != null && targetPos != null) {
         Vec3 pos = Minecraft.getInstance().player.position();
         Vec3 target = targetPos;
         Vec3 diff = target.subtract(pos);
         double remainDistance = Math.abs(diff.length());
         double fullDistance = Math.abs(target.distanceTo(startPos));
         float progress = (float)(1.0 - remainDistance / fullDistance);
         float speedMod = 1.0F;
         if (slowCameraStart && (double)progress < 0.25) {
            speedMod = Mth.clamp(progress * 4.0F, 0.1F, 1.0F);
         }

         if (slowCameraEnd && (double)progress > 0.75) {
            speedMod = Mth.clamp((1.0F - progress) * 4.0F, 0.1F, 1.0F);
         }

         float speed = CameraHandler.speed * speedMod;
         Vec3 delta = diff.normalize().scale((double)speed);
         Minecraft.getInstance().player.setDeltaMovement(delta);
         if (remainDistance < 0.25) {
            Minecraft.getInstance().player.setPosRaw(targetPos.x, targetPos.y, targetPos.z);
            Minecraft.getInstance().player.setPos(targetPos.x, targetPos.y, targetPos.z);
            Minecraft.getInstance().player.setYRot(targetPos.getYaw() % 360.0F);
            Minecraft.getInstance().player.setXRot(Mth.clamp(targetPos.getPitch(), -90.0F, 90.0F) % 360.0F);
            startPos = null;
            targetPos = null;
            Network.sendToServer(new StopCameraPathPacket());
         }
      }
   }

   @SubscribeEvent
   public static void cameraSetup(ComputeCameraAngles event) {
      if (targetPos != null) {
         if (startPos != null) {
            Vec3 pos = Minecraft.getInstance().player.position();
            Vec3 target = targetPos;
            Vec3 diff = target.subtract(pos);
            double remainDistance = Math.abs(diff.length());
            double fullDistance = Math.abs(target.distanceTo(startPos));
            float progress = (float)(1.0 - remainDistance / fullDistance);
            progress = (float)((double)progress0 + (double)(progress - progress0) * event.getPartialTick());
            progress0 = progress;
            float yaw = Mth.lerp(progress, startPos.getYaw(), targetPos.getYaw());
            float pitch = Mth.lerp(progress, startPos.getPitch(), targetPos.getPitch());
            event.setYaw(yaw);
            event.setPitch(pitch);
            if (progress >= 1.0F) {
               Minecraft.getInstance().player.setPosRaw(targetPos.x, targetPos.y, targetPos.z);
               Minecraft.getInstance().player.setPos(targetPos.x, targetPos.y, targetPos.z);
               Minecraft.getInstance().player.setYRot(targetPos.getYaw() % 360.0F);
               Minecraft.getInstance().player.setXRot(Mth.clamp(targetPos.getPitch(), -90.0F, 90.0F) % 360.0F);
               startPos = null;
               targetPos = null;
               Network.sendToServer(new StopCameraPathPacket());
            }
         }
      }
   }
}
