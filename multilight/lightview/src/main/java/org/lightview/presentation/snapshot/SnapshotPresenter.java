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
package org.lightview.presentation.snapshot;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.FadeTransitionBuilder;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyLongProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.util.Duration;

/**
 * User: blog.adam-bien.com Date: 22.11.11 Time: 19:37
 */
public class SnapshotPresenter implements Initializable {

    private String title;
    private String yAxisTitle;
    private String yUnit;
    private XYChart.Series<String, Number> series;
    private static final int MAX_SIZE = 10;

    @FXML
    private XYChart<String, Number> chart;
    private static final double FADE_VALUE = 0.3;
    private DoubleProperty currentValue;
    private boolean activated;
    private ReadOnlyLongProperty idProvider;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        yAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(yAxis, yUnit, null));
        final LineChart chart = new LineChart(xAxis, yAxis);
        chart.setLegendVisible(false);
        chart.setTitle(title);
        yAxis.setLabel(yAxisTitle);
        yAxis.setForceZeroInRange(true);
        this.series = new XYChart.Series<String, Number>();
        chart.getData().add(series);
        this.chart = chart;
        this.chart.setId("snapshotChart");
        deactivate();
        this.registerListeners();
    }

    private void registerListeners() {
        this.currentValue.addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                onNewEntry(newValue);
            }
        });
    }

    public DoubleProperty value() {
        return currentValue;
    }

    public Node view() {
        return this.chart;
    }

    void deactivate() {
        FadeTransition fadeAway = FadeTransitionBuilder.create().fromValue(1.0).toValue(FADE_VALUE).duration(Duration.seconds(1)).node(this.chart).build();
        fadeAway.play();
        activated = false;
    }

    void activate() {
        FadeTransition fadeAway = FadeTransitionBuilder.create().fromValue(FADE_VALUE).toValue(1.0).duration(Duration.seconds(1)).node(this.chart).build();
        fadeAway.play();
        activated = true;
    }

    void onNewEntry(Number value) {
        String id = "-";
        if (idProvider != null) {
            id = String.valueOf(idProvider.get());
        }
        System.out.println("Snapshot: id: " + id);
        if (value.intValue() != 0) {
            this.series.getData().add(new XYChart.Data<String, Number>(id, value));
            if (this.series.getData().size() > MAX_SIZE) {
                this.series.getData().remove(0);
            }

            if (!activated) {
                activate();
            }
        }
    }

}
