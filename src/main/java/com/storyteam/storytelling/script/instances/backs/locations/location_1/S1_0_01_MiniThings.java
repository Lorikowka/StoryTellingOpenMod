package com.storyteam.storytelling.script.instances.backs.locations.location_1;

import com.storyteam.storytelling.api.SaveOnClose;
import com.storyteam.storytelling.api.Script;
import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.client.Animations;
import com.storyteam.storytelling.content.entities.configurations.MovementActionType;
import com.storyteam.storytelling.content.entities.mobs.BaseMob;
import com.storyteam.storytelling.script.BackInstance;
import com.storyteam.storytelling.script.performer.Performer;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.builder.ScriptPerformerBuilder;
import com.storyteam.storytelling.script.performer.builder.reflective.ScriptPerformerBuilderReflective;
import com.storyteam.storytelling.script.performer.entity.behaviour.fight.FightAttacks;
import com.storyteam.storytelling.script.performer.entity.behaviour.fight.TargetSelector;
import com.storyteam.storytelling.script.performer.entity.behaviour.fight.chain.IAttackChain;
import com.storyteam.storytelling.script.performer.entity.behaviour.fight.fightsattacks.FlyTrapAttacks;
import com.storyteam.storytelling.script.story.ScriptEnvironment;
import com.storyteam.storytelling.script.story.characters.Characters;
import com.storyteam.storytelling.system.emotions.Emotion;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.FragmentInstanceManager;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.data.FragmentData;
import com.storyteam.storytelling.system.simulator.me_tab.init.FragmentsST;
import com.storyteam.storytelling.system.time.event.NewDayStartEvent;
import com.storyteam.storytelling.util.UtilST;
import lombok.Generated;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.zeith.hammeranims.api.animation.IAnimationSource;

