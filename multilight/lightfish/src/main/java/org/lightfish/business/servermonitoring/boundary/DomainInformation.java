package org.lightfish.business.servermonitoring.boundary;

import javax.ejb.Stateless;
import javax.inject.Inject;
import org.lightfish.business.logging.Log;
import org.lightfish.business.servermonitoring.control.DomainProvider;
import org.lightfish.business.servermonitoring.control.OneShotProvider;
import org.lightfish.business.servermonitoring.entity.Domain;
import org.lightfish.business.servermonitoring.entity.OneShot;

/**
 *
 * @author Rob Veldpaus
 */
@Stateless
public class DomainInformation {
    @Inject
    DomainProvider provider;
    @Inject
    Log LOG;
    
    
    public Domain fetch(){
        try{
         return provider.fetchDomainInfo();
        }catch(Exception e){
            LOG.error("Cannot fetch domain information", e);
            return new Domain.Builder().build();
        }
         
    }
}
