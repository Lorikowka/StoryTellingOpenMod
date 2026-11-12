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

При первом запуске Gradle и Forge загружают компоненты. Для текущей версии исходников `20.1.34-update-lua-alpha.10` нужны Storytelling, HammerLib, HammerAnimations и GFBS: glTF. Версии обязательных зависимостей указаны в `src/main/resources/META-INF/mods.toml`. Комплект `alpha.10` находится в `dist/minimal-mods`; прежние JAR `alpha.1`–`alpha.3` и `alpha.6`–`alpha.9` сохранены в `dist/previous`. Портрет NPC из группы `Head` создаётся из текстуры glTF и кэшируется. Проверьте его в игре командой `/script start 900008`.

План развития основы Lua-сценариев: [docs/lua-engine-roadmap.md](docs/lua-engine-roadmap.md).

Lua-сценарии находятся в папке игры `ST/scripts`, а GLB-модели NPC — в `ST/assets/models`. Подробности: [docs/story-packs.md](docs/story-packs.md).

В родительском каталоге находятся прежние материалы исследования и отдельный экспериментальный `storytelling_rewrite`; они не входят в сборку этого проекта.





