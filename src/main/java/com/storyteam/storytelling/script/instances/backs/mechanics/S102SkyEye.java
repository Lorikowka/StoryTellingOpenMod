package com.storyteam.storytelling.script.instances.backs.mechanics;

import com.storyteam.storytelling.api.SaveOnClose;
import com.storyteam.storytelling.api.Script;
import com.storyteam.storytelling.client.animations.AnimationsST8;
import com.storyteam.storytelling.content.entities.SkyEyeEntity;
import com.storyteam.storytelling.script.BackInstance;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.builder.reflective.ScriptPerformerBuilderReflective;
import com.storyteam.storytelling.script.performer.entity.EntityState;
import com.storyteam.storytelling.script.performer.entity.look.ILookTarget;
import com.storyteam.storytelling.script.performer.entity.look.SkyEyeLookController;
import com.storyteam.storytelling.script.performer.entity.look.targets.CharacterTarget;
import com.storyteam.storytelling.script.performer.entity.look.targets.PlayerTarget;
import com.storyteam.storytelling.script.performer.entity.look.targets.PositionTarget;
import com.storyteam.storytelling.script.story.ScriptEnvironment;
import com.storyteam.storytelling.script.story.characters.CharactersST8;
import com.storyteam.storytelling.util.PositionParser;
import com.storyteam.storytelling.util.world.Location;
import net.minecraft.world.level.Level;
import org.zeith.hammeranims.api.animation.IAnimationSource;

@SaveOnClose
@Script(-102)
public class S102SkyEye extends BackInstance {
   public static final int SCRIPT_ID = -102;
   private static Location defLocation = new Location(Level.OVERWORLD, 0.0, 0.0, 0.0);
   public ScriptPerformer MAIN = (new ScriptPerformerBuilderReflective(this) {
      @ScriptPerformerBuilderReflective.Secondary("spawn")
      private void spawn(S102SkyEye inst) {
         this.positionEntity(CharactersST8.SKY_EYE, "/execute in minecraft:overworld run tp @s -230.90 500.00 140.26 -255.55 76.80");
         this.thenRun(() -> {
            SkyEyeEntity entity = (SkyEyeEntity)CharactersST8.SKY_EYE.repositoryEntity();
            if (entity != null) {
               S102SkyEye.defLocation = new Location(entity.level().dimension(), entity.getX(), entity.getY(), entity.getZ());
            }
         });
         this.setEntityLookController(CharactersST8.SKY_EYE, new SkyEyeLookController());
         this.entityLookAt(CharactersST8.SKY_EYE, "player", false);
         this.action(CharactersST8.SKY_EYE, new IAnimationSource[]{AnimationsST8.SKYEYE_BASE});
      }
   }).build(this::registerMainPerformer);

   public S102SkyEye(ScriptEnvironment env, int id) {
      super(env, id);
   }

   public static Location getDefLocation() {
      return defLocation;
   }

   public void entityLookAt(String sTarget) {
      EntityState<SkyEyeEntity> eye = this.getRepository().getOrCreate(CharactersST8.SKY_EYE, true);
      Location loc = PositionParser.parseLocationWithRotation(sTarget);
      ILookTarget<SkyEyeEntity> target;
      if (loc != null) {
         target = new PositionTarget(loc, false);
      } else if (sTarget.equals("player")) {
         target = new PlayerTarget();
      } else {
         target = new CharacterTarget(sTarget);
      }

      eye.getData().setLookTarget(target);
   }
}
