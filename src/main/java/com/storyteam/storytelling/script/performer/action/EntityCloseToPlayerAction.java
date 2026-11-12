package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.entity.EntityState;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.util.ScriptUtils;
import com.storyteam.storytelling.util.ToStringBuilder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class EntityCloseToPlayerAction<T extends Entity> extends EntityPerformerAction<T> {
   private final double radius;

   public EntityCloseToPlayerAction(ScriptPerformer performer, Character<T> character, double radius) {
      super(performer, character);
      this.radius = radius;
   }

   @Override
   public void tick() {
      HelpReportBuilder builder = HelpReportBuilder.builder();
      this.gatherHelpReport(builder);
      this.sendInstructionLine(builder.build());
   }

   @Override
   public boolean isDone() {
      return this.getEntity()
         .optionalValidOnly()
         .map(
            ent -> {
               Player nearestPlayer = ent.level().getNearestPlayer(ent, Math.abs(this.radius));
               return this.radius < 0.0
                  ? nearestPlayer == null || ent.distanceToSqr(nearestPlayer) > this.radius * this.radius
                  : nearestPlayer != null && ent.distanceToSqr(nearestPlayer) < this.radius * this.radius;
            }
         )
         .orElse(this.radius < 0.0);
   }

   @Override
   public HelpReportBuilder gatherHelpReport(HelpReportBuilder builder) {
      ServerPlayer player = ScriptUtils.getPlayer();
      EntityState<?> state = this.getEntity();
      String st = "Подход к ";
      if (this.radius < 0.0) {
         st = "Отход от ";
      }

      if (player != null && state != null && state.getPos() != null) {
         builder.append(
            st + "[%s] на %.0f блоков. Текущее расстояние: %d блоков.",
            this.character.getName(),
            Math.abs(this.radius),
            (int)Math.round(player.position().distanceTo(state.getPos()))
         );
      } else {
         builder.append(st + "[%s] на %.0f блоков.", this.character.getName(), Math.abs(this.radius));
      }

      return builder;
   }

   @Override
   public HelpReportBuilder gatherInstruction(HelpReportBuilder builder) {
      ServerPlayer player = ScriptUtils.getPlayer();
      EntityState<?> state = this.getEntity();
      String st = "Подход к ";
      if (this.radius < 0.0) {
         st = "Отход от ";
      }

      if (player != null && state != null && state.getPos() != null) {
         builder.append(
            st + "[%s] на %.0f блоков. Текущее расстояние: %d блоков.",
            this.character.getName(),
            Math.abs(this.radius),
            (int)Math.round(player.position().distanceTo(state.getPos()))
         );
      } else {
         builder.append(st + "[%s] на %.0f блоков.", this.character.getName(), Math.abs(this.radius));
      }

      return builder;
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this).append("character", this.character).append("radius", this.radius).build();
   }

   @Override
   public void fastForwardToStage() {
      HelpReportBuilder builder = HelpReportBuilder.builder();
      this.gatherHelpReport(builder);
      this.sendInstructionLine(builder.build());
   }
}
