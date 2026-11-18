-- Во время движения выполните /script goto 900062 after_route.
-- NPC должен остановиться там, где находится, без переноса к цели.
story(900062)
local reg = npc{key = "jump_tester", model = "reg.gltf", name = "Регир", width = 0.6, height = 2.0}

part("main")
reg:spawn(0.5, -59, 8.5)
reg:speaker(1)
speech(1, "Нажми F8. Пока я иду, введи /script goto 900062 after_route.")
wait_f8()
reg:move(5.5, -60, 9.5, 0.1)
anchor("after_route")
speech(1, "Переход к якорю выполнен; я должен был остановиться на месте.")
wait_f8()
