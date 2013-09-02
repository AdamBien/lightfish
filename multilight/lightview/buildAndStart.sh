#!/bin/bash
./build.sh
mvn failsafe:integration-test failsafe:verify
./lightview-app.sh