package com.storyteam.storytelling.system.persona;

import lombok.Generated;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public final class PersonaInfo {
   private final ResourceLocation textureOverride;
   private final Component nameOverride;
   private final Persona levelingSharedPersona;
   private final Persona sharedPersona;
   private final PersonaModel model;
   private final String chatIcon;
   private final float restoreManaOnHit;
   private final int maxSpells;

   @Generated
   private static Persona $default$levelingSharedPersona() {
      return null;
   }

   @Generated
   private static Persona $default$sharedPersona() {
      return null;
   }

   @Generated
   private static PersonaModel $default$model() {
      return PersonaModel.DEFAULT;
   }

   @Generated
   private static String $default$chatIcon() {
      return "lololowka";
   }

   @Generated
   private static int $default$maxSpells() {
      return 3;
   }

   @Generated
   PersonaInfo(
      ResourceLocation textureOverride,
      Component nameOverride,
      Persona levelingSharedPersona,
      Persona sharedPersona,
      PersonaModel model,
      String chatIcon,
      float restoreManaOnHit,
      int maxSpells
   ) {
      this.textureOverride = textureOverride;
      this.nameOverride = nameOverride;
      this.levelingSharedPersona = levelingSharedPersona;
      this.sharedPersona = sharedPersona;
      this.model = model;
      this.chatIcon = chatIcon;
      this.restoreManaOnHit = restoreManaOnHit;
      this.maxSpells = maxSpells;
   }

   @Generated
   public static PersonaInfo.PersonaInfoBuilder builder() {
      return new PersonaInfo.PersonaInfoBuilder();
   }

   @Generated
   public ResourceLocation getTextureOverride() {
      return this.textureOverride;
   }

   @Generated
   public Component getNameOverride() {
      return this.nameOverride;
   }

   @Generated
   public Persona getLevelingSharedPersona() {
      return this.levelingSharedPersona;
   }

   @Generated
   public Persona getSharedPersona() {
      return this.sharedPersona;
   }

   @Generated
   public PersonaModel getModel() {
      return this.model;
   }

   @Generated
   public String getChatIcon() {
      return this.chatIcon;
   }

   @Generated
   public float getRestoreManaOnHit() {
      return this.restoreManaOnHit;
   }

   @Generated
   public int getMaxSpells() {
      return this.maxSpells;
   }

   @Generated
   @Override
   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof PersonaInfo other)) {
         return false;
      } else if (Float.compare(this.getRestoreManaOnHit(), other.getRestoreManaOnHit()) != 0) {
         return false;
      } else if (this.getMaxSpells() != other.getMaxSpells()) {
         return false;
      } else {
         Object this$textureOverride = this.getTextureOverride();
         Object other$textureOverride = other.getTextureOverride();
         if (this$textureOverride == null ? other$textureOverride == null : this$textureOverride.equals(other$textureOverride)) {
            Object this$nameOverride = this.getNameOverride();
            Object other$nameOverride = other.getNameOverride();
            if (this$nameOverride == null ? other$nameOverride == null : this$nameOverride.equals(other$nameOverride)) {
               Object this$levelingSharedPersona = this.getLevelingSharedPersona();
               Object other$levelingSharedPersona = other.getLevelingSharedPersona();
               if (this$levelingSharedPersona == null ? other$levelingSharedPersona == null : this$levelingSharedPersona.equals(other$levelingSharedPersona)) {
                  Object this$sharedPersona = this.getSharedPersona();
                  Object other$sharedPersona = other.getSharedPersona();
                  if (this$sharedPersona == null ? other$sharedPersona == null : this$sharedPersona.equals(other$sharedPersona)) {
                     Object this$model = this.getModel();
                     Object other$model = other.getModel();
                     if (this$model == null ? other$model == null : this$model.equals(other$model)) {
                        Object this$chatIcon = this.getChatIcon();
                        Object other$chatIcon = other.getChatIcon();
                        return this$chatIcon == null ? other$chatIcon == null : this$chatIcon.equals(other$chatIcon);
                     } else {
                        return false;
                     }
                  } else {
                     return false;
                  }
               } else {
                  return false;
               }
            } else {
               return false;
            }
         } else {
            return false;
         }
      }
   }

   @Generated
   @Override
   public int hashCode() {
      int PRIME = 59;
      int result = 1;
      result = result * 59 + Float.floatToIntBits(this.getRestoreManaOnHit());
      result = result * 59 + this.getMaxSpells();
      Object $textureOverride = this.getTextureOverride();
      result = result * 59 + ($textureOverride == null ? 43 : $textureOverride.hashCode());
      Object $nameOverride = this.getNameOverride();
      result = result * 59 + ($nameOverride == null ? 43 : $nameOverride.hashCode());
      Object $levelingSharedPersona = this.getLevelingSharedPersona();
      result = result * 59 + ($levelingSharedPersona == null ? 43 : $levelingSharedPersona.hashCode());
      Object $sharedPersona = this.getSharedPersona();
      result = result * 59 + ($sharedPersona == null ? 43 : $sharedPersona.hashCode());
      Object $model = this.getModel();
      result = result * 59 + ($model == null ? 43 : $model.hashCode());
      Object $chatIcon = this.getChatIcon();
      return result * 59 + ($chatIcon == null ? 43 : $chatIcon.hashCode());
   }

   @Generated
   @Override
   public String toString() {
      return "PersonaInfo(textureOverride="
         + this.getTextureOverride()
         + ", nameOverride="
         + this.getNameOverride()
         + ", levelingSharedPersona="
         + this.getLevelingSharedPersona()
         + ", sharedPersona="
         + this.getSharedPersona()
         + ", model="
         + this.getModel()
         + ", chatIcon="
         + this.getChatIcon()
         + ", restoreManaOnHit="
         + this.getRestoreManaOnHit()
         + ", maxSpells="
         + this.getMaxSpells()
         + ")";
   }

   @Generated
   public static class PersonaInfoBuilder {
      @Generated
      private ResourceLocation textureOverride;
      @Generated
      private Component nameOverride;
      @Generated
      private boolean levelingSharedPersona$set;
      @Generated
      private Persona levelingSharedPersona$value;
      @Generated
      private boolean sharedPersona$set;
      @Generated
      private Persona sharedPersona$value;
      @Generated
      private boolean model$set;
      @Generated
      private PersonaModel model$value;
      @Generated
      private boolean chatIcon$set;
      @Generated
      private String chatIcon$value;
      @Generated
      private float restoreManaOnHit;
      @Generated
      private boolean maxSpells$set;
      @Generated
      private int maxSpells$value;

      @Generated
      PersonaInfoBuilder() {
      }

      @Generated
      public PersonaInfo.PersonaInfoBuilder textureOverride(ResourceLocation textureOverride) {
         this.textureOverride = textureOverride;
         return this;
      }

      @Generated
      public PersonaInfo.PersonaInfoBuilder nameOverride(Component nameOverride) {
         this.nameOverride = nameOverride;
         return this;
      }

      @Generated
      public PersonaInfo.PersonaInfoBuilder levelingSharedPersona(Persona levelingSharedPersona) {
         this.levelingSharedPersona$value = levelingSharedPersona;
         this.levelingSharedPersona$set = true;
         return this;
      }

      @Generated
      public PersonaInfo.PersonaInfoBuilder sharedPersona(Persona sharedPersona) {
         this.sharedPersona$value = sharedPersona;
         this.sharedPersona$set = true;
         return this;
      }

      @Generated
      public PersonaInfo.PersonaInfoBuilder model(PersonaModel model) {
         this.model$value = model;
         this.model$set = true;
         return this;
      }

      @Generated
      public PersonaInfo.PersonaInfoBuilder chatIcon(String chatIcon) {
         this.chatIcon$value = chatIcon;
         this.chatIcon$set = true;
         return this;
      }

      @Generated
      public PersonaInfo.PersonaInfoBuilder restoreManaOnHit(float restoreManaOnHit) {
         this.restoreManaOnHit = restoreManaOnHit;
         return this;
      }

      @Generated
      public PersonaInfo.PersonaInfoBuilder maxSpells(int maxSpells) {
         this.maxSpells$value = maxSpells;
         this.maxSpells$set = true;
         return this;
      }

      @Generated
      public PersonaInfo build() {
         Persona levelingSharedPersona$value = this.levelingSharedPersona$value;
         if (!this.levelingSharedPersona$set) {
            levelingSharedPersona$value = PersonaInfo.$default$levelingSharedPersona();
         }

         Persona sharedPersona$value = this.sharedPersona$value;
         if (!this.sharedPersona$set) {
            sharedPersona$value = PersonaInfo.$default$sharedPersona();
         }

         PersonaModel model$value = this.model$value;
         if (!this.model$set) {
            model$value = PersonaInfo.$default$model();
         }

         String chatIcon$value = this.chatIcon$value;
         if (!this.chatIcon$set) {
            chatIcon$value = PersonaInfo.$default$chatIcon();
         }

         int maxSpells$value = this.maxSpells$value;
         if (!this.maxSpells$set) {
            maxSpells$value = PersonaInfo.$default$maxSpells();
         }

         return new PersonaInfo(
            this.textureOverride,
            this.nameOverride,
            levelingSharedPersona$value,
            sharedPersona$value,
            model$value,
            chatIcon$value,
            this.restoreManaOnHit,
            maxSpells$value
         );
      }

      @Generated
      @Override
      public String toString() {
         return "PersonaInfo.PersonaInfoBuilder(textureOverride="
            + this.textureOverride
            + ", nameOverride="
            + this.nameOverride
            + ", levelingSharedPersona$value="
            + this.levelingSharedPersona$value
            + ", sharedPersona$value="
            + this.sharedPersona$value
            + ", model$value="
            + this.model$value
            + ", chatIcon$value="
            + this.chatIcon$value
            + ", restoreManaOnHit="
            + this.restoreManaOnHit
            + ", maxSpells$value="
            + this.maxSpells$value
            + ")";
      }
   }
}
