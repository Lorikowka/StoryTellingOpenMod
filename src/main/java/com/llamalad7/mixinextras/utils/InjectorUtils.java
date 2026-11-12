package com.llamalad7.mixinextras.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.spongepowered.asm.mixin.injection.modify.LocalVariableDiscriminator;
import org.spongepowered.asm.mixin.injection.modify.LocalVariableDiscriminator.Context;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.mixin.injection.struct.Target;
import org.spongepowered.asm.mixin.injection.struct.InjectionNodes.InjectionNode;
import org.spongepowered.asm.util.Bytecode;
import org.spongepowered.asm.util.PrettyPrinter;
import org.spongepowered.asm.util.SignaturePrinter;

public class InjectorUtils {
   public static boolean isVirtualRedirect(InjectionNode node) {
      return node.isReplaced() && node.hasDecoration("redirector") && node.getCurrentTarget().getOpcode() != 184;
   }

   public static boolean isDynamicInstanceofRedirect(InjectionNode node) {
      AbstractInsnNode originalTarget = node.getOriginalTarget();
      AbstractInsnNode currentTarget = node.getCurrentTarget();
      return originalTarget.getOpcode() == 193
         && currentTarget instanceof MethodInsnNode
         && Type.getReturnType(((MethodInsnNode)currentTarget).desc).equals(Type.getType(Class.class));
   }

   public static void checkForDupedNews(Map<Target, List<InjectionNode>> targets) {
      for (Entry<Target, List<InjectionNode>> entry : targets.entrySet()) {
         for (InjectionNode node : entry.getValue()) {
            AbstractInsnNode currentTarget = node.getCurrentTarget();
            if (currentTarget.getOpcode() == 187 && currentTarget.getNext().getOpcode() == 89) {
               node.decorate("mixinextras_newIsDuped", true);
            }
         }
      }
   }

   public static boolean isDupedNew(InjectionNode node) {
      AbstractInsnNode currentTarget = node.getCurrentTarget();
      return currentTarget != null && currentTarget.getOpcode() == 187 && node.hasDecoration("mixinextras_newIsDuped");
   }

   public static boolean isDupedFactoryRedirect(InjectionNode node) {
      AbstractInsnNode originalTarget = node.getOriginalTarget();
      return node.isReplaced()
         && originalTarget.getOpcode() == 187
         && !node.hasDecoration("mixinextras_wrappedOperation")
         && node.hasDecoration("mixinextras_newIsDuped");
   }

   public static void checkForImmediatePops(Map<Target, List<InjectionNode>> targets) {
      for (List<InjectionNode> nodeList : targets.values()) {
         for (InjectionNode node : nodeList) {
            AbstractInsnNode currentTarget = node.getCurrentTarget();
            if (currentTarget instanceof MethodInsnNode) {
               Type returnType = Type.getReturnType(((MethodInsnNode)currentTarget).desc);
               if (isTypePoppedByInstruction(returnType, currentTarget.getNext())) {
                  node.decorate("mixinextras_operationIsImmediatelyPopped", true);
               }
            }
         }
      }
   }

   private static boolean isTypePoppedByInstruction(Type type, AbstractInsnNode insn) {
      switch (type.getSize()) {
         case 1:
            return insn.getOpcode() == 87;
         case 2:
            return insn.getOpcode() == 88;
         default:
            return false;
      }
   }

   public static Context getOrCreateLocalContext(Target target, InjectionNode node, InjectionInfo info, Type targetType, boolean isArgsOnly) {
      String decorationKey = getLocalContextKey(targetType, isArgsOnly);
      if (node.hasDecoration(decorationKey)) {
         return (Context)node.getDecoration(decorationKey);
      } else {
         Context context = CompatibilityHelper.makeLvtContext(info, targetType, isArgsOnly, target, node.getCurrentTarget());
         node.decorate(decorationKey, context);
         return context;
      }
   }

   private static String getLocalContextKey(Type targetType, boolean isArgsOnly) {
      return String.format("mixinextras_persistent_localContext(%s,%s)", targetType, isArgsOnly ? "argsOnly" : "fullFrame");
   }

   public static void printLocals(
      Target target, AbstractInsnNode node, Context context, LocalVariableDiscriminator discriminator, Type targetType, boolean isArgsOnly
   ) {
      int baseArgIndex = target.isStatic ? 0 : 1;
      new PrettyPrinter()
         .kvWidth(20)
         .kv("Target Class", target.classNode.name.replace('/', '.'))
         .kv("Target Method", target.method.name)
         .kv("Capture Type", SignaturePrinter.getTypeName(targetType, false))
         .kv("Instruction", "[%d] %s %s", new Object[]{target.insns.indexOf(node), node.getClass().getSimpleName(), Bytecode.getOpcodeName(node.getOpcode())})
         .hr()
         .kv("Match mode", isImplicit(discriminator, baseArgIndex) ? "IMPLICIT (match single)" : "EXPLICIT (match by criteria)")
         .kv("Match ordinal", discriminator.getOrdinal() < 0 ? "any" : discriminator.getOrdinal())
         .kv("Match index", discriminator.getIndex() < baseArgIndex ? "any" : discriminator.getIndex())
         .kv("Match name(s)", discriminator.hasNames() ? discriminator.getNames() : "any")
         .kv("Args only", isArgsOnly)
         .hr()
         .add(context)
         .print(System.err);
   }

   private static boolean isImplicit(LocalVariableDiscriminator discriminator, int baseArgIndex) {
      return discriminator.getOrdinal() < 0 && discriminator.getIndex() < baseArgIndex && discriminator.getNames().isEmpty();
   }

   public static void decorateInjectorSpecific(InjectionNode node, InjectionInfo info, String key, Object value) {
      if (!node.hasDecoration(key)) {
         node.decorate(key, new HashMap());
      }

      Map<InjectionInfo, Object> inner = (Map<InjectionInfo, Object>)node.getDecoration(key);
      inner.put(info, value);
   }

   public static <T> T getInjectorSpecificDecoration(InjectionNode node, InjectionInfo info, String key) {
      Map<InjectionInfo, T> map = (Map<InjectionInfo, T>)node.getDecoration(key);
      return map == null ? null : map.get(info);
   }

   public static boolean hasInjectorSpecificDecoration(InjectionNode node, InjectionInfo info, String key) {
      Map<InjectionInfo, ?> map = (Map<InjectionInfo, ?>)node.getDecoration(key);
      return map == null ? false : map.containsKey(info);
   }
}
