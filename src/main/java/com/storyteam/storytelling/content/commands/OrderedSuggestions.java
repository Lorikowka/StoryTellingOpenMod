package com.storyteam.storytelling.content.commands;

import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import java.util.List;

public class OrderedSuggestions extends Suggestions {
   public OrderedSuggestions(StringRange range, List<Suggestion> suggestions) {
      super(range, suggestions);
   }
}
