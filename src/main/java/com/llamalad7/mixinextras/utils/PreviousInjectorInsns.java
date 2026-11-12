package com.llamalad7.mixinextras.utils;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;

public enum PreviousInjectorInsns {
   DYNAMIC_INSTANCEOF_REDIRECT("dynamic instanceof redirect") {
      @Override
      protected List<Predicate<AbstractInsnNode>> getPredicates() {
         return Arrays.asList(
            it -> it.getOpcode() == 89,
            it -> it.getOpcode() == 199,
            it -> it.getOpcode() == 187 && ((TypeInsnNode)it).desc.equals(PreviousInjectorInsns.NPE),
            it -> it.getOpcode() == 89,
            it -> isMessage(it, new String[]{"@Redirect instanceof handler ", "@ModifyConstant instanceof handler "}),
            it -> it.getOpcode() == 183 && ((MethodInsnNode)it).owner.equals(PreviousInjectorInsns.NPE),
            it -> it.getOpcode() == 191,
            it -> it instanceof LabelNode,
            it -> it.getOpcode() == 95,
            it -> it.getOpcode() == 89,
            it -> it.getOpcode() == 198,
            it -> it.getOpcode() == 182 && ((MethodInsnNode)it).name.equals("getClass"),
            it -> it.getOpcode() == 182 && ((MethodInsnNode)it).name.equals("isAssignableFrom"),
            it -> it.getOpcode() == 167,
            it -> it instanceof LabelNode,
            it -> it.getOpcode() == 87,
            it -> it.getOpcode() == 87,
            it -> it.getOpcode() == 3,
            it -> it instanceof LabelNode
         );
      }
   },
   DUPED_FACTORY_REDIRECT("duped factory redirect") {
      @Override
      protected List<Predicate<AbstractInsnNode>> getPredicates() {
         return Arrays.asList(
            it -> it.getOpcode() == 89,
            it -> it.getOpcode() == 199,
            it -> it.getOpcode() == 187 && ((TypeInsnNode)it).desc.equals(PreviousInjectorInsns.NPE),
            it -> it.getOpcode() == 89,
            it -> isMessage(it, new String[]{"@Redirect constructor handler "}),
            it -> it.getOpcode() == 183 && ((MethodInsnNode)it).owner.equals(PreviousInjectorInsns.NPE),
            it -> it.getOpcode() == 191,
            it -> it instanceof LabelNode
         );
      }
   },
   COMPARISON_WRAPPER("comparison wrapper") {
      private final Predicate<AbstractInsnNode> is0Or1 = it -> it.getOpcode() == 3 || it.getOpcode() == 4;

      @Override
      protected List<Predicate<AbstractInsnNode>> getPredicates() {
         return Arrays.asList(
            it -> it.getOpcode() == 154, this.is0Or1, it -> it.getOpcode() == 167, it -> it instanceof LabelNode, this.is0Or1, it -> it instanceof LabelNode
         );
      }
   };

   private static final String NPE = Type.getInternalName(NullPointerException.class);
   private final String description;

   private PreviousInjectorInsns(String description) {
      this.description = description;
   }

   protected abstract List<Predicate<AbstractInsnNode>> getPredicates();

   public void moveNodes(InsnList from, InsnList to, AbstractInsnNode node) {
      AbstractInsnNode current = node.getNext();

      for (Predicate<AbstractInsnNode> predicate : this.getPredicates()) {
         if (!predicate.test(current)) {
            throw new AssertionError(String.format("Failed assertion when wrapping instructions of %s. Please inform LlamaLad7!", this.description));
         }

         AbstractInsnNode old = current;

         do {
            current = current.getNext();
         } while (current instanceof FrameNode);

         from.remove(old);
         to.add(old);
      }
   }

   public AbstractInsnNode getLast(AbstractInsnNode node) {
      AbstractInsnNode current = node.getNext();
      AbstractInsnNode result = null;

      for (Predicate<AbstractInsnNode> predicate : this.getPredicates()) {
         if (!predicate.test(current)) {
            throw new AssertionError(String.format("Failed assertion when walking instructions of %s. Please inform LlamaLad7!", this.description));
         }

         result = current;

         do {
            current = current.getNext();
         } while (!(current instanceof FrameNode));
      }

      return result;
   }

   protected static boolean isMessage(AbstractInsnNode insn, String... messages) {
      if (!(insn instanceof LdcInsnNode)) {
         return false;
      } else {
         LdcInsnNode ldc = (LdcInsnNode)insn;
         if (!(ldc.cst instanceof String)) {
            return false;
         } else {
            String cst = (String)ldc.cst;
            return Arrays.stream(messages).anyMatch(cst::startsWith);
         }
      }
   }
}
