#!/usr/bin/env bash

# Preparing the package

APP_NAME=vifeco
VERSION=3.0.0
JAR_FILE=vifeco-${VERSION}-all.jar
JRE=jre-11.0.10-full


PACKAGE_DIR=vifeco_${VERSION}_amd64
DEBIAN_PACKAGE_DIR=$PACKAGE_DIR/usr/share/${APP_NAME}
DESKTOP_DIR=${PACKAGE_DIR}/usr/share/applications/
RESSOURCE_DIR=${DEBIAN_PACKAGE_DIR}/runtime
APP_DIR=${DEBIAN_PACKAGE_DIR}/app
BIN_DIR=${PACKAGE_DIR}/usr/local/bin

rm -f ${PACKAGE_DIR}.deb

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
mkdir -p ${RESSOURCE_DIR}
mkdir -p ${APP_DIR}
mkdir -p ${DESKTOP_DIR}
mkdir -p ${BIN_DIR}

cp -r ${JRE}/* ${RESSOURCE_DIR}/
cp ${JAR_FILE} ${APP_DIR}
cp vifeco $BIN_DIR
cp ressources/vifeco.desktop $DESKTOP_DIR
cp ressources/vifeco.png $DEBIAN_PACKAGE_DIR


cp -r DEBIAN $PACKAGE_DIR

# Create package
dpkg-deb --build $PACKAGE_DIR

# Clean
rm -rf ${PACKAGE_DIR}

exit 0




