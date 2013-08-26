package org.lightfish.presentation.publication.escalation;

import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import org.lightfish.business.escalation.entity.Escalation;
import org.lightfish.business.servermonitoring.entity.Snapshot;

/**
 *
 * @author rveldpau
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Escalations {
    private Map<String, Escalation> escalations;

    public Escalations() {
    }

    public Escalations(Map<String, Escalation> escalations) {
        this.escalations = escalations;
    }

    public Map<String, Escalation> getEscalations() {
        return escalations;
    }

    public void setEscalations(Map<String, Escalation> escalations) {
        this.escalations = escalations;
    }
}
