package org.artisoft.dal.repository.utils;

import org.artisoft.domain.ValueLabel;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class CommonDataHelper {


    public static List<ValueLabel<Long, String>> getValueLabels(final String sql, final JdbcTemplate jdbcTemplate) {
        return getValueLabels(sql, jdbcTemplate, null);
    }
    public static List<ValueLabel<Long, String>> getValueLabels(final String sql, final JdbcTemplate jdbcTemplate, Object[] obj){
        return jdbcTemplate.query(sql, obj, rs -> {
            List<ValueLabel<Long, String>> idTitles = new ArrayList<>();
            while (rs.next())
                idTitles.add(new ValueLabel<>(rs.getLong("value"), rs.getString("label")));
            return idTitles;
        });
    }
}
