package com.storyteam.storytelling.api.events;

import com.storyteam.storytelling.api.data.EntityInteractionData;
import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.events.StoryFightEvent;
import com.storyteam.storytelling.events.StoryFightTickEvent;
import com.storyteam.storytelling.events.base.BehaviourEvent;
import com.storyteam.storytelling.events.base.BunkerEnterExitEvent;
import com.storyteam.storytelling.events.base.EricTrainingEndEvent;
import com.storyteam.storytelling.events.base.FlowerMinigameEndEvent;
import com.storyteam.storytelling.events.base.FlowerOtherMinigameEndEvent;
import com.storyteam.storytelling.events.base.GooseEatEvent;
import com.storyteam.storytelling.events.base.GoosePinchEvent;
import com.storyteam.storytelling.events.base.MushroomMinigameEndEvent;
import com.storyteam.storytelling.events.base.NextDayEvent;
import com.storyteam.storytelling.events.base.StoneMinigameEndEvent;
import com.storyteam.storytelling.events.base.SvenFedEndEvent;
import com.storyteam.storytelling.events.base.SylvieHelpEndEvent;
import com.storyteam.storytelling.script.ScriptInstance;
import com.storyteam.storytelling.script.data.EntityController;
import com.storyteam.storytelling.script.performer.state.stage.StageEvent;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.system.exp.events.ExpChangeEvent;
import com.storyteam.storytelling.system.lockpicking.event.LockEvent;
import com.storyteam.storytelling.system.shop.events.ShopExchangeEvent;
import com.storyteam.storytelling.system.time.event.NewDayStartEvent;
import com.storyteam.storytelling.util.data.EntityContext;
import com.storyteam.storytelling.util.data.IEntityInfoPack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.event.level.BlockEvent.BreakEvent;

