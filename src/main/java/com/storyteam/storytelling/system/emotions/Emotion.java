package com.storyteam.storytelling.system.emotions;

import lombok.Generated;

public enum Emotion {
   NOOOOO("nooooo"),
   RED_HEART("red_heart"),
   FEARFUL_FACE("fearful_face"),
   THINKING_FACE("thinking_face"),
   SLIGHTLY_SMILING_FACE("slightly_smiling_face"),
   FLUSHED_FACE("flushed_face"),
   LOUDLY_CRYING_FACE("loudly_crying_face"),
   GRINNING_FACE_WITH_SMILING_EYES("grinning_face_with_smiling_eyes"),
   EYES("eyes"),
   SMILING_FACE_WITH_HEART_EYES("smiling_face_with_heart_eyes"),
   STAR_STRUCK("star_struck"),
   PARTYING_FACE("partying_face"),
   OK_HAND("ok_hand"),
   FOLDED_HANDS("folded_hands"),
   SMILING_FACE("smiling_face"),
   THUMBS_UP("thumbs_up"),
   CLAPPING_HANDS("clapping_hands"),
   FACE_WITH_TEARS_OF_JOY("face_with_tears_of_joy"),
   PENSIVE_FACE("pensive_face"),
   SMILING_FACE_WITH_SUNGLASSES("smiling_face_with_sunglasses"),
   FACE_SCREAMING_IN_FEAR("face_screaming_in_fear"),
   PLEADING_FACE("pleading_face"),
   ENRAGED_FACE("enraged_face"),
   EXPLODING_HEAD("exploding_head"),
   FACE_WITH_HEAD_BANDAGE("face_with_head_bandage"),
   FACE_WITH_RAISED_EYEBROW("face_with_raised_eyebrow"),
   FACE_WITH_ROLLING_EYES("face_with_rolling_eyes"),
   FACE_WITH_SPIRAL_EYES("face_with_spiral_eyes"),
   FIRE("fire"),
   MELTING_FACE("melting_face"),
   MOUTH("mouth"),
   NAIL_POLISH("nail_polish"),
   PERSON_FACEPALMING("person_facepalming"),
   PILE_OF_POO("pile_of_poo"),
   POULTRY_LEG("poultry_leg"),
   SALUTING_FACE("saluting_face"),
   SLEEPING_FACE("sleeping_face"),
   SMILING_FACE_WITH_HORNS("smiling_face_with_horns"),
   SMILING_FACE_WITH_TEAR("smiling_face_with_tear"),
   SPARKLES("sparkles"),
   THUMBS_DOWN("thumbs_down"),
   WAVING_HAND("waving_hand"),
   WINKING_FACE("winking_face"),
   ZANY_FACE("zany_face"),
   SAD_BUT_RELIEVED_FACE("sad_but_relieved_face"),
   ANGUISHED_FACE("anguished_face"),
   FACE_EXHALING("face_exhaling"),
   NEUTRAL_FACE("neutral_face"),
   CONFOUNDED_FACE("confounded_face"),
   WEARY_FACE("weary_face"),
   FACE_WITH_PEEKING_EYE("face_with_peeking_eye"),
   DOWNCAST_FACE_WITH_SWEAT("downcast_face_with_sweat");

   private final String path;

   private Emotion(String path) {
      this.path = path;
   }

   @Generated
   public String getPath() {
      return this.path;
   }
}
