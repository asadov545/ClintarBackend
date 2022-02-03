package org.artisoft.domain.Notification;

import org.artisoft.domain.ValueLabel;

public class MobileNotifcationList {

    private long notifQueId;
    private ValueLabel<Long, String> notifType;
    private String title;
    private String content;
    private long time;
    private long taskId;
    private String deviceId;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public long getNotifQueId() {
        return notifQueId;
    }

    public void setNotifQueId(long notifQueId) {
        this.notifQueId = notifQueId;
    }

    public ValueLabel<Long, String> getNotifType() {
        return notifType;
    }

    public void setNotifType(ValueLabel<Long, String> notifType) {
        this.notifType = notifType;
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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    @Override
    public String toString() {
        return "MobileNotifcationList{" +
                "notifQueId=" + notifQueId +
                ", notifType=" + notifType +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", time=" + time +
                ", taskId=" + taskId +
                '}';
    }
}
