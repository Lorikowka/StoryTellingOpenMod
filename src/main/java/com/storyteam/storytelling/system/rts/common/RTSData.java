package com.storyteam.storytelling.system.rts.common;

import org.zeith.hammerlib.api.io.IAutoNBTSerializable;
import org.zeith.hammerlib.api.io.NBTSerializable;

public class RTSData implements IAutoNBTSerializable {
   @NBTSerializable
   public int foodTicker;
   private static final RTSData rtsData = new RTSData();

   public static RTSData get() {
      return rtsData;
   }
}
