package org.lightview.view;

import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.collections.SetChangeListener.Change;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import org.lightview.model.Application;

/**
 *
 * @author adam-bien.com
 */
public class ApplicationList {
    private ObservableSet<Application> applications;

    public ApplicationList(ObservableSet<Application> applications) {
        this.applications = applications;
    }

    public Node view(){
        ListView<String> listView = new ListView<>();
        final ObservableList<String> items = listView.getItems();
        
        this.applications.addListener(new SetChangeListener<Application>(){

            @Override
            public void onChanged(Change<? extends Application> change) {
                if(change.wasAdded()){
                    items.add(change.getElementAdded().getApplicationName());
                }
                if(change.wasRemoved()){
                    items.remove(change.getElementRemoved().getApplicationName());
                }
            }
        
        });
        return listView;
    }
    
}
