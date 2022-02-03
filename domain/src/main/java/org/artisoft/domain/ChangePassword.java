package org.artisoft.domain;

public class ChangePassword {
    private long  userId;
    private String pswOld;
    private  String pswNew;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getPswOld() {
        return pswOld;
    }

    public void setPswOld(String pswOld) {
        this.pswOld = pswOld;
    }

    public String getPswNew() {
        return pswNew;
    }

    public void setPswNew(String pswNew) {
        this.pswNew = pswNew;
    }

    @Override
    public String toString() {
        return "ChangePassword{" +
                "userId=" + userId +
                ", pswOld='" + pswOld + '\'' +
                ", pswNew='" + pswNew + '\'' +
                '}';
    }
}
