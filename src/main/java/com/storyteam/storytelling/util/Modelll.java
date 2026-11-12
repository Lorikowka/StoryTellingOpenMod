package com.storyteam.storytelling.util;

import java.util.ArrayList;
import java.util.HashMap;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;

public class Modelll extends PlayerModel {
   public Modelll() {
      super(new ModelPart(new ArrayList(), new HashMap<String, ModelPart>() {
         {
            this.put("head", new ModelPart(new ArrayList(), new HashMap()));
            this.put("hat", new ModelPart(new ArrayList(), new HashMap()));
            this.put("body", new ModelPart(new ArrayList(), new HashMap()));
            this.put("right_arm", new ModelPart(new ArrayList(), new HashMap()));
            this.put("left_arm", new ModelPart(new ArrayList(), new HashMap()));
            this.put("right_leg", new ModelPart(new ArrayList(), new HashMap()));
            this.put("left_leg", new ModelPart(new ArrayList(), new HashMap()));
            this.put("ear", new ModelPart(new ArrayList(), new HashMap()));
            this.put("cloak", new ModelPart(new ArrayList(), new HashMap()));
            this.put("left_sleeve", new ModelPart(new ArrayList(), new HashMap()));
            this.put("right_sleeve", new ModelPart(new ArrayList(), new HashMap()));
            this.put("left_pants", new ModelPart(new ArrayList(), new HashMap()));
            this.put("right_pants", new ModelPart(new ArrayList(), new HashMap()));
            this.put("jacket", new ModelPart(new ArrayList(), new HashMap()));
         }
      }), false);
   }
}
