package org.artisoft.dal.dao.ModToolbox;

import org.artisoft.dal.dao.BaseDao;
import org.artisoft.domain.Users;
import org.artisoft.domain.ValueLabel;

import java.util.HashMap;
import java.util.List;

public interface ToolboxUserDao extends BaseDao<Users> {
    List<ValueLabel<Long,String>> getUserList();
    List<Users> filterDataByUserId(HashMap<String, String> map, long userId);
}
