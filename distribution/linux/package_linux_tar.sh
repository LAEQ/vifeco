#!/usr/bin/env bash

# Preparing the package

APP_NAME=vifeco
VERSION=2.0.0
JAR_FILE=../ressources/vifeco-${VERSION}-all.jar
JRE=../jre/linux/jre-11.0.10-full

PACKAGE_DIR=vifeco_${VERSION}_linux_noarch
APP_DIR=${PACKAGE_DIR}/app
BIN_DIR=${PACKAGE_DIR}/bin
RUNTIME=${PACKAGE_DIR}/runtime

rm -f ${PACKAGE_DIR}.tar.gz

# Validation
if [ ! -f ${JAR_FILE} ]; then
  echo "Missing jar"
  exit 1
fi

if [ ! -d ${JRE} ]; then
  echo "JRE is missing"
  exit 1
fi


# Create structure
mkdir -p ${PACKAGE_DIR}
mkdir -p ${APP_DIR}
mkdir -p ${BIN_DIR}
mkdir -p ${RUNTIME}

cp -r ${JRE}/* ${PACKAGE_DIR}/runtime/
cp ${JAR_FILE} ${APP_DIR}
cp vifeco $BIN_DIR


tar -czvf ${PACKAGE_DIR}.tar.gz ${PACKAGE_DIR}
rm -rf ${PACKAGE_DIR}

exit 0




