package com.storyteam.storytelling.system._core.morph;

import com.storyteam.storytelling.St;
import com.storyteam.storytelling.system._core.morph.data.PlayerPoseMap;
import com.storyteam.storytelling.system._sysofsys.ISystem;
import com.storyteam.storytelling.system._sysofsys.SystemRoot;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityEvent.Size;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegistryBuilder;
import org.zeith.api.registry.RegistryMapping;
import org.zeith.hammerlib.abstractions.props.KeyMap;

@SystemRoot("morph")
public class MorphSystem implements ISystem {
   private static Supplier<IForgeRegistry<PlayerMorph>> PLAYER_MORPHS;

   @Override
   public void gatherSimplyRegisters(Consumer<Class<?>> registrar, BiConsumer<String, Class<?>> registrarWithPrefix) {
      ISystem.super.gatherSimplyRegisters(registrar, registrarWithPrefix);
   }

   @Override
   public void subscribe(IEventBus gameBus, IEventBus modBus) {
      modBus.addListener(this::newRegistries);
      gameBus.addListener(this::respectPlayerMorph);
      gameBus.addListener(CommandMorph::register);
      gameBus.register(MorphEventHandler.class);
   }

   private void respectPlayerMorph(Size e) {
      if (e.getEntity() instanceof Player player && player instanceof IAnimatedPlayer anim) {
         KeyMap props = anim.getUnsavedProperties();
         PlayerMorph morph = anim.getMorph();
         if (morph == null) {
            if (props != null) {
               props.put(PlayerPoseMap.POSES, null);
            }

            return;
         }

         MorphSizeHandler sizeFn = morph.getEntitySize();
         if (sizeFn == null) {
            if (props != null) {
               props.put(PlayerPoseMap.POSES, null);
            }

            e.setNewSize(player.getDimensions(e.getPose()));
            return;
         }

         sizeFn.updateSize(player, e);
      }
   }

   private void newRegistries(NewRegistryEvent e) {
      PLAYER_MORPHS = e.create(
         new RegistryBuilder<PlayerMorph>().setName(St.id("player_morphs")).disableSaving(), reg -> RegistryMapping.report(PlayerMorph.class, reg, false)
      );
   }

   public static IForgeRegistry<PlayerMorph> playerMorphs() {
      return PLAYER_MORPHS.get();
   }
}
