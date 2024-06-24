#!/usr/bin/env bash

REPOSITORY=/home/ubuntu/chat
cd $REPOSITORY

APP_NAME=webflux-chat
JAR_NAME=$(ls $REPOSITORY/build/libs/ | grep 'SNAPSHOT.jar' | tail -n 1)
JAR_PATH=$REPOSITORY/build/libs/$JAR_NAME

CURRENT_PID=$(pgrep -f $APP_NAME)

if [ -z $CURRENT_PID ]; then
  echo "> No application running, skipping termination."
else
  echo "> Killing process $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "> Deploying $JAR_PATH"
nohup java -jar $JAR_PATH > $REPOSITORY/nohup.out 2>&1 &

echo "> Deployment script completed."

sleep 10

NEW_PID=$(pgrep -f $APP_NAME)

if [ -z $NEW_PID ]; then
  echo "> Application failed to start"
  exit 1
else
  echo "> Application started successfully. PID: $NEW_PID"
fi
