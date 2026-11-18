story(900057)

local tester = npc{key="door_tester", model="reg.gltf", name="Проверяющий дверь", width=0.6, height=2.0}

part("main")
speaker(1, "Система")
tester:spawn(1.5, -60, 9.5)
speech(1, "Поставьте деревянную дверь нижним блоком в 3 -59 9. Нажмите F8.")
wait_f8()

tester:set_door(3, -59, 9, true)
tester:set_door(3, -59, 9, true)
speech(1, "Дверь открыта, повтор шага не закрыл её. Нажмите F8.")
wait_f8()

tester:set_door(3, -59, 9, false)
speech(1, "Дверь закрыта.")
wait_f8()
tester:remove()
