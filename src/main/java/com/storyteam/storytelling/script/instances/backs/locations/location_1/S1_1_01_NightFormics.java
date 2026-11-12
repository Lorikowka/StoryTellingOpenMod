package com.storyteam.storytelling.script.instances.backs.locations.location_1;

import com.storyteam.storytelling.api.SaveOnClose;
import com.storyteam.storytelling.api.Script;
import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.content.entities.AntEntity;
import com.storyteam.storytelling.script.BackInstance;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.builder.ScriptPerformerBuilder;
import com.storyteam.storytelling.script.performer.builder.reflective.ScriptPerformerBuilderReflective;
import com.storyteam.storytelling.script.story.ScriptEnvironment;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.script.story.characters.CharacterList;
import com.storyteam.storytelling.script.story.characters.Characters;
import com.storyteam.storytelling.util.ScriptUses;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

@SaveOnClose
@Script(
   value = -1101,
   description = "Ночной эмбиент 1 локации (Формики)"
)
public class S1_1_01_NightFormics extends BackInstance {
   public static final int SCRIPT_ID = -1101;
   private static final Random random = new Random((long)"формики халяль".hashCode());
   private static final List<S1_1_01_NightFormics.FormicAction> formicActions = List.of(
      new S1_1_01_NightFormics.FormicAction(
         "/execute in minecraft:overworld run tp @s -56.02 103.00 -411.92 -145.16 -2.40",
         "/execute in minecraft:overworld run tp @s -52.94 103.00 -416.34 -145.16 -2.40"
      ),
      new S1_1_01_NightFormics.FormicAction(
         "/execute in minecraft:overworld run tp @s -42.82 103.00 -451.27 -89.67 -0.90",
         "/execute in minecraft:overworld run tp @s -33.76 103.00 -451.24 -89.67 -0.90"
      ),
      new S1_1_01_NightFormics.FormicAction(
         "/execute in minecraft:overworld run tp @s -30.07 103.00 -457.95 -156.42 -0.00",
         "/execute in minecraft:overworld run tp @s -27.12 103.00 -464.70 -156.42 -0.00"
      ),
      new S1_1_01_NightFormics.FormicAction(
         "/execute in minecraft:overworld run tp @s -31.83 103.00 -428.48 -381.70 4.12",
         "/execute in minecraft:overworld run tp @s -27.41 103.00 -418.02 -380.95 3.28"
      ),
      new S1_1_01_NightFormics.FormicAction(
         "/execute in minecraft:overworld run tp @s -31.43 103.00 -408.53 -331.59 3.14",
         "/execute in minecraft:overworld run tp @s -45.04 103.00 -391.29 -328.27 2.24"
      ),
      new S1_1_01_NightFormics.FormicAction(
         "/execute in minecraft:overworld run tp @s -46.56 103.00 -399.02 -175.23 2.54",
         "/execute in minecraft:overworld run tp @s -45.07 103.00 -416.02 -175.00 2.31"
      )
   );
   private static final List<Item> formicItems = List.of(Items.SPONGE, Items.GRASS_BLOCK, Items.STONE);
   private static final CharacterList<AntEntity> ANTS = Characters.ANT_FAC.createCharacterList("-1101_ant", formicActions.size());
   private static final List<Character<AntEntity>> activeFormics = new CopyOnWriteArrayList<>();
   private static final Set<Character<AntEntity>> finishedFormics = ConcurrentHashMap.newKeySet();
   public final ScriptPerformer mainFlow = (new ScriptPerformerBuilderReflective(this) {
         @ScriptPerformerBuilderReflective.Secondary("handle_formics")
         private void handleFormics(S1_1_01_NightFormics inst) {
            this.thenRun(S1_1_01_NightFormics.activeFormics::clear);
            this.label("loop");
            this.waitForTimeBetween("00:00", "06:00");
            this.label("spawn_loop");
            this.createTicker(
               "handle_formics_performers",
               player -> {
                  if (S1_1_01_NightFormics.this.ticks % 40L == 0L) {
                     for (Character<AntEntity> finishedAnt : S1_1_01_NightFormics.finishedFormics) {
                        S1_1_01_NightFormics.activeFormics.remove(finishedAnt);
                        int finishedIndex = CharacterList.indexOf(finishedAnt);
                        inst.deleteParallelPerformer("%s_ant".formatted(finishedIndex));
                     }

                     S1_1_01_NightFormics.finishedFormics.clear();
                     float formicPoolModifier = 0.8F;
                     int formicPoolCount = (int)((float)S1_1_01_NightFormics.formicActions.size() * formicPoolModifier);

                     while (S1_1_01_NightFormics.activeFormics.size() < formicPoolCount) {
                        Character<AntEntity> ant = S1_1_01_NightFormics.ANTS
                           .stream()
                           .filter(a -> !S1_1_01_NightFormics.activeFormics.contains(a))
                           .findFirst()
                           .orElse(null);
                        if (ant == null) {
                           break;
                        }

                        int antIndex = CharacterList.indexOf(ant);
                        S1_1_01_NightFormics.FormicAction action = S1_1_01_NightFormics.formicActions.get(antIndex);
                        ItemStack item = new ItemStack(
                           (ItemLike)S1_1_01_NightFormics.formicItems.get(S1_1_01_NightFormics.random.nextInt(S1_1_01_NightFormics.formicItems.size()))
                        );
                        ScriptPerformer poolPerformer = ScriptPerformerBuilder.builder(inst)
                           .createTelepornGlitchEffect(ant, action.spawnLocation, item)
                           .thenUseEntity(ant, e -> e.setCustomName(Component.literal("night_formic")))
                           .thenEntityPath(ant, 0.32F, action.pathLocations)
                           .deleteTelepornGlitchEffect(ant)
                           .thenRun(() -> {
                              S1_1_01_NightFormics.activeFormics.remove(ant);
                              S1_1_01_NightFormics.finishedFormics.add(ant);
                           })
                           .build();
                        S1_1_01_NightFormics.activeFormics.add(ant);
                        inst.addParallelPerformer("%s_ant".formatted(antIndex), poolPerformer);
                     }
                  }
               }
            );
            this.waitForTimeBetween("06:01", "23:59");
            this.deleteTicker("handle_formics_performers");
            this.thenDynamicPerformer(b -> {
               List<ScriptPerformer> performers = new ArrayList<>();
               S1_1_01_NightFormics.ANTS.forEach(entity -> performers.add(ScriptPerformerBuilder.builder(inst).deleteTelepornGlitchEffect(entity).build()));
               b.thenAllPerformers(performers.toArray(ScriptPerformer[]::new));
               S1_1_01_NightFormics.activeFormics.clear();
               S1_1_01_NightFormics.finishedFormics.clear();
            });
            this.gotoLabel("loop");
         }

         @ScriptPerformerBuilderReflective.Secondary("give_formic_item")
         private void giveFormicItem(S1_1_01_NightFormics inst) {
            this.label("loop");
            this.thenAwaitEvent(EventType.ENTITY_CLICK, e -> {
               if (e.target instanceof AntEntity ant) {
                  if (ant.getCustomName() == null) {
                     return false;
                  } else if (!ant.getCustomName().getString().equals("night_formic")) {
                     return false;
                  } else {
                     ItemStack item = ant.getItemInHand(InteractionHand.MAIN_HAND);
                     ScriptUses.giveAndSelectItem(item).accept((ServerPlayer)e.player);
                     ant.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                     return true;
                  }
               } else {
                  return false;
               }
            });
            this.gotoLabel("loop");
         }
      })
      .build(this::registerMainPerformer);

   public S1_1_01_NightFormics(ScriptEnvironment env, int id) {
      super(env, id);
   }

   private static class FormicAction {
      String spawnLocation;
      float antSpeed;
      String[] pathLocations;

      public FormicAction(String spawnLocation, float antSpeed, String... pathLocations) {
         this.spawnLocation = spawnLocation;
         this.antSpeed = antSpeed;
         this.pathLocations = pathLocations;
      }

      public FormicAction(String spawnLocation, String... pathLocations) {
         this.spawnLocation = spawnLocation;
         this.antSpeed = 0.32F;
         this.pathLocations = pathLocations;
      }
   }
}
