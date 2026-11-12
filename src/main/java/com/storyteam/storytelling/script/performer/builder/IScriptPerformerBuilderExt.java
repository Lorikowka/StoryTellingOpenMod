package com.storyteam.storytelling.script.performer.builder;

public interface IScriptPerformerBuilderExt {
   default ScriptPerformerBuilder self() {
      return (ScriptPerformerBuilder)this;
   }
}
