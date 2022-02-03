package org.artisoft.domain.ModTask.reports;

public class TaskResultData {
    private long taskId;
    private int taskStatusId;
    private String code;
    private String title;
    private int priorityId;
    private long beginDate;
    private long dueDate;


    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public int getTaskStatusId() {
        return taskStatusId;
    }

    public void setTaskStatusId(int taskStatusId) {
        this.taskStatusId = taskStatusId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPriorityId() {
        return priorityId;
    }

    public void setPriorityId(int priorityId) {
        this.priorityId = priorityId;
    }

    public long getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(long beginDate) {
        this.beginDate = beginDate;
    }

    public long getDueDate() {
        return dueDate;
    }

    public void setDueDate(long dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public String toString() {
        return "TaskResultData{" +
                "taskId=" + taskId +
                ", taskStatusId=" + taskStatusId +
                ", code='" + code + '\'' +
                ", title='" + title + '\'' +
                ", priorityId=" + priorityId +
                ", beginDate=" + beginDate +
                ", dueDate=" + dueDate +
                '}';
    }
}
