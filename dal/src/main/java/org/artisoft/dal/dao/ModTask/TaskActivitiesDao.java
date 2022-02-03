package org.artisoft.dal.dao.ModTask;

import org.artisoft.dal.dao.BaseDao;
import org.artisoft.domain.ModTask.tasks.TaskActivities;

import java.util.List;

public interface TaskActivitiesDao extends BaseDao<TaskActivities> {
    List<TaskActivities> getAllTaskActivitiesByTaskId(long taskId);

}
