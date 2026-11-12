package com.storyteam.storytelling.system.dialog;

import net.minecraft.network.chat.Component;
import org.zeith.hammeranims.api.animation.IAnimationSource;

public record TextInstance(Component text, IAnimationSource animation) {
}
