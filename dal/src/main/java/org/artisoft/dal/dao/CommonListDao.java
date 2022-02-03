package org.artisoft.dal.dao;

import org.artisoft.domain.CommonList;
import org.artisoft.domain.ModTask.TaskAssignUsers;
import org.artisoft.domain.ValueLabel;

import java.util.List;

public interface CommonListDao {
    List<ValueLabel<Long, String>> getTaskPriorityList();
    List<ValueLabel<Long, String>> getTaskStatusList();
    CommonList getCommonList(Long user_id);
    List<TaskAssignUsers> getTaskAssignUsers(Long user_id);
}
