package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;
import com.storyteam.storytelling.util.ScriptUtils;
import com.storyteam.storytelling.util.ToStringBuilder;
import com.storyteam.storytelling.util.world.Location;
import java.util.function.Supplier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class PlayerNearPosAction extends ScriptPerformerAction {
   private final Supplier<Location> pos;
   private final double distance;

   public PlayerNearPosAction(ScriptPerformer performer, Supplier<Location> pos, double distance) {
      super(performer);
      this.pos = pos;
      this.distance = distance;
   }

   @Override
   public boolean isDone() {
      Location pos = this.pos.get();
      if (pos == null) {
         return false;
      } else {
         ServerLevel world = ((MinecraftServer)this.script.env.server).getLevel(pos.getDimension());
         boolean nearby = world.hasNearbyAlivePlayer(pos.x, pos.y, pos.z, Math.abs(this.distance));
         return this.distance < 0.0 ? !nearby : nearby;
      }
   }

   @Override
   public HelpReportBuilder gatherHelpReport(HelpReportBuilder builder) {
      Location target = this.pos.get();
      String st = "Приближение к ";
      if (this.distance < 0.0) {
         st = "Отдаление от ";
      }

      double distance = Math.abs(this.distance);
      ServerPlayer player = ScriptUtils.getPlayer();
      if (player != null) {
         builder.append(
            st + "%s, %s, %s, на %.0f блоков. Текущее расстояние: %d блоков.",
            target.x,
            target.y,
            target.z,
            distance,
            (int)Math.round(player.position().distanceTo(target))
         );
      } else {
         builder.append(st + "%s, %s, %s, на %.0f блоков.", target.x, target.y, target.z, distance);
      }

      return builder;
   }

   @Override
   public HelpReportBuilder gatherInstruction(HelpReportBuilder builder) {
      Location target = this.pos.get();
      ServerPlayer player = ScriptUtils.getPlayer();
      if (player != null) {
         builder.append(
            "Приближение к %s, %s, %s, на %.0f блоков. Текущее расстояние: %d блоков.",
            target.x,
            target.y,
            target.z,
            this.distance,
            (int)Math.round(player.position().distanceTo(target))
         );
      } else {
         builder.append("Приближение к %s, %s, %s, на %.0f блоков.", target.x, target.y, target.z, this.distance);
      }

      return builder;
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this).append("pos", this.pos == null ? null : this.pos.get()).append("distance", this.distance).build();
   }
}
