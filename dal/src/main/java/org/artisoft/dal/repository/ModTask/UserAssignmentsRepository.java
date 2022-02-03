package org.artisoft.dal.repository.ModTask;

import com.google.gson.Gson;
import com.zaxxer.hikari.HikariDataSource;
import org.artisoft.dal.dao.ModTask.UserAssignmentsDao;
import org.artisoft.dal.dao.admin.PrivilegesDao;
import org.artisoft.domain.ModTask.UserAssignments;
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

@Repository("userAssignmentsDao")
public class UserAssignmentsRepository implements UserAssignmentsDao {
    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcCall insUpdProc;
    private SimpleJdbcCall delUpdProc;

    @Autowired
    public UserAssignmentsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    private PrivilegesDao privilegesDao;

    @Autowired
    public void setDataSource(HikariDataSource dataSource) {
        this.insUpdProc = new SimpleJdbcCall(dataSource);
        this.delUpdProc = new SimpleJdbcCall(dataSource);
    }

    @Override
    public UserAssignments getById(long id) {
        try {
            final String sql = " SELECT UA.USER_ASSIGN_ID,UA.USER_ID,US1.FULLNAME as USERTITLE,UA.ASSIGNED_USER_ID, US2.FULLNAME as ASSIGNUSERTITLE,UA.STATUS FROM " +
                    "USER_ASSIGNMENTS AS UA LEFT JOIN USERS AS US1 ON(UA.USER_ID=US1.USER_ID) LEFT JOIN USERS AS US2 ON(UA.ASSIGNED_USER_ID=US2.USER_ID) "
                    + " WHERE UA.STATUS=1 AND UA.USER_ASSIGN_ID=?";
            return jdbcTemplate.query(sql, new Object[]{id}, rs -> {
                if (!rs.next())
                    return null;

                UserAssignments userAssignments = new UserAssignments();
                userAssignments.setUserAssignmentId(rs.getLong("USER_ASSIGN_ID"));
                userAssignments.setUser(new ValueLabel<>(rs.getLong("USER_ID"), rs.getString("USERTITLE")));
                userAssignments.setAssignuser(new ValueLabel<>(rs.getLong("ASSIGNED_USER_ID"), rs.getString("ASSIGNUSERTITLE")));
                userAssignments.setStatus(rs.getInt("STATUS"));
                return userAssignments;
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public long insert(UserAssignments userAssignments) {
        try {
            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("jsonObj", new Gson().toJson(userAssignments))
                    .addValue("newuserAssignmentId", 0);

            SimpleJdbcCall jdbcCall = insUpdProc.withProcedureName("UserAssignmentsInsUpd");
            jdbcCall.declareParameters(
                    new SqlParameter("jsonObj", Types.VARCHAR),
                    new SqlOutParameter("newuserAssignmentId", Types.INTEGER));

            Map<String, Object> resultMap = jdbcCall.execute(parameters);
            userAssignments.setUserAssignmentId(Long.parseLong(resultMap.get("newuserAssignmentId").toString()));
            return 1;

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public boolean update(UserAssignments userAssignments) {
        return insert(userAssignments) > 0;
    }

    @Override
    public boolean delete(long id) {

        try {
            SqlParameterSource parameters = new MapSqlParameterSource().addValue("userAssignmentId", id);
            SimpleJdbcCall jdbcCall = delUpdProc.withProcedureName("UserAssignmentsDel");
            jdbcCall.declareParameters(new SqlParameter("userAssignmentId", Types.INTEGER));
            jdbcCall.execute(parameters);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public List<UserAssignments> filterData2(HashMap<String, String> map, Long user_id) {
        try {
            final String sql = " SELECT UA.USER_ASSIGN_ID,UA.USER_ID,US1.FULLNAME as USERTITLE,UA.ASSIGNED_USER_ID, US2.FULLNAME as ASSIGNUSERTITLE,UA.STATUS FROM " +
                    "USER_ASSIGNMENTS AS UA LEFT JOIN USERS AS US1 ON(UA.USER_ID=US1.USER_ID) LEFT JOIN USERS AS US2 ON(UA.ASSIGNED_USER_ID=US2.USER_ID) WHERE  UA.STATUS=1  ";

            StringBuilder cond = new StringBuilder(sql);
            List<String> paramList = new ArrayList<>();

            if (privilegesDao.checkPrivStatus(user_id, 33) != 1)
            {
                cond.append("  AND UA.USER_ID=" + user_id );
            }

            if (map.containsKey("userTitle") && !map.get("userTitle").trim().equals("")) {
                cond.append(" AND  US1.FULLNAME like ?");
                paramList.add(map.get("userTitle") + "%");
            }

            if (map.containsKey("assignTitle") && !map.get("assignTitle").trim().equals("")) {
                cond.append(" and US2.FULLNAME like ?");
                paramList.add(map.get("assignTitle") + "%");

            }


            ResultSetExtractor<List<UserAssignments>> rm = rs -> {
                List<UserAssignments> userAssignmentsList = new ArrayList<>();
                createUserAssignmentsList(rs, userAssignmentsList);
                return userAssignmentsList;
            };


            if (paramList.isEmpty()) {
                cond.append(" order by UA.USER_ASSIGN_ID desc ");
                return jdbcTemplate.query(cond.toString(), rm);
            } else {
                return jdbcTemplate.query(cond.toString(), paramList.toArray(), rm);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private void createUserAssignmentsList(ResultSet rs, List<UserAssignments> userAssignmentsList) throws SQLException {
        while (rs.next()) {

            UserAssignments userAssignments = new UserAssignments();
            userAssignments.setUserAssignmentId(rs.getLong("USER_ASSIGN_ID"));
            userAssignments.setUser(new ValueLabel<>(rs.getLong("USER_ID"), rs.getString("USERTITLE")));
            userAssignments.setAssignuser(new ValueLabel<>(rs.getLong("ASSIGNED_USER_ID"), rs.getString("ASSIGNUSERTITLE")));
            userAssignments.setStatus(rs.getInt("STATUS"));


            userAssignmentsList.add(userAssignments);
        }
    }


}
