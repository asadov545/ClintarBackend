package org.artisoft.dal.repository.ModTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.artisoft.dal.dao.ModTask.TaskAssigneesDao;
import org.artisoft.dal.dao.ModTask.TaskDao;
import org.artisoft.dal.dao.UserDao;
import org.artisoft.dal.dao.admin.PrivilegesDao;
import org.artisoft.dal.dao.notification.NotificationQueueDao;
import org.artisoft.domain.ModTask.tasks.*;
import org.artisoft.domain.Notification.Notification;
import org.artisoft.domain.Notification.NotificationQueue;
import org.artisoft.domain.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("taskDao")
public class TaskRepository implements TaskDao {
    private static final Logger logger = LogManager.getLogger(TaskRepository.class);

    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcCall insUpdProc;
    private SimpleJdbcCall insUpdProcTaskInfo;
    private SimpleJdbcCall insUpdProcchangeTask;
    private SimpleJdbcCall delUpdProc;
    private SimpleJdbcCall updMarkup;
    @Autowired
    private TaskAssigneesDao taskAssigneesDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private NotificationQueueDao notificationQueueDao;
    @Autowired
    private PrivilegesDao privilegesDao;

    @Autowired
    public TaskRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Autowired
    public void setDataSource(HikariDataSource dataSource) {
        this.insUpdProc = new SimpleJdbcCall(dataSource);
        this.insUpdProcTaskInfo = new SimpleJdbcCall(dataSource);
        this.insUpdProcchangeTask = new SimpleJdbcCall(dataSource);
        this.delUpdProc = new SimpleJdbcCall(dataSource);
        this.updMarkup = new SimpleJdbcCall(dataSource);

    }

