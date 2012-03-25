package org.lightfish.business.escalation.boundary;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author adam bien, adam-bien.com
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Script {
    private String name;
    private String script;
    private boolean active;

    public Script() {
    }

    public Script(String name, String script, boolean active) {
        this.name = name;
        this.script = script;
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return script;
    }

    public void setContent(String script) {
        this.script = script;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 53 * hash + (this.script != null ? this.script.hashCode() : 0);
        hash = 53 * hash + (this.active ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Script other = (Script) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if ((this.script == null) ? (other.script != null) : !this.script.equals(other.script)) {
            return false;
        }
        if (this.active != other.active) {
            return false;
        }
        return true;
    }
 
}
