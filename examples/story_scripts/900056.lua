story(900056)

local old_guide = npc{key="old_guide", type="minecraft:villager", name="Проводник"}
local new_guide = npc{key="new_guide", type="minecraft:villager", name="Проводник"}

part("main")
speaker(1, "Система")
old_guide:state_set("met", "yes")
speech(1, "Старый ключ NPC записан. Нажмите F8 для переноса состояния.")
wait_f8()

npc_state_migrate("old_guide", "new_guide")
new_guide:state_if("met", "yes", "success", "broken")

anchor("success")
speech(1, "Состояние NPC перенесено на новый ключ.")
wait_f8()
goto_anchor("ending")

anchor("broken")
speech(1, "Перенос состояния NPC не прошёл.")
wait_f8()

anchor("ending")
