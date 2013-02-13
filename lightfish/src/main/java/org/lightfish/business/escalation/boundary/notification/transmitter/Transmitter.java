package org.lightfish.business.escalation.boundary.notification.transmitter;

import org.lightfish.business.escalation.entity.Escalation;

/**
 *
 * @author rveldpau
 */
public interface Transmitter<CONFIGTYPE extends TransmitterConfiguration> {
    String getId();
    String getName();
    void send(CONFIGTYPE configuration, Escalation escalation);
}
