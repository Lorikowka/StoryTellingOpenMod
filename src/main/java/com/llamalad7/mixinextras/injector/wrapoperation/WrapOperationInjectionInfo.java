package com.llamalad7.mixinextras.injector.wrapoperation;

import com.llamalad7.mixinextras.injector.MixinExtrasLateInjectionInfo;
import com.llamalad7.mixinextras.utils.ASMUtils;
import com.llamalad7.mixinextras.utils.CompatibilityHelper;
import com.llamalad7.mixinextras.utils.InjectorUtils;
import com.llamalad7.mixinextras.utils.MixinExtrasLogger;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.spongepowered.asm.mixin.injection.code.Injector;
import org.spongepowered.asm.mixin.injection.points.BeforeConstant;
import org.spongepowered.asm.mixin.injection.struct.Target;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo.AnnotationType;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo.HandlerPrefix;
import org.spongepowered.asm.mixin.injection.struct.InjectionNodes.InjectionNode;
import org.spongepowered.asm.mixin.transformer.MixinTargetContext;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.util.Bytecode;

@AnnotationType(WrapOperation.class)
@HandlerPrefix("wrapOperation")
public class WrapOperationInjectionInfo extends MixinExtrasLateInjectionInfo {
   private static final MixinExtrasLogger LOGGER = MixinExtrasLogger.get("WrapOperation");

   public WrapOperationInjectionInfo(MixinTargetContext mixin, MethodNode method, AnnotationNode annotation) {
      super(mixin, method, annotation, determineAtKey(mixin, method, annotation));
   }

   protected Injector parseInjector(AnnotationNode injectAnnotation) {
      return new WrapOperationInjector(this);
   }

   public void prepare() {
      super.prepare();
      InjectorUtils.checkForDupedNews(this.targetNodes);

      for (Entry<Target, List<InjectionNode>> entry : this.targetNodes.entrySet()) {
         Target target = entry.getKey();
         ListIterator<InjectionNode> it = entry.getValue().listIterator();

         while (it.hasNext()) {
            InjectionNode node = it.next();
            AbstractInsnNode currentTarget = node.getCurrentTarget();
            if (currentTarget.getOpcode() == 187) {
               MethodInsnNode initCall = ASMUtils.findInitNodeFor(target, (TypeInsnNode)currentTarget);
               if (initCall == null) {
                  LOGGER.warn("NEW node {} in {} has no init call?", Bytecode.describeNode(currentTarget), target);
                  it.remove();
               } else {
                  node.decorate("mixinextras_newArgTypes", Type.getArgumentTypes(initCall.desc));
               }
            }
         }
      }
   }

   protected void parseInjectionPoints(List<AnnotationNode> ats) {
      if (this.atKey.equals("at")) {
         super.parseInjectionPoints(ats);
      } else {
         Type returnType = Type.getReturnType(this.method.desc);

         for (AnnotationNode at : ats) {
            this.injectionPoints.add(new BeforeConstant(CompatibilityHelper.getMixin(this), at, returnType.getDescriptor()));
         }
      }
   }

   @Override
   public String getLateInjectionType() {
      return "WrapOperation";
   }

   private static String determineAtKey(MixinTargetContext mixin, MethodNode method, AnnotationNode annotation) {
      boolean at = Annotations.getValue(annotation, "at") != null;
      boolean constant = Annotations.getValue(annotation, "constant") != null;
      if (at == constant) {
         throw new IllegalStateException(
            String.format(
               "@WrapOperation injector %s::%s must specify exactly one of `at` and `constant`, got %s.",
               mixin.getMixin().getClassName(),
               method.name,
               at ? "both" : "neither"
            )
         );
      } else {
         return at ? "at" : "constant";
      }
   }
}
