#!/usr/bin/env bash

LOG_DIR=logs
LOG=logs/vifeco.log
JAVA_EXE=java_11_vifeco

if [ ! -h $JAVA_EXE ]
then
  ln -s jre-11.0.10-full/bin/java $JAVA_EXE
fi

mkdir -p $LOG_DIR

if [  ! -f $LOG ]
then
  touch $LOG
fi

./$JAVA_EXE -jar vifeco-2.0.0-all.jar > $LOG 2>&1

