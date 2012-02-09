/*
Copyright 2012 Adam Bien, adam-bien.com

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package org.lightview;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.stage.Stage;
import org.lightview.presenter.DashboardPresenter;
import org.lightview.view.Dashboard;

import java.net.MalformedURLException;

/**
 *
 * @author Adam Bien <blog.adam-bien.com>
 */
public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        String serverURI = getServerURI();
        System.out.println("Base URI: " + serverURI);
        DashboardPresenter dashboardPresenter = new DashboardPresenter(serverURI);
        new Dashboard(primaryStage,dashboardPresenter);
    }
    
    String getServerURI(){
        HostServices hostServices = getHostServices();
        if(runsInBrowser(hostServices)){
            return extractHostWithPort(hostServices.getDocumentBase());
        }
        return null;
    }

    boolean runsInBrowser(HostServices hostServices) {
         return (!hostServices.getDocumentBase().startsWith("file:"));
    }

    String extractHostWithPort(String uri){
        int fromIndex = "http://".length();
        int index = uri.indexOf("/",fromIndex);
        return uri.substring(0,index);
    }

    public static void main(String[] args) throws MalformedURLException {
        launch(args);
    }

}
