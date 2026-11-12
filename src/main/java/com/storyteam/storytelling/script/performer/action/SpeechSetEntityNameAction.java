package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.state.SpeechState;
import com.storyteam.storytelling.util.ToStringBuilder;

public class SpeechSetEntityNameAction extends ScriptPerformerAction {
   private final int entity;
   private final String name;
   private final Integer color;
   private final String icon;

   public SpeechSetEntityNameAction(ScriptPerformer performer, int entity, String name) {
      super(performer);
      this.entity = entity;
      this.name = name;
      this.color = null;
      this.icon = "unknown";
   }

   public SpeechSetEntityNameAction(ScriptPerformer performer, int entity, String name, String icon) {
      super(performer);
      this.entity = entity;
      this.name = name;
      this.color = null;
      this.icon = icon;
   }

   public SpeechSetEntityNameAction(ScriptPerformer performer, int entity, String name, Integer color) {
      super(performer);
      this.entity = entity;
      this.name = name;
      this.color = color;
      this.icon = "unknown";
   }

   public SpeechSetEntityNameAction(ScriptPerformer performer, int entity, String name, Integer color, String icon) {
      super(performer);
      this.entity = entity;
      this.name = name;
      this.color = color;
      this.icon = icon;
   }

   @Override
   public void tick() {
      if (this.color != null) {
         ((SpeechState)this.performer.getState(SpeechState.TYPE)).setEntityName(this.entity, this.name, this.color, this.icon);
      } else {
         ((SpeechState)this.performer.getState(SpeechState.TYPE)).setEntityName(this.entity, this.name, this.icon);
      }
   }

   @Override
   public void fastForwardToStage() {
      if (this.color != null) {
         ((SpeechState)this.performer.getState(SpeechState.TYPE)).setEntityName(this.entity, this.name, this.color, this.icon);
      } else {
         ((SpeechState)this.performer.getState(SpeechState.TYPE)).setEntityName(this.entity, this.name, this.icon);
      }
   }

   @Override
   public void rewindToStage() {
      if (this.color != null) {
         ((SpeechState)this.performer.getState(SpeechState.TYPE)).setEntityName(this.entity, this.name, this.color, this.icon);
      } else {
         ((SpeechState)this.performer.getState(SpeechState.TYPE)).setEntityName(this.entity, this.name, this.icon);
      }
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this)
         .append("entity", this.entity)
         .append("name", this.name)
         .append("color", this.color)
         .append("icon", this.icon)
         .build();
   }

   public int getEntity() {
      return this.entity;
   }

   public String getName() {
      return this.name;
   }
}
