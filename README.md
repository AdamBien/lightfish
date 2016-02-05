
LightFish is Licensed under the Apache License, Version 2.0

#Installation:

Download the lightfish.war from [releases](https://github.com/AdamBien/lightfish/releases) and deploy it to (Payara Server)[http://www.payara.fish] or (GlassFish)[https://glassfish.java.net]

To launch the lightview client, execute: `java -jar lightview-app.jar` 

or from source:

0. Setup JAVA_HOME to point to JDK 1.8 installation (lightfish uses JDK 1.8 features)
1. `git clone https://github.com/AdamBien/lightfish.git`
2. Perform `mvn clean install`in the multi-light folder

Drop the lightfish/target/lightfish.war into /payara41/glassfish/domains/domain1/autodeploy

Point your browser to: http://localhost:8080/lightfish

LightFish should install the necessary connection pool and datasources on-the-fly. There is no setup needed. Tested with Payara v4.1X. [Payara](http://www.payara.fish) is patched GlassFish 4.1, so lightfish should also run GlassFish.

