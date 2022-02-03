package org.artisoft.domain.ModTask.tasks;

import java.util.List;

public class TaskAssignees {
    private long assignId;
    private long taskId;
    private long userId;
    private long assignTypeId;
    private String username;
    private String fullname;
    private List<Long> userIdList;

    public long getAssignId() {
        return assignId;
    }

    public void setAssignId(long assignId) {
        this.assignId = assignId;
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

    public long getAssignTypeId() {
        return assignTypeId;
    }

    public void setAssignTypeId(long assignTypeId) {
        this.assignTypeId = assignTypeId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public List<Long> getUserIdList() {
        return userIdList;
    }

    public void setUserIdList(List<Long> userIdList) {
        this.userIdList = userIdList;
    }
}
