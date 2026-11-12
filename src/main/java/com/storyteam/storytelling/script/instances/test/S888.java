package com.storyteam.storytelling.script.instances.test;

import com.storyteam.storytelling.api.ForgetOnClose;
import com.storyteam.storytelling.api.Script;
import com.storyteam.storytelling.init.EntitiesST;
import com.storyteam.storytelling.script.ScriptInstance;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.builder.reflective.ScriptPerformerBuilderReflective;
import com.storyteam.storytelling.script.story.ScriptEnvironment;
import com.storyteam.storytelling.system.currency.content.entities.EidosEntity;
import com.storyteam.storytelling.util.UtilST;
import java.util.Random;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.zeith.hammerlib.api.io.NBTSerializable;

@ForgetOnClose
@Script(888)
public class S888 extends ScriptInstance {
   public static Random rng = new Random(12345678L);
   @NBTSerializable
   public final ScriptPerformer mainFlow = (new ScriptPerformerBuilderReflective(this) {
      @ScriptPerformerBuilderReflective.ScriptPart(0)
      private void part0(S888 inst) {
         this.thenWaitForF8();
         this.blackout(30);
      }
   }).build(this::registerMainPerformer);

   public S888(ScriptEnvironment env, int id) {
      super(env, id);
   }

   public static void dropExp(Level level, Vec3 loc, int expCount, boolean ignoreModifiers) {
      dropExp(level, loc, expCount, 15, ignoreModifiers);
   }

   public static void dropExp(Level level, Vec3 loc, int expCount, int delay_unpickable, boolean ignoreModifiers) {
      int reward = expCount;
      if (!ignoreModifiers) {
         float exp = (float)expCount;
         int awarded = (int)exp;
         int extra = UtilST.rng.nextFloat() < exp - (float)awarded ? 1 : 0;
         reward = awarded + extra;
      }

      dropExp(level, loc, reward, delay_unpickable);
   }

   private static void dropExp(Level level, Vec3 loc, int expCount, int delay_unpickable) {
      if (expCount != 0) {
         for (int st = EidosEntity.STAGES.length - 1; st >= 0; st--) {
            if (expCount >= EidosEntity.STAGES[st]) {
               int orbCount = expCount / EidosEntity.STAGES[st];
               int remainder = expCount % EidosEntity.STAGES[st];

               for (int i = 0; i < orbCount; i++) {
                  EidosEntity sparkEntity = new EidosEntity(EntitiesST.EIDOS_PICKABLE, level);
                  sparkEntity.setAmount(EidosEntity.STAGES[st]);
                  sparkEntity.setPos(loc);
                  sparkEntity.setDeltaMovement(rng.nextDouble(-0.2, 0.2), rng.nextDouble(0.1, 0.2), rng.nextDouble(-0.2, 0.2));
                  sparkEntity.setDelayUnpickable(delay_unpickable);
                  level.addFreshEntity(sparkEntity);
               }

               dropExp(level, loc, remainder, delay_unpickable);
               return;
            }

            if (st == 0) {
               EidosEntity sparkEntity = new EidosEntity(EntitiesST.EIDOS_PICKABLE, level);
               sparkEntity.setAmount(expCount);
               sparkEntity.setPos(loc);
               sparkEntity.setDeltaMovement(rng.nextDouble(-0.2, 0.2), rng.nextDouble(0.1, 0.2), rng.nextDouble(-0.2, 0.2));
               level.addFreshEntity(sparkEntity);
            }
         }
      }
   }

   @Override
   public void updateAsPlayer(ServerPlayer player) {
      super.updateAsPlayer(player);
   }
}
