package org.lightfish.presentation.publication;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    
    
    public void send(String json){
        try {
            this.asyncContext.getResponse().getWriter().print(json);
            this.asyncContext.complete();
        } catch (IOException ex) {
            Logger.getLogger(BrowserWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
