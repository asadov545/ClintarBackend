package org.artisoft.dal.repository.ModTask;

import com.zaxxer.hikari.HikariDataSource;
import org.artisoft.dal.dao.ModTask.TaskActivitiesDao;
import org.artisoft.domain.ModTask.tasks.TaskActivities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository("taskActivitiesDao")
public class TaskActivitiesRepository implements TaskActivitiesDao {

    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcCall insUpdProc;
    private SimpleJdbcCall delUpdProc;

    @Autowired
    public TaskActivitiesRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    public void setDataSource(HikariDataSource dataSource) {
        this.insUpdProc = new SimpleJdbcCall(dataSource);
        this.delUpdProc = new SimpleJdbcCall(dataSource);
    }

    @Override
    public List<TaskActivities> getAllTaskActivitiesByTaskId(long taskId) {
        try {
            final String sql = " SELECT TSU.TSU_ID,getUserFullNameById(TSU.USER_ID) as FULLNAME,TS.TITLE ,IFNULL(UNIX_TIMESTAMP(TSU.CREATE_DATE),0) as CREATE_DATE FROM TASK_STATUS_USERS AS TSU\n" +
                    "LEFT JOIN TASK_STATUS AS TS ON (TS.TASK_STATUS_ID=TSU.TASK_STATUS_ID) "
                    + " WHERE  TSU.TASK_ID="+taskId;
            StringBuilder cond = new StringBuilder(sql);

            ResultSetExtractor<List<TaskActivities>> rm = rs -> {
                List<TaskActivities> taskActivitiesArrayList = new ArrayList<>();
                createTaskActivitiesList(rs, taskActivitiesArrayList);
                return taskActivitiesArrayList;
            };


            cond.append(" order by TSU_ID desc ");
            return jdbcTemplate.query(cond.toString(), rm);



        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void createTaskActivitiesList(ResultSet rs, List<TaskActivities> taskActivitiesList) throws SQLException {
        while (rs.next()) {
            TaskActivities taskActivities = new TaskActivities();
            taskActivities.setTsuId(rs.getLong("TSU_ID"));
            taskActivities.setStatus(rs.getString("TITLE"));
            taskActivities.setCreateDate(rs.getLong("CREATE_DATE"));
            taskActivities.setFullName(rs.getString("FULLNAME"));
            taskActivitiesList.add(taskActivities);
        }
    }


    @Override
    public TaskActivities getById(long id) {
        return null;
    }

    @Override
    public long insert(TaskActivities taskActivities) {
        return 0;
    }

    @Override
    public boolean update(TaskActivities taskActivities) {
        return false;
    }

    @Override
    public boolean delete(long id) {
        return false;
    }
}
