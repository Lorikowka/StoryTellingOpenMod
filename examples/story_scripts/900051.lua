local guide = require("characters.guide")

story(900051)
local regir = npc(guide)

part("main")
regir:spawn(0.5, -60, 8.5)
regir:speaker(1)
speech(1, "Мои параметры загружены из библиотеки персонажей.")
wait_f8()

