package com.llamalad7.mixinextras.expression.impl.ast.expressions;

import com.llamalad7.mixinextras.expression.impl.ExpressionSource;
import com.llamalad7.mixinextras.expression.impl.ast.identifiers.TypeIdentifier;
import com.llamalad7.mixinextras.expression.impl.flow.FlowValue;
import com.llamalad7.mixinextras.expression.impl.point.ExpressionContext;
import com.llamalad7.mixinextras.expression.impl.utils.ExpressionASMUtils;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;

public class CastExpression extends SimpleExpression {
   public final TypeIdentifier type;
   public final Expression expression;

   public CastExpression(ExpressionSource src, TypeIdentifier type, Expression expression) {
      super(src);
      this.type = type;
      this.expression = expression;
   }

   @Override
   public boolean matches(FlowValue node, ExpressionContext ctx) {
      Type castType = this.getCastType(node.getInsn());
      return castType != null && this.type.matches(ctx.pool, castType) && inputsMatch(node, ctx, new Expression[]{this.expression});
   }

   @Override
   public void capture(FlowValue node, ExpressionContext ctx) {
      if (node.getInsn().getOpcode() == 192) {
         ctx.decorate(node.getInsn(), "mixinextras_simpleOperationArgs", new Type[]{ExpressionASMUtils.OBJECT_TYPE});
         ctx.decorate(node.getInsn(), "mixinextras_simpleOperationReturnType", node.getType());
         ctx.decorate(node.getInsn(), "mixinextras_simpleOperationParamNames", new String[]{"object"});
      }

      super.capture(node, ctx);
   }

   private Type getCastType(AbstractInsnNode insn) {
      switch (insn.getOpcode()) {
         case 133:
         case 140:
         case 143:
            return Type.LONG_TYPE;
         case 134:
         case 137:
         case 144:
            return Type.FLOAT_TYPE;
         case 135:
         case 138:
         case 141:
            return Type.DOUBLE_TYPE;
         case 136:
         case 139:
         case 142:
            return Type.INT_TYPE;
         case 145:
            return Type.BYTE_TYPE;
         case 146:
            return Type.CHAR_TYPE;
         case 147:
            return Type.SHORT_TYPE;
         case 148:
         case 149:
         case 150:
         case 151:
         case 152:
         case 153:
         case 154:
         case 155:
         case 156:
         case 157:
         case 158:
         case 159:
         case 160:
         case 161:
         case 162:
         case 163:
         case 164:
         case 165:
         case 166:
         case 167:
         case 168:
         case 169:
         case 170:
         case 171:
         case 172:
         case 173:
         case 174:
         case 175:
         case 176:
         case 177:
         case 178:
         case 179:
         case 180:
         case 181:
         case 182:
         case 183:
         case 184:
         case 185:
         case 186:
         case 187:
         case 188:
         case 189:
         case 190:
         case 191:
         default:
            return null;
         case 192:
            return Type.getObjectType(((TypeInsnNode)insn).desc);
      }
   }
}
