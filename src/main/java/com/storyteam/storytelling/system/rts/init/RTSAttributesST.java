package com.storyteam.storytelling.system.rts.init;

import com.storyteam.storytelling.St;
import com.storyteam.storytelling.init.AttributesST;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;

@SimplyRegister(
   prefix = "system/rts/attribute/"
)
public interface RTSAttributesST {
   List<Attribute> ATTRIBUTES = new LinkedList<>();
   @RegistryName("attack_range")
   Attribute ATTACK_RANGE = register(
      AttributesST.AttributeBuilder.builder().key(St.id("rts.attack_range").toString()).defaultValue(2.0).minValue(0.0).maxValue(1024.0).build()
   );
   @RegistryName("aggro_range")
   Attribute AGGRO_RANGE = register(
      AttributesST.AttributeBuilder.builder().key(St.id("rts.aggro_range").toString()).defaultValue(0.0).minValue(0.0).maxValue(1024.0).build()
   );
   @RegistryName("attack_per_second")
   Attribute ATTACKS_PER_SECOND = register(
      AttributesST.AttributeBuilder.builder().key(St.id("rts.attack_per_second").toString()).defaultValue(0.5).minValue(0.0).maxValue(1024.0).build()
   );
   @RegistryName("max_resources")
   Attribute MAX_RESOURCES = register(
      AttributesST.AttributeBuilder.builder().key(St.id("rts.max_resources").toString()).defaultValue(100.0).minValue(0.0).maxValue(1024.0).build()
   );
   @RegistryName("food_consumption")
   Attribute FOOD_CONSUMPTION = register(
      AttributesST.AttributeBuilder.builder().key(St.id("rts.food_consumption").toString()).defaultValue(4.0).minValue(0.0).maxValue(1024.0).build()
   );

   private static Attribute register(AttributesST.AttributeBuilder builder) {
      RangedAttribute rangedAttribute = new RangedAttribute(builder.getKey(), builder.getDefaultValue(), builder.getMinValue(), builder.getMaxValue());
      ATTRIBUTES.add(rangedAttribute);
      return rangedAttribute;
   }
}
