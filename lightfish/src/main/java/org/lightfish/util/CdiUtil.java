package org.lightfish.util;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Provides Context Dependency Injection capabilities outside of the CDI
 * container.
 *
 * @author Rob Veldpaus
 */
public class CdiUtil {

    /**
     * Finds the first appropriate bean that is managed by CDI
     *
     * @param <TYPE> The type of object that is requested
     * @param clazz The class of the object to lookup
     * @return The equivalent of a CDI injected bean.
     * @throws CdiException
     */
    public <TYPE> TYPE lookup(Class<TYPE> clazz) throws CdiException {
        BeanManager beanManager = jndiLookup("java:comp/BeanManager", BeanManager.class);

        Bean<TYPE> handlerBean = (Bean<TYPE>) beanManager.getBeans(clazz).iterator().next();
        CreationalContext<TYPE> ctx = beanManager.createCreationalContext(handlerBean);
        TYPE handler = (TYPE) beanManager.getReference(handlerBean, clazz, ctx);
        return handler;

    }

    /**
     * Retrieves an object from JNDI with the specified name
     *
     * @param <TYPE> The type that is expected at the JNDI location
     * @param jndiName The JNDI name to retrieve
     * @param clazz The class that is expected
     * @return The object represented by the JNDI name cast as requested.
     * @throws JndiLookupException
     */
    private <TYPE> TYPE jndiLookup(String jndiName, Class<TYPE> clazz)
            throws CdiException {
        TYPE returnObject = null;
        try {
            Context ctx = retrieveContext();
            returnObject = (TYPE) ctx.lookup(jndiName);

        } catch (NamingException ne) {
            throw new CdiException(
                    "Could not retrieve Object because of a naming exception: " + ne.getMessage(), ne);

        } catch (ClassCastException cce) {
            throw new CdiException(
                    "Retrieved an Object, but it could not be cast to the requested type.", cce);
        }
        return returnObject;
    }
    /**
     * This is the context that is to be used for the actions. See
     * {@link #retrieveContext()} and {@link #resetContext() }
     */
    private Context context;

    /**
     * Gets the context to use for the actions. If a context is not set, the
     * system will use the InitialContext.
     *
     * @return The context to use for the actions.
     * @throws JndiLookupException
     */
    private Context retrieveContext() throws CdiException {
        if (context == null) {
            try {
                context = new InitialContext();
            } catch (NamingException ex) {
                throw new CdiException("Could not retrieve intial context.", ex);
            }
        }
        return context;
    }
}