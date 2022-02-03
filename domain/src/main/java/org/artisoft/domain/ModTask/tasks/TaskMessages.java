package org.artisoft.domain.ModTask.tasks;

public class TaskMessages {
    private  long tmId;
    private long taskId;
    private long userId;
    private String title;
    private String desc;
    private long createDate;
    private String fullName;

    public long getTmId() {
        return tmId;
    }

    public void setTmId(long tmId) {
        this.tmId = tmId;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        return "TaskMessages{" +
                "tmId=" + tmId +
                ", taskId=" + taskId +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", createDate=" + createDate +
                ", fullName='" + fullName + '\'' +
                '}';
    }
}
