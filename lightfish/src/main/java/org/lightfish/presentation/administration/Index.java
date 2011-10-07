package org.lightfish.presentation.administration;

import javax.enterprise.inject.Model;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Adam Bien, blog.adam-bien.com
 */
@Model
public class Index {
    
    @Size(min=5,max=30)
    private String location;
    
    @Min(1)
    private int interval;

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    
    public Object changeAdministration(){
        System.out.println(location + ":" + interval);
        return null;
    }
    
}
