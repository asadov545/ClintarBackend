package org.artisoft.dal.dao.admin;

import org.artisoft.dal.dao.BaseDao;
import org.artisoft.domain.ModTask.permissions.Privileges;

import java.util.List;

public interface PrivilegesDao extends BaseDao<Privileges> {
    Integer checkPrivStatus(long user_id, long priv_id);
    List<Long> getPriviligiesList(Long user_id);
}
