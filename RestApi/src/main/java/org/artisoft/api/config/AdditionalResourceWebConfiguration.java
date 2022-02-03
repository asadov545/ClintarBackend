package org.artisoft.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AdditionalResourceWebConfiguration implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("**/swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

      registry.addResourceHandler("/**").addResourceLocations("classpath:/");

        //registry.addResourceHandler("/**").addResourceLocations("/", "classpath:/", "classpath:/static/", "classpath*:/","classpath*:");
    }
}
