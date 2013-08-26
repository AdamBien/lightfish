/*
Copyright 2012 Adam Bien, adam-bien.com

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package org.lightview.presenter;

import javafx.beans.property.*;
import org.lightview.model.ConnectionPool;

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

    public ReadOnlyStringProperty getJndiName() {
        return jndiName;
    }

    public ReadOnlyIntegerProperty getNumconnfree() {
        return numconnfree;
    }

    public ReadOnlyIntegerProperty getWaitqueuelength() {
        return waitqueuelength;
    }

    public ReadOnlyIntegerProperty getNumpotentialconnleak() {
        return numpotentialconnleak;
    }

    public ReadOnlyIntegerProperty getNumconnused() {
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
