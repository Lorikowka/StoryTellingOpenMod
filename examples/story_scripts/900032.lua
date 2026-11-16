story(900032)
local reg = npc{ key = "reg", model = "reg.gltf", name = "Регир", width = 0.6, height = 2.0 }

part("main")
reg:spawn(0.5, -59, 8.5)
reg:speaker(1)
reg:look("player", "auto")
speech(1, "Теперь я объявлен одним объектом Lua.")
wait_f8()
reg:camera(40, 20, 1.6)
reg:move(5.5, -60, 9.5, 0.7)
speech(1, "Те же действия работают через методы NPC.")
wait_f8()
