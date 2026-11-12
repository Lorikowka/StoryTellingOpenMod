package com.llamalad7.mixinextras.injector.wrapmethod;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.service.MixinExtrasService;
import com.llamalad7.mixinextras.sugar.impl.ShareInfo;
import com.llamalad7.mixinextras.utils.CompatibilityHelper;
import java.util.ArrayList;
import java.util.List;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.spongepowered.asm.mixin.injection.code.Injector;
import org.spongepowered.asm.mixin.injection.code.Injector.InjectorData;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.mixin.injection.struct.Target;
import org.spongepowered.asm.mixin.injection.struct.InjectionNodes.InjectionNode;
import org.spongepowered.asm.util.Annotations;

public class WrapMethodInjector extends Injector {
   private final Type operationType = MixinExtrasService.getInstance()
      .changePackage(Operation.class, Type.getType(CompatibilityHelper.getAnnotation(this.info).desc), WrapMethod.class);
   private final List<ShareInfo> shares = new ArrayList<>();

   public WrapMethodInjector(InjectionInfo info) {
      super(info, "@WrapMethod");
   }

   protected void inject(Target target, InjectionNode node) {
      this.checkTargetModifiers(target, true);
      this.checkSignature(target);
      this.info.addCallbackInvocation(this.methodNode);
      WrapMethodApplicatorExtension.offerWrapper(target, this.methodNode, this.operationType, this.shares);
   }

   private void checkSignature(Target target) {
      InjectorData handler = new InjectorData(target, "method wrapper");
      String description = String.format("%s %s %s from %s", this.annotationType, handler, this, CompatibilityHelper.getMixin(this.info));
      if (!this.returnType.equals(target.returnType)) {
         throw CompatibilityHelper.makeInvalidInjectionException(
            this.info,
            String.format("%s targeting %s has an incorrect return type Expected %s but got %s!", description, target, target.returnType, this.returnType)
         );
      } else {
         int argIndex;
         for (argIndex = 0; argIndex < target.arguments.length; argIndex++) {
            Type theirType = target.arguments[argIndex];
            if (argIndex >= this.methodArgs.length) {
               throw CompatibilityHelper.makeInvalidInjectionException(
                  this.info, String.format("%s targeting %s doesn't have enough parameters!", description, target)
               );
            }

            this.checkCoerce(argIndex, theirType, description, true);
         }

         if (argIndex < this.methodArgs.length && this.methodArgs[argIndex++].equals(this.operationType)) {
            List<AnnotationNode> sugars = (List<AnnotationNode>)Annotations.getValue(CompatibilityHelper.getAnnotation(this.info), "sugars");
            if (sugars != null) {
               for (int i = 0; i < argIndex; i++) {
                  AnnotationNode sugar = sugars.get(i);
                  if (MixinExtrasService.getInstance().isClassOwned(Type.getType(sugar.desc).getClassName())) {
                     throw CompatibilityHelper.makeInvalidInjectionException(
                        this.info, String.format("%s targeting %s has sugar on a non-trailing param which is not allowed!", description, target)
                     );
                  }
               }
            }

            while (argIndex < this.methodArgs.length) {
               ShareInfo share;
               if (sugars == null
                  || (
                        share = ShareInfo.getOrCreate(
                           target, sugars.get(argIndex), this.methodArgs[argIndex], CompatibilityHelper.getMixin(this.info).getMixin(), null
                        )
                     )
                     == null) {
                  throw CompatibilityHelper.makeInvalidInjectionException(
                     this.info, String.format("%s targeting %s has an excess parameter at index %s!", description, target, argIndex)
                  );
               }

               this.shares.add(share);
               argIndex++;
            }
         } else {
            throw CompatibilityHelper.makeInvalidInjectionException(
               this.info, String.format("%s targeting %s is missing Operation parameter!", description, target)
            );
         }
      }
   }
}
