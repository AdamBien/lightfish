package org.lightview.view;

import javafx.animation.FadeTransition;
import javafx.animation.FadeTransitionBuilder;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.util.Duration;


/**
 * User: blog.adam-bien.com
 * Date: 22.11.11
 * Time: 19:37
 */
public class SnapshotView implements NewEntryListener {

    private String title;
    private String yAxisTitle;
    private String yUnit;
    private XYChart.Series<String, Number> series;
    private static final int MAX_SIZE = 10;
    private XYChart<String, Number> chart;
    private static final double FADE_VALUE = 0.3;

    private boolean activated;


    public SnapshotView(String title, String yAxisTitle,String yUnit) {
        this.title = title;
        this.yAxisTitle = yAxisTitle;
        this.yUnit = yUnit;
        this.initialize();
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
           Resizer.register(chart);
           this.chart = chart;
           deactivate();
       }

    public void deactivate(){
        FadeTransition fadeAway = FadeTransitionBuilder.create().fromValue(1.0).toValue(FADE_VALUE).duration(Duration.seconds(1)).node(this.chart).build();
        fadeAway.play();
        activated = false;
    }

    public void activate(){
        FadeTransition fadeAway = FadeTransitionBuilder.create().fromValue(FADE_VALUE).toValue(1.0).duration(Duration.seconds(1)).node(this.chart).build();
        fadeAway.play();
        activated = true;
    }

    public Node view(){
        return this.chart;
    }

    public void onNewEntry(String id, long value) {

        if(value != 0){
            this.series.getData().add(new XYChart.Data<String,Number>(id,value));
            if(this.series.getData().size() > MAX_SIZE)
                this.series.getData().remove(0);

            if(!activated)
                activate();
        }
    }


}
