package org.lightfish.business.monitoring.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Adam Bien, blog.adam-bien.com
 */

@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ConnectionPool {
    
    @Id
    private String jndiName;
    
    private int numconnfree;
    private int waitqueuelength;
    private int numpotentialconnleak;
    private int numconnused;

    public ConnectionPool(String jndiName, int numconnfree, int numconnused, int waitqueuelength, int numpotentialconnleak) {
        this.jndiName = jndiName;
        this.numconnfree = numconnfree;
        this.waitqueuelength = waitqueuelength;
        this.numpotentialconnleak = numpotentialconnleak;
        this.numconnused = numconnused;
    }

    public ConnectionPool() {
    }
    
    

}
