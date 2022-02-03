package org.artisoft.dal.dao.admin;

import org.artisoft.dal.dao.BaseDao;
import org.artisoft.domain.ModTask.permissions.RolePrivsCompdata;
import org.artisoft.domain.ModTask.permissions.UserRolesCompData;
import org.artisoft.domain.ValueLabel;
import org.artisoft.domain.ModTask.permissions.RolePrivs;

import java.util.List;

public interface RolePrivsDao extends BaseDao<RolePrivs> {
    List<ValueLabel<Long, String>> getRoles(String label);
    List<ValueLabel<Long, String>> getPrivs(String label);
    RolePrivsCompdata getRolePrivsCompData();

}
