package com.storyteam.storytelling.script.story.camera;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.action.ActionCheckpoint;
import com.storyteam.storytelling.script.performer.action.ScriptPerformerAction;
import com.storyteam.storytelling.system._core.st_cutscene.STCutsceneData;
import com.storyteam.storytelling.system._core.st_cutscene.ServersideCutsceneHandler;
import com.storyteam.storytelling.util.ScriptUtils;
import com.storyteam.storytelling.util.world.Location;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

/** Shows a fixed camera shot and skips an interrupted shot after a world reload. */
public final class CameraShotAction extends ScriptPerformerAction implements ActionCheckpoint {
   private final double x, y, z;
   private final float yaw, pitch;
   private final int duration;
   private STCutsceneData camera;
   private int elapsed;
   private boolean done;
   private boolean started;

   public CameraShotAction(ScriptPerformer performer, double x, double y, double z,
                           float yaw, float pitch, int duration) {
      super(performer);
      this.x = x;
      this.y = y;
      this.z = z;
      this.yaw = yaw;
      this.pitch = pitch;
      this.duration = duration;
   }

   @Override public void tick() {
      if (done) return;
      ServerPlayer player = ScriptUtils.getPlayer();
      if (player == null) return;
      if (camera == null) {
         if (ServersideCutsceneHandler.currentData != null)
            throw new IllegalStateException("Another camera scene is already active");
         camera = new STCutsceneData(new Location(player.level().dimension(), x, y, z, yaw, pitch), false);
         ServersideCutsceneHandler.initOrTpCamera(camera);
         started = true;
      }
      if (++elapsed >= duration) {
         release();
         done = true;
      }
   }

   private void release() {
      if (camera != null && ServersideCutsceneHandler.currentData == camera)
         ServersideCutsceneHandler.stopCamera();
      camera = null;
   }

   @Override public boolean isDone() { return done; }
   @Override public void reset() { release(); elapsed = 0; started = false; done = false; }
   @Override public void onScriptStop() { release(); }

   @Override public CompoundTag saveActionState() {
      CompoundTag state = new CompoundTag();
      state.putBoolean("started", started);
      return state;
   }

   @Override public void restoreActionState(CompoundTag state) {
      if (state.getBoolean("started")) { started = true; done = true; }
   }
}
