package com.storyteam.storytelling.script.performer.builder;

import com.google.common.base.Preconditions;
import com.storyteam.storytelling.St;
import com.storyteam.storytelling.api.data.EntityInteractionData;
import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.client.Animations;
import com.storyteam.storytelling.client.particle.circleTint.CircleTintData;
import com.storyteam.storytelling.client.particle.controllers.GravityController;
import com.storyteam.storytelling.client.particle.controllers.StandartController;
import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.content.entities.HumanoidEntity;
import com.storyteam.storytelling.content.entities.configurations.AnimConfigurationWrapper;
import com.storyteam.storytelling.content.entities.configurations.IDefaultAnimConfiguration;
import com.storyteam.storytelling.content.entities.configurations.ILivingAnimationEntity;
import com.storyteam.storytelling.content.entities.configurations.MovementActionType;
import com.storyteam.storytelling.content.entities.mobs.BaseMob;
import com.storyteam.storytelling.init.ParticleTypesST;
import com.storyteam.storytelling.init.ScriptCategory;
import com.storyteam.storytelling.init.SoundsST;
import com.storyteam.storytelling.script.ScriptInstance;
import com.storyteam.storytelling.script.performer.CharacterNotVisible;
import com.storyteam.storytelling.script.performer.CharacterVisibleAction;
import com.storyteam.storytelling.script.performer.Performer;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.action.AbsentFlagAction;
import com.storyteam.storytelling.script.performer.action.AnimatedSetTraversalNavigation;
import com.storyteam.storytelling.script.performer.action.BlinkAction;
import com.storyteam.storytelling.script.performer.action.CharacterInitializationAction;
import com.storyteam.storytelling.script.performer.action.ChatMessageAction;
import com.storyteam.storytelling.script.performer.action.ClearChatAction;
import com.storyteam.storytelling.script.performer.action.ClearEntityGoalAction;
import com.storyteam.storytelling.script.performer.action.ClearWebCategoryAction;
import com.storyteam.storytelling.script.performer.action.ConsumeFlagAction;
import com.storyteam.storytelling.script.performer.action.DialogAction;
import com.storyteam.storytelling.script.performer.action.DynamicPerformerAction;
import com.storyteam.storytelling.script.performer.action.EffectAction;
import com.storyteam.storytelling.script.performer.action.EnitiyJumpAction;
import com.storyteam.storytelling.script.performer.action.EnsureScriptDisabledAction;
import com.storyteam.storytelling.script.performer.action.EnsureScriptEnabledAction;
import com.storyteam.storytelling.script.performer.action.EnsureScriptsSetAction;
import com.storyteam.storytelling.script.performer.action.EntityChangeOutfitAction;
import com.storyteam.storytelling.script.performer.action.EntityClearAnimationsAction;
import com.storyteam.storytelling.script.performer.action.EntityCloseToPlayerAction;
import com.storyteam.storytelling.script.performer.action.EntityLookAtCharacterAction;
import com.storyteam.storytelling.script.performer.action.EntityLookAtLocAction;
import com.storyteam.storytelling.script.performer.action.EntityLookAtNearest;
import com.storyteam.storytelling.script.performer.action.EntityLookAtPosAction;
import com.storyteam.storytelling.script.performer.action.EntityLoopPathAction;
import com.storyteam.storytelling.script.performer.action.EntityPathPlayAction;
import com.storyteam.storytelling.script.performer.action.EntityPerformerInvokerAction;
import com.storyteam.storytelling.script.performer.action.EntityPhrasesPerformerAction;
import com.storyteam.storytelling.script.performer.action.EntityPickUpItemsAction;
import com.storyteam.storytelling.script.performer.action.EntityPositionRelativeToPlayer;
import com.storyteam.storytelling.script.performer.action.EntityRandomPickUpAction;
import com.storyteam.storytelling.script.performer.action.EntitySetAnimationAction;
import com.storyteam.storytelling.script.performer.action.EntitySetDisplayNameAction;
import com.storyteam.storytelling.script.performer.action.EntitySetHeldItemAction;
import com.storyteam.storytelling.script.performer.action.EntitySetSpeedAction;
import com.storyteam.storytelling.script.performer.action.EntitySetTextureAction;
import com.storyteam.storytelling.script.performer.action.EntitySightStateAction;
import com.storyteam.storytelling.script.performer.action.EntitySitAction;
import com.storyteam.storytelling.script.performer.action.EntityStartRandomFlyWalkingAction;
import com.storyteam.storytelling.script.performer.action.EntityStartRandomWalkingAction;
import com.storyteam.storytelling.script.performer.action.EntityTypeOverrideAction;
import com.storyteam.storytelling.script.performer.action.EventAwaitingAction;
import com.storyteam.storytelling.script.performer.action.F10PressAction;
import com.storyteam.storytelling.script.performer.action.F8PressAction;
import com.storyteam.storytelling.script.performer.action.F9PressAction;
import com.storyteam.storytelling.script.performer.action.FreeEntityAction;
import com.storyteam.storytelling.script.performer.action.HandleEntityStateAction;
import com.storyteam.storytelling.script.performer.action.HandleStateAction;
import com.storyteam.storytelling.script.performer.action.HumanoidBlockAnimWalkingBodyRotationAction;
import com.storyteam.storytelling.script.performer.action.HumanoidBlockBodyRotationAction;
import com.storyteam.storytelling.script.performer.action.HumanoidDontRotateOnInteractionAction;
import com.storyteam.storytelling.script.performer.action.HumanoidIgnoreNavmeshAction;
import com.storyteam.storytelling.script.performer.action.HumanoidRestrictHeadRotationAction;
import com.storyteam.storytelling.script.performer.action.ImportBuildingAction;
import com.storyteam.storytelling.script.performer.action.KeyPressAction;
import com.storyteam.storytelling.script.performer.action.OverridePredefinedAnimationAction;
import com.storyteam.storytelling.script.performer.action.PerformerAction;
import com.storyteam.storytelling.script.performer.action.PlayerNearPosAction;
import com.storyteam.storytelling.script.performer.action.PlayerSitAction;
import com.storyteam.storytelling.script.performer.action.PlayerTeleportAction;
import com.storyteam.storytelling.script.performer.action.PositionEntityAction;
import com.storyteam.storytelling.script.performer.action.PositionEntityNearPlayerAction;
import com.storyteam.storytelling.script.performer.action.PositionOnceBehindPlayerAction;
import com.storyteam.storytelling.script.performer.action.RemoveEntityAction;
import com.storyteam.storytelling.script.performer.action.RemoveWebMessageAction;
import com.storyteam.storytelling.script.performer.action.RepositionEntityAction;
import com.storyteam.storytelling.script.performer.action.ScreenColorAction;
import com.storyteam.storytelling.script.performer.action.ScreenColorBelowChatAction;
import com.storyteam.storytelling.script.performer.action.ScriptAutostartAction;
import com.storyteam.storytelling.script.performer.action.SendWebMessageAction;
import com.storyteam.storytelling.script.performer.action.SetEntityGoalsEnabledAction;
import com.storyteam.storytelling.script.performer.action.SetPlayerTextureAction;
import com.storyteam.storytelling.script.performer.action.SpectateEntityAction;
import com.storyteam.storytelling.script.performer.action.SpectateStopEntityAction;
import com.storyteam.storytelling.script.performer.action.SpeechMessageAction;
import com.storyteam.storytelling.script.performer.action.SpeechSetEntityNameAction;
import com.storyteam.storytelling.script.performer.action.StartEntityTracking;
import com.storyteam.storytelling.script.performer.action.TDCutsceneEnd;
import com.storyteam.storytelling.script.performer.action.TDCutsceneMoveCameraAction;
import com.storyteam.storytelling.script.performer.action.TDCutsceneTpOrInitAction;
import com.storyteam.storytelling.script.performer.action.ThinkMessageAction;
import com.storyteam.storytelling.script.performer.action.TitleAction;
import com.storyteam.storytelling.script.performer.action.TriggerAction;
import com.storyteam.storytelling.script.performer.action.UseEntityAction;
import com.storyteam.storytelling.script.performer.action.UseTwoEntityAction;
import com.storyteam.storytelling.script.performer.action.WaitPlayerEnterDimensionAction;
import com.storyteam.storytelling.script.performer.action.WhenPlayerInBoxAction;
import com.storyteam.storytelling.script.performer.action.behaviour.BehaviourAction;
import com.storyteam.storytelling.script.performer.action.behaviour.CatmullRomMoveAction;
import com.storyteam.storytelling.script.performer.action.behaviour.EntityFlyAwayAction;
import com.storyteam.storytelling.script.performer.action.behaviour.EntityFollowEntityAction;
import com.storyteam.storytelling.script.performer.action.behaviour.EntityFollowEntitynHeightAction;
import com.storyteam.storytelling.script.performer.action.behaviour.EntityFollowPlayerAction;
import com.storyteam.storytelling.script.performer.action.behaviour.EntityFollowVectorAction;
import com.storyteam.storytelling.script.performer.action.behaviour.EntityMoveAction;
import com.storyteam.storytelling.script.performer.action.behaviour.EntityMoveNoPhysicsAction;
import com.storyteam.storytelling.script.performer.action.behaviour.EntityPathfinderAction;
import com.storyteam.storytelling.script.performer.action.behaviour.FightPerformerAction;
import com.storyteam.storytelling.script.performer.action.behaviour.SimpleBehaviourAction;
import com.storyteam.storytelling.script.performer.action.modificators.PerformerModifier;
import com.storyteam.storytelling.script.performer.entity.EntityState;
import com.storyteam.storytelling.script.performer.entity.behaviour.BehaviourType;
import com.storyteam.storytelling.script.performer.entity.behaviour.fight.chain.IAttackChain;
import com.storyteam.storytelling.script.performer.entity.behaviour.interruption.InterruptionBehaviour;
import com.storyteam.storytelling.script.performer.entity.behaviour.phrases.Phrase;
import com.storyteam.storytelling.script.performer.entity.behaviour.phrases.PhraseOrder;
import com.storyteam.storytelling.script.performer.entity.look.AbstractLookController;
import com.storyteam.storytelling.script.performer.entity.look.DefaultLookController;
import com.storyteam.storytelling.script.performer.entity.look.RestrictedLookController;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.script.story.characters.mobs.SpecialCharacter;
import com.storyteam.storytelling.system._core.morph.performer.IMorphPerformerBuilder;
import com.storyteam.storytelling.system._core.st_cutscene.InterpolationType;
import com.storyteam.storytelling.system._core.st_cutscene.STCutsceneData;
import com.storyteam.storytelling.system._core.st_cutscene.SplineType;
import com.storyteam.storytelling.system.char_icon.performer.AddRenderedIcon;
import com.storyteam.storytelling.system.char_icon.performer.RemoveRenderedIcon;
import com.storyteam.storytelling.system.clothes.Outfit;
import com.storyteam.storytelling.system.dialog.Dialog;
import com.storyteam.storytelling.system.emotions.BubbleType;
import com.storyteam.storytelling.system.emotions.Emotion;
import com.storyteam.storytelling.system.emotions.performer.AddRenderedEmotion;
import com.storyteam.storytelling.system.emotions.performer.ClearRenderedEmotion;
import com.storyteam.storytelling.system.emotions.performer.SetRenderedEmotion;
import com.storyteam.storytelling.system.fight.mob_drop.MobDropUtil;
import com.storyteam.storytelling.system.persona.Persona;
import com.storyteam.storytelling.system.persona.PersonaHandler;
import com.storyteam.storytelling.system.shaders.ShaderModifier;
import com.storyteam.storytelling.system.shaders.ShaderSystem;
import com.storyteam.storytelling.system.simulator.main_page.MainTabType;
import com.storyteam.storytelling.system.simulator.main_page.performer.SetSimulatorBlocking;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.performer.IFragmentPerformerBuilder;
import com.storyteam.storytelling.system.sky_eye.performer.SetSkyEyeLookTarget;
import com.storyteam.storytelling.system.sky_eye.performer.SetSkyEyePosition;
import com.storyteam.storytelling.system.sounds.SoundUtils;
import com.storyteam.storytelling.system.time.performer.ITimePerformerBuilder;
import com.storyteam.storytelling.util.Color;
import com.storyteam.storytelling.util.ParticleHelper;
import com.storyteam.storytelling.util.PickupList;
import com.storyteam.storytelling.util.PositionParser;
import com.storyteam.storytelling.util.ScriptUses;
import com.storyteam.storytelling.util.ScriptUtils;
import com.storyteam.storytelling.util.StoryTicker;
import com.storyteam.storytelling.util.Title;
import com.storyteam.storytelling.util.data.EntityContext;
import com.storyteam.storytelling.util.world.Location;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import lombok.Generated;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import org.jetbrains.annotations.Blocking;
import org.jetbrains.annotations.NonBlocking;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammeranims.api.animation.IAnimationSource;
import org.zeith.hammeranims.api.animsys.ConfiguredAnimation;
import org.zeith.hammeranims.core.init.DefaultsHA;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.java.consumers.Consumer2;
import org.zeith.hammerlib.util.mcf.Resources;

