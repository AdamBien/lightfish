package org.lightfish.util;

/**
 *
 * @author rveldpau
 */
public class CdiException extends Exception {

    /**
     * Creates a new instance of
     * <code>CdiException</code> without detail message.
     */
    public CdiException() {
    }

    /**
     * Constructs an instance of
     * <code>CdiException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public CdiException(String msg) {
        super(msg);
    }

    /**
     * Constructs an instance of
     * <code>CdiException</code> with the specified detail message and cause.
     *
     * @param msg the detail message.
     * @param cause The cause of the exception
     */
    public CdiException(String msg, Throwable cause) {
        super(msg, cause);
    }
}