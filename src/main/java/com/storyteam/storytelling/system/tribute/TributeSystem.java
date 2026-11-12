package com.storyteam.storytelling.system.tribute;

import com.storyteam.storytelling.system._sysofsys.ISystem;
import com.storyteam.storytelling.system._sysofsys.SystemRoot;
import com.storyteam.storytelling.system.tribute.command.TributeSubCommand;
import lombok.Generated;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;

@SystemRoot("tribute")
public class TributeSystem implements ISystem {
   public static final String FRAME_PATH = "frame/%s/";
   public static final String EMOTION_PATH = "emotion/%s_frame";
   private static TributeSystem instance;

   public TributeSystem() {
      instance = this;
   }

   public static ResourceLocation tx(String path) {
      return getInstance().pngTex(path);
   }

   public static ResourceLocation category(String category, String path) {
      return getInstance().categoryId(category, path);
   }

   public static ResourceLocation guiTx(String path) {
      return getInstance().guiTex(path);
   }

   public static ResourceLocation medallionTx(String path) {
      return guiTx("medallion/" + path);
   }

   public static ResourceLocation frameTx(String frameId, String path) {
      return medallionTx(framePath(frameId) + path);
   }

   public static String framePath(String frameId) {
      return "frame/%s/".formatted(frameId);
   }

   @Override
   public void subscribe(IEventBus gameBus, IEventBus modBus) {
      gameBus.register(TributeSubCommand.class);
   }

   @Generated
   private static TributeSystem getInstance() {
      return instance;
   }
}
