-- Проверка освобождения камеры при выходе в меню во время сцены.
-- Запустите /script start 900060, нажмите F8 и выйдите в меню,
-- пока камера остаётся у NPC. Затем вернитесь в мир.
story(900060)
npc_model("camera_tester", "reg.gltf", "Регир")
npc_hitbox("camera_tester", 0.6, 2.0)

part("main")
npc_spawn("camera_tester", 0.5, -59, 8.5)
speaker_npc(1, "camera_tester")
speech(1, "Нажми F8. Пока камера смотрит на меня, выйди в меню и вернись в мир.")
wait_f8()
camera_fly_npc("camera_tester", 60, 600, 1.6)
speech(1, "Камера освободилась и сценарий продолжился.")
wait_f8()
