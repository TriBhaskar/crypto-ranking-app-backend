#!/bin/bash
# stop.sh - Script to stop the Spring Boot application

# Find the PID of the Java application
PID=$(ps aux | grep 'coinRankBE.jar' | grep -v 'grep' | awk '{print $2}')

if [ -z "$PID" ]; then
    echo "Application is not running."
    exit 1
fi

# Attempt to stop the application gracefully
kill -15 $PID

# Wait a few seconds to allow graceful shutdown
sleep 5

# If still running, force kill
if ps -p $PID > /dev/null; then
    echo "Graceful shutdown failed. Force stopping the application."
    kill -9 $PID
fi

echo "Application stopped."