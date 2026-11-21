-- The flag branch waits while main performs a separate timer.
story(900064)
part("main")
speaker(1, "Система")
speech(1, "После F8 начнутся два независимых ожидания.")
wait_f8()
parallel_start("timer")
parallel_start("gate")
wait_ticks(80)
var_set("ready", true)
parallel_join("timer", "gate")
speech(1, "Таймер и проверка флага завершились.")
wait_f8()

part("timer")
wait_ticks(200)

part("gate")
wait_flag("lua.var.ready")
