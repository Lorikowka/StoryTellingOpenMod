package com.storyteam.storytelling.script.instances.backs.mechanics;

import com.storyteam.storytelling.api.RestartOnRest;
import com.storyteam.storytelling.api.SaveOnClose;
import com.storyteam.storytelling.api.Script;
import com.storyteam.storytelling.api.animations.AnimationWithCondition;
import com.storyteam.storytelling.client.content.overlays.TitleOverlay;
import com.storyteam.storytelling.events.base.STDeathEvent;
import com.storyteam.storytelling.events.listeners.ScriptIO;
import com.storyteam.storytelling.init.SoundsST;
import com.storyteam.storytelling.script.BackInstance;
import com.storyteam.storytelling.script.ScriptInstance;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.builder.ScriptPerformerBuilder;
import com.storyteam.storytelling.script.story.ScriptEnvironment;
import com.storyteam.storytelling.system._core.morph.IAnimatedPlayer;
import com.storyteam.storytelling.system._core.morph.net.PacketSetDeadCB;
import com.storyteam.storytelling.util.ScriptUtils;
import com.storyteam.storytelling.util.world.Location;
import java.util.Optional;
import lombok.Generated;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.zeith.hammerlib.api.io.NBTSerializable;
import org.zeith.hammerlib.net.Network;

@SaveOnClose
@Script(-101)
public class S101HandleDeath extends BackInstance {
   public static final int SCRIPT_ID = -101;
   private static boolean showVisualEffects = false;
   public static Location SPAWN = new Location("/execute in minecraft:overworld run tp @s -28.50 103.00 -488.50 0.00 0.00");
   @NBTSerializable
   public final ScriptPerformer mainFlow = new ScriptPerformerBuilder(this)
      .label("loop")
      .thenUsePlayer(p -> p.setInvulnerable(false))
      .thenWaitForFlag("death")
      .thenUsePlayer(p -> p.setInvulnerable(true))
      .thenDelay(20)
      .thenRun(() -> this.flags.remove("death"))
      .thenRun(() -> {
         if (ScriptUtils.getPlayer() != null) {
            ScriptUtils.getPlayer().playSound(SoundsST.DETH, 0.5F, 0.3F);
         }

         if (showVisualEffects) {
            TitleOverlay.setTitleOverlay("screen_title_dead", 30, 40, 30);
         }
      })
      .thenDelay(70)
      .thenDynamicPerformer(b -> {
         if (showVisualEffects) {
            b.screenColor(0, 15, 20, 20);
         }
      })
      .screenColor(0, 15, 20, 20)
      .thenDelay(16)
      .thenUsePlayer(p -> p.setInvulnerable(false))
      .thenDelay(5)
      .teleportPlayer(S101HandleDeath::getRespawnLocation)
      .thenPerform(
         () -> {
            for (Integer script : this.env
               .scriptInstances
               .values()
               .stream()
               .filter(s -> s.getClass().getAnnotation(RestartOnRest.class) != null)
               .map(ScriptInstance::getId)
               .toList()) {
               this.env.stop(script);
               this.env.start(script);
            }
         }
      )
      .thenUsePlayer(p -> {
         p.setInvulnerable(false);
         ((IAnimatedPlayer)p).getUnsavedProperties().put(AnimationWithCondition.DEAD, false);
         Network.sendTo(p, new PacketSetDeadCB(false));
         p.setHealth(p.getMaxHealth());
      })
      .gotoLabel("loop", true)
      .build(this::registerMainPerformer);

   public static Location getRespawnLocation() {
      setRespawnLocation(new Location(SPAWN));
      CompoundTag tag = ScriptIO.getSubTag("death").getCompound("location");
      if (tag.isEmpty()) {
         setRespawnLocation(new Location(SPAWN));
         return new Location(SPAWN);
      } else {
         return Location.deserialize(tag);
      }
   }

   public static void setRespawnLocation(Location loc) {
      ScriptIO.getSubTag("death").put("location", loc.serialize());
   }

   public S101HandleDeath(ScriptEnvironment env, int id) {
      super(env, id);
   }

   @SubscribeEvent
   public void onDeath(LivingDeathEvent event) {
      LivingEntity entity = event.getEntity();
      if (entity instanceof Player player) {
         event.setCanceled(true);
         entity.setHealth(1.0F);
         if (entity instanceof ServerPlayer spl) {
            this.flags.set("death");
            spl.setInvulnerable(true);
            ((IAnimatedPlayer)player).getUnsavedProperties().put(AnimationWithCondition.DEAD, true);
            Network.sendTo(spl, new PacketSetDeadCB(true));
            spl.removeAllEffects();
            STDeathEvent deathEvent = new STDeathEvent(player, false);
            MinecraftForge.EVENT_BUS.post(deathEvent);
            if (deathEvent.dropInv) {
               player.getInventory()
                  .items
                  .forEach(stack -> Optional.ofNullable(player.drop(stack.copyAndClear(), true, false)).ifPresent(e -> e.setPickUpDelay(400)));
               Optional.ofNullable(player.drop(player.getOffhandItem().copyAndClear(), true, false)).ifPresent(e -> e.setPickUpDelay(400));
            }
         }
      }
   }

   @Generated
   public static boolean isShowVisualEffects() {
      return showVisualEffects;
   }

   @Generated
   public static void setShowVisualEffects(boolean showVisualEffects) {
      S101HandleDeath.showVisualEffects = showVisualEffects;
   }
}
