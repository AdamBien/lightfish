package org.lightfish.business.monitoring.boundary;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.inject.Qualifier;

/**
 *
 * @author Adam Bien, blog.adam-bien.com
 */
@Qualifier
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Severity {
    Level value();
    
    enum Level{
        HEARTBEAT,ESCALATION;
    }
}
