package org.lightfish.presentation.administration.escalation;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.lightfish.business.escalation.control.ScriptStore;
import org.lightfish.business.escalation.entity.Script;
import org.lightfish.beanlocator.BeanLocationException;
import org.lightfish.beanlocator.CdiUtil;

/**
 *
 * @author rveldpau
 */
@FacesConverter("scriptConverter")
public class ScriptConverter implements Converter {

    @Inject
    Logger LOG;
    
    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String string) {
        try {
            ScriptStore scriptStore = retrieveScriptStore();
            return scriptStore.getScript(string);
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

    private ScriptStore retrieveScriptStore() throws BeanLocationException{
        return new CdiUtil().lookup(ScriptStore.class);
    }
}
