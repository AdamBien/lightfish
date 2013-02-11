package org.lightfish.presentation.publication.escalation;

import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import org.lightfish.business.monitoring.entity.Snapshot;

/**
 *
 * @author rveldpau
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Escalations {
    private Map<String, Snapshot> escalations;

    public Escalations() {
    }

    public Escalations(Map<String, Snapshot> escalations) {
        this.escalations = escalations;
    }

    public Map<String, Snapshot> getEscalations() {
        return escalations;
    }

    public void setEscalations(Map<String, Snapshot> escalations) {
        this.escalations = escalations;
    }
}
