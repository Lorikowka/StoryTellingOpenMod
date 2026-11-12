package com.llamalad7.mixinextras.wrapper.factory;

import com.llamalad7.mixinextras.wrapper.WrapperInjectionInfo;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo.AnnotationType;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo.HandlerPrefix;
import org.spongepowered.asm.mixin.transformer.MixinTargetContext;

@AnnotationType(FactoryRedirectWrapper.class)
@HandlerPrefix("factoryWrapper")
public class FactoryRedirectWrapperInjectionInfo extends WrapperInjectionInfo {
   public FactoryRedirectWrapperInjectionInfo(MixinTargetContext mixin, MethodNode method, AnnotationNode annotation) {
      super(FactoryRedirectWrapperImpl::new, mixin, method, annotation);
   }
}
