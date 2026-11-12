package com.storyteam.storytelling.init;

import com.storyteam.storytelling.content.commands.arguments.ActiveScriptArgument;
import com.storyteam.storytelling.content.commands.arguments.CharacterArgument;
import com.storyteam.storytelling.content.commands.arguments.CloudStructureFileArgument;
import com.storyteam.storytelling.content.commands.arguments.EntityReferenceArgument;
import com.storyteam.storytelling.content.commands.arguments.ScriptIdArgument;
import com.storyteam.storytelling.content.commands.arguments.ScriptTriggerArgument;
import com.storyteam.storytelling.content.commands.arguments.StructureFileArgument;
import com.storyteam.storytelling.system.simulator.main_page.arguments.MainTabTypeArgument;
import com.storyteam.storytelling.system.weather.WeatherCommand;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;

@SimplyRegister
public interface CommandArgumentsST {
   @RegistryName("any_script")
   ArgumentTypeInfo<?, ?> ANY_SCRIPT_ID = ArgumentTypeInfos.registerByClass(ScriptIdArgument.class, SingletonArgumentInfo.contextFree(ScriptIdArgument::script));
   @RegistryName("active_script")
   ArgumentTypeInfo<?, ?> ACTIVE_SCRIPT_ID = ArgumentTypeInfos.registerByClass(
      ActiveScriptArgument.class, SingletonArgumentInfo.contextFree(ActiveScriptArgument::new)
   );
   @RegistryName("script_trigger")
   ArgumentTypeInfo<?, ?> SCRIPT_TRIGGER = ArgumentTypeInfos.registerByClass(
      ScriptTriggerArgument.class, SingletonArgumentInfo.contextFree(ScriptTriggerArgument::trigger)
   );
   @RegistryName("entity_reference")
   ArgumentTypeInfo<?, ?> ENTITY_REFERENCE = ArgumentTypeInfos.registerByClass(
      EntityReferenceArgument.class, SingletonArgumentInfo.contextFree(EntityReferenceArgument::reference)
   );
   @RegistryName("structure_file")
   ArgumentTypeInfo<?, ?> STRUCTURE_FILE = ArgumentTypeInfos.registerByClass(
      StructureFileArgument.class, SingletonArgumentInfo.contextFree(StructureFileArgument::structure)
   );
   @RegistryName("cloud_structure_file")
   ArgumentTypeInfo<?, ?> CLOUD_STRUCTURE_FILE = ArgumentTypeInfos.registerByClass(
      CloudStructureFileArgument.class, SingletonArgumentInfo.contextFree(CloudStructureFileArgument::cloudStructure)
   );
   @RegistryName("character")
   ArgumentTypeInfo<?, ?> CHARACTER = ArgumentTypeInfos.registerByClass(
      CharacterArgument.class, SingletonArgumentInfo.contextFree(CharacterArgument::character)
   );
   @RegistryName("weather")
   ArgumentTypeInfo<?, ?> WEATHER = ArgumentTypeInfos.registerByClass(
      WeatherCommand.WeatherArgument.class, SingletonArgumentInfo.contextFree(WeatherCommand.WeatherArgument::weather)
   );
   @RegistryName("main_tab_type")
   ArgumentTypeInfo<?, ?> MAIN_TAB_TYPE = ArgumentTypeInfos.registerByClass(
      MainTabTypeArgument.class, SingletonArgumentInfo.contextFree(MainTabTypeArgument::type)
   );
}
