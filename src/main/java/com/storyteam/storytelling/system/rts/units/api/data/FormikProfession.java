package com.storyteam.storytelling.system.rts.units.api.data;

import com.solegendary.reignofnether.hud.AbilityButton;
import com.storyteam.storytelling.St;
import com.storyteam.storytelling.init.geo.EntityModelsST;
import com.storyteam.storytelling.system.rts.common.ResourceCostST;
import lombok.Generated;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammeranims.api.geometry.IGeometryContainer;

public enum FormikProfession {
   WORKER("Работяга", "formik_worker", EntityModelsST.RTS_FORMIK_WORKER),
   FARMER("Работяга", "formik_farmer", EntityModelsST.RTS_FORMIK_FARMER),
   FISHER("Работяга", "formik_fisher", EntityModelsST.RTS_FORMIK_FISHER),
   ETHER_COLLECTOR("Работяга", "formik_ether_collector", EntityModelsST.RTS_FORMIK_ETHER_COLLECTOR),
   MINER("Работяга", "formik_miner", EntityModelsST.RTS_FORMIK_WORKER),
   BUILDER("Работяга", "formik_builder", EntityModelsST.RTS_FORMIK_BUILDER),
   TRADER("Работяга", "formik_trader", EntityModelsST.RTS_FORMIK_WORKER),
   CARPENTER("Работяга", "formik_carpenter", EntityModelsST.RTS_FORMIK_WORKER),
   BLACKSMITH("Работяга", "formik_blacksmith", EntityModelsST.RTS_FORMIK_WORKER);

   private final String unitName;
   private final ResourceLocation tx;
   private final IGeometryContainer geo;
   @Nullable
   private final ResourceCostST cost;
   @Nullable
   private final AbilityButton button;
   @Nullable
   private final AbilityButton eliteButton;

   private FormikProfession(String unitName, String tx, IGeometryContainer geo) {
      this(unitName, tx, geo, null, null, null);
   }

   private FormikProfession(
      String unitName, String tx, IGeometryContainer geo, @Nullable ResourceCostST cost, @Nullable AbilityButton button, @Nullable AbilityButton eliteButton
   ) {
      this.unitName = unitName;
      this.tx = St.tx("entity/rts/" + tx);
      this.geo = geo;
      this.cost = cost;
      this.button = button;
      this.eliteButton = eliteButton;
   }

   @Generated
   public String getUnitName() {
      return this.unitName;
   }

   @Generated
   public ResourceLocation getTx() {
      return this.tx;
   }

   @Generated
   public IGeometryContainer getGeo() {
      return this.geo;
   }

   @Nullable
   @Generated
   public ResourceCostST getCost() {
      return this.cost;
   }

   @Nullable
   @Generated
   public AbilityButton getButton() {
      return this.button;
   }

   @Nullable
   @Generated
   public AbilityButton getEliteButton() {
      return this.eliteButton;
   }
}
