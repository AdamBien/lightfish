To start the application from the command line use:
java -Djavafx.runtime.path=[JAVAFX_SDK]/rt -jar ../lightfish/src/main/webapp/lightview-app.jar

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

