package com.storyteam.storytelling.system._core.structio.structure;

import com.mojang.authlib.GameProfile;
import com.storyteam.storytelling.logging.LoggingController;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import org.zeith.hammerlib.api.io.IAutoNBTSerializable;
import org.zeith.hammerlib.api.io.NBTSerializable;

public class StructureMetadata implements IAutoNBTSerializable {
   @NBTSerializable("version")
   public int structioVersion = 200;
   @NBTSerializable
   public long timestamp;
   @NBTSerializable
   public int revision;
   @NBTSerializable
   public String author;

   public void prepare(MinecraftServer server) {
      this.revision = 1;
      this.structioVersion = 200;
      this.timestamp = System.currentTimeMillis();
      this.author = server.getPlayerList()
         .getPlayers()
         .stream()
         .map(Player::getGameProfile)
         .<String>map(GameProfile::getName)
         .findFirst()
         .orElseGet(LoggingController::resolveName);
   }
}
