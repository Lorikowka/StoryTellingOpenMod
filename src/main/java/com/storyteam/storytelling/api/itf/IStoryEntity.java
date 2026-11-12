package com.storyteam.storytelling.api.itf;

import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.util.ServerScheduler;
import com.storyteam.storytelling.util.data.EntityContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.zeith.hammerlib.util.mcf.Resources;

public interface IStoryEntity {
   static ResourceLocation transformTexture(ResourceLocation origin, Entity entity) {
      if (entity == null) {
         return origin;
      } else {
         String tex = of(entity).getCustomTexture();
         return tex != null ? Resources.location(tex) : origin;
      }
   }

   static IStoryEntity of(Entity e) {
      return (IStoryEntity)e;
   }

   boolean isStorytellingEntity();

   boolean isUndespawnable();

   void storytellingTick();

   default void setUndespawnable(boolean undespawnable) {
      ((Entity)this).getPersistentData().putBoolean("STT_Undespawnable", undespawnable);
   }

   String getStoryName();

   default boolean isOf(Character<?> character) {
      return character != null && character.getReferenceId().equals(this.getStoryName());
   }

   default boolean isOf(Character<?>[] characters) {
      for (Character<?> character : characters) {
         if (character != null && character.getReferenceId().equals(this.getStoryName())) {
            return true;
         }
      }

      return false;
   }

   String getCustomTexture();

   void setCustomTexture(String var1);

   boolean isSEVulnerable();

   CompoundTag gatherAdditionalNbtData_ST();

   void applyAdditionalNbtData_ST(CompoundTag var1);

   default void onEntityLoad(Entity e) {
      ServerScheduler.executeWhen(() -> of(e).isStorytellingEntity() && e.isAlive(), () -> {
         IStoryServer story = IStoryServer.get(e.level().getServer());
         if (story != null) {
            story.postStoryEvent(EventType.ENTITY_REPORTED, new EntityContext(null, null, (Entity)this, Character.getById(this.getStoryName())));
         }
      }, 20);
   }
}
