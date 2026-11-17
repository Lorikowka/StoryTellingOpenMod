local guide = require("characters.guide")

story(900054)
local regir = npc_instance(guide, "persistent_guide", "Регир")

part("main")
regir:spawn(0.5, -60, 8.5)
regir:speaker(1)
regir:state_if("met", "yes", "returning", "first")

anchor("first")
speech(1, "Мы встретились впервые. Я запомню это после перезахода.")
regir:state_set("met", "yes")
wait_f8()
goto_anchor("final")

anchor("returning")
speech(1, "Я помню нашу прошлую встречу.")
wait_f8()

