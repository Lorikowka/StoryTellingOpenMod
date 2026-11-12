package com.storyteam.storytelling.util.data.react;

public interface IState<C> {
   boolean hasChanged(C var1);

   void applyChanges(C var1);
}
