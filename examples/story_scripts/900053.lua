local guide = require("characters.guide")

story(900053)
local first = npc_instance(guide, "guide_a", "Первый проводник")
local second = npc_instance(guide, "guide_b", "Второй проводник")

part("main")
first:spawn(0.5, -60, 8.5)
second:spawn(3.5, -60, 8.5)
first:speaker(1)
speech(1, "Мы созданы из одного шаблона, но у нас разные ключи.")
wait_f8()