public class ScriptPerformerBuilder
   implements PerformerBuilder<ScriptPerformer, ScriptPerformerBuilder>,
   ITimePerformerBuilder,
   IMorphPerformerBuilder,
   IFragmentPerformerBuilder {
   protected final ScriptPerformer performer;
   private PerformerAction<?> lastAction;
   private boolean built = false;
   private float alphaTicker = 0.0F;

   public ScriptPerformerBuilder(ScriptInstance script) {
      this.performer = new ScriptPerformer(script);
   }

   protected ScriptPerformerBuilder(ScriptInstance script, ScriptPerformer performer) {
      this.performer = new ScriptPerformer(script);
   }

   public static ScriptPerformerBuilder builder(ScriptInstance script) {
      return new ScriptPerformerBuilder(script);
   }

   private void checkBuilt() {
      if (this.built) {
         throw new IllegalStateException("Actions can not be added or flagged after \"build\" method invocation.");
      }
   }

   public ScriptPerformerBuilder append(Function<ScriptPerformer, PerformerAction<ScriptPerformer>> actionFactory) {
      this.checkBuilt();
      PerformerAction<ScriptPerformer> action = actionFactory.apply(this.performer);
      this.lastAction = action;
      this.performer.addAction(action);
      return this;
   }

   public ScriptPerformerBuilder appendModifier(Function<ScriptPerformer, PerformerModifier<ScriptPerformer>> actionFactory) {
      this.checkBuilt();
      PerformerModifier<ScriptPerformer> modifier = actionFactory.apply(this.performer);
      this.performer.applyModifier(modifier);
      return this;
   }

   public ScriptPerformerBuilder createTicker(String id, StoryTicker ticker) {
      return this.thenHandleState(b -> {
         if (b) {
            this.performer.script.addTicker(id, ticker);
         } else {
            this.performer.script.removeTicker(id);
         }
      });
   }

   public ScriptPerformerBuilder deleteTicker(String id) {
      return this.thenPerform(() -> this.performer.script.removeTicker(id));
   }

   public ScriptPerformerBuilder createParallelPerformer(final String id, final ScriptPerformer performer) {
      return this.thenHandleState(new HandleStateAction.StateHandler() {
         @Override
         public void onPassed() {
            performer.script.addParallelPerformer(id, performer, ScriptPerformerBuilder.this.getPerformer());
         }

         @Override
         public void onRewind() {
            performer.script.deleteParallelPerformer(id);
         }

         @Override
         public void globalFastForward() {
            for (PerformerAction<ScriptPerformer> action : performer.getActions()) {
               action.globalFastForward();
            }
         }
      });
   }

   public ScriptPerformerBuilder tdCutscene(STCutsceneData data) {
      return this.append(p -> new TDCutsceneTpOrInitAction<>(p, data));
   }

   public ScriptPerformerBuilder tdCutscene(String coord) {
      return this.append(
         p -> new TDCutsceneTpOrInitAction<>(
               p, new STCutsceneData(1, List.of(new Location(coord)), SplineType.LINEAR, InterpolationType.LINEAR, InterpolationType.LINEAR)
            )
      );
   }

   public ScriptPerformerBuilder tdCutscene(Location coord) {
      return this.append(
         p -> new TDCutsceneTpOrInitAction<>(p, new STCutsceneData(1, List.of(coord), SplineType.LINEAR, InterpolationType.LINEAR, InterpolationType.LINEAR))
      );
   }

   public ScriptPerformerBuilder tdCutscene(Supplier<Location> coord) {
      return this.append(
         p -> new TDCutsceneTpOrInitAction<>(
               p, new STCutsceneData(1, List.of(coord.get()), SplineType.LINEAR, InterpolationType.LINEAR, InterpolationType.LINEAR)
            )
      );
   }

   public ScriptPerformerBuilder tdCutscene(
      int time, SplineType sType, InterpolationType distInterpolation, InterpolationType lookInterpolation, String... coord
   ) {
      return this.append(
         p -> new TDCutsceneTpOrInitAction<>(p, new STCutsceneData(time, Location.parseLocations(coord), sType, distInterpolation, lookInterpolation))
      );
   }

   public ScriptPerformerBuilder tdCutscene(int time, SplineType sType, String... coord) {
      return this.append(
         p -> new TDCutsceneTpOrInitAction<>(
               p, new STCutsceneData(time, Location.parseLocations(coord), sType, InterpolationType.EASE_IN_OUT_QUAD, InterpolationType.EASE_IN_OUT_QUAD)
            )
      );
   }

   public ScriptPerformerBuilder tdCutscene(int time, SplineType sType, InterpolationType distInterpolation, String... coord) {
      return this.append(
         p -> new TDCutsceneTpOrInitAction<>(
               p, new STCutsceneData(time, Location.parseLocations(coord), sType, distInterpolation, InterpolationType.EASE_IN_OUT_QUAD)
            )
      );
   }

   public ScriptPerformerBuilder tdCutscene(int time, String... coord) {
      return this.append(
         p -> new TDCutsceneTpOrInitAction<>(
               p,
               new STCutsceneData(
                  time, Location.parseLocations(coord), SplineType.CATMULLROM, InterpolationType.EASE_IN_OUT_QUAD, InterpolationType.EASE_IN_OUT_QUAD
               )
            )
      );
   }

   public ScriptPerformerBuilder tdCutsceneMoveCamera(
      int time, SplineType sType, InterpolationType distInterpolation, InterpolationType lookInterpolation, String... coord
   ) {
      return this.append(
         p -> new TDCutsceneMoveCameraAction<>(p, new STCutsceneData(time, Location.parseLocations(coord), sType, distInterpolation, lookInterpolation))
      );
   }

   public ScriptPerformerBuilder tdCutsceneMoveCamera(int time, SplineType sType, String... coord) {
      return this.append(
         p -> new TDCutsceneMoveCameraAction<>(
               p, new STCutsceneData(time, Location.parseLocations(coord), sType, InterpolationType.EASE_IN_OUT_QUAD, InterpolationType.EASE_IN_OUT_QUAD)
            )
      );
   }

   public ScriptPerformerBuilder tdCutsceneMoveCamera(int time, SplineType sType, InterpolationType distInterpolation, String... coord) {
      return this.append(
         p -> new TDCutsceneMoveCameraAction<>(
               p, new STCutsceneData(time, Location.parseLocations(coord), sType, distInterpolation, InterpolationType.EASE_IN_OUT_QUAD)
            )
      );
   }

   public ScriptPerformerBuilder tdCutsceneMoveCamera(int time, String... coord) {
      return this.append(
         p -> new TDCutsceneMoveCameraAction<>(
               p,
               new STCutsceneData(
                  time, Location.parseLocations(coord), SplineType.CATMULLROM, InterpolationType.EASE_IN_OUT_QUAD, InterpolationType.EASE_IN_OUT_QUAD
               )
            )
      );
   }

   public ScriptPerformerBuilder tdCutsceneMoveCamera(int time, Location... locations) {
      return this.append(
         p -> new TDCutsceneMoveCameraAction<>(
               p, new STCutsceneData(time, List.of(locations), SplineType.CATMULLROM, InterpolationType.EASE_IN_OUT_QUAD, InterpolationType.EASE_IN_OUT_QUAD)
            )
      );
   }

   public ScriptPerformerBuilder endTdCutscene() {
      return this.append(TDCutsceneEnd::new);
   }

   public ScriptPerformerBuilder thenAddEffect(MobEffectInstance effect) {
      return this.append(performer -> new EffectAction(performer, effect));
   }

   public ScriptPerformerBuilder removeParallelPerformer(String id) {
      return this.thenPerform(() -> this.performer.script.deleteParallelPerformer(id));
   }

   public <T extends AnimatedEntity> ScriptPerformerBuilder enableFight(Character<T> character, boolean enable) {
      return this.thenUseEntity(character, animated -> animated.setVulnerable(enable));
   }

   public <T extends AnimatedEntity> ScriptPerformerBuilder catmullRomMovement(Character<T> character, int time, String... locations) {
      return this.append(performer -> new CatmullRomMoveAction<>(performer, character, time, Location.parseLocationArray(locations)));
   }

   public <T extends AnimatedEntity> ScriptPerformerBuilder setMaxHealth(Character<T> character, int maxHealth) {
      return this.setMaxHealth(character, (float)maxHealth);
   }

   public <T extends AnimatedEntity> ScriptPerformerBuilder setMaxHealth(Character<T> character, float maxHealth) {
      return this.thenUseEntity(character, animated -> animated.setMaxHealth(maxHealth));
   }

   public <T extends AnimatedEntity> ScriptPerformerBuilder startFight(Character<T> character, IAttackChain<T> chain) {
      return this.append(performer -> new FightPerformerAction<>(performer, character, chain));
   }

   public <T extends AnimatedEntity> ScriptPerformerBuilder startFight(Character<T> character, boolean deathPosition, IAttackChain<T> chain) {
      return this.append(performer -> new FightPerformerAction<>(performer, character, deathPosition, chain));
   }

   public ScriptPerformerBuilder sendWebMessage(String message, boolean isDebug) {
      return this.append(performer -> new SendWebMessageAction(performer, message, isDebug));
   }

   public ScriptPerformerBuilder sendWebWarning(String message) {
      return this.append(performer -> new SendWebMessageAction(performer, message, false));
   }

   public ScriptPerformerBuilder sendWebWarningRed(String message) {
      return this.sendWebWarning("<span style=\"color: red\">%s</span>".formatted(message));
   }

   public ScriptPerformerBuilder sendWebWarningOrange(String message) {
      return this.sendWebWarning("<span style=\"color: orange\">%s</span>".formatted(message));
   }

   public ScriptPerformerBuilder removeWebMessage(String id) {
      return this.append(performer -> new RemoveWebMessageAction(performer, id));
   }

   public ScriptPerformerBuilder clearWebMessages() {
      return this.append(ClearWebCategoryAction::new);
   }

   public ScriptPerformerBuilder ensureScriptActive(int... ids) {
      return this.append(performer -> new EnsureScriptEnabledAction(performer, ids));
   }

   public ScriptPerformerBuilder ensureScriptActive(boolean forceStart, int... ids) {
      return this.append(performer -> new EnsureScriptEnabledAction(performer, forceStart, ids));
   }

   public ScriptPerformerBuilder ensureScriptActive(ScriptCategory... categories) {
      return this.ensureScriptActive(Arrays.stream(categories).flatMapToInt(category -> Arrays.stream(category.getScripts())).toArray());
   }

   public ScriptPerformerBuilder ensureScriptInactive(int... ids) {
      return this.append(performer -> new EnsureScriptDisabledAction(performer, ids));
   }

   public ScriptPerformerBuilder ensureScriptInactive(ScriptCategory... categories) {
      return this.ensureScriptInactive(Arrays.stream(categories).flatMapToInt(category -> Arrays.stream(category.getScripts())).toArray());
   }

   public ScriptPerformerBuilder ensureScriptsSet(int... ids) {
      return this.append(performer -> new EnsureScriptsSetAction(performer, ids));
   }

   public ScriptPerformerBuilder ensureScriptsSet(ScriptCategory... categories) {
      return this.ensureScriptsSet(Arrays.stream(categories).flatMapToInt(category -> Arrays.stream(category.getScripts())).toArray());
   }

   public ScriptPerformerBuilder thenWaitForFlagAndConsume(String flag) {
      return this.append(performer -> new ConsumeFlagAction(performer, flag, true));
   }

   public ScriptPerformerBuilder thenWaitForFlag(String flag) {
      return this.append(performer -> new ConsumeFlagAction(performer, flag, false));
   }

   public ScriptPerformerBuilder thenWaitForNoFlag(String flag) {
      return this.append(performer -> new AbsentFlagAction(performer, flag));
   }

   public ScriptPerformerBuilder thenWaitForTrigger() {
      return this.append(performer -> new TriggerAction(performer, 100));
   }

   public ScriptPerformerBuilder thenWaitForTrigger(int delay) {
      return this.append(performer -> new TriggerAction(performer, delay));
   }

   public ScriptPerformerBuilder screenColor(int color, int inTime, int time, int outTime) {
      return this.append(performer -> new ScreenColorAction(performer, color, inTime, time, outTime));
   }

   public ScriptPerformerBuilder blackout(int time) {
      return this.append(performer -> new ScreenColorAction(performer, 0, 0, time, 20));
   }

   public ScriptPerformerBuilder blackoutBelowChat(int time) {
      return this.append(performer -> new ScreenColorBelowChatAction(performer, 0, 0, time, 20));
   }

   public ScriptPerformerBuilder close_eyes(int close, int time, int open) {
      return this.append(performer -> new BlinkAction(performer, 0, close, time, open));
   }

   public ScriptPerformerBuilder flash(int time) {
      return this.append(performer -> new ScreenColorAction(performer, 16777215, 20, time, 20));
   }

   public ScriptPerformerBuilder importBuilding(String name) {
      return this.append(performer -> new ImportBuildingAction(performer, name));
   }

   public ScriptPerformerBuilder importBuilding(String name, boolean fastForward) {
      return this.append(performer -> new ImportBuildingAction(performer, name, fastForward));
   }

   public ScriptPerformerBuilder thenWaitForF8() {
      return this.append(F8PressAction::new);
   }

   public ScriptPerformerBuilder thenWaitForF9() {
      return this.append(F9PressAction::new);
   }

   public ScriptPerformerBuilder thenWaitForF10() {
      return this.append(F10PressAction::new);
   }

   public ScriptPerformerBuilder thenWaitForKeyPress(int key) {
      return this.append(performer -> new KeyPressAction(performer, key));
   }

   public ScriptPerformerBuilder thenTitle(Title title) {
      return this.append(performer -> new TitleAction(performer, title));
   }

   public ScriptPerformerBuilder dialog(Dialog dialog, boolean openScreen) {
      return this.append(performer -> new DialogAction(performer, dialog, openScreen));
   }

   public ScriptPerformerBuilder dialog(Dialog dialog) {
      return this.append(performer -> new DialogAction(performer, dialog, true));
   }

   public ScriptPerformerBuilder clearChat() {
      return this.append(ClearChatAction::new);
   }

   @Deprecated(
      forRemoval = true
   )
   public <T extends Entity> ScriptPerformerBuilder removeEntity(Character<T> character) {
      return this.append(performer -> new RemoveEntityAction<>(performer, character));
   }

   public <T extends Entity> ScriptPerformerBuilder positionEntity(Character<T> character, Supplier<Location> location) {
      return this.append(performer -> new PositionEntityAction<>(performer, character, location));
   }

   public <T extends Entity> ScriptPerformerBuilder positionEntity(Character<T> character, String location) {
      RuntimeException source = new RuntimeException();
      return this.append(
         performer -> new PositionEntityAction<>(performer, character, Cast.constant(PositionParser.parseLocationWithRotationOrException(location, source)))
      );
   }

   public <T extends Entity> ScriptPerformerBuilder positionEntity(Character<T> character, String location, boolean hasFastForward) {
      RuntimeException source = new RuntimeException();
      return this.append(
         performer -> new PositionEntityAction<>(
               performer, character, Cast.constant(PositionParser.parseLocationWithRotationOrException(location, source)), hasFastForward
            )
      );
   }

   public <T extends Entity> ScriptPerformerBuilder repositionEntity(Character<T> character, String ifNotFound) {
      RuntimeException source = new RuntimeException();
      return this.append(
         performer -> new RepositionEntityAction<>(performer, character, Cast.constant(PositionParser.parseLocationWithRotationOrException(ifNotFound, source)))
      );
   }

   public <T extends Entity> ScriptPerformerBuilder positionOnceBehindPlayer(Character<T> character, Supplier<Location> location) {
      return this.append(performer -> new PositionOnceBehindPlayerAction<>(performer, character, location));
   }

   public <T extends Entity> ScriptPerformerBuilder positionOnceBehindPlayer(Character<T> character, String location) {
      RuntimeException source = new RuntimeException();
      return this.append(
         performer -> new PositionOnceBehindPlayerAction<>(
               performer, character, Cast.constant(PositionParser.parseLocationWithRotationOrException(location, source))
            )
      );
   }

   public <T extends Entity> ScriptPerformerBuilder positionEntityNearPlayer(Character<T> character, int radius, int yRange) {
      return this.append(performer -> new PositionEntityNearPlayerAction<>(performer, character, radius, yRange, false));
   }

   public <T extends Entity> ScriptPerformerBuilder positionEntityBehindPlayer(Character<T> character, int radius, int yRange) {
      return this.append(performer -> new PositionEntityNearPlayerAction<>(performer, character, radius, yRange, true));
   }

   public <T extends Entity> ScriptPerformerBuilder positionEntityRelativeToPlayer(
      Character<T> character, int distance, int radiusOffset, int yRange, boolean isFront
   ) {
      return this.append(performer -> new EntityPositionRelativeToPlayer<>(performer, character, (double)distance, radiusOffset, yRange, isFront, () -> null));
   }

   public <T extends Entity> ScriptPerformerBuilder positionEntityRelativeToPlayer(
      Character<T> character, int distance, int radiusOffset, int yRange, boolean isFront, Supplier<Location> location
   ) {
      return this.append(performer -> new EntityPositionRelativeToPlayer<>(performer, character, (double)distance, radiusOffset, yRange, isFront, location));
   }

   public <T extends Entity> ScriptPerformerBuilder initCharacter(Character<T> character) {
      return this.append(performer -> new CharacterInitializationAction<>(performer, character));
   }

   public <T extends Entity> ScriptPerformerBuilder initCharacter(Character<T> character, ScriptPerformer repositoryContainer) {
      return this.append(performer -> new CharacterInitializationAction<>(repositoryContainer, character));
   }

   public <T extends Entity> ScriptPerformerBuilder entitySit(Character<T> character) {
      return this.append(performer -> new EntitySitAction<>(performer, character));
   }

   public <T extends HumanoidEntity> ScriptPerformerBuilder entitySit(Character<T> character, String cord, boolean blockBody) {
      return this.positionEntity(character, cord)
         .entitySit(character)
         .createParallelPerformer(
            character.getReferenceId() + "_init",
            new ScriptPerformerBuilder(this.performer.script)
               .thenWaitUntil(() -> this.performer.script.optional(character).map(e -> e.isAlive() && e.level().isLoaded(e.blockPosition())).orElse(false))
               .blockBodyRotation(character, blockBody)
               .build()
         );
   }

   public <T extends HumanoidEntity> ScriptPerformerBuilder entitySit(Character<T> character, String cord) {
      return this.positionEntity(character, cord)
         .entitySit(character)
         .createParallelPerformer(
            character.getReferenceId() + "_init",
            new ScriptPerformerBuilder(this.performer.script)
               .thenWaitUntil(() -> this.performer.script.optional(character).map(e -> e.isAlive() && e.level().isLoaded(e.blockPosition())).orElse(false))
               .blockBodyRotation(character, true)
               .build()
         );
   }

   public <T extends Entity> ScriptPerformerBuilder entityStopSit(Character<T> character) {
      return this.thenClearEntityGoal(character, BehaviourType.MOVEMENT).thenHandleEntityState(character, Entity::stopRiding);
   }

   public <T extends Entity> ScriptPerformerBuilder entityMoveSpeed(Character<T> character, double moveSpeed) {
      return this.append(performer -> new EntitySetSpeedAction<>(performer, character, moveSpeed));
   }

   public <T extends Entity> ScriptPerformerBuilder entityLookAtFromRot(Character<T> character, String angle, boolean waitForEnd) {
      return this.entityLookAtFromRot(character, () -> new Location(angle), 100.0, waitForEnd);
   }

   public <T extends Entity> ScriptPerformerBuilder entityLookAtFromRot(Character<T> character, String angle, boolean waitForEnd, boolean isQuick) {
      return this.entityLookAtFromRot(character, () -> new Location(angle), 100.0, waitForEnd, isQuick);
   }

   public <T extends Entity> ScriptPerformerBuilder entityLookAtFromRot(Character<T> character, String angle, double range) {
      return this.entityLookAtFromRot(character, () -> new Location(angle), range, false);
   }

   public <T extends Entity> ScriptPerformerBuilder entityLookAtFromRot(Character<T> character, String angle, double range, boolean waitForEnd) {
      return this.entityLookAtFromRot(character, () -> new Location(angle), range, waitForEnd);
   }

   public <T extends Entity> ScriptPerformerBuilder entityLookAtFromRot(
      Character<T> character, Supplier<Location> angle, double range, boolean waitForEnd, boolean isQuick
   ) {
      Location loc = angle.get();
      float f = loc.getPitch() * (float) (Math.PI / 180.0);
      float f1 = -loc.getYaw() * (float) (Math.PI / 180.0);
      float f2 = Mth.cos(f1);
      float f3 = Mth.sin(f1);
      float f4 = Mth.cos(f);
      float f5 = Mth.sin(f);
      Location vec = new Location((double)(f3 * f4), (double)(-f5), (double)(f2 * f4));
      Location target = loc.add(vec.scale(range));
      return this.append(performer -> new EntityLookAtPosAction<>(performer, character, target.toCommandString(), isQuick, waitForEnd));
   }

   public <T extends Entity> ScriptPerformerBuilder entityLookAtFromRot(Character<T> character, Supplier<Location> angle, double range, boolean waitForEnd) {
      return this.entityLookAtFromRot(character, angle, range, waitForEnd, false);
   }

   public <T extends Entity> ScriptPerformerBuilder entityLookAtFromRot(Character<T> character, String angle) {
      return this.entityLookAtFromRot(character, angle, 100.0);
   }

   public <T extends Entity> ScriptPerformerBuilder entityLookAtNearest(Character<T> character, double range, boolean storytellingTarget, boolean waitForEnd) {
      return this.append(performer -> new EntityLookAtNearest<>(performer, character, range, storytellingTarget, waitForEnd));
   }

   public <T extends Entity> ScriptPerformerBuilder entityLookAt(Character<T> character, String target, boolean waitForEnd, boolean isQuick) {
      return this.append(performer -> new EntityLookAtPosAction<>(performer, character, target, isQuick, waitForEnd));
   }

   public <T extends Entity> ScriptPerformerBuilder entityLookAt(Character<T> character, String target, boolean waitForEnd) {
      return this.append(performer -> new EntityLookAtPosAction<>(performer, character, target, false, waitForEnd));
   }

   public <T extends Entity> ScriptPerformerBuilder entityLookAt(Character<T> character, String target) {
      return this.append(performer -> new EntityLookAtPosAction<>(performer, character, target, false, false));
   }

   public <T extends Entity, U extends Entity> ScriptPerformerBuilder entityLookAt(Character<T> character, Character<U> target, boolean waitForEnd) {
      return this.append(performer -> new EntityLookAtCharacterAction<>(performer, character, target, false, waitForEnd));
   }

   public <T extends Entity, U extends Entity> ScriptPerformerBuilder entityLookAt(
      Character<T> character, Character<U> target, boolean isQuick, boolean waitForEnd
   ) {
      return this.append(performer -> new EntityLookAtCharacterAction<>(performer, character, target, isQuick, waitForEnd));
   }

   public <T extends Entity> ScriptPerformerBuilder entityLookAt(Character<T> character, Supplier<Location> target, boolean waitForEnd, boolean isQuick) {
      return this.append(performer -> new EntityLookAtLocAction<>(performer, character, target, waitForEnd, isQuick));
   }

   public <T extends Entity> ScriptPerformerBuilder entityLookAt(Character<T> character, Supplier<Location> target, boolean waitForEnd) {
      return this.append(performer -> new EntityLookAtLocAction<>(performer, character, target, waitForEnd, false));
   }

   public <T extends Entity> ScriptPerformerBuilder entityLookAt(Character<T> character, Supplier<Location> target) {
      return this.append(performer -> new EntityLookAtLocAction<>(performer, character, target, false, false));
   }

   public <T extends Entity, U extends Entity> ScriptPerformerBuilder entityLookAt(Character<T> character, Character<U> target) {
      return this.append(performer -> new EntityLookAtCharacterAction<>(performer, character, target, false, false));
   }

   public <T extends Entity> ScriptPerformerBuilder setEntityLookController(Character<T> character, AbstractLookController<T> controller) {
      return this.thenPerform(() -> this.performer.script.getRepository().get(character).getData().setLookController(controller));
   }

   public <T extends LivingEntity> ScriptPerformerBuilder setRestrictedLookController(Character<T> character) {
      return this.setEntityLookController(character, new RestrictedLookController<>());
   }

   public <T extends LivingEntity> ScriptPerformerBuilder setRestrictedLookController(Character<T> character, double maxBodyAngle, double maxHeadAngle) {
      return this.setEntityLookController(character, new RestrictedLookController<>(maxBodyAngle, maxHeadAngle));
   }

   public <T extends LivingEntity> ScriptPerformerBuilder setRestrictedLookController(
      Character<T> character, double maxBodyAngle, double maxHeadAngle, double rotationSpeed
   ) {
      return this.setEntityLookController(character, new RestrictedLookController<>(maxBodyAngle, maxHeadAngle, rotationSpeed));
   }

   public <T extends LivingEntity> ScriptPerformerBuilder setRestrictedLookController(
      Character<T> character, double maxBodyAngle, double maxHeadAngle, double rotationSpeed, double initialAngle
   ) {
      return this.setEntityLookController(character, new RestrictedLookController<>(maxBodyAngle, maxHeadAngle, rotationSpeed, initialAngle));
   }

   public <T extends Entity> ScriptPerformerBuilder setDefaultLookController(Character<T> character) {
      return this.setEntityLookController(character, new DefaultLookController<>());
   }

   public <T extends Entity> ScriptPerformerBuilder startSpectating(Character<T> character) {
      return this.append(performer -> new SpectateEntityAction<>(performer, character));
   }

   public <T extends Entity> ScriptPerformerBuilder stopSpectating() {
      return this.append(SpectateStopEntityAction::new);
   }

   public <T extends Mob> ScriptPerformerBuilder entityTypeOverride(Character<T> character, @Nullable EntityType<? extends T> typeOverride) {
      return this.append(performer -> new EntityTypeOverrideAction(performer, character, typeOverride));
   }

   public <T extends Mob> ScriptPerformerBuilder entityTextureOverride(Character<T> character, ResourceLocation texture) {
      return this.append(performer -> new EntitySetTextureAction(performer, character, texture));
   }

   public <T extends Mob> ScriptPerformerBuilder entityTextureOverride(Character<T> character, String texture) {
      ResourceLocation loc = texture == null ? null : (texture.contains(":") ? Resources.location(texture) : St.id(texture));
      return this.append(performer -> new EntitySetTextureAction(performer, character, loc));
   }

   public <T extends Mob> ScriptPerformerBuilder entityNameOverride(Character<T> character, Component name) {
      return this.append(performer -> new EntitySetDisplayNameAction(performer, character, () -> name));
   }

   public <T extends Mob> ScriptPerformerBuilder entityNameOverride(Character<T> character, String name) {
      return this.append(performer -> new EntitySetDisplayNameAction(performer, character, () -> Component.literal(name)));
   }

   public <T extends Mob> ScriptPerformerBuilder entityNameOverride(Character<T> character, @NotNull Supplier<String> name) {
      return this.append(performer -> new EntitySetDisplayNameAction(performer, character, () -> Component.literal(name.get())));
   }

   public <T extends Entity> ScriptPerformerBuilder freeEntity(Character<T> character, boolean deleteFromRepo) {
      return this.append(performer -> new FreeEntityAction(performer, List.of(character), deleteFromRepo));
   }

   public <T extends Entity> ScriptPerformerBuilder freeEntity(Character<T> character) {
      return this.append(performer -> new FreeEntityAction(performer, List.of(character), true));
   }

   @SafeVarargs
   public final <T extends Entity> ScriptPerformerBuilder freeEntities(boolean deleteFromRepo, Character<? extends Entity>... characters) {
      return this.append(performer -> new FreeEntityAction(performer, Arrays.stream(characters).toList(), deleteFromRepo));
   }

   @SafeVarargs
   public final <T extends Entity> ScriptPerformerBuilder freeEntities(Character<? extends Entity>... characters) {
      return this.freeEntities(true, characters);
   }

   public <T extends Entity> ScriptPerformerBuilder setUpEntityTracking(Character<T> character, boolean startTracking) {
      return this.append(performer -> new StartEntityTracking<>(performer, character, startTracking));
   }

   public <T extends Entity> ScriptPerformerBuilder thenEntityMove(Character<T> character, double speed, String position) {
      return position.equals("player")
         ? this.append(performer -> new EntityMoveAction<>(performer, character, speed, () -> new Location(ScriptUtils.getPlayer()), true))
         : this.append(performer -> new EntityMoveAction<>(performer, character, speed, () -> new Location(position), true));
   }

   public <T extends Entity> ScriptPerformerBuilder thenEntityMove(Character<T> character, double speed, Supplier<Location> position) {
      return this.append(performer -> new EntityMoveAction<>(performer, character, speed, position, true));
   }

   public <T extends Entity> ScriptPerformerBuilder thenEntityMove(Character<T> character, double speed, String position, boolean rotate) {
      Location loc;
      if (position.equals("player")) {
         loc = new Location(ScriptUtils.getPlayer());
      } else {
         loc = new Location(position);
      }

      return this.append(performer -> new EntityMoveAction<>(performer, character, speed, () -> loc, rotate));
   }

   public <T extends Entity> ScriptPerformerBuilder thenEntityMoveNoPhysics(Character<T> character, double speed, String position, boolean rotate) {
      return position.equals("player")
         ? this.append(performer -> new EntityMoveNoPhysicsAction<>(performer, character, speed, () -> new Location(ScriptUtils.getPlayer()), rotate))
         : this.append(performer -> new EntityMoveNoPhysicsAction<>(performer, character, speed, () -> new Location(position), rotate));
   }

   public <T extends Entity> ScriptPerformerBuilder thenEntityMoveNoPhysics(Character<T> character, double speed, Supplier<Location> position, boolean rotate) {
      return this.append(performer -> new EntityMoveNoPhysicsAction<>(performer, character, speed, position::get, rotate));
   }

   public <T extends AnimatedEntity> ScriptPerformerBuilder sit(Character<T> character, double speed, String position) {
      return this.overridePredefinedAnimation(character, (Supplier<ConfiguredAnimation>)(() -> {
            if (character.repositoryEntity() == null) {
               return null;
            } else {
               AnimConfigurationWrapper animConfigurationWrapper = ((AnimatedEntity)character.repositoryEntity()).getWrapper();
               IDefaultAnimConfiguration iDefaultAnimConfiguration = animConfigurationWrapper.getConfiguration();
               if (iDefaultAnimConfiguration != null) {
                  List<ConfiguredAnimation> idleList = iDefaultAnimConfiguration.getAnimations(MovementActionType.IDLE);
                  if (!idleList.isEmpty()) {
                     Random rnd = new Random();
                     return idleList.get(rnd.nextInt(idleList.size()));
                  }
               }

               return null;
            }
         }))
         .blockAnimWalkingBodyRotation(character, true)
         .animation(character, 7, Animations.STAY_TO_SIT.configure().transitionTime(0.4F).important())
         .thenEntityMoveNoPhysics(character, speed, position, false)
         .entitySit(character)
         .removeOverridePredefined(character)
         .blockAnimWalkingBodyRotation(character, false);
   }

   public <T extends AnimatedEntity> ScriptPerformerBuilder sit(Character<T> character, double speed, String position, ConfiguredAnimation anim) {
      return this.overridePredefinedAnimation(character, (Supplier<ConfiguredAnimation>)(() -> {
            if (character.repositoryEntity() == null) {
               return null;
            } else {
               AnimConfigurationWrapper animConfigurationWrapper = ((AnimatedEntity)character.repositoryEntity()).getWrapper();
               IDefaultAnimConfiguration iDefaultAnimConfiguration = animConfigurationWrapper.getConfiguration();
               if (iDefaultAnimConfiguration != null) {
                  List<ConfiguredAnimation> idleList = iDefaultAnimConfiguration.getAnimations(MovementActionType.IDLE);
                  if (!idleList.isEmpty()) {
                     Random rnd = new Random();
                     return idleList.get(rnd.nextInt(idleList.size()));
                  }
               }

               return null;
            }
         }))
         .blockAnimWalkingBodyRotation(character, true)
         .animation(character, 7, anim.transitionTime(0.4F).important())
         .thenEntityMoveNoPhysics(character, speed, position, false)
         .entitySit(character)
         .removeOverridePredefined(character)
         .blockAnimWalkingBodyRotation(character, false);
   }

   public <T extends AnimatedEntity> ScriptPerformerBuilder stay(Character<T> character, double speed, String position) {
      return this.overridePredefinedAnimation(character, (Supplier<ConfiguredAnimation>)(() -> {
            if (character.repositoryEntity() == null) {
               return null;
            } else {
               AnimConfigurationWrapper animConfigurationWrapper = ((AnimatedEntity)character.repositoryEntity()).getWrapper();
               IDefaultAnimConfiguration iDefaultAnimConfiguration = animConfigurationWrapper.getConfiguration();
               if (iDefaultAnimConfiguration != null) {
                  List<ConfiguredAnimation> idleList = iDefaultAnimConfiguration.getAnimations(MovementActionType.IDLE);
                  if (!idleList.isEmpty()) {
                     Random rnd = new Random();
                     return idleList.get(rnd.nextInt(idleList.size()));
                  }
               }

               return null;
            }
         }))
         .blockAnimWalkingBodyRotation(character, true)
         .animation(character, 7, Animations.SIT_TO_STAY.configure().transitionTime(0.4F).important())
         .thenEntityMoveNoPhysics(character, speed, position, false)
         .removeOverridePredefined(character)
         .blockAnimWalkingBodyRotation(character, false);
   }

   public <T extends AnimatedEntity> ScriptPerformerBuilder stay(Character<T> character, double speed, String position, ConfiguredAnimation anim) {
      return this.overridePredefinedAnimation(character, (Supplier<ConfiguredAnimation>)(() -> {
            if (character.repositoryEntity() == null) {
               return null;
            } else {
               AnimConfigurationWrapper animConfigurationWrapper = ((AnimatedEntity)character.repositoryEntity()).getWrapper();
               IDefaultAnimConfiguration iDefaultAnimConfiguration = animConfigurationWrapper.getConfiguration();
               if (iDefaultAnimConfiguration != null) {
                  List<ConfiguredAnimation> idleList = iDefaultAnimConfiguration.getAnimations(MovementActionType.IDLE);
                  if (!idleList.isEmpty()) {
                     Random rnd = new Random();
                     return idleList.get(rnd.nextInt(idleList.size()));
                  }
               }

               return null;
            }
         }))
         .blockAnimWalkingBodyRotation(character, true)
         .animation(character, 7, anim.transitionTime(0.4F).important())
         .thenEntityMoveNoPhysics(character, speed, position, false)
         .removeOverridePredefined(character)
         .blockAnimWalkingBodyRotation(character, false);
   }

   public <T extends Entity> ScriptPerformerBuilder thenEntityMove(Character<T> character, double speed, Supplier<Location> position, boolean rotate) {
      return this.append(performer -> new EntityMoveAction<>(performer, character, speed, position, rotate));
   }

   public <T extends Mob> ScriptPerformerBuilder thenEntityPath(Character<T> character, double speed, String... poses) {
      List<Location> locations = Location.parseLocations(poses);
      return this.append(performer -> new EntityPathfinderAction<>(performer, character, speed, () -> locations));
   }

   public <T extends Mob> ScriptPerformerBuilder thenEntityPathParallel(Character<T> character, double speed, String... poses) {
      List<Location> locations = Location.parseLocations(poses);
      return this.append(performer -> new EntityPathfinderAction<>(performer, character, speed, () -> locations, true));
   }

   public <T extends Mob> ScriptPerformerBuilder thenEntityJump(Character<T> character, double speed, String pose) {
      return this.append(
         performer -> new EnitiyJumpAction<>(
               performer, character, new Vec3(speed, speed, speed), () -> PositionParser.parseLocationWithRotationOrException(pose, new RuntimeException())
            )
      );
   }

   public <T extends Mob> ScriptPerformerBuilder thenEntityJump(Character<T> character, Vec3 speed, String pose) {
      return this.append(
         performer -> new EnitiyJumpAction<>(
               performer, character, speed, () -> PositionParser.parseLocationWithRotationOrException(pose, new RuntimeException())
            )
      );
   }

   public <T extends Mob> ScriptPerformerBuilder thenEntityPath(Character<T> character, Location... poses) {
      return this.append(performer -> new EntityPathfinderAction<>(performer, character, 0.3F, () -> List.of(poses)));
   }

   @Blocking
   public <T extends Mob> ScriptPerformerBuilder thenEntityPath(Character<T> character, String... poses) {
      List<Location> locations = Location.parseLocations(poses);
      return this.append(performer -> new EntityPathfinderAction<>(performer, character, 0.3F, () -> locations));
   }

   public <T extends Mob> ScriptPerformerBuilder thenEntityPathParallel(Character<T> character, String... poses) {
      List<Location> locations = Location.parseLocations(poses);
      return this.append(performer -> new EntityPathfinderAction<>(performer, character, 0.3F, () -> locations, true));
   }

   public <T extends Mob> ScriptPerformerBuilder thenEntityPath(Character<T> character, double speed, Supplier<List<Location>> poses) {
      return this.append(performer -> new EntityPathfinderAction<>(performer, character, speed, poses));
   }

   public <T extends Mob> ScriptPerformerBuilder thenEntityPathParallel(Character<T> character, double speed, Supplier<List<Location>> poses) {
      return this.append(performer -> new EntityPathfinderAction<>(performer, character, speed, poses, true));
   }

   public <T extends Mob> ScriptPerformerBuilder thenEntityFollowEntity(Character<T> character, Character<?> toFollow, double speed) {
      return this.append(performer -> new EntityFollowEntityAction<>(performer, character, toFollow, speed, 1.5, true));
   }

   @NonBlocking
   public <T extends Mob> ScriptPerformerBuilder thenEntityFollowEntity(Character<T> character, Character<?> toFollow, double speed, double distance) {
      return this.append(performer -> new EntityFollowEntityAction<>(performer, character, toFollow, speed, distance, false));
   }

   @NonBlocking
   public <T extends Mob> ScriptPerformerBuilder thenEntityFollowEntityOnHeight(Character<T> character, double speed, double distance, int heightFollow) {
      return this.append(performer -> new EntityFollowEntitynHeightAction<>(performer, character, speed, distance, false, heightFollow));
   }

   public <T extends Mob> ScriptPerformerBuilder thenEntityFollowEntity(
      Character<T> character, Character<?> toFollow, double speed, double distance, boolean endOnArrival
   ) {
      return this.append(performer -> new EntityFollowEntityAction<>(performer, character, toFollow, speed, distance, endOnArrival));
   }

   public <T extends Mob> ScriptPerformerBuilder thenEntityPlayPath(Character<T> character, String name) {
      return this.append(performer -> new EntityPathPlayAction<>(performer, character, name, 1.0F));
   }

   public <T extends Mob> ScriptPerformerBuilder thenEntityPlayPath(Character<T> character, String name, float speedMultiplier) {
      return this.append(performer -> new EntityPathPlayAction<>(performer, character, name, speedMultiplier));
   }

   public <T extends Mob> ScriptPerformerBuilder thenEntityLoopPath(Character<T> character, double speed, Supplier<List<Location>> poses) {
      return this.append(performer -> new EntityLoopPathAction<>(performer, character, speed, poses));
   }

   public <T extends Mob> ScriptPerformerBuilder thenEntityLoopPath(Character<T> character, double speed, String... poses) {
      List<Location> locations = Location.parseLocations(poses);
      return this.append(performer -> new EntityLoopPathAction<>(performer, character, speed, () -> locations));
   }

   public <T extends PathfinderMob> ScriptPerformerBuilder thenEntityRandomWalk(Character<T> character, double speed, int maxPoints, double maxDistance) {
      return this.append(performer -> new EntityStartRandomWalkingAction<>(performer, character, speed, maxPoints, maxDistance));
   }

   public <T extends PathfinderMob> ScriptPerformerBuilder thenEntityFlyRandomWalk(
      Character<T> character, double speed, String start_pos, int minHeight, int maxHeight, int minDistance, int maxDistance
   ) {
      return this.append(
         performer -> new EntityStartRandomFlyWalkingAction<>(performer, character, speed, start_pos, minHeight, maxHeight, minDistance, maxDistance)
      );
   }

   public <T extends Mob> ScriptPerformerBuilder thenEntityFollowVector(Character<T> character, Supplier<Vec3> toFollow, double speed, double distance) {
      return this.append(performer -> new EntityFollowVectorAction<>(performer, character, toFollow, speed, distance, false));
   }

   public <T extends Mob> ScriptPerformerBuilder thenEntityFollowPlayer(Character<T> character, double speed, double minDistance, double maxDistance) {
      return this.append(performer -> new EntityFollowPlayerAction<>(performer, character, speed, minDistance, maxDistance));
   }

   public <T extends Entity> ScriptPerformerBuilder thenClearEntityGoal(Character<T> character) {
      return this.append(performer -> new ClearEntityGoalAction<>(performer, character));
   }

   public <T extends Entity> ScriptPerformerBuilder thenClearEntityGoal(Character<T> character, BehaviourType type) {
      return this.append(performer -> new ClearEntityGoalAction<>(performer, character, type));
   }

   public <T extends Entity> ScriptPerformerBuilder thenHandleEntityState(Character<T> character, Consumer<? super T> after) {
      return this.append(performer -> new HandleEntityStateAction<>(performer, character, e -> {
         }, after));
   }

   public <T extends Entity> ScriptPerformerBuilder thenHandleEntityState(Character<T> character, Consumer<? super T> before, Consumer<? super T> after) {
      return this.append(performer -> new HandleEntityStateAction<>(performer, character, before, after));
   }

   public <T extends Entity> ScriptPerformerBuilder thenHandleEntityState(Character<T> character, BiConsumer<Boolean, ? super T> ifPassed) {
      return this.append(performer -> new HandleEntityStateAction<>(performer, character, ifPassed));
   }

   public <T extends Entity> ScriptPerformerBuilder thenStaticEntityState(Character<T> character, Consumer<? super T> staticState) {
      return this.append(performer -> new HandleEntityStateAction<>(performer, character, staticState, staticState));
   }

   public <T extends Mob> ScriptPerformerBuilder setEntityGoalsEnabled(Character<T> character, boolean goals) {
      return this.append(performer -> new SetEntityGoalsEnabledAction<>(performer, character, goals));
   }

   public <T extends LivingEntity> ScriptPerformerBuilder thenEntityPickupList(Character<T> character, int amount) {
      return this.append(performer -> new EntityRandomPickUpAction(performer, character, amount));
   }

   public <T extends LivingEntity> ScriptPerformerBuilder thenEntityPickupList(Character<T> character, PickupList pickupList) {
      return this.append(performer -> new EntityPickUpItemsAction(performer, character, Cast.constant(pickupList), 1.0F));
   }

   public <T extends LivingEntity> ScriptPerformerBuilder thenEntityPickupList(Character<T> character, PickupList pickupList, float expand) {
      return this.append(performer -> new EntityPickUpItemsAction(performer, character, Cast.constant(pickupList), expand));
   }

   public <T extends LivingEntity> ScriptPerformerBuilder thenEntityPickupList(Character<T> character, Supplier<PickupList> pickupList) {
      return this.append(performer -> new EntityPickUpItemsAction(performer, character, pickupList, 1.0F));
   }

   public <T extends LivingEntity> ScriptPerformerBuilder thenEntityPickupList(Character<T> character, Supplier<PickupList> pickupList, float expand) {
      return this.append(performer -> new EntityPickUpItemsAction(performer, character, pickupList, expand));
   }

   public <T extends Mob> ScriptPerformerBuilder thenWaitCharacterNotVisible(Character<T> character) {
      return this.append(performer -> new CharacterNotVisible(performer, character));
   }

   @SafeVarargs
   public final <T extends Mob> ScriptPerformerBuilder thenWaitCharacterNotVisible(Character<T>... character) {
      return this.append(performer -> new CharacterNotVisible(performer, () -> Arrays.stream(character).toList()));
   }

   public <T extends Mob> ScriptPerformerBuilder thenWaitCharacterVisible(Character<T> character) {
      return this.thenWaitCharacterVisible(character, false);
   }

   public <T extends Mob> ScriptPerformerBuilder thenWaitCharacterVisible(Character<T> character, boolean lineOfSight) {
      return this.append(performer -> new CharacterVisibleAction(performer, character, lineOfSight));
   }

   @SafeVarargs
   public final <T extends Mob> ScriptPerformerBuilder thenWaitCharacterVisible(Character<T>... character) {
      return this.thenWaitCharacterVisible(false, character);
   }

   @SafeVarargs
   public final <T extends Mob> ScriptPerformerBuilder thenWaitCharacterVisible(boolean lineOfSight, Character<T>... character) {
      return this.append(performer -> new CharacterVisibleAction(performer, () -> Arrays.stream(character).toList(), lineOfSight));
   }

   public <T extends Mob> ScriptPerformerBuilder thenEntityFlyAway(Character<T> character, double speed, boolean randomWalk, String... path) {
      return this.append(
         performer -> new EntityFlyAwayAction<>(
               performer,
               character,
               speed,
               randomWalk,
               () -> Arrays.stream(path).flatMap(s -> Arrays.stream(s.split("\n"))).map(Location::new).collect(Collectors.toList())
            )
      );
   }

   public <T extends Mob> ScriptPerformerBuilder thenEntityFlyAway(Character<T> character, double speed, String... path) {
      return this.append(
         performer -> new EntityFlyAwayAction<>(
               performer,
               character,
               speed,
               true,
               () -> Arrays.stream(path).flatMap(s -> Arrays.stream(s.split("\n"))).map(Location::new).collect(Collectors.toList())
            )
      );
   }

   public <T extends Mob> ScriptPerformerBuilder thenEntityFlyAway(Character<T> character, double speed, Location... path) {
      return this.append(performer -> new EntityFlyAwayAction<>(performer, character, speed, true, () -> Arrays.stream(path).toList()));
   }

   public <T extends Mob> ScriptPerformerBuilder thenEntityFlyAway(Character<T> character, double speed) {
      return this.append(performer -> new EntityFlyAwayAction<>(performer, character, speed, true, ArrayList::new));
   }

   public <T extends Entity> ScriptPerformerBuilder thenWhenPlayerCloseTo(Character<T> character, double radius) {
      return this.append(performer -> new EntityCloseToPlayerAction<>(performer, character, radius));
   }

   public ScriptPerformerBuilder thenWhenPlayerCloseTo(Supplier<Location> pos, double distance) {
      return this.append(performer -> new PlayerNearPosAction(performer, pos, distance));
   }

   public ScriptPerformerBuilder thenWhenPlayerCloseTo(String pos, double distance) {
      return this.append(performer -> new PlayerNearPosAction(performer, () -> new Location(pos), distance));
   }

   public ScriptPerformerBuilder thenWhenPlayerEnterDimension(String name) {
      return this.append(performer -> new WaitPlayerEnterDimensionAction(performer, Resources.location(name)));
   }

   public ScriptPerformerBuilder thenWhenPlayerEnterDimension(ResourceKey<Level> dimension) {
      return this.append(performer -> new WaitPlayerEnterDimensionAction(performer, dimension.location()));
   }

   public <T extends Entity> ScriptPerformerBuilder openDoor(Character<T> character, String setBlockCord, boolean open) {
      return this.append(performer -> new UseEntityAction<>(performer, character, ScriptUses.setDoorState(setBlockCord, open)));
   }

   public <T extends Entity> ScriptPerformerBuilder thenUseEntity(Character<T> character, Consumer<? super T> consumer) {
      return this.append(performer -> new UseEntityAction<>(performer, character, consumer));
   }

   public <T extends AnimatedEntity> ScriptPerformerBuilder changeOutfit(Character<T> character, Outfit outfit) {
      return this.append(performer -> new EntityChangeOutfitAction<>(performer, character, Cast.constant(outfit)));
   }

   public <T extends AnimatedEntity> ScriptPerformerBuilder changeOutfit(Character<T> character, Supplier<Outfit> outfit) {
      return this.append(performer -> new EntityChangeOutfitAction<>(performer, character, outfit));
   }

   public <A extends Entity, B extends Entity> ScriptPerformerBuilder thenUse2Entities(
      Character<A> charA, Character<B> charB, Consumer2<? super A, ? super B> consumer
   ) {
      return this.append(performer -> new UseTwoEntityAction<>(performer, charA, charB, consumer));
   }

   public ScriptPerformerBuilder thenUsePlayer(Consumer<ServerPlayer> consumer) {
      return this.thenRun(() -> consumer.accept(ScriptUtils.getPlayer()));
   }

   public ScriptPerformerBuilder thenHandlePlayer(BiConsumer<Boolean, ServerPlayer> consumer) {
      return this.thenHandleState(pass -> consumer.accept(pass, ScriptUtils.getPlayer()));
   }

   public <T extends AnimatedEntity> ScriptPerformerBuilder animation(Character<T> character, int layer, Random random, ConfiguredAnimation... animations) {
      return this.append(performer -> new EntitySetAnimationAction<>(performer, character, "ANIMATION_" + layer, random, animations));
   }

   public <T extends AnimatedEntity> ScriptPerformerBuilder animation(Character<T> character, int layer, ConfiguredAnimation... animations) {
      return this.append(performer -> new EntitySetAnimationAction<>(performer, character, "ANIMATION_" + layer, animations));
   }

   public <T extends AnimatedEntity> ScriptPerformerBuilder animation(Character<T> character, int layer, IAnimationSource... animations) {
      return this.append(
         performer -> new EntitySetAnimationAction<>(
               performer,
               character,
               "ANIMATION_" + layer,
               Arrays.stream(animations)
                  .<ConfiguredAnimation>map(IAnimationSource::configure)
                  .map(c -> c.transitionTime(0.4F))
                  .toArray(ConfiguredAnimation[]::new)
            )
      );
   }

   public <T extends AnimatedEntity> ScriptPerformerBuilder action(Character<T> character, Random random, ConfiguredAnimation... animations) {
      return this.append(performer -> new EntitySetAnimationAction<>(performer, character, "action", random, animations));
   }

   public <T extends AnimatedEntity> ScriptPerformerBuilder action(Character<T> character, ConfiguredAnimation... animations) {
      return this.append(performer -> new EntitySetAnimationAction<>(performer, character, "action", animations));
   }

   public <T extends AnimatedEntity> ScriptPerformerBuilder action(Character<T> character, IAnimationSource... animations) {
      return this.append(
         performer -> new EntitySetAnimationAction<>(
               performer,
               character,
               "action",
               Arrays.stream(animations)
                  .<ConfiguredAnimation>map(IAnimationSource::configure)
                  .map(c -> c.transitionTime(0.4F))
                  .toArray(ConfiguredAnimation[]::new)
            )
      );
   }

   public <T extends AnimatedEntity> ScriptPerformerBuilder emotion(Character<T> character, ConfiguredAnimation... animations) {
      return this.append(performer -> new EntitySetAnimationAction<>(performer, character, "emotion", null, animations));
   }

   public <T extends AnimatedEntity> ScriptPerformerBuilder emotion(Character<T> character, Random random, ConfiguredAnimation... animations) {
      return this.append(performer -> new EntitySetAnimationAction<>(performer, character, "emotion", random, animations));
   }

   public <T extends AnimatedEntity> ScriptPerformerBuilder emotion(Character<T> character, IAnimationSource... animations) {
      return this.append(
         performer -> new EntitySetAnimationAction<>(
               performer,
               character,
               "emotion",
               Arrays.stream(animations)
                  .<ConfiguredAnimation>map(IAnimationSource::configure)
                  .map(c -> c.transitionTime(0.4F))
                  .toArray(ConfiguredAnimation[]::new)
            )
      );
   }

   public <T extends AnimatedEntity> ScriptPerformerBuilder stopAnimation(Character<T> character, int layer) {
      return this.append(
         performer -> new EntitySetAnimationAction<>(performer, character, "ANIMATION_" + layer, DefaultsHA.NULL_ANIM.configure().transitionTime(0.4F))
      );
   }

   public <T extends AnimatedEntity> ScriptPerformerBuilder stopAnimation(Character<T> character, int layer, float transitionTime) {
      return this.append(
         performer -> new EntitySetAnimationAction<>(
               performer, character, "ANIMATION_" + layer, DefaultsHA.NULL_ANIM.configure().transitionTime(transitionTime)
            )
      );
   }

   public <T extends AnimatedEntity> ScriptPerformerBuilder stopAction(Character<T> character) {
      return this.append(performer -> new EntitySetAnimationAction<>(performer, character, "action", DefaultsHA.NULL_ANIM.configure().transitionTime(0.4F)));
   }

   public <T extends AnimatedEntity> ScriptPerformerBuilder stopAction(Character<T> character, float transitionTime) {
      return this.append(
         performer -> new EntitySetAnimationAction<>(performer, character, "action", DefaultsHA.NULL_ANIM.configure().transitionTime(transitionTime))
      );
   }

   public <T extends AnimatedEntity> ScriptPerformerBuilder stopEmotion(Character<T> character) {
      return this.append(performer -> new EntitySetAnimationAction<>(performer, character, "emotion", DefaultsHA.NULL_ANIM.configure().transitionTime(0.4F)));
   }

   public <T extends AnimatedEntity> ScriptPerformerBuilder stopEmotion(Character<T> character, float transitionTime) {
      return this.append(
         performer -> new EntitySetAnimationAction<>(performer, character, "emotion", DefaultsHA.NULL_ANIM.configure().transitionTime(transitionTime))
      );
   }

   public <T extends AnimatedEntity> ScriptPerformerBuilder clearAnimations(Character<T> character) {
      return this.append(performer -> new EntityClearAnimationsAction<>(performer, character, a -> a.transitionTime(0.4F)));
   }

   public <T extends AnimatedEntity> ScriptPerformerBuilder clearAnimations(Character<T> character, float transitionTime) {
      return this.append(performer -> new EntityClearAnimationsAction<>(performer, character, a -> a.transitionTime(transitionTime)));
   }

   public <T extends Entity> ScriptPerformerBuilder simpleBehaviour(Character<T> character, BehaviourType type, Function<EntityContext<T>, Boolean> action) {
      return this.append(performer -> new SimpleBehaviourAction<>(performer, character, true, type, action));
   }

   public <T extends Entity> ScriptPerformerBuilder simpleBehaviour(
      Character<T> character, BehaviourType type, boolean parallel, Function<EntityContext<T>, Boolean> action
   ) {
      return this.append(performer -> new SimpleBehaviourAction<>(performer, character, parallel, type, action));
   }

   public <T extends Entity> ScriptPerformerBuilder interruptBehaviours(Character<T> character, String... layers) {
      return this.append(performer -> new BehaviourAction<T, InterruptionBehaviour<T>>(performer, character) {
            public InterruptionBehaviour<T> createBehaviour() {
               return new InterruptionBehaviour(layers);
            }

            @Override
            public boolean isDone() {
               return this.init;
            }
         });
   }

   public <T extends AnimatedEntity> ScriptPerformerBuilder remoteBehaviourChange(
      Character<T> character, Consumer<ScriptPerformerBuilder> action, String... interruptionLayers
   ) {
      return this.append(performer -> new EntityPerformerInvokerAction(performer, character, action, true, interruptionLayers));
   }

   public <T extends AnimatedEntity> ScriptPerformerBuilder remoteBehaviourChange(
      Character<T> character, boolean parallel, Consumer<ScriptPerformerBuilder> action, String... interruptionLayers
   ) {
      return this.append(performer -> new EntityPerformerInvokerAction(performer, character, action, parallel, interruptionLayers));
   }

   @SafeVarargs
   public final <T extends AnimatedEntity> ScriptPerformerBuilder setEntityPhrases(Character<T> character, Phrase<T>... phrases) {
      return this.append(performer -> new EntityPhrasesPerformerAction<>(performer, character, phrases));
   }

   @SafeVarargs
   public final <T extends AnimatedEntity> ScriptPerformerBuilder setEntityPhrases(Character<T> character, PhraseOrder order, Phrase<T>... phrases) {
      return this.append(performer -> new EntityPhrasesPerformerAction<>(performer, character, order, phrases));
   }

   public ScriptPerformerBuilder teleportPlayer(Supplier<Location> location) {
      return this.append(performer -> new PlayerTeleportAction(performer, location));
   }

   public ScriptPerformerBuilder teleportPlayer(String pos) {
      Location loc = new Location(pos);
      return this.append(performer -> new PlayerTeleportAction(performer, loc));
   }

   public ScriptPerformerBuilder playerSit(String location, boolean lockUnmount) {
      Location loc = new Location(location);
      return this.append(performer -> new PlayerSitAction(performer, Cast.constant(loc), lockUnmount));
   }

   public ScriptPerformerBuilder playerSit(Supplier<Location> location, boolean lockUnmount) {
      return this.append(performer -> new PlayerSitAction(performer, location, lockUnmount));
   }

   public ScriptPerformerBuilder playerSit(boolean lockUnmount) {
      return this.append(performer -> new PlayerSitAction(performer, null, lockUnmount));
   }

   public ScriptPerformerBuilder thenWhenPlayerEnterBox(String cord1, String cord2) {
      return this.append(performer -> new WhenPlayerInBoxAction(performer, new AABB(new Location(cord1), new Location(cord2)), false, true));
   }

   public ScriptPerformerBuilder thenWhenPlayerEnterBox(AABB box) {
      return this.append(performer -> new WhenPlayerInBoxAction(performer, box, false, true));
   }

   public ScriptPerformerBuilder thenWhenPlayerEnterBox(double x1, double y1, double z1, double x2, double y2, double z2) {
      return this.append(performer -> new WhenPlayerInBoxAction(performer, new AABB(x1, y1, z1, x2, y2, z2).expandTowards(1.0, 1.0, 1.0), false, true));
   }

   public ScriptPerformerBuilder thenWhenPlayerTouchBox(AABB box) {
      return this.append(performer -> new WhenPlayerInBoxAction(performer, box, true, true));
   }

   public ScriptPerformerBuilder thenWhenPlayerTouchBox(double x1, double y1, double z1, double x2, double y2, double z2) {
      return this.append(performer -> new WhenPlayerInBoxAction(performer, new AABB(x1, y1, z1, x2, y2, z2).expandTowards(1.0, 1.0, 1.0), true, true));
   }

   public ScriptPerformerBuilder thenWhenPlayerExitBox(String cord1, String cord2) {
      return this.append(performer -> new WhenPlayerInBoxAction(performer, new AABB(new Location(cord1), new Location(cord2)), false, false));
   }

   public ScriptPerformerBuilder thenWhenPlayerExitBox(AABB box) {
      return this.append(performer -> new WhenPlayerInBoxAction(performer, box, false, false));
   }

   public ScriptPerformerBuilder thenWhenPlayerExitBox(double x1, double y1, double z1, double x2, double y2, double z2) {
      return this.append(performer -> new WhenPlayerInBoxAction(performer, new AABB(x1, y1, z1, x2, y2, z2).expandTowards(1.0, 1.0, 1.0), false, false));
   }

   public ScriptPerformerBuilder thenWhenPlayerTouchBoxFromInside(AABB box) {
      return this.append(performer -> new WhenPlayerInBoxAction(performer, box, true, false));
   }

   public ScriptPerformerBuilder thenWhenPlayerTouchBoxFromInside(double x1, double y1, double z1, double x2, double y2, double z2) {
      return this.append(performer -> new WhenPlayerInBoxAction(performer, new AABB(x1, y1, z1, x2, y2, z2).expandTowards(1.0, 1.0, 1.0), true, false));
   }

   public ScriptPerformerBuilder thenWaitChatMessage(@Nullable Predicate<String> filter) {
      return this.append(performer -> new ChatMessageAction(performer, filter, null, true));
   }

   public ScriptPerformerBuilder thenWaitChatMessage(@Nullable Predicate<String> filter, Supplier<String> info) {
      return this.append(performer -> new ChatMessageAction(performer, filter, info, true));
   }

   public ScriptPerformerBuilder thenWaitChatMessage(String... keywords) {
      return this.append(performer -> new ChatMessageAction(performer, message -> {
            String string = message.toLowerCase(Locale.ROOT);
            if (keywords.length == 0) {
               return true;
            } else {
               for (String key : keywords) {
                  if (string.contains(key.toLowerCase(Locale.ROOT))) {
                     return true;
                  }
               }

               return false;
            }
         }, Cast.constant(Arrays.stream(keywords).toList().toString()), true));
   }

   public ScriptPerformerBuilder thenWaitChatMessage(@Nullable Predicate<String> filter, boolean wait) {
      return this.append(performer -> new ChatMessageAction(performer, filter, null, wait));
   }

   public ScriptPerformerBuilder thenWaitChatMessage(@Nullable Predicate<String> filter, Supplier<String> info, boolean wait) {
      return this.append(performer -> new ChatMessageAction(performer, filter, info, wait));
   }

   public ScriptPerformerBuilder thenWaitChatMessage(boolean wait, String... keywords) {
      return this.append(performer -> new ChatMessageAction(performer, message -> {
            String string = message.toLowerCase(Locale.ROOT);
            if (keywords.length == 0) {
               return true;
            } else {
               for (String key : keywords) {
                  if (string.contains(key.toLowerCase(Locale.ROOT))) {
                     return true;
                  }
               }

               return false;
            }
         }, Cast.constant(Arrays.stream(keywords).toList().toString()), wait));
   }

   public ScriptPerformerBuilder thenAwaitEntityClick(Character<?> character) {
      return this.thenAwaitEvent(
         EventType.ENTITY_CLICK, character::is, Cast.constant("Ожидание клика по " + character.getName() + " (" + character.getReferenceId() + ")")
      );
   }

   public ScriptPerformerBuilder thenAwaitEntityClick(Character<?> character, Predicate<EntityInteractionData> filter) {
      Predicate<EntityInteractionData> f0 = character::is;
      return this.thenAwaitEvent(
         EventType.ENTITY_CLICK, f0.and(filter), Cast.constant("Ожидание клика по " + character.getName() + " (" + character.getReferenceId() + ")")
      );
   }

   public ScriptPerformerBuilder thenAwaitBlockClick(BlockPos blockPos) {
      return this.thenAwaitEvent(
         EventType.BLOCK_CLICK,
         data -> data.getPos().equals(blockPos),
         Cast.constant("Ожидание клика по блоку (%s %s %s)".formatted(blockPos.getX(), blockPos.getY(), blockPos.getZ()))
      );
   }

   public ScriptPerformerBuilder thenAwaitBlockClick(String blockPos) {
      return this.thenAwaitBlockClick(PositionParser.parseBlockPos(blockPos));
   }

   public ScriptPerformerBuilder thenAwaitBlockClick(BlockPos blockPos, Predicate<RightClickBlock> filter) {
      Predicate<RightClickBlock> isNeedBlock = data -> data.getPos().equals(blockPos);
      return this.thenAwaitEvent(
         EventType.BLOCK_CLICK,
         isNeedBlock.and(filter),
         Cast.constant("Ожидание клика по блоку (%s %s %s)".formatted(blockPos.getX(), blockPos.getY(), blockPos.getZ()))
      );
   }

   public <T> ScriptPerformerBuilder thenAwaitEvent(EventType<T> type) {
      return this.append(performer -> new EventAwaitingAction<>(performer, type, null));
   }

   public <T> ScriptPerformerBuilder thenAwaitEvent(EventType<T> type, Predicate<T> filter) {
      return this.append(performer -> new EventAwaitingAction<>(performer, type, filter, null));
   }

   public <T> ScriptPerformerBuilder thenAwaitEvent(EventType<T> type, Predicate<T> filter, Supplier<String> stHelpInfo) {
      return this.append(performer -> new EventAwaitingAction<>(performer, type, filter, stHelpInfo));
   }

   public ScriptPerformerBuilder thenSetSpeechEntity(int entity, String name) {
      return this.append(performer -> new SpeechSetEntityNameAction(performer, entity, name));
   }

   public ScriptPerformerBuilder thenSetSpeechEntity(int entity, String name, String icon) {
      return this.append(performer -> new SpeechSetEntityNameAction(performer, entity, name, icon));
   }

   public ScriptPerformerBuilder thenSetSpeechEntity(int entity, String name, int color) {
      return this.append(performer -> new SpeechSetEntityNameAction(performer, entity, name, color));
   }

   public ScriptPerformerBuilder thenSetSpeechEntity(int entity, String name, int color, String icon) {
      return this.append(performer -> new SpeechSetEntityNameAction(performer, entity, name, color, icon));
   }

   public ScriptPerformerBuilder thenSetSpeechEntity(int entity, Character<?> character) {
      return this.append(performer -> new SpeechSetEntityNameAction(performer, entity, character.getName(), character.getReferenceId()));
   }

   public ScriptPerformerBuilder thenSpeechMessage(int entity, Supplier<Component> text) {
      return this.append(performer -> new SpeechMessageAction(performer, entity, null, text, true));
   }

   public ScriptPerformerBuilder thenSpeechMessage(int entity, Supplier<Component> text, boolean waitForF8) {
      return this.append(performer -> new SpeechMessageAction(performer, entity, null, text, waitForF8));
   }

   public ScriptPerformerBuilder thenSpeechMessage(int entity, Supplier<Component> flavor, Supplier<Component> text, boolean waitForF8) {
      return this.append(performer -> new SpeechMessageAction(performer, entity, flavor, text, waitForF8));
   }

   public ScriptPerformerBuilder thenSpeechMessage(int entity, String text) {
      return this.append(performer -> new SpeechMessageAction(performer, entity, null, () -> Component.literal(text), true));
   }

   public ScriptPerformerBuilder thenThink(String text) {
      return this.append(performer -> new ThinkMessageAction(performer, null, () -> Component.literal(text), "Lololowka", "thinking_timeless", true));
   }

   public ScriptPerformerBuilder thenThinkJdh(String text) {
      return this.thenThink(text, "JDH", "jdh_thinking");
   }

   public ScriptPerformerBuilder thenThinkJdh(Supplier<Component> text) {
      return this.thenThink(text, "JDH", "jdh_thinking");
   }

   public ScriptPerformerBuilder thenThinkTimeless(Supplier<Component> text) {
      return this.thenThink(text, "Lololowka", "thinking_timeless");
   }

   public ScriptPerformerBuilder thenThinkTimeless(String text) {
      return this.thenThink(text, "Lololowka", "thinking_timeless");
   }

   public ScriptPerformerBuilder thenThinkTimeless(Supplier<Component> text, boolean waitForF8) {
      return this.thenThink(text, "Lololowka", "thinking_timeless", waitForF8);
   }

   public ScriptPerformerBuilder thenThinkTimeless(String text, boolean waitForF8) {
      return this.thenThink(text, "Lololowka", "thinking_timeless", waitForF8);
   }

   public ScriptPerformerBuilder thenThinkTimelessNoGlasses(Supplier<Component> text) {
      return this.thenThink(text, "Lololowka", "no_glasses_thinking_timeless");
   }

   public ScriptPerformerBuilder thenThinkTimelessNoGlasses(String text) {
      return this.thenThink(text, "Lololowka", "no_glasses_thinking_timeless");
   }

   public ScriptPerformerBuilder thenThinkTimelessNoGlasses(Supplier<Component> text, boolean waitForF8) {
      return this.thenThink(text, "Lololowka", "no_glasses_thinking_timeless", waitForF8);
   }

   public ScriptPerformerBuilder thenThinkTimelessNoGlasses(String text, boolean waitForF8) {
      return this.thenThink(text, "Lololowka", "no_glasses_thinking_timeless", waitForF8);
   }

   public ScriptPerformerBuilder thenThinkLaia(Supplier<Component> text) {
      return this.thenThink(text, "Лайя", "laya_thinking_icon");
   }

   public ScriptPerformerBuilder thenThinkLaia(String text) {
      return this.thenThink(text, "Лайя", "laya_thinking_icon");
   }

   public ScriptPerformerBuilder thenThinkLaia(Supplier<Component> text, boolean waitForF8) {
      return this.thenThink(text, "Лайя", "laya_thinking_icon", waitForF8);
   }

   public ScriptPerformerBuilder thenThinkLaia(String text, boolean waitForF8) {
      return this.thenThink(text, "Лайя", "laya_thinking_icon", waitForF8);
   }

   public ScriptPerformerBuilder thenThinkJdh(String text, boolean waitForF8) {
      return this.thenThink(text, "JDH", "jdh_thinking", waitForF8);
   }

   public ScriptPerformerBuilder thenThinkJdh(Supplier<Component> text, boolean waitForF8) {
      return this.thenThink(text, "JDH", "jdh_thinking", waitForF8);
   }

   public ScriptPerformerBuilder thenThinkJolowka(String text) {
      return this.thenThink(text, "Lo | JDH", "mixed_jdh_lololoshka_thinking_icon");
   }

   public ScriptPerformerBuilder thenThinkJolowka(String text, boolean waitForF8) {
      return this.thenThink(text, "Lo | JDH", "mixed_jdh_lololoshka_thinking_icon", waitForF8);
   }

   public ScriptPerformerBuilder thenThinkAlfred(String text) {
      return this.thenThink(text, "Альфред", "alfred_thinking_icon", true);
   }

   public ScriptPerformerBuilder thenThinkAlfred(String text, boolean waitForF8) {
      return this.thenThink(text, "Альфред", "alfred_thinking_icon", waitForF8);
   }

   public ScriptPerformerBuilder thenThink(String text, String name, String icon) {
      return this.append(performer -> new ThinkMessageAction(performer, null, () -> Component.literal(text), name, icon, true));
   }

   public ScriptPerformerBuilder thenThink(String text, boolean waitForF8) {
      return this.append(performer -> new ThinkMessageAction(performer, null, () -> Component.literal(text), waitForF8));
   }

   public ScriptPerformerBuilder thenThink(String text, String name, String icon, boolean waitForF8) {
      return this.append(performer -> new ThinkMessageAction(performer, null, () -> Component.literal(text), name, icon, waitForF8));
   }

   public ScriptPerformerBuilder thenThink(Supplier<Component> text) {
      return this.append(performer -> new ThinkMessageAction(performer, null, text, true));
   }

   public ScriptPerformerBuilder thenThink(Supplier<Component> text, boolean waitForF8) {
      return this.append(performer -> new ThinkMessageAction(performer, null, text, waitForF8));
   }

   public ScriptPerformerBuilder thenThink(Supplier<Component> text, String name, String icon) {
      return this.append(performer -> new ThinkMessageAction(performer, null, text, name, icon, true));
   }

   public ScriptPerformerBuilder thenThink(Supplier<Component> text, String name, String icon, boolean waitForF8) {
      return this.append(performer -> new ThinkMessageAction(performer, null, text, name, icon, waitForF8));
   }

   public ScriptPerformerBuilder thenSpeechMessage(int entity, String flavor, String text) {
      return this.append(performer -> new SpeechMessageAction(performer, entity, () -> Component.literal(flavor), () -> Component.literal(text), true));
   }

   public ScriptPerformerBuilder thenSpeechMessage(int entity, String text, boolean waitForF8) {
      return this.append(performer -> new SpeechMessageAction(performer, entity, null, () -> Component.literal(text), waitForF8));
   }

   public ScriptPerformerBuilder thenSpeechMessage(int entity, String flavor, String text, boolean waitForF8) {
      return this.append(performer -> new SpeechMessageAction(performer, entity, () -> Component.literal(flavor), () -> Component.literal(text), waitForF8));
   }

   public <T extends Entity & ILivingAnimationEntity> ScriptPerformerBuilder removeOverridePredefined(Character<T> character) {
      return this.overridePredefinedAnimation(character, (ConfiguredAnimation)null);
   }

   public <T extends Entity & ILivingAnimationEntity> ScriptPerformerBuilder overridePredefinedAnimation(
      Character<T> character, @Nullable Supplier<ConfiguredAnimation> animation
   ) {
      return this.append(performer -> new OverridePredefinedAnimationAction<>(performer, character, animation));
   }

   public <T extends Entity & ILivingAnimationEntity> ScriptPerformerBuilder overridePredefinedAnimation(
      Character<T> character, @Nullable IAnimationSource animation
   ) {
      return this.append(performer -> new OverridePredefinedAnimationAction<>(performer, character, animation == null ? null : animation.configure()));
   }

   public <T extends Entity & ILivingAnimationEntity> ScriptPerformerBuilder overridePredefinedAnimation(
      Character<T> character, @Nullable ConfiguredAnimation animation
   ) {
      return this.append(performer -> new OverridePredefinedAnimationAction<>(performer, character, animation));
   }

   public <T extends Entity & ILivingAnimationEntity> ScriptPerformerBuilder overridePredefinedAnimation(
      Character<T> character, MovementActionType type, @Nullable IAnimationSource animation
   ) {
      return this.append(performer -> new OverridePredefinedAnimationAction<>(performer, character, type, animation == null ? null : animation::configure));
   }

   public <T extends Entity & ILivingAnimationEntity> ScriptPerformerBuilder overridePredefinedAnimation(
      Character<T> character, MovementActionType type, @Nullable ConfiguredAnimation animation
   ) {
      return this.append(performer -> new OverridePredefinedAnimationAction<>(performer, character, type, () -> animation));
   }

   public ScriptPerformerBuilder playerTemporarilyTextureOverride(ResourceLocation texture) {
      return this.thenPerform(() -> PersonaHandler.textureOverride = texture.toString());
   }

   public ScriptPerformerBuilder removePlayerTemporarilyTextureOverride() {
      return this.thenPerform(() -> PersonaHandler.textureOverride = null);
   }

   public ScriptPerformerBuilder playerTextureOverride(ResourceLocation texture) {
      return this.append(performer -> new SetPlayerTextureAction(performer, texture));
   }

   public ScriptPerformerBuilder playerTextureReset() {
      return this.append(performer -> new SetPlayerTextureAction(performer, null));
   }

   public ScriptPerformerBuilder thenSetHeldItem(Character<? extends LivingEntity> character, ItemLike item) {
      return this.append(performer -> new EntitySetHeldItemAction(performer, character, InteractionHand.MAIN_HAND, item.asItem()::getDefaultInstance));
   }

   public ScriptPerformerBuilder thenSetHeldItem(Character<? extends LivingEntity> character, ItemStack stack) {
      return this.append(performer -> new EntitySetHeldItemAction(performer, character, InteractionHand.MAIN_HAND, stack::copy));
   }

   public ScriptPerformerBuilder thenSetHeldItem(Character<? extends LivingEntity> character, Supplier<ItemStack> stack) {
      return this.append(performer -> new EntitySetHeldItemAction(performer, character, InteractionHand.MAIN_HAND, stack));
   }

   public ScriptPerformerBuilder thenSetHeldItem(Character<? extends LivingEntity> character, InteractionHand hand, Supplier<ItemStack> stack) {
      return this.append(performer -> new EntitySetHeldItemAction(performer, character, hand, stack));
   }

   public ScriptPerformerBuilder thenSetHeldItem(Character<? extends LivingEntity> character, InteractionHand hand, ItemStack stack) {
      return this.thenSetHeldItem(character, hand, stack::copy);
   }

   public ScriptPerformerBuilder thenSetHeldItem(Character<? extends LivingEntity> character, InteractionHand hand, ItemLike item) {
      return this.thenSetHeldItem(character, hand, item.asItem()::getDefaultInstance);
   }

   public ScriptPerformerBuilder playSound(String locationPosition, SoundEvent sound) {
      Location loc = new Location(locationPosition);
      return this.thenRun(() -> {
         MinecraftServer server = ScriptUtils.server();
         ServerLevel level = loc.getDimension() != null && server != null ? server.getLevel(loc.getDimension()) : ScriptUtils.getLevel();
         if (level != null) {
            level.playSound(null, loc.toBlockPos(), sound, SoundSource.MASTER, 1.0F, 1.0F);
         }
      });
   }

   public ScriptPerformerBuilder playSound(BlockPos position, SoundEvent sound) {
      Location loc = new Location(position);
      return this.thenRun(() -> {
         MinecraftServer server = ScriptUtils.server();
         ServerLevel level = loc.getDimension() != null && server != null ? server.getLevel(loc.getDimension()) : ScriptUtils.getLevel();
         if (level != null) {
            level.playSound(null, loc.toBlockPos(), sound, SoundSource.MASTER, 1.0F, 1.0F);
         }
      });
   }

   public ScriptPerformerBuilder playSound(Supplier<BlockPos> position, SoundEvent sound) {
      return this.thenRun(() -> {
         MinecraftServer server = ScriptUtils.server();
         Location loc = new Location(position.get());
         ServerLevel level = loc.getDimension() != null && server != null ? server.getLevel(loc.getDimension()) : ScriptUtils.getLevel();
         if (level != null) {
            level.playSound(null, loc.toBlockPos(), sound, SoundSource.MASTER, 1.0F, 1.0F);
         }
      });
   }

   public <S extends ScriptInstance> ScriptPerformerBuilder pausePerformer(Class<S> scriptClass, int scriptId, Function<S, ScriptPerformer> getter) {
      this.thenHandleState(passed -> {
         S script = (S)Cast.cast(IStoryServer.get().getScriptEnv().getScript(scriptId));
         if (script != null) {
            ScriptPerformer performer = getter.apply(script);
            if (performer != null) {
               performer.setPaused(passed);
            }
         }
      });
      return this;
   }

   public <S extends ScriptInstance> ScriptPerformerBuilder continuePerformer(
      Class<S> scriptClass, int scriptId, boolean fastForward, Function<S, ScriptPerformer> getter
   ) {
      this.thenHandleState(passed -> {
         S script = (S)Cast.cast(IStoryServer.get().getScriptEnv().getScript(scriptId));
         if (script != null) {
            ScriptPerformer performer = getter.apply(script);
            if (passed && fastForward) {
               int active = performer.getActionIndex();
               performer.gotoSavepoint("start");
               performer.jumpTo(active, false);
            }

            if (performer != null) {
               performer.setPaused(!passed);
            }
         }
      });
      return this;
   }

   public <T extends HumanoidEntity> ScriptPerformerBuilder dontRotateOnInteraction(Character<T> character, boolean state) {
      return this.append(performer -> new HumanoidDontRotateOnInteractionAction<>(performer, character, state));
   }

   public <T extends AnimatedEntity> ScriptPerformerBuilder blockAnimWalkingBodyRotation(Character<T> character, boolean state) {
      return this.append(performer -> new HumanoidBlockAnimWalkingBodyRotationAction<>(performer, character, state));
   }

   public <T extends AnimatedEntity> ScriptPerformerBuilder blockBodyRotation(Character<T> character, boolean state) {
      return this.append(performer -> new HumanoidBlockBodyRotationAction<>(performer, character, state));
   }

   public <T extends HumanoidEntity> ScriptPerformerBuilder ignoreNavmesh(Character<T> character, boolean state) {
      return this.append(performer -> new HumanoidIgnoreNavmeshAction<>(performer, character, state));
   }

   public <T extends AnimatedEntity> ScriptPerformerBuilder setTraversalNavigation(Character<T> character, boolean state) {
      return this.append(performer -> new AnimatedSetTraversalNavigation<>(performer, character, state));
   }

   public <T extends AnimatedEntity> ScriptPerformerBuilder restrictHeadRotation(Character<T> character, boolean state) {
      return this.append(performer -> new HumanoidRestrictHeadRotationAction<>(performer, character, state));
   }

   public <T extends AnimatedEntity> ScriptPerformerBuilder freeSight(Character<T> character, boolean state) {
      return this.append(performer -> new EntitySightStateAction(performer, character, state));
   }

   public <T extends AnimatedEntity> ScriptPerformerBuilder nod(Character<T> character, int times) {
      return this.animationWithTimes(character, Animations.HUMANOID_NOD, times, null);
   }

   public <T extends AnimatedEntity> ScriptPerformerBuilder nod(Character<T> character, int times, int dec) {
      return this.animationWithTimes(character, Animations.HUMANOID_NOD, times, null, dec);
   }

   public <T extends AnimatedEntity> ScriptPerformerBuilder nod(Character<T> character, int times, float speed) {
      return this.animationWithTimes(character, Animations.HUMANOID_NOD, times, speed);
   }

   public <T extends AnimatedEntity> ScriptPerformerBuilder shake(Character<T> character, int times) {
      return this.animationWithTimes(character, Animations.EMOTION_HEAD_SHAKE, times, null);
   }

   public <T extends AnimatedEntity> ScriptPerformerBuilder nope(Character<T> character, int times) {
      return this.animationWithTimes(character, Animations.EMOTION_NOPE, times, null);
   }

   public <T extends AnimatedEntity> ScriptPerformerBuilder nope(Character<T> character, int times, float speed) {
      return this.animationWithTimes(character, Animations.EMOTION_NOPE, times, speed);
   }

   public <T extends AnimatedEntity> ScriptPerformerBuilder sigh(Character<T> character) {
      return this.animationWithTimes(character, Animations.SIGH_NEW, 1, null);
   }

   public <T extends AnimatedEntity> ScriptPerformerBuilder laugh(Character<T> character) {
      return this.animationWithTimes(character, Animations.EMOTION_GIGGLE_NEW_NOMOUTH, 1, null);
   }

   public <T extends AnimatedEntity> ScriptPerformerBuilder laugh(Character<T> character, int times, float speed) {
      return this.animationWithTimes(character, Animations.EMOTION_GIGGLE_NEW_NOMOUTH, times, speed);
   }

   public <T extends AnimatedEntity> ScriptPerformerBuilder animationWithTimes(Character<T> character, IAnimationSource animationSource, int times, Float speed) {
      return this.animationWithTimes(character, animationSource, times, speed, null);
   }

   public <T extends AnimatedEntity> ScriptPerformerBuilder animationWithTimes(
      Character<T> character, IAnimationSource animationSource, int times, Float speed, Integer dec
   ) {
      ConfiguredAnimation configuredAnimation = speed != null ? animationSource.configure().speed(speed) : animationSource.configure();
      double animationLength = configuredAnimation.animation.getData().getLengthSeconds() * 20.0;
      int delay = speed != null ? (int)Math.ceil(animationLength * (double)times / (double)speed.floatValue()) : (int)(animationLength * (double)times);
      int delayWithDec = dec != null ? delay - dec : delay;
      return this.animation(character, 10, configuredAnimation).thenDelay(delayWithDec).stopAnimation(character, 10);
   }

   public ScriptPerformerBuilder thenDynamicPerformer(Consumer<ScriptPerformerBuilder> builder) {
      return this.append(performer -> new DynamicPerformerAction(performer, builder));
   }

   public ScriptPerformerBuilder enlistScriptAutostart(boolean ignore, int... scripts) {
      return this.append(performer -> new ScriptAutostartAction(performer, ignore, scripts));
   }

   public ScriptPerformerBuilder enlistScriptAutostart(boolean ignore, ScriptCategory... scripts) {
      return this.append(
         performer -> new ScriptAutostartAction(
               performer, ignore, Arrays.stream(scripts).map(ScriptCategory::getScripts).flatMapToInt(Arrays::stream).toArray()
            )
      );
   }

   public ScriptPerformerBuilder addShader(ShaderModifier modifier) {
      return this.thenPerform(
         () -> ShaderSystem.addShader(modifier.setStillValid(() -> this.performer.script.env.getScript(this.performer.script.getId()) == this.performer.script))
      );
   }

   public ScriptPerformerBuilder changeShaderPack(String shaderpack, float priority) {
      return this.addShader(new ShaderModifier(shaderpack).priority(priority));
   }

   public ScriptPerformerBuilder changeShaderPack(String modifierName, String shaderpack, float priority) {
      return this.addShader(new ShaderModifier(modifierName, shaderpack).priority(priority));
   }

   public ScriptPerformerBuilder changeShaderPack(String shaderpack) {
      return this.changeShaderPack(shaderpack, Float.POSITIVE_INFINITY);
   }

   public ScriptPerformerBuilder changeShaderPack(String modifierName, String shaderpack) {
      return this.addShader(new ShaderModifier(modifierName, shaderpack).priority(Float.POSITIVE_INFINITY));
   }

   public ScriptPerformerBuilder removeShader(String name) {
      return this.thenPerform(() -> ShaderSystem.removeShader(name));
   }

   public ScriptPerformerBuilder removeShader(ShaderModifier mod) {
      return this.thenPerform(() -> ShaderSystem.removeShader(mod));
   }

   public <T extends BaseMob> ScriptPerformerBuilder startFightWithDeath(Character<T> character, IAttackChain<T> chain, IAnimationSource death) {
      return this.startFightWithDeath(character, chain, death.configure());
   }

   public <T extends BaseMob> ScriptPerformerBuilder startFightWithDeath(Character<T> character, IAttackChain<T> chain, ConfiguredAnimation death) {
      return this.startFight(character, chain)
         .action(character, death)
         .thenDelay(death)
         .thenUseEntity(
            character,
            e -> {
               SoundUtils.playVolumedSound(e.level(), e.blockPosition(), SoundsST.MOB_DISINTEGRATE);
               ParticleHelper.spawnParticleAABB(
                  e.level(), ParticleTypes.CLOUD, e.getBoundingBox().contract(0.0, e.getBoundingBox().getYsize() / 2.0, 0.0).inflate(0.7, 0.0, 0.7), 20, 0.01
               );
            }
         )
         .thenDelay(6)
         .thenRun(() -> {
            Entity e = IStoryServer.get().getEntityRepository().getEntity(character);
            if (e instanceof BaseMob baseMob) {
               if (character instanceof SpecialCharacter<T> specialCharacter) {
                  MobDropUtil.handleDrops(e.level(), e.position().add(0.0, (double)(e.getBbHeight() / 2.0F), 0.0), specialCharacter.getmobDropList());
               }

               baseMob.onDeathByPlayer();
            }
         })
         .freeEntity(character, true);
   }

   public <T extends BaseMob> ScriptPerformerBuilder startFightWithDeathOrReturn(
      Character<T> character, IAttackChain<T> chain, IAnimationSource death, String pos, int delayTp, double speed
   ) {
      return this.startFight(character, chain.copy())
         .thenRun(() -> System.out.println("FIGHT_STOP with " + character.getReferenceId()))
         .thenIf(
            () -> character.repositoryEntity() != null && ((BaseMob)character.repositoryEntity()).getHealth() <= 1.0F,
            builder(this.performer.script)
               .action(character, death)
               .thenDelay(death)
               .thenUseEntity(
                  character,
                  e -> {
                     SoundUtils.playVolumedSound(e.level(), e.blockPosition(), SoundsST.MOB_DISINTEGRATE);
                     ParticleHelper.spawnParticleAABB(
                        e.level(),
                        ParticleTypes.CLOUD,
                        e.getBoundingBox().contract(0.0, e.getBoundingBox().getYsize() / 2.0, 0.0).inflate(0.7, 0.0, 0.7),
                        20,
                        0.01
                     );
                  }
               )
               .thenDelay(6)
               .thenRun(() -> {
                  Entity e = IStoryServer.get().getEntityRepository().getEntity(character);
                  if (e instanceof BaseMob baseMob) {
                     if (character instanceof SpecialCharacter<T> specialCharacter) {
                        MobDropUtil.handleDrops(e.level(), e.position().add(0.0, (double)(e.getBbHeight() / 2.0F), 0.0), specialCharacter.getmobDropList());
                     }

                     baseMob.onDeathByPlayer();
                  }
               })
               .freeEntity(character, true)
               .build(),
            builder(this.performer.script)
               .enableFight(character, false)
               .thenAnyPerformer(
                  new Performer[]{
                     builder(this.performer.script).thenEntityPath(character, speed, pos).entityLookAtFromRot(character, pos).build(),
                     builder(this.performer.script).thenDelay(delayTp).positionEntity(character, pos).build()
                  }
               )
               .thenUseEntity(character, e -> e.setHealth(e.getMaxHealth()))
               .thenRun(() -> this.performer.script.getFlags().set("fight_return" + character.getReferenceId()))
               .build()
         );
   }

   public <T extends BaseMob> ScriptPerformerBuilder startFightWithDeathOrReturn(
      Character<T> character, IAttackChain<T> chain, IAnimationSource death, String pos
   ) {
      return this.startFight(character, chain.copy())
         .thenRun(() -> System.out.println("FIGHT_STOP with " + character.getReferenceId()))
         .thenIf(
            () -> character.repositoryEntity() != null && ((BaseMob)character.repositoryEntity()).getHealth() <= 1.0F,
            builder(this.performer.script)
               .action(character, death)
               .thenDelay(death)
               .thenUseEntity(
                  character,
                  e -> {
                     SoundUtils.playVolumedSound(e.level(), e.blockPosition(), SoundsST.MOB_DISINTEGRATE);
                     ParticleHelper.spawnParticleAABB(
                        e.level(),
                        ParticleTypes.CLOUD,
                        e.getBoundingBox().contract(0.0, e.getBoundingBox().getYsize() / 2.0, 0.0).inflate(0.7, 0.0, 0.7),
                        20,
                        0.01
                     );
                  }
               )
               .thenDelay(6)
               .thenRun(() -> {
                  Entity e = IStoryServer.get().getEntityRepository().getEntity(character);
                  if (e instanceof BaseMob baseMob && character instanceof SpecialCharacter<T> specialCharacter) {
                     MobDropUtil.handleDrops(e.level(), e.position().add(0.0, (double)(e.getBbHeight() / 2.0F), 0.0), specialCharacter.getmobDropList());
                  }
               })
               .freeEntity(character, true)
               .build(),
            builder(this.performer.script)
               .enableFight(character, false)
               .thenAnyPerformer(
                  new Performer[]{
                     builder(this.performer.script).thenEntityPath(character, pos).entityLookAtFromRot(character, pos).build(),
                     builder(this.performer.script).thenDelay(600).positionEntity(character, pos).build()
                  }
               )
               .thenUseEntity(character, e -> e.setHealth(e.getMaxHealth()))
               .thenRun(() -> this.performer.script.getFlags().set("fight_return" + character.getReferenceId()))
               .build()
         );
   }

   public <T extends BaseMob> ScriptPerformerBuilder startFightWithDeathOrReturnForBurnZombie(
      Character<T> character, IAttackChain<T> chain, IAnimationSource death, String pos
   ) {
      return this.startFight(character, chain.copy())
         .thenRun(() -> System.out.println("FIGHT_STOP with " + character.getReferenceId()))
         .thenIf(
            () -> character.repositoryEntity() != null && ((BaseMob)character.repositoryEntity()).getHealth() <= 1.0F,
            builder(this.performer.script)
               .action(character, death)
               .thenDelay(death)
               .thenUseEntity(
                  character,
                  e -> {
                     SoundUtils.playVolumedSound(e.level(), e.blockPosition(), SoundsST.MOB_DISINTEGRATE);
                     ParticleHelper.spawnParticleAABB(
                        e.level(),
                        ParticleTypes.CLOUD,
                        e.getBoundingBox().contract(0.0, e.getBoundingBox().getYsize() / 2.0, 0.0).inflate(0.7, 0.0, 0.7),
                        20,
                        0.01
                     );
                  }
               )
               .thenDelay(6)
               .thenIf(() -> true, builder(this.performer.script).thenRun(() -> {
                  Entity e = IStoryServer.get().getEntityRepository().getEntity(character);
                  if (e instanceof BaseMob baseMob) {
                     if (character instanceof SpecialCharacter<T> specialCharacter) {
                        MobDropUtil.handleDrops(e.level(), e.position().add(0.0, (double)(e.getBbHeight() / 2.0F), 0.0), specialCharacter.getmobDropList());
                     }

                     baseMob.onDeathByPlayer();
                  }
               }).build(), builder(this.performer.script).build())
               .freeEntity(character, true)
               .build(),
            builder(this.performer.script)
               .enableFight(character, false)
               .thenAnyPerformer(
                  new Performer[]{
                     builder(this.performer.script).thenEntityPath(character, pos).entityLookAtFromRot(character, pos).build(),
                     builder(this.performer.script).thenDelay(600).positionEntity(character, pos).build()
                  }
               )
               .thenUseEntity(character, e -> e.setHealth(e.getMaxHealth()))
               .thenRun(() -> this.performer.script.getFlags().set("fight_return" + character.getReferenceId()))
               .build()
         );
   }

   public <T extends BaseMob> ScriptPerformerBuilder startFightWithDeathOrReturnS2(
      Character<T> character, IAttackChain<T> chain, IAnimationSource death, String pos
   ) {
      return this.startFight(character, chain.copy())
         .thenIf(
            () -> character.repositoryEntity() != null && ((BaseMob)character.repositoryEntity()).getHealth() <= 1.0F,
            builder(this.performer.script)
               .action(character, death)
               .thenDelay(death)
               .thenUseEntity(
                  character,
                  e -> {
                     SoundUtils.playVolumedSound(e.level(), e.blockPosition(), SoundsST.MOB_DISINTEGRATE);
                     ParticleHelper.spawnParticleAABB(
                        e.level(),
                        ParticleTypes.CLOUD,
                        e.getBoundingBox().contract(0.0, e.getBoundingBox().getYsize() / 2.0, 0.0).inflate(0.7, 0.0, 0.7),
                        20,
                        0.01
                     );
                  }
               )
               .thenDelay(6)
               .thenRun(() -> {
                  BaseMob baseMob = IStoryServer.get().getEntityRepository().getEntity(character);
                  if (baseMob != null) {
                     baseMob.onDeathByPlayer();
                  }
               })
               .freeEntity(character, true)
               .thenDelay(50)
               .thenRun(() -> this.performer.script.getFlags().set("fight_return" + character.getReferenceId()))
               .build(),
            builder(this.performer.script)
               .thenAnyPerformer(
                  new Performer[]{
                     builder(this.performer.script).thenEntityPath(character, pos).entityLookAtFromRot(character, pos).build(),
                     builder(this.performer.script).thenDelay(600).positionEntity(character, pos).build()
                  }
               )
               .thenUseEntity(character, e -> e.setHealth(e.getMaxHealth()))
               .thenRun(() -> this.performer.script.getFlags().set("fight_return" + character.getReferenceId()))
               .build()
         );
   }

   public <T extends Entity> ScriptPerformerBuilder createTelepornEffect(Character<T> character, String location) {
      return this.thenRun(() -> {
            EntityState<T> state = this.performer.script.getRepository().getOrCreate(character);
            state.setAlphaFadeable(1.0F);
         })
         .thenRun(
            () -> ParticleHelper.spawnDirectedParticle(
                  ScriptUtils.getLevel(),
                  new CircleTintData(new Color(155, 155, 155), 2.1F, 20, 6, -1.0F, false, false, new StandartController()),
                  new Location(location).add(0.0, 1.0, 0.0),
                  Vec3.ZERO
               )
         )
         .thenDelay(20)
         .positionEntity(character, location)
         .createTicker("teleporn_create_" + character.getReferenceId(), p -> {
            EntityState<T> state = this.performer.script.getRepository().getOrCreate(character);
            if (!(state.getData().getAlphaFadeable() < 0.0F)) {
               state.setAlphaFadeable(state.getData().getAlphaFadeable() - 0.02F);
            }
         })
         .thenUseEntity(
            character,
            e -> ParticleHelper.spawnDirectedParticle(
                  e.level(),
                  new CircleTintData(new Color(130, 145, 255), 2.5F, 4, 5, -1.0F, false, false, new StandartController()),
                  e.position().add(e.getLookAngle().normalize().scale(0.35).x, 1.0, e.getLookAngle().normalize().scale(0.35).z),
                  Vec3.ZERO
               )
         )
         .thenDelay(10)
         .thenRun(() -> {
            EntityState<T> state = this.performer.script.getRepository().get(character);
            if (state != null) {
               state.setAlphaFadeable(0.0F);
            }
         })
         .deleteTicker("teleporn_create_" + character.getReferenceId());
   }

   public <T extends Entity> ScriptPerformerBuilder createTelepornEffect(Character<T> character, Supplier<Location> location) {
      return this.thenRun(() -> {
            EntityState<T> state = this.performer.script.getRepository().getOrCreate(character);
            state.setAlphaFadeable(1.0F);
         })
         .thenRun(
            () -> ParticleHelper.spawnDirectedParticle(
                  ScriptUtils.getLevel(),
                  new CircleTintData(new Color(155, 155, 155), 2.1F, 20, 6, -1.0F, false, false, new StandartController()),
                  location.get().add(0.0, 1.0, 0.0),
                  Vec3.ZERO
               )
         )
         .thenDelay(20)
         .positionEntity(character, location)
         .createTicker("teleporn_create_" + character.getReferenceId(), p -> {
            EntityState<T> state = this.performer.script.getRepository().getOrCreate(character);
            if (!(state.getData().getAlphaFadeable() < 0.0F)) {
               state.setAlphaFadeable(state.getData().getAlphaFadeable() - 0.02F);
            }
         })
         .thenUseEntity(
            character,
            e -> ParticleHelper.spawnDirectedParticle(
                  e.level(),
                  new CircleTintData(new Color(130, 145, 255), 2.5F, 4, 5, -1.0F, false, false, new StandartController()),
                  e.position().add(e.getLookAngle().normalize().scale(0.35).x, 1.0, e.getLookAngle().normalize().scale(0.35).z),
                  Vec3.ZERO
               )
         )
         .thenDelay(10)
         .thenRun(() -> {
            EntityState<T> state = this.performer.script.getRepository().get(character);
            if (state != null) {
               state.setAlphaFadeable(0.0F);
            }
         })
         .deleteTicker("teleporn_create_" + character.getReferenceId());
   }

   public <T extends AnimatedEntity> ScriptPerformerBuilder createTelepornEffect(Character<T> character, String location, IAnimationSource iAnimationSource) {
      return this.thenRun(() -> {
            EntityState<T> state = this.performer.script.getRepository().getOrCreate(character);
            state.setAlphaFadeable(1.0F);
         })
         .thenRun(
            () -> ParticleHelper.spawnDirectedParticle(
                  ScriptUtils.getLevel(),
                  new CircleTintData(new Color(155, 155, 155), 2.1F, 20, 6, -1.0F, false, false, new StandartController()),
                  new Location(location).add(0.0, 1.0, 0.0),
                  Vec3.ZERO
               )
         )
         .thenDelay(20)
         .positionEntity(character, location)
         .entitySit(character)
         .animation(character, 7, iAnimationSource)
         .createTicker("teleporn_create_" + character.getReferenceId(), p -> {
            EntityState<T> state = this.performer.script.getRepository().getOrCreate(character);
            if (!(state.getData().getAlphaFadeable() < 0.0F)) {
               state.setAlphaFadeable(state.getData().getAlphaFadeable() - 0.02F);
            }
         })
         .thenUseEntity(
            character,
            e -> ParticleHelper.spawnDirectedParticle(
                  e.level(),
                  new CircleTintData(new Color(130, 145, 255), 2.5F, 4, 5, -1.0F, false, false, new StandartController()),
                  e.position().add(e.getLookAngle().normalize().scale(0.35).x, 1.0, e.getLookAngle().normalize().scale(0.35).z),
                  Vec3.ZERO
               )
         )
         .thenDelay(10)
         .thenRun(() -> {
            EntityState<T> state = this.performer.script.getRepository().get(character);
            if (state != null) {
               state.setAlphaFadeable(0.0F);
            }
         })
         .deleteTicker("teleporn_create_" + character.getReferenceId());
   }

   public <T extends AnimatedEntity> ScriptPerformerBuilder createTelepornEffect(
      Character<T> character, Supplier<Location> location, IAnimationSource iAnimationSource
   ) {
      AtomicReference<Location> loc = new AtomicReference<>(new Location(Vec3.ZERO));
      return this.thenRun(() -> {
            EntityState<T> state = this.performer.script.getRepository().getOrCreate(character);
            state.setAlphaFadeable(1.0F);
         })
         .thenRun(
            () -> {
               loc.set(location.get());
               ParticleHelper.spawnDirectedParticle(
                  ScriptUtils.getLevel(),
                  new CircleTintData(new Color(155, 155, 155), 2.1F, 20, 6, -1.0F, false, false, new StandartController()),
                  loc.get().add(0.0, 1.0, 0.0),
                  Vec3.ZERO
               );
            }
         )
         .thenDelay(20)
         .positionEntity(character, loc::get)
         .entitySit(character)
         .animation(character, 7, iAnimationSource)
         .createTicker("teleporn_create_" + character.getReferenceId(), p -> {
            EntityState<T> state = this.performer.script.getRepository().getOrCreate(character);
            if (!(state.getData().getAlphaFadeable() < 0.0F)) {
               state.setAlphaFadeable(state.getData().getAlphaFadeable() - 0.02F);
            }
         })
         .thenUseEntity(
            character,
            e -> ParticleHelper.spawnDirectedParticle(
                  e.level(),
                  new CircleTintData(new Color(130, 145, 255), 2.5F, 4, 5, -1.0F, false, false, new StandartController()),
                  e.position().add(e.getLookAngle().normalize().scale(0.35).x, 1.0, e.getLookAngle().normalize().scale(0.35).z),
                  Vec3.ZERO
               )
         )
         .thenDelay(10)
         .thenRun(() -> {
            EntityState<T> state = this.performer.script.getRepository().get(character);
            if (state != null) {
               state.setAlphaFadeable(0.0F);
            }
         })
         .deleteTicker("teleporn_create_" + character.getReferenceId());
   }

   public <T extends Entity> ScriptPerformerBuilder createTelepornEffect(Character<T> character, String location, float offset_y) {
      return this.thenRun(() -> {
            EntityState<T> state = this.performer.script.getRepository().getOrCreate(character);
            state.setAlphaFadeable(1.0F);
         })
         .thenRun(
            () -> ParticleHelper.spawnDirectedParticle(
                  ScriptUtils.getLevel(),
                  new CircleTintData(new Color(155, 155, 155), 2.1F, 20, 6, -1.0F, false, false, new StandartController()),
                  new Location(location).add(0.0, (double)(1.0F + offset_y), 0.0),
                  Vec3.ZERO
               )
         )
         .thenDelay(20)
         .positionEntity(character, location)
         .createTicker("teleporn_create_" + character.getReferenceId(), p -> {
            EntityState<T> state = this.performer.script.getRepository().getOrCreate(character);
            if (!(state.getData().getAlphaFadeable() < 0.0F)) {
               state.setAlphaFadeable(state.getData().getAlphaFadeable() - 0.02F);
            }
         })
         .thenUseEntity(
            character,
            e -> ParticleHelper.spawnDirectedParticle(
                  e.level(),
                  new CircleTintData(new Color(130, 145, 255), 2.5F, 4, 5, -1.0F, false, false, new StandartController()),
                  e.position().add(e.getLookAngle().normalize().scale(0.35).x, (double)(1.0F + offset_y), e.getLookAngle().normalize().scale(0.35).z),
                  Vec3.ZERO
               )
         )
         .thenDelay(10)
         .thenRun(() -> {
            EntityState<T> state = this.performer.script.getRepository().get(character);
            if (state != null) {
               state.setAlphaFadeable(0.0F);
            }
         })
         .deleteTicker("teleporn_create_" + character.getReferenceId());
   }

   public <T extends AnimatedEntity> ScriptPerformerBuilder createTelepornEffect(
      Character<T> character, String location, IAnimationSource iAnimationSource, float offset_y
   ) {
      return this.thenRun(() -> {
            EntityState<T> state = this.performer.script.getRepository().getOrCreate(character);
            state.setAlphaFadeable(1.0F);
         })
         .thenRun(
            () -> ParticleHelper.spawnDirectedParticle(
                  ScriptUtils.getLevel(),
                  new CircleTintData(new Color(155, 155, 155), 2.1F, 20, 6, -1.0F, false, false, new StandartController()),
                  new Location(location).add(0.0, (double)(1.0F + offset_y), 0.0),
                  Vec3.ZERO
               )
         )
         .thenDelay(20)
         .positionEntity(character, location)
         .entitySit(character)
         .animation(character, 7, iAnimationSource)
         .createTicker("teleporn_create_" + character.getReferenceId(), p -> {
            EntityState<T> state = this.performer.script.getRepository().getOrCreate(character);
            if (!(state.getData().getAlphaFadeable() < 0.0F)) {
               state.setAlphaFadeable(state.getData().getAlphaFadeable() - 0.02F);
            }
         })
         .thenUseEntity(
            character,
            e -> ParticleHelper.spawnDirectedParticle(
                  e.level(),
                  new CircleTintData(new Color(130, 145, 255), 2.5F, 4, 5, -1.0F, false, false, new StandartController()),
                  e.position().add(e.getLookAngle().normalize().scale(0.35).x, (double)(1.0F + offset_y), e.getLookAngle().normalize().scale(0.35).z),
                  Vec3.ZERO
               )
         )
         .thenDelay(10)
         .thenRun(() -> {
            EntityState<T> state = this.performer.script.getRepository().get(character);
            if (state != null) {
               state.setAlphaFadeable(0.0F);
            }
         })
         .deleteTicker("teleporn_create_" + character.getReferenceId());
   }

   public <T extends Entity> ScriptPerformerBuilder createTelepornEffectWithBuilder(Character<T> character, String location, ScriptPerformer performer) {
      return this.thenRun(() -> {
            EntityState<T> state = this.performer.script.getRepository().getOrCreate(character);
         })
         .createTicker("teleporn_create_" + character.getReferenceId(), p -> {
            EntityState<T> state = this.performer.script.getRepository().getOrCreate(character);
            if (!(state.getData().getAlphaFadeable() >= 1.0F)) {
               state.setAlphaFadeable(state.getData().getAlphaFadeable() + 0.05F);
            }
         })
         .thenRun(
            () -> ParticleHelper.spawnDirectedParticle(
                  ScriptUtils.getLevel(),
                  new CircleTintData(new Color(155, 155, 155), 2.1F, 20, 6, -1.0F, false, false, new StandartController()),
                  new Location(location).add(0.0, 1.0, 0.0),
                  Vec3.ZERO
               )
         )
         .thenDelay(20)
         .deleteTicker("teleporn_create_" + character.getReferenceId())
         .appendBuilder(b -> b.thenAllPerformers(new Performer[]{performer}))
         .createTicker("teleporn_create_" + character.getReferenceId(), p -> {
            EntityState<T> state = this.performer.script.getRepository().getOrCreate(character);
            if (!(state.getData().getAlphaFadeable() < 0.0F)) {
               state.setAlphaFadeable(state.getData().getAlphaFadeable() - 0.02F);
            }
         })
         .thenUseEntity(
            character,
            e -> ParticleHelper.spawnDirectedParticle(
                  e.level(),
                  new CircleTintData(new Color(130, 145, 255), 2.5F, 4, 5, -1.0F, false, false, new StandartController()),
                  e.position().add(e.getLookAngle().normalize().scale(0.35).x, 1.0, e.getLookAngle().normalize().scale(0.35).z),
                  Vec3.ZERO
               )
         )
         .thenDelay(10)
         .thenRun(() -> {
            EntityState<T> state = this.performer.script.getRepository().get(character);
            if (state != null) {
               state.setAlphaFadeable(0.0F);
            }
         })
         .deleteTicker("teleporn_create_" + character.getReferenceId());
   }

   public <T extends Entity> ScriptPerformerBuilder deleteTelepornEffect(Character<T> character) {
      return this.thenRun(() -> {
            EntityState<T> state = this.performer.script.getRepository().getOrCreate(character);
            state.setAlphaFadeable(0.0F);
         })
         .createTicker("teleporn_delete_" + character.getReferenceId(), p -> {
            EntityState<T> state = this.performer.script.getRepository().getOrCreate(character);
            if (!(state.getData().getAlphaFadeable() > 1.0F)) {
               state.setAlphaFadeable(state.getData().getAlphaFadeable() + 0.02F);
            }
         })
         .thenUseEntity(
            character,
            e -> ParticleHelper.spawnDirectedParticle(
                  e.level(),
                  new CircleTintData(new Color(155, 155, 155), 2.1F, 40, 6, -1.0F, false, false, new StandartController()),
                  e.position().add(0.0, (double)(e.getBbHeight() / 2.0F), 0.0),
                  Vec3.ZERO
               )
         )
         .thenDelay(30)
         .thenUseEntity(
            character,
            e -> ParticleHelper.spawnDirectedParticle(
                  e.level(),
                  new CircleTintData(new Color(130, 145, 255), 2.5F, 4, 5, -1.0F, false, false, new StandartController()),
                  e.position().add(0.0, (double)(e.getBbHeight() / 2.0F), 0.0),
                  Vec3.ZERO
               )
         )
         .thenDelay(5)
         .thenDelay(1)
         .thenUseEntity(character, Entity::discard)
         .thenDelay(1)
         .freeEntity(character, true)
         .deleteTicker("teleporn_delete_" + character.getReferenceId());
   }

   public <T extends Entity> ScriptPerformerBuilder deleteTelepornEffectCustomCoord(Character<T> character, String loc) {
      Location location = new Location(loc);
      return this.thenRun(() -> {
            EntityState<T> state = this.performer.script.getRepository().getOrCreate(character);
            state.setAlphaFadeable(0.0F);
         })
         .createTicker("teleporn_delete_" + character.getReferenceId(), p -> {
            EntityState<T> state = this.performer.script.getRepository().getOrCreate(character);
            if (!(state.getData().getAlphaFadeable() > 1.0F)) {
               state.setAlphaFadeable(state.getData().getAlphaFadeable() + 0.02F);
            }
         })
         .thenUseEntity(
            character,
            e -> ParticleHelper.spawnDirectedParticle(
                  e.level(),
                  new CircleTintData(new Color(155, 155, 155), 2.1F, 40, 6, -1.0F, false, false, new StandartController()),
                  location.add(0.0, (double)(e.getBbHeight() / 2.0F), 0.0),
                  Vec3.ZERO
               )
         )
         .thenDelay(30)
         .thenUseEntity(
            character,
            e -> ParticleHelper.spawnDirectedParticle(
                  e.level(),
                  new CircleTintData(new Color(130, 145, 255), 2.5F, 4, 5, -1.0F, false, false, new StandartController()),
                  location.add(0.0, (double)(e.getBbHeight() / 2.0F), 0.0),
                  Vec3.ZERO
               )
         )
         .thenDelay(5)
         .thenDelay(1)
         .thenUseEntity(character, Entity::discard)
         .thenDelay(1)
         .freeEntity(character, true)
         .deleteTicker("teleporn_delete_" + character.getReferenceId());
   }

   public <T extends Entity> ScriptPerformerBuilder deleteTelepornEffectPlayer() {
      return this.thenUsePlayer(
            p -> ParticleHelper.spawnDirectedParticle(
                  p.level(),
                  new CircleTintData(new Color(155, 155, 155), 2.1F, 40, 6, -1.0F, false, false, new StandartController()),
                  p.position().add(0.0, (double)(p.getBbHeight() / 2.0F), 0.0),
                  Vec3.ZERO
               )
         )
         .thenDelay(30)
         .thenUsePlayer(
            p -> ParticleHelper.spawnDirectedParticle(
                  p.level(),
                  new CircleTintData(new Color(130, 145, 255), 2.5F, 4, 5, -1.0F, false, false, new StandartController()),
                  p.position().add(0.0, (double)(p.getBbHeight() / 2.0F), 0.0),
                  Vec3.ZERO
               )
         )
         .thenDelay(5);
   }

   public <T extends Entity> ScriptPerformerBuilder createSparkEffect(Character<T> character, String loc) {
      return this.positionEntity(character, loc)
         .thenUseEntity(
            character,
            e -> {
               for (int i = 0; i < 2; i++) {
                  Color golden = new Color(255, 179, 62);
                  ParticleHelper.spawnParticleAABB(
                     e.level(),
                     new CircleTintData(golden, 0.17F, 40, -1.0F, true, false, new GravityController()),
                     e.getBoundingBox().inflate(0.1, 0.0, 0.1),
                     30,
                     0.086666666F
                  );
                  ParticleHelper.spawnParticleAABB(
                     e.level(),
                     new CircleTintData(golden, 0.17F, 40, -1.0F, true, false, new GravityController()),
                     e.getBoundingBox().inflate(0.1, 0.0, 0.1),
                     30,
                     0.086666666F
                  );
               }
            }
         );
   }

   public <T extends Entity> ScriptPerformerBuilder deleteSparkEffect(Character<T> character) {
      return this.thenUseEntity(
            character,
            e -> {
               for (int i = 0; i < 2; i++) {
                  Color golden = new Color(255, 179, 62);
                  ParticleHelper.spawnParticleAABB(
                     e.level(),
                     new CircleTintData(golden, 0.17F, 40, -1.0F, true, false, new GravityController()),
                     e.getBoundingBox().inflate(0.1, 0.0, 0.1),
                     30,
                     0.086666666F
                  );
                  ParticleHelper.spawnParticleAABB(
                     e.level(),
                     new CircleTintData(golden, 0.17F, 40, -1.0F, true, false, new GravityController()),
                     e.getBoundingBox().inflate(0.1, 0.0, 0.1),
                     30,
                     0.086666666F
                  );
               }
            }
         )
         .freeEntity(character, true);
   }

   public ScriptPerformerBuilder iconAdd(Character<?> character, ItemStack stack) {
      return this.append(performer -> new AddRenderedIcon(performer, character, stack));
   }

   public ScriptPerformerBuilder iconAdd(Character<?> character, ItemStack stack, Vec3 offset) {
      return this.append(performer -> new AddRenderedIcon(performer, character, stack, offset));
   }

   public ScriptPerformerBuilder iconRemove(Character<?> character) {
      return this.append(performer -> new RemoveRenderedIcon(performer, character));
   }

   public ScriptPerformerBuilder characterEmotion(Character<?> character, Emotion... emotion) {
      return this.append(performer -> new SetRenderedEmotion(performer, character.getReferenceId(), emotion, BubbleType.SPEECH));
   }

   public ScriptPerformerBuilder addCharacterEmotion(Character<?> character, Emotion emotion) {
      return this.append(performer -> new AddRenderedEmotion(performer, character.getReferenceId(), emotion, BubbleType.SPEECH));
   }

   public ScriptPerformerBuilder characterThinkingEmotion(Character<?> character, Emotion... emotion) {
      return this.append(performer -> new SetRenderedEmotion(performer, character.getReferenceId(), emotion, BubbleType.THINK));
   }

   public ScriptPerformerBuilder addCharacterThinkingEmotion(Character<?> character, Emotion emotion) {
      return this.append(performer -> new AddRenderedEmotion(performer, character.getReferenceId(), emotion, BubbleType.THINK));
   }

   public ScriptPerformerBuilder clearCharacterEmotion(Character<?> character) {
      return this.append(performer -> new ClearRenderedEmotion(performer, character.getReferenceId()));
   }

   public ScriptPerformerBuilder playerEmotion(Emotion... emotion) {
      return this.append(performer -> new SetRenderedEmotion(performer, "player", emotion, BubbleType.SPEECH));
   }

   public ScriptPerformerBuilder addPlayerEmotion(Emotion emotion) {
      return this.append(performer -> new AddRenderedEmotion(performer, "player", emotion, BubbleType.SPEECH));
   }

   public ScriptPerformerBuilder playerThinkingEmotion(Emotion... emotion) {
      return this.append(performer -> new SetRenderedEmotion(performer, "player", emotion, BubbleType.THINK));
   }

   public ScriptPerformerBuilder addPlayerThinkingEmotion(Emotion emotion) {
      return this.append(performer -> new AddRenderedEmotion(performer, "player", emotion, BubbleType.THINK));
   }

   public ScriptPerformerBuilder clearPlayerEmotion() {
      return this.append(performer -> new ClearRenderedEmotion(performer, "player"));
   }

   public <T extends Entity> ScriptPerformerBuilder createTelepornGlitchEffect(Character<T> character, String location) {
      return this.createTicker("teleporn_glitch_create_" + character.getReferenceId(), p -> {
         Random random = new Random();
         EntityDimensions dim = character.getType().getDimensions();
         Location position = new Location(location);
         double expansion = 0.2;
         double halfWidth = (double)dim.width / 2.0 + expansion;
         double minX = position.x - halfWidth;
         double maxX = position.x + halfWidth;
         double minY = position.y;
         double maxY = position.y + (double)dim.height + expansion;
         double minZ = position.z - halfWidth;
         double maxZ = position.z + halfWidth;

         for (int i = 0; i < 2; i++) {
            Location particleLocation = new Location(random.nextDouble(minX, maxX), random.nextDouble(minY, maxY), random.nextDouble(minZ, maxZ));
            ParticleHelper.spawnDirectedParticle(ScriptUtils.getLevel(), ParticleTypesST.GLITCH, particleLocation, Vec3.ZERO);
         }
      }).thenDelay(15).positionEntity(character, location).thenDelay(3).deleteTicker("teleporn_glitch_create_" + character.getReferenceId()).thenDelay(15);
   }

   public <T extends LivingEntity> ScriptPerformerBuilder createTelepornGlitchEffect(Character<T> character, String location, ItemStack heldItem) {
      return this.createTicker("teleporn_glitch_create_" + character.getReferenceId(), p -> {
            Random random = new Random();
            EntityDimensions dim = character.getType().getDimensions();
            Location position = new Location(location);
            double expansion = 0.2;
            double halfWidth = (double)dim.width / 2.0 + expansion;
            double minX = position.x - halfWidth;
            double maxX = position.x + halfWidth;
            double minY = position.y;
            double maxY = position.y + (double)dim.height + expansion;
            double minZ = position.z - halfWidth;
            double maxZ = position.z + halfWidth;

            for (int i = 0; i < 2; i++) {
               Location particleLocation = new Location(random.nextDouble(minX, maxX), random.nextDouble(minY, maxY), random.nextDouble(minZ, maxZ));
               ParticleHelper.spawnDirectedParticle(ScriptUtils.getLevel(), ParticleTypesST.GLITCH, particleLocation, Vec3.ZERO);
            }
         })
         .thenDelay(15)
         .positionEntity(character, location)
         .thenSetHeldItem(character, heldItem)
         .thenDelay(3)
         .deleteTicker("teleporn_glitch_create_" + character.getReferenceId())
         .thenDelay(15);
   }

   public <T extends Entity> ScriptPerformerBuilder deleteTelepornGlitchEffect(Character<T> character) {
      return this.createTicker("teleporn_glitch_delete_" + character.getReferenceId(), p -> {
            Entity e = character.repositoryEntity();
            if (e != null) {
               Random random = new Random();
               EntityDimensions dim = character.getType().getDimensions();
               Vec3 position = e.position();
               double expansion = 0.2;
               double halfWidth = (double)dim.width / 2.0 + expansion;
               double minX = position.x - halfWidth;
               double maxX = position.x + halfWidth;
               double minY = position.y;
               double maxY = position.y + (double)dim.height + expansion;
               double minZ = position.z - halfWidth;
               double maxZ = position.z + halfWidth;

               for (int i = 0; i < 2; i++) {
                  Location particleLocation = new Location(random.nextDouble(minX, maxX), random.nextDouble(minY, maxY), random.nextDouble(minZ, maxZ));
                  ParticleHelper.spawnDirectedParticle(ScriptUtils.getLevel(), ParticleTypesST.GLITCH, particleLocation, Vec3.ZERO);
               }
            }
         })
         .thenDelay(15)
         .thenUseEntity(character, Entity::discard)
         .thenDelay(1)
         .freeEntity(character, true)
         .deleteTicker("teleporn_glitch_delete_" + character.getReferenceId())
         .thenDelay(15);
   }

   public ScriptPerformerBuilder usePersona(final Persona persona) {
      return this.thenUsePlayer(p -> PersonaHandler.setPersona(p, persona)).thenHandleState(new HandleStateAction.StateHandler() {
         @Override
         public void onPassed() {
            PersonaHandler.setPersona(ScriptUtils.getPlayer(), persona);
         }
      });
   }

   public ScriptPerformer build() {
      Preconditions.checkArgument(!this.built, "ScriptPerformerBuilder is, sadly, capable of building only one instance of ScriptPerformer. :'(");
      this.built = true;
      return this.performer;
   }

   public ScriptPerformer build(Consumer<ScriptPerformer> registerer) {
      ScriptPerformer performer = this.build();
      performer.setName("@");
      registerer.accept(performer);
      return performer;
   }

   public ScriptPerformer build(String name, BiConsumer<String, ScriptPerformer> registerer) {
      ScriptPerformer performer = this.build();
      performer.setName(name);
      registerer.accept(name, performer);
      return performer;
   }

   public ScriptPerformerBuilder setSkyEyeDefaultPosition() {
      return this.append(SetSkyEyePosition::new);
   }

   public ScriptPerformerBuilder setSkyEyePosition(String location) {
      RuntimeException source = new RuntimeException();
      return this.append(performer -> new SetSkyEyePosition(performer, Cast.constant(PositionParser.parseLocationWithRotationOrException(location, source))));
   }

   public <T extends Entity> ScriptPerformerBuilder setSkyEyeLookTarget(Character<T> target) {
      return this.append(performer -> new SetSkyEyeLookTarget(performer, target.getReferenceId()));
   }

   public ScriptPerformerBuilder setSkyEyeLookTarget(String target) {
      return this.append(performer -> new SetSkyEyeLookTarget(performer, target));
   }

   public ScriptPerformerBuilder setSimulatorBlocking(MainTabType type, boolean state) {
      return this.append(performer -> new SetSimulatorBlocking(performer, type, state));
   }

   @Generated
   public ScriptPerformer getPerformer() {
      return this.performer;
   }

   @Generated
   public PerformerAction<?> getLastAction() {
      return this.lastAction;
   }

   @Generated
   public boolean isBuilt() {
      return this.built;
   }
}
