package com.storyteam.storytelling.util.data.buffered;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import org.zeith.hammeranims.api.animsys.ConfiguredAnimation;

public class AnimationListBuffer extends BufferedData<Map<String, ConfiguredAnimation>> {
   HashSet<String> strings = new HashSet<>();

   public AnimationListBuffer() {
      super(new HashMap<>());
   }

   public ConfiguredAnimation put(String string, ConfiguredAnimation animation) {
      this.strings.add(string);
      return this.value.put(string, animation);
   }

   public ConfiguredAnimation remove(String string) {
      this.strings.add(string);
      return this.value.remove(string);
   }

   public void clear() {
      this.strings.addAll(this.value.keySet());
      this.value.clear();
   }

   public ConfiguredAnimation get(String string) {
      this.strings.remove(string);
      return this.value.get(string);
   }

   private void check(String string) {
      this.isChange = !this.strings.isEmpty();
   }
}
