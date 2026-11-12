package com.storyteam.storytelling.system._sysofsys;

import com.storyteam.storytelling.St;
import java.lang.annotation.ElementType;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import lombok.Generated;
import net.minecraftforge.eventbus.api.IEventBus;
import org.objectweb.asm.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeith.hammerlib.annotations.SimplyRegister;
import org.zeith.hammerlib.api.proxy.IProxy;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.mcf.ScanDataHelper;
import org.zeith.hammerlib.util.mcf.ScanDataHelper.ModAwareAnnotationData;

public class SystemOfSystems {
   @Generated
   private static final Logger log = LoggerFactory.getLogger(SystemOfSystems.class);
   private static final boolean ANIM_GAMETEST = Boolean.parseBoolean(System.getProperty("storytelling.anim.gametest"));
   private static final List<ISystem> REGISTRY = ANIM_GAMETEST
      ? List.of()
      : ScanDataHelper.lookupAnnotatedObjects(SystemRoot.class)
         .stream()
         .map(data -> create(data.getOwnerClass().asSubclass(ISystem.class)))
         .peek(s -> St.id(s.getSystemName()))
         .toList();
   private static final Map<Class<? extends ISystem>, ISystem> MAPPINGS = REGISTRY.stream()
      .collect(Collectors.toMap(s -> s.getClass().asSubclass(ISystem.class), UnaryOperator.identity()));

   public static <T extends ISystem> T getInstance(Class<T> type) {
      return Objects.requireNonNull((T)Cast.cast(MAPPINGS.get(type)), "System of " + type);
   }

   public static void construct(IEventBus gameBus, IEventBus modBus) {
      Set<String> declaredSimplyRegisters = new HashSet<>();

      for (ISystem reg : REGISTRY) {
         reg.subscribe(gameBus, modBus);
         BiConsumer<String, Class<?>> registrarWithPrefix = (prefix, t) -> {
            declaredSimplyRegisters.add(t.getName());
            String expectPrefix = "system/" + reg.getSystemName() + "/" + prefix;
            String gotPrefix = ((SimplyRegister)t.getAnnotation(SimplyRegister.class)).prefix();
            if (!gotPrefix.startsWith(expectPrefix)) {
               String msgx = "Mismatched @SimplyRegister prefix for %s in system %s. Expected %s, but got %s"
                  .formatted(t, reg.getSystemName(), expectPrefix, gotPrefix);
               log.error(msgx);
               throw new IllegalArgumentException(msgx);
            }
         };
         reg.gatherSimplyRegisters(t -> registrarWithPrefix.accept("", t), registrarWithPrefix);
      }

      List<String> all = ScanDataHelper.lookupAnnotatedObjects(SimplyRegister.class)
         .stream()
         .filter(d -> d.getTargetType() == ElementType.TYPE)
         .map(ModAwareAnnotationData::clazz)
         .<String>map(Type::getClassName)
         .filter(d -> d.startsWith("com.storyteam.storytelling.system."))
         .filter(d -> !declaredSimplyRegisters.contains(d))
         .toList();
      if (!all.isEmpty()) {
         String msg = "Undeclared/unbound system-local @SimplyRegisters found: " + all;
         log.error(msg);
         throw new IllegalArgumentException(msg);
      } else {
         IProxy.runSided(() -> () -> {
               for (ISystem reg : REGISTRY) {
                  reg.subscribeClient(gameBus, modBus);
               }
            }, () -> () -> {
            });
      }
   }

   private static ISystem create(Class<? extends ISystem> type) {
      try {
         return type.getDeclaredConstructor().newInstance();
      } catch (Throwable var2) {
         if (var2 instanceof RuntimeException runtime) throw runtime;
         if (var2 instanceof Error error) throw error;
         throw new IllegalStateException("Operation failed", var2);
      }
   }
}
