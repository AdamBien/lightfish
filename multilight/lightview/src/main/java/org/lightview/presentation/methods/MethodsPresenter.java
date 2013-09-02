package org.lightview.presentation.methods;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.lightview.business.methods.boundary.MethodMonitoring;
import org.lightview.business.methods.entity.MethodStatistics;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author: adam-bien.com
 */
public class MethodsPresenter implements Initializable{

    @Inject
    MethodMonitoring methodMonitoring;

    @FXML
    TableView methods;

    @FXML
    TableColumn name;
    @FXML
    TableColumn lastExecutionTime;
    @FXML
    TableColumn numberSuccess;
    @FXML
    TableColumn error;
    
    private ObservableList<MethodStatistics> methodStatistics;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
         this.setupBinding();
    }

    void setupBinding(){
        this.name.setCellValueFactory(new PropertyValueFactory<MethodStatistics, String>("name"));
        this.lastExecutionTime.setCellValueFactory(new PropertyValueFactory<MethodStatistics, String>("lastExecutionTime"));
        this.numberSuccess.setCellValueFactory(new PropertyValueFactory<MethodStatistics, String>("totalNumSuccess"));
        this.error.setCellValueFactory(new PropertyValueFactory<MethodStatistics, String>("totalNumSuccess"));
        this.methodStatistics = methods.getItems();
    }


    public void monitor(String monitoredApplication, String ejb) {
        this.methodStatistics.clear();
        this.methodStatistics.addAll(methodMonitoring.getMethodStatistics(monitoredApplication,ejb).all());
    }
}
