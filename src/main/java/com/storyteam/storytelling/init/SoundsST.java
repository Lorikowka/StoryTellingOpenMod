package com.storyteam.storytelling.init;

import com.storyteam.storytelling.St;
import net.minecraft.sounds.SoundEvent;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.Setup;
import org.zeith.hammerlib.annotations.SimplyRegister;

@SimplyRegister
public class SoundsST {
   @RegistryName("tribute_open")
   public static final SoundEvent TRIBUTE_OPEN = $("tribute_open");
   @RegistryName("knocking")
   public static final SoundEvent KNOCKING = $("knocking");
   @RegistryName("knocking_glass")
   public static final SoundEvent KNOCKING_GLASS = $("knocking_glass");
   @RegistryName("tablet_notification")
   public static final SoundEvent TABLET_NOTIFICATION = $("tablet_notification");
   @RegistryName("computer_menu_open")
   public static final SoundEvent COMPUTER_MENU_OPEN = $("computer_menu_open");
   @RegistryName("ducker_helper_open")
   public static final SoundEvent DUCKER_HELPER_OPEN = $("ducker_helper_open");
   @RegistryName("stats_menu_open")
   public static final SoundEvent STATS_MENU_OPEN = $("stats_menu_open");
   @RegistryName("charger_place")
   public static final SoundEvent CHARGER_PLACE = $("charger_place");
   @RegistryName("charger_take")
   public static final SoundEvent CHARGER_TAKE = $("charger_take");
   @RegistryName("quest_screen_open")
   public static final SoundEvent QUEST_SCREEN_OPEN = $("quest_screen_open");
   @RegistryName("activate_button")
   public static final SoundEvent ACTIVATE_BUTTON = $("activate_button");
   @RegistryName("heal")
   public static final SoundEvent HEAL = $("heal");
   @RegistryName("medkit")
   public static final SoundEvent MEDKIT = $("medkit");
   @RegistryName("health_menu_open")
   public static final SoundEvent HEALTH_MENU_OPEN = $("health_menu_open");
   @RegistryName("battery_use")
   public static final SoundEvent BATTERY_USE = $("battery_use");
   @RegistryName("deth")
   public static final SoundEvent DETH = $("deth");
   @RegistryName("dialog_open")
   public static final SoundEvent DIALOG_OPEN = $("dialog_open");
   @RegistryName("mob_disintegrate")
   public static final SoundEvent MOB_DISINTEGRATE = $("mob_disintegrate");
   @RegistryName("shop_buy")
   public static final SoundEvent SHOP_BUY = $("shop_buy");
   @RegistryName("shop_sell")
   public static final SoundEvent SHOP_SELL = $("shop_sell");
   @RegistryName("shop_open")
   public static final SoundEvent SHOP_OPEN = $("shop_open");
   @RegistryName("notification")
   public static final SoundEvent NOTIFICATION = $("notification");
   @RegistryName("interaction_change")
   public static final SoundEvent INTERACTION_CHANGE = $("interaction_change");
   @RegistryName("interaction_click")
   public static final SoundEvent INTERACTION_CLICK = $("interaction_click");
   @RegistryName("alarm1")
   public static final SoundEvent ALARM1 = $("alarm1");
   @RegistryName("alarm2")
   public static final SoundEvent ALARM2 = $("alarm2");
   @RegistryName("ambient_drone")
   public static final SoundEvent AMBIENT_DRONE = $("ambient_drone");
   @RegistryName("open_eyes")
   public static final SoundEvent OPEN_EYES = $("open_eyes");
   @RegistryName("goose_run")
   public static final SoundEvent GOOSE_RUN = $("goose_run");
   @RegistryName("goose_step")
   public static final SoundEvent GOOSE_STEP = $("goose_step");
   @RegistryName("open_dialog")
   public static final SoundEvent OPEN_DIALOG = $("open_dialog");
   @RegistryName("use_interaction")
   public static final SoundEvent USE_INTERACTION = $("use_interaction");
   @RegistryName("elevator_mechanism")
   public static final SoundEvent ELEVATOR_MECHANISM = $("elevator_mechanism");
   @RegistryName("garage_door")
   public static final SoundEvent GARAGE_DOOR = $("garage_door");
   @RegistryName("door_car")
   public static final SoundEvent DOOR_CAR = $("door_car");
   @RegistryName("engine_ambient_car")
   public static final SoundEvent ENGINE_AMBIENT_CAR = $("engine_ambient_car");
   @RegistryName("engine_riding_car")
   public static final SoundEvent ENGINE_RIDING_CAR = $("engine_riding_car");
   @RegistryName("engine_turn_off_car")
   public static final SoundEvent ENGINE_TURN_OFF_CAR = $("engine_turn_off_car");
   @RegistryName("engine_turn_on_car")
   public static final SoundEvent ENGINE_TURN_ON_CAR = $("engine_turn_on_car");
   @RegistryName("start_moving_car")
   public static final SoundEvent CAR_START_MOVING = $("start_moving_car");
   @RegistryName("stop_moving_car")
   public static final SoundEvent CAR_STOP_MOVING = $("stop_moving_car");
   @RegistryName("trunk")
   public static final SoundEvent TRUNK_OPEN_CAR = $("trunk");
   @RegistryName("fuel_fill")
   public static final SoundEvent FUEL_FILL_CAR = $("fuel_fill");
   @RegistryName("fix_car")
   public static final SoundEvent FIX_CAR = $("fix_car");

   private static SoundEvent $(String name) {
      return SoundEvent.createVariableRangeEvent(St.id(name));
   }

   @Setup
   public static void setup() {
   }
}
