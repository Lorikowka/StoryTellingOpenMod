package com.llamalad7.mixinextras.expression.impl.ast.expressions;

import com.llamalad7.mixinextras.expression.impl.ExpressionSource;
import com.llamalad7.mixinextras.expression.impl.flow.FlowValue;
import com.llamalad7.mixinextras.expression.impl.point.ExpressionContext;

public class UnaryExpression extends SimpleExpression {
   public final UnaryExpression.Operator operator;
   public final Expression expression;

   public UnaryExpression(ExpressionSource src, UnaryExpression.Operator operator, Expression expression) {
      super(src);
      this.operator = operator;
      this.expression = expression;
   }

   @Override
   public boolean matches(FlowValue node, ExpressionContext ctx) {
      switch (this.operator) {
         case MINUS:
            switch (node.getInsn().getOpcode()) {
               case 116:
               case 117:
               case 118:
               case 119:
                  return inputsMatch(node, ctx, new Expression[]{this.expression});
            }
         case BITWISE_NOT:
            return new BinaryExpression(null, this.expression, BinaryExpression.Operator.BITWISE_XOR, new IntLiteralExpression(null, -1L)).matches(node, ctx);
         default:
            return false;
      }
   }

   public static enum Operator {
      MINUS,
      BITWISE_NOT;
   }
}
