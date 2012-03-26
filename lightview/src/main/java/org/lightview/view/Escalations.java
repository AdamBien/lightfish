package org.lightview.view;

import java.util.Map.Entry;
import java.util.Set;
import javafx.collections.MapChangeListener;
import javafx.collections.MapChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.lightview.model.Snapshot;
import org.lightview.presenter.EscalationsPresenterBindings;
import org.lightview.service.ScriptManager;
import org.lightview.view.Grid;

/**
 *
 * @author adam bien, adam-bien.com
 */
public class Escalations {
    
    private TabPane pane;
    private final ObservableMap<String, ObservableList<Snapshot>> escalations;
    private final Node firstTab;

    public Escalations(Node firstTab,EscalationsPresenterBindings epb) {
        this.escalations = epb.getEscalations();
        this.firstTab = firstTab;
        this.initialSetup();
        this.registerForChanges();
    }

    private void initialSetup() {
        pane.getTabs().add(createTab("Live Stream",firstTab));
        Set<Entry<String, ObservableList<Snapshot>>> entrySet = this.escalations.entrySet();
        for (Entry<String, ObservableList<Snapshot>> escalation : entrySet) {
            String title = escalation.getKey();
            ObservableList<Snapshot> snapshots = escalation.getValue();
            Grid grid = new Grid(snapshots);
            Tab escalationTab = createTab(title,grid.createTable());
            pane.getTabs().add(escalationTab);
        }
    }

    private Tab createTab(String caption,Node content) {
        Tab tab = new Tab();
        tab.setContent(content);
        tab.setText(caption);
        return tab;
    }

    private void registerForChanges() {
        this.escalations.addListener(new MapChangeListener<String, ObservableList<Snapshot>>(){

            @Override
            public void onChanged(Change<? extends String, ? extends ObservableList<Snapshot>> change) {
                String name = change.getKey();
                if(change.wasRemoved()){
                    remove(name,change.getValueRemoved());
                }
                if(change.wasAdded()){
                    add(name,change.getValueAdded());
                }
            }
        
        });
    }
    
    void remove(String name,ObservableList<Snapshot> change){
        System.out.println("Sync: remove: " + name + change);
    }

    void add(String name,ObservableList<Snapshot> change){
        System.out.println("Sync: add: " + name + change);
    }
    
    public Node view(){
        return this.pane;
    }
}
