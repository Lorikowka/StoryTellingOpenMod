# Compile diagnostic

Run `gradlew.bat compileJava -I gradle/diagnostic.gradle --console plain > compile-errors.log 2>&1` to list up to 2000 errors in one pass. The log may contain local file paths. The diagnostic does not change the production build.
