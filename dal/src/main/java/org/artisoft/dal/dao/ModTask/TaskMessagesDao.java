package org.artisoft.dal.dao.ModTask;

import org.artisoft.dal.dao.BaseDao;
import org.artisoft.domain.ModTask.tasks.TaskMessages;

import java.util.List;

public interface TaskMessagesDao extends BaseDao<TaskMessages> {

    List<TaskMessages> getAllTaskMessageByTaskId(long taskId);
}
