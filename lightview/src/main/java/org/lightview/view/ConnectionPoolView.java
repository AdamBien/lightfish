package org.lightview.view;

import javafx.beans.property.LongProperty;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import org.lightview.presenter.ConnectionPoolBindings;

/**
 * User: blog.adam-bien.com
 * Date: 03.12.11
 * Time: 10:51
 */
public class ConnectionPoolView {

    private HBox box;
    private String jndiName;
    private SnapshotView freeConnections;
    private SnapshotView usedConnections;
    private SnapshotView waitQueueLength;
    private SnapshotView connectionLeaks;
    private ConnectionPoolBindings bindings;
    private LongProperty idProvider;

    public ConnectionPoolView(LongProperty idProvider,String jndiName,ConnectionPoolBindings connectionPoolBindings) {
        this.jndiName = jndiName;
        this.bindings = connectionPoolBindings;
        this.idProvider = idProvider;
        this.createSnapshotViews();
        this.bind();
    }

    private void createSnapshotViews(){
        this.freeConnections = new SnapshotView(idProvider,"Free Connections", "Connections", "");
        this.usedConnections = new SnapshotView(idProvider,"Used Connections", "Connections", "");
        this.waitQueueLength = new SnapshotView(idProvider,"Wait Queue Length", "Queue Length", "");
        this.connectionLeaks = new SnapshotView(idProvider,"Potential Connection Leak", "Leaks", "");
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
