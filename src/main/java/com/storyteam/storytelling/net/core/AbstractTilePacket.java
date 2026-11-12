package com.storyteam.storytelling.net.core;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.zeith.hammerlib.abstractions.sources.IObjectSource;
import org.zeith.hammerlib.api.lighting.ColoredLightManager;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.PacketContext;
import org.zeith.hammerlib.util.java.ReflectionUtil;

public abstract class AbstractTilePacket<T extends BlockEntity> implements IPacket {
   IObjectSource<?> src;
   Class<T> type;

   public AbstractTilePacket(Class<T> type, T src) {
      this.src = (IObjectSource<?>)IObjectSource.ofTile(src).get();
      this.type = type;
   }

   public AbstractTilePacket() {
   }

   public void write(FriendlyByteBuf buf) {
      buf.writeNbt(IObjectSource.writeSource(this.src));
      buf.writeUtf(this.type.getCanonicalName());
   }

   public void read(FriendlyByteBuf buf) {
      this.src = (IObjectSource<?>)IObjectSource.readSource(buf.readNbt()).orElse(null);
      this.type = ReflectionUtil.fetchClass(buf.readUtf());
   }

   public void clientExecute(PacketContext ctx) {
      Player cp = ColoredLightManager.getClientPlayer();
      if (this.src != null && cp != null) {
         this.src.get(this.type, cp.level()).ifPresent(tile -> this.serverExecute(ctx, (T)tile));
      }
   }

   public void serverExecute(PacketContext ctx) {
      ServerPlayer sp = ctx.getSender();
      if (this.src != null && sp != null) {
         this.src.get(this.type, sp.level()).ifPresent(tile -> this.serverExecute(ctx, (T)tile));
      }
   }

   public boolean executeOnMainThread() {
      return true;
   }

   protected void clientExecute(PacketContext ctx, T tile) {
   }

   protected void serverExecute(PacketContext ctx, T tile) {
   }

   public abstract static class ToClient<T extends BlockEntity> extends AbstractTilePacket<T> {
      public ToClient(Class<T> type, T src) {
         super(type, src);
      }

      public ToClient() {
      }

      @Override
      protected abstract void clientExecute(PacketContext var1, T var2);
   }

   public abstract static class ToServer<T extends BlockEntity> extends AbstractTilePacket<T> {
      public ToServer(Class<T> type, T src) {
         super(type, src);
      }

      public ToServer() {
      }

      @Override
      protected abstract void serverExecute(PacketContext var1, T var2);
   }
}
