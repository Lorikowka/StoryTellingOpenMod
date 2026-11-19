-- Parallel waiting branches: both timers continue across a world reload.
story(900063)
part("main")
speaker(1, "Система")
speech(1, "Два таймера запустятся после F8.")
wait_f8()
parallel_start("short_wait")
parallel_start("long_wait")
parallel_join("short_wait", "long_wait")
speech(1, "Оба таймера завершились.")
wait_f8()

part("short_wait")
wait_ticks(200)

part("long_wait")
wait_ticks(600)
