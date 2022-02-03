package org.artisoft.dal.repository.ModTask;

import com.google.gson.Gson;
import com.zaxxer.hikari.HikariDataSource;
import org.artisoft.dal.dao.ModTask.ContactDao;
import org.artisoft.dal.dao.ModTask.CustomerDao;
import org.artisoft.dal.dao.UserDao;
import org.artisoft.domain.Address;
import org.artisoft.domain.ModTask.Customer;
import org.artisoft.domain.Users;
import org.artisoft.domain.ValueLabel;
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

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("customerDao")
public class CustomerRepository implements CustomerDao {

    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcCall insUpdProc;
    private SimpleJdbcCall delUpdProc;

    @Autowired
    private ContactDao contactDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    public CustomerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Autowired
    public void setDataSource(HikariDataSource dataSource) {
        this.insUpdProc = new SimpleJdbcCall(dataSource);
        this.delUpdProc = new SimpleJdbcCall(dataSource);
    }


    @Override
    public Customer getById(long id) {
        try {
            final String sql = "SELECT    CUS.CUSTOMER_ID,CUS.NAME,CUS.ADDRESS_ID, ADDR.COUNTRY_ID,ADDR.ADDRESS_LINE,ADDR.ZIP_CODE ,CO.TITLE AS COTITLE, " +
                    "US.USER_ID,US.USERNAME,US.PASSWORD,US.FULLNAME   FROM CUSTOMERS AS CUS  LEFT JOIN ADDRESS AS ADDR ON (CUS.ADDRESS_ID=ADDR.ADDRESS_ID) " +
                    " LEFT JOIN COUNTRY AS CO ON (ADDR.COUNTRY_ID=CO.COUNTRY_ID) INNER JOIN USERS AS US ON(US.CUSTOMER_ID=CUS.CUSTOMER_ID AND  US.USER_TYPE_ID=2 AND US.CUSTOMER_ID is not null)  "
                    + " WHERE  CUS.STATUS=1 AND  CUS.CUSTOMER_ID=?";
            return jdbcTemplate.query(sql, new Object[]{id}, rs -> {
                if (!rs.next())
                    return null;

                Customer customer = new Customer();
                customer.setCustomerId(rs.getLong("CUSTOMER_ID"));
                customer.setName(rs.getString("NAME"));


                Users users = new Users();
                users.setUserId(rs.getLong("USER_ID"));
                users.setUsername(rs.getString("USERNAME"));
               //  users.setPassword(rs.getString("PASSWORD"));
                users.setPassword("******");
                users.setFullname(rs.getString("FULLNAME"));
                users.setEmail(contactDao.getContactByUserId(rs.getLong("USER_ID"), 1));
                users.setMobile(contactDao.getContactByUserId(rs.getLong("USER_ID"), 2));
                customer.setUser(users);


                Address address = new Address();
                address.setAddressId(rs.getLong("ADDRESS_ID"));
                address.setCountry(new ValueLabel<>(rs.getLong("COUNTRY_ID"), rs.getString("COTITLE")));
                address.setAddressLine(rs.getString("ADDRESS_LINE"));
                address.setZipCode(rs.getString("ZIP_CODE"));
                customer.setAddress(address);

                return customer;
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public long insert(Customer customer) {
//        try {
//            SqlParameterSource parameters = new MapSqlParameterSource()
//                    .addValue("jsonObj", new Gson().toJson(customer))
//                    .addValue("newcustomerId", 0);
//
//            SimpleJdbcCall jdbcCall = insUpdProc.withProcedureName("CustomerInsUpd");
//            jdbcCall.declareParameters(
//                    new SqlParameter("jsonObj", Types.VARCHAR),
//                    new SqlOutParameter("newcustomerId", Types.INTEGER));
//
//            Map<String, Object> resultMap = jdbcCall.execute(parameters);
//            customer.setCustomerId(Long.parseLong(resultMap.get("newcustomerId").toString()));
//            return 1;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw e;
//        }
        return -1;
    }

    @Override
    public boolean update(Customer customer) {
//        return insert(customer) > 0;
        return false;
    }

    @Override
    public boolean delete(long id) {
        try {
            SqlParameterSource parameters = new MapSqlParameterSource().addValue("customerId", id);
            SimpleJdbcCall jdbcCall = delUpdProc.withProcedureName("CustomerDel");
            jdbcCall.declareParameters(new SqlParameter("customerId", Types.INTEGER));
            jdbcCall.execute(parameters);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public List<Customer> filterData(HashMap<String, String> map) {
        try {
            final String sql = "SELECT    CUS.CUSTOMER_ID,CUS.NAME,CUS.ADDRESS_ID, ADDR.COUNTRY_ID,ADDR.ADDRESS_LINE,ADDR.ZIP_CODE ,CO.TITLE AS COTITLE, " +
                    "US.USER_ID,US.USERNAME,US.PASSWORD,US.FULLNAME     FROM CUSTOMERS AS CUS  LEFT JOIN ADDRESS AS ADDR ON (CUS.ADDRESS_ID=ADDR.ADDRESS_ID) " +
                    " LEFT JOIN COUNTRY AS CO ON (ADDR.COUNTRY_ID=CO.COUNTRY_ID) INNER JOIN USERS AS US ON(US.CUSTOMER_ID=CUS.CUSTOMER_ID AND US.USER_TYPE_ID=2 AND US.CUSTOMER_ID is not null)  WHERE CUS.STATUS=1 ";
            StringBuilder cond = new StringBuilder(sql);

            ResultSetExtractor<List<Customer>> rm = rs -> {
                List<Customer> customerList = new ArrayList<>();
                createCustomerList(rs, customerList);
                return customerList;
            };

            cond.append(" order by CUS.CUSTOMER_ID desc ");
            return jdbcTemplate.query(cond.toString(), rm);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void createCustomerList(ResultSet rs, List<Customer> customerList) throws SQLException {
        while (rs.next()) {

            Customer customer = new Customer();
            customer.setCustomerId(rs.getLong("CUSTOMER_ID"));
            customer.setName(rs.getString("NAME"));

            Users users = new Users();
            users.setUserId(rs.getLong("USER_ID"));
            users.setUsername(rs.getString("USERNAME"));
           // users.setPassword(rs.getString("PASSWORD"));
            users.setPassword("");
            users.setFullname(rs.getString("FULLNAME"));
            users.setEmail(contactDao.getContactByUserId(rs.getLong("USER_ID"), 1));
            users.setMobile(contactDao.getContactByUserId(rs.getLong("USER_ID"), 2));
            customer.setUser(users);

            Address address = new Address();
            address.setAddressId(rs.getLong("ADDRESS_ID"));
            address.setCountry(new ValueLabel<>(rs.getLong("COUNTRY_ID"), rs.getString("COTITLE")));
            address.setAddressLine(rs.getString("ADDRESS_LINE"));
            address.setZipCode(rs.getString("ZIP_CODE"));
            customer.setAddress(address);
            customerList.add(customer);
        }
    }


    @Override
    public List<Customer> getAll() {
        return null;
    }

    @Override
    public long insertNew(Customer customer) throws AddressException, MessagingException, IOException {
        try {

            Users users1=new Users();
            users1=customer.getUser();
            String password=users1.getPassword();
            users1.setPassword(BCrypt.hashpw(users1.getPassword(), BCrypt.gensalt()));
           // users1.setModId(1);
            customer.setUser(users1);
            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("jsonObj", new Gson().toJson(customer))
                    .addValue("newcustomerId", 0);

            SimpleJdbcCall jdbcCall = insUpdProc.withProcedureName("CustomerInsUpd");
            jdbcCall.declareParameters(
                    new SqlParameter("jsonObj", Types.VARCHAR),
                    new SqlOutParameter("newcustomerId", Types.INTEGER));

            Map<String, Object> resultMap = jdbcCall.execute(parameters);
            customer.setCustomerId(Long.parseLong(resultMap.get("newcustomerId").toString()));
            Users users = new Users();
            users = getById(customer.getCustomerId()).getUser();
            users.setPassword(password);
            userDao.addNewUserToNotificationQueue(users,2,2);
            userDao.addNewUserToNotificationQueue(users,3,2);
            return 1;

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public boolean updateNew(Customer customer) throws AddressException, MessagingException, IOException {
        return insertNew(customer) > 0;
    }

    @Override
    public List<Customer> filterDataByModId(HashMap<String, String> map, Long modId) {
        try {
            final String sql = "SELECT    CUS.CUSTOMER_ID,CUS.NAME,CUS.ADDRESS_ID, ADDR.COUNTRY_ID,ADDR.ADDRESS_LINE,ADDR.ZIP_CODE ,CO.TITLE AS COTITLE, " +
                    "US.USER_ID,US.USERNAME,US.PASSWORD,US.FULLNAME     FROM CUSTOMERS AS CUS  LEFT JOIN ADDRESS AS ADDR ON (CUS.ADDRESS_ID=ADDR.ADDRESS_ID) " +
                    " LEFT JOIN COUNTRY AS CO ON (ADDR.COUNTRY_ID=CO.COUNTRY_ID) INNER JOIN USERS AS US ON(US.CUSTOMER_ID=CUS.CUSTOMER_ID AND US.USER_TYPE_ID=2 AND US.CUSTOMER_ID is not null) and  "+
            " EXISTS (SELECT 1 FROM USER_MODULES AS UM WHERE CUS.STATUS=1 AND (US.USER_ID=UM.USER_ID AND UM.MOD_ID="+modId+"))";
            StringBuilder cond = new StringBuilder(sql);

            ResultSetExtractor<List<Customer>> rm = rs -> {
                List<Customer> customerList = new ArrayList<>();
                createCustomerList(rs, customerList);
                return customerList;
            };

            cond.append(" order by CUS.CUSTOMER_ID desc ");
            return jdbcTemplate.query(cond.toString(), rm);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<ValueLabel<Long, String>> filterCustomerList(Long modId) {
        try {
            final String sql = "SELECT    CUS.CUSTOMER_ID,CUS.NAME  FROM CUSTOMERS AS CUS  " +
                    " INNER JOIN USERS AS US ON(US.CUSTOMER_ID=CUS.CUSTOMER_ID AND US.USER_TYPE_ID=2 AND US.CUSTOMER_ID is not null) and  "+
                    " EXISTS (SELECT 1 FROM USER_MODULES AS UM WHERE CUS.STATUS=1 AND (US.USER_ID=UM.USER_ID AND UM.MOD_ID="+modId+
                    " )) ";
            return jdbcTemplate.query(sql, rs -> {
                List<ValueLabel<Long, String>> list = new ArrayList<>();
                while (rs.next())
                    list.add(new ValueLabel<>(rs.getLong("CUSTOMER_ID"), rs.getString("NAME")));

                return list;
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<ValueLabel<Long, String>> filterCustomerListByLabel(String label, Long modId) {
        try {
            final String sql = " SELECT    CUS.CUSTOMER_ID,CUS.NAME  FROM CUSTOMERS AS CUS " +
                    " INNER JOIN USERS AS US ON(US.CUSTOMER_ID=CUS.CUSTOMER_ID AND US.USER_TYPE_ID=2  AND US.CUSTOMER_ID is not null) and  "+
                    " EXISTS (SELECT 1 FROM USER_MODULES AS UM WHERE CUS.STATUS=1 AND (US.USER_ID=UM.USER_ID AND UM.MOD_ID="+modId+
                    " )) and CUS.NAME like ?";
            return jdbcTemplate.query(sql, new Object[]{"%".concat(label).concat("%")}, rs -> {
                List<ValueLabel<Long, String>> list = new ArrayList<>();
                while (rs.next())
                    list.add(new ValueLabel<>(rs.getLong("CUSTOMER_ID"), rs.getString("NAME")));


                return list;
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }
}
