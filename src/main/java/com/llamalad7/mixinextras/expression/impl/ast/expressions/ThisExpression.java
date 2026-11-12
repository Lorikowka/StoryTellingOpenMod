package com.llamalad7.mixinextras.expression.impl.ast.expressions;

import com.llamalad7.mixinextras.expression.impl.ExpressionSource;
import com.llamalad7.mixinextras.expression.impl.flow.FlowValue;
import com.llamalad7.mixinextras.expression.impl.point.ExpressionContext;
import org.objectweb.asm.tree.VarInsnNode;

public class ThisExpression extends SimpleExpression {
   public ThisExpression(ExpressionSource src) {
      super(src);
   }

   @Override
   public boolean matches(FlowValue node, ExpressionContext ctx) {
      return ctx.isStatic ? false : node.getInsn().getOpcode() == 25 && ((VarInsnNode)node.getInsn()).var == 0;
   }
}
