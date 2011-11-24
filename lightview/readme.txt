To start the application from the command line use:
java -cp ./target/lightview-1.0-SNAPSHOT.jar:[PATH_TO_JAVAFX]/javafx-sdk2.0.2-beta/rt/lib/jfxrt.jar  org.lightview.App

You will need a profile to set the fx.home variable:
	    <profile>
			<id>javafx</id>
         <activation>
                <activeByDefault>false</activeByDefault>
          </activation>
           <properties>
			 <fx.home>[JAVAFX_HOME]</fx.home>
		</properties>
		</profile>

