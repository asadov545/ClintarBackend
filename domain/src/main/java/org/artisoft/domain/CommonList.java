package org.artisoft.domain;

import org.artisoft.domain.ModTask.TaskAssignUsers;

import java.util.List;

public class CommonList {

    private List<ValueLabel<Long, String>> taskPriorityList;
    private List<ValueLabel<Long, String>> taskStatusList;
    private List<ValueLabel<Long, String>> assignTypesList;
    private List<TaskAssignUsers> taskAssignUsersList;
    private List<Long> privs;

    public List<ValueLabel<Long, String>> getTaskPriorityList() {
        return taskPriorityList;
    }

    public void setTaskPriorityList(List<ValueLabel<Long, String>> taskPriorityList) {
        this.taskPriorityList = taskPriorityList;
    }

    public List<ValueLabel<Long, String>> getTaskStatusList() {
        return taskStatusList;
    }

    public void setTaskStatusList(List<ValueLabel<Long, String>> taskStatusList) {
        this.taskStatusList = taskStatusList;
    }

    public List<ValueLabel<Long, String>> getAssignTypesList() {
        return assignTypesList;
    }

    public void setAssignTypesList(List<ValueLabel<Long, String>> assignTypesList) {
        this.assignTypesList = assignTypesList;
    }

    public List<TaskAssignUsers> getTaskAssignUsersList() {
        return taskAssignUsersList;
    }

    public void setTaskAssignUsersList(List<TaskAssignUsers> taskAssignUsersList) {
        this.taskAssignUsersList = taskAssignUsersList;
    }

    public List<Long> getPrivs() {
        return privs;
    }

    public void setPrivs(List<Long> privs) {
        this.privs = privs;
    }

    @Override
    public String toString() {
        return "CommonList{" +
                "taskPriorityList=" + taskPriorityList +
                ", taskStatusList=" + taskStatusList +
                ", assignTypesList=" + assignTypesList +
                ", taskAssignUsersList=" + taskAssignUsersList +
                ", privs=" + privs +
                '}';
    }
}
