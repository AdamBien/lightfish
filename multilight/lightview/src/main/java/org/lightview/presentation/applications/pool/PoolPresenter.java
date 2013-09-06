/*
 Copyright 2012 Adam Bien, adam-bien.com

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package org.lightview.presentation.applications.pool;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Function;

import javafx.animation.FadeTransition;
import javafx.animation.FadeTransitionBuilder;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.util.Duration;
import javax.inject.Inject;
import org.lightview.business.pool.boundary.EJBPoolMonitoring;
import org.lightview.business.pool.entity.PoolStatistics;
import org.lightview.model.Snapshot;
import org.lightview.presentation.dashboard.DashboardModel;

/**
 * User: blog.adam-bien.com Date: 22.11.11 Time: 19:37
 */
public class PoolPresenter implements Initializable {

    private static final int MAX_SIZE = 8;
    private static final double FADE_VALUE = 0.3;

    @FXML
    private LineChart<String, Number> lineChart;
    private XYChart.Series<String, Number> series;

    private boolean activated;

    @Inject
    DashboardModel model;

    @Inject
    EJBPoolMonitoring poolMonitoring;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    Function<PoolStatistics,Integer> extractor;

    private String applicationName;
    private String ejbName;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.series = new XYChart.Series<String, Number>();
        yAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(yAxis, null, null));
        yAxis.setForceZeroInRange(true);
        lineChart.setLegendVisible(false);
        lineChart.getData().add(series);
        deactivate();
    }

    public void monitor(Function<PoolStatistics,Integer> extractor,String title, String label, String applicationName, String ejbName) {
        this.setTitle(title);
        this.setLabel(label);
        this.applicationName = applicationName;
        this.ejbName = ejbName;
        this.extractor = extractor;
        this.registerListeners();
    }

    public void setTitle(String title) {
        this.lineChart.setTitle(title);
    }

    public void setLabel(String label) {
        this.yAxis.setLabel(label);
    }

    private void registerListeners() {
        this.model.currentSnapshotProperty().addListener(new ChangeListener<Snapshot>() {

            @Override
            public void changed(ObservableValue<? extends Snapshot> ov, Snapshot t, Snapshot newSnapshot) {
                fetchValues(newSnapshot.getId());
            }
        });
    }

    void deactivate() {
        FadeTransition fadeAway = FadeTransitionBuilder.create().fromValue(1.0).toValue(FADE_VALUE).duration(Duration.seconds(1)).node(this.lineChart).build();
        fadeAway.play();
        activated = false;
    }

    void activate() {
        FadeTransition fadeAway = FadeTransitionBuilder.create().fromValue(FADE_VALUE).toValue(1.0).duration(Duration.seconds(1)).node(this.lineChart).build();
        fadeAway.play();
        activated = true;
    }

    void onNewEntry(long snapshotId, int value) {
        String id = String.valueOf(snapshotId);
        System.out.println("Received: " + snapshotId + " " + value);
        if (value != 0) {
            this.series.getData().add(new XYChart.Data<String, Number>(id, value));
            if (this.series.getData().size() > MAX_SIZE) {
                this.series.getData().remove(0);
            }
            if (!activated) {
                activate();
            }
        }
    }

    void fetchValues(long id) {
        PoolStatistics poolStats = poolMonitoring.getPoolStats(this.applicationName, this.ejbName);
        if(poolStats.isValid())
            this.onNewEntry(id, extractor.apply(poolStats));
    }


}
