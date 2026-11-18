# Storytelling — Forge MDK

Основной проект мода Storytelling для Minecraft 1.20.1 и Forge 47.4.28.

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

При первом запуске Gradle и Forge загружают компоненты. Версия исходников этой ветки и рабочего MDK — `20.1.34-update-lua-alpha.80`. Для игры нужны Storytelling, HammerLib, HammerAnimations и GFBS: glTF; версии обязательных зависимостей указаны в `src/main/resources/META-INF/mods.toml`. Портрет NPC из группы `Head` создаётся из glTF-модели и кэшируется.

Базовое состояние и проведённые изменения: [отчёт alpha.66](docs/progress-report-alpha66.md). Дальнейшие шаги: [актуальный план развития](docs/engine-expansion-plan.md). Формат новых пакетов: [Story Pack API 1](docs/story-pack-format.md). Повторное создание NPC: [шаблоны и экземпляры](docs/lua-npc-instances.md). Области переменных: [долговременное состояние](docs/lua-persistent-state.md).

Lua-сценарии находятся в папке игры `ST/scripts`, а GLB-модели NPC — в `ST/assets/models`. Подробности: [docs/story-packs.md](docs/story-packs.md).

В родительском каталоге находятся прежние материалы исследования и отдельный экспериментальный `storytelling_rewrite`; они не входят в сборку этого проекта.







