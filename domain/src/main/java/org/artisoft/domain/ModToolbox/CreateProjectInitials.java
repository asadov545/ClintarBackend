package org.artisoft.domain.ModToolbox;

import org.artisoft.domain.ModTask.Customer;
import org.artisoft.domain.ModToolbox.project.MainProjectList;
import org.artisoft.domain.ValueLabel;

import java.util.List;

public class CreateProjectInitials {
    private List<TemplateCategory> templateCategories;
    private List<Customer> customerList;
    private String projectNo;
    private List<MainProjectList> projectList;
    private List<ValueLabel<Long, String>> userList;
    private List<ValueLabel<Long, String>> templateList;

    public List<ValueLabel<Long, String>> getUserList() {
        return userList;
    }

    public void setUserList(List<ValueLabel<Long, String>> userList) {
        this.userList = userList;
    }

    public List<ValueLabel<Long, String>> getTemplateList() {
        return templateList;
    }

    public void setTemplateList(List<ValueLabel<Long, String>> templateList) {
        this.templateList = templateList;
    }

    public List<TemplateCategory> getTemplateCategories() {
        return templateCategories;
    }

    public void setTemplateCategories(List<TemplateCategory> templateCategories) {
        this.templateCategories = templateCategories;
    }

    public List<Customer> getCustomerList() {
        return customerList;
    }

    public void setCustomerList(List<Customer> customerList) {
        this.customerList = customerList;
    }

    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    public List<MainProjectList> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<MainProjectList> projectList) {
        this.projectList = projectList;
    }
}
