package com.llamalad7.mixinextras.expression.impl.flow.expansion;

import com.llamalad7.mixinextras.expression.impl.flow.DummyFlowValue;
import com.llamalad7.mixinextras.expression.impl.flow.FlowValue;
import com.llamalad7.mixinextras.expression.impl.flow.postprocessing.FlowPostProcessor;
import com.llamalad7.mixinextras.expression.impl.flow.postprocessing.StringConcatPostProcessor;
import com.llamalad7.mixinextras.expression.impl.utils.ExpressionASMUtils;
import com.llamalad7.mixinextras.lib.apache.commons.StringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.spongepowered.asm.mixin.injection.struct.Target;
import org.spongepowered.asm.mixin.injection.struct.InjectionNodes.InjectionNode;

public class StringConcatFactoryExpander extends InsnExpander {
   private static final String STRING_CONCAT_FACTORY = "java/lang/invoke/StringConcatFactory";
   private static final Type STRING_BUILDER = Type.getType(StringBuilder.class);
   private static final Type STRING = Type.getType(String.class);

   @Override
   public void process(FlowValue node, FlowPostProcessor.OutputSink sink) {
      AbstractInsnNode indy = node.getInsn();
      List<StringConcatFactoryExpander.ConcatPart> parts = this.parseConcat(indy);
      if (parts != null) {
         FlowValue current = new DummyFlowValue(STRING_BUILDER);
         List<FlowValue> appendCalls = new ArrayList<>();
         int nextArgument = 0;
         int finishedParts = 0;

         for (StringConcatFactoryExpander.ConcatPart part : parts) {
            FlowValue component;
            if (part instanceof StringConcatFactoryExpander.ConcatPart.Argument) {
               component = this.unwrapConcatArgument(node.getInput(nextArgument++), sink);
            } else {
               Object cst;
               if (part instanceof StringConcatFactoryExpander.ConcatPart.PooledConstant) {
                  cst = ((StringConcatFactoryExpander.ConcatPart.PooledConstant)part).value;
               } else {
                  cst = ((StringConcatFactoryExpander.ConcatPart.TemplateString)part).value;
               }

               AbstractInsnNode componentInsn = new LdcInsnNode(cst);
               component = new FlowValue(ExpressionASMUtils.getNewType(componentInsn), componentInsn);
               this.registerComponent(component, part, indy);
               sink.registerFlow(component);
            }

            current = new FlowValue(STRING_BUILDER, dummyInsn(), current, component);
            this.registerComponent(current, new StringConcatFactoryExpander.PartialResult(finishedParts++), indy);
            sink.registerFlow(current);
            appendCalls.add(current);
         }

         node.setInsn(dummyInsn());
         node.setParents(current);
         this.registerComponent(node, StringConcatFactoryExpander.Component.TO_STRING, indy);
         StringConcatPostProcessor.decorateConcat(appendCalls, node);
      }
   }

   private FlowValue unwrapConcatArgument(FlowValue argument, FlowPostProcessor.OutputSink sink) {
      if (!argument.isComplex() && this.isStringValueOf(argument.getInsn())) {
         sink.markAsSynthetic(argument);
         return argument.getInput(0);
      } else {
         return argument;
      }
   }

   private boolean isStringValueOf(AbstractInsnNode insn) {
      if (insn.getOpcode() != 184) {
         return false;
      } else {
         MethodInsnNode call = (MethodInsnNode)insn;
         return call.owner.equals(STRING.getInternalName()) && call.name.equals("valueOf") && call.desc.equals("(Ljava/lang/Object;)Ljava/lang/String;");
      }
   }

   @Override
   public void expand(Target target, InjectionNode node, InsnExpander.Expansion expansion) {
      InvokeDynamicInsnNode indy = (InvokeDynamicInsnNode)node.getCurrentTarget();
      Set<InsnExpander.InsnComponent> interests = expansion.registeredInterests();
      if (interests.size() == 1 && interests.iterator().next() == StringConcatFactoryExpander.Component.TO_STRING) {
         expansion.registerInsn(StringConcatFactoryExpander.Component.TO_STRING, node.getCurrentTarget());
      } else {
         List<AbstractInsnNode> insns = new ArrayList<>();
         Type[] argTypes = Type.getArgumentTypes(indy.desc);
         int[] argMap = this.storeArgs(target, argTypes, insns::add);
         insns.add(this.makeNewBuilder());
         insns.add(new InsnNode(89));
         target.method.maxStack += 2;
         insns.add(this.makeBuilderInit());
         int nextArgument = 0;
         int finishedParts = 0;

         for (StringConcatFactoryExpander.ConcatPart part : this.parseConcat(indy)) {
            Type partType;
            if (part instanceof StringConcatFactoryExpander.ConcatPart.Argument) {
               int arg = nextArgument++;
               partType = argTypes[arg];
               insns.add(new VarInsnNode(partType.getOpcode(21), argMap[arg]));
            } else {
               Object cst;
               if (part instanceof StringConcatFactoryExpander.ConcatPart.PooledConstant) {
                  cst = ((StringConcatFactoryExpander.ConcatPart.PooledConstant)part).value;
               } else {
                  cst = ((StringConcatFactoryExpander.ConcatPart.TemplateString)part).value;
               }

               AbstractInsnNode componentInsn = expansion.registerInsn(part, new LdcInsnNode(cst));
               partType = ExpressionASMUtils.getNewType(componentInsn);
               target.method.maxStack = target.method.maxStack + partType.getSize();
               insns.add(componentInsn);
            }

            insns.add(expansion.registerInsn(new StringConcatFactoryExpander.PartialResult(finishedParts++), this.makeAppendCall(partType)));
         }

         insns.add(expansion.registerInsn(StringConcatFactoryExpander.Component.TO_STRING, this.makeToStringCall()));
         this.expandInsn(target, node, insns.toArray(new AbstractInsnNode[0]));
      }
   }

