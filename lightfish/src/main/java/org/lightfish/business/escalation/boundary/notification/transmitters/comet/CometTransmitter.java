package org.lightfish.business.escalation.boundary.notification.transmitters.comet;

import javax.inject.Inject;
import org.lightfish.business.escalation.boundary.notification.transmitter.Transmitter;
import org.lightfish.business.escalation.boundary.notification.transmitter.TransmitterType;
import org.lightfish.business.escalation.entity.Escalation;

/**
 * The timer declaration needs to be separated from the injectable 
 * {@link Transmitter}, so this class simply represents what is required for the broker
 * and delegates all real action to {@link CometTransmitterDelegate}. 
 * @author rveldpau
 */
@TransmitterType("comet")
public class CometTransmitter  implements Transmitter<CometTransmitterConfiguration>{

    @Inject CometTransmitterDelegate delegate;
    
    @Override
    public String getId() {
        return "comet";
    }

    @Override
    public String getName() {
        return "COMET (AJAX)";
    }

    @Override
    public void send(CometTransmitterConfiguration configuration, Escalation escalation) {
        delegate.addEscalation(escalation);
    }
    
}
