package com.storyteam.storytelling.system.simulator.me_tab.init;

import com.storyteam.storytelling.client.Animations;
import com.storyteam.storytelling.system.currency.storage.CurrencyStorage;
import com.storyteam.storytelling.system.ico.Ico;
import com.storyteam.storytelling.system.simulator.me_tab.attributes.PlayerStats;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.data.DescribedCondition;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.data.FragmentData;
import net.minecraft.network.chat.Component;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;

@SimplyRegister(
   prefix = "system/simulator/me_tab/"
)
public interface FragmentsST {
   @RegistryName("beautifully")
   FragmentData BEAUTIFULLY = FragmentData.builder()
      .word(Component.literal("КРАСИВО"))
      .thought(Component.literal("ヽ[♡‿♡]ノ"))
      .durationData(0, 14, 0)
      .addRequirement(DescribedCondition.level(4))
      .acceptAction(player -> {
         PlayerStats.addBodyV(1);
         PlayerStats.addMindV(1);
      }, player -> {
         PlayerStats.addBodyV(-1);
         PlayerStats.addMindV(-1);
      }, Component.literal(Ico.BODY.getSymbol() + ": +1").append("\n").append(Ico.MIND.getSymbol() + ": +1"))
      .rejectAction(player -> {
         PlayerStats.addWillpowerV(1);
         CurrencyStorage.addEidos(10);
      }, player -> {
         PlayerStats.addWillpowerV(-1);
         CurrencyStorage.addEidos(-10);
      }, Component.literal(Ico.WILLPOWER.getSymbol() + ": +1").append("\n").append(Ico.EIDOS.getSymbol() + ": +10"))
      .setAnimation(Animations.EMOTION_INSPIRATION_2)
      .build();
   @RegistryName("painfully")
   FragmentData PAINFULLY = FragmentData.builder()
      .word(Component.literal("БОЛЬНО"))
      .thought(Component.literal("[×﹏×]"))
      .durationData(0, 10, 0)
      .addRequirement(DescribedCondition.level(2))
      .acceptAction(player -> PlayerStats.addBodyV(1), player -> PlayerStats.addBodyV(-1), Component.literal(Ico.BODY.getSymbol() + ": +1"))
      .rejectAction(player -> CurrencyStorage.addEidos(5), player -> CurrencyStorage.addEidos(-5), Component.literal(Ico.EIDOS.getSymbol() + ": +5"))
      .setAnimation(Animations.EMOTION_PAIN)
      .build();
   @RegistryName("scary")
   FragmentData SCARY = FragmentData.builder()
      .word(Component.literal("СТРАШНО"))
      .thought(Component.literal("〣[ºΔº]〣"))
      .durationData(0, 11, 0)
      .addRequirement(DescribedCondition.level(2))
      .acceptAction(player -> PlayerStats.addMindV(1), player -> PlayerStats.addMindV(-1), Component.literal(Ico.MIND.getSymbol() + ": +1"))
      .rejectAction(player -> CurrencyStorage.addEidos(6), player -> CurrencyStorage.addEidos(-6), Component.literal(Ico.EIDOS.getSymbol() + ": +6"))
      .setAnimation(Animations.EMOTION_FRIGHT)
      .build();
   @RegistryName("funny")
   FragmentData FUNNY = FragmentData.builder()
      .word(Component.literal("СМЕШНО"))
      .thought(Component.literal("ヽ[o＾▽＾o]ノ"))
      .durationData(0, 14, 0)
      .addRequirement(DescribedCondition.level(4))
      .acceptAction(player -> {
         PlayerStats.addBodyV(1);
         PlayerStats.addMindV(1);
      }, player -> {
         PlayerStats.addBodyV(-1);
         PlayerStats.addMindV(-1);
      }, Component.literal(Ico.BODY.getSymbol() + ": +1").append("\n").append(Ico.MIND.getSymbol() + ": +1"))
      .rejectAction(player -> {
         PlayerStats.addWillpowerV(1);
         CurrencyStorage.addEidos(10);
      }, player -> {
         PlayerStats.addWillpowerV(-1);
         CurrencyStorage.addEidos(-10);
      }, Component.literal(Ico.WILLPOWER.getSymbol() + ": +1").append("\n").append(Ico.EIDOS.getSymbol() + ": +10"))
      .setAnimation(Animations.EMOTION_HAPPY)
      .build();
   @RegistryName("disgusting")
   FragmentData DISGUSTING = FragmentData.builder()
      .word(Component.literal("ПРОТИВНО"))
      .thought(Component.literal("[´ж｀;]"))
      .durationData(0, 13, 0)
      .addRequirement(DescribedCondition.level(3))
      .acceptAction(player -> PlayerStats.addWillpowerV(1), player -> PlayerStats.addWillpowerV(-1), Component.literal(Ico.WILLPOWER.getSymbol() + ": +1"))
      .rejectAction(player -> PlayerStats.addMindV(1), player -> PlayerStats.addMindV(-1), Component.literal(Ico.MIND.getSymbol() + ": +1"))
      .setAnimation(Animations.EMOTION_DISGUST)
      .build();
   @RegistryName("me")
   FragmentData ME = FragmentData.builder()
      .word(Component.literal("Я"))
      .thought(Component.literal("Кто это? Это я?"))
      .durationData(0, 8, 10)
      .addRequirement(DescribedCondition.level(1))
      .acceptAction(player -> player.sendSystemMessage(Component.literal("Принято")), Component.literal("Просто принять"))
      .rejectAction(player -> player.sendSystemMessage(Component.literal("Отклонено")), Component.literal("Просто отклонить"))
      .setAnimation(Animations.EMOTION_DISTRUST)
      .build();
   @RegistryName("game")
   FragmentData GAME = FragmentData.builder()
      .word(Component.literal("ИГРА"))
      .thought(Component.literal("(／_^)／　　●　＼(^_＼)"))
      .durationData(0, 15, 0)
      .addRequirement(DescribedCondition.level(4))
      .acceptAction(player -> {
         PlayerStats.addBodyP(1);
         PlayerStats.addMindP(1);
      }, player -> {
         PlayerStats.addBodyP(-1);
         PlayerStats.addMindP(-1);
      }, Component.literal(Ico.BODY.getSymbol() + ": +1").append("\n").append(Ico.MIND.getSymbol() + ": +1"))
      .rejectAction(player -> PlayerStats.addWillpowerP(2), player -> PlayerStats.addWillpowerP(-2), Component.literal(Ico.WILLPOWER.getSymbol() + ": +2"))
      .setAnimation(Animations.EMOTION_INSPIRATION_2)
      .build();
   @RegistryName("joyfully")
   FragmentData JOYFULLY = FragmentData.builder()
      .word(Component.literal("РАДОСТНО"))
      .thought(Component.literal("(*¯︶¯*)"))
      .durationData(0, 6, 0)
      .addRequirement(DescribedCondition.level(3))
      .acceptAction(player -> PlayerStats.addMindP(2), player -> PlayerStats.addMindP(-2), Component.literal(Ico.MIND.getSymbol() + ": +2"))
      .rejectAction(player -> {
         PlayerStats.addWillpowerP(1);
         PlayerStats.addBodyP(-1);
      }, player -> {
         PlayerStats.addWillpowerP(-1);
         PlayerStats.addBodyP(1);
      }, Component.literal(Ico.WILLPOWER.getSymbol() + ": +1").append("\n").append(Ico.BODY.getSymbol() + ": -1"))
      .setAnimation(Animations.EMOTION_HAPPY_NEW)
      .build();
   @RegistryName("dangerous")
   FragmentData DANGEROUS = FragmentData.builder()
      .word(Component.literal("ОПАСНО"))
      .thought(Component.literal("＼(º □ º l|l)/"))
      .durationData(0, 6, 0)
      .addRequirement(DescribedCondition.level(3))
      .acceptAction(player -> {
         PlayerStats.addWillpowerP(2);
         PlayerStats.addMindP(-1);
      }, player -> {
         PlayerStats.addWillpowerP(-2);
         PlayerStats.addMindP(1);
      }, Component.literal(Ico.WILLPOWER.getSymbol() + ": +2").append("\n").append(Ico.MIND.getSymbol() + ": -1"))
      .rejectAction(player -> {
         PlayerStats.addMindP(2);
         PlayerStats.addWillpowerP(-1);
      }, player -> {
         PlayerStats.addMindP(-2);
         PlayerStats.addWillpowerP(1);
      }, Component.literal(Ico.MIND.getSymbol() + ": +2").append("\n").append(Ico.WILLPOWER.getSymbol() + ": -1"))
      .setAnimation(Animations.EMOTION_FRIGHT)
      .build();
   @RegistryName("grow_up")
   FragmentData GROW_UP = FragmentData.builder()
      .word(Component.literal("Взросление"))
      .thought(Component.literal("Взросление — это не только обретение контроля, но и принятие ответственности."))
      .durationData(0, 30, 0)
      .addRequirement(DescribedCondition.level(15))
      .acceptAction(player -> player.sendSystemMessage(Component.literal("Принято")), Component.literal("Просто принять"))
      .rejectAction(player -> player.sendSystemMessage(Component.literal("Отклонено")), Component.literal("Просто отклонить"))
      .setAnimation(Animations.EMOTION_FRIGHT)
      .build();
   @RegistryName("satisfaction")
   FragmentData SATISFACTION = FragmentData.builder()
      .word(Component.literal("Удовлетворение"))
      .thought(Component.literal("Я хотел что-то сделать, и у меня получилось."))
      .durationData(0, 26, 0)
      .addRequirement(DescribedCondition.level(8))
      .acceptAction(player -> player.sendSystemMessage(Component.literal("Принято")), Component.literal("Просто принять"))
      .rejectAction(player -> player.sendSystemMessage(Component.literal("Отклонено")), Component.literal("Просто отклонить"))
      .setAnimation(Animations.EMOTION_FRIGHT)
      .build();
   @RegistryName("interesting")
   FragmentData INTERESTING = FragmentData.builder()
      .word(Component.literal("Интересно"))
      .thought(Component.literal("(☆▽☆)"))
      .durationData(0, 13, 0)
      .addRequirement(DescribedCondition.level(3))
      .acceptAction(player -> player.sendSystemMessage(Component.literal("Принято")), Component.literal("Просто принять"))
      .rejectAction(player -> player.sendSystemMessage(Component.literal("Отклонено")), Component.literal("Просто отклонить"))
      .setAnimation(Animations.EMOTION_FRIGHT)
      .build();
   @RegistryName("friend")
   FragmentData FRIEND = FragmentData.builder()
      .word(Component.literal("Друг"))
      .thought(Component.literal("＼(＾∀＾)メ(＾∀＾)ノ"))
      .durationData(0, 14, 0)
      .addRequirement(DescribedCondition.level(4))
      .acceptAction(player -> player.sendSystemMessage(Component.literal("Принято")), Component.literal("Просто принять"))
      .rejectAction(player -> player.sendSystemMessage(Component.literal("Отклонено")), Component.literal("Просто отклонить"))
      .setAnimation(Animations.EMOTION_FRIGHT)
      .build();
   @RegistryName("big")
   FragmentData BIG = FragmentData.builder()
      .word(Component.literal("Большой"))
      .thought(Component.literal("⛰⛭"))
      .durationData(0, 13, 0)
      .addRequirement(DescribedCondition.level(3))
      .acceptAction(player -> player.sendSystemMessage(Component.literal("Принято")), Component.literal("Просто принять"))
      .rejectAction(player -> player.sendSystemMessage(Component.literal("Отклонено")), Component.literal("Просто отклонить"))
      .setAnimation(Animations.EMOTION_FRIGHT)
      .build();
   @RegistryName("amazing")
   FragmentData AMAZING = FragmentData.builder()
      .word(Component.literal("УДИВИТЕЛЬНО"))
      .thought(Component.literal("(⊙_⊙)"))
      .durationData(0, 12, 0)
      .addRequirement(DescribedCondition.level(4))
      .acceptAction(player -> player.sendSystemMessage(Component.literal("Принято")), Component.literal("Просто принять"))
      .rejectAction(player -> player.sendSystemMessage(Component.literal("Отклонено")), Component.literal("Просто отклонить"))
      .setAnimation(Animations.EMOTION_DISTRUST)
      .build();
   @RegistryName("forward")
   FragmentData FORWARD = FragmentData.builder()
      .word(Component.literal("ВПЕРЁД"))
      .thought(Component.literal(" ⇨ "))
      .durationData(0, 12, 0)
      .addRequirement(DescribedCondition.level(2))
      .acceptAction(player -> player.sendSystemMessage(Component.literal("Принято")), Component.literal("Просто принять"))
      .rejectAction(player -> player.sendSystemMessage(Component.literal("Отклонено")), Component.literal("Просто отклонить"))
      .setAnimation(Animations.EMOTION_EXAMINE)
      .build();
   @RegistryName("another")
   FragmentData ANOTHER = FragmentData.builder()
      .word(Component.literal("ДРУГОЙ"))
      .thought(Component.literal("Я не один."))
      .durationData(0, 16, 0)
      .addRequirement(DescribedCondition.level(7))
      .acceptAction(player -> player.sendSystemMessage(Component.literal("Принято")), Component.literal("Просто принять"))
      .rejectAction(player -> player.sendSystemMessage(Component.literal("Отклонено")), Component.literal("Просто отклонить"))
      .setAnimation(Animations.EMOTION_HAPPY_NEW)
      .build();
   @RegistryName("growth")
   FragmentData GROWTH = FragmentData.builder()
      .word(Component.literal("РОСТ"))
      .thought(Component.literal(" ↑ "))
      .durationData(0, 14, 0)
      .addRequirement(DescribedCondition.level(4))
      .acceptAction(player -> player.sendSystemMessage(Component.literal("Принято")), Component.literal("Просто принять"))
      .rejectAction(player -> player.sendSystemMessage(Component.literal("Отклонено")), Component.literal("Просто отклонить"))
      .setAnimation(Animations.EMOTION_REALIZATION)
      .build();
   @RegistryName("desire")
   FragmentData DESIRE = FragmentData.builder()
      .word(Component.literal("ЖЕЛАНИЕ"))
      .thought(Component.literal(" (*♡∀♡)人 "))
      .durationData(0, 12, 0)
      .addRequirement(DescribedCondition.level(3))
      .acceptAction(player -> player.sendSystemMessage(Component.literal("Принято")), Component.literal("Просто принять"))
      .rejectAction(player -> player.sendSystemMessage(Component.literal("Отклонено")), Component.literal("Просто отклонить"))
      .setAnimation(Animations.EMOTION_HAPPY)
      .build();
   @RegistryName("difference")
   FragmentData DIFFERENCE = FragmentData.builder()
      .word(Component.literal("РАЗНИЦА"))
      .thought(Component.literal("◯◼"))
      .durationData(0, 16, 0)
      .addRequirement(DescribedCondition.level(5))
      .acceptAction(player -> player.sendSystemMessage(Component.literal("Принято")), Component.literal("Просто принять"))
      .rejectAction(player -> player.sendSystemMessage(Component.literal("Отклонено")), Component.literal("Просто отклонить"))
      .setAnimation(Animations.EMOTION_EYE_ROLLING_THINKY)
      .build();
   @RegistryName("patience")
   FragmentData PATIENCE = FragmentData.builder()
      .word(Component.literal("ТЕРПЕНИЕ"))
      .thought(Component.literal("Тот, кто умеет ждать, не будет сдаваться и достигнет своей цели."))
      .durationData(1, 1, 0)
      .addRequirement(DescribedCondition.level(7))
      .acceptAction(player -> player.sendSystemMessage(Component.literal("Принято")), Component.literal("Просто принять"))
      .rejectAction(player -> player.sendSystemMessage(Component.literal("Отклонено")), Component.literal("Просто отклонить"))
      .setAnimation(Animations.EMOTION_ANGRY_HALF)
      .build();
}
