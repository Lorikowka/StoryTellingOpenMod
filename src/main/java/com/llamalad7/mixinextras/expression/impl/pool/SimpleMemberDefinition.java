package com.llamalad7.mixinextras.expression.impl.pool;

import com.llamalad7.mixinextras.expression.impl.flow.FlowValue;
import com.llamalad7.mixinextras.expression.impl.flow.postprocessing.LMFInfo;
import org.objectweb.asm.Handle;
import org.objectweb.asm.tree.AbstractInsnNode;

public interface SimpleMemberDefinition extends MemberDefinition {
   boolean matches(AbstractInsnNode var1);

   default boolean matches(Handle handle) {
      return false;
   }

   @Override
   default boolean matches(FlowValue node) {
      LMFInfo lmfInfo = node.getDecoration("lmfInfo");
      return lmfInfo != null ? this.matches(lmfInfo.impl) : this.matches(node.getInsn());
   }
}
