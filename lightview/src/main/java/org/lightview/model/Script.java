package org.lightview.model;


import java.util.Objects;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author adam bien, adam-bien.com
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Script {
    private String name;
    private String content;
    
    @XmlElement(defaultValue="true")
    private boolean active;

    public Script() {
    }

    public Script(String name, String content, boolean active) {
        this.name = name;
        this.content = content;
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.content, other.content)) {
            return false;
        }
        if (this.active != other.active) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.name);
        hash = 89 * hash + Objects.hashCode(this.content);
        hash = 89 * hash + (this.active ? 1 : 0);
        return hash;
    }

    

    @Override
    public String toString() {
        return "Script{" + "name=" + name + ", script=" + content + ", active=" + active + '}';
    }
}
