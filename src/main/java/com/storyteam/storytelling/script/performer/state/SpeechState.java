package com.storyteam.storytelling.script.performer.state;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import javax.annotation.Nullable;

public class SpeechState extends State<ScriptPerformer> {
   public static final State.Type<ScriptPerformer, SpeechState> TYPE = create(SpeechState::new);
   private final Int2ObjectMap<SpeechEntity> entities = new Int2ObjectOpenHashMap();

   public SpeechState(ScriptPerformer performer) {
      super(performer);
   }

   public void setEntityName(int id, String name, String icon) {
      this.entities.put(id, new SpeechEntity(name, 16750070, icon));
   }

   public void setEntityName(int id, String name, int color, String icon) {
      this.entities.put(id, new SpeechEntity(name, color, icon));
   }

   public void removeEntityName(int id) {
      this.entities.remove(id);
   }

   @Nullable
   public String getEntityName(int id) {
      SpeechEntity entity = (SpeechEntity)this.entities.get(id);
      return entity == null ? null : entity.getName();
   }

   @Nullable
   public SpeechEntity getEntity(int id) {
      return (SpeechEntity)this.entities.getOrDefault(id, null);
   }
}
