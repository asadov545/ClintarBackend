package org.artisoft.domain.ModTask.permissions;

import org.artisoft.domain.ValueLabel;

import java.util.List;

public class RolePrivsCompdata {

    private List<ValueLabel<Long, String>> RoleList;
    private List<ValueLabel<Long, String>> PrivList;

    public List<ValueLabel<Long, String>> getRoleList() {
        return RoleList;
    }

    public void setRoleList(List<ValueLabel<Long, String>> roleList) {
        RoleList = roleList;
    }

    public List<ValueLabel<Long, String>> getPrivList() {
        return PrivList;
    }

    public void setPrivList(List<ValueLabel<Long, String>> privList) {
        PrivList = privList;
    }

    @Override
    public String toString() {
        return "RolePrivsCompdata{" +
                "RoleList=" + RoleList +
                ", PrivList=" + PrivList +
                '}';
    }
}
