package org.artisoft.dal.repository.admin;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zaxxer.hikari.HikariDataSource;
import org.artisoft.dal.dao.admin.RoleDao;
import org.artisoft.domain.ModTask.permissions.Roles;
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

@Repository("roleDao")
public class RoleRepository implements RoleDao {
    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcCall insUpdProc;
    private SimpleJdbcCall delUpdProc;


    @Autowired
    public RoleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    public void setDataSource(HikariDataSource dataSource) {
        this.insUpdProc = new SimpleJdbcCall(dataSource);
        this.delUpdProc = new SimpleJdbcCall(dataSource);
    }


    @Override
    public Roles getById(long id) {
        try {
            final String sql = "SELECT RoleByIdJson(?) AS jsonObj";
            return jdbcTemplate.query(sql, new Object[]{id}, rs -> {
                if (!rs.next())
                    return null;
                return new Gson().fromJson(rs.getString("jsonObj"), Roles.class);
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public long insert(Roles role) {
        try {
            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("jsonObj", new Gson().toJson(role))
                    .addValue("newRoleId", 0);

            SimpleJdbcCall jdbcCall = insUpdProc.withProcedureName("RoleInsUpd");
            jdbcCall.declareParameters(
                    new SqlParameter("jsonObj", Types.VARCHAR),
                    new SqlOutParameter("newRoleId", Types.INTEGER));

            Map<String, Object> resultMap = jdbcCall.execute(parameters);
            role.setRoleId(Integer.parseInt(resultMap.get("newRoleId").toString()));
            return 1;

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public boolean update(Roles role) {
        return insert(role) > 0;
    }

    @Override
    public boolean delete(long id) {
        try {
            SqlParameterSource parameters = new MapSqlParameterSource().addValue("roleId", id);
            SimpleJdbcCall jdbcCall = delUpdProc.withProcedureName("RolesDel");
            jdbcCall.declareParameters(new SqlParameter("roleId", Types.INTEGER));
            jdbcCall.execute(parameters);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public List<Roles> getAll() {
        try {
            return getRoles(jdbcTemplate);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private static List<Roles> getRoles(JdbcTemplate jdbcTemplate) {
        return jdbcTemplate.query("SELECT GetAllRoles() AS jsonObj", rs -> {
            if (!rs.next())
                return null;
            Type listType = new TypeToken<ArrayList<Roles>>() {
            }.getType();
            return new Gson().fromJson(rs.getString("jsonObj"), listType);
        });
    }
}
