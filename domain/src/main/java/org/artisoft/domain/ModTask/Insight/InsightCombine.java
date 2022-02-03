package org.artisoft.domain.ModTask.Insight;

import org.artisoft.domain.ValueLabel;

import java.util.List;

public class InsightCombine {
    private List<InsightTaskStatusList> insightTaskStatusLists;
    private List<ValueLabel<Integer, String>> customerTasks;

    public List<InsightTaskStatusList> getInsightTaskStatusLists() {
        return insightTaskStatusLists;
    }

    public void setInsightTaskStatusLists(List<InsightTaskStatusList> insightTaskStatusLists) {
        this.insightTaskStatusLists = insightTaskStatusLists;
    }

    public List<ValueLabel<Integer, String>> getCustomerTasks() {
        return customerTasks;
    }

    public void setCustomerTasks(List<ValueLabel<Integer, String>> customerTasks) {
        this.customerTasks = customerTasks;
    }

    @Override
    public String toString() {
        return "InsightCombine{" +
                "insightTaskStatusLists=" + insightTaskStatusLists +
                ", customerTasks=" + customerTasks +
                '}';
    }
}
