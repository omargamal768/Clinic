package com.example.clinic.admin.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Map "/static/**" to the resources in the static folder of the classpath
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");

        // Optionally, you can add other resource mappings like for external folders
        // registry.addResourceHandler("/external-static/**")
        //         .addResourceLocations("file:/path/to/external/static/");
    }
}