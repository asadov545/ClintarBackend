package org.artisoft.domain.ModToolbox.project;

import org.artisoft.domain.Users;

import java.util.List;

public class MainProject {
    private long  mainPrjId;
    private String  name;
    private String taskLocation;
    private String firstAidContact;
    private long createDate;
    private long createUserId;
    private List<Long> usersList;

    public long getMainPrjId() {
        return mainPrjId;
    }

    public void setMainPrjId(long mainPrjId) {
        this.mainPrjId = mainPrjId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTaskLocation() {
        return taskLocation;
    }

    public void setTaskLocation(String taskLocation) {
        this.taskLocation = taskLocation;
    }

    public String getFirstAidContact() {
        return firstAidContact;
    }

    public void setFirstAidContact(String firstAidContact) {
        this.firstAidContact = firstAidContact;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(long createUserId) {
        this.createUserId = createUserId;
    }

    public List<Long> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<Long> usersList) {
        this.usersList = usersList;
    }

    @Override
    public String toString() {
        return "MainProject{" +
                "mainPrjId=" + mainPrjId +
                ", name='" + name + '\'' +
                ", taskLocation='" + taskLocation + '\'' +
                ", firstAidContact='" + firstAidContact + '\'' +
                ", createDate=" + createDate +
                ", createUserId=" + createUserId +
                ", usersList=" + usersList +
                '}';
    }
}
