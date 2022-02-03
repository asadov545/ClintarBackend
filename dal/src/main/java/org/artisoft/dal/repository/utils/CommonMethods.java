package org.artisoft.dal.repository.utils;

import com.zaxxer.hikari.HikariDataSource;
import org.artisoft.domain.ValueLabel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Component
public class CommonMethods {

    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcCall insUpdProc;
    private SimpleJdbcCall delUpdProc;

    @Autowired
    public CommonMethods(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Autowired
    public void setDataSource(HikariDataSource dataSource) {
        this.insUpdProc = new SimpleJdbcCall(dataSource);
        this.delUpdProc = new SimpleJdbcCall(dataSource);
    }

    public List<ValueLabel<Long, String>> getValueLabels(final String sql, Object[] obj) {
        return getValueLabels(sql, this.jdbcTemplate, obj);
    }

    public List<ValueLabel<Long, String>> getValueLabels(final String sql) {
        return getValueLabels(sql, this.jdbcTemplate, null);
    }

    public static List<ValueLabel<Long, String>> getValueLabels(final String sql, final JdbcTemplate jdbcTemplate) {
        return getValueLabels(sql, jdbcTemplate, null);
    }


    public static List<ValueLabel<Long, String>> getValueLabels(final String sql, final JdbcTemplate jdbcTemplate, Object[] obj) {
        return jdbcTemplate.query(sql, obj, rs -> {
            List<ValueLabel<Long, String>> idTitles = new ArrayList<>();
            while (rs.next())
                idTitles.add(new ValueLabel<>(rs.getLong("value"), rs.getString("label")));
            return idTitles;
        });
    }
    public static Session GetSession() {

        Properties props = new Properties();
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.smtp.host", "smtp.gmail.com");
//        props.put("mail.smtp.port", "587");


        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.sparkpostmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("asadov.tech@gmail.com", "Altay51406-");
            }
        });
        return  session;
    }








}
