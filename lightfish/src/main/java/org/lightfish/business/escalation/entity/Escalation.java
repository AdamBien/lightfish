package org.lightfish.business.escalation.entity;

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
public class Escalation {

    private String channel;
    private String basicMessage;
    private String richMessage;
    private Snapshot snapshot;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getBasicMessage() {
        return basicMessage;
    }

    public void setBasicMessage(String basicMessage) {
        this.basicMessage = basicMessage;
    }

    public String getRichMessage() {
        return richMessage;
    }

    public void setRichMessage(String richMessage) {
        this.richMessage = richMessage;
    }

    public Snapshot getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(Snapshot snapshot) {
        this.snapshot = snapshot;
    }

    public static class Builder {

        private Escalation escalation;

        public Builder() {
            this.escalation = new Escalation();
        }

        public Builder channel(String channel) {
            escalation.channel = channel;
            return this;
        }

        public Builder basicMessage(String message) {
            escalation.basicMessage = message;
            return this;
        }
        
        public Builder richMessage(String message) {
            escalation.richMessage = message;
            return this;
        }

        public Builder snapshot(Snapshot snapshot) {
            escalation.snapshot = snapshot;
            return this;
        }

        public Escalation build() {
            return this.escalation;
        }
    }
}
