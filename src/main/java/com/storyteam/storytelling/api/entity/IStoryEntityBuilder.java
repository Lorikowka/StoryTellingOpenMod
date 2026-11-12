package com.storyteam.storytelling.api.entity;

import com.storyteam.storytelling.content.entities.StoryEntityType;
import java.util.function.Consumer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType.Builder;

public interface IStoryEntityBuilder<T extends Entity> {
   <B extends Builder<T> & IStoryEntityBuilder<T>> B applyProps(Consumer<StoryEntityType.Properties.Builder<T>> var1);
}
