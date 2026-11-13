story(900028)

part("main")
speaker(1, "Система")
var_set("has_key", false)
choice_if("route", "Первый ответ скрыт. Введи 1: выбор не должен сработать. Затем выбери 2:",
    { text = "Открыть дверь", when = "has_key", equals = "true" },
    { text = "Уйти" })
var_if("route", 2, "correct", "wrong")
anchor("wrong")
speech(1, "Ошибка: скрытый ответ был принят.")
goto_anchor("final")
anchor("correct")
speech(1, "Скрытый ответ не был принят; выбор 2 сработал.")
wait_f8()
