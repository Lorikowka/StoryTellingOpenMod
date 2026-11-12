package com.storyteam.storytelling.system.persona;

import lombok.Generated;

public enum Persona {
   LOLOLOWKA(PersonaInfo.builder().build());

   final PersonaInfo info;

   public Persona getSharedPersona() {
      Persona sharedPersona = this.info.getSharedPersona();
      return sharedPersona == null ? this : sharedPersona.getSharedPersona();
   }

   public Persona getLevelingPersona() {
      Persona sharedPersona = this.info.getLevelingSharedPersona();
      return sharedPersona == null ? this : sharedPersona.getLevelingPersona();
   }

   @Generated
   public PersonaInfo getInfo() {
      return this.info;
   }

   @Generated
   private Persona(PersonaInfo info) {
      this.info = info;
   }
}
