package org.lightfish.business.monitoring.entity;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class represents a Glassfish server instance, more accurately, the information
 * about a Glassfish domain that we care about.
 * <quote>
 * A GlassFish Server instance is a single Virtual Machine for the Java platform 
 * (Java Virtual Machine or JVM machine) on a single node in which GlassFish 
 * Server is running. A node defines the host where the GlassFish Server instance
 * resides</quote>
 * @see http://docs.oracle.com/cd/E18930_01/html/821-2426/gkrbv.html
 * 
 * @author Rob Veldpaus
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ServerInstance {
    private String configRef;

    public ServerInstance() {
    }

    public String getConfigRef() {
        return configRef;
    }
    
    public static class Builder{
        private ServerInstance domainInstance;

        public Builder() {
            this.domainInstance = new ServerInstance();
        }
        
        public Builder configRef(String configRef){
            this.domainInstance.configRef = configRef;
            return this;
        }
        
        public ServerInstance build(){
            return this.domainInstance;
        }
    }
}
