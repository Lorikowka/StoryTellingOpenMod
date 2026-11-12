package com.storyteam.storytelling.util.data;

import com.storyteam.storytelling.script.data.EntityController;
import com.storyteam.storytelling.script.performer.entity.EntityState;
import com.storyteam.storytelling.script.story.characters.Character;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.Entity;

public class EntityContext<T extends Entity> implements IEntityInfoPack<T> {
   private final EntityController<? extends T> ref;
   private final EntityState<T> state;
   private final T entity;
   private final Character<T> character;

   public EntityContext(EntityController<? extends T> ref, EntityState<T> state, T entity, Character<T> character) {
      this.ref = ref;
      this.state = state;
      this.entity = entity;
      this.character = character;
   }

   @Override
   public Character<T> getCharacter() {
      return this.character;
   }

   public EntityController<? extends T> getRef() {
      return this.ref;
   }

   public EntityState<T> getState() {
      return this.state;
   }

   public boolean is(EntityContext<T> context) {
      boolean f1 = context.getEntity() != null && this.getEntity() == context.getEntity();
      boolean f2 = context.getCharacter() != null && this.getCharacter() == context.getCharacter();
      boolean f3 = context.getState() != null && this.getState() == context.getState();
      return f1 || f2 || f3;
   }

   @Override
   public T getEntity() {
      return this.entity;
   }

   public DamageSources damageSources() {
      return this.entity.level().damageSources();
   }
}
