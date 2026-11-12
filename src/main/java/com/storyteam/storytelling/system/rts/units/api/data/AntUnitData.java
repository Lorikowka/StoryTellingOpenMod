package com.storyteam.storytelling.system.rts.units.api.data;

import lombok.Generated;
import org.zeith.hammerlib.api.io.IAutoNBTSerializable;
import org.zeith.hammerlib.api.io.NBTSerializable;

public final class AntUnitData implements IAutoNBTSerializable {
   @NBTSerializable
   private FormikProfession profession = FormikProfession.WORKER;
   @NBTSerializable
   private boolean elite = false;

   public FormikProfession getProfession() {
      return this.profession == null ? FormikProfession.WORKER : this.profession;
   }

   public AntUnitData setProfession(FormikProfession profession) {
      this.profession = profession;
      return this;
   }

   public AntUnitData setElite(boolean elite) {
      this.elite = elite;
      return this;
   }

   @Generated
   public AntUnitData() {
   }

   @Generated
   public AntUnitData(FormikProfession profession, boolean elite) {
      this.profession = profession;
      this.elite = elite;
   }

   @Generated
   public boolean isElite() {
      return this.elite;
   }
}
