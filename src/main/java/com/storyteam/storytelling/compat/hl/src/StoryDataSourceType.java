package com.storyteam.storytelling.compat.hl.src;

import com.storyteam.storytelling.content.caps.StoryData;
import com.storyteam.storytelling.init.CapsST;
import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.zeith.hammerlib.abstractions.sources.IObjectSource;
import org.zeith.hammerlib.abstractions.sources.IObjectSourceType;
import org.zeith.hammerlib.util.java.Cast;

public class StoryDataSourceType implements IObjectSourceType {
   public IObjectSource<StoryData> readSource(CompoundTag tag) {
      return new StoryDataSourceType.StDataSource(tag);
   }

   public static Supplier<StoryDataSourceType.StDataSource> create(StoryData data) {
      return () -> new StoryDataSourceType.StDataSource(data.getEntity().getId());
   }

   public static class StDataSource implements IObjectSource<StoryData> {
      public final int id;

      public StDataSource(int id) {
         this.id = id;
      }

      public StDataSource(CompoundTag nbt) {
         this.id = nbt.getInt("id");
      }

      public CompoundTag writeSource() {
         CompoundTag tag = new CompoundTag();
         tag.putInt("id", this.id);
         return tag;
      }

      public StoryData get(Level world) {
         return StoryData.of((Entity)Cast.cast(world.getEntity(this.id), Entity.class));
      }

      public IObjectSourceType getType() {
         return CapsST.STORY_DATA_SRC_TYPE;
      }

      public Class<StoryData> getBaseType() {
         return StoryData.class;
      }
   }
}
