package com.storyteam.storytelling.init.system;

import com.storyteam.storytelling.system.riddles.data.RiddlesDef;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;

@SimplyRegister(
   prefix = "system/riddles/"
)
public interface RiddlesST {
   @RegistryName("s3_head")
   RiddlesDef S3_HEAD = RiddlesDef.builder()
      .coordinate("/execute in minecraft:overworld run tp @s -68.50 103.00 -453.53 -90.00 0.00")
      .text("Когда в ней мысли бьют ключом,\nБудет тебе всё нипочём.\nА если пусто в ней совсем,\nЖди от судьбы одних проблем.\n")
      .answer("голова")
      .reward(10)
      .build();
}
