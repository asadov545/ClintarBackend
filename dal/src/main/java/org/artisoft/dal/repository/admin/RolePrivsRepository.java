package org.artisoft.dal.repository.admin;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zaxxer.hikari.HikariDataSource;
import org.artisoft.dal.dao.admin.RolePrivsDao;
import org.artisoft.dal.repository.utils.CommonDataHelper;
import org.artisoft.dal.repository.utils.CommonMethods;
import org.artisoft.domain.ModTask.permissions.RolePrivsCompdata;
import org.artisoft.domain.ValueLabel;
import org.artisoft.domain.ModTask.permissions.RolePrivs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Type;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository("rolePrivsDao")
public class RolePrivsRepository implements RolePrivsDao {
    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcCall insUpdProc;
    private SimpleJdbcCall delUpdProc;


    @Autowired
    public RolePrivsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    CommonMethods commonMethods;

    @Autowired
    public void setDataSource(HikariDataSource dataSource) {
        this.insUpdProc = new SimpleJdbcCall(dataSource);
        this.delUpdProc = new SimpleJdbcCall(dataSource);
    }


    @Override
    public RolePrivs getById(long id) {
        try {
            final String sql = "SELECT RolePrivsByIdJson(?) AS jsonObj";
            return jdbcTemplate.query(sql, new Object[]{id}, rs -> {
                if (!rs.next())
                    return null;
                return new Gson().fromJson(rs.getString("jsonObj"), RolePrivs.class);
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public long insert(RolePrivs rolePrivs) {
        try {
            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("jsonObj", new Gson().toJson(rolePrivs))
                    .addValue("newRolePrivId", 0);

            SimpleJdbcCall jdbcCall = insUpdProc.withProcedureName("RolePrivsInsUpd");
            jdbcCall.declareParameters(
                    new SqlParameter("jsonObj", Types.VARCHAR),
                    new SqlOutParameter("newRolePrivId", Types.INTEGER));

            Map<String, Object> resultMap = jdbcCall.execute(parameters);
            rolePrivs.setRolePrivId(Integer.parseInt(resultMap.get("newRolePrivId").toString()));
            return 1;

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public boolean update(RolePrivs rolePrivs) {
        return insert(rolePrivs) > 0;
    }

    @Override
    public boolean delete(long id) {
        try {
            SqlParameterSource parameters = new MapSqlParameterSource().addValue("rolePrivId", id);
            SimpleJdbcCall jdbcCall = delUpdProc.withProcedureName("RolePrivsDel");
            jdbcCall.declareParameters(new SqlParameter("rolePrivId", Types.INTEGER));
            jdbcCall.execute(parameters);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public List<RolePrivs> getAll() {
        try {
            return getRolePrivs(jdbcTemplate);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private static List<RolePrivs> getRolePrivs(JdbcTemplate jdbcTemplate) {
        return jdbcTemplate.query("SELECT GetAllRolePrivs() AS jsonObj", rs -> {
            if (!rs.next())
                return null;
            Type listType = new TypeToken<ArrayList<RolePrivs>>() {
            }.getType();
            return new Gson().fromJson(rs.getString("jsonObj"), listType);
        });
    }

    @Override
    public List<ValueLabel<Long, String>> getRoles(String label) {
        return commonMethods.getValueLabels("SELECT ROLE_ID as value, TITLE as label FROM ROLES WHERE STATUS=1 " +
                "AND TRIM(LOWER(TITLE)) LIKE ?", jdbcTemplate, new Object[]{label.trim().concat("%")});
    }

    @Override
    public List<ValueLabel<Long, String>> getPrivs(String label) {
        return commonMethods.getValueLabels("SELECT PRIV_ID AS value, TITLE as label FROM `PRIVILEGES` WHERE STATUS=1 " +
                "AND TRIM(LOWER(TITLE)) LIKE ?", jdbcTemplate, new Object[]{label.trim().concat("%")});
    }

    @Override
    public RolePrivsCompdata getRolePrivsCompData() {
        final String sqlroles = "SELECT ROLE_ID as value, TITLE as label FROM ROLES WHERE STATUS=1 ";
        final String sqlprives = "SELECT PRIV_ID AS value, TITLE as label FROM `PRIVILEGES` WHERE STATUS=1 ";

        RolePrivsCompdata rolePrivsCompdata = new RolePrivsCompdata();
        rolePrivsCompdata.setRoleList(CommonDataHelper.getValueLabels(sqlroles, this.jdbcTemplate));
        rolePrivsCompdata.setPrivList(CommonDataHelper.getValueLabels(sqlprives, this.jdbcTemplate));

        return rolePrivsCompdata;
    }


}
