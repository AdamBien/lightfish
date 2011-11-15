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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BrowserWindow other = (BrowserWindow) obj;
        if (this.asyncContext != other.asyncContext && (this.asyncContext == null || !this.asyncContext.equals(other.asyncContext))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + (this.asyncContext != null ? this.asyncContext.hashCode() : 0);
        return hash;
    }
    
    
    
}
