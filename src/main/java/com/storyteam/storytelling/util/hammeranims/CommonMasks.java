package com.storyteam.storytelling.util.hammeranims;

import java.util.List;
import org.zeith.hammeranims.api.animsys.SerializableMask;

public class CommonMasks {
   public static final List<String> RIGHT_EYE = List.of("eye_right", "ocular_right");
   public static final List<String> LEFT_EYE = List.of("eye_left", "ocular_left");
   public static final List<String> RIGHT_HAND = List.of("RightArm", "RightHand", "Right_placeholder");
   public static final List<String> LEFT_HAND = List.of("LeftArm", "LeftHand", "Left_placeholder");

   public static SerializableMask noRightEye() {
      return SerializableMask.builder().excludes(RIGHT_EYE).build();
   }

   public static SerializableMask noLeftEye() {
      return SerializableMask.builder().excludes(LEFT_EYE).build();
   }

   public static SerializableMask noEyes() {
      return SerializableMask.builder().excludes(RIGHT_EYE).excludes(LEFT_EYE).build();
   }

   public static SerializableMask noRightHand() {
      return SerializableMask.builder().excludes(RIGHT_HAND).build();
   }

   public static SerializableMask noLeftHand() {
      return SerializableMask.builder().excludes(LEFT_HAND).build();
   }

   public static SerializableMask noHands() {
      return SerializableMask.builder().excludes(RIGHT_HAND).excludes(LEFT_HAND).build();
   }
}
