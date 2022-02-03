package org.artisoft.domain.ModToolbox.project;

import org.artisoft.domain.Users;

import java.util.List;

public class MainProjectList {
    private Long  mainPrjId;
    private String  name;
    private String taskLocation;
    private String firstAidContact;


    public Long getMainPrjId() {
        return mainPrjId;
    }

    public void setMainPrjId(Long mainPrjId) {
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

    @Override
    public String toString() {
        return "MainProjectList{" +
                "mainPrjId=" + mainPrjId +
                ", name='" + name + '\'' +
                ", taskLocation='" + taskLocation + '\'' +
                ", firstAidContact='" + firstAidContact + '\'' +
                '}';
    }
}
