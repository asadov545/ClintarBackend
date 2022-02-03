package org.artisoft.domain.ModTask.tasks;

import java.util.List;

public class TaskAssineeList {

    private long taskId;
    private List<Long> userIdList;
    private  long assignTypeId;

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public List<Long> getUserIdList() {
        return userIdList;
    }

    public void setUserIdList(List<Long> userIdList) {
        this.userIdList = userIdList;
    }

    public long getAssignTypeId() {
        return assignTypeId;
    }

    public void setAssignTypeId(long assignTypeId) {
        this.assignTypeId = assignTypeId;
    }

    @Override
    public String toString() {
        return "TaskAssineeList{" +
                "taskId=" + taskId +
                ", userIdList=" + userIdList +
                ", assignTypeId=" + assignTypeId +
                '}';
    }
}
