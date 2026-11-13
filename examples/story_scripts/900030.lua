-- Контрольная глава для тестового мира у рычага 1 -59 9.
-- Выйдите в меню на экране выбора, затем вернитесь: выбор должен открыться снова.
story(900030)
npc_model("guide", "reg.gltf", "Регир")
npc_hitbox("guide", 0.6, 2.0)

part("main")
speaker_npc(1, "guide")
var_if("chapter_900030_done", true, "already_done", "first_run", "world")

anchor("already_done")
speech(1, "Эту главу мы уже прошли. Сохранённый флаг мира работает.")
wait_f8()
goto_anchor("final")

anchor("first_run")
npc_spawn("guide", -3.5, -60, 8.5)
npc_look("guide", "player", "auto")
speech(1, "Я Регир. Выбери, пойдём ли мы вместе.")
wait_f8()
choice("route", "Как поступим?", "Пойти вместе", "Остаться")
var_if("route", 1, "follow", "stay")

anchor("stay")
speech(1, "Хорошо, тогда я останусь здесь.")
wait_f8()
var_set("chapter_900030_done", true, "world")
goto_anchor("final")

anchor("follow")
speech(1, "Тогда идём к точке у рычага.")
wait_f8()
npc_move("guide", 0.5, -60, 8.5, 0.7)
speech(1, "Мы дошли. Теперь этот выбор и маршрут сохранены.")
wait_f8()
var_set("chapter_900030_done", true, "world")
