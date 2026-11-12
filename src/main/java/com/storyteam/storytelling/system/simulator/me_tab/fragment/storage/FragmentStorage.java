package com.storyteam.storytelling.system.simulator.me_tab.fragment.storage;

import com.storyteam.storytelling.system.simulator.me_tab.fragment.data.FragmentData;
import java.util.HashMap;
import java.util.Map;

public class FragmentStorage {
   public static Map<String, FragmentData> FRAGMENTS_MAP = new HashMap<>();

   public static FragmentData register(FragmentData data) {
      FRAGMENTS_MAP.put(data.getId(), data);
      return data;
   }
}
