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
package org.lightfish.business.heartbeat.control;

import org.lightfish.business.monitoring.entity.Snapshot;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.lightfish.business.escalation.entity.Escalation;
import org.lightfish.presentation.publication.escalation.Escalations;

/**
 *
 * @author Adam Bien <blog.adam-bien.com>
 */
public class Serializer {
    @Inject Logger LOG;
    
    private Marshaller marshaller;

    @PostConstruct
    public void initialize(){
        try {
            JAXBContext jaxb = JAXBContext.newInstance(Snapshot.class,Escalations.class, Escalation.class);
            this.marshaller = jaxb.createMarshaller();
        } catch (JAXBException ex) {
            throw new IllegalStateException("Cannot initialize JAXB context " + ex);
        }
    }
    
    public void serialize(Snapshot snapshot,Writer writer){
        serializeObject(snapshot, writer);
    }
    
    public void serialize(Escalations escalations,Writer writer){
        serializeObject(escalations, writer);
    }
    
    public void serialize(Escalation escalation,Writer writer){
        serializeObject(escalation, writer);
    }
    
    private void serializeObject(Object obj,Writer writer){
        try {
            synchronized(this.marshaller){
                this.marshaller.marshal(obj, writer);
            }
        } catch (JAXBException ex) {
            throw new RuntimeException("Cannot marshal object " + obj + " Reason: " +ex,ex);
        }
    }
}
