package com.storyteam.storytelling.net;

import com.storyteam.storytelling.api.itf.IStoryServer;
import net.minecraft.server.level.ServerPlayer;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.Network;
import org.zeith.hammerlib.net.PacketContext;

/** Requests a fresh server snapshot when the journal key is pressed. */
@MainThreaded
public class PacketQuestJournalRequest implements IPacket {
   @Override public void serverExecute(PacketContext ctx) {
      ServerPlayer player = ctx.getSender();
      IStoryServer storyServer = IStoryServer.get();
      if (player == null || storyServer == null) return;
      Network.sendTo(player, new PacketQuestJournal(storyServer.getScriptEnv().questJournal.snapshot()));
   }
}
