package org.artisoft.domain.Report;

public class DownloadRequestResponse {
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "DownloadRequestResponse{" +
                "token='" + token + '\'' +
                '}';
    }
}
