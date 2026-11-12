package com.storyteam.storytelling.script.instances.backs.mechanics;

import com.storyteam.storytelling.api.SaveOnClose;
import com.storyteam.storytelling.api.Script;
import com.storyteam.storytelling.client.particle.circleTint.CircleTintData;
import com.storyteam.storytelling.client.particle.controllers.ScatterController;
import com.storyteam.storytelling.script.BackInstance;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.builder.reflective.ScriptPerformerBuilderReflective;
import com.storyteam.storytelling.script.story.ScriptEnvironment;
import com.storyteam.storytelling.util.Color;
import com.storyteam.storytelling.util.MathUtils;
import com.storyteam.storytelling.util.ParticleHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

@SaveOnClose
@Script(-103)
public class S103Fireflies<T extends Entity> extends BackInstance {
   public static final int SCRIPT_ID = -103;
   private static int[] BASE_COLORS = new int[]{28851, 7604399, 14894405};
   public ScriptPerformer MAIN = (new ScriptPerformerBuilderReflective(this) {
      @ScriptPerformerBuilderReflective.Secondary("spawn_fireflies")
      private void spawn(S103Fireflies inst) {
         this.label("loop");
         this.waitForTimeBetween("00:00", "06:00");
         this.createTicker("fireflies", player -> S103Fireflies.this.spawnParticles(player));
         this.waitForTimeBetween("06:01", "23:59");
         this.deleteTicker("fireflies");
         this.gotoLabel("loop");
      }
   }).build(this::registerMainPerformer);

   public S103Fireflies(ScriptEnvironment env, int id) {
      super(env, id);
   }

   private void spawnParticles(Player player) {
      Level level = player.level();
      RandomSource random = level.getRandom();
      if (!(random.nextFloat() > 0.3F)) {
         Vec3 playerPos = player.position();
         int count = 2 + random.nextInt(2);

         for (int i = 0; i < count; i++) {
            double radius = 20.0 + random.nextDouble() * 7.0;
            double angle = random.nextDouble() * Math.PI * 2.0;
            double px = playerPos.x + Math.cos(angle) * radius;
            double pz = playerPos.z + Math.sin(angle) * radius;
            double py = playerPos.y + random.nextDouble() * 10.0;
            int base = BASE_COLORS[random.nextInt(BASE_COLORS.length)];
            Vector3f colorVec = this.getColorVector(base, random);
            Color color = new Color(colorVec.x, colorVec.y, colorVec.z);
            float diameter = 0.04F + random.nextFloat() * 0.07F;
            int lifetime = 120 + random.nextInt(100);
            CircleTintData data = new CircleTintData(color, diameter, 40 + random.nextInt(20), lifetime, -1.0F, true, false, new ScatterController());
            double speedX = (random.nextDouble() - 0.5) * 0.008;
            double speedY = (random.nextDouble() - 0.5) * 0.006;
            double speedZ = (random.nextDouble() - 0.5) * 0.008;
            ParticleHelper.spawnDirectedParticle(level, data, px, py, pz, speedX, speedY, speedZ);
         }
      }
   }

   private Vector3f getColorVector(int baseColor, RandomSource rand) {
      int r = baseColor >> 16 & 0xFF;
      int g = baseColor >> 8 & 0xFF;
      int b = baseColor & 0xFF;
      float variation = 0.75F + rand.nextFloat() * 0.5F;
      float rf = MathUtils.clamp((float)r / 255.0F * variation, 0.0F, 1.0F);
      float gf = MathUtils.clamp((float)g / 255.0F * variation, 0.0F, 1.0F);
      float bf = MathUtils.clamp((float)b / 255.0F * variation, 0.0F, 1.0F);
      return new Vector3f(rf, gf, bf);
   }
}
