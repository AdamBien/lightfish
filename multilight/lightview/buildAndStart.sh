#!/bin/bash
set JAVA_HOME=$JAVA_8_HOME
./build.sh
mvn failsafe:integration-test failsafe:verify
./lightview-app.sh