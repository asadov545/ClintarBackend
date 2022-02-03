package org.artisoft.dal.dao.ModTask;

import org.artisoft.dal.dao.BaseDao;
import org.artisoft.domain.ModTask.UserAssignments;

import java.util.HashMap;
import java.util.List;

public interface UserAssignmentsDao extends BaseDao<UserAssignments> {
    List<UserAssignments> filterData2(HashMap<String, String> map,Long user_id);
}
