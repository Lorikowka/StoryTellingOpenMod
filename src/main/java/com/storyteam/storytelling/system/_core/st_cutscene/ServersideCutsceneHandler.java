package com.storyteam.storytelling.system._core.st_cutscene;

import com.storyteam.storytelling.util.ScriptUtils;
import com.storyteam.storytelling.util.interpolation.Interpolation;
import com.storyteam.storytelling.util.interpolation.LinearInterpolation;
import com.storyteam.storytelling.util.splines.Spline;
import com.storyteam.storytelling.util.world.Location;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.level.GameType;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.zeith.hammerlib.net.Network;

@EventBusSubscriber(
   modid = "storytelling",
   bus = Bus.FORGE
)
public class ServersideCutsceneHandler {
   public static STCutsceneData currentData;
   public static Spline spline;
   public static Interpolation timeInterpolation;
   public static Interpolation lookInterpolation = new LinearInterpolation();
   public static int cutsceneTime;
   public static int fullCutsceneTime;
   public static GameType cachedGameType;

   public static void initOrTpCamera(STCutsceneData data) {
      ServerPlayer player = ScriptUtils.getPlayer();
      if (cachedGameType == null) {
         cachedGameType = player.gameMode.getGameModeForPlayer();
      }

      if (data.cameraOut) {
         player.setGameMode(GameType.SPECTATOR);
      }

      currentData = data;
      cutsceneTime = 0;
      fullCutsceneTime = data.time;
      if (data.locations.size() > 1) {
         spline = data.splineType.splineFactory.apply(data.locations.toArray(new Location[0]));
         timeInterpolation = data.interpolationType.interpolationFactory.get();
         lookInterpolation = new LinearInterpolation();
      } else {
         spline = null;
         timeInterpolation = null;
         lookInterpolation = null;
      }

      Location initLocation = data.locations.get(0);
      if (data.cameraOut) {
         initLocation.teleport(player);
      }

      STCutscenePacket packet = new STCutscenePacket(STCutscenePacket.Action.INIT_OR_TP, data);
      Network.sendTo(player, packet);
   }

   public static void moveCamera(STCutsceneData data) {
      if (currentData == null) {
         throw new RuntimeException("Camera was not initialized");
      } else {
         STCutsceneData copy = new STCutsceneData(data);
         Location location = ClientsideCutsceneHandler.getCurrentCameraLocation(
            currentData.locations.get(0), spline, timeInterpolation, lookInterpolation, cutsceneTime, fullCutsceneTime, 0.0F
         );
         data.locations.add(0, location);
         currentData = data;
         cutsceneTime = 0;
         fullCutsceneTime = data.time;
         spline = data.splineType.splineFactory.apply(data.locations.toArray(new Location[0]));
         timeInterpolation = data.interpolationType.interpolationFactory.get();
         lookInterpolation = new LinearInterpolation();
         STCutscenePacket packet = new STCutscenePacket(STCutscenePacket.Action.MOVE, copy);
         Network.sendTo(ScriptUtils.getPlayer(), packet);
      }
   }

   public static void stopCamera() {
      cutsceneTime = 0;
      fullCutsceneTime = 0;
      timeInterpolation = null;
      spline = null;
      ServerPlayer player = ScriptUtils.getPlayer();
      STCutscenePacket packet = new STCutscenePacket(STCutscenePacket.Action.STOP_CAMERA, new STCutsceneData());
      Network.sendTo(player, packet);
      if (cachedGameType != null && currentData.cameraOut) {
         player.setGameMode(cachedGameType);
      }

      cachedGameType = null;
      currentData = null;
   }

   public static void tickPlayer(ServerPlayer player) {
      if (currentData.cameraOut) {
         Location location = ClientsideCutsceneHandler.getCurrentCameraLocation(
            currentData.locations.get(0), spline, timeInterpolation, lookInterpolation, cutsceneTime, fullCutsceneTime, 0.0F
         );
         location.teleport(player);
      }
   }

   @SubscribeEvent
   public static void tickPlayer(PlayerTickEvent event) {
      if (event.phase == Phase.START && event.player instanceof ServerPlayer player && currentData != null) {
         cutsceneTime = Mth.clamp(cutsceneTime + 1, 0, fullCutsceneTime);
         if (cutsceneTime % 5 == 0 || cutsceneTime == fullCutsceneTime) {
            tickPlayer(player);
         }
      }
   }
}
