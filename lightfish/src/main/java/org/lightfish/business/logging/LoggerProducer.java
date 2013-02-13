package org.lightfish.business.logging;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

/**
 *
 * @author rveldpau
 */
public class LoggerProducer {
    private Map<String, Logger> loggers = new HashMap<>();
    
    @Produces
    public Logger getProducer(InjectionPoint ip){
        String key = getKeyFromIp(ip);
        if(!loggers.containsKey(key)){
            loggers.put(key, Logger.getLogger(key));
        }
        return loggers.get(key);
    }
    
    private String getKeyFromIp(InjectionPoint ip){
        return ip.getMember().getDeclaringClass().getCanonicalName();
    }
}