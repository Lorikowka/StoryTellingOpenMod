package com.storyteam.storytelling.system.simulator.cognition_tab;

import com.storyteam.storytelling.init.CapsST;
import com.storyteam.storytelling.system.currency.storage.CurrencyStorage;
import com.storyteam.storytelling.system.simulator.cognition_tab.capability.CognitionCapability;
import com.storyteam.storytelling.system.simulator.cognition_tab.network.S2CSyncCognitionCapability;
import com.storyteam.storytelling.system.simulator.cognition_tab.network.S2CSyncCognitionCustomReques;
import com.storyteam.storytelling.system.simulator.cognition_tab.utils.CognitionCustomRequement;
import com.storyteam.storytelling.system.simulator.cognition_tab.utils.CognitionTierInfo;
import com.storyteam.storytelling.system.simulator.cognition_tab.utils.CustomUpgradeHolder;
import com.storyteam.storytelling.system.simulator.me_tab.attributes.PlayerStats;
import com.storyteam.storytelling.system.simulator.me_tab.attributes.data.AttributeType;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.zeith.hammerlib.net.Network;

public class CognitionHelper {
   public static Optional<CognitionCapability> getOptional(Player player) {
      return player.getCapability(CapsST.COG_CAP).resolve();
   }

   public static int getTier(Player player, Cognition cognition) {
      Optional<CognitionCapability> optional = getOptional(player);
      if (optional.isEmpty()) {
         return 0;
      } else {
         CognitionCapability cap = optional.get();
         return cap.getCognitionTier(cognition);
      }
   }

   @Nullable
   public static CognitionTierInfo getTierInfo(Cognition cognition, int tier) {
      return cognition.levels().getOrDefault(tier, null);
   }

   @Nullable
   public static CognitionTierInfo getNextTierInfo(Player player, Cognition cognition) {
      int nextTier = getTier(player, cognition) + 1;
      return cognition.levels().getOrDefault(nextTier, null);
   }

   public static boolean isAttributesEnough(Cognition cognition, int tier) {
      CognitionTierInfo tierUpgrade = getTierInfo(cognition, tier);
      if (tierUpgrade == null) {
         return false;
      } else {
         Map<AttributeType, Integer> types = tierUpgrade.types();

         for (Entry<AttributeType, Integer> entry : types.entrySet()) {
            AttributeType type = entry.getKey();
            int value = entry.getValue();
            boolean isEnough = PlayerStats.getAttribute(type).getValue() >= value;
            if (!isEnough) {
               return false;
            }
         }

         return true;
      }
   }

   public static boolean isAttributesEnoughForNextTier(Player player, Cognition cognition) {
      int nextTier = getTier(player, cognition) + 1;
      return isAttributesEnough(cognition, nextTier);
   }

   public static boolean isCardResearched(Player player, Cognition cognition) {
      return getTier(player, cognition) > 0;
   }

   public static boolean isAllDepsResearch(Player player, Cognition cognition) {
      for (Cognition dep : cognition.allDependencies()) {
         boolean research = isCardResearched(player, dep);
         if (!research) {
            return false;
         }
      }

      return true;
   }

   public static boolean isLevelEnough(Player player, Cognition cognition, int tier) {
      int level = PlayerStats.getLevel();
      CognitionTierInfo tierUpgrade = getTierInfo(cognition, tier);
      if (tierUpgrade == null) {
         return false;
      } else {
         return tierUpgrade.level() < 0 ? false : tierUpgrade.level() <= level;
      }
   }

   public static boolean isLevelEnoughForNextTier(Player player, Cognition cognition) {
      int nextTier = getTier(player, cognition) + 1;
      return isLevelEnough(player, cognition, nextTier);
   }

   public static boolean isEidosEnough(Cognition cognition, int tier) {
      int eidos = CurrencyStorage.getEidos();
      CognitionTierInfo tierUpgrade = getTierInfo(cognition, tier);
      if (tierUpgrade == null) {
         return false;
      } else {
         return tierUpgrade.eidos() < 0 ? false : tierUpgrade.eidos() <= eidos;
      }
   }

   public static boolean isEidosEnoughForNextTier(Player player, Cognition cognition) {
      int nextTier = getTier(player, cognition) + 1;
      return isEidosEnough(cognition, nextTier);
   }

