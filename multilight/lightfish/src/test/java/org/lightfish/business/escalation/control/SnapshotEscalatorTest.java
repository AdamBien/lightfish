package org.lightfish.business.escalation.control;

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.event.Event;
import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.lightfish.business.escalation.entity.Escalation;
import org.lightfish.business.escalation.entity.Script;
import org.lightfish.business.logging.Log;
import org.lightfish.business.servermonitoring.entity.Snapshot;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 *
 * @author adam bien, adam-bien.com
 */
public class SnapshotEscalatorTest {

    SnapshotEscalator cut;
    Escalation lastEscalation = null;

    @Before
    public void init() {
        cut = new SnapshotEscalator();
        cut.escalationSink = mock(Event.class);
        cut.messageProcessor = mock(EscalationMessageProcessor.class);
        when(cut.messageProcessor.processBasicMessage(anyString(), (Snapshot) anyObject()))
                .thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                return (String) invocation.getArguments()[0];
            }
        });
        doAnswer(
                new Answer() {
                    @Override
                    public Object answer(InvocationOnMock invocation) throws Throwable {
                        lastEscalation = (Escalation) invocation.getArguments()[0];
                        return null;
                    }
                }).when(
                this.cut.escalationSink).fire((Escalation) anyObject());
        cut.scripting = mock(ScriptStore.class);
        cut.LOG = new Log();

        cut.initScripting();
    }

    @Test
    public void firstEscalation() throws ScriptException {
        Snapshot snapshot = new Snapshot.Builder().instanceName("something").build();
        when(cut.scripting.activeScripts()).thenReturn(scripts(""));
        this.cut.escalate(snapshot);


        this.cut.scriptEngine = mock(ScriptEngine.class);
        verify(
                this.cut.scriptEngine, never()).eval(any(String.class), any(Bindings.class));
    }

    @Test
    public void escalation() throws ScriptException {
        Snapshot snapshot = new Snapshot.Builder().instanceName("something").build();
        when(cut.scripting.activeScripts()).thenReturn(scripts("true"));
        this.cut.escalate(snapshot);
        this.cut.escalate(snapshot); //second invocation needed to enable evaluation
        verify(this.cut.escalationSink).fire((Escalation) anyObject());
        assertEquals(snapshot, lastEscalation.getSnapshot());
    }

    @Test
    public void escalation_with_message() throws ScriptException {
        String expectedMessage = "I am expected";
        Snapshot snapshot = new Snapshot.Builder().instanceName("something").build();
        when(cut.scripting.activeScripts()).thenReturn(scripts("true", expectedMessage));
        this.cut.escalate(snapshot);
        this.cut.escalate(snapshot); //second invocation needed to enable evaluation
        verify(this.cut.escalationSink).fire((Escalation) anyObject());
        assertEquals(expectedMessage, lastEscalation.getBasicMessage());
        assertEquals(snapshot, lastEscalation.getSnapshot());
    }

    @Test
    public void noEscalation() throws ScriptException {
        Snapshot snapshot = new Snapshot.Builder().instanceName("something").build();
        when(cut.scripting.activeScripts()).thenReturn(scripts("false"));
        this.cut.escalate(snapshot);
        this.cut.escalate(snapshot); //second invocation needed to enable evaluation
        verify(this.cut.escalationSink, never()).fire((Escalation) anyObject());
        assertNull(lastEscalation);
    }

    @Test
    public void snapshotDependentEscalation() throws ScriptException {
        Snapshot snapshot = new Snapshot.Builder().instanceName("something").committedTX(1).build();
        when(cut.scripting.activeScripts()).thenReturn(scripts("current.committedTX == 1"));
        this.cut.escalate(snapshot);
        this.cut.escalate(snapshot); //second invocation needed to enable evaluation
        verify(this.cut.escalationSink).fire((Escalation) anyObject());
        assertEquals(snapshot, lastEscalation.getSnapshot());
    }

    @Test
    public void subequentEscalation() throws ScriptException {
        this.firstEscalation();
        when(this.cut.scriptEngine.createBindings()).thenReturn(mock(Bindings.class));
        Snapshot snapshot = new Snapshot.Builder().instanceName("something").build();
        this.cut.escalate(snapshot);
        verify(this.cut.scriptEngine).eval(any(String.class), any(Bindings.class));
    }

    @Test
    public void convert() {
        assertFalse(this.cut.canBeConvertedToTrue(null));
        assertFalse(this.cut.canBeConvertedToTrue(false));
        assertFalse(this.cut.canBeConvertedToTrue(Boolean.FALSE));
        assertFalse(this.cut.canBeConvertedToTrue("hugo"));

        assertTrue(this.cut.canBeConvertedToTrue(true));
        assertTrue(this.cut.canBeConvertedToTrue(Boolean.TRUE));
    }

    public List<Script> scripts(String scriptContent) {
        return scripts(scriptContent, "no message");
    }

    public List<Script> scripts(String scriptContent, String message) {
        Script script = new Script();
        script.setActive(true);
        script.setName("name " + scriptContent);
        script.setBasicMessage(message);
        script.setContent(scriptContent);
        List<Script> scripts = new ArrayList<>();
        scripts.add(script);
        return scripts;
    }
}
