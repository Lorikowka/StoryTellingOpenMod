package com.storyteam.storytelling.network;

import com.storyteam.storytelling.network.packets.RequestPacket;
import com.storyteam.storytelling.network.packets.ResponsePacket;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;

/** Compatibility facade for legacy call sites. The remote StoryServer is retired. */
public final class StoryServer {
   private static final StoryServer INSTANCE = new StoryServer();
   private static final String DISABLED = "Remote StoryServer is no longer supported";

   private StoryServer() { }

   public static StoryServer getInstance() {
      return INSTANCE;
   }

   public CompletableFuture<Void> connect() {
      return CompletableFuture.failedFuture(new UnsupportedOperationException(DISABLED));
   }

   public void disconnect() { }

   public int getPacketQueueSize() {
      return 0;
   }

   public void sendPacket(@NotNull BasePacket packet) { }

   public OutboundPacket sendPacket(@NotNull BasePacket packet, int priority) {
      return new OutboundPacket("", priority, null);
   }

   public <T extends ResponsePacket> CompletableFuture<T> sendPacketSync(RequestPacket<T> packet) {
      return CompletableFuture.failedFuture(new UnsupportedOperationException(DISABLED));
   }

   public void sendPacketSync(BasePacket packet, int priority) { }
}
