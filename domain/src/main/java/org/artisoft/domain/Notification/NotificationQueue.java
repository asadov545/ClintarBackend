package org.artisoft.domain.Notification;

public class NotificationQueue {
private long notifQueId;
private Notification notification;
private long toUserId;
private long sendDate;
private long reserveDate;
private int type;
private int toUserType;
private String grpKey;
private int status;

    public long getNotifQueId() {
        return notifQueId;
    }

    public void setNotifQueId(long notifQueId) {
        this.notifQueId = notifQueId;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public long getToUserId() {
        return toUserId;
    }

    public void setToUserId(long toUserId) {
        this.toUserId = toUserId;
    }

    public long getSendDate() {
        return sendDate;
    }

    public void setSendDate(long sendDate) {
        this.sendDate = sendDate;
    }

    public long getReserveDate() {
        return reserveDate;
    }

    public void setReserveDate(long reserveDate) {
        this.reserveDate = reserveDate;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getToUserType() {
        return toUserType;
    }

    public void setToUserType(int toUserType) {
        this.toUserType = toUserType;
    }

    public String getGrpKey() {
        return grpKey;
    }

    public void setGrpKey(String grpKey) {
        this.grpKey = grpKey;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "NotificationQueue{" +
                "notifQueId=" + notifQueId +
                ", notification=" + notification +
                ", toUserId=" + toUserId +
                ", sendDate=" + sendDate +
                ", reserveDate=" + reserveDate +
                ", type=" + type +
                ", toUserType=" + toUserType +
                ", grpKey='" + grpKey + '\'' +
                ", status=" + status +
                '}';
    }
}
