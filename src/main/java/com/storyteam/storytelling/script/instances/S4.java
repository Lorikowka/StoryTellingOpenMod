package com.storyteam.storytelling.script.instances;

import com.storyteam.storytelling.api.ForgetOnClose;
import com.storyteam.storytelling.api.Script;
import com.storyteam.storytelling.client.animations.AnimationsST8;
import com.storyteam.storytelling.content.entities.AntEntity;
import com.storyteam.storytelling.init.BlocksST;
import com.storyteam.storytelling.init.ParticleTypesST;
import com.storyteam.storytelling.init.seasonal.MorphsST8;
import com.storyteam.storytelling.script.ScriptInstance;
import com.storyteam.storytelling.script.performer.Performer;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.builder.ScriptPerformerBuilder;
import com.storyteam.storytelling.script.performer.builder.reflective.ScriptPerformerBuilderReflective;
import com.storyteam.storytelling.script.story.ScriptEnvironment;
import com.storyteam.storytelling.script.story.characters.CharacterList;
import com.storyteam.storytelling.script.story.characters.CharactersST8;
import com.storyteam.storytelling.system._core.st_cutscene.InterpolationType;
import com.storyteam.storytelling.system._core.st_cutscene.SplineType;
import com.storyteam.storytelling.system.emotions.Emotion;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.FragmentInstanceManager;
import com.storyteam.storytelling.system.simulator.me_tab.init.FragmentsST;
import com.storyteam.storytelling.util.ParticleHelper;
import com.storyteam.storytelling.util.ScriptUtils;
import com.storyteam.storytelling.util.world.Location;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.zeith.hammeranims.api.animation.IAnimationSource;
import org.zeith.hammerlib.api.io.NBTSerializable;

