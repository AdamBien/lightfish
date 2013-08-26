#!/bin/bash
${JAVAFX_HOME}/bin/javafxpackager -createjar -nocss2bin -appclass org.lightview.App -srcdir ./target/classes -outdir . -outfile lightview-app -v