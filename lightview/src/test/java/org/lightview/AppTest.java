package org.lightview;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * User: blog.adam-bien.com
 * Date: 08.02.12
 * Time: 14:50
 */
public class AppTest {
    
    private App cut;
    
    @Before
    public void init(){
        this.cut = new App();        
    }
    
    @Test
    public void extractHostWithPort(){
        String input = "http://10.134.115.213:8080/lightfish/lighview.html";
        String actual = this.cut.extractHostWithPort(input);
        String expected = "http://10.134.115.213:8080";
        assertThat(actual,is(expected));
    }
}
