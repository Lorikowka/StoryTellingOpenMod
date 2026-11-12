package com.storyteam.storytelling.events.base;

import com.mojang.brigadier.builder.ArgumentBuilder;
import java.util.function.Consumer;
import lombok.Generated;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.eventbus.api.Event;

public class GatherSTSubcommandsEvent extends Event {
   final CommandBuildContext context;
   final Consumer<ArgumentBuilder<CommandSourceStack, ?>> register;

   public GatherSTSubcommandsEvent(CommandBuildContext context, Consumer<ArgumentBuilder<CommandSourceStack, ?>> register) {
      this.context = context;
      this.register = register;
   }

   public void register(ArgumentBuilder<CommandSourceStack, ?> command) {
      this.register.accept(command);
   }

   @Generated
   public CommandBuildContext getContext() {
      return this.context;
   }
}
