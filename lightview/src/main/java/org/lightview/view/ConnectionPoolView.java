package org.lightview.view;

import javafx.scene.Node;
import javafx.scene.layout.HBox;
import org.lightview.entity.ConnectionPool;
import org.lightview.entity.Snapshot;

/**
 * User: blog.adam-bien.com
 * Date: 03.12.11
 * Time: 10:51
 */
public class ConnectionPoolView implements SnapshotListener {

    private SnapshotView freeConnections;
    private SnapshotView usedConnections;
    private SnapshotView waitQueueLength;
    private SnapshotView connectionLeaks;
    private String jndiName;
    private HBox box;

    public ConnectionPoolView(String jndiName) {
        this.jndiName = jndiName;
        this.createSnapshotViews();
    }

    private void createSnapshotViews(){
        this.freeConnections = new SnapshotView("Free Connections", "Connections", "");
        this.usedConnections = new SnapshotView("Used Connections", "Connections", "");
        this.waitQueueLength = new SnapshotView("Wait Queue Length", "Queue Length", "");
        this.connectionLeaks = new SnapshotView("Potential Connection Leak", "Leaks", "");
        this.box = new HBox();
        box.getChildren().add(freeConnections.view());
        box.getChildren().add(usedConnections.view());
        box.getChildren().add(waitQueueLength.view());
        box.getChildren().add(connectionLeaks.view());

    }

    public void onSnapshotArrival(Snapshot snapshot) {
        String id = String.valueOf(snapshot.getId());
        ConnectionPool pool = snapshot.getPool(this.jndiName);
        updateView(id,pool);
    }

    private void updateView(String id,ConnectionPool pool) {
        this.freeConnections.onNewEntry(id,pool.getNumconnfree());
        this.usedConnections.onNewEntry(id,pool.getNumconnused());
        this.connectionLeaks.onNewEntry(id,pool.getNumpotentialconnleak());
        this.waitQueueLength.onNewEntry(id,pool.getWaitqueuelength());
    }


    public Node view() {
        return box;
    }
}
