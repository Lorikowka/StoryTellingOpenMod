package com.storyteam.storytelling.script.story.camera;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.action.ScriptPerformerAction;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.system._core.st_cutscene.InterpolationType;
import com.storyteam.storytelling.system._core.st_cutscene.STCutsceneData;
import com.storyteam.storytelling.system._core.st_cutscene.ServersideCutsceneHandler;
import com.storyteam.storytelling.system._core.st_cutscene.SplineType;
import com.storyteam.storytelling.util.ScriptUtils;
import com.storyteam.storytelling.util.world.Location;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

/** Flies to the front of a story NPC, holds the shot, then flies back to the player. */
public final class NpcCameraFlightAction extends ScriptPerformerAction {
   private final Character<?> character;
   private final int flightTicks;
   private final int holdTicks;
   private final double distance;
   private STCutsceneData camera;
   private int phase;
   private int elapsed;
   private int missingTicks;

   public NpcCameraFlightAction(ScriptPerformer performer, Character<?> character,
                                int flightTicks, int holdTicks, double distance) {
      super(performer);
      this.character = character;
      this.flightTicks = flightTicks;
      this.holdTicks = holdTicks;
      this.distance = distance;
   }

   @Override public void tick() {
      if (phase == 4) return;
      ServerPlayer player = ScriptUtils.getPlayer();
      if (player == null) return;
      switch (phase) {
         case 0 -> startFlight(player);
         case 1 -> {
            if (++elapsed >= flightTicks) {
               elapsed = 0;
               if (holdTicks == 0) startReturn(player);
               else phase = 2;
            }
         }
         case 2 -> {
            if (++elapsed >= holdTicks) startReturn(player);
         }
         case 3 -> {
            if (++elapsed >= flightTicks) {
               release();
               phase = 4;
            }
         }
         default -> throw new IllegalStateException("Invalid NPC camera phase " + phase);
      }
   }

   private void startFlight(ServerPlayer player) {
      if (ServersideCutsceneHandler.currentData != null) {
         throw new IllegalStateException("Another camera scene is already active");
      }
      if (!(character.repositoryEntity() instanceof LivingEntity npc) || !npc.isAlive()) {
         if (++missingTicks > 200) throw new IllegalStateException("NPC is unavailable for camera flight: " + character.getReferenceId());
         return;
      }
      if (npc.level().dimension() != player.level().dimension()) {
         throw new IllegalStateException("NPC camera flight cannot cross dimensions: " + character.getReferenceId());
      }
      Location start = playerView(player);
      Vec3 forward = npc.getLookAngle().multiply(1, 0, 1).normalize();
      if (forward.lengthSqr() < 0.0001) forward = new Vec3(0, 0, 1);
      Vec3 side = forward.yRot((float) (Math.PI / 2));
      Vec3 target = npc.position().add(forward.scale(distance)).add(side.scale(0.1))
         .add(0, npc.getEyeHeight() - player.getEyeHeight(), 0);
      List<Location> points = new ArrayList<>();
      points.add(start);
      Vec3 towardNpc = target.subtract(start).multiply(1, 0, 1).normalize();
      if (towardNpc.dot(forward) > 0.75) {
         Vec3 left = npc.position().add(side);
         Vec3 right = npc.position().subtract(side);
         Vec3 waypoint = left.distanceToSqr(start) <= right.distanceToSqr(start) ? left : right;
         points.add(new Location(npc.level().dimension(),
            waypoint.add(0, npc.getEyeHeight() - player.getEyeHeight(), 0),
            npc.getViewYRot(0) + 90, -npc.getViewXRot(0)));
      }
      points.add(new Location(npc.level().dimension(), target,
         npc.getViewYRot(0) + 165, -npc.getViewXRot(0)));
      camera = new STCutsceneData(flightTicks, points, SplineType.CATMULLROM,
         InterpolationType.EASE_IN_OUT_QUAD, InterpolationType.LINEAR);
      camera.cameraOut = false;
      ServersideCutsceneHandler.initOrTpCamera(camera);
      phase = 1;
      elapsed = 0;
   }

   private void startReturn(ServerPlayer player) {
      STCutsceneData back = new STCutsceneData(flightTicks, List.of(playerView(player)),
         SplineType.CATMULLROM, InterpolationType.EASE_IN_OUT_QUAD, InterpolationType.LINEAR);
      back.cameraOut = false;
      ServersideCutsceneHandler.moveCamera(back);
      camera = back;
      phase = 3;
      elapsed = 0;
   }

   private static Location playerView(ServerPlayer player) {
      return new Location(player.level().dimension(),
         player.position().add(player.getLookAngle().scale(0.25)), player.getYRot(), player.getXRot());
   }

   private void release() {
      if (camera != null && ServersideCutsceneHandler.currentData == camera
         && ScriptUtils.getPlayer() != null) ServersideCutsceneHandler.stopCamera();
      camera = null;
   }

   @Override public boolean isDone() { return phase == 4; }
   @Override public void stop() { release(); }
   @Override public void onScriptStop() { release(); }
   @Override public void reset() { release(); phase = 0; elapsed = 0; missingTicks = 0; }
}
