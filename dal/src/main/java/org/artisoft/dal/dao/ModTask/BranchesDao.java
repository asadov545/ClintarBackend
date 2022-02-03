package org.artisoft.dal.dao.ModTask;

import org.artisoft.dal.dao.BaseDao;
import org.artisoft.domain.ModTask.Branches;
import org.artisoft.domain.ValueLabel;

import java.util.List;

public interface BranchesDao extends BaseDao<Branches> {
    List<ValueLabel<Long, String>> filterBranchesList(String label);
}
