#!/bin/bash
${JAVAFX_HOME}/bin/javafxpackager -deploy -embedCertificates -nocss2bin -appclass org.lightview.App -srcdir ./target/classes -outdir ./target/webstart -outfile lightview-web -width 800 -height 600 -name "LightView" -title "LightView Real Time Monitoring" -vendor adam-bien.com -embedjnlp -v





