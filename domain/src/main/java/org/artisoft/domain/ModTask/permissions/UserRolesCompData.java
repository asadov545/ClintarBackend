package org.artisoft.domain.ModTask.permissions;

import org.artisoft.domain.ValueLabel;

import java.util.List;

public class UserRolesCompData {

    private List<ValueLabel<Long, String>> RoleList;
    private List<ValueLabel<Long, String>> UserList;

    public List<ValueLabel<Long, String>> getRoleList() {
        return RoleList;
    }

    public void setRoleList(List<ValueLabel<Long, String>> roleList) {
        RoleList = roleList;
    }

    public List<ValueLabel<Long, String>> getUserList() {
        return UserList;
    }

    public void setUserList(List<ValueLabel<Long, String>> userList) {
        UserList = userList;
    }

    @Override
    public String toString() {
        return "UserRolesCompData{" +
                "RoleList=" + RoleList +
                ", UserList=" + UserList +
                '}';
    }
}
