package org.artisoft.dal.repository.ModToolbox;

import com.google.gson.Gson;
import com.zaxxer.hikari.HikariDataSource;
import org.artisoft.dal.dao.ModToolbox.MainProjectDao;
import org.artisoft.domain.ModToolbox.project.MainProject;
import org.artisoft.domain.Users;
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

@Repository("mainProjectDao")
public class MainProjectRepository implements MainProjectDao {
    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcCall insUpdProc;
    private SimpleJdbcCall delProc;


    @Autowired
    public MainProjectRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    public void setDataSource(HikariDataSource dataSource) {

        this.insUpdProc = new SimpleJdbcCall(dataSource);
        this.delProc = new SimpleJdbcCall(dataSource);
    }


    @Override
    public MainProject getById(long id) {
        try {
            final String sql = " select TMP.MAIN_PRJ_ID,TMP.NAME,TMP.TASK_LOCATION,TMP.FIRST_AID_CONTACT, IFNULL(UNIX_TIMESTAMP(TMP.CREATE_DATE),0) as CREATE_DATE from  TB_MAIN_PRJ as TMP " +
                    "where TMP.MAIN_PRJ_ID=?";
            return jdbcTemplate.query(sql, new Object[]{id}, rs -> {
                if (!rs.next())
                    return null;
                MainProject mainProject = new MainProject();
                mainProject.setMainPrjId(rs.getLong("MAIN_PRJ_ID"));
                mainProject.setName(rs.getString("NAME"));
                mainProject.setTaskLocation(rs.getString("TASK_LOCATION"));
                mainProject.setFirstAidContact(rs.getString("FIRST_AID_CONTACT"));
                mainProject.setCreateDate(rs.getLong("CREATE_DATE"));
                mainProject.setUsersList(getMainProjectUsersList(mainProject.getMainPrjId()));
                return mainProject;
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private List<Long> getMainProjectUsersList(long mainPrjId) {
        final String sql = " select MPU.USER_ID from TB_MAIN_PRJ_USERS as MPU WHERE MPU.STATUS=1 AND MPU.MAIN_PRJ_ID=?";
        return jdbcTemplate.query(sql, new Object[]{mainPrjId}, rs -> {
            List<Long> resultList = new ArrayList<>();
            while (rs.next()) {
                resultList.add(rs.getLong("USER_ID"));
            }
            return resultList;
        });
    }


    @Override
    public long insert(MainProject mainProject) {
        try {
            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("jsonObj", new Gson().toJson(mainProject))
                    .addValue("newmainPrjId", 0);

            SimpleJdbcCall jdbcCall = insUpdProc.withProcedureName("MainProjectInsUpd");
            jdbcCall.declareParameters(
                    new SqlParameter("jsonObj", Types.VARCHAR),
                    new SqlOutParameter("newmainPrjId", Types.INTEGER));

            Map<String, Object> resultMap = jdbcCall.execute(parameters);
            mainProject.setMainPrjId(Long.parseLong(resultMap.get("newmainPrjId").toString()));
            return 1;

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public boolean update(MainProject mainProject) {

        return insert(mainProject) > 0;
    }

    @Override
    public boolean delete(long id) {
        try {
            SqlParameterSource parameters = new MapSqlParameterSource().addValue("mainPrjId", id);
            SimpleJdbcCall jdbcCall = delProc.withProcedureName("MainProjectDel");
            jdbcCall.declareParameters(new SqlParameter("mainPrjId", Types.INTEGER));
            jdbcCall.execute(parameters);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public List<MainProject> filterData2(long userId) {
        try {

            final String sql = " select TMP.MAIN_PRJ_ID,TMP.NAME,TMP.TASK_LOCATION,TMP.FIRST_AID_CONTACT," +
                    " IFNULL(UNIX_TIMESTAMP(TMP.CREATE_DATE),0) as CREATE_DATE from  TB_MAIN_PRJ as TMP " +
                    " where TMP.STATUS=1 AND TMP.CREATE_USER_ID=" + userId;
            StringBuilder cond = new StringBuilder(sql);

            ResultSetExtractor<List<MainProject>> rm = rs -> {
                List<MainProject> mainProjectList = new ArrayList<>();
                createMainProjectList(rs, mainProjectList);
                return mainProjectList;
            };
            cond.append(" order by TMP.MAIN_PRJ_ID desc ");
            return jdbcTemplate.query(cond.toString(), rm);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean deleteMainProjectUser(long mainPrjId,long userId) {
        try {
            SqlParameterSource parameters = new MapSqlParameterSource().addValue("mainPrjId", mainPrjId)
                    .addValue("userId", userId);
            SimpleJdbcCall jdbcCall = delProc.withProcedureName("MainPrjUserDel");
            jdbcCall.declareParameters(new SqlParameter("mainPrjId", Types.INTEGER),
                    new SqlParameter("userId", Types.INTEGER));
            jdbcCall.execute(parameters);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    private void createMainProjectList(ResultSet rs, List<MainProject> mainProjectList) throws SQLException {
        while (rs.next()) {

            MainProject mainProject = new MainProject();
            mainProject.setMainPrjId(rs.getLong("MAIN_PRJ_ID"));
            mainProject.setName(rs.getString("NAME"));
            mainProject.setTaskLocation(rs.getString("TASK_LOCATION"));
            mainProject.setFirstAidContact(rs.getString("FIRST_AID_CONTACT"));
            mainProject.setCreateDate(rs.getLong("CREATE_DATE"));
            mainProject.setUsersList(getMainProjectUsersList(mainProject.getMainPrjId()));
            mainProjectList.add(mainProject);
        }
    }


}
