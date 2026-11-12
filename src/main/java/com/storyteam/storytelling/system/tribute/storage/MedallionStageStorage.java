package com.storyteam.storytelling.system.tribute.storage;

import com.storyteam.storytelling.events.base.ScriptIOEvent;
import com.storyteam.storytelling.events.listeners.ScriptIO;
import com.storyteam.storytelling.system.persona.Persona;
import com.storyteam.storytelling.system.persona.PersonaHandler;
import com.storyteam.storytelling.system.tribute.data.medallion.MedallionStage;
import com.storyteam.storytelling.system.tribute.storage.registry.MedallionStageRegistry;
import com.storyteam.storytelling.util.ScriptUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import lombok.Generated;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public final class MedallionStageStorage {
   private static final String STAGE_TAG_NAME = "medallion_stage";
   private static final Map<Persona, MedallionStage> stageData = new HashMap<>();

   public static MedallionStage getStage() {
      return getStageForPersona(PersonaHandler.getCurrentPersona());
   }

   public static void setStage(MedallionStage stage) {
      setStageForPersona(PersonaHandler.getCurrentPersona(), stage);
   }

   public static MedallionStage getStageForPersona(Persona persona) {
      return stageData.getOrDefault(persona, null);
   }

   public static void setStageForPersona(Persona persona, MedallionStage stage) {
      stageData.put(persona, stage);
   }

   public static void load() {
      stageData.clear();
      CompoundTag root = getStageTag();

      for (Persona persona : Persona.values()) {
         String id = root.getString(getStagePersonaTagName(persona));
         if (id.isEmpty()) {
            MedallionStageRegistry.getAll().stream().findFirst().ifPresent(stage -> setStageForPersona(persona, stage));
         } else {
            MedallionStageRegistry.get(id).stream().findFirst().ifPresent(stage -> setStageForPersona(persona, stage));
         }
      }

      Map<Persona, MedallionStage> data = stageData;
      System.out.println(data);
   }

   public static void save() {
      CompoundTag root = getStageTag();

      for (Entry<Persona, MedallionStage> entry : stageData.entrySet()) {
         Persona persona = entry.getKey();
         MedallionStage stage = entry.getValue();
         if (stage != null && stage.getIdPath() != null) {
            root.putString(getStagePersonaTagName(persona), stage.getIdPath());
         }
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

   private static String getStagePersonaTagName(Persona persona) {
      return persona.name();
   }

   private static CompoundTag getStageTag() {
      return getOrCreateTag(ScriptIO.WORLD_EXTRA_NBT, "medallion_stage");
   }

   private static CompoundTag getOrCreateTag(CompoundTag tag, String tagName) {
      return ScriptUtils.getTagFromTag(tag, tagName);
   }

   @Generated
   private MedallionStageStorage() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
