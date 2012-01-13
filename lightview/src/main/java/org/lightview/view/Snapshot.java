package org.lightview.view;

import javafx.animation.FadeTransition;
import javafx.animation.FadeTransitionBuilder;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyLongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.util.Duration;


/**
 * User: blog.adam-bien.com
 * Date: 22.11.11
 * Time: 19:37
 */
public class Snapshot {

    private String title;
    private String yAxisTitle;
    private String yUnit;
    private XYChart.Series<String, Number> series;
    private static final int MAX_SIZE = 10;
    private XYChart<String, Number> chart;
    private static final double FADE_VALUE = 0.3;
    private DoubleProperty currentValue;
    private boolean activated;
    private ReadOnlyLongProperty idProvider;

    public Snapshot(ReadOnlyLongProperty idProvider, String title, String yAxisTitle, String yUnit) {
        this.title = title;
        this.yAxisTitle = yAxisTitle;
        this.yUnit = yUnit;
        this.currentValue = new SimpleDoubleProperty();
        this.idProvider = idProvider;
        this.initialize();
        this.registerListeners();
    }

    public Snapshot(ReadOnlyLongProperty idProvider, String title, String yAxisTitle) {
        this(idProvider,title,yAxisTitle,null);
    }


    private void initialize() {
           final CategoryAxis xAxis = new CategoryAxis();
           final NumberAxis yAxis = new NumberAxis();
           yAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(yAxis,yUnit,null));
           final LineChart chart = new LineChart(xAxis,yAxis);
           chart.setLegendVisible(false);
           chart.setTitle(title);
           yAxis.setLabel(yAxisTitle);
           yAxis.setForceZeroInRange(true);
           this.series = new XYChart.Series<String,Number>();
           chart.getData().add(series);
           this.chart = chart;
           this.chart.setId("snapshotChart");
           deactivate();
       }

    private void registerListeners(){
        this.currentValue.addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                onNewEntry(newValue);
            }
        });
    }

    public DoubleProperty value() {
        return currentValue;
    }

    public Node view(){
        return this.chart;
    }

    void deactivate(){
        FadeTransition fadeAway = FadeTransitionBuilder.create().fromValue(1.0).toValue(FADE_VALUE).duration(Duration.seconds(1)).node(this.chart).build();
        fadeAway.play();
        activated = false;
    }

    void activate(){
        FadeTransition fadeAway = FadeTransitionBuilder.create().fromValue(FADE_VALUE).toValue(1.0).duration(Duration.seconds(1)).node(this.chart).build();
        fadeAway.play();
        activated = true;
    }

    void onNewEntry(Number value) {
        String id = "-";
        if(idProvider != null)
            id = String.valueOf(idProvider.get());
        if(value.intValue() != 0){
            this.series.getData().add(new XYChart.Data<String,Number>(id,value));
            if(this.series.getData().size() > MAX_SIZE)
                this.series.getData().remove(0);

            if(!activated)
                activate();
        }
    }
}
