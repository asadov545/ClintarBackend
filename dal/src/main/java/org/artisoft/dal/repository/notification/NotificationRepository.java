package org.artisoft.dal.repository.notification;

import com.google.gson.Gson;
import com.zaxxer.hikari.HikariDataSource;
import org.artisoft.dal.dao.notification.NotificationDao;
import org.artisoft.domain.Notification.Notification;
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
@Repository("notificationDao")
public class NotificationRepository implements NotificationDao {

    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcCall insUpdProc;
    private SimpleJdbcCall delUpdProc;

    @Autowired
    public NotificationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    public void setDataSource(HikariDataSource dataSource) {
        this.insUpdProc = new SimpleJdbcCall(dataSource);
        this.delUpdProc = new SimpleJdbcCall(dataSource);
    }

    @Override
    public Notification getById(long id) {
        try {
            final String sql = " select NOTI.NOTIF_ID,NOTI.NOTIF_TYPE_ID,NT.TITLE AS NTTITLE,NOTI.TITLE,NOTI.CONTENT from NOTIFICATION AS NOTI \n" +
                    " LEFT JOIN NOTIFICATION_TYPE AS NT ON (NOTI.NOTIF_TYPE_ID=NT.NOTIF_TYPE_ID)  "
                    + " WHERE   NOTI.NOTIF_ID=?";
            return jdbcTemplate.query(sql, new Object[]{id}, rs -> {
                if (!rs.next())
                    return null;

                Notification notification = new Notification();
                notification.setNotifId(rs.getLong("NOTIF_ID"));
                notification.setNotifTypeId(rs.getLong("NOTIF_TYPE_ID"));
                notification.setTitle(rs.getString("TITLE"));
                notification.setContent(rs.getString("CONTENT"));
                return notification;
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public long insert(Notification notification) {
        try {
            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("jsonObj", new Gson().toJson(notification))
                    .addValue("newNotifId", 0);

            SimpleJdbcCall jdbcCall = insUpdProc.withProcedureName("NotificationInsUpd");
            jdbcCall.declareParameters(
                    new SqlParameter("jsonObj", Types.VARCHAR),
                    new SqlOutParameter("newNotifId", Types.INTEGER));

            Map<String, Object> resultMap = jdbcCall.execute(parameters);
            notification.setNotifId(Long.parseLong(resultMap.get("newNotifId").toString()));
            return 1;

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public boolean update(Notification notification) {
        return false;
    }

    @Override
    public boolean delete(long id) {
        return false;
    }
}
