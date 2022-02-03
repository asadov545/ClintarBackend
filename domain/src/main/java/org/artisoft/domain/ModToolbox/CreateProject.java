package org.artisoft.domain.ModToolbox;

import org.artisoft.domain.ModToolbox.hr.HR_1_1;
import org.artisoft.domain.ModToolbox.hr.HR_1_2;
import org.artisoft.domain.ModToolbox.project.SignerInfo;
import org.artisoft.domain.ModToolbox.template.CheckList;
import org.artisoft.domain.ModToolbox.template.CheckListOption;
import org.artisoft.domain.ModToolbox.template.CompletionQuestion;
import org.artisoft.domain.ModToolbox.template.Task;
import org.artisoft.domain.ValueLabel;

import java.util.List;
import java.util.Map;

public class CreateProject {
    private String projectName;
    private String taskLocation;
    private String firstAidContact;
    private int templateId;
    private ValueLabel<Long, String> customer;
    private List<CheckList> checkList;
    private List<CheckListOption> checkListOptions;
    private List<Task> tasks;
    private List<SignerInfo> signatures;
    private SignerInfo supervisorSign;
    private List<CompletionQuestion> cmplQuestions;
    private List<Integer> checkedKeys;
    private Map<String, CheckListOption> checkedItems;
    private long startDate;
    private long endDate;
    private String comment;
    private List<HR_1_2> hr_1_2_list;
    private HR_1_1 hr_1_1;

    public List<HR_1_2> getHr_1_2_list() {
        return hr_1_2_list;
    }

    public void setHr_1_2_list(List<HR_1_2> hr_1_2_list) {
        this.hr_1_2_list = hr_1_2_list;
    }

    public HR_1_1 getHr_1_1() {
        return hr_1_1;
    }

    public void setHr_1_1(HR_1_1 hr_1_1) {
        this.hr_1_1 = hr_1_1;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
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

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public ValueLabel<Long, String> getCustomer() {
        return customer;
    }

    public void setCustomer(ValueLabel<Long, String> customer) {
        this.customer = customer;
    }

    public List<CheckList> getCheckList() {
        return checkList;
    }

    public void setCheckList(List<CheckList> checkList) {
        this.checkList = checkList;
    }

    public List<CheckListOption> getCheckListOptions() {
        return checkListOptions;
    }

    public void setCheckListOptions(List<CheckListOption> checkListOptions) {
        this.checkListOptions = checkListOptions;
    }

    public List<Integer> getCheckedKeys() {
        return checkedKeys;
    }

    public void setCheckedKeys(List<Integer> checkedKeys) {
        this.checkedKeys = checkedKeys;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<SignerInfo> getSignatures() {
        return signatures;
    }

    public void setSignatures(List<SignerInfo> signatures) {
        this.signatures = signatures;
    }

    public SignerInfo getSupervisorSign() {
        return supervisorSign;
    }

    public void setSupervisorSign(SignerInfo supervisorSign) {
        this.supervisorSign = supervisorSign;
    }

    public List<CompletionQuestion> getCmplQuestions() {
        return cmplQuestions;
    }

    public void setCmplQuestions(List<CompletionQuestion> cmplQuestions) {
        this.cmplQuestions = cmplQuestions;
    }

    public Map<String, CheckListOption> getCheckedItems() {
        return checkedItems;
    }

    public void setCheckedItems(Map<String, CheckListOption> checkedItems) {
        this.checkedItems = checkedItems;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
