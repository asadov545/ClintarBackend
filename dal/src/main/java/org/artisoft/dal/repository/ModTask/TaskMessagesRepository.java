package org.artisoft.dal.repository.ModTask;

import com.google.gson.Gson;
import com.zaxxer.hikari.HikariDataSource;
import org.artisoft.dal.dao.ModTask.TaskMessagesDao;
import org.artisoft.domain.ModTask.tasks.TaskMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository("taskMessagesDao")
public class TaskMessagesRepository implements TaskMessagesDao {

    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcCall insUpdProc;
    private SimpleJdbcCall delUpdProc;

    @Autowired
    public TaskMessagesRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    public void setDataSource(HikariDataSource dataSource) {
        this.insUpdProc = new SimpleJdbcCall(dataSource);
        this.delUpdProc = new SimpleJdbcCall(dataSource);
    }

    @Override
    public TaskMessages getById(long id) {
        try {
            final String sql = " SELECT TM_ID,TASK_ID,getUserFullNameById(USER_ID) as FULLNAME,USER_ID,TITLE,`DESC`, IFNULL(UNIX_TIMESTAMP(CREATE_DATE),0) as CREATE_DATE FROM TASK_MESSAGES  "
                    + " WHERE  TM_ID=?";
            return jdbcTemplate.query(sql, new Object[]{id}, rs -> {
                if (!rs.next())
                    return null;

                TaskMessages taskMessages = new TaskMessages();
                taskMessages.setTmId(rs.getLong("TM_ID"));
                taskMessages.setTaskId(rs.getLong("TASK_ID"));
                taskMessages.setUserId(rs.getLong("USER_ID"));
                taskMessages.setTitle(rs.getString("TITLE"));
                taskMessages.setDesc(rs.getString("DESC"));
                taskMessages.setCreateDate(rs.getLong("CREATE_DATE"));
                taskMessages.setFullName(rs.getString("FULLNAME"));
                return taskMessages;
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public long insert(TaskMessages taskMessages) {
        try {
            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("jsonObj", new Gson().toJson(taskMessages))
                    .addValue("newtmId", 0);

            SimpleJdbcCall jdbcCall = insUpdProc.withProcedureName("TaskMessagesInsUpd");
            jdbcCall.declareParameters(
                    new SqlParameter("jsonObj", Types.VARCHAR),
                    new SqlOutParameter("newtmId", Types.INTEGER));

            Map<String, Object> resultMap = jdbcCall.execute(parameters);
            taskMessages.setTmId(Long.parseLong(resultMap.get("newtmId").toString()));
            return 1;

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public boolean update(TaskMessages taskMessages) {
        return false;
    }

    @Override
    public boolean delete(long id) {
        return false;
    }

    @Override
    public List<TaskMessages> getAll() {
        return null;
    }

    @Override
    public List<TaskMessages> getAllTaskMessageByTaskId(long taskId) {
        try {
            final String sql = " SELECT TM_ID,TASK_ID,USER_ID,getUserFullNameById(USER_ID) as FULLNAME,TITLE,`DESC`, IFNULL(UNIX_TIMESTAMP(CREATE_DATE),0) as CREATE_DATE FROM TASK_MESSAGES  "
                                   + " WHERE  TASK_ID="+taskId;
            StringBuilder cond = new StringBuilder(sql);

            ResultSetExtractor<List<TaskMessages>> rm = rs -> {
                List<TaskMessages> taskMessagesList = new ArrayList<>();
                createTaskMessagesList(rs, taskMessagesList);
                return taskMessagesList;
            };


                cond.append(" order by TM_ID desc ");
                return jdbcTemplate.query(cond.toString(), rm);



        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void createTaskMessagesList(ResultSet rs, List<TaskMessages> taskMessagesList) throws SQLException {
        while (rs.next()) {
            TaskMessages taskMessages = new TaskMessages();
            taskMessages.setTmId(rs.getLong("TM_ID"));
            taskMessages.setTaskId(rs.getLong("TASK_ID"));
            taskMessages.setUserId(rs.getLong("USER_ID"));
            taskMessages.setTitle(rs.getString("TITLE"));
            taskMessages.setDesc(rs.getString("DESC"));
            taskMessages.setCreateDate(rs.getLong("CREATE_DATE"));
            taskMessages.setFullName(rs.getString("FULLNAME"));
            taskMessagesList.add(taskMessages);
        }
    }

}
