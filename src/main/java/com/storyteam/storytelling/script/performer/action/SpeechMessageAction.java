package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.client.content.overlays.Message;
import com.storyteam.storytelling.net.PacketMessageOverlay;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;
import com.storyteam.storytelling.script.performer.state.SpeechEntity;
import com.storyteam.storytelling.script.performer.state.SpeechState;
import com.storyteam.storytelling.util.ToStringBuilder;
import java.util.function.Supplier;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.net.Network;

public class SpeechMessageAction extends ScriptPerformerAction {
   protected final int entity;
   private final Supplier<Component> flavor;
   protected final Supplier<Component> message;
   protected final boolean waitForF8;
   private boolean printed;
   private boolean read;

   public SpeechMessageAction(ScriptPerformer performer, int entity, @Nullable Supplier<Component> flavor, Supplier<Component> message, boolean waitForF8) {
      super(performer);
      this.entity = entity;
      this.flavor = flavor;
      this.message = message;
      this.waitForF8 = waitForF8;
   }

   @Override
   public void tick() {
      if (!this.printed) {
         this.printMessage(false);
         this.printed = true;
      }
   }

   @Override
   public void reset() {
      this.printed = false;
      this.read = false;
   }

   @Override
   public void rewindToStage() {
      this.printed = false;
      this.read = false;
   }

   protected SpeechEntity getSpeaker() {
      return ((SpeechState)this.performer.getState(SpeechState.TYPE)).getEntity(this.entity);
   }

   private void printMessage(boolean chatLogOnly) {
      SpeechEntity e = this.getSpeaker();
      Component toSend = this.message.get();
      MutableComponent message = Component.empty().append(toSend);
      MutableComponent spkrName;
      if (e.getName().isEmpty()) {
         spkrName = Component.literal(e.getName());
      } else {
         spkrName = Component.empty().append(Component.literal("[").append(e.getName()).append("]").withStyle(Style.EMPTY.withColor(e.getColor())));
      }

      Component flvr = (Component)(this.flavor == null ? Component.empty() : this.flavor.get());
      MutableComponent flavor = Component.empty();
      if (!flvr.getString().isBlank()) {
         flavor = Component.empty().append(flvr).withStyle(Style.EMPTY.withColor(e.getColor()));
      }

      Message msg = new Message(e.getIcon(), spkrName, flavor, 0, 200, 15, message);
      if (!chatLogOnly) {
         Network.sendToAll(new PacketMessageOverlay(msg));
      }

      this.performer.script.addMessageToChatlog(msg);
   }

   @Override
   public void fastForwardToStage() {
      super.fastForwardToStage();
      this.printMessage(true);
   }

   @Override
   public boolean isDone() {
      if (this.waitForF8 && this.read) {
      }

      return !this.waitForF8 || this.read;
   }

   @Override
   public <T> void handleEvent(EventType<T> eventType, T event) {
      if (eventType == EventType.F8_PRESS) {
         this.read = true;
      }
   }

   @Override
   public HelpReportBuilder gatherHelpReport(HelpReportBuilder builder) {
      return builder.append(
         "Речь персонажа [%s]: %s %s",
         ((SpeechState)this.performer.getState(SpeechState.TYPE)).getEntityName(this.entity),
         this.message.get().getString(),
         this.waitForF8 ? " [F8]" : ""
      );
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this)
         .append("entity", this.entity)
         .append("message", this.message.get() == null ? null : this.message.get().getString())
         .append("waitForF8", this.waitForF8)
         .build();
   }

   public int getEntity() {
      return this.entity;
   }
}
