package org.artisoft.dal.repository.ModTask;

import com.zaxxer.hikari.HikariDataSource;
import org.artisoft.dal.dao.ModTask.ReportDao;
import org.artisoft.dal.dao.admin.PrivilegesDao;
import org.artisoft.domain.ModTask.reports.TaskResultData;
import org.artisoft.domain.ModTask.reports.TaskSearchInfo;
import org.artisoft.domain.ModTask.reports.UserCustResultData;
import org.artisoft.domain.ModTask.reports.UserCustSearchİnfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository("reportDao")
public class ReportRepository implements ReportDao {

    private final JdbcTemplate jdbcTemplate;
    private final JdbcTemplate jdbcTemplateUser;
    private SimpleJdbcCall insUpdProc;
    private SimpleJdbcCall delUpdProc;

    @Autowired
    private PrivilegesDao privilegesDao;

    public ReportRepository(JdbcTemplate jdbcTemplate, JdbcTemplate jdbcTemplateUser) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcTemplateUser = jdbcTemplateUser;
    }

    @Autowired
    public void setDataSource(HikariDataSource dataSource) {
        this.insUpdProc = new SimpleJdbcCall(dataSource);
        this.delUpdProc = new SimpleJdbcCall(dataSource);
    }

    @Override
    public List<TaskResultData> filterTaskList(TaskSearchInfo taskSearchInfo) {
        try {
            final String sql = " SELECT T.TASK_ID,T.TASK_STATUS_ID,T.TITLE,T.CODE,IFNULL(UNIX_TIMESTAMP(T.BEGIN_DATE),0) as BEGIN_DATE," +
                    "IFNULL(UNIX_TIMESTAMP(T.DUE_DATE),0) as DUE_DATE,T.PRIORITY_ID " +
                    "FROM TASKS AS T LEFT JOIN TASK_PRIORITY AS TP ON(T.PRIORITY_ID=TP.PRIORITY_ID)" +
                    "LEFT JOIN TASK_STATUS AS TS ON (T.TASK_STATUS_ID=TS.TASK_STATUS_ID) ";

            StringBuilder cond = new StringBuilder(sql);
            List<String> paramList = new ArrayList<>();
            boolean isFirst = true;

            if (taskSearchInfo == null) {
                return null;
            }
            if (taskSearchInfo.getUserId() == 0) {
                return null;
            }
            // 33 ENTERPRISE ADMIN, 39 REPORT ADMIN
            if ((privilegesDao.checkPrivStatus(taskSearchInfo.getUserId(), 33) != 1) || (privilegesDao.checkPrivStatus(taskSearchInfo.getUserId(), 39) != 1)) {

                isFirst = false;


                cond.append("  where (T.CREATE_USER_ID=" + taskSearchInfo.getUserId() + " OR (EXISTS (SELECT 1 FROM TASK_ASSIGNEES AS TA WHERE (T.TASK_ID=TA.TASK_ID AND  TA.USER_ID=" + taskSearchInfo.getUserId() + "))))");
            }


            if (taskSearchInfo.getTaskStatusId() > 0) {
                cond.append(isFirst ? " where" : " AND");
                cond.append(" T.TASK_STATUS_ID = ? ");
                paramList.add(String.valueOf(taskSearchInfo.getTaskStatusId()));
                isFirst = false;
            }

            if (taskSearchInfo.getPriorityId() > 0) {
                cond.append(isFirst ? " where" : " AND");
                cond.append(" T.PRIORITY_ID=? ");
                paramList.add(String.valueOf(taskSearchInfo.getPriorityId()));
                isFirst = false;
            }

            if (taskSearchInfo.getTitle() != null && taskSearchInfo.getTitle().trim().length() > 0) {
                cond.append(isFirst ? " where" : " AND");
                cond.append(" T.TITLE like concat('%',?,'%') ");
                paramList.add(String.valueOf(taskSearchInfo.getTitle()));
                isFirst = false;
            }

            if (taskSearchInfo.getCode() != null && taskSearchInfo.getCode().trim().length() > 0) {
                cond.append(isFirst ? " where" : " AND");
                cond.append(" T.CODE like concat('%',?,'%') ");
                paramList.add(String.valueOf(taskSearchInfo.getCode()));
                isFirst = false;
            }

            if (taskSearchInfo.getAssigneeIds() != null & taskSearchInfo.getAssigneeIds().size() > 0) {
                String assignList = "";
                for (int i = 0; i < taskSearchInfo.getAssigneeIds().size(); i++) {
                    if (assignList.trim().length() > 0) assignList = assignList + ",";
                    assignList += taskSearchInfo.getAssigneeIds().get(i).toString();
                }

                cond.append(isFirst ? " where" : " AND");
                cond.append(" EXISTS (SELECT 1 FROM TASK_ASSIGNEES AS TA WHERE (T.TASK_ID=TA.TASK_ID AND TA.USER_ID IN (?)))");
                paramList.add(assignList);
                isFirst = false;
            }

            if (taskSearchInfo.getBeginDate() != null) {
                if (taskSearchInfo.getBeginDate().getFrom() > 0) {
                    cond.append(isFirst ? " where" : " AND");
                    cond.append(" IFNULL(UNIX_TIMESTAMP(T.BEGIN_DATE),0)>=? ");
                    paramList.add(String.valueOf(taskSearchInfo.getBeginDate().getFrom()));
                    isFirst = false;
                }

                if (taskSearchInfo.getBeginDate().getTo() > 0) {
                    cond.append(isFirst ? " where" : " AND");
                    cond.append(" IFNULL(UNIX_TIMESTAMP(T.BEGIN_DATE),0)<=? ");
                    paramList.add(String.valueOf(taskSearchInfo.getBeginDate().getTo()));
                    isFirst = false;
                }
            }

            if (taskSearchInfo.getDueDate() != null) {

                if (taskSearchInfo.getDueDate().getFrom() > 0) {
                    cond.append(isFirst ? " where" : " AND");
                    cond.append(" IFNULL(UNIX_TIMESTAMP(T.DUE_DATE),0)>=? ");
                    paramList.add(String.valueOf(taskSearchInfo.getDueDate().getFrom()));
                    isFirst = false;
                }


                if (taskSearchInfo.getDueDate().getTo() > 0) {
                    cond.append(isFirst ? " where" : " AND");
                    cond.append(" IFNULL(UNIX_TIMESTAMP(T.DUE_DATE),0)<=? ");
                    paramList.add(String.valueOf(taskSearchInfo.getDueDate().getTo()));
                    isFirst = false;
                }
            }

            ResultSetExtractor<List<TaskResultData>> rm = rs -> {
                List<TaskResultData> taskResultDataList = new ArrayList<>();
                createTaskList(rs, taskResultDataList);
                return taskResultDataList;
            };

            if (paramList.isEmpty()) {
                cond.append(" order by T.TASK_ID ");
                return jdbcTemplate.query(cond.toString(), rm);
            } else {
                cond.append(" order by T.TASK_ID ");
                return jdbcTemplate.query(cond.toString(), paramList.toArray(), rm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    private void createTaskList(ResultSet rs, List<TaskResultData> taskResultDataList) throws SQLException {
        while (rs.next()) {

            TaskResultData taskResultData = new TaskResultData();
            taskResultData.setTaskId(rs.getLong("TASK_ID"));
            taskResultData.setTaskStatusId(rs.getInt("TASK_STATUS_ID"));
            taskResultData.setCode(rs.getString("CODE"));
            taskResultData.setTitle(rs.getString("TITLE"));
            taskResultData.setBeginDate(rs.getLong("BEGIN_DATE"));
            taskResultData.setDueDate(rs.getLong("DUE_DATE"));
            taskResultData.setPriorityId(rs.getInt("PRIORITY_ID"));

            taskResultDataList.add(taskResultData);
        }
    }
    @Override
    public List<UserCustResultData> filterUserCustList(UserCustSearchİnfo userCustSearchİnfo) {
        try {
            final String sql = " SELECT  U.USER_ID,U.FULLNAME ,U.USERNAME, IFNULL(ADDR2.ADDRESS_LINE,ADDR.ADDRESS_LINE) AS ADDRESS_LINE,IFNULL(ADDR2.ZIP_CODE,ADDR.ZIP_CODE) AS ZIP_CODE, " +
                    " getContactNameById(U.USER_ID,1) AS MAIL , " +
                    " getContactNameById(U.USER_ID,2) AS MOB, BR.TITLE AS BRNAME,(select CO.TITLE FROM COUNTRY AS CO  WHERE CO.COUNTRY_ID=IFNULL(ADDR2.COUNTRY_ID,ADDR.COUNTRY_ID)) AS COUNTRYNAME FROM " +
                    " USERS AS U LEFT JOIN  CUSTOMERS AS CUS ON (U.CUSTOMER_ID=CUS.CUSTOMER_ID AND CUS.STATUS=1) " +
                    " LEFT JOIN ADDRESS AS ADDR ON (CUS.ADDRESS_ID=ADDR.ADDRESS_ID) " +
                    "          LEFT JOIN ADDRESS AS ADDR2 ON (U.ADDRESS_ID=ADDR2.ADDRESS_ID) " +
                    "          LEFT JOIN BRANCHES AS BR ON (BR.BRANCHES_ID=U.BRANCH_ID) " +
                    "          WHERE U.STATUS=1 AND exists (SELECT 1 FROM USER_MODULES AS UM WHERE UM.USER_ID=U.USER_ID AND UM.MOD_ID=1) ";

            StringBuilder cond = new StringBuilder(sql);
            List<String> paramList = new ArrayList<>();


            if (userCustSearchİnfo == null) {
                return null;
            }

            // 33 ENTERPRISE ADMIN, 39 REPORT ADMIN
//            if ((privilegesDao.checkPrivStatus(taskSearchInfo.getUserId(), 33) != 1) || (privilegesDao.checkPrivStatus(taskSearchInfo.getUserId(), 39) != 1)) {
//                isFirst = false;
//                cond.append("  where (T.CREATE_USER_ID=" + taskSearchInfo.getUserId() + " OR (EXISTS (SELECT 1 FROM TASK_ASSIGNEES AS TA WHERE (T.TASK_ID=TA.TASK_ID AND  TA.USER_ID=" + taskSearchInfo.getUserId() + "))))");
//            }


            if (userCustSearchİnfo.getFullname() != null && userCustSearchİnfo.getFullname().trim().length() > 0) {

                cond.append("  AND U.FULLNAME like concat('%',?,'%') ");
                paramList.add(String.valueOf(userCustSearchİnfo.getFullname()));

            }


            if (userCustSearchİnfo.getUsername() != null && userCustSearchİnfo.getUsername().trim().length() > 0) {

                cond.append(" AND   U.USERNAME like concat('%',?,'%') ");
                paramList.add(String.valueOf(userCustSearchİnfo.getUsername()));

            }
            if (userCustSearchİnfo.getEmail() != null && userCustSearchİnfo.getEmail().trim().length() > 0) {

                cond.append(" and exists (SELECT 1 FROM CONTACT AS CON WHERE CON.USER_ID=U.USER_ID AND CON.TEXT LIKE concat('%',?,'%'))   ");
                paramList.add(String.valueOf(userCustSearchİnfo.getEmail()));

            }

            ResultSetExtractor<List<UserCustResultData>> rm = rs -> {
                List<UserCustResultData> userCustResultDataList = new ArrayList<>();
                createCustResultList(rs, userCustResultDataList);
                return userCustResultDataList;
            };

            if (paramList.isEmpty()) {
                cond.append(" order by U.USER_ID ");
                return jdbcTemplateUser.query(cond.toString(), rm);
            } else {
                cond.append(" order by  U.USER_ID ");
                return jdbcTemplateUser.query(cond.toString(), paramList.toArray(), rm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private void createCustResultList(ResultSet rs, List<UserCustResultData> userCustResultDataList) throws SQLException {
        while (rs.next()) {
            UserCustResultData userCustResultData = new UserCustResultData();
            userCustResultData.setAddressLine(rs.getString("ADDRESS_LINE"));
            userCustResultData.setBranch(rs.getString("BRNAME"));
            userCustResultData.setCountry(rs.getString("COUNTRYNAME"));
            userCustResultData.setFullName(rs.getString("FULLNAME"));
            userCustResultData.setMail(rs.getString("MAIL"));
            userCustResultData.setMob(rs.getString("MOB"));
            userCustResultData.setZipCode(rs.getString("ZIP_CODE"));
            userCustResultDataList.add(userCustResultData);
        }
    }

}
