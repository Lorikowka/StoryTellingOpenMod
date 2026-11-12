package com.storyteam.storytelling.compat.iris;

import com.mojang.blaze3d.systems.RenderSystem;
import com.storyteam.storytelling.compat.base.BaseCompatST;
import java.io.IOException;
import lombok.Generated;
import net.irisshaders.iris.Iris;
import net.irisshaders.iris.config.IrisConfig;
import net.minecraftforge.eventbus.api.IEventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeith.hammerlib.compat.base.BaseCompat.LoadCompat;

@LoadCompat(
   compatType = BaseCompatST.class,
   modid = "oculus"
)
public class IrisCompat extends BaseCompatST implements IrisProxy {
   @Generated
   private static final Logger log = LoggerFactory.getLogger(IrisCompat.class);

   @Override
   public void construct(IEventBus modBus, IEventBus gameBus) {
      IrisProxy.Instance.instance = this;
   }

   @Override
   public String getCurrentShader() {
      return (String)Iris.getIrisConfig().getShaderPackName().orElse(null);
   }

   @Override
   public void changeShader(String shaderZipFileName) {
      RenderSystem.recordRenderCall(() -> {
         try {
            IrisConfig cfg = Iris.getIrisConfig();
            cfg.setShaderPackName(shaderZipFileName);
            cfg.save();
            Iris.reload();
         } catch (IOException var2) {
            log.error("Failed to switch shaders", var2);
         }
      });
   }
}
