package com.storyteam.storytelling.client.animations;

import com.storyteam.storytelling.api.Since;
import com.storyteam.storytelling.api.animations.SpecialAnim;
import com.storyteam.storytelling.client.AnimationsRegistry;
import com.storyteam.storytelling.client.AnimationsRegistrySeasonal;
import com.storyteam.storytelling.client.core.SidedAnimationSet;
import org.zeith.hammeranims.api.animation.IAnimationSource;

@Since(Since.Season.ST8)
public interface AnimationsST8 {
   @SpecialAnim("ANT")
   IAnimationSource ANT_IDLE = AnimationsRegistrySeasonal.register("ant/ant_idle");
   @SpecialAnim("ANT")
   IAnimationSource ANT_IDLE_HEAD_ROTATE = AnimationsRegistrySeasonal.register("ant/ant_idle_head_rotate");
   @SpecialAnim("ANT")
   IAnimationSource ANT_WALK = AnimationsRegistrySeasonal.register("ant/ant_walk");
   @SpecialAnim("ANT")
   IAnimationSource ANT_RUN = AnimationsRegistrySeasonal.register("ant/ant_run");
   @SpecialAnim("ANT")
   IAnimationSource ANT_HAPPY_JUMP_CYCLE = AnimationsRegistrySeasonal.register("ant/ant_happy_jump_cycle");
   @SpecialAnim("ANT")
   IAnimationSource ANT_NOD_CYCLE = AnimationsRegistrySeasonal.register("ant/ant_nod_cycle");
   @SpecialAnim("ANT")
   IAnimationSource ANT_BUGEYED_BLINK = AnimationsRegistrySeasonal.register("ant/ant_bugeyed_blink");
   @SpecialAnim("ANT")
   IAnimationSource ANT_SAD = AnimationsRegistrySeasonal.register("ant/ant_sad");
   @SpecialAnim("ANT")
   IAnimationSource ANT_DANCE = AnimationsRegistrySeasonal.register("ant/ant_dance");
   @SpecialAnim("ANT")
   IAnimationSource ANT_HARD_WALK_PUSH = AnimationsRegistrySeasonal.register("ant/ant_hard_walk_push");
   @SpecialAnim("ANT")
   IAnimationSource ANT_IDLE_MODEL2 = AnimationsRegistrySeasonal.register("ant/ant_idle_model2");
   @SpecialAnim("ANT")
   IAnimationSource ANT_IDLE_MODEL3 = AnimationsRegistrySeasonal.register("ant/ant_idle_model3");
   @SpecialAnim("ANT")
   IAnimationSource ANT_IDLE_MODEL4 = AnimationsRegistrySeasonal.register("ant/ant_idle_model4");
   @SpecialAnim("ANT")
   IAnimationSource ANT_IDLE_MODEL5 = AnimationsRegistrySeasonal.register("ant/ant_idle_model5");
   @SpecialAnim("ANT")
   IAnimationSource ANT_IDLE_MODEL6 = AnimationsRegistrySeasonal.register("ant/ant_idle_model6");
   @SpecialAnim("ANT")
   IAnimationSource ANT_IDLE_MODEL7 = AnimationsRegistrySeasonal.register("ant/ant_idle_model7");
   @SpecialAnim("ANT")
   IAnimationSource ANT_IDLE_MODEL8 = AnimationsRegistrySeasonal.register("ant/ant_idle_model8");
   @SpecialAnim("ANT")
   IAnimationSource ANT_IDLE_MODEL9 = AnimationsRegistrySeasonal.register("ant/ant_idle_model9");
   @SpecialAnim("ANT")
   IAnimationSource ANTS_BLINKING = AnimationsRegistrySeasonal.register("ant/ants_blinking");
   @SpecialAnim("ANT")
   IAnimationSource ANT_HANDS_TO_SIDES = AnimationsRegistrySeasonal.register("ant/ant_hands_to_sides");
   @SpecialAnim("SPIDER_HEAD")
   IAnimationSource SPIDER_HEAD_IDLE = AnimationsRegistrySeasonal.register("spider_head/spider_head_lololoshka_idle");
   @SpecialAnim("SPIDER_HEAD")
   IAnimationSource SPIDER_HEAD_WALK = AnimationsRegistrySeasonal.register("spider_head/spider_head_lololoshka_walk");
   @SpecialAnim("SPIDER_HEAD")
   IAnimationSource SPIDER_HEAD_SHIFT = AnimationsRegistrySeasonal.register("spider_head/spider_head_lololoshka_shift");
   @SpecialAnim("SPIDER_HEAD")
   IAnimationSource SPIDER_HEAD_JUMP = AnimationsRegistrySeasonal.register("spider_head/spider_head_lololoshka_jump");
   @SpecialAnim("SPIDER_HEAD")
   IAnimationSource SPIDER_HEAD_LAND = AnimationsRegistrySeasonal.register("spider_head/spider_head_lololoshka_land");
   @SpecialAnim("SPIDER_HEAD")
   IAnimationSource SPIDER_HEAD_ATTACK = AnimationsRegistrySeasonal.register("spider_head/spider_head_lololoshka_attack");
   @SpecialAnim("SPIDER_HEAD")
   IAnimationSource SPIDER_HEAD_DAMAGE = AnimationsRegistrySeasonal.register("spider_head/spider_head_lololoshka_damage");
   @SpecialAnim("SPIDER_HEAD")
   IAnimationSource SPIDER_HEAD_DEATH = AnimationsRegistrySeasonal.register("spider_head/spider_head_lololoshka_death");
   @SpecialAnim("SPIDER_HEAD")
   IAnimationSource SPIDER_HEAD_10_DEGREE_HEAD = AnimationsRegistrySeasonal.register("spider_head/spider_head_lololoshka_10_degree_head");
   @SpecialAnim("SPIDER_HEAD")
   IAnimationSource SPIDER_HEAD_FROZE = AnimationsRegistrySeasonal.register("spider_head/spider_head_lololoshka_froze");
   @SpecialAnim("SPIDER_HEAD")
   IAnimationSource SPIDER_HEAD_JUMP_FROM_SLEEP = AnimationsRegistrySeasonal.register("spider_head/spider_head_lololoshka_jump_from_sleep");
   @SpecialAnim("SPIDER_HEAD")
   IAnimationSource SPIDER_HEAD_SIT = AnimationsRegistrySeasonal.register("spider_head/spider_head_lololoshka_sit");
   @SpecialAnim("SPIDER_HEAD")
   IAnimationSource SPIDER_HEAD_SIT_START_CRY = AnimationsRegistrySeasonal.register("spider_head/spider_head_lololoshka_sit_start_cry");
   @SpecialAnim("SPIDER_HEAD")
   IAnimationSource SPIDER_HEAD_SIT_CRY = AnimationsRegistrySeasonal.register("spider_head/spider_head_lololoshka_sit_cry");
   @SpecialAnim("SPIDER_HEAD")
   IAnimationSource SPIDER_HEAD_TIRED_POSE = AnimationsRegistrySeasonal.register("spider_head/spider_head_lololoshka_tired_pose");
   @SpecialAnim("SPIDER_HEAD")
   SidedAnimationSet SPIDER_HEAD_KICK = AnimationsRegistrySeasonal.registerSided(
      "spider_head_lololoshka_kick_right", "spider_head_lololoshka_kick_left", "spider_head/spider_head_lololoshka_kick"
   );
   @SpecialAnim("SPIDER_HEAD")
   SidedAnimationSet SPIDER_HEAD_SLEEP = AnimationsRegistrySeasonal.registerSided(
      "spider_head_lololoshka_sleep_right", "spider_head_lololoshka_sleep_left", "spider_head/spider_head_lololoshka_sleep"
   );
   @SpecialAnim("SPIDER_HEAD")
   SidedAnimationSet SPIDER_HEAD_SLEEP_CYCLE = AnimationsRegistrySeasonal.registerSided(
      "spider_head_lololoshka_sleep_cycle_right", "spider_head_lololoshka_sleep_cycle_left", "spider_head/spider_head_lololoshka_sleep_cycle"
   );
   @SpecialAnim("HEAD_LOLOLOSHKA")
   IAnimationSource HEAD_LOLOLOSHKA_START_SPIN = AnimationsRegistrySeasonal.register("head_lololoshka/head_lololoshka_start_spin");
   @SpecialAnim("HEAD_LOLOLOSHKA")
   IAnimationSource HEAD_LOLOLOSHKA_SPIN = AnimationsRegistrySeasonal.register("head_lololoshka/head_lololoshka_spin");
   @SpecialAnim("HEAD_LOLOLOSHKA")
   IAnimationSource HEAD_LOLOLOSHKA_END_SPIN = AnimationsRegistrySeasonal.register("head_lololoshka/head_lololoshka_end_spin");
   @SpecialAnim("HEAD_LOLOLOSHKA")
   IAnimationSource HEAD_LOLOLOSHKA_ROTATE = AnimationsRegistrySeasonal.register("head_lololoshka/head_lololoshka_rotate");
   @SpecialAnim("HEAD_LOLOLOSHKA_ANT")
   IAnimationSource HEAD_LOLOLOSHKA_ANT_IDLE = AnimationsRegistrySeasonal.register("head_lololoshka_ant/head_lololoshka_ant_idle");
   @SpecialAnim("HEAD_LOLOLOSHKA_ANT")
   IAnimationSource HEAD_LOLOLOSHKA_ANT_WALK = AnimationsRegistrySeasonal.register("head_lololoshka_ant/head_lololoshka_ant_walk");
   @SpecialAnim("HEAD_LOLOLOSHKA_ANT")
   IAnimationSource CUTSCENE_HEAD_LOLOLOSHKA_ANT_TAKE_HEAD = AnimationsRegistrySeasonal.register("cutscene/cutscene_head_lololoshka_ant_take_head");
   @SpecialAnim("HEAD_LOLOLOSHKA_ANT")
   IAnimationSource HEAD_LOLOLOSHKA_ANT_STAGGER = AnimationsRegistrySeasonal.register("head_lololoshka_ant/head_lololoshka_ant_stagger");
   @SpecialAnim("HEAD_LOLOLOSHKA_ANT")
   IAnimationSource HEAD_LOLOLOSHKA_ANT_TRANSFORM_TO_SPIDER = AnimationsRegistrySeasonal.register("head_lololoshka_ant/head_lololoshka_ant_transform_to_spider");
   @SpecialAnim("HEAD_LOLOLOSHKA_ANT")
   IAnimationSource CUTSCENE_HEAD_LOLOLOSHKA_ANT_TAKE_OFF_HEAD = AnimationsRegistrySeasonal.register("cutscene/cutscene_head_lololoshka_ant_take_off_head");
   @SpecialAnim("HEAD_LOLOLOSHKA_ANT")
   IAnimationSource HEAD_LOLOLOSHKA_ANT_LONG_JUMP = AnimationsRegistrySeasonal.register("head_lololoshka_ant/head_lololoshka_ant_long_jump");
   @SpecialAnim("HEAD_LOLOLOSHKA_ANT")
   IAnimationSource HEAD_LOLOLOSHKA_ANT_LONG_LAND = AnimationsRegistrySeasonal.register("head_lololoshka_ant/head_lololoshka_ant_long_land");
   IAnimationSource EIDOS_IDLE = AnimationsRegistrySeasonal.register("eidos_idle");
   @SpecialAnim("SKY_EYE")
   IAnimationSource SKYEYE_BASE = AnimationsRegistrySeasonal.register("sky_eye/skyeye_base");
   IAnimationSource SKYEYE_OPEN = AnimationsRegistrySeasonal.register("sky_eye/skyeye_open");
   IAnimationSource SKYEYE_FEAR = AnimationsRegistrySeasonal.register("sky_eye/skyeye_fear");
   IAnimationSource SKYEYE_INTEREST = AnimationsRegistrySeasonal.register("sky_eye/skyeye_interest");
   IAnimationSource SKYEYE_LAUGH = AnimationsRegistrySeasonal.register("sky_eye/skyeye_laugh");
   IAnimationSource SKYEYE_LOOK_RIGHT_TO_LEFT = AnimationsRegistrySeasonal.register("sky_eye/skyeye_look_right_to_left");
   IAnimationSource SKYEYE_CLOSE = AnimationsRegistrySeasonal.register("sky_eye/skyeye_close");
   IAnimationSource SKYEYE_SATISFACTION = AnimationsRegistrySeasonal.register("sky_eye/skyeye_satisfaction");
   IAnimationSource SKYEYE_SIGH = AnimationsRegistrySeasonal.register("sky_eye/skyeye_sigh");
   IAnimationSource SKYEYE_SURPRISE = AnimationsRegistrySeasonal.register("sky_eye/skyeye_surprise");
   IAnimationSource SKYEYE_SLIGHT_ANGRY = AnimationsRegistrySeasonal.register("sky_eye/skyeye_slight_angry");
   IAnimationSource FLOATING_CYCLE_LAYER = AnimationsRegistrySeasonal.register("humanoid/floating_cycle_layer");
   IAnimationSource FLOATING_POSE = AnimationsRegistrySeasonal.register("humanoid/floating_pose");
   IAnimationSource EYES_UP_NARROW_EYED = AnimationsRegistrySeasonal.register("humanoid/eyes_up_for_narrow_eyed");
   IAnimationSource EYES_DOWN_NARROW_EYED = AnimationsRegistrySeasonal.register("humanoid/eyes_down_for_narrow_eyed");
   IAnimationSource RUBBING_HANDS_LOOP = AnimationsRegistrySeasonal.register("humanoid/rubbing_hands_loop");
   IAnimationSource ANU_POSE = AnimationsRegistrySeasonal.register("anu_pose");
   IAnimationSource UNIT_BASE_BIGGER_STRONG_PUNCH = AnimationsRegistrySeasonal.register("humanoid/unit_base_bigger_strong_punch");
   IAnimationSource MUD_MAN_FALL_FROM_PUNCH = AnimationsRegistrySeasonal.register("humanoid/mud_man_fall_from_punch");
   @SpecialAnim("BLUE_MAN")
   SidedAnimationSet BLUE_MAN_SCRATCHHEAD = AnimationsRegistrySeasonal.registerSided(
      "blue_man_scratchhead_right", "blue_man_scratchhead_left", "humanoid/blue_man_scratchhead"
   );
   IAnimationSource FLYTRAP_ATTACK = AnimationsRegistrySeasonal.register("flytrap/flytrap_attack");
   IAnimationSource FLYTRAP_DEAD = AnimationsRegistrySeasonal.register("flytrap/flytrap_dead");
   IAnimationSource FLYTRAP_IDLE = AnimationsRegistrySeasonal.register("flytrap/flytrap_idle");
   IAnimationSource FLYTRAP_OUT_FROM_GROUND = AnimationsRegistrySeasonal.register("flytrap/flytrap_out_from_ground");
   IAnimationSource FLYTRAP_UNDERGROUND = AnimationsRegistrySeasonal.register("flytrap/flytrap_underground");
   IAnimationSource CLOWN_BOX_CLOSED_POSE = AnimationsRegistrySeasonal.register("clown_box/clown_box_closed_pose");
   IAnimationSource CLOWN_BOX_OPEN_POSE = AnimationsRegistrySeasonal.register("clown_box/clown_box_open_pose");
   IAnimationSource CLOWN_BOX_OPEN_ENDANIMATED = AnimationsRegistrySeasonal.register("clown_box/clown_box_open_endanimated");
   IAnimationSource PAMPUSHKA_MUSHROOM_JUMP = AnimationsRegistrySeasonal.register("pampushka_mushroom_jump");
   IAnimationSource BUTTERFLY_FLY = AnimationsRegistrySeasonal.register("butterfly_fly");
   IAnimationSource CUTSCENE_ANT_PILLAR_JUMP = AnimationsRegistrySeasonal.register("cutscene/cutscene_ant_pillar_jump");
   @SpecialAnim("CUTSCENE_MERGE_ANT")
   IAnimationSource CUTSCENE_MERGE_ANT_JUMP = AnimationsRegistrySeasonal.register("cutscene/cutscene_merge_ant_jump");
   @SpecialAnim("CUTSCENE_MERGE_ANT")
   IAnimationSource CUTSCENE_MERGE_ANT_BIGGER = AnimationsRegistrySeasonal.register("cutscene/cutscene_merge_ant_bigger");
   IAnimationSource SIMULATOR_IDLE = AnimationsRegistrySeasonal.register("simulator_block_idle");
   IAnimationSource SIMULATOR_HOLO_IDLE = AnimationsRegistrySeasonal.register("simulator_hologram_idle");
   IAnimationSource HEAD_LAND = AnimationsRegistrySeasonal.register("head_land");
   IAnimationSource SKYEYE_SERIOUSNESS = AnimationsRegistrySeasonal.register("sky_eye/skyeye_seriousness");
   @SpecialAnim("ANT")
   SidedAnimationSet ANT_ROTATE_WALKING = AnimationsRegistrySeasonal.registerSided(
      "ant_rotate_walking_left", "ant_rotate_walking_right", "ant/ant_rotate_walking"
   );
   @SpecialAnim("HEAD_LOLOLOSHKA_ANT")
   SidedAnimationSet HEAD_LOLOLOSHKA_ANT_ROTATE_WALKING = AnimationsRegistrySeasonal.registerSided(
      "head_lololoshka_ant_rotate_right", "head_lololoshka_ant_rotate_left", "head_lololoshka_ant/head_lololoshka_ant_rotate_walking"
   );
   AnimationsRegistry.EyeGaze SKY_EYE_GAZE = new AnimationsRegistry.EyeGaze(
      AnimationsRegistrySeasonal.registerSidedSeparate("sky_eye/gaze/skyeye_eye_left", "sky_eye/gaze/skyeye_eye_empty"),
      AnimationsRegistrySeasonal.registerSidedSeparate("sky_eye/gaze/skyeye_eye_right", "sky_eye/gaze/skyeye_eye_empty"),
      AnimationsRegistrySeasonal.register("sky_eye/gaze/skyeye_eye_up"),
      AnimationsRegistrySeasonal.register("sky_eye/gaze/skyeye_eye_down")
   );
}
