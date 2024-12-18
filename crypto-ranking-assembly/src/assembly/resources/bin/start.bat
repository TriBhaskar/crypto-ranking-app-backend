@echo off
REM start.bat - Batch script to start the Spring Boot application

REM Set Java options
set APP_OPTS=%APP_OPTS% -Dlogging.config=..\logs\logback-spring.xml

REM Run the application
java %APP_OPTS% -jar ..\lib\coinRankBE.jar --spring.config.location=file:..\config\

REM Check if the application started successfully
if %ERRORLEVEL% neq 0 (
    echo Application failed to start
    pause
)