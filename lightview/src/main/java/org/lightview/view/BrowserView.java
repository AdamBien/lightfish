package org.lightview.view;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Duration;

/**
 * @author Adam Bien <blog.adam-bien.com>
 */
public class BrowserView {

    private StringProperty uri = new SimpleStringProperty();

    private WebEngine engine;
    private WebView webView;
    private double prefHeight;
    private boolean minimized = false;


    public StringProperty getUriProperty() {
        return uri;
    }

    public BrowserView() {
        this.webView = new WebView();
        this.engine = webView.getEngine();
        this.prefHeight = this.webView.getPrefHeight();
        this.registerListeners();
    }

    private void registerListeners() {
        uri.addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> arg0, String old, String newValue) {
                if (newValue != null) {
                    engine.load(skipLastSegment(newValue));
                }
            }
        });
    }

    private String skipLastSegment(String uri) {
        return uri.substring(0, uri.lastIndexOf("/"));
    }

    Node view() {
        return webView;
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
                        new KeyValue(this.webView.maxHeightProperty(), toValue)));
        timeline.playFromStart();
    }

}
