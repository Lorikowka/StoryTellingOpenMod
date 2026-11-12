package com.storyteam.storytelling.system.tribute.storage;

import com.storyteam.storytelling.events.base.ScriptIOEvent;
import com.storyteam.storytelling.events.listeners.ScriptIO;
import com.storyteam.storytelling.system.persona.Persona;
import com.storyteam.storytelling.system.persona.PersonaHandler;
import com.storyteam.storytelling.system.time.core.TimeController;
import com.storyteam.storytelling.system.tribute.data.offer.TributeOffer;
import com.storyteam.storytelling.system.tribute.storage.registry.TributeOfferRegistry;
import com.storyteam.storytelling.util.ScriptUtils;
import java.util.HashMap;
import java.util.Map;
import lombok.Generated;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public final class TributeOfferStorage {
   private static final String OFFER_TAG_NAME = "tribute_offer";
   private static final Map<Persona, TributeOffer> offerData = new HashMap<>();
   private static final Map<Persona, Integer> offerDayData = new HashMap<>();

   public static TributeOffer getOffer() {
      return getOfferForPersona(PersonaHandler.getCurrentPersona());
   }

   public static int getOfferDay() {
      return getOfferDayForPersona(PersonaHandler.getCurrentPersona());
   }

   public static void setOffer(TributeOffer offer, int dayAssigned) {
      setOfferForPersona(PersonaHandler.getCurrentPersona(), offer, dayAssigned);
   }

   public static void setNextOffer() {
      setNextOffer(TimeController.get().getCurrentDay());
   }

   public static void setNextOffer(int dayAssigned) {
      setNextOfferForPersona(PersonaHandler.getCurrentPersona(), dayAssigned);
   }

   public static TributeOffer getOfferForPersona(Persona persona) {
      return offerData.getOrDefault(persona, TributeOffer.empty());
   }

   public static int getOfferDayForPersona(Persona persona) {
      return offerDayData.getOrDefault(persona, -1) + getOfferForPersona(persona).getDaysToSubmit();
   }

   public static void setOfferForPersona(Persona persona, TributeOffer offer, int dayAssigned) {
      offerData.put(persona, offer);
      offerDayData.put(persona, dayAssigned);
   }

   public static void setNextOfferForPersona(Persona persona, int dayAssigned) {
      TributeOffer currentOffer = getOfferForPersona(persona);
      if (currentOffer.isEmpty()) {
         currentOffer = TributeOfferRegistry.getAll().stream().findFirst().orElse(TributeOffer.empty());
         setOfferForPersona(persona, currentOffer, dayAssigned);
      } else {
         currentOffer.getNextOffer()
            .ifPresentOrElse(
               nextOffer -> setOfferForPersona(persona, nextOffer, dayAssigned), () -> setOfferForPersona(persona, TributeOffer.empty(), dayAssigned)
            );
      }
   }

   public static void load() {
      offerData.clear();
      offerDayData.clear();
      CompoundTag root = getOfferTag();

      for (Persona persona : Persona.values()) {
         CompoundTag personaTag = root.getCompound(persona.name());
         String id = personaTag.getString("id");
         int day = personaTag.getInt("day");
         if (id.isEmpty()) {
            offerData.put(persona, TributeOffer.empty());
         } else {
            TributeOfferRegistry.get(id).stream().findFirst().ifPresent(offer -> offerData.put(persona, offer));
         }

         offerDayData.put(persona, day);
      }
   }

   public static void save() {
      CompoundTag root = getOfferTag();

      for (Persona persona : Persona.values()) {
         CompoundTag personaTag = new CompoundTag();
         TributeOffer offer = offerData.getOrDefault(persona, TributeOffer.empty());
         int day = offerDayData.getOrDefault(persona, -1);
         if (offer.getIdPath() != null) {
            personaTag.putString("id", offer.isEmpty() ? "" : offer.getIdPath());
         }

         if (day >= 0) {
            personaTag.putInt("day", day);
         }

         root.put(persona.name(), personaTag);
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

   private static CompoundTag getOfferTag() {
      return getOrCreateTag(ScriptIO.WORLD_EXTRA_NBT, "tribute_offer");
   }

   private static CompoundTag getOrCreateTag(CompoundTag tag, String tagName) {
      return ScriptUtils.getTagFromTag(tag, tagName);
   }

   @Generated
   private TributeOfferStorage() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
