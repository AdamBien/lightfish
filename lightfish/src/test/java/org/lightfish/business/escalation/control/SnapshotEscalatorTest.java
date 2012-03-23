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
import org.lightfish.business.escalation.entity.Script;
import org.lightfish.business.logging.Log;
import org.lightfish.business.monitoring.entity.Snapshot;
import static org.mockito.Mockito.*;
/**
 *
 * @author adam bien, adam-bien.com
 */
public class SnapshotEscalatorTest {
    
    SnapshotEscalator cut;

    @Before
    public void init(){
        cut = new SnapshotEscalator();
        cut.escalationSink = mock(Event.class);
        cut.scripting = mock(Scripting.class);
        cut.LOG = new Log();
        cut.initScripting();
    }

    @Test
    public void firstEscalation() throws ScriptException{
        Snapshot snapshot = new Snapshot();
        when(cut.scripting.activeScripts()).thenReturn(scripts(""));
        this.cut.escalate(snapshot);
        this.cut.scriptEngine = mock(ScriptEngine.class);
        verify(this.cut.scriptEngine,never()).eval(any(String.class), any(Bindings.class));
    }

    @Test
    public void escalation() throws ScriptException{
        Snapshot snapshot = new Snapshot();
        when(cut.scripting.activeScripts()).thenReturn(scripts("true"));
        this.cut.escalate(snapshot);
        this.cut.escalate(snapshot); //second invocation needed to enable evaluation
        verify(this.cut.escalationSink).fire(snapshot);
    }

    @Test
    public void noEscalation() throws ScriptException{
        Snapshot snapshot = new Snapshot();
        when(cut.scripting.activeScripts()).thenReturn(scripts("false"));
        this.cut.escalate(snapshot);
        this.cut.escalate(snapshot); //second invocation needed to enable evaluation
        verify(this.cut.escalationSink,never()).fire(snapshot);
    }

    @Test
    public void snapshotDependentEscalation() throws ScriptException{
        Snapshot snapshot = new Snapshot.Builder().committedTX(1).build();
        when(cut.scripting.activeScripts()).thenReturn(scripts("current.committedTX == 1"));
        this.cut.escalate(snapshot);
        this.cut.escalate(snapshot); //second invocation needed to enable evaluation
        verify(this.cut.escalationSink).fire(snapshot);
    }

    @Test
    public void subequentEscalation() throws ScriptException{
        this.firstEscalation();
        when(this.cut.scriptEngine.createBindings()).thenReturn(mock(Bindings.class));
        Snapshot snapshot = new Snapshot();
        this.cut.escalate(snapshot);
        verify(this.cut.scriptEngine).eval(any(String.class), any(Bindings.class));
    }
    
    @Test
    public void convert() {
        assertFalse(this.cut.convert(null));
        assertFalse(this.cut.convert(false));
        assertFalse(this.cut.convert(Boolean.FALSE));
        assertFalse(this.cut.convert("hugo"));
        
        assertTrue(this.cut.convert(true));
        assertTrue(this.cut.convert(Boolean.TRUE));
    }
    
    
    public List<Script> scripts(String scriptContent){
        Script script = new Script();
        script.setActive(true);
        script.setName("name " + scriptContent);
        script.setContent(scriptContent);
        List<Script> scripts = new ArrayList<>();
        scripts.add(script);
        return scripts;
    }
}
