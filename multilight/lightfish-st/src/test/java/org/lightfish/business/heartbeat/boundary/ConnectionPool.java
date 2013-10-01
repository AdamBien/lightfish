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
package org.lightfish.business.heartbeat.boundary;

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

    private long id;

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

    public long getId() {
        return id;
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

    public void setJndiName(String jndiName) {
        this.jndiName = jndiName;
    }

    public void setNumconnfree(int numconnfree) {
        this.numconnfree = numconnfree;
    }

    public void setWaitqueuelength(int waitqueuelength) {
        this.waitqueuelength = waitqueuelength;
    }

    public void setNumpotentialconnleak(int numpotentialconnleak) {
        this.numpotentialconnleak = numpotentialconnleak;
    }

    public void setNumconnused(int numconnused) {
        this.numconnused = numconnused;
    }

}
