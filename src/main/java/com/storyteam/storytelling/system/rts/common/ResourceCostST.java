package com.storyteam.storytelling.system.rts.common;

import com.solegendary.reignofnether.resources.ResourceCost;
import com.solegendary.reignofnether.resources.Resources;
import com.solegendary.reignofnether.resources.ResourcesClientEvents;
import com.solegendary.reignofnether.resources.ResourcesServerEvents;
import com.solegendary.reignofnether.unit.UnitClientEvents;
import com.solegendary.reignofnether.unit.UnitServerEvents;
import com.solegendary.reignofnether.util.MyRenderer;
import com.storyteam.storytelling.system.currency.storage.CurrencyStorage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import net.minecraft.util.FormattedCharSequence;
import org.zeith.hammerlib.api.proxy.IProxy;

public class ResourceCostST {
   private final HashMap<ResourceST, Integer> resourceCosts = new HashMap<>();

   private ResourceCostST() {
   }

   private ResourceCostST(HashMap<ResourceST, Integer> resourceCosts) {
      this.resourceCosts.putAll(resourceCosts);
   }

   public static ResourceCostST create() {
      return new ResourceCostST();
   }

   public int get(ResourceST resource) {
      return this.resourceCosts.getOrDefault(resource, 0);
   }

   private ResourceCostST setNoClone(ResourceST resource, int amount) {
      this.resourceCosts.put(resource, amount);
      return this;
   }

   public ResourceCostST set(ResourceST resource, int amount) {
      return this.copy().setNoClone(resource, amount);
   }

   public ResourceCostST add(ResourceST resource, int amount) {
      return this.copy().setNoClone(resource, amount + this.resourceCosts.getOrDefault(resource, 0));
   }

   public ResourceCostST multiply(double amount) {
      return this.multiply(amount, List.of());
   }

   public ResourceCostST multiplyCost(double amount) {
      return this.multiplyOnly(amount, List.of(ResourceST.RICE, ResourceST.ETHER, ResourceST.VERSIFORM, ResourceST.EIDOS));
   }

   public ResourceCostST multiply(double amount, List<ResourceST> exclude) {
      ResourceCostST r = this.copy();

      for (ResourceST key : this.resourceCosts.keySet()) {
         if (!exclude.contains(key)) {
            r.setNoClone(key, (int)Math.ceil((double)this.resourceCosts.get(key).intValue() * amount));
         }
      }

      return r;
   }

   public ResourceCostST multiplyOnly(double amount, List<ResourceST> include) {
      ResourceCostST r = this.copy();

      for (ResourceST key : this.resourceCosts.keySet()) {
         if (include.contains(key)) {
            r.setNoClone(key, (int)Math.ceil((double)this.resourceCosts.get(key).intValue() * amount));
         }
      }

      return r;
   }

   public ResourceCostST copy() {
      return new ResourceCostST(this.resourceCosts);
   }

   public static ResourceCostST getResources(String ownerName) {
      Resources resources = ((ArrayList)IProxy.create(() -> () -> ResourcesClientEvents.resourcesList, () -> () -> ResourcesServerEvents.resourcesList))
         .stream()
         .filter(r -> Objects.equals(r.ownerName, ownerName))
         .findFirst()
         .orElse(new Resources(ownerName, 0, 0, 0));
      Integer population = (Integer)IProxy.create(
         () -> () -> UnitClientEvents.getCurrentPopulation(ownerName), () -> () -> UnitServerEvents.getCurrentPopulation(null, ownerName)
      );
      int eidos = CurrencyStorage.getEidos();
      return create()
         .setNoClone(ResourceST.RICE, resources.getRice())
         .setNoClone(ResourceST.ETHER, resources.getEther())
         .setNoClone(ResourceST.VERSIFORM, resources.getVersiform())
         .setNoClone(ResourceST.EIDOS, eidos)
         .setNoClone(ResourceST.POPULATION, population);
   }

   public boolean canAfford(String ownerName) {
      ResourceCostST resources = getResources(ownerName);
      return resources.get(ResourceST.RICE) >= this.get(ResourceST.RICE)
         && resources.get(ResourceST.ETHER) >= this.get(ResourceST.ETHER)
         && resources.get(ResourceST.VERSIFORM) >= this.get(ResourceST.VERSIFORM)
         && resources.get(ResourceST.EIDOS) >= this.get(ResourceST.EIDOS);
   }

   public void deposit(String ownerName) {
      ResourcesServerEvents.addSubtractResources(
         new Resources(ownerName, this.get(ResourceST.RICE), this.get(ResourceST.ETHER), this.get(ResourceST.VERSIFORM))
      );
      CurrencyStorage.addEidos(this.get(ResourceST.EIDOS));
   }

   public boolean trySubtract(String ownerName) {
      if (!this.canAfford(ownerName)) {
         return false;
      } else {
         ResourcesServerEvents.addSubtractResources(
            new Resources(ownerName, -this.get(ResourceST.RICE), -this.get(ResourceST.ETHER), -this.get(ResourceST.VERSIFORM))
         );
         CurrencyStorage.removeEidos(this.get(ResourceST.EIDOS));
         return true;
      }
   }

   public static ResourceCostST fromResourceCost(ResourceCost resourceCost) {
      return create()
         .setNoClone(ResourceST.RICE, resourceCost.rice)
         .setNoClone(ResourceST.ETHER, resourceCost.ether)
         .setNoClone(ResourceST.VERSIFORM, resourceCost.versiform)
         .setNoClone(ResourceST.POPULATION, resourceCost.population)
         .setNoClone(ResourceST.TIME, resourceCost.ticks);
   }

   public ResourceCost asResourceCost() {
      return ResourceCost.Unit(
         this.get(ResourceST.RICE), this.get(ResourceST.ETHER), this.get(ResourceST.VERSIFORM), this.get(ResourceST.TIME), this.get(ResourceST.POPULATION)
      );
   }

   public FormattedCharSequence render(List<ResourceST> resources) {
      StringBuilder str = new StringBuilder();

      for (ResourceST res : resources) {
         int amount = this.get(res);
         if (amount != 0) {
            String display = String.valueOf(amount);
            str.append(res.getIconChar()).append(" ").append(display).append("   ");
         }
      }

      return FormattedCharSequence.forward(str.toString().trim(), MyRenderer.iconStyle);
   }

   public FormattedCharSequence renderCost() {
      return this.render(List.of(ResourceST.RICE, ResourceST.ETHER, ResourceST.VERSIFORM, ResourceST.EIDOS));
   }

   public FormattedCharSequence renderPopulationAndTime() {
      return this.render(List.of(ResourceST.POPULATION, ResourceST.TIME));
   }
}
