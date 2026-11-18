story(900058)

local tester = npc{key="trapdoor_tester_model", model="reg.gltf", name="Проверяющий люк", width=0.6, height=2.0}

part("main")
speaker(1, "Система")
tester:spawn(2.5, -60, 9.5)
speech(1, "Поставьте деревянный люк в 4 -59 9. Нажмите F8.")
wait_f8()

tester:set_trapdoor(4, -59, 9, true)
tester:set_trapdoor(4, -59, 9, true)
speech(1, "Люк открыт, повтор шага не закрыл его. Нажмите F8.")
wait_f8()

tester:set_trapdoor(4, -59, 9, false)
speech(1, "Люк закрыт.")
wait_f8()
tester:remove()
