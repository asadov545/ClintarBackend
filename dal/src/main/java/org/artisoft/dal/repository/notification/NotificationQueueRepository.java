package org.artisoft.dal.repository.notification;

import com.google.gson.Gson;
import com.zaxxer.hikari.HikariDataSource;
import org.artisoft.dal.dao.notification.NotificationQueueDao;
import org.artisoft.domain.Notification.MobileNotifcationList;
import org.artisoft.domain.Notification.NotificationQueue;
import org.artisoft.domain.Notification.SendNotificationByMailList;
import org.artisoft.domain.ValueLabel;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("notificationQueueDao")
public class NotificationQueueRepository implements NotificationQueueDao {

    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcCall insUpdProc;
    private SimpleJdbcCall updDeviceProc;
    private SimpleJdbcCall delUpdProc;

    @Autowired
    public NotificationQueueRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    public void setDataSource(HikariDataSource dataSource) {
        this.insUpdProc = new SimpleJdbcCall(dataSource);
        this.delUpdProc = new SimpleJdbcCall(dataSource);
        this.updDeviceProc = new SimpleJdbcCall(dataSource);
    }

    @Override
    public NotificationQueue getById(long id) {
        return null;
    }

    @Override
    public long insert(NotificationQueue notificationQueue) {
        try {
            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("jsonObj", new Gson().toJson(notificationQueue))
                    .addValue("newnotifQueId", 0);

            SimpleJdbcCall jdbcCall = insUpdProc.withProcedureName("NotificationQueueInsUpd");
            jdbcCall.declareParameters(
                    new SqlParameter("jsonObj", Types.VARCHAR),
                    new SqlOutParameter("newnotifQueId", Types.INTEGER));

            Map<String, Object> resultMap = jdbcCall.execute(parameters);
            notificationQueue.setNotifQueId(Long.parseLong(resultMap.get("newnotifQueId").toString()));
            return Long.parseLong(resultMap.get("newnotifQueId").toString());

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public boolean update(NotificationQueue notificationQueue) {
        return false;
    }

    @Override
    public boolean delete(long id) {

        try {
            SqlParameterSource parameters = new MapSqlParameterSource().addValue("notifQueId", id);
            SimpleJdbcCall jdbcCall = delUpdProc.withProcedureName("NotificationQueueDel");
            jdbcCall.declareParameters(new SqlParameter("notifQueId", Types.INTEGER));
            jdbcCall.execute(parameters);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public List<NotificationQueue> filterData(HashMap<String, String> map) {
        return null;
    }

    @Override
    public List<NotificationQueue> getAll() {
        return null;
    }

    @Override
    public List<SendNotificationByMailList> getQueueForMailList() {
        try {
            final String sql = " select NQ.NOTIF_QUE_ID,NQ.NOTIF_ID,NQ.TO_USER_ID,NN.TITLE,NN.CONTENT," +
                    "(SELECT  TRIM(CT.TEXT) FROM CONTACT AS CT WHERE CT.USER_ID=NQ.TO_USER_ID AND CT.CONTACT_TYPE_ID=1 LIMIT 1) as EM" +
                    " from NOTIFICATION_QUEUE AS NQ LEFT JOIN NOTIFICATION AS NN  ON  (NN.NOTIF_ID=NQ.NOTIF_ID) " +
                    "LEFT JOIN USERS AS UR ON (UR.USER_ID=NQ.TO_USER_ID) WHERE NQ.`TYPE`=2 AND NQ.STATUS=0  ";

            StringBuilder cond = new StringBuilder(sql);
            ResultSetExtractor<List<SendNotificationByMailList>> rm = rs -> {
                List<SendNotificationByMailList> sendNotificationByMailLists = new ArrayList<>();
                createSendNotificationByMailList(rs, sendNotificationByMailLists);
                return sendNotificationByMailLists;
            };

            cond.append(" order by  NQ.NOTIF_QUE_ID ASC limit 10000");
            return jdbcTemplate.query(cond.toString(), rm);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void NotifQueStatusUpdate(long notifQueId,int sendStatus) {

        try {
            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("notifQueId", notifQueId)
                    .addValue("sendStatus", sendStatus);
            SimpleJdbcCall jdbcCall = delUpdProc.withProcedureName("NotifQueStatusUpd");
            jdbcCall.declareParameters(
                    new SqlParameter("notifQueId", Types.INTEGER),
                    new SqlParameter("sendStatus", Types.INTEGER));
            jdbcCall.execute(parameters);
        } catch (Exception e) {
            e.printStackTrace();

        }


    }

    @Override
    public List<MobileNotifcationList> getQueueForMobileList(Long user_id) {

        try {
            final String sql = " select NQ.NOTIF_QUE_ID, " +
                    "NN.NOTIF_TYPE_ID, " +
                    "NT.TITLE AS NTTITLE, " +
                    "NN.TITLE," +
                    "NN.CONTENT,NN.TASK_ID, " +
                    " IFNULL(UNIX_TIMESTAMP(NQ.CREATE_DATE),0) as CREATE_DATE, UR.DEVICE_ID " +
                    "from NOTIFICATION_QUEUE AS NQ LEFT JOIN NOTIFICATION AS NN  ON  (NN.NOTIF_ID=NQ.NOTIF_ID) " +
                    "LEFT JOIN USERS AS UR ON (UR.USER_ID=NQ.TO_USER_ID)  " +
                    "LEFT JOIN NOTIFICATION_TYPE AS NT ON (NN.NOTIF_TYPE_ID=NT.NOTIF_TYPE_ID) " +
                    "WHERE NQ.`TYPE`=3 AND NQ.STATUS=1 AND NQ.TO_USER_ID= "+ user_id.toString();

            StringBuilder cond = new StringBuilder(sql);
            ResultSetExtractor<List<MobileNotifcationList>> rm = rs -> {
                List<MobileNotifcationList> mobileNotifcationLists = new ArrayList<>();
                createMobileNotifcationList(rs, mobileNotifcationLists);
                return mobileNotifcationLists;
            };

            cond.append(" order by  NQ.NOTIF_QUE_ID ASC limit 10000");
            return jdbcTemplate.query(cond.toString(), rm);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;


    }

    @Override
    public List<MobileNotifcationList> getAllActiveMobileList() {

        try {
            final String sql = " select NQ.NOTIF_QUE_ID, " +
                    "NN.NOTIF_TYPE_ID, " +
                    "NT.TITLE AS NTTITLE, " +
                    "NN.TITLE," +
                    "NN.CONTENT,NN.TASK_ID, " +
                    " IFNULL(UNIX_TIMESTAMP(NQ.CREATE_DATE),0) as CREATE_DATE, UR.DEVICE_ID " +
                    "from NOTIFICATION_QUEUE AS NQ LEFT JOIN NOTIFICATION AS NN  ON  (NN.NOTIF_ID=NQ.NOTIF_ID) " +
                    "LEFT JOIN USERS AS UR ON (UR.USER_ID=NQ.TO_USER_ID)  " +
                    "LEFT JOIN NOTIFICATION_TYPE AS NT ON (NN.NOTIF_TYPE_ID=NT.NOTIF_TYPE_ID) " +
                    "WHERE NQ.`TYPE`=3 AND NQ.STATUS=0" ;

            StringBuilder cond = new StringBuilder(sql);
            ResultSetExtractor<List<MobileNotifcationList>> rm = rs -> {
                List<MobileNotifcationList> mobileNotifcationLists = new ArrayList<>();
                createMobileNotifcationList(rs, mobileNotifcationLists);
                return mobileNotifcationLists;
            };

            cond.append(" order by  NQ.NOTIF_QUE_ID ASC limit 10000");
            return jdbcTemplate.query(cond.toString(), rm);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean updateDeviceId(long userId, String deviceId) {
        try {
            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("userId",userId )
                    .addValue("deviceId",deviceId )
                    .addValue("response", 0);
            SimpleJdbcCall jdbcCall = updDeviceProc.withProcedureName("UpdDeviceId");
            jdbcCall.declareParameters(
                    new SqlParameter("userId", Types.INTEGER),
                    new SqlParameter("deviceId", Types.VARCHAR),
                    new SqlOutParameter("response", Types.INTEGER));
            Map<String, Object> resultMap = jdbcCall.execute(parameters);
            return  Long.parseLong(resultMap.get("response").toString())>0 ;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void createMobileNotifcationList(ResultSet rs, List<MobileNotifcationList> mobileNotifcationLists) throws SQLException {
        while (rs.next()) {

            MobileNotifcationList mobileNotifcationList = new MobileNotifcationList();
            mobileNotifcationList.setNotifQueId(rs.getLong("NOTIF_QUE_ID"));
            mobileNotifcationList.setNotifType(new ValueLabel<>(rs.getLong("NOTIF_TYPE_ID"), rs.getString("NTTITLE")));
              mobileNotifcationList.setTitle(rs.getString("TITLE"));
            mobileNotifcationList.setDeviceId(rs.getString("DEVICE_ID"));
            mobileNotifcationList.setContent(rs.getString("CONTENT"));
            mobileNotifcationList.setTaskId(rs.getLong("TASK_ID"));
            mobileNotifcationList.setTime(rs.getLong("CREATE_DATE"));

            mobileNotifcationLists.add(mobileNotifcationList);
        }
    }


    private void createSendNotificationByMailList(ResultSet rs, List<SendNotificationByMailList> sendNotificationByMailLists) throws SQLException {
        while (rs.next()) {

            SendNotificationByMailList sendNotificationByMailList = new SendNotificationByMailList();
            sendNotificationByMailList.setEmail(rs.getString("EM"));
            sendNotificationByMailList.setTitle(rs.getString("TITLE"));
            sendNotificationByMailList.setContent(rs.getString("CONTENT"));
            sendNotificationByMailList.setNotifQueId(rs.getLong("NOTIF_QUE_ID"));
            if(sendNotificationByMailList.getEmail() != null && !sendNotificationByMailList.getEmail().isEmpty())
         //   if((sendNotificationByMailList.getEmail().trim()!=null)&& (sendNotificationByMailList.getEmail().trim()!=""))
            {
                sendNotificationByMailLists.add(sendNotificationByMailList);
            }
        }
    }

}
