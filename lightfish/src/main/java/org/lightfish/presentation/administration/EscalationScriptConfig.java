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

import java.util.List;
import org.lightfish.business.configuration.boundary.Configurator;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.lightfish.business.escalation.control.ScriptStore;
import org.lightfish.business.escalation.entity.Script;
import org.lightfish.business.monitoring.boundary.DomainInformation;

/**
 *
 * @author Rob Veldpaus
 */
@Model
public class EscalationScriptConfig {
    @Inject ScriptStore scriptStore;
    
    public List<Script> getScripts(){
        return scriptStore.activeScripts();
    }
    
    public String updateConfiguration(){
        return "index?faces-redirect=true";
    }
}
