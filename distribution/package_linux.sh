#!/usr/bin/env bash

VERSION=2.0.0
JAR_FILE=vifeco-${VERSION}-all.jar
SOURCE=build/libs/$JAR_FILE
DEST=distribution/linux/$JAR_FILE


# Move to project root
cd ..

if [  ! -f $SOURCE ]
then
  gradle clean
  gradle shadowJar
fi

cp $SOURCE $DEST

cd distribution/linux
rm logs/*.log

zip -r vifeco-${VERSION}.zip jre-11.0.10-full $JAR_FILE vifeco.sh logs licence.txt

rm $JAR_FILE
rmdir logs
rm java_11_vifeco