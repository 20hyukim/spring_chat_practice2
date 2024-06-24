#!/usr/bin/env bash

REPOSITORY=/home/ubuntu/chat
cd $REPOSITORY

APP_NAME=chat
JAR_NAME=$(ls $REPOSITORY/build/libs/ | grep 'SNAPSHOT.jar' | tail -n 1)
JAR_PATH=$REPOSITORY/build/libs/$JAR_NAME

CURRENT_PID=$(pgrep -f $APP_NAME)

if [ -z $CURRENT_PID ]; then
  echo "> 종료할 애플리케이션이 없습니다."
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "> Deploy - $JAR_PATH"
nohup java -jar $JAR_PATH > $REPOSITORY/nohup.out 2>&1 &

echo "> Deployment script completed."

sleep 10

NEW_PID=$(pgrep -f $APP_NAME)

if [ -z $NEW_PID ]; then
  echo "> 애플리케이션 시작 실패"
  exit 1
else
  echo "> 애플리케이션이 성공적으로 시작되었습니다. PID: $NEW_PID"
fi
