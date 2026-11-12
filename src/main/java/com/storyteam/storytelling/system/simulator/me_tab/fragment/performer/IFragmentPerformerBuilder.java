package com.storyteam.storytelling.system.simulator.me_tab.fragment.performer;

import com.storyteam.storytelling.script.performer.action.HandleStateAction;
import com.storyteam.storytelling.script.performer.builder.IScriptPerformerBuilderExt;
import com.storyteam.storytelling.script.performer.builder.ScriptPerformerBuilder;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.FragmentInstanceManager;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.data.FragmentData;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.data.FragmentInstance;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.data.FragmentState;

public interface IFragmentPerformerBuilder extends IScriptPerformerBuilderExt {
   default ScriptPerformerBuilder activateFragment(final FragmentData fragment) {
      return this.self().thenHandleState(new HandleStateAction.StateHandler() {
         @Override
         public void onPassed() {
            FragmentInstanceManager.addActive(fragment);
         }

         @Override
         public void onRewind() {
            FragmentInstanceManager.removeActive(fragment);
         }

         @Override
         public void globalFastForward() {
            this.onPassed();
         }
      });
   }

   default ScriptPerformerBuilder deactivateFragment(final FragmentData fragment) {
      return this.self().thenHandleState(new HandleStateAction.StateHandler() {
         @Override
         public void onPassed() {
            FragmentInstanceManager.removeActive(fragment);
         }

         @Override
         public void onRewind() {
            this.onPassed();
         }

         @Override
         public void globalFastForward() {
            this.onPassed();
         }
      });
   }

   default ScriptPerformerBuilder setFragmentState(final FragmentData fragment, final FragmentState state) {
      return this.self().thenHandleState(new HandleStateAction.StateHandler() {
         @Override
         public void onPassed() {
            FragmentInstance fragmentInstance = FragmentInstanceManager.getActive(fragment);
            if (fragmentInstance != null) {
               fragmentInstance.setState(state);
            }
         }

         @Override
         public void onRewind() {
            this.onPassed();
         }

         @Override
         public void globalFastForward() {
            this.onPassed();
         }
      });
   }
}
