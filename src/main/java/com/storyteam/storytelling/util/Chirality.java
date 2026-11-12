package com.storyteam.storytelling.util;

import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Chirality {
   LEFT,
   RIGHT;

   public static final Map<String, Chirality> MAPPINGS = Stream.of(values()).collect(Collectors.toMap(Enum::name, UnaryOperator.identity()));
}
