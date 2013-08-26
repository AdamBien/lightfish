package org.lightfish.business.escalation.boundary.notification.transmitter;

import javax.enterprise.util.AnnotationLiteral;

/**
 *
 * @author rveldpau
 */
public class TransmitterTypeAnnotationLiteral extends AnnotationLiteral<TransmitterType> implements TransmitterType {

    private String value = null;

    
    @Override
    public String value() {
        return value;
    }

    public static class Builder{
        private TransmitterTypeAnnotationLiteral literal;
        public Builder(){
            literal = new TransmitterTypeAnnotationLiteral();
        }
        
        public Builder value(String value){
            this.literal.value = value;
            return this;
        }
        
        public TransmitterTypeAnnotationLiteral build(){
            return this.literal;
        }
    }
    
}
