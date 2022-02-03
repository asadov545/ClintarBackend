package org.artisoft.dal.repository.ModToolbox;

import com.zaxxer.hikari.HikariDataSource;
import org.artisoft.dal.dao.ModTask.ContactDao;
import org.artisoft.dal.dao.ModToolbox.ToolboxUserDao;
import org.artisoft.dal.dao.admin.PrivilegesDao;
import org.artisoft.domain.Address;
import org.artisoft.domain.Users;
import org.artisoft.domain.ValueLabel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository("toolboxUserDao")
public class ToolBoxUserRepository implements ToolboxUserDao {

    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcCall insUpdProc;
    private SimpleJdbcCall delUpdProc;



    @Autowired
    private ContactDao contactDao;

    @Autowired
    private PrivilegesDao privilegesDao;

    @Autowired
    public ToolBoxUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    public void setDataSource(HikariDataSource dataSource) {
        this.insUpdProc = new SimpleJdbcCall(dataSource);
        this.delUpdProc = new SimpleJdbcCall(dataSource);

    }
    @Override
    public Users getById(long id) {
        try {
            final String sql = " SELECT US.USER_ID,US.USER_TYPE_ID, UT.TITLE AS UTTITLE ,US.USERNAME,US.PASSWORD, " +
                    " US.FULLNAME,US.BRANCH_ID,US.POSITION,US.CUSTOMER_ID,US.MAINPRJ_ID, " +
                    "(SELECT B.TITLE FROM BRANCHES AS B  WHERE B.BRANCHES_ID= US.BRANCH_ID) AS BTITLE," +
                    " (SELECT CS.NAME FROM CUSTOMERS AS CS WHERE CS.CUSTOMER_ID=US.CUSTOMER_ID) AS CUSTITLE, " +
                    " US.ADDRESS_ID,ADDR.COUNTRY_ID,ADDR.ADDRESS_LINE,ADDR.ZIP_CODE,CO.TITLE AS COTITLE " +
                    " FROM USERS AS US LEFT JOIN ADDRESS AS ADDR ON (US.ADDRESS_ID=ADDR.ADDRESS_ID) " +
                    " LEFT JOIN COUNTRY AS CO ON (ADDR.COUNTRY_ID=CO.COUNTRY_ID) " +
                    " LEFT JOIN USER_TYPES AS UT ON (UT.USER_TYPE_ID=US.USER_TYPE_ID) "
                    + " WHERE US.STATUS=1  AND US.USER_ID=? ";
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
                 users.setPosition(rs.getString("POSITION"));
                users.setCustomer(new ValueLabel<>(rs.getLong("CUSTOMER_ID"), rs.getString("CUSTITLE")));
               users.setMainprj(new ValueLabel<>(rs.getLong("MAINPRJ_ID"), ""));
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
        return 0;
    }

    @Override
    public boolean update(Users users) {
        return false;
    }

    @Override
    public boolean delete(long id) {
        return false;
    }

    @Override
    public List<Users> filterData(HashMap<String, String> map) {
        try {
            final String sql = " SELECT US.USER_ID,US.USER_TYPE_ID, UT.TITLE AS UTTITLE ,US.USERNAME,US.PASSWORD, " +
                    " US.FULLNAME,US.BRANCH_ID,(SELECT B.TITLE FROM BRANCHES AS B " +
                    " WHERE B.BRANCHES_ID= US.BRANCH_ID) AS BTITLE, " +
                    " US.ADDRESS_ID,ADDR.COUNTRY_ID,ADDR.ADDRESS_LINE,ADDR.ZIP_CODE,CO.TITLE AS COTITLE " +
                    " FROM USERS AS US LEFT JOIN ADDRESS AS ADDR ON (US.ADDRESS_ID=ADDR.ADDRESS_ID) " +
                    " LEFT JOIN COUNTRY AS CO ON (ADDR.COUNTRY_ID=CO.COUNTRY_ID) " +
                    " LEFT JOIN USER_TYPES AS UT ON (UT.USER_TYPE_ID=US.USER_TYPE_ID) WHERE US.STATUS=1 AND US.USER_TYPE_ID=1 "+
                    "   and EXISTS (SELECT 1 FROM USER_MODULES AS UM WHERE (US.USER_ID=UM.USER_ID AND UM.MOD_ID=2))";


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
    public List<Users> filterDataByUserId(HashMap<String, String> map, long userId) {
        try {
            Users us=new Users();
            us=getById(userId);



            String sql="";
            if (privilegesDao.checkPrivStatus(userId, 49) != 1) {

                 sql = " SELECT US.USER_ID,US.USER_TYPE_ID, UT.TITLE AS UTTITLE ,US.USERNAME,US.PASSWORD, " +
                        " US.FULLNAME,US.BRANCH_ID,(SELECT B.TITLE FROM BRANCHES AS B " +
                        " WHERE B.BRANCHES_ID= US.BRANCH_ID) AS BTITLE, " +
                        " US.ADDRESS_ID,ADDR.COUNTRY_ID,ADDR.ADDRESS_LINE,ADDR.ZIP_CODE,CO.TITLE AS COTITLE " +
                        " FROM USERS AS US LEFT JOIN ADDRESS AS ADDR ON (US.ADDRESS_ID=ADDR.ADDRESS_ID) " +
                        " LEFT JOIN COUNTRY AS CO ON (ADDR.COUNTRY_ID=CO.COUNTRY_ID) " +
                        " LEFT JOIN USER_TYPES AS UT ON (UT.USER_TYPE_ID=US.USER_TYPE_ID) WHERE US.STATUS=1 AND US.USER_TYPE_ID=1 " +
                        "   and EXISTS (SELECT 1 FROM USER_MODULES AS UM WHERE (US.USER_ID=UM.USER_ID AND UM.MOD_ID=2))";
            }else
            {
                sql = " SELECT US.USER_ID,US.USER_TYPE_ID, UT.TITLE AS UTTITLE ,US.USERNAME,US.PASSWORD, " +
                        " US.FULLNAME,US.BRANCH_ID,(SELECT B.TITLE FROM BRANCHES AS B " +
                        " WHERE B.BRANCHES_ID= US.BRANCH_ID) AS BTITLE, " +
                        " US.ADDRESS_ID,ADDR.COUNTRY_ID,ADDR.ADDRESS_LINE,ADDR.ZIP_CODE,CO.TITLE AS COTITLE " +
                        " FROM USERS AS US LEFT JOIN ADDRESS AS ADDR ON (US.ADDRESS_ID=ADDR.ADDRESS_ID) " +
                        " LEFT JOIN COUNTRY AS CO ON (ADDR.COUNTRY_ID=CO.COUNTRY_ID) " +
                        " LEFT JOIN USER_TYPES AS UT ON (UT.USER_TYPE_ID=US.USER_TYPE_ID) WHERE US.STATUS=1 AND US.MAINPRJ_ID="+ us.getMainprj().getValue()+"  and US.USER_TYPE_ID=1 " +
                        "   and EXISTS (SELECT 1 FROM USER_MODULES AS UM WHERE (US.USER_ID=UM.USER_ID AND UM.MOD_ID=2))";

            }

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
    public List<ValueLabel<Long, String>> getUserList() {

        final String sqluser = "SELECT US.USER_ID as value, US.FULLNAME as label FROM USERS AS US WHERE US.STATUS=1 AND US.USER_TYPE_ID=1 \n" +
                "and EXISTS (SELECT 1 FROM USER_MODULES AS UM WHERE (US.USER_ID=UM.USER_ID AND UM.MOD_ID=2))";

            return jdbcTemplate.query(sqluser, rs -> {
                List<ValueLabel<Long,String>> resultList = new ArrayList<>();

                while (rs.next()) {
                    ValueLabel<Long,String> usr = new ValueLabel<Long,String>();
                    usr.setValue(rs.getLong("value"));
                    usr.setLabel(rs.getString("label"));
                    resultList.add(usr);
                }

                return resultList;
            });

    }


}
