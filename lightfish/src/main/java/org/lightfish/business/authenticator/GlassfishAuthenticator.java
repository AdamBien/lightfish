/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lightfish.business.authenticator;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Singleton;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.lightfish.business.monitoring.control.SnapshotProvider;

/**
 *
 * @author cdelahunt
 */
@Singleton
public class GlassfishAuthenticator {

    private static TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {

    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
        return null;
    }

    public void checkClientTrusted(
            java.security.cert.X509Certificate[] certs, String authType) {
    }

    public void checkServerTrusted(
            java.security.cert.X509Certificate[] certs, String authType) {
    }
}};

    public void setAuthenticationForUser(final String username, final String password) {

        Authenticator.setDefault(new Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password.toCharArray());
            }
        });
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        } catch (KeyManagementException ex) {
            Logger.getLogger(SnapshotProvider.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(SnapshotProvider.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
