package org.lightview.presentation.toolbar;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToolBar;
import org.lightview.business.administration.boundary.LightfishAdministration;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author: adam-bien.com
 */
public class ToolbarPresenter implements Initializable{

    @FXML
    ToolBar toolbar;

    @Inject
    LightfishAdministration administration;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    public void startMonitoring(){

    }

    public void stopMonitoring(){

    }

    public void activateMonitoring(){}

    public void deactivateMonitoring(){}

    public void status(){}
}
