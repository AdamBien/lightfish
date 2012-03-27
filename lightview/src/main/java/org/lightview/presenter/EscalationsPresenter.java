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
public final class EscalationsPresenter implements EscalationsPresenterBindings {
    public static final String ESCALATIONS_URI = "/escalations/";

    ScriptManager scriptManager;
    StringProperty uri;
    private ObservableMap<String, ObservableList<Snapshot>> escalationBindings;
    private List<SnapshotProvider> runningServices;
    
    public EscalationsPresenter(StringProperty uri) {
        this.uri = uri;
        this.escalationBindings = FXCollections.observableHashMap();
        this.runningServices = new ArrayList<>();
        registerURIListener();
    }
    
   void resyncActiveScripts(){
      List<Script> scripts = this.scriptManager.getAllScripts();
       for (Script script : scripts) {
           final String name = script.getName();
           this.escalationBindings.put(name, getSnapshots(name));
       }

   }

    void registerURIListener() {
        this.uri.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String uri) {
                System.out.println("Uri changed to: " + uri);
                if(uri != null){
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
            SnapshotProvider provider = new SnapshotProvider(getUri() + ESCALATIONS_URI + scriptName);
            this.runningServices.add(provider);
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
    
   void restartServices() {
       for (SnapshotProvider snapshotProvider : runningServices) {
           restartService(snapshotProvider);
       }
        this.startFetching();
   }
   
   void restartService(SnapshotProvider service){
        if (service != null && service.isRunning()) {
            service.cancel();
            service.reset();
        }
   }

    private void onSnapshotArrival(String scriptName, Snapshot newValue) {
        System.out.println("Arrived: " + scriptName + " " + newValue);
        resyncActiveScripts();
        ObservableList<Snapshot> snapshots = getSnapshots(scriptName);
        if(!snapshots.contains(newValue)){
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
}
