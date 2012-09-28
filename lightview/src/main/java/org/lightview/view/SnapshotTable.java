package org.lightview.view;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.lightview.model.Snapshot;

/**
 * User: blog.adam-bien.com
 * Date: 23.11.11
 * Time: 20:02
 */
public class SnapshotTable {

    private ObservableList<Snapshot> snapshots;

    public SnapshotTable(ObservableList<Snapshot> snapshots) {
        this.snapshots = snapshots;
    }

    public Node createTable(){
        TableView tableView = new TableView();
        ObservableList columns = tableView.getColumns();
        columns.add(createColumn("monitoringTime","Monitoring Time"));
        columns.add(createColumn("usedHeapSizeInMB","Heap Size"));
        columns.add(createColumn("threadCount","Thread Count"));
        columns.add(createColumn("peakThreadCount","Peak Thread Count"));
        columns.add(createColumn("totalErrors","Total Errors"));
        columns.add(createColumn("currentThreadBusy","Busy Threads"));
        columns.add(createColumn("committedTX","Commits"));
        columns.add(createColumn("rolledBackTX","Rollbacks"));
        columns.add(createColumn("queuedConnections","Queued Connections"));
        columns.add(createColumn("totalErrors","Total Errors"));
        columns.add(createColumn("activeSessions", "Active Sessions"));
        columns.add(createColumn("expiredSessions", "Expired Sessions"));
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setItems(this.snapshots);
        return tableView;
    }

    private TableColumn createColumn(String name,String caption) {
      TableColumn column = new TableColumn(caption);
      column.setCellValueFactory(new PropertyValueFactory<Snapshot,String>(name));
      return column;
    }
}
