/*
Copyright 2012 Adam Bien, adam-bien.com

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package org.lightfish.presentation.publication;

import javax.servlet.AsyncContext;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.Writer;

/**
 *
 * @author Adam Bien, blog.adam-bien.com
 */
public class BrowserWindow {
    
    private AsyncContext asyncContext;
    private final ServletResponse response;

    public BrowserWindow(AsyncContext asyncContext) {
        this.asyncContext = asyncContext;
        this.response = this.asyncContext.getResponse();
        this.response.setContentType("application/xml");
    }
    
    
    public void send(){
        try{
            this.asyncContext.complete();
        }catch(Exception e){
            System.err.println("Cannot complete context: " + e);
        }
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
