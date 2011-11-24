package org.lightview.view;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.*;
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
public class BrowserView extends CollapsibleView {

    private StringProperty uri = new SimpleStringProperty();

    private WebEngine engine;
    private WebView webView;

    public BrowserView() {
        this.webView = new WebView();
        this.engine = webView.getEngine();
        this.prefHeight = this.webView.getPrefHeight();
        this.registerListeners();
    }



    public StringProperty getUriProperty() {
        return uri;
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


    @Override
    public DoubleProperty getMaxHeightProperty() {
        return this.webView.maxHeightProperty();
    }
}