@ForgetOnClose
@Script(4)
public class S4 extends ScriptInstance {
   private static final CharacterList<AntEntity> ANTS = CharactersST8.ANT_FAC.createCharacterList("s1_ants", 18);
   @NBTSerializable
   public ScriptPerformer main = (new ScriptPerformerBuilderReflective(this) {
         @ScriptPerformerBuilderReflective.ScriptPart(0)
         private void part0(S4 inst) {
            this.saveState("0");
            this.thenSetSpeechEntity(0, "???", "sky_eye");
            this.thenSetSpeechEntity(1, "???", "lo_icon");
            this.thenSetSpeechEntity(2, "???", "anu_icon");
            this.setTime(1, "14:00");
            this.sendWebWarning("Начало на плоту, по F8 начало диалога");
            this.thenWaitForF8();
            this.blackout(50);
            this.thenRunMorph(MorphsST8.SPOODER);
            this.teleportPlayer("/execute in minecraft:overworld run tp @s -22.03 103.00 -467.45 -232.74 -6.00");
            this.thenDelay(60);
            this.thenSpeechMessage(0, "Ты уже не грустишь?");
            this.playerEmotion(new Emotion[]{Emotion.SLIGHTLY_SMILING_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(0, "Что ж, я рад.");
            this.thenSpeechMessage(0, "Я решил дать тебе немного времени, чтобы справиться с эмоциями.");
            this.thenSpeechMessage(0, "И ты справился.");
            this.thenSpeechMessage(0, "Ты развиваешься быстрее, чем многие другие существа.");
            this.playerEmotion(new Emotion[]{Emotion.PARTYING_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(0, "Я думаю, что пришло время…");
            this.thenSpeechMessage(0, "…Отдать тебе часть контроля.");
            this.thenSpeechMessage(0, "Взросление — это в каком-то смысле и есть обретение контроля.");
            this.playerThinkingEmotion(new Emotion[]{Emotion.THINKING_FACE});
            this.thenDelay(20);
            this.thenRun(() -> FragmentInstanceManager.addActive(FragmentsST.GROW_UP));
            this.thenSpeechMessage(0, "Только не забывай, что с контролем приходит и ответственность.");
            this.thenSpeechMessage(0, "Но мне кажется, что ты готов.");
            this.playerEmotion(new Emotion[]{Emotion.SMILING_FACE_WITH_SUNGLASSES});
            this.thenDelay(20);
            this.thenSpeechMessage(0, "…");
            this.playerEmotion(new Emotion[]{Emotion.EYES});
            this.thenDelay(20);
            this.thenSpeechMessage(0, "…");
            this.playerEmotion(new Emotion[]{Emotion.FACE_WITH_RAISED_EYEBROW});
            this.thenDelay(20);
            this.thenSpeechMessage(0, "…");
            this.thenSpeechMessage(0, "Так а ты разве сам не знаешь, что делать?");
            this.playerEmotion(new Emotion[]{Emotion.FACE_WITH_ROLLING_EYES});
            this.thenDelay(20);
            this.sendWebWarning("Когда глаз будет смеяться, озвучь его смех");
            this.action(CharactersST8.SKY_EYE, new IAnimationSource[]{AnimationsST8.SKYEYE_LAUGH});
            this.thenDelay(AnimationsST8.SKYEYE_LAUGH);
            this.thenSpeechMessage(0, "Давай, не ленись.");
            this.saveState("1");
            this.sendWebWarning("Иди к координатам 145 108.00 418 там будет партикл, когда подойдешь начнется катсцена");
            this.thenHandleState(S4.this.createFlag("particle"));
            this.thenWhenPlayerCloseTo("/execute in minecraft:overworld run tp @s -145.62 108.00 -418.59 -270.24 -1.50", 2.0);
            this.saveState("2");
            this.blackout(40);
            this.thenHandleState(S4.this.clearFlag("particle"));
            this.positionEntity(S4.ANTS.get(0), "/execute in minecraft:overworld run tp @s -163.93 108.00 -424.42 -400.58 1.20");
            this.positionEntity(S4.ANTS.get(1), "/execute in minecraft:overworld run tp @s -163.19 108.00 -425.80 -373.28 0.90");
            this.positionEntity(S4.ANTS.get(2), "/execute in minecraft:overworld run tp @s -164.98 108.00 -424.65 -406.88 3.00");
            this.positionEntity(S4.ANTS.get(3), "/execute in minecraft:overworld run tp @s -165.54 108.00 -424.81 -412.43 2.70");
            this.thenUsePlayer(p -> p.level().setBlockAndUpdate(new BlockPos(-150, 108, -420), Blocks.AIR.defaultBlockState()));
            this.tdCutscene("/execute in minecraft:overworld run tp @s -145.62 108.00 -418.59 92.91 -1.65");
            this.clearPlayerEmotion();
            this.clearChat();
            this.thenDelay(50);
            this.tdCutsceneMoveCamera(
               50, SplineType.LINEAR, InterpolationType.LINEAR, new String[]{"/execute in minecraft:overworld run tp @s -145.62 108.00 -418.59 15.96 3.00"}
            );
            this.thenDelay(50);
            this.tdCutsceneMoveCamera(
               100, SplineType.LINEAR, InterpolationType.LINEAR, new String[]{"/execute in minecraft:overworld run tp @s -145.62 108.00 -418.59 164.01 -0.75"}
            );
            this.thenDelay(100);
            this.thenEntityPathParallel(
               S4.ANTS.get(0),
               new String[]{
                  "/execute in minecraft:overworld run tp @s -162.47 108.00 -421.18 -34.14 10.05",
                  "/execute in minecraft:overworld run tp @s -148.11 108.00 -418.77 273.96 -2.25"
               }
            );
            this.thenEntityPathParallel(
               S4.ANTS.get(1),
               new String[]{
                  "/execute in minecraft:overworld run tp @s -163.68 108.00 -420.85 -32.49 12.45",
                  "/execute in minecraft:overworld run tp @s -149.08 108.00 -419.52 282.21 4.05"
               }
            );
            this.thenEntityPathParallel(
               S4.ANTS.get(2),
               new String[]{
                  "/execute in minecraft:overworld run tp @s -162.09 108.00 -420.75 -38.79 14.55",
                  "/execute in minecraft:overworld run tp @s -148.70 108.00 -417.76 -101.49 5.10"
               }
            );
            this.tdCutsceneMoveCamera(
               50, SplineType.LINEAR, InterpolationType.LINEAR, new String[]{"/execute in minecraft:overworld run tp @s -145.62 108.00 -418.59 101.16 -0.15"}
            );
            this.thenDelay(50);
            this.playerEmotion(new Emotion[]{Emotion.SLIGHTLY_SMILING_FACE});
            this.entityLookAt(S4.ANTS.get(0), "player");
            this.entityLookAt(S4.ANTS.get(1), "player");
            this.entityLookAt(S4.ANTS.get(2), "player", true);
            this.playerEmotion(new Emotion[]{Emotion.WAVING_HAND});
            this.thenDelay(50);
            this.thenEntityPathParallel(S4.ANTS.get(0), new String[]{"/execute in minecraft:overworld run tp @s -149.88 108.00 -419.84 -42.69 4.20"});
            this.thenEntityPathParallel(S4.ANTS.get(1), new String[]{"/execute in minecraft:overworld run tp @s -149.12 108.00 -419.53 41.01 7.20"});
            this.thenEntityPath(S4.ANTS.get(2), new String[]{"/execute in minecraft:overworld run tp @s -149.50 108.00 -419.11 181.56 -7.20"});
            this.thenHandleState(S4.this.createFlag("set_glitch_effect_on_block"));
            this.thenAllPerformers(
               new Performer[]{
                  ScriptPerformerBuilder.builder(inst).thenDelay(70).build(),
                  ScriptPerformerBuilder.builder(inst).thenDelay(20).freeEntities(true, S4.ANTS.get(0), S4.ANTS.get(1), S4.ANTS.get(2)).build(),
                  ScriptPerformerBuilder.builder(inst)
                     .thenDelay(30)
                     .thenUsePlayer(p -> p.level().setBlockAndUpdate(new BlockPos(-150, 108, -420), BlocksST.PEN.defaultBlockState()))
                     .build()
               }
            );
            this.thenHandleState(S4.this.clearFlag("set_glitch_effect_on_block"));
            this.thenDelay(10);
            this.playerEmotion(new Emotion[]{Emotion.CLAPPING_HANDS});
            this.addPlayerEmotion(Emotion.STAR_STRUCK);
            this.thenDelay(35);
            this.blackout(40);
            this.endTdCutscene();
            this.teleportPlayer("/execute in minecraft:overworld run tp @s -145.62 108.00 -418.59 101.16 -0.15");
            this.clearPlayerEmotion();
            this.clearChat();
            this.thenDelay(50);
            this.saveState("3");
            this.sendWebWarning("Для продолжения нужно кликнуть по пеньку");
            this.thenAwaitBlockClick("/execute in minecraft:overworld run tp @s -147.13 108.00 -418.75 109.86 -0.30");
            this.thenSpeechMessage(0, "Хм-м… неплохо, неплохо.");
            this.thenSpeechMessage(0, "Я знал, что у тебя получится.");
            this.playerEmotion(new Emotion[]{Emotion.SMILING_FACE_WITH_SUNGLASSES});
            this.thenDelay(20);
            this.thenSpeechMessage(0, "Значит, я точно могу дать тебе больше контроля.");
            this.thenSpeechMessage(0, "Не подведи.");
            this.thenSpeechMessage(0, "Как тебе?");
            this.thenSpeechMessage(0, "Теперь ты можешь самостоятельно обустраивать свой мир.");
            this.thenSpeechMessage(0, "Ты рад?");
            this.playerEmotion(new Emotion[]{Emotion.PENSIVE_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(0, "Что случилось, что не так?");
            this.thenSpeechMessage(0, "Ах да…");
            this.thenSpeechMessage(0, "Ты уже хочешь что-то построить, но не получается?");
            this.thenSpeechMessage(0, "Действительно, для этого тебе нужны рабочие.");
            this.playerEmotion(new Emotion[]{Emotion.SAD_BUT_RELIEVED_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(0, "Не печалься, это не беда.");
            this.thenSpeechMessage(0, "Мы поможем тебе.");
            this.thenSpeechMessage(0, "Эй, формики!");
            this.thenSpeechMessage(0, "Тут для вас работа нашлась.");
            this.thenSpeechMessage(0, "Формики работают ещё эффективнее, когда объединяются.");
            this.thenSpeechMessage(0, "Теперь ты сможешь построить всё, что захочешь.");
            this.playerEmotion(new Emotion[]{Emotion.SPARKLES});
            this.thenDelay(20);
            this.thenSpeechMessage(0, "Конечно, если тебе будет, чем заплатить.");
            this.playerEmotion(new Emotion[]{Emotion.NEUTRAL_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(0, "Ведь нельзя построить что-то из ничего.");
            this.thenSpeechMessage(0, "Помимо вложенного труда нужны ещё ресурсы.");
            this.thenSpeechMessage(0, "Я думаю, ты сам со временем это поймёшь.");
            this.thenSpeechMessage(0, "Я смотрю, ты открыл секрет этих загадочных деревьев.");
            this.thenSpeechMessage(0, "Они совсем не похожи на обычные деревья, правда?");
            this.thenSpeechMessage(0, "Это потому, что они состоят из волшебного газа — эфира.");
            this.playerEmotion(new Emotion[]{Emotion.SMILING_FACE_WITH_HEART_EYES});
            this.thenSpeechMessage(0, "А это важнейший ресурс.");
            this.thenSpeechMessage(0, "О, ты снова нашёл версиформ.");
            this.thenSpeechMessage(0, "Продолжай искать.");
            this.thenSpeechMessage(0, "Он незаменим в обустройстве мира.");
            this.playerEmotion(new Emotion[]{Emotion.SALUTING_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(0, "Поздравляю с первыми успехами!");
            this.thenSpeechMessage(0, "Всегда радостно наблюдать за теми, кто быстро учится.");
            this.playerEmotion(new Emotion[]{Emotion.PARTYING_FACE});
            this.thenDelay(20);
         }

         @ScriptPerformerBuilderReflective.ScriptPart(1)
         private void part1(S4 inst) {
            this.thenSpeechMessage(0, "Какое странное облако.");
            this.thenSpeechMessage(0, "Я таких никогда не видел.");
            this.thenSpeechMessage(0, "Впрочем, мало ли какие формы может принимать версиформ…");
            this.thenSpeechMessage(0, "Улетело.");
            this.thenSpeechMessage(0, "Ты уже и первое здание построил.");
            this.thenSpeechMessage(0, "Даже и не знаю, как ещё тебя похвалить.");
            this.playerEmotion(new Emotion[]{Emotion.SMILING_FACE_WITH_SUNGLASSES});
            this.thenDelay(20);
            this.thenSpeechMessage(0, "Ты удовлетворён результатом?");
            this.playerEmotion(new Emotion[]{Emotion.SMILING_FACE});
            this.addPlayerEmotion(Emotion.THUMBS_UP);
            this.thenDelay(20);
            this.thenRun(() -> FragmentInstanceManager.addActive(FragmentsST.SATISFACTION));
            this.thenWaitForF8();
            this.thenSpeechMessage(0, "О, ничего себе.");
            this.thenSpeechMessage(0, "Тебе подарок.");
            this.thenSpeechMessage(0, "Интересно, от кого?");
            this.thenRun(() -> FragmentInstanceManager.addActive(FragmentsST.SATISFACTION));
            this.playerEmotion(new Emotion[]{Emotion.STAR_STRUCK});
            this.thenDelay(20);
            this.thenSpeechMessage(0, "Ого, как щедро.");
            this.thenSpeechMessage(0, "А что это за… буквы?");
            this.thenSpeechMessage(0, "Хм-м, что бы это значило?");
            this.thenSpeechMessage(1, "…");
            this.playerThinkingEmotion(new Emotion[]{Emotion.THINKING_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(1, "…");
            this.sendWebWarning("Лошку читать нормальным его голосом, не детским, интонации по большей части лучше сохранять нейтральные");
            this.thenSpeechMessage(1, "Не знаю.");
            this.thenSpeechMessage(0, "Неужели… вот так сразу и заговорил?");
            this.playerEmotion(new Emotion[]{Emotion.FACE_WITH_PEEKING_EYE});
            this.thenDelay(20);
            this.thenSpeechMessage(0, "И сам удивился, да?");
            this.sendWebWarning("Голос смеётся");
            this.thenSpeechMessage(0, "Да уж, не перестаёшь ты меня удивлять.");
            this.playerEmotion(new Emotion[]{Emotion.NEUTRAL_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(1, "Ты кто?");
            this.sendWebWarning("Голос смеётся");
            this.thenSpeechMessage(0, "Кто я?");
            this.thenSpeechMessage(0, "Я твой друг.");
            this.playerThinkingEmotion(new Emotion[]{Emotion.THINKING_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(1, "Что такое друг?");
            this.thenSpeechMessage(0, "Хороший вопрос.");
            this.thenSpeechMessage(0, "Это тот, кто помогает.");
            this.thenRun(() -> FragmentInstanceManager.addActive(FragmentsST.FRIEND));
            this.playerEmotion(new Emotion[]{Emotion.NEUTRAL_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(1, "Ничего не понимаю.");
            this.thenSpeechMessage(1, "Кто ты?");
            this.sendWebWarning("Голос вздыхает");
            this.thenSpeechMessage(0, "Такого ответа тебе недостаточно, да?");
            this.thenSpeechMessage(0, "Хорошо.");
            this.thenSpeechMessage(0, "Как бы тебе объяснить?..");
            this.thenSpeechMessage(0, "Боюсь, ты всё равно не поймёшь.");
            this.thenSpeechMessage(0, "Я таков, каким меня видишь ты.");
            this.playerEmotion(new Emotion[]{Emotion.FACE_WITH_RAISED_EYEBROW});
            this.thenDelay(20);
            this.thenSpeechMessage(0, "Каким ты меня видишь?");
            this.thenSpeechMessage(0, "Или… слышишь?");
            this.thenSpeechMessage(1, "Я слышу голос.");
            this.thenSpeechMessage(0, "А видишь?");
            this.thenSpeechMessage(1, "Глаз.");
            this.thenSpeechMessage(0, "Значит, это я и есть.");
            this.playerEmotion(new Emotion[]{Emotion.DOWNCAST_FACE_WITH_SWEAT});
            this.thenDelay(20);
            this.thenSpeechMessage(0, "Непонятно, да?");
            this.sendWebWarning("Голос вздыхает");
            this.thenSpeechMessage(0, "Не расстраивайся: когда-нибудь ты поймёшь.");
            this.playerEmotion(new Emotion[]{Emotion.FLUSHED_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(1, "А я?");
            this.thenSpeechMessage(0, "Ты — это ты.");
            this.playerThinkingEmotion(new Emotion[]{Emotion.THINKING_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(1, "Я — это ты?");
            this.thenSpeechMessage(0, "Нет, я — это я.");
            this.thenSpeechMessage(0, "А ты — это ты.");
            this.playerThinkingEmotion(new Emotion[]{Emotion.THINKING_FACE});
            this.addPlayerThinkingEmotion(Emotion.THINKING_FACE);
            this.thenDelay(20);
            this.thenSpeechMessage(1, "Хм-м…");
            this.thenSpeechMessage(1, "Допустим.");
            this.thenSpeechMessage(1, "А… другой?");
            this.thenSpeechMessage(1, "Другой — это кто?");
            this.thenSpeechMessage(0, "“Другой”?..");
            this.thenSpeechMessage(0, "О ком это ты?");
            this.playerEmotion(new Emotion[]{Emotion.MELTING_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(0, "Ты про то существо, которое ты видел?");
            this.thenSpeechMessage(1, "Да.");
            this.thenSpeechMessage(1, "Большое.");
            this.thenSpeechMessage(0, "Ты прав, это действительно было очень крупное существо.");
            this.thenSpeechMessage(0, "Возможно, ты когда-нибудь с ним познакомишься.");
         }

         @ScriptPerformerBuilderReflective.ScriptPart(2)
         private void part2(S4 inst) {
            this.playerEmotion(new Emotion[]{Emotion.NEUTRAL_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(1, "Хочу сейчас.");
            this.thenSpeechMessage(0, "Сейчас не получится.");
            this.playerEmotion(new Emotion[]{Emotion.CONFOUNDED_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(1, "Почему?");
            this.thenSpeechMessage(0, "Ты не готов к большому миру.");
            this.playerEmotion(new Emotion[]{Emotion.WEARY_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(0, "Но… неужели тебе не нравится твой собственный мир?");
            this.thenSpeechMessage(0, "Разве в нём не интересно?");
            this.playerEmotion(new Emotion[]{Emotion.PENSIVE_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(1, "Интересно…");
            this.thenSpeechMessage(0, "А сколько всего тебе ещё предстоит в нём открыть!");
            this.thenSpeechMessage(0, "Только представь.");
            this.playerEmotion(new Emotion[]{Emotion.FACE_EXHALING});
            this.thenDelay(20);
            this.thenSpeechMessage(0, "Всему своё время, друг мой, всему своё время.");
            this.thenSpeechMessage(0, "Для начала исследуй свой собственный мир.");
            this.playerEmotion(new Emotion[]{Emotion.EYES});
            this.thenDelay(20);
            this.thenSpeechMessage(0, "Для тебя пока что и он огромен.");
            this.thenSpeechMessage(0, "И совершенно неизвестен.");
            this.thenSpeechMessage(1, "Что это?");
            this.thenSpeechMessage(0, "Это камень.");
            this.playerThinkingEmotion(new Emotion[]{Emotion.THINKING_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(1, "А это что?");
            this.thenSpeechMessage(0, "Это слизень.");
            this.playerEmotion(new Emotion[]{Emotion.MELTING_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(1, "А это что?");
            this.thenSpeechMessage(0, "Это горы.");
            this.playerEmotion(new Emotion[]{Emotion.EXPLODING_HEAD});
            this.thenDelay(20);
            this.thenSpeechMessage(1, "Большие.");
            this.thenSpeechMessage(0, "Да.");
            this.thenRun(() -> FragmentInstanceManager.addActive(FragmentsST.BIG));
            this.thenSpeechMessage(1, "А это что?");
            this.thenSpeechMessage(0, "Это озеро.");
            this.thenSpeechMessage(1, "Озеро?");
            this.thenSpeechMessage(0, "Да, оно состоит из воды.");
            this.thenSpeechMessage(1, "Воды…");
            this.playerThinkingEmotion(new Emotion[]{Emotion.THINKING_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(1, "Что это было?");
            this.thenSpeechMessage(0, "Не бойся, это всего лишь рыба.");
            this.thenSpeechMessage(0, "Она совершенно безобидна…");
            this.thenSpeechMessage(0, "И даже полезна: ею ты можешь кормить своих рабочих.");
            this.thenSpeechMessage(0, "Только сам не лезь туда.");
            this.thenSpeechMessage(0, "Это опасно, помнишь?");
            this.playerEmotion(new Emotion[]{Emotion.NEUTRAL_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(1, "А это что?");
            this.thenSpeechMessage(0, "Это цветок.");
            this.thenSpeechMessage(1, "Я знаю.");
            this.sendWebWarning("Голос ворчливо");
            this.thenSpeechMessage(0, "Так а зачем спрашиваешь тогда?");
            this.thenSpeechMessage(1, "Цветок, поговори со мной.");
            this.thenSpeechMessage(0, "…");
            this.thenSpeechMessage(1, "…");
            this.thenSpeechMessage(0, "Цветы не разговаривают.");
            this.playerEmotion(new Emotion[]{Emotion.PLEADING_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(1, "Почему?");
            this.thenSpeechMessage(0, "Потому что так мир устроен.");
            this.playerEmotion(new Emotion[]{Emotion.WEARY_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(1, "Мне не нравится!");
            this.thenSpeechMessage(0, "Не нравится?");
            this.thenSpeechMessage(0, "Неужели цветок не красив?");
            this.thenSpeechMessage(0, "Разве он должен обязательно разговаривать, чтобы быть прекрасным?");
            this.playerEmotion(new Emotion[]{Emotion.LOUDLY_CRYING_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(0, "Хорошо, я понял, не время для таких рассуждений.");
            this.playerEmotion(new Emotion[]{Emotion.LOUDLY_CRYING_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(0, "Формики!");
            this.playerEmotion(new Emotion[]{Emotion.LOUDLY_CRYING_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(0, "Развеселите нашего друга.");
            this.playerEmotion(new Emotion[]{Emotion.LOUDLY_CRYING_FACE});
            this.thenDelay(20);
            this.playerEmotion(new Emotion[]{Emotion.SLIGHTLY_SMILING_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(1, "Формики.");
            this.playerEmotion(new Emotion[]{Emotion.WAVING_HAND});
            this.thenDelay(20);
            this.thenSpeechMessage(1, "Привет.");
            this.thenSpeechMessage(1, "Формики, вы кто?");
            this.playerEmotion(new Emotion[]{Emotion.NEUTRAL_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(1, "Я вас не понимаю.");
            this.playerEmotion(new Emotion[]{Emotion.FACE_WITH_RAISED_EYEBROW});
            this.thenDelay(20);
            this.thenSpeechMessage(1, "Вы что, не разговариваете?");
            this.playerEmotion(new Emotion[]{Emotion.CONFOUNDED_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(1, "И вы тоже…");
            this.thenSpeechMessage(1, "Как цветок…");
            this.thenSpeechMessage(1, "Что?");
            this.thenSpeechMessage(1, "Не понимаю.");
            this.thenSpeechMessage(0, "Вот видишь.");
            this.thenSpeechMessage(0, "Формики смогли помочь тебе несмотря на то, что они не разговаривают.");
            this.playerEmotion(new Emotion[]{Emotion.SLIGHTLY_SMILING_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(0, "Они твои друзья.");
            this.playerEmotion(new Emotion[]{Emotion.RED_HEART});
            this.thenDelay(20);
            this.thenSpeechMessage(1, "Формики — мои друзья.");
         }

         @ScriptPerformerBuilderReflective.ScriptPart(3)
         private void part3(S4 inst) {
            this.thenSpeechMessage(0, "Конечно.");
            this.thenSpeechMessage(0, "Мы с формиками в любой момент тебе поможем.");
            this.thenSpeechMessage(0, "Правда, ночью я занят другими делами…");
            this.thenSpeechMessage(0, "Но формики всегда рядом.");
            this.sendWebWarning("Чел заикается, это его постоянная характеристика; читать как лоха");
            this.thenSpeechMessage(2, "П-привет.");
            this.playerEmotion(new Emotion[]{Emotion.ANGUISHED_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(1, "Привет.");
            this.thenSpeechMessage(2, "Это т-твой мир, да?");
            this.thenSpeechMessage(1, "Да.");
            this.thenSpeechMessage(2, "П-прости за вторжение…");
            this.thenSpeechMessage(2, "Я с-случайно к тебе попал.");
            this.thenSpeechMessage(2, "Н-но раз уж попал, д-давай познакомимся.");
            this.thenSpeechMessage(2, "Как тебя зовут?");
            this.playerThinkingEmotion(new Emotion[]{Emotion.THINKING_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(1, "Зовут?..");
            this.thenSpeechMessage(2, "Ну д-да.");
            this.thenSpeechMessage(1, "Не понимаю.");
            this.thenSpeechMessage(2, "К-как твоё имя?");
            this.playerEmotion(new Emotion[]{Emotion.DOWNCAST_FACE_WITH_SWEAT});
            this.thenDelay(20);
            this.thenSpeechMessage(1, "Имя?..");
            this.thenSpeechMessage(2, "Да, имя.");
            this.playerEmotion(new Emotion[]{Emotion.DOWNCAST_FACE_WITH_SWEAT});
            this.thenDelay(20);
            this.thenSpeechMessage(1, "О чём ты?");
            this.thenSpeechMessage(2, "Н-ну, э-э…");
            this.thenSpeechMessage(2, "Имя — это то, как м-мы з-зовёмся.");
            this.thenSpeechMessage(2, "К-как нас называют д-другие.");
            this.sendWebWarning("Ударение: анУ");
            this.thenSpeechMessage(2, "Н-например, меня з-зовут Ану.");
            this.playerThinkingEmotion(new Emotion[]{Emotion.THINKING_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(1, "Ану?..");
            this.thenSetSpeechEntity(2, "Ану", "anu_icon");
            this.thenSpeechMessage(2, "Д-да, Ану.");
            this.thenSpeechMessage(2, "А у т-тебя есть имя?");
            this.playerEmotion(new Emotion[]{Emotion.ANGUISHED_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(1, "У меня?..");
            this.thenSpeechMessage(1, "Имя?..");
            this.thenSpeechMessage(1, "Имя…");
            this.playerEmotion(new Emotion[]{Emotion.FLUSHED_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(2, "Ч-что т-такое, у т-тебя пока н-нет имени?");
            this.playerEmotion(new Emotion[]{Emotion.CONFOUNDED_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(1, "Нет, у меня нет имени.");
            this.thenSpeechMessage(2, "Н-ну что ты, н-ну не п-плачь…");
            this.thenSpeechMessage(2, "Э-э, н-ну, в общем, я хотел с-сказать, э-э…");
            this.thenSpeechMessage(2, "Т-ты ещё совсем м-маленький — это н-нормально, что у тебя п-пока нет имени.");
            this.playerEmotion(new Emotion[]{Emotion.PENSIVE_FACE});
            this.thenSpeechMessage(1, "Но я хочу, чтобы у меня было имя.");
            this.thenSpeechMessage(2, "К-конечно, в-всем нужно имя.");
            this.thenSpeechMessage(2, "П-придумай его себе с-сам.");
            this.thenSpeechMessage(2, "Я с-сам придумал…");
            this.playerEmotion(new Emotion[]{Emotion.NEUTRAL_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(1, "Сам?");
            this.thenSpeechMessage(1, "А как?");
            this.thenSpeechMessage(2, "Э-э, н-ну, н-не знаю…");
            this.thenSpeechMessage(2, "П-просто с-ставил разные з-звуки рядом…");
            this.thenSpeechMessage(2, "И н-нашёл с-сочетание, к-которое мне нравится.");
            this.playerEmotion(new Emotion[]{Emotion.SLIGHTLY_SMILING_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(1, "Мне тоже нравится Ану.");
            this.thenSpeechMessage(1, "Можно, меня тоже будут звать Ану?");
            this.thenSpeechMessage(2, "Н-ну, н-не знаю, Ану — это м-моё имя…");
            this.thenSpeechMessage(2, "Ты д-должен с-своё придумать.");
            this.playerEmotion(new Emotion[]{Emotion.ENRAGED_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(2, "Т-тебе и с-самому т-так будет лучше!");
            this.thenSpeechMessage(2, "Имя, оно же д-должно быть твоё!");
            this.thenSpeechMessage(2, "Н-нет, т-только не снова…");
            this.thenSpeechMessage(2, "П-пожалуйста.");
            this.thenSpeechMessage(2, "Н-ну не плачь, п-прошу тебя.");
            this.thenSpeechMessage(2, "Ай-й, ч-что же делать?!");
            this.thenSpeechMessage(2, "Л-ладно, э-э…");
            this.thenSpeechMessage(2, "Я тут это…");
            this.thenSpeechMessage(2, "М-мне тут н-надо…");
            this.thenSpeechMessage(2, "Н-ну, там, т-туда, знаешь…");
            this.thenSpeechMessage(1, "Куда?!");
            this.playerEmotion(new Emotion[]{Emotion.ENRAGED_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(1, "Эй!");
            this.playerEmotion(new Emotion[]{Emotion.ENRAGED_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(1, "Вернись!");
            this.thenSpeechMessage(1, "…");
            this.playerEmotion(new Emotion[]{Emotion.FACE_EXHALING});
            this.thenDelay(20);
            this.thenSpeechMessage(1, "И этот ушёл…");
            this.sendWebWarning("Скрипт всё!");
            this.timeMarker("23:50");
         }
      })
      .build(this::registerMainPerformer);

   public S4(ScriptEnvironment env, int id) {
      super(env, id);
   }

   @Override
   public void updateAsPlayer(ServerPlayer player) {
      super.updateAsPlayer(player);
      if (this.flags.has("particle")) {
         ParticleHelper.spawnDirectedParticle(player.level(), ParticleTypes.END_ROD, new Vec3(-145.5, 108.5, -418.5), Vec3.ZERO);
      }

      if (this.flags.has("set_glitch_effect_on_block")) {
         this.setGlitchEffectOnBlock(new BlockPos(-150, 108, -420), 1.3F);
      }
   }

   @Override
   public void handleStart() {
      super.handleStart();
   }

   @Override
   public void handleStop() {
      super.handleStop();
   }

   @Override
   public ScriptPerformer handleSetup() {
      return new ScriptPerformerBuilder(this).build();
   }

   private void setGlitchEffectOnBlock(BlockPos blockPos, double expansion) {
      Random random = new Random();
      double baseX = (double)blockPos.getX() + 0.5;
      double baseY = (double)blockPos.getY() + 0.5;
      double baseZ = (double)blockPos.getZ() + 0.5;

      for (int i = 0; i < 5; i++) {
         double x = baseX + (random.nextDouble() - 0.5) * expansion;
         double y = baseY + (random.nextDouble() - 0.5) * expansion;
         double z = baseZ + (random.nextDouble() - 0.5) * expansion;
         Location particleLocation = new Location(x, y, z);
         ParticleHelper.spawnDirectedParticle(ScriptUtils.getLevel(), ParticleTypesST.GLITCH, particleLocation, Vec3.ZERO);
      }
   }
}
