package org.artisoft.domain.ModToolbox.cpanel;

import java.util.List;

public class CreateTemplate {
    private CreateProjectType projectType;
    private List<CreateCheckList> checkList;
    private List<CreateTask> tasks;
    private List<CreateCmplQuestion> cmplQuestions;

    public CreateProjectType getProjectType() {
        return projectType;
    }

    public void setProjectType(CreateProjectType projectType) {
        this.projectType = projectType;
    }

    public List<CreateCheckList> getCheckList() {
        return checkList;
    }

    public void setCheckList(List<CreateCheckList> checkList) {
        this.checkList = checkList;
    }

    public List<CreateTask> getTasks() {
        return tasks;
    }

    public void setTasks(List<CreateTask> tasks) {
        this.tasks = tasks;
    }

    public List<CreateCmplQuestion> getCmplQuestions() {
        return cmplQuestions;
    }

    public void setCmplQuestions(List<CreateCmplQuestion> cmplQuestions) {
        this.cmplQuestions = cmplQuestions;
    }
}
