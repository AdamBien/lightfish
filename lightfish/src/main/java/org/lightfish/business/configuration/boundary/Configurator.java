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
package org.lightfish.business.configuration.boundary;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.Startup;

/**
 *
 * @author Adam Bien, blog.adam-bien.com
 */
@Startup
@Singleton
public class Configurator {
    private int interval = 5;
    
    private Map<String,String> configuration;
    
    @PostConstruct
    public void initialize(){
        this.configuration = new HashMap<>();
        this.configuration.put("location", "localhost:4848");
        this.configuration.put("jdbcPoolNames","SamplePool");
        this.configuration.put("interval","2");
        this.configuration.put("username", "");
        this.configuration.put("password", "");
    }

    @Produces
    public int getInteger(InjectionPoint ip) {
        return Integer.parseInt(getString(ip));
    }

    
    @Produces
    public String getString(InjectionPoint ip) {
        String name = ip.getMember().getName();
        return this.configuration.get(name);
    }
    
    @Produces
    public String[] getStringArray(InjectionPoint ip){
        return asArray(getString(ip));
    }

    public String getValue(String key){
        return this.configuration.get(key);
    }
    
    
    public String[] asArray(String value){
       return value.split(",");
    }

    public Map<String, String> getConfiguration() {
        return configuration;
    }

    public int getValueAsInt(String interval) {
        return Integer.parseInt(getValue(interval));
    }

    public void setValue(String key,int interval) {
        this.setValue(key, String.valueOf(interval));
    }

    public void setValue(String key, String value) {
        this.configuration.put(key, value);
    }
}
