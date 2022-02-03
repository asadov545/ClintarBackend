package org.artisoft.dal.dao.ModTask;

import org.artisoft.dal.dao.BaseDao;
import org.artisoft.domain.ModTask.tasks.TaskAssignees;

import java.util.List;

public interface TaskAssigneesDao extends BaseDao<TaskAssignees> {
    List<TaskAssignees> getAllTaskAssigneesByTaskId(long taskId);

    List<TaskAssignees> insertNew(TaskAssignees taskAssignees);
}
