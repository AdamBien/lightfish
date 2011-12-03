package org.lightfish.business.configuration.boundary;

import static org.junit.Assert.assertArrayEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Adam Bien, blog.adam-bien.com
 */
public class ConfiguratorTest {
    
    Configurator cut;
    
    @Before
    public void initialize(){
        cut = new Configurator();
    }


    @Test
    public void asArrayWithMultipleSlots() {
        String[] expected = {"hey","joe"};
        String[] actual = cut.asArray("hey,joe");
        assertArrayEquals(expected, actual);
    }

    @Test
    public void asArrayWithSingleSlot() {
        String[] expected = {"hey"};
        String[] actual = cut.asArray("hey");
        assertArrayEquals(expected, actual);
    }
}
