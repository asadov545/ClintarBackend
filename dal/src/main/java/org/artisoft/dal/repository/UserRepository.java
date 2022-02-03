package org.artisoft.dal.repository;

import com.google.gson.Gson;
import com.zaxxer.hikari.HikariDataSource;
import org.artisoft.dal.dao.ModTask.ContactDao;
import org.artisoft.dal.dao.UserDao;
import org.artisoft.dal.dao.admin.PrivilegesDao;
import org.artisoft.dal.dao.notification.NotificationQueueDao;
import org.artisoft.dal.repository.utils.CommonDataHelper;
import org.artisoft.dal.repository.utils.CommonMethods;
import org.artisoft.domain.*;
import org.artisoft.domain.Address;
import org.artisoft.domain.Auth.LoginStatusInfo;
import org.artisoft.domain.Auth.UserAuthInfo;
import org.artisoft.domain.ModToolbox.UserCompdata;
import org.artisoft.domain.Notification.JobsCompdata;
import org.artisoft.domain.Notification.Notification;
import org.artisoft.domain.Notification.NotificationQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Repository;

import javax.mail.*;
import javax.mail.internet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

@Repository("userDao")
public class UserRepository implements UserDao {

    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcCall insUpdProc;
    private SimpleJdbcCall delUpdProc;
    private SimpleJdbcCall saveLoginInfo;
    private SimpleJdbcCall updPassProc;

    @Autowired
    private ContactDao contactDao;

    @Autowired
    private NotificationQueueDao notificationQueueDao;

    @Autowired
    private PrivilegesDao privilegesDao;

    @Autowired
    HttpServletRequest request;

    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    public void setDataSource(HikariDataSource dataSource) {
        this.insUpdProc = new SimpleJdbcCall(dataSource);
        this.delUpdProc = new SimpleJdbcCall(dataSource);
        this.saveLoginInfo = new SimpleJdbcCall(dataSource);
        this.updPassProc = new SimpleJdbcCall(dataSource);
    }

