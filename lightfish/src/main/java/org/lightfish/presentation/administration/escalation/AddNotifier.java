package org.lightfish.presentation.administration.escalation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;
import org.lightfish.business.escalation.boundary.notification.EscalationNotificationBroker;
import org.lightfish.business.escalation.entity.Notifier;
import org.lightfish.business.escalation.boundary.notification.NotifierStore;
import org.lightfish.business.escalation.boundary.notification.transmitter.Transmitter;
import org.lightfish.business.escalation.boundary.notification.transmitter.TransmitterConfiguration;
import org.lightfish.business.escalation.boundary.notification.transmitter.TransmitterType;
import org.lightfish.business.escalation.boundary.notification.transmitter.TransmitterTypeAnnotationLiteral;

/**
 *
 * @author rveldpau
 */
@Named
@ConversationScoped
public class AddNotifier implements Serializable {

    @Inject
    Conversation conversation;
    @Inject transient 
    NotifierStore notificationStore;
    @Inject
    @Any
    Instance<TransmitterConfiguration> transmitterConfigurationInstance;
    @Inject
    @Any
    Instance<Transmitter> transmitterInstance;
    @NotNull
    @Size(min = 3, max = 150, message = "Name should be between 3 and 150 characters")
    private String name;
    @NotNull
    private String notifierId;
    private TransmitterConfiguration transmitterConfiguration;

    @PostConstruct
    public void initialize() {
        if (conversation.isTransient()) {
            conversation.begin();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotifierId() {
        return notifierId;
    }

    public void setNotifierId(String notifierId) {
        this.notifierId = notifierId;
    }

    public TransmitterConfiguration getTransmitterConfiguration() {
        return transmitterConfiguration;
    }

    public void setTransmitterConfiguration(TransmitterConfiguration transmitterConfiguration) {
        this.transmitterConfiguration = transmitterConfiguration;
    }

    public List<Transmitter> getTransmitterTypes() {
        List<Transmitter> notifierList = new ArrayList<>();
        for (Transmitter notifier : transmitterInstance) {
            notifierList.add(notifier);
        }
        Collections.sort(notifierList, new Comparator<Transmitter>() {
            @Override
            public int compare(Transmitter o1, Transmitter o2) {
                if (o1.getName() == null && o2.getName() != null) {
                    return 1;
                }
                return o1.getName().compareTo(o2.getName());
            }
        });
        return notifierList;
    }

    public String configure() {
        Instance<TransmitterConfiguration> specificTransmitterConfigInstance =
                transmitterConfigurationInstance.select(
                new TransmitterTypeAnnotationLiteral.Builder().value(this.notifierId).build());

        if (specificTransmitterConfigInstance.isAmbiguous() || specificTransmitterConfigInstance.isUnsatisfied()) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "No configuration available for specified transmitter type.", 
                    "Could not find the configuration for the specified transmitter type, it is either unsatisifed or ambiguous.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return null;
        }
        
        this.transmitterConfiguration = specificTransmitterConfigInstance.get();
        
        return "addConfiguration";
    }
    
    public String cancel(){
        if(!conversation.isTransient()){
            conversation.end();
        }
        return "/escalation/configuration?faces-redirect=true";
    }
    
    public String save(){
        Notifier config = new Notifier.Builder()
                .name(name)
                .configuration(transmitterConfiguration)
                .transmitterId(notifierId)
                .build();
        notificationStore.save(config);
        if(!conversation.isTransient()){
            conversation.end();
        }
        return "/escalation/configuration?faces-redirect=true";
    }
}
