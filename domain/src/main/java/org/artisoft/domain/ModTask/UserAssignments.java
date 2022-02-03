package org.artisoft.domain.ModTask;

import org.artisoft.domain.ValueLabel;

public class UserAssignments {

    private long userAssignmentId;
    private ValueLabel<Long, String> user;
    private ValueLabel<Long, String> assignuser;
    private int status;

    public long getUserAssignmentId() {
        return userAssignmentId;
    }

    public void setUserAssignmentId(long userAssignmentId) {
        this.userAssignmentId = userAssignmentId;
    }

    public ValueLabel<Long, String> getUser() {
        return user;
    }

    public void setUser(ValueLabel<Long, String> user) {
        this.user = user;
    }

    public ValueLabel<Long, String> getAssignuser() {
        return assignuser;
    }

    public void setAssignuser(ValueLabel<Long, String> assignuser) {
        this.assignuser = assignuser;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UserAssignments{" +
                "userAssignmentId=" + userAssignmentId +
                ", user=" + user +
                ", assignuser=" + assignuser +
                ", status=" + status +
                '}';
    }
}
