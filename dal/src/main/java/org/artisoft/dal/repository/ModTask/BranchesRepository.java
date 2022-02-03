package org.artisoft.dal.repository.ModTask;

import com.google.gson.Gson;
import com.zaxxer.hikari.HikariDataSource;
import org.artisoft.dal.dao.ModTask.BranchesDao;
import org.artisoft.dal.dao.ModTask.ContactDao;
import org.artisoft.domain.Address;
import org.artisoft.domain.ModTask.Branches;
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

@Repository("branchesDao")
public class BranchesRepository implements BranchesDao {


    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcCall insUpdProc;
    private SimpleJdbcCall delUpdProc;


    @Autowired
    private ContactDao contactDao;

    @Autowired
    public BranchesRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Autowired
    public void setDataSource(HikariDataSource dataSource) {
        this.insUpdProc = new SimpleJdbcCall(dataSource);
        this.delUpdProc = new SimpleJdbcCall(dataSource);
    }

    @Override
    public Branches getById(long id) {
        try {
            final String sql = "   SELECT BR.BRANCHES_ID,BR.TITLE,BR.PID,(select B.TITLE from BRANCHES as B WHERE B.BRANCHES_ID= BR.PID ) AS PIDTITLE,BR.ADDRESS_ID,ADDR.COUNTRY_ID,ADDR.ADDRESS_LINE,ADDR.ZIP_CODE ,CO.TITLE AS COTITLE\n" +
                    "                    FROM BRANCHES AS BR\n" +
                    "                    LEFT JOIN ADDRESS AS ADDR ON (BR.ADDRESS_ID=ADDR.ADDRESS_ID)\n" +
                    "                    LEFT JOIN COUNTRY AS CO ON (ADDR.COUNTRY_ID=CO.COUNTRY_ID) "
                    + " WHERE BR.STATUS=1 AND BR.BRANCHES_ID=?";
            return jdbcTemplate.query(sql, new Object[]{id}, rs -> {
                if (!rs.next())
                    return null;

                Branches branches = new Branches();
                branches.setBranchesId(rs.getLong("BRANCHES_ID"));
                branches.setTitle(rs.getString("TITLE"));
                branches.setPid(new ValueLabel<>(rs.getLong("PID"), rs.getString("PIDTITLE")));
                branches.setEmail(contactDao.getContactByBranchesId(rs.getLong("BRANCHES_ID"),1));
                branches.setMobile(contactDao.getContactByBranchesId(rs.getLong("BRANCHES_ID"),2));

                Address address = new Address();
                address.setAddressId(rs.getLong("ADDRESS_ID"));
                address.setCountry(new ValueLabel<>(rs.getLong("COUNTRY_ID"), rs.getString("COTITLE")));
                address.setAddressLine(rs.getString("ADDRESS_LINE"));
                address.setZipCode(rs.getString("ZIP_CODE"));
                branches.setAddress(address);

                return branches;
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public long insert(Branches branches) {
        try {
            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("jsonObj", new Gson().toJson(branches))
                    .addValue("newbranchesId", 0);

            SimpleJdbcCall jdbcCall = insUpdProc.withProcedureName("BranchesInsUpd");
            jdbcCall.declareParameters(
                    new SqlParameter("jsonObj", Types.VARCHAR),
                    new SqlOutParameter("newbranchesId", Types.INTEGER));

            Map<String, Object> resultMap = jdbcCall.execute(parameters);
            branches.setBranchesId(Long.parseLong(resultMap.get("newbranchesId").toString()));
            return 1;

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public boolean update(Branches branches) {
        return insert(branches) > 0;
    }

    @Override
    public boolean delete(long id) {
        try {
            SqlParameterSource parameters = new MapSqlParameterSource().addValue("branchesId", id);
            SimpleJdbcCall jdbcCall = delUpdProc.withProcedureName("BranchDel");
            jdbcCall.declareParameters(new SqlParameter("branchesId", Types.INTEGER));
            jdbcCall.execute(parameters);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public List<Branches> filterData(HashMap<String, String> map) {
        try {
            final String sql = "   SELECT BR.BRANCHES_ID,BR.TITLE,BR.PID,(select B.TITLE from BRANCHES as B WHERE B.BRANCHES_ID= BR.PID ) AS PIDTITLE,BR.ADDRESS_ID,ADDR.COUNTRY_ID,ADDR.ADDRESS_LINE,ADDR.ZIP_CODE ,CO.TITLE AS COTITLE\n" +
                    "                    FROM BRANCHES AS BR\n" +
                    "                    LEFT JOIN ADDRESS AS ADDR ON (BR.ADDRESS_ID=ADDR.ADDRESS_ID)\n" +
                    "                    LEFT JOIN COUNTRY AS CO ON (ADDR.COUNTRY_ID=CO.COUNTRY_ID) WHERE BR.STATUS=1 ";



            StringBuilder cond = new StringBuilder(sql);


            ResultSetExtractor<List<Branches>> rm = rs -> {
                List<Branches> branchesList = new ArrayList<>();
                createBranchesList(rs, branchesList);
                return branchesList;
            };

            cond.append(" order by BR.BRANCHES_ID desc");
            return jdbcTemplate.query(cond.toString(), rm);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void createBranchesList(ResultSet rs, List<Branches> branchesList) throws SQLException {
        while (rs.next()) {

            Branches branches = new Branches();
            branches.setBranchesId(rs.getLong("BRANCHES_ID"));
            branches.setTitle(rs.getString("TITLE"));
            branches.setPid(new ValueLabel<>(rs.getLong("PID"), rs.getString("PIDTITLE")));
            branches.setEmail(contactDao.getContactByBranchesId(rs.getLong("BRANCHES_ID"),1));
            branches.setMobile(contactDao.getContactByBranchesId(rs.getLong("BRANCHES_ID"),2));

            Address address = new Address();
            address.setAddressId(rs.getLong("ADDRESS_ID"));
            address.setCountry(new ValueLabel<>(rs.getLong("COUNTRY_ID"), rs.getString("COTITLE")));
            address.setAddressLine(rs.getString("ADDRESS_LINE"));
            address.setZipCode(rs.getString("ZIP_CODE"));
            branches.setAddress(address);
            branchesList.add(branches);
        }
    }

    @Override
    public List<Branches> getAll() {
        return null;
    }

    @Override
    public List<ValueLabel<Long, String>> filterBranchesList(String label) {
        try {
            final String sql = "SELECT BRANCHES_ID, TITLE FROM BRANCHES where `STATUS`=1 AND TITLE like ?";
            return jdbcTemplate.query(sql, new Object[]{"%".concat(label).concat("%")}, rs -> {
                List<ValueLabel<Long, String>> list = new ArrayList<>();
                while (rs.next())
                    list.add(new ValueLabel<>(rs.getLong("BRANCHES_ID"), rs.getString("TITLE")));


                return list;
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
