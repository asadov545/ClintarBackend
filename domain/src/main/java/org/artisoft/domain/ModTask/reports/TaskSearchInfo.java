package org.artisoft.domain.ModTask.reports;

import java.util.List;

public class TaskSearchInfo {
    private long taskId;
    private String code;
    private String title;
    private int taskStatusId;
    private List<Long> assigneeIds;
    private DateType beginDate;
    private DateType dueDate;
    private int priorityId;
    private long userId;

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
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

    public int getTaskStatusId() {
        return taskStatusId;
    }

    public void setTaskStatusId(int taskStatusId) {
        this.taskStatusId = taskStatusId;
    }

    public List<Long> getAssigneeIds() {
        return assigneeIds;
    }

    public void setAssigneeIds(List<Long> assigneeIds) {
        this.assigneeIds = assigneeIds;
    }

    public DateType getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(DateType beginDate) {
        this.beginDate = beginDate;
    }

    public DateType getDueDate() {
        return dueDate;
    }

    public void setDueDate(DateType dueDate) {
        this.dueDate = dueDate;
    }

    public int getPriorityId() {
        return priorityId;
    }

    public void setPriorityId(int priorityId) {
        this.priorityId = priorityId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "TaskSearchInfo{" +
                "code='" + code + '\'' +
                ", title='" + title + '\'' +
                ", taskStatusId=" + taskStatusId +
                ", assigneeIds=" + assigneeIds +
                ", beginDate=" + beginDate +
                ", dueDate=" + dueDate +
                ", priorityId=" + priorityId +
                ", userId=" + userId +
                '}';
    }
}
