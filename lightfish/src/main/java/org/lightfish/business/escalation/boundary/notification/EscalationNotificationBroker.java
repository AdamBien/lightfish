package org.lightfish.business.escalation.boundary.notification;

import org.lightfish.business.escalation.boundary.notification.transmitter.Transmitter;
import org.lightfish.business.escalation.boundary.notification.transmitter.TransmitterTypeAnnotationLiteral;
import org.lightfish.business.escalation.entity.Notifier;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.lightfish.business.escalation.entity.Escalation;

/**
 *
 * @author rveldpau
 */
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class EscalationNotificationBroker {

    private static final Logger LOG = Logger.getLogger(EscalationNotificationBroker.class.getName());
    
    @Inject NotifierStore notifierStore;
    
    @Inject @Any Instance<Transmitter> transmitters;
    
    @Inject AsyncTransmitterService asyncService;
    
    public void onEscalationBrowserRequest(@Observes Escalation escalation) {
        for(Notifier notifier: notifierStore.all()){
            try {
                LOG.fine("Sending notification to " + notifier);
                
                Transmitter transmitter = retrieveTransmitter(notifier.getTransmitterId());
                asyncService.send(transmitter, notifier.getConfiguration(), escalation);
            } catch (NotificationException ex) {
                LOG.log(Level.WARNING, 
                        "Failed to send escalation notification for " + escalation + " to " + notifier, ex);
            }
        }
    }
    
    private Transmitter retrieveTransmitter(String notifierTypeId) throws NotificationException{
        Instance<Transmitter> specificNotifierInstance = transmitters.select(
                new TransmitterTypeAnnotationLiteral.Builder().value(notifierTypeId).build());
        
        if(specificNotifierInstance.isAmbiguous()){
            throw new NotificationException(notifierTypeId + " is an ambiguous notifier");
        }
        
        if(specificNotifierInstance.isUnsatisfied()){
            throw new NotificationException(notifierTypeId + " is an unsatisified notifier");
        }
        
        return specificNotifierInstance.get();
    }

}
