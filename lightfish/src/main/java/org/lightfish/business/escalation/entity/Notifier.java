package org.lightfish.business.escalation.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import org.lightfish.business.escalation.boundary.notification.transmitter.TransmitterConfiguration;

/**
 *
 * @author rveldpau
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "notifier.all", query = "select n from Notifier n"),
    @NamedQuery(name = "notifier.nonsystem", query = "select n from Notifier n where n.system = false")
})
public class Notifier {

    @Id
    private String name;
    private String transmitterId;
    private TransmitterConfiguration configuration;
    private boolean system = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TransmitterConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(TransmitterConfiguration configuration) {
        this.configuration = configuration;
    }

    public String getTransmitterId() {
        return transmitterId;
    }

    public void setTransmitterId(String transmitterId) {
        this.transmitterId = transmitterId;
    }

    public boolean isSystem() {
        return system;
    }

    public void setSystem(boolean system) {
        this.system = system;
    }

    @Override
    public String toString() {
        return "NotificationConfiguration{" + "name=" + name + ", transmitterId=" + transmitterId + '}';
    }

    public static class Builder {

        private Notifier config = null;

        public Builder() {
            this.config = new Notifier();
        }

        public Builder name(String name) {
            this.config.name = name;
            return this;
        }

        public Builder configuration(TransmitterConfiguration config) {
            this.config.configuration = config;
            return this;
        }

        public Builder transmitterId(String typeId) {
            this.config.transmitterId = typeId;
            return this;
        }

        public Builder system(boolean system) {
            this.config.system = system;
            return this;
        }

        public Notifier build() {
            return this.config;
        }
    }
}
