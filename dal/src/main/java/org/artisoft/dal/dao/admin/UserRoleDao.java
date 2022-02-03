package org.artisoft.dal.dao.admin;

import org.artisoft.dal.dao.BaseDao;
import org.artisoft.domain.ModTask.permissions.UserRolesCompData;
import org.artisoft.domain.ValueLabel;
import org.artisoft.domain.ModTask.permissions.UserRoles;

import java.util.List;

public interface UserRoleDao extends BaseDao<UserRoles> {
    List<ValueLabel<Long, String>> getUsers(String label);
    UserRolesCompData getUserRolesCompData();
}
