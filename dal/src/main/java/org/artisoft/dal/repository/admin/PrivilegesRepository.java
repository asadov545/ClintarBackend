package org.artisoft.dal.repository.admin;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zaxxer.hikari.HikariDataSource;
import org.artisoft.dal.dao.admin.PrivilegesDao;
import org.artisoft.domain.ModTask.permissions.Privileges;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository("privilegesDao")
public class PrivilegesRepository implements PrivilegesDao {
    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcCall insUpdProc;
    private SimpleJdbcCall CheckStatusProc;
    private SimpleJdbcCall delUpdProc;


    @Autowired
    public PrivilegesRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    public void setDataSource(HikariDataSource dataSource) {
        this.insUpdProc = new SimpleJdbcCall(dataSource);
        this.delUpdProc = new SimpleJdbcCall(dataSource);
        this.CheckStatusProc = new SimpleJdbcCall(dataSource);
    }


    @Override
    public Privileges getById(long id) {
        try {
            final String sql = "SELECT PrivilegeByIdJson(?) AS jsonObj";
            return jdbcTemplate.query(sql, new Object[]{id}, rs -> {
                if (!rs.next())
                    return null;
                return new Gson().fromJson(rs.getString("jsonObj"), Privileges.class);
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public long insert(Privileges privileges) {
        try {
            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("jsonObj", new Gson().toJson(privileges))
                    .addValue("newPrivId", 0);

            SimpleJdbcCall jdbcCall = insUpdProc.withProcedureName("PrivilegeInsUpd");
            jdbcCall.declareParameters(
                    new SqlParameter("jsonObj", Types.VARCHAR),
                    new SqlOutParameter("newPrivId", Types.INTEGER));

            Map<String, Object> resultMap = jdbcCall.execute(parameters);
            privileges.setPrivId(Integer.parseInt(resultMap.get("newPrivId").toString()));
            return 1;

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public boolean update(Privileges privileges) {
        return insert(privileges) > 0;
    }

    @Override
    public boolean delete(long id) {
        try {
            SqlParameterSource parameters = new MapSqlParameterSource().addValue("privId", id);
            SimpleJdbcCall jdbcCall = delUpdProc.withProcedureName("PrivilegeDel");
            jdbcCall.declareParameters(new SqlParameter("privId", Types.INTEGER));
            jdbcCall.execute(parameters);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public List<Privileges> getAll() {
        try {
            return getRoles(jdbcTemplate);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private static List<Privileges> getRoles(JdbcTemplate jdbcTemplate) {
        return jdbcTemplate.query("SELECT GetAllPrivileges() AS jsonObj", rs -> {
            if (!rs.next())
                return null;
            Type listType = new TypeToken<ArrayList<Privileges>>() {
            }.getType();
            return new Gson().fromJson(rs.getString("jsonObj"), listType);
        });
    }

    @Override
    public Integer checkPrivStatus(long user_id, long priv_id) {
        if (user_id==0 || priv_id==0)
            return -1;
        try {
            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("user_id", user_id)
                    .addValue("priv_id", priv_id)
                    .addValue("res", 0);

            SimpleJdbcCall jdbcCall = CheckStatusProc.withProcedureName("CheckPrivStatus");
            jdbcCall.declareParameters(
                    new SqlParameter("user_id", Types.INTEGER),
                    new SqlParameter("priv_id", Types.INTEGER),
                    new SqlOutParameter("res", Types.INTEGER));

            Map<String, Object> resultMap = jdbcCall.execute(parameters);
              return Integer.parseInt(resultMap.get("res").toString());

        } catch (Exception e) {

            e.printStackTrace();
            throw e;

        }


    }

    @Override
    public List<Long> getPriviligiesList(Long user_id) {
        try {
            final String sql = "select RP.PRIV_ID from USER_ROLES AS US INNER JOIN ROLE_PRIVS   AS RP ON (US.ROLE_ID=RP.ROLE_ID) " +
                    " WHERE RP.`STATUS`=1 AND US.`STATUS`=1 AND US.USER_ID="+user_id.toString();
            StringBuilder cond = new StringBuilder(sql);

            ResultSetExtractor<List<Long>> rm = rs -> {
                List<Long> privileges = new ArrayList<>();
                createPriviligiesList(rs, privileges);
                return privileges;
            };

            cond.append(" order by  RP.PRIV_ID desc ");
            return jdbcTemplate.query(cond.toString(), rm);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private void createPriviligiesList(ResultSet rs, List<Long> privileges) throws SQLException {
        while (rs.next()) {
         privileges.add(rs.getLong("PRIV_ID"));

        }
    }
}
