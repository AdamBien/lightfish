package org.lightview.view;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.WritableValue;
import javafx.scene.web.WebView;
import javafx.util.Duration;

/**
 * User: blog.adam-bien.com
 * Date: 24.11.11
 * Time: 20:29
 */
public abstract class CollapsibleView {
    protected double prefHeight;
    private boolean minimized = false;

    public void minimize() {
        animate(0);
    }

    public void maximize() {
        animate(this.prefHeight);
    }

    public void animate(double toValue){
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(1),
                        new KeyValue(getMaxHeightProperty(), toValue)));
        timeline.playFromStart();
    }

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

    public abstract DoubleProperty getMaxHeightProperty();
}
