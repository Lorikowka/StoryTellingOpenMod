package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.mixins.minecraft.ItemEntityAccessor;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;
import com.storyteam.storytelling.script.story.characters.Character;
import java.util.Objects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class EntityRandomPickUpAction<T extends Entity> extends EntityPerformerAction<T> {
   public int picked;
   public int amount;

   public EntityRandomPickUpAction(ScriptPerformer performer, @NotNull Character<T> character, int amount) {
      super(performer, character);
      this.amount = amount;
   }

   @Override
   public void tick() {
      this.getEntity().optionalValidOnly().filter(ent -> ent instanceof LivingEntity).ifPresent(entity -> {
         for (ItemEntity item : entity.level().getEntitiesOfClass(ItemEntity.class, entity.getBoundingBox().inflate(2.0))) {
            if (item.isAlive() && !item.hasPickUpDelay() && !Objects.equals(((ItemEntityAccessor)item).getThrower(), entity.getUUID())) {
               ItemStack stack = item.getItem();
               if (!stack.isEmpty()) {
                  int amtPick = Math.min(this.amount - this.picked, stack.getCount());
                  ((LivingEntity)entity).take(item, amtPick);
                  stack.shrink(amtPick);
                  this.picked += amtPick;
                  item.setItem(stack);
                  if (stack.isEmpty()) {
                     item.discard();
                  }

                  item.setDefaultPickUpDelay();
                  if (this.picked >= this.amount) {
                     return;
                  }
               }
            }
         }
      });
   }

   @Override
   public void reset() {
      super.reset();
      this.picked = 0;
   }

   @Override
   public void rewindToStage() {
      this.reset();
   }

   @Override
   public HelpReportBuilder gatherHelpReport(HelpReportBuilder builder) {
      return builder.append("%s ожидает случайные предметы: %d", this.character.getReportName(), this.amount - this.picked);
   }

   @Override
   public boolean isDone() {
      return this.picked >= this.amount;
   }
}
