package com.storyteam.storytelling.client.screen.widgets;

import com.storyteam.storytelling.system._core.st_cutscene.InterpolationType;

public record AnimatedData(int dX, int dY, InterpolationType type, float delay, float speed) {
   public static final AnimatedData EMPTY = new AnimatedData(0, 0, InterpolationType.LINEAR, 0.0F, 1.0F);
}