@SaveOnClose
@Script(
   value = -1001,
   description = "Мини-объекты для 1 локации (Бабочка / Сундук / Плотоядный цветок / Клоунская коробка / Слизень)"
)
public class S1_0_01_MiniThings extends BackInstance {
   public static final int SCRIPT_ID = -1001;
   private static final String CAN_KILL_FLYTRAP_TAG = "canKillFlyTrap";
   private static final boolean CAN_KILL_FLYTRAP_DEFAULT = true;
   private static Boolean canKillFlyTrap = true;
   private static final String CAN_OPEN_CLOWN_BOX_TAG = "canOpenClownBox";
   private static final boolean CAN_OPEN_CLOWN_BOX_DEFAULT = true;
   private static Boolean canOpenClownBox = true;
   private boolean isFlytrapKilled = false;
   public final ScriptPerformer mainFlow = (new ScriptPerformerBuilderReflective(this) {
         @ScriptPerformerBuilderReflective.Secondary("fly_trap")
         private void flyTrap(S1_0_01_MiniThings inst) {
            this.positionEntity(Characters.FLYTRAP, "/execute in minecraft:overworld run tp @s -44.90 103.00 -447.11 -538.76 -0.45");
            this.entitySit(Characters.FLYTRAP);
            this.label("loop");
            this.overridePredefinedAnimation(Characters.FLYTRAP, MovementActionType.IDLE, Animations.NULL);
            this.enableFight(Characters.FLYTRAP, false);
            this.thenUseEntity(Characters.FLYTRAP, e -> e.setHealth(100.0F));
            this.entityLookAt(Characters.FLYTRAP, "");
            this.thenDynamicPerformer(b -> {
               IAnimationSource animation = S1_0_01_MiniThings.this.isFlytrapKilled ? Animations.FLYTRAP_DEAD : Animations.FLYTRAP_UNDERGROUND;
               b.action(Characters.FLYTRAP, animation);
               b.thenDelay(animation);
               b.thenRun(() -> S1_0_01_MiniThings.this.isFlytrapKilled = false);
            });
            this.thenWaitUntil(() -> S1_0_01_MiniThings.canKillFlyTrap);
            this.thenWhenPlayerCloseTo(Characters.FLYTRAP, 4.0);
            this.entityLookAt(Characters.FLYTRAP, "player");
            this.overridePredefinedAnimation(Characters.FLYTRAP, MovementActionType.IDLE, Animations.FLYTRAP_IDLE);
            this.action(Characters.FLYTRAP, new IAnimationSource[]{Animations.FLYTRAP_OUT_FROM_GROUND});
            this.thenDelay(Animations.FLYTRAP_OUT_FROM_GROUND);
            this.action(Characters.FLYTRAP, new IAnimationSource[]{Animations.FLYTRAP_ATTACK});
            this.thenDelay(Animations.FLYTRAP_ATTACK);
            this.thenIf(() -> {
               FragmentData fragmentToGive = FragmentsST.SCARY;
               return !FragmentInstanceManager.isActive(fragmentToGive);
            }, ScriptPerformerBuilder.builder(inst).addPlayerEmotion(Emotion.FEARFUL_FACE).thenRun(() -> {
               FragmentData fragmentToGive = FragmentsST.SCARY;
               FragmentInstanceManager.addActive(fragmentToGive);
            }).build());
            this.thenAnyPerformer(
               new Performer[]{
                  ScriptPerformerBuilder.builder(inst)
                     .enableFight(Characters.FLYTRAP, true)
                     .entitySit(Characters.FLYTRAP)
                     .startFight(Characters.FLYTRAP, S1_0_01_MiniThings.this.createFlyTrapFight())
                     .thenRun(() -> S1_0_01_MiniThings.setCanKillFlyTrap(false))
                     .thenRun(() -> S1_0_01_MiniThings.this.isFlytrapKilled = true)
                     .thenUseEntity(Characters.FLYTRAP, e -> {
                        Vec3 pos = e.position();
                        UtilST.dropEidos(e.level(), pos, 1, false);
                     })
                     .build(),
                  ScriptPerformerBuilder.builder(inst).thenWhenPlayerCloseTo(Characters.FLYTRAP, -6.0).build()
               }
            );
            this.gotoLabel("loop");
         }

         @ScriptPerformerBuilderReflective.Secondary("clown_box")
         private void clownBox(S1_0_01_MiniThings inst) {
            this.positionEntity(Characters.CLOWN_BOX, "/execute in minecraft:overworld run tp @s -48.27 103.00 -443.71 360.79 -1.20");
            this.entitySit(Characters.CLOWN_BOX);
            this.action(Characters.CLOWN_BOX, new IAnimationSource[]{Animations.CLOWN_BOX_OPEN_POSE});
            this.label("loop");
            this.thenWaitUntil(() -> S1_0_01_MiniThings.canOpenClownBox);
            this.action(Characters.CLOWN_BOX, new IAnimationSource[]{Animations.CLOWN_BOX_CLOSED_POSE});
            this.thenAwaitEntityClick(Characters.CLOWN_BOX);
            this.action(Characters.CLOWN_BOX, new IAnimationSource[]{Animations.CLOWN_BOX_OPEN_ENDANIMATED});
            this.thenDelay(20);
            this.addPlayerEmotion(Emotion.FACE_WITH_TEARS_OF_JOY);
            this.thenDelay(20);
            this.thenRun(() -> {
               FragmentData fragmentToGive = FragmentsST.FUNNY;
               FragmentInstanceManager.addActive(fragmentToGive);
            });
            this.thenUsePlayer(player -> player.heal(1.0F));
            this.thenRun(() -> S1_0_01_MiniThings.setCanOpenClownBox(false));
            this.gotoLabel("loop");
         }

         @ScriptPerformerBuilderReflective.Secondary("butterfly")
         private void butterfly(S1_0_01_MiniThings inst) {
            this.positionEntity(Characters.BUTTERFLY_1, "/execute in minecraft:overworld run tp @s -47.44 104.38 -434.57 -527.51 4.80");
            this.entitySit(Characters.BUTTERFLY_1);
            this.action(Characters.BUTTERFLY_1, new IAnimationSource[]{Animations.BUTTERFLY_FLY});
            this.createParallelPerformer(
               "butterfly_fly",
               ScriptPerformerBuilder.builder(inst)
                  .label("loop")
                  .thenEntityMove(Characters.BUTTERFLY_1, 0.05, "/execute in minecraft:overworld run tp @s -48.50 105.16 -434.77 -641.23 18.30")
                  .thenEntityMove(Characters.BUTTERFLY_1, 0.05, "/execute in minecraft:overworld run tp @s -49.51 104.42 -436.62 -568.32 27.75")
                  .thenEntityMove(Characters.BUTTERFLY_1, 0.05, "/execute in minecraft:overworld run tp @s -47.88 104.79 -438.06 -477.42 21.15")
                  .gotoLabel("loop")
                  .build()
            );
            this.label("loop");
            this.thenWaitUntil(() -> {
               FragmentData fragmentToGive = FragmentsST.BEAUTIFULLY;
               return !FragmentInstanceManager.isActive(fragmentToGive);
            });
            this.thenAwaitEntityClick(Characters.BUTTERFLY_1);
            this.addPlayerEmotion(Emotion.STAR_STRUCK);
            this.thenDelay(20);
            this.thenRun(() -> {
               FragmentData fragmentToGive = FragmentsST.BEAUTIFULLY;
               FragmentInstanceManager.addActive(fragmentToGive);
            });
            this.gotoLabel("loop", true);
         }
      })
      .build(this::registerMainPerformer);

