package com.storyteam.storytelling.system._core.morph;

import com.storyteam.storytelling.util.data.react.IContextKeyMapHolder;
import org.zeith.hammeranims.api.tile.IAnimatedEntity;

public interface IAnimatedPlayer extends IAnimatedEntity, IContextKeyMapHolder {
   default void resetPlayerMorph() {
      this.morphInto(null);
   }

   void morphInto(PlayerMorph var1);

   PlayerMorph getMorph();

   boolean isPlayerMotionBlocked();

   void blockPlayerMotion(boolean var1);

   boolean isPlayerJumping();
}
