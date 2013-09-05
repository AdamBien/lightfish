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

import com.airhacks.afterburner.injection.InjectionProvider;
import java.net.MalformedURLException;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.stage.Stage;
import org.lightview.presentation.dashboard.DashboardPresenter;
import org.lightview.presentation.dashboard.DashboardView;

/**
 *
 * @author Adam Bien <blog.adam-bien.com>
 */
public class App extends Application {

    private static final String VERSION = "1.3-snapshot";

    @Override
    public void start(Stage primaryStage) {
        System.out.println("Launching LighView " + VERSION);
        DashboardPresenter dashboardPresenter = new DashboardPresenter();
        InjectionProvider.registerExistingAndInject(dashboardPresenter);
        dashboardPresenter.initialize(null, null);
        new DashboardView(primaryStage, dashboardPresenter);
    }



    public static void main(String[] args) throws MalformedURLException {
        launch(args);
    }
}
