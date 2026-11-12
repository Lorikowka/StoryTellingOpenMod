package com.storyteam.storytelling.system.simulator.me_tab.fragment;

import com.storyteam.storytelling.system.simulator.me_tab.fragment.data.FragmentData;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.data.FragmentInstance;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.data.FragmentState;

public class FragmentActions {
   public static void give(FragmentData data) {
      FragmentInstance inst = FragmentInstanceManager.getActive(data);
      if (inst != null) {
         inst.setState(FragmentState.GIVEN);
      }
   }

   public static void startThinking(FragmentData data) {
      FragmentInstance inst = FragmentInstanceManager.getActive(data);
      if (inst != null) {
         inst.setState(FragmentState.THINKING);
      }
   }

   public static void ready(FragmentData data) {
      FragmentInstance inst = FragmentInstanceManager.getActive(data);
      if (inst != null) {
         inst.setState(FragmentState.READY);
      }
   }
}
