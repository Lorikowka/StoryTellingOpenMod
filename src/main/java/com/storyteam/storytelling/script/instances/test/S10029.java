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
import com.storyteam.storytelling.script.story.characters.Characters;
import com.storyteam.storytelling.system.dialog.Dialog;
import com.storyteam.storytelling.system.emotions.Emotion;
import com.storyteam.storytelling.system.emotions.RenderHolder;
import net.minecraft.server.level.ServerPlayer;
import org.zeith.hammerlib.api.io.NBTSerializable;

@ForgetOnClose
@Script(
   value = 10029,
   description = "Test script (FoxyFine)"
)
public class S10029 extends ScriptInstance {
   public static final int ID = 10029;
   @NBTSerializable
   public final ScriptPerformer mainFlow = (new ScriptPerformerBuilderReflective(this) {
         @ScriptPerformerBuilderReflective.ScriptPart(0)
         private void part0(S10029 inst) {
            this.positionEntity(Characters.ZEITHERON, "/execute in minecraft:overworld run tp @s -180.31 76.00 144.73 -180.11 -1.80");
            this.thenWaitForF8();
            this.dialog(
               new Dialog(98, Characters.ZEITHERON)
                  .setTalkingTo(Characters.ZEITHERON)
                  .addText("Что случилось-то, Lo?")
                  .addAnswer("[Рассказать всё]", true)
                  .addAnswer("[Рассказать, не упоминая личность директора]", false)
                  .addAnswer("[Не рассказывать]", true)
            );
            this.thenWaitForF8();
            this.thenUseEntity(Characters.ZEITHERON, e -> {
               RenderHolder.removeRenderedUUID(Characters.ZEITHERON.getReferenceId());
               RenderHolder.addRenderedUUID(Characters.ZEITHERON.getReferenceId(), Emotion.NOOOOO);
            });
            this.thenWaitForF8();
            this.thenUseEntity(Characters.ZEITHERON, e -> RenderHolder.removeRenderedUUID(Characters.ZEITHERON.getReferenceId()));
         }
      })
      .build(this::registerMainPerformer);

   public S10029(ScriptEnvironment env, int id) {
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
