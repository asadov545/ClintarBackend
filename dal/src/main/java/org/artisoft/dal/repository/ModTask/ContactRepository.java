package org.artisoft.dal.repository.ModTask;

import com.zaxxer.hikari.HikariDataSource;
import org.artisoft.dal.dao.ModTask.ContactDao;
import org.artisoft.domain.ModTask.Contact;
import org.artisoft.domain.ValueLabel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository("contactDao")
public class ContactRepository implements ContactDao {

    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcCall insUpdProc;
    private SimpleJdbcCall delUpdProc;

    @Autowired
    public ContactRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    public void setDataSource(HikariDataSource dataSource) {
        this.insUpdProc = new SimpleJdbcCall(dataSource);
        this.delUpdProc = new SimpleJdbcCall(dataSource);
    }

    @Override
    public Contact getById(long id) {
        return null;
    }

    @Override
    public long insert(Contact contact) {
        return 0;
    }

    @Override
    public boolean update(Contact contact) {
        return false;
    }

    @Override
    public boolean delete(long id) {
        return false;
    }

    @Override
    public List<Contact> filterData(HashMap<String, String> map) {
        return null;
    }

    @Override
    public List<Contact> getAll() {
        return null;
    }

    @Override
    public List<Contact> getAllContactByUserId(long id) {
        final String sql = " SELECT CT.CONTACT_ID,CT.CONTACT_TYPE_ID,CT.USER_ID,CT.TEXT, CTP.TITLE AS CTPTITLE FROM CONTACT AS CT\n" +
                "LEFT JOIN CONTACT_TYPE AS CTP ON (CT.CONTACT_TYPE_ID=CTP.CONTACT_TYPE_ID)  where CT.USER_ID = ? ";
        return jdbcTemplate.query(sql, new Object[]{id}, rs -> {




            List<Contact> contactList = new ArrayList<>();
            while (rs.next()) {
                Contact contact = new Contact();

                contact.setContactId(rs.getLong("CONTACT_ID"));
                contact.setContactType(new ValueLabel<>(rs.getLong("CONTACT_TYPE_ID"), rs.getString("CTPTITLE")));
                contact.setUserId(rs.getLong("USER_ID"));
                contact.setText(rs.getString("TEXT"));
                contactList.add(contact);
            }
            return contactList;

        });
    }

    @Override
    public List<Contact> getAllContactByBranchesId(long id) {
        final String sql = " SELECT CT.CONTACT_ID,CT.CONTACT_TYPE_ID,CT.USER_ID,CT.TEXT, CTP.TITLE AS CTPTITLE,CT.BRANCHES_ID FROM CONTACT AS CT\n" +
                "LEFT JOIN CONTACT_TYPE AS CTP ON (CT.CONTACT_TYPE_ID=CTP.CONTACT_TYPE_ID)  where CT.BRANCHES_ID = ? ";
        return jdbcTemplate.query(sql, new Object[]{id}, rs -> {

            if (!rs.next())
                return null;


            List<Contact> contactList = new ArrayList<>();
            while (rs.next()) {
                Contact contact = new Contact();

                contact.setContactId(rs.getLong("CONTACT_ID"));
                contact.setContactType(new ValueLabel<>(rs.getLong("CONTACT_TYPE_ID"), rs.getString("CTPTITLE")));
                contact.setBranchesId(rs.getLong("BRANCHES_ID"));
                contact.setText(rs.getString("TEXT"));
                contactList.add(contact);
            }
            return contactList;

        });
    }

    @Override
    public String getContactByBranchesId(long branchesId, long type) {

        try {
            final String sql = " select C.TEXT from CONTACT AS C WHERE C.BRANCHES_ID=? AND C.CONTACT_TYPE_ID=? ";
            return jdbcTemplate.query(sql, new Object[]{branchesId,type}, rs -> {
                if (!rs.next())
                    return null;
                return rs.getString("TEXT");
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public String getContactByUserId(long userId, long type) {

        try {
            final String sql = " select C.TEXT from CONTACT AS C WHERE C.USER_ID=? AND C.CONTACT_TYPE_ID=? ";
            return jdbcTemplate.query(sql, new Object[]{userId,type}, rs -> {
                if (!rs.next())
                    return null;
                return rs.getString("TEXT");
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
