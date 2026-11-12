package com.storyteam.storytelling.init.system;

import com.storyteam.storytelling.system.tribute.data.offer.TributeOffer;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;

@SimplyRegister
public interface TributeOffersST {
   @RegistryName("first")
   TributeOffer FIRST = TributeOffer.builder().requiredCount(20).daysToSubmit(1).nextOfferId("second").build();
   @RegistryName("second")
   TributeOffer SECOND = TributeOffer.builder().requiredCount(2000).daysToSubmit(10).build();
}
