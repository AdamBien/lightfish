package org.lightview.presentation.dashboard.toolbar;

import javafx.animation.FadeTransition;
import javafx.animation.FadeTransitionBuilder;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.util.Duration;
import javafx.util.Pair;
import org.lightview.business.administration.boundary.MonitoringLevelActivation;
import org.lightview.business.administration.boundary.PollingSetup;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author: adam-bien.com
 */
public class ToolbarPresenter implements Initializable {

    @FXML
    ToolBar toolbar;

    @FXML
    TextField location;

    @FXML
    public TextField interval;

    @FXML
    public Label message;

    @Inject
    PollingSetup administration;

    @Inject
    MonitoringLevelActivation monitoringLevelActivation;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    public void startMonitoring() {
        final String location = this.location.getText();
        final String intervalText = this.interval.getText();
        final String nextTimeout = administration.changeInterval(location, Integer.valueOf(intervalText));
        status();
    }

    public void stopMonitoring() {
        administration.stopPolling();
        status();
    }

    public void activateMonitoring() {
        monitoringLevelActivation.activateMonitoring();
    }

    public void deactivateMonitoring() {
        monitoringLevelActivation.deactivateMonitoring();
    }

    public void status() {
        final Pair<String,String> status = administration.status();
        showMessage();
        message.setText("Interval: " + status.getKey() + " next timeout: " + status.getValue());
    }

    void showMessage() {
        final FadeTransition fadeTransition = new FadeTransition(Duration.seconds(3),this.message);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1.0);
        fadeTransition.setAutoReverse(true);
        fadeTransition.setCycleCount(2);
        fadeTransition.play();
    }

}
