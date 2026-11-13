package com.storyteam.storytelling.net;

import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.script.ScriptInstance;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.story.LuaScriptInstance;
import com.storyteam.storytelling.util.ScriptUtils;
import net.minecraft.network.FriendlyByteBuf;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

/** Delivers a button press only to the matching active Lua action. */
@MainThreaded
public class PacketLuaChoiceSelection implements IPacket {
   private int scriptId;
   private int actionIndex;
   private int option;

   public PacketLuaChoiceSelection() { }

   public PacketLuaChoiceSelection(int scriptId, int actionIndex, int option) {
      this.scriptId = scriptId;
      this.actionIndex = actionIndex;
      this.option = option;
   }

   @Override public void write(FriendlyByteBuf buf) {
      buf.writeVarInt(scriptId);
      buf.writeVarInt(actionIndex);
      buf.writeVarInt(option);
   }

   @Override public void read(FriendlyByteBuf buf) {
      scriptId = buf.readVarInt();
      actionIndex = buf.readVarInt();
      option = buf.readVarInt();
   }

   @Override public void serverExecute(PacketContext ctx) {
      if (option < 1 || option > 6 || ctx.getSender() != ScriptUtils.getPlayer()) return;
      ScriptInstance script = IStoryServer.get().getScriptEnv().getScript(scriptId);
      if (!(script instanceof LuaScriptInstance)) return;
      ScriptPerformer main = script.getMainPerformer();
      if (main != null && main.getActionIndex() == actionIndex) {
         script.passEvent2Stage(EventType.LUA_CHOICE_SELECTED, Integer.toString(option));
      }
   }
}
