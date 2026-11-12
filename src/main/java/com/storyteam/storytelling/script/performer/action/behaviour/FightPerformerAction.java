package com.storyteam.storytelling.script.performer.action.behaviour;

import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.events.listeners.ScriptIO;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.entity.behaviour.fight.EntityFightBehaviour;
import com.storyteam.storytelling.script.performer.entity.behaviour.fight.chain.IAttackChain;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.util.world.Location;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class FightPerformerAction<T extends AnimatedEntity> extends BehaviourAction<T, EntityFightBehaviour<T>> {
   final IAttackChain<T> chain;
   private final boolean saveDeathEntityPosition;

   public FightPerformerAction(ScriptPerformer performer, @NotNull Character<T> character, IAttackChain<T> chain) {
      super(performer, character);
      this.chain = chain;
      this.saveDeathEntityPosition = false;
   }

   public FightPerformerAction(ScriptPerformer performer, @NotNull Character<T> character, boolean deathPosition, IAttackChain<T> chain) {
      super(performer, character);
      this.chain = chain;
      this.saveDeathEntityPosition = false;
   }

   @Override
   public void stop() {
      if (this.saveDeathEntityPosition && this.performer.script != null) {
         Entity e = this.performer.script.getEntity(this.character);
         if (e != null) {
            ScriptIO.getScriptTag(this.script.getId()).putString(this.character.getReferenceId() + "DeathPosition", new Location(e).toCommandString());
         }
      }
   }

   public EntityFightBehaviour<T> createBehaviour() {
      return new EntityFightBehaviour<>(this.chain);
   }

   @Override
   public void fastForwardToStage() {
      super.fastForwardToStage();
      if (this.saveDeathEntityPosition
         && this.performer.script != null
         && ScriptIO.getScriptTag(this.script.getId()).contains(this.character.getReferenceId() + "DeathPosition")) {
         String pos = ScriptIO.getScriptTag(this.script.getId()).getString(this.character.getReferenceId() + "DeathPosition");
         this.getEntity().setPosition(new Location(pos));
      }
   }
}
