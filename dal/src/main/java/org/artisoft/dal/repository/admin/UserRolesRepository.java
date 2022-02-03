package org.artisoft.dal.repository.admin;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zaxxer.hikari.HikariDataSource;
import org.artisoft.dal.dao.admin.UserRoleDao;
import org.artisoft.dal.repository.utils.CommonDataHelper;
import org.artisoft.domain.ModTask.permissions.UserRolesCompData;
import org.artisoft.domain.ValueLabel;
import org.artisoft.domain.ModTask.permissions.UserRoles;
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

@Repository("UserRoleDao")
public class UserRolesRepository implements UserRoleDao {
    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcCall insUpdProc;
    private SimpleJdbcCall delUpdProc;

    @Autowired
    public void setDataSource(HikariDataSource dataSource) {
        this.insUpdProc = new SimpleJdbcCall(dataSource);
        this.delUpdProc = new SimpleJdbcCall(dataSource);
    }

    @Autowired
    public UserRolesRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<ValueLabel<Long, String>> getUsers(String label) {
        return jdbcTemplate.query("SELECT GetUsers(concat('%',?,'%')) AS jsonObj",
                new Object[]{label.toLowerCase()}, rs -> {
                    if (!rs.next())
                        return null;
                    Type listType = new TypeToken<List<ValueLabel<Long, String>>>() {
                    }.getType();
                    return new Gson().fromJson(rs.getString("jsonObj"), listType);
                });
    }

    @Override
    public UserRolesCompData getUserRolesCompData() {
        final String sqlroles = "SELECT ROLE_ID as value, TITLE as label FROM ROLES WHERE STATUS=1 ";
        final String sqlusers = "SELECT  U.USER_ID AS value,  U.USERNAME as label FROM  USERS U WHERE U.STATUS=1 ";

        UserRolesCompData userRolesCompData = new UserRolesCompData();
        userRolesCompData.setRoleList(CommonDataHelper.getValueLabels(sqlroles, this.jdbcTemplate));
        userRolesCompData.setUserList(CommonDataHelper.getValueLabels(sqlusers, this.jdbcTemplate));

        return userRolesCompData;
    }

    @Override
    public List<UserRoles> getAll() {
        try {
            return getUserRoles(jdbcTemplate);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    private static List<UserRoles> getUserRoles(JdbcTemplate jdbcTemplate) {
        return jdbcTemplate.query("SELECT GetAllUserRoles() AS jsonObj", rs -> {
            if (!rs.next())
                return null;
            Type listType = new TypeToken<ArrayList<UserRoles>>() {
            }.getType();
            return new Gson().fromJson(rs.getString("jsonObj"), listType);
        });
    }

    @Override
    public UserRoles getById(long id) {
        try {
            final String sql = "SELECT UserRoleByIdJson(?) AS jsonObj";
            return jdbcTemplate.query(sql, new Object[]{id}, rs -> {
                if (!rs.next())
                    return null;
                return new Gson().fromJson(rs.getString("jsonObj"), UserRoles.class);
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public long insert(UserRoles userRoles) {
        try {
            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("jsonObj", new Gson().toJson(userRoles))
                    .addValue("newUserRoleId", 0);

            SimpleJdbcCall jdbcCall = insUpdProc.withProcedureName("UserRoleInsUpd");
            jdbcCall.declareParameters(
                    new SqlParameter("jsonObj", Types.VARCHAR),
                    new SqlOutParameter("newUserRoleId", Types.INTEGER));

            Map<String, Object> resultMap = jdbcCall.execute(parameters);
            userRoles.setUserRoleId(Integer.parseInt(resultMap.get("newUserRoleId").toString()));
            return 1;

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public boolean update(UserRoles userRoles) {
        return insert(userRoles) > 0;
    }

    @Override
    public boolean delete(long id) {
        try {
            SqlParameterSource parameters = new MapSqlParameterSource().addValue("userRoleId", id);
            SimpleJdbcCall jdbcCall = delUpdProc.withProcedureName("UserRoleDel");
            jdbcCall.declareParameters(new SqlParameter("userRoleId", Types.INTEGER));
            jdbcCall.execute(parameters);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
