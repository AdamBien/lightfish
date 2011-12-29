package org.lightview;

import javafx.application.Application;
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
        DashboardPresenter dashboardPresenter = new DashboardPresenter();
        new Dashboard(primaryStage,dashboardPresenter);
    }

    public static void main(String[] args) throws MalformedURLException {
        launch(args);
    }

}
