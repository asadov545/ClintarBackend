package org.artisoft.dal.repository.ModToolbox;

import com.zaxxer.hikari.HikariDataSource;
import org.artisoft.domain.ModToolbox.ProjectList;
import org.artisoft.domain.ModToolbox.ProjectTemplateList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository("projectTemplateListRepo")
public class ProjectTemplateListRepository {

    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcCall insUpdProc;
    private SimpleJdbcCall delProc;

    @Autowired
    public ProjectTemplateListRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    public void setDataSource(HikariDataSource dataSource) {
        this.insUpdProc = new SimpleJdbcCall(dataSource);
        this.delProc = new SimpleJdbcCall(dataSource);
    }

    public List<ProjectTemplateList> getProjectTemplateList(long user_id) {
        try {
            final String sql = " SELECT TPT.PT_ID,TPTC.TITLE AS CATTITLE,TPT.TITLE,IFNULL(UNIX_TIMESTAMP(TPT.CREATE_DATE),0) as CREATE_DATE FROM TB_PRJ_TEMPLATES" +
                    " AS TPT LEFT JOIN  TB_PRJ_TMPL_CATEGORIES AS TPTC ON(TPT.PT_CAT_ID=TPTC.PT_CAT_ID) where TPT.status=1";

            StringBuilder cond = new StringBuilder(sql);
            ResultSetExtractor<List<ProjectTemplateList>> rm = rs -> {
                List<ProjectTemplateList> projectTemplateLists = new ArrayList<>();
                createProjectTemplateList(rs, projectTemplateLists);
                return projectTemplateLists;
            };
            cond.append(" ORDER  BY TPT.PT_ID desc ");
            return jdbcTemplate.query(cond.toString(), rm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private void createProjectTemplateList(ResultSet rs, List<ProjectTemplateList> projectTemplateLists) throws SQLException {
        while (rs.next()) {
            ProjectTemplateList projectTemplateList = new ProjectTemplateList();
            projectTemplateList.setPtId(rs.getLong("PT_ID"));
            projectTemplateList.setCategory(rs.getString("CATTITLE"));
            projectTemplateList.setTempName(rs.getString("TITLE"));
            projectTemplateList.setCreateDate(rs.getLong("CREATE_DATE"));
            projectTemplateLists.add(projectTemplateList);
        }
    }

}
