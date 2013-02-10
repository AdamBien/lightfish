package org.lightfish.beanlocator;

/**
 *
 * @author rveldpau
 */
public class BeanLocationException extends Exception {

    /**
     * Creates a new instance of
     * <code>CdiException</code> without detail message.
     */
    public BeanLocationException() {
    }

    /**
     * Constructs an instance of
     * <code>CdiException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public BeanLocationException(String msg) {
        super(msg);
    }

    /**
     * Constructs an instance of
     * <code>CdiException</code> with the specified detail message and cause.
     *
     * @param msg the detail message.
     * @param cause The cause of the exception
     */
    public BeanLocationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}