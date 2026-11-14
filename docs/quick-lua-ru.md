# Storytelling Lua: быстрая шпаргалка

Файлы сценариев лежат в `ST/scripts/*.lua`. Мод читает их при входе в мир или после `/script reload 900030.lua` для одного файла. `/script reload` перечитывает все файлы. Для проверки всех файлов введи `/script validate`; для запуска — `/script start ID`. Если меняешь действия уже запущенного сценария, сначала заверши его или начни новый ID: сохранённый шаг сверяется с содержимым файла.

## Минимальный рабочий файл

```lua
story(900000)                 -- уникальный положительный ID
part("main")                 -- обязательная непустая основная часть
speaker(1, "Рассказчик")
speech(1, "Привет!")
wait_f8()                   -- ждать F8 перед следующим действием
```

Одного `story(id)` или объявления `npc_model(...)` недостаточно: без `part("main")` и действия файл не зарегистрируется. Пустой файл с расширением `.lua` даст ошибку в логе.

## Диалог и ветвление

```lua
story(900001)
part("main")
speaker(1, "Проводник")
speech(1, "Пойдёшь со мной?")
wait_f8()
choice("answer", "Как поступим?", "Пойти", "Остаться")
var_if("answer", 1, "go", "stay")

anchor("stay")
speech(1, "Тогда я подожду.")
wait_f8()
goto_anchor("final")

anchor("go")
speech(1, "Идём!")
wait_f8()
```

`choice` показывает кнопки; Escape закрывает окно, после чего можно выбрать номер через чат. `choice_if` принимает таблицы вариантов с `text`, `when`, `equals`, `scope` и скрывает неподходящие ответы. `anchor("name")` обозначает точку перехода, `goto_anchor("final")` завершает сценарий.

## NPC с glTF-моделью

Положи `reg.gltf` (или `.glb`) в `ST/assets/models/` и связанные текстуры туда, где их ожидает модель.

```lua
story(900002)
npc_model("reg", "reg.gltf", "Регир")
npc_hitbox("reg", 0.6, 2.0)
part("main")
speaker_npc(1, "reg")        -- портрет из группы Head модели
npc_spawn("reg", 0.5, -60, 8.5)
npc_look("reg", "player", "auto")
speech(1, "Я здесь.")
wait_f8()
npc_move("reg", 2.5, -60, 8.5, 0.7)
```

Первый аргумент `npc_model`, `npc_hitbox`, `npc_spawn`, `npc_look`, `npc_move` и `speaker_npc` — один и тот же ключ NPC (`"reg"`). `speaker(1, "имя")` ищет портрет по точному совпадению имени; `speaker_npc` надёжнее. Группа головы модели должна называться `Head`.

Можно записать те же действия через объект: `local reg = npc{key="reg", model="reg.gltf", name="Регир", width=0.6, height=2.0}` до `part("main")`, затем `reg:spawn(0.5, -60, 8.5)`, `reg:speaker(1)`, `reg:look("player")`, `reg:move(2.5, -60, 8.5, 0.7)`. Методы создают те же шаги сценария, что старые команды. Полный пример — `900032.lua`.

## Частые команды

| Вызов | Что делает |
| --- | --- |
| `wait_ticks(20)` | Ждёт примерно секунду. |
| `wait_chat("готово")` | Ждёт сообщение игрока. |
| `wait_pos(x, y, z, radius)` | Ждёт приближения к точке. |
| `var_set("flag", true, "world")` | Сохраняет значение для всего мира. Вместо `world` можно `script` или `player`. |
| `var_if("flag", true, "yes", "no", "world")` | Переходит к одному из двух якорей ниже по файлу. |
| `npc_approach_use_block("reg", x, y, z, "pressed")` | NPC подходит и пытается нажать блок; результат пишется в переменную. |
| `npc_animation("reg", "wave", "once")` | Проигрывает клип из glTF. |
| `play_sound("minecraft:block.note_block.pling")` | Воспроизводит звук у игрока. |
| `give_item("minecraft:apple", 1)` | Выдаёт предмет. |
| `spawn_particle_here("minecraft:happy_villager", 20)` | Показывает частицы перед игроком. |
| `camera_shot(x, y, z, yaw, pitch, ticks)` | Показывает временный кадр камерой. |
| `camera_fly_npc("reg", 40, 40, 1.6)` | Подводит камеру к NPC, держит кадр и возвращает к игроку. |
| `quest_create("intro", "Встреча", "Найдите проводника")` | Объявляет квест в мире. |
| `quest_task("intro", "talk", "Поговорить")` | Добавляет ручную задачу. |
| `quest_start("intro")` | Активирует квест. |
| `quest_task_complete("intro", "talk")` | Отмечает задачу выполненной. |
| `quest_complete("intro")` | Завершает квест. |

Журнал открывается командой `/quest`. Состояние квестов общее для мира и сохраняется при выходе. Пример — `900033.lua`.

В одиночном мире команда `/storyedit` открывает редактор `ST/scripts/*.lua`. Кнопка «Проверить» разбирает файл без запуска, «Сохранить» делает резервную копию `.bak` и перезагружает один сценарий. Пока доступны файлы с числовыми именами; изменённый активный сценарий может потребовать отдельного перезапуска.

Команды автора: `/script list`, `/script validate`, `/script reload`, `/script start ID`, `/script active`, `/script inspect ID`, `/script goto ID anchor`, `/script stop ID`. Полное описание аргументов и ограничений смотри в `docs/story-packs.md` репозитория. Рабочие примеры лежат в `examples/story_scripts/`.
