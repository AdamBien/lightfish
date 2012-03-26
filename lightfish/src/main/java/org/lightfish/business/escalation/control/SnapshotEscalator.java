package org.lightfish.business.escalation.control;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import org.lightfish.business.escalation.entity.Script;
import org.lightfish.business.logging.Log;
import org.lightfish.business.monitoring.boundary.Severity;
import org.lightfish.business.monitoring.entity.Snapshot;

/**
 *
 * @author adam bien, adam-bien.com
 */
@Singleton
public class SnapshotEscalator {

    private static final String ENGINE_NAME = "JavaScript";
    Snapshot recent;
    ScriptEngine scriptEngine;
    @Inject
    Log LOG;
    @Inject
    Scripting scripting;
    @Inject
    @Severity(Severity.Level.ESCALATION)
    Event<Snapshot> escalationSink;

    @PostConstruct
    public void initScripting() {
        ScriptEngineManager engineManager = new ScriptEngineManager();
        this.scriptEngine = engineManager.getEngineByName(ENGINE_NAME);
        if (this.scriptEngine == null) {
            throw new IllegalStateException("Cannot create ScriptEngine: " + ENGINE_NAME);
        }
    }

    public void escalate(@Observes @Severity(Severity.Level.HEARTBEAT) Snapshot current) {
        List<Script> scripts = this.scripting.activeScripts();
        try {
            Bindings binding = this.scriptEngine.createBindings();
            binding.put("current", current);
            binding.put("recent", recent);
            long start = System.currentTimeMillis();
            try {
                if (recent != null) {
                    for (Script script : scripts) {
                        Object retVal = this.scriptEngine.eval(script.getContent(), binding);
                        if (convert(retVal)) {
                            escalationSink.fire(current);
                            current.setEscalated(true);
                        }
                    }
                }

            } finally {
                LOG.info("Performance: " + (System.currentTimeMillis() - start));
            }
            this.recent = current;
        } catch (Exception e) {
            throw new IllegalStateException("Exception during script evaluation: " + e, e);
        }
    }

    boolean convert(Object retVal) {
        if (retVal == null) {
            return false;
        }
        if (!(retVal instanceof Boolean)) {
            return false;
        }
        return (boolean) retVal;
    }
}
