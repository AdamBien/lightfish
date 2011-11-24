package org.lightview.view;

import javafx.animation.ScaleTransition;
import javafx.animation.ScaleTransitionBuilder;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

public class Resizer {

    public static void doubleSize(Node node) {
        ScaleTransition scaleTransition = ScaleTransitionBuilder.create().duration(Duration.seconds(1)).byX(2f).byY(2f).node(node).build();
        scaleTransition.play();
    }

    public static void halfSize(Node node) {
        ScaleTransition scaleTransition = ScaleTransitionBuilder.create().duration(Duration.seconds(1)).byX(-2f).byY(-2f).node(node).build();
        scaleTransition.play();
    }

    public static void minimize(Node node) {
        ScaleTransition scaleTransition = ScaleTransitionBuilder.create().duration(Duration.seconds(1)).toX(0).toY(0).node(node).build();
        scaleTransition.play();
    }

    public static void register(final Node chart) {
        chart.setOnMouseClicked(new EventHandler<MouseEvent>() {
            boolean minimized = false;
            public void handle(MouseEvent mouseEvent) {
                System.out.println("---" + mouseEvent);
                if(minimized){
                    halfSize(chart);
                    minimized = false;
                }else{
                    doubleSize(chart);
                    minimized = true;
                }
            }
        });
    }
}