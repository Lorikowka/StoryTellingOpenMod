story(900027)

part("main")
speaker(1, "Система")
speech(1, "После F8 частицы появятся прямо перед тобой. Посмотри вперёд.")
wait_f8()
spawn_particle_here("minecraft:happy_villager", 80)
speech(1, "Частицы отправлены прямо перед игроком.")
wait_f8()
