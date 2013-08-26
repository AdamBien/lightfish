package org.lightfish.business.monitoring.entity;

import java.util.Collections;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class represents a Glassfish Domain, more accurately, the information
 * about a Glassfish domain that we care about.
 * <quote>A domain (statically) is an administrative name space. It's a boundary,
 * all GlassFish entities within which are controlled by an administrator...</quote>
 * @see https://blogs.oracle.com/bloggerkedar/entry/concept_of_a_glassfish_domain
 * @author Rob Veldpaus
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Domain {

    private List<String> instances;

    public Domain() {
    }

    public List<String> getInstances() {
        if(this.instances==null){
            return null;
        }
        return Collections.unmodifiableList(this.instances);
    }


    public static class Builder{
        private Domain domainInfo;

        public Builder() {
            this.domainInfo = new Domain();
        }
        
        public Builder instances(List<String> instances){
            this.domainInfo.instances = instances;
            return this;
        }
        
        public Domain build(){
            return this.domainInfo;
        }
    }
}
