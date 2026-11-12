package com.storyteam.storytelling.script.instances.test;

import com.storyteam.storytelling.api.Script;
import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.init.EntitiesST;
import com.storyteam.storytelling.script.ScriptInstance;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.builder.ScriptPerformerBuilder;
import com.storyteam.storytelling.script.performer.builder.reflective.ScriptPerformerBuilderReflective;
import com.storyteam.storytelling.script.story.ScriptEnvironment;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.script.story.characters.Characters;
import com.storyteam.storytelling.system.dialog.Dialog;
import com.storyteam.storytelling.system.dialog.DialogEmotion;
import com.storyteam.storytelling.system.emotions.Emotion;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import org.zeith.hammerlib.api.io.NBTSerializable;
import org.zeith.hammerlib.util.java.Cast;

@Script(
   value = 911,
   description = "Testicle script"
)
public class SASorSOS extends ScriptInstance {
   public static final int ID = 911;
   Character<AnimatedEntity> HEAD = Character.builder("s911_head", Cast.constant(EntitiesST.SPIDER_HEAD_LOLOLOSHKA)).name("Формик").build();
   @NBTSerializable
   public final ScriptPerformer mainFlow = (new ScriptPerformerBuilderReflective(this) {
         @ScriptPerformerBuilderReflective.ScriptPart(0)
         private void part0(SASorSOS inst) {
            this.playerEmotion(new Emotion[]{Emotion.THINKING_FACE})
               .thenWaitForF8()
               .playerEmotion(Emotion.EYES)
               .thenWaitForF8()
               .playerEmotion(Emotion.NOOOOO)
               .thenWaitForF8()
               .playerEmotion(Emotion.NAIL_POLISH)
               .thenWaitForF8()
               .playerEmotion(Emotion.FEARFUL_FACE)
               .thenWaitForF8()
               .playerEmotion(Emotion.PENSIVE_FACE)
               .thenWaitForF8()
               .createTelepornGlitchEffect(Characters.ZEITHERON, "/execute in minecraft:overworld run tp @s -60.88 103.00 -373.95 239.06 5.04")
               .thenWaitForF8()
               .dialog(
                  new Dialog(911, Characters.ZEITHERON)
                     .setEmotion(DialogEmotion.CONFUSED)
                     .addText(
                        Component.literal(
                              "The purpose of this section is to accurately produce tints (pure white added) and shades (pure black added) of your selected color in 10% increments."
                           )
                           .withStyle(Style.EMPTY.withColor(16764153))
                           .append(
                              Component.literal(
                                    "The purpose of this section is to accurately produce tints (pure white added) and shades (pure black added) of your selected color in 10% increments."
                                 )
                                 .withStyle(Style.EMPTY.withItalic(true).withColor(ChatFormatting.DARK_GREEN))
                           )
                     )
                     .addAnswer("aa", DialogEmotion.ANGRY)
                     .addAnswer(Component.literal("bb").withStyle(Style.EMPTY.withColor(ChatFormatting.YELLOW)), false, DialogEmotion.CUTE)
               )
               .timeMarker("16:00");
         }
      })
      .build(this::registerMainPerformer);

   public SASorSOS(ScriptEnvironment env, int id) {
      super(env, id);
   }

   @Override
   public void updateAsPlayer(ServerPlayer player) {
      super.updateAsPlayer(player);
   }

   @Override
   public void handleStop() {
      this.getRepository().removeAll();
      super.handleStop();
   }

   @Override
   public void handleStart() {
      super.handleStart();
   }

   @Override
   public ScriptPerformer handleSetup() {
      return new ScriptPerformerBuilder(this).build();
   }
}
