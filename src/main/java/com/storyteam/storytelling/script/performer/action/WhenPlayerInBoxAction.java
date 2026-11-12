package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;
import com.storyteam.storytelling.util.ScriptUtils;
import com.storyteam.storytelling.util.ToStringBuilder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.AABB;

public class WhenPlayerInBoxAction extends ScriptPerformerAction {
   private final AABB box;
   private final boolean touch;
   private final boolean inside;

   public WhenPlayerInBoxAction(ScriptPerformer performer, AABB box, boolean touch, boolean inside) {
      super(performer);
      this.box = box;
      this.touch = touch;
      this.inside = inside;
   }

   @Override
   public boolean isDone() {
      ServerPlayer player = ScriptUtils.getPlayer();
      if (player == null) {
         return false;
      } else if (this.touch) {
         return this.inside ? this.box.intersects(player.getBoundingBox()) : !this.isOneFullyInsideAnother(player.getBoundingBox(), this.box);
      } else {
         return this.box.contains(player.position()) == this.inside;
      }
   }

   private boolean isOneFullyInsideAnother(AABB one, AABB another) {
      return one.minX >= another.minX
         && one.maxX <= another.maxX
         && one.minY >= another.minY
         && one.maxY <= another.maxY
         && one.minZ >= another.minZ
         && one.maxZ <= another.maxZ;
   }

   @Override
   public HelpReportBuilder gatherHelpReport(HelpReportBuilder builder) {
      if (this.touch) {
         if (this.inside) {
            builder.append("Касание к территории.");
         } else {
            builder.append("Касание к границе территории изнутри.");
         }
      } else if (this.inside) {
         builder.append("Вход на территорию.");
      } else {
         builder.append("Выход с территории.");
      }

      return builder;
   }

   @Override
   public HelpReportBuilder gatherInstruction(HelpReportBuilder builder) {
      if (this.touch) {
         if (this.inside) {
            builder.append("Касание к территории.");
         } else {
            builder.append("Касание к границе территории изнутри.");
         }
      } else if (this.inside) {
         builder.append("Вход на территорию.");
      } else {
         builder.append("Выход с территории.");
      }

      return builder;
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this).append("box", this.box).append("touch", this.touch).append("inside", this.inside).build();
   }
}
