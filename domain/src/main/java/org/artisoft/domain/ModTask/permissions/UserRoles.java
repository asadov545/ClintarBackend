package org.artisoft.domain.ModTask.permissions;


import org.artisoft.domain.ValueLabel;

public class UserRoles {
    private long userRoleId;
    private ValueLabel<Long, String> user;
    private ValueLabel<Long, String> role;
    private int status;

    public long getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(long userRoleId) {
        this.userRoleId = userRoleId;
    }

    public ValueLabel<Long, String> getUser() {
        return user;
    }

    public void setUser(ValueLabel<Long, String> user) {
        this.user = user;
    }

    public ValueLabel<Long, String> getRole() {
        return role;
    }

    public void setRole(ValueLabel<Long, String> role) {
        this.role = role;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UserRoles{" +
                "userRoleId=" + userRoleId +
                ", user=" + user +
                ", role=" + role +
                ", status=" + status +
                '}';
    }
}
