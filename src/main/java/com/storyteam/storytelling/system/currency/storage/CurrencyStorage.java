package com.storyteam.storytelling.system.currency.storage;

import com.storyteam.storytelling.events.base.ScriptIOEvent;
import com.storyteam.storytelling.events.listeners.ScriptIO;
import com.storyteam.storytelling.system.currency.Currency;
import com.storyteam.storytelling.system.currency.CurrencySystem;
import com.storyteam.storytelling.system.persona.Persona;
import com.storyteam.storytelling.system.persona.PersonaHandler;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.utils.FragmentUtils;
import com.storyteam.storytelling.util.ScriptUtils;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import lombok.Generated;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public final class CurrencyStorage {
   private static final String CURRENCY_TAG_NAME = "currency";
   private static final int MIN_CURRENCY_COUNT = 0;
   private static final Map<Persona, Map<Currency, Integer>> currencyData = new HashMap<>();

   public static int getEidos() {
      return getCurrency(CurrencySystem.EIDOS_CURRENCY);
   }

   public static void setEidos(int count) {
      setCurrency(CurrencySystem.EIDOS_CURRENCY, count);
   }

   public static void addEidos(int count) {
      count = FragmentUtils.fragmentModificationAddEidos(count);
      addCurrency(CurrencySystem.EIDOS_CURRENCY, count);
   }

   public static void removeEidos(int count) {
      removeCurrency(CurrencySystem.EIDOS_CURRENCY, count);
   }

   public static int getCurrency(Currency currency) {
      return getCurrencyForPersona(PersonaHandler.getCurrentPersona(), currency);
   }

   public static void setCurrency(Currency currency, int count) {
      setCurrencyForPersona(PersonaHandler.getCurrentPersona(), currency, count);
   }

   public static void addCurrency(Currency currency, int count) {
      Persona persona = PersonaHandler.getCurrentPersona();
      int newCount = Math.max(0, getCurrencyForPersona(persona, currency) + count);
      setCurrencyForPersona(persona, currency, newCount);
   }

   public static void removeCurrency(Currency currency, int count) {
      Persona persona = PersonaHandler.getCurrentPersona();
      int newCount = Math.max(0, getCurrencyForPersona(persona, currency) - count);
      setCurrencyForPersona(persona, currency, newCount);
   }

   public static int getCurrencyForPersona(Persona persona, Currency currency) {
      return currencyData.getOrDefault(persona, Collections.emptyMap()).getOrDefault(currency, 0);
   }

   public static void setCurrencyForPersona(Persona persona, Currency currency, int count) {
      currencyData.computeIfAbsent(persona, p -> new HashMap<>()).put(currency, count);
   }

   public static void load() {
      currencyData.clear();
      CompoundTag root = getCurrencyTag();

      for (Persona persona : Persona.values()) {
         CompoundTag personaTag = ScriptUtils.getTagFromTag(root, getCurrencyPersonaTagName(persona));
         Map<Currency, Integer> map = new HashMap<>();

         for (Currency currency : CurrencyRegistry.getAll()) {
            map.put(currency, personaTag.getInt(currency.getId()));
         }

         currencyData.put(persona, map);
      }
   }

   public static void save() {
      CompoundTag root = getCurrencyTag();

      for (Entry<Persona, Map<Currency, Integer>> entry : currencyData.entrySet()) {
         Persona persona = entry.getKey();
         Map<Currency, Integer> map = entry.getValue();
         CompoundTag personaTag = new CompoundTag();

         for (Entry<Currency, Integer> cEntry : map.entrySet()) {
            personaTag.putInt(cEntry.getKey().getId(), cEntry.getValue());
         }

         root.put(getCurrencyPersonaTagName(persona), personaTag);
      }
   }

   @SubscribeEvent
   public static void scriptIoSave(ScriptIOEvent.Save event) {
      save();
   }

   @SubscribeEvent
   public static void scriptIoLoad(ScriptIOEvent.Load event) {
      load();
   }

   private static String getCurrencyPersonaTagName(Persona persona) {
      return persona.name();
   }

   private static CompoundTag getCurrencyTag() {
      return getOrCreateTag(ScriptIO.WORLD_EXTRA_NBT, "currency");
   }

   private static CompoundTag getOrCreateTag(CompoundTag tag, String tagName) {
      return ScriptUtils.getTagFromTag(tag, tagName);
   }

   @Generated
   private CurrencyStorage() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
