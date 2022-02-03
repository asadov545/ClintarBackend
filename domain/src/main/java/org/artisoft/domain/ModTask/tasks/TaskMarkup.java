package org.artisoft.domain.ModTask.tasks;

public class TaskMarkup {
    private long taskId;
    private  String markup;

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public String getMarkup() {
        return markup;
    }

    public void setMarkup(String markup) {
        this.markup = markup;
    }

    @Override
    public String toString() {
        return "TaskMarkup{" +
                "taskId=" + taskId +
                ", markup='" + markup + '\'' +
                '}';
    }
}
