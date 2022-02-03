package org.artisoft.dal.dao.ModToolbox;

import org.artisoft.domain.ModToolbox.ProjectList;
import org.artisoft.domain.ModToolbox.project.ToolboxFormSearcInfo;

import java.util.HashMap;
import java.util.List;

public interface ProjectListDao {
    List<ProjectList> getProjectList(HashMap<String, String> map, long user_id);
}
