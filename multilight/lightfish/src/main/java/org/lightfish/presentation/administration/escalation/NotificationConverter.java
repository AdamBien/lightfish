package org.lightfish.presentation.administration.escalation;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.lightfish.business.escalation.entity.Script;
import org.lightfish.beanlocator.BeanLocationException;
import org.lightfish.beanlocator.CdiUtil;
import org.lightfish.business.escalation.boundary.notification.NotifierStore;

/**
 *
 * @author rveldpau
 */
@FacesConverter("notificationConverter")
public class NotificationConverter implements Converter {
    @Inject
    Logger LOG;
    
    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String string) {
        try {
            NotifierStore notifierStore = retrieveNotifierStore();
            return notifierStore.getNotifier(string);
        } catch (BeanLocationException ex) {
            LOG.log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
        if(o instanceof Script){
            return ((Script)o).getName();
        }
        return null;
    }

    private NotifierStore retrieveNotifierStore() throws BeanLocationException{
        return new CdiUtil().lookup(NotifierStore.class);
    }
}
