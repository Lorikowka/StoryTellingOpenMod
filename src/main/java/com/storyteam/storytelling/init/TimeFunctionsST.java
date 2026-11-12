package com.storyteam.storytelling.init;

import com.storyteam.storytelling.system.time_function_anims.ClampedTimeFunction;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;

@SimplyRegister
public interface TimeFunctionsST {
   @RegistryName("clamped")
   ClampedTimeFunction CLAMPED_FUNCTION = new ClampedTimeFunction();
}
