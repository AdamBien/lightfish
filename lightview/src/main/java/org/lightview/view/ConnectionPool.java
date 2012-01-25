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

import javafx.beans.property.ReadOnlyLongProperty;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import org.lightview.presenter.ConnectionPoolBindings;

/**
 * User: blog.adam-bien.com
 * Date: 03.12.11
 * Time: 10:51
 */
public class ConnectionPool {

    private HBox box;
    private Snapshot freeConnections;
    private Snapshot usedConnections;
    private Snapshot waitQueueLength;
    private Snapshot connectionLeaks;
    private ConnectionPoolBindings bindings;
    private ReadOnlyLongProperty idProvider;

    public ConnectionPool(ReadOnlyLongProperty idProvider, ConnectionPoolBindings connectionPoolBindings) {
        this.bindings = connectionPoolBindings;
        this.idProvider = idProvider;
        this.createSnapshotViews();
        this.bind();
    }

    private void createSnapshotViews(){
        this.freeConnections = new Snapshot(idProvider,"Free Connections", "Connections", "");
        this.usedConnections = new Snapshot(idProvider,"Used Connections", "Connections", "");
        this.waitQueueLength = new Snapshot(idProvider,"Wait Queue Length", "Queue Length", "");
        this.connectionLeaks = new Snapshot(idProvider,"Potential Connection Leak", "Leaks", "");
        this.box = new HBox();
        box.getChildren().add(freeConnections.view());
        box.getChildren().add(usedConnections.view());
        box.getChildren().add(waitQueueLength.view());
        box.getChildren().add(connectionLeaks.view());

    }


    private void bind() {
        this.freeConnections.value().bind(this.bindings.getNumconnfree());
        this.usedConnections.value().bind(this.bindings.getNumconnused());
        this.waitQueueLength.value().bind(this.bindings.getWaitqueuelength());
        this.connectionLeaks.value().bind(this.bindings.getNumpotentialconnleak());
    }


    public Node view() {
        return box;
    }
}
