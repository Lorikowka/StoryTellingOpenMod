package com.storyteam.storytelling.mixins._client.iris;

import com.storyteam.storytelling.system.shaders.AddShaderSettingsButton;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import net.irisshaders.iris.gui.element.ShaderPackSelectionList;
import net.minecraft.client.gui.components.AbstractSelectionList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ShaderPackSelectionList.class})
public class ShaderPackSelectionListMixin {
   @Inject(
      method = {"refresh"},
      at = {@At("TAIL")},
      remap = false
   )
   public void refresh(CallbackInfo ci) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
      Class<?> entryClass = Arrays.stream(AbstractSelectionList.class.getDeclaredClasses())
         .filter(c -> Objects.equals(c.getSimpleName(), "Entry"))
         .findAny()
         .get();

      Method addEntryMethod;
      try {
         addEntryMethod = AbstractSelectionList.class.getDeclaredMethod("addEntry", entryClass);
      } catch (Exception var11) {
         try {
            addEntryMethod = AbstractSelectionList.class.getDeclaredMethod("b", entryClass);
         } catch (Exception var10) {
            try {
               addEntryMethod = AbstractSelectionList.class.getDeclaredMethod("method_25321", entryClass);
            } catch (Exception var9) {
               try {
                  addEntryMethod = AbstractSelectionList.class.getDeclaredMethod("m_7085_", entryClass);
               } catch (Exception var8) {
                  throw new RuntimeException(
                     "нет такого метода, иди нахуй\n" + var11.getMessage() + "\n" + var10.getMessage() + "\n" + var9.getMessage() + "\n" + var8.getMessage()
                  );
               }
            }
         }
      }

      addEntryMethod.setAccessible(true);
      ShaderPackSelectionList self = (ShaderPackSelectionList)this;
      addEntryMethod.invoke(this, new AddShaderSettingsButton(self));
      if (AddShaderSettingsButton.newSettings != null) {
         self.select(AddShaderSettingsButton.newSettings);
         AddShaderSettingsButton.newSettings = null;
      }
   }
}
