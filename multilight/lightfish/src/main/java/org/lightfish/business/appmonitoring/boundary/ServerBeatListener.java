/*
 *
 */
package org.lightfish.business.appmonitoring.boundary;

import javax.inject.Inject;
import org.lightfish.business.logging.Log;
import org.lightfish.business.servermonitoring.control.BeatListener;

/**
 *
 * @author adam-bien.com
 */
public class ServerBeatListener implements BeatListener {

    @Inject
    Log LOG;

    @Inject
    ApplicationsSocket applicationSocket;

    @Override
    public void onBeat() {
        LOG.info("New beat received");
        applicationSocket.distributeMessage();
    }

}
