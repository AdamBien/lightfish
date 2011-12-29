package org.lightview.view;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * @author Adam Bien <blog.adam-bien.com>
 */
public class Browser extends Collapsible {

    private StringProperty uri = new SimpleStringProperty();

    private WebEngine engine;
    private WebView webView;
    private final static int HEIGHT = 280;

    public Browser() {
        this.webView = new WebView();
        this.webView.setPrefHeight(HEIGHT);
        this.engine = webView.getEngine();
        this.prefHeight = this.webView.getPrefHeight();
        this.registerListeners();
    }



    public StringProperty getURI() {
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
