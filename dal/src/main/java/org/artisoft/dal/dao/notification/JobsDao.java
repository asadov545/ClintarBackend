package org.artisoft.dal.dao.notification;

import org.artisoft.dal.dao.BaseDao;
import org.artisoft.domain.ModTask.permissions.RolePrivsCompdata;
import org.artisoft.domain.Notification.GetJobs;
import org.artisoft.domain.Notification.Jobs;
import org.artisoft.domain.Notification.JobsCompdata;

import java.util.HashMap;
import java.util.List;

public interface JobsDao extends BaseDao<Jobs> {

    JobsCompdata getJobsCompData();
     List<GetJobs> getAllJobs(HashMap<String, String> map);
}
