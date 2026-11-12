package com.storyteam.storytelling.script.instances.test;

import com.storyteam.storytelling.api.ForgetOnClose;
import com.storyteam.storytelling.api.Script;
import com.storyteam.storytelling.api.data.EntityInteractionData;
import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.content.entities.AntEntity;
import com.storyteam.storytelling.init.BlocksST;
import com.storyteam.storytelling.init.ParticleTypesST;
import com.storyteam.storytelling.script.ScriptInstance;
import com.storyteam.storytelling.script.performer.Performer;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.builder.ScriptPerformerBuilder;
import com.storyteam.storytelling.script.performer.builder.reflective.ScriptPerformerBuilderReflective;
import com.storyteam.storytelling.script.story.ScriptEnvironment;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.script.story.characters.CharacterList;
import com.storyteam.storytelling.script.story.characters.CharactersST8;
import com.storyteam.storytelling.util.ParticleHelper;
import com.storyteam.storytelling.util.ScriptUtils;
import com.storyteam.storytelling.util.world.Location;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.zeith.hammerlib.api.io.NBTSerializable;

@ForgetOnClose
@Script(
   value = 12312,
   description = "Test script (VANYA2PIZZA)"
)
public class S12312 extends ScriptInstance {
   private static final CharacterList<AntEntity> ANTS = CharactersST8.ANT_FAC.createCharacterList("s1_ants", 8);
   BlockPos block = new BlockPos(-743, 103, 99);
   Vec3 toS1 = new Vec3(-0.005, 0.026, -0.999);
   Vec3 toS2 = new Vec3(0.699, 0.023, 0.714);
   Vec3 toS3 = new Vec3(-0.712, -0.012, 0.701);
   @NBTSerializable
   public final ScriptPerformer mainFlow = (new ScriptPerformerBuilderReflective(this) {
         @ScriptPerformerBuilderReflective.ScriptPart(0)
         private void part0(S12312 inst) {
            this.saveState("0");
            this.thenSetSpeechEntity(2, "Ану", "anu_icon");
            this.positionEntity(CharactersST8.STATUE_1, "/execute in minecraft:overworld run tp @s -742.45 104.00 103.36 -180.31 -1.50");
            this.positionEntity(CharactersST8.STATUE_2, "/execute in minecraft:overworld run tp @s -745.35 104.00 96.62 -44.41 -1.35");
            this.positionEntity(CharactersST8.STATUE_3, "/execute in minecraft:overworld run tp @s -739.50 104.00 96.53 45.44 0.75");
            this.thenDelay(20);
            this.thenWaitUntil(() -> {
               Player p = ScriptUtils.getPlayer();
               if (p != null) {
                  int playerX = p.blockPosition().getX();
                  int playerY = p.blockPosition().getY() - 1;
                  int playerZ = p.blockPosition().getZ();
                  Entity s1 = ScriptUtils.getEntityFromCharacter("statue_1");
                  Entity s2 = ScriptUtils.getEntityFromCharacter("statue_2");
                  Entity s3 = ScriptUtils.getEntityFromCharacter("statue_3");
                  if (s1 != null && s2 != null && s3 != null) {
                     Vec3 lookS1 = S12312.this.getLookEntityWith3SymbolsAfterComma(s1);
                     Vec3 lookS2 = S12312.this.getLookEntityWith3SymbolsAfterComma(s2);
                     Vec3 lookS3 = S12312.this.getLookEntityWith3SymbolsAfterComma(s3);
                     return S12312.this.checkFullCondition(new BlockPos(playerX, playerY, playerZ), lookS1, lookS2, lookS3);
                  }
               }

               return false;
            });
            this.thenSpeechMessage(2, "Test!");
            this.freeEntities(true, new Character[]{CharactersST8.STATUE_1, CharactersST8.STATUE_2, CharactersST8.STATUE_3});
            this.saveState("1");
            this.setTime("12:00");
            this.thenDelay(100);
            this.thenSpeechMessage(2, "Test!");
            this.thenDelay(200);
            this.thenWaitForF8();
            this.timeMarker("12:03");
            this.saveState("3");
            this.positionEntity(S12312.ANTS.get(0), "/execute in minecraft:overworld run tp @s -150.41 108.00 -417.65 258.06 0.75");
            this.positionEntity(S12312.ANTS.get(1), "/execute in minecraft:overworld run tp @s -152.41 108.00 -417.65 258.06 0.75");
            this.thenEntityPathParallel(S12312.ANTS.get(0), new String[]{"/execute in minecraft:overworld run tp @s -149.52 108.00 -419.62 -544.57 0.75"});
            this.thenEntityPath(S12312.ANTS.get(1), new String[]{"/execute in minecraft:overworld run tp @s -149.52 108.00 -419.62 -544.57 0.75"});
            this.thenHandleState(S12312.this.createFlag("set_glitch_effect_on_block"));
            this.thenAllPerformers(
               new Performer[]{
                  ScriptPerformerBuilder.builder(inst).thenDelay(70).build(),
                  ScriptPerformerBuilder.builder(inst).thenDelay(20).freeEntities(true, S12312.ANTS.get(0), S12312.ANTS.get(1)).build(),
                  ScriptPerformerBuilder.builder(inst)
                     .thenDelay(30)
                     .thenUsePlayer(p -> p.level().setBlockAndUpdate(new BlockPos(-150, 108, -420), BlocksST.PEN.defaultBlockState()))
                     .build()
               }
            );
            this.thenHandleState(S12312.this.clearFlag("set_glitch_effect_on_block"));
         }
      })
      .build(this::registerMainPerformer);

