package org.artisoft.domain.Auth;

public class LoginStatusInfo {
    private String username;
    private int loginStatus;
    private String loginIpAddr;
    private int isCaptchaProvided;

    public LoginStatusInfo(String _username, int _loginStatus, String _loginIpAddr, int _isCaptchaProvided) {
        this.username = _username;
        this.loginStatus = _loginStatus;
        this.loginIpAddr = _loginIpAddr;
        this.isCaptchaProvided = _isCaptchaProvided;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(int loginStatus) {
        this.loginStatus = loginStatus;
    }

    public String getLoginIpAddr() {
        return loginIpAddr;
    }

    public void setLoginIpAddr(String loginIpAddr) {
        this.loginIpAddr = loginIpAddr;
    }

    public int getIsCaptchaProvided() {
        return isCaptchaProvided;
    }

    public void setIsCaptchaProvided(int isCaptchaProvided) {
        this.isCaptchaProvided = isCaptchaProvided;
    }
}