   private List<StringConcatFactoryExpander.ConcatPart> parseConcat(AbstractInsnNode insn) {
      if (!(insn instanceof InvokeDynamicInsnNode)) {
         return null;
      } else {
         InvokeDynamicInsnNode indy = (InvokeDynamicInsnNode)insn;
         if (!indy.bsm.getOwner().equals("java/lang/invoke/StringConcatFactory")) {
            return null;
         } else if (indy.bsm.getName().equals("makeConcat")) {
            int inputCount = Type.getArgumentTypes(indy.desc).length;
            return this.parseConcatWithConstants(new Object[]{StringUtils.repeat('\u0001', inputCount)});
         } else {
            return indy.bsm.getName().equals("makeConcatWithConstants") ? this.parseConcatWithConstants(indy.bsmArgs) : null;
         }
      }
   }

   private AbstractInsnNode makeNewBuilder() {
      return new TypeInsnNode(187, STRING_BUILDER.getInternalName());
   }

   private AbstractInsnNode makeBuilderInit() {
      return new MethodInsnNode(183, STRING_BUILDER.getInternalName(), "<init>", "()V", false);
   }

   private AbstractInsnNode makeAppendCall(Type type) {
      if (type.getSort() == 10) {
         type = ExpressionASMUtils.OBJECT_TYPE;
      }

      return new MethodInsnNode(182, STRING_BUILDER.getInternalName(), "append", Type.getMethodDescriptor(STRING_BUILDER, new Type[]{type}), false);
   }

   private AbstractInsnNode makeToStringCall() {
      return new MethodInsnNode(182, STRING_BUILDER.getInternalName(), "toString", Type.getMethodDescriptor(STRING, new Type[0]), false);
   }

   private List<StringConcatFactoryExpander.ConcatPart> parseConcatWithConstants(Object[] bsmArgs) {
      String template = (String)bsmArgs[0];
      List<StringConcatFactoryExpander.ConcatPart> result = new ArrayList<>();
      int id = 0;
      int nextCst = 1;
      StringBuilder currentString = null;

      for (int i = 0; i < template.length(); i++) {
         char c = template.charAt(i);
         if ((c == 1 || c == 2) && currentString != null) {
            result.add(new StringConcatFactoryExpander.ConcatPart.TemplateString(id++, currentString.toString()));
            currentString = null;
         }

         switch (c) {
            case '\u0001':
               result.add(new StringConcatFactoryExpander.ConcatPart.Argument(id++));
               break;
            case '\u0002':
               result.add(new StringConcatFactoryExpander.ConcatPart.PooledConstant(id++, bsmArgs[nextCst++]));
               break;
            default:
               if (currentString == null) {
                  currentString = new StringBuilder();
               }

               currentString.append(c);
         }
      }

      if (currentString != null) {
         result.add(new StringConcatFactoryExpander.ConcatPart.TemplateString(id, currentString.toString()));
      }

      return result;
   }

   private int[] storeArgs(Target target, Type[] args, Consumer<AbstractInsnNode> add) {
      int[] map = new int[args.length];

      for (int i = args.length - 1; i >= 0; i--) {
         Type type = args[i];
         int index = target.allocateLocals(type.getSize());
         target.addLocalVariable(index, "concatTemp" + index, type.getDescriptor());
         map[i] = index;
         add.accept(new VarInsnNode(type.getOpcode(54), index));
      }

      return map;
   }

   private static enum Component implements InsnExpander.InsnComponent {
      TO_STRING;
   }

   private abstract static class ConcatPart implements InsnExpander.InsnComponent {
      private final int id;

      private ConcatPart(int id) {
         this.id = id;
      }

      @Override
      public boolean equals(Object o) {
         if (this == o) {
            return true;
         } else if (o != null && this.getClass() == o.getClass()) {
            StringConcatFactoryExpander.ConcatPart that = (StringConcatFactoryExpander.ConcatPart)o;
            return this.id == that.id;
         } else {
            return false;
         }
      }

      @Override
      public int hashCode() {
         return Objects.hash(this.getClass(), this.id);
      }

      public static class Argument extends StringConcatFactoryExpander.ConcatPart {
         public Argument(int id) {
            super(id);
         }
      }

      public static class PooledConstant extends StringConcatFactoryExpander.ConcatPart {
         public final Object value;

         public PooledConstant(int id, Object value) {
            super(id);
            this.value = value;
         }
      }

      public static class TemplateString extends StringConcatFactoryExpander.ConcatPart {
         public final String value;

         public TemplateString(int id, String value) {
            super(id);
            this.value = value;
         }
      }
   }

   private static class PartialResult implements InsnExpander.InsnComponent {
      public final int finishedParts;

      private PartialResult(int finishedParts) {
         this.finishedParts = finishedParts;
      }

      @Override
      public boolean equals(Object o) {
         if (this == o) {
            return true;
         } else if (o != null && this.getClass() == o.getClass()) {
            StringConcatFactoryExpander.PartialResult that = (StringConcatFactoryExpander.PartialResult)o;
            return this.finishedParts == that.finishedParts;
         } else {
            return false;
         }
      }

      @Override
      public int hashCode() {
         return Objects.hash(this.getClass(), this.finishedParts);
      }
   }
}