public record EventType<T>(String eventType, Class<T> type) {
   public static final EventType<ScriptInstance> START = new EventType<>("start", ScriptInstance.class);
   public static final EventType<ScriptInstance> STAGE_START = new EventType<>("stage_start", ScriptInstance.class);
   public static final EventType<StageEvent> STAGE_PASS = new EventType<>("stage_pass", StageEvent.class);
   public static final EventType<IStoryServer> STAGE_END = new EventType<>("stage_end", IStoryServer.class);
   public static final EventType<IStoryServer> UPDATE = new EventType<>("update", IStoryServer.class);
   public static final EventType<ServerPlayer> PLAYER_UPDATE = new EventType<>("player_update", ServerPlayer.class);
   public static final EventType<IEntityInfoPack> ENTITY_REPORTED = new EventType<>("entity_reported", IEntityInfoPack.class);
   public static final EventType<EntityController> ENTITY_REMOVED = new EventType<>("entity_removed", EntityController.class);
   public static final EventType<EntityInteractionData> ENTITY_ATTACK = new EventType<>("entity_left_click", EntityInteractionData.class);
   public static final EventType<EntityInteractionData> ENTITY_CLICK = new EventType<>("entity_click", EntityInteractionData.class);
   public static final EventType<EntityInteractionData> ENTITY_CLICK_ONE_HAND = new EventType<>("entity_click_one_hand", EntityInteractionData.class);
   public static final EventType<EntityInteractionData> ENTITY_KILL = new EventType<>("entity_kill", EntityInteractionData.class);
   public static final EventType<EntityInteractionData> ENTITY_HIT = new EventType<>("entity_kill", EntityInteractionData.class);
   public static final EventType<LivingHurtEvent> LIVING_HURT_DIRECT = new EventType<>("living_hurt_direct", LivingHurtEvent.class);
   public static final EventType<LivingKnockBackEvent> LIVING_KNOCK_BACK = new EventType<>("living_damage", LivingKnockBackEvent.class);
   public static final EventType<EntityInteractionData> PLAYER_WAKE_UP = new EventType<>("player_wake_up", EntityInteractionData.class);
   public static final EventType<String> CHAT_MESSAGE_SENT = new EventType<>("chat_message_sent", String.class);
   public static final EventType<Integer> TRIGGER = new EventType<>("trigger", Integer.class);
   public static final EventType<ItemEntity> ITEM_IN_FIRE = new EventType<>("item_in_fire", ItemEntity.class);
   public static final EventType<ServerPlayer> F8_PRESS = new EventType<>("f8_press", ServerPlayer.class);
   public static final EventType<ServerPlayer> F9_PRESS = new EventType<>("f9_press", ServerPlayer.class);
   public static final EventType<IStoryServer> CAMERA_PATH_END = new EventType<>("camera_path_end", IStoryServer.class);
   public static final EventType<ServerPlayer> F10_PRESS = new EventType<>("f10_press", ServerPlayer.class);
   public static final EventType<Integer> KEY_PRESS = new EventType<>("key_press", Integer.class);
   public static final EventType<Integer> MOUSE_PRESS = new EventType<>("mouse_press", Integer.class);
   public static final EventType<ScriptInstance> STOP = new EventType<>("stop", ScriptInstance.class);
   public static final EventType<Block> BLOCK_PLACE = new EventType<>("block_place", Block.class);
   public static final EventType<RightClickBlock> BLOCK_CLICK = new EventType<>("block_click", RightClickBlock.class);
   public static final EventType<BreakEvent> BLOCK_DESTROY = new EventType<>("block_destroy", BreakEvent.class);
   public static final EventType<RightClickItem> ITEM_CLICK = new EventType<>("item_click", RightClickItem.class);
   public static final EventType<ServerPlayer> LEFT_CLICK_AIR = new EventType<>("left_click_air", ServerPlayer.class);
   public static final EventType<Player> SLEEP_FINISH = new EventType<>("server_player", Player.class);
   public static final EventType<BehaviourEvent.BehaviourStopEvent> BEHAVIOUR_END = new EventType<>("behaviour_end", BehaviourEvent.BehaviourStopEvent.class);
   public static final EventType<BehaviourEvent.BehaviourStartEvent> BEHAVIOUR_START = new EventType<>(
      "behaviour_start", BehaviourEvent.BehaviourStartEvent.class
   );
   public static final EventType<StoryFightEvent> ATTACK_FINISH = new EventType<>("story_attack_start", StoryFightEvent.class);
   public static final EventType<StoryFightEvent> ATTACK_START = new EventType<>("story_attack_end", StoryFightEvent.class);
   public static final EventType<EntityContext> ATTACK_NOT_FOUND = new EventType<>("story_attack_not_found", EntityContext.class);
   public static final EventType<NextDayEvent> NEXT_DAY = new EventType<>("next_day", NextDayEvent.class);
   public static final EventType<ShopExchangeEvent> SHOP_EXCHANGE_EVENT = new EventType<>("shop_exchange", ShopExchangeEvent.class);
   public static final EventType<ExpChangeEvent> EXP_CHANGE_EVENT = new EventType<>("exp_change_event", ExpChangeEvent.class);
   public static final EventType<BunkerEnterExitEvent> BUNKER_ENTER_EXIT_EVENT = new EventType<>("bunker_enter_exit_event", BunkerEnterExitEvent.class);
   public static final EventType<LockEvent.Unlock> UNLOCK_LOCK = new EventType<>("lock_unlocked", LockEvent.Unlock.class);
   public static final EventType<Character> CHARACTER_FREE = new EventType<>("character_free", Character.class);
   public static final EventType<GooseEatEvent> GOOSE_EAT_EVENT = new EventType<>("goose_eat_event", GooseEatEvent.class);
   public static final EventType<GoosePinchEvent> GOOSE_PINCH_EVENT = new EventType<>("goose_pinch_event", GoosePinchEvent.class);
   public static final EventType<StoryFightTickEvent> STORY_FIGHT_TICK_EVENT_EVENT_PRE = new EventType<>(
      "story_fight_tick_event_event_pre", StoryFightTickEvent.class
   );
   public static final EventType<StoryFightTickEvent> STORY_FIGHT_TICK_EVENT_EVENT_POST = new EventType<>(
      "story_fight_tick_event_event_post", StoryFightTickEvent.class
   );
   public static final EventType<EricTrainingEndEvent> ERIC_TRAINING_END_EVENT = new EventType<>("eric_training_end_event", EricTrainingEndEvent.class);
   public static final EventType<SvenFedEndEvent> SVEN_FED_END_EVENT = new EventType<>("sven_fed_end_event", SvenFedEndEvent.class);
   public static final EventType<SylvieHelpEndEvent> SYLVIE_HEPL_END_EVENT = new EventType<>("sylvie_help_end_event", SylvieHelpEndEvent.class);
   public static final EventType<MovementInputUpdateEvent> MOVEMENT_INPUT_UPDATE = new EventType<>("movement_event_update", MovementInputUpdateEvent.class);
   public static final EventType<NewDayStartEvent> NEW_DAY_START = new EventType<>("new_day_start", NewDayStartEvent.class);
   public static final EventType<FlowerMinigameEndEvent> FLOWER_MINIGAME_END_EVENT = new EventType<>("flower_minigame_end_event", FlowerMinigameEndEvent.class);
   public static final EventType<StoneMinigameEndEvent> STONE_MINIGAME_END_EVENT = new EventType<>("stone_minigame_end_event", StoneMinigameEndEvent.class);
   public static final EventType<FlowerOtherMinigameEndEvent> FLOWER_OTHER_MINIGAME_END_EVENT = new EventType<>(
      "flower_other_minigame_end_event", FlowerOtherMinigameEndEvent.class
   );
   public static final EventType<MushroomMinigameEndEvent> MUSHROOM_MINIGAME_END_EVENT = new EventType<>(
      "mushroom_minigame_end_event", MushroomMinigameEndEvent.class
   );
}
