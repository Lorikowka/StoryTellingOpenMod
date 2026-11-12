package com.storyteam.storytelling.script.performer.entity.look.targets;

import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.script.performer.entity.look.ILookTarget;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.util.data.EntityContext;
import java.util.Optional;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class CharacterTarget<T extends Entity> implements ILookTarget<T> {
   final Character<?> character;

   public CharacterTarget(Character<?> character) {
      this.character = character;
   }

   public CharacterTarget(String characterId) {
      this.character = Character.getById(characterId);
   }

   @Nullable
   @Override
   public Vec3 getTarget(EntityContext<T> context) {
      if (this.character == null) {
         return null;
      } else {
         Optional<? extends Entity> entity = IStoryServer.get().getEntityRepository().optional((Character<? extends Entity>)this.character);
         return entity.<Vec3>map(Entity::getEyePosition).orElse(null);
      }
   }
}
