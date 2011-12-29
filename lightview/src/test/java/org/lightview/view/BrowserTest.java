package org.lightview.view;

import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * User: blog.adam-bien.com
 * Date: 29.12.11
 * Time: 06:47
 */
public class BrowserTest {
    private Browser cut;
    
    @Before
    public void init(){
        this.cut = new Browser();
    }
    
    @Test
    public void skipLastSlashWithExistingSlash(){
        String expected = "hey";
        String origin = "hey/";
        String actual = this.cut.skipLastSlash(origin);
        assertThat(actual,is(expected));
    }
    @Test
    public void skipLastSlashWithoutExistingSlash(){
        String expected = "hey";
        String origin = "hey";
        String actual = this.cut.skipLastSlash(origin);
        assertThat(actual,is(expected));
    }
}
