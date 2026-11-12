package com.storyteam.storytelling.api.annotations;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface AnnotationHandler {
   Class<? extends Annotation> target();

   Priority priority() default Priority.MEDIUM;
}
