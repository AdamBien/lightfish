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
package org.lightfish.business.servermonitoring.entity;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.Map;

/**
 *
 * @author Rob Veldpaus
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
@Entity
@NamedQueries({
    @NamedQuery(name=LogRecord.QUERY_BY_SNAPSHOT_ID,query="select s.logRecords from Snapshot s where s.id = :id"),
    @NamedQuery(name=LogRecord.QUERY_FROM,query="select l from LogRecord l where l.instanceName like :instance and l.monitoringTime >= :from order by l.monitoringTime desc"),
    @NamedQuery(name=LogRecord.QUERY_BETWEEN,query="select l from LogRecord l where l.instanceName like :instance and l.monitoringTime >= :from and l.monitoringTime <= :to order by l.monitoringTime desc")
})
public class LogRecord implements Comparable<LogRecord> {

    
    public static final String QUERY_BY_SNAPSHOT_ID = "logrecord.bySnapshot";
    public static final String QUERY_FROM = "logrecord.from";
    public static final String QUERY_BETWEEN = "logrecord.between";
    
    @Id
    @GeneratedValue
    private long id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date monitoringTime;
    private String instanceName;
    @Column(length=4000)
    private String loggerName;
    @ElementCollection
    @JoinTable(name = "LOGRECORD_NAME_VALUE")
    private Map<String, String> nameValuePairs;
    private String level;
    private String messageId;
    @Column(length=4000)
    private String message;

    public LogRecord() {
        this.monitoringTime = new Date();
    }

    public long getId() {
        return id;
    }

    public Date getMonitoringTime() {
        return monitoringTime;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getLoggerName() {
        return loggerName;
    }

    public void setLoggerName(String loggerName) {
        this.loggerName = loggerName;
    }

    public Map<String, String> getNameValuePairs() {
        return nameValuePairs;
    }

    public void setNameValuePairs(Map<String, String> nameValuePairs) {
        this.nameValuePairs = nameValuePairs;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Override
    public int compareTo(LogRecord that) {
        if(that==null){
            return 1;
        }
        
        if(this.getMonitoringTime()==null || that.getMonitoringTime()==null){
            return (int)(this.id - that.id);
        }
        
        return this.getMonitoringTime().compareTo(that.getMonitoringTime());
    }

    public static class Builder {

        private LogRecord record = new LogRecord();

        public Builder monitoringTime(Date monitoringTime) {
            this.record.monitoringTime = monitoringTime;
            return this;
        }

        public Builder instanceName(String instanceName) {
            this.record.instanceName = instanceName;
            return this;
        }

        public Builder loggerName(String loggerName) {
            this.record.loggerName = loggerName;
            return this;
        }

        public Builder nameValuePairs(Map<String, String> nameValuePairs) {
            this.record.nameValuePairs = nameValuePairs;
            return this;
        }

        public Builder level(String level) {
            this.record.level = level;
            return this;
        }

        public Builder messageId(String messageId) {
            this.record.messageId = messageId;
            return this;
        }

        public Builder message(String message) {
            this.record.message = message;
            return this;
        }
        
        public LogRecord build(){
            return this.record;
        }
    }
}
