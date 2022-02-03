package org.artisoft.domain.Auth;

import java.util.ArrayList;
import java.util.List;

public class UserAuthInfo {
    private long userId;
    private String username;
    private String fullname;
    private List<String> roles = new ArrayList<>();
    private List<UserModule> modules = new ArrayList<>();
    private String deviceId;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<UserModule> getModules() {
        return modules;
    }

    public void setModules(List<UserModule> modules) {
        this.modules = modules;
    }
}
