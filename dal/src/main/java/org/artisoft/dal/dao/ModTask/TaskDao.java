package org.artisoft.dal.dao.ModTask;

import org.artisoft.dal.dao.BaseDao;
import org.artisoft.domain.ModTask.tasks.*;
import org.artisoft.domain.Users;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public interface TaskDao extends BaseDao<Task> {
    List<TaskPhoto> getAllTaskPhotosByTaskId(long taskId);
    long insertNew(Task task) throws AddressException, MessagingException, IOException;
    boolean updateNew(Task task) throws AddressException, MessagingException, IOException;
    String changeTaskStatus(TaskStatus taskStatus);
    StatusInfo getTaskInfo(Long taskId, Long userId);
    List<Task> filterData2(HashMap<String, String> map, long user_id);
    String updateMarkup(TaskMarkup taskMarkup);

}
