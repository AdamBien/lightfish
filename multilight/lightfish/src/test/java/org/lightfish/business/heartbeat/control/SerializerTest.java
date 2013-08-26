package org.lightfish.business.heartbeat.control;

import java.io.StringWriter;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.lightfish.business.servermonitoring.entity.Snapshot;

/**
 *
 * @author Adam Bien <blog.adam-bien.com>
 */
public class SerializerTest {
    
    Serializer serializer;

    @Before
    public void initialize(){
        this.serializer = new Serializer();
        this.serializer.initialize();
    }
    
    @Test
    public void serialize() {
      Snapshot snapshot = new Snapshot.Builder().usedHeapSize(42).build();
      StringWriter writer = new StringWriter();
      this.serializer.serialize(snapshot, writer);
      String serializedRepresentation = writer.getBuffer().toString();
      assertNotNull(serializedRepresentation);
      assertTrue(serializedRepresentation.contains("42"));
    }


}
