package com.storyteam.storytelling.system.clothes;

import com.storyteam.storytelling.system._sysofsys.ISystem;
import com.storyteam.storytelling.system._sysofsys.SystemRoot;
import com.storyteam.storytelling.system.clothes.entity.VanityInvDataSerializer;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@SystemRoot("clothes")
public class ClothesSystem implements ISystem {
   @Override
   public void gatherSimplyRegisters(Consumer<Class<?>> registrar, BiConsumer<String, Class<?>> registrarWithPrefix) {
      registrar.accept(VanityInvDataSerializer.class);
   }
}
