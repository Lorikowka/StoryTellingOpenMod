package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.util.ScriptUtils;
import com.storyteam.storytelling.util.world.Location;
import java.util.function.Supplier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;

public class PositionOnceBehindPlayerAction<T extends Entity> extends PositionEntityAction<T> {
   public PositionOnceBehindPlayerAction(ScriptPerformer performer, Character<T> character, Supplier<Location> location) {
      super(performer, character, location);
   }

   @Override
   public void tick() {
      if (ScriptUtils.getPlayer() != null) {
         Player p = ScriptUtils.getPlayer();
         if (!this.positioned
            && (!this.canSee(this.position.get()) || !(p.distanceToSqr(this.position.get()) < 2500.0) || !(ScriptUtils.getAngle(p, this.position.get()) < 90.0))
            )
          {
            Location pos = this.position.get();
            if (pos != null) {
               this.positioned = true;
               this.doRePosition(this.getOrCreateEntity(true), pos);
            }
         }
      }
   }

   public boolean canSee(Vec3 Vec31) {
      if (ScriptUtils.getPlayer() == null) {
         return false;
      } else {
         Vec3 Vec3 = new Vec3(ScriptUtils.getPlayer().getX(), ScriptUtils.getPlayer().getEyeY(), ScriptUtils.getPlayer().getZ());
         return ScriptUtils.getPlayer().level().dimension() == this.position.get().getDimension() && !(Vec31.distanceToSqr(Vec3) > 16384.0)
            ? ScriptUtils.getLevel().clip(new ClipContext(Vec3, Vec31, Block.COLLIDER, Fluid.NONE, ScriptUtils.getPlayer())).getType() == Type.MISS
            : false;
      }
   }

   @Override
   public HelpReportBuilder gatherHelpReport(HelpReportBuilder builder) {
      return builder.append("Ожидание отворота для появления %s", this.character.getReportName());
   }
}
