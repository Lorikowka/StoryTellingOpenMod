package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.entity.EntityState;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.util.ToStringBuilder;
import com.storyteam.storytelling.util.UtilST;
import com.storyteam.storytelling.util.world.Location;
import java.util.function.Supplier;
import net.minecraft.world.entity.Entity;

public class PositionEntityAction<T extends Entity> extends EntityPerformerAction<T> {
   protected final Supplier<Location> position;
   protected boolean positioned = false;
   protected boolean hasFastForward = true;

   public PositionEntityAction(ScriptPerformer performer, Character<T> character, Supplier<Location> location) {
      super(performer, character);
      this.position = location;
   }

   public PositionEntityAction(ScriptPerformer performer, Character<T> character, Supplier<Location> location, boolean hasFastForward) {
      super(performer, character);
      this.position = location;
      this.hasFastForward = hasFastForward;
   }

   protected void doRePosition(EntityState<T> entity, Location pos) {
      entity.setPosition(pos, true);
      entity.markForReposition();
   }

   @Override
   public void tick() {
      if (!this.positioned) {
         Location pos = this.position.get();
         if (pos != null) {
            this.positioned = true;
            this.doRePosition(this.getOrCreateEntity(true), pos);
         }
      }
   }

   @Override
   public void reset() {
      this.positioned = false;
   }

   @Override
   public void fastForwardToStage() {
      if (this.hasFastForward) {
         this.doRePosition(this.getOrCreateEntity(true), UtilST.supplyNullOnException(this.position));
      }
   }

   @Override
   public void rewindToStage() {
      EntityState<T> entity = this.getEntity();
      this.positioned = false;
      entity.setPosition(null);
      if (this.position.get().hasRotation()) {
         entity.getData().setLookTarget(null);
      }

      entity.markForDelete();
   }

   @Override
   public void onInit() {
      this.positioned = false;
   }

   @Override
   public boolean isDone() {
      return this.positioned;
   }

   @Override
   public HelpReportBuilder gatherHelpReport(HelpReportBuilder builder) {
      Location target = this.position.get();
      builder.append(
         "Спавн персонажа [%s] на координатах X:%d Y:%d Z:%d в мире %s.",
         this.character.getName(),
         (int)target.x,
         (int)target.y,
         (int)target.z,
         target.getDimension().location().toString()
      );
      return builder;
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this).append("character", this.character).append("position", this.position.get()).build();
   }
}
