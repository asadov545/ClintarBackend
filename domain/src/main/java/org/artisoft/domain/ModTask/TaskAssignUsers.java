package org.artisoft.domain.ModTask;

public class TaskAssignUsers {
    private  Long userId;
    private  String userName;
    private  String fullname;
    private  int status;
    private  Long branchId;
    private int userTypeId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public int getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(int userTypeId) {
        this.userTypeId = userTypeId;
    }

    @Override
    public String toString() {
        return "TaskAssignUsers{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", fullname='" + fullname + '\'' +
                ", status=" + status +
                ", branchId=" + branchId +
                ", userTypeId=" + userTypeId +
                '}';
    }
}