   public S1_0_01_MiniThings(ScriptEnvironment env, int id) {
      super(env, id);
   }

   private IAttackChain<BaseMob> createFlyTrapFight() {
      return FightAttacks.<BaseMob>createStagedFight()
         .addAttack(1.0, FlyTrapAttacks.flyTrap1PunchAttack(5, TargetSelector.playerSelector(), 2.7F, 2.0F))
         .stopCondition(context -> (double)((BaseMob)context.getEntity()).getHealth() <= 1.5)
         .handleEvent(EventType.LIVING_KNOCK_BACK, (event, ctx) -> {
            if (event.getEntity() == ctx.getEntity()) {
               event.setStrength(0.0F);
            }
         })
         .build();
   }

   @SubscribeEvent
   public void updateOnNewDay(NewDayStartEvent event) {
      setCanKillFlyTrap(true);
      setCanOpenClownBox(true);
   }

   @Override
   public CompoundTag serializeNBT() {
      CompoundTag tag = super.serializeNBT();
      tag.putBoolean("canKillFlyTrap", canKillFlyTrap != null ? canKillFlyTrap : true);
      tag.putBoolean("canOpenClownBox", canOpenClownBox != null ? canOpenClownBox : true);
      return tag;
   }

   @Override
   public void deserializeNBT(CompoundTag nbt) {
      super.deserializeNBT(nbt);
      setCanKillFlyTrap(nbt.contains("canKillFlyTrap") ? nbt.getBoolean("canKillFlyTrap") : true);
      setCanKillFlyTrap(nbt.contains("canOpenClownBox") ? nbt.getBoolean("canOpenClownBox") : true);
   }

   @Generated
   public static Boolean getCanKillFlyTrap() {
      return canKillFlyTrap;
   }

   @Generated
   public static void setCanKillFlyTrap(Boolean canKillFlyTrap) {
      S1_0_01_MiniThings.canKillFlyTrap = canKillFlyTrap;
   }

   @Generated
   public static Boolean getCanOpenClownBox() {
      return canOpenClownBox;
   }

   @Generated
   public static void setCanOpenClownBox(Boolean canOpenClownBox) {
      S1_0_01_MiniThings.canOpenClownBox = canOpenClownBox;
   }
}
