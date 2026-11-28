# TellingEngine — Forge MDK

Сюжетный движок TellingEngine для Minecraft 1.20.1 и Forge 47.4.28. Технический mod ID текущей версии — `storytelling`: он сохранён для совместимости с существующими мирами до проверки миграции зарегистрированных блоков и сущностей.

- `src/main/java/com/storyteam/storytelling` — Java-код мода.
- `src/main/resources` — ресурсы, данные и метаданные мода.
- `libs` — локальные JAR-файлы. Сборка использует HammerLib, HammerAnimations и GFBS: glTF; старые файлы HammerMultipart, HammerMicroblocks и WorldEdit в зависимости не включены.
- `gradle`, `gradlew`, `gradlew.bat`, `build.gradle` и `gradle.properties` — конфигурация Gradle Wrapper и ForgeGradle.
- `build` и `.gradle` — генерируемые файлы, не исходный код.

Для сборки требуется JDK 17. В PowerShell из этой папки:

```powershell
$env:JAVA_HOME = 'C:\Program Files\Java\jdk-17'
.\gradlew.bat build
```

При первом запуске Gradle и Forge загружают компоненты. Текущая версия исходников — `20.1.34-update-lua-alpha.96`. Для игры нужны TellingEngine, HammerLib, HammerAnimations и GFBS: glTF; версии обязательных зависимостей указаны в `src/main/resources/META-INF/mods.toml`. Портрет NPC из группы `Head` создаётся из glTF-модели и кэшируется. Публичная бета ещё не выпущена: необходимые проверки перечислены в [плане](docs/engine-expansion-plan.md).

Зависимости устанавливаются отдельными JAR-файлами: [HammerLib 20.1.50](https://www.curseforge.com/minecraft/mc-mods/hammer-lib/files/6337292), [HammerAnimations 20.1.57](https://www.curseforge.com/minecraft/mc-mods/hammer-animations) и [GFBS: glTF 1.5.1](https://www.curseforge.com/minecraft/mc-mods/gfbs-gltf/files/9511785). HammerLib и HammerAnimations запрещают встраивать их классы в JAR другого мода; сборка TellingEngine их не включает.

Базовое состояние и проведённые изменения: [отчёт alpha.66](docs/progress-report-alpha66.md). Дальнейшие шаги: [актуальный план развития](docs/engine-expansion-plan.md). Формат новых пакетов: [Story Pack API 1](docs/story-pack-format.md). Повторное создание NPC: [шаблоны и экземпляры](docs/lua-npc-instances.md). Области переменных: [долговременное состояние](docs/lua-persistent-state.md).

При новой установке Lua-сценарии находятся в папке игры `TellingEngine/scripts`, а GLB-модели NPC — в `TellingEngine/assets/models`. Если уже существует папка `ST`, мод продолжает использовать `ST/scripts` и `ST/assets/models` без переноса или удаления файлов. Подробности: [docs/story-packs.md](docs/story-packs.md).

В родительском каталоге находятся прежние материалы исследования и отдельный экспериментальный `storytelling_rewrite`; они не входят в сборку этого проекта.







