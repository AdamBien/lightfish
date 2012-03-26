package org.lightview.view;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.lightview.service.ScriptManager;
import org.lightview.view.Grid;

/**
 *
 * @author adam bien, adam-bien.com
 */
public class Escalations {
    
    private TabPane pane;
    private final ObservableList<String> scripts;
    
    public Escalations(ObservableList<String> scripts) {
        this.scripts = scripts;
        this.initTabs();
    }
    
    


    private void initTabs() {
        Grid grid = new Grid(null);
        for (String scriptName : scripts) {
            createTab(pane, scriptName);
            pane.getTabs().add(null );
        }
    }
    private Tab createTab(Node content, String caption) {
        Tab tab = new Tab();
        tab.setContent(content);
        tab.setText(caption);
        return tab;
    }
}
