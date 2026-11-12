package com.storyteam.storytelling.client.animations;

import com.storyteam.storytelling.api.Since;
import com.storyteam.storytelling.api.animations.SpecialAnim;
import com.storyteam.storytelling.client.AnimationsRegistrySeasonal;
import com.storyteam.storytelling.client.core.SidedAnimationSet;
import org.zeith.hammeranims.api.animation.IAnimationSource;

@Since(Since.Season.LR)
public interface AnimationsST5 {
   IAnimationSource EMOTION_CLOSED_EYES = AnimationsRegistrySeasonal.register("closed_eyes_pose");
   IAnimationSource EMOTION_EYE_ROLLING_NEW = AnimationsRegistrySeasonal.register("emotions/eye_rolling_new");
   IAnimationSource SIT_TO_LAY_ON_BACK_LEFT = AnimationsRegistrySeasonal.register("sit_to_lay_back_left");
   @SpecialAnim
   IAnimationSource ABILKA_CHECKING = AnimationsRegistrySeasonal.register("city_cat_checking");
   @SpecialAnim
   IAnimationSource ABILKA_EAT = AnimationsRegistrySeasonal.register("city_cat_eat");
   @SpecialAnim
   IAnimationSource ABILKA_IDLE = AnimationsRegistrySeasonal.register("city_cat_idle");
   @SpecialAnim
   IAnimationSource ABILKA_LAY = AnimationsRegistrySeasonal.register("city_cat_lay");
   @SpecialAnim
   IAnimationSource ABILKA_PET = AnimationsRegistrySeasonal.register("city_cat_pet");
   @SpecialAnim
   IAnimationSource ABILKA_SIT = AnimationsRegistrySeasonal.register("city_cat_sit");
   @SpecialAnim
   IAnimationSource ABILKA_SLEEP = AnimationsRegistrySeasonal.register("city_cat_sleep");
   @SpecialAnim
   IAnimationSource ABILKA_STRETCH = AnimationsRegistrySeasonal.register("city_cat_stretch");
   @SpecialAnim
   IAnimationSource ABILKA_WALK = AnimationsRegistrySeasonal.register("city_cat_walk");
   @SpecialAnim
   IAnimationSource CITY_CAT_HOLDED = AnimationsRegistrySeasonal.register("city_cat_holded");
   @SpecialAnim
   IAnimationSource ABILKA_JUMP = AnimationsRegistrySeasonal.register("cat_jump");
   @SpecialAnim
   IAnimationSource ABILKA_PLAY = AnimationsRegistrySeasonal.register("cat_play");
   @SpecialAnim
   IAnimationSource ABILKA_SCRATCH = AnimationsRegistrySeasonal.register("cat_scratching_post");
   @SpecialAnim
   IAnimationSource ABILKA_POOP = AnimationsRegistrySeasonal.register("cat_pooping");
   @SpecialAnim
   IAnimationSource ABILKA_DIG = AnimationsRegistrySeasonal.register("cat_digging");
   @SpecialAnim
   IAnimationSource CAT_SUBARU_DANCE = AnimationsRegistrySeasonal.register("cats/cat_subaru_dance");
   @SpecialAnim
   IAnimationSource CAT2_ROLL = AnimationsRegistrySeasonal.register("cat2_roll");
   @SpecialAnim
   IAnimationSource KITTEN_LAY = AnimationsRegistrySeasonal.register("kitten_lay");
   @SpecialAnim
   IAnimationSource KITTEN_SLEEP = AnimationsRegistrySeasonal.register("cats/city_kitten_sleep");
   @SpecialAnim
   IAnimationSource PET_HOLDING = AnimationsRegistrySeasonal.register("pet_holded");
   @SpecialAnim
   IAnimationSource KITTEN_SIT = AnimationsRegistrySeasonal.register("cats/city_kitten_sit");
   @SpecialAnim
   IAnimationSource PET_EAT = AnimationsRegistrySeasonal.register("pets/city_pet_eat");
   @SpecialAnim
   IAnimationSource PET_STROKING_START = AnimationsRegistrySeasonal.register("pet_stroking_start");
   @SpecialAnim
   IAnimationSource PET_STROKING_CYCLE = AnimationsRegistrySeasonal.register("pet_stroking_cycle");
   @SpecialAnim
   IAnimationSource KITTEN_JUMP = AnimationsRegistrySeasonal.register("kitten_jump");
   @SpecialAnim
   IAnimationSource GIGA_KITTEN_WALK = AnimationsRegistrySeasonal.register("giga_kitten_walk", "giga_kitten_walk");
   @SpecialAnim
   IAnimationSource GIGA_KITTEN_WALK_NOHANDS = AnimationsRegistrySeasonal.register("giga_kitten_walk", "giga_kitten_walk_nohands");
   @SpecialAnim
   IAnimationSource GIGA_KITTEN_HOLD = AnimationsRegistrySeasonal.register("giga_kitten_hold");
   @SpecialAnim
   IAnimationSource KITTEN_WASH = AnimationsRegistrySeasonal.register("city_kitten_wash");
   @SpecialAnim
   IAnimationSource DOG_ATTACK_ENDANIMATED = AnimationsRegistrySeasonal.register("dog/dog_attack_endanimated");
   @SpecialAnim
   IAnimationSource DOG_LAY_POSE = AnimationsRegistrySeasonal.register("dog/dog_lay_pose");
   @SpecialAnim
   IAnimationSource DOG_RUN_CYCLE = AnimationsRegistrySeasonal.register("dog/dog_run_cycle");
   @SpecialAnim
   IAnimationSource DOG_SIT_POSE = AnimationsRegistrySeasonal.register("dog/dog_sit_pose");
   @SpecialAnim
   IAnimationSource PUPPY_LAY_POSE = AnimationsRegistrySeasonal.register("dog/puppy_lay_pose");
   @SpecialAnim
   IAnimationSource PUPPY_LAY_SLEEP = AnimationsRegistrySeasonal.register("dog/puppy_lay_sleep");
   @SpecialAnim
   IAnimationSource PUPPY_SIT_POSE = AnimationsRegistrySeasonal.register("dog/puppy_sit_pose");
   @SpecialAnim
   IAnimationSource TAIL_WIGGLE = AnimationsRegistrySeasonal.register("dog/tail_wiggle");
   @SpecialAnim
   IAnimationSource BIG_DOG_GET_UP = AnimationsRegistrySeasonal.register("dog/big_dog_get_up");
   @SpecialAnim
   IAnimationSource BIG_DOG_ON_SHOULDERS = AnimationsRegistrySeasonal.register("dog/big_dog_on_shoulders");
   @SpecialAnim
   IAnimationSource BIG_DOG_SIDE_LAY = AnimationsRegistrySeasonal.register("dog/big_dog_side_lay");
   @SpecialAnim
   IAnimationSource BIG_DOG_LAY = AnimationsRegistrySeasonal.register("dog/big_dog_lay");
   @SpecialAnim
   IAnimationSource SOLUS_HAT_GET_ITEM = AnimationsRegistrySeasonal.register("custom/simon_solus/solus_hat_get_item");
   @SpecialAnim
   IAnimationSource TARGET_FALLING = AnimationsRegistrySeasonal.register("custom/target_falling");
   IAnimationSource PSYCHO_LAUGH = AnimationsRegistrySeasonal.register("humanoid/psycho_laugh");
   IAnimationSource DRAMATIC_HAND = AnimationsRegistrySeasonal.register("humanoid/dramatic_hand");
   IAnimationSource POPCORN_KEEPING = AnimationsRegistrySeasonal.register("humanoid/popcorn_eating", "popcorn_keeping");
   SidedAnimationSet POPCORN_EATING = AnimationsRegistrySeasonal.registerSided("popcorn_eating_right", "popcorn_eating_left", "humanoid/popcorn_eating");
   IAnimationSource CARDS_OFFER = AnimationsRegistrySeasonal.register("humanoid/cards_offer");
   IAnimationSource CARDSTHROW = AnimationsRegistrySeasonal.register("humanoid/cards_throw");
   IAnimationSource WIPE_MOUTH_ENDANIMATED = AnimationsRegistrySeasonal.register("humanoid/wipe_mouth_endanimated");
   IAnimationSource EATING_CYCLE = AnimationsRegistrySeasonal.register("humanoid/eating_cycle");
   IAnimationSource KID_EATING_CYCLE = AnimationsRegistrySeasonal.register("humanoid/kid_eating_cycle");
   IAnimationSource EATING_CYCLE_TALL = AnimationsRegistrySeasonal.register("humanoid/eating_cycle_tall");
   IAnimationSource WINK_PICKED_SHOULDER = AnimationsRegistrySeasonal.register("humanoid/wink_picked_shoulder");
   IAnimationSource PICKED_SHOULDER_POSE = AnimationsRegistrySeasonal.register("humanoid/picked_shoulder_pose");
   IAnimationSource PICKED_UP_FROM_BED = AnimationsRegistrySeasonal.register("humanoid/picked_up_from_bed");
   IAnimationSource PICKUP_FROM_BED = AnimationsRegistrySeasonal.register("humanoid/pickup_from_bed");
   IAnimationSource PICKUP_SHOULDER_POSE = AnimationsRegistrySeasonal.register("humanoid/pickup_shoulder_pose");
   IAnimationSource HOLDING_OUT_POSE = AnimationsRegistrySeasonal.register("holding_out_pose");
   IAnimationSource HOLDING_OUT_ENDANIMATED = AnimationsRegistrySeasonal.register("holding_out_endanimated");
   IAnimationSource CRYING_CYCLE = AnimationsRegistrySeasonal.register("crying_cycle");
   IAnimationSource EXAMINE_LEFT_HAND = AnimationsRegistrySeasonal.register("hands/examine_left_hand");
   IAnimationSource ORANGE_BAG_TRANSFER = AnimationsRegistrySeasonal.register("orange_bag_transfer");
   IAnimationSource WASHING_WALL = AnimationsRegistrySeasonal.register("hands/washing_wall");
   IAnimationSource SQUATDOWN_POSE = AnimationsRegistrySeasonal.register("squatdown_pose");
   IAnimationSource SPLASHES_HANDS = AnimationsRegistrySeasonal.register("hands/splashes_hands");
   IAnimationSource PREPARE_HIT = AnimationsRegistrySeasonal.register("prepare_hit");
   IAnimationSource DEFEND_FROM_HIT = AnimationsRegistrySeasonal.register("defend_from_hit");
   IAnimationSource FASTER_POCKETS_CHECK = AnimationsRegistrySeasonal.register("hands/faster_pocket_check");
   IAnimationSource BODY_POSE_LYINGONHANDS_IN_PLACE = AnimationsRegistrySeasonal.register("body_pose_lyingonhands_in_place");
   IAnimationSource WALL_PAINTING_CYCLE = AnimationsRegistrySeasonal.register("wall_painting_cycle");
   IAnimationSource BUSH_CUT_CYCLE = AnimationsRegistrySeasonal.register("bush_cut_cycle");
   IAnimationSource FINGER_COUNT = AnimationsRegistrySeasonal.register("finger_count");
   IAnimationSource FINGER_COUNT_REPEAT = AnimationsRegistrySeasonal.register("finger_count_repeat");
   IAnimationSource ARMSIDE_POSE_LEFT = AnimationsRegistrySeasonal.register("armside_pose_left");
   @SpecialAnim
   IAnimationSource CAR_WHEELS = AnimationsRegistrySeasonal.register("car_wheels");
   @SpecialAnim
   IAnimationSource ENGINE_LOOP = AnimationsRegistrySeasonal.register("engine_loop");
   IAnimationSource CYCLE_WATERING_LEFT = AnimationsRegistrySeasonal.register("cycle_watering_left");
   IAnimationSource HAPPY_SHAKING = AnimationsRegistrySeasonal.register("happy_shaking");
   IAnimationSource TOTAL_SHOCK = AnimationsRegistrySeasonal.register("emotions/total_shock");
   IAnimationSource HUMANOID_FUNKY_LAY = AnimationsRegistrySeasonal.register("funky_lay");
   IAnimationSource HUMANOID_BACKFLIP = AnimationsRegistrySeasonal.register("backflip");
   @SpecialAnim
   IAnimationSource BUS_DOOR_CLOSE = AnimationsRegistrySeasonal.register("bus_door_close");
   @SpecialAnim
   IAnimationSource BUS_DOOR_OPEN = AnimationsRegistrySeasonal.register("bus_door_open");
   IAnimationSource HOLD_CLIPBOARD = AnimationsRegistrySeasonal.register("hold_clipboard");
   IAnimationSource LOOKUP_ENDANIMATED = AnimationsRegistrySeasonal.register("lookup_endanimated");
   IAnimationSource SLOW_TYPING_LEFT = AnimationsRegistrySeasonal.register("slow_typing_left");
   IAnimationSource SLOW_TYPING_RIGHT = AnimationsRegistrySeasonal.register("slow_typing_right");
   IAnimationSource SIT_SLEEP_POSE = AnimationsRegistrySeasonal.register("sit_sleep_pose");
   IAnimationSource TYPING_CYCLE = AnimationsRegistrySeasonal.register("typing_cycle");
   IAnimationSource HOLD_LEFT_LAPTOP = AnimationsRegistrySeasonal.register("laptop_hold_left");
   IAnimationSource HOLD_RIGHT_LAPTOP = AnimationsRegistrySeasonal.register("laptop_hold_right");
   @SpecialAnim
   IAnimationSource PEN_CLOSE = AnimationsRegistrySeasonal.register("pen_close");
   IAnimationSource TAKEOFF_HEADPHONES = AnimationsRegistrySeasonal.register("takeoff_headphones");
   IAnimationSource HUMANOID_LAY_RIGHT_TO_SIT = AnimationsRegistrySeasonal.register("lyingonside_right_to_sit_straight");
   IAnimationSource HUMANOID_LAY_RIGHT_NEW = AnimationsRegistrySeasonal.register("body_pose_lyingonside_right");
   IAnimationSource HUMANOID_LAY_LEFT_NEW = AnimationsRegistrySeasonal.register("body_pose_lyingonside_left");
   IAnimationSource LYING_PHONE_TAP_LEFT = AnimationsRegistrySeasonal.register("lying_phone_tap_left");
   IAnimationSource LYING_PHONE_TAP_RIGHT = AnimationsRegistrySeasonal.register("lying_phone_tap_right");
   IAnimationSource OFFERHAND_MONEY_LEFT = AnimationsRegistrySeasonal.register("offerhand_money_left");
   IAnimationSource OFFERHAND_MONEY_RIGHT = AnimationsRegistrySeasonal.register("offerhand_money_right");
   IAnimationSource LAPTOP_TYPE_LEFT = AnimationsRegistrySeasonal.register("laptop_type_left");
   IAnimationSource LAPTOP_TYPE_RIGHT = AnimationsRegistrySeasonal.register("laptop_type_right");
   IAnimationSource HAIR_FLEX = AnimationsRegistrySeasonal.register("hair_flex");
   IAnimationSource EATING_CYCLE_LEFT_ON_HAND = AnimationsRegistrySeasonal.register("eating_cycle_left_hold_hand");
   IAnimationSource EATING_CYCLE_RIGHT_ON_HAND = AnimationsRegistrySeasonal.register("eating_cycle_right_hold_hand");
   IAnimationSource PISTOL_FINGERS = AnimationsRegistrySeasonal.register("pistol_fingers");
   IAnimationSource BRANDON_POSING_1 = AnimationsRegistrySeasonal.register("brandon_posing1");
   IAnimationSource BRANDON_POSING_2 = AnimationsRegistrySeasonal.register("brandon_posing2");
   IAnimationSource BRANDON_POSING_3 = AnimationsRegistrySeasonal.register("brandon_posing3");
   IAnimationSource BRANDON_POSING_4 = AnimationsRegistrySeasonal.register("brandon_posing4");
   IAnimationSource BRANDON_POSING_5 = AnimationsRegistrySeasonal.register("brandon_posing5");
   IAnimationSource BRANDON_POSING_6 = AnimationsRegistrySeasonal.register("brandon_posing6");
   IAnimationSource BRANDON_POSING_7 = AnimationsRegistrySeasonal.register("brandon_posing7");
   IAnimationSource BRANDON_POSING_8 = AnimationsRegistrySeasonal.register("brandon_posing8");
   IAnimationSource LEAN_RAISE_LEG = AnimationsRegistrySeasonal.register("lean_raise_leg");
   IAnimationSource LEAN_TWOHANDS = AnimationsRegistrySeasonal.register("lean_twohands");
   IAnimationSource CITY_BRANDON_WALK = AnimationsRegistrySeasonal.register("city_brandon_walk");
   IAnimationSource POINT_UP = AnimationsRegistrySeasonal.register("point_up");
   IAnimationSource BROTHER_HUG = AnimationsRegistrySeasonal.register("brother_hug");
   IAnimationSource DRAMATIC_GRIEF_ENDANIMATED = AnimationsRegistrySeasonal.register("dramatic_grief_endanimated");
   IAnimationSource SQUATS_CYCLE = AnimationsRegistrySeasonal.register("squats_cycle");
   IAnimationSource RAISING_KNEES_CYCLE = AnimationsRegistrySeasonal.register("raising_knees_cycle");
   IAnimationSource MILL_CYCLE = AnimationsRegistrySeasonal.register("mill_cycle");
   IAnimationSource LYING_WITH_LOOKING_PHONE_POSE = AnimationsRegistrySeasonal.register("lying_with_looking_phone_pose");
   IAnimationSource NPC_HIDEEYES = AnimationsRegistrySeasonal.register("npc_hideeyes");
   IAnimationSource WINE_TRAY_HOLD_RIGHT = AnimationsRegistrySeasonal.register("vineglass_care");
   IAnimationSource POCKET_TAKE_LEFT = AnimationsRegistrySeasonal.register("pocket_take_left");
   IAnimationSource POCKET_TAKE_RIGHT = AnimationsRegistrySeasonal.register("pocket_take_right");
   IAnimationSource DANCING_CYCLE = AnimationsRegistrySeasonal.register("dancing_cycle");
   IAnimationSource PHONE_MIRROR = AnimationsRegistrySeasonal.register("phone_mirror");
   IAnimationSource ENDANIMATED_PAT_PIG_LEFT = AnimationsRegistrySeasonal.register("endanimated_pat_pig_left");
   IAnimationSource ENDANIMATED_PAT_PIG_RIGHT = AnimationsRegistrySeasonal.register("endanimated_pat_pig_right");
   IAnimationSource ENDANIMATED_PAT_SHEEP_RIGHT = AnimationsRegistrySeasonal.register("endanimated_pat_sheep_left");
   IAnimationSource ENDANIMATED_PAT_SHEEP_LEFT = AnimationsRegistrySeasonal.register("endanimated_pat_sheep_right");
   IAnimationSource EASEL_PAINTING_CYCLE = AnimationsRegistrySeasonal.register("easel_painting_cycle");
   IAnimationSource POKE = AnimationsRegistrySeasonal.register("poke_endanimated");
   IAnimationSource LOWER_EYES = AnimationsRegistrySeasonal.register("lower_eyes");
   IAnimationSource HOLD_PHONE = AnimationsRegistrySeasonal.register("hold_phone");
   IAnimationSource HOLD_PHONE_TYPING_LEFT = AnimationsRegistrySeasonal.register("hold_phone_typing_left");
   IAnimationSource HOLD_PHONE_TYPING_RIGHT = AnimationsRegistrySeasonal.register("hold_phone_typing_right");
   @SpecialAnim
   IAnimationSource ANIMAL_WALK = AnimationsRegistrySeasonal.register("animal_walk");
   IAnimationSource EMOTION_HAPPY_NEW = AnimationsRegistrySeasonal.register("emotion_happy");
   IAnimationSource SHY_HI_ENDANIMATED = AnimationsRegistrySeasonal.register("shy_hi_endanimated");
   IAnimationSource SHY_POSE = AnimationsRegistrySeasonal.register("shy_pose");
   IAnimationSource WARM_UP_CYCLE = AnimationsRegistrySeasonal.register("warm_up_cycle");
   IAnimationSource COCKTAIL_PLAY = AnimationsRegistrySeasonal.register("cocktail_play");
   IAnimationSource VILLAGER_IMITATE = AnimationsRegistrySeasonal.register("villager_imitate");
   IAnimationSource EMOTION_SAD1 = AnimationsRegistrySeasonal.register("emotions/emotion_sad1");
   IAnimationSource EMOTION_SAD2 = AnimationsRegistrySeasonal.register("emotions/emotion_sad2");
   IAnimationSource EMOTION_SAD3 = AnimationsRegistrySeasonal.register("emotions/emotion_sad3");
   IAnimationSource DONTCARE_SHOULDERS = AnimationsRegistrySeasonal.register("dontcare_shoulders");
   IAnimationSource DUMBBELL_LIFT_CYCLE = AnimationsRegistrySeasonal.register("dumbbell_lift_cycle");
   IAnimationSource DUMBBELL_LIFT_CYCLE_2 = AnimationsRegistrySeasonal.register("dumbbell_lift_cycle2");
   IAnimationSource DONT_LISTEN = AnimationsRegistrySeasonal.register("dont_listen");
   IAnimationSource IRONY_POSE = AnimationsRegistrySeasonal.register("irony_pose");
   IAnimationSource HALF_OPEN_EYES = AnimationsRegistrySeasonal.register("halfopen_eyes");
   IAnimationSource LYING_STOMACH = AnimationsRegistrySeasonal.register("lying_stomach");
   IAnimationSource STOMACH_TO_LYING_BACK = AnimationsRegistrySeasonal.register("stomach_to_lying_back");
   IAnimationSource LYING_STOMACH_GETUP = AnimationsRegistrySeasonal.register("lying_stomach_getup");
   IAnimationSource STRONG_HUG_CYCLE = AnimationsRegistrySeasonal.register("stronghug_cycle");
   IAnimationSource SPRAY_PERFUME = AnimationsRegistrySeasonal.register("spray_perfume");
   IAnimationSource BODY_SIT_ROWING = AnimationsRegistrySeasonal.register("body_sit_rowing");
   IAnimationSource WINK_EYEBROW = AnimationsRegistrySeasonal.register("wink_eyebrow");
   IAnimationSource NUH_UH_ENDANIMATED = AnimationsRegistrySeasonal.register("nuh_uh_endanimated");
   IAnimationSource STEP_BACK = AnimationsRegistrySeasonal.register("step_back");
   IAnimationSource RUN_FLIP_JUMP = AnimationsRegistrySeasonal.register("run_flip_jump");
   IAnimationSource SHAKING_HEAD = AnimationsRegistrySeasonal.register("shaking_head");
   IAnimationSource BACKPACK_TOOK_OFF = AnimationsRegistrySeasonal.register("backpack_took_off");
   IAnimationSource LETHAL_COMP_DANCE = AnimationsRegistrySeasonal.register("lethal_company_dance");
   IAnimationSource POOL_BORDER_LEAN = AnimationsRegistrySeasonal.register("staying_above_water_pose");
   IAnimationSource PHONE_PHOTOS = AnimationsRegistrySeasonal.register("twohand_hold_phone");
   IAnimationSource MENACE_FIST_LEFT = AnimationsRegistrySeasonal.register("menace_fist_left");
   IAnimationSource FLIP_PHONE = AnimationsRegistrySeasonal.register("flip_phone");
   IAnimationSource SWAYING_CYCLE = AnimationsRegistrySeasonal.register("swaying_cycle");
   IAnimationSource PHONE_EAR_LEFT = AnimationsRegistrySeasonal.register("phone_ear_left");
   IAnimationSource PHONE_EAR_RIGHT = AnimationsRegistrySeasonal.register("phone_ear_right");
   IAnimationSource SHOW_SO_SMALL = AnimationsRegistrySeasonal.register("show_so_small");
   IAnimationSource MONEY_FAN = AnimationsRegistrySeasonal.register("money_fan_pose");
   IAnimationSource FILM_CAMERA = AnimationsRegistrySeasonal.register("film_camera");
   IAnimationSource HOLD_CAMERA = AnimationsRegistrySeasonal.register("hold_camera");
   IAnimationSource ADJUST_HAT = AnimationsRegistrySeasonal.register("adjust_hat");
   IAnimationSource CIRCLE_HANDS = AnimationsRegistrySeasonal.register("circle_hands");
   IAnimationSource SAFE_WORK_CYCLE = AnimationsRegistrySeasonal.register("safe_work_cycle");
   IAnimationSource DISHES_CYCLE = AnimationsRegistrySeasonal.register("humanoid/dishes_cycle");
   IAnimationSource HANDS_WASH = AnimationsRegistrySeasonal.register("humanoid/hands_wash");
   IAnimationSource EATING_WITH_PHONE_CYCLE_LEFT = AnimationsRegistrySeasonal.register("eating_with_phone_cycle_left");
   IAnimationSource EATING_WITH_PHONE_CYCLE_RIGHT = AnimationsRegistrySeasonal.register("eating_with_phone_cycle_right");
   IAnimationSource SEE_WATCH_RIGHT = AnimationsRegistrySeasonal.register("see_watch_right");
   IAnimationSource CAT_PETTING = AnimationsRegistrySeasonal.register("cat_petting");
   IAnimationSource PIANO_PLAYING = AnimationsRegistrySeasonal.register("piano_playing");
   IAnimationSource DONT_CARE_POSE = AnimationsRegistrySeasonal.register("dont_care_pose");
   IAnimationSource VIOLIN_CYCLE = AnimationsRegistrySeasonal.register("violin_cycle");
   IAnimationSource VIOLIN_PREPARE = AnimationsRegistrySeasonal.register("violin_prepare");
   IAnimationSource BIG_SHOULDERS_HUG = AnimationsRegistrySeasonal.register("big_shoulders_hug");
   IAnimationSource ROTATE_UPPER_BODY_LEFT = AnimationsRegistrySeasonal.register("rotate_upper_body_left");
   IAnimationSource ROTATE_UPPER_BODY_RIGHT = AnimationsRegistrySeasonal.register("rotate_upper_body_right");
   IAnimationSource PEEKING_SIDE_RIGHT = AnimationsRegistrySeasonal.register("humanoid/peeking_side_right");
   IAnimationSource PEEKING_SIDE_LEFT = AnimationsRegistrySeasonal.register("humanoid/peeking_side_left");
   IAnimationSource BOOK_SPY_LOOK = AnimationsRegistrySeasonal.register("book_spy_look");
   IAnimationSource BOOK_SPY_LOOK_DOWN = AnimationsRegistrySeasonal.register("book_spy_look_down");
   IAnimationSource HOLD_PLATE_LEFT = AnimationsRegistrySeasonal.register("hold_plate_left");
   IAnimationSource HOLD_PLATE_RIGHT = AnimationsRegistrySeasonal.register("hold_plate_right");
   IAnimationSource SHOW_PHONE = AnimationsRegistrySeasonal.register("show_phone");
   IAnimationSource LEG_CIRCLE = AnimationsRegistrySeasonal.register("leg_circle");
   IAnimationSource HEART_GRAB = AnimationsRegistrySeasonal.register("heart_grab");
   IAnimationSource CAT_HOLD_POSE = AnimationsRegistrySeasonal.register("cat_hold_pose");
   IAnimationSource SCARED_MOVE = AnimationsRegistrySeasonal.register("scared_move");
   IAnimationSource SPORT_BAG_ON = AnimationsRegistrySeasonal.register("sport_bag_on");
   IAnimationSource BARBELL_LIFT_CYCLE = AnimationsRegistrySeasonal.register("barbell_lift_cycle");
   IAnimationSource CAT_STROKING_SIT = AnimationsRegistrySeasonal.register("cat_stroking_sit");
   IAnimationSource SECURE_BARBELL = AnimationsRegistrySeasonal.register("secure_barbell");
   IAnimationSource WALK_NOHANDS_ALEX = AnimationsRegistrySeasonal.register("city_walk_alex_nohands");
   IAnimationSource WALK_NOHANDS_STEVE = AnimationsRegistrySeasonal.register("city_walk_steve_nohands");
   IAnimationSource SUITCASE_HOLD = AnimationsRegistrySeasonal.register("suitcase_hold");
   IAnimationSource PUSHUP_CYCLE = AnimationsRegistrySeasonal.register("pushup_cycle");
   IAnimationSource PUSHUP_PREPARE = AnimationsRegistrySeasonal.register("pushup_prepare");
   IAnimationSource SQUAT_CYCLE = AnimationsRegistrySeasonal.register("squat_cycle");
   IAnimationSource PRESS_SWING_PREPARE = AnimationsRegistrySeasonal.register("press_swing_prepare");
   IAnimationSource PRESS_SWING_CYCLE = AnimationsRegistrySeasonal.register("press_swing_cycle");
   IAnimationSource WARMUP_CYCLE1_LEFT = AnimationsRegistrySeasonal.register("warmup_cycle1_left");
   IAnimationSource WARMUP_CYCLE1_RIGHT = AnimationsRegistrySeasonal.register("warmup_cycle1_right");
   IAnimationSource WARMUP_CYCLE2 = AnimationsRegistrySeasonal.register("warmup_cycle2");
   IAnimationSource WARMUP_CYCLE3 = AnimationsRegistrySeasonal.register("warmup_cycle3");
   IAnimationSource HUG_YOURSELF_NOW = AnimationsRegistrySeasonal.register("hug_yourself_now");
   IAnimationSource CYCLE_RUBBING = AnimationsRegistrySeasonal.register("placeholder_cycle_rubbing_right");
   IAnimationSource SLEEP_ON_TABLE = AnimationsRegistrySeasonal.register("sleep_on_table");
   IAnimationSource LAYKNEES_TO_LEFT = AnimationsRegistrySeasonal.register("layknees_to_left");
   IAnimationSource FULLKNEES_TO_STAY = AnimationsRegistrySeasonal.register("fullknees_getup");
   IAnimationSource FULLKNEES_TO_STAY_NOHANDS = AnimationsRegistrySeasonal.register("fullknees_getup_noarms");
   IAnimationSource STAY_TO_FULLKNEES = AnimationsRegistrySeasonal.register("fullknees_getdown");
   IAnimationSource STAY_TO_FULLKNEES_NOHANDS = AnimationsRegistrySeasonal.register("fullknees_getdown_noarms");
   IAnimationSource KNEEL_LEFT = AnimationsRegistrySeasonal.register("lean_on_knee_noarms");
   IAnimationSource SMALL_SHAKING = AnimationsRegistrySeasonal.register("small_shaking");
   IAnimationSource BREATHE = AnimationsRegistrySeasonal.register("breath_cycle");
   IAnimationSource EMOTION_TERRIFIED = AnimationsRegistrySeasonal.register("terrified_emotion");
   IAnimationSource SHAKE_SHOULDERS = AnimationsRegistrySeasonal.register("shaking_someone");
   IAnimationSource SHAKEN_FROM_SHOULDERS = AnimationsRegistrySeasonal.register("shaked_on_ground");
   IAnimationSource FAINTED_LAY = AnimationsRegistrySeasonal.register("fainted_lay");
   IAnimationSource READING_NOHANDS = AnimationsRegistrySeasonal.register("cycle_reading_nohands");
   IAnimationSource BAG_PUNCHING = AnimationsRegistrySeasonal.register("bag_punching");
   IAnimationSource SHOP_DOOR_OPEN = AnimationsRegistrySeasonal.register("door/shop_door_open");
   IAnimationSource KNIFE_ATTACK2_LEFT = AnimationsRegistrySeasonal.register("knife_attack2_left");
   IAnimationSource KNIFE_ATTACK3_LEFT = AnimationsRegistrySeasonal.register("knife_attack3_left");
   IAnimationSource KNIFE_ATTACK_IDLE = AnimationsRegistrySeasonal.register("knife_attack_idle");
   IAnimationSource CASUAL_SQUAT = AnimationsRegistrySeasonal.register("casual_squat");
   IAnimationSource BAT_ATTACK_LEFT = AnimationsRegistrySeasonal.register("bat_attack_left");
   IAnimationSource BAT_ATTACK_RIGHT = AnimationsRegistrySeasonal.register("bat_attack_right");
   IAnimationSource FISTFIGHT_IDLE_NO_LEGS = AnimationsRegistrySeasonal.register("fistfight_idle_no_legs");
   IAnimationSource TAKE_SELFIE_STICK = AnimationsRegistrySeasonal.register("selfie_stick_take");
   IAnimationSource CARD_PUT_LEFT = AnimationsRegistrySeasonal.register("card_put_left");
   IAnimationSource CARD_TAKE_LEFT = AnimationsRegistrySeasonal.register("card_take_left");
   IAnimationSource CARDS_HOLD_LEFT = AnimationsRegistrySeasonal.register("cards_hold_left");
   IAnimationSource CARDS_HOLD_RIGHT = AnimationsRegistrySeasonal.register("cards_hold_right");
   IAnimationSource GIVE_CARDS_CYCLE = AnimationsRegistrySeasonal.register("give_cards_cycle");
   IAnimationSource LYING_CROSS_LEGS = AnimationsRegistrySeasonal.register("lying_cross_legs");
   IAnimationSource HUG_YOURSELF_NOW_NEW = AnimationsRegistrySeasonal.register("hug_yourself_now_new");
   IAnimationSource DRAG_POSE_NO_HEAD = AnimationsRegistrySeasonal.register("drag_pose_no_head");
   IAnimationSource HANDS_BEHIND_HEAD = AnimationsRegistrySeasonal.register("hands_pose_behind_head");
   @SpecialAnim
   IAnimationSource S35_DYLAN_CUTSCENE = AnimationsRegistrySeasonal.register("dylan_typing_cutscene");
   IAnimationSource SNORE_LAY_ENDANIMATED = AnimationsRegistrySeasonal.register("snore_lay_endanimated");
   IAnimationSource LEFT_BACK_DOOR_OPEN = AnimationsRegistrySeasonal.register("left_back_door_open");
   IAnimationSource LEFT_FRONT_DOOR_OPEN = AnimationsRegistrySeasonal.register("left_front_door_open");
   IAnimationSource RIGHT_BACK_DOOR_OPEN = AnimationsRegistrySeasonal.register("right_back_door_open");
   IAnimationSource RIGHT_FRONT_DOOR_OPEN = AnimationsRegistrySeasonal.register("right_front_door_open");
   IAnimationSource FALL_CYCLE = AnimationsRegistrySeasonal.register("fall_cycle");
   IAnimationSource FALL_MOVE_CYCLE = AnimationsRegistrySeasonal.register("fall_move_cycle");
   IAnimationSource HOLDED_STEVE = AnimationsRegistrySeasonal.register("holded_steve");
   IAnimationSource HOLDING_BACK = AnimationsRegistrySeasonal.register("holding_back");
   IAnimationSource HOLDING_FRONT = AnimationsRegistrySeasonal.register("holding_front");
   IAnimationSource CLOTHES_TAKE_OFF = AnimationsRegistrySeasonal.register("clothes_take_off");
   IAnimationSource HANDBAG_HOLD_LEFT = AnimationsRegistrySeasonal.register("handbag_hold_left");
   IAnimationSource PHOTO_1_POSE = AnimationsRegistrySeasonal.register("photo_1_pose");
   IAnimationSource PHOTO_2_POSE = AnimationsRegistrySeasonal.register("photo_2_pose");
   IAnimationSource PHOTO_3_POSE = AnimationsRegistrySeasonal.register("photo_3_pose");
   IAnimationSource TASTE_ICE_CREAM = AnimationsRegistrySeasonal.register("taste_ice_cream");
   IAnimationSource EAT_ICE_CREAM = AnimationsRegistrySeasonal.register("eat_ice_cream");
   IAnimationSource SLAPPING_CAR = AnimationsRegistrySeasonal.register("slapping_car");
   IAnimationSource HAIRFLUFF_ENDANIMATED = AnimationsRegistrySeasonal.register("hairfluff_endanimated");
   IAnimationSource HAIRFLUFF_KID_ENDANIMATED = AnimationsRegistrySeasonal.register("hairfluff_kid_endanimated");
   IAnimationSource CHILD_PICKUP = AnimationsRegistrySeasonal.register("child_pickup");
   IAnimationSource CHILD_PICKUP_CHILD = AnimationsRegistrySeasonal.register("child_pickup_child");
   IAnimationSource SQUEEZE = AnimationsRegistrySeasonal.register("squeeze");
   IAnimationSource JDH_CHECK_OUT = AnimationsRegistrySeasonal.register("jdh_check_out");
   IAnimationSource SHOW_POT = AnimationsRegistrySeasonal.register("show_pot");
   IAnimationSource BENCH_LIFT_CYCLE = AnimationsRegistrySeasonal.register("bench_lift_cycle");
   IAnimationSource BENCH_STEVE_IDLE = AnimationsRegistrySeasonal.register("bench_steve_idle");
   IAnimationSource BENCH_STEVE_SIT = AnimationsRegistrySeasonal.register("bench_steve_sit");
   IAnimationSource BENCH_STEVE_THROWED = AnimationsRegistrySeasonal.register("bench_steve_throwed");
   IAnimationSource BENCH_THROW = AnimationsRegistrySeasonal.register("bench_throw");
   IAnimationSource LYING_WAWING_CYCLE = AnimationsRegistrySeasonal.register("lying_waving_cycle");
   IAnimationSource LYING_NO_LOOKING_PHONE = AnimationsRegistrySeasonal.register("lying_no_looking_phone");
   IAnimationSource PISTOL_PREPARE = AnimationsRegistrySeasonal.register("pistol_prepare");
   IAnimationSource PISTOL_SHOT = AnimationsRegistrySeasonal.register("pistol_shot");
   IAnimationSource OPENED_HEAD = AnimationsRegistrySeasonal.register("opened_head");
   IAnimationSource ROBOT_ENDANIMATED = AnimationsRegistrySeasonal.register("robot_endanimated");
   IAnimationSource BROKEN_ROBOT_POSE = AnimationsRegistrySeasonal.register("broken_robot_pose");
   @SpecialAnim
   IAnimationSource CAR_CRASH = AnimationsRegistrySeasonal.register("car_crush_cutscene");
   IAnimationSource DEAD_LAY_1 = AnimationsRegistrySeasonal.register("dead_lay1");
   IAnimationSource DEAD_LAY_2 = AnimationsRegistrySeasonal.register("dead_lay2");
   IAnimationSource WIRE_NECK_CONNECT = AnimationsRegistrySeasonal.register("wire_neck_connect");
   IAnimationSource WIRE_NECK_CONNECT_ONEHAND = AnimationsRegistrySeasonal.register("wire_neck_one_hand");
   IAnimationSource BALLOON_MAKING = AnimationsRegistrySeasonal.register("balloon_making");
   IAnimationSource BALLOON_POP = AnimationsRegistrySeasonal.register("balloon_pop");
   IAnimationSource JUMP_BACK = AnimationsRegistrySeasonal.register("jump_back");
   IAnimationSource SNAKE_WALK = AnimationsRegistrySeasonal.register("snake_walk");
   IAnimationSource BODY_ENDANIMATED_SPINPUNCH = AnimationsRegistrySeasonal.register("body_endanimated_spinpunch");
   IAnimationSource CHILL_SIT_NO_ARMS = AnimationsRegistrySeasonal.register("chill_sit_no_arms");
   IAnimationSource RAGDOLL_FALL = AnimationsRegistrySeasonal.register("ragdoll_fall");
   IAnimationSource STOMACH_GETUP = AnimationsRegistrySeasonal.register("stomach_getup");
   IAnimationSource CHUCKLE_CYCLE = AnimationsRegistrySeasonal.register("chuckle_cycle");
   IAnimationSource FIRECRACKER_SHOT = AnimationsRegistrySeasonal.register("firecracker_shot");
   IAnimationSource HOLDED_POSE = AnimationsRegistrySeasonal.register("holded_pose");
   IAnimationSource PAPERS_TO_CHEST = AnimationsRegistrySeasonal.register("papers_to_chest");
   IAnimationSource HEAVY_BREATHING = AnimationsRegistrySeasonal.register("heavy_breathing");
   IAnimationSource HURRY_POCKET_CHECK = AnimationsRegistrySeasonal.register("hurry_pocket_check");
   IAnimationSource INHALER_ENDANIMATED = AnimationsRegistrySeasonal.register("inhaler_endanimated");
   IAnimationSource HOLD_BOOK_INFRONT = AnimationsRegistrySeasonal.register("hold_book_infront");
   IAnimationSource PLAYING_GUITAR = AnimationsRegistrySeasonal.register("playing_guitar");
   IAnimationSource FLUTE_PLAY_CYCLE = AnimationsRegistrySeasonal.register("flute_play_cycle");
   IAnimationSource PLAYING_DRUM = AnimationsRegistrySeasonal.register("playing_drum");
   IAnimationSource SPINS_RACKET_CYCLE = AnimationsRegistrySeasonal.register("spins_racket_cycle");
   IAnimationSource JODAH_CLOSED_WINGS_NEW = AnimationsRegistrySeasonal.register("jodah_closed_wings");
   IAnimationSource JODAH_OPEN_WINGS_NEW = AnimationsRegistrySeasonal.register("jodah_wings_open");
   IAnimationSource JDH_BIG_WINK = AnimationsRegistrySeasonal.register("jdh_big_wink");
   IAnimationSource WRITE_CLIPBOARD = AnimationsRegistrySeasonal.register("write_clipboard");
   IAnimationSource OPEN_AMBULANCE_DOORS = AnimationsRegistrySeasonal.register("ambulance_doors");
   IAnimationSource PANDUS_CART_ANGLE = AnimationsRegistrySeasonal.register("pandus_angle");
   IAnimationSource PULL_SOMEONE_WIRE = AnimationsRegistrySeasonal.register("pull_someone_wire");
   IAnimationSource GUITAR_HOLD = AnimationsRegistrySeasonal.register("guitar_hold");
   IAnimationSource POTION_SELLER_FACE = AnimationsRegistrySeasonal.register("potion_seller_face");
   IAnimationSource POTION_SELLER_HAPPY = AnimationsRegistrySeasonal.register("potion_seller_happy");
   IAnimationSource POTION_SELLER_ANGRY = AnimationsRegistrySeasonal.register("potion_seller_angry");
   IAnimationSource LOOK_AT_ITEM_IN_HAND_RIGHT = AnimationsRegistrySeasonal.register("look_at_item_in_hand_right");
   IAnimationSource HOLD_ON_SHOULDER_OLD = AnimationsRegistrySeasonal.register("hold_on_shoulder");
   IAnimationSource ARCHRAGE = AnimationsRegistrySeasonal.register("archrage");
   IAnimationSource BIG_HEAD = AnimationsRegistrySeasonal.register("big_head");
   IAnimationSource SHY_HANDS = AnimationsRegistrySeasonal.register("shy_hands");
   IAnimationSource HANDS_POSE_STRONGHUG1_RIGHT = AnimationsRegistrySeasonal.register("hands_pose_stronghug1_right");
   IAnimationSource HANDS_POSE_STRONGHUG2_RIGHT = AnimationsRegistrySeasonal.register("hands_pose_stronghug2_right");
   IAnimationSource BROKEN_LAY = AnimationsRegistrySeasonal.register("broken_lay");
   IAnimationSource BROKEN_LAY_MOVING = AnimationsRegistrySeasonal.register("broken_lay_moving");
   IAnimationSource BOOK_TO_CHEST = AnimationsRegistrySeasonal.register("book_to_chest");
   IAnimationSource BOOK_THROW = AnimationsRegistrySeasonal.register("book_throw");
   IAnimationSource HEAD_GLITCH = AnimationsRegistrySeasonal.register("head_glitch");
   IAnimationSource SHOCKED_SQUAT = AnimationsRegistrySeasonal.register("shocked_squat");
   IAnimationSource GROPE_ENDANIMATED = AnimationsRegistrySeasonal.register("grope_endanimated");
   IAnimationSource SUBARU_DANCE = AnimationsRegistrySeasonal.register("subaru_dance");
   IAnimationSource HANDS_POSE_HANDATMOUTH_LEFT = AnimationsRegistrySeasonal.register("hands_pose_handatmouth_left");
   IAnimationSource HOLD_SHIP_WHEEL = AnimationsRegistrySeasonal.register("hold_ship_wheel");
   IAnimationSource BANGING_HEAD = AnimationsRegistrySeasonal.register("head_banging");
   IAnimationSource BROKEN_WALK1 = AnimationsRegistrySeasonal.register("broken_walk1");
   IAnimationSource BROKEN_WALK2 = AnimationsRegistrySeasonal.register("broken_walk2");
   IAnimationSource BROKEN_WALK3 = AnimationsRegistrySeasonal.register("broken_walk3");
   IAnimationSource BROKEN_WALK4 = AnimationsRegistrySeasonal.register("broken_walk4");
   IAnimationSource BROKEN_WALK6 = AnimationsRegistrySeasonal.register("broken_walk6");
   IAnimationSource BROKEN_RUN = AnimationsRegistrySeasonal.register("broken_run");
   IAnimationSource DEAD_EDGE_HANG = AnimationsRegistrySeasonal.register("dead_hang_on_edge");
   IAnimationSource FALL_ON_BACK = AnimationsRegistrySeasonal.register("fall_on_back");
   IAnimationSource STAY_TO_SIT_GROUND = AnimationsRegistrySeasonal.register("sit_on_ground");
   IAnimationSource SIT_GROUND_TO_LAY = AnimationsRegistrySeasonal.register("sitground_to_lay");
   IAnimationSource LAUGH_ON_BACK = AnimationsRegistrySeasonal.register("laugh_on_back");
   @SpecialAnim
   IAnimationSource CAR_CRASHING = AnimationsRegistrySeasonal.register("car_crashing");
   @SpecialAnim
   IAnimationSource CAR_BREAK = AnimationsRegistrySeasonal.register("car_stopping");
   IAnimationSource WAVING_CYCLE = AnimationsRegistrySeasonal.register("waving_cycle");
   IAnimationSource SPREAD_LEGS_SIT = AnimationsRegistrySeasonal.register("spread_legs_sit");
   IAnimationSource SPREAD_LEGS_ARMS_LAYER = AnimationsRegistrySeasonal.register("spread_legs_arms_layer");
   IAnimationSource HANDS_DOWN_ENDANIMATED = AnimationsRegistrySeasonal.register("hands_down_endanimated");
   IAnimationSource FOOT_FORWARD = AnimationsRegistrySeasonal.register("foot_forward");
   IAnimationSource START_FLYING = AnimationsRegistrySeasonal.register("start_flying");
   IAnimationSource FLYING_FORWARD = AnimationsRegistrySeasonal.register("flying_forward");
   IAnimationSource SLOW_PSYCHO = AnimationsRegistrySeasonal.register("slow_psycho");
   IAnimationSource SIT_LEG_ON_KNEE_WITH_HANDS = AnimationsRegistrySeasonal.register("leg_on_knee_endanimated");
   IAnimationSource SIT_LEG_ON_KNEE = AnimationsRegistrySeasonal.register("leg_on_knee_only");
   IAnimationSource HANDS_ON_KNEE = AnimationsRegistrySeasonal.register("arms_on_knee_only");
   IAnimationSource STAND_LEAN_TABLE = AnimationsRegistrySeasonal.register("stand_lean");
   IAnimationSource WINE_TASTE = AnimationsRegistrySeasonal.register("vineglass_taste");
   IAnimationSource EMOTION_ANNOYED = AnimationsRegistrySeasonal.register("emotion_annoyed");
   IAnimationSource THROW_CARDS = AnimationsRegistrySeasonal.register("cards_throw");
   IAnimationSource HANDSHAKE_RIGHT = AnimationsRegistrySeasonal.register("handshake_right");
   IAnimationSource HANDSHAKE_LEFT = AnimationsRegistrySeasonal.register("handshake_left");
   IAnimationSource STANDONHAND_LEFT = AnimationsRegistrySeasonal.register("standonhand_left");
   IAnimationSource EMOTION_SELFPRIDE = AnimationsRegistrySeasonal.register("emotion_selfpride");
   IAnimationSource FEELING_COLD = AnimationsRegistrySeasonal.register("feeling_cold");
   IAnimationSource FEELING_COLD_REPEAT = AnimationsRegistrySeasonal.register("feeling_cold_repeat");
   IAnimationSource DRINK_CYCLE = AnimationsRegistrySeasonal.register("drinking_cycle");
   IAnimationSource DRINK_LAYER = AnimationsRegistrySeasonal.register("drinking_layer");
   IAnimationSource HANDS_ON_LEGS_POSE = AnimationsRegistrySeasonal.register("hands_on_legs_pose");
   IAnimationSource GRAFFITI_CYCLE = AnimationsRegistrySeasonal.register("graffiti_cycle");
   IAnimationSource COIN_THROW = AnimationsRegistrySeasonal.register("coin_throw");
   IAnimationSource COIN_THROW_CYCLE = AnimationsRegistrySeasonal.register("coin_throw_cycle");
   IAnimationSource COIN_THROW_GAME = AnimationsRegistrySeasonal.register("coin_throw_game");
   SidedAnimationSet COIN_REVEAL = AnimationsRegistrySeasonal.registerSided("coin_reveal_reshka", "coin_reveal_orel", "coin_reveal");
   IAnimationSource HOLD_GLASSES = AnimationsRegistrySeasonal.register("hold_glasses");
   IAnimationSource LAY_ON_BACK_TO_SIT = AnimationsRegistrySeasonal.register("lay_to_knee_sit");
   IAnimationSource REVOLVER_POSE_1 = AnimationsRegistrySeasonal.register("revolver_pose1");
   IAnimationSource REVOLVER_POSE_2 = AnimationsRegistrySeasonal.register("revolver_pose2");
   IAnimationSource REVOLVER_POSE_3 = AnimationsRegistrySeasonal.register("revolver_pose3");
   IAnimationSource PISTOL_SHOT_LAYER = AnimationsRegistrySeasonal.register("pistol_shot_layer");
   IAnimationSource SWINGING_PUNCH = AnimationsRegistrySeasonal.register("swinging_punch");
   IAnimationSource RELOAD_REVOLVER = AnimationsRegistrySeasonal.register("reload_revolver");
   IAnimationSource LAY_ON_STOMAACH = AnimationsRegistrySeasonal.register("lay_on_stomach");
   IAnimationSource HANDS_BEHIND_HEAD_LAYER = AnimationsRegistrySeasonal.register("hands_behind_head_layer");
   @SpecialAnim
   IAnimationSource TURNSTILE_BACKWARD = AnimationsRegistrySeasonal.register("turnstile_backward");
   @SpecialAnim
   IAnimationSource TURNSTILE_FORWARD = AnimationsRegistrySeasonal.register("turnstile_forward");
   IAnimationSource HUGGING_DOG = AnimationsRegistrySeasonal.register("hugging_dog");
   @SpecialAnim
   IAnimationSource METRO_DOOR_OPEN = AnimationsRegistrySeasonal.register("metro_door_open");
   IAnimationSource HEAD_TO_DOG = AnimationsRegistrySeasonal.register("head_to_dog");
   IAnimationSource HEAD_TO_DOG_SIDE = AnimationsRegistrySeasonal.register("head_to_dog_side");
   IAnimationSource HUSH = AnimationsRegistrySeasonal.register("shh_finger");
   IAnimationSource CLEO_COSPLAY_POSE_1 = AnimationsRegistrySeasonal.register("cosplay_photo_pose_1");
   IAnimationSource CLEO_COSPLAY_POSE_2 = AnimationsRegistrySeasonal.register("cosplay_photo_pose_2");
   IAnimationSource CLEO_COSPLAY_POSE_3 = AnimationsRegistrySeasonal.register("cosplay_photo_pose_3");
   IAnimationSource CLEO_COSPLAY_POSE_4 = AnimationsRegistrySeasonal.register("cosplay_photo_pose_4");
   IAnimationSource LEANING_ON_TABLE_POSE = AnimationsRegistrySeasonal.register("leaning_on_table_pose");
   IAnimationSource FAR_LOOKING = AnimationsRegistrySeasonal.register("far_looking");
   IAnimationSource SEARCH_FOR_INTERNET = AnimationsRegistrySeasonal.register("search_for_internet");
   IAnimationSource PRESENT = AnimationsRegistrySeasonal.register("present_hands");
   IAnimationSource RUBBING_CAR = AnimationsRegistrySeasonal.register("rubbing_car");
   IAnimationSource AMBIENT_CAR_WHEELS = AnimationsRegistrySeasonal.register("ambient_car_wheels");
   IAnimationSource AMBIENT_CAR_IDLE = AnimationsRegistrySeasonal.register("ambient_car_idle");
   IAnimationSource POKING_PHONE = AnimationsRegistrySeasonal.register("poking_phone");
   IAnimationSource ARMS_IN_HAIR = AnimationsRegistrySeasonal.register("arms_in_hair_both");
   @SpecialAnim
   IAnimationSource ANGAR_DOOR_OPEN = AnimationsRegistrySeasonal.register("angar_door_open");
   @SpecialAnim
   IAnimationSource ANGAR_DOOR_OPEN_LEFT = AnimationsRegistrySeasonal.register("angar_door_open_left");
   IAnimationSource PET_STROKING = AnimationsRegistrySeasonal.register("pet_stroking");
   IAnimationSource CHAIR_CIRCLE_CYCLE = AnimationsRegistrySeasonal.register("chair_circle_cycle");
   IAnimationSource ENTITY_SPIN_CHAIR = AnimationsRegistrySeasonal.register("entity_spin_chair");
   IAnimationSource CARD_TOWER_FALL = AnimationsRegistrySeasonal.register("card_tower_fall");
   @SpecialAnim
   IAnimationSource NEWSPAPER_CLOSED = AnimationsRegistrySeasonal.register("newspaper_closed");
   IAnimationSource HOLD_PHONE_VERTICAL = AnimationsRegistrySeasonal.register("hold_phone_vertical");
   IAnimationSource PUSH_SOMEONE = AnimationsRegistrySeasonal.register("push_someone");
   @SpecialAnim
   IAnimationSource MASK_LAUGH = AnimationsRegistrySeasonal.register("mask_laugh");
   IAnimationSource EMOTION_CRUSH1 = AnimationsRegistrySeasonal.register("emotion_crush1");
   IAnimationSource EMOTION_CRUSH2 = AnimationsRegistrySeasonal.register("emotion_crush2");
   IAnimationSource EMOTION_SLEEPY_NEW = AnimationsRegistrySeasonal.register("sleepy_emote");
   IAnimationSource HOLD_ON = AnimationsRegistrySeasonal.register("hold_on");
   IAnimationSource HOLD_HAT = AnimationsRegistrySeasonal.register("hold_hat_pose");
   IAnimationSource HOLD_HAT_UP = AnimationsRegistrySeasonal.register("hold_hat_head");
   IAnimationSource EMOTION_PRIDE_NEW = AnimationsRegistrySeasonal.register("emotion_pride");
   @SpecialAnim
   IAnimationSource WALL_IDLE = AnimationsRegistrySeasonal.register("wall_idle");
   @SpecialAnim
   IAnimationSource TREE_ON_SHOULDER = AnimationsRegistrySeasonal.register("tree_on_shoulder");
   IAnimationSource HANDCUFF_BACK = AnimationsRegistrySeasonal.register("handcuff_back");
   IAnimationSource VERY_SMALL_LAUGH = AnimationsRegistrySeasonal.register("very_small_laugh");
   IAnimationSource EMOTION_PSYCHO_EYES = AnimationsRegistrySeasonal.register("emotion_psycho_eyes");
   IAnimationSource BREAK_GLASS_POSE = AnimationsRegistrySeasonal.register("break_glass_pose");
   IAnimationSource ROBOT_EXPLODE = AnimationsRegistrySeasonal.register("robot_explode");
   @SpecialAnim
   IAnimationSource POLICE_LIGHTS = AnimationsRegistrySeasonal.register("police_lights");
   IAnimationSource CASUAL_SQUAT_HANDS = AnimationsRegistrySeasonal.register("casual_squat_layers", "casual_squat_arms");
   IAnimationSource CASUAL_SQUAT_POSE = AnimationsRegistrySeasonal.register("casual_squat_layers", "casual_squat_noarms");
   IAnimationSource VINEGLASS_THROW_BACK = AnimationsRegistrySeasonal.register("vineglass_throw_back");
   IAnimationSource PUPPET_HANG = AnimationsRegistrySeasonal.register("doll_transform");
   IAnimationSource CITY_BIG_RUN = AnimationsRegistrySeasonal.register("humanoid/city_big_run");
   IAnimationSource HOLDED_STEVE_BACK = AnimationsRegistrySeasonal.register("holded_steve_back");
   IAnimationSource PISTOL_ON_HEAD = AnimationsRegistrySeasonal.register("pistol_on_head");
   IAnimationSource LOW_LEGKICK = AnimationsRegistrySeasonal.register("low_legkick");
   IAnimationSource TURNOFF_CLOCK = AnimationsRegistrySeasonal.register("lisba", "turnoff_clock");
   IAnimationSource BULLYING_CYCLE = AnimationsRegistrySeasonal.register("bullying_cycle");
   IAnimationSource BULLIED_CYCLE = AnimationsRegistrySeasonal.register("bullied_cycle");
   IAnimationSource CRUNCHING_NECK = AnimationsRegistrySeasonal.register("crunching_neck");
   IAnimationSource SMUG_TO_SCARED = AnimationsRegistrySeasonal.register("smug_to_scared");
   IAnimationSource CLOTHES_HOLD = AnimationsRegistrySeasonal.register("clothes_hold");
   IAnimationSource TOOK_OFF_MASK = AnimationsRegistrySeasonal.register("took_off_mask");
   IAnimationSource EMOTION_READING = AnimationsRegistrySeasonal.register("fast_reading_layer");
   IAnimationSource HICCUP_ENDANIMATED = AnimationsRegistrySeasonal.register("hiccup_endanimated");
   IAnimationSource PISTOL_UP = AnimationsRegistrySeasonal.register("pistol_up");
   IAnimationSource HUMANOID_ROBOT_GLITCH = AnimationsRegistrySeasonal.register("humanoid/robot_glitch");
   SidedAnimationSet ITEM_APPEAR = AnimationsRegistrySeasonal.registerSided("item_appear_right", "item_appear_left", "humanoid/item_appear");
   SidedAnimationSet BOTTLE_SHOWING_POSE = AnimationsRegistrySeasonal.registerSided(
      "bottle_showing_right", "bottle_showing_left", "humanoid/bottle_showing_pose"
   );
   SidedAnimationSet DOOR_KNOCKING = AnimationsRegistrySeasonal.registerSided("door_knocking_right", "door_knocking_left", "humanoid/door_knocking");
   SidedAnimationSet DODGE_SIDE = AnimationsRegistrySeasonal.registerSided("dodge_side_right", "dodge_side_left", "dodge_side");
   SidedAnimationSet BAG_GIVING = AnimationsRegistrySeasonal.registerSided("bag_giving_right", "bag_giving_left", "humanoid/bag_giving");
   SidedAnimationSet HOLD_SIGN = AnimationsRegistrySeasonal.registerSided("hold_sign_right", "hold_sign_left", "hold_sign");
   SidedAnimationSet WAVE_SIGN = AnimationsRegistrySeasonal.registerSided("wave_sign_right", "wave_sign_left", "wave_sign");
   SidedAnimationSet PROTEST_HAND = AnimationsRegistrySeasonal.registerSided("protest_hand_right", "protest_hand_left", "protest_hand");
   SidedAnimationSet HOLD_SPEAKER = AnimationsRegistrySeasonal.registerSided("hold_speaker_right", "hold_speaker_left", "hold_speaker");
   SidedAnimationSet WAITER_TOWEL_HOLD = AnimationsRegistrySeasonal.registerSided(
      "waiter_towel_hold_right", "waiter_towel_hold_left", "humanoid/waiter_towel_hold"
   );
   SidedAnimationSet HILL_ENDANIMATED_RIGHT = AnimationsRegistrySeasonal.registerSided("hill_endanimated_right", "hill_endanimated_left", "hill_endanimated");
   SidedAnimationSet TAKE_OFF_GLASSES = AnimationsRegistrySeasonal.registerSidedSeparate("take_off_glasses_right", "take_off_glasses_left");
   SidedAnimationSet OFFER_SIT = AnimationsRegistrySeasonal.registerSidedSeparate("offer_sit_right", "offer_sit_left");
   SidedAnimationSet WALK_HOLDING = AnimationsRegistrySeasonal.registerSidedSeparate("walk_holding_right", "walk_holding_left");
   SidedAnimationSet PONDERING = AnimationsRegistrySeasonal.registerSidedSeparate("pondering_right", "pondering_left");
   SidedAnimationSet PONDERING_TALL = AnimationsRegistrySeasonal.registerSidedSeparate("pondering_right_tall", "pondering_left_tall");
   SidedAnimationSet HOLD_HAND = AnimationsRegistrySeasonal.registerSided("hold_hand_right", "hold_hand_left", "hold_hand");
   SidedAnimationSet SIDE_SURPRISED = AnimationsRegistrySeasonal.registerSidedSeparate("side_surprised_right", "side_surprised_left");
   SidedAnimationSet HOLD_INFRONT = AnimationsRegistrySeasonal.registerSided("hold_infront_right", "hold_infront_left", "hold_infront");
   SidedAnimationSet SIDELAY_TO_SIT = AnimationsRegistrySeasonal.registerSided("sidelay_to_sit_right", "sidelay_to_sit_left", "sidelay_to_sit");
   SidedAnimationSet EATING_HOLDING_PHONE = AnimationsRegistrySeasonal.registerSided("hold_phone_eating_right", "hold_phone_eating_left", "hold_phone_eating");
   SidedAnimationSet HOLDING_SOMEONE = AnimationsRegistrySeasonal.registerSided("holding_someone_right", "holding_someone_left", "holding_someone");
   SidedAnimationSet ARM_SIDE = AnimationsRegistrySeasonal.registerSided("arm_side_right", "arm_side_left", "arm_side");
   SidedAnimationSet THROW_ITEM = AnimationsRegistrySeasonal.registerSided("throw_down_right", "throw_down_left", "throw_down");
   SidedAnimationSet FACEPALM = AnimationsRegistrySeasonal.registerSided("facepalm_right", "facepalm_left", "facepalm");
   SidedAnimationSet CITY_RUN_ALEX_WITHOUT_HAND = AnimationsRegistrySeasonal.registerSided(
      "city_run_alex_right_hand", "city_run_alex_left_hand", "city_run_alex_hands"
   );
   SidedAnimationSet CITY_RUN_STEVE_WITHOUT_HAND = AnimationsRegistrySeasonal.registerSided(
      "city_run_steve_left_hand", "city_run_steve_right_hand", "city_run_steve_hands"
   );
   SidedAnimationSet SHOW_HAND = AnimationsRegistrySeasonal.registerSided("showing_hand_right", "showing_hand_left", "showing_hand");
   SidedAnimationSet GRAB_HAND = AnimationsRegistrySeasonal.registerSided("grab_hand_right", "grab_hand_left", "grab_hand");
   SidedAnimationSet GRABBED_HAND = AnimationsRegistrySeasonal.registerSided("grabbed_hand_right", "grabbed_hand_left", "grabbed_hand");
   SidedAnimationSet GRABBED_HAND_PULL_AWAY = AnimationsRegistrySeasonal.registerSided("pull_grabbed_hand_right", "pull_grabbed_hand_left", "pull_grabbed_hand");
   SidedAnimationSet PHONE_EAR = AnimationsRegistrySeasonal.registerSided("phone_ear_right", "phone_ear_left", "phone_ear");
   SidedAnimationSet PICK_AND_THROW = AnimationsRegistrySeasonal.registerSided("pick_and_throw_right", "pick_and_throw_left", "pick_and_throw");
   SidedAnimationSet LAY_CARDS = AnimationsRegistrySeasonal.registerSided("cards_laying_right", "cards_laying_left", "cards_laying");
   SidedAnimationSet THROWING_ITEM_CYCLE = AnimationsRegistrySeasonal.registerSided(
      "throwing_item_cycle_right", "throwing_item_cycle_left", "throwing_item_cycle"
   );
   SidedAnimationSet HIGH_FIVE = AnimationsRegistrySeasonal.registerSided("high_five_active_right", "high_five_active_left", "high_five_active");
   SidedAnimationSet HIGH_FIVE_WAIT = AnimationsRegistrySeasonal.registerSided("high_five_waiting_right", "high_five_waiting_left", "high_five_waiting");
   SidedAnimationSet TRAGIC_HOLD_HAND = AnimationsRegistrySeasonal.registerSided("tragic_hold_hand_right", "tragic_hold_hand_left", "hands/tragic_hold_hand");
   SidedAnimationSet HOLD_UMBRELLA = AnimationsRegistrySeasonal.registerSided("hold_umbrella_right", "hold_umbrella_left", "humanoid/hold_umbrella");
   SidedAnimationSet PHOTO_POSE = AnimationsRegistrySeasonal.registerSidedSeparate("photo_pose_right", "photo_pose_left");
   SidedAnimationSet PHONE_TO_MOUTH = AnimationsRegistrySeasonal.registerSided("voice_message_pose_right", "voice_message_pose_left", "phone_to_mouth");
   SidedAnimationSet HANDS_LOCK = AnimationsRegistrySeasonal.registerSided("hands_lock", "head_down_eyes_up", "hands_lock");
   SidedAnimationSet WATCH_TYPING = AnimationsRegistrySeasonal.registerSided("watch_tapping_right", "watch_tapping_left", "watch_tapping");
   SidedAnimationSet SELFIE_POSE = AnimationsRegistrySeasonal.registerSided("selfie_pose_right", "selfie_pose_left", "selfie_pose");
   SidedAnimationSet THROW_OFF_TABLE = AnimationsRegistrySeasonal.registerSided("throw_off_table_right", "throw_off_table_left", "throw_off_table");
   @SpecialAnim
   SidedAnimationSet CAR_BACK_WINDOW_OPEN = AnimationsRegistrySeasonal.registerSided("back_window_open_right", "back_window_open_left", "back_window_open");
   SidedAnimationSet HAND_IN_HAIR = AnimationsRegistrySeasonal.registerSided("arm_in_hair_right", "arm_in_hair_left", "arms_in_hair");
   SidedAnimationSet WIPE_TEAR = AnimationsRegistrySeasonal.registerSided("wipe_tear_right", "wipe_tear_left", "wipe_tear");
   SidedAnimationSet RAISE_HAND = AnimationsRegistrySeasonal.registerSided("raise_hand_right", "raise_hand_left", "raise_hand");
   SidedAnimationSet CIGARETTE_ENDANIMATED = AnimationsRegistrySeasonal.registerSided(
      "ciggarete_endanimated_right", "ciggarete_endanimated_left", "ciggarete_endanimated"
   );
   SidedAnimationSet CIGARETTE_POSE = AnimationsRegistrySeasonal.registerSided("ciggarete_pose_right", "ciggarete_pose_left", "ciggarete_pose");
   SidedAnimationSet LOOK_WATCH = AnimationsRegistrySeasonal.registerSided("look_watch_right", "look_watch_left", "look_watch");
   SidedAnimationSet SHAKING_HAND = AnimationsRegistrySeasonal.registerSided("shaking_hand_right", "shaking_hand_left", "shaking_hand");
   SidedAnimationSet SHOUTING = AnimationsRegistrySeasonal.registerSided("shouting_endanimated_right", "shouting_endanimated_left", "shouting_endanimated");
   SidedAnimationSet SEARCH_IN_HAT = AnimationsRegistrySeasonal.registerSided(
      "search_in_hat_endanimated_right", "search_in_hat_endanimated_left", "search_in_hat_endanimated"
   );
   SidedAnimationSet NOSE_SCRATCH = AnimationsRegistrySeasonal.registerSided("nose_scratch_right", "nose_scratch_left", "nose_scratch");
   SidedAnimationSet CIGARETTE_SKY = AnimationsRegistrySeasonal.registerSided("ciggarete_sky_right", "ciggarete_sky_left", "ciggarete_sky");
   SidedAnimationSet KNOCKBACK_FACE = AnimationsRegistrySeasonal.registerSided("knockback_face_right", "knockback_face_left", "knockback_face");
   SidedAnimationSet SHAKED_CYCLE = AnimationsRegistrySeasonal.registerSided("shakehand_cycle_right", "shakehand_cycle_left", "shakehand_cycle");
   SidedAnimationSet EYEBROW_RISE = AnimationsRegistrySeasonal.registerSided("eyebrow_rise_right", "eyebrow_rise_left", "eyebrow_rise");
   SidedAnimationSet PLACEHOLDER_3_SIZE = AnimationsRegistrySeasonal.registerSided("placeholder_3size_right", "placeholder_3size_left", "placeholder_3size");
   SidedAnimationSet HOLD_NEAR_WALL = AnimationsRegistrySeasonal.registerSided("hold_near_wall_right", "hold_near_wall_left", "hold_near_wall");
   SidedAnimationSet KNIFE_PULL = AnimationsRegistrySeasonal.registerSided("knife_pull_right", "knife_pull_left", "knife_pull");
   SidedAnimationSet ARM_SIDEWAYS = AnimationsRegistrySeasonal.registerSided("arm_sideways_right", "arm_sideways_left", "arm_sideways");
   SidedAnimationSet LOOK_SEAT_BACK = AnimationsRegistrySeasonal.registerSided("look_seat_back_right", "look_seat_back_left", "look_seat_back");
   SidedAnimationSet PAPER_HOLD = AnimationsRegistrySeasonal.registerSided("paper_hold_right", "paper_hold_left", "paper_hold");
   SidedAnimationSet PAPER_HOLD_PASS = AnimationsRegistrySeasonal.registerSided("paper_hold_pass_right", "paper_hold_pass_left", "paper_hold_pass");
   SidedAnimationSet SHOULDERS_UP = AnimationsRegistrySeasonal.registerSided("shoulders_up_right", "shoulders_up_left", "shoulders_up");
   SidedAnimationSet HILL_ENDANIMATED = AnimationsRegistrySeasonal.registerSided("hill_endanimated_right", "hill_endanimated_left", "hill_endanimated");
   SidedAnimationSet PUT_PHONE_ASIDE = AnimationsRegistrySeasonal.registerSidedSeparate("put_phone_aside_right", "put_phone_aside_left");
   SidedAnimationSet LOOK_PHONE_FROM_BACK = AnimationsRegistrySeasonal.registerSidedSeparate("look_phone_from_back_right", "look_phone_from_back_left");
   SidedAnimationSet HAND_TO_BACKHEAD = AnimationsRegistrySeasonal.registerSidedSeparate("hand_to_backhead_right", "hand_to_backhead_left");
   SidedAnimationSet HOLD_PHONE_STUCK = AnimationsRegistrySeasonal.registerSided("hold_phone_stuck_right", "hold_phone_stuck_left", "hold_phone_stuck");
   SidedAnimationSet HANDS_ENDANIMATED_STUFF_SHAKE = AnimationsRegistrySeasonal.registerSided(
      "hands_endanimated_stuffshake_right", "hands_endanimated_stuffshake_left", "hands_endanimated_stuffshake"
   );
   SidedAnimationSet EXPLAINING_CROSSED_ARMS = AnimationsRegistrySeasonal.registerSided(
      "explaining_crossed_arms_right", "explaining_crossed_arms_left", "explaining_crossed_arms"
   );
   SidedAnimationSet SERIOUS_ORDER = AnimationsRegistrySeasonal.registerSided("serious_order_right", "serious_order_left", "serious_order");
   SidedAnimationSet ADJUST_GLASSES_ENDANIMATED = AnimationsRegistrySeasonal.registerSided(
      "adjust_glasses_right", "adjust_glasses_left", "adjust_glasses_endanimated"
   );

