package org.artisoft.domain.ModTask.permissions;

import org.artisoft.domain.ValueLabel;

public class RolePrivs {
    private int rolePrivId;
    private ValueLabel<Long, String> role;
    private ValueLabel<Long, String> priv;
    private int status;

    public ValueLabel<Long, String> getRole() {
        return role;
    }

    public void setRole(ValueLabel<Long, String> role) {
        this.role = role;
    }

    public ValueLabel<Long, String> getPriv() {
        return priv;
    }

    public void setPriv(ValueLabel<Long, String> priv) {
        this.priv = priv;
    }

    public int getRolePrivId() {
        return rolePrivId;
    }

    public void setRolePrivId(int rolePrivId) {
        this.rolePrivId = rolePrivId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "RolePrivs{" +
                "rolePrivId=" + rolePrivId +
                ", role=" + role +
                ", priv=" + priv +
                ", status=" + status +
                '}';
    }
}
