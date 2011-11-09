package org.lightfish.presentation.publication;

import java.io.IOException;
import java.io.Writer;
import javax.servlet.AsyncContext;

/**
 *
 * @author Adam Bien, blog.adam-bien.com
 */
public class BrowserWindow {
    
    private AsyncContext asyncContext;

    public BrowserWindow(AsyncContext asyncContext) {
        this.asyncContext = asyncContext;
    }
    
    
    public void send(){
            this.asyncContext.complete();
    }
    
    public Writer getWriter(){
        try {
            return this.asyncContext.getResponse().getWriter();
        } catch (IOException ex) {
            throw new IllegalStateException("Cannot return writer: " + ex,ex);
        }
    }
    
}
