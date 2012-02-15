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
package org.lightfish.business.monitoring.boundary;

import javax.ejb.Stateless;
import javax.inject.Inject;
import org.lightfish.business.logging.Log;
import org.lightfish.business.monitoring.control.OneShotProvider;

/**
 *
 * @author adam bien, blog.adam-bien.com
 */
@Stateless
public class ServerInformation {
    private String NOT_AVAILABLE = "--";
    
    @Inject
    OneShotProvider provider;
    @Inject
    Log LOG;
    
    
    public String getVersion(){
        try{
         return provider.fetchOneShot().getVersion();
        }catch(Exception e){
            LOG.error("Cannot fetch static server information", e);
            return NOT_AVAILABLE;
        }
         
    }
}
