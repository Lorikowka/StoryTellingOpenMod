package com.llamalad7.mixinextras.expression.impl.ast.expressions;

import com.llamalad7.mixinextras.expression.impl.ExpressionSource;
import com.llamalad7.mixinextras.expression.impl.flow.FlowValue;
import com.llamalad7.mixinextras.expression.impl.point.ExpressionContext;
import com.llamalad7.mixinextras.expression.impl.utils.ComparisonInfo;
import com.llamalad7.mixinextras.expression.impl.utils.ComplexComparisonInfo;
import com.llamalad7.mixinextras.expression.impl.utils.ExpressionASMUtils;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;

public class ComparisonExpression extends Expression {
   public final Expression left;
   public final ComparisonExpression.Operator operator;
   public final Expression right;

   public ComparisonExpression(ExpressionSource src, Expression left, ComparisonExpression.Operator operator, Expression right) {
      super(src);
      this.left = left;
      this.operator = operator;
      this.right = right;
   }

   @Override
   public boolean matches(FlowValue node, ExpressionContext ctx) {
      return this.operator.matches(node, ctx) && inputsMatch(node, ctx, new Expression[]{this.left, this.right});
   }

   @Override
   public void capture(FlowValue node, ExpressionContext ctx) {
      ctx.decorate(node.getInsn(), "mixinextras_simpleExpressionType", Type.BOOLEAN_TYPE);
      super.capture(node, ctx);
   }

   public static enum Operator implements Opcodes {
      EQ(165, 159, 166, 160, 149, 151),
      NE(166, 160, 165, 159, 149, 151),
      LT(0, 161, 0, 162, 150, 152),
      LE(0, 164, 0, 163, 150, 152),
      GT(0, 163, 0, 164, 149, 151),
      GE(0, 162, 0, 161, 149, 151);

      private static final int WITH_ZERO_OFFSET = 6;
      private final int directObject;
      private final int directInt;
      private final int invertedObject;
      private final int invertedInt;
      private final int fcmp;
      private final int dcmp;

      private Operator(int directObject, int directInt, int invertedObject, int invertedInt, int fcmp, int dcmp) {
         this.directObject = directObject;
         this.directInt = directInt;
         this.invertedObject = invertedObject;
         this.invertedInt = invertedInt;
         this.fcmp = fcmp;
         this.dcmp = dcmp;
      }

      public boolean matches(FlowValue node, ExpressionContext ctx) {
         AbstractInsnNode insn = node.getInsn();
         int opcode = insn.getOpcode();
         if (node.inputCount() != 2) {
            return false;
         } else {
            boolean isComplex = false;
            Type input;
            if (opcode == this.directObject || opcode == this.invertedObject) {
               input = ExpressionASMUtils.OBJECT_TYPE;
            } else if (opcode == this.directInt || opcode == this.invertedInt) {
               input = ExpressionASMUtils.getCommonSupertype(null, node.getInput(0).getType(), node.getInput(1).getType());
            } else if (opcode == 148) {
               input = Type.LONG_TYPE;
               isComplex = true;
            } else if (opcode == this.fcmp) {
               input = Type.FLOAT_TYPE;
               isComplex = true;
            } else {
               if (opcode != this.dcmp) {
                  return false;
               }

               input = Type.DOUBLE_TYPE;
               isComplex = true;
            }

            ComparisonInfo info;
            if (isComplex) {
               int zeroDirect = this.directInt - 6;
               int zeroInverted = this.invertedInt - 6;
               FlowValue jumpNode = node.getDecoration("complexComparisonJump");
               JumpInsnNode jump = (JumpInsnNode)jumpNode.getInsn();
               if (jump == null || jump.getOpcode() != zeroDirect && jump.getOpcode() != zeroInverted) {
                  return false;
               }

               info = new ComplexComparisonInfo(opcode, node, input, jumpNode, jump.getOpcode() == zeroDirect);
            } else {
               info = new ComparisonInfo(opcode, node, input, opcode == this.directObject || opcode == this.directInt);
            }

            info.attach((k, v) -> ctx.decorate(insn, k, v), (k, v) -> ctx.decorateInjectorSpecific(insn, k, v));
            return true;
         }
      }
   }
}
