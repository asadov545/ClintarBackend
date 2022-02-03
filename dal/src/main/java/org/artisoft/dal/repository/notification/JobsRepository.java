package org.artisoft.dal.repository.notification;

import com.google.gson.Gson;
import com.zaxxer.hikari.HikariDataSource;
import org.artisoft.dal.dao.notification.JobsDao;
import org.artisoft.dal.repository.utils.CommonDataHelper;
import org.artisoft.domain.ModTask.permissions.RolePrivsCompdata;
import org.artisoft.domain.ModToolbox.project.MainProjectList;
import org.artisoft.domain.Notification.GetJobs;
import org.artisoft.domain.Notification.Jobs;
import org.artisoft.domain.Notification.JobsCompdata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("jobsDao")
public class JobsRepository implements JobsDao {

    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcCall insUpdProc;
    private SimpleJdbcCall delUpdProc;

    @Autowired
    public JobsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    public void setDataSource(HikariDataSource dataSource) {
        this.insUpdProc = new SimpleJdbcCall(dataSource);
        this.delUpdProc = new SimpleJdbcCall(dataSource);
    }


    @Override
    public Jobs getById(long id) {
        return null;
    }

    @Override
    public long insert(Jobs jobs) {
        try {
            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("jsonObj", new Gson().toJson(jobs))
                    .addValue("newjobId", 0);

            SimpleJdbcCall jdbcCall = insUpdProc.withProcedureName("JobsInsUpd");
            jdbcCall.declareParameters(
                    new SqlParameter("jsonObj", Types.VARCHAR),
                    new SqlOutParameter("newjobId", Types.INTEGER));

            Map<String, Object> resultMap = jdbcCall.execute(parameters);
            jobs.setJobId(Long.parseLong(resultMap.get("newjobId").toString()));
            return 1;

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public boolean update(Jobs jobs) {
        return false;
    }

    @Override
    public boolean delete(long id) {
        try {
            SqlParameterSource parameters = new MapSqlParameterSource().addValue("jobsId", id);
            SimpleJdbcCall jdbcCall = delUpdProc.withProcedureName("JobsDel");
            jdbcCall.declareParameters(new SqlParameter("jobsId", Types.INTEGER));
            jdbcCall.execute(parameters);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public List<Jobs> filterData(HashMap<String, String> map) {


        return null;
    }

    @Override
    public List<Jobs> getAll() {
        return null;
    }

    @Override
    public JobsCompdata getJobsCompData() {
        final String sqluser = "SELECT US.USER_ID as value, US.FULLNAME as label FROM USERS AS US WHERE US.STATUS=1 AND US.USER_TYPE_ID=1 \n" +
                "and EXISTS (SELECT 1 FROM USER_MODULES AS UM WHERE (US.USER_ID=UM.USER_ID AND UM.MOD_ID=2))";

        final String sqlforms = " SELECT PT.PT_ID as value, PT.TITLE as label  FROM  TB_PRJ_TEMPLATES AS PT WHERE PT.STATUS=1  ";

        final String sqlhours = " SELECT HOURS_ID as value,TITLE as label FROM HOURS ";
        final String sqldays = " SELECT DAYS_ID AS value,TITLE as label FROM DAYS ";

        JobsCompdata jobsCompdata = new JobsCompdata();
        jobsCompdata.setUserList(CommonDataHelper.getValueLabels(sqluser, this.jdbcTemplate));
        jobsCompdata.setFormList(CommonDataHelper.getValueLabels(sqlforms, this.jdbcTemplate));
        jobsCompdata.setHourList(CommonDataHelper.getValueLabels(sqlhours, this.jdbcTemplate));
        jobsCompdata.setDayList(CommonDataHelper.getValueLabels(sqldays, this.jdbcTemplate));
        return jobsCompdata;
    }

    @Override
    public List<GetJobs> getAllJobs(HashMap<String, String> map) {

        final String sql = " select NJ.NJ_ID,U.FULLNAME,TP.TITLE AS TTITLE,H.TITLE AS HTITLE, IF(NJ.IS_MAIL=1,'Checked','') AS MAIL,IF(NJ.IS_PUSH=1,'Checked','') AS PUSH from NOTIFICATION_JOBS as NJ\n" +
                "LEFT JOIN USERS AS U  ON (NJ.USER_ID=U.USER_ID)\n" +
                "LEFT JOIN TB_PRJ_TEMPLATES AS TP ON (NJ.PRJ_ID=TP.PT_ID)\n" +
                "LEFT JOIN HOURS AS H ON (NJ.HOURS_ID=H.HOURS_ID)  order by  NJ.NJ_ID desc";
        return jdbcTemplate.query(sql, new Object[]{}, rs -> {
            List<GetJobs> resultList = new ArrayList<>();
            while (rs.next()) {
                GetJobs getJobs = new GetJobs();
                getJobs.setJobId(rs.getLong("NJ_ID"));
                getJobs.setUser(rs.getString("FULLNAME"));
                getJobs.setForm(rs.getString("TTITLE"));
                getJobs.setHours(rs.getString("HTITLE"));
                getJobs.setMail(rs.getString("MAIL"));
                getJobs.setPush(rs.getString("PUSH"));
                resultList.add(getJobs);
            }
            return resultList;

        });



    }


}
