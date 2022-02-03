package org.artisoft.dal.repository.ModTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zaxxer.hikari.HikariDataSource;
import org.artisoft.dal.dao.ModTask.TaskAssigneesDao;
import org.artisoft.domain.ModTask.tasks.TaskAssignees;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository("taskAssigneesDao")
public class TaskAssigneesRepository implements TaskAssigneesDao {
    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcCall insUpdProc;
    private SimpleJdbcCall delUpdProc;

    @Autowired
    public TaskAssigneesRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    public void setDataSource(HikariDataSource dataSource) {
        this.insUpdProc = new SimpleJdbcCall(dataSource);
        this.delUpdProc = new SimpleJdbcCall(dataSource);
    }

    @Override
    public List<TaskAssignees> getAllTaskAssigneesByTaskId(long taskId) {
        try {
            final String sql = " SELECT TA.ASSIGN_ID,TA.TASK_ID,TA.USER_ID,TA.ASSIGN_TYPE_ID, " +
                    " US.USERNAME,US.FULLNAME  FROM TASK_ASSIGNEES as TA LEFT JOIN USERS AS US ON (TA.USER_ID=US.USER_ID)"
            + " WHERE  TA.TASK_ID="+taskId;
            StringBuilder cond = new StringBuilder(sql);
            List<String> paramList = new ArrayList<>();


            ResultSetExtractor<List<TaskAssignees>> rm = rs -> {
                List<TaskAssignees> taskAssigneesList = new ArrayList<>();
                createTaskAssigneesList(rs, taskAssigneesList);
                return taskAssigneesList;
            };

            if (paramList.isEmpty()) {
                cond.append(" order by TA.ASSIGN_ID desc ");
                return jdbcTemplate.query(cond.toString(), rm);
            } else {
                return jdbcTemplate.query(cond.toString(), paramList.toArray(), rm);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<TaskAssignees> insertNew(TaskAssignees taskAssignees) {
        try {
            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("jsonObj", new Gson().toJson(taskAssignees))
                    .addValue("newassignIdList", 0);

            SimpleJdbcCall jdbcCall = insUpdProc.withProcedureName("TaskAssigneeInsUpd");
            jdbcCall.declareParameters(
                    new SqlParameter("jsonObj", Types.VARCHAR),
                    new SqlOutParameter("newassignIdList", Types.VARCHAR));

            Map<String, Object> resultMap = jdbcCall.execute(parameters);


            Type listType = new TypeToken<ArrayList<Long>>() {}.getType();
           List<Long> listid=  new Gson().fromJson(resultMap.get("newassignIdList").toString(),listType);

             List<TaskAssignees> taskAssigneesList= new ArrayList<>();
            for (long c : listid) {
                     taskAssigneesList.add(getById(c));
            }
            // taskAssignees.setAssignId(Long.parseLong(resultMap.get("newassignId").toString()));
            return taskAssigneesList;

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    private void createTaskAssigneesList(ResultSet rs, List<TaskAssignees> taskAssigneesList) throws SQLException {
        while (rs.next()) {
            TaskAssignees taskAssignees = new TaskAssignees();
            taskAssignees.setAssignId(rs.getLong("ASSIGN_ID"));
            taskAssignees.setTaskId(rs.getLong("TASK_ID"));
            taskAssignees.setUserId(rs.getLong("USER_ID"));
            taskAssignees.setAssignTypeId(rs.getLong("ASSIGN_TYPE_ID"));
            taskAssignees.setUsername(rs.getString("USERNAME"));
            taskAssignees.setFullname(rs.getString("FULLNAME"));
            taskAssigneesList.add(taskAssignees);
        }
    }


    @Override
    public TaskAssignees getById(long id) {

        try {
            final String sql = " SELECT TA.ASSIGN_ID,TA.TASK_ID,TA.USER_ID,TA.ASSIGN_TYPE_ID, " +
                    " US.USERNAME,US.FULLNAME  FROM TASK_ASSIGNEES as TA LEFT JOIN USERS AS US ON (TA.USER_ID=US.USER_ID)"
                    + " WHERE   TA.ASSIGN_ID=?";
            return jdbcTemplate.query(sql, new Object[]{id}, rs -> {
                if (!rs.next())
                    return null;

                TaskAssignees taskAssignees = new TaskAssignees();
                taskAssignees.setAssignId(rs.getLong("ASSIGN_ID"));
                taskAssignees.setTaskId(rs.getLong("TASK_ID"));
                taskAssignees.setUserId(rs.getLong("USER_ID"));
                taskAssignees.setAssignTypeId(rs.getLong("ASSIGN_TYPE_ID"));
                return taskAssignees;
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public long insert(TaskAssignees taskAssignees) {

        try {
            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("jsonObj", new Gson().toJson(taskAssignees))
                    .addValue("newassignId", 0);

            SimpleJdbcCall jdbcCall = insUpdProc.withProcedureName("TaskAssigneeInsUpd");
            jdbcCall.declareParameters(
                    new SqlParameter("jsonObj", Types.VARCHAR),
                    new SqlOutParameter("newassignId", Types.INTEGER));

            Map<String, Object> resultMap = jdbcCall.execute(parameters);
            taskAssignees.setAssignId(Long.parseLong(resultMap.get("newassignId").toString()));
            return 1;

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public boolean update(TaskAssignees taskAssignees) {
        return false;
    }

    @Override
    public boolean delete(long id) {

        try {
            SqlParameterSource parameters = new MapSqlParameterSource().addValue("assignId", id);
            SimpleJdbcCall jdbcCall = delUpdProc.withProcedureName("TaskAssigneesDel");
            jdbcCall.declareParameters(new SqlParameter("assignId", Types.INTEGER));
            jdbcCall.execute(parameters);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
