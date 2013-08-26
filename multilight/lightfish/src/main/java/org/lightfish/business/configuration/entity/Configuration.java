package org.lightfish.business.configuration.entity;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapKeyColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author rveldpau
 */
@Entity
public class Configuration {
    public static final String ID = "configuration";
    

    @Id
    private String id = ID;
    @ElementCollection
    private Map<String, String> valueMap = new HashMap<>();

    public void put(String key, String value) {
        valueMap.put(key, value);
    }

    public String get(String key) {
        return valueMap.get(key);
    }
}
