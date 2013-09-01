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
import javafx.scene.layout.AnchorPane;
import javax.inject.Inject;

import javafx.scene.layout.HBox;
import org.lightview.business.pool.boundary.EJBPoolMonitoring;
import org.lightview.business.pool.entity.PoolStatistics;
import org.lightview.model.Application;
import org.lightview.presentation.pool.PoolPresenter;
import org.lightview.presentation.pool.PoolView;

/**
 *
 * @author adam-bien.com
 */
public class EJBsPresenter implements Initializable {

    @FXML
    ListView<String> ejbsList;

    @FXML
    HBox statisticsPane;

    private ObservableList<String> ejbs;

    private String monitoredApplication;

    @Inject
    EJBPoolMonitoring poolMonitoring;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        prepareList();
    }

    private void prepareList() {
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

    void ejbSelected(String selectedEjb) {
        PoolStatistics poolStats = poolMonitoring.getPoolStats(this.monitoredApplication, selectedEjb);
        System.out.println("Selected EJB: " + selectedEjb + " from application: " + this.monitoredApplication + " with stats: " + poolStats);
        activateStatistics(selectedEjb);

    }

    void activateStatistics(String ejb) {
        PoolView beansCreatedView = new PoolView();
        PoolPresenter beansCreatedPresenter = (PoolPresenter) beansCreatedView.getPresenter();
        beansCreatedPresenter.monitor((PoolStatistics p) ->
                p.getTotalBeansCreated()
                , "Created beans", "Number of beans", this.monitoredApplication, ejb);

        PoolView beansDestroyedView = new PoolView();
        PoolPresenter beansDestroyedViewPresenter = (PoolPresenter) beansDestroyedView.getPresenter();
        beansDestroyedViewPresenter.monitor((PoolStatistics p) ->
                p.getTotalBeansDestroyed()
                , "Destroyed beans", "Number of beans", this.monitoredApplication, ejb);

        PoolView totalThreadsWaitingView = new PoolView();
        PoolPresenter totalThreadsWaitingViewPresenter = (PoolPresenter) totalThreadsWaitingView.getPresenter();
        totalThreadsWaitingViewPresenter.monitor((PoolStatistics p) ->
                p.getCurrentThreadsWaiting()
                , "Total Threads Waiting", "Number of beans", this.monitoredApplication, ejb);

        PoolView totalThreadsWaitingHighView = new PoolView();
        PoolPresenter totalThreadsWaitingViewPresenterHigh = (PoolPresenter) totalThreadsWaitingHighView.getPresenter();
        totalThreadsWaitingViewPresenterHigh.monitor((PoolStatistics p) ->
                p.getThreadsWaitingHighwatermark()
                , "Total Threads Waiting High", "Number of beans", this.monitoredApplication, ejb);

        this.statisticsPane.getChildren().clear();
        this.statisticsPane.getChildren().add(beansCreatedView.getView());
        this.statisticsPane.getChildren().add(beansDestroyedView.getView());
        this.statisticsPane.getChildren().add(totalThreadsWaitingView.getView());
        this.statisticsPane.getChildren().add(totalThreadsWaitingHighView.getView());
    }

}
