package org.lightview.view;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.util.Duration;

/**
 * User: blog.adam-bien.com
 * Date: 24.11.11
 * Time: 20:29
 */
public abstract class Collapsible {
    protected double prefHeight;
    private boolean minimized = false;

    public boolean toggleMinimize() {
        if (minimized) {
            maximize();
            minimized = false;
        } else {
            minimize();
            minimized = true;
        }
        return minimized;

    }

    void minimize() {
        animate(0);
    }

    void maximize() {
        animate(this.prefHeight);
    }

    void animate(double toValue){
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(1),
                        new KeyValue(getMaxHeightProperty(), toValue)));
        timeline.playFromStart();
    }

    protected abstract DoubleProperty getMaxHeightProperty();
}
