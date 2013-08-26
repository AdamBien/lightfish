#!/bin/bash
${GLASSFISH_HOME}/bin/asadmin --port 8048 deploy --force ./target/lightfish.war
