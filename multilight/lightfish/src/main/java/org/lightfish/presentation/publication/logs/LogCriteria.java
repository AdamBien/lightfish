package org.lightfish.presentation.publication.logs;

import java.util.Date;

/**
 *
 * @author rveldpau
 */
public class LogCriteria {

    private String instance;
    private Long snapshotId;
    private Date fromDate;
    private Date toDate;
    private Integer maxResults;

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public Long getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(Long snapshotId) {
        this.snapshotId = snapshotId;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Integer getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
    }

    @Override
    public String toString() {
        return "LogCriteria{" + "instance=" + instance + ", snapshotId=" + snapshotId + ", fromDate=" + fromDate + ", toDate=" + toDate + ", maxResults=" + maxResults + '}';
    }
    
    public static class Builder {

        private LogCriteria criteria = new LogCriteria();

        public Builder instance(String instance) {
            this.criteria.instance = instance;
            return this;
        }

        public Builder snapshotId(Long snapshotId) {
            this.criteria.snapshotId = snapshotId;
            return this;
        }

        public Builder fromDate(Date fromDate) {
            this.criteria.fromDate = fromDate;
            return this;
        }

        public Builder toDate(Date toDate) {
            this.criteria.toDate = toDate;
            return this;
        }

        public Builder maxResults(Integer maxResults) {
            this.criteria.maxResults = maxResults;
            return this;
        }

        public LogCriteria build() {
            return this.criteria;
        }
    }
}
