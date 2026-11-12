package com.storyteam.storytelling.system.animation;

import com.storyteam.storytelling.client.AnimationWithMetadata;
import com.storyteam.storytelling.client.Animations;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;

public class AnimationsDescription {
   private static final Map<Field, String> NAMES = new HashMap<>();

   @NotNull
   public static String forAnimation(Field name) {
      return NAMES.getOrDefault(name, "Нет описания");
   }

   static {
      Pattern regex = Pattern.compile("(IAnimationSource|SidedAnimationSet)\\s+(?<name>[^ ]*)( = .+)?;\\s*\\/\\/(?<comment>.*)");
      Map<Class<?>, Map<String, String>> decoded = new HashMap<>();
      Function<Class<?>, Map<String, String>> comments = type -> decoded.computeIfAbsent(type, t -> {
            Path p = DevWorkspaceAccessor.resolveClassPath((Class<?>)t);
            Map<String, String> kv = new HashMap<>();

            try {
               Files.lines(p, StandardCharsets.UTF_8).map(String::trim).forEach(ln -> {
                  Matcher m = regex.matcher(ln);
                  if (m.find()) {
                     String name = m.group("name").replace(";", "").trim();
                     String comment = m.group("comment").toLowerCase(Locale.ROOT).trim();
                     kv.put(name, comment);
                  }
               });
            } catch (Exception var5) {
               throw new RuntimeException(var5);
            }

            return Collections.unmodifiableMap(kv);
         });
      Animations.animations().map(AnimationWithMetadata::referenceField).forEach(f -> {
         String comment = comments.apply(f.getDeclaringClass()).get(f.getName());
         if (comment != null && !comment.isBlank()) {
            NAMES.put(f, comment);
         }
      });
   }
}
