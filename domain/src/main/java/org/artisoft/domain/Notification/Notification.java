package org.artisoft.domain.Notification;

public class Notification {
    private long notifId;
    private long notifTypeId;
    private String title;
    private String content;
    private long taskId;

    public long getNotifId() {
        return notifId;
    }

    public void setNotifId(long notifId) {
        this.notifId = notifId;
    }

    public long getNotifTypeId() {
        return notifTypeId;
    }

    public void setNotifTypeId(long notifTypeId) {
        this.notifTypeId = notifTypeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "notifId=" + notifId +
                ", notifTypeId=" + notifTypeId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", taskId=" + taskId +
                '}';
    }
}
