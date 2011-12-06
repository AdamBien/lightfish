package org.lightview.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Adam Bien, blog.adam-bien.com
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ConnectionPool {
    
    private String jndiName;
    
    private int numconnfree;
    private int waitqueuelength;
    private int numpotentialconnleak;
    private int numconnused;


    public ConnectionPool() {
    }


    public String getJndiName() {
        return jndiName;
    }

    public int getNumconnfree() {
        return numconnfree;
    }

    public int getWaitqueuelength() {
        return waitqueuelength;
    }

    public int getNumpotentialconnleak() {
        return numpotentialconnleak;
    }

    public int getNumconnused() {
        return numconnused;
    }
}
