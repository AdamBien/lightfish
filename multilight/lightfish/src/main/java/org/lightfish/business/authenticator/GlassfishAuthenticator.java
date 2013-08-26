/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lightfish.business.authenticator;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.Client;
import org.lightfish.business.servermonitoring.control.SnapshotProvider;

/**
 *
 * @author cdelahunt
 */
@Singleton
@LocalBean
public class GlassfishAuthenticator {

    private static TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {

        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkClientTrusted(
                java.security.cert.X509Certificate[] certs, String authType) {
        }

        @Override
        public void checkServerTrusted(
                java.security.cert.X509Certificate[] certs, String authType) {
        }
    }};

    public void addAuthenticator(Client client, String username, String password) {
        //TODO migration
        // client.addFilter(new HTTPBasicAuthFilter(username, password));
        /*
         * Bypass certificates
         */
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

            @Override
            public boolean verify(String string, SSLSession ssls) {
                return true;
            }
        });
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        } catch (KeyManagementException | NoSuchAlgorithmException ex) {
            Logger.getLogger(SnapshotProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
