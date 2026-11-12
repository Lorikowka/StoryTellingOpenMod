package com.storyteam.storytelling.script.instances.backs.world_regions;

import com.storyteam.storytelling.api.Script;
import com.storyteam.storytelling.api.data.EntityInteractionData;
import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.init.system.world_map_region.WorldMapRegionsST;
import com.storyteam.storytelling.script.ScriptInstance;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.builder.ScriptPerformerBuilder;
import com.storyteam.storytelling.script.performer.builder.reflective.ScriptPerformerBuilderReflective;
import com.storyteam.storytelling.script.story.ScriptEnvironment;
import com.storyteam.storytelling.system.simulator.world_tab.WorldTabSystem;
import net.minecraft.server.level.ServerPlayer;
import org.zeith.hammerlib.api.io.NBTSerializable;

@Script(113377)
public class S113377 extends ScriptInstance {
   @NBTSerializable
   public final ScriptPerformer mainFlow = (new ScriptPerformerBuilderReflective(this) {
      @ScriptPerformerBuilderReflective.ScriptPart(0)
      private void part0(S113377 inst) {
         this.thenPerform(WorldMapRegionsST.REGION_1.setState(2));
         this.thenPerform(WorldMapRegionsST.REGION_3.setState(2));
         this.thenPerform(WorldMapRegionsST.REGION_5.setState(2));
         this.thenPerform(WorldMapRegionsST.REGION_7.setState(2));
         this.thenWaitForF8();

         for (int i = 1; i <= 20; i++) {
            this.thenRun(WorldTabSystem.setRegionState("region_" + i, (byte)0));
         }

         this.thenWaitForF8();

         for (int i = 1; i <= 20; i++) {
            this.thenRun(WorldTabSystem.setRegionState("region_" + i, (byte)1));
         }

         this.thenWaitForF8();

         for (int i = 1; i <= 20; i++) {
            this.thenRun(WorldTabSystem.setRegionState("region_" + i, (byte)2));
         }
      }
   }).build(this::registerMainPerformer);

   public S113377(ScriptEnvironment env, int id) {
      super(env, id);
   }

   @Override
   public void updateAsPlayer(ServerPlayer player) {
      super.updateAsPlayer(player);
   }

   @Override
   public void handleStop() {
      this.getRepository().removeAll();
      super.handleStop();
   }

   @Override
   public void handleStart() {
      super.handleStart();
      this.handleEvent(EventType.ENTITY_CLICK, this::onClick);
   }

   @Override
   public ScriptPerformer handleSetup() {
      return new ScriptPerformerBuilder(this).build();
   }

   public void onClick(EntityInteractionData ctx) {
   }
}
