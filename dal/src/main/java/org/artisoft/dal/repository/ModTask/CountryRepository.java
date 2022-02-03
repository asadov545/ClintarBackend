package org.artisoft.dal.repository.ModTask;

import com.zaxxer.hikari.HikariDataSource;
import org.artisoft.dal.dao.CountryDao;
import org.artisoft.domain.ValueLabel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("countryDao")
public class CountryRepository implements CountryDao {

    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcCall insUpdProc;
    private SimpleJdbcCall delUpdProc;

    @Autowired
    public CountryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Autowired
    public void setDataSource(HikariDataSource dataSource) {
        this.insUpdProc = new SimpleJdbcCall(dataSource);
        this.delUpdProc = new SimpleJdbcCall(dataSource);
    }
    @Override
    public List<ValueLabel<Long, String>> filterCountryList(String label) {
        try {
            final String sql = "SELECT COUNTRY_ID, TITLE FROM COUNTRY where TITLE like ?";
            return jdbcTemplate.query(sql, new Object[]{"%".concat(label).concat("%")}, rs -> {
                List<ValueLabel<Long, String>> list = new ArrayList<>();
                while (rs.next())
                    list.add(new ValueLabel<>(rs.getLong("COUNTRY_ID"), rs.getString("TITLE")));

                return list;
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
