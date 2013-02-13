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
package org.lightfish.presentation.administration.escalation;

import java.util.List;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import org.lightfish.business.escalation.boundary.notification.NotifierStore;
import org.lightfish.business.escalation.entity.Notifier;
import org.lightfish.business.escalation.control.ScriptStore;
import org.lightfish.business.escalation.entity.Script;

/**
 *
 * @author Rob Veldpaus
 */
@Model
public class EscalationConfiguration {
    @Inject ScriptStore scriptStore;
    @Inject NotifierStore notifierStore;
    
    public List<Script> getScripts(){
        return scriptStore.scripts();
    }
    
    public List<Notifier> getNotifiers(){
        return notifierStore.all(false);
    }
}
