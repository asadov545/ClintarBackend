package org.artisoft.dal.repository;

import com.zaxxer.hikari.HikariDataSource;
import org.artisoft.dal.dao.CommonListDao;
import org.artisoft.dal.dao.admin.PrivilegesDao;
import org.artisoft.dal.repository.utils.CommonMethods;
import org.artisoft.domain.CommonList;
import org.artisoft.domain.ModTask.TaskAssignUsers;
import org.artisoft.domain.ValueLabel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository("commonListDao")
public class CommonListRepository implements CommonListDao {

    @Autowired
    CommonMethods commonMethods;

    @Autowired
    private PrivilegesDao privilegesDao;

    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcCall insUpdProc;
    private SimpleJdbcCall delUpdProc;

    @Autowired
    public CommonListRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    public void setDataSource(HikariDataSource dataSource) {
        this.insUpdProc = new SimpleJdbcCall(dataSource);
        this.delUpdProc = new SimpleJdbcCall(dataSource);
    }


    @Override
    public List<ValueLabel<Long, String>> getTaskPriorityList() {
        return commonMethods.getValueLabels("SELECT PRIORITY_ID as VALUE, TITLE AS LABEL FROM TASK_PRIORITY ");
    }

    @Override
    public List<ValueLabel<Long, String>> getTaskStatusList() {
        return commonMethods.getValueLabels("SELECT TASK_STATUS_ID as VALUE, TITLE AS LABEL FROM TASK_STATUS ");
    }

    @Override
    public CommonList getCommonList(Long user_id) {
        try {
            final String sqlTaskPriority = "SELECT PRIORITY_ID as VALUE, TITLE AS LABEL FROM TASK_PRIORITY ";

            final String sqlTaskStatus = "SELECT TASK_STATUS_ID as VALUE, TITLE AS LABEL FROM TASK_STATUS ";

            final String sqlAssignTypes = "SELECT ASSIGN_TYPES_ID as VALUE, TITLE AS LABEL FROM ASSIGN_TYPES ";


            CommonList commonList = new CommonList();
            commonList.setTaskStatusList(commonMethods.getValueLabels(sqlTaskStatus, this.jdbcTemplate));
            commonList.setTaskPriorityList(commonMethods.getValueLabels(sqlTaskPriority, this.jdbcTemplate));
            commonList.setAssignTypesList(commonMethods.getValueLabels(sqlAssignTypes, this.jdbcTemplate));
            commonList.setTaskAssignUsersList(getTaskAssignUsers(user_id));
            commonList.setPrivs(privilegesDao.getPriviligiesList(user_id));
            return commonList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<TaskAssignUsers> getTaskAssignUsers(Long user_id) {
        try {
//           String sql = " SELECT US2.USER_TYPE_ID,UA.ASSIGNED_USER_ID as USER_ID,US2.USERNAME, US2.FULLNAME , US2.BRANCH_ID, US2.STATUS FROM\n" +
//                    " USER_ASSIGNMENTS AS UA  LEFT JOIN USERS AS US2 ON(UA.ASSIGNED_USER_ID=US2.USER_ID) where UA.STATUS=1";

            String sql = " SELECT US.USER_TYPE_ID,US.USER_ID, US.USERNAME, US.FULLNAME , US.BRANCH_ID, US.`STATUS` FROM USERS AS US " +
                    " WHERE exists (SELECT 1 FROM  USER_ASSIGNMENTS AS UA WHERE UA.ASSIGNED_USER_ID=US.USER_ID and UA.`STATUS`=1 AND  US.`STATUS`=1 ";


            if (privilegesDao.checkPrivStatus(user_id, 33) != 1) {
                sql=sql+" AND  UA.USER_ID=" + Long.toString(user_id);
            }
            sql=sql+" )";
                StringBuilder cond = new StringBuilder(sql);


                ResultSetExtractor<List<TaskAssignUsers>> rm = rs -> {
                    List<TaskAssignUsers> taskAssignUsersList = new ArrayList<>();
                    createTaskAssignUsersList(rs, taskAssignUsersList);
                    return taskAssignUsersList;
                };
                return jdbcTemplate.query(cond.toString(), rm);


            } catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }


    private void createTaskAssignUsersList(ResultSet rs, List<TaskAssignUsers> taskAssignUsersList) throws SQLException {
        while (rs.next()) {

            TaskAssignUsers taskAssignUsers = new TaskAssignUsers();
            taskAssignUsers.setUserId(rs.getLong("USER_ID"));
            taskAssignUsers.setBranchId(rs.getLong("BRANCH_ID"));
            taskAssignUsers.setFullname(rs.getString("FULLNAME"));
            taskAssignUsers.setUserName(rs.getString("USERNAME"));
            taskAssignUsers.setStatus(rs.getInt("STATUS"));
            taskAssignUsers.setUserTypeId(rs.getInt("USER_TYPE_ID"));
            taskAssignUsersList.add(taskAssignUsers);
        }
    }


}
