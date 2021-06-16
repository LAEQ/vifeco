#!/bin/bash

echo "HOME: $HOME"
echo "JAVA_HOME: $JAVA_HOME"
echo "GRADLE_HOME: $GRADLE_HOME"

java -version
gradle -version

ls -la /root/

bash -c "echo Hello world my name is $INPUT_MY_NAME"
