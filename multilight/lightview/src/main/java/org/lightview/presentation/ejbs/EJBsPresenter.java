/*
 *
 */
package org.lightview.presentation.ejbs;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
import javax.inject.Inject;
import org.lightview.business.pool.boundary.EJBPoolMonitoring;
import org.lightview.model.Application;

/**
 *
 * @author adam-bien.com
 */
public class EJBsPresenter implements Initializable {

    @FXML
    ListView<String> ejbsList;
    private ObservableList<String> ejbs;

    private String monitoredApplication;

    @Inject
    EJBPoolMonitoring poolMonitoring;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.ejbs = ejbsList.getItems();
        MultipleSelectionModel<String> selectionModel = this.ejbsList.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        selectionModel.selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> ov, String oldValue, String newValue) {
                ejbSelected(newValue);
            }

        });
    }

    public void showComponentsForApp(Set<Application> applications, String newSelection) {
        this.monitoredApplication = newSelection;
        ejbs.clear();
        List<String> componentsForApplication = getComponentsForApplication(applications, newSelection);
        ejbs.addAll(componentsForApplication);
    }

    private List<String> getComponentsForApplication(Set<Application> applications, String newSelection) {
        for (Application application : applications) {
            if (application.getApplicationName().equalsIgnoreCase(newSelection)) {
                return application.getComponents();
            }
        }
        return Collections.EMPTY_LIST;
    }

    void ejbSelected(String newValue) {
        System.out.println("Selected EJB: " + newValue);
        poolMonitoring;
    }

}
