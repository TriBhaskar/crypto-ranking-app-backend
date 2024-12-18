@echo off
REM stop.bat - Script to stop the Spring Boot application

REM Find and kill the Java application
for /f "tokens=2" %%a in ('tasklist /FI "IMAGENAME eq java.exe" /FI "WINDOWTITLE eq *coinRankBE.jar*" 2^>NUL') do (
    set "PID=%%a"
)

if "%PID%"=="" (
    echo Application is not running.
    exit /b 1
)

REM Attempt graceful shutdown
taskkill /PID %PID% /T /F

echo Application stopped.