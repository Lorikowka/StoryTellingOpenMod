package com.storyteam.storytelling.system.lockpicking.data;

import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.system.lockpicking.event.LockEvent;
import com.storyteam.storytelling.system.lockpicking.net.PacketCloseLockpicking;
import com.storyteam.storytelling.system.lockpicking.net.PacketUpdateLockpicking;
import com.storyteam.storytelling.system.sounds.SoundUtils;
import java.util.Arrays;
import java.util.stream.Stream;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.zeith.hammerlib.api.io.IAutoNBTSerializable;
import org.zeith.hammerlib.api.io.NBTSerializable;
import org.zeith.hammerlib.net.Network;

public class LockRegistryEntry implements IAutoNBTSerializable {
   public LockEntry entry;
   @NBTSerializable
   public GlobalPos root;
   @NBTSerializable
   private boolean[] progress = new boolean[0];
   @NBTSerializable
   private int unlocked = -1;

   public LockRegistryEntry(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   public LockRegistryEntry(LockEntry entry) {
      this.progress = new boolean[entry.getNotches()];
      this.entry = entry;
      this.root = entry.getPos();
   }

   public Stream<GlobalPos> positions() {
      ResourceKey<Level> level = this.entry.getPos().dimension();
      return Stream.concat(Stream.of(this.entry.getPos().pos()), Arrays.stream(this.entry.getTotal())).map(pos -> GlobalPos.of(level, pos));
   }

   public GlobalPos getPos() {
      return this.root;
   }

   public void progress(ServerPlayer sender, int index) {
      boolean[] progress = this.getProgress();
      if (index == -1) {
         this.damageLockPick(sender, 0.3F);
      }

      if (index >= 0 && index < progress.length) {
         progress[index] = true;
         this.unlocked = -1;
         if (sender != null) {
            Network.sendTo(sender, new PacketUpdateLockpicking(this.root, this));
            this.damageLockPick(sender, 0.3F);
            if (this.isUnlocked()) {
               SoundUtils.playVolumedSound(sender, SoundEvents.ARMOR_EQUIP_NETHERITE);
               LockEvent.Unlock evt = new LockEvent.Unlock(sender, this);
               Storytelling.BUS.post(evt);
               IStoryServer.get().postStoryEvent(EventType.UNLOCK_LOCK, evt);
               Network.sendTo(sender, new PacketCloseLockpicking());
            }
         }
      }
   }

   private void damageLockPick(ServerPlayer player, float chance) {
      if (!(player.getRandom().nextFloat() > chance)) {
         ItemStack itemStack = player.getItemInHand(InteractionHand.MAIN_HAND);
         itemStack.shrink(1);
         if (itemStack.isEmpty()) {
            itemStack = ItemStack.EMPTY;
            Network.sendTo(player, new PacketCloseLockpicking());
         }

         player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);
      }
   }

   public void unlock() {
      this.unlocked = 1;
      Arrays.fill(this.getProgress(), true);
   }

   public void reset() {
      this.unlocked = 0;
      Arrays.fill(this.getProgress(), false);
   }

   public boolean[] getProgress() {
      int nc = this.entry.getNotches();
      if (this.progress == null) {
         this.progress = new boolean[nc];
      } else if (nc != this.progress.length) {
         boolean[] np = new boolean[nc];
         System.arraycopy(this.progress, 0, np, 0, Math.min(this.progress.length, nc));
         this.progress = np;
      }

      return this.progress;
   }

   public boolean isUnlocked() {
      if (this.unlocked == -1) {
         this.unlocked = 1;

         for (boolean b : this.getProgress()) {
            if (!b) {
               this.unlocked = 0;
               return false;
            }
         }
      }

      return this.unlocked == 1;
   }
}
