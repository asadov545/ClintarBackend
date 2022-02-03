package org.artisoft.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest implements Serializable {
    private String username;
    private String password;
    private int captchaProvided;

    public int getCaptchaProvided() {
        return captchaProvided;
    }

    public void setCaptchaProvided(int captchaProvided) {
        this.captchaProvided = captchaProvided;
    }

    public boolean isCaptchaProvided() {
        return captchaProvided == 1;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}