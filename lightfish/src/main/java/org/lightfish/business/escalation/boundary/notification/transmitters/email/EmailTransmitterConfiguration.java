package org.lightfish.business.escalation.boundary.notification.transmitters.email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Email;
import org.lightfish.business.escalation.boundary.notification.transmitter.TransmitterConfiguration;
import org.lightfish.business.escalation.boundary.notification.transmitter.TransmitterType;

/**
 *
 * @author rveldpau
 */
@TransmitterType("email")
public class EmailTransmitterConfiguration implements TransmitterConfiguration {

    @NotNull
    private String protocol = "SMTP";
    @NotNull
    private String host;
    private int port = 25;
    @NotNull @Email
    private String from;
    @NotNull 
    private String to;
    private boolean authorizationRequired;
    private String userName;
    private String password;

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public boolean isAuthorizationRequired() {
        return authorizationRequired;
    }

    public void setAuthorizationRequired(boolean authorizationRequired) {
        this.authorizationRequired = authorizationRequired;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public static class Builder {

        private EmailTransmitterConfiguration config;

        public Builder() {
            config = new EmailTransmitterConfiguration();
        }

        public Builder protocol(String protocol) {
            this.config.protocol = protocol;
            return this;
        }

        public Builder host(String host) {
            this.config.host = host;
            return this;
        }

        public Builder port(int port) {
            this.config.port = port;
            return this;
        }

        public Builder from(String from) {
            this.config.from = from;
            return this;
        }

        public Builder to(String to) {
            this.config.to = to;
            return this;
        }

        public Builder authorizationRequired(boolean authorizationRequired) {
            this.config.authorizationRequired = authorizationRequired;
            return this;
        }

        public Builder userName(String userName) {
            this.config.userName = userName;
            return this;
        }

        public Builder password(String password) {
            this.config.password = password;
            return this;
        }

        public EmailTransmitterConfiguration build() {
            return this.config;
        }
    }
}
