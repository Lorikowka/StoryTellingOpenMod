package com.storyteam.storytelling.system._core.morph.client;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;

public interface IAnimatedPlayerRenderer {
   PlayerModel<AbstractClientPlayer> getAnimatedPlayerModel(AbstractClientPlayer var1);
}
