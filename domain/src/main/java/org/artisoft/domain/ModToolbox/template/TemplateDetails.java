package org.artisoft.domain.ModToolbox.template;

import java.util.List;

public class TemplateDetails {
    private List<CheckList> checkList;
    private List<CheckListOption> checkListOptions;
    private List<Task> tasks;
    private List<CompletionQuestion> cmplQuestions;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<CheckList> getCheckList() {
        return checkList;
    }

    public void setCheckList(List<CheckList> checkList) {
        this.checkList = checkList;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<CompletionQuestion> getCmplQuestions() {
        return cmplQuestions;
    }

    public void setCmplQuestions(List<CompletionQuestion> cmplQuestions) {
        this.cmplQuestions = cmplQuestions;
    }

    public List<CheckListOption> getCheckListOptions() {
        return checkListOptions;
    }

    public void setCheckListOptions(List<CheckListOption> checkListOptions) {
        this.checkListOptions = checkListOptions;
    }
}
