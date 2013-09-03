package org.lightview.presentation.applications;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.collections.SetChangeListener.Change;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.*;
import org.lightview.model.Application;
import org.lightview.presentation.dashboard.DashboardModel;
import org.lightview.presentation.ejbs.EJBsPresenter;
import org.lightview.presentation.ejbs.EJBsView;

import javax.inject.Inject;

import static javafx.scene.layout.Priority.*;

/**
 *
 * @author adam-bien.com
 */
public class ApplicationsPresenter implements Initializable {

    private EJBsPresenter ejbPresenter;
    private EJBsView ejbView;

    @FXML
    private AnchorPane ejbs;

    @FXML
    private ListView<String> applicationsList;

    @Inject
    private DashboardModel dashboardModel;



    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.ejbView = new EJBsView();
        this.ejbPresenter = (EJBsPresenter) ejbView.getPresenter();
        this.setupViews();
    }

    void setupViews() {
        MultipleSelectionModel<String> selectionModel = applicationsList.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        selectionModel.selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String newSelection) {
                ejbPresenter.showComponentsForApp(dashboardModel.applicationsSetProperty(), newSelection);

            }
        });

        final Parent ejbView = this.ejbView.getView();
        final ObservableList<String> items = applicationsList.getItems();

        this.dashboardModel.applicationsSetProperty().addListener(new SetChangeListener<Application>() {
            @Override
            public void onChanged(Change<? extends Application> change) {
                if (change.wasAdded()) {
                    items.add(change.getElementAdded().getApplicationName());
                }
                if (change.wasRemoved()) {
                    items.remove(change.getElementRemoved().getApplicationName());
                }
            }
        });
        ejbs.getChildren().add(ejbPresenter.getSplitPane());
    }

}
