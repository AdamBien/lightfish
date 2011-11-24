package org.lightview;

import java.net.MalformedURLException;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.stage.Stage;
import org.lightview.entity.Snapshot;
import org.lightview.service.SnapshotProvider;
import org.lightview.view.DashboardPresenter;
import org.lightview.view.DashboardView;

/**
 *
 * @author Adam Bien <blog.adam-bien.com>
 */
public class App extends Application {


    @Override
    public void start(Stage primaryStage) {
        DashboardPresenter dashboardPresenter = new DashboardPresenter();
        new DashboardView(primaryStage,dashboardPresenter);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws MalformedURLException {
        launch(args);
    }

}
