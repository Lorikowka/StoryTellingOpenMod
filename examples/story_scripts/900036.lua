-- Проверка журнала: три задания, затем три разных состояния.
-- Повторный запуск возвращает все три задания в «Текущие».
story(900036)

part("main")
speaker(0, "Проверка журнала")

quest_create("journal_test_route", "Проверить маршрут", "Первое тестовое задание")
quest_source("journal_test_route", "Тест журнала")
quest_task("journal_test_route", "inspect", "Осмотреть путь")
quest_start("journal_test_route")

quest_create("journal_test_map", "Изучить карту", "Второе тестовое задание")
quest_source("journal_test_map", "Тест журнала")
quest_task("journal_test_map", "read", "Прочитать карту")
quest_start("journal_test_map")

quest_create("journal_test_signal", "Найти сигнал", "Третье тестовое задание")
quest_source("journal_test_signal", "Тест журнала")
quest_task("journal_test_signal", "listen", "Прислушаться")
quest_start("journal_test_signal")

speech(0, "Откройте журнал клавишей J. В текущих заданиях должны быть три пункта. Переключите их и выберите «Изучить карту».")
wait_f8()

quest_task_complete("journal_test_map", "read")
quest_complete("journal_test_map")
speech(0, "«Изучить карту» теперь в готовых. Проверьте вкладки и снова нажмите F8.")
wait_f8()

quest_fail("journal_test_signal")
speech(0, "«Найти сигнал» теперь в проваленных. Проверьте все три вкладки и снова нажмите F8.")
wait_f8()

quest_task_complete("journal_test_route", "inspect")
quest_complete("journal_test_route")
speech(0, "Проверка окончена. Откройте J: два задания в готовых, одно в проваленных. Закройте и откройте журнал: выбор должен сохраниться.")
wait_f8()
