#!/bin/bash
# start.sh - Bash script to start the Spring Boot application

APP_OPTS="$APP_OPTS -Dlogging.config=../logs/logback-spring.xml"

exec $JAVA_HOME/bin/java $APP_OPTS -jar ../lib/coinRankBE.jar --spring.config.location=file:../config/