   @Since(Since.Season.LR)
   public interface AnimationsIdle {
      IAnimationSource CITY_IDLE_ALEX = AnimationsRegistrySeasonal.register("idle/city_idle_alex");
      IAnimationSource CITY_FASTWALK_ALEX = AnimationsRegistrySeasonal.register("idle/city_fastwalk_alex");
      IAnimationSource CITY_RUN_ALEX = AnimationsRegistrySeasonal.register("idle/city_run_alex");
      IAnimationSource CITY_WALK_ALEX = AnimationsRegistrySeasonal.register("idle/city_walk_alex");
      IAnimationSource CITY_FASTWALK_STEVE = AnimationsRegistrySeasonal.register("idle/city_fastwalk_steve");
      IAnimationSource CITY_IDLE_STEVE = AnimationsRegistrySeasonal.register("idle/city_idle_steve");
      IAnimationSource CITY_RUN_STEVE = AnimationsRegistrySeasonal.register("idle/city_run_steve");
      IAnimationSource CITY_WALK_STEVE = AnimationsRegistrySeasonal.register("idle/city_walk_steve");
      IAnimationSource CITY_FAST_WALK_ALEX_NEW = AnimationsRegistrySeasonal.register("base_anim_last_reality/city_fastwalk_alex");
      IAnimationSource CITY_FAST_WALK_STEVE_NEW = AnimationsRegistrySeasonal.register("base_anim_last_reality/city_fastwalk_steve");
      IAnimationSource CITY_IDLE_ALEX_NEW = AnimationsRegistrySeasonal.register("base_anim_last_reality/city_idle_alex");
      IAnimationSource CITY_IDLE_STEVE_NEW = AnimationsRegistrySeasonal.register("base_anim_last_reality/city_idle_steve");
      IAnimationSource CITY_RUN_ALEX_NEW = AnimationsRegistrySeasonal.register("base_anim_last_reality/city_run_alex");
      IAnimationSource CITY_RUN_STEVE_NEW = AnimationsRegistrySeasonal.register("base_anim_last_reality/city_run_steve");
      IAnimationSource CITY_RUN_STEVE_NEW_NO_HANDS = AnimationsRegistrySeasonal.register("base_anim_last_reality/city_run_steve_nohands");
      IAnimationSource CITY_WALK_ALEX_NEW = AnimationsRegistrySeasonal.register("base_anim_last_reality/city_walk_alex");
      IAnimationSource CITY_WALK_STEVE_NEW = AnimationsRegistrySeasonal.register("base_anim_last_reality/city_walk_steve");
      IAnimationSource CITY_SNEAK_CYCLE = AnimationsRegistrySeasonal.register("base_anim_last_reality/city_sneaking_cycle");
   }
}
