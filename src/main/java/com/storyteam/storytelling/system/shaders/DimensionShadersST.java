package com.storyteam.storytelling.system.shaders;

import com.storyteam.storytelling.util.ScriptUtils;
import net.minecraft.world.level.Level;

public interface DimensionShadersST {
   static void init() {
      DimensionShaderHandler.setDimensionShader(Level.OVERWORLD, new ShaderModifier("overworld"));
      DimensionShaderHandler.setDimensionShader(ScriptUtils.THEABYSS, new ShaderModifier("theabyss").priority(1.0F));
      DimensionShaderHandler.setDimensionShader(ScriptUtils.WHITE, new ShaderModifier("white").priority(1.0F));
      DimensionShaderHandler.defaultShaders = new ShaderModifier("overworld");
   }
}
