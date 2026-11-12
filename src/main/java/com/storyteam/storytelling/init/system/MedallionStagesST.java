package com.storyteam.storytelling.init.system;

import com.storyteam.storytelling.system.tribute.data.medallion.MedallionEmotion;
import com.storyteam.storytelling.system.tribute.data.medallion.MedallionStage;
import java.util.List;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;

@SimplyRegister
public interface MedallionStagesST {
   @RegistryName("closed")
   MedallionStage CLOSED = MedallionStage.builder().build();
   @RegistryName("partially_open")
   MedallionStage PARTIALLY_OPEN = MedallionStage.builder().build();
   @RegistryName("opened")
   MedallionStage OPENED = MedallionStage.builder()
      .supportedEmotions(List.of(MedallionEmotion.CALM, MedallionEmotion.SERIOUS, MedallionEmotion.FROWN, MedallionEmotion.AGGRESSIVE))
      .build();
}