    public User getUserSecurityInfoByUserName(String userName) {
        try {
            User userInfo = (User) request.getAttribute("userInfo");

            if (userInfo != null) {
                return userInfo;
            }
            final String sql = "SELECT GetUserDetailsByUserName(?) AS jsonObj";
            return jdbcTemplate.query(sql, new Object[]{userName}, rs -> {
                if (!rs.next())
                    return null;
                return new Gson().fromJson(rs.getString("jsonObj"), User.class);
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public UserAuthInfo getUserSessionInfoById(long userId) {
        try {
            final String sql = "SELECT GetUserSessionInfoById(?) AS jsonObj";
            return jdbcTemplate.query(sql, new Object[]{userId}, rs -> {
                if (!rs.next())
                    return null;
                return new Gson().fromJson(rs.getString("jsonObj"), UserAuthInfo.class);
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public Users getById(long id) {
        try {
            final String sql = " SELECT US.USER_ID,US.USER_TYPE_ID, UT.TITLE AS UTTITLE ,US.USERNAME,US.PASSWORD,\n" +
                    "US.FULLNAME,US.BRANCH_ID,(SELECT B.TITLE FROM BRANCHES AS B\n" +
                    "WHERE B.BRANCHES_ID= US.BRANCH_ID) AS BTITLE,\n" +
                    "US.ADDRESS_ID,ADDR.COUNTRY_ID,ADDR.ADDRESS_LINE,ADDR.ZIP_CODE,CO.TITLE AS COTITLE\n" +
                    "FROM USERS AS US LEFT JOIN ADDRESS AS ADDR ON (US.ADDRESS_ID=ADDR.ADDRESS_ID)\n" +
                    "LEFT JOIN COUNTRY AS CO ON (ADDR.COUNTRY_ID=CO.COUNTRY_ID)\n" +
                    "LEFT JOIN USER_TYPES AS UT ON (UT.USER_TYPE_ID=US.USER_TYPE_ID) "
                    + " WHERE US.STATUS=1  and US.USER_ID=?";
            return jdbcTemplate.query(sql, new Object[]{id}, rs -> {
                if (!rs.next())
                    return null;

                Users users = new Users();
                users.setUserId(rs.getLong("USER_ID"));
                users.setUserType(new ValueLabel<>(rs.getLong("USER_TYPE_ID"), rs.getString("UTTITLE")));
                users.setUsername(rs.getString("USERNAME"));
              //  users.setPassword(rs.getString("PASSWORD"));
                users.setPassword("******");
                users.setFullname(rs.getString("FULLNAME"));
                users.setBranches(new ValueLabel<>(rs.getLong("BRANCH_ID"), rs.getString("BTITLE")));
                users.setEmail(contactDao.getContactByUserId(rs.getLong("USER_ID"), 1));
                users.setMobile(contactDao.getContactByUserId(rs.getLong("USER_ID"), 2));
                Address address = new Address();
                address.setAddressId(rs.getLong("ADDRESS_ID"));
                address.setCountry(new ValueLabel<>(rs.getLong("COUNTRY_ID"), rs.getString("COTITLE")));
                address.setAddressLine(rs.getString("ADDRESS_LINE"));
                address.setZipCode(rs.getString("ZIP_CODE"));
                users.setAddress(address);
                return users;
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public long insert(Users users) {
        try {
            String pas=users.getPassword();
            users.setPassword(BCrypt.hashpw(users.getPassword(), BCrypt.gensalt()));
            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("jsonObj", new Gson().toJson(users))
                    .addValue("newusersId", 0);

            SimpleJdbcCall jdbcCall = insUpdProc.withProcedureName("UsersInsUpd");
            jdbcCall.declareParameters(
                    new SqlParameter("jsonObj", Types.VARCHAR),
                    new SqlOutParameter("newusersId", Types.INTEGER));

            Map<String, Object> resultMap = jdbcCall.execute(parameters);
            users.setUserId(Long.parseLong(resultMap.get("newusersId").toString()));

            users.setPassword(pas);
            addNewUserToNotificationQueue(users,2,1);
            addNewUserToNotificationQueue(users,3,1);
            //  sendMailToUsers(users);
            return 1;

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }


    }

    public void SaveLoginInfo(LoginStatusInfo loginInfo) {
        try {
//            SqlParameterSource parameters = new MapSqlParameterSource()
//                    .addValue("pLoginInfo", new Gson().toJson(loginInfo))
//                    .addValue("pResStr", "")
//                    .addValue("pRes", 0);
//
//            SimpleJdbcCall saveUserLoginInfo = saveLoginInfo.withProcedureName("SaveUserLoginInfo");
//            saveUserLoginInfo.declareParameters(
//                    new SqlParameter("pLoginInfo", Types.VARCHAR),
//                    new SqlOutParameter("pResStr", Types.VARCHAR),
//                    new SqlOutParameter("pRes", Types.INTEGER));
//
//            Map<String, Object> resultMap = saveUserLoginInfo.execute(parameters);
//            String resultStr = resultMap.get("pResStr").toString();
//            int res = Integer.parseInt(resultMap.get("pRes").toString());
            return;

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public boolean update(Users users) {

        return insert(users) > 0;

    }

    @Override
    public boolean delete(long id) {
        try {
            SqlParameterSource parameters = new MapSqlParameterSource().addValue("userId", id);
            SimpleJdbcCall jdbcCall = delUpdProc.withProcedureName("UsersDel");
            jdbcCall.declareParameters(new SqlParameter("userId", Types.INTEGER));
            jdbcCall.execute(parameters);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public List<Users> filterData(HashMap<String, String> map) {
        try {




            final String sql = " SELECT US.USER_ID,US.USER_TYPE_ID, UT.TITLE AS UTTITLE ,US.USERNAME,US.PASSWORD,\n" +
                    "US.FULLNAME,US.BRANCH_ID,(SELECT B.TITLE FROM BRANCHES AS B\n" +
                    "WHERE B.BRANCHES_ID= US.BRANCH_ID) AS BTITLE,\n" +
                    "US.ADDRESS_ID,ADDR.COUNTRY_ID,ADDR.ADDRESS_LINE,ADDR.ZIP_CODE,CO.TITLE AS COTITLE\n" +
                    "FROM USERS AS US LEFT JOIN ADDRESS AS ADDR ON (US.ADDRESS_ID=ADDR.ADDRESS_ID)\n" +
                    "LEFT JOIN COUNTRY AS CO ON (ADDR.COUNTRY_ID=CO.COUNTRY_ID)\n" +
                    "LEFT JOIN USER_TYPES AS UT ON (UT.USER_TYPE_ID=US.USER_TYPE_ID) WHERE US.STATUS=1 AND US.USER_TYPE_ID=1 ";

            StringBuilder cond = new StringBuilder(sql);


            ResultSetExtractor<List<Users>> rm = rs -> {
                List<Users> usersList = new ArrayList<>();
                createUsersList(rs, usersList);
                return usersList;
            };

            cond.append(" order by US.USER_ID desc ");
            return jdbcTemplate.query(cond.toString(), rm);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private void createUsersList(ResultSet rs, List<Users> usersList) throws SQLException {
        while (rs.next()) {

            Users users = new Users();
            users.setUserId(rs.getLong("USER_ID"));
            users.setUserType(new ValueLabel<>(rs.getLong("USER_TYPE_ID"), rs.getString("UTTITLE")));
            users.setUsername(rs.getString("USERNAME"));
           // users.setPassword(rs.getString("PASSWORD"));
            users.setPassword("");
            users.setFullname(rs.getString("FULLNAME"));
            users.setBranches(new ValueLabel<>(rs.getLong("BRANCH_ID"), rs.getString("BTITLE")));
            users.setEmail(contactDao.getContactByUserId(rs.getLong("USER_ID"), 1));
            users.setMobile(contactDao.getContactByUserId(rs.getLong("USER_ID"), 2));
            Address address = new Address();
            address.setAddressId(rs.getLong("ADDRESS_ID"));
            address.setCountry(new ValueLabel<>(rs.getLong("COUNTRY_ID"), rs.getString("COTITLE")));
            address.setAddressLine(rs.getString("ADDRESS_LINE"));
            address.setZipCode(rs.getString("ZIP_CODE"));
            users.setAddress(address);
            usersList.add(users);

        }
    }

    @Override
    public List<Users> getAll() {
        return null;
    }

    @Override
    public List<ValueLabel<Long, String>> filterUserList(String label, Long modId) {
        try {
            final String sql = "SELECT US.USER_ID, US.FULLNAME FROM USERS AS US where  US.STATUS=1 AND " +
                    " EXISTS (SELECT 1 FROM USER_MODULES AS UM WHERE (US.USER_ID=UM.USER_ID AND UM.MOD_ID="+modId+
                    " )) AND US.FULLNAME like ?";
            return jdbcTemplate.query(sql, new Object[]{"%".concat(label).concat("%")}, rs -> {
                List<ValueLabel<Long, String>> list = new ArrayList<>();
                while (rs.next())
                    list.add(new ValueLabel<>(rs.getLong("USER_ID"), rs.getString("FULLNAME")));

                return list;
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    @Override
    public void sendMailNewUsers(Users users) throws AddressException, MessagingException, IOException {
        if (users.getEmail().trim().length() > 0) {

            Message msg = new MimeMessage(CommonMethods.GetSession());
            msg.setFrom(new InternetAddress("asadov.tech@gmail.com", false));

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(users.getEmail()));
            msg.setSubject("Registration completed");
            // msg.setContent("you have new task content1 ", "text/html");
            msg.setSentDate(new Date());


            String messageContex = " <html>\n" +
                    "<head>\n" +
                    "\t<title></title>\n" +
                    "\t<link href=\"https://svc.webspellchecker.net/spellcheck31/lf/scayt3/ckscayt/css/wsc.css\" rel=\"stylesheet\" type=\"text/css\" />\n" +
                    "</head>\n" +
                    "<body aria-readonly=\"false\"><span style=\"background-color:rgb(255, 255, 255); color:rgb(34, 34, 34); font-family:arial,helvetica,sans-serif; font-size:small\">Hi " + users.getFullname() + ",&nbsp;</span><br />\n" +
                    "<br />\n" +
                    "<span style=\"background-color:rgb(255, 255, 255); color:rgb(34, 34, 34); font-family:arial,helvetica,sans-serif; font-size:small\">Congratulations,&nbsp;</span><strong>your Task Manager Systems account has been created successfully</strong><span style=\"background-color:rgb(255, 255, 255); color:rgb(34, 34, 34); font-family:arial,helvetica,sans-serif; font-size:small\">&nbsp;and we are pleased to welcome you to our community.&nbsp;</span><br />\n" +
                    "<span style=\"background-color:rgb(255, 255, 255); color:rgb(34, 34, 34); font-family:arial,helvetica,sans-serif; font-size:small\">We recommend you keep this e-mail to store your credentials.&nbsp;</span><br />\n" +
                    "<br />\n" +
                    "<span style=\"background-color:rgb(255, 255, 255); color:rgb(34, 34, 34); font-family:arial,helvetica,sans-serif; font-size:small\">Your credentials:</span>\n" +
                    "<ul>\n" +
                    "\t<li><strong>Username:</strong>&nbsp;" + users.getUsername() + "</li>\n" +
                    "\t<li><strong>Password:</strong>&nbsp;" + users.getPassword() + "</a></li>\n" +
                    "</ul>\n" +
                    "<br />\n" +
                    "<br />\n" +
                    "<span style=\"background-color:rgb(255, 255, 255); color:rgb(34, 34, 34); font-family:arial,helvetica,sans-serif; font-size:small\">You can now sign in to Task Manager Systems with&nbsp;</span><strong>your username and password</strong><span style=\"background-color:rgb(255, 255, 255); color:rgb(34, 34, 34); font-family:arial,helvetica,sans-serif; font-size:small\">.&nbsp;</span><br />\n" +
                    "<br />\n" +
                    "<span style=\"background-color:rgb(255, 255, 255); color:rgb(34, 34, 34); font-family:arial,helvetica,sans-serif; font-size:small\">Thank you for your trust in our solutions,&nbsp;</span><br />\n" +
                    "<span style=\"background-color:rgb(255, 255, 255); color:rgb(34, 34, 34); font-family:arial,helvetica,sans-serif; font-size:small\">ArtiSoft Team</span></body>\n" +
                    "</html>\n ";

            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(messageContex, "text/html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            MimeBodyPart attachPart = new MimeBodyPart();

            // attachPart.attachFile("/var/tmp/image19.png");
            //    multipart.addBodyPart(attachPart);
            msg.setContent(multipart);
            Transport.send(msg);
        }
    }

    @Override
    public void sendMailTaskAssignees(Long userId) throws AddressException, MessagingException, IOException {
        Users assignUsers = new Users();
        assignUsers = getById(userId);
        if (assignUsers.getEmail().trim().length() > 0) {

            Message msg = new MimeMessage(CommonMethods.GetSession());
            msg.setFrom(new InternetAddress("asadov.tech@gmail.com", false));

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(assignUsers.getEmail()));
            msg.setSubject("You assigned new task ");
            // msg.setContent("you have new task content1 ", "text/html");
            msg.setSentDate(new Date());


            String messageContex = " You welcome new task";

            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(messageContex, "text/html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            MimeBodyPart attachPart = new MimeBodyPart();

            // attachPart.attachFile("/var/tmp/image19.png");
            //    multipart.addBodyPart(attachPart);
            msg.setContent(multipart);
            Transport.send(msg);
        }

    }

    @Override
    public long insertNew(Users users) throws AddressException, MessagingException, IOException {
//        try {
//            users.setPassword(BCrypt.hashpw(users.getPassword(), BCrypt.gensalt()));
//            SqlParameterSource parameters = new MapSqlParameterSource()
//                    .addValue("jsonObj", new Gson().toJson(users))
//                    .addValue("newusersId", 0);
//
//            SimpleJdbcCall jdbcCall = insUpdProc.withProcedureName("UsersInsUpd");
//            jdbcCall.declareParameters(
//                    new SqlParameter("jsonObj", Types.VARCHAR),
//                    new SqlOutParameter("newusersId", Types.INTEGER));
//
//            Map<String, Object> resultMap = jdbcCall.execute(parameters);
//            users.setUserId(Long.parseLong(resultMap.get("newusersId").toString()));
//
//            sendMailNewUsers(users);
//            return 1;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw e;
//        }
        return -1;
    }

    @Override
    public boolean updateNew(Users users) throws AddressException, MessagingException, IOException {
//        return insertNew(users) > 0;
        return false;
    }

    @Override
    public String createNewUserNotificationContext(Users users) {
        String messageContex =
//                " <html>\n" +
//                "<head>\n" +
//                "\t<title></title>\n" +
//                "\t<link href=\"https://svc.webspellchecker.net/spellcheck31/lf/scayt3/ckscayt/css/wsc.css\" rel=\"stylesheet\" type=\"text/css\" />\n" +
//                "</head>\n" +
//                "<body aria-readonly=\"false\"><span style=\"background-color:rgb(255, 255, 255); color:rgb(34, 34, 34); font-family:arial,helvetica,sans-serif; font-size:small\">Hi " + users.getFullname() + ",&nbsp;</span><br />\n" +
//                "<br />\n" +
//                "<span style=\"background-color:rgb(255, 255, 255); color:rgb(34, 34, 34); font-family:arial,helvetica,sans-serif; font-size:small\">Congratulations,&nbsp;</span><strong>your Task Manager Systems account has been created successfully</strong><span style=\"background-color:rgb(255, 255, 255); color:rgb(34, 34, 34); font-family:arial,helvetica,sans-serif; font-size:small\">&nbsp;and we are pleased to welcome you to our community.&nbsp;</span><br />\n" +
//                "<span style=\"background-color:rgb(255, 255, 255); color:rgb(34, 34, 34); font-family:arial,helvetica,sans-serif; font-size:small\">We recommend you keep this e-mail to store your credentials.&nbsp;</span><br />\n" +
//                "<br />\n" +
//                "<span style=\"background-color:rgb(255, 255, 255); color:rgb(34, 34, 34); font-family:arial,helvetica,sans-serif; font-size:small\">Your credentials:</span>\n" +
              "<ul>\n" +
                "\t<li><strong>Username:</strong>&nbsp;" + users.getUsername() + "</li>\n" +
                "\t<li><strong>Password:</strong>&nbsp;" + users.getPassword() + "</a></li>\n"+
               "</ul>\n" +
             "<br />\n" +
                "<br /> https://app.clintar.com/ "
//                "<span style=\"background-color:rgb(255, 255, 255); color:rgb(34, 34, 34); font-family:arial,helvetica,sans-serif; font-size:small\">You can now sign in to Task Manager Systems with&nbsp;</span><strong>your username and password</strong><span style=\"background-color:rgb(255, 255, 255); color:rgb(34, 34, 34); font-family:arial,helvetica,sans-serif; font-size:small\">.&nbsp;</span><br />\n" +
//                "<br />\n" +
//                "<span style=\"background-color:rgb(255, 255, 255); color:rgb(34, 34, 34); font-family:arial,helvetica,sans-serif; font-size:small\">Thank you for your trust in our solutions,&nbsp;</span><br />\n" +
//                "<span style=\"background-color:rgb(255, 255, 255); color:rgb(34, 34, 34); font-family:arial,helvetica,sans-serif; font-size:small\">ArtiSoft Team</span></body>\n" +
//                "</html>\n "
                ;
        return messageContex;
    }

    @Override
    public void addNewUserToNotificationQueue(Users users,int sendtype,int notifType) {
        try {
            Notification notification = new Notification();
            notification.setNotifTypeId(0);
            if(sendtype==3) {
                notification.setContent(createNewUserNotificationContextforMobile(users));}

                else{
                notification.setContent(createNewUserNotificationContext(users));
            }
           // notification.setContent("CONTEX");
            notification.setNotifTypeId(notifType);//user yoxsa customer
            notification.setTitle("Registration completed");
            NotificationQueue notificationQueue = new NotificationQueue();
            notificationQueue.setNotification(notification);
            notificationQueue.setToUserId(users.getUserId());
            notificationQueue.setToUserType(1);
            notificationQueue.setStatus(0);
            notificationQueue.setType(sendtype); //mail ,push,sms

            notificationQueueDao.insert(notificationQueue);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<Users> filterDataByModId(HashMap<String, String> map, Long modId) {
        try {
            final String sql = " SELECT US.USER_ID,US.USER_TYPE_ID, UT.TITLE AS UTTITLE ,US.USERNAME,US.PASSWORD, " +
                    " US.FULLNAME,US.BRANCH_ID,(SELECT B.TITLE FROM BRANCHES AS B " +
                    " WHERE B.BRANCHES_ID= US.BRANCH_ID) AS BTITLE, " +
                    " US.ADDRESS_ID,ADDR.COUNTRY_ID,ADDR.ADDRESS_LINE,ADDR.ZIP_CODE,CO.TITLE AS COTITLE " +
                    " FROM USERS AS US LEFT JOIN ADDRESS AS ADDR ON (US.ADDRESS_ID=ADDR.ADDRESS_ID) " +
                    " LEFT JOIN COUNTRY AS CO ON (ADDR.COUNTRY_ID=CO.COUNTRY_ID) " +
                    " LEFT JOIN USER_TYPES AS UT ON (UT.USER_TYPE_ID=US.USER_TYPE_ID) WHERE US.STATUS=1 AND US.USER_TYPE_ID=1 "+
                    "   and EXISTS (SELECT 1 FROM USER_MODULES AS UM WHERE (US.USER_ID=UM.USER_ID AND UM.MOD_ID="+modId+"))";

            StringBuilder cond = new StringBuilder(sql);


            ResultSetExtractor<List<Users>> rm = rs -> {
                List<Users> usersList = new ArrayList<>();
                createUsersList(rs, usersList);
                return usersList;
            };

            cond.append(" order by US.USER_ID desc ");
            return jdbcTemplate.query(cond.toString(), rm);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Users> getUserListByCustomerId(long customerId) {
        try {
            final String sql = " SELECT US.USER_ID,US.USER_TYPE_ID, UT.TITLE AS UTTITLE ,US.USERNAME,US.PASSWORD, " +
                    " US.FULLNAME,US.BRANCH_ID,(SELECT B.TITLE FROM BRANCHES AS B " +
                    " WHERE B.BRANCHES_ID= US.BRANCH_ID) AS BTITLE, " +
                    " US.ADDRESS_ID,ADDR.COUNTRY_ID,ADDR.ADDRESS_LINE,ADDR.ZIP_CODE,CO.TITLE AS COTITLE,US.CUSTOMER_ID " +
                    " FROM USERS AS US LEFT JOIN ADDRESS AS ADDR ON (US.ADDRESS_ID=ADDR.ADDRESS_ID) " +
                    " LEFT JOIN COUNTRY AS CO ON (ADDR.COUNTRY_ID=CO.COUNTRY_ID) " +
                    " LEFT JOIN USER_TYPES AS UT ON (UT.USER_TYPE_ID=US.USER_TYPE_ID) WHERE US.STATUS=1 AND US.USER_TYPE_ID=1 "+
                    "   and EXISTS (SELECT 1 FROM USER_MODULES AS UM WHERE (US.USER_ID=UM.USER_ID AND UM.MOD_ID=2)) and US.CUSTOMER_ID="+customerId;

            StringBuilder cond = new StringBuilder(sql);


            ResultSetExtractor<List<Users>> rm = rs -> {
                List<Users> usersList = new ArrayList<>();
                createUsersList(rs, usersList);
                return usersList;
            };

            cond.append(" order by US.USER_ID desc ");
            return jdbcTemplate.query(cond.toString(), rm);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Boolean changePasword(ChangePassword changePassword) {
        try {
            changePassword.setPswNew(BCrypt.hashpw(changePassword.getPswNew(), BCrypt.gensalt()));
            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("jsonObj", new Gson().toJson(changePassword))
                    .addValue("response", 0);
            SimpleJdbcCall jdbcCall = updPassProc.withProcedureName("UpdPassword");
            jdbcCall.declareParameters(
                    new SqlParameter("jsonObj", Types.VARCHAR),
                    new SqlOutParameter("response", Types.INTEGER));
            Map<String, Object> resultMap = jdbcCall.execute(parameters);
            return  Long.parseLong(resultMap.get("response").toString())>0 ;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public UserCompdata getUsersCompData() {


        final String sqlmainprj = " SELECT TP.MAIN_PRJ_ID as value, TP.NAME as label FROM TB_MAIN_PRJ TP where TP.STATUS=1  ";

      //  final String sqlhours = " SELECT HOURS_ID as value,TITLE as label FROM HOURS;";

        UserCompdata userCompdata = new UserCompdata();

        userCompdata.setMainProjectList(CommonDataHelper.getValueLabels(sqlmainprj, this.jdbcTemplate));
     //   jobsCompdata.setHourList(CommonDataHelper.getValueLabels(sqlhours, this.jdbcTemplate));

        return userCompdata;
    }


    private String createNewUserNotificationContextforMobile(Users users) {
        String messageContex ="Username :"+users.getUsername()+","+
                       "Password: "+users.getPassword() +
                        " ; Site link :" +"https://app.clintar.com/ " ;
        return messageContex;
    }
}