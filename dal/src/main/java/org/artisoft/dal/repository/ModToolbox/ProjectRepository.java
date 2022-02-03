package org.artisoft.dal.repository.ModToolbox;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.artisoft.dal.repository.ModTask.TaskRepository;
import org.artisoft.domain.Address;
import org.artisoft.domain.ModTask.Branches;
import org.artisoft.domain.ModToolbox.CreateProject;
import org.artisoft.domain.ModToolbox.ProjectTemplate;
import org.artisoft.domain.ModToolbox.TemplateCategory;
import org.artisoft.domain.ModToolbox.project.MainProjectList;
import org.artisoft.domain.ModToolbox.template.CheckList;
import org.artisoft.domain.ModToolbox.template.CheckListOption;
import org.artisoft.domain.ModToolbox.template.CompletionQuestion;
import org.artisoft.domain.ModToolbox.template.Task;
import org.artisoft.domain.ValueLabel;
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

@Repository("projectRepo")
public class ProjectRepository {
    private static final Logger logger = LogManager.getLogger(TaskRepository.class);

    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcCall insUpdProc;
    private SimpleJdbcCall delProc;

    @Autowired
    public ProjectRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    public void setDataSource(HikariDataSource dataSource) {

        this.insUpdProc = new SimpleJdbcCall(dataSource);
        this.delProc = new SimpleJdbcCall(dataSource);
    }

