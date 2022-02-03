package org.artisoft.dal.dao.ModTask;


import org.artisoft.domain.ModTask.reports.TaskResultData;
import org.artisoft.domain.ModTask.reports.TaskSearchInfo;
import org.artisoft.domain.ModTask.reports.UserCustResultData;
import org.artisoft.domain.ModTask.reports.UserCustSearchİnfo;

import java.util.List;

public interface ReportDao {
    List<TaskResultData> filterTaskList(TaskSearchInfo taskSearchInfo);
    List<UserCustResultData> filterUserCustList(UserCustSearchİnfo userCustSearchİnfo);
}
