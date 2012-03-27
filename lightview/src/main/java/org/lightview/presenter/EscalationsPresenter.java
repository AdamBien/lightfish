package org.lightview.presenter;

import java.util.*;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.concurrent.Worker;
import org.lightview.model.Script;
import org.lightview.model.Snapshot;
import org.lightview.service.ScriptManager;
import org.lightview.service.SnapshotProvider;

/**
 *
 * @author adam bien, adam-bien.com
 */
public final class EscalationsPresenter implements EscalationsPresenterBindings {

    public static final String ESCALATIONS_URI = "/escalations/";
    ScriptManager scriptManager;
    StringProperty uri;
    private ObservableMap<String, ObservableList<Snapshot>> escalationBindings;
    private Map<String, SnapshotProvider> runningServices;

    public EscalationsPresenter(StringProperty uri) {
        this.uri = uri;
        this.escalationBindings = FXCollections.observableHashMap();
        this.runningServices = new HashMap<>();
        registerURIListener();
    }

    void resyncActiveScripts() {
        List<Script> scripts = this.scriptManager.getAllScripts();
        Set<String> keySet = this.escalationBindings.keySet();
        for (String scriptName : keySet) {
            if (!scriptExists(scripts, scriptName)) {
                //stop service
                removeEscalation(scriptName);
            }
        }
        for (Script script : scripts) {
            final String name = script.getName();
            if (!this.escalationBindings.containsKey(name)) {
                this.escalationBindings.put(name, getSnapshots(name));
                this.registerService(name);
            }
        }


    }

    void registerURIListener() {
        this.uri.addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String uri) {
                System.out.println("Uri changed to: " + uri);
                if (uri != null) {
                    reinitializeScriptManager(uri);
                    restartServices();
                }
            }
        });
    }

    void startFetching() {
        runningServices.clear();
        List<Script> scripts = this.scriptManager.getAllScripts();
        System.out.println("Scripts: " + scripts);
        for (Script script : scripts) {
            final String scriptName = script.getName();
            registerService(scriptName);
        }
    }

    void registerService(final String scriptName) {
        SnapshotProvider provider = new SnapshotProvider(getUri() + ESCALATIONS_URI + scriptName);
        this.runningServices.put(scriptName, provider);
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

    void restartServices() {
        for (SnapshotProvider snapshotProvider : runningServices.values()) {
            resetService(snapshotProvider);
        }
        this.startFetching();
    }

    void resetService(SnapshotProvider service) {
        if (service != null && service.isRunning()) {
            service.cancel();
            service.reset();
        }
    }

    private void onSnapshotArrival(String scriptName, Snapshot newValue) {
        System.out.println("Arrived: " + scriptName + " " + newValue);
        resyncActiveScripts();
        ObservableList<Snapshot> snapshots = getSnapshots(scriptName);
        if (!snapshots.contains(newValue)) {
            snapshots.add(newValue);
        }
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

    public String getUri() {
        return this.uri.getValue();
    }

    @Override
    public ObservableMap<String, ObservableList<Snapshot>> getEscalations() {
        return escalationBindings;
    }

    ObservableList<Snapshot> getSnapshots(String scriptName) {
        ObservableList<Snapshot> escalationForScript = this.escalationBindings.get(scriptName);
        if (escalationForScript == null) {
            escalationForScript = FXCollections.observableArrayList();
            this.escalationBindings.put(scriptName, escalationForScript);
        }
        return escalationForScript;
    }

    void reinitializeScriptManager(String uri) {
        this.scriptManager = new ScriptManager(uri);
    }

    @Override
    public void deleteScript(String name) {
        this.scriptManager.deleteScript(name);
    }

    @Override
    public void newScript(String name, String content) {
        this.scriptManager.registerNewScript(new Script(name, content, true));
    }

    boolean scriptExists(List<Script> scripts, String scriptName) {
        for (Script script : scripts) {
            if (script.getName().equalsIgnoreCase(scriptName)) {
                return true;
            }
        }
        return false;
    }

    void removeEscalation(String scriptName) {

    }
}
