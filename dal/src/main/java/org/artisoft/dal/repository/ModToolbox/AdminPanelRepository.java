package org.artisoft.dal.repository.ModToolbox;

import com.google.gson.Gson;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.artisoft.dal.repository.ModTask.TaskRepository;
import org.artisoft.domain.ModToolbox.cpanel.CreateTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.Map;

@Repository("adminPanelRepo")
public class AdminPanelRepository {
    private static final Logger logger = LogManager.getLogger(TaskRepository.class);

    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcCall insUpdProc;
    private SimpleJdbcCall delProc;

    @Autowired
    public AdminPanelRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    public void setDataSource(HikariDataSource dataSource) {

        this.insUpdProc = new SimpleJdbcCall(dataSource);
        this.delProc = new SimpleJdbcCall(dataSource);
    }

    public Object createTemplate(CreateTemplate projectTemplate) {
        try {
            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("pRequestJson", new Gson().toJson(projectTemplate))
                    .addValue("pResponseJson", null);

            SimpleJdbcCall jdbcCall = insUpdProc.withProcedureName("CreateProjectTemplate");
            jdbcCall.declareParameters(
                    new SqlParameter("pRequestJson", Types.VARCHAR),
                    new SqlOutParameter("pResponseJson", Types.VARCHAR));

            Map<String, Object> resultMap = jdbcCall.execute(parameters);
            Object resultObject = resultMap.get("pResponseJson");

            return resultObject;

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    public Object deleteTemplate(long templateId, long logUserId) {
        try {
            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("pTemplateId", templateId)
                    .addValue("pLogUserId", logUserId)
                    .addValue("pResponseJson", null);

            SimpleJdbcCall jdbcCall = delProc.withProcedureName("DeleteTemplate");
            jdbcCall.declareParameters(
                    new SqlParameter("pTemplateId", Types.VARCHAR),
                    new SqlParameter("pLogUserId", Types.VARCHAR),
                    new SqlOutParameter("pResponseJson", Types.VARCHAR));

            Map<String, Object> resultMap = jdbcCall.execute(parameters);
            Object resultObject = resultMap.get("pResponseJson");

            return resultObject;

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }



    public boolean deleteTemplate2(long id) {
        try {
            SqlParameterSource parameters = new MapSqlParameterSource().addValue("pTemplateId", id);
            SimpleJdbcCall jdbcCall = delProc.withProcedureName("DeleteTemplate2");
            jdbcCall.declareParameters(new SqlParameter("pTemplateId", Types.INTEGER));
            jdbcCall.execute(parameters);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


}
