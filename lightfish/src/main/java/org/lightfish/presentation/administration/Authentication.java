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
package org.lightfish.presentation.administration;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import org.lightfish.business.configuration.boundary.Configurator;

/**
 *
 * @author adam bien, adam-bien.com
 */
@Model
public class Authentication {

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    @Inject
    Configurator configurator;

    public String getUsername() {
        return configurator.getValue(USERNAME);
    }

    public void setUsername(String username) {
        this.configurator.setValue(USERNAME, username);
    }

    public String getPassword() {
        return configurator.getValue(PASSWORD);
    }

    public void setPassword(String password) {
        this.configurator.setValue(PASSWORD, password);
    }

    public Object storeCredentials() {
        return "index";
    }

    public String getProtocol() {
        String protocol = "http://";
        if (getUsername() != null && !getUsername().isEmpty()) {
            protocol = "https://";
        }
        return protocol;
    }
}
