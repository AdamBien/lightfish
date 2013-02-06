package org.lightfish.business.monitoring.control.collectors.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import org.codehaus.jettison.json.JSONException;
import org.lightfish.business.monitoring.control.SnapshotProvider;
import org.lightfish.business.monitoring.control.collectors.AbstractRestDataCollector;
import org.lightfish.business.monitoring.control.collectors.DataPoint;
import org.lightfish.business.monitoring.entity.Application;

/**
 *
 * @author rveldpau
 */
public class ApplicationCollector extends AbstractRestDataCollector<List<Application>> {

    private static final String APPLICATIONS = "applications";
    private static final Logger LOG = Logger.getLogger(SnapshotProvider.class.getName());

    @Override
    public DataPoint<List<Application>> collect() throws Exception {
        LOG.fine("Collecting application data");
        String[] applicationNames = applications();
        LOG.finer("Application names: " + applicationNames.length);
        List<Application> applications = new ArrayList<>(applicationNames.length);
        for (String appName : applicationNames) {
            applications.add(fetchApplication(appName));
        }
        LOG.finer("Application Count: " + applications.size());
        return new DataPoint<>("applications", applications);
    }

    private String[] applications() throws JSONException {
        return getStringArray(APPLICATIONS, "childResources");
    }
    
    private Application fetchApplication(String applicationName) throws Exception{
        String[] components = components(applicationName);
        return new Application(applicationName, Arrays.asList(components));
    }

    private String[] components(String applicationName) throws JSONException {
        return getStringArray(APPLICATIONS + "/" + applicationName, "childResources");
    }
}
