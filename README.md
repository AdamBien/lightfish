[![Build Status](https://travis-ci.org/AdamBien/lightfish.svg?branch=travisci)](https://travis-ci.org/AdamBien/lightfish)

LightFish is Licensed under the Apache License, Version 2.0

#Installation:

Download the lightfish.war from [https://github.com/AdamBien/lightfish/releases](https://github.com/AdamBien/lightfish/releases)

or

0. Setup JAVA_HOME to point to JDK 1.7 installation (lightfish uses JDK 1.7 features)
1. `git clone https://github.com/AdamBien/lightfish.git`
2. Perform `mvn clean install`in the multi-light folder

Drop the lightfish/target/lightfish.war into /glassfish4.0.X/glassfish/domains/domain1/autodeploy

Point your browser to: http://localhost:8080/lightfish

LightFish should install the necessary connection pool and datasources on-the-fly. There is no setup needed. Tested with GlassFish v4.0.1

