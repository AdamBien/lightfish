package org.lightview.presenter;

import java.util.Map.Entry;
import java.util.*;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.concurrent.Worker;
import javafx.util.Pair;
import org.lightview.model.Script;
import org.lightview.model.Snapshot;
import org.lightview.service.ScriptManager;
import org.lightview.service.SnapshotProvider;

/**
 *
 * @author adam bien, adam-bien.com
 */
public class EscalationsPresenter implements EscalationsPresenterBindings {

    ScriptManager scriptManager;
    StringProperty uri;
    private ObservableMap<String, ObservableList<Snapshot>> escalationBindings;

    public EscalationsPresenter(StringProperty uri) {
        this.uri = uri;
        this.escalationBindings = FXCollections.observableHashMap();
    }

    void startFetching() {
        List<Pair<String, String>> scripts = getScripts();
        for (Pair<String, String> script : scripts) {
            final String scriptName = script.getKey();
            SnapshotProvider provider = new SnapshotProvider(getUri()+"/"+scriptName);
            provider.start();
            provider.valueProperty().addListener(
                    new ChangeListener<Snapshot>() {

                        @Override
                        public void changed(ObservableValue<? extends Snapshot> observable, Snapshot old, Snapshot newValue) {
                            if (newValue != null) {
                                onSnapshotArrival(scriptName, newValue);
                            }
                        }
                    });
            registerRestarting(provider);
        }
    }

    private void onSnapshotArrival(String scriptName, Snapshot newValue) {
        getSnapshots(scriptName).add(newValue);
    }

    void registerRestarting(final SnapshotProvider provider) {
        provider.stateProperty().addListener(new ChangeListener<Worker.State>() {

            @Override
            public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldState, Worker.State newState) {
                if (newState.equals(Worker.State.SUCCEEDED) || newState.equals(Worker.State.FAILED)) {
                    provider.reset();
                    provider.start();
                }
            }
        });
    }

    List<Pair<String, String>> getScripts() {
        List<Pair<String, String>> pairs = new ArrayList<>();
        List<Script> allScripts = this.scriptManager.getAllScripts();
        for (Script script : allScripts) {
            Pair pair = new Pair(script.getName(), script.getContent());
            pairs.add(pair);
        }
        return pairs;
    }

    public String getUri() {
        return this.uri.getValue();
    }

    @Override
    public ObservableMap<String, ObservableList<Snapshot>> getEscalations() {
        return escalationBindings;
    }

    ObservableList<Snapshot> getSnapshots(String scriptName) {
        ObservableList<Snapshot> escalationForScript = this.escalationBindings.get(scriptName);
        if(escalationForScript == null){
            escalationForScript = FXCollections.observableArrayList();
            this.escalationBindings.put(scriptName, escalationForScript);
        }
        return escalationForScript;
    }

}
