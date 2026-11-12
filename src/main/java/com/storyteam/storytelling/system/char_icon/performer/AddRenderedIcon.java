package com.storyteam.storytelling.system.char_icon.performer;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.action.ScriptPerformerAction;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.system.char_icon.network.S2CAddItemToRender;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.zeith.hammerlib.net.Network;

public class AddRenderedIcon extends ScriptPerformerAction {
   private final String refId;
   private final ItemStack stack;
   private Vec3 offset = Vec3.ZERO;
   private boolean isDone = false;

   public AddRenderedIcon(ScriptPerformer performer, Character<?> character, ItemStack stack) {
      super(performer);
      this.refId = character.getReferenceId();
      this.stack = stack;
   }

   public AddRenderedIcon(ScriptPerformer performer, Character<?> character, ItemStack stack, Vec3 offset) {
      this(performer, character, stack);
      this.offset = offset;
   }

   @Override
   public void tick() {
      if (!this.isDone) {
         Network.sendToAll(new S2CAddItemToRender(this.refId, this.stack, this.offset));
         this.isDone = true;
      }
   }

   @Override
   public void reset() {
      super.reset();
      this.isDone = false;
   }

   @Override
   public boolean isDone() {
      return this.isDone;
   }

   @Override
   public void fastForwardToStage() {
      Network.sendToAll(new S2CAddItemToRender(this.refId, this.stack, this.offset));
   }

   @Override
   public void globalFastForward() {
      Network.sendToAll(new S2CAddItemToRender(this.refId, this.stack, this.offset));
   }

   @Override
   public HelpReportBuilder gatherHelpReport(HelpReportBuilder builder) {
      return builder.append("Добавление иконки в рендер персонажа.");
   }
}
