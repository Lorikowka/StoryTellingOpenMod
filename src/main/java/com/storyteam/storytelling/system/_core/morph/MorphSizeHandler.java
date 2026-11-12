package com.storyteam.storytelling.system._core.morph;

import com.google.common.collect.ImmutableMap;
import com.storyteam.storytelling.St;
import com.storyteam.storytelling.system._core.morph.data.PlayerPoseMap;
import com.storyteam.storytelling.util.data.react.IKeyedState;
import com.storyteam.storytelling.util.data.react.IState;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityEvent.Size;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.abstractions.props.Key;
import org.zeith.hammerlib.abstractions.props.KeyMap;

public abstract class MorphSizeHandler {
   protected static final EntityDimensions SLEEPING_DIMENSIONS = EntityDimensions.fixed(0.2F, 0.2F);
   private static final Map<Pose, EntityDimensions> POSES = ImmutableMap.<Pose, EntityDimensions>builder()
      .put(Pose.STANDING, Player.STANDING_DIMENSIONS)
      .put(Pose.SLEEPING, SLEEPING_DIMENSIONS)
      .put(Pose.FALL_FLYING, EntityDimensions.scalable(0.6F, 0.6F))
      .put(Pose.SWIMMING, EntityDimensions.scalable(0.6F, 0.6F))
      .put(Pose.SPIN_ATTACK, EntityDimensions.scalable(0.6F, 0.6F))
      .put(Pose.CROUCHING, EntityDimensions.scalable(0.6F, 1.5F))
      .put(Pose.DYING, EntityDimensions.fixed(0.2F, 0.2F))
      .build();
   public static final Key<Pose> CTX_POSE = Key.of(St.id("context_pose"), Pose.class);
   public static final Key<Pose> K_POSE = Key.of(St.id("pose"), Pose.class);
   public static final Key<Boolean> K_SHIFTING = Key.of(St.id("shifting"), Boolean.class);
   public static IKeyedState<Player, Pose> PLAYER_POSE = IKeyedState.property(K_POSE, Entity::getPose);
   public static IKeyedState<Player, Boolean> PLAYER_SNEAKING = IKeyedState.property(K_SHIFTING, Entity::isShiftKeyDown);
   public static final MorphSizeHandler DEFAULT = of(0.6F, 1.8F).watchState(PLAYER_POSE).watchState(PLAYER_SNEAKING);
   protected final List<IState<Player>> states = new ArrayList<>();

   @Nullable
   protected abstract EntityDimensions updateSize(Player var1, EntityDimensions var2, Pose var3);

   public MorphSizeHandler watchState(IState<Player> state) {
      this.states.add(state);
      return this;
   }

   public void updateSize(Player player, Size event) {
      for (IState<Player> state : this.states) {
         state.applyChanges(player);
      }

      PlayerPoseMap.PlayerPoseMapBuilder map = PlayerPoseMap.builder();

      for (Pose value : POSES.keySet()) {
         map.pose(value, this.updateSize(player, POSES.get(value), event.getPose()));
      }

      ((IAnimatedPlayer)player).getUnsavedProperties().put(PlayerPoseMap.POSES, map.build());
      EntityDimensions ns = this.updateSize(player, event.getNewSize(), event.getPose());
      if (ns != null) {
         event.setNewSize(ns);
      }
   }

   public void update(Player player) {
      boolean changed = false;

      for (IState<Player> state : this.states) {
         if (state.hasChanged(player)) {
            changed = true;
         }
      }

      if (changed) {
         player.refreshDimensions();
      }
   }

   public static MorphSizeHandler useState(Function<KeyMap, EntityDimensions> state, List<IKeyedState<Player, ?>> properties) {
      return useState((props, old) -> state.apply(props), properties);
   }

   public static MorphSizeHandler useState(final BiFunction<KeyMap, EntityDimensions, EntityDimensions> state, final List<IKeyedState<Player, ?>> properties) {
      return new MorphSizeHandler() {
         {
            this.states.addAll(properties);
         }

         @Nullable
         @Override
         protected EntityDimensions updateSize(Player player, EntityDimensions original, Pose pose) {
            KeyMap props = ((IAnimatedPlayer)player).getUnsavedProperties();
            props.put(CTX_POSE, pose);
            return state.apply(props, original);
         }

         @Override
         public String toString() {
            return "MorphSizeHandler.useState{state=" + state + ",properties=" + properties + "}";
         }
      };
   }

   public static MorphSizeHandler of(float pWidth, float pHeight) {
      return of(pWidth, pHeight, false);
   }

   public static MorphSizeHandler of(float pWidth, float pHeight, boolean pFixed) {
      return of((pl, dim) -> new EntityDimensions(pWidth, pHeight, pFixed));
   }

   public static MorphSizeHandler of(final BiFunction<Player, EntityDimensions, EntityDimensions> size) {
      return new MorphSizeHandler() {
         @Nullable
         @Override
         protected EntityDimensions updateSize(Player player, EntityDimensions original, Pose pose) {
            return size.apply(player, original);
         }

         @Override
         public String toString() {
            return "MorphSizeHandler{" + size + "}";
         }
      };
   }
}
