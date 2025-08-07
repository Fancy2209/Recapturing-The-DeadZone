@echo off
setlocal

:: Step 1: Build fat jar
call gradlew shadowJar || exit /b

:: Step 2: Copy static/ into deploy/
if exist deploy\static rmdir /s /q deploy\static
xcopy /E /I /Y static deploy\static

echo Build completed: deploy\deadzone-server.jar + static\

endlocal
