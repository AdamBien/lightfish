package org.lightview.presentation.methods;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.lightview.business.methods.boundary.MethodMonitoring;
import org.lightview.business.methods.entity.MethodStatistics;
import org.lightview.business.pool.boundary.EJBPoolMonitoring;
import org.lightview.model.Snapshot;
import org.lightview.presentation.dashboard.DashboardModel;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author: adam-bien.com
 */
public class MethodsPresenter implements Initializable{

    @Inject
    MethodMonitoring methodMonitoring;


    @Inject
    DashboardModel dashboardModel;


    @FXML
    TableView methods;

    @FXML
    TableColumn name;

    @FXML
    TableColumn lastExecutionTime;

    @FXML
    TableColumn totalInvocationTime;

    @FXML
    TableColumn maxTime;

    @FXML
    TableColumn numberSuccess;

    @FXML
    TableColumn invocationCount;

    @FXML
    TableColumn error;

    private ObservableList<MethodStatistics> methodStatistics;
    private String monitoriedApplication;
    private String ejb;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
         this.methods.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
         this.setupBinding();
         this.setupRefresh();
    }


    void setupBinding(){
        this.name.setCellValueFactory(new PropertyValueFactory<MethodStatistics, String>("name"));
        this.lastExecutionTime.setCellValueFactory(new PropertyValueFactory<MethodStatistics, String>("lastExecutionTime"));
        this.numberSuccess.setCellValueFactory(new PropertyValueFactory<MethodStatistics, String>("totalNumSuccess"));
        this.error.setCellValueFactory(new PropertyValueFactory<MethodStatistics, String>("totalNumError"));
        this.totalInvocationTime.setCellValueFactory(new PropertyValueFactory<MethodStatistics, String>("totalInvocationTime"));
        this.maxTime.setCellValueFactory(new PropertyValueFactory<MethodStatistics, String>("maxTime"));
        this.invocationCount.setCellValueFactory(new PropertyValueFactory<MethodStatistics, String>("invocationCount"));
        this.methodStatistics = methods.getItems();
    }

    private void setupRefresh() {
         this.dashboardModel.currentSnapshotProperty().addListener((o,oldValue,newValue) -> monitor());
    }

    public void monitor(){
        this.monitor(this.monitoriedApplication,this.ejb);
    }

    public void monitor(String monitoredApplication, String ejb) {
        this.monitoriedApplication = monitoredApplication;
        this.ejb = ejb;
        this.methodStatistics.clear();
        this.methodStatistics.addAll(methodMonitoring.getMethodStatistics(monitoredApplication, ejb).all());
    }
}
