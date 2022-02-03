package org.artisoft.dal.dao.ModToolbox;

import org.artisoft.dal.dao.BaseDao;
import org.artisoft.domain.ModToolbox.project.MainProject;

import java.util.List;

public interface MainProjectDao extends BaseDao<MainProject> {
    List<MainProject> filterData2(long userId);
    boolean deleteMainProjectUser(long mainPrjId,long userId);
}
