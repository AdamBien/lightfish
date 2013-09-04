package org.lightview.presentation.dashboard.toolbar;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import org.lightview.business.administration.boundary.PollingSetup;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author: adam-bien.com
 */
public class ToolbarPresenter implements Initializable{

    @FXML
    ToolBar toolbar;

    @FXML
    TextField location;

    @FXML
    public TextField interval;

    @Inject
    PollingSetup administration;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    public void startMonitoring(){
        final String location = this.location.getText();
        final String intervalText = this.interval.getText();
        final String nextTimeout = administration.changeInterval(location, Integer.valueOf(intervalText));

    }

    public void stopMonitoring(){
       administration.stopPolling();
    }

    public void activateMonitoring(){}

    public void deactivateMonitoring(){}

    public void status(){}
}