    @Override
    public Task getById(long id) {
        try {
            final String sql = " SELECT T.TASK_ID,T.TASK_STATUS_ID,TS.TITLE AS TSTITLE,T.TITLE,T.CODE,T.DESCRIPTION,T.LOC_ID,T.MARKUP, " +
                    "IFNULL(UNIX_TIMESTAMP(T.BEGIN_DATE),0) * 1000 as BEGIN_DATE,IFNULL(UNIX_TIMESTAMP(T.DUE_DATE),0) * 1000 as DUE_DATE,T.PRIORITY_ID,T.CREATE_USER_ID, TP.TITLE AS TPTITLE,IFNULL(UNIX_TIMESTAMP(T.CREATE_DATE),0) as CREATE_DATE ," +
                    " TS.COLOR, TS.ICON1,TS.ICON2 " +
                    " FROM TASKS AS T LEFT JOIN TASK_PRIORITY AS TP ON(T.PRIORITY_ID=TP.PRIORITY_ID)" +
                    " LEFT JOIN TASK_STATUS AS TS ON (T.TASK_STATUS_ID=TS.TASK_STATUS_ID) "
                    + " WHERE  T.TASK_ID=?";
            return jdbcTemplate.query(sql, new Object[]{id}, rs -> {
                if (!rs.next())
                    return null;

                Task task = new Task();
                task.setTaskId(rs.getLong("TASK_ID"));
                task.setTaskStatusId(rs.getInt("TASK_STATUS_ID"));
                task.setTitle(rs.getString("TITLE"));
                task.setCode(rs.getString("CODE"));
                task.setDescription(rs.getString("DESCRIPTION"));
                task.setBeginDate(rs.getLong("BEGIN_DATE"));
                task.setDueDate(rs.getLong("DUE_DATE"));
                task.setPriorityId(rs.getInt("PRIORITY_ID"));
                task.setCreateUserId(rs.getLong("CREATE_USER_ID"));
                task.setCreateDate(rs.getLong("CREATE_DATE"));
                task.setLocId(rs.getLong("LOC_ID"));
                task.setLocationMarkup(rs.getString("MARKUP"));
                task.setTaskAssigneesList(taskAssigneesDao.getAllTaskAssigneesByTaskId(rs.getLong("TASK_ID")));
                task.setTaskPhotos(getAllTaskPhotosByTaskId(rs.getLong("TASK_ID")));
                task.setColor(rs.getString("COLOR"));
                task.setIcon1(rs.getString("ICON1"));
                task.setIcon2(rs.getString("ICON2"));
                return task;
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public long insert(Task task) {
        try {
            Long taskId = task.getTaskId();
            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("jsonObj", new Gson().toJson(task))
                    .addValue("newtaskId", 0);

            SimpleJdbcCall jdbcCall = insUpdProc.withProcedureName("TaskInsUpd");
            jdbcCall.declareParameters(
                    new SqlParameter("jsonObj", Types.VARCHAR),
                    new SqlOutParameter("newtaskId", Types.INTEGER));

            Map<String, Object> resultMap = jdbcCall.execute(parameters);
            Object resultObject = resultMap.get("newtaskId");
            task.setTaskId(Long.parseLong(resultObject.toString()));

            if (taskId == 0 & task.getTaskAssigneesList() != null & task.getTaskAssigneesList().size() > 0) {

                TaskAssignees taskAssignees = new TaskAssignees();

                for (int i = 0; i < task.getTaskAssigneesList().size(); i++) {

                    taskAssignees = task.getTaskAssigneesList().get(i);
                    //bax bura
                     addAssigneesUserToNotificationQueue(taskAssignees.getUserId(),2,task.getTaskId());
                     addAssigneesUserToNotificationQueue(taskAssignees.getUserId(),3,task.getTaskId());

                }
            }

            return 1;

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    @Override
    public boolean update(Task task) {

        return insert(task) > 0;
        //  return false;
    }

    @Override
    public boolean delete(long id) {

        try {
            SqlParameterSource parameters = new MapSqlParameterSource().addValue("taskId", id);
            SimpleJdbcCall jdbcCall = delUpdProc.withProcedureName("TaskDel");
            jdbcCall.declareParameters(new SqlParameter("taskId", Types.INTEGER));
            jdbcCall.execute(parameters);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public List<Task> filterData2(HashMap<String, String> map, long user_id) {
        try {
//            final String sql = " SELECT T.TASK_ID,T.TASK_STATUS_ID,TS.TITLE AS TSTITLE,T.TITLE,T.CODE,T.DESCRIPTION,T.LOCATION_ID," +
//                    "IFNULL(UNIX_TIMESTAMP(T.BEGIN_DATE),0) as BEGIN_DATE,IFNULL(UNIX_TIMESTAMP(T.DUE_DATE),0) as DUE_DATE,T.PRIORITY_ID, T.CREATE_USER_ID,TP.TITLE AS TPTITLE,IFNULL(UNIX_TIMESTAMP(T.CREATE_DATE),0) as CREATE_DATE" +
//                    " FROM TASKS AS T LEFT JOIN TASK_PRIORITY AS TP ON(T.PRIORITY_ID=TP.PRIORITY_ID)" +
//                    " LEFT JOIN TASK_STATUS AS TS ON (T.TASK_STATUS_ID=TS.TASK_STATUS_ID) ";

            final String sql = " SELECT T.TASK_ID,T.TASK_STATUS_ID,TS.TITLE AS TSTITLE,T.TITLE,T.CODE,T.DESCRIPTION,T.LOC_ID, " +
                    "IFNULL(UNIX_TIMESTAMP(T.BEGIN_DATE),0) as BEGIN_DATE,IFNULL(UNIX_TIMESTAMP(T.DUE_DATE),0) as DUE_DATE,T.PRIORITY_ID, T.CREATE_USER_ID,TP.TITLE AS TPTITLE,IFNULL(UNIX_TIMESTAMP(T.CREATE_DATE),0) as CREATE_DATE," +
                    "TS.COLOR, TS.ICON1,TS.ICON2 " +
                    " FROM TASKS AS T LEFT JOIN TASK_PRIORITY AS TP ON(T.PRIORITY_ID=TP.PRIORITY_ID)" +
                    " LEFT JOIN TASK_STATUS AS TS ON (T.TASK_STATUS_ID=TS.TASK_STATUS_ID) ";

            if (user_id == 0) {
                    return  null;
            }


            StringBuilder cond = new StringBuilder(sql);
            List<String> paramList = new ArrayList<>();
            boolean isFirst = true;


            if (privilegesDao.checkPrivStatus(user_id, 33) != 1) {
                 isFirst = false;
                // cond.append("  where (T.CREATE_USER_ID="+ user_id+" OR (EXISTS (SELECT 1 FROM TASK_ASSIGNEES AS TA WHERE (T.TASK_ID=TA.TASK_ID AND  TA.USER_ID="+user_id+"))))");
                cond.append("  where (T.CREATE_USER_ID=" + Long.toString(user_id) + " OR (EXISTS (SELECT 1 FROM TASK_ASSIGNEES AS TA WHERE (T.TASK_ID=TA.TASK_ID AND  TA.USER_ID=" + Long.toString(user_id) + "))))");

            }

            if (map.containsKey("title") && !map.get("title").trim().equals("")) {
                if (isFirst)
                    cond.append(" where  T.TITLE like ?");
                else
                    cond.append(" and    T.TITLE like ?");
                paramList.add(map.get("title") + "%");
                isFirst = false;
            }

            if (map.containsKey("taskPriorityId") && !map.get("taskPriorityId").trim().equals("") && Integer.parseInt(map.get("taskPriorityId").trim()) > 0) {
                if (isFirst)
                    cond.append(" where  T.PRIORITY_ID=? ");
                else
                    cond.append(" and T.PRIORITY_ID=? ");
                paramList.add(map.get("taskPriorityId"));
                isFirst = false;
            }

            if (map.containsKey("taskStatusId") && !map.get("taskStatusId").trim().equals("") && Integer.parseInt(map.get("taskStatusId").trim()) > 0) {
                if (isFirst)
                    cond.append(" where  T.TASK_STATUS_ID=? ");
                else
                    cond.append(" and T.TASK_STATUS_ID=? ");
                paramList.add(map.get("taskStatusId"));
                isFirst = false;
            }


            ResultSetExtractor<List<Task>> rm = rs -> {
                List<Task> taskList = new ArrayList<>();
                createTaskList(rs, taskList);
                return taskList;
            };

            if (paramList.isEmpty()) {
                cond.append(" order by T.TASK_ID desc ");
                return jdbcTemplate.query(cond.toString(), rm);
            } else {
                cond.append(" order by T.TASK_ID desc ");
                return jdbcTemplate.query(cond.toString(), paramList.toArray(), rm);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private void createTaskList(ResultSet rs, List<Task> taskList) throws SQLException {
        while (rs.next()) {

            Task task = new Task();
            task.setTaskId(rs.getLong("TASK_ID"));
            task.setTaskStatusId(rs.getInt("TASK_STATUS_ID"));
            task.setTitle(rs.getString("TITLE"));
            task.setCode(rs.getString("CODE"));
            task.setDescription(rs.getString("DESCRIPTION"));
            task.setBeginDate(rs.getLong("BEGIN_DATE"));
            task.setDueDate(rs.getLong("DUE_DATE"));
            task.setPriorityId(rs.getInt("PRIORITY_ID"));
            task.setCreateUserId(rs.getLong("CREATE_USER_ID"));
            task.setCreateDate(rs.getLong("CREATE_DATE"));
            task.setLocId(rs.getLong("LOC_ID"));
            task.setColor(rs.getString("COLOR"));
            task.setIcon1(rs.getString("ICON1"));
            task.setIcon2(rs.getString("ICON2"));

            //   task.setTaskAssigneesList(taskAssigneesDao.getAllTaskAssigneesByTaskId(rs.getLong("TASK_ID")));
            //   task.setTaskPhotos(getAllTaskPhotosByTaskId(rs.getLong("TASK_ID")));
            taskList.add(task);
        }
    }

    @Override
    public List<TaskPhoto> getAllTaskPhotosByTaskId(long taskId) {
        try {


            final String sql = " SELECT TP.TP_ID,TP.UPFID,UF.ORIGINALFILENAME," +
                    "UF.RELATIVE_PATH,UF.FILENAME," +
                    "0 AS OP FROM TASK_PHOTOS AS TP  LEFT JOIN UPLOADED_FILES AS UF ON (TP.UPFID=UF.UPFID)  "
                    + " WHERE  TP.TASK_ID=" + taskId;
            StringBuilder cond = new StringBuilder(sql);
            List<String> paramList = new ArrayList<>();


            ResultSetExtractor<List<TaskPhoto>> rm = rs -> {
                List<TaskPhoto> taskPhotos = new ArrayList<>();
                try {
                    createTaskAssigneesList(rs, taskPhotos);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                return taskPhotos;
            };

            if (paramList.isEmpty()) {
                cond.append(" order by TP.TP_ID desc ");
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
    public long insertNew(Task task) throws AddressException, MessagingException, IOException {
        try {
//            Long taskId = task.getTaskId();
//            SqlParameterSource parameters = new MapSqlParameterSource()
//                    .addValue("jsonObj", new Gson().toJson(task))
//                    .addValue("newtaskId", 0);
//
//            SimpleJdbcCall jdbcCall = insUpdProc.withProcedureName("TaskInsUpd");
//            jdbcCall.declareParameters(
//                    new SqlParameter("jsonObj", Types.VARCHAR),
//                    new SqlOutParameter("newtaskId", Types.INTEGER));
//
//            Map<String, Object> resultMap = jdbcCall.execute(parameters);
//            Object resultObject = resultMap.get("newtaskId");
//            task.setTaskId(Long.parseLong(resultObject.toString()));
//
//
//            if (taskId == 0 & task.getTaskAssigneesList() != null & task.getTaskAssigneesList().size() > 0) {
//
//                TaskAssignees taskAssignees = new TaskAssignees();
//
//                for (int i = 0; i < task.getTaskAssigneesList().size(); i++) {
//
//                    taskAssignees = task.getTaskAssigneesList().get(i);
//                    userDao.sendMailTaskAssignees(taskAssignees.getUserId());
//
//
//                }
//
//            }
            return -1;

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public boolean updateNew(Task task) throws AddressException, MessagingException, IOException {
        return insertNew(task) > 0;
    }

    @Override
    public String changeTaskStatus(TaskStatus taskStatus) {
        try {
            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("jsonObj", new Gson().toJson(taskStatus))
                    .addValue("str", 0);

            SimpleJdbcCall jdbcCall = insUpdProcchangeTask.withProcedureName("ChangeTaskStatus");
            jdbcCall.declareParameters(
                    new SqlParameter("jsonObj", Types.VARCHAR),
                    new SqlOutParameter("str", Types.VARCHAR));

            Map<String, Object> resultMap = jdbcCall.execute(parameters);
            // taskStatus.setUserAssignmentId(Long.parseLong(resultMap.get("newuserAssignmentId").toString()));
            return resultMap.get("str").toString();

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public StatusInfo getTaskInfo(Long taskId, Long userId) {
        try {
            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("task_id", taskId)
                    .addValue("user_id", userId)
                    .addValue("jsonTaskInfo", 0);

            SimpleJdbcCall jdbcCall = insUpdProcTaskInfo.withProcedureName("GetTaskInfo");
            jdbcCall.declareParameters(
                    new SqlParameter("task_id", Types.INTEGER),
                    new SqlParameter("user_id", Types.INTEGER),
                    new SqlOutParameter("jsonTaskInfo", Types.VARCHAR));

            Map<String, Object> resultMap = jdbcCall.execute(parameters);

            Type listType = new TypeToken<StatusInfo>() {
            }.getType();
            return new Gson().fromJson(resultMap.get("jsonTaskInfo").toString(), listType);


        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Autowired
    HttpServletRequest servletRequest;

    public String GetUrlBase() throws MalformedURLException {
        URL requestURL = new URL(servletRequest.getRequestURL().toString());
        String port = requestURL.getPort() == -1 ? "" : ":" + requestURL.getPort();
        return requestURL.getProtocol() + "://" + requestURL.getHost() + port;
    }

    private void createTaskAssigneesList(ResultSet rs, List<TaskPhoto> taskPhotos) throws SQLException, MalformedURLException {
        while (rs.next()) {
            TaskPhoto taskPhoto = new TaskPhoto();
            taskPhoto.setTpId(rs.getLong("TP_ID"));
            taskPhoto.setFilenameOriginal(rs.getString("ORIGINALFILENAME"));
            taskPhoto.setUpfid(rs.getLong("UPFID"));


            Path fullpathFile = new File(rs.getString("RELATIVE_PATH")).toPath().resolve(rs.getString("FILENAME"));

            logger.warn("fullpathFile = " + fullpathFile.toString());
            String fullpath = GetUrlBase() + fullpathFile.toString().replace("\\", "/");
            logger.warn("fullpath = " + fullpath.toString());

            //requestURL.getProtocol() + "://" + requestURL.getHost() + port
            taskPhoto.setFullPath(fullpath);
            taskPhoto.setOp(rs.getInt("OP"));
            //  taskPhoto.setUploadKey(rs.getString("uploadKey"));
            taskPhotos.add(taskPhoto);
        }
    }

    public void addAssigneesUserToNotificationQueue(Long userId, int sendtype, long taskId) {
        try {
            Notification notification = new Notification();
            notification.setNotifTypeId(0);
            //  notification.setContent(createNewUserNotificationContext(users));
            notification.setContent("You welcome new task");
            notification.setNotifTypeId(3);
            notification.setTitle("You assigned new task");
            notification.setTaskId(taskId);
            NotificationQueue notificationQueue = new NotificationQueue();
            notificationQueue.setNotification(notification);
            notificationQueue.setToUserId(userId);
            notificationQueue.setToUserType(1);
            notificationQueue.setStatus(0);

            notificationQueue.setType(sendtype);

            notificationQueueDao.insert(notificationQueue);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    @Override
    public String updateMarkup(TaskMarkup taskMarkup) {
        try {
            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("jsonObj", new Gson().toJson(taskMarkup))
                    .addValue("str", 0);
            SimpleJdbcCall jdbcCall = updMarkup.withProcedureName("UpdMarkup");
            jdbcCall.declareParameters(
                    new SqlParameter("jsonObj", Types.VARCHAR),
                    new SqlOutParameter("str", Types.VARCHAR));
            Map<String, Object> resultMap = jdbcCall.execute(parameters);
            return resultMap.get("str").toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
