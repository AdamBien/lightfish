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
package org.lightview.view;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Adam Bien <blog.adam-bien.com>
 */
public class Browser extends Collapsible {

    private StringProperty uri = new SimpleStringProperty();

    private WebEngine engine;
    private WebView webView;
    private final static int HEIGHT = 280;

    Browser() {}

    Node view() {
        if(webView == null)
            initialize();
        return webView;
    }

    private void initialize() {
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
            public void changed(ObservableValue<? extends String> observable, String old, String newValue) {
                if (isValid(newValue)) {
                    engine.load(skipLastSlash(newValue));
                }
            }
        });
    }

    boolean isValid(String newValue) {
        if(newValue == null){
            return false;
        }

        try {
            new URL(newValue);
        } catch (MalformedURLException e) {
            System.out.println("URI: " + newValue + " is invalid: " + e);
            return false;
        }
        return true;
    }

    
    String skipLastSlash(String uri) {
        if(!uri.endsWith("/"))
            return uri;
        return uri.substring(0, uri.lastIndexOf("/"));
    }

    @Override
    public DoubleProperty getMaxHeightProperty() {
        return this.webView.maxHeightProperty();
    }
}
