package org.lightfish.business.configuration;

import javax.annotation.sql.DataSourceDefinition;
import javax.ejb.Singleton;

/**
 *
 * @author adam bien, blog.adam-bien.com
 */
@DataSourceDefinition(
    className="org.apache.derby.jdbc.ClientDataSource",
    serverName="localhost",
    name="java:global/jdbc/lightfish",
    databaseName="lightfish;create=true",
    portNumber=1527,
    user="lightfish",
    password="lighfish"
)
@Singleton
public class DataSourceConfiguration {
    
}
