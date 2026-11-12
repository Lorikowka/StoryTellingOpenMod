package com.storyteam.storytelling.api.annotations.client;

import com.storyteam.storytelling.Storytelling;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import net.minecraft.resources.ResourceLocation;
import org.zeith.hammerlib.annotations.ap.IAPContext;
import org.zeith.hammerlib.annotations.ap.IAnnotationProcessor;
import org.zeith.hammerlib.annotations.ap.RegisterAP;

@Target({ElementType.FIELD})
public @interface WithScreen {
   Class<?> value();

   @RegisterAP(
      value = WithScreen.class,
      clientOnly = true
   )
   public static class AP implements IAnnotationProcessor<WithScreen> {
      public void onPostRegistered(IAPContext context, WithScreen annotation, Field field, Object value) {
         if (value instanceof WithScreen.IScreenTypeReceiver bet) {
            bet.acceptScreenAnnotation(annotation.value());
            String id = context.getRegistryName().<String>map(ResourceLocation::toString).orElse("??:??");
            Storytelling.LOG
               .debug("Applied WithScreen for {}[{}] {}.{}", field.getType().getSimpleName(), id, field.getDeclaringClass().getSimpleName(), field.getName());
         }
      }
   }

   public interface IScreenTypeReceiver {
      void acceptScreenAnnotation(Class<?> var1);
   }
}
