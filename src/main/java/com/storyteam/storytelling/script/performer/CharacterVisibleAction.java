package com.storyteam.storytelling.script.performer;

import com.storyteam.storytelling.script.performer.action.PerformerAction;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.util.ScriptUtils;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class CharacterVisibleAction<T extends Entity> extends PerformerAction<ScriptPerformer> {
   private boolean isDone;
   Supplier<List<Character<T>>> characters;
   private final boolean lineOfSight;

   public CharacterVisibleAction(ScriptPerformer performer, @NotNull Character<T> character, boolean lineOfSight) {
      super(performer);
      this.characters = () -> List.of(character);
      this.lineOfSight = lineOfSight;
   }

   public CharacterVisibleAction(ScriptPerformer performer, Supplier<List<Character<T>>> characters, boolean lineOfSight) {
      super(performer);
      this.characters = characters;
      this.lineOfSight = lineOfSight;
   }

   @Override
   public void tick() {
      this.isDone = this.complete(this.characters);
   }

   private boolean complete(Supplier<List<Character<T>>> characters) {
      Player player = ScriptUtils.getPlayer();

      for (int i = 0; i < characters.get().size(); i++) {
         T entity = characters.get().get(i).repositoryEntity();
         if (entity != null
            && (
               !entity.level().dimension().location().equals(player.level().dimension().location())
                  || ScriptUtils.getAngle(player, entity) > 90.0 && entity.distanceTo(player) > 5.0F && (!this.lineOfSight || player.hasLineOfSight(entity))
            )) {
            return false;
         }
      }

      return true;
   }

   @Override
   public void fastForwardToStage() {
      this.isDone = true;
   }

   @Override
   public boolean isDone() {
      return this.isDone;
   }

   @Override
   public void reset() {
      this.isDone = false;
   }

   @Override
   public void rewindToStage() {
      this.isDone = false;
   }
}
