package com.storyteam.storytelling.script.instances.backs.mechanics;

import com.storyteam.storytelling.api.SaveOnClose;
import com.storyteam.storytelling.api.Script;
import com.storyteam.storytelling.script.ScriptInstance;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.builder.ScriptPerformerBuilder;
import com.storyteam.storytelling.script.story.ScriptEnvironment;
import com.storyteam.storytelling.system.regions.ScriptRegion;
import com.storyteam.storytelling.util.LevelAABB;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.ApiStatus.Internal;

@SaveOnClose
@Script(
   value = -1301,
   description = "Разделение бэков на регионы"
)
public class S1301BackController extends ScriptInstance {
   public static final int SCRIPT_ID = -1301;
   private static final Set<ScriptRegion> REGIONS = new HashSet<>();
   private static final IntSet IGNORED = new IntOpenHashSet();
   public final ScriptPerformer main = new ScriptPerformerBuilder(this).thenInfiniteSleep().build(this::registerMainPerformer);

   public S1301BackController(ScriptEnvironment env, int id) {
      super(env, id);
   }

   @Internal
   public static void register(ScriptRegion $) {
      REGIONS.add($);
   }

   public static void ignoreScript(int... scripts) {
      IGNORED.addAll(IntSet.of(scripts));
   }

   public static void heedScript(int... scripts) {
      IGNORED.removeAll(IntSet.of(scripts));
   }

   @Override
   public void updateAsPlayer(ServerPlayer player) {
      super.updateAsPlayer(player);
      if (player.tickCount % 20 == 0) {
         IntSet inactive = new IntOpenHashSet();
         IntSet active = new IntOpenHashSet();
         this.gatherScripts(player, inactive, active);
         IntIterator itr = active.iterator();

         while (itr.hasNext()) {
            int script = itr.nextInt();
            if (this.env.getScript(script) == null) {
               this.env.start(script);
            }
         }

         itr = inactive.iterator();

         while (itr.hasNext()) {
            int script = itr.nextInt();
            if (this.env.getScript(script) != null) {
               this.env.stop(script);
            }
         }
      }
   }

   public void gatherScripts(ServerPlayer player, IntSet inactive, IntSet active) {
      IntSet all = new IntOpenHashSet();

      for (ScriptRegion entry : REGIONS) {
         List<LevelAABB> aabbList = entry.getAreas();
         IntArrayList scripts = entry.getScripts();
         all.addAll(scripts);

         for (LevelAABB aabb : aabbList) {
            if (aabb.contains(player)) {
               active.addAll(scripts);
               break;
            }
         }
      }

      active.removeAll(IGNORED);
      all.removeAll(IGNORED);
      all.removeAll(active);
      inactive.addAll(all);
   }
}
