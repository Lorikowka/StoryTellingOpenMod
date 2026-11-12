package com.storyteam.storytelling.script.instances.test;

import com.storyteam.storytelling.api.ForgetOnClose;
import com.storyteam.storytelling.api.Script;
import com.storyteam.storytelling.api.data.EntityInteractionData;
import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.script.ScriptInstance;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.builder.ScriptPerformerBuilder;
import com.storyteam.storytelling.script.performer.builder.reflective.ScriptPerformerBuilderReflective;
import com.storyteam.storytelling.script.story.ScriptEnvironment;
import net.minecraft.server.level.ServerPlayer;
import org.zeith.hammerlib.api.io.NBTSerializable;

@ForgetOnClose
@Script(1005)
public class S1005 extends ScriptInstance {
   @NBTSerializable
   public final ScriptPerformer mainFlow = (new ScriptPerformerBuilderReflective(this) {
         @ScriptPerformerBuilderReflective.ScriptPart(0)
         private void part0(S1005 inst) {
            this.saveState("0")
               .thenWaitForF8()
               .sendWebWarning("АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ абвгдеёжзийклмнопрстуфхцчшщъыьэюя")
               .thenDelay(2)
               .sendWebWarningOrange("АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ абвгдеёжзийклмнопрстуфхцчшщъыьэюя")
               .thenDelay(2)
               .sendWebWarningRed("АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ абвгдеёжзийклмнопрстуфхцчшщъыьэюя")
               .thenWaitForF8()
               .thenDelay(2)
               .sendWebWarning("ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijklmnopqrstuvwxyz")
               .thenDelay(2)
               .sendWebWarningOrange("ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijklmnopqrstuvwxyz")
               .thenDelay(2)
               .sendWebWarningRed("ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijklmnopqrstuvwxyz")
               .thenWaitForF8()
               .thenDelay(2)
               .sendWebWarning("1234567890 !@#$%^&*()_+-=[]{};:',.<>/?|")
               .thenDelay(2)
               .sendWebWarningOrange("1234567890 !@#$%^&*()_+-=[]{};:',.<>/?|")
               .thenDelay(2)
               .sendWebWarningRed("1234567890 !@#$%^&*()_+-=[]{};:',.<>/?|")
               .thenWaitForF8()
               .thenDelay(2)
               .sendWebWarning("1234567890")
               .thenDelay(2)
               .sendWebWarningOrange("1234567890")
               .thenDelay(2)
               .sendWebWarningRed("1234567890")
               .thenWaitForF8()
               .thenDelay(2)
               .sendWebWarning("/execute in storytelling:bunker run tp @s -19.63 36.00 -12.51 231.39 -1.05")
               .thenDelay(2)
               .sendWebWarningOrange("/execute in storytelling:bunker run tp @s -19.63 36.00 -12.51 231.39 -1.05")
               .thenDelay(2)
               .sendWebWarningRed("/execute in storytelling:bunker run tp @s -19.63 36.00 -12.51 231.39 -1.05");
         }

         @ScriptPerformerBuilderReflective.ScriptPart(1)
         private void part1(S1005 inst) {
            this.saveState("1")
               .thenWaitForF8()
               .thenSetSpeechEntity(0, "Lololowka")
               .thenSpeechMessage(0, "ТестТестТест &0ТестТестТест& &1ТестТестТест& &2ТестТестТест& &3ТестТестТест& &4ТестТестТест&")
               .thenSpeechMessage(0, "&5ТестТестТест &6ТестТестТест& &7ТестТестТест& &8ТестТестТест& &9ТестТестТест&")
               .thenSpeechMessage(0, "&aТестТестТест &bТестТестТест& &cТестТестТест& &dТестТестТест& &eТестТестТест& &fТестТестТест&")
               .thenSpeechMessage(0, "&kТестТестТест &lТестТестТест& &mТестТестТест& &nТестТестТест& &oТестТестТест& &rТестТестТест&")
               .thenSpeechMessage(0, "TestTestTest &0TestTestTest& &1TestTestTest& &2TestTestTest& &3TestTestTest& &4TestTestTest&")
               .thenSpeechMessage(0, "&5TestTestTest &6TestTestTest& &7TestTestTest& &8TestTestTest& &9TestTestTest&")
               .thenSpeechMessage(0, "&aTestTestTest &bTestTestTest& &cTestTestTest& &dTestTestTest& &eTestTestTest& &fTestTestTest&")
               .thenSpeechMessage(0, "&kTestTestTest &lTestTestTest& &mTestTestTest& &nTestTestTest& &oTestTestTest& &rTestTestTest&");
         }

         @ScriptPerformerBuilderReflective.ScriptPart(2)
         private void part2(S1005 inst) {
            this.saveState("2")
               .thenWaitForF8()
               .thenDelay(2)
               .sendWebWarning("ÁÉÍÓÚÝàèìòùâêîôûäëïöüÿãñõåčšžłøđĳ")
               .thenDelay(2)
               .sendWebWarningOrange("ÁÉÍÓÚÝàèìòùâêîôûäëïöüÿãñõåčšžłøđĳ")
               .thenDelay(2)
               .sendWebWarningRed("ÁÉÍÓÚÝàèìòùâêîôûäëïöüÿãñõåčšžłøđĳ")
               .thenWaitForF8()
               .thenDelay(2)
               .sendWebWarning("あいうえおかきくけこさしすせそたちつてとなにぬねのはひふへほまみむめもやゆよらりるれろわをん")
               .thenDelay(2)
               .sendWebWarningOrange("あいうえおかきくけこさしすせそたちつてとなにぬねのはひふへほまみむめもやゆよらりるれろわをん")
               .thenDelay(2)
               .sendWebWarningRed("あいうえおかきくけこさしすせそたちつてとなにぬねのはひふへほまみむめもやゆよらりるれろわをん")
               .thenWaitForF8()
               .thenDelay(2)
               .sendWebWarning("アイウエオカキクケコサシスセソタチツテトナニヌネノハヒフヘホマミムメモヤユヨラリルレロワヲン")
               .thenDelay(2)
               .sendWebWarningOrange("アイウエオカキクケコサシスセソタチツテトナニヌネノハヒフヘホマミムメモヤユヨラリルレロワヲン")
               .thenDelay(2)
               .sendWebWarningRed("アイウエオカキクケコサシスセソタチツテトナニヌネノハヒフヘホマミムメモヤユヨラリルレロワヲン")
               .thenWaitForF8()
               .thenDelay(2)
               .sendWebWarning("中国文学汉字語日本語学校東京北京")
               .thenDelay(2)
               .sendWebWarningOrange("中国文学汉字語日本語学校東京北京")
               .thenDelay(2)
               .sendWebWarningRed("中国文学汉字語日本語学校東京北京")
               .thenWaitForF8()
               .thenDelay(2)
               .sendWebWarning("ابتثجحخدذرزسشصضطظعغفقكلمنهوي")
               .thenDelay(2)
               .sendWebWarningOrange("ابتثجحخدذرزسشصضطظعغفقكلمنهوي")
               .thenDelay(2)
               .sendWebWarningRed("ابتثجحخدذرزسشصضطظعغفقكلمنهوي");
         }

         @ScriptPerformerBuilderReflective.ScriptPart(3)
         private void part3(S1005 inst) {
            this.saveState("3")
               .thenWaitForF8()
               .thenSpeechMessage(0, "&0ÁÉÍÓ &1ÁÉÍÓ& &2ÁÉÍÓ& &3ÁÉÍÓ& &4ÁÉÍÓ&")
               .thenSpeechMessage(0, "&5àèìò &6àèìò& &7àèìò& &8àèìò& &9àèìò&")
               .thenSpeechMessage(0, "&aâêîô &bâêîô& &câêîô& &dâêîô& &eâêîô&")
               .thenSpeechMessage(0, "&fäëïö &käëïö& &läëïö& &mäëïö& &näëïö&")
               .thenSpeechMessage(0, "&oãñõå &rãñõå& &kãñõå& &lãñõå& &mãñõå&")
               .thenSpeechMessage(0, "&0あいうえ &1あいうえ& &2あいうえ& &3あいうえ& &4あいうえ&")
               .thenSpeechMessage(0, "&5かきくけ &6かきくけ& &7かきくけ& &8かきくけ& &9かきくけ&")
               .thenSpeechMessage(0, "&aさしすせ &bさしすせ& &cさしすせ& &dさしすせ& &eさしすせ&")
               .thenSpeechMessage(0, "&fたちつて &kたちつて& &lたちつて& &mたちつて& &nたちつて&")
               .thenSpeechMessage(0, "&oなにぬね &rなにぬね& &kなにぬね& &lなにぬね& &mなにぬね&")
               .thenSpeechMessage(0, "&0アイウエ &1アイウエ& &2アイウエ& &3アイウエ& &4アイウエ&")
               .thenSpeechMessage(0, "&5カキクケ &6カキクケ& &7カキクケ& &8カキクケ& &9カキクケ&")
               .thenSpeechMessage(0, "&aサシスセ &bサシスセ& &cサシスセ& &dサシスセ& &eサシスセ&")
               .thenSpeechMessage(0, "&fタチツテ &kタチツテ& &lタチツテ& &mタチツテ& &nタチツテ&")
               .thenSpeechMessage(0, "&oナニヌネ &rナニヌネ& &kナニヌネ& &lナニヌネ& &mナニヌネ&")
               .thenSpeechMessage(0, "&0中国文学 &1中国文学& &2中国文学& &3中国文学& &4中国文学&")
               .thenSpeechMessage(0, "&5汉字語日 &6汉字語日& &7汉字語日& &8汉字語日& &9汉字語日&")
               .thenSpeechMessage(0, "&a本語学校 &b本語学校& &c本語学校& &d本語学校& &e本語学校&")
               .thenSpeechMessage(0, "&f東京北京 &k東京北京& &l東京北京& &m東京北京& &n東京北京&")
               .thenSpeechMessage(0, "&o語字文汉 &r語字文汉& &k語字文汉& &l語字文汉& &m語字文汉&")
               .thenSpeechMessage(0, "&0ابتثج &1ابتثج& &2ابتثج& &3ابتثج& &4ابتثج&")
               .thenSpeechMessage(0, "&5حخدذر &6حخدذر& &7حخدذر& &8حخدذر& &9حخدذر&")
               .thenSpeechMessage(0, "&aزسشص &bزسشص& &cزسشص& &dزسشص& &eزسشص&")
               .thenSpeechMessage(0, "&fضطظع &kضطظع& &lضطظع& &mضطظع& &nضطظع&")
               .thenSpeechMessage(0, "&oغفقكل &rغفقكل& &kغفقكل& &lغفقكل& &mغفقكل&");
         }
      })
      .build(this::registerMainPerformer);

   public S1005(ScriptEnvironment env, int id) {
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
      this.handleEvent(EventType.ENTITY_CLICK, this::onClick);
   }

   @Override
   public ScriptPerformer handleSetup() {
      return new ScriptPerformerBuilder(this).build();
   }

   public void onClick(EntityInteractionData ctx) {
   }
}
