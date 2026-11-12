package com.storyteam.storytelling.system.regions;

import com.storyteam.storytelling.init.ScriptCategory;
import com.storyteam.storytelling.script.instances.backs.mechanics.S1301BackController;
import com.storyteam.storytelling.util.LevelAABB;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import lombok.Generated;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegisterEvent;
import org.zeith.hammerlib.api.fml.ICustomRegistrar;

public final class ScriptRegion implements ICustomRegistrar {
   private final List<LevelAABB> areas;
   private final IntArrayList scripts;

   public void performRegister(RegisterEvent event, ResourceLocation id) {
      event.register(Registries.ITEM, h -> S1301BackController.register(this));
   }

   @Generated
   ScriptRegion(List<LevelAABB> areas, IntArrayList scripts) {
      this.areas = areas;
      this.scripts = scripts;
   }

   @Generated
   public static ScriptRegion.ScriptRegionBuilder builder() {
      return new ScriptRegion.ScriptRegionBuilder();
   }

   @Generated
   public List<LevelAABB> getAreas() {
      return this.areas;
   }

   @Generated
   public IntArrayList getScripts() {
      return this.scripts;
   }

   @Generated
   @Override
   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof ScriptRegion other)) {
         return false;
      } else {
         Object this$areas = this.getAreas();
         Object other$areas = other.getAreas();
         if (this$areas == null ? other$areas == null : this$areas.equals(other$areas)) {
            Object this$scripts = this.getScripts();
            Object other$scripts = other.getScripts();
            return this$scripts == null ? other$scripts == null : this$scripts.equals(other$scripts);
         } else {
            return false;
         }
      }
   }

   @Generated
   @Override
   public int hashCode() {
      int PRIME = 59;
      int result = 1;
      Object $areas = this.getAreas();
      result = result * 59 + ($areas == null ? 43 : $areas.hashCode());
      Object $scripts = this.getScripts();
      return result * 59 + ($scripts == null ? 43 : $scripts.hashCode());
   }

   @Generated
   @Override
   public String toString() {
      return "ScriptRegion(areas=" + this.getAreas() + ", scripts=" + this.getScripts() + ")";
   }

   public static class ScriptRegionBuilder {
      @Generated
      private ArrayList<LevelAABB> areas;
      @Generated
      private IntArrayList scripts;

      public ScriptRegion.ScriptRegionBuilder category(ScriptCategory cat) {
         return this.script(cat.getScripts());
      }

      public ScriptRegion.ScriptRegionBuilder script(int... scripts) {
         if (this.scripts == null) {
            this.scripts(new IntArrayList());
         }

         this.scripts.addAll(IntList.of(scripts));
         return this;
      }

      @Generated
      ScriptRegionBuilder() {
      }

      @Generated
      public ScriptRegion.ScriptRegionBuilder area(LevelAABB area) {
         if (this.areas == null) {
            this.areas = new ArrayList<>();
         }

         this.areas.add(area);
         return this;
      }

      @Generated
      public ScriptRegion.ScriptRegionBuilder areas(Collection<? extends LevelAABB> areas) {
         if (areas == null) {
            throw new NullPointerException("areas cannot be null");
         } else {
            if (this.areas == null) {
               this.areas = new ArrayList<>();
            }

            this.areas.addAll(areas);
            return this;
         }
      }

      @Generated
      public ScriptRegion.ScriptRegionBuilder clearAreas() {
         if (this.areas != null) {
            this.areas.clear();
         }

         return this;
      }

      @Generated
      public ScriptRegion.ScriptRegionBuilder scripts(IntArrayList scripts) {
         this.scripts = scripts;
         return this;
      }

      @Generated
      public ScriptRegion build() {
         return new ScriptRegion(switch (this.areas == null ? 0 : this.areas.size()) {
            case 0 -> Collections.emptyList();
            case 1 -> Collections.singletonList(this.areas.get(0));
            default -> Collections.unmodifiableList(new ArrayList<>(this.areas));
         }, this.scripts);
      }

      @Generated
      @Override
      public String toString() {
         return "ScriptRegion.ScriptRegionBuilder(areas=" + this.areas + ", scripts=" + this.scripts + ")";
      }
   }
}
