package org.lightview.view;

import java.util.Map.Entry;
import java.util.Set;
import javafx.collections.MapChangeListener;
import javafx.collections.MapChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.lightview.model.Snapshot;
import org.lightview.presenter.EscalationsPresenterBindings;

/**
 *
 * @author adam bien, adam-bien.com
 */
public class Escalations {

    private TabPane pane;
    private final ObservableMap<String, ObservableList<Snapshot>> escalations;
    private final Node firstTab;
    private final EscalationsPresenterBindings presenter;

    public Escalations(Node firstTab, EscalationsPresenterBindings epb) {
        this.presenter = epb;
        this.pane = new TabPane();
        this.escalations = epb.getEscalations();
        this.firstTab = firstTab;
        this.initialSetup();
        this.registerForChanges();
    }

    private void initialSetup() {
        final Tab liveStream = createTab("Live Stream", firstTab);
        liveStream.setClosable(false);
        registerNewScriptMenu(liveStream);
        pane.getTabs().add(liveStream);
        Set<Entry<String, ObservableList<Snapshot>>> entrySet = this.escalations.entrySet();
        for (Entry<String, ObservableList<Snapshot>> escalation : entrySet) {
            String title = escalation.getKey();
            ObservableList<Snapshot> snapshots = escalation.getValue();
            addGrid(title, snapshots);
        }
    }

    private Tab createTab(String caption, Node content) {
        Tab tab = new Tab();
        tab.setContent(content);
        tab.setText(caption);
        return tab;
    }

    private void registerForChanges() {
        this.escalations.addListener(new MapChangeListener<String, ObservableList<Snapshot>>() {

            @Override
            public void onChanged(Change<? extends String, ? extends ObservableList<Snapshot>> change) {
                String name = change.getKey();
                if (change.wasAdded() && change.wasRemoved()) {
                    return;
                }
                if (change.wasRemoved()) {
                    remove(name, change.getValueRemoved());
                }
                if (change.wasAdded()) {
                    add(name, change.getValueAdded());
                }
            }
        });
    }

    void remove(String name, ObservableList<Snapshot> change) {
        System.out.println("Sync: remove " + name + change);
    }

    void add(String name, ObservableList<Snapshot> change) {
        System.out.println("Sync: add: " + name + change);
        addGrid(name, change);
    }

    public Node view() {
        return this.pane;
    }

    void addGrid(String title, ObservableList<Snapshot> snapshots) {
        Grid grid = new Grid(snapshots);
        Tab escalationTab = createTab(title, grid.createTable());
        registerNewScriptMenu(escalationTab);
        registerDeleteScriptMenu(escalationTab);
        pane.getTabs().add(escalationTab);
    }

    void registerNewScriptMenu(final Tab tab) {
        ContextMenu contextMenu = tab.getContextMenu();
        if (contextMenu == null) {
            contextMenu = new ContextMenu();
        }
        MenuItem newScript = new MenuItem("New Script");
        newScript.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                openNewScriptDialog();
            }
        });
        contextMenu.getItems().addAll(newScript);
        tab.setContextMenu(contextMenu);
    }

    void registerDeleteScriptMenu(final Tab tab) {
        ContextMenu contextMenu = tab.getContextMenu();
        if (contextMenu == null) {
            contextMenu = new ContextMenu();
        }
        MenuItem newScript = new MenuItem("Delete");
        newScript.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                String scriptName = tab.getText();
                presenter.deleteScript(scriptName);
                pane.getTabs().remove(tab);
            }
        });
        contextMenu.getItems().addAll(newScript);
        tab.setContextMenu(contextMenu);
    }

    private void openNewScriptDialog() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        
        HBox nameBox = new HBox(10);
        final TextField name = new TextField();
        name.setText("Errors");
        nameBox.getChildren().addAll(new Text("Name:"), name);
        

        final TextArea content = new TextArea();
        String msg = getDefaultScript();
        content.setText(msg);
        Button save = new Button("Save");
        save.setDefaultButton(true);
        save.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                String nameText = name.getText();
                String contentText = content.getText();
                presenter.newScript(nameText, contentText);
            }
        });
        VBox vbox = VBoxBuilder.create().
                             children(nameBox, content, save).
                             alignment(Pos.CENTER).spacing(10).padding(new Insets(10)).build();
        final Scene scene = new Scene(vbox);
        name.requestFocus();
        dialogStage.setScene(scene);
        dialogStage.setTitle("New Script");
        dialogStage.show();
    }

    String getDefaultScript() {
       return "(current.totalErrors - previous.totalErrors) == 0";
    }
}
