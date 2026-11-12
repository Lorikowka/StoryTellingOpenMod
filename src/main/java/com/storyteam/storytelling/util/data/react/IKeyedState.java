package com.storyteam.storytelling.util.data.react;

import java.util.Objects;
import java.util.function.Function;
import org.zeith.hammerlib.abstractions.props.Key;
import org.zeith.hammerlib.abstractions.props.KeyMap;

public interface IKeyedState<C, T> extends IState<C> {
   static <C, T> IKeyedState<C, T> property(final Key<T> property, final Function<C, T> compute) {
      return new IKeyedState<C, T>() {
         @Override
         public Key<T> key() {
            return property;
         }

         @Override
         public T apply(C context) {
            return compute.apply(context);
         }

         @Override
         public String toString() {
            return "IKeyedState<" + property.type().getSimpleName() + "/" + property.name() + ">{" + compute + "}";
         }
      };
   }

   Key<T> key();

   T apply(C var1);

   default KeyMap getKeyMap(C context) {
      return ((IContextKeyMapHolder)context).getUnsavedProperties();
   }

   @Override
   default boolean hasChanged(C context) {
      KeyMap map = this.getKeyMap(context);
      T newVal = this.apply(context);
      Key<T> k = this.key();
      return map.opt(k).map(p -> !Objects.equals(p, newVal)).orElse(true);
   }

   @Override
   default void applyChanges(C context) {
      KeyMap map = this.getKeyMap(context);
      map.put(this.key(), this.apply(context));
   }
}
