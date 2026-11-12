package com.storyteam.storytelling.api.data;

import com.storyteam.storytelling.api.itf.IStoryEntity;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.script.story.characters.CharacterList;
import java.util.Arrays;
import java.util.Optional;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class EntityInteractionData {
   public final Player player;
   public final Entity target;
   public final IStoryEntity storyTarget;
   public final Runnable preventDefault;

   public EntityInteractionData(Player player) {
      this.player = player;
      this.target = player;
      this.storyTarget = IStoryEntity.of(this.target);
      this.preventDefault = () -> {
      };
   }

   public EntityInteractionData(Player player, Entity target, Runnable preventDefault) {
      this.player = player;
      this.target = target;
      this.storyTarget = IStoryEntity.of(target);
      this.preventDefault = preventDefault;
   }

   public boolean isOf(Character<?> character) {
      return this.storyTarget != null && character.getReferenceId().equals(this.storyTarget.getStoryName());
   }

   public boolean isOf(Character<?>... character) {
      return Arrays.stream(character).anyMatch(e -> e.getReferenceId().equals(this.storyTarget.getStoryName()));
   }

   public <E extends Entity> boolean isOf(CharacterList<?> character) {
      return character.stream().anyMatch(e -> e.getReferenceId().equals(this.storyTarget.getStoryName()));
   }

   public <T extends Entity> Optional<Character<T>> getOf(Iterable<Character<T>> characters) {
      if (this.storyTarget == null) {
         return Optional.empty();
      } else {
         String storyName = this.storyTarget.getStoryName();

         for (Character<T> character : characters) {
            if (character.getReferenceId().equals(storyName)) {
               return Optional.of(character);
            }
         }

         return Optional.empty();
      }
   }

   public void preventDefault() {
      if (this.preventDefault != null) {
         this.preventDefault.run();
      }
   }
}
