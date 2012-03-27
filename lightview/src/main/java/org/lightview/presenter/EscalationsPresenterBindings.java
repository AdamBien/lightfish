package org.lightview.presenter;

import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import org.lightview.model.Snapshot;

/**
 *
 * @author adam bien, adam-bien.com
 */
public interface EscalationsPresenterBindings {

    ObservableMap<String, ObservableList<Snapshot>> getEscalations();
    void deleteScript(String name);
    void newScript(String name,String content);
    
}
