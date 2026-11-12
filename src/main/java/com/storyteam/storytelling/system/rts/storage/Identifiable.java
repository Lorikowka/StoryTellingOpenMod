package com.storyteam.storytelling.system.rts.storage;

public interface Identifiable<TKey> {
   TKey getId();

   void setId(TKey var1);
}
