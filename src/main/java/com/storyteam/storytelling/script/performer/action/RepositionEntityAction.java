package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.entity.EntityState;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.system.clothes.Outfit;
import com.storyteam.storytelling.system.clothes.entity.VanityInventory;
import com.storyteam.storytelling.util.ToStringBuilder;
import com.storyteam.storytelling.util.UtilST;
import com.storyteam.storytelling.util.world.Location;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.world.entity.Entity;

public class RepositionEntityAction<T extends Entity> extends EntityPerformerAction<T> {
   protected final Supplier<Location> position;
   protected boolean positioned = false;

   public RepositionEntityAction(ScriptPerformer performer, Character<T> character, Supplier<Location> location) {
      super(performer, character);
      this.position = location;
   }

   protected void doRePosition(EntityState<T> entity, Location pos) {
      if (this.character.getOutfit() != null) {
         Outfit v = entity.getData().getVanity().asOutfit();
         VanityInventory v2 = new VanityInventory().apply(this.character.getOutfit()).apply(v);
         entity.getData().setVanity(v2);
      }

      Optional<T> e = entity.optionalValidOnly();
      e.ifPresentOrElse(t -> entity.setPosition(new Location(t)), () -> entity.setPosition(pos, true));
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
      this.doRePosition(this.getOrCreateEntity(true), UtilST.supplyNullOnException(this.position));
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