   public S12312(ScriptEnvironment env, int id) {
      super(env, id);
   }

   private Vec3 getLookEntityWith3SymbolsAfterComma(Entity entity) {
      double x = entity.getLookAngle().x;
      double y = entity.getLookAngle().y;
      double z = entity.getLookAngle().z;
      double resulX = (double)((long)(x * 1000.0)) / 1000.0;
      double resulY = (double)((long)(y * 1000.0)) / 1000.0;
      double resulZ = (double)((long)(z * 1000.0)) / 1000.0;
      return new Vec3(resulX, resulY, resulZ);
   }

   private boolean checkLookAngleEntity(Vec3 entity, Vec3 to) {
      return entity.equals(to);
   }

   private boolean checkPlayerUnderBloock(BlockPos blockPos, BlockPos player) {
      return blockPos.equals(player);
   }

   private boolean checkFullCondition(BlockPos blockPos, Vec3 entity1, Vec3 entity2, Vec3 entity3) {
      return this.checkPlayerUnderBloock(this.block, blockPos)
         && this.checkLookAngleEntity(entity1, this.toS1)
         && this.checkLookAngleEntity(entity2, this.toS2)
         && this.checkLookAngleEntity(entity3, this.toS3);
   }

   @Override
   public void updateAsPlayer(ServerPlayer player) {
      super.updateAsPlayer(player);
      if (this.flags.has("set_glitch_effect_on_block")) {
         this.setGlitchEffectOnBlock(new BlockPos(-150, 108, -420));
      }
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

   private void setGlitchEffectOnBlock(BlockPos blockPos) {
      Random random = new Random();
      double baseX = (double)blockPos.getX() + 0.5;
      double baseY = (double)blockPos.getY() + 0.5;
      double baseZ = (double)blockPos.getZ() + 0.5;
      double expansion = 1.3;

      for (int i = 0; i < 5; i++) {
         double x = baseX + (random.nextDouble() - 0.5) * expansion;
         double y = baseY + (random.nextDouble() - 0.5) * expansion;
         double z = baseZ + (random.nextDouble() - 0.5) * expansion;
         Location particleLocation = new Location(x, y, z);
         ParticleHelper.spawnDirectedParticle(ScriptUtils.getLevel(), ParticleTypesST.GLITCH, particleLocation, Vec3.ZERO);
      }
   }

   @Override
   public ScriptPerformer handleSetup() {
      return new ScriptPerformerBuilder(this).build();
   }

   public void onClick(EntityInteractionData ctx) {
   }
}
