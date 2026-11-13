story(900026)

part("main")
speaker(1, "Система")
var_set("has_key", true)
choice_if("route", "Выберите путь:",
    { text = "Открыть дверь", when = "has_key", equals = "true" },
    { text = "Уйти" })
var_if("route", 1, "open", "leave")
anchor("open")
speech(1, "Дверь открыта: условный ответ сработал.")
goto_anchor("final")
anchor("leave")
speech(1, "Вы решили уйти.")
wait_f8()
