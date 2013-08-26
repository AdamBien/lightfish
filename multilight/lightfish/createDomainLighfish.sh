#!/bin/bash
$GLASSFISH_HOME/bin/asadmin stop-domain lighfish
$GLASSFISH_HOME/bin/asadmin delete-domain lighfish
$GLASSFISH_HOME/bin/asadmin create-domain --portbase 8000 --nopassword=true lighfish
$GLASSFISH_HOME/bin/asadmin start-domain lighfish