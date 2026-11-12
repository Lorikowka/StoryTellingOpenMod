package com.storyteam.storytelling.client;

import com.storyteam.storytelling.api.Since;
import java.lang.reflect.Field;
import org.zeith.hammeranims.api.animation.IAnimationSource;

public record AnimationWithMetadata(String name, IAnimationSource animation, Since since, Field referenceField) {
}