    public Object createProject(CreateProject createProjectInfo) {
        try {
            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("pRequestJson", new Gson().toJson(createProjectInfo))
                    .addValue("pResponseJson", null);

            SimpleJdbcCall jdbcCall = insUpdProc.withProcedureName("CreateProject");
            jdbcCall.declareParameters(
                    new SqlParameter("pRequestJson", Types.VARCHAR),
                    new SqlOutParameter("pResponseJson", Types.VARCHAR));

            Map<String, Object> resultMap = jdbcCall.execute(parameters);
            Object resultObject = resultMap.get("pResponseJson");

            return resultObject;

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    public List<TemplateCategory> getTemplateCategories() {
        final String sql = "SELECT PTC.PT_CAT_ID, PTC.TITLE FROM TB_PRJ_TMPL_CATEGORIES PTC";

        return jdbcTemplate.query(sql, rs -> {
            List<TemplateCategory> resultList = new ArrayList<>();

            while (rs.next()) {
                TemplateCategory cat = new TemplateCategory();
                cat.setCatId(rs.getLong("PT_CAT_ID"));
                cat.setTitle(rs.getString("TITLE"));
                resultList.add(cat);
            }

            return resultList;
        });
    }


    public List<ValueLabel<Long,String>> getTemplateList() {
        final String sqlforms = " SELECT PT.PT_ID as value, PT.TITLE as label  FROM  TB_PRJ_TEMPLATES AS PT WHERE PT.STATUS=1  ";


        return jdbcTemplate.query(sqlforms, rs -> {
            List<ValueLabel<Long,String>> resultList = new ArrayList<>();

            while (rs.next()) {
                ValueLabel<Long,String> temp = new ValueLabel<Long,String>();
                temp.setValue(rs.getLong("value"));
                temp.setLabel(rs.getString("label"));
                resultList.add(temp);
            }

            return resultList;
        });
    }



    public List<ProjectTemplate> getCategoryTemplates(long catId) {
        final String sql = "SELECT PT.PT_ID, PT.PT_CAT_ID, PT.TITLE,PT.DESCRIPTION FROM TB_PRJ_TEMPLATES PT WHERE PT.STATUS=1 AND PT.PT_CAT_ID=?";

        return jdbcTemplate.query(sql, new Object[]{catId}, rs -> {
            List<ProjectTemplate> resultList = new ArrayList<>();

            while (rs.next()) {
                ProjectTemplate cat = new ProjectTemplate();
                cat.setTemplateId(rs.getLong("PT_ID"));
                cat.setCatId(rs.getLong("PT_CAT_ID"));
                cat.setTitle(rs.getString("TITLE"));
                cat.setDescription(rs.getString("DESCRIPTION"));
                resultList.add(cat);
            }

            return resultList;
        });
    }

    public List<CompletionQuestion> getTemplateQuestions(long templateId) {
        final String sql = "SELECT Q.PTCQ_ID, Q.PT_ID, Q.QUEST_TITLE, Q.QUEST_TYPE FROM TB_PRJ_TMPL_CMPL_QUESTS Q WHERE Q.PT_ID=?";

        return jdbcTemplate.query(sql, new Object[]{templateId}, rs -> {
            List<CompletionQuestion> resultList = new ArrayList<>();

            while (rs.next()) {
                CompletionQuestion cat = new CompletionQuestion();
                cat.setQuestionId(rs.getLong("PTCQ_ID"));
                cat.setTemplateId(rs.getLong("PT_ID"));
                cat.setQuestionType(rs.getInt("QUEST_TYPE"));
                cat.setTitle(rs.getString("QUEST_TITLE"));
                resultList.add(cat);
            }

            return resultList;
        });
    }

    public List<CheckList> getTemplateCategories(long templateId) {
        final String sql = "SELECT C.LCAT_ID, C.TITLE, C.PID, C.LT_CODE, C.PT_ID, C.WEIGHT FROM TB_CHECKLIST_CATEGORIES C WHERE C.PT_ID=?";

        return jdbcTemplate.query(sql, new Object[]{templateId}, rs -> {
            List<CheckList> resultList = new ArrayList<>();

            while (rs.next()) {
                CheckList cat = new CheckList();
                cat.setCheckListId(rs.getLong("LCAT_ID"));
                cat.setTemplateId(rs.getLong("PT_ID"));
                cat.setPid(rs.getLong("PID"));
                cat.setListTypeCode(rs.getString("LT_CODE"));
                cat.setTitle(rs.getString("TITLE"));
                cat.setWeight(rs.getInt("WEIGHT"));
                resultList.add(cat);
            }

            return resultList;
        });
    }

    public String getNextPojectNo() {
        final String sql = "SELECT GetNextProjectNo() AS PROJECT_NO";

        return jdbcTemplate.query(sql, rs -> {
            String projectNo = "";

            if (rs.next()) {
                projectNo = rs.getString("PROJECT_NO");
            }

            return projectNo;
        });
    }

    public List<CheckListOption> getTemplateCheckListOptions(long templateId) {
        final String sql = "SELECT O.LOPT_ID, O.LCAT_ID, O.TITLE, O.WEIGHT, O.DEFAULT_VAL FROM TB_CHECKLIST_OPTIONS O, TB_CHECKLIST_CATEGORIES C WHERE C.PT_ID=? AND C.LCAT_ID=O.LCAT_ID";

        return jdbcTemplate.query(sql, new Object[]{templateId}, rs -> {
            List<CheckListOption> resultList = new ArrayList<>();

            while (rs.next()) {
                CheckListOption cat = new CheckListOption();

                cat.setOptionId(rs.getLong("LOPT_ID"));
                cat.setCheckListId(rs.getLong("LCAT_ID"));
                cat.setTitle(rs.getString("TITLE"));
                cat.setWeight(rs.getInt("WEIGHT"));
                resultList.add(cat);
            }

            return resultList;
        });
    }

    public String getByTempDescription(long templateId) {
        try {
            final String sql = "SELECT TPT.DESCRIPTION  FROM CLINTARDB.TB_PRJ_TEMPLATES TPT WHERE TPT.PT_ID=?";
            return jdbcTemplate.query(sql, new Object[]{templateId}, rs -> {
                if (!rs.next())
                    return null;

                return rs.getString("DESCRIPTION");
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    public long getCategoryByPrjId(long PrjId) {
        try {
            final String sql = "SELECT TPT.PT_CAT_ID FROM TB_PRJ T LEFT JOIN TB_PRJ_TEMPLATES TPT ON (T.PT_ID=TPT.PT_ID ) WHERE  T.PRJ_ID=?";
            return jdbcTemplate.query(sql, new Object[]{PrjId}, rs -> {
                if (!rs.next())
                    return null;

                return rs.getLong("PT_CAT_ID");
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    public List<Task> getTemplateTasks(long templateId) {
        final String sql = "SELECT T.PRJ_TT_ID, T.PT_ID, T.TITLE, T.HAZARD_TEXT, T.CONTROL_TEXT, T.RISK_BEFORE, T.RISK_AFTER, " +
                "(SELECT JSON_ARRAYAGG(CAST(CL.CHECKLIST AS JSON)) FROM TB_PRJ_TMPL_TASK_CHECKLIST CL " +
                "WHERE CL.PRJ_TT_ID=T.PRJ_TT_ID) CHECKLIST FROM TB_PRJ_TMPL_TASKS T WHERE T.PT_ID=?";

        return jdbcTemplate.query(sql, new Object[]{templateId}, rs -> {
            List<Task> resultList = new ArrayList<>();

            while (rs.next()) {
                Task task = new Task();

                task.setTaskId(rs.getLong("PRJ_TT_ID"));
                task.setTaskTitle(rs.getString("TITLE"));
                task.setHazardTitle(rs.getString("HAZARD_TEXT"));
                task.setControlTitle(rs.getString("CONTROL_TEXT"));
                task.setRiskBefore(rs.getString("RISK_BEFORE"));
                task.setRiskAfter(rs.getString("RISK_AFTER"));
                Type listType = new TypeToken<List<List<Long>>>(){}.getType();
                task.setCheckList(new Gson().fromJson(rs.getString("CHECKLIST"), listType));
                resultList.add(task);
            }

            return resultList;
        });
    }

    public Object deleteProject(long projectId, long logUserId) {
        try {
            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("pProjectId", projectId)
                    .addValue("pLogUserId", logUserId)
                    .addValue("pResponseJson", null);

            SimpleJdbcCall jdbcCall = delProc.withProcedureName("DeleteProject");
            jdbcCall.declareParameters(
                    new SqlParameter("pProjectId", Types.VARCHAR),
                    new SqlParameter("pLogUserId", Types.VARCHAR),
                    new SqlOutParameter("pResponseJson", Types.VARCHAR));

            Map<String, Object> resultMap = jdbcCall.execute(parameters);
            Object resultObject = resultMap.get("pResponseJson");

            return resultObject;

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public List<MainProjectList> getMainProjectList(long userId) {
               final String sql = " select MP.MAIN_PRJ_ID,MPU.USER_ID,MP.NAME,MP.TASK_LOCATION,MP.FIRST_AID_CONTACT from" +
                " TB_MAIN_PRJ_USERS AS MPU RIGHT JOIN TB_MAIN_PRJ AS MP ON (MPU.MAIN_PRJ_ID=MP.MAIN_PRJ_ID)" +
                " WHERE  MP.STATUS=1 AND  MPU.`STATUS`=1 AND MPU.USER_ID=?";
        return jdbcTemplate.query(sql, new Object[]{userId}, rs -> {
            List<MainProjectList> resultList = new ArrayList<>();
            while (rs.next()) {
                MainProjectList mainProjectList= new MainProjectList();
                mainProjectList.setMainPrjId(rs.getLong("MAIN_PRJ_ID"));
                mainProjectList.setFirstAidContact(rs.getString("FIRST_AID_CONTACT"));
                mainProjectList.setName(rs.getString("NAME"));
                mainProjectList.setTaskLocation(rs.getString("TASK_LOCATION"));
                  resultList.add(mainProjectList);
            }
            return resultList;
        });
    }



}
