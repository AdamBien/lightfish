
package org.lightfish.business.escalation.boundary.notification;

/**
 *
 * @author rveldpau
 */
public class NotificationException extends Exception {

    /**
     * Creates a new instance of <code>NotificationException</code> without detail message.
     */
    public NotificationException() {
    }


    /**
     * Constructs an instance of <code>NotificationException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public NotificationException(String msg) {
        super(msg);
    }

    /**
     * Constructs an instance of <code>NotificationException</code> with the specified detail message
     * and cause.
     * @param msg the detail message.
     * @param cause The cause of the exception
     */
    public NotificationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
