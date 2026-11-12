package com.storyteam.storytelling.client.animations;

import com.storyteam.storytelling.api.Since;
import com.storyteam.storytelling.api.animations.SpecialAnim;
import com.storyteam.storytelling.client.AnimationsRegistry;
import com.storyteam.storytelling.client.AnimationsRegistrySeasonal;
import com.storyteam.storytelling.client.core.SidedAnimationSet;
import org.zeith.hammeranims.api.animation.IAnimationSource;

@Since(Since.Season.TN)
public interface AnimationsST7 {
   @SpecialAnim("RECYCLER")
   IAnimationSource RECYCLER_OPEN = AnimationsRegistrySeasonal.register("recycler/recycler.open");
   @SpecialAnim("RECYCLER")
   IAnimationSource RECYCLER_WORKING = AnimationsRegistrySeasonal.register("recycler/recycler.working");
   @SpecialAnim("BUNKER_DOOR")
   IAnimationSource BUNKER_DOOR_OPEN = AnimationsRegistrySeasonal.register("bunker_door/bunker_door_open");
   @SpecialAnim("BUNKER_DOOR")
   IAnimationSource BUNKER_DOOR_CLOSE = AnimationsRegistrySeasonal.register("bunker_door/bunker_door_close");
   @SpecialAnim("BUNKER_FLOOR_DOOR")
   IAnimationSource BUNKER_FLOOR_DOOR_OPEN = AnimationsRegistrySeasonal.register("bunker_door/bunker_floor_door_open");
   IAnimationSource TOOKOFFHAT_HOLDHAT = AnimationsRegistrySeasonal.register("humanoid/body_endanimated_tookoffhat_holdhat");
   IAnimationSource BOOKS_PUT_TABLE = AnimationsRegistrySeasonal.register("humanoid/books_put_table");
   IAnimationSource READING_BOOKS_CUTSCENE_CYCLE = AnimationsRegistrySeasonal.register("cutscene/reading_books_cutscene_cycle");
   IAnimationSource READING_BOOKS_CUTSCENE_ENDING = AnimationsRegistrySeasonal.register("cutscene/reading_books_cutscene_ending");
   IAnimationSource READING_BOOKS_CUTSCENE_ENDING_CYCLE = AnimationsRegistrySeasonal.register("cutscene/reading_books_cutscene_ending_cycle");
   IAnimationSource HAZMAT_TAKE_OFF = AnimationsRegistrySeasonal.register("humanoid/hazmat_take_off");
   IAnimationSource CAREFUL_WALK = AnimationsRegistrySeasonal.register("humanoid/very_careful_walk_cycle");
   IAnimationSource CLONE_FOLDER_OPENING = AnimationsRegistrySeasonal.register("humanoid/clone_folder_opening");
   IAnimationSource CLONE_FOLDER_OPEN_POSE = AnimationsRegistrySeasonal.register("humanoid/clone_folder_open_pose");
   IAnimationSource READING_CYCLE = AnimationsRegistrySeasonal.register("humanoid/reading_cycle");
   IAnimationSource CLONE_FOLDER_FLIP = AnimationsRegistrySeasonal.register("humanoid/clone_folder_flip");
   IAnimationSource CLONE_RAGE_FOLDER = AnimationsRegistrySeasonal.register("humanoid/clone_rage_folder");
   IAnimationSource CLONE_FOLDER_POINT = AnimationsRegistrySeasonal.register("humanoid/clone_folder_point");
   IAnimationSource HEART_ATTACK = AnimationsRegistrySeasonal.register("humanoid/heart_attack");
   IAnimationSource HEART_ATTACK_BREATH_CYCLE = AnimationsRegistrySeasonal.register("humanoid/heart_attack_breath_cycle");
   IAnimationSource HEART_ATTACK_FALL = AnimationsRegistrySeasonal.register("humanoid/heart_attack_fall");
   IAnimationSource CUP_HEATING_POSE = AnimationsRegistrySeasonal.register("humanoid/cup_heating_pose");
   IAnimationSource SANDSTORM_WALK2 = AnimationsRegistrySeasonal.register("humanoid/sandstorm_walk2");
   IAnimationSource HUG_PLUSH_POSE = AnimationsRegistrySeasonal.register("humanoid/hug_plush_pose");
   IAnimationSource CAR_START = AnimationsRegistrySeasonal.register("car/car_start");
   IAnimationSource CAR_STOP = AnimationsRegistrySeasonal.register("car/car_stop");
   IAnimationSource CAR_WHEELS_VARIABLE = AnimationsRegistrySeasonal.register("car/car_wheels_variable");
   IAnimationSource CAR_IDLE = AnimationsRegistrySeasonal.register("car/ambient_car_idle");
   IAnimationSource FALL_LEAN_TO_WALL = AnimationsRegistrySeasonal.register("humanoid/fall_lean_to_wall");
   IAnimationSource GLASSES_LEANED_TO_WALL_FALL = AnimationsRegistrySeasonal.register("humanoid/glasses_leaned_to_wall_fall");
   IAnimationSource GLASSES_FALL_ENDANIMATED = AnimationsRegistrySeasonal.register("humanoid/glasses_fall_endanimated");
   IAnimationSource EMOTION_LOLOLOSHKA_PSYCHO_LAUGH = AnimationsRegistrySeasonal.register("humanoid/emotion_lololoshka_psycho_laugh");
   IAnimationSource EMOTION_LOLOLOSHKA_SHOCK = AnimationsRegistrySeasonal.register("humanoid/emotion_lololoshka_shock");
   IAnimationSource LEAN_WALL_SLIDE_TO_SIT = AnimationsRegistrySeasonal.register("humanoid/lean_wall_slide_to_sit");
   IAnimationSource LEAN_WALL_SIT_TO_CRAWL = AnimationsRegistrySeasonal.register("humanoid/wall_lean_sit_to_crawl");
   IAnimationSource PRIMAL_AGRESSION_CYCLE = AnimationsRegistrySeasonal.register("humanoid/primal_agression_cycle");
   IAnimationSource ANGRY_WALK_CYCLE = AnimationsRegistrySeasonal.register("humanoid/angry_walk_cycle");
   IAnimationSource HOSTAGE_NEW = AnimationsRegistrySeasonal.register("humanoid/hostage_new");
   IAnimationSource HOSTAGED_NEW = AnimationsRegistrySeasonal.register("humanoid/hostaged_new");
   IAnimationSource POWERFUL_LEG_KICK = AnimationsRegistrySeasonal.register("humanoid/powerful_leg_kick");
   IAnimationSource PISTOL_FAST_PREPARE = AnimationsRegistrySeasonal.register("humanoid/pistol_fast_prepare");
   IAnimationSource PROP_HEAD_HANDS = AnimationsRegistrySeasonal.register("humanoid/prop_head_hands");
   IAnimationSource EMOTION_REALIZATION = AnimationsRegistrySeasonal.register("humanoid/emotion_realization");
   IAnimationSource SIT_ARM_UP_JUMPING = AnimationsRegistrySeasonal.register("humanoid/sit_arm_up_jumping");
   IAnimationSource THINKING_ARGUING = AnimationsRegistrySeasonal.register("humanoid/thinking_arguing");
   IAnimationSource SCRIMER_SHOCKED = AnimationsRegistrySeasonal.register("humanoid/scrimer_shocked");
   IAnimationSource LOLOLOSHKA_CLONES_GLITCH = AnimationsRegistrySeasonal.register("humanoid/lololoshka_clones_glitch");
   IAnimationSource TENTACLE_HANDS_POSE_CROSSEDARMS = AnimationsRegistrySeasonal.register("humanoid/tentacle_hands_pose_crossedarms");
   IAnimationSource TENTACLE_HANDS_CYCLE_CLAPPING = AnimationsRegistrySeasonal.register("humanoid/tentacle_hands_cycle_clapping");
   IAnimationSource POKE_POINTER_ENDANIMATED = AnimationsRegistrySeasonal.register("humanoid/poke_pointer_endanimated");
   IAnimationSource UP_GLASSES_WHILE_LOOKING_TO_SIDE = AnimationsRegistrySeasonal.register("humanoid/up_glasses_while_looking_to_side");
   IAnimationSource SKRIMER_SUPERIORITY = AnimationsRegistrySeasonal.register("humanoid/skrimer_superiority");
   @SpecialAnim("SCREAMER_WITH_CANNON")
   IAnimationSource YELLER_DRINK_COCKTAIL_CYCLE = AnimationsRegistrySeasonal.register("yeller_drink_cocktail_cycle");
   @SpecialAnim("SCREAMER_WITH_CANNON")
   IAnimationSource YELLER_DRINK_TO_HOLD_COCKTAIL = AnimationsRegistrySeasonal.register("yeller_drink_to_hold_cocktail");
   @SpecialAnim("SCREAMER_WITH_CANNON")
   IAnimationSource YELLER_HOLD_COCKTAIL_POSE = AnimationsRegistrySeasonal.register("yeller_hold_cocktail_pose");
   @SpecialAnim("SCREAMER_WITH_CANNON")
   SidedAnimationSet DRINKING_LAYER_HAND = AnimationsRegistrySeasonal.registerSided(
      "drinking_layer_hand_right", "drinking_layer_hand_left", "drinking_layer_hand"
   );
   IAnimationSource HOLD_BOOK_UP = AnimationsRegistrySeasonal.register("humanoid/hold_book_up");
   IAnimationSource FAST_HAZMAT_TAKE_OFF = AnimationsRegistrySeasonal.register("humanoid/fast_hazmat_take_off");
   IAnimationSource FAST_HELMET_TAKE_OFF = AnimationsRegistrySeasonal.register("humanoid/fast_helmet_take_off");
   IAnimationSource CARDS_THROW = AnimationsRegistrySeasonal.register("humanoid/cards_throw");
   IAnimationSource SLIME_LOLOLOSHKA_HAZMAT_CUTSCENE = AnimationsRegistrySeasonal.register("humanoid/slime_lololoshka_hazmat_cutscene");
   IAnimationSource FOOT_TAP_CYCLE = AnimationsRegistrySeasonal.register("humanoid/foot_tap_cycle");
   IAnimationSource FOOT_TAP_RIGHT_CYCLE = AnimationsRegistrySeasonal.register("humanoid/foot_tap_right_cycle");
   IAnimationSource CHECKING_AROUND_LOOK_OTHER = AnimationsRegistrySeasonal.register("humanoid/checking_around_look_other");
   IAnimationSource FATHER_ENERGY_ATTACK = AnimationsRegistrySeasonal.register("humanoid/father_energy_attack");
   IAnimationSource PLAYFULNESS_LAUGH_ENDANIMATED = AnimationsRegistrySeasonal.register("humanoid/playfulness_laugh_endanimated");
   IAnimationSource HOLD_HAT_POSE = AnimationsRegistrySeasonal.register("humanoid/hold_hat_pose");
   IAnimationSource DISGUST_POSE = AnimationsRegistrySeasonal.register("humanoid/disgust_pose");
   IAnimationSource STOMACH_GETUP_FROM_FALL_FORWARD = AnimationsRegistrySeasonal.register("humanoid/stomach_getup_from_fall_forward");
   IAnimationSource THRONE_SIT_NO_ARMS_LEFT = AnimationsRegistrySeasonal.register("throne_sit_no_arms_left");
   IAnimationSource FIST_TO_CHEST_LEFT = AnimationsRegistrySeasonal.register("humanoid/fist_to_chest_left");
   IAnimationSource EMOTION_DISGUST_LEFT = AnimationsRegistrySeasonal.register("humanoid/disgust_left");
   IAnimationSource POKE_LEFT = AnimationsRegistrySeasonal.register("humanoid/poke_endanimated_left");
   IAnimationSource ARM_TO_HEART_LEFT = AnimationsRegistrySeasonal.register("humanoid/arm_to_heart_left");
   IAnimationSource POKING_IN_CHEST_LEFT = AnimationsRegistrySeasonal.register("humanoid/poking_in_chest_left");
   IAnimationSource FATHER_HEART_ATTACK = AnimationsRegistrySeasonal.register("humanoid/father_heart_attack");
   IAnimationSource TAKE_FROM_HANDS = AnimationsRegistrySeasonal.register("humanoid/take_from_hands");
   IAnimationSource HUMANOID_WAVE_HAND_RIGHT = AnimationsRegistrySeasonal.register("humanoid/hands_cycle_waving_right");
   IAnimationSource SPITTING_FORWARD = AnimationsRegistrySeasonal.register("humanoid/spitting_forward");
   IAnimationSource PULL_UNDER_WATER = AnimationsRegistrySeasonal.register("humanoid/pull_under_water");
   IAnimationSource SWIM_TO_HOLD = AnimationsRegistrySeasonal.register("humanoid/swim_to_hold");
   IAnimationSource EMOTION_QUESTION_LEFT = AnimationsRegistrySeasonal.register("humanoid/emoc_pose_question_left");
   IAnimationSource STRAIGHT_SIT_ARMSBACK_POSE_NEW = AnimationsRegistrySeasonal.register("humanoid/straight_sit_armsback_pose_new");
   IAnimationSource LOTUS_ONLY_HANDS = AnimationsRegistrySeasonal.register("humanoid/lotus_only_hands");
   IAnimationSource HANDS_POINT_UP_LEFT = AnimationsRegistrySeasonal.register("humanoid/hands_point_up_left");
   IAnimationSource JDH_PAIR_DANCE = AnimationsRegistrySeasonal.register("humanoid/jdh_pair_dance");
   IAnimationSource LOLO_PAIR_DANCE = AnimationsRegistrySeasonal.register("humanoid/lolo_pair_dance");
   IAnimationSource JDH_PAIR_DANCE_CYCLE = AnimationsRegistrySeasonal.register("humanoid/jdh_pair_dance_cycle");
   IAnimationSource LOLO_PAIR_DANCE_CYCLE = AnimationsRegistrySeasonal.register("humanoid/lolo_pair_dance_cycle");
   IAnimationSource PAIR_DANCE_TO_STAND = AnimationsRegistrySeasonal.register("humanoid/pair_dance_to_stand");
   IAnimationSource NEVER_SHOCKWVE_FALL_BACK = AnimationsRegistrySeasonal.register("crow_shockwave_fall_back");
   SidedAnimationSet MAGIC_BALL_THROW = AnimationsRegistrySeasonal.registerSided("magic_ball_throw_right", "magic_ball_throw_left", "humanoid/magic_ball_throw");
   SidedAnimationSet MAGIC_THROW_FORWARD = AnimationsRegistrySeasonal.registerSided(
      "magic_throw_forward_right", "magic_throw_forward_left", "humanoid/magic_throw_forward"
   );
   SidedAnimationSet RAISE_HAND_MAGIC_BALL = AnimationsRegistrySeasonal.registerSided(
      "raise_hand_magic_ball_right", "raise_hand_magic_ball_left", "humanoid/raise_hand_magic_ball"
   );
   SidedAnimationSet WORLDER_MAGIC_CAST_FORWARD = AnimationsRegistrySeasonal.registerSided(
      "worlder_magic_cast_forward_right", "worlder_magic_cast_forward_left", "humanoid/worlder_magic_cast_forward"
   );
   IAnimationSource SIT_GROUND_LEG_ON_LEG = AnimationsRegistrySeasonal.register("humanoid/sit_ground_leg_on_leg");
   SidedAnimationSet SIT_GROUND_LEG_ON_LEG_NOHAND = AnimationsRegistrySeasonal.registerSided(
      "sit_ground_leg_on_leg_nohand_right", "sit_ground_leg_on_leg_nohand_left", "humanoid/sit_ground_leg_on_leg_nohand"
   );
   IAnimationSource JDH_JUMP_OUT = AnimationsRegistrySeasonal.register("humanoid/jdh_jump_out");
   IAnimationSource ROLL_LAY_TO_ON_STOMACH = AnimationsRegistrySeasonal.register("humanoid/roll_lay_to_on_stomach");
   IAnimationSource LOTUS_POSE_COVER_WITH_HANDS = AnimationsRegistrySeasonal.register("humanoid/lotus_pose_cover_with_hands");
   SidedAnimationSet SIT_GROUND_LEG_ON_LEG_CALL_TO_SIT = AnimationsRegistrySeasonal.registerSided(
      "sit_ground_leg_on_leg_call_to_sit_right", "sit_ground_leg_on_leg_call_to_sit_left", "humanoid/sit_ground_leg_on_leg_call_to_sit"
   );
   IAnimationSource SIT_LEAN_ON_GROUND = AnimationsRegistrySeasonal.register("humanoid/sit_lean_on_ground");
   IAnimationSource SIT_LEAN_ON_GROUND_STAND_UP_180_DEGREE = AnimationsRegistrySeasonal.register("humanoid/sit_lean_on_ground_stand_up_180_degree");
   IAnimationSource LAY_TO_SIT_ON_GROUND = AnimationsRegistrySeasonal.register("humanoid/lay_to_sit_on_ground");
   IAnimationSource LAY_TO_SIT_ON_GROUND_WITH_STRAIGHT_LEGS = AnimationsRegistrySeasonal.register("humanoid/lay_to_sit_on_ground_with_straight_legs");
   SidedAnimationSet SHIELDING_FROM_MAGIC_BLOW_WITH_HAND = AnimationsRegistrySeasonal.registerSided(
      "shielding_from_magic_blow_with_hand_right", "shielding_from_magic_blow_with_hand_left", "humanoid/shielding_from_magic_blow_with_hand"
   );
   IAnimationSource BACK_BOUNCE_FROM_MAGIC_BLOW = AnimationsRegistrySeasonal.register("humanoid/back_bounce_from_magic_blow");
   IAnimationSource GET_UP_AFTER_MAGIC_BLOW = AnimationsRegistrySeasonal.register("humanoid/get_up_after_magic_blow");
   IAnimationSource KNOCK_DOWN_FROM_RUN = AnimationsRegistrySeasonal.register("humanoid/knock_down_from_run");
   IAnimationSource BEAT_UP_SITTING = AnimationsRegistrySeasonal.register("humanoid/beat_up_sitting");
   IAnimationSource BEAT_UP_SITTING_CYCLE = AnimationsRegistrySeasonal.register("humanoid/beat_up_sitting_cycle");
   IAnimationSource THROWN_AWAY_FROM_BEATING = AnimationsRegistrySeasonal.register("humanoid/thrown_away_from_beating");
   IAnimationSource KNOCKED_DOWN = AnimationsRegistrySeasonal.register("humanoid/knocked_down");
   IAnimationSource KNOCKED_DOWN_POSE = AnimationsRegistrySeasonal.register("humanoid/knocked_down_pose");
   IAnimationSource BEATEN_UP_DEFENDING = AnimationsRegistrySeasonal.register("humanoid/beaten_up_defending");
   IAnimationSource BEATEN_UP_DEFENDING_CYCLE = AnimationsRegistrySeasonal.register("humanoid/beaten_up_defending_cycle");
   IAnimationSource THROW_OFF_FROM_BEATEN_UP = AnimationsRegistrySeasonal.register("humanoid/throw_off_from_beaten_up");
   IAnimationSource INSPECTING_AROUND = AnimationsRegistrySeasonal.register("humanoid/inspecting_around");
   IAnimationSource WORLDER_FALL_FROM_ATTACK_STAND = AnimationsRegistrySeasonal.register("humanoid/worlder_fall_from_attack_stand");
   IAnimationSource WORLDER_FALL_FROM_ATTACK = AnimationsRegistrySeasonal.register("humanoid/worlder_fall_from_attack");
   IAnimationSource WORLDER_FALL_FROM_ATTACK_GETUP = AnimationsRegistrySeasonal.register("humanoid/worlder_fall_from_attack_getup");
   IAnimationSource WORLDER_DEFEND = AnimationsRegistrySeasonal.register("humanoid/worlder_sword_cutscene_defend");
   IAnimationSource LO_SWORD_ATTACK = AnimationsRegistrySeasonal.register("humanoid/worlder_sword_cutscene_attack");
   IAnimationSource TRY_TO_GET_UP_FALL_HOLDING = AnimationsRegistrySeasonal.register("humanoid/try_to_get_up_fall_holding");
   IAnimationSource BIG_FIST_PUNCH = AnimationsRegistrySeasonal.register("humanoid/big_fist_punch");
   IAnimationSource FALL_FROM_PUNCH_POSE = AnimationsRegistrySeasonal.register("humanoid/fall_from_punch_pose");
   IAnimationSource FALL_ENTANGLED_IN_COCOON = AnimationsRegistrySeasonal.register("humanoid/fall_entangled_in_cocoon");
   IAnimationSource TRYING_GETOUT_COCOON = AnimationsRegistrySeasonal.register("humanoid/trying_getout_cocoon");
   IAnimationSource GETOUT_COCOON = AnimationsRegistrySeasonal.register("humanoid/getout_cocoon");
   IAnimationSource JDH_ATTACK_DAGGER = AnimationsRegistrySeasonal.register("humanoid/spark_jdh_dagger");
   IAnimationSource EMOTION_SMIRK_LEFT = AnimationsRegistrySeasonal.register("humanoid/emotion_snide_smirk_left");
   IAnimationSource EMOTION_PRIDE_NEW_LEFT = AnimationsRegistrySeasonal.register("humanoid/emotion_pride_new_left");
   IAnimationSource LEFT_HAND_AT_MOUTH = AnimationsRegistrySeasonal.register("humanoid/hands_pose_handatmouth_left");
   IAnimationSource BODY_PONDER_LEFT = AnimationsRegistrySeasonal.register("humanoid/body_pose_ponder_left");
   SidedAnimationSet HAND_POKE_SIDE = AnimationsRegistrySeasonal.registerSided(
      "hands_cycle_touch_side_right", "hands_cycle_touch_side_left", "humanoid/hands_cycle_touch_side"
   );
   @SpecialAnim("WORLDER_AND_LO_CLASH_CUTSCENE")
   IAnimationSource WORLDER_AND_LO_CLASH = AnimationsRegistrySeasonal.register("worlder_and_lo_clash");
   @SpecialAnim("ORGANIZATION_LOBSTER")
   IAnimationSource LOBSTER_EYES_CLOSED = AnimationsRegistrySeasonal.register("lobster_eyes_closed");
   @SpecialAnim("ORGANIZATION_LOBSTER")
   IAnimationSource LOBSTER_BLINK = AnimationsRegistrySeasonal.register("lobster_blink");
   @SpecialAnim("WORLDER_LO")
   SidedAnimationSet WORLDER_LO_MASK_HOLD = AnimationsRegistrySeasonal.registerSided(
      "worlder_lo_mask_hold_right", "worlder_lo_mask_hold_left", "humanoid/worlder_lo_mask_hold"
   );
   @SpecialAnim("WORLDER_LO")
   IAnimationSource TOOK_OFF_MASK_WORLDER = AnimationsRegistrySeasonal.register("humanoid/took_off_mask_worlder");
   @SpecialAnim("WORLDER_LO")
   IAnimationSource TOOK_OFF_MASK_TO_REMOVE_MASK = AnimationsRegistrySeasonal.register("humanoid/took_off_mask_to_remove_mask");
   @SpecialAnim("WORLDER_LO_SPEARS")
   IAnimationSource WORLDER_SEND_SPEARS = AnimationsRegistrySeasonal.register("humanoid/worlder_send_spears");
   @SpecialAnim("WORLDER_LO_SPEARS")
   IAnimationSource WORLDER_SPEARS_CAST = AnimationsRegistrySeasonal.register("humanoid/worlder_spears_cast");
   @SpecialAnim("WORLDER_LO_SPEARS")
   IAnimationSource WORLDER_SPEARS_SPIN_CYCLE = AnimationsRegistrySeasonal.register("humanoid/worlder_spears_spin_cycle");
   @SpecialAnim("SHANGRIN")
   IAnimationSource SHANGRIN_PHASE_1_STUN1 = AnimationsRegistrySeasonal.register("shangrin_phase1/shangrin_phase1_stun1");
   @SpecialAnim("SHANGRIN")
   IAnimationSource SHANGRIN_PHASE_1_STUN2 = AnimationsRegistrySeasonal.register("shangrin_phase1/shangrin_phase1_stun2");
   @SpecialAnim("SHANGRIN")
   IAnimationSource SHANGRIN_PHASE_1_IDLE = AnimationsRegistrySeasonal.register("shangrin_phase1/shangrin_phase1_fight_idle");
   @SpecialAnim("SHANGRIN")
   IAnimationSource SHANGRIN_PHASE_1_ATTACK1 = AnimationsRegistrySeasonal.register("shangrin_phase1/shangrin_phase1_attack1");
   @SpecialAnim("SHANGRIN")
   IAnimationSource SHANGRIN_PHASE_1_ATTACK2 = AnimationsRegistrySeasonal.register("shangrin_phase1/shangrin_phase1_attack2");
   @SpecialAnim("SHANGRIN")
   IAnimationSource SHANGRIN_PHASE_1_ATTACK3 = AnimationsRegistrySeasonal.register("shangrin_phase1/shangrin_phase1_attack3");
   @SpecialAnim("SHANGRIN")
   IAnimationSource SHANGRIN_PHASE_1_ATTACK4 = AnimationsRegistrySeasonal.register("shangrin_phase1/shangrin_phase1_attack4");
   @SpecialAnim("SHANGRIN")
   IAnimationSource SHANGRIN_PHASE_1_ATTACK5 = AnimationsRegistrySeasonal.register("shangrin_phase1/shangrin_phase1_attack5");
   @SpecialAnim("SHANGRIN")
   IAnimationSource SHANGRIN_SWORD_ATTACK = AnimationsRegistrySeasonal.register("shangrin_phase1/shangrin_sword_attack");
   @SpecialAnim("SHANGRIN_PHASE_2")
   IAnimationSource SHANGRIN_PHASE_2_ATTACK1 = AnimationsRegistrySeasonal.register("shangrin_phase2/shangrin_phase2_attack1");
   @SpecialAnim("SHANGRIN_PHASE_2")
   IAnimationSource SHANGRIN_PHASE_2_ATTACK2 = AnimationsRegistrySeasonal.register("shangrin_phase2/shangrin_phase2_attack2");
   @SpecialAnim("SHANGRIN_PHASE_2")
   IAnimationSource SHANGRIN_PHASE_2_ATTACK3 = AnimationsRegistrySeasonal.register("shangrin_phase2/shangrin_phase2_attack3");
   @SpecialAnim("SHANGRIN_PHASE_2")
   IAnimationSource SHANGRIN_PHASE_2_ATTACK4 = AnimationsRegistrySeasonal.register("shangrin_phase2/shangrin_phase2_attack4");
   @SpecialAnim("SHANGRIN_PHASE_2")
   IAnimationSource SHANGRIN_PHASE_2_ATTACK5 = AnimationsRegistrySeasonal.register("shangrin_phase2/shangrin_phase2_attack5");
   @SpecialAnim("SHANGRIN_PHASE_2")
   IAnimationSource SHANGRIN_PHASE_2_ATTACK6 = AnimationsRegistrySeasonal.register("shangrin_phase2/shangrin_phase2_attack6");
   @SpecialAnim("SHANGRIN_PHASE_2")
   SidedAnimationSet SHANGRIN_PHASE_2_ATTACK7 = AnimationsRegistrySeasonal.registerSided(
      "shangrin_phase2_attack7_right", "shangrin_phase2_attack7_left", "shangrin_phase2/shangrin_phase2_attack7"
   );
   @SpecialAnim("SHANGRIN_PHASE_2")
   IAnimationSource SHANGRIN_PHASE_2_IDLE = AnimationsRegistrySeasonal.register("shangrin_phase2/shangrin_phase2_fight_idle");
   @SpecialAnim("SHANGRIN_PHASE_2")
   IAnimationSource SHANGRIN_PHASE_2_WALK = AnimationsRegistrySeasonal.register("shangrin_phase2/shangrin_phase2_walk");
   @SpecialAnim("SHANGRIN_PHASE_2")
   IAnimationSource SHANGRIN_PHASE_2_RUN = AnimationsRegistrySeasonal.register("shangrin_phase2/shangrin_phase2_run");
   @SpecialAnim("SHANGRIN_PHASE_2_CUTSCENE")
   IAnimationSource SHANGRIN_PHASE_2_ANIMATION_CUTSCENE = AnimationsRegistrySeasonal.register("shangrin_phase2/shangrin_phase_2_animation_cutscene");
   @SpecialAnim("REPO")
   IAnimationSource REPO_TALK_CYCLE = AnimationsRegistrySeasonal.register("repo_talk_cycle");
   @SpecialAnim("PATATE")
   IAnimationSource PATATE_SEARCH_IN_BAG_RIGHT = AnimationsRegistrySeasonal.register("patate/patate_rummageinbag_right_endanimated");
   @SpecialAnim("PATATE")
   IAnimationSource PATATE_TABLET_TAP = AnimationsRegistrySeasonal.register("patate/patate_tablet_tap");
   @SpecialAnim("DUSTY")
   IAnimationSource SINGULARITY_IDLE = AnimationsRegistrySeasonal.register("dusty/singularity_idle");
   @SpecialAnim("S13_BOOK_CUTSCENE")
   IAnimationSource S13_CUTSCENE_BOOK = AnimationsRegistrySeasonal.register("cutscene/pedestal_cutscene_animation");
   @SpecialAnim
   IAnimationSource DRONE_IDLE = AnimationsRegistrySeasonal.register("drone_idle_blades_on");
   @SpecialAnim
   IAnimationSource DRONE_FORWARD = AnimationsRegistrySeasonal.register("drone_forward");
   @SpecialAnim
   IAnimationSource DRONE_GROUND_FALL = AnimationsRegistrySeasonal.register("mob/drone_ground_fall");
   @SpecialAnim
   IAnimationSource DRONE_SHOT_DAMAGE = AnimationsRegistrySeasonal.register("mob/drone_shot_damaged");
   IAnimationSource STICK_TO_WALL = AnimationsRegistrySeasonal.register("humanoid/stick_lean_to_wall");
   IAnimationSource EGG_INSPECT = AnimationsRegistrySeasonal.register("humanoid/egg_inspect");
   @SpecialAnim
   IAnimationSource SLIME_IDLE = AnimationsRegistrySeasonal.register("mob/slime_idle");
   @SpecialAnim
   IAnimationSource SLIME_JUMP = AnimationsRegistrySeasonal.register("mob/slime_jump");
   IAnimationSource BOX_THROW_DRONE_FULLKNEES_ENDANIMATED = AnimationsRegistrySeasonal.register("humanoid/box_throw_drone_fullknees_endanimated");
   IAnimationSource DECONSTRUCT_DRONE_FULLKNEES_ENDANIMATED = AnimationsRegistrySeasonal.register("humanoid/deconstruct_drone_fullknees_endanimated");
   IAnimationSource INSPECT_DRONE_FULLKNEES_ENDANIMATED = AnimationsRegistrySeasonal.register("humanoid/inspect_drone_fullknees_endanimated");
   IAnimationSource TAKE_DRONE_FULLKNEES_ENDANIMATED = AnimationsRegistrySeasonal.register("humanoid/take_drone_fullknees_endanimated");
   IAnimationSource HOLD_INSPECT_DRONE_FULLKNEES_CYCLE = AnimationsRegistrySeasonal.register("humanoid/hold_inspect_drone_fullknees_cycle");
   @SpecialAnim
   IAnimationSource EMOC_RAYAPRIME_BASE = AnimationsRegistrySeasonal.register("rayaprime/emoc_rayaprime_base");
   @SpecialAnim
   IAnimationSource EMOC_RAYAPRIME_QUESTION = AnimationsRegistrySeasonal.register("rayaprime/emoc_rayaprime_question");
   @SpecialAnim
   IAnimationSource SPINNING_RAYAPRIME_CYCLE = AnimationsRegistrySeasonal.register("rayaprime/spinning_rayaprime_cycle");
   @SpecialAnim
   IAnimationSource RAYA_THROW_ENDANIMATED = AnimationsRegistrySeasonal.register("rayaprime/raya_throw_endanimated");
   @SpecialAnim
   IAnimationSource BASE_RAYAPRIME_CYCLE = AnimationsRegistrySeasonal.register("rayaprime/base_rayaprime_cycle");
   @SpecialAnim
   IAnimationSource PARROT_DANCE = AnimationsRegistrySeasonal.register("mob/parrot_dance");
   IAnimationSource PARROT_DANCE_HUMAN = AnimationsRegistrySeasonal.register("humanoid/parrot_dance_human");
   @SpecialAnim
   IAnimationSource RAYA_PARROT_DANCE = AnimationsRegistrySeasonal.register("rayaprime/raya_parrot_dance");
   IAnimationSource RAYA_THROWER_ENDANIMATED = AnimationsRegistrySeasonal.register("humanoid/raya_thrower_endanimated");
   @SpecialAnim
   IAnimationSource JDH_RAYA_CUTSCENE_TAKEN_ENDANIMATED = AnimationsRegistrySeasonal.register("raya_jdh_cutscene/raya_taken_endanimated");
   @SpecialAnim
   IAnimationSource JDH_RAYA_CUTSCENE_THROW_ENDANIMATED = AnimationsRegistrySeasonal.register("raya_jdh_cutscene/raya_throw_endanimated");
   @SpecialAnim
   IAnimationSource JDH_RAYA_CUTSCENE_NO_RAYA = AnimationsRegistrySeasonal.register("raya_jdh_cutscene/no_raya_cutscene");
   @SpecialAnim
   IAnimationSource JDH_RAYA_CUTSCENE_RAYA_SHORTS_IDLE = AnimationsRegistrySeasonal.register("raya_jdh_cutscene/raya_shorts_idle");
   IAnimationSource VANILLA_PUNCH = AnimationsRegistrySeasonal.register("humanoid/vanilla_punch");
   IAnimationSource CAT_JUMP_FROM_BED = AnimationsRegistrySeasonal.register("cat_jump_from_bed");
   @SpecialAnim("GARAGE_DOOR")
   IAnimationSource GARAGE_DOOR_CLOSE = AnimationsRegistrySeasonal.register("garage_door/garage_door_close");
   @SpecialAnim("GARAGE_DOOR")
   IAnimationSource GARAGE_DOOR_OPEN = AnimationsRegistrySeasonal.register("garage_door/garage_door_open");
   @SpecialAnim("GARAGE_DOOR")
   IAnimationSource GARAGE_OPENED_HANDS_GARDGE = AnimationsRegistrySeasonal.register("garage_door/door_opened_hands_gardge");
   @SpecialAnim
   IAnimationSource HAZMAT_LAY_CUTSCENE = AnimationsRegistrySeasonal.register("cutscene/hazmat_lay_cutscene");
   @SpecialAnim
   IAnimationSource LAIA_LAY_CUTSCENE = AnimationsRegistrySeasonal.register("cutscene/laia_and_clone_cutscene");
   IAnimationSource MEASURING_LENGTH = AnimationsRegistrySeasonal.register("humanoid/measuring_length");
   @SpecialAnim
   IAnimationSource WALTZ_CUTSCENE_START = AnimationsRegistrySeasonal.register("cutscene/waltz_cutscene_start");
   @SpecialAnim
   IAnimationSource WALTZ_CUTSCENE_CYCLE = AnimationsRegistrySeasonal.register("cutscene/waltz_cutscene_cycle");
   @SpecialAnim
   IAnimationSource WALTZ_CUTSCENE_ENDING = AnimationsRegistrySeasonal.register("cutscene/waltz_cutscene_ending");
   @SpecialAnim
   IAnimationSource DRONE_WALTZ_PART1 = AnimationsRegistrySeasonal.register("cutscene/drones_waltz_part1");
   @SpecialAnim
   IAnimationSource DRONE_WALTZ_PART2 = AnimationsRegistrySeasonal.register("cutscene/drone_waltz_part2");
   @SpecialAnim
   IAnimationSource BOWLING_BALL_THROW = AnimationsRegistrySeasonal.register("bowling/bowling_ball_throw");
   @SpecialAnim
   IAnimationSource BOWLING_BALL_MOVING_CYCLE = AnimationsRegistrySeasonal.register("bowling/bowling_ball_moving_cycle");
   @SpecialAnim
   IAnimationSource BOWLING_1_REMAIN_LEFT = AnimationsRegistrySeasonal.register("bowling/bowling_1remain_left");
   @SpecialAnim
   IAnimationSource BOWLING_1_REMAIN_RIGHT = AnimationsRegistrySeasonal.register("bowling/bowling_1remain_right");
   @SpecialAnim
   IAnimationSource BOWLING_3_REMAIN_LEFT = AnimationsRegistrySeasonal.register("bowling/bowling_3remain_left");
   @SpecialAnim
   IAnimationSource BOWLING_3_REMAIN_RIGHT = AnimationsRegistrySeasonal.register("bowling/bowling_3remain_right");
   @SpecialAnim
   IAnimationSource BOWLING_7_REMAIN_LEFT = AnimationsRegistrySeasonal.register("bowling/bowling_7remain_left");
   @SpecialAnim
   IAnimationSource BOWLING_7_REMAIN_RIGHT = AnimationsRegistrySeasonal.register("bowling/bowling_7remain_right");
   @SpecialAnim
   IAnimationSource BOWLING_STRIKE = AnimationsRegistrySeasonal.register("bowling/bowling_strike");
   @SpecialAnim
   IAnimationSource PINS_FALL_STELLA_HIT = AnimationsRegistrySeasonal.register("bowling/pins_fall_stella_hit");
   @SpecialAnim
   IAnimationSource BOWLING_PINS_CLOSING = AnimationsRegistrySeasonal.register("bowling/bowling_pins_closing");
   @SpecialAnim
   IAnimationSource ORG_BOWLING_PINS_CLOSING = AnimationsRegistrySeasonal.register("bowling/org_bowling_pins_closing");
   @SpecialAnim
   IAnimationSource ORG_BOWLING_PINS_OPENED = AnimationsRegistrySeasonal.register("bowling/org_bowling_pins_opened");
   @SpecialAnim
   IAnimationSource BOWLING_ARROW_SINUS_ROTATION_Y = AnimationsRegistrySeasonal.register("bowling/bowling_arrow_sinus_rotation_y");
   @SpecialAnim
   IAnimationSource BOWLING_ARROW_SINUS_SIZE_XZ = AnimationsRegistrySeasonal.register("bowling/bowling_arrow_sinus_size_xz");
   @SpecialAnim
   IAnimationSource TAIL_WAG = AnimationsRegistrySeasonal.register("mob/tail_wag");
   @SpecialAnim("FATHER_GLITCH_GOOSE_CUTSCENE")
   IAnimationSource GOOSE_FATHER_THROW = AnimationsRegistrySeasonal.register("goose_father_throw");
   IAnimationSource WALTZ_STEVE = AnimationsRegistrySeasonal.register("humanoid/waltz_steve");
   IAnimationSource WALTZ_ALEX = AnimationsRegistrySeasonal.register("humanoid/waltz_alex");
   IAnimationSource SIT_TO_STAY = AnimationsRegistrySeasonal.register("humanoid/body_endanimated_sittostay");
   IAnimationSource STAY_TO_SIT = AnimationsRegistrySeasonal.register("humanoid/body_endanimated_staytosit");
   IAnimationSource LONG_CHECK_CYCLE = AnimationsRegistrySeasonal.register("humanoid/long_check_cycle");
   IAnimationSource TABLET_TAP = AnimationsRegistrySeasonal.register("humanoid/tablet_tap");
   IAnimationSource TABLET_TAP_FROM_POSE = AnimationsRegistrySeasonal.register("humanoid/tablet_tap_from_pose");
   IAnimationSource TABLET_WRITING_CYCLE = AnimationsRegistrySeasonal.register("humanoid/tablet_writing_cycle");
   IAnimationSource VINEGLASS_GIVE = AnimationsRegistrySeasonal.register("humanoid/vineglass_give");
   IAnimationSource DUMMY_APPEAR = AnimationsRegistrySeasonal.register("humanoid/dummy_appear");
   IAnimationSource DUMMY_PISTOL_DYING = AnimationsRegistrySeasonal.register("humanoid/dummy_appear");
   IAnimationSource DUMMY_PISTOL_SHOOTING = AnimationsRegistrySeasonal.register("humanoid/dummy_pistol_shooting");
   IAnimationSource START_RUNNING = AnimationsRegistrySeasonal.register("humanoid/start_running");
   IAnimationSource STOP_RUNNING = AnimationsRegistrySeasonal.register("humanoid/stop_running");
   IAnimationSource DRINKING_LAYER_HARD = AnimationsRegistrySeasonal.register("humanoid/drinking_layer_hard");
   IAnimationSource LAY_WEAKENED = AnimationsRegistrySeasonal.register("humanoid/steve_lay_weakened");
   IAnimationSource CLAP_HANDS_STOMACH = AnimationsRegistrySeasonal.register("humanoid/clap_hands_stomach");
   IAnimationSource CUFFS_ADJUST = AnimationsRegistrySeasonal.register("humanoid/cuffs_adjust");
   @SpecialAnim
   IAnimationSource FATHER_GLITCH_IDLE = AnimationsRegistrySeasonal.register("father_glitch_idle");
   IAnimationSource SWORD_FIGHT_BLOCKING = AnimationsRegistrySeasonal.register("humanoid/sword_fight_blocking");
   IAnimationSource FALL_ON_KNEES_HOLD_HEAD = AnimationsRegistrySeasonal.register("humanoid/fall_on_knees_hold_head");
   IAnimationSource SKRIMER_EVIL_FACE = AnimationsRegistrySeasonal.register("humanoid/skrimer_evil_face");
   @SpecialAnim("SKRIMER")
   IAnimationSource SKRIMER_FIRST_BUMP_HOLD = AnimationsRegistrySeasonal.register("skrimer_fist_bump_hold");
   IAnimationSource PISTOL_TO_HEAD_TREMBLING = AnimationsRegistrySeasonal.register("humanoid/pistol_to_head_trembling");
   IAnimationSource SLID_DOWN_SIT = AnimationsRegistrySeasonal.register("humanoid/slid_down_sit");
   @SpecialAnim
   IAnimationSource CONCRETE_SLAB_FALL = AnimationsRegistrySeasonal.register("concrete_slab_fall");
   IAnimationSource GOT_UP_BED_FRONT = AnimationsRegistrySeasonal.register("humanoid/got_up_bed_front");
   IAnimationSource STAB_PREPARE = AnimationsRegistrySeasonal.register("humanoid/stab_prepare");
   IAnimationSource STAB_JUMP = AnimationsRegistrySeasonal.register("humanoid/stab_jump");
   IAnimationSource STAB_GETUP = AnimationsRegistrySeasonal.register("humanoid/stab_getup");
   IAnimationSource LOLOLOSHKA_PSYCHO_FACE = AnimationsRegistrySeasonal.register("humanoid/lololoshka_psycho_face");
   IAnimationSource RAISE_EGG_ENDANIMATED = AnimationsRegistrySeasonal.register("humanoid/raise_egg_endanimated");
   IAnimationSource TIRED_WALK = AnimationsRegistrySeasonal.register("humanoid/tired_walk_cycle");
   @SpecialAnim
   IAnimationSource BB_CUTSCENE_1 = AnimationsRegistrySeasonal.register("bloodborne_cutscene1");
   @SpecialAnim
   IAnimationSource BB_CUTSCENE_2 = AnimationsRegistrySeasonal.register("bloodborne_cutscene2");
   IAnimationSource ONLY_SHOULDERS_SHRUG_UPDOWN = AnimationsRegistrySeasonal.register("humanoid/only_shoulders_up_endanimated");
   IAnimationSource ARMS_BACKWARD_20 = AnimationsRegistrySeasonal.register("humanoid/arms_20degrees_down");
   @SpecialAnim
   IAnimationSource ZERO_SCALE_2 = AnimationsRegistrySeasonal.register("zero_scale2");
   @SpecialAnim
   IAnimationSource BB_METAL_GATES_CUTSCENE = AnimationsRegistrySeasonal.register("metal_gates_cutscene");
   @SpecialAnim
   IAnimationSource BB_OKETRA_TRANSFORM = AnimationsRegistrySeasonal.register("oketra_transform");
   IAnimationSource KNEEL_PRAY_POSE = AnimationsRegistrySeasonal.register("humanoid/kneel_pray_pose");
   IAnimationSource KNEEL_PRAY_HANDS_TO_CHEST = AnimationsRegistrySeasonal.register("humanoid/kneel_pray_hands_to_chest");
   @SpecialAnim
   IAnimationSource BB_JDH_SHANK = AnimationsRegistrySeasonal.register("jdh_lo_sword_scene");
   IAnimationSource HOLD_BOOK_POSE = AnimationsRegistrySeasonal.register("humanoid/hold_book_pose");
   IAnimationSource HUMANOID_BOOK_OPEN_150_DEGREES = AnimationsRegistrySeasonal.register("humanoid/human_book_open_150degrees");
   IAnimationSource ALEX_STAND_AIM_GUN = AnimationsRegistrySeasonal.register("humanoid/alex_stand_aim_gun");
   IAnimationSource LAIA_WAKE_SCENE_S39 = AnimationsRegistrySeasonal.register("humanoid/laya_wake_scene_s39");
   IAnimationSource RUBBING_HANDS_ENDANIMATED = AnimationsRegistrySeasonal.register("humanoid/rubbing_hands_endanimated");
   IAnimationSource TABLET_SWIPING_CYCLE = AnimationsRegistrySeasonal.register("humanoid/tablet_swiping_cycle");
   IAnimationSource TABLET_RATATOUILLE_MEME = AnimationsRegistrySeasonal.register("humanoid/tablet_ratatouille_meme");
   IAnimationSource HIT_CAR_HOOD = AnimationsRegistrySeasonal.register("humanoid/hit_car_hood");
   IAnimationSource EMOTION_EMPATHY = AnimationsRegistrySeasonal.register("humanoid/emotion_empathy");
   IAnimationSource GET_UP_FROM_PRAY = AnimationsRegistrySeasonal.register("humanoid/get_up_from_pray");
   IAnimationSource GESTURE_RAGE_1 = AnimationsRegistrySeasonal.register("humanoid/gesture_rage1");
   IAnimationSource GESTURE_RAGE_2 = AnimationsRegistrySeasonal.register("humanoid/gesture_rage2");
   IAnimationSource GESTURE_RAGE_3 = AnimationsRegistrySeasonal.register("humanoid/gesture_rage3");
   IAnimationSource GESTURE_RAGE_4 = AnimationsRegistrySeasonal.register("humanoid/gesture_rage4");
   IAnimationSource GESTURE_RAGE_5 = AnimationsRegistrySeasonal.register("humanoid/gesture_rage5");
   IAnimationSource RAGE_FISTS_POSE = AnimationsRegistrySeasonal.register("humanoid/rage_fists_pose");
   IAnimationSource RAGE_WAVING_HANDS = AnimationsRegistrySeasonal.register("humanoid/rage_waving_hands");
   IAnimationSource ROLL_UP_SLEEVES = AnimationsRegistrySeasonal.register("humanoid/roll_up_sleeves");
   IAnimationSource RAGE_SHAKING = AnimationsRegistrySeasonal.register("humanoid/rage_shaking");
   IAnimationSource GET_HAZMAT_FROM_BOX = AnimationsRegistrySeasonal.register("humanoid/get_hazmat_from_box");
   IAnimationSource TWITCHING_BODY = AnimationsRegistrySeasonal.register("twitching_body");
   @SpecialAnim
   IAnimationSource SCINT_BROKE = AnimationsRegistrySeasonal.register("humanoid/scint_broke");
   IAnimationSource ELF_BOOK_TO_CHEST = AnimationsRegistrySeasonal.register("humanoid/elf_book_to_chest");
   IAnimationSource EMOTION_SLIGHT_HAPPY = AnimationsRegistrySeasonal.register("humanoid/emotion_slight_happy");
   @SpecialAnim
   IAnimationSource S39_LINES_APPEAR = AnimationsRegistrySeasonal.register("cutscene/lines_appear");
   @SpecialAnim
   IAnimationSource S39_LINES_HEAD_APPEAR = AnimationsRegistrySeasonal.register("cutscene/lines_head_appear");
   @SpecialAnim
   IAnimationSource S39_LINES_HEAD_FALL = AnimationsRegistrySeasonal.register("cutscene/lines_head_fall");
   @SpecialAnim
   IAnimationSource S39_LINES_HEAD_MOVING_CYCLE = AnimationsRegistrySeasonal.register("cutscene/lines_head_moving_cycle");
   @SpecialAnim
   IAnimationSource S86_CUTSCENE_1_LO = AnimationsRegistrySeasonal.register("cutscene2/cutscnene_s86_1_lo");
   @SpecialAnim
   IAnimationSource S86_CUTSCENE_1_MALETSKI = AnimationsRegistrySeasonal.register("cutscene2/cutscnene_s86_1_maletski");
   @SpecialAnim
   IAnimationSource S86_CUTSCENE_2_LAIA = AnimationsRegistrySeasonal.register("cutscene2/cutscnene_s86_2_laya");
   @SpecialAnim
   IAnimationSource S86_CUTSCENE_2_MALETSKI = AnimationsRegistrySeasonal.register("cutscene2/cutscnene_s86_2_maletski");
   @SpecialAnim
   IAnimationSource S86_CUTSCENE_3_LAIA = AnimationsRegistrySeasonal.register("cutscene2/cutscnene_s86_3_laya");
   @SpecialAnim
   IAnimationSource S86_CUTSCENE_3_MALETSKI = AnimationsRegistrySeasonal.register("cutscene2/cutscnene_s86_3_maletski");
   @SpecialAnim
   IAnimationSource S86_CUTSCENE_3_LO = AnimationsRegistrySeasonal.register("cutscene2/cutscnene_s86_3_lo");
   @SpecialAnim
   IAnimationSource S86_CUTSCENE_5_LAIA = AnimationsRegistrySeasonal.register("cutscene2/cutscnene_s86_5_laya");
   @SpecialAnim
   IAnimationSource S86_CUTSCENE_5_LO = AnimationsRegistrySeasonal.register("cutscene2/cutscnene_s86_5_lo");
   @SpecialAnim
   IAnimationSource S86_CUTSCENE_5_MALETSKI = AnimationsRegistrySeasonal.register("cutscene2/cutscnene_s86_5_maletski");
   @SpecialAnim
   IAnimationSource S86_CUTSCENE_6_LO_CYCLE = AnimationsRegistrySeasonal.register("cutscene2/cutscnene_s86_6_lo_cycle");
   @SpecialAnim
   IAnimationSource S96_LOLOLOSHKA_AMBUSH_CUTSCENE = AnimationsRegistrySeasonal.register("cutscene/lololoshka_ambush_cutscene");
   @SpecialAnim
   IAnimationSource S97_GROUND_ATTACK_LAYER = AnimationsRegistrySeasonal.register("humanoid/s97_ground_attack_layer");
   IAnimationSource EPILEPSY_CYCLE = AnimationsRegistrySeasonal.register("epilepsy_cycle");
   IAnimationSource BACK_CRAWL_TO_STAND = AnimationsRegistrySeasonal.register("humanoid/back_crawl_to_stand");
   IAnimationSource EYE_ROLLING_RIGHT_TO_LEFT = AnimationsRegistrySeasonal.register("humanoid/eye_rolling_right_to_left");
   IAnimationSource FULLKNEES_SIT_HOLD_SHOULDER = AnimationsRegistrySeasonal.register("humanoid/fullknees_sit_hold_shoulder");
   IAnimationSource LOSE_LAY_TO_SIT = AnimationsRegistrySeasonal.register("humanoid/lose_lay_to_sit");
   IAnimationSource ON_KNEES_FALL_BACK = AnimationsRegistrySeasonal.register("humanoid/on_knees_fall_back");
   IAnimationSource ON_KNEES_FALL_BACK_POSE = AnimationsRegistrySeasonal.register("humanoid/on_knees_fall_back_pose");
   IAnimationSource FLOPPING_TO_SIDELAY_RIGHT = AnimationsRegistrySeasonal.register("humanoid/flopping_to_sidelay_right");
   IAnimationSource TWIST_SCERWDRIVER_CYCLE = AnimationsRegistrySeasonal.register("humanoid/twist_scerwdriver_cycle");
   IAnimationSource MOVE_HEAD_SIDE_TO_SIDE = AnimationsRegistrySeasonal.register("humanoid/move_head_side_to_side");
   IAnimationSource VALDHAR_SIT_CAR_CUTSCENE = AnimationsRegistrySeasonal.register("valdhar_sit_car_cutscene");
   IAnimationSource HEAD2_40_UP = AnimationsRegistrySeasonal.register("humanoid/head2_40_up");
   IAnimationSource HEAD2_CURIOUS = AnimationsRegistrySeasonal.register("humanoid/head2_curious", "no_face");
   IAnimationSource NO_MODEL3 = AnimationsRegistrySeasonal.register("no_model3");
   IAnimationSource HOLD_BAG_POSE_NEW = AnimationsRegistrySeasonal.register("humanoid/hold_bag_pose_new");
   IAnimationSource STEAL_AND_PUT_IN_BAG_CYCLE = AnimationsRegistrySeasonal.register("humanoid/steal_and_put_in_bag_cycle");
   IAnimationSource HOLLOW_EYES = AnimationsRegistrySeasonal.register("humanoid/hollow_eyes");
   IAnimationSource FIST_BUMP_HOLD = AnimationsRegistrySeasonal.register("humanoid/fist_bump_hold");
   IAnimationSource MODEL_SCALE = AnimationsRegistrySeasonal.register("model_scale");
   IAnimationSource SCALE_08 = AnimationsRegistrySeasonal.register("scale0_8");
   IAnimationSource BODY_CLOTHES_TAKE_OFF = AnimationsRegistrySeasonal.register("humanoid/body_clothes_take_off");
   IAnimationSource EYES_UP = AnimationsRegistrySeasonal.register("humanoid/eyes_up");
   IAnimationSource BRUSHING_HEAD = AnimationsRegistrySeasonal.register("humanoid/brushing_head");
   IAnimationSource HARD_SCRATCHING_BODY = AnimationsRegistrySeasonal.register("humanoid/hard_scratching_body");
   IAnimationSource ROTATE_AROUND_ITS_AXIS_CYCLE = AnimationsRegistrySeasonal.register("rotate_around_its_axis_cycle");
   IAnimationSource GRAB_NECK = AnimationsRegistrySeasonal.register("humanoid/grab_neck");
   IAnimationSource SWINGING_LEGS_LAYER_CYCLE = AnimationsRegistrySeasonal.register("humanoid/swinging_legs_layer_cycle");
   IAnimationSource TYPING_TALL_CYCLE = AnimationsRegistrySeasonal.register("humanoid/typing_tall_cycle");
   IAnimationSource HOLD_BOTTLES_POSE = AnimationsRegistrySeasonal.register("humanoid/hold_bottles_pose");
   IAnimationSource HOLD_TEDDY_POSE = AnimationsRegistrySeasonal.register("humanoid/hold_teddy_pose");
   IAnimationSource LEFT_POKING_PHONE = AnimationsRegistrySeasonal.register("left_poking_phone");
   IAnimationSource SKYZERN_CLOSED_WINGS = AnimationsRegistrySeasonal.register("skyzern_closed_wings");
   @SpecialAnim("ORGANIZATION_DOOR")
   IAnimationSource ORGANIZATION_DOOR_OPEN = AnimationsRegistrySeasonal.register("organization_door_open");
   @SpecialAnim("ORGANIZATION_DOOR")
   IAnimationSource ORGANIZATION_DOOR_CLOSE = AnimationsRegistrySeasonal.register("organization_door_close");
   IAnimationSource WALL_HIT_FIST_CYCLE = AnimationsRegistrySeasonal.register("humanoid/wall_hit_fist_cycle");
   @SpecialAnim("MODDED_DONKEY")
   IAnimationSource DONKEY_EAT = AnimationsRegistrySeasonal.register("mob/donkey/donkey_eat");
   @SpecialAnim("MODDED_DONKEY")
   IAnimationSource DONKEY_GRAZE = AnimationsRegistrySeasonal.register("mob/donkey/donkey_graze");
   @SpecialAnim("MODDED_DONKEY")
   IAnimationSource DONKEY_RUN = AnimationsRegistrySeasonal.register("mob/donkey/donkey_run");
   @SpecialAnim("MODDED_DONKEY")
   IAnimationSource DONKEY_SUSPICIOUS = AnimationsRegistrySeasonal.register("mob/donkey/donkey_suspicious");
   @SpecialAnim("MODDED_DONKEY")
   IAnimationSource DONKEY_TIRED = AnimationsRegistrySeasonal.register("mob/donkey/donkey_tired");
   @SpecialAnim("MODDED_DONKEY")
   IAnimationSource DONKEY_DONTCARE = AnimationsRegistrySeasonal.register("mob/donkey/donkey_dontcare");
   @SpecialAnim("MODDED_DONKEY")
   IAnimationSource DONKEY_FALL = AnimationsRegistrySeasonal.register("mob/donkey/donkey_fall");
   @SpecialAnim("MODDED_DONKEY")
   IAnimationSource DONKEY_GET_UP = AnimationsRegistrySeasonal.register("mob/donkey/donkey_get_up");
   @SpecialAnim("MODDED_DONKEY")
   IAnimationSource DONKEY_JUMP_LAYER = AnimationsRegistrySeasonal.register("mob/donkey/donkey_jump_layer");
   @SpecialAnim("MODDED_DONKEY")
   IAnimationSource DONKEY_SHOCKED = AnimationsRegistrySeasonal.register("mob/donkey/donkey_shocked");
   @SpecialAnim("MODDED_DONKEY")
   IAnimationSource DONKEY_WALK_CYCLE = AnimationsRegistrySeasonal.register("mob/donkey/donkey_walk_cycle");
   IAnimationSource SHAKED_LAYER = AnimationsRegistrySeasonal.register("humanoid/shaked_layer");
   IAnimationSource MODEL_SWING_LEFT_RIGHT = AnimationsRegistrySeasonal.register("humanoid/model_swing_left_right");
   IAnimationSource SIT_PUNCH_GROUND_BOTH = AnimationsRegistrySeasonal.register("humanoid/sit_punch_ground_both");
   @SpecialAnim
   IAnimationSource PM_WOLF_WALK = AnimationsRegistrySeasonal.register("mob/pm_wolf_walk");
   IAnimationSource ASSA_CERBERUS_BARK = AnimationsRegistrySeasonal.register("mob/assa_cerberus_bark");
   @SpecialAnim
   IAnimationSource PIG_FALL = AnimationsRegistrySeasonal.register("mob/pig/pig_fall");
   @SpecialAnim
   IAnimationSource PIG_GET_UP = AnimationsRegistrySeasonal.register("mob/pig/pig_get_up");
   @SpecialAnim
   IAnimationSource PIG_HAPPY_JUMPING = AnimationsRegistrySeasonal.register("mob/pig/pig_happy_jumping");
   @SpecialAnim
   IAnimationSource PIG_RUN_CYCLE = AnimationsRegistrySeasonal.register("mob/pig/pig_run_cycle");
   @SpecialAnim
   IAnimationSource PIG_TIRED = AnimationsRegistrySeasonal.register("mob/pig/pig_tired");
   @SpecialAnim
   IAnimationSource BIRD_COUGHING = AnimationsRegistrySeasonal.register("mob/bird/bird_coughing");
   @SpecialAnim
   IAnimationSource BIRD_FLYING = AnimationsRegistrySeasonal.register("mob/bird/bird_flying");
   @SpecialAnim
   IAnimationSource BIRD_IDLE = AnimationsRegistrySeasonal.register("mob/bird/bird_idle");
   @SpecialAnim
   IAnimationSource MOLE_HEAD_APPEAR = AnimationsRegistrySeasonal.register("mole_head_appear");
   @SpecialAnim
   IAnimationSource MOLE_HEAD_PUNCH = AnimationsRegistrySeasonal.register("mole_head_punch");
   IAnimationSource HIT_UPPER_BLOCK = AnimationsRegistrySeasonal.register("hit_upper_block");
   IAnimationSource JUMP_UP = AnimationsRegistrySeasonal.register("jump_up");
   IAnimationSource LAY_BROKEN_LEG = AnimationsRegistrySeasonal.register("lay_broken_leg");
   IAnimationSource HAND_SCULPTING_CYCLE = AnimationsRegistrySeasonal.register("hand_sculpting_cycle");
   IAnimationSource SLIME_PILE_GROW = AnimationsRegistrySeasonal.register("slime_pile_grow");
   IAnimationSource SLIME_TRANSFORM = AnimationsRegistrySeasonal.register("slime_transform");
   IAnimationSource SLIMES_CLIMB_OVER_WALL = AnimationsRegistrySeasonal.register("slimes_climb_over_wall");
   IAnimationSource SLIMES_CLIMB_OVER_WALL_AND_SIT = AnimationsRegistrySeasonal.register("slimes_climb_over_wall_and_sit");
   IAnimationSource SLIMES_CLIMB_OVER_WALL_GET_UP = AnimationsRegistrySeasonal.register("slimes_climb_over_wall_get_up");
   IAnimationSource TRIPPING_LAYER = AnimationsRegistrySeasonal.register("tripping_layer");
   IAnimationSource WORM_CRAWLING = AnimationsRegistrySeasonal.register("worm_crawling");
   IAnimationSource LOOK_OVER_BLOCKS = AnimationsRegistrySeasonal.register("look_over_blocks");
   IAnimationSource SLIME_BODY_SHAKING = AnimationsRegistrySeasonal.register("slime_body_shaking");
   IAnimationSource SLIME_BODY_APPEAR = AnimationsRegistrySeasonal.register("slime_body_appear");
   IAnimationSource SLIME_BODY_WAVE = AnimationsRegistrySeasonal.register("slime_body_wave");
   IAnimationSource SLIME_BODY_GONE = AnimationsRegistrySeasonal.register("slime_body_gone");
   IAnimationSource NO_SLIME1 = AnimationsRegistrySeasonal.register("no_slime1");
   IAnimationSource NO_SLIME2 = AnimationsRegistrySeasonal.register("no_slime2");
   @SpecialAnim("SPACE_SHEET")
   IAnimationSource SPACE_SHEET_APPEAR = AnimationsRegistrySeasonal.register("space_sheet/space_sheet_appear");
   @SpecialAnim("SPACE_SHEET")
   IAnimationSource SPACE_SHEET_WORLD1 = AnimationsRegistrySeasonal.register("space_sheet/space_sheet_world1");
   @SpecialAnim("SPACE_SHEET")
   IAnimationSource SPACE_SHEET_WORLD2 = AnimationsRegistrySeasonal.register("space_sheet/space_sheet_world2");
   @SpecialAnim("SPACE_SHEET")
   IAnimationSource SPACE_SHEET_WORLD3 = AnimationsRegistrySeasonal.register("space_sheet/space_sheet_world3");
   IAnimationSource LAY_FROM_SIDE_TO_SIDE = AnimationsRegistrySeasonal.register("humanoid/lay_from_side_to_side");
   IAnimationSource FLOPPING_ON_STOMACH_RIGHT = AnimationsRegistrySeasonal.register("humanoid/flopping_on_stomach_right");
   @SpecialAnim("BUNKER_DOOR_SECRET")
   IAnimationSource BUNKER_DOOR_SECRET_OPEN = AnimationsRegistrySeasonal.register("bunker_door_secret/open_bunker_door_endanimated");
   @SpecialAnim("SCREAMER_WITH_CANNON")
   IAnimationSource SCREAMER_TAKE_CANNON = AnimationsRegistrySeasonal.register("screamer_cannon_take");
   @SpecialAnim("SCREAMER_WITH_CANNON")
   IAnimationSource SCREAMER_TAKE_AIM = AnimationsRegistrySeasonal.register("screamer_cannon_scope_pose");
   @SpecialAnim("SCREAMER_WITH_CANNON")
   IAnimationSource SCREAMER_TAKE_AIM_UP = AnimationsRegistrySeasonal.register("screamer_cannon_airstrike_stand");
   @SpecialAnim("SCREAMER_WITH_CANNON")
   IAnimationSource SCREAMER_CANNON_HOLD = AnimationsRegistrySeasonal.register("screamer_with_cannon_idle");
   @SpecialAnim("SCREAMER_WITH_CANNON")
   IAnimationSource SCREAMER_CANNON_SHOT = AnimationsRegistrySeasonal.register("screamer_cannon_shot_layer");
   @SpecialAnim("SCREAMER_WITH_CANNON")
   IAnimationSource SCREAMER_CANNON_SHOT_BIG = AnimationsRegistrySeasonal.register("screamer_cannon_big_shot_layer");
   @SpecialAnim("SCREAMER_WITH_CANNON")
   IAnimationSource SCREAMER_CANNON_RELOAD = AnimationsRegistrySeasonal.register("screamer_cannon_reloading");
   @SpecialAnim("SCREAMER_WITH_CANNON")
   IAnimationSource SCREAMER_ROCKETJUMP_START = AnimationsRegistrySeasonal.register("screamer_cannon_rocketjump_start");
   @SpecialAnim("SCREAMER_WITH_CANNON")
   IAnimationSource SCREAMER_ROCKETJUMP_FLY = AnimationsRegistrySeasonal.register("screamer_cannon_rocketjump_midjump_idle");
   @SpecialAnim("SCREAMER_WITH_CANNON")
   IAnimationSource SCREAMER_ROCKETJUMP_BREAK = AnimationsRegistrySeasonal.register("screamer_cannon_rocketjump_to_ground_lay");
   @SpecialAnim("SCREAMER_WITH_CANNON")
   IAnimationSource SCREAMER_GIVE_UP = AnimationsRegistrySeasonal.register("screamer_paper_waving");
   @SpecialAnim("SCREAMER_WITH_CANNON")
   IAnimationSource SCREAMER_REMOVE_PAPER = AnimationsRegistrySeasonal.register("screamer_paper_remove");
   @SpecialAnim("SCREAMER_WITH_CANNON")
   IAnimationSource SCREAMER_REPO = AnimationsRegistrySeasonal.register("screamer_squat");
   @SpecialAnim("SCREAMER_WITH_CANNON")
   IAnimationSource SCREAMER_REPO_TO_SALUTE = AnimationsRegistrySeasonal.register("screamer_squat_to_respect");
   IAnimationSource MALETSKI_ARCHRAGE = AnimationsRegistrySeasonal.register("maletsky_archrage");
   IAnimationSource JOVANNI_SHH_FINGER = AnimationsRegistrySeasonal.register("humanoid/jovanni_shh_finger");
   IAnimationSource SKRIMER_EMOTION_SNIDE_SMIRK = AnimationsRegistrySeasonal.register("humanoid/skrimer_emotion_snide_smirk");
   SidedAnimationSet SKRIMER_FACEPALM = AnimationsRegistrySeasonal.registerSided("skrimer_facepalm_right", "skrimer_facepalm_left", "humanoid/skrimer_facepalm");
   IAnimationSource SKRIMER_RUBBING_HANDS_ENDANIMATED = AnimationsRegistrySeasonal.register("humanoid/skrimer_rubbing_hands_endanimated");
   IAnimationSource DECLARE_WINNER_HAND_RAISE = AnimationsRegistrySeasonal.register("humanoid/declare_winner_hand_raise");
   IAnimationSource WINNER_HAND_RAISED = AnimationsRegistrySeasonal.register("humanoid/winner_hand_raised");
   IAnimationSource HOLD_HAND_ON_KNEES_LAYER = AnimationsRegistrySeasonal.register("humanoid/hold_hand_on_knees_layer");
   IAnimationSource STRAIGHT_SIT_ARMSBACK_ON_CHAIR = AnimationsRegistrySeasonal.register("humanoid/straight_sit_armsback_on_chair");
   IAnimationSource HOLD_BOOK_ELBOW_POSE = AnimationsRegistrySeasonal.register("humanoid/hold_book_elbow_pose");
   @SpecialAnim
   IAnimationSource MID_BOOK_FLOOR_LAY = AnimationsRegistrySeasonal.register("mid_book_floor_lay");
   IAnimationSource DUMMY_HORNS_APPEARING = AnimationsRegistrySeasonal.register("dummy_horns_appearing");
   @SpecialAnim("STELLA")
   IAnimationSource STELLA_MOVE_HANDS_RIGHT_TO_LEFT = AnimationsRegistrySeasonal.register("stella/stella_move_hands_right_to_left");
   @SpecialAnim("STELLA")
   IAnimationSource STELLA_PLACES_CASE = AnimationsRegistrySeasonal.register("stella/stella_places_case");
   @SpecialAnim("STELLA")
   IAnimationSource STELLA_EMOTION_PRIDE = AnimationsRegistrySeasonal.register("stella/stella_emotion_pride");
   @SpecialAnim("STELLA")
   IAnimationSource STELLA_EYE_ROLLING_NEW = AnimationsRegistrySeasonal.register("stella/stella_eye_rolling_new");
   @SpecialAnim("STELLA")
   IAnimationSource STELLA_SURPRISE_POSE = AnimationsRegistrySeasonal.register("stella/stella_surprise_pose");
   @SpecialAnim("STELLA")
   IAnimationSource STELLA_BLINKING = AnimationsRegistrySeasonal.register("stella/stella_blinking");
   @SpecialAnim("STELLA")
   IAnimationSource STELLA_CITYWALK = AnimationsRegistrySeasonal.register("stella/stella_citywalk");
   @SpecialAnim("STELLA")
   IAnimationSource STELLA_IDLE = AnimationsRegistrySeasonal.register("stella/stella_idle");
   @SpecialAnim("STELLA")
   IAnimationSource STELLA_EYEBROWS_UP = AnimationsRegistrySeasonal.register("stella/stella_eyebrows_up");
   @SpecialAnim("STELLA")
   IAnimationSource STELLA_SIGH_NEW = AnimationsRegistrySeasonal.register("stella/stella_inhale_breath");
   @SpecialAnim("STELLA")
   SidedAnimationSet STELLA_EXPLAINING_CROSSED_ARMS = AnimationsRegistrySeasonal.registerSided(
      "explaining_crossed_arms_left", "explaining_crossed_arms_right", "stella/stella_explaining_crossed_arms"
   );
   IAnimationSource NAILS_CHECK_CYCLE = AnimationsRegistrySeasonal.register("stella/stella_nails_check_cycle");
   @SpecialAnim("STELLA")
   IAnimationSource STELLA_FRIGHT = AnimationsRegistrySeasonal.register("stella/stella_fright");
   @SpecialAnim("STELLA")
   SidedAnimationSet STELLA_CROSSED_ARMS = AnimationsRegistrySeasonal.registerSided("crossed_arms_left", "crossed_arms_right", "stella/stella_crossed_arms");
   @SpecialAnim("STELLA")
   IAnimationSource STELLA_SLANT_ENDANIMATED = AnimationsRegistrySeasonal.register("stella/stella_slant_endanimated");
   @SpecialAnim("STELLA")
   IAnimationSource STELLA_SLANT_DOWN_HOLDING = AnimationsRegistrySeasonal.register("stella/stella_slant_down_holding");
   @SpecialAnim("STELLA")
   IAnimationSource STELLA_JUMPS_BACK_FOR_SLANT_DOWN = AnimationsRegistrySeasonal.register("stella/stella_jumps_back_for_slant_down");
   @SpecialAnim("STELLA")
   IAnimationSource STELLA_PAIN_ENDANIMATED = AnimationsRegistrySeasonal.register("stella/stella_pain_endanimated");
   @SpecialAnim("STELLA")
   IAnimationSource STELLA_DYSPNEA_CYCLE = AnimationsRegistrySeasonal.register("stella/stella_dyspnea_cycle");
   @SpecialAnim("STELLA")
   IAnimationSource STELLA_BODY_CYCLE_EXAMINE = AnimationsRegistrySeasonal.register("stella/stella_body_cycle_examine");
   @SpecialAnim("STELLA")
   IAnimationSource STELLA_REVIVER_THINKING = AnimationsRegistrySeasonal.register("stella/stella_reviver_thinking");
   @SpecialAnim("STELLA")
   IAnimationSource STELLA_CIRCULAR_HIT = AnimationsRegistrySeasonal.register("stella/stella_circular_hit");
   @SpecialAnim("STELLA")
   IAnimationSource STELLA_HUG_YOURSELF_NOW = AnimationsRegistrySeasonal.register("stella/stella_hug_yourself_now");
   @SpecialAnim("STELLA")
   IAnimationSource STELLA_BODY_POSE_EXORCISM = AnimationsRegistrySeasonal.register("stella/stella_body_pose_exorcism");
   @SpecialAnim("STELLA")
   IAnimationSource STELLA_POSE_DISGUST = AnimationsRegistrySeasonal.register("stella/stella_emoc_pose_disgust");
   @SpecialAnim("STELLA")
   IAnimationSource STELLA_PRAY_ENDANIMATED_SLIM = AnimationsRegistrySeasonal.register("stella/stella_pray_endanimated_slim");
   @SpecialAnim("STELLA")
   IAnimationSource STELLA_CLOSE_EYES_HANDS = AnimationsRegistrySeasonal.register("stella/stella_close_eyes_hands");
   @SpecialAnim("STELLA")
   IAnimationSource STELLA_EMBARRASSMENT = AnimationsRegistrySeasonal.register("stella/stella_embarrassment");
   @SpecialAnim("STELLA")
   IAnimationSource STELLA_BODY_CYCLE_JUMPCLAP = AnimationsRegistrySeasonal.register("stella/stella_body_cycle_jumpclap");
   @SpecialAnim("STELLA")
   IAnimationSource STELLA_ANGRY = AnimationsRegistrySeasonal.register("stella/stella_angry");
   @SpecialAnim("STELLA")
   IAnimationSource STELLA_ANGRY_HALF = AnimationsRegistrySeasonal.register("stella/stella_angry_half");
   @SpecialAnim("STELLA")
   IAnimationSource STELLA_HANDS_POSE_HANDSONSIDES = AnimationsRegistrySeasonal.register("stella/stella_hands_pose_handsonsides");
   @SpecialAnim("STELLA")
   SidedAnimationSet STELLA_SURPRISED = AnimationsRegistrySeasonal.registerSided(
      "stella_side_surprised_right", "stella_side_surprised_left", "stella/stella_surprised"
   );
   @SpecialAnim("STELLA")
   IAnimationSource STELLA_SHOCKED = AnimationsRegistrySeasonal.register("stella/stella_shocked");
   @SpecialAnim("STELLA_DYING")
   IAnimationSource STELLA_DEATH = AnimationsRegistrySeasonal.register("stella/stella_death");
   IAnimationSource HEAD_DOWN_TO_TOP = AnimationsRegistrySeasonal.register("humanoid/head_down_to_top");
   IAnimationSource CRAWLS_STRANGLES = AnimationsRegistrySeasonal.register("humanoid/crawls_strangles");
   IAnimationSource TURN_BOOK_ELBOW = AnimationsRegistrySeasonal.register("humanoid/turn_book_elbow");
   IAnimationSource EYEBROWS_UP = AnimationsRegistrySeasonal.register("humanoid/eyebrows_up");
   IAnimationSource LAYA_SHOCK = AnimationsRegistrySeasonal.register("humanoid/laya_shock");
   IAnimationSource WRITE_HOLD_PAPER_CYCLE = AnimationsRegistrySeasonal.register("humanoid/write_hold_paper_cycle");
   @SpecialAnim("STELLA")
   IAnimationSource STELLA_FACE_POSE_JOY = AnimationsRegistrySeasonal.register("stella/stella_face_pose_joy");
   @SpecialAnim("STELLA")
   IAnimationSource STELLA_FINGER_COUNT_REPEAT = AnimationsRegistrySeasonal.register("stella/stella_finger_count_repeat");
   @SpecialAnim("STELLA")
   SidedAnimationSet STELLA_HAND_TO_CHEST = AnimationsRegistrySeasonal.registerSided(
      "stella_hand_to_chest_left", "stella_hand_to_chest_right", "stella/stella_hand_to_chest"
   );
   @SpecialAnim("STELLA")
   SidedAnimationSet STELLA_POINT_FORWARD = AnimationsRegistrySeasonal.registerSided(
      "stella_point_forward_left", "stella_point_forward_right", "stella/stella_point_forward"
   );
   IAnimationSource GESTURE_ITALIAN_CALM = AnimationsRegistrySeasonal.register("humanoid/gesture_italian_calm");
   IAnimationSource GESTURE_RAGE2_ENDANIMATED = AnimationsRegistrySeasonal.register("humanoid/gesture_rage2_endanimated");
   IAnimationSource MASK_DISABLE = AnimationsRegistrySeasonal.register("humanoid/mask_disable");
   IAnimationSource EMOTION_THINKING_CYCLE_LEFT = AnimationsRegistrySeasonal.register("humanoid/thinking_cycle_left");
   IAnimationSource TAKE_OFF_PANTS = AnimationsRegistrySeasonal.register("humanoid/take_off_pants");
   @SpecialAnim
   IAnimationSource PANTS_LAY_ON_GROUND_POSE = AnimationsRegistrySeasonal.register("pants_lay_on_ground_pose");
   IAnimationSource MOUNTAIN_CLIMBING_FINISH = AnimationsRegistrySeasonal.register("humanoid/mountain_climbing_finish");
   IAnimationSource CUT_OFF_HAIR_ENDANIMATED = AnimationsRegistrySeasonal.register("humanoid/cut_off_hair_endanimated");
   @SpecialAnim("VOLT")
   IAnimationSource ROBODOG_ATTACK_ENDANIMATED = AnimationsRegistrySeasonal.register("robo_dog/robodog_attack_endanimated");
   @SpecialAnim("VOLT")
   IAnimationSource ROBODOG_SAD_ENDANIMTED = AnimationsRegistrySeasonal.register("robo_dog/robodog_sad_endanimted");
   @SpecialAnim("VOLT")
   IAnimationSource ROBODOG_SIT_POSE = AnimationsRegistrySeasonal.register("robo_dog/robodog_sit_pose");
   @SpecialAnim("VOLT")
   IAnimationSource TAIL_WIGGLE_CYCLE = AnimationsRegistrySeasonal.register("robo_dog/tail_wiggle_cycle");
   @SpecialAnim("VOLT")
   IAnimationSource ROBODOG_WALK = AnimationsRegistrySeasonal.register("robo_dog/robodog_walk");
   @SpecialAnim("VOLT")
   IAnimationSource DOG_ANGRY_CYCLE = AnimationsRegistrySeasonal.register("robo_dog/dog_angry_cycle");
   @SpecialAnim("VOLT")
   IAnimationSource DOG_JUMP = AnimationsRegistrySeasonal.register("robo_dog/dog_jump");
   @SpecialAnim("VOLT")
   IAnimationSource ROBO_DOG_LAY = AnimationsRegistrySeasonal.register("robo_dog/robo_dog_lay");
   @SpecialAnim("VOLT")
   IAnimationSource ROBODOG_HAPPY_LIES_CYCLE = AnimationsRegistrySeasonal.register("robo_dog/robodog_happy_lies_cycle");
   @SpecialAnim("VOLT")
   IAnimationSource ROBODOG_INTEREST = AnimationsRegistrySeasonal.register("robo_dog/robodog_interest");
   IAnimationSource ELITE_ROBODOG_WINGS_CYCLE = AnimationsRegistrySeasonal.register("robo_dog/elite_robodog_wings_cycle");
   IAnimationSource GOSSE_SIT_FOR_CAT_STROKING_POSE = AnimationsRegistrySeasonal.register("gosse_sit_for_cat_stroking_pose");
   IAnimationSource GOSSE_JUMP_OFF_ENDANIMATED = AnimationsRegistrySeasonal.register("gosse_jump_off_endanimated");
   IAnimationSource ANGRY_PETTING_PET_INFRONT_PREPARE = AnimationsRegistrySeasonal.register("angry_petting_pet_infront_prepare");
   IAnimationSource ANGRY_PETTING_PET_INFRONT_CYCLE = AnimationsRegistrySeasonal.register("angry_petting_pet_infront_cycle");
   IAnimationSource SLIGHT_PUSH_SOMEONE = AnimationsRegistrySeasonal.register("slight_push_someone");
   IAnimationSource KNOCBACK_STEP_BACK = AnimationsRegistrySeasonal.register("knocback_step_back");
   IAnimationSource JUMP_WITH_CLAPS_CYCLE = AnimationsRegistrySeasonal.register("jump_with_claps_cycle");
   IAnimationSource FATHER_DANCE = AnimationsRegistrySeasonal.register("father_dance");
   IAnimationSource TRAIN_ROLLING = AnimationsRegistrySeasonal.register("train_rolling");
   IAnimationSource CLOCK_BREAK1 = AnimationsRegistrySeasonal.register("clock_break1");
   IAnimationSource CLOCK_BREAK2 = AnimationsRegistrySeasonal.register("clock_break2");
   IAnimationSource CLOCK_BREAK3 = AnimationsRegistrySeasonal.register("clock_break3");
   IAnimationSource BODY_GLITCHING = AnimationsRegistrySeasonal.register("body_glitching");
   IAnimationSource MEDICAL_SCENE = AnimationsRegistrySeasonal.register("medical_scene");
   IAnimationSource CUTSCENE_MEDICAL_LAYER_WALK = AnimationsRegistrySeasonal.register("cutscene_medical_layer_walk");
   IAnimationSource CUTSCENE_MEDICAL_POSE_HOLD = AnimationsRegistrySeasonal.register("cutscene_medical_pose_hold");
   IAnimationSource PROP_HEAD_HAND_SLEEP = AnimationsRegistrySeasonal.register("prop_head_hand_sleep");
   IAnimationSource BAG_SEARCH_CYCLE = AnimationsRegistrySeasonal.register("bag_hands_cycle_rummageinbag");
   IAnimationSource TABLET_HOLD_HANDS = AnimationsRegistrySeasonal.register("tablet_hold_hands");
   IAnimationSource TABLET_CHECK_HEAD_POSE = AnimationsRegistrySeasonal.register("humanoid/tablet_check_head_pose");
   IAnimationSource STAFF_IDLE = AnimationsRegistrySeasonal.register("dnd/staff_idle");
   IAnimationSource STAFF_ATTACK_WEAK = AnimationsRegistrySeasonal.register("dnd/staff_attack_weak");
   IAnimationSource STAFF_ATTACK_STRONG = AnimationsRegistrySeasonal.register("dnd/staff_attack_strong");
   IAnimationSource STAFF_ATTACK_FINAL = AnimationsRegistrySeasonal.register("dnd/staff_attack_final");
   IAnimationSource AXE_IDLE = AnimationsRegistrySeasonal.register("dnd/axe_idle");
   IAnimationSource AXE_ATTACK = AnimationsRegistrySeasonal.register("dnd/axe_attack");
   IAnimationSource AXE_ATTACK_FINAL = AnimationsRegistrySeasonal.register("dnd/axe_attack_final");
   IAnimationSource SWORD_IDLE = AnimationsRegistrySeasonal.register("dnd/sword_idle");
   IAnimationSource SWORD_ATTACK = AnimationsRegistrySeasonal.register("dnd/sword_attack");
   IAnimationSource SWORD_ATTACK_FINAL = AnimationsRegistrySeasonal.register("dnd/sword_attack_final");
   IAnimationSource TAIL_WIGGLE_LIZARDMAN = AnimationsRegistrySeasonal.register("tail_wiggle_lizaedman");
   IAnimationSource FAST_ALIEN_TYPING_CYCLE = AnimationsRegistrySeasonal.register("humanoid/fast_alien_typing_cycle");
   IAnimationSource JUMPING_SKIPPING_ROPE_CYCLE = AnimationsRegistrySeasonal.register("humanoid/jumping_skipping_rope_cycle");
   IAnimationSource SYLVIE_FEAR_POSE = AnimationsRegistrySeasonal.register("sylvie/sylvie_fear_pose");
   IAnimationSource SYLVIE_EATING_CYCLE = AnimationsRegistrySeasonal.register("sylvie/sylvie_eating_cycle");
   IAnimationSource SYLVIE_EATING_CYCLE_LAYER = AnimationsRegistrySeasonal.register("sylvie/sylvie_eating_cycle_layer");
   IAnimationSource SYLVIE_FULLKNEES_GETDOWN = AnimationsRegistrySeasonal.register("sylvie/sylvie_fullknees_getdown");
   IAnimationSource SYLVIE_OFF_ROBODOG = AnimationsRegistrySeasonal.register("sylvie/sylvie_off_robodog");
   IAnimationSource SYLVIE_PSYCHO_LAUGH = AnimationsRegistrySeasonal.register("sylvie/sylvie_psycho_laugh");
   IAnimationSource SYLVIE_HANDSONCHEST = AnimationsRegistrySeasonal.register("sylvie/sylvie_handsonchest");
   IAnimationSource SYLVIE_FAST_TYPING_CYCLE = AnimationsRegistrySeasonal.register("sylvie/sylvie_fast_typing_cycle");
   IAnimationSource SKRIMER_HANDSONCHEST_ENDANIMATED = AnimationsRegistrySeasonal.register("skrimer_handsonchest_endanimated");
   IAnimationSource FAST_HUMANOID_TYPING_CYCLE = AnimationsRegistrySeasonal.register("humanoid/fast_humanoid_typing_cycle");
   IAnimationSource SYLVIE_TAKE_LUNCHBOX = AnimationsRegistrySeasonal.register("sylvie/sylvie_take_lunchbox");
   @SpecialAnim("CYCLOP_STEVE")
   IAnimationSource EMOTION_CYCLOP_ANGRY = AnimationsRegistrySeasonal.register("cyclop/cyclop_angry");
   @SpecialAnim("CYCLOP_STEVE")
   IAnimationSource EMOTION_CYCLOP_ANGRY_HALF = AnimationsRegistrySeasonal.register("cyclop/cyclop_angry_half");
   @SpecialAnim("CYCLOP_STEVE")
   IAnimationSource EMOTION_CYCLOP_DISGUST = AnimationsRegistrySeasonal.register("cyclop/cyclop_disgust");
   @SpecialAnim("CYCLOP_STEVE")
   IAnimationSource EMOTION_CYCLOP_DISTRUST = AnimationsRegistrySeasonal.register("cyclop/cyclop_distrust");
   @SpecialAnim("CYCLOP_STEVE")
   IAnimationSource EMOTION_CYCLOP_EMBARASSMENT = AnimationsRegistrySeasonal.register("cyclop/cyclop_embarrassment");
   @SpecialAnim("CYCLOP_STEVE")
   IAnimationSource CYCLOP_EMOC_POSE_DISGUST = AnimationsRegistrySeasonal.register("cyclop/cyclop_emoc_pose_disgust");
   @SpecialAnim("CYCLOP_STEVE")
   IAnimationSource CYCLOP_EMOC_POSE_QUESTION = AnimationsRegistrySeasonal.register("cyclop/cyclop_emoc_pose_question");
   @SpecialAnim("CYCLOP_STEVE")
   IAnimationSource CYCLOP_EMOC_SUS = AnimationsRegistrySeasonal.register("cyclop/cyclop_emoc_sus");
   @SpecialAnim("CYCLOP_STEVE")
   IAnimationSource EMOTION_CYCLOP_SAD2 = AnimationsRegistrySeasonal.register("cyclop/cyclop_emotion_sad2");
   @SpecialAnim("CYCLOP_STEVE")
   IAnimationSource EMOTION_CYCLOP_EYE_ROLLING = AnimationsRegistrySeasonal.register("cyclop/cyclop_eye_rolling");
   @SpecialAnim("CYCLOP_STEVE")
   IAnimationSource EMOTION_CYCLOP_EYE_ROLLING_NEW = AnimationsRegistrySeasonal.register("cyclop/cyclop_eye_rolling_new");
   @SpecialAnim("CYCLOP_STEVE")
   IAnimationSource EMOTION_CYCLOP_FRIGHT = AnimationsRegistrySeasonal.register("cyclop/cyclop_fright");
   @SpecialAnim("CYCLOP_STEVE")
   IAnimationSource EMOTION_CYCLOP_HAPPY = AnimationsRegistrySeasonal.register("cyclop/cyclop_happy");
   @SpecialAnim("CYCLOP_STEVE")
   IAnimationSource CYCLOP_HEAD_POSE_INTEREST_LEFT = AnimationsRegistrySeasonal.register("cyclop/cyclop_head_pose_interest_left");
   @SpecialAnim("CYCLOP_STEVE")
   IAnimationSource CYCLOP_SIDE_SURPRISED_LEFT = AnimationsRegistrySeasonal.register("cyclop/cyclop_side_surprised_left");
   @SpecialAnim("CYCLOP_STEVE")
   IAnimationSource CYCLOP_SIDE_SURPRISED_RIGHT = AnimationsRegistrySeasonal.register("cyclop/cyclop_side_surprised_right");
   @SpecialAnim("CYCLOP_STEVE")
   IAnimationSource FIDGETING_LAYER_SIT_ENDANIMATED = AnimationsRegistrySeasonal.register("fidgeting_layer_sit_endanimated");
   @SpecialAnim("CYCLOP_STEVE")
   IAnimationSource EMOTION_CYCLOP_WINK = AnimationsRegistrySeasonal.register("cyclop/cyclop_wink");
   @SpecialAnim("CYCLOP_STEVE")
   IAnimationSource EMOTION_CYCLOP_GIGGLE = AnimationsRegistrySeasonal.register("cyclop/cyclop_giggle");
   IAnimationSource CUTSCENE_LOLOLOSHKA_FAINTING = AnimationsRegistrySeasonal.register("cutscene/cutscene_lololoshka_fainting");
   IAnimationSource SNORE_TURN_LEFT = AnimationsRegistrySeasonal.register("humanoid/snore_turn_left");
   IAnimationSource SNORE_TURN_RIGHT = AnimationsRegistrySeasonal.register("humanoid/snore_turn_right");
   IAnimationSource SNORE_POSE_NEW = AnimationsRegistrySeasonal.register("humanoid/snore_pose_new");
   IAnimationSource FATHER_EMOTION_CUTE = AnimationsRegistrySeasonal.register("humanoid/father_emoc_cute");
   IAnimationSource DRINK_LEFT_NEW = AnimationsRegistrySeasonal.register("placeholder_pose_drinkcup_left_new");
   IAnimationSource CITY_SNEAKING_CYCLE_NEW = AnimationsRegistrySeasonal.register("city_sneaking_cycle_new");
   IAnimationSource CUTSCENE_FATHER_CATCH_LO = AnimationsRegistrySeasonal.register("cutscene/father_catch_lo");
   IAnimationSource TAIL_RAISE_UP = AnimationsRegistrySeasonal.register("tail_raise_up");
   IAnimationSource PETTING_ROBODOG_BRUSH_CYCLE = AnimationsRegistrySeasonal.register("humanoid/petting_robodog_brush_cycle");
   IAnimationSource GIVE_PLATE = AnimationsRegistrySeasonal.register("give_plate_endaniamted");
   IAnimationSource EATING_CYCLE_CAKE = AnimationsRegistrySeasonal.register("eating_cycle_cake");
   IAnimationSource HOLD_PLATE = AnimationsRegistrySeasonal.register("humanoid/hold_plate");
   IAnimationSource ROTATE_MODEL_Z = AnimationsRegistrySeasonal.register("rotate_model_z");
   IAnimationSource SNIFF_ARMPIT = AnimationsRegistrySeasonal.register("sniff_armpit");
   IAnimationSource TWISTING_TEMPLATE_ENDANIMATED = AnimationsRegistrySeasonal.register("twisting_temple_endanimated");
   IAnimationSource BRING_HANDS_TOGETHER_FACE = AnimationsRegistrySeasonal.register("bring_hands_together_face");
   IAnimationSource VICTIM_KNOCK_CHIN = AnimationsRegistrySeasonal.register("victim_knock_chin");
   IAnimationSource HITTER_KNOCK_CHIN = AnimationsRegistrySeasonal.register("hitter_knock_chin");
   IAnimationSource JUMP_OFF_TABLE = AnimationsRegistrySeasonal.register("jump_off_table");
   IAnimationSource FEATHER_FALL_CYCLE = AnimationsRegistrySeasonal.register("feather_fall_cycle");
   IAnimationSource FEATHER_FALLED_ENDANIMATED = AnimationsRegistrySeasonal.register("feather_falled_endanimated");
   IAnimationSource NEWER_BOSS_FALLING_CYCLE = AnimationsRegistrySeasonal.register("newer_boss_falling_cycle");
   IAnimationSource CHILL_SIT_TO_LAY = AnimationsRegistrySeasonal.register("chill_sit_to_lay");
   IAnimationSource SEA_MINE_BOBBING = AnimationsRegistrySeasonal.register("sea_mine_bobbing");
   IAnimationSource HOLD_TRAY_NEW = AnimationsRegistrySeasonal.register("humanoid/hold_tray");
   IAnimationSource SAD_BLOW_CANDLE = AnimationsRegistrySeasonal.register("humanoid/sad_blow_candle");
   @SpecialAnim
   IAnimationSource FALLING_ASLEEP_READING_TWOHANDS = AnimationsRegistrySeasonal.register("humanoid/falling_asleep_reading_twohands");
   @SpecialAnim
   IAnimationSource HORSE_RUN_CYCLE = AnimationsRegistrySeasonal.register("mob/horse_run_cycle");
   IAnimationSource AWAKE_READING_TWOHANDS = AnimationsRegistrySeasonal.register("humanoid/awake_reading_twohands");
   IAnimationSource MEDIEVAL_DANCE_CYCLE = AnimationsRegistrySeasonal.register("humanoid/medieval_dance_cycle");
   @SpecialAnim
   IAnimationSource HORSE_CYCLE = AnimationsRegistrySeasonal.register("mob/horse_cycle");
   @SpecialAnim("PSYHOPOMP")
   IAnimationSource PSYCHOPOMP_ARMS_FOLDED_POSE = AnimationsRegistrySeasonal.register("psychopomp/psychopomp_arms_folded_pose");
   @SpecialAnim("PSYHOPOMP")
   IAnimationSource PSYCHOPOMP_WALK_CYCLE = AnimationsRegistrySeasonal.register("psychopomp/psychopomp_walk_cycle");
   IAnimationSource ATTACK_SWORD_DEATH_STRIKE = AnimationsRegistrySeasonal.register("humanoid/attack_sword_death_strike");
   IAnimationSource NIGHTMARE_WAKE_UP = AnimationsRegistrySeasonal.register("humanoid/nightmare_wake_up");
   IAnimationSource SHAKE_HELMET_TAKE_OFF = AnimationsRegistrySeasonal.register("humanoid/shake_helmet_take_off");
   IAnimationSource RAISE_MASK_ENDANIMATED = AnimationsRegistrySeasonal.register("humanoid/raise_mask_endanimated");
   IAnimationSource VILLIAN_LAUGH_CYCLE = AnimationsRegistrySeasonal.register("humanoid/villian_laugh_cycle");
   IAnimationSource HANDS_POSE_PRAY_TABLET = AnimationsRegistrySeasonal.register("humanoid/hands_pose_pray_tablet");
   IAnimationSource RAISE_HAND_HOLD_TABLET = AnimationsRegistrySeasonal.register("humanoid/raise_hand_hold_tablet");
   IAnimationSource DANCING_HEAD_CYCLE = AnimationsRegistrySeasonal.register("humanoid/dancing_head_cycle");
   IAnimationSource PISTOL_FAST_PREPARE_UP = AnimationsRegistrySeasonal.register("humanoid/pistol_fast_prepare_up");
   IAnimationSource POSE_WITH_TEDDY = AnimationsRegistrySeasonal.register("humanoid/pose_with_teddy");
   IAnimationSource PUT_TEDDY_ON_BLOCK = AnimationsRegistrySeasonal.register("humanoid/put_teddy_on_block");
   IAnimationSource FALL_ON_KNEES_TO_LAY = AnimationsRegistrySeasonal.register("humanoid/fall_on_knees_to_lay");
   SidedAnimationSet SIDELAY_TAKE_BEAR_TO_SIT = AnimationsRegistrySeasonal.registerSided(
      "sidelay_take_bear_to_sit_right", "sidelay_take_bear_to_sit_left", "sidelay_take_bear_to_sit"
   );
   IAnimationSource HOLD_TEDDY_ROTATE_180_POSE = AnimationsRegistrySeasonal.register("hold_teddy_rotate_180_pose");
   IAnimationSource HOLD_TEDDY_ARM_LENGTH = AnimationsRegistrySeasonal.register("hold_teddy_arm_length");
   IAnimationSource POSE_ROTATE_180 = AnimationsRegistrySeasonal.register("pose_rotate_180");
   IAnimationSource DRINKING_LAYER_POSE = AnimationsRegistrySeasonal.register("humanoid/drinking_layer_pose");
   IAnimationSource DRINKING_LAYER_ENDANIMATED = AnimationsRegistrySeasonal.register("humanoid/drinking_layer_endanimated");
   IAnimationSource TREE_CHOPPING_CYCLE = AnimationsRegistrySeasonal.register("humanoid/tree_chopping_cycle");
   IAnimationSource HARPY_EATING_FLOOR_LAYER = AnimationsRegistrySeasonal.register("humanoid/harpy_eating_floor_layer");
   IAnimationSource FULLKNEES_GROUND_LISTEN = AnimationsRegistrySeasonal.register("humanoid/fullknees_ground_listen");
   IAnimationSource FULLKNEES_LISTEN_TO_SIT = AnimationsRegistrySeasonal.register("humanoid/fullknees_listen_to_sit");
   IAnimationSource OPEN_DOOR_LOCK = AnimationsRegistrySeasonal.register("humanoid/open_door_lock");
   IAnimationSource CLIMB_OVER_FENCE_GAP = AnimationsRegistrySeasonal.register("climb_over_fence_gap");
   IAnimationSource GIVES_DRINK_LYING_HUMAN = AnimationsRegistrySeasonal.register("gives_drink_lying_human");
   IAnimationSource RECEIVED_DRINK_LYING_HUMAN = AnimationsRegistrySeasonal.register("received_drink_lying_human");
   IAnimationSource SHAKED_HAND_LYING_HUMAN = AnimationsRegistrySeasonal.register("shaked_hand_lying_human");
   IAnimationSource SHAKING_HAND_LYING_HUMAN = AnimationsRegistrySeasonal.register("shaking_hand_lying_human");
   IAnimationSource WAVING_HAND_FRONT_FACE = AnimationsRegistrySeasonal.register("waving_hand_front_face");
   IAnimationSource WAVING_RIGHT_HAND_FRONT_FACE = AnimationsRegistrySeasonal.register("waving_right_hand_front_face");
   IAnimationSource PET_CARRY_IN_AIR = AnimationsRegistrySeasonal.register("pet_carry_in_air");
   IAnimationSource JETPACK_EXPLODE_CUTSCENE_ANIMATION = AnimationsRegistrySeasonal.register("jetpack_explode_cutscene");
   IAnimationSource LIFTING_RECUMBENT_ARMPITS_RIGHT = AnimationsRegistrySeasonal.register("lifting_recumbent_armpits_right");
   IAnimationSource LIFTING_RECUMBENT_ARMPITS_LIE = AnimationsRegistrySeasonal.register("lifting_recumbent_armpits_lie");
   IAnimationSource LIFTING_RECUMBENT_ARMPITS_LEFT = AnimationsRegistrySeasonal.register("lifting_recumbent_armpits_left");
   IAnimationSource AFTER_JETPACK_EXPLOSION_POSE = AnimationsRegistrySeasonal.register("after_jetpack_explosion_pose");
   IAnimationSource MALETSKI_SIT_ON_GROUND = AnimationsRegistrySeasonal.register("maletski_sit_on_ground");
   IAnimationSource MALETSKI_ENDANIMATED_SITONGROUND = AnimationsRegistrySeasonal.register("maletski_endanimated_sitonground");
   IAnimationSource AFTER_JETPACK_EXPLOSION_POSE_FEAR = AnimationsRegistrySeasonal.register("after_jetpack_explosion_pose_fear");
   IAnimationSource JETPACK_FLY_UP_CUTSCENE = AnimationsRegistrySeasonal.register("jetpack_fly_up_cutscene");
   IAnimationSource LO_HIT_JETPACK = AnimationsRegistrySeasonal.register("lo_hit_jetpack");
   IAnimationSource LO_AGAIN_HIT_JETPACK = AnimationsRegistrySeasonal.register("lo_again_hit_jetpack");
   IAnimationSource PETE_CARRY_IN_AIR_HOLDEN = AnimationsRegistrySeasonal.register("pete_carry_in_air_holden");
   IAnimationSource PETE_CARRY_IN_AIR_LO = AnimationsRegistrySeasonal.register("pete_carry_in_air_lo");
   IAnimationSource PETE_CARRY_IN_AIR_MALETSKI = AnimationsRegistrySeasonal.register("pete_carry_in_air_maletski");
   IAnimationSource PETE_CARRY_IN_AIR_PIT = AnimationsRegistrySeasonal.register("pete_carry_in_air_pit");
   IAnimationSource JETPACK_CUSCENE_DASH = AnimationsRegistrySeasonal.register("jetpack_cuscene_dash");
   IAnimationSource NO_HEAD_POSE = AnimationsRegistrySeasonal.register("humanoid/no_head_pose");
   IAnimationSource CASUAL_LAY_TO_ELBOWS_SIT = AnimationsRegistrySeasonal.register("humanoid/casual_lay_to_elbows_sit");
   IAnimationSource ELBOWS_SIT_TO_SIT_GROUND = AnimationsRegistrySeasonal.register("humanoid/elbows_sit_to_sit_on_ground");
   IAnimationSource SWORD_ATTACK_2_SLOW = AnimationsRegistrySeasonal.register("humanoid/sword_attack_2_slow");
   IAnimationSource YES_GESTURE = AnimationsRegistrySeasonal.register("humanoid/yes_gesture");
   IAnimationSource HOLD_FLOWERS = AnimationsRegistrySeasonal.register("humanoid/hold_flowers_bunch");
   IAnimationSource FLOPPING_SIT_CHAIR = AnimationsRegistrySeasonal.register("humanoid/flopping_sit_chair");
   IAnimationSource TOOK_OFF_MASK_WORLDER_LO = AnimationsRegistrySeasonal.register("humanoid/took_off_mask_worlder_lo");
   IAnimationSource FALL_HIT_LEG_POSE = AnimationsRegistrySeasonal.register("humanoid/fall_hit_leg_pose");
   IAnimationSource TABLET_CHEST_HOLD = AnimationsRegistrySeasonal.register("humanoid/tablet_chest_hold");
   IAnimationSource TABLET_CHEST_HOLD_POSE = AnimationsRegistrySeasonal.register("humanoid/tablet_chest_hold_pose");
   IAnimationSource SCINT_SPHERE_IDLE = AnimationsRegistrySeasonal.register("scint_sphere_idle");
   IAnimationSource MURDER_LO_EGRESSAL_KILLER = AnimationsRegistrySeasonal.register("humanoid/murder_lo_egressal_killer");
   IAnimationSource MURDER_LO_EGRESSAL_VICTIM = AnimationsRegistrySeasonal.register("humanoid/murder_lo_egressal_victim");
   IAnimationSource MURDER_LO_OKETRA_KILLER = AnimationsRegistrySeasonal.register("humanoid/murder_lo_oketra_killer");
   IAnimationSource MURDER_LO_OKETRA_VICTIM = AnimationsRegistrySeasonal.register("humanoid/murder_lo_oketra_victim");
   IAnimationSource MURDER_LO_RAYA_KILLER = AnimationsRegistrySeasonal.register("humanoid/murder_lo_raya_killer");
   IAnimationSource MURDER_LO_RAYA_VICTIM = AnimationsRegistrySeasonal.register("humanoid/murder_lo_raya_victim");
   IAnimationSource BODY_POSE_SHOCKED = AnimationsRegistrySeasonal.register("humanoid/body_pose_shocked");
   IAnimationSource CHECKING_AROUND_LOOK_BASE = AnimationsRegistrySeasonal.register("humanoid/checking_around_look_base");
   IAnimationSource FAINT_FALLING_ON_TABLET = AnimationsRegistrySeasonal.register("humanoid/faint_falling_on_tablet");
   IAnimationSource WAKEUP_FAINT_FALLING_ON_TABLET = AnimationsRegistrySeasonal.register("humanoid/wakeup_faint_falling_on_tablet");
   SidedAnimationSet WAVE_HAND_FRONT_FACE_TABLE = AnimationsRegistrySeasonal.registerSided(
      "wave_hand_front_face_table_right", "wave_hand_front_face_table_left", "humanoid/wave_hand_front_face_table"
   );
   IAnimationSource SPIN_180_MODEL2 = AnimationsRegistrySeasonal.register("spin_180_model2");
   IAnimationSource CDERIK_HUG_LO = AnimationsRegistrySeasonal.register("cderik_hug_lo");
   IAnimationSource CDERIK_HUG_LO_END = AnimationsRegistrySeasonal.register("cderik_hug_lo_end");
   IAnimationSource OKETRA_SIT = AnimationsRegistrySeasonal.register("oketra_sit");
   IAnimationSource RAYA_LAY_ON_OKETRA = AnimationsRegistrySeasonal.register("raya_lay_on_oketra");
   IAnimationSource JODAH_SLEEP_COVER_WINGS = AnimationsRegistrySeasonal.register("jodah_sleep_cover_wings");
   IAnimationSource OPEN_WINGS_FROM_TOUCH = AnimationsRegistrySeasonal.register("open_wings_from_touch");
   IAnimationSource GRAB_THE_WAIST = AnimationsRegistrySeasonal.register("grab_the_waist");
   IAnimationSource FLY_OPEN_WINGS = AnimationsRegistrySeasonal.register("fly_open_wings");
   IAnimationSource SIT_LOTUS_REST_HEAD_HAND = AnimationsRegistrySeasonal.register("sit_lotus_rest_head_hand");
   IAnimationSource SHAKE_SHOULDERS_AND_WINGS = AnimationsRegistrySeasonal.register("shake_shoulders_and_wings");
   IAnimationSource DINOSAUR_PARODY = AnimationsRegistrySeasonal.register("dinosaur_parody");
   SidedAnimationSet ROTATE_HEAD2_60 = AnimationsRegistrySeasonal.registerSided("rotate_head_60_left", "rotate_head_60_right", "humanoid/head2_60");
   SidedAnimationSet BAD_STAND_ON_HANDS = AnimationsRegistrySeasonal.registerSided(
      "bad_stand_on_hands_right", "bad_stand_on_hands_left", "humanoid/bad_stand_on_hands"
   );
   SidedAnimationSet HOLD_CORKBOARD = AnimationsRegistrySeasonal.registerSided("holding_corkboard_right", "holding_corkboard_left", "humanoid/hold_corkboard");
   SidedAnimationSet PLACE_CORKBOARD = AnimationsRegistrySeasonal.registerSided("place_corkboard_right", "place_corkboard_left", "humanoid/place_corkboard");
   SidedAnimationSet SIT_ON_FENCE = AnimationsRegistrySeasonal.registerSided("sit_on_fence_right", "sit_on_fence_left", "humanoid/sit_on_fence");
   SidedAnimationSet TAKE_BOOK_SIDE = AnimationsRegistrySeasonal.registerSided("take_book_side_right", "take_book_side_left", "humanoid/take_book_side");
   SidedAnimationSet TAKE_BOOK_SIDE_SECOND = AnimationsRegistrySeasonal.registerSided(
      "take_book_side_second_right", "take_book_side_second_left", "humanoid/take_book_side_second"
   );
   SidedAnimationSet TAKE_BOOK_SIDE_THIRD = AnimationsRegistrySeasonal.registerSided(
      "take_book_side_third_right", "take_book_side_third_left", "humanoid/take_book_side_third"
   );
   SidedAnimationSet PAT_KID_ON_BED = AnimationsRegistrySeasonal.registerSided("pat_kid_on_bed_right", "pat_kid_on_bed_left", "humanoid/pat_kid_on_bed");
   SidedAnimationSet CAR_WHEELS_ROTATE = AnimationsRegistrySeasonal.registerSided("car_wheels_rotate_right", "car_wheels_rotate_left", "car/car_wheels_rotate");
   SidedAnimationSet TAPE_PATCH_UP = AnimationsRegistrySeasonal.registerSided("tape_patch_up_right", "tape_patch_up_left", "humanoid/tape_patch_up");
   SidedAnimationSet SIDE_WALL_PUNCH = AnimationsRegistrySeasonal.registerSided("side_wall_punch_right", "side_wall_punch_left", "humanoid/side_wall_punch");
   SidedAnimationSet HEADSHOT_FALL = AnimationsRegistrySeasonal.registerSided("headshot_fall_right", "headshot_fall_left", "humanoid/headshot_fall");
   SidedAnimationSet HOLD_LEG_JUMP_CYCLE = AnimationsRegistrySeasonal.registerSided(
      "hold_leg_jump_cycle_right", "hold_leg_jump_cycle_left", "humanoid/hold_leg_jump_cycle"
   );
   SidedAnimationSet HEAD_SLIGHT_SIDE = AnimationsRegistrySeasonal.registerSided("head_slight_side_right", "head_slight_side_left", "humanoid/head_slight_side");
   SidedAnimationSet SHOW_AND_HAND_LOOK = AnimationsRegistrySeasonal.registerSided(
      "show_and_hand_look_right", "show_and_hand_look_left", "humanoid/show_and_hand_look"
   );
   SidedAnimationSet WAVING_NO = AnimationsRegistrySeasonal.registerSided("waving_no_right", "waving_no_left", "humanoid/waving_no");
   SidedAnimationSet FULLKNEES_COPTER_MAINTENANCE = AnimationsRegistrySeasonal.registerSided(
      "fullknees_copter_maintenance_right", "fullknees_copter_maintenance_left", "humanoid/fullknees_copter_maintenance"
   );
   SidedAnimationSet FULLKNEES_COPTER_MAINTENANCE_BREAK = AnimationsRegistrySeasonal.registerSided(
      "fullknees_copter_maintenance_break_right", "fullknees_copter_maintenance_break_left", "humanoid/fullknees_copter_maintenance_break"
   );
   SidedAnimationSet PLACEHOLDER_2DESIZE = AnimationsRegistrySeasonal.registerSided(
      "placeholder_2desize_right", "placeholder_2desize_left", "placeholder_2desize"
   );
   SidedAnimationSet DANCE_CALL = AnimationsRegistrySeasonal.registerSided("dance_call_right", "dance_call_left", "humanoid/dance_call");
   SidedAnimationSet DANCE_REPLY = AnimationsRegistrySeasonal.registerSided("dance_reply_right", "dance_reply_left", "humanoid/dance_reply");
   SidedAnimationSet SHORT_SHOULDER_CLAP = AnimationsRegistrySeasonal.registerSided(
      "short_shoulder_clap_right", "short_shoulder_clap_left", "humanoid/short_shoulder_clap"
   );
   SidedAnimationSet HOLD_TABLET_POSE = AnimationsRegistrySeasonal.registerSided("hold_tablet_right", "hold_tablet_left", "humanoid/hold_tablet_pose");
   SidedAnimationSet PULL_OUT = AnimationsRegistrySeasonal.registerSided("pull_out_right", "pull_out_left", "humanoid/pull_out");
   SidedAnimationSet PASS_ITEM = AnimationsRegistrySeasonal.registerSided("pass_item_right", "pass_item_left", "humanoid/pass_item");
   SidedAnimationSet PASS_ITEM_2 = AnimationsRegistrySeasonal.registerSided("pass_item_right", "pass_item_left", "humanoid/pass_item_2");
   SidedAnimationSet GRAB_INNER_POCKET = AnimationsRegistrySeasonal.registerSided(
      "grab_inner_pocket_right", "grab_inner_pocket_left", "humanoid/grab_inner_pocket"
   );
   SidedAnimationSet EMOTION_WINK_NEW = AnimationsRegistrySeasonal.registerSided("wink_right", "wink_left", "humanoid/wink");
   SidedAnimationSet SHOW_ON_YOURSELF = AnimationsRegistrySeasonal.registerSided("show_on_yourself_right", "show_on_yourself_left", "humanoid/show_on_yourself");
   SidedAnimationSet HANDSHAKE_TALL = AnimationsRegistrySeasonal.registerSided("handshake_tall_right", "handshake_tall_left", "humanoid/handshake_tall");
   SidedAnimationSet HANDSHAKE_SHORT = AnimationsRegistrySeasonal.registerSided("handshake_short_right", "handshake_short_left", "humanoid/handshake_short");
   SidedAnimationSet AIM_ONE_EYE = AnimationsRegistrySeasonal.registerSided("aim_one_eye_right", "aim_one_eye_left", "humanoid/aim_one_eye");
   SidedAnimationSet HOLD_OUT_AFTER_PULL_OUT = AnimationsRegistrySeasonal.registerSided(
      "hold_out_after_pull_out_right", "hold_out_after_pull_out_left", "humanoid/hold_out_after_pull_out"
   );
   SidedAnimationSet POUR_BOTTLE_CUP = AnimationsRegistrySeasonal.registerSided(
      "pour_bottle_to_cup_right", "pour_bottle_to_cup_left", "humanoid/pour_bottle_to_cup"
   );
   SidedAnimationSet HOLD_DAMAGED_HAND = AnimationsRegistrySeasonal.registerSided(
      "hold_damaged_hand_right", "hold_damaged_hand_left", "humanoid/hold_damaged_hand"
   );
   SidedAnimationSet FALL_ON_BED = AnimationsRegistrySeasonal.registerSided("fall_on_block_right", "fall_on_block_left", "humanoid/fall_on_block");
   SidedAnimationSet MOVE_PLACEHOLDER_X = AnimationsRegistrySeasonal.registerSided("move_placeholder_right_x", "move_placeholder_left_x", "move_placeholder_x");
   SidedAnimationSet MOVE_PLACEHOLDER_Y = AnimationsRegistrySeasonal.registerSided("move_placeholder_right_y", "move_placeholder_left_y", "move_placeholder_y");
   SidedAnimationSet MOVE_PLACEHOLDER_Z = AnimationsRegistrySeasonal.registerSided("move_placeholder_right_z", "move_placeholder_left_z", "move_placeholder_z");
   SidedAnimationSet FINGER_SNAP = AnimationsRegistrySeasonal.registerSided("finger_snap_right", "finger_snap_left", "humanoid/finger_snap");
   SidedAnimationSet ARM_MOVE_UPDOWN = AnimationsRegistrySeasonal.registerSided("arm_move_updown_right", "arm_move_updown_left", "humanoid/arm_move_updown");
   SidedAnimationSet SHAKE_FINGER = AnimationsRegistrySeasonal.registerSided("shake_finger_right", "shake_finger_left", "humanoid/shake_finger");
   SidedAnimationSet SHOW_POINTER = AnimationsRegistrySeasonal.registerSided("show_pointer_right", "show_pointer_left", "humanoid/show_pointer");
   SidedAnimationSet TENTACLE_PONDERING = AnimationsRegistrySeasonal.registerSidedSeparate(
      "humanoid/tentacle_pondering_right", "humanoid/tentacle_pondering_left"
   );
   SidedAnimationSet SHOCKED_SIDE_LOOK = AnimationsRegistrySeasonal.registerSided(
      "shocked_side_look_right", "shocked_side_look_left", "humanoid/shocked_side_look"
   );
   SidedAnimationSet MUSCLE_SHOW = AnimationsRegistrySeasonal.registerSided("muscle_show_right", "muscle_show_left", "humanoid/muscle_show");
   SidedAnimationSet WIGGLE_HAND = AnimationsRegistrySeasonal.registerSided("wiggle_hand_right", "wiggle_hand_left", "humanoid/wiggle_hand");
   SidedAnimationSet HAND_TO_FOREHEAD = AnimationsRegistrySeasonal.registerSided("hand_to_forehead_right", "hand_to_forehead_left", "humanoid/hand_to_forehead");
   SidedAnimationSet BEND_GIVE = AnimationsRegistrySeasonal.registerSided("bend_give_right", "bend_give_left", "humanoid/bend_give");
   SidedAnimationSet PUT_HAND_TO_FACE = AnimationsRegistrySeasonal.registerSided("put_hand_to_face_right", "put_hand_to_face_left", "humanoid/put_hand_to_face");
   SidedAnimationSet SPILLING_OUT_SIDE = AnimationsRegistrySeasonal.registerSided(
      "spilling_out_side_right", "spilling_out_side_left", "humanoid/spilling_out_side"
   );
   SidedAnimationSet LEAN_SIDE_TO_WALL = AnimationsRegistrySeasonal.registerSided(
      "lean_side_to_wall_right", "lean_side_to_wall_left", "humanoid/lean_side_to_wall"
   );
   SidedAnimationSet EMOTION_OFFENDED = AnimationsRegistrySeasonal.registerSided("emotion_offended_right", "emotion_offended_left", "humanoid/emotion_offended");
   SidedAnimationSet SIT_IN_CAR = AnimationsRegistrySeasonal.registerSided("sit_in_car_right", "sit_in_car_left", "humanoid/sit_in_car");
   SidedAnimationSet SIDELAY_TAKE_BEAR = AnimationsRegistrySeasonal.registerSided(
      "sidelay_take_bear_right", "sidelay_take_bear_left", "humanoid/sidelay_take_bear"
   );
   SidedAnimationSet SIDELAY_BUTTSCRATCH = AnimationsRegistrySeasonal.registerSided(
      "sidelay_buttscratch_right", "sidelay_buttscratch_left", "humanoid/sidelay_buttscratch"
   );
   SidedAnimationSet SIDELAY_GOAWAY = AnimationsRegistrySeasonal.registerSided("sidelay_goaway_right", "sidelay_goaway_left", "humanoid/sidelay_goaway");
   SidedAnimationSet CARDS_HOLD = AnimationsRegistrySeasonal.registerSidedSeparate("humanoid/cards_hold_right", "humanoid/cards_hold_left");
   SidedAnimationSet PICK_AND_CARRY_HELMET = AnimationsRegistrySeasonal.registerSided(
      "pick_and_carry_helmet_right", "pick_and_carry_helmet_left", "humanoid/pick_and_carry_helmet"
   );
   SidedAnimationSet CARRIED_HELMET_TO_POCKET = AnimationsRegistrySeasonal.registerSided(
      "carried_helmet_to_pocket_right", "carried_helmet_to_pocket_left", "humanoid/carried_helmet_to_pocket"
   );
   SidedAnimationSet RISE_FIST_TO_SIDE_HAND = AnimationsRegistrySeasonal.registerSided(
      "rise_fist_to_side_hand_right", "rise_fist_to_side_hand_left", "humanoid/rise_fist_to_side_hand"
   );
   SidedAnimationSet SLOW_RISE_FIST = AnimationsRegistrySeasonal.registerSided("slow_rise_fist_right", "slow_rise_fist_left", "humanoid/slow_rise_fist");
   SidedAnimationSet WIPE_MOUTH_ONCE = AnimationsRegistrySeasonal.registerSided("wipe_mouth_once_right", "wipe_mouth_once_left", "humanoid/wipe_mouth_once");
   SidedAnimationSet HELMET_TAKE_OFF = AnimationsRegistrySeasonal.registerSided("helmet_take_off_right", "helmet_take_off_left", "humanoid/helmet_take_off");
   SidedAnimationSet LAY_WAVING_HAND_LAYER = AnimationsRegistrySeasonal.registerSided(
      "lay_waving_hand_layer_right", "lay_waving_hand_layer_left", "humanoid/lay_waving_hand_layer"
   );
   SidedAnimationSet HOLD_TABLET_VERTICAL_POSE = AnimationsRegistrySeasonal.registerSided(
      "hold_tablet_vertical_right", "hold_tablet_vertical_left", "humanoid/hold_tablet_vertical_pose"
   );
   SidedAnimationSet BELLY_LAY_ARM = AnimationsRegistrySeasonal.registerSided("belly_lay_arm_right", "belly_lay_arm_left", "humanoid/belly_lay_arm");
   SidedAnimationSet BELLY_LAY_ARM_POINT = AnimationsRegistrySeasonal.registerSided(
      "belly_lay_arm_point_layer_right", "belly_lay_arm_point_layer_left", "humanoid/belly_lay_arm_point_layer"
   );
   SidedAnimationSet BIG_FINGER_UP = AnimationsRegistrySeasonal.registerSided("finger_up_right", "finger_up_left", "humanoid/finger_up");
   SidedAnimationSet TOAST_CUP = AnimationsRegistrySeasonal.registerSided("toast_cup_right", "toast_cup_left", "humanoid/toast_cup");
   SidedAnimationSet HOLD_CUP = AnimationsRegistrySeasonal.registerSided("hold_cup_right", "hold_cup_left", "humanoid/hold_cup");
   SidedAnimationSet DRINK_CUP = AnimationsRegistrySeasonal.registerSided("drink_cup_right", "drink_cup_left", "humanoid/drink_cup");
   SidedAnimationSet BASKET_HOLD = AnimationsRegistrySeasonal.registerSided("basket_hold_right", "basket_hold_left", "humanoid/basket_hold");
   SidedAnimationSet TROW_BALL = AnimationsRegistrySeasonal.registerSided("trow_ball_right", "trow_ball_left", "humanoid/trow_ball");
   SidedAnimationSet STAND_ON_HANDS_POSE_WAVING_HAND = AnimationsRegistrySeasonal.registerSided(
      "stand_on_hands_pose_waving_hand_right", "stand_on_hands_pose_waving_hand_left", "humanoid/stand_on_hands_pose_waving_hand"
   );
   SidedAnimationSet HUG_FROM_BEHIND_SIDE = AnimationsRegistrySeasonal.registerSided(
      "hug_frog_behind_side_right", "hug_frog_behind_side_left", "humanoid/hug_frog_behind_side"
   );
   SidedAnimationSet STAND_ON_ONE_HAND = AnimationsRegistrySeasonal.registerSided(
      "stand_on_one_hand_right", "stand_on_one_hand_left", "humanoid/stand_on_one_hand"
   );
   SidedAnimationSet FALL_FROM_ONE_HAND = AnimationsRegistrySeasonal.registerSided(
      "fall_from_one_hand_right", "fall_from_one_hand_left", "humanoid/fall_from_one_hand"
   );
   SidedAnimationSet HAIRFLUFF_TALL = AnimationsRegistrySeasonal.registerSided("hairfluff_tall_right", "hairfluff_tall_left", "humanoid/hairfluff_tall");
   SidedAnimationSet POLICE_PISTOL_BLOW = AnimationsRegistrySeasonal.registerSided(
      "police_pistol_blow_right", "police_pistol_blow_left", "humanoid/police_pistol_blow"
   );
   SidedAnimationSet HANDS_BRUSHOFF_BACK = AnimationsRegistrySeasonal.registerSided(
      "hands_brushoff_back_right", "hands_brushoff_back_left", "humanoid/hands_brushoff_back"
   );
   SidedAnimationSet BRUSH_OFF_SHOULDER = AnimationsRegistrySeasonal.registerSided(
      "brush_off_shoulder_right", "brush_off_shoulder_left", "humanoid/brush_off_shoulder"
   );
   SidedAnimationSet FALL_ON_BLOCK_FACE = AnimationsRegistrySeasonal.registerSided(
      "fall_on_block_face_right", "fall_on_block_face_left", "humanoid/fall_on_block_face"
   );
   SidedAnimationSet FALL_ON_BED_FACE = AnimationsRegistrySeasonal.registerSided("fall_on_bed_face_right", "fall_on_bed_face_left", "humanoid/fall_on_bed_face");
   SidedAnimationSet FALL_FACE_TO_SIT = AnimationsRegistrySeasonal.registerSided("fall_face_to_sit_right", "fall_face_to_sit_left", "humanoid/fall_face_to_sit");
   SidedAnimationSet FALL_FACE_TO_SIT_HOVER = AnimationsRegistrySeasonal.registerSided(
      "fall_face_to_sit_hover_right", "fall_face_to_sit_hover_left", "humanoid/fall_face_to_sit_hover"
   );
   SidedAnimationSet THRONE_SIT_ARMS_LAY = AnimationsRegistrySeasonal.registerSided(
      "throne_sit_arms_lay_right", "throne_sit_arms_lay_left", "throne_sit_arms_lay"
   );
   SidedAnimationSet BASKET_PUT = AnimationsRegistrySeasonal.registerSided("basket_put_right", "basket_put_left", "humanoid/basket_put");
   SidedAnimationSet EAT_TASTY = AnimationsRegistrySeasonal.registerSided("eat_tasty_right", "eat_tasty_left", "humanoid/eat_tasty");
   SidedAnimationSet TAKE_FROM_BASKET_SITTING = AnimationsRegistrySeasonal.registerSided(
      "take_from_basket_sitting_right", "take_from_basket_sitting_left", "humanoid/take_from_basket_sitting"
   );
   SidedAnimationSet WAVE_HAND = AnimationsRegistrySeasonal.registerSided("wave_hand_right", "wave_hand_left", "humanoid/wave_hand");
   SidedAnimationSet FUNKY_LAY_NAHD_LOWERED = AnimationsRegistrySeasonal.registerSided(
      "funky_lay_hand_lowered_right", "funky_lay_hand_lowered_left", "funky_lay_hand_lowered"
   );
   SidedAnimationSet SEE_SIDE_ENDANIMETED = AnimationsRegistrySeasonal.registerSided(
      "see_right_endanimated", "see_left_endanimated", "humanoid/see_side_endanimated"
   );
   SidedAnimationSet HIT_FROM_SIDE = AnimationsRegistrySeasonal.registerSided("hit_from_side_right", "hit_from_side_left", "humanoid/hit_from_side");
   SidedAnimationSet SIT_PUNCH_GROUND = AnimationsRegistrySeasonal.registerSided("sit_punch_ground_right", "sit_punch_ground_left", "humanoid/sit_punch_ground");
   SidedAnimationSet MOVE_POINTED_ARM = AnimationsRegistrySeasonal.registerSided("move_pointed_arm_right", "move_pointed_arm_left", "humanoid/move_pointed_arm");
   SidedAnimationSet POINTED_ARM_MOVED = AnimationsRegistrySeasonal.registerSided(
      "pointed_arm_moved_right", "pointed_arm_moved_left", "humanoid/pointed_arm_moved"
   );
   SidedAnimationSet FATHER_CRUSHING_SCINT = AnimationsRegistrySeasonal.registerSided(
      "father_crushing_scint_right", "father_crushing_scint_left", "humanoid/father_crushing_scint"
   );
   SidedAnimationSet EXPLAINING_BASE_ARMS = AnimationsRegistrySeasonal.registerSided(
      "explaining_base_arms_right", "explaining_base_arms_left", "humanoid/explaining_base_arms"
   );
   SidedAnimationSet VINEGLASS_CIRCLE_AROUND = AnimationsRegistrySeasonal.registerSided(
      "vineglass_circle_around_right", "vineglass_circle_around_left", "humanoid/vineglass_circle_around"
   );
   SidedAnimationSet SIT_THINKING_LAYER = AnimationsRegistrySeasonal.registerSided(
      "sit_thinking_layer_right", "sit_thinking_layer_left", "humanoid/sit_thinking_layer"
   );
   SidedAnimationSet BOOK_POINT = AnimationsRegistrySeasonal.registerSided("book_point_right", "book_point_left", "humanoid/book_point");
   SidedAnimationSet HOLD_MOP_POSE = AnimationsRegistrySeasonal.registerSided("hold_mop_pose_right", "hold_mop_pose_left", "humanoid/hold_mop_pose");
   SidedAnimationSet SHAKING_SOMEONE_ONE_ARM = AnimationsRegistrySeasonal.registerSided(
      "shaking_someone_one_arm_right", "shaking_someone_one_arm_left", "humanoid/shaking_someone_one_arm"
   );
   SidedAnimationSet WRITES_THOUGHTFULLY_BOOK = AnimationsRegistrySeasonal.registerSided(
      "writes_thoughtfully_book_right", "writes_thoughtfully_book_left", "humanoid/writes_thoughtfully_book"
   );
   SidedAnimationSet BOTTLE_HEAR_AND_THROW = AnimationsRegistrySeasonal.registerSided(
      "bottle_hear_and_throw_right", "bottle_hear_and_throw_left", "humanoid/bottle_hear_and_throw"
   );
   SidedAnimationSet PLACEHOLDER_2DE_SIZE_Z = AnimationsRegistrySeasonal.registerSided(
      "placeholder_2de_size_z_right", "placeholder_2de_size_z_left", "placeholder_2de_size_z"
   );
   SidedAnimationSet HOLD_GOOSE_POSE = AnimationsRegistrySeasonal.registerSided("hold_goose_pose_right", "hold_goose_pose_left", "humanoid/hold_goose_pose");
   SidedAnimationSet HIDE_GOOSE_UNDER_BED = AnimationsRegistrySeasonal.registerSided(
      "hide_goose_under_bed_right", "hide_goose_under_bed_left", "humanoid/hide_goose_under_bed"
   );
   @SpecialAnim("STELLA")
   SidedAnimationSet STELLA_TRUNK_SIT = AnimationsRegistrySeasonal.registerSided("stella_trunk_sit_right", "stella_trunk_sit_left", "stella_trunk_sit");
   @SpecialAnim("STELLA")
   SidedAnimationSet STELLA_SLANT_TO_SID = AnimationsRegistrySeasonal.registerSided(
      "stella_slant_to_side_right", "stella_slant_to_side_left", "stella/stella_slant_to_side"
   );
   SidedAnimationSet GESTURE_ITALIAN_CALM_ONE_HAND = AnimationsRegistrySeasonal.registerSided(
      "gesture_italian_calm_right", "gesture_italian_calm_left", "humanoid/gesture_italian_calm_one_hand"
   );
   SidedAnimationSet PROP_HEAD_HAND_POSE = AnimationsRegistrySeasonal.registerSided(
      "prop_head_hand_right", "prop_head_hand_left", "humanoid/prop_head_hand_pose"
   );
   SidedAnimationSet DRUNK_CYCLE_HAND = AnimationsRegistrySeasonal.registerSided("drunk_cycle_right", "drunk_cycle_left", "drunk_cycle_hand");
   SidedAnimationSet TADAM_POSE_NEW = AnimationsRegistrySeasonal.registerSided("tadam_pose_new_right", "tadam_pose_new_left", "humanoid/tadam_pose_new");
   SidedAnimationSet HUGGING_FROM_SIDE_POSE = AnimationsRegistrySeasonal.registerSided(
      "hugging_from_side_right", "hugging_from_side_left", "hugging_from_side_pose"
   );
   SidedAnimationSet PAPER_HOLD_NEW = AnimationsRegistrySeasonal.registerSided("paper_hold_right", "paper_hold_left", "humanoid/paper_hold_new");
   SidedAnimationSet HANDS_BRUSHOFF_BACK_THREE = AnimationsRegistrySeasonal.registerSided(
      "hands_brushoff_back_three_right", "hands_brushoff_back_three_left", "humanoid/hands_brushoff_back_three"
   );
   SidedAnimationSet SYLVIE_HUG = AnimationsRegistrySeasonal.registerSided("sylvie_hug_right", "sylvie_hug_left", "sylvie_hug");
   SidedAnimationSet SQUINT_OVER_GLASSES = AnimationsRegistrySeasonal.registerSided(
      "squint_over_glasses_right", "squint_over_glasses_left", "humanoid/squint_over_glasses"
   );
   SidedAnimationSet HOLD_CHICKEN_UNDER_ARMPIT = AnimationsRegistrySeasonal.registerSided(
      "hold_chicken_under_armpit_right", "hold_chicken_under_armpit_left", "humanoid/hold_chicken_under_armpit"
   );
   SidedAnimationSet HOLD_CHICKEN_ARMPIT_PET_START = AnimationsRegistrySeasonal.registerSided(
      "hold_chicken_armpit_pet_start_right", "hold_chicken_armpit_pet_start_left", "humanoid/hold_chicken_armpit_pet_start"
   );
   SidedAnimationSet HOLD_CHICKEN_ARMPIT_PET_CYCLE = AnimationsRegistrySeasonal.registerSided(
      "hold_chicken_armpit_pet_cycle_right", "hold_chicken_armpit_pet_cycle_left", "humanoid/hold_chicken_armpit_pet_cycle"
   );
   SidedAnimationSet JODAH_500_EYES_CLOSE = AnimationsRegistrySeasonal.registerSidedSeparate("jdh_eyes_closed_right", "jdh_eyes_closed_left");
   SidedAnimationSet BODY_HILL_ENDANIMATED = AnimationsRegistrySeasonal.registerSided(
      "body_endanimated_hill_right", "body_endanimated_hill_left", "body_endanimated_hill"
   );
   IAnimationSource HORSE_WALK = AnimationsRegistrySeasonal.register("mob/horse_walk_cycle");
   IAnimationSource ELEGANT_SIT = AnimationsRegistrySeasonal.register("humanoid/elegant_sit");
   IAnimationSource CAR_HELMET_TAKE_OFF = AnimationsRegistrySeasonal.register("car_helmet_take_off");
   IAnimationSource NO_MODEL = AnimationsRegistrySeasonal.register("no_model");
   IAnimationSource DEEP_BREATH_DOWN = AnimationsRegistrySeasonal.register("deep_breath_down");
   IAnimationSource DEEP_BREATH_UP = AnimationsRegistrySeasonal.register("deep_breath_up");
   IAnimationSource SHAKED_WALL = AnimationsRegistrySeasonal.register("shaked_wall");
   IAnimationSource SHAKING_SOMEONE_WALL = AnimationsRegistrySeasonal.register("shaking_someone_wall");
   IAnimationSource STANDUP_WALLDEATH = AnimationsRegistrySeasonal.register("standup_walldeath");
   IAnimationSource WAKEUP_WALLDEATH = AnimationsRegistrySeasonal.register("wakeup_walldeath");
   IAnimationSource PEEK_HANGING = AnimationsRegistrySeasonal.register("peek_hanging");
   IAnimationSource EYEBROWS_CALL_GESTURE = AnimationsRegistrySeasonal.register("eyebrows_call_gesture");
   IAnimationSource HEEL_TOES_ROLLING = AnimationsRegistrySeasonal.register("heel_toes_rolling");
   IAnimationSource SWITCHING_LEGS = AnimationsRegistrySeasonal.register("switching_legs");
   IAnimationSource PISTOL_THREAT = AnimationsRegistrySeasonal.register("pistol_threat");
   IAnimationSource STEP_ASIDE_ENDANIMATED = AnimationsRegistrySeasonal.register("step_aside_endanimated");
   IAnimationSource UNSURE_WALK_CYCLE = AnimationsRegistrySeasonal.register("humanoid/unsure_walk_cycle");
   IAnimationSource HOLDING_ELBOW_START = AnimationsRegistrySeasonal.register("humanoid/holding_elbow_start");
   IAnimationSource HOLDING_ELBOW_END = AnimationsRegistrySeasonal.register("humanoid/holding_elbow_end");
   IAnimationSource HOLDED_EBLOW_POSE = AnimationsRegistrySeasonal.register("humanoid/holded_eblow_pose");
   IAnimationSource HOLDED_ELBOW_RUN_POSE = AnimationsRegistrySeasonal.register("humanoid/holded_elbow_run_pose");
   IAnimationSource HOLDED_EBLOW_BREAKS_OUT = AnimationsRegistrySeasonal.register("humanoid/holded_eblow_breaks_out");
   IAnimationSource BACK_DEAD_POSE = AnimationsRegistrySeasonal.register("humanoid/back_dead_pose");
   IAnimationSource FALL_AFTER_STEPS = AnimationsRegistrySeasonal.register("humanoid/fall_after_steps");
   IAnimationSource TABLET_CHECK_CYCLE = AnimationsRegistrySeasonal.register("humanoid/tablet_check_cycle");
   IAnimationSource LEAN_ON_ARMS_FROM_SIT = AnimationsRegistrySeasonal.register("humanoid/lean_on_arms_from_sit");
   IAnimationSource ON_KNEES_LOOKING_DOWN = AnimationsRegistrySeasonal.register("humanoid/on_knees_looking_down");
   IAnimationSource ON_KNEES_SWAYING_CYCLE = AnimationsRegistrySeasonal.register("humanoid/on_knees_swaying_cycle");
   IAnimationSource FROM_KNEES_TO_SIT = AnimationsRegistrySeasonal.register("humanoid/from_knees_to_sit");
   IAnimationSource COVER_EYES_FROM_LEAN_ON_ARMS = AnimationsRegistrySeasonal.register("humanoid/cover_eyes_from_lean_on_arms");
   IAnimationSource SIT_COVER_EARS_FROM_COVER_EYES = AnimationsRegistrySeasonal.register("humanoid/sit_cover_ears_from_cover_eyes");
   IAnimationSource TABLET_HOLD_POSE = AnimationsRegistrySeasonal.register("humanoid/tablet_hold_pose");
   IAnimationSource HEAD_UP_FROM_LEAN_ON_ARMS = AnimationsRegistrySeasonal.register("humanoid/head_up_from_lean_on_arms");
   IAnimationSource LOOK_AROUND_ON_ARMS_LAYERED = AnimationsRegistrySeasonal.register("humanoid/look_around_on_arms_layered");
   IAnimationSource LEAN_ON_ARMS_FROM_SIT_OPEN_EYES = AnimationsRegistrySeasonal.register("humanoid/lean_on_arms_from_sit_open_eyes");
   IAnimationSource ROTATE_MODEL_90 = AnimationsRegistrySeasonal.register("rotate_model_90");
   IAnimationSource BLINKING_HETINTEP_CYCLE = AnimationsRegistrySeasonal.register("humanoid/blinking_hetintep_cycle");
   IAnimationSource ARM_TO_HEART = AnimationsRegistrySeasonal.register("humanoid/arm_to_heart");
   IAnimationSource SQUATTING_PET_INFRONT = AnimationsRegistrySeasonal.register("humanoid/squatting1_pet_infront");
   IAnimationSource HAZMAT_HUG_TO_CHILD = AnimationsRegistrySeasonal.register("humanoid/hazmat_hug_to_child");
   IAnimationSource LOLOLOSHKA_GOOSE_HOLD_POSE = AnimationsRegistrySeasonal.register("humanoid/lololoshka_goose_hold_pose");
   IAnimationSource GOOSE_RUN_FROM_HOLD = AnimationsRegistrySeasonal.register("humanoid/lololoshka_goose_run_from_hold");
   IAnimationSource HAIR_FIX_BOTH_ARMS = AnimationsRegistrySeasonal.register("humanoid/hair_fix_both_arms");
   IAnimationSource CLOSE_EYES_HANDS = AnimationsRegistrySeasonal.register("humanoid/close_eyes_hands");
   IAnimationSource HANDS_CYCLE_WRITING_LAY_ONE = AnimationsRegistrySeasonal.register("humanoid/hands_cycle_writing_lay1");
   IAnimationSource HANDS_CYCLE_WRITING_LAY_TWO = AnimationsRegistrySeasonal.register("humanoid/hands_cycle_writing_lay2");
   IAnimationSource TABLET_GAMING_CYCLE = AnimationsRegistrySeasonal.register("humanoid/tablet_gaming_cycle");
   IAnimationSource TABLET_GAMING_CYCLE_ONLY_HANDS = AnimationsRegistrySeasonal.register("humanoid/tablet_gaming_cycle_only_hands");
   IAnimationSource TABLET_GAMING_CYCLE_ONLY_HANDS_STEVE = AnimationsRegistrySeasonal.register("humanoid/tablet_gaming_cycle_only_hands_steve");
   IAnimationSource HUMAN_OPEN_HANDS_GARDGE = AnimationsRegistrySeasonal.register("humanoid/human_open_hands_gardge");
   IAnimationSource JOJO_POSE_ENDANIMATED = AnimationsRegistrySeasonal.register("humanoid/jojo_pose_endanimated");
   IAnimationSource FANCY_WIDE_WALK = AnimationsRegistrySeasonal.register("fancy_wide_walk");
   IAnimationSource ROTATE_SOMEBODY = AnimationsRegistrySeasonal.register("humanoid/rotate_somebody");
   IAnimationSource NO_LEGS_CRAWL_POSE = AnimationsRegistrySeasonal.register("humanoid/no_legs_crawl_pose");
   IAnimationSource NO_LEGS_CRAWL_STOPPING = AnimationsRegistrySeasonal.register("humanoid/no_legs_crawl_stopping");
   IAnimationSource CRAWL_LAY_CYCLE = AnimationsRegistrySeasonal.register("humanoid/crawl_lay_cycle");
   IAnimationSource CRAWL_LAY_CYCLE2 = AnimationsRegistrySeasonal.register("humanoid/crawl_lay_cycle2");
   IAnimationSource RUN_PREPARE_STAND = AnimationsRegistrySeasonal.register("humanoid/run_prepare_stand");
   IAnimationSource RUN_PREPARE_START = AnimationsRegistrySeasonal.register("humanoid/run_prepare_start");
   IAnimationSource THROW_CRAWL_LAYER = AnimationsRegistrySeasonal.register("humanoid/throw_crawl_layer");
   IAnimationSource GET_FROM_SIDE_CRAWL_POSE_LAYER = AnimationsRegistrySeasonal.register("humanoid/get_from_side_crawl_pose_layer");
   IAnimationSource PISTOL_PREPARE_DOWN = AnimationsRegistrySeasonal.register("humanoid/pistol_prepare_down");
   IAnimationSource COVER_YOURSELF_HANDS_ENDANIMATED = AnimationsRegistrySeasonal.register("humanoid/cover_yourself_hands_endanimated");
   IAnimationSource RELEASE_WALK = AnimationsRegistrySeasonal.register("humanoid/release_walk");
   IAnimationSource PULLING_WALK = AnimationsRegistrySeasonal.register("humanoid/pulling_walk");
   IAnimationSource LANDING_TO_RING = AnimationsRegistrySeasonal.register("humanoid/landing_to_ring");
   IAnimationSource THROW_TO_RING = AnimationsRegistrySeasonal.register("humanoid/throw_to_ring");
   IAnimationSource WAVE_ANGEL_CYCLE = AnimationsRegistrySeasonal.register("humanoid/wave_angel_cycle");
   IAnimationSource ROBO_DOG_IDLE = AnimationsRegistrySeasonal.register("robo_dog_idle");
   IAnimationSource TIE_ADJUST = AnimationsRegistrySeasonal.register("tie_adjust");
   IAnimationSource ADOLF_SHAKE_BAG_CYCLE = AnimationsRegistrySeasonal.register("adolf/shake_bag_cycle");
   IAnimationSource ADOLF_TAKE_BAG_FROM_HIP = AnimationsRegistrySeasonal.register("adolf/take_bag_from_hip");
   IAnimationSource HAZMAT_SHAKE_AND_GIVE = AnimationsRegistrySeasonal.register("hazmat_shake_and_give");
   IAnimationSource SIDELAY_TAKE_BEAR_RIGHT = AnimationsRegistrySeasonal.register("sidelay_take_bear_right");
   IAnimationSource BODY2_POSE_BOW = AnimationsRegistrySeasonal.register("body2_pose_bow");
   IAnimationSource HUMANOID2_IDLE = AnimationsRegistrySeasonal.register("model2_idle1_cycle");
   IAnimationSource CROUCH_IN_CAR = AnimationsRegistrySeasonal.register("crouch_in_car");
   IAnimationSource ROLL_FROM_STOMACH_ROTATED = AnimationsRegistrySeasonal.register("roll_from_stomach_rotated");
   IAnimationSource BODY_POSE_LYINGONSIDE_RIGHT_ROTATED = AnimationsRegistrySeasonal.register("body_pose_lyingonside_right_rotated");
   SidedAnimationSet LEG_ARM_WIPE = AnimationsRegistrySeasonal.registerSided("leg_arm_wipe_right", "leg_arm_wipe_left", "leg_arm_wipe");
   SidedAnimationSet LAUGH_CLOSE_MOUTH = AnimationsRegistrySeasonal.registerSided("laugh_close_mouth_right", "laugh_close_mouth_left", "laugh_close_mouth");
   SidedAnimationSet PUSHUP_ONE_HAND_CYCLE = AnimationsRegistrySeasonal.registerSided(
      "pushup_one_hand_cycle_right", "pushup_one_hand_cycle_left", "humanoid/pushup_one_hand_cycle"
   );
   SidedAnimationSet PUSHUP_PREARE_ONE_HAND = AnimationsRegistrySeasonal.registerSided(
      "pushup_prepare_one_hand_right", "pushup_prepare_one_hand_left", "humanoid/pushup_prepare_one_hand"
   );
   IAnimationSource BODY_DISSOLVE = AnimationsRegistrySeasonal.register("body_dissolve");
   IAnimationSource POSE_STRESSFUL = AnimationsRegistrySeasonal.register("pose_stressful");
   IAnimationSource ANGRY_TAPPING_BUTTON_CYCLE = AnimationsRegistrySeasonal.register("humanoid/angry_tapping_button_cycle");
   IAnimationSource DRONE_SCINT_CUTSCENE = AnimationsRegistrySeasonal.register("drone_scint_cutscene");
   IAnimationSource FOLDED_HANDS_EXPLAIN = AnimationsRegistrySeasonal.register("humanoid/folded_hands_explain");
   IAnimationSource FOLDED_HANDS_EXPLAIN_TALL = AnimationsRegistrySeasonal.register("humanoid/folded_hands_explain_tall");
   IAnimationSource FURRY_IDLE_CYCLE = AnimationsRegistrySeasonal.register("furry_idle_cycle");
   IAnimationSource MADMAN_LOOKING_AROUND = AnimationsRegistrySeasonal.register("madman_looking_around");
   IAnimationSource DIZZY_FALL = AnimationsRegistrySeasonal.register("dizzy_fall");
   IAnimationSource FALLEN_RAISE_LAY_TO_SIT = AnimationsRegistrySeasonal.register("humanoid/fallen_raise_lay_to_sit");
   IAnimationSource HELPER_RAISE_LAY_TO_SIT = AnimationsRegistrySeasonal.register("humanoid/helper_raise_lay_to_sit");
   IAnimationSource FALL_TIRED_BASE_ARMS = AnimationsRegistrySeasonal.register("fall_tired_base_arms");
   IAnimationSource BODY_ENDANIMATION_CRAWL_SOUTH = AnimationsRegistrySeasonal.register("body_endanimation_crawl_south");
   IAnimationSource ROLL_FROM_LAY_ON_STOMACH_SOUTH = AnimationsRegistrySeasonal.register("roll_from_lay_on_stomach_south");
   IAnimationSource FALLEN_PUT_ON_CHAIR = AnimationsRegistrySeasonal.register("fallen_put_on_chair");
   IAnimationSource HELPER_PUT_ON_CHAIR = AnimationsRegistrySeasonal.register("helper_put_on_chair");
   IAnimationSource LAY_TO_KNEE_SIT_FORWARD = AnimationsRegistrySeasonal.register("lay_to_knee_sit_forward");
   IAnimationSource LAY_TO_KNEE_SIT_FORWARD_ANOTHER = AnimationsRegistrySeasonal.register("lay_to_knee_sit_forward_another");
   IAnimationSource CYCLE_FLOOR_CLEANING_TALL = AnimationsRegistrySeasonal.register("cycle_cleaning_tall");
   IAnimationSource ROLL_FROM_LAY_ON_STOMACH = AnimationsRegistrySeasonal.register("humanoid/roll_from_lay_on_stomach");
   IAnimationSource PREPAIR_DOWN_LAZER_GUN = AnimationsRegistrySeasonal.register("humanoid/prepair_down_lazer_gun");
   IAnimationSource SHOT_DOWN_LAZER_GUN = AnimationsRegistrySeasonal.register("humanoid/shot_down_lazer_gun");
   IAnimationSource SIT_ON_THRONE = AnimationsRegistrySeasonal.register("sit_on_throne");
   IAnimationSource SITGROUND_HOLD_KNEES = AnimationsRegistrySeasonal.register("humanoid/sitground_hold_knees");
   IAnimationSource TAKE_TEDDY_HOLD_HANDS = AnimationsRegistrySeasonal.register("humanoid/take_teddy_hold_hands");
   IAnimationSource HOLD_LAZER_GUN_POSE = AnimationsRegistrySeasonal.register("hold_lazer_gun_pose");
   IAnimationSource PREPAIR_LAZER_GUN = AnimationsRegistrySeasonal.register("prepair_lazer_gun");
   IAnimationSource SHOT_LAZER_GUN = AnimationsRegistrySeasonal.register("shot_lazer_gun");
   IAnimationSource LAY_FACE_RAISE_HAND = AnimationsRegistrySeasonal.register("humanoid/lay_face_raise_hand");
   @SpecialAnim("STELLA")
   IAnimationSource STELLA_SHRINK_DOWN = AnimationsRegistrySeasonal.register("stella/stella_shrink_down");
   @SpecialAnim("STELLA")
   IAnimationSource STELLA_THINK_CYCLE = AnimationsRegistrySeasonal.register("stella/stella_thinking_cycle");
   @SpecialAnim("STELLA")
   IAnimationSource STELLA_TAKE_FROM_HAND_RIGHT = AnimationsRegistrySeasonal.register("stella/stella_take_from_hand_right");
   @SpecialAnim("STELLA")
   IAnimationSource STELLA_TAKE_FROM_HAND_LEFT = AnimationsRegistrySeasonal.register("stella/stella_take_from_hand_left");
   @SpecialAnim("STELLA")
   IAnimationSource STELLA_SMALL_LAUGH = AnimationsRegistrySeasonal.register("stella/stella_small_laugh");
   @SpecialAnim("STELLA")
   IAnimationSource STELLA_INSPIRATION = AnimationsRegistrySeasonal.register("stella/stella_inspiration");
   @SpecialAnim("STELLA")
   IAnimationSource STELLA_INSPIRATION_DOUBLE = AnimationsRegistrySeasonal.register("stella/stella_inspiration_double");
   @SpecialAnim("TRISTAN")
   IAnimationSource TRISTAN_DISTRUST = AnimationsRegistrySeasonal.register("tristan/tristan_distrust");
   @SpecialAnim("TRISTAN")
   IAnimationSource TRISTAN_EMBARRASSMENT = AnimationsRegistrySeasonal.register("tristan/tristan_emoc_rolling");
   @SpecialAnim("TRISTAN")
   IAnimationSource TRISTAN_HAPPY_NEW = AnimationsRegistrySeasonal.register("tristan/tristan_emotion_happy");
   @SpecialAnim("TRISTAN")
   IAnimationSource TRISTAN_SAD2 = AnimationsRegistrySeasonal.register("tristan/tristan_emotion_sad2");
   @SpecialAnim("TRISTAN")
   IAnimationSource TRISTAN_FRIGHT = AnimationsRegistrySeasonal.register("tristan/tristan_fright");
   @SpecialAnim("TRISTAN")
   IAnimationSource TRISTAN_SIGH_NEW = AnimationsRegistrySeasonal.register("tristan/tristan_inhale_breath");
   @SpecialAnim("TRISTAN")
   IAnimationSource TRISTAN_REALIZATION = AnimationsRegistrySeasonal.register("tristan/tristan_emotion_realization");
   @SpecialAnim("TRISTAN")
   SidedAnimationSet TRISTAN_SHOCKED_SIDE_LOOK = AnimationsRegistrySeasonal.registerSided(
      "tristan_shocked_side_look_right", "tristan_shocked_side_look_left", "tristan/tristan_shocked_side_look"
   );
   @SpecialAnim("TRISTAN")
   IAnimationSource TRISTAN_GIGGLE_NEW = AnimationsRegistrySeasonal.register("tristan/tristan_small_laugh");
   @SpecialAnim("TRISTAN")
   IAnimationSource TRISTAN_BLINKING = AnimationsRegistrySeasonal.register("tristan/tristan_blinking");
   IAnimationSource OFFERHAND_LEFT_TO_STELLA = AnimationsRegistrySeasonal.register("offerhand_left_to_stella");
   IAnimationSource OFFERHAND_RIGHT_TO_STELLA = AnimationsRegistrySeasonal.register("offerhand_right_to_stella");
   IAnimationSource SIGNBOARD_LEVITATION = AnimationsRegistrySeasonal.register("signboard_levitationjson");
   IAnimationSource ZOMBIE_BLOB_ATTACK = AnimationsRegistrySeasonal.register("mob/zombie_blob/zombie_blob_attack");
   IAnimationSource ZOMBIE_BLOB_WALK = AnimationsRegistrySeasonal.register("mob/zombie_blob/zombie_blob_walk");
   IAnimationSource ZOMBIE_IMPROVE1_IDLE = AnimationsRegistrySeasonal.register("mob/zombie_improve_1/zombie_improve_1_idle");
   IAnimationSource ZOMBIE_IMPROVE1_WALK = AnimationsRegistrySeasonal.register("mob/zombie_improve_1/zombie_improve_1_walk");
   IAnimationSource ZOMBIE_IMPROVE1_ATTACK1 = AnimationsRegistrySeasonal.register("mob/zombie_improve_1/zombie_improve_1_attack1");
   IAnimationSource ZOMBIE_IMPROVE1_ATTACK2 = AnimationsRegistrySeasonal.register("mob/zombie_improve_1/zombie_improve_1_attack2");
   IAnimationSource EXPLODING_CYCLE = AnimationsRegistrySeasonal.register("mob/zombie_burn/exploding_cycle");
   IAnimationSource PREPARE_TO_EXPLODE = AnimationsRegistrySeasonal.register("mob/zombie_burn/prepare_to_explode");
   IAnimationSource MOTHER_IDLE = AnimationsRegistrySeasonal.register("mob/your_mom/woman_giant_idle");
   IAnimationSource MOTHER_ATTACK_SPIKES = AnimationsRegistrySeasonal.register("mob/your_mom/woman_giant_attack_ground");
   IAnimationSource MOTHER_ATTACK_SIDE = AnimationsRegistrySeasonal.register("mob/your_mom/woman_giant_attack_side");
   IAnimationSource MOTHER_ATTACK_GROUND = AnimationsRegistrySeasonal.register("mob/your_mom/woman_giant_attack_updown");
   IAnimationSource MOTHER_DEATH = AnimationsRegistrySeasonal.register("mob/your_mom/woman_giant_fall");
   IAnimationSource MOTHER_RUN = AnimationsRegistrySeasonal.registerSided("woman_giant_run", "woman_giant_run_attacking", "mob/your_mom/woman_giant_run")
      .right();
   IAnimationSource MOTHER_RUN_ATTACK = AnimationsRegistrySeasonal.registerSided("woman_giant_run", "woman_giant_run_attacking", "mob/your_mom/woman_giant_run")
      .left();
   IAnimationSource MOTHER_STUN1 = AnimationsRegistrySeasonal.register("mob/your_mom/woman_giant_stun1");
   IAnimationSource MOTHER_STUN2 = AnimationsRegistrySeasonal.register("mob/your_mom/woman_giant_stun2");
   IAnimationSource MOTHER_STUN3 = AnimationsRegistrySeasonal.register("mob/your_mom/woman_giant_stun3");
   IAnimationSource MOTHER_WALK = AnimationsRegistrySeasonal.register("mob/your_mom/woman_giant_walk");
   IAnimationSource TUMOR_ATTACK = AnimationsRegistrySeasonal.register("mob/tumor/tumor_attack");
   IAnimationSource TUMOR_DEATH = AnimationsRegistrySeasonal.register("mob/tumor/tumor_death");
   IAnimationSource TUMOR_IDLE = AnimationsRegistrySeasonal.register("mob/tumor/tumor_idle");
   IAnimationSource ORK_LOG_ATTACK = AnimationsRegistrySeasonal.register("mob/ork_log_attack");
   IAnimationSource DWARF_ATTACKING_RUN = AnimationsRegistrySeasonal.register("mob/dwarf_hammer/dwarf_attacking_run");
   IAnimationSource DWARF_ATTACKING_ATTACK_LEFT = AnimationsRegistrySeasonal.register("mob/dwarf_hammer/dwarf_hammer_attack_left");
   IAnimationSource DWARF_ATTACKING_ATTACK_RIGHT = AnimationsRegistrySeasonal.register("mob/dwarf_hammer/dwarf_hammer_attack_right");
   IAnimationSource DWARF_ATTACKING_VERTICAL_ATTACK = AnimationsRegistrySeasonal.register("mob/dwarf_hammer/dwarf_hammer_vertical_attack");
   IAnimationSource DWARF_ATTACKING_VERTICAL_ATTACK_BACK = AnimationsRegistrySeasonal.register("mob/dwarf_hammer/dwarf_hammer_vertical_attack_back");
   IAnimationSource ELF_BOW_ATTACK_CHARGE = AnimationsRegistrySeasonal.register("mob/elf_bow/bow_attack_charge");
   IAnimationSource ELF_BOW_ATTACK_CHARGE_POSE = AnimationsRegistrySeasonal.register("mob/elf_bow/bow_attack_charge_pose");
   IAnimationSource ELF_BOW_ATTACK_SHOOT = AnimationsRegistrySeasonal.register("mob/elf_bow/bow_attack_shoot");
   IAnimationSource JDH_ATTACK_2 = AnimationsRegistrySeasonal.register("mob/jdh_attack/spark_jdh_attack_2");
   IAnimationSource JDH_ATTACK_3 = AnimationsRegistrySeasonal.register("mob/jdh_attack/spark_jdh_attack_3");
   IAnimationSource JDH_ATTACK_4 = AnimationsRegistrySeasonal.register("mob/jdh_attack/spark_jdh_attack_4");
   IAnimationSource BALLET_DANCE = AnimationsRegistrySeasonal.register("ballet_dance");
   IAnimationSource SITTING_LOTUS_POSE = AnimationsRegistrySeasonal.register("humanoid/sitting_lotus_pose");
   IAnimationSource JETPACK_FLY_LAYER_LOTUS = AnimationsRegistrySeasonal.register("humanoid/jetpack_fly_layer_lotus");
   SidedAnimationSet PULLING_ON_SLEEVE = AnimationsRegistrySeasonal.registerSided(
      "pulling_on_sleeve_right", "pulling_on_sleeve_left", "humanoid/pulling_on_sleeve"
   );
   SidedAnimationSet BEING_PULLED_ON_SLEEVE = AnimationsRegistrySeasonal.registerSided(
      "being_pulled_on_sleeve_right", "being_pulled_on_sleeve_left", "humanoid/being_pulled_on_sleeve"
   );
   IAnimationSource PSYCHOPOMP_GIVE_JETPACK = AnimationsRegistrySeasonal.register("psychopomp_give_jetpack");
   IAnimationSource PSYCHOPOMP_HOLD_JETPACK = AnimationsRegistrySeasonal.register("psychopomp_hold_jetpack");
   IAnimationSource JOVANNI_RUBBING_HANDS_ENDANIMATED = AnimationsRegistrySeasonal.register("humanoid/jovanni_rubbing_hands_endanimated");
   @SpecialAnim("SCINT_CLUSTER")
   IAnimationSource SCINT_CLUSTER_GROWING = AnimationsRegistrySeasonal.register("lukiy_scint_cluster_growing");
   @SpecialAnim("FATHER_HAZMAT")
   IAnimationSource FATHER_HAZMAT_THROW_HELMET = AnimationsRegistrySeasonal.register("father_hazmat_throw_helmet");
   IAnimationSource EMOTION_REDGI_SUS = AnimationsRegistrySeasonal.register("emotion_redgi_sus");
   SidedAnimationSet CHILL_SIT_FEET_UP_ON = AnimationsRegistrySeasonal.registerSided(
      "chill_sit_feet_up_on_right", "chill_sit_feet_up_on_left", "humanoid/chill_sit_feet_up_on"
   );
   SidedAnimationSet BRING_SHELL_EAR = AnimationsRegistrySeasonal.registerSided("bring_shell_ear_right", "bring_shell_ear_left", "bring_shell_ear");
   IAnimationSource DIE_FROM_SHOT_IN_CHEST = AnimationsRegistrySeasonal.register("humanoid/die_from_shot_in_chest");
   IAnimationSource FALL_ON_KNEES_SLIDING_GLASS = AnimationsRegistrySeasonal.register("humanoid/fall_on_knees_sliding_glass");
   IAnimationSource WAKE_UP_FOR_DIE_FROM_SHOT_IN_CHEST = AnimationsRegistrySeasonal.register("humanoid/wake_up_for_die_from_shot_in_chest");
   IAnimationSource HITTING_THE_GLASS = AnimationsRegistrySeasonal.register("humanoid/hitting_the_glass");
   IAnimationSource LEAN_ON_GLASS = AnimationsRegistrySeasonal.register("humanoid/lean_on_glass");
   IAnimationSource PSYCHO_LAUGH_CYCLE = AnimationsRegistrySeasonal.register("humanoid/psycho_laugh_cycle");
   IAnimationSource LO_JUMP_ROBODOG = AnimationsRegistrySeasonal.register("lo_jump_from_robodog");
   IAnimationSource MALETSKI_JUMP_FROM_ROBODOG = AnimationsRegistrySeasonal.register("maletski_jump_from_robodog");
   IAnimationSource ROBODOGSKINT_WALK_HUMAN_IDLE = AnimationsRegistrySeasonal.register("robodogskintwalk_human_idle");
   IAnimationSource SIT_ON_GROUND_HUG_KNEES = AnimationsRegistrySeasonal.register("sitonground_hug_knees");
   SidedAnimationSet HAND_1_DEGREES_X = AnimationsRegistrySeasonal.registerSided("hand_1_degrees_x_right", "hand_1_degrees_x_left", "hand_1_degrees_x");
   SidedAnimationSet HAND_1_DEGREES_Y = AnimationsRegistrySeasonal.registerSided("hand_1_degrees_y_right", "hand_1_degrees_y_left", "hand_1_degrees_y");
   SidedAnimationSet HAND_1_DEGREES_Z = AnimationsRegistrySeasonal.registerSided("hand_1_degrees_z_right", "hand_1_degrees_z_left", "hand_1_degrees_z");
   IAnimationSource JETPACK_CLUMSY_LANDING = AnimationsRegistrySeasonal.register("jetpack/jetpack_clumsy_landing");
   IAnimationSource JETPACK_FLY_BACK = AnimationsRegistrySeasonal.register("jetpack/jetpack_fly_back");
   IAnimationSource JETPACK_FLY_LEFT = AnimationsRegistrySeasonal.register("jetpack/jetpack_fly_left");
   IAnimationSource JETPACK_FLY_RIGHT = AnimationsRegistrySeasonal.register("jetpack/jetpack_fly_right");
   IAnimationSource JETPACK_FLY_UP = AnimationsRegistrySeasonal.register("jetpack/jetpack_fly_up");
   IAnimationSource JETPACK_KNOCK_WALL = AnimationsRegistrySeasonal.register("jetpack/jetpack_knock_the_wall");
   IAnimationSource JETPACK_LANDING = AnimationsRegistrySeasonal.register("jetpack/jetpack_landing");
   IAnimationSource GIVE_JETPACK = AnimationsRegistrySeasonal.register("jetpack/give_jetpack");
   IAnimationSource HOLD_JETPACK = AnimationsRegistrySeasonal.register("jetpack/hold_jetpack");
   IAnimationSource LO_WEAR_JETPACK = AnimationsRegistrySeasonal.register("jetpack/lo_wear_jetpack");
   IAnimationSource MALETSKI_WEAR_JETPACK = AnimationsRegistrySeasonal.register("jetpack/maletski_wear_jetpack");
   IAnimationSource PICK_FROM_GROUND_JETPACK = AnimationsRegistrySeasonal.register("jetpack/pick_from_ground_jetpack");
   IAnimationSource JETPACK_THROW_AWAY = AnimationsRegistrySeasonal.register("jetpack/lo_remove_discard_jetpack");
   @SpecialAnim
   IAnimationSource MASK_BREAK = AnimationsRegistrySeasonal.register("lololoshka_musk_break");
   IAnimationSource MAGIC_FLY_UP = AnimationsRegistrySeasonal.register("humanoid/magic_fly_up");
   IAnimationSource MAGIC_FLY_UP_CYCLE = AnimationsRegistrySeasonal.register("humanoid/magic_fly_up_cycle");
   IAnimationSource BREAKDANCE = AnimationsRegistrySeasonal.register("humanoid/breakdance_wildmill");
   SidedAnimationSet JETPACK_FLY_WITHOUT_HAND = AnimationsRegistrySeasonal.registerSided(
      "jetpack_fly_without_right_hand", "jetpack_fly_without_left_hand", "jetpack/jetpack_fly_without_hand"
   );
   SidedAnimationSet JETPACK_HAND_REFUEL = AnimationsRegistrySeasonal.registerSided(
      "jetpack_right_hand_refuel", "jetpack_left_hand_refuel", "jetpack/jetpack_hand_refuel"
   );
   @SpecialAnim("JETPACK")
   IAnimationSource JUMP_FLY_JETPACK = AnimationsRegistrySeasonal.register("jetpack/jump_fly_jetpack");
   @SpecialAnim("JETPACK")
   IAnimationSource JUMP_FLY_COVEREYES_LAYER = AnimationsRegistrySeasonal.register("jetpack/jetpack_fly_covereyes_layer");
   @SpecialAnim("JETPACK")
   IAnimationSource JUMP_FLY_STAY = AnimationsRegistrySeasonal.register("jetpack/jetpack_fly_stay");
   @SpecialAnim("JETPACK")
   IAnimationSource JETPACK_LANDING_NO_MOVE = AnimationsRegistrySeasonal.register("jetpack_landing_no_move");
   @SpecialAnim("ROBO_DOG_SKINT")
   IAnimationSource ROBODOG_SKINT_IDLE = AnimationsRegistrySeasonal.register("robodog_skint_idle");
   @SpecialAnim("ROBO_DOG_SKINT")
   IAnimationSource ROBODOG_SKINT_WALK = AnimationsRegistrySeasonal.register("robodog_skint_walk");
   @SpecialAnim("ROBO_DOG_SKINT")
   IAnimationSource ROBODOG_SKINT_LAY = AnimationsRegistrySeasonal.register("robo_dog/robodog_scint_lay");
   IAnimationSource HUG_ADULT_CHILD_STROKING = AnimationsRegistrySeasonal.register("hug_adult_to_child_stroking");
   IAnimationSource HUG_ADULT_CHILD_STROKING_CYCLE = AnimationsRegistrySeasonal.register("hug_adult_to_child_stroking_cycle");
   SidedAnimationSet INSPECTION_ENDANIMATED = AnimationsRegistrySeasonal.registerSided(
      "inspection_endanimated_right", "inspection_endanimated_left", "inspection_endanimated"
   );
   SidedAnimationSet BLOCKING_WAY_HAND = AnimationsRegistrySeasonal.registerSided("blocking_way_hand_right", "blocking_way_hand_left", "blocking_way_hand");
   IAnimationSource HUG_STEVE_OKETRA_LEFT = AnimationsRegistrySeasonal.register("hug_steve_oketra_left");
   IAnimationSource MODEL_ROTATE_1_X = AnimationsRegistrySeasonal.register("model_rotate_1_x");
   IAnimationSource MODEL_ROTATE_1_Y = AnimationsRegistrySeasonal.register("model_rotate_1_y");
   IAnimationSource MODEL_ROTATE_1_Z = AnimationsRegistrySeasonal.register("model_rotate_1_z");
   IAnimationSource CLIMBING_UP = AnimationsRegistrySeasonal.register("climbing_up");
   IAnimationSource ROTATE_MODEL_45 = AnimationsRegistrySeasonal.register("humanoid/rotate_model_45y");
   IAnimationSource GETUP_FALL_FROM_PUNCH = AnimationsRegistrySeasonal.register("humanoid/getup_fall_from_punch");
   IAnimationSource RAISE_SWORD_UP = AnimationsRegistrySeasonal.register("humanoid/raise_sword_up");
   SidedAnimationSet STAND_ON_HANDS_FAIL = AnimationsRegistrySeasonal.registerSided(
      "stand_on_hands_fail_right", "stand_on_hands_fail_left", "stand_on_hands_fail"
   );
   @SpecialAnim("NEVER")
   SidedAnimationSet NEVER_FINGER_UP = AnimationsRegistrySeasonal.registerSided(
      "newer_finger_up_right", "newer_finger_up_left", "st_6_undying_spark/never/newer_finger_up"
   );
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_LOOK_STUMPED = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_sit_looking");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_FLY_SIT_TO_STAND = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_fly_stand");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_SIT_GROUND_POSE = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_sit_ground_pose");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_SIT_GROUND = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_sit_on_ground");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_SIT_GROUND_NO_ARMS = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_sit_ground_pose_noarms");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_LOOK_AROUND = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_look_out");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_CLOSE_WINGS = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_hide");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_CLOSE_FACE = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_close_face");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_FLY_CYCLE = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_fly_cycle");
   @SpecialAnim("NEVER")
   IAnimationSource CROW_SHY_POSE = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/crow_shy_pose");
   @SpecialAnim("NEVER")
   SidedAnimationSet NEVER_SURPRISED = AnimationsRegistrySeasonal.registerSided(
      "newer_surpsrised_right", "newer_surpsrised_left", "st_6_undying_spark/never/newer_surpsrised"
   );
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_CONSIDER = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_lookdown");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_SAD = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_emotion_sad");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_PECK = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_beak_hit");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_JUMPING_AROUND = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_jumping_around");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_JUMPING = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_jumping");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_HURT_LEG = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_hurt_leg");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_CAR = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_kar");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_WAITING = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_stand_still_idle");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_EMOTION_FEAR = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_emotion_fear");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_WINGS_WIDE = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_wings_wide");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_SHOO = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_get_off");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_FOOTSIES = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_foot_to_foot");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_SELFHAMB = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_selfthumb");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_TAKE_HEAD = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_take_head");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_FALL_ON_KNEES = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_fall_on_knees");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_KNEE_PRAY = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_knee_pray");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_PULL_HANDS = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_pull_hands");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_SLEEP_POSE = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_sleep_pose");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_SLEEP_HEAD_UP_POSE = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_sleep_head_up_pose");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_SCARED_SLEEP_TO_SIT = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_scared_sleep_to_sit");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_TO_SLEEP_HEAD_UP = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_to_sleep_head_up");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_SLEEP_IDLE = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_sleep_idle");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_TO_SLEEP = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_to_sleep");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_MAGIC_CAST = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_magic_cast");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_COME_HERE = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_come_here");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_EXPLAINING_CROSSED_ARMS = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_explaining_crossed_arms");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_WINGS_BACK = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_wings_back");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_HANDS_ON_SIDES = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_hands_on_sides");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_SHAKE = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_shake");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_DEFENDING = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_defending");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_DISGUST_RIGHT = AnimationsRegistrySeasonal.register("never_disgust_right");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_PSYCHO_LAUGH = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/kardinal_psycho_laugh");
   @SpecialAnim("NEVER")
   IAnimationSource SCARED_MOVE_NO_EYES = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/scared_move_no_eyes");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_EMOTION_FEAR_CYCLE = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_emotion_fear_cycle");
   @SpecialAnim("NEVER")
   IAnimationSource KARDINAL_HIDE_EYES = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/kardinal_hide_eyes");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_SIT_GROUND_ARMS_ONLY = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_sit_ground_arms_only");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_EMOTION_BLUSH = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_emotion_blush");
   @SpecialAnim("NEVER")
   SidedAnimationSet NEVER_SHOCKED_LOOK_AT_HAND = AnimationsRegistrySeasonal.registerSided(
      "newer_shocked_look_at_hand_right", "newer_shocked_look_at_hand_left", "st_6_undying_spark/never/newer_shocked_look_at_hand"
   );
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_WINGS_WHOOSH = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_wings_whoosh");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_TOUCH_SHOULDER = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_touch_shoulder");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_PRAY = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_pray");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_HIDE_EYES = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_hide_eyes");
   @SpecialAnim("NEVER")
   SidedAnimationSet NEVER_DEFENDING_POSE_NO_HANDS = AnimationsRegistrySeasonal.registerSided(
      "newer_magic_attack_left", "newer_magic_attack_left", "st_6_undying_spark/never/newer_defending_pose_nohands"
   );
   @SpecialAnim("NEVER")
   SidedAnimationSet NEVER_MAGIC_ATTACK = AnimationsRegistrySeasonal.registerSided(
      "newer_magic_attack_right", "newer_magic_attack_left", "st_6_undying_spark/never/newer_magic_attack"
   );
   @SpecialAnim("NEVER")
   SidedAnimationSet NEVER_MAGIC_ATTACK_HARD = AnimationsRegistrySeasonal.registerSided(
      "newer_magic_attack_hard_right", "newer_magic_attack_hard_left", "st_6_undying_spark/never/newer_magic_hard_attack"
   );
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_BODY_TILTED_DOWN = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/body_down");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_SNEER = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/kardinal_laugh");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_PUNCH_FIST_ON_TABLE = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/kardinal_table_punch");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_FACEPALM = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/kardinal_facepalm");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_GET_OFF = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/kardinal_get_off");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_SIT_BLOCK = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_sit_block");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_INSULT = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_insult");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_CLEAN_WING_CYCLE = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_clean_wings_cycle");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_SIT_ON_GROUND_BLOCK = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_sit_on_ground_block");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_PRIDE = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_pride");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_SALTO_FLY = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/crow_salto_fly");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_LEG_CLAP_JUMP = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/crow_leg_clap_jump");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_BOOK_ON_HEAD = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/crow_book_on_head");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_LAY_ON_STOMACH_DANGLE_LEGS = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/crow_lay_stomach_dangle_legs");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_TAKE_GLASSES = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/crow_take_glasses");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_YELLING_HEAD = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/crow_yelling_head");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_DRAMATIC_GRIEF = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_dramatic_grief");
   @SpecialAnim("NEVER")
   SidedAnimationSet NEVER_HAND_TO_HEART = AnimationsRegistrySeasonal.registerSided(
      "newer_hand_to_heart_right", "newer_hand_to_heart_left", "st_6_undying_spark/never/newer_hand_to_heart"
   );
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_LAY_STOMACH = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_lay_stomach");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_STOMACH_TO_SIDELAY = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_stomach_to_sidelay");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_LAY_GROUND = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_lay_ground");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_ROLL_FROM_STOMACH = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_roll_from_stomach");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_STAY_TO_LAY_GROUND = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_to_lay_ground");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_HEAVY_FLY = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_new_heavy_fly_cycle");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_FLY_NEW = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_new_fly_cycle");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_FLAP_WINGS_MIDAIR = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/newer_flap_wings_mid_air_layer");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_HANDS_TO_CHEST = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/crow_hands_to_chest");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_LEGS_SHUFFLING = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/crow_legs_shuffling");
   @SpecialAnim("NEVER")
   IAnimationSource CROW_CLOSE_EYES = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/crow_close_eyes");
   @SpecialAnim("NEVER")
   IAnimationSource CROW_COUGHING_LAYER = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/crow_coughing_layer");
   @SpecialAnim("NEVER")
   IAnimationSource CROW_EMOTION_RAGE = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/crow_emotion_rage");
   @SpecialAnim("NEVER")
   IAnimationSource CROW_FAINTED_LAY = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/crow_fainted_lay");
   @SpecialAnim("NEVER")
   IAnimationSource CROW_NOWHERE_LOOK = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/crow_nowhere_look");
   @SpecialAnim("NEVER")
   IAnimationSource CROW_WING_ON_CHEST_LAYERED = AnimationsRegistrySeasonal.register("st_6_undying_spark/never/crow_wing_on_chest_layered");
   @SpecialAnim("GOOSE")
   IAnimationSource GOOSE_HOLDED_POSE = AnimationsRegistrySeasonal.register("goose/goose_holded_pose");
   @SpecialAnim("GOOSE")
   IAnimationSource GOOSE_IDLE = AnimationsRegistrySeasonal.register("goose/goose_idle");
   @SpecialAnim("GOOSE")
   IAnimationSource GOOSE_IDLE_MODEL2 = AnimationsRegistrySeasonal.register("goose/goose_idle_model2");
   @SpecialAnim("GOOSE")
   IAnimationSource GOOSE_JUMP_BACK = AnimationsRegistrySeasonal.register("goose/goose_jump_back");
   @SpecialAnim("GOOSE")
   IAnimationSource GOOSE_JUMP_FORWARD = AnimationsRegistrySeasonal.register("goose/goose_jump_forward");
   @SpecialAnim("GOOSE")
   IAnimationSource GOOSE_RUN = AnimationsRegistrySeasonal.register("goose/goose_run");
   @SpecialAnim("GOOSE")
   IAnimationSource GOOSE_SHAKE = AnimationsRegistrySeasonal.register("goose/goose_shake");
   @SpecialAnim("GOOSE")
   SidedAnimationSet GOOSE_SIDE_LOOK = AnimationsRegistrySeasonal.registerSided("goose_side_look_right", "goose_side_look_left", "goose/goose_side_look");
   @SpecialAnim("GOOSE")
   IAnimationSource GOOSE_WALK = AnimationsRegistrySeasonal.register("goose/goose_walk");
   @SpecialAnim("GOOSE")
   IAnimationSource GOOSE_WIGGLE = AnimationsRegistrySeasonal.register("goose/goose_wiggle");
   @SpecialAnim("GOOSE")
   IAnimationSource GOOSE_EAT = AnimationsRegistrySeasonal.register("goose/goose_eat");
   @SpecialAnim("GOOSE")
   IAnimationSource GOOSE_FLY_CYCLE = AnimationsRegistrySeasonal.register("goose/goose_fly_cycle");
   @SpecialAnim("GOOSE")
   IAnimationSource GOOSE_PINCH = AnimationsRegistrySeasonal.register("goose/goose_pinch");
   @SpecialAnim("GOOSE")
   IAnimationSource GOOSE_SIT = AnimationsRegistrySeasonal.register("goose/goose_sit");
   @SpecialAnim("GOOSE")
   IAnimationSource GOOSE_SLEEP = AnimationsRegistrySeasonal.register("goose/goose_sleep");
   @SpecialAnim("GOOSE")
   IAnimationSource GOOSE_WINGS_UP = AnimationsRegistrySeasonal.register("goose/goose_wings_up");
   @SpecialAnim("GOOSE")
   IAnimationSource GOOSE_POOPING = AnimationsRegistrySeasonal.register("goose/goose_pooping");
   @SpecialAnim("GOOSE")
   IAnimationSource GOOSE_ANGRY = AnimationsRegistrySeasonal.register("goose/goose_angry");
   @SpecialAnim("GOOSE")
   IAnimationSource GOOSE_SWIM = AnimationsRegistrySeasonal.register("goose/goose_swim");
   @SpecialAnim("GOOSE")
   IAnimationSource GOOSE_SWIM_SPLASHING = AnimationsRegistrySeasonal.register("goose/goose_swim_splashing_layer");
   @SpecialAnim("GOOSE")
   IAnimationSource GOOSE_POOP_POSE = AnimationsRegistrySeasonal.register("goose/goose_poop_pose");
   @SpecialAnim("GOOSE")
   IAnimationSource GOOSE_POOP_ENDANIMATED = AnimationsRegistrySeasonal.register("goose/goose_poop_endanimated");
   @SpecialAnim("GOOSE")
   IAnimationSource GOOSE_RUN_WIGGLE_LAYER = AnimationsRegistrySeasonal.register("goose/goose_run_wiggle_layered");
   @SpecialAnim("GOOSE")
   IAnimationSource GOOSE_GET_ATTACKED = AnimationsRegistrySeasonal.register("goose/goose_get_attacked");
   @SpecialAnim("GOOSE")
   IAnimationSource GOOSE_GET_ATTACKED_POSE = AnimationsRegistrySeasonal.register("goose/goose_get_attacked_pose");
   @SpecialAnim("GOOSE")
   IAnimationSource GOOSE_HAPPY_JUMP = AnimationsRegistrySeasonal.register("goose/goose_happy_jump");
   @SpecialAnim("GOOSE")
   IAnimationSource GOOSE_AWAKE = AnimationsRegistrySeasonal.register("goose/goose_awake");
   @SpecialAnim("GOOSE")
   IAnimationSource GOOSE_CRAZY_ATTACK = AnimationsRegistrySeasonal.register("goose/goose_crazy_attack");
   @SpecialAnim("GOOSE")
   SidedAnimationSet GOOSE_HELD = AnimationsRegistrySeasonal.registerSided("goose_held_right", "goose_held_left", "goose/goose_held");
   @SpecialAnim("GOOSE")
   SidedAnimationSet GOOSE_SQUISH = AnimationsRegistrySeasonal.registerSided("goose_squish_right", "goose_squish_left", "goose/goose_squish");
   @SpecialAnim("MODDED_RACCOON")
   IAnimationSource RACCOON_BLACK_EYES = AnimationsRegistrySeasonal.register("mob/raccoon/raccoon_black_eyes");
   @SpecialAnim("MODDED_RACCOON")
   IAnimationSource RACCOON_HANDS_ON_HEART_LAYER = AnimationsRegistrySeasonal.register("mob/raccoon/raccoon_hands_on_heart_layer");
   @SpecialAnim("MODDED_RACCOON")
   IAnimationSource RACCOON_HOVER_CYCLE = AnimationsRegistrySeasonal.register("mob/raccoon/raccoon_hover_cycle");
   @SpecialAnim("MODDED_RACCOON")
   IAnimationSource RACCOON_IDLE = AnimationsRegistrySeasonal.register("mob/raccoon/raccoon_idle");
   @SpecialAnim("MODDED_RACCOON")
   IAnimationSource RACCOON_IMAGINATION_LAYER = AnimationsRegistrySeasonal.register("mob/raccoon/raccoon_imagination_layer");
   @SpecialAnim("MODDED_RACCOON")
   IAnimationSource RACCOON_NEVERMIND = AnimationsRegistrySeasonal.register("mob/raccoon/raccoon_nevermind");
   @SpecialAnim("MODDED_RACCOON")
   IAnimationSource RACCOON_QUESTION_POSING = AnimationsRegistrySeasonal.register("mob/raccoon/raccoon_question_posing");
   @SpecialAnim("MODDED_RACCOON")
   IAnimationSource RACCOON_SURPRISED = AnimationsRegistrySeasonal.register("mob/raccoon/raccoon_surprised");
   @SpecialAnim("MODDED_RACCOON")
   IAnimationSource RACCOON_ANGRY_POINT = AnimationsRegistrySeasonal.register("mob/raccoon/raccoon_angry_point");
   @SpecialAnim("MODDED_RACCOON")
   IAnimationSource RACCOON_CALLING_LAYER = AnimationsRegistrySeasonal.register("mob/raccoon/raccoon_calling_layer");
   @SpecialAnim("MODDED_RACCOON")
   IAnimationSource RACCOON_CROSSED_ARMS_LAYER = AnimationsRegistrySeasonal.register("mob/raccoon/raccoon_crossed_arms_layer");
   @SpecialAnim("MODDED_RACCOON")
   IAnimationSource RACCOON_FACEPALM = AnimationsRegistrySeasonal.register("mob/raccoon/raccoon_facepalm");
   @SpecialAnim("MODDED_RACCOON")
   IAnimationSource RACCOON_HAND_CLAP = AnimationsRegistrySeasonal.register("mob/raccoon/raccoon_hand_clap");
   @SpecialAnim("MODDED_RACCOON")
   IAnimationSource RACCOON_LASER_EYES = AnimationsRegistrySeasonal.register("mob/raccoon/raccoon_laser_eyes");
   @SpecialAnim("MODDED_RACCOON")
   IAnimationSource RACCOON_LAUGH = AnimationsRegistrySeasonal.register("mob/raccoon/raccoon_laugh");
   @SpecialAnim("MODDED_RACCOON")
   IAnimationSource RACCOON_RUBBING_HANDS_LAYER = AnimationsRegistrySeasonal.register("mob/raccoon/raccoon_rubbing_hands_layer");
   @SpecialAnim("MODDED_RACCOON")
   IAnimationSource RACCOON_SHRUG = AnimationsRegistrySeasonal.register("mob/raccoon/raccoon_shrug");
   @SpecialAnim("MODDED_RACCOON")
   IAnimationSource RACCOON_VERY_ANGRY_LAYER = AnimationsRegistrySeasonal.register("mob/raccoon/raccoon_very_angry_layer");
   @SpecialAnim("MODDED_RACCOON")
   IAnimationSource RACCOON_WIDE_ARMS_LAYER = AnimationsRegistrySeasonal.register("mob/raccoon/raccoon_wide_arms_layer");
   @SpecialAnim("MODDED_RACCOON")
   IAnimationSource RACCOON_HEAVY_BREATH_LAYER = AnimationsRegistrySeasonal.register("mob/raccoon/raccoon_heavy_breath_layer");
   @SpecialAnim("MODDED_RACCOON")
   IAnimationSource RACCOON_POINT_HEART_LAYER = AnimationsRegistrySeasonal.register("mob/raccoon/raccoon_point_heart_layer");
   @SpecialAnim("MODDED_RACCOON")
   IAnimationSource RACCOON_RUB_BELLY = AnimationsRegistrySeasonal.register("mob/raccoon/raccoon_rub_belly");
   @SpecialAnim("MODDED_RACCOON")
   IAnimationSource RACCOON_SHOW_PAPER = AnimationsRegistrySeasonal.register("mob/raccoon/raccoon_show_paper");
   IAnimationSource FATHER_AIRSTRIKE_JUMP = AnimationsRegistrySeasonal.register("father_fight/phase1/father_phase1_airstrike_jump");
   IAnimationSource FATHER_AIRSTRIKE_PIERCE = AnimationsRegistrySeasonal.register("father_fight/phase1/father_phase1_airstrike_pierce");
   IAnimationSource FATHER_AIRSTRIKE_RUSH = AnimationsRegistrySeasonal.register("father_fight/phase1/father_phase1_airstrike_rush");
   IAnimationSource FATHER_ATTRACT = AnimationsRegistrySeasonal.register("father_fight/phase1/father_phase1_attract");
   IAnimationSource FATHER_MIRAGE_CAST = AnimationsRegistrySeasonal.register("father_fight/phase1/father_phase1_mirage_cast");
   IAnimationSource FATHER_POSE_PHASE1 = AnimationsRegistrySeasonal.register("father_fight/phase1/father_phase1_pose");
   IAnimationSource FATHER_SLASH_LEFT = AnimationsRegistrySeasonal.register("father_fight/phase1/father_phase1_slash_left");
   IAnimationSource FATHER_SLASH_LEFT_FAST = AnimationsRegistrySeasonal.register("father_fight/phase1/father_phase1_slash_left_fast");
   IAnimationSource FATHER_SLASH_RIGHT = AnimationsRegistrySeasonal.register("father_fight/phase1/father_phase1_slash_right");
   IAnimationSource FATHER_SLASH_RIGHT_FAST = AnimationsRegistrySeasonal.register("father_fight/phase1/father_phase1_slash_right_fast");
   IAnimationSource FATHER_PHASE1_WALK = AnimationsRegistrySeasonal.register("father_fight/phase1/father_phase1_walk");
   SidedAnimationSet FATHER_ATTACK_1 = AnimationsRegistrySeasonal.registerSided(
      "father_attack1_right", "father_attack1_left", "father_fight/phase2/father_attack1"
   );
   SidedAnimationSet FATHER_ATTACK_2 = AnimationsRegistrySeasonal.registerSided(
      "father_attack2_right", "father_attack2_left", "father_fight/phase2/father_attack2"
   );
   IAnimationSource FATHER_ATTACK_3 = AnimationsRegistrySeasonal.register("father_fight/phase2/father_attack3");
   IAnimationSource FATHER_ATTACK_4 = AnimationsRegistrySeasonal.register("father_fight/phase2/father_attack4");
   SidedAnimationSet FATHER_FIREBALL = AnimationsRegistrySeasonal.registerSided(
      "father_phase3_fireball_right", "father_phase3_fireball_left", "father_fight/phase3/father_phase3_fireball"
   );
   IAnimationSource FATHER_FIST_HIT = AnimationsRegistrySeasonal.register("father_fight/phase3/father_phase3_fist_hit");
   IAnimationSource FATHER_GROUND_HIT = AnimationsRegistrySeasonal.register("father_fight/phase3/father_phase3_ground_hit");
   IAnimationSource FATHER_POSE_PHASE3 = AnimationsRegistrySeasonal.register("father_fight/phase3/father_phase3_pose");
   IAnimationSource FATHER_SPEAR_FLURRY = AnimationsRegistrySeasonal.register("father_fight/phase3/father_phase3_spear_flurry");
   SidedAnimationSet FATHER_VOID_WAVE = AnimationsRegistrySeasonal.registerSided(
      "father_phase3_void_wave_right", "father_phase3_void_wave_left", "father_fight/phase3/father_phase3_void_wave"
   );
   IAnimationSource FATHER_DEATH = AnimationsRegistrySeasonal.register("father_death");
   @SpecialAnim
   SidedAnimationSet BB_OKETRA_ATTACK = AnimationsRegistrySeasonal.registerSided(
      "oketra_attack_single_slash_right", "oketra_attack_single_slash_left", "mob/amelia/oketra_attack_single_slash"
   );
   @SpecialAnim
   SidedAnimationSet BB_OKETRA_ATTACK_DOUBLE = AnimationsRegistrySeasonal.registerSided(
      "oketra_attack_double_slash_right", "oketra_attack_double_slash_left", "mob/amelia/oketra_attack_double_slash"
   );
   @SpecialAnim
   IAnimationSource BB_OKETRA_ATTACK_FOLDED_CRASH = AnimationsRegistrySeasonal.register("mob/amelia/oketra_attack_folded_arms_crash");
   @SpecialAnim
   SidedAnimationSet BB_OKETRA_ATTACK_JUMP = AnimationsRegistrySeasonal.registerSided(
      "oketra_attack_jump_right", "oketra_attack_jump_left", "mob/amelia/oketra_attack_jump"
   );
   @SpecialAnim
   SidedAnimationSet BB_OKETRA_ATTACK_BACKSTEP = AnimationsRegistrySeasonal.registerSided(
      "oketra_attack_with_backstep_right", "oketra_attack_with_backstep_left", "mob/amelia/oketra_attack_with_backstep"
   );
   @SpecialAnim
   IAnimationSource BB_OKETRA_IDLE = AnimationsRegistrySeasonal.register("mob/amelia/oketra_idle");
   @SpecialAnim
   IAnimationSource BB_OKETRA_WALK = AnimationsRegistrySeasonal.register("mob/amelia/oketra_walk_cycle");
   @SpecialAnim
   IAnimationSource BB_OKETRA_DEATH = AnimationsRegistrySeasonal.register("mob/amelia/oketra_death");
   @SpecialAnim
   IAnimationSource DRAGON_WINGS_FOLDED_POSE = AnimationsRegistrySeasonal.register("mob/dragon/dragon_wings_folded_pose");
   @SpecialAnim
   IAnimationSource DRAGON_FLYING_CYCLE = AnimationsRegistrySeasonal.register("mob/dragon/dragon_flying_cycle");
   @SpecialAnim
   IAnimationSource DRAGON_NORMAL_IDLE = AnimationsRegistrySeasonal.register("mob/dragon/dragon_normal_idle");
   @SpecialAnim
   IAnimationSource DRAGON_WOUNDED_IDLE = AnimationsRegistrySeasonal.register("mob/dragon/dragon_wounded_idle");
   @SpecialAnim
   SidedAnimationSet DRAGON_ATTACK_HIT = AnimationsRegistrySeasonal.registerSided(
      "dragon_attack_hit_right", "dragon_attack_hit_left", "mob/dragon/dragon_attack_hit"
   );
   @SpecialAnim
   IAnimationSource DRAGON_ATTACK_BITE = AnimationsRegistrySeasonal.register("mob/dragon/dragon_attack_bite");
   @SpecialAnim
   IAnimationSource DRAGON_DAMAGE_ENDANIMATED = AnimationsRegistrySeasonal.register("mob/dragon/dragon_damage_endanimated");
   @SpecialAnim
   IAnimationSource DRAGON_HIT = AnimationsRegistrySeasonal.register("mob/dragon/dragon_hit");
   @SpecialAnim
   IAnimationSource DRAGON_DEFEATED = AnimationsRegistrySeasonal.register("mob/dragon/dragon_defeated");
   IAnimationSource BOXING_GAME_PUNCH = AnimationsRegistrySeasonal.register("punching_bag/boxing_game_punch");
   IAnimationSource BOXING_HIT_PREPARATION = AnimationsRegistrySeasonal.register("punching_bag/boxing_hit_preparation");
   IAnimationSource BOXING_HIT = AnimationsRegistrySeasonal.register("punching_bag/boxing_hit");
   AnimationsRegistry.EyeGaze STELLA_EYE_GAZE = new AnimationsRegistry.EyeGaze(
      AnimationsRegistrySeasonal.registerSidedSeparate("stella/gaze/stella_eye_right_left", "stella/gaze/stella_eye_left_left"),
      AnimationsRegistrySeasonal.registerSidedSeparate("stella/gaze/stella_eye_right_right", "stella/gaze/stella_eye_left_right"),
      AnimationsRegistrySeasonal.register("stella/gaze/stella_eyes_up"),
      AnimationsRegistrySeasonal.register("stella/gaze/stella_eyes_down")
   );
}
