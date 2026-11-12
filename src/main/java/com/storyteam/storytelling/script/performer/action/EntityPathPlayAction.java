package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.events.listeners.ScriptIO;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.entity.EntityState;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.util.ToStringBuilder;
import com.storyteam.storytelling.util.world.Location;
import java.io.File;
import java.util.LinkedList;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;
import org.zeith.hammerlib.util.mcf.Resources;

public class EntityPathPlayAction<T extends Mob> extends EntityPerformerAction<T> {
   private LinkedList<Location> positions;
   private LinkedList<Vec3> subPositions;
   private Location next;
   private final String name;
   private boolean started = false;
   private boolean completed = false;
   private float speedMultiplier = 1.0F;
   private int ticks = 0;
   private Vec3 remainingMove = Vec3.ZERO;
   private Vec3 lastPoint = null;
   private Location nextPoint = null;
   private int ticksInTick = 2;

   public EntityPathPlayAction(ScriptPerformer performer, Character<T> character, String name, float speedMultiplier) {
      super(performer, character);
      this.name = name;
      this.speedMultiplier = speedMultiplier;
   }

   @Override
   public void tick() {
      if (!this.started) {
         this.positions = new LinkedList<>();
         CompoundTag nbt = ScriptIO.read(new File(ScriptIO.WORLD_PATH.toFile(), "paths/" + this.name + ".dat"));
         nbt.getList("positions", 10)
            .forEach(
               nbtBase -> {
                  CompoundTag nbt1 = (CompoundTag)nbtBase;
                  this.positions
                     .add(
                        new Location(
                           ResourceKey.create(Registries.DIMENSION, Resources.location(nbt1.getString("world"))),
                           nbt1.getDouble("x"),
                           nbt1.getDouble("y"),
                           nbt1.getDouble("z"),
                           nbt1.getFloat("yaw"),
                           nbt1.getFloat("pitch")
                        )
                     );
               }
            );
         this.started = true;
         this.ticks = 0;
      }

      if (!this.positions.isEmpty()) {
         this.getEntity().getData().setLookTarget(null);
         this.getEntity().optionalValidOnly().ifPresent(e -> {
            Vec3 current = e.position();
            if (this.ticks % this.ticksInTick == 0) {
               this.next = this.positions.poll();
               this.subPositions = new LinkedList<>();
               Vec3 move = this.next.subtract(current);
               Vec3 step = move.scale(1.0 / (double)this.ticksInTick);

               for (int i = 0; i < this.ticksInTick; i++) {
                  this.subPositions.add(step);
               }
            }

            Vec3 move = this.subPositions.poll();
            if (this.next.getDimension() == e.level().dimension()) {
               e.setDeltaMovement(move);
               e.setYRot(this.next.getYaw());
               e.setXRot(this.next.getPitch());
               e.yHeadRot = this.next.getYaw();
               this.ticks++;
            }
         });
      } else {
         this.completed = true;
      }
   }

   @Override
   public void reset() {
      this.started = false;
      this.completed = false;
   }

   @Override
   public boolean isDone() {
      return this.completed;
   }

   @Override
   public void fastForwardToStage() {
      EntityState<T> entity = this.getEntity();
      this.positions = new LinkedList<>();
      CompoundTag nbt = ScriptIO.read(new File(ScriptIO.WORLD_PATH.toFile(), "paths/" + this.name + ".dat"));
      nbt.getList("positions", 10)
         .forEach(
            nbtBase -> {
               CompoundTag nbt1 = (CompoundTag)nbtBase;
               this.positions
                  .add(
                     new Location(
                        ResourceKey.create(Registries.DIMENSION, Resources.location(nbt1.getString("world"))),
                        nbt1.getDouble("x"),
                        nbt1.getDouble("y"),
                        nbt1.getDouble("z"),
                        nbt1.getFloat("yaw"),
                        nbt1.getFloat("pitch")
                     )
                  );
            }
         );
      if (!this.positions.isEmpty()) {
         Location next = this.positions.getLast();
         entity.setPosition(next);
      }

      entity.markForReposition();
   }

   @Override
   public void rewindToStage() {
      this.getEntity().setPosition(null);
   }

   @Override
   public HelpReportBuilder gatherHelpReport(HelpReportBuilder builder) {
      return builder.append("Путь персонажа \"%s\" (%s).", this.character.getName(), this.character.getReferenceId());
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this).append("character", this.character).build();
   }
}
