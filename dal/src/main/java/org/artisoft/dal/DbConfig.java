package org.artisoft.dal;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan
@EnableTransactionManagement
@PropertySource(value = {"classpath:application.properties"})

public class DbConfig {
    @Autowired
    private Environment env;

    @Value("${init-db:false}")
    private String initDatabase;

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public JdbcTemplate jdbcTemplate(HikariDataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public PlatformTransactionManager transactionManager(HikariDataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public HikariDataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
        dataSource.setJdbcUrl(env.getProperty("spring.datasource.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.password"));
        //dataSource.addDataSourceProperty("sslMode", "DISABLED");
        //dataSource.addDataSourceProperty("allowPublicKeyRetrieval", "false");

        String _connectionTimeout = env.getProperty("spring.datasource.hikari.connectionTimeout");
        if (_connectionTimeout != null)
            dataSource.setConnectionTimeout(Long.parseLong(_connectionTimeout));

        String _idleTimeout = env.getProperty("spring.datasource.hikari.idleTimeout");
        dataSource.setIdleTimeout(Long.parseLong(_idleTimeout));

        String _maxLifetime = env.getProperty("spring.datasource.hikari.maxLifetime");
        if (_maxLifetime != null)
            dataSource.setMaxLifetime(Long.parseLong(_maxLifetime));
        return dataSource;
    }

}
