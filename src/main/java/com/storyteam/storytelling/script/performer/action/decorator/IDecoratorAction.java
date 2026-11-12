package com.storyteam.storytelling.script.performer.action.decorator;

@FunctionalInterface
public interface IDecoratorAction<A> {
   A decorate(A var1);
}
