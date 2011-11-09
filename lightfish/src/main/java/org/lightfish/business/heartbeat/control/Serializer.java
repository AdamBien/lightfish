/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lightfish.business.heartbeat.control;

import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.lightfish.business.monitoring.entity.Snapshot;

/**
 *
 * @author Adam Bien <blog.adam-bien.com>
 */
public class Serializer {

    private Marshaller marshaller;

    @PostConstruct
    public void initialize(){
        try {
            JAXBContext jaxb = JAXBContext.newInstance(Snapshot.class);
            this.marshaller = jaxb.createMarshaller();
        } catch (JAXBException ex) {
            throw new IllegalStateException("Cannot initialize JAXB context " + ex);
        }
    }
    
    public void serialize(Snapshot snapshot,Writer writer){
        try {
            this.marshaller.marshal(snapshot, writer);
        } catch (JAXBException ex) {
            throw new RuntimeException("Cannot marshal Snapshot " + snapshot + " Reason: " +ex,ex);
        }
    }
}
