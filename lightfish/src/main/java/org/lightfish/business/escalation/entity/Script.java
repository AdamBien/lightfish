package org.lightfish.business.escalation.entity;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author adam bien, adam-bien.com
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
@Entity
@NamedQueries({
    @NamedQuery(name=Script.findAll,query="Select s from Script s"),
    @NamedQuery(name=Script.findAll,query="Select s from Script s where s.active == true")
})
public class Script {
    public static final String PREFIX = "org.lightfish.business.scripting.entity.";
    public static final String findAll = PREFIX + "findAll";
    public static final String findAllActive = PREFIX + "findAllActive";
    
    @Id
    private String name;
    @Lob
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

    
}
