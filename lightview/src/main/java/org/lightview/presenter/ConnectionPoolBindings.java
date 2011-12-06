package org.lightview.presenter;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.lightview.entity.ConnectionPool;

/**
 * User: blog.adam-bien.com
 * Date: 06.12.11
 * Time: 03:27
 */
public class ConnectionPoolBindings {
    private StringProperty jndiName;

    private IntegerProperty numconnfree;
    private IntegerProperty waitqueuelength;
    private IntegerProperty numpotentialconnleak;
    private IntegerProperty numconnused;

    private ConnectionPoolBindings() {
        this.jndiName = new SimpleStringProperty();
        this.numconnfree = new SimpleIntegerProperty();
        this.waitqueuelength = new SimpleIntegerProperty();
        this.numpotentialconnleak = new SimpleIntegerProperty();
        this.numconnused = new SimpleIntegerProperty();
    }

    public StringProperty getJndiName() {
        return jndiName;
    }

    public IntegerProperty getNumconnfree() {
        return numconnfree;
    }

    public IntegerProperty getWaitqueuelength() {
        return waitqueuelength;
    }

    public IntegerProperty getNumpotentialconnleak() {
        return numpotentialconnleak;
    }

    public IntegerProperty getNumconnused() {
        return numconnused;
    }

    public void setJndiName(String jndiName) {
        this.jndiName.set(jndiName);
    }

    public void setNumconnfree(int numconnfree) {
        this.numconnfree.set(numconnfree);
    }

    public void setWaitqueuelength(int waitqueuelength) {
        this.waitqueuelength.set(waitqueuelength);
    }

    public void setNumpotentialconnleak(int numpotentialconnleak) {
        this.numpotentialconnleak.set(numpotentialconnleak);
    }

    public void setNumconnused(int numconnused) {
        this.numconnused.set(numconnused);
    }


    public static ConnectionPoolBindings from(ConnectionPool connectionPool) {
        ConnectionPoolBindings poolBindings = new ConnectionPoolBindings();
        poolBindings.update(connectionPool);
        return poolBindings;
    }

    void update(ConnectionPool connectionPool) {
        this.setJndiName(connectionPool.getJndiName());
        this.setNumconnfree(connectionPool.getNumconnfree());
        this.setNumpotentialconnleak(connectionPool.getNumpotentialconnleak());
        this.setNumconnused(connectionPool.getNumconnused());
        this.setWaitqueuelength(connectionPool.getWaitqueuelength());
    }
}
