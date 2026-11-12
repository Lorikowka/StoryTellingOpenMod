package com.storyteam.storytelling.network;

import java.util.concurrent.CompletableFuture;
import javax.annotation.Nullable;

public record OutboundPacket(String content, int priority, @Nullable CompletableFuture<Void> future) {
}
