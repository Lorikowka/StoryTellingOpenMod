package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.util.PickupList;
import com.storyteam.storytelling.util.ToStringBuilder;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.zeith.hammerlib.util.java.Cast;

public class EntityPickUpItemsAction<T extends Entity> extends EntityPerformerAction<T> {
   private final Supplier<PickupList> backupPickupList;
   private PickupList pickupList;
   private final float expand;

   public EntityPickUpItemsAction(ScriptPerformer performer, Character<T> character, Supplier<PickupList> pickupList, float expand) {
      super(performer, character);
      this.backupPickupList = pickupList;
      this.pickupList = pickupList.get();
      this.expand = expand;
   }

   @Override
   public void tick() {
      this.getEntity()
         .optionalValidOnly()
         .filter(ent -> ent instanceof LivingEntity)
         .ifPresent(ent -> this.pickupList.pickupAs((LivingEntity)Cast.cast(ent), this.expand));
   }

   @Override
   public void reset() {
      super.reset();
      this.pickupList = this.backupPickupList.get();
   }

   @Override
   public void rewindToStage() {
      this.reset();
   }

   @Override
   public boolean isDone() {
      return !this.pickupList.hasAnyMoreItems();
   }

   @Override
   public HelpReportBuilder gatherHelpReport(HelpReportBuilder builder) {
      return builder.append(
         "%s ожидает предметы:%s",
         this.character.getReportName(),
         this.pickupList.getLeftoverItems().stream().<CharSequence>map(Component::getString).collect(Collectors.joining("\n", "\n", ""))
      );
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this)
         .append("character", this.character)
         .append("expand", this.expand)
         .append("pickupList", this.backupPickupList)
         .build();
   }
}
