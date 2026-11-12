package com.storyteam.storytelling.system.simulator.me_tab.fragment.call_context;

import java.util.function.Supplier;

public final class CallContext {
   private static final ThreadLocal<ActionContext> CONTEXT = ThreadLocal.withInitial(() -> ActionContext.DEFAULT);

   private CallContext() {
   }

   private static void set(ActionContext ctx) {
      CONTEXT.set(ctx);
   }

   public static ActionContext get() {
      ActionContext ctx = CONTEXT.get();
      return ctx == null ? ActionContext.DEFAULT : ctx;
   }

   private static void clear() {
      CONTEXT.remove();
   }

   public static void runIn(ActionContext ctx, Runnable action) {
      ActionContext prev = get();
      set(ctx);

      try {
         action.run();
      } finally {
         set(prev);
      }
   }

   public static <T> T callIn(ActionContext ctx, Supplier<T> supplier) {
      ActionContext prev = get();
      set(ctx);

      Object var3;
      try {
         var3 = supplier.get();
      } finally {
         set(prev);
      }

      return (T)var3;
   }
}
