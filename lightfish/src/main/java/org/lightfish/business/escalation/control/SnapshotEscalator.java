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
import javax.script.ScriptException;
import org.lightfish.business.escalation.entity.Escalation;
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
    ScriptStore scripting;
    @Inject
    EscalationMessageProcessor messageProcessor;
    @Inject
    @Severity(Severity.Level.ESCALATION)
    Event<Escalation> escalationSink;

    @PostConstruct
    public void initScripting() {
        
        ScriptEngineManager engineManager = new ScriptEngineManager();
        this.scriptEngine = engineManager.getEngineByName(ENGINE_NAME);
        if (this.scriptEngine == null) {
            throw new IllegalStateException("Cannot create ScriptEngine: " + ENGINE_NAME);
        }
        LOG.info("Loaded script engine: " + ENGINE_NAME);
    }

    public void escalate(@Observes @Severity(Severity.Level.HEARTBEAT) Snapshot current) {
        List<Script> scripts = this.scripting.activeScripts();
        try {
            Bindings binding = this.scriptEngine.createBindings();
            binding.put("current", current);
            binding.put("previous", recent);
            long start = System.currentTimeMillis();
            try {
                if (recent != null) {
                    for (Script script : scripts) {
                        Object retVal = null;
                        try {
                            final String content = script.getContent();
                            if(content == null)
                                return;
                            retVal = this.scriptEngine.eval(content, binding);
                        } catch (ScriptException scriptException) {
                            LOG.error("Cannot evaluate script: " + script, scriptException);
                        }
                        if (canBeConvertedToTrue(retVal)) {
                            String basicMessage = messageProcessor.processBasicMessage(script.getBasicMessage(), current);
                            String richMessage = messageProcessor.processRichMessage(script.getRichMessage(), current);
                            Escalation escalation = new Escalation.Builder()
                                    .channel(script.getName())
                                    .basicMessage(basicMessage)
                                    .richMessage(richMessage)
                                    .snapshot(current)
                                    .build();
                            escalationSink.fire(escalation);
                            LOG.info("Escalated: " + script + " for snapshot: " + current + " and recent: " + recent);
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

    boolean canBeConvertedToTrue(Object retVal) {
        if (retVal == null) {
            return false;
        }
        if (!(retVal instanceof Boolean)) {
            return false;
        }
        return (boolean) retVal;
    }
}
