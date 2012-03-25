package org.lightview.presenter;

import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.util.Pair;
import org.lightview.model.Snapshot;

/**
 *
 * @author adam bien, adam-bien.com
 */
public interface EscalationsPresenterBindings {

    ObservableMap<Pair, ObservableList<Snapshot>> getEscalations();
    
}
