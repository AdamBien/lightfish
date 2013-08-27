/*
 Copyright 2012 Adam Bien, adam-bien.com

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package org.lightfish.business.servermonitoring.boundary;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import org.lightfish.business.authenticator.GlassfishAuthenticator;
import org.lightfish.business.servermonitoring.control.ServerInstanceProvider;

/**
 *
 * @author Adam Bien, blog.adam-bien.com
 */
@RequestScoped
public class MonitoringAdmin {

    private static final String OFF = "OFF";
    private static final String ON = "HIGH";

    @Inject Logger LOG;

    @Inject
    String location;
    @Inject
    String username;
    @Inject
    String password;
    @Inject
    Instance<String[]> serverInstances;
    @Inject
    Instance<GlassfishAuthenticator> authenticator;
    @Inject
    ServerInstanceProvider instancePorvider;

    @Inject
    private Client client;
    private String baseUri;
    private String modules[] = new String[]{"connectorConnectionPool", "connectorService", "deployment", "ejbContainer", "httpService", "jdbcConnectionPool", "jersey", "jmsService", "jpa", "jvm", "orb", "security", "threadPool", "transactionService", "webContainer", "webServicesContainer"};

    @PostConstruct
    public void initializeClient() {
        this.baseUri = getProtocol() + location;
    }

    public boolean activateMonitoring() {
        return changeMonitoringState(ON);
    }

    public boolean deactivateMonitoring() {
        return changeMonitoringState(OFF);
    }

    private boolean changeMonitoringState(String state) {
        authenticator.get().addAuthenticator(client, username, password);
        MultivaluedMap formData = new MultivaluedHashMap();
        for (String module : modules) {
            formData.add(module, state);
        }
        return postForms(formData);
    }

    boolean postForms(MultivaluedMap form) {
        for (String instanceName : serverInstances.get()) {
            Response response = null;
            int status = -1;
            try {
                response = this.client.target(this.baseUri).path(getEnableMonitoringURI_312(instanceName)).request().header("X-Requested-By", "LightFish").post(Entity.form(form));
                status = response.getStatus();
            } catch (Exception ex) {
                LOG.severe("Problem sending request: " + ex);
            }
            LOG.log(Level.INFO, "Got status: {0} for path: {1}  form: {2}", new Object[]{status, getEnableMonitoringURI_312(instanceName), form});
            if (response == null || 200 != response.getStatus()) {
                return false;
            }
        }
        return true;
    }

    private String getProtocol() {
        String protocol = "http://";
        if (username != null && !username.isEmpty()) {
            protocol = "https://";
        }
        return protocol;
    }

    private String getEnableMonitoringURI_312(String instanceName) {

        String uri = "/management/domain/configs/config/"
                + instancePorvider.fetchServerInstanceInfo(instanceName).getConfigRef()
                + "/monitoring-service/module-monitoring-levels/";
        return uri;
    }

}
