package org.artisoft.dal.repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zaxxer.hikari.HikariDataSource;
import org.artisoft.dal.dao.FileDao;
import org.artisoft.domain.StoreProcedureResponse;
import org.artisoft.domain.UploadedFiles;
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

@Repository("fileDao")
public class FileRepository implements FileDao {
    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcCall insUpdProc;
    private SimpleJdbcCall delUpdProc;


    @Autowired
    public FileRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    public void setDataSource(HikariDataSource dataSource) {
        this.insUpdProc = new SimpleJdbcCall(dataSource);
        this.delUpdProc = new SimpleJdbcCall(dataSource);

    }


    @Override
    public UploadedFiles getById(long id) {
        return null;
    }

    @Override
    public long insert(UploadedFiles uploadedFiles) {
        try {
            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("jsonObj", new Gson().toJson(uploadedFiles))
                    .addValue("newupfid", 0);

            SimpleJdbcCall jdbcCall = insUpdProc.withProcedureName("UploadedFilesInsUpd");
            jdbcCall.declareParameters(
                    new SqlParameter("jsonObj", Types.VARCHAR),
                    new SqlOutParameter("newupfid", Types.INTEGER));

            Map<String, Object> resultMap = jdbcCall.execute(parameters);
            uploadedFiles.setUpfid(Long.parseLong(resultMap.get("newupfid").toString()));
            return 1;

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public boolean update(UploadedFiles uploadedFiles) {
        return insert(uploadedFiles) > 0;
    }

    @Override
    public boolean delete(long id) {
        return false;
    }

    @Override
    public List<Long> insertAll(List<UploadedFiles> uploadedFiles) {
        try {
            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("jsonObjs", new Gson().toJson(uploadedFiles))
                    .addValue("presjson", 0);

            SimpleJdbcCall jdbcCall = insUpdProc.withProcedureName("UploadedFilesAllInsUpd");
            jdbcCall.declareParameters(
                    new SqlParameter("jsonObjs", Types.VARCHAR),
                    new SqlOutParameter("presjson", Types.VARCHAR));

            Map<String, Object> resultMap = jdbcCall.execute(parameters);
           // uploadedFiles.setUpfid(Long.parseLong(resultMap.get("newupfid").toString()));
            // List<Long> listid=new ArrayList<>();
          //  List<Long>  =

            Type listType = new TypeToken<ArrayList<Long>>() {}.getType();
           return   new Gson().fromJson(resultMap.get("presjson").toString(),listType);

          //  return 1;

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
