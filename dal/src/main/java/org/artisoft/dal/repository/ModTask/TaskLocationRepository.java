package org.artisoft.dal.repository.ModTask;

import com.google.gson.Gson;
import com.zaxxer.hikari.HikariDataSource;

import org.apache.commons.io.FileUtils;
import org.artisoft.dal.dao.ModTask.TaskLocationDao;
import org.artisoft.domain.ModTask.tasks.TaskLocation;
import org.artisoft.domain.ModTask.tasks.TaskLocationDoc;
import org.artisoft.domain.ModTask.tasks.TaskMarkup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

@Repository("taskLocationDao")
public class TaskLocationRepository implements TaskLocationDao {

    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcCall insUpdProc;
    private SimpleJdbcCall delUpdProc;


    @Autowired
    public TaskLocationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    public void setDataSource(HikariDataSource dataSource) {
        this.insUpdProc = new SimpleJdbcCall(dataSource);
        this.delUpdProc = new SimpleJdbcCall(dataSource);

    }

    @Override
    public TaskLocation getById(long id) {
        try {
            final String sql = " SELECT TL.LOC_ID, IFNULL(TLF.LOC_FILE_ID, 0) as LOC_FILE_ID,TL.TITLE,TL.PID , " +
                    " IFNULL(  UF.ORIGINALFILENAME, '') as  ORIGINALFILENAME  ,  IFNULL(UF.RELATIVE_PATH, '') as RELATIVE_PATH , " +
                    " IFNULL(UF.FILENAME,'') as FILENAME , 0 AS OP,UF.UPFID FROM TASK_LOCATIONS as TL  LEFT  JOIN TASK_LOCATION_FILES AS " +
                    "  TLF ON (TL.LOC_ID=TLF.LOC_ID AND TLF.`STATUS`=1) " +
                    " LEFT JOIN UPLOADED_FILES AS UF ON (TLF.UPFID=UF.UPFID )  "
                    + " WHERE TL.STATUS=1 AND TL.LOC_ID=? ";
            return jdbcTemplate.query(sql, new Object[]{id}, rs -> {
                if (!rs.next())
                    return null;
                TaskLocation taskLocation = new TaskLocation();
                taskLocation.setLocId(rs.getLong("LOC_ID"));
                taskLocation.setTitle(rs.getString("TITLE"));
                taskLocation.setPid(rs.getLong("PID"));

                if(rs.getLong("LOC_FILE_ID") > 0) {
                    TaskLocationDoc taskLocationDoc = new TaskLocationDoc();
                    taskLocationDoc.setLocFileId(rs.getLong("LOC_FILE_ID"));
                    taskLocationDoc.setFilenameOriginal(rs.getString("ORIGINALFILENAME"));
                    taskLocationDoc.setUpfid(rs.getLong("UPFID"));
                    Path fullpathFile = new File(rs.getString("RELATIVE_PATH")).toPath().resolve(rs.getString("FILENAME"));

                    String fullpath = null;
                    try {
                        fullpath = GetUrlBase() + fullpathFile.toString().replace("\\", "/");
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    taskLocationDoc.setFullPath(fullpath);
                    taskLocationDoc.setOp(rs.getInt("OP"));


                    try {
                        System.out.println(fullpathFile);
///app/webapps


                        byte[] fileContent = FileUtils.readFileToByteArray(new File("/app/webapps"+fullpathFile.toString()));
                        String encodedString = Base64.getEncoder().encodeToString(fileContent);
                        taskLocationDoc.setImgbase64(encodedString);
                        taskLocation.setTaskLocationDoc(taskLocationDoc);
                    }catch (Exception e) {
                        e.printStackTrace();
                    }



                    taskLocation.setTaskLocationDoc(taskLocationDoc);
                }
                return taskLocation;
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

//    private static String encodeFileToBase64Binary(File file) throws Exception{
//        FileInputStream fileInputStreamReader = new FileInputStream(file);
//        byte[] bytes = new byte[(int)file.length()];
//        fileInputStreamReader.read(bytes);
//        return new String(Base64.encodeBase64(bytes), "UTF-8");
//    }


    @Autowired
    HttpServletRequest servletRequest;

    public String GetUrlBase() throws MalformedURLException {
        URL requestURL = new URL(servletRequest.getRequestURL().toString());
        String port = requestURL.getPort() == -1 ? "" : ":" + requestURL.getPort();
        return requestURL.getProtocol() + "://" + requestURL.getHost() + port;
    }


    @Override
    public long insert(TaskLocation taskLocation)
    {
        try {

            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("jsonObj", new Gson().toJson(taskLocation))
                    .addValue("newlocId", 0);

            SimpleJdbcCall jdbcCall = insUpdProc.withProcedureName("TaskLocationInsUpd");
            jdbcCall.declareParameters(
                    new SqlParameter("jsonObj", Types.VARCHAR),
                    new SqlOutParameter("newlocId", Types.INTEGER));

            Map<String, Object> resultMap = jdbcCall.execute(parameters);
            taskLocation.setLocId(Long.parseLong(resultMap.get("newlocId").toString()));
              return 1;

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public boolean update(TaskLocation taskLocation) {
        return insert(taskLocation) > 0;
    }

    @Override
    public boolean delete(long id) {
        try {
            SqlParameterSource parameters = new MapSqlParameterSource().addValue("locId", id);
            SimpleJdbcCall jdbcCall = delUpdProc.withProcedureName("TaskLocationDel");
            jdbcCall.declareParameters(new SqlParameter("locId", Types.INTEGER));
            jdbcCall.execute(parameters);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public List<TaskLocation> filterData(HashMap<String, String> map) {

        try {
            final String sql = " SELECT TL.LOC_ID,TL.TITLE,TL.PID FROM TASK_LOCATIONS as TL WHERE TL.STATUS=1  ";

            StringBuilder cond = new StringBuilder(sql);

            ResultSetExtractor<List<TaskLocation>> rm = rs -> {
                List<TaskLocation> taskLocationList = new ArrayList<>();
                createTaskLocation(rs, taskLocationList);
                return taskLocationList;
            };
            cond.append(" order by TL.LOC_ID desc ");
            return jdbcTemplate.query(cond.toString(), rm);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private void createTaskLocation(ResultSet rs, List<TaskLocation> taskLocationList) throws SQLException {
        while (rs.next()) {
            TaskLocation taskLocation = new TaskLocation();
            taskLocation.setLocId(rs.getLong("LOC_ID"));
            taskLocation.setTitle(rs.getString("TITLE"));
            taskLocation.setPid(rs.getLong("PID"));
            taskLocationList.add(taskLocation);
        }
    }



}
