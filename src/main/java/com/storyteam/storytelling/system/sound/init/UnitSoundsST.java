package com.storyteam.storytelling.system.sound.init;

import com.storyteam.storytelling.St;
import net.minecraft.sounds.SoundEvent;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;

@SimplyRegister(
   prefix = "system/sound/unit/"
)
public interface UnitSoundsST {
   @RegistryName("garrison_mercenary/special_sound")
   SoundEvent SPECIAL_SOUND = $("special_sound");
   @RegistryName("crossbowman/choice_crossbowman")
   SoundEvent CHOICE_CROSSBOWMAN = $("choice_crossbowman");
   @RegistryName("crossbowman/movement_crossbowman")
   SoundEvent MOVEMENT_CROSSBOWMAN = $("movement_crossbowman");
   @RegistryName("crossbowman/attack_crossbowman")
   SoundEvent ATTACK_CROSSBOWMAN = $("attack_crossbowman");
   @RegistryName("crossbowman/use_ability_crossbowman")
   SoundEvent USE_ABILITY_CROSSBOWMAN = $("use_ability_crossbowman");
   @RegistryName("crossbowman/work_crossbowman")
   SoundEvent WORK_CROSSBOWMAN = $("work_crossbowman");
   @RegistryName("crossbowman/damage_crossbowman")
   SoundEvent DAMAGE_CROSSBOWMAN = $("damage_crossbowman");
   @RegistryName("crossbowman/damage2_crossbowman")
   SoundEvent DAMAGE2_CROSSBOWMAN = $("damage2_crossbowman");
   @RegistryName("crossbowman/run_crossbowman")
   SoundEvent RUN_CROSSBOWMAN = $("run_crossbowman");
   @RegistryName("berserk/choice_berserk")
   SoundEvent CHOICE_BERSERK = $("choice_berserk");
   @RegistryName("berserk/movement_berserk")
   SoundEvent MOVEMENT_BERSERK = $("movement_berserk");
   @RegistryName("berserk/attack_berserk")
   SoundEvent ATTACK_BERSERK = $("attack_berserk");
   @RegistryName("berserk/use_ability_berserk")
   SoundEvent USE_ABILITY_BERSERK = $("use_ability_berserk");
   @RegistryName("berserk/work_berserk")
   SoundEvent WORK_BERSERK = $("work_berserk");
   @RegistryName("garrison_mercenary/choice_garrison_mercenary")
   SoundEvent CHOICE_GARRISON_MERCENARY = $("choice_garrison_mercenary");
   @RegistryName("garrison_mercenary/movement_garrison_mercenary")
   SoundEvent MOVEMENT_GARRISON_MERCENARY = $("movement_garrison_mercenary");
   @RegistryName("garrison_mercenary/attack_garrison_mercenary")
   SoundEvent ATTACK_GARRISON_MERCENARY = $("attack_garrison_mercenary");
   @RegistryName("garrison_mercenary/use_ability_garrison_mercenary")
   SoundEvent USE_ABILITY_GARRISON_MERCENARY = $("use_ability_garrison_mercenary");
   @RegistryName("garrison_mercenary/work_garrison_mercenary")
   SoundEvent WORK_GARRISON_MERCENARY = $("work_garrison_mercenary");
   @RegistryName("perjurer/choice_perjurer")
   SoundEvent CHOICE_PERJURER = $("choice_perjurer");
   @RegistryName("perjurer/movement_perjurer")
   SoundEvent MOVEMENT_PERJURER = $("movement_perjurer");
   @RegistryName("perjurer/attack_perjurer")
   SoundEvent ATTACK_PERJURER = $("attack_perjurer");
   @RegistryName("perjurer/use_ability_perjurer")
   SoundEvent USE_ABILITY_PERJURER = $("use_ability_perjurer");
   @RegistryName("perjurer/work_perjurer")
   SoundEvent WORK_PERJURER = $("work_perjurer");
   @RegistryName("spearman/choice_spearman")
   SoundEvent CHOICE_SPEARMAN = $("choice_spearman");
   @RegistryName("spearman/movement_spearman")
   SoundEvent MOVEMENT_SPEARMAN = $("movement_spearman");
   @RegistryName("spearman/attack_spearman")
   SoundEvent ATTACK_SPEARMAN = $("attack_spearman");
   @RegistryName("spearman/use_ability_spearman")
   SoundEvent USE_ABILITY_SPEARMAN = $("use_ability_spearman");
   @RegistryName("spearman/work_spearman")
   SoundEvent WORK_SPEARMAN = $("work_spearman");
   @RegistryName("blacksmith/choice_blacksmith")
   SoundEvent CHOICE_BLACKSMITH = $("choice_blacksmith");
   @RegistryName("blacksmith/movement_blacksmith")
   SoundEvent MOVEMENT_BLACKSMITH = $("movement_blacksmith");
   @RegistryName("blacksmith/attack_blacksmith")
   SoundEvent ATTACK_BLACKSMITH = $("attack_blacksmith");
   @RegistryName("blacksmith/use_ability_blacksmith")
   SoundEvent USE_ABILITY_BLACKSMITH = $("use_ability_blacksmith");
   @RegistryName("blacksmith/work_blacksmith")
   SoundEvent WORK_BLACKSMITH = $("work_blacksmith");
   @RegistryName("healer/choice_healer")
   SoundEvent CHOICE_HEALER = $("choice_healer");
   @RegistryName("healer/movement_healer")
   SoundEvent MOVEMENT_HEALER = $("movement_healer");
   @RegistryName("healer/attack_healer")
   SoundEvent ATTACK_HEALER = $("attack_healer");
   @RegistryName("healer/use_ability_healer")
   SoundEvent USE_ABILITY_HEALER = $("use_ability_healer");
   @RegistryName("healer/work_healer")
   SoundEvent WORK_HEALER = $("work_healer");
   @RegistryName("archer/choice_archer")
   SoundEvent CHOICE_ARCHER = $("choice_archer");
   @RegistryName("archer/movement_archer")
   SoundEvent MOVEMENT_ARCHER = $("movement_archer");
   @RegistryName("archer/attack_archer")
   SoundEvent ATTACK_ARCHER = $("attack_archer");
   @RegistryName("archer/use_ability_archer")
   SoundEvent USE_ABILITY_ARCHER = $("use_ability_archer");
   @RegistryName("archer/work_archer")
   SoundEvent WORK_ARCHER = $("work_archer");
   @RegistryName("monk/choice_monk")
   SoundEvent CHOICE_MONK = $("choice_monk");
   @RegistryName("monk/movement_monk")
   SoundEvent MOVEMENT_MONK = $("movement_monk");
   @RegistryName("monk/attack_monk")
   SoundEvent ATTACK_MONK = $("attack_monk");
   @RegistryName("monk/use_ability_monk")
   SoundEvent USE_ABILITY_MONK = $("use_ability_monk");
   @RegistryName("monk/work_monk")
   SoundEvent WORK_MONK = $("work_monk");
   @RegistryName("carpenter/choice_carpenter")
   SoundEvent CHOICE_CARPENTER = $("choice_carpenter");
   @RegistryName("carpenter/movement_carpenter")
   SoundEvent MOVEMENT_CARPENTER = $("movement_carpenter");
   @RegistryName("carpenter/attack_carpenter")
   SoundEvent ATTACK_CARPENTER = $("attack_carpenter");
   @RegistryName("carpenter/use_ability_carpenter")
   SoundEvent USE_ABILITY_CARPENTER = $("use_ability_carpenter");
   @RegistryName("carpenter/work_carpenter")
   SoundEvent WORK_CARPENTER = $("work_carpenter");
   @RegistryName("worker/choice_worker")
   SoundEvent CHOICE_WORKER = $("choice_worker");
   @RegistryName("worker/movement_worker")
   SoundEvent MOVEMENT_WORKER = $("movement_worker");
   @RegistryName("worker/attack_worker")
   SoundEvent ATTACK_WORKER = $("attack_worker");
   @RegistryName("worker/use_ability_worker")
   SoundEvent USE_ABILITY_WORKER = $("use_ability_worker");
   @RegistryName("worker/work_worker")
   SoundEvent WORK_WORKER = $("work_worker");
   @RegistryName("recruit/choice_recruit")
   SoundEvent CHOICE_RECRUIT = $("choice_recruit");
   @RegistryName("recruit/movement_recruit")
   SoundEvent MOVEMENT_RECRUIT = $("movement_recruit");
   @RegistryName("recruit/attack_recruit")
   SoundEvent ATTACK_RECRUIT = $("attack_recruit");
   @RegistryName("recruit/use_ability_recruit")
   SoundEvent USE_ABILITY_RECRUIT = $("use_ability_recruit");
   @RegistryName("recruit/work_recruit")
   SoundEvent WORK_RECRUIT = $("work_recruit");
   @RegistryName("miner/choice_miner")
   SoundEvent CHOICE_MINER = $("choice_miner");
   @RegistryName("miner/movement_miner")
   SoundEvent MOVEMENT_MINER = $("movement_miner");
   @RegistryName("miner/attack_miner")
   SoundEvent ATTACK_MINER = $("attack_miner");
   @RegistryName("miner/use_ability_miner")
   SoundEvent USE_ABILITY_MINER = $("use_ability_miner");
   @RegistryName("miner/work_miner")
   SoundEvent WORK_MINER = $("work_miner");
   @RegistryName("fisherman/choice_fisherman")
   SoundEvent CHOICE_FISHERMAN = $("choice_fisherman");
   @RegistryName("fisherman/movement_fisherman")
   SoundEvent MOVEMENT_FISHERMAN = $("movement_fisherman");
   @RegistryName("fisherman/attack_fisherman")
   SoundEvent ATTACK_FISHERMAN = $("attack_fisherman");
   @RegistryName("fisherman/use_ability_fisherman")
   SoundEvent USE_ABILITY_FISHERMAN = $("use_ability_fisherman");
   @RegistryName("fisherman/work_fisherman")
   SoundEvent WORK_FISHERMAN = $("work_fisherman");
   @RegistryName("samurai/choice_samurai")
   SoundEvent CHOICE_SAMURAI = $("choice_samurai");
   @RegistryName("samurai/movement_samurai")
   SoundEvent MOVEMENT_SAMURAI = $("movement_samurai");
   @RegistryName("samurai/attack_samurai")
   SoundEvent ATTACK_SAMURAI = $("attack_samurai");
   @RegistryName("samurai/use_ability_samurai")
   SoundEvent USE_ABILITY_SAMURAI = $("use_ability_samurai");
   @RegistryName("samurai/work_samurai")
   SoundEvent WORK_SAMURAI = $("work_samurai");
   @RegistryName("builder/choice_builder")
   SoundEvent CHOICE_BUILDER = $("choice_builder");
   @RegistryName("builder/movement_builder")
   SoundEvent MOVEMENT_BUILDER = $("movement_builder");
   @RegistryName("builder/attack_builder")
   SoundEvent ATTACK_BUILDER = $("attack_builder");
   @RegistryName("builder/use_ability_builder")
   SoundEvent USE_ABILITY_BUILDER = $("use_ability_builder");
   @RegistryName("builder/work_builder")
   SoundEvent WORK_BUILDER = $("work_builder");
   @RegistryName("trader/choice_trader")
   SoundEvent CHOICE_TRADER = $("choice_trader");
   @RegistryName("trader/movement_trader")
   SoundEvent MOVEMENT_TRADER = $("movement_trader");
   @RegistryName("trader/attack_trader")
   SoundEvent ATTACK_TRADER = $("attack_trader");
   @RegistryName("trader/use_ability_trader")
   SoundEvent USE_ABILITY_TRADER = $("use_ability_trader");
   @RegistryName("trader/work_trader")
   SoundEvent WORK_TRADER = $("work_trader");
   @RegistryName("scientist/choice_scientist")
   SoundEvent CHOICE_SCIENTIST = $("choice_scientist");
   @RegistryName("scientist/movement_scientist")
   SoundEvent MOVEMENT_SCIENTIST = $("movement_scientist");
   @RegistryName("scientist/attack_scientist")
   SoundEvent ATTACK_SCIENTIST = $("attack_scientist");
   @RegistryName("scientist/use_ability_scientist")
   SoundEvent USE_ABILITY_SCIENTIST = $("use_ability_scientist");
   @RegistryName("scientist/work_scientist")
   SoundEvent WORK_SCIENTIST = $("work_scientist");
   @RegistryName("farmer/choice_farmer")
   SoundEvent CHOICE_FARMER = $("choice_farmer");
   @RegistryName("farmer/movement_farmer")
   SoundEvent MOVEMENT_FARMER = $("movement_farmer");
   @RegistryName("farmer/attack_farmer")
   SoundEvent ATTACK_FARMER = $("attack_farmer");
   @RegistryName("farmer/use_ability_farmer")
   SoundEvent USE_ABILITY_FARMER = $("use_ability_farmer");
   @RegistryName("farmer/work_farmer")
   SoundEvent WORK_FARMER = $("work_farmer");
   @RegistryName("elite_mercenary/choice_elite_mercenary")
   SoundEvent CHOICE_ELITE_MERCENARY = $("choice_elite_mercenary");
   @RegistryName("elite_mercenary/movement_elite_mercenary")
   SoundEvent MOVEMENT_ELITE_MERCENARY = $("movement_elite_mercenary");
   @RegistryName("elite_mercenary/attack_elite_mercenary")
   SoundEvent ATTACK_ELITE_MERCENARY = $("attack_elite_mercenary");
   @RegistryName("elite_mercenary/use_ability_elite_mercenary")
   SoundEvent USE_ABILITY_ELITE_MERCENARY = $("use_ability_elite_mercenary");
   @RegistryName("elite_mercenary/work_elite_mercenary")
   SoundEvent WORK_ELITE_MERCENARY = $("work_elite_mercenary");
   @RegistryName("ether_collector/choice_ether_collector")
   SoundEvent CHOICE_ETHER_COLLECTOR = $("choice_ether_collector");
   @RegistryName("ether_collector/movement_ether_collector")
   SoundEvent MOVEMENT_ETHER_COLLECTOR = $("movement_ether_collector");
   @RegistryName("ether_collector/attack_ether_collector")
   SoundEvent ATTACK_ETHER_COLLECTOR = $("attack_ether_collector");
   @RegistryName("ether_collector/use_ability_ether_collector")
   SoundEvent USE_ABILITY_ETHER_COLLECTOR = $("use_ability_ether_collector");
   @RegistryName("ether_collector/work_ether_collector")
   SoundEvent WORK_ETHER_COLLECTOR = $("work_ether_collector");

   private static SoundEvent $(String name) {
      return SoundEvent.createVariableRangeEvent(St.id(name));
   }
}
