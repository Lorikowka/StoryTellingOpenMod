package com.llamalad7.mixinextras.utils;

import java.util.ArrayList;
import java.util.Collection;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;

public class MixinInternals_v0_8_6 {
   private static final InternalField<InjectionInfo, Object> TARGETS = InternalField.of(InjectionInfo.class, "targets");

   public static Collection<MethodNode> getTargets(InjectionInfo info) {
      Collection<MethodNode> result = new ArrayList<>();
      try {
         for (Object selected : (Iterable<?>) TARGETS.get(info)) {
            result.add((MethodNode) selected.getClass().getMethod("getMethod").invoke(selected));
         }
         return result;
      } catch (ReflectiveOperationException e) {
         throw new IllegalStateException("Cannot read Mixin target methods", e);
      }
   }
}
