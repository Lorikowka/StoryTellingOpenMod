package com.storyteam.storytelling.script.story;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/** Narrow optional bridge to the public Story Engine API; no compile-time mod dependency. */
public final class StoryEngineAdapter {
   private static final String API_CLASS = "com.storyengine.api.StoryApi";

   private StoryEngineAdapter() {
   }

   public enum Operation {
      ENSURE_QUEST("ensureQuest", 3),
      EDIT_QUEST("editQuestDescription", 2),
      ADD_TASK("addManualTask", 3),
      START_QUEST("startQuestAll", 1),
      RESET_QUEST("resetQuestAll", 1),
      COMPLETE_TASK("completeTaskAll", 2),
      COMPLETE_QUEST("completeQuestAll", 1),
      FAIL_QUEST("failQuestAll", 1),
      NOTIFY("notify", 2);

      private final String method;
      private final int argumentCount;

      Operation(String method, int argumentCount) {
         this.method = method;
         this.argumentCount = argumentCount;
      }
   }

   public static void invoke(Operation operation, String... arguments) {
      if (arguments.length != operation.argumentCount) {
         throw new IllegalArgumentException("Wrong number of arguments for " + operation.method);
      }
      try {
         Class<?> api = Class.forName(API_CLASS);
         Class<?>[] signature = new Class<?>[arguments.length];
         java.util.Arrays.fill(signature, String.class);
         Method method = api.getMethod(operation.method, signature);
         if (!Modifier.isStatic(method.getModifiers())) {
            throw new IllegalStateException("Story Engine API method is not static: " + operation.method);
         }
         method.invoke(null, (Object[]) arguments);
      } catch (ClassNotFoundException e) {
         throw new IllegalStateException("This Lua story requires a compatible Story Engine mod: " + API_CLASS, e);
      } catch (NoSuchMethodException | IllegalAccessException e) {
         throw new IllegalStateException("Incompatible Story Engine API method: " + operation.method, e);
      } catch (InvocationTargetException e) {
         throw new IllegalStateException("Story Engine API call failed: " + operation.method, e.getCause());
      }
   }
}
