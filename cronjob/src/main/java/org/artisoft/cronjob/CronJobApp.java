package org.artisoft.cronjob;

import org.apache.log4j.BasicConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@SpringBootApplication
@EnableScheduling
@ComponentScan("org.artisoft")

public class CronJobApp {
    public static void main(String[] args) {
        SpringApplication.run(CronJobApp.class);
      ///  BasicConfigurator.configure();
    }
}
