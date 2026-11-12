package com.storyteam.storytelling.content.entities.base;

import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.content.entities.configurations.AnimConfigurationWrapper;
import com.storyteam.storytelling.content.entities.configurations.IDefaultAnimConfiguration;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType.EntityFactory;
import org.jetbrains.annotations.Nullable;

public class AnimatedEntityRegistrar<T extends AnimatedEntity> extends EntityRegistrar<T> {
   public AnimatedEntityRegistrar(
      EntityRegistrar.EntityRegisrarHolder holder, EntityFactory<T> factory, MobCategory category, @Nullable IDefaultAnimConfiguration animConfiguration
   ) {
      super(holder, (type, level) -> {
         T entity = (T)factory.create(type, level);
         entity.setWrapper(new AnimConfigurationWrapper(animConfiguration));
         return entity;
      }, category);
   }
}
