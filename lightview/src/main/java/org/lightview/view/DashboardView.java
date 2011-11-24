package org.lightview.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.lightview.entity.Snapshot;

/**
 * User: blog.adam-bien.com
 * Date: 18.11.11
 * Time: 17:19
 */
public class DashboardView implements SnapshotListener{

    DashboardPresenter dashboardPresenter;
    Stage stage;
    private TextField txtUri;
    private BrowserView browserView;
    private SnapshotView heapView;
    private SnapshotView threadCountView;
    private SnapshotView busyThreadView;
    private SnapshotView queuedConnectionsView;
    private VBox horizontal;
    private SnapshotView commitCountView;
    private SnapshotView rollbackCountView;
    private SnapshotView totalErrorsView;
    private GridView gridView;
    private WebView browserViewView;

    public DashboardView(Stage stage,DashboardPresenter dashboardPresenter) {
        this.dashboardPresenter = dashboardPresenter;
        this.dashboardPresenter.addSnapshotsObserver(this);
        this.stage = stage;
        this.createViews();
        this.open();
        this.bind();
    }

    private void bind() {
        this.dashboardPresenter.getUriProperty().bind(txtUri.textProperty());
        this.browserView.getUriProperty().bind(txtUri.textProperty());
    }

    private void createViews() {
        this.horizontal = new VBox();
        this.horizontal.setPadding(new Insets(10, 10, 10, 10));
        this.horizontal.getChildren().add(createURIInputView());
        HBox threadsAndMemory = new HBox();
        threadsAndMemory.setPadding(new Insets(10, 10, 10, 10));

        HBox suspicious = new HBox();
        suspicious.setPadding(new Insets(10, 10, 10, 10));

        this.browserView = new BrowserView();
        this.horizontal.getChildren().add(browserView.view());
        this.heapView = new SnapshotView("Heap Size","Used Heap",null);
        this.threadCountView = new SnapshotView("Thread Count","Threads",null);
        this.busyThreadView = new SnapshotView("Busy Thread Count","Threads",null);

        threadsAndMemory.getChildren().add(this.heapView.createChart());
        threadsAndMemory.getChildren().add(this.threadCountView.createChart());
        this.horizontal.getChildren().add(threadsAndMemory);

        HBox transactions = new HBox();
        transactions.setPadding(new Insets(10, 10, 10, 10));
        this.commitCountView = new SnapshotView("TX Commit","#",null);
        this.rollbackCountView = new SnapshotView("TX Rollback","#",null);

        transactions.getChildren().add(this.commitCountView.createChart());
        transactions.getChildren().add(this.rollbackCountView.createChart());

        this.queuedConnectionsView = new SnapshotView("Queued Connections","Connections",null);
        this.totalErrorsView = new SnapshotView("Errors","#",null);
        suspicious.getChildren().add(this.queuedConnectionsView.createChart());
        suspicious.getChildren().add(this.totalErrorsView.createChart());

        this.gridView = new GridView(this.dashboardPresenter.getSnapshots());
        this.horizontal.getChildren().addAll(transactions,this.busyThreadView.createChart(),suspicious,this.gridView.createTable());
    }

    private Node createURIInputView() {
        final Button button = new Button();
        button.setText("Minimize Browser");
        button.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                toggleBrowserSize(button);
            }
        });
        HBox hBox = HBoxBuilder.create().spacing(10).build();
        this.txtUri = TextFieldBuilder.create().editable(true).text("http://localhost:8080/lightfish/live").prefColumnCount(40).minHeight(20).build();
        Label uri = LabelBuilder.create().labelFor(txtUri).text("LightFish location:").build();
        hBox.getChildren().addAll(uri, txtUri,button);
        return hBox;
    }

    private void toggleBrowserSize(Button button) {
        boolean minimized = this.browserView.toggleMinimize();
        if(minimized){
            button.setText("Maximize Browser");
        }else{
            button.setText("Minimize Browser");
        }
    }

    public void open(){
        Scene scene = new Scene(this.horizontal);
        stage.setFullScreen(true);
        stage.setScene(scene);
        stage.show();
    }

    public void onSnapshotArrival(Snapshot snapshot) {
        String id = String.valueOf(snapshot.getId());
        this.heapView.onNewEntry(id, snapshot.getUsedHeapSizeInMB());
        this.threadCountView.onNewEntry(id,snapshot.getThreadCount());
        this.busyThreadView.onNewEntry(id,snapshot.getCurrentThreadBusy());
        this.queuedConnectionsView.onNewEntry(id,snapshot.getQueuedConnections());
        this.commitCountView.onNewEntry(id,snapshot.getCommittedTX());
        this.rollbackCountView.onNewEntry(id,snapshot.getRolledBackTX());
        this.totalErrorsView.onNewEntry(id,snapshot.getTotalErrors());
    }
}