   public static boolean isCustomRequirementsCompleted(Player player, Cognition cognition, int tier) {
      CognitionTierInfo tierUpgrade = cognition.levels().getOrDefault(tier, null);
      if (tierUpgrade == null) {
         return false;
      } else {
         for (CognitionCustomRequement reque : tierUpgrade.customReques()) {
            boolean completed;
            if (player instanceof ServerPlayer serverPlayer) {
               completed = reque.test(serverPlayer);
            } else {
               completed = CustomUpgradeHolder.getCustomUpgradeState(reque.getId());
            }

            if (!completed) {
               return false;
            }
         }

         return true;
      }
   }

   public static boolean isCustomRequirementsCompleted(Player player, Cognition cognition) {
      int nextTier = getTier(player, cognition) + 1;
      return isCustomRequirementsCompleted(player, cognition, nextTier);
   }

   public static boolean canResearch(Player player, Cognition cognition) {
      if (!isLevelEnoughForNextTier(player, cognition)) {
         return false;
      } else if (!isAllDepsResearch(player, cognition)) {
         return false;
      } else if (!isEidosEnoughForNextTier(player, cognition)) {
         return false;
      } else {
         return !isCustomRequirementsCompleted(player, cognition) ? false : isAttributesEnoughForNextTier(player, cognition);
      }
   }

   public static boolean canResearch(Player player, Cognition cognition, int tier) {
      if (!isLevelEnough(player, cognition, tier)) {
         return false;
      } else if (!isAllDepsResearch(player, cognition)) {
         return false;
      } else if (!isEidosEnough(cognition, tier)) {
         return false;
      } else {
         return !isCustomRequirementsCompleted(player, cognition, tier) ? false : isAttributesEnough(cognition, tier);
      }
   }

   public static void upgrade(Player player, Cognition cognition, int tier, boolean force) {
      int currTier = getTier(player, cognition);
      if (currTier < tier) {
         if (currTier + 1 == tier) {
            if (!force) {
               boolean allDepsResearch = isAllDepsResearch(player, cognition);
               if (!allDepsResearch) {
                  return;
               }

               boolean isEidosEnough = isEidosEnough(cognition, tier);
               if (!isEidosEnough) {
                  return;
               }

               boolean isAttributesEnough = isAttributesEnough(cognition, tier);
               if (!isAttributesEnough) {
                  return;
               }

               boolean customRequirementsCompleted = isCustomRequirementsCompleted(player, cognition, tier);
               if (!customRequirementsCompleted) {
                  return;
               }
            }

            Optional<CognitionCapability> optional = getOptional(player);
            if (!optional.isEmpty()) {
               CognitionTierInfo tierUpgrade = getTierInfo(cognition, tier);
               if (tierUpgrade != null) {
                  CurrencyStorage.removeEidos(tierUpgrade.eidos());
                  CognitionCapability cap = optional.get();
                  cap.upgrade(cognition);
                  if (player instanceof ServerPlayer serverPlayer) {
                     for (Consumer<ServerPlayer> action : tierUpgrade.onUpgrade()) {
                        action.accept(serverPlayer);
                     }
                  }

                  syncAllData(player);
               }
            }
         }
      }
   }

   public static void downgrade(Player player, Cognition cognition) {
      Optional<CognitionCapability> optional = getOptional(player);
      if (!optional.isEmpty()) {
         CognitionCapability cap = optional.get();
         cap.downgrade(cognition);
         syncAllData(player);
      }
   }

   public static void remove(Player player, Cognition cognition) {
      Optional<CognitionCapability> optional = getOptional(player);
      if (!optional.isEmpty()) {
         CognitionCapability cap = optional.get();
         cap.remove(cognition);
         syncAllData(player);
      }
   }

   public static void copy(Player newPlayer, Player oldPlayer) {
      Optional<CognitionCapability> newCap = getOptional(newPlayer);
      Optional<CognitionCapability> oldCap = getOptional(oldPlayer);
      if (newCap.isPresent() && oldCap.isPresent()) {
         newCap.get().copyFrom(oldCap.get());
      }

      syncAllData(newPlayer);
   }

   public static void syncAllData(Player player) {
      if (player != null) {
         if (player instanceof ServerPlayer serverPlayer) {
            Optional<CognitionCapability> optional = getOptional(serverPlayer);
            optional.ifPresent(cap -> Network.sendTo(player, new S2CSyncCognitionCapability(cap.serializeNBT())));
         }
      }
   }

   public static void syncCustomReques(Player player) {
      if (player instanceof ServerPlayer serverPlayer) {
         Network.sendTo(player, new S2CSyncCognitionCustomReques(serverPlayer));
      }
   }
}
