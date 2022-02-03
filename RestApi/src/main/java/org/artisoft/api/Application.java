package org.artisoft.api;

import org.artisoft.api.property.FileStorageProperties;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;


@Configuration
@SpringBootApplication
@ComponentScan("org.artisoft")
@EnableConfigurationProperties(FileStorageProperties.class)
public class Application extends SpringBootServletInitializer {


    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);

        Properties properties = new Properties();
        properties.setProperty("spring.resources.static-locations",
                "classpath:/tp/**, classpath:/**");
        app.setDefaultProperties(properties);

        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }
}
