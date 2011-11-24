package org.lightview.view;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;


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


    public SnapshotView(String title, String yAxisTitle,String yUnit) {
        this.title = title;
        this.yAxisTitle = yAxisTitle;
        this.yUnit = yUnit;
    }

    public BarChart<String, Number> createChart() {
           final CategoryAxis xAxis = new CategoryAxis();
           final NumberAxis yAxis = new NumberAxis();
           yAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(yAxis,yUnit,null));
           final BarChart chart = new BarChart(xAxis,yAxis);
           chart.setLegendVisible(false);
           chart.setTitle(title);
           yAxis.setLabel(yAxisTitle);
           yAxis.setForceZeroInRange(true);
           this.series = new XYChart.Series<String,Number>();
           chart.getData().add(series);
           Resizer.register(chart);
        return chart;
       }

    public void onNewEntry(String id, long value) {
        if(value != 0){
            this.series.getData().add(new XYChart.Data<String,Number>(id,value));
            if(this.series.getData().size() > MAX_SIZE)
                this.series.getData().remove(0);
        }
    }


}
