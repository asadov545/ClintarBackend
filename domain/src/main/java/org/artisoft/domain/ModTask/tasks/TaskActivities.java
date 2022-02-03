package org.artisoft.domain.ModTask.tasks;

public class TaskActivities {
    private  long tsuId;
    private String status;
    private String fullName;
    private long createDate;

    public long getTsuId() {
        return tsuId;
    }

    public void setTsuId(long tsuId) {
        this.tsuId = tsuId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }
}
