package org.lightfish.presentation.publication;

import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
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
