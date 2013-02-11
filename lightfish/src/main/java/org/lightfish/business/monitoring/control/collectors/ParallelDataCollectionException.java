
package org.lightfish.business.monitoring.control.collectors;

/**
 *
 * @author rveldpau
 */
public class ParallelDataCollectionException extends Exception {

    /**
     * Creates a new instance of <code>DataCollectionException</code> without detail message.
     */
    public ParallelDataCollectionException() {
    }


    /**
     * Constructs an instance of <code>DataCollectionException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ParallelDataCollectionException(String msg) {
        super(msg);
    }

    /**
     * Constructs an instance of <code>DataCollectionException</code> with the specified detail message
     * and cause.
     * @param msg the detail message.
     * @param cause The cause of the exception
     */
    public ParallelDataCollectionException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
