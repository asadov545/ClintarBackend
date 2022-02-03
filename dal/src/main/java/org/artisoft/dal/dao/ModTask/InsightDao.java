package org.artisoft.dal.dao.ModTask;

import org.artisoft.domain.ModTask.Insight.InsightCombine;
import org.artisoft.domain.ModTask.Insight.InsightTaskStatusList;
import org.artisoft.domain.ValueLabel;

import java.util.List;

public interface InsightDao {
    InsightCombine getInsightCombine(long user_id);
    List<InsightTaskStatusList> getTaskStatusList(long user_id);
}
