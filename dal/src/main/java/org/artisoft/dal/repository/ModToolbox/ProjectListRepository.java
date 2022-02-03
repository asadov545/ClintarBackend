package org.artisoft.dal.repository.ModToolbox;

import com.zaxxer.hikari.HikariDataSource;
import org.artisoft.dal.dao.ModToolbox.ProjectListDao;
import org.artisoft.dal.dao.admin.PrivilegesDao;
import org.artisoft.domain.ModToolbox.ProjectList;
import org.artisoft.domain.ModToolbox.project.ToolboxFormSearcInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository("projectListDao")
public class ProjectListRepository implements ProjectListDao {
    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcCall insUpdProc;
    private SimpleJdbcCall delUpdProc;

    @Autowired
    private PrivilegesDao privilegesDao;

    @Autowired
    public ProjectListRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    public void setDataSource(HikariDataSource dataSource) {
        this.insUpdProc = new SimpleJdbcCall(dataSource);
        this.delUpdProc = new SimpleJdbcCall(dataSource);
    }

    @Override
    public List<ProjectList> getProjectList(HashMap<String, String> map, long user_id) {
        try {
            final String sql = " SELECT PR.PRJ_ID,PR.PRJ_CODE,PT.TITLE AS PTTITLE,PR.NAME,IFNULL(UNIX_TIMESTAMP(PR.BEGIN_DATE),0) as BEGIN_DATE,IFNULL(UNIX_TIMESTAMP(PR.END_DATE),0) as END_DATE,IFNULL(UNIX_TIMESTAMP(PR.CREATE_DATE),0) as CREATE_DATE,  getUserFullNameById(PR.SUPERVISIOR_ID) AS SUPERVISIOR FROM TB_PRJ AS PR\n" +
                    "left join TB_PRJ_TEMPLATES AS PT ON (PR.PT_ID=PT.PT_ID) " ;

                    //"left join TB_PRJ_TMPL_CATEGORIES AS PTC ON(PT.PT_CAT_ID=PTC.PT_CAT_ID) " ;

            StringBuilder cond = new StringBuilder(sql);
            List<String> paramList = new ArrayList<>();
            boolean isFirst = true;

            if (user_id == 0) {
                return null;
            }
            if ( privilegesDao.checkPrivStatus(user_id,33)!=1)
            {
                isFirst = false;
                cond.append("  where (PR.SUPERVISIOR_ID="+user_id+"  OR    EXISTS (SELECT 1 FROM  TB_PRJ_USERS AS TPU WHERE (TPU.PRJ_ID=PR.PRJ_ID AND  TPU.USER_ID="+user_id +" ))) ");
            }

            if (map.containsKey("projectCode") && !map.get("projectCode").trim().equals("")) {
                cond.append(isFirst ? " where" : " AND");
                cond.append(" PR.PRJ_CODE like concat('%',?,'%') ");
                paramList.add(map.get("projectCode") + "%");
                isFirst = false;
            }

            if (map.containsKey("projectTitle") && !map.get("projectTitle").trim().equals("")) {
                cond.append(isFirst ? " where" : " AND");
                cond.append(" PR.NAME like concat('%',?,'%') ");
                paramList.add(map.get("projectTitle") + "%");
                isFirst = false;
            }


            if (map.containsKey("templateId") && !map.get("templateId").trim().equals("") && Integer.parseInt(map.get("templateId").trim()) > 0) {
                cond.append(isFirst ? " where" : " AND");
                cond.append(" PT.PT_CAT_ID =? ");
                paramList.add(map.get("templateId"));
                isFirst = false;
            }

            if (map.containsKey("supervisior_id") && !map.get("supervisior_id").trim().equals("") && Integer.parseInt(map.get("supervisior_id").trim()) > 0) {
                cond.append(isFirst ? " where" : " AND");
                cond.append(" PR.SUPERVISIOR_ID =? ");
                paramList.add(map.get("supervisior_id"));
                isFirst = false;
            }

            //templateId2 esl template
            if (map.containsKey("templateId2") && !map.get("templateId2").trim().equals("") && Integer.parseInt(map.get("templateId2").trim()) > 0) {
                cond.append(isFirst ? " where" : " AND");
                cond.append(" PT.PT_ID =? ");
                paramList.add(map.get("templateId2"));
                isFirst = false;
            }


            ResultSetExtractor<List<ProjectList>> rm = rs -> {
                List<ProjectList> projectLists = new ArrayList<>();
                createProjectList(rs, projectLists);
                return projectLists;
            };

            cond.append(" order by PR.PRJ_ID desc ");
            if (paramList.isEmpty()) {
                return jdbcTemplate.query(cond.toString(), rm);
            } else {
                return jdbcTemplate.query(cond.toString(), paramList.toArray(), rm);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private void createProjectList(ResultSet rs, List<ProjectList> projectLists) throws SQLException {
        while (rs.next()) {
            ProjectList projectList = new ProjectList();
            projectList.setPrjId(rs.getLong("PRJ_ID"));
            projectList.setPrjCode(rs.getString("PRJ_CODE"));
            projectList.setPtTitle(rs.getString("PTTITLE"));
            projectList.setName(rs.getString("NAME"));
            projectList.setBeginDate(rs.getLong("BEGIN_DATE"));
            projectList.setEndDate(rs.getLong("END_DATE"));
            projectList.setSupervisior(rs.getString("SUPERVISIOR"));
            projectList.setCreate_date(rs.getLong("CREATE_DATE"));
            projectLists.add(projectList);
        }
    }

}
