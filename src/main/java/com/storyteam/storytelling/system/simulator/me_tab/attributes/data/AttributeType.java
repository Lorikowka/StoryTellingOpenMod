package com.storyteam.storytelling.system.simulator.me_tab.attributes.data;

import com.storyteam.storytelling.system.ico.Ico;
import com.storyteam.storytelling.system.simulator.me_tab.MeTabSystem;
import java.util.EnumSet;
import java.util.Set;
import lombok.Generated;
import net.minecraft.resources.ResourceLocation;

public enum AttributeType {
   BODY("Тело", 1, Ico.BODY),
   MIND("Разум", 2, Ico.MIND),
   WILLPOWER("Воля", 4, Ico.WILLPOWER);

   private final String displayName;
   private final int bit;
   private final Ico ico;
   private final MeTabSystem sys = MeTabSystem.INSTANCE;

   private AttributeType(String displayName, int bit, Ico ico) {
      this.displayName = displayName;
      this.bit = bit;
      this.ico = ico;
   }

   public ResourceLocation getBaseTexture() {
      return MeTabSystem.INSTANCE.guiTex("attribute/" + this.name().toLowerCase());
   }

   public ResourceLocation getFilledTexture() {
      return MeTabSystem.INSTANCE.guiTex("attribute/" + this.name().toLowerCase() + "_filled");
   }

   public Ico ico() {
      return this.ico;
   }

   public static int combine(AttributeType... types) {
      int mask = 0;

      for (AttributeType type : types) {
         mask |= type.bit;
      }

      return mask;
   }

   public static Set<AttributeType> fromMask(int mask) {
      EnumSet<AttributeType> set = EnumSet.noneOf(AttributeType.class);

      for (AttributeType type : values()) {
         if ((mask & type.bit) != 0) {
            set.add(type);
         }
      }

      return set;
   }

   @Generated
   public String getDisplayName() {
      return this.displayName;
   }
}
