/*
 *
 */
package org.lightfish.business.rest;

import javax.enterprise.inject.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

/**
 *
 * @author adam-bien.com
 */
public class ClientPublisher {

    @Produces
    public Client create() {
        return ClientBuilder.newClient();
    }
}
