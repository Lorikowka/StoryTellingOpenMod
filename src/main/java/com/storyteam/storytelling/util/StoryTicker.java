package com.storyteam.storytelling.util;

import net.minecraft.server.level.ServerPlayer;

@FunctionalInterface
public interface StoryTicker {
   void tick(ServerPlayer var1);
}
