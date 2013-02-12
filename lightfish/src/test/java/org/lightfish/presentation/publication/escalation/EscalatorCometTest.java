package org.lightfish.presentation.publication.escalation;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author adam bien, adam-bien.com
 */
public class EscalatorCometTest {
    
    CometEscalator cut;

    @Before
    public void init(){
        cut = new CometEscalator();
    }
    
    @Test
    public void extractChannel() {
        String expected = "duke";
        String actual = this.cut.extractChannel("/escalation/" + expected);
        assertThat(actual,is(expected));
    }
}
