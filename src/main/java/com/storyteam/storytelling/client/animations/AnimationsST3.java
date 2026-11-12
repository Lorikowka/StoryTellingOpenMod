package com.storyteam.storytelling.client.animations;

import com.storyteam.storytelling.api.Since;
import com.storyteam.storytelling.api.animations.SpecialAnim;
import com.storyteam.storytelling.client.AnimationsRegistrySeasonal;
import com.storyteam.storytelling.client.core.SidedAnimationSet;
import org.zeith.hammeranims.api.animation.IAnimationSource;

@Since(Since.Season.TV)
public interface AnimationsST3 {
   IAnimationSource HUMANOID_ROTATE_HEAD_180_RIGHT = AnimationsRegistrySeasonal.register("humanoid/rotate_head_180", "rotate_head_180_right");
   IAnimationSource HUMANOID_ROTATE_HEAD_180_LEFT = AnimationsRegistrySeasonal.register("humanoid/rotate_head_180", "rotate_head_180_left");
   IAnimationSource HUMANOID_ROTATE_HEAD_180_Z = AnimationsRegistrySeasonal.register("humanoid/rotate_head_180", "rotate_head_180_z");
   IAnimationSource HUMANOID_LOOK_AT_HAND_NO_HANDS_RIGHT = AnimationsRegistrySeasonal.register("humanoid/look_at_right_hand_no_hands");
   IAnimationSource HUMANOID_ROTATE_HEAD_30_DOWN = AnimationsRegistrySeasonal.register("humanoid/head_pose_down_30deg");
   IAnimationSource RIGHT_HAND_45 = AnimationsRegistrySeasonal.register("right_hand_45");
   IAnimationSource LEFT_HAND_45 = AnimationsRegistrySeasonal.register("left_hand_45");
   IAnimationSource HUMANOID_SCRATCHING_BACK_HEAD_RIGHT = AnimationsRegistrySeasonal.register(
      "humanoid/emoc_endanimated_scratchhead", "emoc_endanimated_scratchhead_right"
   );
   IAnimationSource HUMANOID_SCRATCHING_BACK_HEAD_LEFT = AnimationsRegistrySeasonal.register(
      "humanoid/emoc_endanimated_scratchhead", "emoc_endanimated_scratchhead_left"
   );
   IAnimationSource HUMANOID_WIPE_SWEAT = AnimationsRegistrySeasonal.register("hands/hands_endanimated_wipessweat");
   IAnimationSource HUMANOID_WIPE_SWEAT_LEFT = AnimationsRegistrySeasonal.register("hands/hands_endanimated_wipessweat_left");
   IAnimationSource HUMANOID_KNEEL = AnimationsRegistrySeasonal.register("body_pose_kneeling");
   IAnimationSource HUMANOID_SPREADHANDS = AnimationsRegistrySeasonal.register("humanoid/hands_pose_spreadhands");
   IAnimationSource HUMANOID_ARMSBEHINDBACK = AnimationsRegistrySeasonal.register("humanoid/hands_pose_armsbehindback", "hands_pose_armsbehindback_both");
   IAnimationSource ARMBEHINDBACK_LEFT = AnimationsRegistrySeasonal.register("humanoid/hands_pose_armbehindback_left");
   IAnimationSource HUMANOID_WAVE_HAND_LEFT = AnimationsRegistrySeasonal.register("humanoid/hands_cycle_waving_left");
   IAnimationSource RUN_NEW = AnimationsRegistrySeasonal.register("body_cycle_run");
   IAnimationSource HUMANOID_CROSSEDARMS = AnimationsRegistrySeasonal.register("humanoid/hands_pose_crossedarms");
   @SpecialAnim
   IAnimationSource RAPTOR_ATTACK = AnimationsRegistrySeasonal.register("raptor_attack");
   @SpecialAnim
   IAnimationSource RAPTOR_IDLE = AnimationsRegistrySeasonal.register("raptor_stay");
   @SpecialAnim
   IAnimationSource RAPTOR_WALK = AnimationsRegistrySeasonal.register("raptor_walk");
   @SpecialAnim
   IAnimationSource RAPTOR_HORN = AnimationsRegistrySeasonal.register("raptor_horn");
   IAnimationSource HUMANOID_TAKE_OFF_HAT_IN_HANDS = AnimationsRegistrySeasonal.register("humanoid/hat_endanimated_takeoffhat");
   IAnimationSource HUMANOID_PUT_ON_HAT = AnimationsRegistrySeasonal.register("humanoid/hat_endanimated_putonhat");
   IAnimationSource HUMANOID_FRONTKEEPHAT = AnimationsRegistrySeasonal.register("hat/hat_hands_pose_frontkeephat");
   IAnimationSource BODY_PONDER = AnimationsRegistrySeasonal.register("body_pose_ponder", "body_pose_ponder");
   IAnimationSource BODY_SIT_PULL_UP_LEGS = AnimationsRegistrySeasonal.register("body_pose_sitpulluplegs");
   IAnimationSource BODY_SIT_PULL_UP_LEGS_WITHOUT_HANDS = AnimationsRegistrySeasonal.register("humanoid/body_animated_sitground_without_hands");
   IAnimationSource HUMANOID_SIT_GROUND_TO_STAY = AnimationsRegistrySeasonal.register("body_endanimated_sitgroundtostay");
   IAnimationSource BODY_POSE_INTERCEDE = AnimationsRegistrySeasonal.register("body_pose_intercede");
   IAnimationSource BODY_POSE_STEND = AnimationsRegistrySeasonal.register("body_pose_stend");
   IAnimationSource EMOTION_INSULT = AnimationsRegistrySeasonal.register("emotions/emoc_pose_sad");
   IAnimationSource EMOTION_PRIDE = AnimationsRegistrySeasonal.register("emotions/emoc_pose_pride", "emoc_pose_pride_withouthands");
   IAnimationSource EMOTION_POSE_DISGUST = AnimationsRegistrySeasonal.register("emotions/emoc_pose_disgust");
   IAnimationSource EMOTION_INSPIRATION_2 = AnimationsRegistrySeasonal.register("emotions/emoc_pose_insipiration_2");
   IAnimationSource EMOTION_INSPIRATION_2_NO_MOUTH = AnimationsRegistrySeasonal.register("emotions/inspiration_no_mouth");
   IAnimationSource HANDS_ENDANIMATED_TREMBLING = AnimationsRegistrySeasonal.register("hands_endanimated_trembling");
   IAnimationSource PLACEHOLDER_CYCLE_SWINGINGSWORD = AnimationsRegistrySeasonal.register("placeholder_cycle_swingingsword");
   IAnimationSource BODY_ENDANIMATED_SPINPOSING = AnimationsRegistrySeasonal.register("body_endanimated_spinposing");
   IAnimationSource BODY_ENDANIMATED_SPINPOSING_LOOP = AnimationsRegistrySeasonal.register("body_endanimated_spinposing_loop");
   IAnimationSource HAT_HANDS_ENDANIMATED_TAKESOFFHAT = AnimationsRegistrySeasonal.register("hat_hands_endanimated_takesoffhat");
   IAnimationSource HANDS_ENDANIMATED_BOOKOPEN = AnimationsRegistrySeasonal.register("humanoid/hands_endanimated_bookopen", "hands_endanimated_boockopen");
   IAnimationSource HANDS_ENDANIMATED_BOOKCLOSE = AnimationsRegistrySeasonal.register("humanoid/hands_endanimated_bookopen", "hands_endanimated_boockclose");
   IAnimationSource HANDS_ENDANIMATED_EXAMINEHANDS = AnimationsRegistrySeasonal.register("humanoid/hands_endanimated_examinehands");
   IAnimationSource HAT_ENDANIMATED_TAKEOFFHAT = AnimationsRegistrySeasonal.register("hat/hat_endanimated_takeoffhat");
   IAnimationSource HAT_ENDANIMATED_PUTONHAT = AnimationsRegistrySeasonal.register("hat/hat_endanimated_putonhat");
   IAnimationSource HANDS_ENDANIMATED_HITTHETABLE = AnimationsRegistrySeasonal.register("hands/hands_endanimated_hitthetable");
   IAnimationSource YAWN_RIGHT = AnimationsRegistrySeasonal.register("humanoid/hands_endanimated_yawn_right", "hands_endanimated_yawn");
   IAnimationSource YAWN_LEFT = AnimationsRegistrySeasonal.register("humanoid/hands_endanimated_yawn_left", "hands_endanimated_yawn");
   IAnimationSource YAWN_RIGHT_NO_EMOTIONS = AnimationsRegistrySeasonal.register(
      "humanoid/hands_endanimated_yawn_right", "hands_endanimated_yawn_right_withoutemoc"
   );
   IAnimationSource HUMANOID_DRUNK = AnimationsRegistrySeasonal.register("hands_cycle_drunk");
   IAnimationSource HUMANOID_HANDS_SURRENDER = AnimationsRegistrySeasonal.register("hands_pose_surrender");
   IAnimationSource EXAMINE = AnimationsRegistrySeasonal.register("body_cycle_examine");
   IAnimationSource EMOTION_EXAMINE = AnimationsRegistrySeasonal.register("emoc_cycle_examine");
   IAnimationSource USE_MAGNIFYING_GLASS = AnimationsRegistrySeasonal.register("placeholder_pose_looking_magnifying_glass");
   IAnimationSource HANDS_SWARMING_UP = AnimationsRegistrySeasonal.register("hands_cycle_searchfromabove");
   IAnimationSource HUMANOID_NAMAZ_POSE = AnimationsRegistrySeasonal.register("body_pose_namaz");
   IAnimationSource PRAY = AnimationsRegistrySeasonal.register("hand_pose_lookatsky");
   IAnimationSource LOOK_INTO_MUG = AnimationsRegistrySeasonal.register("hand_pose_lookintomug_right");
   IAnimationSource WIPE_TABLE = AnimationsRegistrySeasonal.register("hands_cycle_wipingtable");
   IAnimationSource FOLD_HAND_SON_STOMACH = AnimationsRegistrySeasonal.register("humanoid/hands_pose_foldhandsonstomach");
   IAnimationSource WIPE_EYES = AnimationsRegistrySeasonal.register("humanoid/hands_endanimated_wipeseyes");
   IAnimationSource HANG_ON_CHAINS = AnimationsRegistrySeasonal.register("humanoid/body_pose_hangingonchains");
   IAnimationSource TILT_BODY = AnimationsRegistrySeasonal.register("humanoid/body_pose_tilt");
   IAnimationSource HOLD_ITEM = AnimationsRegistrySeasonal.register("placeholder_pose_holdatarmslength");
   @SpecialAnim("METAL_DOOR")
   IAnimationSource OPENING_METAL_DOOR = AnimationsRegistrySeasonal.register("metal_door_anim", "metal_door_anim");
   @SpecialAnim("METAL_DOOR")
   IAnimationSource METAL_DOOR_POSE = AnimationsRegistrySeasonal.register("metal_door_anim", "metal_door_pose");
   IAnimationSource SAD_LEAN_WALL = AnimationsRegistrySeasonal.register("humanoid/hands_pose_sadleanswall");
   IAnimationSource CALM_BREATH = AnimationsRegistrySeasonal.register("humanoid/body_endanimated_calmsdown_scale");
   IAnimationSource HUMANOID_WRITING = AnimationsRegistrySeasonal.register("humanoid/hands_cycle_writing_sitting");
   IAnimationSource HUMANOID_ATTACH_TO_CEIL = AnimationsRegistrySeasonal.register("humanoid/body_pose_attach_to_ceil");
   IAnimationSource HUMANOID_STAY_TO_SIT_GROUND = AnimationsRegistrySeasonal.register("humanoid/body_endanimated_sitonground");
   IAnimationSource HUMANOID_BRING_OGJECT_TO_EAR_RIGHT = AnimationsRegistrySeasonal.register("humanoid/placeholder_pose_bringobjecttoear_right");
   IAnimationSource HUMANOID_COVER_FACE = AnimationsRegistrySeasonal.register("humanoid/hands_pose_coversfacehand_right");
   IAnimationSource HUMANOID_HAND_ACROSS_OBJECT = AnimationsRegistrySeasonal.register("humanoid/hands_endanimated_patshead_lying");
   IAnimationSource HUMANOID_PUT_ITEM_ON_HEAD = AnimationsRegistrySeasonal.register("humanoid/placeholder_pose_puttingonbook");
   @SpecialAnim
   IAnimationSource PORTAL_IDLE = AnimationsRegistrySeasonal.register("portal_1_anims", "portal_1_idle");
   @SpecialAnim
   IAnimationSource PORTAL_OPENING = AnimationsRegistrySeasonal.register("portal_1_anims", "portal_1_open");
   @SpecialAnim
   IAnimationSource PORTAL2_IDLE = AnimationsRegistrySeasonal.register("portal_2_anims", "portal_1_idle");
   @SpecialAnim
   IAnimationSource PORTAL2_OPENING = AnimationsRegistrySeasonal.register("portal_2_anims", "portal_1_open");
   IAnimationSource HANDS_POSE_AGITATES = AnimationsRegistrySeasonal.register("hands/hands_pose_agitates");
   IAnimationSource BODY_POSE_SHOCK = AnimationsRegistrySeasonal.register("body/body_pose_shock");
   IAnimationSource BODY_POSE_SHOCK_LEFT = AnimationsRegistrySeasonal.register("body/body_pose_shock_left");
   IAnimationSource BODY_POSE_SHOCK_ONLY_FACE = AnimationsRegistrySeasonal.register("body/body_pose_shock_only_face");
   IAnimationSource PLACEHOLDER_ENDANIMATED_PUTTINGITEMONTABLE = AnimationsRegistrySeasonal.register("placeholder/placeholder_endanimated_puttingitemontable");
   IAnimationSource HANDS_POSE_HANDSONSIDES = AnimationsRegistrySeasonal.register("hands/hands_pose_handsonsides");
   IAnimationSource PLACEHOLDER_CYCLE_BARBARIAN = AnimationsRegistrySeasonal.register("placeholder/placeholder_cycle_barbarian");
   IAnimationSource EMOTION_POSE_NIGHTMARE = AnimationsRegistrySeasonal.register("humanoid/emoc_pose_nightmare");
   IAnimationSource HUMANOID_SHORT_BOW = AnimationsRegistrySeasonal.register("humanoid/body_andanimated_bow");
   IAnimationSource HUMANOID_LIEONELBOWS = AnimationsRegistrySeasonal.register("humanoid/body_pose_lieonelbows");
   IAnimationSource HUMANOID_STROKING = AnimationsRegistrySeasonal.register("humanoid/body_cycle_stroking");
   IAnimationSource PLACEHOLDER_HOLDS_IN_FRONT_UP = AnimationsRegistrySeasonal.register("placeholder/placeholder_pose_holdsinfront_up");
   IAnimationSource HANDS_NEVERMIND = AnimationsRegistrySeasonal.register("hands_endanimated_nevermind");
   IAnimationSource ENDANIMATED_FAINTING = AnimationsRegistrySeasonal.register("body_endanimated_fainting");
   IAnimationSource HANDS_POSE_PUSHING = AnimationsRegistrySeasonal.register("hands/hands_pose_pushing");
   IAnimationSource PLACEHOLDER_POSE_HOLD_SHOOTGUN = AnimationsRegistrySeasonal.register("placeholder/placeholder_pose_hold_shootgun");
   IAnimationSource PLACEHOLDER_ENDANIMATED_SHOOT_SHOOTGUN = AnimationsRegistrySeasonal.register("placeholder/placeholder_endanimated_shoot_shootgun");
   IAnimationSource RIGHT_HAND_WITH_SWORD = AnimationsRegistrySeasonal.register("humanoid/right_hand_with_sword");
   IAnimationSource LEAN_ON_WALL_HAND_ON_SIDE = AnimationsRegistrySeasonal.register("hands_pose_leanedtowall");
   IAnimationSource BODY_POSE_STRAIGHT_LEGS_SIT_GROUND_LEFT = AnimationsRegistrySeasonal.register("humanoid/body_pose_straightlegsitground_left");
   IAnimationSource SIDE_BUMB_LEFT = AnimationsRegistrySeasonal.register("hands/hands_endanimated_sidebump");
   IAnimationSource SIDE_BUMB_RIGHT = AnimationsRegistrySeasonal.register("hands/hands_endanimated_sidebump_left");
   IAnimationSource HANDS_CYCLE_FACE = AnimationsRegistrySeasonal.register("hands/hands_cycle_facecoolingshake");
   IAnimationSource POSE_KEEPCUP_RIGHT = AnimationsRegistrySeasonal.register("hands/placeholder_pose_keepcup_right");
   IAnimationSource POSE_HOLDING_TRAY = AnimationsRegistrySeasonal.register("placeholder/placeholder_pose_holdingtray_right");
   IAnimationSource ELDER_BODY_WALK = AnimationsRegistrySeasonal.register("elder_body_animation_walk");
   IAnimationSource ELDER_BODY_STAY = AnimationsRegistrySeasonal.register("elder_body_pose_stay");
   IAnimationSource HANDS_LEANED_TO_WALL = AnimationsRegistrySeasonal.register("hands/hands_pose_leanedtowall");
   IAnimationSource HUMANOID_DRINK_SPECIAL = AnimationsRegistrySeasonal.register("humanoid/body_drink_in_special_pose");
   IAnimationSource CYCLE_CRAWL = AnimationsRegistrySeasonal.register("body_cycle_crawl");
   IAnimationSource ENDANIMATED_CRAWL_TO_FALL = AnimationsRegistrySeasonal.register("body_endanimated_crawl_to_fall");
   IAnimationSource ENDANIMATED_CRAWL = AnimationsRegistrySeasonal.register("body_endanimation_crawl");
   IAnimationSource ENDANIMATED_LAY_ON_BELLY = AnimationsRegistrySeasonal.register("body_endanimation_layonbelly");
   IAnimationSource LAY_ON_BELLY = AnimationsRegistrySeasonal.register("body_pose_layonbelly");
   IAnimationSource EMOTION_QUESTION = AnimationsRegistrySeasonal.register("emotions/emoc_pose_question");
   IAnimationSource FACECOOLINGSHAKE = AnimationsRegistrySeasonal.register("hands_cycle_facecoolingshake");
   IAnimationSource DRINK = AnimationsRegistrySeasonal.register("placeholder_pose_drinkcup_right");
   IAnimationSource DRINK_LEFT = AnimationsRegistrySeasonal.register("placeholder_pose_drinkcup_left");
   IAnimationSource HUMANOID_POSE_LAY = AnimationsRegistrySeasonal.register("humanoid/body_pose_lay");
   IAnimationSource EO_HAT_ON_FACE = AnimationsRegistrySeasonal.register("hat_endanimated_hatonface");
   IAnimationSource EO_HAT_CRYING = AnimationsRegistrySeasonal.register("hat_cycle_hatcrying");
   IAnimationSource BREAST_SELF_PUNCH = AnimationsRegistrySeasonal.register("hand_endanimated_breastselfpunch");
   IAnimationSource ENDANIMATED_LAY_ON_BELLY_TO_SIT_GROUND = AnimationsRegistrySeasonal.register("body_endanimated_layonbellytositground");
   IAnimationSource WIPE_TEARS_RIGHT = AnimationsRegistrySeasonal.register("hands_endanimated_wipeseye_right");
   IAnimationSource WIPE_TEARS_LEFT = AnimationsRegistrySeasonal.register("hands_endanimated_wipeseye_left");
   IAnimationSource GRAB_THROAT = AnimationsRegistrySeasonal.register("hands_pose_grabthroat");
   IAnimationSource LOSE_FISH = AnimationsRegistrySeasonal.register("body_endanimated_fishlose");
   IAnimationSource HUMANOID_CLAPPING = AnimationsRegistrySeasonal.register("humanoid/hands_cycle_clapping");
   IAnimationSource BODY_ENDANIMATED_STANDONHANDS = AnimationsRegistrySeasonal.register("body/body_endanimated_standonhands");
   IAnimationSource BODY_ENDANIMATED_STANDONHANDSBACK = AnimationsRegistrySeasonal.register("body/body_endanimated_standonhandsback");
   IAnimationSource BODY_POSE_LYING_ON_SIDE_BASE = AnimationsRegistrySeasonal.register("body/body_pose_lyingonside");
   IAnimationSource S2005_HAND_ON_SHOULDER_RIGHT = AnimationsRegistrySeasonal.register("custom/s2005/s2005_hand_on_shoulder_right");
   IAnimationSource S2005_HAND_ON_SHOULDER_LEFT = AnimationsRegistrySeasonal.register("custom/s2005/s2005_hand_on_shoulder_left");
   IAnimationSource HUMANOID_OATH = AnimationsRegistrySeasonal.register("hands/hands_pose_oath");
   IAnimationSource LEAN_ON_ELBOWS = AnimationsRegistrySeasonal.register("humanoid/body_pose_leanonelbows_layer");
   IAnimationSource BODY_CYCLE_SWIMMING_VERSAWHALE = AnimationsRegistrySeasonal.register("body/body_cycle_swimming_versawhale");
   IAnimationSource BODY_POSE_LAYONBELLY_SPEC = AnimationsRegistrySeasonal.register("body/body_pose_layonbelly_spec");
   IAnimationSource BODY_POSE_BOW = AnimationsRegistrySeasonal.register("body/body_pose_bow");
   IAnimationSource EMOTION_LOOKING_DOWN = AnimationsRegistrySeasonal.register("emotions/emoc_pose_lookingdown");
   IAnimationSource HANDS_CYCLE_PERSUASIONSWINGHANDS = AnimationsRegistrySeasonal.register("hands/fat_hands_cycle_persuasionswinghands");
   IAnimationSource WHEELCHAIR_BODY_CYCLE_WALK = AnimationsRegistrySeasonal.register("body/wheelchair_body_cycle_walk");
   IAnimationSource BODY_POSE_PSHYHO = AnimationsRegistrySeasonal.register("body/body_pose_psycho");
   IAnimationSource BODY_ENDANIMATED_CHESTPUNCH = AnimationsRegistrySeasonal.register("body/body_endanimated_chestpunch");
   IAnimationSource POSE_LOTUS = AnimationsRegistrySeasonal.register("body_pose_lotusposition");
   IAnimationSource POSE_LOTUS_NO_HANDS = AnimationsRegistrySeasonal.register("lotus_pose_nohands");
   IAnimationSource POSE_LOTUS_ONLY_HANDS = AnimationsRegistrySeasonal.register("body_pose_lotusposition_only_hands");
   IAnimationSource SIT_ON_STAIRS = AnimationsRegistrySeasonal.register("body_pose_sit_stairs");
   IAnimationSource BODY_POSE_SIT_STAIRS = AnimationsRegistrySeasonal.register("body_pose_sit_stairs_new", "body_pose_sit_stairs");
   IAnimationSource ARMS_CRY_LAYER = AnimationsRegistrySeasonal.register("body_pose_sit_stairs_new", "arms_cry_layer");
   IAnimationSource ARMS_HEAD_LAYER = AnimationsRegistrySeasonal.register("body_pose_sit_stairs_new", "arms_cry_head_layer");
   IAnimationSource POCKETS_CHECK = AnimationsRegistrySeasonal.register("hands/hands_endanimated_pocketscheck");
   IAnimationSource MIDJUMP = AnimationsRegistrySeasonal.register("body/body_cycle_midjump_florist");
   IAnimationSource MIDJUMP_WITHOUT = AnimationsRegistrySeasonal.register("body/body_cycle_midjump_florist_without_hands");
   IAnimationSource JUMP_START = AnimationsRegistrySeasonal.register("body/body_endanimated_jump1_florist");
   IAnimationSource JUMP_END = AnimationsRegistrySeasonal.register("body/body_endanimated_jump2_florist");
   IAnimationSource HANDS_POSE_MESSENGER = AnimationsRegistrySeasonal.register("hands/hands_pose_messenger_storytelling");
   IAnimationSource EATINGTWOHANDS_RIGHT = AnimationsRegistrySeasonal.register("hands/hands_cycle_eatingonehand_right");
   IAnimationSource EATINGTWOHANDS_RIGHT_END = AnimationsRegistrySeasonal.register("hands/hands_cycle_eatingonehand_right_end");
   IAnimationSource HANDS_POINT_UP = AnimationsRegistrySeasonal.register("hands/hands_endanimated_pointup");
   IAnimationSource HAND_POSE_FACEPALM_LEFT = AnimationsRegistrySeasonal.register("hands/hand_pose_facepalm_left");
   IAnimationSource COVER_FACE = AnimationsRegistrySeasonal.register("hands_pose_facecover");
   IAnimationSource GRAB_STOMACH = AnimationsRegistrySeasonal.register("body_pose_grabbedstomach");
   IAnimationSource HICCUP = AnimationsRegistrySeasonal.register("body_cycle_hiccup");
   IAnimationSource HANDS_AT_MOUTH = AnimationsRegistrySeasonal.register("hands_pose_handsatmouth");
   IAnimationSource FARAGONDA_HANDS_POSE_FACECOVER = AnimationsRegistrySeasonal.register("faragonda_hands_pose_facecover");
   IAnimationSource FIST_BUMP = AnimationsRegistrySeasonal.register("hands_endanimated_fistbump");
   IAnimationSource INLOVE_POSE = AnimationsRegistrySeasonal.register("hands/inlove_pose");
   IAnimationSource SIT_DUCK_POSE = AnimationsRegistrySeasonal.register("sitduck_pose");
   IAnimationSource FLIGHT = AnimationsRegistrySeasonal.register("flight");
   IAnimationSource SUPERMAN_POSE = AnimationsRegistrySeasonal.register("body/superman_pose");
   IAnimationSource CHICK_HOLD_POSE = AnimationsRegistrySeasonal.register("chick_hold_pose");
   IAnimationSource CHICK_WALK = AnimationsRegistrySeasonal.register("chick_walk");
   IAnimationSource SEXY_LAY = AnimationsRegistrySeasonal.register("sexy_lay");
   IAnimationSource SEXY_LAY_TO_SITPULLEGS = AnimationsRegistrySeasonal.register("sexylay_to_sitpulllegs_improved");
   IAnimationSource HEADINSULT_ENDANIMATED = AnimationsRegistrySeasonal.register("headinsult_endanimated");
   IAnimationSource HEELSWALK_CYCLE = AnimationsRegistrySeasonal.register("heelswalk_cycle");
   IAnimationSource BODY_ENDANIMATED_HAMMERBLOW_LEFT = AnimationsRegistrySeasonal.register("body_endanimated_hammerblow_left");
   IAnimationSource CATCH_FISH = AnimationsRegistrySeasonal.register("placeholder_endanimated_catch_fishingrod");
   IAnimationSource THROW_FISHING_ROD = AnimationsRegistrySeasonal.register("placeholder_endanimated_throw_fishingrod");
   IAnimationSource PULL_FISHING_ROD = AnimationsRegistrySeasonal.register("placeholder_pose_pull_fishingrod");
   IAnimationSource SPIN_FISHING_ROD = AnimationsRegistrySeasonal.register("placeholder_cycle_spinning_fishingrod");
   IAnimationSource FACEPALM_LEFT = AnimationsRegistrySeasonal.register("hand_pose_facepalm_left");
   IAnimationSource SIT_IN_BOAT = AnimationsRegistrySeasonal.register("body/body_sit_in_boat_without_hands");
   IAnimationSource POSE_CRAWLING_NO_BUGS = AnimationsRegistrySeasonal.register("body_endanimation_crawl_norh");
   @SpecialAnim
   IAnimationSource RABBIT_SIT = AnimationsRegistrySeasonal.register("body_pose_sit_rabbit");
   @SpecialAnim
   IAnimationSource RABBIT_JUMPING = AnimationsRegistrySeasonal.register("body_cycle_run_rabbit");
   @SpecialAnim
   IAnimationSource RABBIT_FROM_HOLDER_POSE_TO_SIT = AnimationsRegistrySeasonal.register("body_endanimated_jumpdown_rabbit");
   @SpecialAnim
   IAnimationSource RABBIT_IDLE = AnimationsRegistrySeasonal.register("body_cycle_idle_rabbit");
   @SpecialAnim
   IAnimationSource RABBIT_HOLDER_POSE = AnimationsRegistrySeasonal.register("body/body_pose_holdarmpits_rabbit");
   @SpecialAnim
   IAnimationSource RABBIT_HOLDER_POSE_EARS = AnimationsRegistrySeasonal.register("body_pose_holdears_rabbit");
   @SpecialAnim
   IAnimationSource RABBIT_TURN = AnimationsRegistrySeasonal.register("head_cycle_headturn_rabbit");
   IAnimationSource HOLD_RABBIT = AnimationsRegistrySeasonal.register("hands/hands_pose_holdrabbitears");
   IAnimationSource HOLD_RABBIT_PITS = AnimationsRegistrySeasonal.register("hands_pose_holdrabbitarmpits");
   IAnimationSource HOLD_TO_RABBIT_PETTING_ACTION = AnimationsRegistrySeasonal.register("hands/hands_cycle_petrabbit");
   IAnimationSource HOLD_TO_RABBIT_PETTING_POSE = AnimationsRegistrySeasonal.register("body/body_pose_squatdown");
   IAnimationSource WALK_NEW = AnimationsRegistrySeasonal.register("body_cycle_walk");
   IAnimationSource WALK_NEW_WITHOUT_HANDS = AnimationsRegistrySeasonal.register("walk1_cycle_nohands");
   IAnimationSource WALK_NEW_ALEX = AnimationsRegistrySeasonal.register("body_cycle_alexwalk");
   IAnimationSource BODY_ENDANIMATED_HAPPY_JUMP = AnimationsRegistrySeasonal.register("body/body_endanimated_happyjump");
   IAnimationSource BODY_ENDANIMATED_HAPPY_JUMP_NOHANDS_LOOP = AnimationsRegistrySeasonal.register("body/body_endanimated_happyjump_nohands_loop");
   IAnimationSource HANDS_CYCLE_EATING_TWO_HANDS = AnimationsRegistrySeasonal.register("hands/hands_cycle_eatingtwohands");
   IAnimationSource BODY_POSE_UPSIDEDOWN = AnimationsRegistrySeasonal.register("body/body_pose_upsidedown");
   IAnimationSource BODY_CYCLE_WALLBEAT = AnimationsRegistrySeasonal.register("body/body_cycle_wallbeat");
   IAnimationSource PLACEHOLDER_POSE_POINT = AnimationsRegistrySeasonal.register("placeholder/placeholder_pose_point");
   IAnimationSource HANDS_POSE_HANDATMOUTH_RIGHT = AnimationsRegistrySeasonal.register("hands/hands_pose_handatmouth_right");
   IAnimationSource STAY_TAIL = AnimationsRegistrySeasonal.register("stay_tail");
   IAnimationSource HANDS_ENDANIMATED_RUB_CHEEK = AnimationsRegistrySeasonal.register("hands/hands_endanimated_cheekpat");
   IAnimationSource HANDS_POSE_CHEERS_UPARM = AnimationsRegistrySeasonal.register("hands/hands_pose_cheers_uparm");
   IAnimationSource BODY_POSE_DOUBLEHUG = AnimationsRegistrySeasonal.register("body/body_pose_doublehug");
   IAnimationSource HANDS_ENDANIMATED_DRINKTOBOTTOM = AnimationsRegistrySeasonal.register("hands/hands_endanimated_drinktobottom");
   IAnimationSource BODY_CYCLE_DINERO = AnimationsRegistrySeasonal.register("body/body_cycle_dinero");
   IAnimationSource PLACEHOLDER_POSE_DEFEATING = AnimationsRegistrySeasonal.register("placeholder/placeholder_pose_defeating");
   IAnimationSource PLACE_HOLDER_SWORD_ATTACK = AnimationsRegistrySeasonal.register("placeholder/placeholder_endanimated_swordattack");
   IAnimationSource PLACEHOLDER_CYCLE_AXEATTACK = AnimationsRegistrySeasonal.register("placeholder/placeholder_cycle_axeattack");
   IAnimationSource PLACEHOLDER_CYCLE_BLOCKATTACK = AnimationsRegistrySeasonal.register("placeholder/placeholder_pose_blockattack");
   IAnimationSource SWING_BEFORE_HITTING_RIGHT = AnimationsRegistrySeasonal.register("body/body_pose_swingbeforehitting_right");
   IAnimationSource DRUNKEN_SWORD_DANCE = AnimationsRegistrySeasonal.register("placeholder_cycle_drunksworddance");
   IAnimationSource BODY_POSE_STOMACH_HURT = AnimationsRegistrySeasonal.register("body_pose_stomachhurt");
   IAnimationSource HUMANOID_ARMS_CROSSED = AnimationsRegistrySeasonal.register("hands/hands_pose_armscrossed_sit");
   IAnimationSource HUMANOID_ARMS_CROSSED_HIT = AnimationsRegistrySeasonal.register("hands/hands_endanimated_tablehit_armscrossed");
   IAnimationSource HUMANOID_FLOAT = AnimationsRegistrySeasonal.register("body/body_cycle_float");
   IAnimationSource HUMANOID_KNEEL_TO_SIDE_LAY = AnimationsRegistrySeasonal.register("body/body_endanimated_kneestosidelay");
   IAnimationSource HUMANOID_SIDE_LAY_TO_SIT_GROUND = AnimationsRegistrySeasonal.register("body/body_endanimated_sidelaytosit");
   IAnimationSource HUMANOID_SIDE_LAY_TO_SIT_GROUND_RIGHT = AnimationsRegistrySeasonal.register("body_endanimated_sidelaytosit_right");
   IAnimationSource HUMANOID_DRINK_GIGA = AnimationsRegistrySeasonal.register("placeholder/placeholder_endanimated_drink");
   IAnimationSource HANDS_ENDANIMATED_TEMPLEKNOCK = AnimationsRegistrySeasonal.register("hands/hands_endanimated_templeknock");
   @SpecialAnim
   IAnimationSource EAGLE_BODY_CYCLE_FLYING = AnimationsRegistrySeasonal.register("eagle/body_cycle_flying_eagle");
   IAnimationSource NOOSE_BODY_CYCLE_IDLEWITHOUTMAN = AnimationsRegistrySeasonal.register("noose_body_cycle_idlewithoutman");
   IAnimationSource NOOSE_BODY_CYCLE_IDLE = AnimationsRegistrySeasonal.register("noose_body_cycle_idle");
   IAnimationSource HANDS_TO_HEAD_WHILE_SITTING = AnimationsRegistrySeasonal.register("body_pose_propheadwithhands");
   IAnimationSource WHEEL = AnimationsRegistrySeasonal.register("body/body_endanimated_wheel");
   IAnimationSource WHEEL_LEFT = AnimationsRegistrySeasonal.register("wheel_left");
   IAnimationSource ROLL = AnimationsRegistrySeasonal.register("body/body_endanimated_rolling");
   IAnimationSource ROLL_GET_UP = AnimationsRegistrySeasonal.register("body/body_endanimated_rollgetup");
   IAnimationSource HANDS_GET_OUT = AnimationsRegistrySeasonal.register("hands_endanimated_getoff");
   IAnimationSource HANDS_PULL_FROM_BODY = AnimationsRegistrySeasonal.register("hands_endanimated_pull");
   IAnimationSource HUMANOID_READING = AnimationsRegistrySeasonal.register("hands/hands_cycle_reading_twohands");
   IAnimationSource HUMANOID_WRITE = AnimationsRegistrySeasonal.register("placeholder/placeholder_cycle_writing_twohands");
   IAnimationSource DOOR_ENDANIMATED_FALLSTRANGE = AnimationsRegistrySeasonal.register("door/door_endanimated_fallstrange");
   IAnimationSource DOOR_OPEN = AnimationsRegistrySeasonal.register("door/door.open");
   IAnimationSource LION_GATES_OPEN = AnimationsRegistrySeasonal.register("door/liongates_open");
   IAnimationSource HEAD_COVER = AnimationsRegistrySeasonal.register("hands/hands_pose_headcover");
   IAnimationSource FALL_ON_THE_BACK = AnimationsRegistrySeasonal.register("body/body_endanimated_falldownpush");
   IAnimationSource MONSTER_1_IDLE = AnimationsRegistrySeasonal.register("monster/monster1_body_cycle_idle");
   IAnimationSource MONSTER_2_IDLE = AnimationsRegistrySeasonal.register("monster/monster2_cycle_idle");
   IAnimationSource MONSTER_2_APPEAR = AnimationsRegistrySeasonal.register("monster/monster2_endanimated_appear");
   IAnimationSource MONSTER_2_ATTACK = AnimationsRegistrySeasonal.register("monster/monster2_endanimated_attack");
   IAnimationSource MONSTER_2_DISAPPEAR = AnimationsRegistrySeasonal.register("monster/monster2_endanimated_disappear");
   IAnimationSource MONSTER_3_ATTACK = AnimationsRegistrySeasonal.register("monster/monster3_cycle_attack");
   IAnimationSource MONSTER_3_IDLE = AnimationsRegistrySeasonal.register("monster/monster3_cycle_idle");
   IAnimationSource MONSTER_3_WALK = AnimationsRegistrySeasonal.register("monster/monster3_cycle_walk");
   IAnimationSource MONSTER_4_MEAT_WALK = AnimationsRegistrySeasonal.register("monster/monster4meat_body_cycle_walk");
   IAnimationSource SCRATCHBACK = AnimationsRegistrySeasonal.register("hands/hands_endanimated_scratchback");
   IAnimationSource SIT_GROUND_TO_STAY_WITH_HELP = AnimationsRegistrySeasonal.register("body/body_endanimated_sitpulluplegs_getup");
   IAnimationSource HUMANOID_LICK_ARM = AnimationsRegistrySeasonal.register("hands/hands_cycle_pawlicking");
   IAnimationSource EMOC_POSE_SIDELOOK = AnimationsRegistrySeasonal.register("emotions/emoc_pose_sidelook");
   IAnimationSource PLACEHOLDER_POSE_EATMOUSE = AnimationsRegistrySeasonal.register("placeholder/placeholder_pose_eatmouse");
   IAnimationSource HUMANOID_WAVE_UP_WAND = AnimationsRegistrySeasonal.register("hands_endanimated_waveupwand", "hands_endanimated_wavewand_up");
   IAnimationSource ARMRESTLING_FIGHT = AnimationsRegistrySeasonal.register("hands/hands_cycle_fight_armwrestling");
   IAnimationSource ARMRESTLING_DEFEAT = AnimationsRegistrySeasonal.register("hands/hands_endanimated_defeat_armwrestling");
   IAnimationSource ARMRESTLING_PREPARE = AnimationsRegistrySeasonal.register("hands/hands_pose_prepare_armwrestling");
   IAnimationSource ARMRESTLING_VICTORY = AnimationsRegistrySeasonal.register("hands/hands_pose_victory_armwrestling");
   @SpecialAnim
   IAnimationSource BODY_POSE_SLEEPDOG = AnimationsRegistrySeasonal.register("body_pose_sleepdog");
   @SpecialAnim
   IAnimationSource BODY_POSE_SLEEPDOG_LEFT = AnimationsRegistrySeasonal.register("body_pose_sleepdog_left");
   @SpecialAnim
   IAnimationSource BODY_POSE_SLEEPDOG_RIGHT = AnimationsRegistrySeasonal.register("body_pose_sleedog_right");
   @SpecialAnim
   IAnimationSource SLEEPDOG_TO_SIT_LEFT = AnimationsRegistrySeasonal.register("sleepdog_to_sit_left");
   @SpecialAnim
   IAnimationSource BODY_CYCLE_SLEEPDOG_IDLE = AnimationsRegistrySeasonal.register("body/body_cycle_sleepdog_idle");
   IAnimationSource FIX_POSE_S64 = AnimationsRegistrySeasonal.register("custom/s64/fix_pose_21_04");
   IAnimationSource BLOW = AnimationsRegistrySeasonal.register("body_cycle_blowing_right");
   IAnimationSource DRAGGING_BY_FEET = AnimationsRegistrySeasonal.register("body_pose_draggingbyfeet");
   IAnimationSource PULL_BACK_AND_THROW = AnimationsRegistrySeasonal.register("hands_endanimated_pullback");
   IAnimationSource HANDS_TO_EYES = AnimationsRegistrySeasonal.register("hands/hands_pose_handstoeyes");
   IAnimationSource NAUSEA_BOTHHANDS = AnimationsRegistrySeasonal.register("body/body_pose_nausea_bothhands");
   IAnimationSource FACE_POSE_JOY = AnimationsRegistrySeasonal.register("face/face_pose_joy");
   IAnimationSource STRONG_HUG_1 = AnimationsRegistrySeasonal.register("hands/hands_pose_stronghug1");
   IAnimationSource STRONG_HUG_2 = AnimationsRegistrySeasonal.register("hands/hands_pose_stronghug2");
   IAnimationSource BODY_LAY_HURT = AnimationsRegistrySeasonal.register("body/body_pose_sidelayhurt_left");
   IAnimationSource EXTEND_RIGHT_HAND = AnimationsRegistrySeasonal.register("placeholder_pose_extendhandover_right");
   IAnimationSource BODY_ENDANIMETED_WAVINGWAND_RIGHT = AnimationsRegistrySeasonal.register("body/body_endanimeted_wavingwand_right");
   IAnimationSource PLACEHOLDER_POSE_HATFALL = AnimationsRegistrySeasonal.register("placeholder_pose_hatfall");
   IAnimationSource PLACEHOLDER_ENDANIMATED_HATFALL = AnimationsRegistrySeasonal.register("placeholder_endanimated_hatfall");
   IAnimationSource HANDS_ON_ELBOW = AnimationsRegistrySeasonal.register("hands_pose_handsonelbow");
   IAnimationSource HANDS_FUMBLING_DOWN = AnimationsRegistrySeasonal.register("hands_cycle_fumblingdown");
   IAnimationSource JODAH_WINGS_CYCLE_FLY = AnimationsRegistrySeasonal.register("jodah_wings_cycle_fly");
   IAnimationSource JODAH_WINGS_POSE_CLOSED = AnimationsRegistrySeasonal.register("jodah_wings_pose_closed");
   IAnimationSource BODY_POSE_LYINGONHANDS = AnimationsRegistrySeasonal.register("body_pose_lyingonhands");
   IAnimationSource BODY_POSE_LYINGONHANDS_WITHOUT_SCALE = AnimationsRegistrySeasonal.register("body_pose_lyingonhands_without");
   IAnimationSource BODY_CYCLE_DANCE_1 = AnimationsRegistrySeasonal.register("body/body_cycle_dance1");
   IAnimationSource BODY_CYCLE_DANCE_2 = AnimationsRegistrySeasonal.register("body/body_cycle_dance2");
   IAnimationSource BODY_CYCLE_DANCE_3 = AnimationsRegistrySeasonal.register("body/body_cycle_dance3");
   IAnimationSource CLAPABOVE = AnimationsRegistrySeasonal.register("hands/hands_endanimated_clapabove");
   IAnimationSource DOOR_KICK = AnimationsRegistrySeasonal.register("body_endanimated_doorknockoff");
   IAnimationSource CLOSE_DOOR_BACK = AnimationsRegistrySeasonal.register("body_endanimated_doorclose");
   IAnimationSource BODY_CYCLE_INJUREDWALK = AnimationsRegistrySeasonal.register("body/body_cycle_injuredwalk");
   IAnimationSource AIR_KISS = AnimationsRegistrySeasonal.register("hands_endanimated_airkiss");
   IAnimationSource PLACEHOLDER_POSE_HOLDSINFRONT = AnimationsRegistrySeasonal.register("placeholder_pose_holdsinfront");
   IAnimationSource PLACEHOLDER_POSE_HOLDSINFRONT_UP = AnimationsRegistrySeasonal.register("placeholder_pose_holdsinfront_up");
   IAnimationSource HANDS_POSE_CONSIDEROBJECTDOWN = AnimationsRegistrySeasonal.register("hands_pose_considerobjectdown_withouthead");
   IAnimationSource HANDS_POSE_HURTSHOULDER = AnimationsRegistrySeasonal.register("hands/hands_pose_hurtshoulder");
   IAnimationSource TABLE_TURN = AnimationsRegistrySeasonal.register("placeholder_pose_tableturn");
   IAnimationSource ROCKSTAR = AnimationsRegistrySeasonal.register("rockstar_sound");
   IAnimationSource HOTWEATHER_CYCLE = AnimationsRegistrySeasonal.register("hotweather_cycle");
   IAnimationSource HUMANOID_LOOK_AROUND_SCARED = AnimationsRegistrySeasonal.register("look_around_scared");
   IAnimationSource HUMANOID_HEAD_LOOK_AROUND = AnimationsRegistrySeasonal.register("look_around");
   IAnimationSource TAKE_HEAD = AnimationsRegistrySeasonal.register("take_head");
   IAnimationSource HANDS_POSE_PRAY = AnimationsRegistrySeasonal.register("hands_pose_pray");
   IAnimationSource SALUTE = AnimationsRegistrySeasonal.register("hands_endanimated_salute");
   IAnimationSource BODY_COMBAT_STAND = AnimationsRegistrySeasonal.register("body/body_cycle_combatstand");
   IAnimationSource RIGHT_HAND_AT_MOUTH = AnimationsRegistrySeasonal.register("hands_pose_handatmouth_right");
   IAnimationSource HUMANOID_WALLPUNCH = AnimationsRegistrySeasonal.register("humanoid/hands_endanimated_wallpunch_right");
   IAnimationSource HUMANOID_SHOCKED = AnimationsRegistrySeasonal.register("humanoid/body_pose_shocked");
   IAnimationSource HUMANOID_SIT = AnimationsRegistrySeasonal.register("humanoid/body_pose_sit");
   IAnimationSource HUMANOID_STAY_TO_SIT = AnimationsRegistrySeasonal.register("humanoid/body_endanimated_staytosit");
   IAnimationSource HUMANOID_SIT_TO_STAY = AnimationsRegistrySeasonal.register("humanoid/body_endanimated_sittostay");
   IAnimationSource HUMANOID_WAVING_HAND_RIGHT = AnimationsRegistrySeasonal.register("humanoid/hend_cycle_wavinghand_right");
   IAnimationSource HUMANOID_WAVING_HAND_LEFT = AnimationsRegistrySeasonal.register("humanoid/hend_cycle_wavinghand_left");
   IAnimationSource HUMANOID_HAND_POSE_WHISPER = AnimationsRegistrySeasonal.register("humanoid/hend_pose_whisper");
   IAnimationSource HUMANOID_SIT_CHAIR = AnimationsRegistrySeasonal.register("humanoid/sit_chair");
   IAnimationSource CHILD_RUN = AnimationsRegistrySeasonal.register("kid_body_cycle_run");
   IAnimationSource JUMP_AND_CLAP = AnimationsRegistrySeasonal.register("body_cycle_jumpclap");
   IAnimationSource HUMANOID_PULLOUTSWORD = AnimationsRegistrySeasonal.register("humanoid/placeholder_pose_pulloutsword");
   IAnimationSource PLAY_GUITAR = AnimationsRegistrySeasonal.register("placeholder_cycle_playgutar");
   IAnimationSource HUMANOID_SNEAK = AnimationsRegistrySeasonal.register("humanoid/body_pose_sneak");
   IAnimationSource HUMANOID_PEEKING_LEFT = AnimationsRegistrySeasonal.register("humanoid/body_pose_peeking", "body_pose_peeking_left");
   IAnimationSource HUMANOID_PEEKING_RIGHT = AnimationsRegistrySeasonal.register("humanoid/body_pose_peeking", "body_pose_peeking_right");
   IAnimationSource HUMANOID_AIRPLANE = AnimationsRegistrySeasonal.register("humanoid/hands_pose_likeairplane");
   IAnimationSource HUMANOID_KEEP_BLOCK = AnimationsRegistrySeasonal.register("humanoid/placeholder_pose_keepblock");
   IAnimationSource HUMANOID_RUN_WITHOUT_HANDS = AnimationsRegistrySeasonal.register("humanoid/body_cycle_run_withouthands");
   IAnimationSource HUMANOID_LETSLIP = AnimationsRegistrySeasonal.register("humanoid/hands_pose_letslip");
   IAnimationSource HUMANOID_CURTSEY = AnimationsRegistrySeasonal.register("humanoid/body_endanimated_curtsey");
   IAnimationSource HUMANOID_HOLDS_OVER = AnimationsRegistrySeasonal.register("humanoid/placeholder_pose_holdsover");
   IAnimationSource HUMANOID_HOLDS_IN_FRONT = AnimationsRegistrySeasonal.register("humanoid/placeholder_pose_holdsinfront");
   IAnimationSource HUMANOID_BYE = AnimationsRegistrySeasonal.register("humanoid/hat_hands_endanimated_bye");
   IAnimationSource EMOTION_PSYCHO = AnimationsRegistrySeasonal.register("emotions/emoc_pose_strangesmile");
   IAnimationSource HUMANOID_HITTING_WITH_HAMMER_RIGHT = AnimationsRegistrySeasonal.register("humanoid/placeholder_cycle_hittingwithhammer_right");
   IAnimationSource HUMANOID_DYSPNEA = AnimationsRegistrySeasonal.register("humanoid/body_cycle_shortness");
   IAnimationSource HUMANOID_HANDS_UP = AnimationsRegistrySeasonal.register("humanoid/hands_pose_throwuphands");
   IAnimationSource HUMANOID_WAVE_FRONT_RIGHT = AnimationsRegistrySeasonal.register("humanoid/hands_endanimated_brushoff_right");
   IAnimationSource HUMANOID_WAVE_FRONT_LEFT = AnimationsRegistrySeasonal.register("humanoid/hands_endanimated_brushoff_left");
   IAnimationSource HUMANOID_SLEEPILY_STRETCH = AnimationsRegistrySeasonal.register("humanoid/arms_endanimated_stretch");
   IAnimationSource HUMANOID_SIT_TO_LAY_SIDED_LEFT = AnimationsRegistrySeasonal.register("humanoid/body_endanimated_sittolay");
   IAnimationSource HUMANOID_SIT_TO_LAY_SIDED_RIGHT = AnimationsRegistrySeasonal.register("humanoid/body_endanimated_sittolay_left");
   IAnimationSource HUMANOID_SIT_TO_LAY_SIDED_BED_LEFT = AnimationsRegistrySeasonal.register("humanoid/body_endanimated_sittolay_bed");
   IAnimationSource HUMANOID_SIT_TO_LAY_SIDED_BED_LEFT_ROTATED = AnimationsRegistrySeasonal.register("humanoid/body_endanimated_sittolay_bed_rotated");
   IAnimationSource HUMANOID_SIT_TO_LAY_SIDED_BED_RIGHT = AnimationsRegistrySeasonal.register("humanoid/body_endanimated_sittolay_bed_right");
   IAnimationSource HUMANOID_POSE_LAY_ON_SIDE_LEFT = AnimationsRegistrySeasonal.register("humanoid/body_pose_lyingonside_left");
   IAnimationSource HUMANOID_POSE_LAY_ON_SIDE_RIGHT = AnimationsRegistrySeasonal.register("humanoid/body_pose_lyingonside_right");
   IAnimationSource HUMANOID_POSE_LAY_ON_SIDE_BED_RIGHT = AnimationsRegistrySeasonal.register("humanoid/body_pose_lyingonside_left_bed");
   IAnimationSource HUMANOID_POSE_LAY_ON_SIDE_BED_RIGHT_ROTATED = AnimationsRegistrySeasonal.register("humanoid/body_pose_lyingonside_left_bed_rotated");
   IAnimationSource HUMANOID_POSE_LAY_ON_SIDE_BED_LEFT = AnimationsRegistrySeasonal.register("humanoid/body_pose_lyingonside_right_bed");
   IAnimationSource HUMANOID_POSE_LAY_BED = AnimationsRegistrySeasonal.register("humanoid/body_pose_lay_bed");
   IAnimationSource HUMANOID_POSE_SIT_LEGS_CROSSED = AnimationsRegistrySeasonal.register("humanoid/body_pose_sitlegscrossed");
   IAnimationSource HUMANOID_POSE_SIT_LEGS_CROSSED_BED = AnimationsRegistrySeasonal.register("humanoid/body_pose_sitlegscrossed_bed");
   IAnimationSource HUMANOID_EXORCISM = AnimationsRegistrySeasonal.register("humanoid/body_pose_exorcism");
   IAnimationSource HUMANOID_SPEAKER = AnimationsRegistrySeasonal.register("humanoid/body_pose_speaker");
   IAnimationSource HUMANOID_TREMBLING = AnimationsRegistrySeasonal.register("humanoid/hands_endanimated_trembling");
   IAnimationSource HUMANOID_SIGH = AnimationsRegistrySeasonal.register("humanoid/body_endanimated_sigh_scale");
   IAnimationSource HUMANOID_SHOOTING = AnimationsRegistrySeasonal.register("humanoid/placeholder_shooting");
   IAnimationSource HUMANOID_SHOOTING_LOOP = AnimationsRegistrySeasonal.register("humanoid/placeholder_cycle_shooting");
   IAnimationSource HUMANOID_FALLING_TO_BACK_FROM_HIT = AnimationsRegistrySeasonal.register("humanoid/body_endanimated_fallingtobackfromhit");
   IAnimationSource EMOTION_TALKING = AnimationsRegistrySeasonal.register("humanoid/talk", "emoc_cycle_talk");
   IAnimationSource HUMANOID_TALKING = AnimationsRegistrySeasonal.register("humanoid/talk", "emoc_cycle_talkmoves");
   IAnimationSource EMOTION_CURIOUS = AnimationsRegistrySeasonal.register("humanoid/head_pose_interest_left", "no_face");
   IAnimationSource EMOTION_CURIOUS_WITH_FACE = AnimationsRegistrySeasonal.register("humanoid/head_pose_interest_left", "with_face");
   IAnimationSource HUMANOID_TAKE_OFF_HAT = AnimationsRegistrySeasonal.register("humanoid/hat_body_endanimated_bow");
   IAnimationSource HUMANOID_ONE_CLAP = AnimationsRegistrySeasonal.register("humanoid/hands_endanimated_clap");
   IAnimationSource HUMANOID_HANDS_POSE_CLAPENDING = AnimationsRegistrySeasonal.register("humanoid/hands_pose_clapending");
   IAnimationSource HUMANOID_ROTATE_HEAD_SIDEWAYS_RIGHT = AnimationsRegistrySeasonal.register("humanoid/head_pose_back", "head_pose_back_right");
   IAnimationSource HUMANOID_ROTATE_HEAD_SIDEWAYS_LEFT = AnimationsRegistrySeasonal.register("humanoid/head_pose_back", "head_pose_back_left");
   IAnimationSource HUMANOID_SLEEP_LEANING_ON_TABLE = AnimationsRegistrySeasonal.register("humanoid/body_pose_sleepleaningontable");
   IAnimationSource HUMANOID_EATING = AnimationsRegistrySeasonal.register("humanoid/hands_cycle_eating");
   IAnimationSource HUMANOID_WIPE_HANDS = AnimationsRegistrySeasonal.register("humanoid/hands_endanimated_wipeshands");
   IAnimationSource HUMANOID_WIPE_CUP = AnimationsRegistrySeasonal.register("humanoid/placeholder_cycle_wipescup");
   IAnimationSource HUMANOID_LAY_ON_WALL = AnimationsRegistrySeasonal.register("humanoid/body_pose_layonwall");
   IAnimationSource HUMANOID_SIT_ON_EDGE = AnimationsRegistrySeasonal.register("humanoid/body_endanimated_sitonedge");
   IAnimationSource SIT_ON_HORSE = AnimationsRegistrySeasonal.register("humanoid/body_pose_sit_on_horse");
   IAnimationSource HUMANOID_BANG_FISTS = AnimationsRegistrySeasonal.register("humanoid/hands_endanimated_fisttofist");
   IAnimationSource HUMANOID_MUSCLE_FLEX = AnimationsRegistrySeasonal.register("humanoid/body_pose_showingmuscles");
   IAnimationSource HUMANOID_OBEISANCE = AnimationsRegistrySeasonal.register("humanoid/body_endanimated_obeisance");
   IAnimationSource HUMANOID_OBEISANCE_HOLD = AnimationsRegistrySeasonal.register("humanoid/body_endanimated_obeisance_hold");
   IAnimationSource HUMANOID_CUT_OFF_HEAD = AnimationsRegistrySeasonal.register("humanoid/head_endanimated_cutoffhead");
   IAnimationSource HUMANOID_CALL_RIGHT = AnimationsRegistrySeasonal.register("humanoid/hands_endanimated_callfor_right");
   IAnimationSource HUMANOID_CALL_LEFT = AnimationsRegistrySeasonal.register("humanoid/hands_endanimated_callfor_left");
   IAnimationSource HUMANOID_CONSIDER_MANICURE = AnimationsRegistrySeasonal.register("humanoid/hand_pose_lookingbackofhand_right");
   IAnimationSource HUMANOID_CONSIDER_HAND = AnimationsRegistrySeasonal.register("humanoid/hands_pose_considerobjectdown");
   IAnimationSource BAG_SEARCH_IN_BAG = AnimationsRegistrySeasonal.register("bag/bag_hands_endanimated_rummageinbag");
   IAnimationSource HUMANOID_POSE_TWO_HANDED = AnimationsRegistrySeasonal.register("humanoid/placeholder_pose_prepareattack");
   IAnimationSource HUMANOID_SHAKE_HAND_LEFT = AnimationsRegistrySeasonal.register("humanoid/hands_endanimated_shakehands_left");
   IAnimationSource HUMANOID_SHAKE_HAND_RIGHT = AnimationsRegistrySeasonal.register("humanoid/hands_endanimated_shakehands_right");
   IAnimationSource HUMANOID_CLIMB = AnimationsRegistrySeasonal.register("humanoid/body_cycle_climb");
   IAnimationSource HUMANOID_THROW_AXE = AnimationsRegistrySeasonal.register("humanoid/placeholder_endanimated_strongattack");
   IAnimationSource HUMANOID_HANDUP_130 = AnimationsRegistrySeasonal.register("humanoid/hands_pose_handup_130deg");
   IAnimationSource JUMP_WITHOUT_ANIMATION = AnimationsRegistrySeasonal.register("body_cycle_statuejump");
   IAnimationSource WALK_WITHOUT_ANIMATION = AnimationsRegistrySeasonal.register("body_cycle_statuewalk");
   IAnimationSource HUMANOID_FALL_ON_BED_RIGHT = AnimationsRegistrySeasonal.register("humanoid/body_endanimated_fallonback_right_bed");
   IAnimationSource HUMANOID_FALL_ON_BED_LEFT = AnimationsRegistrySeasonal.register("humanoid/body_endanimated_fallonback_left_bed");
   IAnimationSource SITTING_HOLDING_HEAD_UPPER = AnimationsRegistrySeasonal.register("body/body_pose_sitting_holding_head", "upper_body");
   IAnimationSource SITTING_HOLDING_HEAD_LOWER = AnimationsRegistrySeasonal.register("body/body_pose_sitting_holding_head", "lower_body");
   SidedAnimationSet HUMANOID_RUB_HAND_RIGHT_NEW = AnimationsRegistrySeasonal.registerSided(
      "hands_endanimated_rubslefthand_right", "hands_endanimated_rubslefthand_left", "hands/rub"
   );
   SidedAnimationSet SIT_SLEEP_NEW = AnimationsRegistrySeasonal.registerSided(
      "body_pose_sit_sleep_new_right", "body_pose_sit_sleep_new_left", "humanoid/body_pose_sit_sleep_new"
   );
   SidedAnimationSet HAND_POKE = AnimationsRegistrySeasonal.registerSided("hands_cycle_touch_right", "hands_cycle_touch_left", "hands_cycle_touch");
   SidedAnimationSet HUMANOID_NAUSEA = AnimationsRegistrySeasonal.registerSided("body_pose_nausea_right", "body_pose_nausea_left", "humanoid/body_pose_nausea");
   SidedAnimationSet HUMANOID_WAVE_WAND = AnimationsRegistrySeasonal.registerSided(
      "hands_endanimated_wavewand_right", "hands_endanimated_wavewand_left", "hands_endanimated_wavewand"
   );
   SidedAnimationSet HANDS_POSE_HAND = AnimationsRegistrySeasonal.registerSidedSeparate("hands_pose_hand_right", "hands_pose_hand_left");
   SidedAnimationSet HANDS_ENDANIMATED_THROW = AnimationsRegistrySeasonal.registerSidedSeparate("hands_endanimated_throw_right", "hands_endanimated_throw_left");
   SidedAnimationSet EMOTION_CLOSED_EYE = AnimationsRegistrySeasonal.registerSided("emoc_pose_closeeye_right", "emoc_pose_closeeye_left", "emoc_pose_closeeye");
   SidedAnimationSet HUMANOID_SLAP_FACE = AnimationsRegistrySeasonal.registerSidedSeparate(
      "humanoid/hands_endanimated_slapface_right", "humanoid/hands_endanimated_slapface_left"
   );
   SidedAnimationSet HUMANOID_HOLD_CHEEK = AnimationsRegistrySeasonal.registerSidedSeparate(
      "humanoid/hands_pose_holdcheek_right", "humanoid/hands_pose_holdcheek_left"
   );
   SidedAnimationSet EMOTION_WINKS = AnimationsRegistrySeasonal.registerSided(
      "head_endanimated_fludingeyes_right", "head_endanimated_fludingeyes_left", "emotions/head_endanimated_fluding_eyes"
   );
   SidedAnimationSet PLACEHOLDER_POSE_HOLDINGTRAY = AnimationsRegistrySeasonal.registerSidedSeparate(
      "placeholder/placeholder_pose_holdingtray_right", "placeholder/placeholder_pose_holdingtray_left"
   );
   SidedAnimationSet HUMANOID_LOOK_AT_HAND = AnimationsRegistrySeasonal.registerSided(
      "hands_pose_lookathand_right", "hands_pose_lookathand_left", "humanoid/hands_pose_lookathand"
   );
   SidedAnimationSet BODY_POSE_LYING_ON_SIDE = AnimationsRegistrySeasonal.registerSidedSeparate(
      "body/body_pose_lyingonside_right", "body/body_pose_lyingonside_left"
   );
   SidedAnimationSet PLACEHOLDER_POSE_SWORDFORWARD = AnimationsRegistrySeasonal.registerSided("right", "left", "placeholder/placeholder_pose_swordforward");
   SidedAnimationSet EMOTION_PRIDE_WITH_HANDS = AnimationsRegistrySeasonal.registerSided(
      "emoc_pose_pride_right", "emoc_pose_pride_left", "emotions/emoc_pose_pride"
   );
   SidedAnimationSet HUMANOID_LEAN_ON_TABLE = AnimationsRegistrySeasonal.registerSided(
      "hands_pose_leanhandontable_right", "hands_pose_leanhandontable_left", "hands/hands_pose_leanhandontable"
   );
   SidedAnimationSet HANDS_POSE_SPREADHANDS_SEPARATE = AnimationsRegistrySeasonal.registerSided("right", "left", "humanoid/hands_pose_spreadhands_separate");
   SidedAnimationSet HANDS_POSE_TAKE_HAND_FRONT = AnimationsRegistrySeasonal.registerSided(
      "hands_pose_takehandfront_right", "hands_pose_takehandfront_left", "hands/hands_pose_takehandfront"
   );
   SidedAnimationSet LEGS_POSE_LEG = AnimationsRegistrySeasonal.registerSided("legs_pose_leg_right", "legs_pose_leg_left", "legs_pose_leg");
   SidedAnimationSet HUMANOID_DRAW_ON_WALL = AnimationsRegistrySeasonal.registerSided(
      "draw_on_wall_right", "draw_on_wall_left", "humanoid/hands_cycle_drawonwall"
   );
}
