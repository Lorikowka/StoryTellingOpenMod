package com.storyteam.storytelling.client.animations;

import com.storyteam.storytelling.api.Since;
import com.storyteam.storytelling.api.animations.SpecialAnim;
import com.storyteam.storytelling.client.AnimationsRegistrySeasonal;
import com.storyteam.storytelling.client.core.SidedAnimationSet;
import org.zeith.hammeranims.api.animation.IAnimationSource;

@Since(Since.Season.DS)
public interface AnimationsST6 {
   IAnimationSource LOW_SLAP = AnimationsRegistrySeasonal.register("low_slap_endanimated");
   IAnimationSource SUMMON_ARMS = AnimationsRegistrySeasonal.register("summon_arms");
   IAnimationSource ACHERON_ATTACK1 = AnimationsRegistrySeasonal.register("acheron_attack1");
   IAnimationSource ACHERON_ATTACK2 = AnimationsRegistrySeasonal.register("acheron_attack2");
   IAnimationSource BATTLE_RUN_CYCLE = AnimationsRegistrySeasonal.register("battle_run_cycle");
   IAnimationSource BATTLE_WALK_CYCLE = AnimationsRegistrySeasonal.register("battle_walk_cycle");
   IAnimationSource HOUND_ATTACK = AnimationsRegistrySeasonal.register("hound_attack");
   IAnimationSource KAFKA_ATTACK1 = AnimationsRegistrySeasonal.register("kafka_attack1");
   IAnimationSource NATASHA_COMBAT_POSE = AnimationsRegistrySeasonal.register("natasha_combat_pose");
   IAnimationSource BIG_BREATH = AnimationsRegistrySeasonal.register("humanoid/big_breath");
   IAnimationSource SOBBING_LAYER = AnimationsRegistrySeasonal.register("humanoid/sobbing_layer");
   IAnimationSource REVIVER_JUMP_UP = AnimationsRegistrySeasonal.register("humanoid/reviver_jump_up");
   IAnimationSource FALL_FORWARD = AnimationsRegistrySeasonal.register("humanoid/fall_forward");
   IAnimationSource FALL_FORWARD_TO_ROLL = AnimationsRegistrySeasonal.register("humanoid/fall_forward_to_roll");
   IAnimationSource FALL_TIRED = AnimationsRegistrySeasonal.register("humanoid/fall_tired");
   IAnimationSource ROLL_DODGE = AnimationsRegistrySeasonal.register("humanoid/roll_dodge");
   IAnimationSource FIREBALLS_THROW = AnimationsRegistrySeasonal.register("humanoid/fireballs_throw");
   IAnimationSource TRYING_TO_SEE = AnimationsRegistrySeasonal.register("humanoid/trying_to_see");
   IAnimationSource LAND_ON_GROUND = AnimationsRegistrySeasonal.register("humanoid/land_on_ground");
   IAnimationSource GLASSES_TAKEOFF_SCARED = AnimationsRegistrySeasonal.register("humanoid/glasses_takeoff_scared");
   IAnimationSource REVIVER_PULL_DAGGERS = AnimationsRegistrySeasonal.register("humanoid/reviver_pull_daggers");
   IAnimationSource PULL_DAGGERS = AnimationsRegistrySeasonal.register("humanoid/spin_pull_daggers");
   @SpecialAnim
   IAnimationSource REVIVER_AIR_THROW = AnimationsRegistrySeasonal.register("reviver_air_throw");
   IAnimationSource REVIVER_THINKING = AnimationsRegistrySeasonal.register("humanoid/reviver_thinking");
   IAnimationSource SIT_TO_PRAY = AnimationsRegistrySeasonal.register("humanoid/sit_to_pray");
   IAnimationSource FAST_RAISE_HANDS = AnimationsRegistrySeasonal.register("humanoid/fast_raise_hands");
   IAnimationSource LEVITATING_FORWARD = AnimationsRegistrySeasonal.register("levitating_forward");
   IAnimationSource SHAKE_HANDS_UP = AnimationsRegistrySeasonal.register("wavinghands_up");
   IAnimationSource KNEE_SNIFFING_GROUND = AnimationsRegistrySeasonal.register("knee_sniffing_ground");
   IAnimationSource WEAR_BRACERS = AnimationsRegistrySeasonal.register("wear_bracers");
   IAnimationSource REVIVER_BURNING_LINE_ATTACK = AnimationsRegistrySeasonal.register("humanoid/reviver_burning_line_attack");
   IAnimationSource STAND_ON_HANDS = AnimationsRegistrySeasonal.register("stand_on_hands");
   IAnimationSource SIT_GROUND_WITHOUT_HAND_KID = AnimationsRegistrySeasonal.register("body_endanimated_sitground_without_hands");
   IAnimationSource SIT_ON_GROUND_KID = AnimationsRegistrySeasonal.register("sit_on_ground_kid");
   IAnimationSource LOTUS_POSE_NOHANDS_KID = AnimationsRegistrySeasonal.register("lotus_pose_nohands_kid");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_OFFENDED = AnimationsRegistrySeasonal.register("newer_offended");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_DEPRESSION = AnimationsRegistrySeasonal.register("never_depression");
   IAnimationSource REVIVER_ATTACK1 = AnimationsRegistrySeasonal.register("reviver_attack1");
   IAnimationSource LAY_BACK_ATTACKING = AnimationsRegistrySeasonal.register("lay_back_attacking");
   IAnimationSource LAY_BACK_WITH_SWORD = AnimationsRegistrySeasonal.register("lay_back_with_sword");
   IAnimationSource DAMAGED_WALK = AnimationsRegistrySeasonal.register("damaged_walk");
   IAnimationSource STAND_FROM_BACK = AnimationsRegistrySeasonal.register("stand_from_back");
   IAnimationSource LEONORA_MOM_SKINT_CONSUME = AnimationsRegistrySeasonal.register("leonora_mom_recover");
   IAnimationSource LEONORA_MOM_CUTSCENE = AnimationsRegistrySeasonal.register("leonora_mom_harakiri");
   IAnimationSource REVIVER_DAGGER_PLAY = AnimationsRegistrySeasonal.register("reviver_dagger_play");
   IAnimationSource BEND_FORWARD_POSE = AnimationsRegistrySeasonal.register("humanoid/body_down");
   SidedAnimationSet REACH_HAND = AnimationsRegistrySeasonal.registerSided("reach_hand_right", "reach_hand_left", "reach_hand");
   SidedAnimationSet EXAMINE_ITEM_PULLED_HAND = AnimationsRegistrySeasonal.registerSided(
      "examine_item_pulled_hand_right", "examine_item_pulled_hand_left", "examine_item_pulled_hand"
   );
   SidedAnimationSet SMELL_ITEM_IN_HAND = AnimationsRegistrySeasonal.registerSided("smell_item_hand_right", "smell_item_hand_left", "smell_item_hand");
   SidedAnimationSet PRESENT_HAND = AnimationsRegistrySeasonal.registerSided("present_hand_right", "present_hand_left", "present_hand");
   IAnimationSource STRANGE_BLINKING = AnimationsRegistrySeasonal.register("strange_blinking");
   IAnimationSource TOUCHING_HEAD = AnimationsRegistrySeasonal.register("touching_head");
   IAnimationSource EMOTION_ORK_SHOCK = AnimationsRegistrySeasonal.register("ork_shocked");
   IAnimationSource CIRCLE_HAND_PASSES = AnimationsRegistrySeasonal.register("circle_hand_passes");
   IAnimationSource TRY_TO_STEP = AnimationsRegistrySeasonal.register("try_to_step");
   IAnimationSource STEP_TO_FALL = AnimationsRegistrySeasonal.register("step_to_fall");
   IAnimationSource SIT_ARMSBACK_TO_SITGROUND_NOHANDS = AnimationsRegistrySeasonal.register("sit_armsback_to_sitground_nohands");
   IAnimationSource WILD_LOOK_AROUND = AnimationsRegistrySeasonal.register("humanoid/wild_look_around");
   IAnimationSource BALCON_FORWARD_HAND_LEFT = AnimationsRegistrySeasonal.register("balcon_forward_hand_left");
   IAnimationSource KNEELS_DOWN = AnimationsRegistrySeasonal.register("humanoid/kneels_down");
   IAnimationSource KNEELS_DOWN_LEFT = AnimationsRegistrySeasonal.register("humanoid/kneels_down_left");
   IAnimationSource KNEELS_DOWN_RIGHT = AnimationsRegistrySeasonal.register("humanoid/kneels_down_right");
   IAnimationSource RAISE_CHIN = AnimationsRegistrySeasonal.register("humanoid/raise_chin");
   IAnimationSource HEADUP_CHIN = AnimationsRegistrySeasonal.register("humanoid/headup_chin");
   IAnimationSource POKING_IN_CHEST = AnimationsRegistrySeasonal.register("humanoid/poking_in_chest");
   IAnimationSource STUNNED_POSE = AnimationsRegistrySeasonal.register("humanoid/stunned_pose");
   IAnimationSource HOOD_FELL_OFF = AnimationsRegistrySeasonal.register("hood_fell_off");
   IAnimationSource ANGRY_SNORT = AnimationsRegistrySeasonal.register("angry_snort");
   IAnimationSource CHECK_POINTED_SWORD = AnimationsRegistrySeasonal.register("check_pointed_sword");
   IAnimationSource DAGGERS_ATTACK3 = AnimationsRegistrySeasonal.register("daggers_attack3");
   IAnimationSource DAGGERS_SPIN_ATTACK = AnimationsRegistrySeasonal.register("daggers_spin_attack");
   IAnimationSource DEAD_FALL = AnimationsRegistrySeasonal.register("dead_fall");
   IAnimationSource COVER_WITH_HANDS = AnimationsRegistrySeasonal.register("cover_with_hands");
   IAnimationSource HUG_ARMS_LAYER = AnimationsRegistrySeasonal.register("hug_arms_layer");
   SidedAnimationSet WEAR_HOOD_NEW = AnimationsRegistrySeasonal.registerSided("wear_hood_right", "wear_hood_left", "wear_hood");
   IAnimationSource CYCLE_PAT_DEER_RIGHT = AnimationsRegistrySeasonal.register("cycle_pat_deer_right");
   IAnimationSource FAINT_WHITE_EYES = AnimationsRegistrySeasonal.register("faint_white_eyes");
   IAnimationSource SUMMON_ARMS_FAST = AnimationsRegistrySeasonal.register("summon_arms_fast");
   IAnimationSource GENTLE_LEG_KICK = AnimationsRegistrySeasonal.register("gentle_leg_kick");
   IAnimationSource ROLL_ON_STOMACH = AnimationsRegistrySeasonal.register("roll_on_stomach");
   IAnimationSource SPIN_AROUND_CHECKING = AnimationsRegistrySeasonal.register("spin_around_checking");
   IAnimationSource LOOK_AT_HANDS_POSE = AnimationsRegistrySeasonal.register("look_at_hands_pose");
   IAnimationSource EMOTION_CUTE_NO_HANDS = AnimationsRegistrySeasonal.register("emotions/emotion_cute_nohands");
   IAnimationSource BENT_SIT_STAIRS_POSE = AnimationsRegistrySeasonal.register("bent_sit_stairs_pose");
   IAnimationSource CASUAL_STAIRS_SIT = AnimationsRegistrySeasonal.register("casual_stairs_sit");
   IAnimationSource CREATE_HANDLE_ROTATE = AnimationsRegistrySeasonal.register("create_handle_rotate");
   IAnimationSource CREATE_HANDLE_ROTATE_REVERSE = AnimationsRegistrySeasonal.register("create_handle_rotate_reverse");
   @SpecialAnim
   IAnimationSource LOOK_OVER_GLASSES = AnimationsRegistrySeasonal.register("humanoid/look_over_glasses_pose");
   IAnimationSource ANGRY_SHAKING_LAY_TO_SIT = AnimationsRegistrySeasonal.register("angry_shaking_lay_to_sit");
   IAnimationSource SIGH_NEW = AnimationsRegistrySeasonal.register("humanoid/inhale_breath");
   @SpecialAnim
   IAnimationSource COCOON_BOTTOM_BREAK = AnimationsRegistrySeasonal.register("cocoon_bottom_break");
   @SpecialAnim
   IAnimationSource REVIVER_WINGS_IDLE = AnimationsRegistrySeasonal.register("reviver_wings_idle");
   @SpecialAnim
   IAnimationSource REVIVER_BIRTH = AnimationsRegistrySeasonal.register("reviver_fall");
   @SpecialAnim
   IAnimationSource COCOON_SHAKE = AnimationsRegistrySeasonal.register("cocoon_shake");
   @SpecialAnim
   IAnimationSource COCOON_CRASH = AnimationsRegistrySeasonal.register("cocoon_crash");
   IAnimationSource REVIVER_FIRST_FLAMES = AnimationsRegistrySeasonal.register("reviver_hands_flames");
   IAnimationSource REVIVER_SWORD_OVERHEAD = AnimationsRegistrySeasonal.register("reviver_pervades_pose");
   IAnimationSource MARC_LARVA_SWORD_OVERHEAD = AnimationsRegistrySeasonal.register("fetus_pervades");
   @SpecialAnim
   IAnimationSource MARC_LARVA_FALL_TO_KNEES = AnimationsRegistrySeasonal.register("fetus_fall_tired");
   @SpecialAnim
   IAnimationSource JUMP_UP_TRY_REACH = AnimationsRegistrySeasonal.register("jump_up_try_reach");
   @SpecialAnim
   IAnimationSource SWORD_FLY_UP = AnimationsRegistrySeasonal.register("sword_fly_up");
   @SpecialAnim
   IAnimationSource CATAPULT_ATTACK = AnimationsRegistrySeasonal.register("catapult_attack");
   @SpecialAnim
   IAnimationSource CATAPULT_PREPARE = AnimationsRegistrySeasonal.register("catapult_prepare");
   @SpecialAnim
   IAnimationSource CATAPULT_WALK = AnimationsRegistrySeasonal.register("catapult_walk");
   @SpecialAnim
   IAnimationSource CATAPULT_BREAK = AnimationsRegistrySeasonal.register("catapult_break");
   IAnimationSource HOLD_BOTTOM_LAYER = AnimationsRegistrySeasonal.register("hold_bottom_layer");
   IAnimationSource SCINTOSAURUS_LANDING = AnimationsRegistrySeasonal.register("scintosaurus_landing");
   IAnimationSource MODEL_ANGLE_37_5 = AnimationsRegistrySeasonal.register("model_angle_37_5");
   @SpecialAnim
   IAnimationSource FATHER_SPEAR_SPAWN = AnimationsRegistrySeasonal.register("father_spear_spawn");
   IAnimationSource PAT_LAYING_DOWN = AnimationsRegistrySeasonal.register("humanoid/lay_down_pats");
   IAnimationSource TAKEN_FROM_FLOOR = AnimationsRegistrySeasonal.register("humanoid/taked_from_floor");
   IAnimationSource TAKE_BODY_FROM_FLOOR = AnimationsRegistrySeasonal.register("humanoid/take_body_from_floor");
   IAnimationSource TAKEN_BODY_POSE = AnimationsRegistrySeasonal.register("humanoid/body_taken_pose");
   IAnimationSource SCREAMER_TURN = AnimationsRegistrySeasonal.register("screamer_turn");
   IAnimationSource SIT_ON_BED = AnimationsRegistrySeasonal.register("sit_on_bed");
   IAnimationSource SIT_ON_BED_BEND = AnimationsRegistrySeasonal.register("sit_on_bed_bend");
   IAnimationSource STAND_ON_HANDS_LOTUS = AnimationsRegistrySeasonal.register("stand_on_hands_lotus");
   IAnimationSource BLIND_SEEKING = AnimationsRegistrySeasonal.register("blind_seeking");
   @SpecialAnim
   IAnimationSource ALFRED_POSSESSION = AnimationsRegistrySeasonal.register("alfred_possession");
   @SpecialAnim
   IAnimationSource ALFRED_POSSESSION_CRAWL = AnimationsRegistrySeasonal.register("alfred_possession_crawl");
   @SpecialAnim
   IAnimationSource DIZZY_STANDING = AnimationsRegistrySeasonal.register("dizzy_standing");
   @SpecialAnim
   IAnimationSource LEAN_LAY_STOMACH_TO_WALL_SIT = AnimationsRegistrySeasonal.register("lean_lay_stomach_to_wall_sit");
   @SpecialAnim
   IAnimationSource LOOKING_AROUND_BLINKING = AnimationsRegistrySeasonal.register("looking_around_blinking");
   @SpecialAnim
   IAnimationSource SQUAT_CHEST_OPENING = AnimationsRegistrySeasonal.register("squat_chest_opening");
   @SpecialAnim
   IAnimationSource TOUCH_CHEST_CRYSTAL = AnimationsRegistrySeasonal.register("touch_chest_crystal");
   @SpecialAnim
   IAnimationSource WRITING_AND_THINKING_CYCLE = AnimationsRegistrySeasonal.register("writing_and_thinking_cycle");
   IAnimationSource VIOLENT_HEAD_SHAKING = AnimationsRegistrySeasonal.register("violent_head_shaking");
   @SpecialAnim
   IAnimationSource CLOUD_IDLE = AnimationsRegistrySeasonal.register("cloud/cloud_idle");
   @SpecialAnim
   IAnimationSource CLOUD_INSPIRATION = AnimationsRegistrySeasonal.register("cloud/cloud_happy_emotion");
   @SpecialAnim
   IAnimationSource CLOUD_CIRCLING_CYCLE = AnimationsRegistrySeasonal.register("cloud/cloud_circling_cycle");
   @SpecialAnim
   IAnimationSource CLOUD_WIGGLING = AnimationsRegistrySeasonal.register("cloud/cloud_wiggling");
   @SpecialAnim
   IAnimationSource CLOUD_ANGRY = AnimationsRegistrySeasonal.register("cloud/cloud_angry");
   @SpecialAnim
   IAnimationSource CLOUD_CLOSE_EYES = AnimationsRegistrySeasonal.register("cloud/cloud_close_eyes");
   @SpecialAnim
   IAnimationSource CLOUD_EAT = AnimationsRegistrySeasonal.register("cloud/cloud_eat");
   @SpecialAnim
   IAnimationSource CLOUD_HAPPY = AnimationsRegistrySeasonal.register("cloud/cloud_happy");
   @SpecialAnim
   IAnimationSource CLOUD_JOY = AnimationsRegistrySeasonal.register("cloud/cloud_joy");
   IAnimationSource TREE_GROW_PORTRAY = AnimationsRegistrySeasonal.register("tree_grow_portray");
   IAnimationSource ZOMBIE_POSING = AnimationsRegistrySeasonal.register("zombie_posing");
   IAnimationSource BRACER_BLADE_APPEAR = AnimationsRegistrySeasonal.register("bracer_blade_appear");
   IAnimationSource SHIELD_POSE = AnimationsRegistrySeasonal.register("humanoid/shield_standing_pose");
   IAnimationSource ZOMBIE_POSING_MOVING = AnimationsRegistrySeasonal.register("zombie_posing_moving");
   IAnimationSource TOSSING_AND_TURNING_LAYER = AnimationsRegistrySeasonal.register("tossing_and_turning_layer");
   IAnimationSource BUTT_KICK_JUMP = AnimationsRegistrySeasonal.register("butt_kick_jump");
   IAnimationSource DEER_HELD_BY_BACK = AnimationsRegistrySeasonal.register("deer_held_by_back");
   IAnimationSource DEER_FALL_ENDANIMATED = AnimationsRegistrySeasonal.register("deer_fall_endanimated");
   IAnimationSource DEER_HELD_BY_BACK_CYCLE = AnimationsRegistrySeasonal.register("deer_held_by_back_cycle");
   IAnimationSource DEER_MOVING_LAYER = AnimationsRegistrySeasonal.register("deer_body_moving_layer");
   IAnimationSource TIRED_LEG_LEAN = AnimationsRegistrySeasonal.register("humanoid/tired_leg_lean");
   @SpecialAnim
   IAnimationSource GROW_SCINT_FLYING = AnimationsRegistrySeasonal.register("soaring_germination_scint");
   @SpecialAnim
   IAnimationSource UNGROW_SCINT_FLYING = AnimationsRegistrySeasonal.register("back_germination_scint");
   @SpecialAnim
   IAnimationSource NO_SCINT = AnimationsRegistrySeasonal.register("no_scint_pose");
   IAnimationSource UNGROW_SCINT_FALL = AnimationsRegistrySeasonal.register("falling_body");
   IAnimationSource CAUGHT = AnimationsRegistrySeasonal.register("caught_body");
   IAnimationSource SHOCKWAVE_FALL_BACK_SIT = AnimationsRegistrySeasonal.register("humanoid/shockwave_fall_back_to_sit");
   IAnimationSource FLOATING_IDLE = AnimationsRegistrySeasonal.register("humanoid/soaring_idle");
   IAnimationSource SHOOTING_SPELLS_CYCLE = AnimationsRegistrySeasonal.register("humanoid/shooting_spells_cycle_layer");
   @SpecialAnim
   IAnimationSource CROW_CALL_WITH_WINGS = AnimationsRegistrySeasonal.register("crow_call_with_wings");
   @SpecialAnim
   IAnimationSource CROW_CALL_WITH_WINGS_RIGHT = AnimationsRegistrySeasonal.register("crow_call_with_wings_right");
   @SpecialAnim
   IAnimationSource CROW_EMOTION_HAPPY = AnimationsRegistrySeasonal.register("crow_emotion_happy");
   @SpecialAnim
   IAnimationSource CROW_EMOTION_HOPE = AnimationsRegistrySeasonal.register("crow_emotion_hope");
   @SpecialAnim
   IAnimationSource CROW_GO_SKIPPING = AnimationsRegistrySeasonal.register("crow_go_skipping");
   @SpecialAnim
   IAnimationSource CROW_LOST_HAT = AnimationsRegistrySeasonal.register("crow_lost_hat");
   @SpecialAnim
   IAnimationSource CROW_PICKUP_HAT = AnimationsRegistrySeasonal.register("crow_pickup_hat");
   @SpecialAnim
   IAnimationSource CROW_WEAR_HAT = AnimationsRegistrySeasonal.register("crow_wear_hat");
   @SpecialAnim
   IAnimationSource CROW_TREE_CLIMBING = AnimationsRegistrySeasonal.register("crow_tree_climbing");
   @SpecialAnim
   IAnimationSource CROW_TREE_FALL_POSE = AnimationsRegistrySeasonal.register("crow_tree_fall_pose");
   @SpecialAnim
   IAnimationSource CROW_TREE_LANDING = AnimationsRegistrySeasonal.register("crow_tree_landing");
   @SpecialAnim
   IAnimationSource CROW_LAUGH_CYCLE = AnimationsRegistrySeasonal.register("crow_laugh_cycle");
   @SpecialAnim
   IAnimationSource CROW_HANDS_ON_STOMACH = AnimationsRegistrySeasonal.register("crow_hands_on_stomach");
   @SpecialAnim
   IAnimationSource CROW_HUG_DOWN = AnimationsRegistrySeasonal.register("crow_hug_down");
   @SpecialAnim
   IAnimationSource KARDINAL_THINKING_CYCLE = AnimationsRegistrySeasonal.register("kardinal_thinking_cycle");
   @SpecialAnim
   IAnimationSource DWARF_EMOTION_RAGE = AnimationsRegistrySeasonal.register("dwarf_emotion_rage");
   @SpecialAnim
   IAnimationSource DWARF_SIT_ON_GROUND = AnimationsRegistrySeasonal.register("dwarf_sit_on_ground");
   IAnimationSource SHAKE_HAT = AnimationsRegistrySeasonal.register("berengariy_shake_hat");
   IAnimationSource SIT_GET_SLAPPED_FROM_SIDE = AnimationsRegistrySeasonal.register("sit_get_slapped_from_side");
   IAnimationSource STAND_ON_SIDE_SLAP_LOWER = AnimationsRegistrySeasonal.register("stand_on_side_slap_lower");
   IAnimationSource EMOTION_AGGRESSION = AnimationsRegistrySeasonal.register("emotion_aggression", "emotion_aggression");
   IAnimationSource BODY_TAKE_BIGBLOCK_ENDANIMATED = AnimationsRegistrySeasonal.register("humanoid/body_take_bigblock_endanimated");
   IAnimationSource SIT_GET_SLAPPED_FROM_SIDE_LAYERED = AnimationsRegistrySeasonal.register("sit_get_slapped_from_side_layered");
   IAnimationSource ARMS_REACH_FORWARD_ENDANIMATED = AnimationsRegistrySeasonal.register("arms_reach_forward_endanimated");
   IAnimationSource CHILD_HOLDING_HAND_GRIEF = AnimationsRegistrySeasonal.register("child_holding_hand_grief");
   IAnimationSource CRADLING_BABY_BOTH_HANDS_CYCLE = AnimationsRegistrySeasonal.register("cradling_baby_both_hands_cycle");
   IAnimationSource ELF_HANDS_ENDANIMATED_TABLEHIT_ARMSCROSSED = AnimationsRegistrySeasonal.register("elf_hands_endanimated_tablehit_armscrossed");
   IAnimationSource GENTLE_LAY_ARM_FROM_BEHIND_ENDANIMATED = AnimationsRegistrySeasonal.register("gentle_lay_arm_from_behind_endanimated");
   IAnimationSource LAYING_PUKE_LAYERED_ENDANIMATED = AnimationsRegistrySeasonal.register("laying_puke_layered_endanimated");
   IAnimationSource LAY_BAG_ON_TABLE_ENDANIMATED = AnimationsRegistrySeasonal.register("lay_bag_on_table_endanimated");
   IAnimationSource FIRST_MOTHER_IDLE = AnimationsRegistrySeasonal.register("mother_idle");
   IAnimationSource FIRST_MOTHER_SHARE_BLOOD = AnimationsRegistrySeasonal.register("mother_share_blood");
   IAnimationSource SIT_ON_BED_POSE = AnimationsRegistrySeasonal.register("sit_on_bed_pose");
   IAnimationSource ELF_STAFF_CEREMONY = AnimationsRegistrySeasonal.register("elf_staff_ceremony");
   IAnimationSource MOTHER_HURT = AnimationsRegistrySeasonal.register("mother_hurt");
   IAnimationSource FATHER_SPEAR_PIERCE = AnimationsRegistrySeasonal.register("father_spear_pierce");
   IAnimationSource SPEAR_TO_BACK_RIGHT = AnimationsRegistrySeasonal.register("spear_to_back_right");
   IAnimationSource FOXYFINE_HAT_SEARCH_START = AnimationsRegistrySeasonal.register("foxyfine_hat_search_start");
   IAnimationSource FOXYFINE_HAT_SEARCH_CYCLE = AnimationsRegistrySeasonal.register("foxyfine_hat_search_cycle");
   IAnimationSource FOXYFINE_HAT_SCINT_CORRUPT = AnimationsRegistrySeasonal.register("foxyfine_hat_scint_corrupt");
   IAnimationSource VAN_HAT_HILL = AnimationsRegistrySeasonal.register("van_hat_hill");
   IAnimationSource SASHA_TAIL_WIGGLE = AnimationsRegistrySeasonal.register("sasha_tail_wiggle");
   IAnimationSource HAKAGRON_ARMS_CROSSED = AnimationsRegistrySeasonal.register("hakagron_arms_crossed");
   IAnimationSource RAT_TAIL_WIGGLE = AnimationsRegistrySeasonal.register("rat_tail_wiggle");
   IAnimationSource RAT_JUMP = AnimationsRegistrySeasonal.register("rat_jump");
   IAnimationSource VEBSTE_HEAD_IDLE = AnimationsRegistrySeasonal.register("vebste_head_idle");
   IAnimationSource FREEDOMDEATH_SIGMA = AnimationsRegistrySeasonal.register("freedomdeath_sigma");
   IAnimationSource MORGAN_ENDERMAN_APPEAR = AnimationsRegistrySeasonal.register("morgan_enderman_appear");
   IAnimationSource MORGAN_FINGER_SNAP = AnimationsRegistrySeasonal.register("morgan_finger_snap");
   IAnimationSource MORGAN_HANDS_ON_CHEST = AnimationsRegistrySeasonal.register("morgan_hands_on_chest");
   IAnimationSource MORGAN_HANDS_ON_CHEST_SPLASH = AnimationsRegistrySeasonal.register("morgan_hands_on_chest_splash");
   IAnimationSource MORGAN_LAUGH = AnimationsRegistrySeasonal.register("morgan_laugh");
   IAnimationSource MORGAN_OBJECTS_ANIMATION = AnimationsRegistrySeasonal.register("morgan_objects_animation");
   IAnimationSource MORGAN_PAINTING_FALL = AnimationsRegistrySeasonal.register("morgan_painting_fall");
   IAnimationSource MORGAN_SCRATCH_HEAD = AnimationsRegistrySeasonal.register("morgan_scratch_head");
   IAnimationSource MORGAN_SHOW_BACK = AnimationsRegistrySeasonal.register("morgan_show_back");
   IAnimationSource BEAR_BOW_LAYER = AnimationsRegistrySeasonal.register("bear_bow_layer");
   IAnimationSource BEAR_CATAPULT_LAUNCH_LAYER = AnimationsRegistrySeasonal.register("bear_catapult_launch_layer");
   IAnimationSource BEAR_HANDS_UP_LAYER = AnimationsRegistrySeasonal.register("bear_hands_up_layer");
   IAnimationSource BEAR_IN_CATAPULT_ROAR_LAYER = AnimationsRegistrySeasonal.register("bear_in_catapult_roar_layer");
   IAnimationSource BEAR_SALUTE_LAYER = AnimationsRegistrySeasonal.register("bear_salute_layer");
   IAnimationSource BEAR_SIT_ON_BACK = AnimationsRegistrySeasonal.register("bear_sit_on_back");
   IAnimationSource WERMUS_MOOSE_SIT_POSE = AnimationsRegistrySeasonal.register("wermus_moose_sit_pose");
   IAnimationSource WERMUS_SIT_POSE = AnimationsRegistrySeasonal.register("wermus_sit_pose");
   @SpecialAnim("ZEITHERON")
   IAnimationSource ZEITH_TAIL_IDLE = AnimationsRegistrySeasonal.register("zeith_extras", "zeith_tail_idle");
   @SpecialAnim("ZEITHERON")
   IAnimationSource ZEITH_TAIL_WHEN_SIDE_LAY = AnimationsRegistrySeasonal.register("zeith_extras", "zeith_tail_when_side_lay");
   @SpecialAnim("ZEITHERON")
   IAnimationSource ZEITH_EARS_PRESSED_AGAINST_GROUND = AnimationsRegistrySeasonal.register("zeith_extras", "ears_pressed_against_ground");
   @SpecialAnim("ZEITHERON")
   IAnimationSource ZEITH_EARS_TWITCH = AnimationsRegistrySeasonal.register("zeith_extras", "ears_twitch");
   @SpecialAnim("ZEITHERON")
   IAnimationSource ZEITH_TAIL_RAISE = AnimationsRegistrySeasonal.register("zeith_extras", "tail_raise");
   @SpecialAnim
   IAnimationSource CRAZY_WHITE_EYES = AnimationsRegistrySeasonal.register("humanoid/crazy_white_eyes");
   @SpecialAnim
   IAnimationSource SCREW_LIGHT_ROOF = AnimationsRegistrySeasonal.register("humanoid/screw_light_roof");
   @SpecialAnim
   IAnimationSource SLEEP_SCINT_RIGHT = AnimationsRegistrySeasonal.register("humanoid/sleep_scint_right");
   @SpecialAnim
   IAnimationSource WAVING_ITEMS_UP = AnimationsRegistrySeasonal.register("humanoid/waving_items_up");
   @SpecialAnim
   IAnimationSource BISHOP_POSE = AnimationsRegistrySeasonal.register("humanoid/bishop_pose");
   @SpecialAnim
   IAnimationSource GUIDING_CLEAR_SCINT_POSE = AnimationsRegistrySeasonal.register("clear_scint_pose");
   @SpecialAnim
   IAnimationSource GUIDING_DIRTY_SCINT_POSE = AnimationsRegistrySeasonal.register("dirty_scint_pose");
   @SpecialAnim
   IAnimationSource GUIDING_SCINT_ACTIVATION = AnimationsRegistrySeasonal.register("guiding_scint_activation");
   @SpecialAnim
   IAnimationSource SPARK_PLAYER_IDLE = AnimationsRegistrySeasonal.register("spark_idle");
   @SpecialAnim
   IAnimationSource BUMP_INTO_WALL = AnimationsRegistrySeasonal.register("humanoid/bump_into_wall");
   @SpecialAnim
   IAnimationSource FUMBLING_CYCLE = AnimationsRegistrySeasonal.register("fumbling_cycle");
   IAnimationSource HEAVY_BREATH_CYCLE = AnimationsRegistrySeasonal.register("humanoid/heavy_breath_cycle");
   IAnimationSource LOTUS_HANDS_STAND_SPIN = AnimationsRegistrySeasonal.register("humanoid/lotus_hands_stand_spin");
   IAnimationSource DODGE_BACK = AnimationsRegistrySeasonal.register("humanoid/dodge_back");
   IAnimationSource DODGE_ROLL_BACK = AnimationsRegistrySeasonal.register("humanoid/dodge_roll_back");
   IAnimationSource CROW_WALK = AnimationsRegistrySeasonal.register("crow_boss/crow_walk");
   IAnimationSource CROW_ATTACKED1 = AnimationsRegistrySeasonal.register("crow_boss/crow_attacked", "attacked_layer1");
   IAnimationSource CROW_ATTACKED2 = AnimationsRegistrySeasonal.register("crow_boss/crow_attacked", "attacked_layer2");
   IAnimationSource CROW_ATTACKED3 = AnimationsRegistrySeasonal.register("crow_boss/crow_attacked", "attacked_layer3");
   IAnimationSource CROW_ATTACKED4 = AnimationsRegistrySeasonal.register("crow_boss/crow_attacked", "attacked_layer4");
   IAnimationSource CROW_BEAK_ATTACK1 = AnimationsRegistrySeasonal.register("crow_boss/crow_beak_attacks", "crow_beak_attack1");
   IAnimationSource CROW_BEAK_ATTACK2 = AnimationsRegistrySeasonal.register("crow_boss/crow_beak_attacks", "crow_beak_attack2");
   IAnimationSource CROW_FLY = AnimationsRegistrySeasonal.register("crow_boss/crow_fly");
   IAnimationSource CROW_JUMP = AnimationsRegistrySeasonal.register("crow_boss/crow_jump");
   IAnimationSource CROW_SMASH_ATTACK = AnimationsRegistrySeasonal.register("crow_boss/crow_smash_attack");
   IAnimationSource CROW_STAND = AnimationsRegistrySeasonal.register("crow_boss/crow_stand");
   IAnimationSource CROW_TAKE_OFF = AnimationsRegistrySeasonal.register("crow_boss/crow_take_off");
   IAnimationSource CROW_TWO_WINGS_ATTACK = AnimationsRegistrySeasonal.register("crow_boss/crow_two_wings_attack");
   IAnimationSource CROW_WING_ATTACK_RIGHT = AnimationsRegistrySeasonal.register("crow_boss/crow_wing_attacks", "crow_wingattack_right_layer");
   IAnimationSource CROW_WING_ATTACK_LEFT = AnimationsRegistrySeasonal.register("crow_boss/crow_wing_attacks", "crow_wingattack_left_layer");
   IAnimationSource CROW_DEATH = AnimationsRegistrySeasonal.register("crow_boss/crow_death");
   IAnimationSource SPIN_AROUND = AnimationsRegistrySeasonal.register("spin_around");
   IAnimationSource HOLDED_SHAKING_CYCLE = AnimationsRegistrySeasonal.register("holded_shaking_cycle");
   IAnimationSource CITY_BIG_RUN_NOHANDS = AnimationsRegistrySeasonal.register("city_big_run_nohands");
   IAnimationSource PICKS_GIRL_UP_POSE = AnimationsRegistrySeasonal.register("picks_girl_up_pose");
   IAnimationSource PICKS_GIRL_UP_STRIKER = AnimationsRegistrySeasonal.register("picks_girl_up_striker");
   IAnimationSource PICKS_GIRL_UP_VICTIM = AnimationsRegistrySeasonal.register("picks_girl_up_victim");
   @SpecialAnim
   IAnimationSource LEONORA_TRANSFORM = AnimationsRegistrySeasonal.register("girl_transform");
   @SpecialAnim
   IAnimationSource LEONORA_TRANSFORM_POSE = AnimationsRegistrySeasonal.register("girl_transform_pose_idle");
   @SpecialAnim
   IAnimationSource LEONORA_SPIDER_WALK = AnimationsRegistrySeasonal.register("girl_spider_walk_layer");
   @SpecialAnim
   IAnimationSource LEONORA_SPIDER_ATTACK = AnimationsRegistrySeasonal.register("girl_spider_attack_layer");
   @SpecialAnim
   IAnimationSource POINT_SWORD_DOWN = AnimationsRegistrySeasonal.register("point_sword_down");
   @SpecialAnim
   IAnimationSource POINT_SWORD_FORWARD = AnimationsRegistrySeasonal.register("point_sword_forward");
   @SpecialAnim
   IAnimationSource HORSE_BRAKE = AnimationsRegistrySeasonal.register("horse_brake");
   @SpecialAnim
   IAnimationSource HORSE_CONTROLING_POSE = AnimationsRegistrySeasonal.register("horse_controling", "horse_controling_pose");
   @SpecialAnim
   IAnimationSource HORSE_CONTROLING = AnimationsRegistrySeasonal.register("horse_controling", "horse_controling");
   @SpecialAnim
   IAnimationSource WAYWARD_SCINT_ACTIVATION = AnimationsRegistrySeasonal.register("all_scint_activation");
   @SpecialAnim("CITY_GATES")
   IAnimationSource CITY_GATES_OPEN = AnimationsRegistrySeasonal.register("city_gates_open");
   @SpecialAnim
   IAnimationSource SCARECROW_IDLE = AnimationsRegistrySeasonal.register("scarecrow_idle");
   @SpecialAnim
   IAnimationSource SCARECROW_DAMAGE = AnimationsRegistrySeasonal.register("scarecrow_damage");
   IAnimationSource PANIC_SHAKING = AnimationsRegistrySeasonal.register("panic_shaking");
   IAnimationSource HANDS_TO_EYES_SITTING = AnimationsRegistrySeasonal.register("hands_to_eyes_sitting");
   IAnimationSource WALL_WOUND_CARE = AnimationsRegistrySeasonal.register("wall_wound_care");
   IAnimationSource SWORD_THREATEN_POSE = AnimationsRegistrySeasonal.register("sword_threat_pose");
   IAnimationSource HALBERD_THREATEN_POSE = AnimationsRegistrySeasonal.register("halberd_threat_pose");
   IAnimationSource TRY_GET_UP_FROM_BACK_LAYER = AnimationsRegistrySeasonal.register("humanoid/try_get_up_from_back_layer");
   IAnimationSource SIT_TO_STAND_BROKEN_LEG = AnimationsRegistrySeasonal.register("humanoid/sit_to_stand_broken_leg");
   SidedAnimationSet SHOW_LEG = AnimationsRegistrySeasonal.registerSided("show_leg_right", "show_leg_left", "humanoid/show_leg");
   IAnimationSource SLIGHT_DAMAGED_WALK = AnimationsRegistrySeasonal.register("humanoid/slight_damaged_walk");
   IAnimationSource PULL_SWORD_RIGHT = AnimationsRegistrySeasonal.register("humanoid/pull_sword_right");
   IAnimationSource EGG_CRUSH = AnimationsRegistrySeasonal.register("humanoid/egg_crush");
   IAnimationSource CUT_FINGER_WITH_KNIFE = AnimationsRegistrySeasonal.register("cut_finger_with_knife");
   IAnimationSource HAMMER_WALL = AnimationsRegistrySeasonal.register("hammer_wall");
   IAnimationSource HANDSHOLDSTOMACH = AnimationsRegistrySeasonal.register("hands_hold_stomach");
   IAnimationSource VASE_TAKE = AnimationsRegistrySeasonal.register("vase_take");
   SidedAnimationSet ITEM_TO_BACK = AnimationsRegistrySeasonal.registerSided("item_to_back_right", "item_to_back_left", "humanoid/item_to_back");
   IAnimationSource PROUD_POSE = AnimationsRegistrySeasonal.register("humanoid/proud_pose");
   IAnimationSource POINT_AXE_FORWARD = AnimationsRegistrySeasonal.register("humanoid/point_axe_forward");
   IAnimationSource AXE_PUNCH_GROUND = AnimationsRegistrySeasonal.register("humanoid/axe_punch_ground");
   IAnimationSource WEAPON_ON_SHOULDER = AnimationsRegistrySeasonal.register("humanoid/weapon_on_shoulder");
   IAnimationSource SURPRISED_WAKE_UP = AnimationsRegistrySeasonal.register("humanoid/surprised_wake_up");
   IAnimationSource ANT_ATTACK_1 = AnimationsRegistrySeasonal.register("giant_ant/ant_attack1");
   IAnimationSource ANT_ATTACK_2 = AnimationsRegistrySeasonal.register("giant_ant/ant_attack2");
   IAnimationSource ANT_DEATH = AnimationsRegistrySeasonal.register("giant_ant/ant_death");
   IAnimationSource ANT_IDLE_CYCLE = AnimationsRegistrySeasonal.register("giant_ant/ant_idle_cycle");
   IAnimationSource ANT_IDLE_POSE = AnimationsRegistrySeasonal.register("giant_ant/ant_idle_pose");
   IAnimationSource ANT_WALK_CYCLE = AnimationsRegistrySeasonal.register("giant_ant/ant_walk_cycle");
   SidedAnimationSet AXEMAN_ATTACK1 = AnimationsRegistrySeasonal.registerSided("axeman_attack1_right", "axeman_attack1_left", "axeman_attack1");
   IAnimationSource HAMMERMAN_ATTACK1 = AnimationsRegistrySeasonal.register("hammerman_attack1");
   IAnimationSource HAMMERMAN_ATTACK2 = AnimationsRegistrySeasonal.register("hammerman_attack2");
   IAnimationSource EXTINGUISHING_CYCLE = AnimationsRegistrySeasonal.register("extinguishing_cycle");
   IAnimationSource LAY_ON_GROUND_OPEN = AnimationsRegistrySeasonal.register("lay_on_ground_open");
   IAnimationSource LAY_TO_SHAKING_SIT = AnimationsRegistrySeasonal.register("lay_to_shaking_sit");
   IAnimationSource STAND_ON_KNEES_HEROIC = AnimationsRegistrySeasonal.register("stand_on_knees_heroic");
   IAnimationSource TRADER_CAPE_OPEN = AnimationsRegistrySeasonal.register("trader_cape_open");
   IAnimationSource CHECKING_AROUND_LOOK = AnimationsRegistrySeasonal.register("checking_around_look");
   IAnimationSource DAGGER_JUMP_ATTACK1_LAYER = AnimationsRegistrySeasonal.register("ork_dagger/dagger_jump_attack1_layer");
   IAnimationSource DAGGER_JUMP_ATTACK2_LAYER = AnimationsRegistrySeasonal.register("ork_dagger/dagger_jump_attack2_layer");
   IAnimationSource DAGGERS_ATTACK1_LAYER = AnimationsRegistrySeasonal.register("ork_dagger/daggers_attack1_layer");
   IAnimationSource DAGGERS_ATTACK2_LAYER = AnimationsRegistrySeasonal.register("ork_dagger/daggers_attack2_layer");
   IAnimationSource DAGGERS_POSE = AnimationsRegistrySeasonal.register("ork_dagger/daggers_pose");
   IAnimationSource STAND_TO_DAGGERS_POSE = AnimationsRegistrySeasonal.register("ork_dagger/stand_to_daggers_pose");
   IAnimationSource NO_ORK_DAGGERS = AnimationsRegistrySeasonal.register("ork_dagger/no_ork_daggers");
   IAnimationSource ORK_BACK_JUMP_LAYER = AnimationsRegistrySeasonal.register("ork_dagger/ork_back_jump_layer");
   IAnimationSource TRY_TO_LIFT_CYCLE = AnimationsRegistrySeasonal.register("try_to_lift_cycle");
   IAnimationSource BIG_BAG_TAKE = AnimationsRegistrySeasonal.register("big_bag_take");
   IAnimationSource SHARPENING_AXE = AnimationsRegistrySeasonal.register("sharpening_axe");
   IAnimationSource ELBOW_GET_UP = AnimationsRegistrySeasonal.register("elbow_get_up");
   IAnimationSource BODY_WHEEL_NEW = AnimationsRegistrySeasonal.register("body_wheel_new");
   IAnimationSource TOSSING_AND_TURNING = AnimationsRegistrySeasonal.register("tossing_and_turning");
   IAnimationSource JDH_GLASSES_CHECK_SHORT = AnimationsRegistrySeasonal.register("jdh_glasses_check_short");
   IAnimationSource JDH_GLASSES_CHECK_START = AnimationsRegistrySeasonal.register("jdh_glasses_check_start");
   IAnimationSource JDH_GLASSES_CHECK_CYCLE = AnimationsRegistrySeasonal.register("jdh_glasses_check_cycle");
   IAnimationSource JDH_GLASSES_CHECK_ENDING = AnimationsRegistrySeasonal.register("jdh_glasses_check_ending");
   IAnimationSource JDH_GLASSES_TO_POCKET = AnimationsRegistrySeasonal.register("jdh_glasses_to_pocket");
   IAnimationSource JDH_LOWER_GLASSES = AnimationsRegistrySeasonal.register("jdh_lower_glasses");
   IAnimationSource JDH_SLEEPY_EMOTE = AnimationsRegistrySeasonal.register("jdh_sleepy_emote");
   IAnimationSource CRYBABY_CYCLE_KID = AnimationsRegistrySeasonal.register("crybaby_cycle_kid");
   IAnimationSource SQUAT_TAKE_FROM_GROUND_BOTH_HANDS = AnimationsRegistrySeasonal.register("squat_take_from_ground_both_hands");
   IAnimationSource DWARF_SQUAT_TAKE_FROM_GROUND_BOTH_HANDS = AnimationsRegistrySeasonal.register("dwarf_squat_take_from_ground_both_hands");
   IAnimationSource SWORD_ATTACK_RIGHT = AnimationsRegistrySeasonal.register("sword_attack_right");
   IAnimationSource SWORD_ATTACK_LEFT = AnimationsRegistrySeasonal.register("sword_attack_left");
   SidedAnimationSet BROKEN_ARM = AnimationsRegistrySeasonal.registerSided("broken_arm_right", "broken_arm_left", "humanoid/broken_arm");
   @SpecialAnim
   IAnimationSource BEAR_FIGHT = AnimationsRegistrySeasonal.register("bear_fight");
   @SpecialAnim
   IAnimationSource CRYSTAL_CHEST_GROWTH = AnimationsRegistrySeasonal.register("crystal_chest_growth");
   SidedAnimationSet LAY_HOLD_SIDE = AnimationsRegistrySeasonal.registerSided("lay_hold_side_right", "lay_hold_side_left", "lay_hold_side");
   IAnimationSource LAY_ON_GROUND = AnimationsRegistrySeasonal.register("lay_on_ground");
   @SpecialAnim
   IAnimationSource NO_CRYSTAL = AnimationsRegistrySeasonal.register("no_crystal_pose");
   IAnimationSource BEAR_DEATH_POSE = AnimationsRegistrySeasonal.register("bear_death_pose");
   IAnimationSource COUGHING_LAYER = AnimationsRegistrySeasonal.register("coughing_layer");
   IAnimationSource SICK_LAY_TURNING_CYCLE = AnimationsRegistrySeasonal.register("sick_lay_turning_cycle");
   SidedAnimationSet LAY_REMOVE_HOLD_SIDE = AnimationsRegistrySeasonal.registerSided(
      "lay_remove_hold_side_right", "lay_remove_hold_side_left", "lay_remove_hold_side"
   );
   SidedAnimationSet FINGER_UP = AnimationsRegistrySeasonal.registerSided("finger_up_right", "finger_up_left", "finger_up");
   SidedAnimationSet HAND_TO_CHEST = AnimationsRegistrySeasonal.registerSided("hand_to_chest_right", "hand_to_chest_left", "hand_to_chest");
   @SpecialAnim
   IAnimationSource BEARTRAP_ACTIVATE = AnimationsRegistrySeasonal.register("beartrap_activate");
   @SpecialAnim
   IAnimationSource BEARTRAP_ACTIVATE_POSE = AnimationsRegistrySeasonal.register("beartrap_activate_pose");
   IAnimationSource SAYRISSA_TAKE_HEAD = AnimationsRegistrySeasonal.register("sayrissa_take_head");
   IAnimationSource CASUAL_LAY = AnimationsRegistrySeasonal.register("casual_lay");
   IAnimationSource SEXY_SIT_TO_CASUAL_LAY = AnimationsRegistrySeasonal.register("sexy_sit_to_casual_lay");
   @SpecialAnim
   IAnimationSource DEATH_LIZARD = AnimationsRegistrySeasonal.register("lizard_death");
   IAnimationSource LAY_ON_STOMACH_POSE = AnimationsRegistrySeasonal.register("lay_on_stomach_pose");
   IAnimationSource ROLL_FROM_STOMACH = AnimationsRegistrySeasonal.register("roll_from_stomach");
   IAnimationSource STARES_AT_HAND = AnimationsRegistrySeasonal.register("humanoid/stares_at_hand");
   IAnimationSource REVIVER_STROKE_SWORD = AnimationsRegistrySeasonal.register("humanoid/reviver_stroke_sword");
   IAnimationSource JDH_AMAZED = AnimationsRegistrySeasonal.register("humanoid/jdh_amazed");
   IAnimationSource LESHIY_SPAWN = AnimationsRegistrySeasonal.register("leshiy_spawn");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_SCINT_GROW = AnimationsRegistrySeasonal.register("newer_scint_grow");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_CLOSE_EYES = AnimationsRegistrySeasonal.register("crow_close_eyes");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_EMOTION_RAGE = AnimationsRegistrySeasonal.register("crow_emotion_rage");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_FACEPALM = AnimationsRegistrySeasonal.register("kardinal_facepalm");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_HIDE_EYES = AnimationsRegistrySeasonal.register("kardinal_hide_eyes");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_SNEER = AnimationsRegistrySeasonal.register("kardinal_laugh");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_LOOK_HIM_OVER = AnimationsRegistrySeasonal.register("kardinal_person_check");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_THINK_ABOUT_IT = AnimationsRegistrySeasonal.register("kardinal_think_about_it");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_DISGUST = AnimationsRegistrySeasonal.register("never_disgust");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_DISTRUST = AnimationsRegistrySeasonal.register("newer_distrust");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_ANGRY = AnimationsRegistrySeasonal.register("newer_emotion_angry");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_EMOTION_BLUSH = AnimationsRegistrySeasonal.register("newer_emotion_blush");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_EMOTION_FEAR = AnimationsRegistrySeasonal.register("newer_emotion_fear");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_EMOTION_FEAR_CYCLE = AnimationsRegistrySeasonal.register("newer_emotion_fear_cycle");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_SAD = AnimationsRegistrySeasonal.register("newer_emotion_sad");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_EXPLAINING_CROSSED_ARMS = AnimationsRegistrySeasonal.register("newer_explaining_crossed_arms");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_EYE_ROLLING = AnimationsRegistrySeasonal.register("newer_eye_rolling");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_INSULT = AnimationsRegistrySeasonal.register("newer_insult");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_GIGGLE = AnimationsRegistrySeasonal.register("newer_laugh");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_CONSIDER = AnimationsRegistrySeasonal.register("newer_lookdown");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_PRIDE = AnimationsRegistrySeasonal.register("newer_pride");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_SUSPICIOUS = AnimationsRegistrySeasonal.register("newer_suspicious");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_TAKE_HEAD = AnimationsRegistrySeasonal.register("newer_take_head");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_WAVE_WINGS = AnimationsRegistrySeasonal.register("newer_wave_cycle");
   @SpecialAnim("NEVER")
   IAnimationSource NEVER_CLAP_WINGS = AnimationsRegistrySeasonal.register("newer_wings_clap");
   IAnimationSource HANDSONCHEST_TALL = AnimationsRegistrySeasonal.register("handsonchest_tall");
   @SpecialAnim
   IAnimationSource ALFRED_CUTSCENE_ANIM1 = AnimationsRegistrySeasonal.register("alfred_cutscene_anim1");
   @SpecialAnim
   IAnimationSource ALFRED_CUTSCENE_ANIM2 = AnimationsRegistrySeasonal.register("alfred_cutscene_anim2", "alfred_cutscene_part2");
   @SpecialAnim
   IAnimationSource ALFRED_CUTSCENE_ANIM2_STATIC = AnimationsRegistrySeasonal.register("alfred_cutscene_anim2", "alfred_cutscene_part2_static");
   @SpecialAnim
   IAnimationSource ALFRED_CUTSCENE_IDLE1 = AnimationsRegistrySeasonal.register("alfred_cutscene_idle_1");
   @SpecialAnim
   IAnimationSource ALFRED_CUTSCENE_IDLE2 = AnimationsRegistrySeasonal.register("alfred_cutscene_idle_2");
   @SpecialAnim
   IAnimationSource ALFRED_CUTSCENE_IDLE3 = AnimationsRegistrySeasonal.register("alfred_cutscene_idle_3");
   IAnimationSource SCINTOSAURUS_ROAR = AnimationsRegistrySeasonal.register("scintosaurus_roar");
   IAnimationSource SCINTOSAURUS_PULL_FROM_WALL = AnimationsRegistrySeasonal.register("scintosaurus_pull_from_wall");
   IAnimationSource SCINTOSAURUS_IDLE = AnimationsRegistrySeasonal.register("scintosaurus_idle");
   IAnimationSource SCINTOSAURUS_WINGS_FOLD = AnimationsRegistrySeasonal.register("scintosaurus_wings_fold");
   IAnimationSource NO_LEGS_CRAWL_CYCLE = AnimationsRegistrySeasonal.register("no_legs_crawl_cycle");
   IAnimationSource NO_LEGS_CRAWL_POSE = AnimationsRegistrySeasonal.register("no_legs_crawl_pose");
   IAnimationSource NO_LEGS_CRAWL_ENDANIMATED = AnimationsRegistrySeasonal.register("no_legs_crawl_endanimated");
   IAnimationSource JUMP_ATTACK_LEG_POSE = AnimationsRegistrySeasonal.register("jump_attack_leg_pose");
   IAnimationSource JUMP_ATTACK_POSE = AnimationsRegistrySeasonal.register("jump_attack_pose");
   IAnimationSource JUMP_FORWARD = AnimationsRegistrySeasonal.register("jump_forward");
   IAnimationSource SCINTOSAURUS_RUN = AnimationsRegistrySeasonal.register("scintosaurus_run");
   IAnimationSource SCINTOSAURUS_HIT_EYE1 = AnimationsRegistrySeasonal.register("scintosaurus_hit_eye1");
   IAnimationSource SCINTOSAURUS_HIT_EYE2 = AnimationsRegistrySeasonal.register("scintosaurus_hit_eye2");
   IAnimationSource SCINTOSAURUS_HIT_EYE3 = AnimationsRegistrySeasonal.register("scintosaurus_hit_eye3");
   IAnimationSource SCINTOSAURUS_HIT_EYE4 = AnimationsRegistrySeasonal.register("scintosaurus_hit_eye4");
   IAnimationSource SCINTOSAURUS_HIT_EYE5 = AnimationsRegistrySeasonal.register("scintosaurus_hit_eye5");
   IAnimationSource SCINTOSAURUS_HIT_EYE6 = AnimationsRegistrySeasonal.register("scintosaurus_hit_eye6");
   SidedAnimationSet SCINTOSAURUS_HIT_HEAD = AnimationsRegistrySeasonal.registerSided(
      "scintosaurus_hit_head_right", "scintosaurus_hit_head_left", "scintosaurus_hit_head"
   );
   SidedAnimationSet PULLING_HAND = AnimationsRegistrySeasonal.registerSided("pulling_hand_right", "pulling_hand_left", "pulling_hand");
   IAnimationSource PANIC_JUMP = AnimationsRegistrySeasonal.register("panic_jump");
   IAnimationSource EMBARRASSMENT_HANDS = AnimationsRegistrySeasonal.register("embarrassment_hands");
   IAnimationSource CAREFULL_WALK_CYCLE = AnimationsRegistrySeasonal.register("carefull_walk_cycle");
   IAnimationSource CIRCLE_WALK_CYCLE = AnimationsRegistrySeasonal.register("circle_walk_cycle");
   IAnimationSource CLAPPING_GAME_CYCLE = AnimationsRegistrySeasonal.register("clapping_game_cycle");
   IAnimationSource CLAPPING_GAME_CYCLE_KID = AnimationsRegistrySeasonal.register("clapping_game_cycle_kid");
   IAnimationSource HANDS_GRAB_HEAD = AnimationsRegistrySeasonal.register("hands_grab_head");
   IAnimationSource MARK_SCINT_GROW = AnimationsRegistrySeasonal.register("mark_scint_grow");
   IAnimationSource SCINTOSAURUS_PULL_FROM_WALL_TO_FLY = AnimationsRegistrySeasonal.register("scintosaurus_pull_from_wall_to_fly");
   IAnimationSource SCINTOSAURUS_BLIND_FLY = AnimationsRegistrySeasonal.register("scintosaurus_blind_fly");
   IAnimationSource SCINTOSAURUS_ROAR_PAIN = AnimationsRegistrySeasonal.register("scintosaurus_roar_pain");
   IAnimationSource JUMP_SWORD_ATTACK_POSE = AnimationsRegistrySeasonal.register("jump_sword_attack_pose");
   IAnimationSource JUMP_FORWARD_SWORD = AnimationsRegistrySeasonal.register("jump_forward_sword");
   IAnimationSource WOMAN_SCINT_GROW = AnimationsRegistrySeasonal.register("woman_scint_grow");
   IAnimationSource GIBBERISH_SLEEP_LAYER = AnimationsRegistrySeasonal.register("gibberish_sleep_layer");
   @SpecialAnim
   IAnimationSource CARDINAL_NERVOIS = AnimationsRegistrySeasonal.register("kardinal_nervous");
   @SpecialAnim
   IAnimationSource CARDINAL_HOLD_TEA = AnimationsRegistrySeasonal.register("kardinal_hold_tea");
   @SpecialAnim
   IAnimationSource CARDINAL_DRINK_TEA = AnimationsRegistrySeasonal.register("kardinal_drink_tea");
   IAnimationSource RAT_HOLDED_POSE = AnimationsRegistrySeasonal.register("rat_holded_pose");
   IAnimationSource RAT_HOLDED_TWITCHING = AnimationsRegistrySeasonal.register("rat_holded_twitching");
   IAnimationSource RAT_TRANSFORM = AnimationsRegistrySeasonal.register("rat_transform");
   @SpecialAnim
   IAnimationSource BOTH_EYES_CLOSE = AnimationsRegistrySeasonal.register("both_eyes_closed");
   IAnimationSource JDH_NO_GLASSES = AnimationsRegistrySeasonal.register("jdh_no_glasses");
   IAnimationSource APPROVING_NODS = AnimationsRegistrySeasonal.register("approving_nods");
   IAnimationSource CLOSE_HAND_LOOK = AnimationsRegistrySeasonal.register("close_hand_look");
   SidedAnimationSet PALM_FORWARD = AnimationsRegistrySeasonal.registerSided("palm_forward_right", "palm_forward_left", "palm_forward");
   SidedAnimationSet TOUCH_SHOULDER = AnimationsRegistrySeasonal.registerSided("touch_shoulder_right", "touch_shoulder_left", "touch_shoulder");
   IAnimationSource DIZZY_CYCLE = AnimationsRegistrySeasonal.register("dizzy_cycle");
   IAnimationSource SAIMAN_SPAWN_CUTSCENE = AnimationsRegistrySeasonal.register("saiman_spawn_cutscene");
   IAnimationSource SAIMAN_SPAWN_IDLE = AnimationsRegistrySeasonal.register("saiman_spawn_idle");
   IAnimationSource SAIMAN_TRANSFORM_PHASE_2 = AnimationsRegistrySeasonal.register("saiman_boss_transform_phase2");
   IAnimationSource ROLLING_BODY = AnimationsRegistrySeasonal.register("humanoid/rolling_body");
   IAnimationSource SADYKA_FULLKNESS_SIT = AnimationsRegistrySeasonal.register("humanoid/sadyka_fullknees_sit");
   IAnimationSource SAIMANI_DEATH = AnimationsRegistrySeasonal.register("saiman_death");
   IAnimationSource SHAKING_BODY = AnimationsRegistrySeasonal.register("humanoid/shaking_body");
   IAnimationSource TILTED_UPPER_BODY = AnimationsRegistrySeasonal.register("humanoid/tilted_upper_body");
   SidedAnimationSet RUMMAGE_BAG = AnimationsRegistrySeasonal.registerSided(
      "rummage_bag_while_holding_right", "rummage_bag_while_holding_left", "rummage_bag_while_holding"
   );
   IAnimationSource DIZZY_SIT_ON_GROUND = AnimationsRegistrySeasonal.register("dizzy_sit_on_ground");
   SidedAnimationSet APPEAR_FROM_CORNER = AnimationsRegistrySeasonal.registerSided("appear_from_corner_right", "appear_from_corner_left", "appear_from_corner");
   IAnimationSource CONFUSED_CHECKING = AnimationsRegistrySeasonal.register("confused_checking");
   IAnimationSource GHOST_SWAYING = AnimationsRegistrySeasonal.register("ghost_swaying");
   IAnimationSource FEAR_FALL_TO_CRAWL = AnimationsRegistrySeasonal.register("fear_fall_to_crawl");
   IAnimationSource CRAWL_BACK_CYCLE = AnimationsRegistrySeasonal.register("crawl_back_cycle");
   IAnimationSource VILLAIN_LAUGH = AnimationsRegistrySeasonal.register("villain_laugh");
   IAnimationSource BOOK_READ_NODDING_LAYER = AnimationsRegistrySeasonal.register("book_read_nodding_layer");
   IAnimationSource SPILLING_OUT_LEFT = AnimationsRegistrySeasonal.register("spilling_out_left");
   IAnimationSource FATHER_SPEAR_NPC_ATTACK = AnimationsRegistrySeasonal.register("father_spear_npc_attack");
   IAnimationSource FATHER_SPEAR_SPAWN_PIERCE = AnimationsRegistrySeasonal.register("father_spear_spawn_pierce");
   IAnimationSource SIT_ON_GROUND_GET_UP = AnimationsRegistrySeasonal.register("sit_on_ground_get_up");
   IAnimationSource BERENGARIY_TAKE_OFF_HAT = AnimationsRegistrySeasonal.register("berengariy_take_off_hat");
   IAnimationSource HUG_ADULT_TO_CHILD = AnimationsRegistrySeasonal.register("hug_adult_to_child");
   IAnimationSource HUG_CHILD_TO_ADULT = AnimationsRegistrySeasonal.register("hug_child_to_adult");
   IAnimationSource POSE_ENDURE_PAIN = AnimationsRegistrySeasonal.register("pose_endure_pain");
   IAnimationSource STRANGE_BODY_TWISTING = AnimationsRegistrySeasonal.register("strange_body_twisting");
   IAnimationSource SHIELD_DEFEND_FORWARD = AnimationsRegistrySeasonal.register("big_shield_defend_forward");
   IAnimationSource SHIELD_DEFEND_UP = AnimationsRegistrySeasonal.register("big_shield_defend_up");
   IAnimationSource DROP_BAG_ENDANIMATED = AnimationsRegistrySeasonal.register("drop_bag_endanimated");
   IAnimationSource HOLD_BAG_POSE = AnimationsRegistrySeasonal.register("hold_bag_pose");
   IAnimationSource CASTING_SPELL_SHIELD = AnimationsRegistrySeasonal.register("casting_spell_shield");
   IAnimationSource KNEEL_DOWN_TALL_PUNCH_GROUND = AnimationsRegistrySeasonal.register("kneel_down_tall_punch_ground");
   IAnimationSource KNEEL_TO_SIDE_FALL_TALL = AnimationsRegistrySeasonal.register("kneel_to_side_fall_tall");
   IAnimationSource SLIGHT_PULL_HANDS = AnimationsRegistrySeasonal.register("slight_pull_hands");
   SidedAnimationSet EXAMINE_ITEM_PULLED_UP = AnimationsRegistrySeasonal.registerSided(
      "examine_item_pulled_up_right", "examine_item_pulled_up_left", "examine_item_pulled_up"
   );
   SidedAnimationSet TURNING_TO_SIDE_SITTING = AnimationsRegistrySeasonal.registerSided(
      "turning_to_side_sitting_right", "turning_to_side_sitting_left", "turning_to_side_sitting"
   );
   IAnimationSource PUNCHED_IN_FACE_FALL = AnimationsRegistrySeasonal.register("punched_in_face_fall");
   IAnimationSource KNEES_LOOK_ON_HAND = AnimationsRegistrySeasonal.register("knees_look_on_hand");
   IAnimationSource KNEES_GET_UP = AnimationsRegistrySeasonal.register("knees_get_up");
   IAnimationSource BROTHER_HUG_SHORT = AnimationsRegistrySeasonal.register("brother_hug_short");
   IAnimationSource BROTHER_HUG_TALL = AnimationsRegistrySeasonal.register("brother_hug_tall");
   IAnimationSource BACKSTABBING_STABBER = AnimationsRegistrySeasonal.register("backstabbing_stabber");
   IAnimationSource BACKSTABBING_VICTIM = AnimationsRegistrySeasonal.register("backstabbing_victim");
   IAnimationSource SAIRISSA_GET_BOW_READY = AnimationsRegistrySeasonal.register("sairissa_get_bow_ready");
   SidedAnimationSet HAND_SILENCE = AnimationsRegistrySeasonal.registerSided("hand_silence_right", "hand_silence_left", "hand_silence");
   IAnimationSource ARMS_SCINT_GROW = AnimationsRegistrySeasonal.register("arms_scint_grow");
   IAnimationSource CHEST_SCINT_GROW = AnimationsRegistrySeasonal.register("chest_scint_grow");
   IAnimationSource FALL_ON_KNEES = AnimationsRegistrySeasonal.register("fall_on_knees");
   IAnimationSource FALL_ON_KNEES_HUGGING = AnimationsRegistrySeasonal.register("fall_on_knees_hugging");
   IAnimationSource FALL_ON_KNEES_STROKING = AnimationsRegistrySeasonal.register("fall_on_knees_stroking");
   IAnimationSource DRAGGED_ON_FLOOR_POSE = AnimationsRegistrySeasonal.register("dragged_on_floor_pose");
   IAnimationSource DRAGGED_ON_FLOOR_SOMEONE_POSE_RIGHT = AnimationsRegistrySeasonal.register("dragging_floor_someone_pose_right");
   IAnimationSource DRAGGED_ON_FLOOR_SOMEONE_POSE_LEFT = AnimationsRegistrySeasonal.register("dragging_floor_someone_pose_left");
   IAnimationSource DRAGGED_ON_FLOOR_IDLE = AnimationsRegistrySeasonal.register("dragged_on_floor_idle");
   IAnimationSource DRAGGED_ON_FLOOR_SOMEONE_IDLE_RIGHT = AnimationsRegistrySeasonal.register("dragging_floor_someone_idle_right");
   IAnimationSource DRAGGED_ON_FLOOR_SOMEONE_IDLE_LEFT = AnimationsRegistrySeasonal.register("dragging_floor_someone_idle_left");
   IAnimationSource SHOCKWAVE_FALL_FORWARD = AnimationsRegistrySeasonal.register("shockwave_fall_forward");
   IAnimationSource ORK_PICK_UP_BODY = AnimationsRegistrySeasonal.register("ork_pick_up_body");
   IAnimationSource PANICKING_HOLDED_MODEL2 = AnimationsRegistrySeasonal.register("panicking_holded_model2");
   IAnimationSource SHIELD_APPEAR = AnimationsRegistrySeasonal.register("shield_appear");
   IAnimationSource WALL_HIT_FIST = AnimationsRegistrySeasonal.register("wall_hit_fist");
   IAnimationSource WALL_HIT_SWORD = AnimationsRegistrySeasonal.register("wall_hit_sword");
   IAnimationSource CASTING_SPELL_CYCLE = AnimationsRegistrySeasonal.register("casting_spell_cycle");
   IAnimationSource GLASSES_THROW_ABYSS = AnimationsRegistrySeasonal.register("glasses_throw_abyss");
   IAnimationSource CUTTING_ON_TABLE_CYCLE = AnimationsRegistrySeasonal.register("cutting_on_table_cycle");
   IAnimationSource FALL_KNEES_PUNCHING_GROUND = AnimationsRegistrySeasonal.register("fall_knees_punching_ground");
   IAnimationSource CITY_IDLE_STEVE_MODEL1 = AnimationsRegistrySeasonal.register("city_idle_steve_model1");
   IAnimationSource CITY_IDLE_STEVE_MODEL2 = AnimationsRegistrySeasonal.register("city_idle_steve_model2");
   IAnimationSource CITY_IDLE_STEVE_MODEL3 = AnimationsRegistrySeasonal.register("city_idle_steve_model3");
   IAnimationSource CITY_IDLE_STEVE_MODEL4 = AnimationsRegistrySeasonal.register("city_idle_steve_model4");
   IAnimationSource CITY_IDLE_STEVE_MODEL5 = AnimationsRegistrySeasonal.register("city_idle_steve_model5");
   IAnimationSource BREAK_FROM_HOLD = AnimationsRegistrySeasonal.register("break_from_hold");
   IAnimationSource GIANT_ORK_MODEL1_FALL = AnimationsRegistrySeasonal.register("giant_ork_model1_fall");
   IAnimationSource GIANT_ORK_MODEL1_STUN = AnimationsRegistrySeasonal.register("giant_ork_model1_stun");
   IAnimationSource SHANGRIN_FLY_SIT = AnimationsRegistrySeasonal.register("shangrin_fly_sit");
   IAnimationSource MONSTER_MARK_CROSS = AnimationsRegistrySeasonal.register("monster_mark_cross");
   IAnimationSource SHANGRIN_WIDE_GESTURE = AnimationsRegistrySeasonal.register("shangrin_wide_gesture");
   IAnimationSource SHANGRIN_WALK_CYCLE = AnimationsRegistrySeasonal.register("shangrin_walk_cycle");
   IAnimationSource BLOCK_FALL_KNOCKBACK = AnimationsRegistrySeasonal.register("block_fall_knockback");
   IAnimationSource SHANGRIN_HOLD_SIDE_LEFT = AnimationsRegistrySeasonal.register("shangrin_hold_side_left");
   IAnimationSource SHANGRIN_WIDE_ARMS = AnimationsRegistrySeasonal.register("shangrin_wide_arms");
   IAnimationSource SHOCKWAVE_FALL_BACK = AnimationsRegistrySeasonal.register("shockwave_fall_back");
   IAnimationSource TALL_LAY_ON_BLOCK = AnimationsRegistrySeasonal.register("tall_lay_on_block");
   IAnimationSource GLASSES_SLIPPING = AnimationsRegistrySeasonal.register("glasses_slipping");
   IAnimationSource NEWER_SIT_ARMS_ONLY = AnimationsRegistrySeasonal.register("newer_sit_arms_only");
   IAnimationSource NEWER_SIT_ON_GROUND_BLOCK_NO_ARMS = AnimationsRegistrySeasonal.register("newer_sit_on_ground_block_no_arms");
   SidedAnimationSet BYE_BACK = AnimationsRegistrySeasonal.registerSided("bye_back_right", "bye_back_left", "bye_back");
   IAnimationSource SCRUB_DEER = AnimationsRegistrySeasonal.register("action_scrub_deer");
   IAnimationSource EXAMINE_OBJECTS_IN_HANDS = AnimationsRegistrySeasonal.register("examine_objects_in_hands");
   IAnimationSource STIR_HERBS = AnimationsRegistrySeasonal.register("stir_herbs");
   IAnimationSource CUT_FINGER_WITH_BRACER = AnimationsRegistrySeasonal.register("cut_finger_with_bracer");
   IAnimationSource SHANGRIN_PHASE2_CUTSCENE = AnimationsRegistrySeasonal.register("shangrin_phase2_cutscene");
   IAnimationSource SHANGRIN_SHOWING_DOWN_POSE = AnimationsRegistrySeasonal.register("shangrin_showing_down_pose");
   IAnimationSource JACUZZI_ENDANIMATED = AnimationsRegistrySeasonal.register("jacuzzi_endanimated");
   IAnimationSource ONE_KNEE_HAND_TO_SIT_KNEES_ARMS_BACK = AnimationsRegistrySeasonal.register("one_knee_hand_to_sit_knees_arms_back");
   IAnimationSource FALL_ON_KNEE_ONE_HAND = AnimationsRegistrySeasonal.register("fall_on_knee_one_hand");
   IAnimationSource FALL_ON_TWO_HANDS_POSE = AnimationsRegistrySeasonal.register("fall_on_two_hands_pose");
   IAnimationSource NEWER_SLEEP_LOOK_BACK_IDLE = AnimationsRegistrySeasonal.register("newer_sleep_look_back_idle");
   IAnimationSource DEAD_FALL_BACK = AnimationsRegistrySeasonal.register("dead_fall_back");
   IAnimationSource PUT_HAND_IN_GROUND = AnimationsRegistrySeasonal.register("put_hand_in_ground");
   IAnimationSource STAND_ON_KNEE_ONE_HAND_POSE = AnimationsRegistrySeasonal.register("humanoid/stand_on_knee_one_hand_pose");
   IAnimationSource WAKE_UP_KNEES = AnimationsRegistrySeasonal.register("humanoid/wake_up_knees");
   IAnimationSource GO_UP_HEAD_MAGIC = AnimationsRegistrySeasonal.register("go_up_head_magic");
   IAnimationSource HIT_HEAD_WITH_HANDLE = AnimationsRegistrySeasonal.register("hit_head_with_handle");
   IAnimationSource GET_ITEM_FROM_GROUND = AnimationsRegistrySeasonal.register("get_item_from_ground");
   IAnimationSource MOTHER_DEAD_POSE = AnimationsRegistrySeasonal.register("mother_dead_pose");
   IAnimationSource LUKY_SCINT_CLUSTER_GROWING = AnimationsRegistrySeasonal.register("lukiy_scint_cluster_growing");
   IAnimationSource SAIRISSA_SCINT_PURIFY = AnimationsRegistrySeasonal.register("sairissa_scint_purify");
   IAnimationSource DWARF_SHARPEN = AnimationsRegistrySeasonal.register("dwarf_sharpen");
   IAnimationSource TALL_SHARPEN = AnimationsRegistrySeasonal.register("tall_sharpen");
   SidedAnimationSet STAND_ON_HANDS_KID = AnimationsRegistrySeasonal.registerSided("stand_on_hands_kid_right", "stand_on_hands_kid_left", "stand_on_hands_kid");
   IAnimationSource FALL_ON_KNEE_TWO_HAND = AnimationsRegistrySeasonal.register("fall_on_two_hands");
   IAnimationSource NEWER_FROM_STOMACH_TO_SIT_GROUND = AnimationsRegistrySeasonal.register("newer_from_stomach_to_sit_ground");
   IAnimationSource ONE_KID_RUNNING_CIRCLE = AnimationsRegistrySeasonal.register("one_kid_running_circle");
   IAnimationSource FALL_ON_BUTT = AnimationsRegistrySeasonal.register("fall_on_butt");
   IAnimationSource FALL_ON_BUTT_KID = AnimationsRegistrySeasonal.register("fall_on_butt_kid");
   IAnimationSource CLOSE_EARS = AnimationsRegistrySeasonal.register("close_ears");
   IAnimationSource SMEAR_HANDS = AnimationsRegistrySeasonal.register("smear_hands");
   IAnimationSource LOLOLOSHKA_SCINT_GROW = AnimationsRegistrySeasonal.register("lololoshka_scint_grow");
   IAnimationSource DIZZY_STAND_UP_FROM_HANDS = AnimationsRegistrySeasonal.register("dizzy_stand_up_from_hands");
   IAnimationSource SAYRISSA_NO_HOOD = AnimationsRegistrySeasonal.register("sayrissa_no_hood");
   IAnimationSource EYES_ROLL_EXTREME_UP = AnimationsRegistrySeasonal.register("humanoid/eyes_up_emotion");
   IAnimationSource EYES_ROLL_EXTREME_DOWN = AnimationsRegistrySeasonal.register("humanoid/eyes_down_emotion");
   IAnimationSource EYES_CRAZY_MOVING = AnimationsRegistrySeasonal.register("humanoid/eyes_crazy_moving");
   IAnimationSource EYES_TREMBLING = AnimationsRegistrySeasonal.register("humanoid/eyes_trembling_cycle");
   IAnimationSource PLACE_SPEAR = AnimationsRegistrySeasonal.register("humanoid/place_spear_endanimated");
   IAnimationSource STAGGER_ENDANIMATED = AnimationsRegistrySeasonal.register("humanoid/stagger_endanimated");
   SidedAnimationSet MAGIC_CAST_FORWARD = AnimationsRegistrySeasonal.registerSided(
      "magic_cast_forward_right", "magic_cast_forward_left", "humanoid/magic_cast_forward"
   );
   IAnimationSource EMOTION_RAGE = AnimationsRegistrySeasonal.register("humanoid/emotion_rage");
   IAnimationSource GIANT_SHARP_SCINT_ATTACK = AnimationsRegistrySeasonal.register("giant_scint_attack");
   IAnimationSource DEFENSE_POSE_ARMS_FRONT = AnimationsRegistrySeasonal.register("humanoid/defense_pose_arms_in_front");
   IAnimationSource DEFENSE_POSE_ARMS_SIDES = AnimationsRegistrySeasonal.register("humanoid/defense_pose_arms_on_sides");
   IAnimationSource SPREAD_ARMS_DRAMATIC = AnimationsRegistrySeasonal.register("humanoid/spread_arms_dramatic_endanimated");
   IAnimationSource FIST_TO_CHEST = AnimationsRegistrySeasonal.register("humanoid/fist_to_chest");
   IAnimationSource CRAZY_JOY = AnimationsRegistrySeasonal.register("humanoid/crazy_joy");
   IAnimationSource JDH_RUN_WITH_POWDER_SLOW = AnimationsRegistrySeasonal.register("humanoid/jdh_run_forward_take_powder_slowmo");
   IAnimationSource JDH_USE_POWDER_SLOW = AnimationsRegistrySeasonal.register("humanoid/jdh_use_powder_slowmo");
   IAnimationSource JDH_HADOUKEN = AnimationsRegistrySeasonal.register("humanoid/magic_cast_hadouken_endanimated");
   @SpecialAnim
   IAnimationSource LO_UNSCINT_PARTIAL = AnimationsRegistrySeasonal.register("humanoid/lololoshka_scint_start_disappearing_endanimated");
   @SpecialAnim
   IAnimationSource LO_UNSCINT_FULL = AnimationsRegistrySeasonal.register("humanoid/lololoshka_scint_fully_disappearing_endanimated");
   IAnimationSource EMOTION_SMIRK = AnimationsRegistrySeasonal.register("humanoid/emotion_snide_smirk");
   IAnimationSource THINKING_NOSE_SCRATCH_CYCLE = AnimationsRegistrySeasonal.register("humanoid/thinking_nose_scratch_cycle");
   IAnimationSource EVIL_LAUGH = AnimationsRegistrySeasonal.register("evil_laugh");
   IAnimationSource EVIL_SMILE = AnimationsRegistrySeasonal.register("evil_smile");
   IAnimationSource MASK_TOOK_OFF = AnimationsRegistrySeasonal.register("mask_took_off");
   IAnimationSource WIPE_FACE_BOTH_HANDS = AnimationsRegistrySeasonal.register("wipe_face_both_hands");
   IAnimationSource SNEAKING_CYCLE = AnimationsRegistrySeasonal.register("sneaking_cycle");
   IAnimationSource DIGGING_GROUND_CYCLE_SHORT = AnimationsRegistrySeasonal.register("humanoid/digging_ground_cycle_short");
   IAnimationSource DIGGING_GROUND_CYCLE_TALL = AnimationsRegistrySeasonal.register("humanoid/digging_ground_cycle_tall");
   IAnimationSource HANDS_NO_PLACEHOLDER = AnimationsRegistrySeasonal.register("hands_no_placeholder");
   SidedAnimationSet S57_VS_FATHER_CUTS = AnimationsRegistrySeasonal.registerSided(
      "jdh_lololoshka_vs_father_cutscene_s57_part1", "jdh_lololoshka_vs_father_cutscene_s57_part2", "jdh_lololoshka_vs_father_cutscene_s57"
   );
   SidedAnimationSet LEANED_TO_WALL = AnimationsRegistrySeasonal.registerSided("leaned_to_wall_right", "leaned_to_wall_left", "leaned_to_wall");
   IAnimationSource FATHER_JUMP_SIT_THRONE = AnimationsRegistrySeasonal.register("father_jump_sit_throne");
   IAnimationSource THRONE_SIT = AnimationsRegistrySeasonal.register("throne_sit");
   IAnimationSource THRONE_SIT_ARMS_ONLY = AnimationsRegistrySeasonal.register("throne_sit_arms_only");
   IAnimationSource THRONE_SIT_NO_ARMS = AnimationsRegistrySeasonal.register("throne_sit_no_arms");
   IAnimationSource FATHER_BARRIER_ANIMATION = AnimationsRegistrySeasonal.register("father_barrier_animation");
   IAnimationSource LOLOJPHN_FATHER_CURSCENE_1 = AnimationsRegistrySeasonal.register("lolojohn_attacks_father_cutscene_s60_part1");
   IAnimationSource LOLOJPHN_FATHER_CURSCENE_2 = AnimationsRegistrySeasonal.register("lolojohn_attacks_father_cutscene_s60_part2");
   IAnimationSource LOLOJPHN_FATHER_CURSCENE_3 = AnimationsRegistrySeasonal.register("lolojohn_attacks_father_cutscene_s60_part3");
   IAnimationSource TALL_STOMACH_HOLD = AnimationsRegistrySeasonal.register("tall_stomach_hold");
   IAnimationSource CRYSTAL_LINE_ATTACK_LONG = AnimationsRegistrySeasonal.register("crystal_line_attack_long");
   @SpecialAnim
   IAnimationSource WOLF_SLEEP_POSE = AnimationsRegistrySeasonal.register("wolf_sleep_pose");
   IAnimationSource CITY_IDLE_STEVE_CUTSCENE = AnimationsRegistrySeasonal.register("city_idle_steve_cutscene");
   IAnimationSource CITY_FAST_RUN = AnimationsRegistrySeasonal.register("humanoid/fast_city_run");
   IAnimationSource ST6_HAND_AWAY = AnimationsRegistrySeasonal.register("humanoid/s60_handaway");
   IAnimationSource SIT_ON_EDGE_DANGLELEGS_CYCLE = AnimationsRegistrySeasonal.register("sit_on_edge_danglelegs_cycle");
   @SpecialAnim
   SidedAnimationSet ROTATE_WALKING = AnimationsRegistrySeasonal.registerSided("rotate_walking_right", "rotate_walking_left", "humanoid/rotate_walking");
   SidedAnimationSet ITEM_KNOCKING = AnimationsRegistrySeasonal.registerSided("item_knocking_left", "item_knocking_right", "item_knocking");
   SidedAnimationSet POINT_FORWARD = AnimationsRegistrySeasonal.registerSided("point_forward_right", "point_forward_left", "point_forward");
   @SpecialAnim
   SidedAnimationSet SHAKE_SWORD = AnimationsRegistrySeasonal.registerSided("shake_sword_right", "shake_sword_left", "shake_sword");
   @SpecialAnim
   SidedAnimationSet HOLD_ON_SHOULDER = AnimationsRegistrySeasonal.registerSided("hold_on_shoulder_right", "hold_on_shoulder_left", "humanoid/hold_on_shoulder");
   SidedAnimationSet BITE_FOOD = AnimationsRegistrySeasonal.registerSided("bite_food_right", "bite_food_left", "bite_food");
   SidedAnimationSet BOUNCE_BACK = AnimationsRegistrySeasonal.registerSided("bounce_back_right", "bounce_back_left", "bounce_back");
   SidedAnimationSet HOLD_LEG = AnimationsRegistrySeasonal.registerSided("hold_leg_right", "hold_leg_left", "hold_leg");
   SidedAnimationSet PASS_HAND = AnimationsRegistrySeasonal.registerSided("pass_hand_right", "pass_hand_left", "pass_hand");
   SidedAnimationSet POINT_BACK = AnimationsRegistrySeasonal.registerSided("show_hand_back_right", "show_hand_back_left", "humanoid/show_hand_back");
   SidedAnimationSet PICK_FROM_GROUND = AnimationsRegistrySeasonal.registerSided("pick_from_ground_right", "pick_from_ground_left", "pick_from_ground");
   SidedAnimationSet RAISE_HAND_LAY_LAYER = AnimationsRegistrySeasonal.registerSided(
      "raise_hand_lay_layer_right", "raise_hand_lay_layer_left", "raise_hand_lay_layer"
   );
   SidedAnimationSet BEND_GIVE_ON_BED = AnimationsRegistrySeasonal.registerSided("slight_bend_give_right", "slight_bend_give_left", "bend_give_on_bed");
   SidedAnimationSet CHECK_ITEM = AnimationsRegistrySeasonal.registerSided("check_item_right", "check_item_left", "check_item");
   SidedAnimationSet SPITTING = AnimationsRegistrySeasonal.registerSided("spitting_right", "spitting_left", "humanoid/spitting");
   SidedAnimationSet BEARD_STROKING = AnimationsRegistrySeasonal.registerSided("beard_stroking_right", "beard_stroking_left", "humanoid/beard_stroking");
   SidedAnimationSet CROW_MENACE_FIST = AnimationsRegistrySeasonal.registerSided("crow_menace_fist_right", "crow_menace_fist_left", "crow_menace_fist");
   SidedAnimationSet LEAN_KNEE = AnimationsRegistrySeasonal.registerSided("lean_knee_right", "lean_knee_left", "lean_knee");
   SidedAnimationSet LEAN_KNEE_OFFERHAND = AnimationsRegistrySeasonal.registerSided(
      "lean_knee_offerhand_right", "lean_knee_offerhand_left", "lean_knee_offerhand"
   );
   SidedAnimationSet SIT_PULL_LEGS_HELP_GETUP = AnimationsRegistrySeasonal.registerSided(
      "sit_pull_legs_help_getup_right", "sit_pull_legs_help_getup_left", "sit_pull_legs_help_getup"
   );
   SidedAnimationSet REVIVER_PULL_ONE_DAGGER = AnimationsRegistrySeasonal.registerSided(
      "reviver_pull_one_dagger_right", "reviver_pull_one_dagger_left", "reviver_pull_one_dagger"
   );
   SidedAnimationSet REVIVER_NECK_DAGGER = AnimationsRegistrySeasonal.registerSided(
      "reviver_neck_dagger_right", "reviver_neck_dagger_left", "reviver_neck_dagger"
   );
   SidedAnimationSet STAND_ON_HANDS_NEW = AnimationsRegistrySeasonal.registerSided("stand_on_hands_right", "stand_on_hands_left", "stand_on_hands_new");
   SidedAnimationSet BOOK_STROKING = AnimationsRegistrySeasonal.registerSided("book_stroking_right", "book_stroking_left", "book_stroking");
   SidedAnimationSet REACH_TOUCH = AnimationsRegistrySeasonal.registerSided(
      "reach_and_touch_endanimated_right", "reach_and_touch_endanimated_left", "humanoid/reach_and_touch_endanimated"
   );
   SidedAnimationSet LYING_GRAB_HAND_LAYER = AnimationsRegistrySeasonal.registerSided(
      "lying_grab_hand_layer_right", "lying_grab_hand_layer_left", "lying_grab_hand_layer"
   );
   SidedAnimationSet STEP_FORWARD = AnimationsRegistrySeasonal.registerSided("step_forward_right", "step_forward_left", "step_forward");
   SidedAnimationSet SHOCKWAVE_FALL_SIDE = AnimationsRegistrySeasonal.registerSided(
      "shockwave_fall_side_right", "shockwave_fall_side_left", "shockwave_fall_side"
   );
   SidedAnimationSet JDH_QUESTION_POSING = AnimationsRegistrySeasonal.registerSided(
      "jdh_question_posing_right", "jdh_question_posing_left", "jdh_question_posing"
   );
   IAnimationSource ZOMBIE1_IDLE = AnimationsRegistrySeasonal.register("mob/zombie1/zombie1_idle");
   IAnimationSource ZOMBIE1_WALK = AnimationsRegistrySeasonal.register("mob/zombie1/zombie1_walk");
   IAnimationSource ZOMBIE1_ATTACK = AnimationsRegistrySeasonal.register("mob/zombie1/zombie1_attack");
   IAnimationSource ZOMBIE2_IDLE = AnimationsRegistrySeasonal.register("mob/zombie2/zombie2_idle");
   IAnimationSource ZOMBIE2_WALK = AnimationsRegistrySeasonal.register("mob/zombie2/zombie2_walk");
   IAnimationSource ZOMBIE2_ATTACK = AnimationsRegistrySeasonal.register("mob/zombie2/zombie2_attack");
   IAnimationSource ZOMBIE4_SWORD_ATTACK = AnimationsRegistrySeasonal.register("mob/attack/zombie4_sword_attack");
   IAnimationSource ZOMBIE4_ATTACK = AnimationsRegistrySeasonal.register("mob/attack/zombie4_attack");
   IAnimationSource ZOMBIE6_ATTACK = AnimationsRegistrySeasonal.register("mob/attack/zombie6_attack");
   IAnimationSource ZOMBIE8_ATTACK = AnimationsRegistrySeasonal.register("mob/attack/zombie8_attack");
   IAnimationSource ZOMBIE10_ATTACK = AnimationsRegistrySeasonal.register("mob/attack/zombie10_attack");
   IAnimationSource ZOMBIE3_IDLE = AnimationsRegistrySeasonal.register("mob/zombie3/zombie3_idle");
   IAnimationSource ZOMBIE3_WALK = AnimationsRegistrySeasonal.register("mob/zombie3/zombie3_walk");
   IAnimationSource ZOMBIE3_ATTACK = AnimationsRegistrySeasonal.register("mob/zombie3/zombie3_attack");
   IAnimationSource ZOMBIE_STUN_1 = AnimationsRegistrySeasonal.register("mob/stun/zombie_stun", "attacked_layer1");
   IAnimationSource ZOMBIE_STUN_2 = AnimationsRegistrySeasonal.register("mob/stun/zombie_stun", "attacked_layer2");
   IAnimationSource ZOMBIE_STUN_3 = AnimationsRegistrySeasonal.register("mob/stun/zombie_stun", "attacked_layer5");
   IAnimationSource ZOMBIE_DEATH = AnimationsRegistrySeasonal.register("mob/death/zombie_death");
   IAnimationSource ZOMBIE_RUN = AnimationsRegistrySeasonal.register("mob/run/zombie_run");
   IAnimationSource RAT_IDLE = AnimationsRegistrySeasonal.register("mob/rat/rat_idle");
   IAnimationSource RAT_WALK = AnimationsRegistrySeasonal.register("mob/rat/rat_walk");
   IAnimationSource RAT_GIGA_WALK = AnimationsRegistrySeasonal.register("mob/rat/walk_giga_rat");
   IAnimationSource RAT_RUN = AnimationsRegistrySeasonal.register("mob/rat/rat_run");
   IAnimationSource RAT_ATTACK = AnimationsRegistrySeasonal.register("mob/rat/rat_attack");
   IAnimationSource RAT_GIGA_ATTACK = AnimationsRegistrySeasonal.register("mob/rat/hit_giga_rat");
   IAnimationSource RAT_STUN_1 = AnimationsRegistrySeasonal.register("mob/stun/rat_stun", "attacked_layer1");
   IAnimationSource RAT_STUN_2 = AnimationsRegistrySeasonal.register("mob/stun/rat_stun", "attacked_layer2");
   IAnimationSource RAT_STUN_3 = AnimationsRegistrySeasonal.register("mob/stun/rat_stun", "attacked_layer3");
   IAnimationSource RAT_STUN_4 = AnimationsRegistrySeasonal.register("mob/stun/rat_stun", "attacked_layer4");
   IAnimationSource RAT_DEATH = AnimationsRegistrySeasonal.register("mob/death/rat_death");
   IAnimationSource HALBERD_FIGHT_POSE = AnimationsRegistrySeasonal.register("mob/inquisitor_scint/halberd_fight_pose");
   IAnimationSource HALBERD_ATTACK_1 = AnimationsRegistrySeasonal.register("mob/inquisitor_scint/halberd_layer_attack1");
   IAnimationSource HALBERD_ATTACK_2 = AnimationsRegistrySeasonal.register("mob/inquisitor_scint/halberd_layer_attack2");
   IAnimationSource HALBERD_ATTACK_3 = AnimationsRegistrySeasonal.register("mob/inquisitor_scint/halberd_layer_attack3");
   IAnimationSource SWORD_FIGHT_POSE = AnimationsRegistrySeasonal.register("mob/inquisitor_scint/sword_fight_pose");
   IAnimationSource SWORD_ATTACK_1 = AnimationsRegistrySeasonal.register("mob/inquisitor_scint/inquisitor_sword_attack1");
   IAnimationSource SWORD_ATTACK_2 = AnimationsRegistrySeasonal.register("mob/inquisitor_scint/inquisitor_sword_attack2");
   IAnimationSource SWORD_ATTACK_3 = AnimationsRegistrySeasonal.register("mob/inquisitor_scint/inquisitor_sword_attack3");
   IAnimationSource BEAR_IDLE = AnimationsRegistrySeasonal.register("mob/zombie_bear/bear_idle");
   IAnimationSource BEAR_ATTACK1 = AnimationsRegistrySeasonal.register("mob/zombie_bear/bear_attack1");
   IAnimationSource BEAR_ATTACK2 = AnimationsRegistrySeasonal.register("mob/zombie_bear/bear_attack2");
   IAnimationSource BEAR_ATTACK3 = AnimationsRegistrySeasonal.register("mob/zombie_bear/bear_attack3");
   IAnimationSource BEAR_STUN1 = AnimationsRegistrySeasonal.register("mob/zombie_bear/bear_stun1");
   IAnimationSource BEAR_STUN2 = AnimationsRegistrySeasonal.register("mob/zombie_bear/bear_stun2");
   IAnimationSource BEAR_WALK = AnimationsRegistrySeasonal.register("mob/zombie_bear/bear_walk");
   IAnimationSource BEAR_DEATH = AnimationsRegistrySeasonal.register("mob/zombie_bear/bear_fall_on_side");
   @SpecialAnim
   IAnimationSource DEER_RUN = AnimationsRegistrySeasonal.register("mob/deer/deer_run");
   @SpecialAnim
   IAnimationSource DEER_WALK = AnimationsRegistrySeasonal.register("mob/deer/deer_walk");
   @SpecialAnim
   IAnimationSource DEER_LAY = AnimationsRegistrySeasonal.register("mob/deer/deer_lay");
   @SpecialAnim
   IAnimationSource WOLF_ATTACK1 = AnimationsRegistrySeasonal.register("mob/wolf/wolf_attacks", "wolf_attack1");
   @SpecialAnim
   IAnimationSource WOLF_ATTACK2 = AnimationsRegistrySeasonal.register("mob/wolf/wolf_attacks", "wolf_attack2");
   @SpecialAnim
   IAnimationSource WOLF_ATTACK3 = AnimationsRegistrySeasonal.register("mob/wolf/wolf_attacks", "wolf_attack3");
   @SpecialAnim
   IAnimationSource WOLF_STUN1 = AnimationsRegistrySeasonal.register("mob/wolf/wolf_stun", "attacked_1");
   @SpecialAnim
   IAnimationSource WOLF_STUN2 = AnimationsRegistrySeasonal.register("mob/wolf/wolf_stun", "attacked_2");
   @SpecialAnim
   IAnimationSource WOLF_STUN3 = AnimationsRegistrySeasonal.register("mob/wolf/wolf_stun", "attacked_3");
   @SpecialAnim
   IAnimationSource WOLF_STUN4 = AnimationsRegistrySeasonal.register("mob/wolf/wolf_stun", "attacked_4");
   @SpecialAnim
   IAnimationSource WOLF_RUN = AnimationsRegistrySeasonal.register("mob/wolf/wolf_run");
   @SpecialAnim
   IAnimationSource WOLF_DEATH = AnimationsRegistrySeasonal.register("mob/wolf/wolf_death");
   @SpecialAnim
   IAnimationSource WOLF_WALK = AnimationsRegistrySeasonal.register("mob/wolf/wolf_walk");
   IAnimationSource LIZARD_IDLE = AnimationsRegistrySeasonal.register("mob/lizard/lizard_idle");
   IAnimationSource LIZARD_WALK = AnimationsRegistrySeasonal.register("mob/lizard/lizard_walk");
   IAnimationSource SPIDER_IDLE = AnimationsRegistrySeasonal.register("mob/spider/spider_idle");
   IAnimationSource SPIDER_WALK_CYCLE = AnimationsRegistrySeasonal.register("mob/spider/spider_walk_cycle");

   @Since(Since.Season.DS)
   public interface AnimationsIdle {
      IAnimationSource VOX_TAIL_IDLE = AnimationsRegistrySeasonal.register("vox_tail_idle");
   }
}
