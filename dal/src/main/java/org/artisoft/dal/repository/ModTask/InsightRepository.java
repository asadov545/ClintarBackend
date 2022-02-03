package org.artisoft.dal.repository.ModTask;

import com.zaxxer.hikari.HikariDataSource;
import org.artisoft.dal.dao.ModTask.InsightDao;
import org.artisoft.dal.dao.admin.PrivilegesDao;
import org.artisoft.domain.ModTask.Insight.InsightCombine;
import org.artisoft.domain.ModTask.Insight.InsightTaskStatusList;
import org.artisoft.domain.ValueLabel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository("inshiteDao")
public class InsightRepository implements InsightDao {

    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcCall insUpdProc;
    private SimpleJdbcCall delUpdProc;

    @Autowired
    private PrivilegesDao privilegesDao;

    @Autowired
    public InsightRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    public void setDataSource(HikariDataSource dataSource) {
        this.insUpdProc = new SimpleJdbcCall(dataSource);
        this.delUpdProc = new SimpleJdbcCall(dataSource);
    }


    private List<ValueLabel<Integer, String>> getCustomerTasksList(long user_id) {
        try {
             String sql = " select CUS.NAME LABEL,COUNT(TA.TASK_ID) as VALUE FROM TASK_ASSIGNEES as TA " +
                    " LEFT OUTER JOIN USERS AS US ON(TA.USER_ID=US.USER_ID) " +
                    " LEFT OUTER JOIN CUSTOMERS AS CUS ON (CUS.CUSTOMER_ID=US.CUSTOMER_ID) " ;

            if ( privilegesDao.checkPrivStatus(user_id,33)!=1)
            {
                sql=sql+(" where TA.USER_ID IN ("+ Long.toString(user_id)+") OR EXISTS (SELECT 1 FROM TASKS AS T WHERE (T.TASK_ID=TA.TASK_ID AND  T.CREATE_USER_ID="+ Long.toString(user_id)+")) ");
            }
             sql=sql+ " GROUP BY TA.USER_ID,CUS.NAME";

            ResultSetExtractor<List<ValueLabel<Integer, String>>> rm = rs -> {
                List<ValueLabel<Integer, String>> customerTasksList = new ArrayList<>();
                fill(rs, customerTasksList);
                return customerTasksList;
            };

            return jdbcTemplate.query(sql, rm);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<InsightTaskStatusList> getTaskStatusList( long user_id) {
        try {
            String sql = "select " +
                    " TS.TITLE as LABEL, " +
                    " SUM(IF(T.TASK_ID IS NULL ,0,1)) AS VALUE,TS.COLOR,TS.ICON1,TS.ICON2,TS.TASK_STATUS_ID  " +
                    " from TASKS as T RIGHT JOIN TASK_STATUS AS TS  ON (T.TASK_STATUS_ID=TS.TASK_STATUS_ID) " ;

            if ( privilegesDao.checkPrivStatus(user_id,33)!=1)
            {

                sql=sql+("  where (T.CREATE_USER_ID="+ Long.toString(user_id)+" OR (EXISTS (SELECT 1 FROM TASK_ASSIGNEES AS TA WHERE (T.TASK_ID=TA.TASK_ID AND  TA.USER_ID="+ Long.toString(user_id)+"))))");
            }

            sql=sql+" GROUP BY T.TASK_STATUS_ID,TS.TITLE,TS.COLOR,TS.ICON1,TS.ICON2,TS.TASK_STATUS_ID ";
            return jdbcTemplate.query(sql, rs -> {
                List<InsightTaskStatusList> list = new ArrayList<>();
                while (rs.next()) {
                    InsightTaskStatusList insightTaskStatusList = new InsightTaskStatusList();
                    insightTaskStatusList.setValue(rs.getInt("VALUE"));
                    insightTaskStatusList.setTitle(rs.getString("LABEL"));
                    insightTaskStatusList.setColor(rs.getString("COLOR"));
                    insightTaskStatusList.setIcon1(rs.getString("ICON1"));
                    insightTaskStatusList.setIcon2(rs.getString("ICON2"));
                    insightTaskStatusList.setId(rs.getLong("TASK_STATUS_ID"));
                    list.add(insightTaskStatusList);
                }
                return list;
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void fill(ResultSet rs, List<ValueLabel<Integer, String>> customerTasksList) throws SQLException {
        while (rs.next())
            customerTasksList.add(new ValueLabel<>(rs.getInt("VALUE"), rs.getString("LABEL")));
    }

    @Override
    public InsightCombine getInsightCombine(long user_id) {
        InsightCombine insightCombine = new InsightCombine();
        insightCombine.setCustomerTasks(getCustomerTasksList(user_id));
        insightCombine.setInsightTaskStatusLists(getTaskStatusList(user_id));
        return insightCombine;
    }
}
