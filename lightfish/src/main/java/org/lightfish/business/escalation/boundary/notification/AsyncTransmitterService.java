package org.lightfish.business.escalation.boundary.notification;

import org.lightfish.business.escalation.boundary.notification.transmitter.Transmitter;
import org.lightfish.business.escalation.boundary.notification.transmitter.TransmitterConfiguration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.lightfish.business.escalation.entity.Escalation;

/**
 *
 * @author rveldpau
 */
@Stateless
public class AsyncTransmitterService {
    @Inject Logger LOG;
    
    @Asynchronous
    public void send(Transmitter transmitter, TransmitterConfiguration configuration, Escalation escalation){
        LOG.log(Level.FINE, "Start sending: {0}", transmitter.getId());
        transmitter.send(configuration, escalation);
        LOG.log(Level.FINE, "Stop sending: {0}", transmitter.getId());
    }
}
