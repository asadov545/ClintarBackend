package org.artisoft.domain.ModTask.tasks;

public class TaskStatus {
    private long taskId;
    private int taskStatusId;
    private long userId;

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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "TaskStatus{" +
                "taskId=" + taskId +
                ", taskStatusId=" + taskStatusId +
                ", userId=" + userId +
                '}';
    }
}
