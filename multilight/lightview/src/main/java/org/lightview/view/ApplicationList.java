package org.lightview.view;

import java.util.Collections;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.collections.SetChangeListener.Change;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.HBox;
import org.lightview.model.Application;

/**
 *
 * @author adam-bien.com
 */
public class ApplicationList {

    private ObservableSet<Application> applications;
    private ListView<String> componentsList;
    private final ObservableList<String> items;

    public ApplicationList(ObservableSet<Application> applications) {
        this.applications = applications;
        this.componentsList = new ListView<>();
        this.items = this.componentsList.getItems();
    }

    public Node view() {
        HBox view = new HBox();
        ListView<String> applicationsList = new ListView<>();
        MultipleSelectionModel<String> selectionModel = applicationsList.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        selectionModel.selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String newSelection) {
                items.clear();
                items.addAll(getComponentsForApplication(newSelection));
            }
        });
        view.getChildren().add(applicationsList);
        view.getChildren().add(componentsList);

        final ObservableList<String> items = applicationsList.getItems();

        this.applications.addListener(new SetChangeListener<Application>() {
            @Override
            public void onChanged(Change<? extends Application> change) {
                if (change.wasAdded()) {
                    items.add(change.getElementAdded().getApplicationName());
                }
                if (change.wasRemoved()) {
                    items.remove(change.getElementRemoved().getApplicationName());
                }
            }
        });
        return view;
    }

    private List<String> getComponentsForApplication(String newSelection) {
        for (Application application : applications) {
            if(application.getApplicationName().equalsIgnoreCase(newSelection)){
                return application.getComponents();
            }
        }
        return Collections.EMPTY_LIST;
    }
}
