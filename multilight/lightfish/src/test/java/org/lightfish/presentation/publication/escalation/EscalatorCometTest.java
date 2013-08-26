package org.lightfish.presentation.publication.escalation;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author adam bien, adam-bien.com
 */
@Ignore
public class EscalatorCometTest {

    CometEscalator cut;

    @Before
    public void init() {
        cut = new CometEscalator();
    }

    @Test
    public void extractChannel() {
        String expected = "duke";
        String actual = this.cut.extractChannel("/escalation/" + expected);
        assertThat(actual, is(expected));
    }
}
