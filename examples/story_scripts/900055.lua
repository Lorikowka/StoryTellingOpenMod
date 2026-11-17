story(900055)

part("main")
speaker(1, "Система")
var_set("kept", "yes", "script")
var_set("phase", "opening", "scene")
speech(1, "Первая сцена. Нажмите F8 для перехода.")
wait_f8()

scene_reset()
var_if("phase", "opening", "broken", "cleared", "scene")
anchor("cleared")
var_if("kept", "yes", "success", "broken", "script")

anchor("success")
speech(1, "Сцена очищена, значение сценария сохранилось.")
wait_f8()
goto_anchor("ending")

anchor("broken")
speech(1, "Проверка состояния не прошла.")
wait_f8()

anchor("ending")
