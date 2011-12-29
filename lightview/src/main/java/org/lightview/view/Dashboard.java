package org.lightview.view;

import javafx.beans.property.ReadOnlyLongProperty;
import javafx.collections.MapChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.lightview.presenter.ConnectionPoolBindings;
import org.lightview.presenter.DashboardPresenterBindings;

/**
 * User: blog.adam-bien.com
 * Date: 18.11.11
 * Time: 17:19
 */
public class Dashboard {

    DashboardPresenterBindings dashboardPresenter;
    Stage stage;
    private TextField txtUri;
    private Browser browser;
    private Snapshot heap;
    private Snapshot threadCount;
    private Snapshot busyThread;
    private Snapshot queuedConnections;
    private VBox vertical;
    private Snapshot commitCount;
    private Snapshot rollbackCount;
    private Snapshot totalErrors;
    private Snapshot activeSessions;
    private Snapshot expiredSessions;
    private Grid grid;
    private TabPane tabPane;
    private Snapshot peakThreadCount;
    private Node uriInputView;

    public Dashboard(Stage stage, DashboardPresenterBindings dashboardPresenter) {
        this.dashboardPresenter = dashboardPresenter;
        this.stage = stage;
        this.tabPane = new TabPane();
        this.createViews();
        this.bind();
        this.open();
    }

    private void instantiateViews() {
        this.uriInputView = createURIInputView();
        this.browser = new Browser();
        ReadOnlyLongProperty id = this.dashboardPresenter.getId();
        this.heap = new Snapshot(id,"Heap Size","Used Heap");
        this.threadCount = new Snapshot(id,"Thread Count","Threads");
        this.peakThreadCount = new Snapshot(id,"Peak Thread Count", "Threads");
        this.busyThread = new Snapshot(id,"Busy Thread Count","Threads");
        this.commitCount = new Snapshot(id,"TX Commit","#");
        this.rollbackCount = new Snapshot(id,"TX Rollback","#");
        this.totalErrors = new Snapshot(id,"Errors","#");
        this.queuedConnections = new Snapshot(id,"Queued Connections","Connections");
        this.activeSessions = new Snapshot(id,"HTTP Sessions","#");
        this.expiredSessions = new Snapshot(id,"Expired Sessions","#");
        this.grid = new Grid(this.dashboardPresenter.getSnapshots());
    }

    private void createViews() {
        this.vertical = new VBox();
        HBox threadsAndMemory = new HBox();
        HBox paranormal = new HBox();
        HBox transactions = new HBox();
        HBox web = new HBox();

        String hBoxClass = "boxSpacing";
        this.vertical.getStyleClass().add(hBoxClass);
        threadsAndMemory.getStyleClass().add(hBoxClass);
        paranormal.getStyleClass().add(hBoxClass);
        transactions.getStyleClass().add(hBoxClass);
        web.getStyleClass().add(hBoxClass);

        instantiateViews();

        threadsAndMemory.getChildren().addAll(this.heap.view(), this.threadCount.view(), this.peakThreadCount.view());
        transactions.getChildren().addAll(this.commitCount.view(), this.rollbackCount.view());
        paranormal.getChildren().addAll(this.queuedConnections.view(), this.totalErrors.view(), this.busyThread.view());
        web.getChildren().addAll(this.activeSessions.view());
        web.getChildren().addAll(this.expiredSessions.view());

        Tab threadsAndMemoryTab = createTab(threadsAndMemory,"Threads And Memory");
        Tab transactionsTab = createTab(transactions, "Transactions");
        Tab paranormalTab = createTab(paranormal,"Paranormal Activity");
        Tab webTab = createTab(web,"Web");
        this.tabPane.getTabs().addAll(threadsAndMemoryTab, transactionsTab, paranormalTab,webTab);

        this.vertical.getChildren().addAll(uriInputView, this.browser.view(), this.tabPane, this.grid.createTable());
    }

    private void bind() {
        this.dashboardPresenter.getUriProperty().bind(txtUri.textProperty());
        this.browser.getURI().bind(txtUri.textProperty());
        this.heap.value().bind(this.dashboardPresenter.getUsedHeapSizeInMB());

        this.threadCount.value().bind(this.dashboardPresenter.getThreadCount());
        this.busyThread.value().bind(this.dashboardPresenter.getBusyThreads());
        this.peakThreadCount.value().bind(this.dashboardPresenter.getPeakThreadCount());

        this.commitCount.value().bind(this.dashboardPresenter.getCommitCount());
        this.rollbackCount.value().bind(this.dashboardPresenter.getRollbackCount());

        this.queuedConnections.value().bind(this.dashboardPresenter.getQueuedConnections());
        this.totalErrors.value().bind(this.dashboardPresenter.getTotalErrors());
        this.activeSessions.value().bind(this.dashboardPresenter.getActiveSessions());
        this.expiredSessions.value().bind(this.dashboardPresenter.getExpiredSessions());

        this.dashboardPresenter.getPools().addListener(new MapChangeListener<String, ConnectionPoolBindings>() {
            public void onChanged(Change<? extends String, ? extends ConnectionPoolBindings> change) {
                ConnectionPoolBindings valueAdded = change.getValueAdded();
                if(valueAdded != null)
                    createPoolTab(valueAdded);
            }
        });
    }


    private Tab createTab(Node content, String caption) {
        Tab tab = new Tab();
        tab.setContent(content);
        tab.setText(caption);
        return tab;
    }

    void createPoolTab(ConnectionPoolBindings valueAdded) {
        ReadOnlyLongProperty id = this.dashboardPresenter.getId();
        String jndiName = valueAdded.getJndiName().get();
        ConnectionPool connectionPool = new ConnectionPool(id,valueAdded);
        Node view = connectionPool.view();
        Tab tab = createTab(view, "Resource: " + jndiName);
        this.tabPane.getTabs().add(tab);
    }

    private Node createURIInputView() {
        final Button button = new Button();
        button.setText("-");
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
        boolean minimized = this.browser.toggleMinimize();
        if(minimized){
            button.setText("+");
        }else{
            button.setText("-");
        }
    }
    public void open(){
        Scene scene = new Scene(this.vertical);
        scene.getStylesheets().add(this.getClass().getResource("lightfish.css").toExternalForm());
        stage.setFullScreen(false);
        stage.setScene(scene);
        stage.show();
    }
}
